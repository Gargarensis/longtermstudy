package usi.memotion2personal.local.database.tables;

import android.provider.BaseColumns;

public class EdiaryTable {
    public final static String TABLE_EDIARY_TABLE = "EDIARY";

    public final static String _ID = BaseColumns._ID;
    public final static String TIMESTAMP = "timestamp";
    public final static String ACTIVITY = "activity";
    public final static String START_TIME = "start_time";
    public final static String END_TIME = "end_time";
    public final static String SOCIAL_INTERACTION = "social_interaction";
    public final static String ENTRY_DATE = "entry_date";
    public final static String COMMENTS = "comments";
    public final static String EMOTION = "emotion";
    public final static String ISWEEKLY = "is_weekly";

    public static String getCreateQuery() {
        return "CREATE TABLE " + TABLE_EDIARY_TABLE + "("
                + _ID + " INTEGER PRIMARY KEY AUTOINCREMENT,"
                + TIMESTAMP + " INTEGER DEFAULT CURRENT_TIMESTAMP, "
                + ACTIVITY + " TEXT,"
                + START_TIME + " TEXT,"
                + END_TIME + " TEXT,"
                + ENTRY_DATE + " TEXT, "
                + EMOTION + " TEXT, "
                + SOCIAL_INTERACTION + " TEXT,"
                + COMMENTS + " TEXT,"
                + ISWEEKLY + " TEXT)";
    }

    public static String[] getColumns() {
        String[] columns = {_ID, TIMESTAMP, TIMESTAMP,ACTIVITY,START_TIME,END_TIME,SOCIAL_INTERACTION,COMMENTS, ENTRY_DATE, ISWEEKLY};
        return columns;
    }
}
