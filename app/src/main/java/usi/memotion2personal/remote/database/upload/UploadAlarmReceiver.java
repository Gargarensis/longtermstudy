package usi.memotion2personal.remote.database.upload;

import android.app.AlarmManager;
import android.app.PendingIntent;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.util.Log;

/**
 * Created by shkurtagashi on 04.03.17.
 */

public class UploadAlarmReceiver extends BroadcastReceiver {

    @Override
    public void onReceive(Context context, Intent intent) {
        System.out.println("I am in UPLOAD ALARM receiver.");
        setAlarm(context, 2);

        //Start UploadService
        Intent intent1 = new Intent(context, AlarmService.class);
        context.startService(intent1);
    }
    public void setAlarm(Context context, int requestCode){
        Intent intent = new Intent(context, UploadAlarmReceiver.class);
        AlarmManager alarmManager = (AlarmManager) context.getSystemService(context.ALARM_SERVICE);

        alarmManager.set(AlarmManager.RTC_WAKEUP, System.currentTimeMillis()+86400000, PendingIntent.getBroadcast(context, requestCode, intent, PendingIntent.FLAG_ONE_SHOT)); //86 400 000 (1 day)
        Log.v("UploadAlarmReceiver", "Alarm set for next day as well.");
    }
}
