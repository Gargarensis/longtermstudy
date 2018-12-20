package usi.memotion2personal.local.database.tables;

import android.provider.BaseColumns;

/**
 * Created by biancastancu
 */

public class PSQISurveyTable {
    public final static String TABLE_PSQUI_SURVEY = "PSQISurvey";

    public final static String _ID = BaseColumns._ID;
    public final static String TIMESTAMP = "timestamp";
    public final static String QUESTION_1 = "question1";
    public final static String QUESTION_2 = "question2";
    public final static String QUESTION_3 = "question3";
    public final static String QUESTION_4 = "question4";
    public final static String QUESTION_5 = "question5";
    public final static String QUESTION_5a = "question5a";
    public final static String QUESTION_5b = "question5b";
    public final static String QUESTION_5c = "question5c";
    public final static String QUESTION_5d = "question5d";
    public final static String QUESTION_5e = "question5e";
    public final static String QUESTION_5f = "question5f";
    public final static String QUESTION_5g = "question5g";
    public final static String QUESTION_5h = "question5h";
    public final static String QUESTION_5i = "question5i";
    public final static String QUESTION_5j = "question5j";
    public final static String QUESTION_5j_text = "question5jText";
    public final static String QUESTION_6 = "question6";
    public final static String QUESTION_7 = "question7";
    public final static String QUESTION_8 = "question8";
    public final static String QUESTION_9 = "question9";
    public final static String QUESTION_10 = "question10";
    public final static String QUESTION_10a = "question10a";
    public final static String QUESTION_10b = "question10b";
    public final static String QUESTION_10c = "question10c";
    public final static String QUESTION_10d = "question10d";
    public final static String QUESTION_10e = "question10e";

    public static String getCreateQuery() {
        return "CREATE TABLE " + TABLE_PSQUI_SURVEY + "("
                + _ID + " INTEGER PRIMARY KEY AUTOINCREMENT,"
                + TIMESTAMP + "  INTEGER DEFAULT CURRENT_TIMESTAMP, "
                + QUESTION_1 + " TEXT, "
                + QUESTION_2 + " TEXT, "
                + QUESTION_3 + " TEXT, "
                + QUESTION_4 + " TEXT, "
                + QUESTION_5 + " TEXT, "
                + QUESTION_5a + " TEXT, "
                + QUESTION_5b + " TEXT, "
                + QUESTION_5c + " TEXT, "
                + QUESTION_5d + " TEXT, "
                + QUESTION_5e + " TEXT, "
                + QUESTION_5f + " TEXT, "
                + QUESTION_5g + " TEXT, "
                + QUESTION_5h + " TEXT, "
                + QUESTION_5i + " TEXT, "
                + QUESTION_5j + " TEXT, "
                + QUESTION_5j_text + " TEXT, "
                + QUESTION_6 + " TEXT, "
                + QUESTION_7 + " TEXT, "
                + QUESTION_8 + " TEXT, "
                + QUESTION_9 + " TEXT, "
                + QUESTION_10 + " TEXT, "
                + QUESTION_10a + " TEXT, "
                + QUESTION_10b + " TEXT, "
                + QUESTION_10c + " TEXT, "
                + QUESTION_10d + " TEXT, "
                + QUESTION_10e + " TEXT )";
    }


    public static String[] getColumns(){
        String[] columns = {_ID, TIMESTAMP, QUESTION_1, QUESTION_2, QUESTION_3, QUESTION_4, QUESTION_5,
                QUESTION_5a, QUESTION_5b, QUESTION_5c,QUESTION_5d, QUESTION_5e,
                QUESTION_5f, QUESTION_5g, QUESTION_5h, QUESTION_5i, QUESTION_5j, QUESTION_5j_text,QUESTION_6,
            QUESTION_7, QUESTION_8, QUESTION_9, QUESTION_10, QUESTION_10a,QUESTION_10b,
                QUESTION_10c,QUESTION_10d,QUESTION_10e};

        return columns;
    }
}
