package usi.memotion2personal.gathering.gatheringServices.Notifications;

import android.annotation.SuppressLint;
import android.app.Notification;
import android.content.ContentValues;
import android.content.Context;
import android.content.pm.ApplicationInfo;
import android.content.pm.PackageManager;
import android.content.pm.PackageManager.NameNotFoundException;
import android.database.Cursor;
import android.os.Build;
import android.os.PowerManager;
import android.service.notification.StatusBarNotification;

import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.Calendar;
import java.util.Locale;

import usi.memotion2personal.gathering.gatheringServices.Notifications.Utils.APILevel;
import usi.memotion2personal.gathering.gatheringServices.Notifications.Utils.Log;
import usi.memotion2personal.local.database.controllers.LocalStorageController;
import usi.memotion2personal.local.database.controllers.SQLiteController;
import usi.memotion2personal.local.database.tableHandlers.NotificationData;
import usi.memotion2personal.local.database.tables.NotificationsTable;

/**
 * Created by abhinavmerotra
 */

public class NotificationDataCollector {
    private final Log log;
    private final Context context;
    private LocalStorageController localStorageController;
    private MessageDigest messageDigest;

    public NotificationDataCollector(Context context) {
        this.log = new Log();
        this.context = context;
        localStorageController = SQLiteController.getInstance(context);
        try {
            messageDigest = MessageDigest.getInstance("MD5");
        } catch (NoSuchAlgorithmException e) {
        }
    }

    @SuppressLint("NewApi")
    protected void onNotificationPosted(StatusBarNotification sbn) {
        try {
            NotificationType nt = new NotificationType(sbn);
            if (nt.isCallNotification() || nt.isOngoingNotification()
                    || nt.isCollectionNotification()) {
                return;
            }

            Notification n = sbn.getNotification();
            String tag = " ";
            if (sbn.getTag() != null) {
                tag = getHex(messageDigest.digest(sbn.getTag().getBytes()));
            }
            String key = " ";
            if (new APILevel().getDeviceAPILevel() >= 20) {
                if (sbn.getKey() != null) {
                    key = getHex(messageDigest.digest(sbn.getKey().getBytes()));
                }
            }

            int priority = n.priority;
            long arrivalTime = sbn.getPostTime();
            long removalTime = 0; //to be updated later
            int clicked = 0; //temporary
            String app_name = getAppNameFromPackage(context, sbn.getPackageName());
            String app_package = sbn.getPackageName();
            String title = "";
            if (n.extras.getCharSequence(Notification.EXTRA_TITLE) != null) {
                title = getHex(messageDigest.digest(n.extras.getCharSequence(Notification.EXTRA_TITLE).toString().getBytes()));
            }
            if (title.equalsIgnoreCase(app_name) && n.extras.getCharSequence(Notification.EXTRA_TEXT) != null) {
                title = getHex(messageDigest.digest(n.extras.getCharSequence(Notification.EXTRA_TEXT).toString().getBytes()));
            }
            title = title.replace("\n", " ").trim();
            insertRecord(tag, key, priority, title, arrivalTime, removalTime, clicked, app_name, app_package);
        } catch (Exception e) {
            log.e("NotificationDataCollector: unable to save notification data in database " + e.toString());
        }

    }


    @SuppressLint("NewApi")
    protected void onNotificationRemoved(StatusBarNotification sbn) {
        NotificationType nt = new NotificationType(sbn);
        if (nt.isCallNotification() || nt.isOngoingNotification()
                || nt.isCollectionNotification()) {
            return;
        }

        final Notification n = sbn.getNotification();

        // check for screen lock
        PowerManager powerManager = (PowerManager) context.getSystemService(Context.POWER_SERVICE);
        @SuppressWarnings("deprecation")
        boolean isSceenAwake = (Build.VERSION.SDK_INT < 20 ? powerManager.isScreenOn() : powerManager.isInteractive());
        new Log().e("Screen awake: " + isSceenAwake);

        final String app_name = getAppNameFromPackage(context, sbn.getPackageName());

        String title = "";

        if (n.extras.getCharSequence(Notification.EXTRA_TITLE) != null)
            title = n.extras.getCharSequence(Notification.EXTRA_TITLE).toString();
        if (title.equalsIgnoreCase(app_name) && n.extras.getCharSequence(Notification.EXTRA_TEXT) != null)
            title = n.extras.getCharSequence(Notification.EXTRA_TEXT).toString().trim();

        String key = "";
        if (sbn.getKey() != null) {
            key = sbn.getKey();
        }
        NotificationData n_data = null;

        try {
            n_data = getRecord(app_name, getHex(messageDigest.digest(title.getBytes())), getHex(messageDigest.digest(key.getBytes())));
        } catch (Exception e) {
            log.e("Error: unable to get notification: " + e.toString());
        }

        if (n_data != null) {
            n_data.setRemovalTime(Calendar.getInstance().getTimeInMillis());
            updateNotification(n_data);
        } else {
            log.d("Notification ignored because it was not saved in the SharedPref when posted!");
            log.d("Title: " + title);
        }

    }


    private void insertRecord(String tag, String key, int priority, String title, long arrival_time, long removal_time, int click, String app_n, String app_p) {
        ContentValues record = new ContentValues();

        record.put(NotificationsTable.KEY_NOTIF_TAG, tag);
        record.put(NotificationsTable.KEY_NOTIF_KEY, key);
        record.put(NotificationsTable.KEY_NOTIF_PRIORITY, priority);
        record.put(NotificationsTable.KEY_NOTIF_TITLE, title);
        record.put(NotificationsTable.KEY_NOTIF_ARRIVAL_TIME, arrival_time);
        record.put(NotificationsTable.KEY_NOTIF_REMOVAL_TIME, removal_time);
        record.put(NotificationsTable.KEY_NOTIF_CLICKED, click);
        record.put(NotificationsTable.KEY_NOTIF_APP_NAME, app_n);
        record.put(NotificationsTable.KEY_NOTIF_APP_PACKAGE, app_p);


        localStorageController.insertRecord(NotificationsTable.TABLE_NOTIFICATIONS, record);
        android.util.Log.d("NOTIFICATIONS SERVICE", "Added record: TAG: " + record.get(NotificationsTable.KEY_NOTIF_TAG)
                + ", KEY: " + record.get(NotificationsTable.KEY_NOTIF_KEY) + ", PRIORITY: " + record.get(NotificationsTable.KEY_NOTIF_PRIORITY) + ", TITLE: " + record.get(NotificationsTable.KEY_NOTIF_TITLE)
                + ", ARRIVAL_TIME: " + record.get(NotificationsTable.KEY_NOTIF_ARRIVAL_TIME) + ", REMOVAL_TIME: " + record.get(NotificationsTable.KEY_NOTIF_REMOVAL_TIME)
                + ", CLICKED: " + record.get(NotificationsTable.KEY_NOTIF_CLICKED) + ", APP_NAME: " + record.get(NotificationsTable.KEY_NOTIF_APP_NAME) + ", APP_Package: " + record.get(NotificationsTable.KEY_NOTIF_APP_PACKAGE));
    }

    private void updateNotification(NotificationData data) {
        String clause = NotificationsTable.KEY_NOTIF_APP_NAME + " = \"" + data.getAppName() + "\" AND " + NotificationsTable.KEY_NOTIF_TITLE + " = \"" + data.getTitle() + "\"";

        ContentValues val = new ContentValues();
        val.put(NotificationsTable.KEY_NOTIF_APP_NAME, data.getAppName());
        val.put(NotificationsTable.KEY_NOTIF_APP_PACKAGE, data.getAppPackageName());
        val.put(NotificationsTable.KEY_NOTIF_TAG, data.getTag());
        val.put(NotificationsTable.KEY_NOTIF_KEY, data.getKey());
        val.put(NotificationsTable.KEY_NOTIF_PRIORITY, data.getPriority());
        val.put(NotificationsTable.KEY_NOTIF_TITLE, data.getTitle());
        val.put(NotificationsTable.KEY_NOTIF_ARRIVAL_TIME, data.getArrivalTime());
        val.put(NotificationsTable.KEY_NOTIF_REMOVAL_TIME, data.getRemovalTime());
        val.put(NotificationsTable.KEY_NOTIF_CLICKED, data.getClicked());

        localStorageController.update(NotificationsTable.TABLE_NOTIFICATIONS, val, clause);

        android.util.Log.d("NOTIFICATIONS SERVICE", "Updated record: APP: " + val.get(NotificationsTable.KEY_NOTIF_APP_NAME) + ", TITLE: " + val.get(NotificationsTable.KEY_NOTIF_TITLE)
                + ", KEY: " + val.get(NotificationsTable.KEY_NOTIF_KEY) + ", ARRIVAL TIME: " + val.get(NotificationsTable.KEY_NOTIF_ARRIVAL_TIME) + ", REMOVAL TIME: " + val.get(NotificationsTable.KEY_NOTIF_REMOVAL_TIME));
    }

    private NotificationData getRecord(String app_name, String title, String key) {

        LocalStorageController localController = SQLiteController.getInstance(this.context);
        String query = "SELECT * FROM " + NotificationsTable.TABLE_NOTIFICATIONS + " WHERE " + NotificationsTable.KEY_NOTIF_APP_NAME + " = \"" + app_name + "\" AND " + NotificationsTable.KEY_NOTIF_KEY + " = \"" + key + "\" AND " + NotificationsTable.KEY_NOTIF_TITLE + " = \"" + title + "\"";
        Cursor records = localController.rawQuery(query, null);
        records.moveToFirst();

        NotificationData data = new NotificationData(false);
        data.setTag(records.getString(records.getColumnIndex(NotificationsTable.KEY_NOTIF_TAG)));
        data.setKey(records.getString(records.getColumnIndex(NotificationsTable.KEY_NOTIF_KEY)));
        data.setPriority(records.getInt(records.getColumnIndex(NotificationsTable.KEY_NOTIF_PRIORITY)));
        data.setTitle(records.getString(records.getColumnIndex(NotificationsTable.KEY_NOTIF_TITLE)));
        data.setArrivalTime(records.getLong(records.getColumnIndex(NotificationsTable.KEY_NOTIF_ARRIVAL_TIME)));
        data.setRemovalTime(records.getLong(records.getColumnIndex(NotificationsTable.KEY_NOTIF_REMOVAL_TIME)));
        data.setClicked(records.getInt(records.getColumnIndex(NotificationsTable.KEY_NOTIF_CLICKED)));
        data.setAppName(records.getString(records.getColumnIndex(NotificationsTable.KEY_NOTIF_APP_NAME)));
        data.setAppPackage(records.getString(records.getColumnIndex(NotificationsTable.KEY_NOTIF_APP_PACKAGE)));

        records.close();

        log.e("The TITLE of the notification was: " + data.getTitle());
        return data;

    }

    private String getAppNameFromPackage(Context context, String package_name) {
        try {
            PackageManager packageManager = context.getPackageManager();
            ApplicationInfo applicationInfo = packageManager.getApplicationInfo(package_name, 0);
            if (applicationInfo == null) {
                return "UNKNOWN";
            } else {
                String app_name = packageManager.getApplicationLabel(applicationInfo).toString();
                if (app_name.toLowerCase(Locale.getDefault()).contains("google search"))
                    app_name = "Google Services";
                return app_name;
            }
        } catch (NameNotFoundException e) {
            return "UNKNOWN";
        }
    }

    /**
     * Method used to transform bytes generated by the MessageDigest with MD5 to a comparable string
     * for future usage.
     *
     * @param hash
     * @return
     */
    public String getHex(byte[] hash) {
        StringBuffer hexString = new StringBuffer();
        for (int i = 0; i < hash.length; i++)
            hexString.append(Integer.toHexString(0xFF & hash[i]));
        return hexString.toString();

    }

}
