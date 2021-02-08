package durakonline.sk.durakonline;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.net.Socket;
import java.net.UnknownHostException;
import java.util.ArrayList;
import java.util.List;

import durakonline.sk.durakonline.other.Log;
import durakonline.sk.durakonline.other.SessionInfo;

/**
 * Created by sk on 18.03.16.
 */
public class QueueNetwork
{
    private final static byte LIMIT_COUNT_SENDED  = 3;
    private final static long INTERVAL_SEND       = 5000; // in millisecond

    public final static int TYPE_SEND__FORCE = 0x10;
    public final static int TYPE_SEND__LONG  = 0x12;

    private SessionInfo session_info;

    // буффер входяших комманд
    // регистрирует номера обработанных пакетов с сервера
    // регистрация нужна чтобы исключить повторную обработку
    private List<Long> input_requests_packets = new ArrayList<>();

    // буффер на отправку команд
    private class OutputData
    {
        long timestamp; /// 8 байт
        int type_command; /// меняет тип отправки команды
        byte count_sended = 0;
        long time_sendned_last = 0;

        String cmd;
        ArrayList<String> params;
        byte[] add_buffer;
        int priority;

        boolean wait_send = false;
    }

    private List<OutputData> output_requests_pakets = new ArrayList<OutputData>();

    static class theLock extends Object { }
    static public theLock lockObject = new theLock();
    static public theLock lockInput  = new theLock();
    static public theLock thread_output_block  = new theLock();

    private ClassNetWork network = null;

    private ThreadOutput thread_output = new ThreadOutput();

    //private boolean clear_all = false;

    public void setSessionInfo(SessionInfo v)
    {
        session_info = v;
    }

    public QueueNetwork(ClassNetWork _network, SessionInfo _session_info)
    {
        network = _network;
        thread_output.start();
        session_info = _session_info;
    }

    public void OnDestroy()
    {
        if( thread_output != null )
        {
            thread_output.running = false;
            thread_output = null;
        }
    }

    public void okSend(long timestamp)
    {
        synchronized (lockObject)
        {
            for(int i = 0; i < output_requests_pakets.size(); i++)
            {
                if( output_requests_pakets.get(i).timestamp == timestamp )
                {
                    output_requests_pakets.remove(i);
                    break;
                }
            }
        }
    }

    private boolean found_timestamp(long timestamp)
    {
        synchronized (lockObject)
        {
            for(int i = 0; i < output_requests_pakets.size(); i++)
            {
                if( output_requests_pakets.get(i).timestamp == timestamp )
                {
                    return true;
                }
            }
        }

        return false;
    }

    private boolean found_timestamp2(long timestamp)
    {
        //synchronized (lockObject)
        {
            for(int i = 0; i < output_requests_pakets.size(); i++)
            {
                if( output_requests_pakets.get(i).timestamp == timestamp )
                {
                    return true;
                }
            }
        }

        return false;
    }

    public void clearAllQueue()
    {
        //synchronized (thread_output_block)
        {
            synchronized (lockObject)
            {
                output_requests_pakets.clear();
            }

            synchronized (lockInput)
            {
                input_requests_packets.clear();
            }
        }
    }



    public void add(String cmd, ArrayList<String> params, byte[] add_buffer, int priority, int type_command)
    {
        OutputData out = new OutputData();
        out.cmd = cmd;
        out.params = params;

        out.timestamp = System.currentTimeMillis();

        /*while( found_timestamp(out.timestamp) )
        {
            out.timestamp += 1;
        }*/

        out.add_buffer = add_buffer;

        out.priority = priority;

        out.type_command = type_command;

        synchronized (lockObject)
        {
            /// поиск дублирования метки времени
            while( found_timestamp2(out.timestamp) )
            {
                Log.i("TAG", "found_timestamp2");
                out.timestamp += 100;
            }

            output_requests_pakets.add( out );

            Log.i("TAG", "ADD OK: " + out.cmd + " " + String.valueOf(out.timestamp));
        }
    }

    public boolean foundInput(long timestamp)
    {
        synchronized (lockInput)
        {
            for(int i = 0; i < input_requests_packets.size(); i++)
            {
                if( input_requests_packets.get(i) == timestamp )
                {
                    return true;
                }
            }
        }

        return false;
    }

    public void addInput(long timestamp)
    {
        synchronized (lockInput)
        {
            input_requests_packets.add(timestamp);

            if( input_requests_packets.size() > 250 )
            {
                input_requests_packets.remove(0);
            }
        }
    }

    /*public void stopWait(long timestamp)
    {
        synchronized (lockObject)
        {
            for(int i = 0; i < output_requests_pakets.size(); i++)
            {
                if (output_requests_pakets.get(i).timestamp == timestamp)
                {
                    output_requests_pakets.get(i).wait_send = true;
                    output_requests_pakets.get(i).time_sendned_last = System.currentTimeMillis();
                }
            }
        }
    }*/

    public void unStopWait(long timestamp)
    {
        synchronized (lockObject)
        {
            for(int i = 0; i < output_requests_pakets.size(); i++)
            {
                if (output_requests_pakets.get(i).timestamp == timestamp)
                {
                    output_requests_pakets.get(i).wait_send = false;
                    output_requests_pakets.get(i).time_sendned_last = 0;
                }
            }
        }
    }

    /*public void deleteQueueItem(long timestamp)
    {
        synchronized (lockObject)
        {
            for(int i = 0; i < output_requests_pakets.size(); i++)
            {
                if (output_requests_pakets.get(i).timestamp == timestamp)
                {
                    output_requests_pakets.remove(i);
                }
            }
        }
    }*/

    //-------------------------------------------------------------------------------------------------------

    private void thread_send_long_command(final byte[] send_command, final Object send_to_exec_function, final long timestamp)
    {
        Thread th = new Thread(new Runnable()
        {
            private boolean ok_stop = false;

            @Override
            public void run()
            {
                Thread.currentThread().setPriority(Thread.MAX_PRIORITY - 1);

                Socket sock = null;
                boolean ok_connect = false;

                String[] host_list = JniApi.f1().split(";");

                for(int j = 0; j < host_list.length; j++)
                {
                    for(int i = 0; i < 7; i++)
                    {
                        try
                        {
                            Log.i("TAG", ".. connection server: " + String.valueOf(i) + " " + host_list[j].trim() + " " + String.valueOf(
                                    JniApi.port2() ));
                            sock = new Socket( host_list[j].trim(), JniApi.port2() );
                            sock.setTcpNoDelay(true);
                            sock.setKeepAlive(true);
                            sock.setSoTimeout(60000);
                            //sock.setSendBufferSize(send_command.length);

                            ok_connect = true;
                            break;
                        }

                        catch (UnknownHostException e)
                        { e.printStackTrace(); }
                        catch (IOException e)
                        { e.printStackTrace(); }

                        try
                        { Thread.sleep(500); }
                        catch (InterruptedException e)
                        { e.getLocalizedMessage(); }
                    }

                    if(ok_connect) { break; }
                }

                //-------------------------------------------

                if( ! ok_connect)
                {
                    /*runOnUiThread(new Runnable()
                    {
                        @Override
                        public void run()
                        {
                            AlertDialog.Builder alertDialog = new AlertDialog.Builder( MainActivity.this );

                            alertDialog.setMessage( "Error network" );
                            alertDialog.setPositiveButton("OK", new DialogInterface.OnClickListener()
                            {
                                @Override
                                public void onClick(DialogInterface dialog, int which) { }
                            });

                            alertDialog.setCancelable(false);
                            alertDialog.show();
                        }
                    });*/

                    unStopWait(timestamp);
                    return;
                }

                //-------------------------------------------------------

                DataOutputStream output = null;

                DataInputStream input = null;

                try
                {
                    input = new DataInputStream(sock.getInputStream());
                }
                catch (IOException e)
                {
                    input = null;
                    e.printStackTrace();
                }

                try
                {
                    output = new DataOutputStream(sock.getOutputStream());

                    output.write(send_command);

                    output.flush();
                }
                catch (IOException e)
                {
                    output = null;
                    e.printStackTrace();
                }

                System.gc();

                try
                { Thread.sleep(200); }
                catch (InterruptedException e)
                { e.printStackTrace(); }

                while(true)
                {
                    ReadCommand r = ProtokolUtils.read_command_encrypt( input, session_info );

                    if( r != null )
                    {
                        if(r.close_sock == true)
                        {
                            break;
                        }

                        if(r.init_ok == true)
                        {
                            if( _exec2(input, r, sock, send_to_exec_function) )
                            {
                                ok_stop = true;
                                break;
                            }
                        }
                        else
                        {
                            break;
                                /*Log.i("TAG", "Error read: __thread_send_long_command");

                                runOnUiThread(new Runnable()
                                {
                                    @Override
                                    public void run()
                                    {
                                        AlertDialog.Builder alertDialog = new AlertDialog.Builder( MainActivity.this );

                                        alertDialog.setMessage( "Error network: error read" );
                                        alertDialog.setPositiveButton("OK", new DialogInterface.OnClickListener()
                                        {
                                            @Override
                                            public void onClick(DialogInterface dialog, int which) { }
                                        });

                                        alertDialog.setCancelable(false);
                                        alertDialog.show();
                                    }
                                });*/
                        }
                    }

                }

                try
                { input.close(); }
                catch (IOException e)
                { e.printStackTrace(); }

                try
                {
                    if(sock != null)
                    { sock.close(); }
                }
                catch(IOException e)
                { }

                System.gc();

                // обработка выполнена успешно
                if( ok_stop )
                {
                    //deleteQueueItem(timestamp);
                }
                else
                {
                    unStopWait(timestamp);
                }
            }
        });

        th.start();

    }

    private class ThreadOutput extends Thread
    {
        public boolean running = true;

        @Override
        public void run()
        {
            Thread.currentThread().setPriority(Thread.MAX_PRIORITY - 1);

            /*try
            {
                Thread.sleep(400);
            }
            catch (InterruptedException e)
            {
                e.printStackTrace();
            }*/

            while( running )
            {
                    try
                    {
                        Thread.sleep(150);
                    }
                    catch (InterruptedException e)
                    {
                        e.printStackTrace();
                    }

                    synchronized (thread_output_block)
                    {
                        synchronized (lockObject)
                        {
                            if (output_requests_pakets.size() > 0)
                            {
                                for (int f = 0; f < output_requests_pakets.size(); f++)
                                {
                                    if (network == null)
                                    {
                                        break;
                                    }

                                    OutputData t = output_requests_pakets.get(f);

                                    if (       ! t.wait_send
                                            && System.currentTimeMillis() - t.time_sendned_last > INTERVAL_SEND
                                            )
                                    {
                                        t.time_sendned_last = System.currentTimeMillis();

                                        if (t.type_command == TYPE_SEND__FORCE)
                                        {
                                            if (network != null)
                                            {
                                                Log.i("TAG", "network.addOutBuffer " + t.cmd);

                                                network.addOutBuffer(t.cmd, t.params, t.add_buffer, t.priority, t.timestamp);
                                            }
                                        }
                                        else
                                        {
                                            byte[] b_cmd = ProtokolUtils.get_buffer_command_encrypt(
                                                      t.cmd
                                                    , t.params
                                                    , t.add_buffer
                                                    , session_info
                                                    , t.timestamp);

                                            Log.i(">>>", t.cmd + " " + String.valueOf(t.timestamp));

                                            t.wait_send = true;
                                            thread_send_long_command(b_cmd, null, t.timestamp);
                                        }

                                        t.count_sended += 1;

                                        if (t.count_sended >= LIMIT_COUNT_SENDED)
                                        {
                                            //synchronized (lockObject)
                                            {
                                                Log.i("TAG", "output_requests_pakets.remove " + t.cmd + " " + String.valueOf(t.count_sended));
                                                output_requests_pakets.remove(t);
                                            }

                                            break;
                                        }

                                        break;
                                    }
                                }
                            }
                        }
                    }
            }
        }
    }

    public boolean _exec2(final DataInputStream in_soket, final ReadCommand r, Socket sock, final Object param)
    {
        //Log.i("TAG", "cmd: " + r.cmd);
        return true;
    }
}
