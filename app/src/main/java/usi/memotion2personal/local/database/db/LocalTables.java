package usi.memotion2personal.local.database.db;

import usi.memotion2personal.local.database.tables.AccelerometerTable;
import usi.memotion2personal.local.database.tables.ActivityRecognitionTable;
import usi.memotion2personal.local.database.tables.ApplicationLogsTable;
import usi.memotion2personal.local.database.tables.BlueToothTable;
import usi.memotion2personal.local.database.tables.EdiaryTable;
import usi.memotion2personal.local.database.tables.FatigueSurveyTable;
import usi.memotion2personal.local.database.tables.LectureSurveyTable;
import usi.memotion2personal.local.database.tables.LocationTable;
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
import usi.memotion2personal.local.database.tables.NotificationsTable;


/**
 * Created by usi on 18/01/17.
 */

public enum LocalTables {
    TABLE_BLUETOOTH(BlueToothTable.class),
    TABLE_LOCATION(LocationTable.class),
    TABLE_CALL_LOG(PhoneCallLogTable.class),
    TABLE_PHONELOCK(PhoneLockTable.class),
    TABLE_SMS(SMSTable.class),
    TABLE_WIFI(WiFiTable.class),
    TABLE_PAM(PAMTable.class),
    TABLE_SIMPLE_MOOD(SimpleMoodTable.class),
    TABLE_USER(UserTable.class),
    TABLE_ACCELEROMETER(AccelerometerTable.class),
    TABLE_LECTURE_SURVEY(LectureSurveyTable.class),
    TABLE_NOTIFICATIONS(NotificationsTable.class),
    TABLE_APPLICATION_LOGS(ApplicationLogsTable.class),
    TABLE_ACTIVITY_RECOGNITION(ActivityRecognitionTable.class),
    TABLE_PERSONALITY(PersonalitySurveyTable.class),
    TABLE_PSQI(PSQISurveyTable.class),
    TABLE_SWLS(SWLSSurveyTable.class),
    TABLE_PSSS(PSSSurveyTable.class),
    TABLE_SLEEP_QUALITY(SleepQualityTable.class),
    TABLE_FATIGUE_SURVEY(FatigueSurveyTable.class),
    TABLE_OVERALL_SURVEY(OverallSurveyTable.class),
    TABLE_PRODUCTIVITY_SURVEY(ProductivitySurveyTable.class),
    TABLE_STRESS_SURVEY(StressSurveyTable.class),
    TABLE_WEEKLY_SURVEY(WeeklySurveyTable.class),
    TABLE_PAM_SURVEY(PAMSurveyTable.class),
    TABLE_EDIARY_TABLE(EdiaryTable.class);

    LocalTables(Class a) {
    }

    public String getTableName() {
        switch(this) {
            case TABLE_PAM:
                return PAMTable.TABLE_PAM;
            case TABLE_USER:
                return UserTable.TABLE_USER;
            case TABLE_LECTURE_SURVEY:
                return LectureSurveyTable.TABLE_LECTURE_SURVEY;
            case TABLE_APPLICATION_LOGS:
                return ApplicationLogsTable.TABLE_APPLICATION_LOGS;
            case TABLE_NOTIFICATIONS:
                return NotificationsTable.TABLE_NOTIFICATIONS;
            case TABLE_LOCATION:
                return LocationTable.TABLE_LOCATION;
            case TABLE_CALL_LOG:
                return PhoneCallLogTable.TABLE_CALL_LOG;
            case TABLE_SMS:
                return SMSTable.TABLE_SMS;
            case TABLE_PHONELOCK:
                return PhoneLockTable.TABLE_PHONELOCK;
            case TABLE_SIMPLE_MOOD:
                return SimpleMoodTable.TABLE_SIMPLE_MOOD;
            case TABLE_PERSONALITY:
                return PersonalitySurveyTable.TABLE_PERSONALITY_SURVEY;
            case TABLE_PSSS:
                return PSSSurveyTable.TABLE_PSS_SURVEY;
            case TABLE_PSQI:
                return PSQISurveyTable.TABLE_PSQUI_SURVEY;
            case TABLE_SWLS:
                return SWLSSurveyTable.TABLE_SWLSS_SURVEY;
            case TABLE_ACTIVITY_RECOGNITION:
                return ActivityRecognitionTable.TABLE_ACTIVITY_RECOGNITION;
            case TABLE_SLEEP_QUALITY:
                return SleepQualityTable.TABLE_SLEEP_QUALITY_SURVEY;
            case TABLE_FATIGUE_SURVEY:
                return FatigueSurveyTable.TABLE_FATIGUE_SURVEY;
            case TABLE_OVERALL_SURVEY:
                return OverallSurveyTable.TABLE_OVERALL_SURVEY;
            case TABLE_PRODUCTIVITY_SURVEY:
                return ProductivitySurveyTable.TABLE_PRODUCTIVITY_SURVEY;
            case TABLE_STRESS_SURVEY:
                return StressSurveyTable.TABLE_STRESS_SURVEY;
            case TABLE_WEEKLY_SURVEY:
                return WeeklySurveyTable.TABLE_WEEKLY_SURVEY;
            case TABLE_PAM_SURVEY:
                return PAMSurveyTable.TABLE_PAM_SURVEY;
            case TABLE_EDIARY_TABLE:
                return EdiaryTable.TABLE_EDIARY_TABLE;
            default:
                throw new IllegalArgumentException("Table not found!");
        }
    }

}
