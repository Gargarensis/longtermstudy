package usi.memotion2personal.Reminders;

/**
 * Created by shkurtagashi on 11.02.17.
 */

public class Weekday {
    public int _HOUR;
    public int _MINUTE;
    public String _DAY;
    public int day;
    String description;

    public Weekday(int h, int m, String d, String desc){
        setHour(h);
        setMinute(m);
        setDay(d);
        setDescription(desc);
    }

    public Weekday(int h, int m, int d, String description){
        setHour(h);
        setMinute(m);
        setDay2(d);
        setDescription(description);
    }

    public void setHour(int hour) {
        this._HOUR = hour;
    }

    public void setMinute(int minute) {
        this._MINUTE = minute;
    }

    public void setDay(String day) {
        this._DAY = day;
    }

    public int getHour(){
        return this._HOUR;
    }

    public int getMinute(){
        return this._MINUTE;
    }

    public String getDay(){
        return this._DAY;
    }

    public int getDay2(){
        return this.day;
    }

    public void setDay2(int day) {
        this.day = day;
    }

    public void setDescription(String description) {
        this.description = description;
    }
    public String getDescription(){
        return this.description;
    }


}
