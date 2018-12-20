package usi.memotion2personal.remote.database.upload;

import android.app.IntentService;
import android.app.Notification;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.app.usage.UsageStats;
import android.app.usage.UsageStatsManager;
import android.content.ContentValues;
import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.provider.Settings;
import android.support.v4.app.NotificationCompat;
import android.support.v4.app.TaskStackBuilder;
import android.util.Log;

import java.util.Calendar;
import java.util.List;
import java.util.Random;
import java.util.Timer;
import java.util.TimerTask;

import usi.memotion2personal.AccountUtils;
import usi.memotion2personal.MainActivity;
import usi.memotion2personal.R;
import usi.memotion2personal.gathering.gatheringServices.ApplicationLogs.CustomUsageStats;
import usi.memotion2personal.local.database.controllers.LocalStorageController;
import usi.memotion2personal.local.database.controllers.SQLiteController;
import usi.memotion2personal.local.database.db.LocalSQLiteDBHelper;
import usi.memotion2personal.local.database.tableHandlers.ApplicationLogData;
import usi.memotion2personal.local.database.tables.ApplicationLogsTable;
import usi.memotion2personal.local.database.tables.UserTable;
import usi.memotion2personal.remote.database.controllers.SwitchDriveController;


/**
 * Created by shkurtagashi
 */

/**
 * An {@link IntentService} subclass for handling asynchronous task requests in
 * a service on a separate handler thread.
 * <p>
 */

public class AlarmService extends IntentService {
    Timer timer;

    SwitchDriveController switchDriveController;
    SQLiteController localController;
    LocalSQLiteDBHelper dbHelper;
    String androidID;

    UsageStatsManager mUsageStatsManager;
    LocalStorageController localStorageController;

    public AlarmService() {
        super("AlarmService");
    }

    @Override
    protected void onHandleIntent(Intent intent) {

        int MINUTES = 30; //The delay in minutes

        Log.v("AlarmService", "I am in AlarmService");

        androidID = Settings.Secure.getString(getApplicationContext().getContentResolver(), Settings.Secure.ANDROID_ID);
        dbHelper = new LocalSQLiteDBHelper(getApplicationContext());
        switchDriveController = new SwitchDriveController(getApplicationContext().getString(R.string.server_address), getApplicationContext().getString(R.string.token), AccountUtils.getPassword(getApplicationContext()));
        localController = SQLiteController.getInstance(getApplicationContext());
        String query = "SELECT * FROM usersTable";
        Cursor records = localController.rawQuery(query, null);
        records.moveToFirst();

        String username = null;

        if (records.getCount() > 0){
            username = records.getString(records.getColumnIndex(UserTable.USERNAME));
        }

        String userName = username + "_" + androidID;

//        localController.rawQuery("UPDATE " + NotificationsTable.TABLE_NOTIFICATIONS + " SET " + NotificationsTable.KEY_NOTIF_TITLE + " = 0", null);

        final Uploader uploader = new Uploader(userName, switchDriveController, localController, dbHelper);

        localStorageController = SQLiteController.getInstance(getApplicationContext());
        mUsageStatsManager = (UsageStatsManager) getSystemService(Context.USAGE_STATS_SERVICE);

        //Insert data from UsageStats to ApplicationLogs table
        getUsageStatistics(UsageStatsManager.INTERVAL_DAILY);


        timer = new Timer();
        timer.schedule(new TimerTask() {
            @Override
            public void run() {

                ConnectivityManager connectivityManager = (ConnectivityManager) getApplicationContext().getSystemService(Context.CONNECTIVITY_SERVICE);
                NetworkInfo wifi = connectivityManager.getNetworkInfo(ConnectivityManager.TYPE_WIFI);

                //If there is WiFi connection when Alarm is triggered then UPLOAD data
                if (wifi.isConnected()) {
                    Log.v("Blla blla", "WIFI CONNECTED");
                    //Upload Local Tables
                    uploader.dailyUpload();
                }else{
                    Log.v("UPLOAD TEST NO WIFI", "TIMER NOT CANCELED, BUT TRY AGAIN TO UPLOAD");
                    uploader.dailyUpload();

                }
            }
        }, 0, 1000*60*MINUTES);
    }

    public void setNotification(Context context, String title, String content, int notificationID){
        Random rand = new Random();
        int code = rand.nextInt(100000000);
        System.out.println("code: "+code);

        NotificationCompat.Builder builder = new NotificationCompat.Builder(context);
        builder.setContentTitle(title);
        builder.setContentText(content);
        builder.setDefaults(Notification.DEFAULT_SOUND);
        builder.setAutoCancel(true);

        Intent intent = new Intent(context, MainActivity.class);

        TaskStackBuilder stackBuilder = TaskStackBuilder.create(context);
        stackBuilder.addParentStack(MainActivity.class);
        stackBuilder.addNextIntent(intent);
        PendingIntent pendingIntent = stackBuilder.getPendingIntent(code, PendingIntent.FLAG_UPDATE_CURRENT);

        builder.setContentIntent(pendingIntent);
        builder.setSmallIcon(R.drawable.info);

        NotificationManager notificationManager = (NotificationManager) context.getSystemService(context.NOTIFICATION_SERVICE);
        notificationManager.notify(notificationID, builder.build());

    }

    public void deleteNotificationsTitle(){

//        rawQuery("SELECT id, name FROM people WHERE name = ? AND id = ?", new String[] {"David", "2"});



    }


    public List<UsageStats> getUsageStatistics(int intervalType) {
        // Get the app statistics since one year ago from the current time.
        Calendar cal1 = Calendar.getInstance();
        cal1.add(Calendar.DAY_OF_MONTH, -1);
        cal1.add(Calendar.HOUR, 23);
        cal1.add(Calendar.MINUTE, 0);


        Calendar cal2 = Calendar.getInstance();
        cal2.add(Calendar.DAY_OF_MONTH, 0);
        cal2.add(Calendar.HOUR, 23);
        cal2.add(Calendar.MINUTE, 0);

        long start = cal1.getTimeInMillis();
        long end = cal2.getTimeInMillis();

        List<UsageStats> queryUsageStats = mUsageStatsManager.queryUsageStats(intervalType, start, end);
        for (int i = 0; i < queryUsageStats.size(); i++) {
            CustomUsageStats customUsageStats = new CustomUsageStats();
            customUsageStats.usageStats = queryUsageStats.get(i);

            ApplicationLogData appData = new ApplicationLogData(true);
            appData.setFirstTimestamp(customUsageStats.usageStats.getFirstTimeStamp());
            appData.setLastTimestamp(customUsageStats.usageStats.getLastTimeStamp());
            appData.setLastTimeUsed(customUsageStats.usageStats.getLastTimeUsed());
            appData.setAppPackage(customUsageStats.usageStats.getPackageName());
            appData.setTotalTimeInForeground(customUsageStats.usageStats.getTotalTimeInForeground());
            insertRecord(appData);
        }

        return queryUsageStats;
    }

    private void insertRecord(ApplicationLogData appData) {
        ContentValues record = new ContentValues();

        record.put(ApplicationLogsTable.KEY_APP_FIRST_TIMESTAMP, appData.getFirstTimestamp());
        record.put(ApplicationLogsTable.KEY_APP_LAST_TIMESTAMP, appData.getLastTimestamp());
        record.put(ApplicationLogsTable.KEY_APP_LAST_TIME_USED, appData.getLastTimeUsed());
        record.put(ApplicationLogsTable.KEY_APP_TOTAL_TIME_FOREGROUND, appData.getTotalTimeInForeground());
        record.put(ApplicationLogsTable.KEY_APP_PACKAGE_NAME, appData.getAppPackageName());


        localStorageController.insertRecord(ApplicationLogsTable.TABLE_APPLICATION_LOGS, record);
        android.util.Log.d("ALARM SERVICE", "Added APPLICATION LOG: " + ", First_timestamp: " + record.get(ApplicationLogsTable.KEY_APP_FIRST_TIMESTAMP)
                + ", Last_timestamp: " + record.get(ApplicationLogsTable.KEY_APP_LAST_TIMESTAMP) + ", Last_time_used: " + record.get(ApplicationLogsTable.KEY_APP_LAST_TIME_USED) +
                ", Total_time: " + record.get(ApplicationLogsTable.KEY_APP_TOTAL_TIME_FOREGROUND) + ", APP_Package: " + record.get(ApplicationLogsTable.KEY_APP_PACKAGE_NAME));
    }

}
