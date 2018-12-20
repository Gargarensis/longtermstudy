package usi.memotion2personal.local.database.tables;

import android.provider.BaseColumns;

/**
 * Created by biancastancu
 */

public class ProductivitySurveyTable {
    public final static String TABLE_PRODUCTIVITY_SURVEY = "PRODUCTIVITY_SURVEY";

    public final static String _ID = BaseColumns._ID;
    public final static String TIMESTAMP = "timestamp";
    public final static String QUESTION_1 = "question1";
    public final static String QUESTION_2 = "question2";
    public final static String QUESTION_3 = "question3";
    public final static String QUESTION_4 = "question4";

    public static String getCreateQuery() {
        return "CREATE TABLE " + TABLE_PRODUCTIVITY_SURVEY + "("
                + _ID + " INTEGER PRIMARY KEY AUTOINCREMENT,"
                + TIMESTAMP + "  INTEGER DEFAULT CURRENT_TIMESTAMP, "
                + QUESTION_1 + " TEXT, "
                + QUESTION_2 + " TEXT, "
                + QUESTION_3 + " TEXT, "
                + QUESTION_4 + " TEXT)";
    }


    public static String[] getColumns() {
        String[] columns = {_ID, TIMESTAMP, QUESTION_1, QUESTION_2, QUESTION_3, QUESTION_4};
        return columns;
    }
}
