package usi.memotion2personal.Reminders;

/**
 * Created by shkurtagashi on 9/18/18.
 */

public class Reminder {

    public int _HOUR;
    public int _MINUTE;
    String description;

    public Reminder(int h, int m, String desc){
        setHour(h);
        setMinute(m);
        setDescription(desc);
    }

    public void setHour(int hour) {
        this._HOUR = hour;
    }

    public void setMinute(int minute) {
        this._MINUTE = minute;
    }


    public int getHour(){
        return this._HOUR;
    }

    public int getMinute(){
        return this._MINUTE;
    }

    public void setDescription(String description) {
        this.description = description;
    }
    public String getDescription(){
        return this.description;
    }

}


