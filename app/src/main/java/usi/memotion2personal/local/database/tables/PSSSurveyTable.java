package usi.memotion2personal.local.database.tables;

import android.provider.BaseColumns;


/**
 * Created by biancastancu
 */
public class PSSSurveyTable {
    public final static String TABLE_PSS_SURVEY = "PSSSurvey";

    public final static String _ID = BaseColumns._ID;
    public final static String TIMESTAMP = "timestamp";
    public final static String QUESTION_1 = "question1";
    public final static String QUESTION_2 = "question2";
    public final static String QUESTION_3 = "question3";
    public final static String QUESTION_4 = "question4";
    public final static String QUESTION_5 = "question5";
    public final static String QUESTION_6 = "question6";
    public final static String QUESTION_7 = "question7";
    public final static String QUESTION_8 = "question8";
    public final static String QUESTION_9 = "question9";
    public final static String QUESTION_10 = "question10";

    public static String getCreateQuery() {
        return "CREATE TABLE " + TABLE_PSS_SURVEY + "("
                + _ID + " INTEGER PRIMARY KEY AUTOINCREMENT,"
                + TIMESTAMP + "  INTEGER DEFAULT CURRENT_TIMESTAMP, "
                + QUESTION_1 + " TEXT, "
                + QUESTION_2 + " TEXT, "
                + QUESTION_3 + " TEXT, "
                + QUESTION_4 + " TEXT, "
                + QUESTION_5 + " TEXT, "
                + QUESTION_6 + " TEXT, "
                + QUESTION_7 + " TEXT, "
                + QUESTION_8 + " TEXT, "
                + QUESTION_9 + " TEXT, "
                + QUESTION_10 + " TEXT "+ ")";
    }


    public static String[] getColumns(){
        String[] columns = {_ID, TIMESTAMP, QUESTION_1, QUESTION_2, QUESTION_3, QUESTION_4, QUESTION_5, QUESTION_6, QUESTION_7, QUESTION_8, QUESTION_9, QUESTION_10};

        return columns;
    }
}
