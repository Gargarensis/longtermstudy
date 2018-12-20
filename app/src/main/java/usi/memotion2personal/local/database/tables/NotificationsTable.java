package usi.memotion2personal.local.database.tables;

/**
 * Created by shkurtagashi on 9/19/18.
 */

public class NotificationsTable {
    public static final String TABLE_NOTIFICATIONS = "notifications";
    public static final String KEY_NOTIF_ID = "notif_id";
    public static final String KEY_NOTIF_TAG = "tag";
    public static final String KEY_NOTIF_KEY = "key";
    public static final String KEY_NOTIF_PRIORITY = "priority";
    public static final String KEY_NOTIF_TITLE = "title";
    public static final String KEY_NOTIF_ARRIVAL_TIME = "arrival_time";
    public static final String KEY_NOTIF_REMOVAL_TIME = "removal_time";
    public static final String KEY_NOTIF_CLICKED = "clicked";
//    public static final String KEY_NOTIF_LED = "led";
//    public static final String KEY_NOTIF_VIBRATE = "vibrate";
//    public static final String KEY_NOTIF_SOUND = "sound";
//    public static final String KEY_NOTIF_UNIQUE_SOUND = "unique_sound";
    public static final String KEY_NOTIF_APP_NAME = "app_name";
    public static final String KEY_NOTIF_APP_PACKAGE = "app_package";


    public static String getCreateQuery() {
        return "CREATE TABLE " + TABLE_NOTIFICATIONS +
                "(" +
                KEY_NOTIF_ID + " INTEGER PRIMARY KEY AUTOINCREMENT, " +
                KEY_NOTIF_TAG + " TEXT, " +
                KEY_NOTIF_KEY + " TEXT, " +
                KEY_NOTIF_PRIORITY + " INTEGER, " +
                KEY_NOTIF_TITLE + " TEXT, " +
                KEY_NOTIF_ARRIVAL_TIME + " INTEGER, " +
                KEY_NOTIF_REMOVAL_TIME + " INTEGER, " +
                KEY_NOTIF_CLICKED + " INTEGER, " +
//                KEY_NOTIF_LED + " TEXT, " +
//                KEY_NOTIF_VIBRATE + " TEXT, " +
//                KEY_NOTIF_SOUND + " TEXT, " +
//                KEY_NOTIF_UNIQUE_SOUND + " TEXT, " +
                KEY_NOTIF_APP_NAME + " TEXT, " +
                KEY_NOTIF_APP_PACKAGE + " TEXT " + ")";
    }

    public static String[] getColumns() {
        String[] columns = {
                KEY_NOTIF_ID,
                KEY_NOTIF_TAG,
                KEY_NOTIF_KEY,
                KEY_NOTIF_PRIORITY,
                KEY_NOTIF_TITLE,
                KEY_NOTIF_ARRIVAL_TIME,
                KEY_NOTIF_REMOVAL_TIME,
                KEY_NOTIF_CLICKED,
//                KEY_NOTIF_LED,
//                KEY_NOTIF_VIBRATE,
//                KEY_NOTIF_SOUND,
//                KEY_NOTIF_UNIQUE_SOUND,
                KEY_NOTIF_APP_NAME,
                KEY_NOTIF_APP_PACKAGE
        };
        return columns;
    }
}
