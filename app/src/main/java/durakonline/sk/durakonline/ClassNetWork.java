package durakonline.sk.durakonline;

import android.content.Context;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.Socket;
import java.net.UnknownHostException;
import java.util.ArrayList;
import java.util.List;

import durakonline.sk.durakonline.other.Log;
import durakonline.sk.durakonline.other.SessionInfo;

//------------------------------------------------------------------------------
public class ClassNetWork 
{
	public static Integer GAME_NET_WORK__OUTPUT_PRIORITY_HIGH_2  =  9;
	public static Integer GAME_NET_WORK__OUTPUT_PRIORITY_HIGH    =  10;
	public static Integer GAME_NET_WORK__OUTPUT_PRIORITY_MEDIUM  =  50;
	public static Integer GAME_NET_WORK__OUTPUT_PRIORITY_LOW     =  100;
	// CONST
	private int    TCP_PORT = 0;
	private String TCP_HOST = ""; 
	
	private long   _time_start_connect = 0;
	
	public static String XOR_PASS   = "#RT45@Z_(hR";
	public static String XOR_PASS_2 = "h56_(+R#@ZT";
	// -----------------------------------------------------------------------------
	// VARS
	private Thread thread_connection_server = null;
	private RunThread_IN_Buffer thread_in_buffer = null;
	private RunThread_OUT_Buffer thread_out_buffer = null;
	private Socket client_socket = null;
	private boolean on_destroy = false;
	
	static class theLock extends Object { }
	static public theLock lockObject = new theLock();
	
	private byte[]     in_buffer        = null;
	private List<_OutBuffer> out_buffer = new ArrayList<_OutBuffer>();
	
	private boolean _use_callback_on_read = true;
	public boolean _callback_on_read_execute = false;

	public QueueNetwork queue_network  = null;

	private int index_run_thread_in_out = 0;

	private SessionInfo session_info;
	// ----------------------------------------------------------------------------
	
	private boolean work_net = true;

	//private DB db = null;

	//------------------------------------------------------------------------------
	
	/*int htonl(int value) 
	{
		  if (ByteOrder.nativeOrder().equals(ByteOrder.BIG_ENDIAN) )
		  {
		     return value;
		  }
		  return Integer.reverseBytes(value);
	}*/
	
	private class _OutBuffer
	{
		public String cmd = "";
		public ArrayList<String> params = null;
		public int priority;
		public long timeadd;
		public byte[] buffer;
		
		public _OutBuffer(String _cmd, ArrayList<String> params, byte[] add_buffer, int _priority, long timeAdd)
		{
			this.cmd = _cmd;
			this.priority = _priority;
			this.timeadd = timeAdd;
			this.params  = params;
			this.buffer  = add_buffer;
		}
	}
	
	//---------------------------------------------------------------------------------------
	
	/*int fromByteArray(byte[] bytes) 
	{
	     return ByteBuffer.wrap(bytes).getInt();
	}*/
	
	//---------------------------------------------------------------------------------------
	
	// CONNECTION SERVER -----------------------------------------------------------
	//------------------------------------------------------------------------------
	private void RunThreadConnectionServer()
	{
		if( thread_connection_server != null )
		{ return; }
		
		thread_connection_server = new Thread(new Runnable()
		{
			private boolean is_first_run = true;
			private int count_open_connection = 0;
			//private boolean __callback_timeout_connect_to_server = false;
			
			@Override
			public void run()
			{
				Thread.currentThread().setPriority(Thread.MAX_PRIORITY - 1);

				try
				{ Thread.sleep(300); }
				catch (InterruptedException e)
				{ e.getLocalizedMessage(); }

				while(true)
				{
					if( ! work_net )
					{
						thread_connection_server = null;
						Log.i("TAG", "RunThreadConnectionServer stop");
						return;
					}
					
					/*if(System.currentTimeMillis() - _time_start_connect > 20000 && !__callback_timeout_connect_to_server )
					{
						callback_timeout_connect_to_server();
						__callback_timeout_connect_to_server = true;
					}*/
					
					if(! this.is_first_run)
					{
						/*try
						{ Thread.sleep(200); }
						catch (InterruptedException e) 
						{ e.getLocalizedMessage(); }*/
						
						this.is_first_run = false;
					}
					
					try 
					{ Thread.sleep(300); }
					catch (InterruptedException e) 
					{ e.getLocalizedMessage(); }
					
					while(client_socket == null || ! _is_connected())
					{
						Log.i("TAG", "!connection run");
						//this.run_connect();
						
						if( ! work_net )
						{ Log.i("TAG", "!exit"); break; }
						
						String[] host_list = TCP_HOST.split(";");
					    
					    for(int j = 0; j < host_list.length; j++)
					    {
							try
							{
								Log.i("TAG10", ".. connection server: " + host_list[j].trim() + " " + String.valueOf(TCP_PORT));
								client_socket = new Socket(host_list[j].trim(), TCP_PORT);
								client_socket.setTcpNoDelay(true);
								client_socket.setKeepAlive(true);
								client_socket.setSoTimeout(3000);
								//client_socket.setReceiveBufferSize(2*1024*1024);
							} 
							catch (UnknownHostException e)
							{
								Log.i("TAG", "UnknownHostException");
								e.printStackTrace();

								if( thread_in_buffer != null )
								{
									thread_in_buffer.running = false;
									thread_in_buffer = null;
								}

								if( thread_out_buffer != null )
								{
									thread_out_buffer.running = false;
									thread_out_buffer = null;
								}

								client_socket = null;

								try {
									Thread.sleep(1500);
								} catch (InterruptedException e1) {
									e1.printStackTrace();
								}

								continue;
							}
							catch (IOException e)
							{
								Log.i("TAG", "IOException");
								e.printStackTrace();

								try
								{
									if( client_socket != null )
									{
										client_socket.shutdownOutput();
										client_socket.close();
									}
								}
								catch (IOException e1)
								{
									e1.printStackTrace();
								}

								if( thread_in_buffer != null )
								{
									thread_in_buffer.running = false;
									thread_in_buffer = null;
								}

								if( thread_out_buffer != null )
								{
									thread_out_buffer.running = false;
									thread_out_buffer = null;
								}

								client_socket = null;
								continue;
							}
							
							if( _is_connected() )
							{
								callback_open_connection_init();
								callback_open_connection(count_open_connection);

								count_open_connection += 1;

								break;
							}
					    }
						
						try 
						{ Thread.sleep(1500); }
						catch (InterruptedException e) 
						{ e.getLocalizedMessage(); }
					}
					
				}

				//Log.i("TAG", "RunThreadConnectionServer stop2");
				
			}
		});
		
		thread_connection_server.setPriority(Thread.MAX_PRIORITY - 1);
		
		thread_connection_server.start();
	}
	// --------------------------------------------------------------------------------------
	private boolean _is_connected()
	{
		if(this.client_socket == null)
		{ return false; }
		
		return ! this.client_socket.isClosed();
	}
	// --------------------------------------------------------------------------------------
	public void clearBuffersInOut()
	{
		synchronized (lockObject)
		{
			in_buffer = null;
			this.out_buffer.clear();
			Log.i("TAG", "clearBuffersInOut");
		}
	}

    public SessionInfo getSessionInfo()
    {
        return session_info;
    }

	public void setSessionInfo(SessionInfo v)
	{
		session_info = v;

		if(queue_network != null)
		{
			queue_network.setSessionInfo(v);
		}
	}
	
	public void clearInPutBuffer()
	{
		in_buffer = null;
	}
	
	public void clearBeginBuffer(int new_offset)
	{
		if(in_buffer.length - new_offset <= 0)
		{
			in_buffer = null;
		}
		else
		{
			byte[] new_in_buf = new byte[ in_buffer.length - new_offset ];
			
			System.arraycopy(in_buffer, new_offset, new_in_buf, 0  , in_buffer.length - new_offset);
			
			in_buffer = new_in_buf;
		}
	}

	private class RunThread_OUT_Buffer extends Thread
	{
		public boolean running = true;
		private long time_echo_send = System.currentTimeMillis() + 20000;
		private long time_out_need_send_echo = 9 * 1000;
		private DataOutputStream output = null;
		private boolean callback_init = false;

		@Override
		public void run()
		{
			Thread.currentThread().setPriority(Thread.MAX_PRIORITY - 1);

			/*try {
				Thread.sleep(300);
			} catch (InterruptedException e) {
				e.printStackTrace();
			}*/

			Log.d("TAG", "RunThread_OUT_Buffer start");

			while (running)
			{
				if( ! work_net )
				{
					Log.d("TAG", "RunThread_OUT_Buffer stop1");
					return;
				}

				if( _is_connected() )
				{
							if( output != null && (System.currentTimeMillis() - this.time_echo_send > this.time_out_need_send_echo))
							{
								this.time_echo_send = System.currentTimeMillis();

								callback_on_send_echo();
							}

							if( ! callback_init )
							{
								callback_init = true;

								index_run_thread_in_out += 1;
								callback_open_connection2(index_run_thread_in_out, "RunThread_OUT_Buffer");
							}

							if( output == null )
							{
								try
								{
									output = new DataOutputStream(client_socket.getOutputStream());
								}
								catch (IOException e)
								{
									output = null;
									e.printStackTrace();
									continue;
								}
							}

							//out_put = t.cmd;
							//------------------------------------------------------------------------

							if(client_socket == null)
							{
								Log.d("TAG", "___=________=__________");
								continue;
							}


							if(output != null && out_buffer.size() > 0)
							{
								_OutBuffer t = null;
								String out_put = "";

								synchronized (lockObject)
								{
									if ( ! out_buffer.listIterator().hasNext())
									{
										continue;
									}

									t = out_buffer.listIterator().next();

									if(t == null )
									{
										continue;
									}
								}

									try
									{
										//output.flush();

										// encrypt
										//byte[] _os = Xor.xor(out_put, XOR_PASS);
											   /*byte[] _os = dataEncrypt(out_put.getBytes(), XOR_PASS);

											   byte[] _len_message = ByteBuffer.allocate(4).putInt(_os.length).array();

											   byte[] _all = new byte[ 4 + 1 + _os.length ];

											   System.arraycopy(_len_message,   0, _all, 0                      , _len_message.length);
											   System.arraycopy(";".getBytes(), 0, _all, _len_message.length    , 1);
											   System.arraycopy(_os,            0, _all, _len_message.length + 1, _os.length);
											   */

										//output.write(_all);

										output.write( ProtokolUtils.get_buffer_command_encrypt(
										          t.cmd
												, t.params
												, t.buffer
												, session_info
												, t.timeadd) );

											   /*output.writeBytes(new String(_len_message)
																+";".getBytes()
																+new String(_os)
											   ); */

											   /*output.writeInt(_os.length);
											   output.writeBytes(";");
											   output.writeBytes( new String(_os) );*/

										Log.i("!!", ">> " + out_put );
										output.flush();


										callback_on_write( out_put );

										//out_buffer.entrySet().remove(tmp);

										synchronized (lockObject)
										{
											out_buffer.remove(t);
										}
									}
									catch (IOException e)
									{
										e.printStackTrace();
									}


						     }
						// -----------------------------------------------------------------------------


				}



				try
				{ Thread.sleep(200); }
				catch (InterruptedException e)
				{ e.getLocalizedMessage(); }
			}

			if(output != null)
			{
				try {
					output.close();
				} catch (IOException e) {
					e.printStackTrace();
				}
			}

			Log.d("TAG", "RunThread_OUT_Buffer stop0");
		}
	}

	private class RunThread_IN_Buffer extends Thread
	{
		public boolean running = true;
		private DataInputStream input = null;
		private boolean callback_init = false;

		@Override
		public void run()
		{
			Thread.currentThread().setPriority(Thread.MAX_PRIORITY - 1);

			/*try {
				Thread.sleep(300);
			} catch (InterruptedException e) {
				e.printStackTrace();
			}*/

			Log.d("TAG", "RunThread_IN_Buffer start");

			while (running)
			{
				if( ! work_net )
				{
					Log.d("TAG", "RunThread_IN_Buffer stop1");
					return;
				}

                try
                { Thread.sleep(150); }
                catch (InterruptedException e)
                { e.getLocalizedMessage(); }

				if( _is_connected() )
				{
					if( ! callback_init )
					{
						callback_init = true;

						index_run_thread_in_out += 1;
						callback_open_connection2(index_run_thread_in_out, "RunThread_IN_Buffer");
					}

					if( input == null )
					{
						try
						{
							input = new DataInputStream(client_socket.getInputStream());
						}
						catch (IOException e)
						{
							input = null;
							e.printStackTrace();
							continue;
						}
					}

					try
					{
						//byte[] buffer = new byte[1024*2];

						if(client_socket == null)
						{
							Log.d("TAG", "___+________+__________");
							continue;
						}

						//int a = input.available();
						//int num_read = input.read(buffer);

						ReadCommand r = ProtokolUtils.read_command_encrypt(input, session_info);

                        if( r != null)
                        {
                            if(r.init_ok == true)
                            {
                                callback_on_read(r, input);
                            }
                            else if(r.close_sock == true)
                            {
                                Log.i("TAG", "---+-");

                                session_info = new SessionInfo();

                                if(client_socket != null)
                                {
                                    client_socket.close();
                                }

                                if( ! on_destroy )
                                {
                                    callback_close_connection();

                                    callback_close_connection2();
                                }

                                try
                                { Thread.sleep(700); }
                                catch (InterruptedException e)
                                { e.getLocalizedMessage(); }

                                continue;
                            }
                        }


					}
					catch(java.net.SocketTimeoutException e)
					{
						Log.i("!!", "SocketTimeoutException");
					}
					catch (IOException e)
					{
						Log.i("!!", "read_1"); /* time out */

						try
						{
							Log.i("TAG", "--- close socket");
							client_socket.close();

							if( ! on_destroy )
							{
								callback_close_connection();

								callback_close_connection2();
							}

						}
						catch (IOException e2)
						{ e2.printStackTrace(); }

						try
						{ Thread.sleep(500); }
						catch (InterruptedException e1)
						{ e1.getLocalizedMessage(); }

						continue;
					}

					///-------------------

				}



				try
				{ Thread.sleep(200); }
				catch (InterruptedException e)
				{ e.getLocalizedMessage(); }
			}

			if(input != null)
			{
				try {
					input.close();
				} catch (IOException e) {
					e.printStackTrace();
				}
			}

			Log.d("TAG", "RunThread_IN_Buffer stop0");
		}
	}
	
	public ClassNetWork(Context context, String host, int port, SessionInfo _session_info)
	{
		this.TCP_HOST = host;
		this.TCP_PORT = port;

		session_info = _session_info;

		_time_start_connect = System.currentTimeMillis();

		queue_network = new QueueNetwork( this, session_info )
		{
			@Override
			public boolean _exec2(final DataInputStream in_soket, final ReadCommand r, Socket sock, final Object param)
			{
				return callback_exec2(in_soket, r, sock, param);
			}
		};
		
		RunThreadConnectionServer();

		//db = new DB(context);
		
		/*
		this.thread_connection_server = new ThreadConnectionServer();
		this.thread_connection_server.execute();
		
		this.thread_in_buffer= new Thread_IN_Buffer();
		this.thread_in_buffer.execute();
		
		this.thread_out_buffer= new Thread_OUT_Buffer();
		this.thread_out_buffer.execute();	*/
	}
	
	public void OnDestroy()
	{
		Log.i("TAG", "ClassNetWork OnDestroy");

		on_destroy = true;

		this.queue_network.OnDestroy();
		this.queue_network = null;

		try
    	{
    		if(client_socket != null || this._is_connected())
    		{ 
    			work_net = false;

				if( thread_in_buffer != null )
				{
					thread_in_buffer.running = false;
					thread_in_buffer = null;
				}

				if( thread_out_buffer != null )
				{
					thread_out_buffer.running = false;
					thread_out_buffer = null;
				}

    			//client_socket.shutdownInput();
    			//client_socket.shutdownOutput();
    			client_socket.close();
    			client_socket = null;
    		}
    	}
    	catch(IOException e)
    	{
    		Log.i("TAG", "error close socket #0101");
			e.printStackTrace();
    	}
		finally
		{
			if(client_socket != null)
			{
				client_socket = null;
			}
		}

    	System.gc();		
	}
	
	public void addOutBuffer(String cmd, ArrayList<String> params, byte[] add_buffer, int priority, long timestamp)
	{	
		synchronized (lockObject)
		{
			_OutBuffer _t = new _OutBuffer(cmd, params, add_buffer, priority, timestamp);
			
			List<_OutBuffer> _tmp = new ArrayList<_OutBuffer>(this.out_buffer);
			
			_tmp.add( _t );
			
			for(int i=0; i < _tmp.size() - 1; i++)
			{
				for(int j=i+1; j < _tmp.size(); j++)
				{
					if(
							(_tmp.get(i).priority > _tmp.get(j).priority)
							||
							(
								   (_tmp.get(i).priority == _tmp.get(j).priority)
								&& (_tmp.get(i).timeadd > _tmp.get(j).timeadd)
							)
					   )
					{
						_OutBuffer _t1 = _tmp.get(j);
						
						_tmp.set(j, _tmp.get(i) );
						_tmp.set(i, _t1);
					}
				}
			}
			
			Log.i("!!", "------------------------------------------");
			for(int i=0; i < _tmp.size(); i++)
			{
				Log.i("!!", "::: " + _tmp.get(i).cmd + " - " + String.valueOf(_tmp.get(i).priority));
			}
			
			this.out_buffer = _tmp;
		}
	}
	
	
	public byte[] getInBuffer()
	{
		return in_buffer;
	}
	
	public List<_OutBuffer> getOutBuffer()
	{
		return this.out_buffer;
	}	
	
	public void set_use_callback_on_read(boolean value)
	{
		this._use_callback_on_read = value;
	}
	
	// callbacks -------------------------------------------
	public void callback_close_connection() { }

	private void callback_close_connection2()
	{
		if( thread_in_buffer != null )
		{
			thread_in_buffer.running = false;
			thread_in_buffer = null;
		}

		if( thread_out_buffer != null )
		{
			thread_out_buffer.running = false;
			thread_out_buffer = null;
		}
	}

	public void callback_open_connection2(int index_c, String v) { }

	public void callback_open_connection_init() 
	{ 
		if( _is_connected() )
		{
			System.gc();

			index_run_thread_in_out = 0;

			if( thread_in_buffer != null )
			{
				thread_in_buffer.running = false;
				thread_in_buffer = null;
			}

			thread_in_buffer = new RunThread_IN_Buffer();
			thread_in_buffer.start();

			System.gc();

			if( thread_out_buffer != null )
			{
				thread_out_buffer.running = false;
				thread_out_buffer = null;
			}

			thread_out_buffer = new RunThread_OUT_Buffer();
			thread_out_buffer.start();

			Log.i("TAG", "start in out threads");
		}
		else
		{
			client_socket = null;
		}
	}
	
	public void callback_open_connection(int count_open_connection) { }
	
	public int callback_on_read(ReadCommand r, InputStream input)
	{
		return 0;
	}
	
	public void callback_on_write(String write)
	{ }

	public void callback_on_send_echo()
	{ }
	
	public void callback_timeout_connect_to_server()
	{ }

	public boolean callback_exec2(final DataInputStream in_soket, final ReadCommand r, Socket sock, final Object param) { return
			true; }
	
	public static long getLong(byte[] array, int offset) 
	{
	    return
	      ((long)(array[offset]   & 0xff) << 56) |
	      ((long)(array[offset+1] & 0xff) << 48) |
	      ((long)(array[offset+2] & 0xff) << 40) |
	      ((long)(array[offset+3] & 0xff) << 32) |
	      ((long)(array[offset+4] & 0xff) << 24) |
	      ((long)(array[offset+5] & 0xff) << 16) |
	      ((long)(array[offset+6] & 0xff) << 8) |
	      ((long)(array[offset+7] & 0xff));
	 }
	
	public static int byteArrayToInt(byte[] bytes) 
	{
	    int result = 0;
	    int l = bytes.length - 1;
	    for(int i = 0; i < bytes.length; i++) 
	      if(i == l) result += bytes[i] << i * 8;
	      else result += (bytes[i] & 0xFF) << i * 8;
	    return result;
	}
	
	public static final long unsignedIntToLong(byte[] b) 
	{
	    long l = 0;
	    l |= b[0] & 0xFF;
	    l <<= 8;
	    l |= b[1] & 0xFF;
	    l <<= 8;
	    l |= b[2] & 0xFF;
	    l <<= 8;
	    l |= b[3] & 0xFF;
	    return l;
	}


	
	
}
