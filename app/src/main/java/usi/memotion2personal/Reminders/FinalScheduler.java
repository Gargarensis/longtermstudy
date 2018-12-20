package usi.memotion2personal.Reminders;

import android.app.AlarmManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.util.Log;
//import android.icu.util.Calendar;

import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.Random;

import usi.memotion2personal.UI.EdiaryAdapter;
import usi.memotion2personal.UI.fragments.EdiaryFragment;
import usi.memotion2personal.local.database.controllers.SQLiteController;
import usi.memotion2personal.local.database.tables.EdiaryTable;

import static usi.memotion2personal.MyApplication.getContext;


/**
 * Created by shkurtagashi on 11.02.17.
 */

public class FinalScheduler {

    /********** Mobile Computing - Day 1 ***********/
    private Weekday preLecture1 = new Weekday(15, 20, Calendar.WEDNESDAY, "Wednesday - Pre"); //15, 20
    private Weekday breakLecture1 = new Weekday(16, 15, Calendar.WEDNESDAY, "Wednesday - Break"); //16, 15
    private Weekday postLecture1 = new Weekday(17, 15, Calendar.WEDNESDAY, "Wednesday - Post"); //17, 15

    /********** Mobile Computing - Day 2 ***********/
    private Weekday preLecture2 = new Weekday(13, 20, Calendar.FRIDAY, "Friday - Pre"); // 13, 20
    private Weekday breakLecture2 = new Weekday(14, 15, Calendar.FRIDAY, "Friday - Break"); // 14, 15
    private Weekday postLecture2 = new Weekday(15, 15, Calendar.FRIDAY, "Friday - Post"); //15, 15

    /********** Daily Reminders *********/
    private Reminder morningSurvey = new Reminder(7, 15, "early morning"); //7:15
    private Reminder afternoonSurvey = new Reminder(12, 30, "morning"); //12:30
    private Reminder eveningSurvey = new Reminder(19, 15, "afternoon"); //19:15
    private Reminder e4 = new Reminder(21, 15, "E4"); //21:15

    /********** Weekly Reminder *********/
    private Weekday weeklySurvey = new Weekday(20, 0, Calendar.FRIDAY, "weekly"); //20:00

    /********** Ediary Reminder *********/
    private Reminder ediarySurvey = new Reminder(21, 0, "ediary"); //21:00


    private Calendar createCalendar(int day, int hour, int minute){
        Calendar calendar = Calendar.getInstance();
        calendar.set(Calendar.HOUR_OF_DAY, hour);
        calendar.set(Calendar.MINUTE, minute);
        calendar.set(Calendar.DAY_OF_WEEK, day);
        calendar.set(Calendar.SECOND, 0);

        return calendar;
    }

    private Calendar createCalendar2(int hour, int minute){
        Calendar calendar = Calendar.getInstance();
        calendar.set(Calendar.HOUR_OF_DAY, hour);
        calendar.set(Calendar.MINUTE, minute);
        calendar.set(Calendar.SECOND, 0);

        return calendar;
    }

      /* Creation of the reminder for lecture and daily surveys */
    public void createReminder(Context context) {

//        preLecture1, preLecture2,

        Weekday [] lectureReminders = {preLecture1, breakLecture1, postLecture1, preLecture2, breakLecture2, postLecture2};

        Reminder [] dailyReminders = {morningSurvey, afternoonSurvey, eveningSurvey, e4, ediarySurvey};



        Random rand = new Random();
        int code;

        //Lecture reminders
        for (Weekday w: lectureReminders) {
            code = rand.nextInt(100000000);
            setWeeklyAlarms(context, code, w.getDescription(), w);
            Log.v("Scheduler", "Session: "+ w.getDescription() + "code: "+code);
        }

        //E4 and Daily surveys
        for (Reminder w: dailyReminders) {
            code = rand.nextInt(100000000);
            setDailyAlarms(context, code, w.getDescription(), w);
            Log.v("Scheduler", "Session: "+ w.getDescription() + "code: "+code);
        }

        code = rand.nextInt(100000000);
        setWeeklyAlarms(context, code, weeklySurvey.getDescription(), weeklySurvey);
        Log.v("Scheduler", "Session: "+ weeklySurvey.getDescription() + "code: "+code);

        HashMap<String, Weekday> ediaryWeeklyReminders = createWeeklyEdiaryReminders();

        for (String timestamp : ediaryWeeklyReminders.keySet()) {
            code = rand.nextInt(100000000);
            Weekday w = ediaryWeeklyReminders.get(timestamp);
            setWeeklyAlarms(context, code, w.getDescription(), w, timestamp);
        }

    }

    public HashMap<String, Weekday> createWeeklyEdiaryReminders() {
        HashMap<String, Weekday> result = new HashMap<>();

        String query = "SELECT " + EdiaryTable.ACTIVITY + "," + EdiaryTable.END_TIME + "," + EdiaryTable.TIMESTAMP + "," + EdiaryTable.ISWEEKLY + " FROM " + EdiaryTable.TABLE_EDIARY_TABLE + " WHERE " + EdiaryTable.ISWEEKLY + " LIKE \"1\";";
        SQLiteController localController = SQLiteController.getInstance(getContext());
        Cursor c = localController.rawQuery(query, null);

        while (c.moveToNext()) {
            Integer hour = Integer.parseInt(c.getString(1).split(":")[0]);
            Integer minute = Integer.parseInt(c.getString(1).split(":")[1]);
            Calendar calendar = Calendar.getInstance();
            calendar.setTime(new Date(new Timestamp(c.getLong(2)).getTime()));
            int dayOfWeek = calendar.get(Calendar.DAY_OF_WEEK);
            Weekday temp = new Weekday(hour, minute, dayOfWeek, c.getString(0));
            result.put(Long.toString(c.getLong(2)), temp);
        }
        c.close();

        return result;
    }

    /*
        Setting the alarm for lecture related surveys, repeated every week.
        Also used for setting weekly alarms for well-being survey.
     */
    public void setWeeklyAlarms(Context context, int requestCode, String session, Weekday weekday){
        Intent myIntent = new Intent(context, AlarmNotificationReceiver.class);
        myIntent.putExtra("Session", session);
        AlarmManager alarmManager = (AlarmManager) context.getSystemService(Context.ALARM_SERVICE);
        PendingIntent pendingIntent1 = PendingIntent.getBroadcast(context, requestCode, myIntent, PendingIntent.FLAG_ONE_SHOT);
        Calendar calendar1 = createCalendar(weekday.getDay2(),weekday.getHour(), weekday.getMinute());

        //If the time already past, set it for next week
        if (calendar1.getTimeInMillis() > System.currentTimeMillis()) {
            alarmManager.set(AlarmManager.RTC_WAKEUP, calendar1.getTimeInMillis(), pendingIntent1);
        } else {
            calendar1.add(Calendar.DAY_OF_MONTH, 7);
            alarmManager.set(AlarmManager.RTC_WAKEUP, calendar1.getTimeInMillis(), pendingIntent1);
        }
    }

    public void setWeeklyAlarms(Context context, int requestCode, String session, Weekday weekday, String timestamp){
        Intent myIntent = new Intent(context, AlarmNotificationReceiver.class);
        myIntent.putExtra("Session", session);
        String arr[] = {weekday.getDescription(), Integer.toString(weekday.getDay2()), Integer.toString(weekday.getHour()) + ":" + Integer.toString(weekday.getMinute()), timestamp};
        myIntent.putExtra("weekday", arr);
        AlarmManager alarmManager = (AlarmManager) context.getSystemService(Context.ALARM_SERVICE);
        PendingIntent pendingIntent1 = PendingIntent.getBroadcast(context, requestCode, myIntent, PendingIntent.FLAG_ONE_SHOT);
        Calendar calendar1 = createCalendar(weekday.getDay2(),weekday.getHour(), weekday.getMinute());

        //If the time already past, set it for next week
        if (calendar1.getTimeInMillis() > System.currentTimeMillis()) {
            alarmManager.set(AlarmManager.RTC_WAKEUP, calendar1.getTimeInMillis(), pendingIntent1);
        } else {
            calendar1.add(Calendar.DAY_OF_MONTH, 7);
            alarmManager.set(AlarmManager.RTC_WAKEUP, calendar1.getTimeInMillis(), pendingIntent1);
        }
    }

    /*
        Setting the alarm for daily surveys and reminders, repeated every day
     */
    public void setDailyAlarms(Context context, int requestCode, String session, Reminder weekday){
        Intent myIntent = new Intent(context, AlarmNotificationReceiver.class);
        myIntent.putExtra("Session", session);
        AlarmManager alarmManager = (AlarmManager) context.getSystemService(Context.ALARM_SERVICE);
        PendingIntent pendingIntent1 = PendingIntent.getBroadcast(context, requestCode, myIntent, PendingIntent.FLAG_ONE_SHOT);
        Calendar calendar1 = createCalendar2(weekday.getHour(), weekday.getMinute());

        //If the time already past, set it for next day
        if (calendar1.getTimeInMillis() > System.currentTimeMillis()) {
            alarmManager.set(AlarmManager.RTC_WAKEUP, calendar1.getTimeInMillis(), pendingIntent1);
            alarmManager.setRepeating(AlarmManager.RTC_WAKEUP, calendar1.getTimeInMillis(), AlarmManager.INTERVAL_DAY, pendingIntent1);
        } else {
            calendar1.add(Calendar.DAY_OF_MONTH, 1);
            alarmManager.set(AlarmManager.RTC_WAKEUP, calendar1.getTimeInMillis(), pendingIntent1);
            alarmManager.setRepeating(AlarmManager.RTC_WAKEUP, calendar1.getTimeInMillis(), AlarmManager.INTERVAL_DAY, pendingIntent1);
        }
    }
}

