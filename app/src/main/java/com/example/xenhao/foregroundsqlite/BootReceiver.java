package com.example.xenhao.foregroundsqlite;

import android.app.AlarmManager;
import android.app.PendingIntent;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.util.Log;
import android.widget.Toast;

import java.util.Calendar;

/**
 * Created by XenHao on 4/12/2014.
 */
public class BootReceiver extends BroadcastReceiver {
    @Override
    public void onReceive(Context context, Intent intent) {
        if(intent.getAction().equals("android.intent.action.BOOT_COMPLETED")){
            //  Do work here
            Intent serviceIntent = new Intent(context, ForegroundService.class);
            serviceIntent.setAction(Constants.ACTION.STARTFOREGROUND_ACTION);
//            PendingIntent pendingIntent = PendingIntent.getBroadcast(context, 0, serviceIntent, 0);
            PendingIntent pendingIntent = PendingIntent.getService(context, 0, serviceIntent, 0);
            AlarmManager manager = (AlarmManager)context.getSystemService(Context.ALARM_SERVICE);

            //  define time to start service
            Calendar calendar = Calendar.getInstance();
            calendar.setTimeInMillis(System.currentTimeMillis());
            calendar.set(Calendar.HOUR_OF_DAY, 9);
            calendar.set(Calendar.MINUTE, 0);

            //  run alarm manager to start service on defined time
            manager.setRepeating(AlarmManager.RTC_WAKEUP, calendar.getTimeInMillis(), AlarmManager.INTERVAL_DAY, pendingIntent);

            Toast.makeText(context, "Service Start Time Set Successfully", Toast.LENGTH_LONG).show();
            Log.i("Auto Start Service", "Successfully set auto startup");
        }
    }
}
