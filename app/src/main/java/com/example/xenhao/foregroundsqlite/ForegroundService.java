package com.example.xenhao.foregroundsqlite;

import android.app.AlarmManager;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.app.Service;
import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.IBinder;
import android.support.v4.app.NotificationCompat;
import android.util.Log;

import java.util.Calendar;

/**
 * Created by XenHao on 30/11/2014.
 */
public class ForegroundService extends Service{

    //  notification variables
    NotificationManager notificationManager;
    NotificationCompat.Builder notification = new NotificationCompat.Builder(this);
    int notiID = 101;
    int count = 0;

    //  database variables
    SQLiteDatabase db;

    @Override
    public void onCreate(){
        super.onCreate();
        //  creates SQLite database
        db = openOrCreateDatabase("RecordsDB", Context.MODE_PRIVATE, null);
        db.execSQL("CREATE TABLE IF NOT EXISTS records("+"click_id INTEGER PRIMARY KEY AUTOINCREMENT,"+"clickno INTEGER)");
        Cursor c = db.rawQuery("SELECT * FROM records", null);
        if (c.getCount() == 0)   db.execSQL("INSERT INTO records(clickno)VALUES(0)");
    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId){           Log.i("Foreground Service", "Service called");
        //  network checking
        Log.i("Foreground Network Check", "Network availability is: " + isNetworkAvailable());

        //  start IntentService to call URL
        Intent startIntent = new Intent(ForegroundService.this, URLExecution.class);
        startService(startIntent);

        //  this part for autostart & SQLite updating
        if(intent.getAction().equals(Constants.ACTION.STARTFOREGROUND_ACTION)){     Log.i("Foreground Service", "Service Started");
            //  define intents
            Intent notificationIntent = new Intent(this, MainActivity.class);
            notificationIntent.setAction(Constants.ACTION.MAIN_ACTION);
            notificationIntent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
            PendingIntent pendingIntent = PendingIntent.getActivity(this, 0, notificationIntent, 0);

            Intent previousIntent = new Intent(this,  ForegroundService.class);
            previousIntent.setAction(Constants.ACTION.PREV_ACTION);
            PendingIntent ppreviousIntent = PendingIntent.getService(this, 0, previousIntent, 0);

            Intent playIntent = new Intent(this, ForegroundService.class);
            playIntent.setAction(Constants.ACTION.PLAY_ACTION);
            PendingIntent pplayIntent = PendingIntent.getService(this, 0, playIntent, 0);

            Intent nextIntent = new Intent(this, ForegroundService.class);
            nextIntent.setAction(Constants.ACTION.NEXT_ACTION);
            PendingIntent pnextIntent = PendingIntent.getService(this, 0, nextIntent, 0);

            //  icon for service notification
            Bitmap icon = BitmapFactory.decodeResource(getResources(), R.drawable.ic_launcher);

            //  define & build notification
            notification.setContentTitle("SQLite Foreground Service")
                    .setTicker("SQLite Foreground Service")
                    .setContentText("Button tapped " + count + " times.")
                    .setSmallIcon(R.drawable.ic_launcher)
                    .setLargeIcon(Bitmap.createScaledBitmap(icon, 128, 128, false))
                    .setContentIntent(pendingIntent)
                    .setOngoing(true)
                    .addAction(android.R.drawable.ic_media_previous, "Tapped", ppreviousIntent)
                    .addAction(android.R.drawable.ic_media_play, "Display", pplayIntent)
                    .addAction(android.R.drawable.ic_media_next, "Tapped", pnextIntent).build();
            notificationManager = (NotificationManager)getSystemService(Context.NOTIFICATION_SERVICE);
            notificationManager.notify(notiID, notification.build());

            //  start foreground service
            startForeground(Constants.NOTIFICATION_ID.FOREGROUND_SERVICE, notification.build());
            //  set shut down time after starting service
            setShutdownTime();
        } else if (intent.getAction().equals(Constants.ACTION.PREV_ACTION)){
            updateCount();
        } else if (intent.getAction().equals(Constants.ACTION.PLAY_ACTION)){
            displayCount();
        } else if (intent.getAction().equals(Constants.ACTION.NEXT_ACTION)){
            updateCount();
        } else if (intent.getAction().equals(Constants.ACTION.STOPFOREGROUND_ACTION)){
            //  stop foreground service
            stopForeground(true);
            stopSelf();
            Log.i("Foreground Service", "Service successfully killed.");
        }
        return START_REDELIVER_INTENT;
    }

//    private void readStream(InputStream inputStream) {
//        BufferedReader reader = null;
//        try{
//            reader = new BufferedReader(new InputStreamReader(inputStream));
//            String line = "";
//            while ((line = reader.readLine()) != null){
//                System.out.println(line);
//            }
//        }catch (IOException e){
//            e.printStackTrace();
//        }finally {
//            if(reader != null){
//                try{
//                    reader.close();     Log.i("Foreground URL Call", "Process done and closed");
//                }catch (IOException e){
//                    e.printStackTrace();
//                }
//            }
//        }
//    }

    private boolean isNetworkAvailable() {
        ConnectivityManager cm = (ConnectivityManager)getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo networkInfo = cm.getActiveNetworkInfo();
        //  if no network is available, networkInfo will be null
        //  otherwise, check if we are connected
        if(networkInfo != null && networkInfo.isConnected()){
            return true;
        }
        return false;
    }

    private void setShutdownTime() {
        Intent serviceIntent = new Intent(this, ForegroundService.class);
        serviceIntent.setAction(Constants.ACTION.STOPFOREGROUND_ACTION);
        PendingIntent pendingIntent = PendingIntent.getService(this, 0, serviceIntent, 0);
        AlarmManager manager = (AlarmManager)getSystemService(Context.ALARM_SERVICE);

        //  define time to stop service
        Calendar calendar = Calendar.getInstance();
        calendar.setTimeInMillis(System.currentTimeMillis());
        calendar.set(Calendar.HOUR_OF_DAY, 18);
        calendar.set(Calendar.MINUTE, 0);

        //  run alarm manager to stop service on defined time
        manager.setRepeating(AlarmManager.RTC_WAKEUP, calendar.getTimeInMillis(), AlarmManager.INTERVAL_DAY, pendingIntent);

    }

    private void updateCount(){
        count++;
        db.execSQL("INSERT INTO records(clickno)VALUES("+ count +")");
        notification.setContentText("Button tapped " + count + " times.");
        notificationManager.notify(notiID, notification.build());
    }

    private void displayCount() {
        Cursor c = db.rawQuery("SELECT * FROM records", null);
        while(c.moveToNext()){
            Log.i("SQLite Foreground Service", "How many times button pushed: " + c.getInt(c.getColumnIndexOrThrow("clickno")) + " ID: " + c.getInt(c.getColumnIndexOrThrow("click_id")));
            notification.setContentText("Button tapped a total of " + c.getInt(c.getColumnIndexOrThrow("clickno")) + " times.");
            notificationManager.notify(notiID, notification.build());
        }
    }

    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }
}
