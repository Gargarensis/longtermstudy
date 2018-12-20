package usi.memotion2personal.local.database.tables;

import android.provider.BaseColumns;

/**
 * Created by shkurtagashi on 12/09/18.
 */
public class UserTable {


    // Private constructor to prevent someone from accidentally instantiating the contract class,
    private UserTable() {}

        /*
        * Users - Table and Columns declaration
         */
        public final static String TABLE_USER = "usersTable";

        public final static String _ID = BaseColumns._ID;
        public final static String ANDROID_ID = "android_id";
        public final static String USERNAME = "username";
        public final static String COLUMN_GENDER = "gender";
        public final static String COLUMN_AGE = "age";
        public final static String COLUMN_STATUS = "status";

        public static String getCreateQuery(){
            return "CREATE TABLE " + TABLE_USER + " ("
                    + _ID + " INTEGER PRIMARY KEY AUTOINCREMENT, "
                    + ANDROID_ID + " TEXT, "
                    + USERNAME + " TEXT, "
                    + COLUMN_GENDER + " TEXT, "
                    + COLUMN_AGE + " TEXT, "
                    + COLUMN_STATUS + " TEXT);";
        }


        /**
         * Possible values for the age of the user
         */
        public static final String AGE_20_30 = "20-30";
        public static final String AGE_30_40 = "30-40";
        public static final String AGE_40_50 = "40-50";
        public static final String AGE_50_ABOVE = "50 and above";


        /**
         * Possible values for the gender of the user
         */
        public static final String GENDER_MALE = "M";
        public static final String GENDER_FEMALE = "F";


        public static final String STATUS_FULL_PROFESSOR = "Professor";
        public static final String STATUS_POST_DOC = "Post-doc";
        public static final String STATUS_PHD_STUDENT = "Ph.D. Student";
        public static final String RESEARCHER = "Researcher";
        public static final String STATUS_ASSISTANT = "Assistant";
        public static final String STUDENT = "Student";

        public static final String OTHER = "Other";


        public static String[] getColumns(){
            String[] columns = {_ID, ANDROID_ID, USERNAME, COLUMN_GENDER, COLUMN_AGE, COLUMN_STATUS}; //EMPATICAID, EMAIL, SWITCH_TOKEN, SWITCH_PASS

            return columns;
        }

}
