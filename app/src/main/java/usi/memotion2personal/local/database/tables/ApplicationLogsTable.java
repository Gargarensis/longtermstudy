package usi.memotion2personal.local.database.tables;

/**
 * Created by shkurtagashi on 9/20/18.
 */

public class ApplicationLogsTable {
    public static final String TABLE_APPLICATION_LOGS = "applicationLogs";

    public static final String KEY_APPLICATION_ID = "appId";
    public static final String KEY_APP_TOTAL_TIME_FOREGROUND = "totalTimeInForeground";
    public static final String KEY_APP_FIRST_TIMESTAMP = "firstTimestamp";
    public static final String KEY_APP_LAST_TIMESTAMP = "lastTimestamp";
    public static final String KEY_APP_LAST_TIME_USED = "lastTimeUsed";
    public static final String KEY_APP_PACKAGE_NAME = "packageName";

    public static String getCreateQuery() {
        return "CREATE TABLE " + TABLE_APPLICATION_LOGS +
                "(" +
                KEY_APPLICATION_ID + " INTEGER PRIMARY KEY AUTOINCREMENT, " +
                KEY_APP_TOTAL_TIME_FOREGROUND + " INTEGER, " +
                KEY_APP_FIRST_TIMESTAMP + " INTEGER, " +
                KEY_APP_LAST_TIMESTAMP + " INTEGER, " +
                KEY_APP_LAST_TIME_USED + " INTEGER, " +
                KEY_APP_PACKAGE_NAME + " TEXT " + ")";
    }

    public static String[] getColumns() {
        String[] columns = {
                KEY_APPLICATION_ID,
                KEY_APP_TOTAL_TIME_FOREGROUND,
                KEY_APP_FIRST_TIMESTAMP,
                KEY_APP_LAST_TIMESTAMP,
                KEY_APP_LAST_TIME_USED,
                KEY_APP_PACKAGE_NAME
        };
        return columns;
    }


}
