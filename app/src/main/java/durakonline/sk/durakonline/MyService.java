package durakonline.sk.durakonline;

import android.app.AlarmManager;
import android.app.Notification;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.app.Service;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.os.Build;
import android.os.IBinder;
import android.os.SystemClock;
import android.support.annotation.Nullable;
import android.support.annotation.RequiresApi;
import android.support.v4.app.NotificationCompat;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.io.OutputStreamWriter;
import java.io.UnsupportedEncodingException;
import java.net.HttpURLConnection;
import java.net.URL;
import java.net.URLEncoder;
import java.util.HashMap;
import java.util.Map;

import javax.net.ssl.HttpsURLConnection;

import durakonline.sk.durakonline.other.Log;

public class MyService extends Service
{
    private String HOST = "";
    private DB db;
    private String app_name = "";

    @Nullable
    @Override
    public IBinder onBind(Intent intent)
    {
        return null;
    }

    public static String getApplicationName(Context context)
    {
        int stringId = context.getApplicationInfo().labelRes;
        return context.getString(stringId);
    }

    @Override
    public void onCreate()
    {
        super.onCreate();

        app_name = getApplicationName(MyService.this);

        /*if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O)
        {
            startMyOwnForeground();
        }
        else
        {
            //startForeground(1, new Notification());

            NotificationCompat.Builder builder = new NotificationCompat.Builder(this)
                    .setContentTitle(getString(R.string.app_name))
                    .setContentText(app_name)
                    .setPriority(NotificationCompat.PRIORITY_DEFAULT)
                    .setAutoCancel(true);

            Notification notification = builder.build();

            startForeground(1, notification);
        }*/
    }

    @RequiresApi(api = Build.VERSION_CODES.O)
    private void startMyOwnForeground()
    {
        String NOTIFICATION_CHANNEL_ID = getPackageName();
        String channelName = "Background Service";
        NotificationChannel chan = new NotificationChannel(NOTIFICATION_CHANNEL_ID, channelName, NotificationManager.IMPORTANCE_NONE);
        chan.setLightColor(Color.BLUE);
        chan.setLockscreenVisibility(Notification.VISIBILITY_PRIVATE);
        NotificationManager manager = (NotificationManager) getSystemService(Context.NOTIFICATION_SERVICE);
        assert manager != null;
        manager.createNotificationChannel(chan);

        NotificationCompat.Builder notificationBuilder = new NotificationCompat.Builder(this, NOTIFICATION_CHANNEL_ID);
        Notification notification = notificationBuilder.setOngoing(true)
                //.setSmallIcon(R.mipmap.ic_launcher)
                .setContentTitle("Running in background")
                .setPriority(NotificationManager.IMPORTANCE_NONE)
                //.setCategory(Notification.CATEGORY_SERVICE)
                .build();

        //notification.flags |= Notification.FLAG_NO_CLEAR;

        startForeground(1, notification);
    }

    @Override
    public int onStartCommand (Intent intent, int flags, int startId)
    {
        HOST = JniApi.f1();
        thread_read_invitation();

        db = new DB( getApplicationContext() );

        return Service.START_STICKY;
    }

    Thread thread_read_sos = null;
    boolean read_sos = false;

    private String getPostDataString(HashMap<String, String> params) throws UnsupportedEncodingException
    {
        StringBuilder result = new StringBuilder();
        boolean first = true;
        for(Map.Entry<String, String> entry : params.entrySet())
        {
            if (first)
                first = false;
            else
                result.append("&");

            result.append(URLEncoder.encode(entry.getKey(), "UTF-8"));
            result.append("=");
            result.append(URLEncoder.encode(entry.getValue(), "UTF-8"));
        }

        return result.toString();
    }


    private String performPostCall(String requestURL, HashMap<String, String> postDataParams)
    {
        URL url;
        String response = "";
        try
        {
            url = new URL(requestURL);

            HttpURLConnection conn = (HttpURLConnection) url.openConnection();
            conn.setReadTimeout(15000);
            conn.setConnectTimeout(15000);
            conn.setRequestMethod("POST");

            conn.setDoInput(true);
            conn.setDoOutput(true);

            OutputStream os = conn.getOutputStream();
            BufferedWriter writer = new BufferedWriter( new OutputStreamWriter(os, "UTF-8") );
            writer.write(getPostDataString(postDataParams));

            writer.flush();
            writer.close();
            os.close();
            int responseCode=conn.getResponseCode();

            if (responseCode == HttpsURLConnection.HTTP_OK)
            {
                String line;
                BufferedReader br=new BufferedReader(new InputStreamReader(conn.getInputStream()));
                while ((line=br.readLine()) != null)
                {
                    response+=line;
                }
            }
            else
            {
                response="";

            }
        }
        catch (Exception e)
        {
            e.printStackTrace();
        }

        return response;
    }

    private void add_Notification2(String title, String textContent, Bitmap b, long room_id, boolean non_canceble)
    {
        NotificationCompat.Builder mBuilder = null;

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O)
        {
            NotificationManager notificationManager = (NotificationManager) getSystemService(NOTIFICATION_SERVICE);
            NotificationChannel notificationChannel = new NotificationChannel("notification_channel_2", "My Notifications", NotificationManager.IMPORTANCE_DEFAULT);

            // Configure the notification channel.
            notificationChannel.setDescription(textContent);
            notificationChannel.enableLights(true);
            notificationChannel.setLightColor(Color.RED);
            //notificationChannel.setVibrationPattern(new long[]{0, 1000, 500, 1000});
            notificationChannel.enableVibration(true);
            notificationManager.createNotificationChannel(notificationChannel);
            notificationChannel.shouldShowLights();

            //*************
            Intent notificationIntent = new Intent(MyService.this, MainActivity.class);

            notificationIntent.putExtra("open_room_id", room_id);

            notificationIntent.setFlags(Intent.FLAG_ACTIVITY_SINGLE_TOP);

            PendingIntent contentIntent = PendingIntent.getActivity(this, 0, notificationIntent, PendingIntent.FLAG_UPDATE_CURRENT);

            NotificationCompat.Builder builder = new NotificationCompat.Builder(this, "notification_channel_2")
                    //.setVibrate(new long[]{0, 100, 100, 100, 100, 100})
                    //.setSound(RingtoneManager.getDefaultUri(RingtoneManager.TYPE_NOTIFICATION))
                    .setSmallIcon(R.mipmap.ic_launcher)
                    .setAutoCancel(false)
                    .setContentTitle( title )
                    .setContentIntent(contentIntent)
                    .setContentText( textContent );

            builder.setVibrate(new long[] { 500, 500 });
            //LED
            builder.setLights(Color.BLUE, 2000, 3000);

            Notification note = builder.build();
            //note.flags = Notification.FLAG_SHOW_LIGHTS;

            notificationManager.notify(10955, note);

            return;
        }

        if(android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.LOLLIPOP)
        {
            mBuilder = new NotificationCompat.Builder(
                    this)
                    .setSmallIcon(R.drawable.w_ic_launcher)
                    .setContentTitle( title )
                    .setLargeIcon(b)
                    .setAutoCancel(true)
                    .setContentText(textContent)
                    .setTicker(textContent)
                    .setWhen(System.currentTimeMillis()) //отображаемое время уведомления
                    .setPriority(Notification.PRIORITY_HIGH)
            ;
        }
        else
        {
            mBuilder = new NotificationCompat.Builder(
                    this)
                    .setSmallIcon(R.drawable.w_ic_launcher)
                    .setContentTitle( title )
                    .setLargeIcon(b)
                    .setAutoCancel(true)
                    .setContentText(textContent)
                    .setTicker(textContent)
                    .setWhen(System.currentTimeMillis()) //отображаемое время уведомления
                    .setPriority(Notification.PRIORITY_HIGH)
            ;
        }

        Intent notificationIntent = new Intent(MyService.this, MainActivity.class);

        notificationIntent.putExtra("open_room_id", room_id);

        notificationIntent.setFlags(Intent.FLAG_ACTIVITY_SINGLE_TOP);

        PendingIntent contentIntent = PendingIntent.getActivity(this, 0, notificationIntent, PendingIntent.FLAG_UPDATE_CURRENT);

        mBuilder.setContentIntent(contentIntent);
        mBuilder.setVibrate(new long[] { 500, 500 });
        //LED
        mBuilder.setLights(Color.BLUE, 2000, 3000);

        Notification note = mBuilder.build();

        //note.flags = Notification.FLAG_ONLY_ALERT_ONCE; // For Non cancellable notification
        note.flags = Notification.FLAG_SHOW_LIGHTS;

        note.defaults |= Notification.DEFAULT_VIBRATE;
        note.defaults |= Notification.DEFAULT_SOUND;

        // Add as notification
        NotificationManager manager = (NotificationManager) getSystemService(Context.NOTIFICATION_SERVICE);

        manager.notify(10955, note);
    }

    private void check_invitation()
    {
        String personId = db.getKeyValue("accout_info.personId", "");

        if( personId == null || personId.trim().length() == 0 )
        {
            return;
        }

        String[] host_list = HOST.split(";");

        String _url = "http://"+ host_list[0] +":10568/get_data_invitation/";

        HashMap<String, String> postDataParams = new HashMap<>();

        postDataParams.put("p", personId);


        String result = performPostCall(_url, postDataParams);

        Log.i("TAG", result);

        if( result.trim().length() == 0 )
        {
            return;
        }

        try
        {
            JSONArray r = new JSONArray(result);

            if( r.length() > 0 )
            {
                JSONObject item = r.getJSONObject(0);

                String room_id    = item.getString("room_id");
                String nik_from   = item.getString("nikname_uid_from");
                int uid_from    = item.getInt("uid_from");

                Bitmap b = null;

                b = BitmapFactory.decodeResource(getResources(), R.mipmap.ic_launcher);

                String _txt = "", _txt1 = "";

                _txt = getString(R.string.txt_98);
                _txt1 = getString(R.string.txt_99);

                add_Notification2(_txt,  " " + _txt1 + " " + nik_from, b, Long.parseLong(room_id), true);
            }

        }
        catch (JSONException e)
        {
            e.printStackTrace();
        }
    }

    private void thread_read_invitation()
    {
        if(thread_read_sos != null)
        { return; }

        read_sos = true;

        thread_read_sos = new Thread(new Runnable()
        {
            @Override
            public void run()
            {
                int wait_count = 0;

                while( read_sos )
                {
                    wait_count += 1;

                    if( wait_count > 30 )
                    {
                        wait_count = 0;
                        check_invitation();
                    }

                    try {
                        Thread.sleep(500);
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }
                }

                thread_read_sos =  null;
            }
        });

        Log.i("TAG", "Check invitation STOP");

        thread_read_sos.setPriority(Thread.MAX_PRIORITY);

        thread_read_sos.start();
    }

    @Override
    public void onLowMemory()
    {
        super.onLowMemory();
        Log.i("TAG", "onLowMemory()");
    }

    @Override
    public void onDestroy()
    {
        super.onDestroy();

        Log.i("TAG", "onDestroy");

        Intent restartService = new Intent("com.onlineradio.RestartService");
        sendBroadcast(restartService);
    }

    @Override
    public void onTaskRemoved(Intent rootIntent)
    {
        Log.i("TAG", "onTaskRemoved");


        Intent restartService = new Intent(getApplicationContext(),
                this.getClass());
        restartService.setPackage(getPackageName());
        PendingIntent restartServicePI = PendingIntent.getService(
                getApplicationContext(), 1, restartService,
                PendingIntent.FLAG_ONE_SHOT);

        //Restart the service once it has been killed android


        AlarmManager alarmService = (AlarmManager)getApplicationContext().getSystemService(Context.ALARM_SERVICE);
        alarmService.set(AlarmManager.ELAPSED_REALTIME, SystemClock.elapsedRealtime() +100, restartServicePI);

    }
}
