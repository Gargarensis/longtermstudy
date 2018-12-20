package usi.memotion2personal.local.database.db;

import usi.memotion2personal.local.database.tables.AccelerometerTable;
import usi.memotion2personal.local.database.tables.ActivityRecognitionTable;
import usi.memotion2personal.local.database.tables.ApplicationLogsTable;
import usi.memotion2personal.local.database.tables.EdiaryTable;
import usi.memotion2personal.local.database.tables.FatigueSurveyTable;
import usi.memotion2personal.local.database.tables.LectureSurveyTable;
import usi.memotion2personal.local.database.tables.LocationTable;
import usi.memotion2personal.local.database.tables.NotificationsTable;
import usi.memotion2personal.local.database.tables.OverallSurveyTable;
import usi.memotion2personal.local.database.tables.PAMSurveyTable;
import usi.memotion2personal.local.database.tables.PAMTable;
import usi.memotion2personal.local.database.tables.PSQISurveyTable;
import usi.memotion2personal.local.database.tables.PSSSurveyTable;
import usi.memotion2personal.local.database.tables.PersonalitySurveyTable;
import usi.memotion2personal.local.database.tables.PhoneCallLogTable;
import usi.memotion2personal.local.database.tables.PhoneLockTable;
import usi.memotion2personal.local.database.tables.ProductivitySurveyTable;
import usi.memotion2personal.local.database.tables.SMSTable;
import usi.memotion2personal.local.database.tables.SWLSSurveyTable;
import usi.memotion2personal.local.database.tables.SimpleMoodTable;
import usi.memotion2personal.local.database.tables.SleepQualityTable;
import usi.memotion2personal.local.database.tables.StressSurveyTable;
import usi.memotion2personal.local.database.tables.UserTable;
import usi.memotion2personal.local.database.tables.WeeklySurveyTable;
import usi.memotion2personal.local.database.tables.WiFiTable;

/**
 * Created by usi on 18/01/17.
 */

public class LocalDbUtility {
    private final static int DATA_TABLES_COUNT = 23;

    public static String getTableName(LocalTables table) {
        switch (table) {
            case TABLE_CALL_LOG:
                return PhoneCallLogTable.TABLE_CALL_LOG;
            case TABLE_LOCATION:
                return LocationTable.TABLE_LOCATION;
            case TABLE_PHONELOCK:
                return PhoneLockTable.TABLE_PHONELOCK;
            case TABLE_SMS:
                return SMSTable.TABLE_SMS;
            case TABLE_PAM:
                return PAMTable.TABLE_PAM;
            case TABLE_SIMPLE_MOOD:
                return SimpleMoodTable.TABLE_SIMPLE_MOOD;
            case TABLE_NOTIFICATIONS:
                return NotificationsTable.TABLE_NOTIFICATIONS;
            case TABLE_USER:
                return UserTable.TABLE_USER;
            case TABLE_LECTURE_SURVEY:
                return LectureSurveyTable.TABLE_LECTURE_SURVEY;
            case TABLE_APPLICATION_LOGS:
                return ApplicationLogsTable.TABLE_APPLICATION_LOGS;
            case TABLE_ACTIVITY_RECOGNITION:
                return ActivityRecognitionTable.TABLE_ACTIVITY_RECOGNITION;
            case TABLE_PSQI:
                return PSQISurveyTable.TABLE_PSQUI_SURVEY;
            case TABLE_PERSONALITY:
                return PersonalitySurveyTable.TABLE_PERSONALITY_SURVEY;
            case TABLE_PSSS:
                return PSSSurveyTable.TABLE_PSS_SURVEY;
            case TABLE_SWLS:
                return SWLSSurveyTable.TABLE_SWLSS_SURVEY;
            case TABLE_SLEEP_QUALITY:
                return SleepQualityTable.TABLE_SLEEP_QUALITY_SURVEY;
            case TABLE_FATIGUE_SURVEY:
                return FatigueSurveyTable.TABLE_FATIGUE_SURVEY;
            case TABLE_OVERALL_SURVEY:
                return OverallSurveyTable.TABLE_OVERALL_SURVEY;
            case TABLE_STRESS_SURVEY:
                return StressSurveyTable.TABLE_STRESS_SURVEY;
            case TABLE_WEEKLY_SURVEY:
                return WeeklySurveyTable.TABLE_WEEKLY_SURVEY;
            case TABLE_EDIARY_TABLE:
                return EdiaryTable.TABLE_EDIARY_TABLE;
            case TABLE_PAM_SURVEY:
                return PAMSurveyTable.TABLE_PAM_SURVEY;
            case TABLE_PRODUCTIVITY_SURVEY:
                return ProductivitySurveyTable.TABLE_PRODUCTIVITY_SURVEY;
            default:
                return null;
        }
    }


    public static String[] getTableColumns(LocalTables table) {
        switch (table) {
            case TABLE_ACCELEROMETER:
                return AccelerometerTable.getColumns();
            case TABLE_CALL_LOG:
                return PhoneCallLogTable.getColumns();
            case TABLE_LOCATION:
                return LocationTable.getColumns();
            case TABLE_PHONELOCK:
                return PhoneLockTable.getColumns();
            case TABLE_SMS:
                return SMSTable.getColumns();
            case TABLE_WIFI:
                return WiFiTable.getColumns();
            case TABLE_PAM:
                return PAMTable.getColumns();
            case TABLE_SIMPLE_MOOD:
                return SimpleMoodTable.getColumns();
            case TABLE_NOTIFICATIONS:
                return NotificationsTable.getColumns();
            case TABLE_APPLICATION_LOGS:
                return ApplicationLogsTable.getColumns();
            case TABLE_ACTIVITY_RECOGNITION:
                return ActivityRecognitionTable.getColumns();
            case TABLE_LECTURE_SURVEY:
                return LectureSurveyTable.getColumns();
            case TABLE_USER:
                return UserTable.getColumns();
            case TABLE_PERSONALITY:
                return PersonalitySurveyTable.getColumns();
            case TABLE_PSQI:
                return PSQISurveyTable.getColumns();
            case TABLE_PSSS:
                return PSSSurveyTable.getColumns();
            case TABLE_SWLS:
                return SWLSSurveyTable.getColumns();
            case TABLE_SLEEP_QUALITY:
                return SleepQualityTable.getColumns();
            case TABLE_FATIGUE_SURVEY:
                return FatigueSurveyTable.getColumns();
            case TABLE_OVERALL_SURVEY:
                return OverallSurveyTable.getColumns();
            case TABLE_PRODUCTIVITY_SURVEY:
                return ProductivitySurveyTable.getColumns();
            case TABLE_STRESS_SURVEY:
                return StressSurveyTable.getColumns();
            case TABLE_WEEKLY_SURVEY:
                return WeeklySurveyTable.getColumns();
            case TABLE_PAM_SURVEY:
                return PAMSurveyTable.getColumns();
            case TABLE_EDIARY_TABLE:
                return EdiaryTable.getColumns();
            default:
                return null;
        }
    }
}
