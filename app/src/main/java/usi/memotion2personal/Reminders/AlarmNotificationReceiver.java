package usi.memotion2personal.Reminders;

import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.media.RingtoneManager;
import android.os.Handler;
import android.support.v4.app.NotificationCompat;
import android.support.v4.app.TaskStackBuilder;

import java.util.Random;

import usi.memotion2personal.MainActivity;
import usi.memotion2personal.R;
import usi.memotion2personal.Surveys;
import usi.memotion2personal.WeeklyActivityPopup;

/**
 * Created by shkurtagashi
 */

public class AlarmNotificationReceiver extends BroadcastReceiver {

    @Override
    public void onReceive(Context context, Intent intent) {
        System.out.println("I am in receiver!");

        String session = intent.getExtras().get("Session").toString();
        String arr[] = (String[]) intent.getExtras().get("weekday");

        if(session.equals("Wednesday - Pre")){
            setLectureNotification(context, "Lecture Survey", " ", session, 100090);
        }else if(session.equals("Wednesday - Break")){
            setLectureNotification(context, "Lecture Survey", " ", session, 100091);
        }else if(session.equals("Wednesday - Post")){
            setLectureNotification(context, "Lecture Survey", " ", session, 100092);
        }else if(session.equals("Friday - Pre")){
            setLectureNotification(context, "Lecture Survey", " ", session, 100093);
        }else if(session.equals("Friday - Break")){
            setLectureNotification(context, "Lecture Survey", " ", session, 100094);
        }else if(session.equals("Friday - Post")){
            setLectureNotification(context, "Lecture Survey", " ", session, 100095);
        }else if(session.equals("E4")) {
            setE4Notification(context, "E4 charging time", "Please don't forget to charge E4 and upload the data", 100096);
        } else if (session.equals("early morning")) {
            setDailyNotification(context, "Morning survey", " ", 100097, context.getString(R.string.early_morning));
        } else if (session.equals("morning")) {
            setDailyNotification(context, "Midday survey", " ", 100098, context.getString(R.string.morning));
        } else if (session.equals("afternoon")) {
            setDailyNotification(context, "Afternoon survey", " ", 100099, context.getString(R.string.afternoon));
        } else if (session.equals("weekly")) {
            setDailyNotification(context, "Weekly survey", " ", 100100, context.getString(R.string.weekly));
        } else if (session.equals("ediary")) {
            setEdiary(context, "E-diary reminder", " ", context.getString(R.string.ediary),100101);
        } else {
            setEdiaryWeeklyNotification(context, "Weekly Activity Update", "Have you just finished to do: " + session + "?", context.getString(R.string.ediary), 100200, arr);
        }
    }


    public void setLectureNotification(Context context, String title, String content, String session, final int notificationID) {
        Random rand = new Random();
        int code = rand.nextInt(1000000);
        System.out.println("code: " + code);

        NotificationCompat.Builder builder = new NotificationCompat.Builder(context);
        builder.setContentTitle(title);
        builder.setContentText(content);
        builder.setSound(RingtoneManager.getDefaultUri(RingtoneManager.TYPE_NOTIFICATION));
        builder.setAutoCancel(true);
        builder.setOngoing(true);
        /*
        When notification clicked, open Lecture Surveys Fragment
         */
        Intent intent = new Intent(context, Surveys.class);
        intent.putExtra("type_survey", session);


        TaskStackBuilder stackBuilder = TaskStackBuilder.create(context);
        stackBuilder.addParentStack(MainActivity.class);
        stackBuilder.addNextIntent(intent);
        PendingIntent pendingIntent = stackBuilder.getPendingIntent(code, PendingIntent.FLAG_UPDATE_CURRENT);
        builder.setContentIntent(pendingIntent);
        builder.setSmallIcon(R.drawable.surveys);

        final NotificationManager notificationManager = (NotificationManager) context.getSystemService(context.NOTIFICATION_SERVICE);
        if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.O) {
            String CHANNEL_ID = "memotion_channel";// The id of the channel.
            CharSequence name = "memotion";
            int importance = NotificationManager.IMPORTANCE_HIGH;
            NotificationChannel mChannel = new NotificationChannel(CHANNEL_ID, name, importance);
            notificationManager.createNotificationChannel(mChannel);
            builder.setChannelId(CHANNEL_ID);
        }

        notificationManager.notify(notificationID, builder.build());

        Handler h = new Handler();
        long delayInMilliseconds = 30 * 60 * 1000; // 60 * 1000;
        h.postDelayed(new Runnable() {
            public void run() {
                notificationManager.cancel(notificationID);
            }
        }, delayInMilliseconds);
    }


    public void setE4Notification(Context context, String title, String content, final int notificationID) {
        Random rand = new Random();
        int code = rand.nextInt(1000000);
        System.out.println("code: " + code);

        NotificationCompat.Builder builder = new NotificationCompat.Builder(context);
        builder.setContentTitle(title);
        builder.setContentText(content);
        builder.setSound(RingtoneManager.getDefaultUri(RingtoneManager.TYPE_NOTIFICATION));
        builder.setAutoCancel(true);
        builder.setOngoing(true);
//        builder.setDefaults(Notification.DEFAULT_LIGHTS | Notification.DEFAULT_VIBRATE);


//        long[] pattern = {500,500,500,500,500,500};
//        builder.setVibrate(pattern);


        Intent intent = new Intent(context, MainActivity.class);

        TaskStackBuilder stackBuilder = TaskStackBuilder.create(context);
        stackBuilder.addParentStack(MainActivity.class);
        stackBuilder.addNextIntent(intent);
        PendingIntent pendingIntent = stackBuilder.getPendingIntent(code, PendingIntent.FLAG_UPDATE_CURRENT);

        builder.setContentIntent(pendingIntent);
        builder.setSmallIcon(R.drawable.surveys);

        final NotificationManager notificationManager = (NotificationManager) context.getSystemService(context.NOTIFICATION_SERVICE);
        if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.O) {
            String CHANNEL_ID = "memotion_channel";// The id of the channel.
            CharSequence name = "memotion";
            int importance = NotificationManager.IMPORTANCE_HIGH;
            NotificationChannel mChannel = new NotificationChannel(CHANNEL_ID, name, importance);
            notificationManager.createNotificationChannel(mChannel);
            builder.setChannelId(CHANNEL_ID);
        }

        notificationManager.notify(notificationID, builder.build());

        Handler h = new Handler();
        long delayInMilliseconds = 5 * 60 * 60 * 1000; // 2 * 60 * 1000;
        h.postDelayed(new Runnable() {
            public void run() {
                notificationManager.cancel(notificationID);
            }
        }, delayInMilliseconds);
    }


    public void setDailyNotification(Context context, String title, String content, final int notificationID,
                                     String type) {

        Random rand = new Random();
        int code = rand.nextInt(1000000);
        System.out.println("code: " + code);


        NotificationCompat.Builder builder = new NotificationCompat.Builder(context);
        builder.setContentTitle(title);
        builder.setContentText(content);
        builder.setSound(RingtoneManager.getDefaultUri(RingtoneManager.TYPE_NOTIFICATION));
        builder.setAutoCancel(true);
        builder.setOngoing(true);

        Intent intent = new Intent(context, Surveys.class);
        intent.putExtra("type_survey", type);

        TaskStackBuilder stackBuilder = TaskStackBuilder.create(context);
        stackBuilder.addParentStack(MainActivity.class);
        stackBuilder.addNextIntent(intent);
        PendingIntent pendingIntent = stackBuilder.getPendingIntent(code, PendingIntent.FLAG_UPDATE_CURRENT);

        builder.setContentIntent(pendingIntent);
        builder.setSmallIcon(R.drawable.surveys);

        final NotificationManager notificationManager = (NotificationManager) context.getSystemService(context.NOTIFICATION_SERVICE);
        if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.O) {
            String CHANNEL_ID = "memotion_channel";// The id of the channel.
            CharSequence name = "memotion";
            int importance = NotificationManager.IMPORTANCE_HIGH;
            NotificationChannel mChannel = new NotificationChannel(CHANNEL_ID, name, importance);
            notificationManager.createNotificationChannel(mChannel);
            builder.setChannelId(CHANNEL_ID);
        }

        notificationManager.notify(notificationID, builder.build());

        Handler h = new Handler();
        long delayInMilliseconds = 2 * 60 * 60 * 1000; // 3 * 60 * 1000;
        h.postDelayed(new Runnable() {
            public void run() {
                notificationManager.cancel(notificationID);
            }
        }, delayInMilliseconds);
    }

    public void setEdiary(Context context, String title, String content, String session, final int notificationID) {
        Random rand = new Random();
        int code = rand.nextInt(1000000);
        System.out.println("code: " + code);

        NotificationCompat.Builder builder = new NotificationCompat.Builder(context);
        builder.setContentTitle(title);
        builder.setContentText(content);
        builder.setSound(RingtoneManager.getDefaultUri(RingtoneManager.TYPE_NOTIFICATION));
        builder.setAutoCancel(true);
        builder.setOngoing(true);
        Intent intent = new Intent(context, MainActivity.class);
        intent.putExtra("fragmentChoice", session);

        TaskStackBuilder stackBuilder = TaskStackBuilder.create(context);
        stackBuilder.addParentStack(MainActivity.class);
        stackBuilder.addNextIntent(intent);
        PendingIntent pendingIntent = stackBuilder.getPendingIntent(code, PendingIntent.FLAG_UPDATE_CURRENT);

        builder.setContentIntent(pendingIntent);
        builder.setSmallIcon(R.drawable.surveys);

        final NotificationManager notificationManager = (NotificationManager) context.getSystemService(context.NOTIFICATION_SERVICE);
        if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.O) {
            String CHANNEL_ID = "memotion_channel";// The id of the channel.
            CharSequence name = "memotion";
            int importance = NotificationManager.IMPORTANCE_HIGH;
            NotificationChannel mChannel = new NotificationChannel(CHANNEL_ID, name, importance);
            notificationManager.createNotificationChannel(mChannel);
            builder.setChannelId(CHANNEL_ID);
        }

        notificationManager.notify(notificationID, builder.build());

        Handler h = new Handler();
        long delayInMilliseconds = 30 * 60 * 1000; // 60 * 1000;
        h.postDelayed(new Runnable() {
            public void run() {
                notificationManager.cancel(notificationID);
            }
        }, delayInMilliseconds);
    }

    // Added by Riccardo Gabriele & Michele Lustro
    public void setEdiaryWeeklyNotification(Context context, String title, String content, String session, final int notificationID, String[] arr) {

        Random rand = new Random();
        int code = rand.nextInt(1000000);
        System.out.println("code: " + code);

        NotificationCompat.Builder builder = new NotificationCompat.Builder(context);
        builder.setContentTitle(title);
        builder.setContentText(content);
        builder.setSound(RingtoneManager.getDefaultUri(RingtoneManager.TYPE_NOTIFICATION));
        builder.setAutoCancel(true);
        builder.setOngoing(true);

        Intent intentYes = new Intent(context, WeeklyActivityPopup.class);
        intentYes.putExtra("type_yes", session);
        intentYes.putExtra("weekday", arr);
        intentYes.putExtra("notificationId", notificationID);
        Intent intentNo = new Intent(context, WeeklyActivityPopup.class);
        intentNo.putExtra("type_no", session);
        intentNo.putExtra("weekday", arr);
        intentNo.putExtra("notificationId", notificationID);
        Intent intentRemove = new Intent(context, WeeklyActivityPopup.class);
        intentRemove.putExtra("type_remove", session);
        intentRemove.putExtra("weekday", arr);
        intentRemove.putExtra("notificationId", notificationID);

        builder.addAction(R.drawable.common_google_signin_btn_icon_dark, "Yes", PendingIntent.getActivity(context, code, intentYes, PendingIntent.FLAG_UPDATE_CURRENT));
        builder.addAction(R.drawable.common_google_signin_btn_icon_dark, "No", PendingIntent.getActivity(context, code+1, intentNo,PendingIntent.FLAG_UPDATE_CURRENT));
        builder.addAction(R.drawable.common_google_signin_btn_icon_dark, "Remove from weekly activities", PendingIntent.getActivity(context, code+2, intentRemove, PendingIntent.FLAG_UPDATE_CURRENT));

        builder.setSmallIcon(R.drawable.surveys);

        final NotificationManager notificationManager = (NotificationManager) context.getSystemService(context.NOTIFICATION_SERVICE);
        if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.O) {
            String CHANNEL_ID = "memotion_channel";// The id of the channel.
            CharSequence name = "memotion";
            int importance = NotificationManager.IMPORTANCE_HIGH;
            NotificationChannel mChannel = new NotificationChannel(CHANNEL_ID, name, importance);
            notificationManager.createNotificationChannel(mChannel);
            builder.setChannelId(CHANNEL_ID);
        }

        notificationManager.notify(notificationID, builder.build());

        Handler h = new Handler();
        long delayInMilliseconds = 30 * 60 * 1000; // 60 * 1000;
        h.postDelayed(new Runnable() {
            public void run() {
                notificationManager.cancel(notificationID);
            }
        }, delayInMilliseconds);
    }
}