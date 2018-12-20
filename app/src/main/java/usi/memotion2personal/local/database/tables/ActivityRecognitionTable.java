package usi.memotion2personal.local.database.tables;


/**
 * Created by biancastancu
 */
public class ActivityRecognitionTable {
    public static final String TABLE_ACTIVITY_RECOGNITION = "activityRecognition";

    public static final String KEY_ACTIVITY_LOG_ID = "activityLogId";
    public static final String KEY_TIMESTAMP = "timestamp";
    public static final String KEY_ACTIVITY = "activity";
    public static final String KEY_TRANSITION_TYPE = "transition";

    public static String getCreateQuery() {
        return "CREATE TABLE " + TABLE_ACTIVITY_RECOGNITION +
                "(" +
                KEY_ACTIVITY_LOG_ID + " INTEGER PRIMARY KEY AUTOINCREMENT, " +
                KEY_TIMESTAMP + " INTEGER DEFAULT CURRENT_TIMESTAMP, " +
                KEY_ACTIVITY + " INTEGER, " +
                KEY_TRANSITION_TYPE + " INTEGER " + ")";
    }

    public static String[] getColumns() {
        String[] columns = {
                KEY_ACTIVITY_LOG_ID,
                KEY_TIMESTAMP,
                KEY_ACTIVITY,
                KEY_TRANSITION_TYPE
        };
        return columns;
    }
}
