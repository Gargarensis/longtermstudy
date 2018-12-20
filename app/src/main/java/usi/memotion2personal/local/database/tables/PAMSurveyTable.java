package usi.memotion2personal.local.database.tables;

import android.provider.BaseColumns;

/**
 * Created by biancastancu
 */

public class PAMSurveyTable {
    public final static String TABLE_PAM_SURVEY = "PAM_SURVEY";

    public final static String _ID = BaseColumns._ID;
    public final static String TIMESTAMP = "timestamp";
    public final static String IMAGE_CHOSEN = "image";

    public static String getCreateQuery() {
        return "CREATE TABLE " + TABLE_PAM_SURVEY + "("
                + _ID + " INTEGER PRIMARY KEY AUTOINCREMENT,"
                + TIMESTAMP + " INTEGER DEFAULT CURRENT_TIMESTAMP, "
                + IMAGE_CHOSEN + " TEXT)";
    }


    public static String[] getColumns() {
        String[] columns = {_ID, TIMESTAMP, IMAGE_CHOSEN};
        return columns;
    }
}
