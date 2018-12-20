package usi.memotion2personal.local.database.tableHandlers;

import android.content.ContentValues;
import android.database.Cursor;

import java.text.SimpleDateFormat;

import usi.memotion2personal.local.database.db.LocalTables;

/**
 * Created by shkurtagashi on 17.01.17.
 */

public class User extends TableHandler {
    public String _android_id;
    public String _username;
    public String _empaticaID;
    public String _emailAdress;
    public String _gender;
    public String _age;
    public String _status;
    public String switch_token;
    public String switch_password;



    public void setAndroidId(String id) {
        this._android_id = id;
    }

    public String getAndroidId(){
        return this._android_id;
    }

    public void setGender(String gender) {
        this._gender = gender;
    }

    public String getGender(){
        return this._gender;
    }

    public void setAge(String age) {
        this._age = age;
    }

    public String getAge(){
        return this._age;
    }

    public void setStatus(String status) {
        this._status = status;
    }

    public String getStatus() {
        return this._status;
    }

    public void setUsername(String username) {
        this._username = username;
    }

    public String getUsername(){
        return this._username;
    }

    public void setEmpaticaID(String empaticaID) {
        this._empaticaID = empaticaID;
    }

    public String getEmpaticaID(){
        return this._empaticaID;
    }

    public void setEmail(String email) {
        this._emailAdress = email;
    }

    public String getEmail(){
        return this._emailAdress;
    }

    public void setSwitch_token(String token){this.switch_token = token;}

    public String getSwitch_token(){return this.switch_token;}

    public void setSwitch_password(String pwd){this.switch_password = pwd;};

    public String getSwitch_password(){return this.switch_password;}

    private static LocalTables table = LocalTables.TABLE_USER;

    public User(boolean isNewRecord) {
        super(isNewRecord);
        id = -1;
    }

    @Override
    public void setAttributes(ContentValues attributes) {

    }

    @Override
    public ContentValues getAttributes() {
        return null;
    }

    @Override
    public ContentValues getAttributesFromCursor(Cursor cursor) {
        return null;
    }

    @Override
    public void save() {

    }

    @Override
    public String[] getColumns() {
        return new String[0];
    }

    @Override
    public String getTableName() {
        return null;
    }

    @Override
    public void delete() {

    }

    public static String getCreationDate() {
        Cursor user = localController.rawQuery("SELECT * FROM " + table.getTableName(), null);

        if(user.getCount() > 0) {
            user.moveToFirst();

            int time = user.getInt(9);

            SimpleDateFormat format = new SimpleDateFormat("dd-MM-yyyy");
//            DateTime d = new DateTime(time * 1000L);
//
//
//            DateTimeFormatter dtfOut = DateTimeFormat.forPattern("dd-MM-yyyy");
            return format.format(time * 1000L);
        }

        return null;
    }

    public static String getEndStudyDate() {
        Cursor user = localController.rawQuery("SELECT * FROM " + table.getTableName(), null);

        if(user.getCount() > 0) {
            user.moveToFirst();
            return user.getString(10);
        }

        return null;
    }

    public static boolean isEnrolled() {
        Cursor user = localController.rawQuery("SELECT * FROM " + table.getTableName(), null);

        if(user.getCount() > 0) {
            user.moveToFirst();
            return user.getInt(2) == 0 ? false : true;
        }

        return false;
    }
}
