package usi.memotion2personal.remote.database.upload;

import android.content.ContentValues;
import android.database.Cursor;
import android.util.Log;

import usi.memotion2personal.local.database.db.LocalSQLiteDBHelper;
import usi.memotion2personal.remote.database.controllers.SwitchDriveController;
import usi.memotion2personal.local.database.controllers.LocalStorageController;
import usi.memotion2personal.local.database.db.LocalDbUtility;
import usi.memotion2personal.local.database.db.LocalTables;
import usi.memotion2personal.local.database.tables.UploaderUtilityTable;
import usi.memotion2personal.remote.database.RemoteStorageController;
import java.util.*;
import java.text.SimpleDateFormat;


/**
 * Created by usi on 19/01/17.
 */

public class Uploader implements SwitchDriveController.OnTransferCompleted {
    private RemoteStorageController remoteController;
    private LocalStorageController localController;
    private LocalTables tableToClean;
    private long uploadThreshold;
    private String userId;
    private HashMap<String, TableInfo> map;
    LocalSQLiteDBHelper dbHelper;

    public Uploader(String userId, RemoteStorageController remoteController, LocalStorageController localController, long uploadThreshold) {
        this.remoteController = remoteController;
        ((SwitchDriveController) remoteController).setCallback(this);
        this.localController = localController;
        this.uploadThreshold  = uploadThreshold;
        //the start table to clean
        tableToClean = LocalTables.values()[0];
        //user id is the phone id
        this.userId = userId;

        Cursor c = localController.rawQuery("SELECT * FROM uploader_utility", null);
        c.moveToFirst();
        map = new HashMap<>();
//        Log.d("DATA UPLOAD INIT", c.getString(1) + " " + c.getString(2) + " " + c.getInt(3) + " " + c.getString(4));

    }

    public Uploader(String userId, RemoteStorageController remoteController, LocalStorageController localController, LocalSQLiteDBHelper dbHelper) {
        this.remoteController = remoteController;
        this.localController = localController;
        //the start table to clean
        tableToClean = LocalTables.values()[0];
        //user id is the phone id
        this.userId = userId;
        this.dbHelper = dbHelper;
    }

    /**
     * Upload
     */
    public void upload() {

        //upload only when the uploadThreshold is reached
        Log.d("UPLOADER SIZE", "" + localController.getDbSize());
        if(localController.getDbSize() > uploadThreshold) {
            Log.d("DATA UPLOAD SERVICE", "START CLEANING...");

            //if a new day starts, we need to clean the file part array, so that it restart from 0
            String today = buildDate();
            if(!checkDate(today)) {
                cleanFileParts();
                updateDate(today);
            }

            //clean all tables
            LocalTables[] a = LocalTables.values();
            for(LocalTables t: LocalTables.values()) {
                processTable(t);
            }
        }
    }

    /**
     * This method uploads only the tables that need to be uploaded at the end of the day
     */
    public void dailyUpload() {

        processTable(LocalTables.TABLE_LECTURE_SURVEY);
        processTable(LocalTables.TABLE_APPLICATION_LOGS);
        processTable(LocalTables.TABLE_NOTIFICATIONS);
        processTable(LocalTables.TABLE_PHONELOCK);
        processTable(LocalTables.TABLE_CALL_LOG);
        processTable(LocalTables.TABLE_SMS);
        processTable(LocalTables.TABLE_LOCATION);
        processTable(LocalTables.TABLE_PAM_SURVEY);
        processTable(LocalTables.TABLE_SIMPLE_MOOD);
        processTable(LocalTables.TABLE_ACTIVITY_RECOGNITION);
        processTable(LocalTables.TABLE_EDIARY_TABLE);
        processTable(LocalTables.TABLE_STRESS_SURVEY);
        processTable(LocalTables.TABLE_SLEEP_QUALITY);
        processTable(LocalTables.TABLE_OVERALL_SURVEY);
        processTable(LocalTables.TABLE_FATIGUE_SURVEY);
        processTable(LocalTables.TABLE_PRODUCTIVITY_SURVEY);

    }

    /**
     * This method uploads only the tables that need to be uploaded after account creation and surveys completion
     */
    public void oneTimeUpload() {
        processTable(LocalTables.TABLE_USER);
        processTable(LocalTables.TABLE_SWLS);
        processTable(LocalTables.TABLE_PERSONALITY);
        processTable(LocalTables.TABLE_PSSS);
        processTable(LocalTables.TABLE_PSQI);
    }


    public void weeklyUpload() {
        processTable(LocalTables.TABLE_WEEKLY_SURVEY);
    }

    private void processTable(LocalTables table) {

        Log.d("DATA UPLOAD SERVICE", "Processing table " + LocalDbUtility.getTableName(table));

        if(table == LocalTables.TABLE_USER || table == LocalTables.TABLE_PERSONALITY || table == LocalTables.TABLE_PSQI
                || table == LocalTables.TABLE_PSSS || table == LocalTables.TABLE_SWLS || table == LocalTables.TABLE_WEEKLY_SURVEY){
            // Upload only once, DO NOT delete

            String query = getQuery(table);
            Cursor records = localController.rawQuery(query, null);

            if (records.getCount() > 0) {
                String fileName = buildFileName(table);
                Log.d("DATA UPLOAD SERVICE", "Preparing the data to upload for table " + LocalDbUtility.getTableName(table));
                remoteController.upload(fileName, toCSV(records, table));
                Log.d("DATA UPLOAD SERVICE", "Upload request sent for table " + LocalDbUtility.getTableName(table));

            }else {
                Log.d("DATA UPLOAD SERVICE", "Table is empty, nothing to upload");
            }

        }else if(table == LocalTables.TABLE_LECTURE_SURVEY || table == LocalTables.TABLE_NOTIFICATIONS || table == LocalTables.TABLE_LOCATION ||
                table == LocalTables.TABLE_PHONELOCK || table == LocalTables.TABLE_CALL_LOG || table == LocalTables.TABLE_SMS ||
                table == LocalTables.TABLE_APPLICATION_LOGS || table == LocalTables.TABLE_ACTIVITY_RECOGNITION || table == LocalTables.TABLE_EDIARY_TABLE
                || table == LocalTables.TABLE_SIMPLE_MOOD || table == LocalTables.TABLE_FATIGUE_SURVEY || table == LocalTables.TABLE_OVERALL_SURVEY
                || table == LocalTables.TABLE_SLEEP_QUALITY || table == LocalTables.TABLE_PRODUCTIVITY_SURVEY || table == LocalTables.TABLE_STRESS_SURVEY
                || table  == LocalTables.TABLE_PAM_SURVEY) {
            //Here we should add also Daily Surveys
            // Upload 1 time per day, delete
            String query = getQuery(table);

            Cursor records = localController.rawQuery(query, null);

            if (records.getCount() > 0) {
                String fileName = buildFileName(table);
                Log.d("DATA UPLOAD SERVICE", "Preparing the data to upload for table " + LocalDbUtility.getTableName(table));
                remoteController.upload(fileName, toCSV(records, table));
                Log.d("DATA UPLOAD SERVICE", "Upload request sent for table " + LocalDbUtility.getTableName(table));
            } else {
                Log.d("DATA UPLOAD SERVICE", "Table is empty, nothing to upload");
            }
            records.close();
        }
    }

    private String getQuery(LocalTables table) {
        String[] columns = LocalDbUtility.getTableColumns(table);
        return "SELECT * FROM " + LocalDbUtility.getTableName(table)
                + " WHERE " + columns[0] + " > " + Integer.toString(getRecordId(table));

    }

    private boolean checkDate(String date) {
        Cursor c = localController.rawQuery("SELECT * FROM " + UploaderUtilityTable.TABLE_UPLOADER_UTILITY + " WHERE " + UploaderUtilityTable.KEY_UPLOADER_UTILITY_ID + " = 0", null);
        c.moveToNext();

        String d = c.getString(2);

        return date.equals(d);
    }

    /**
     * Utility function to increment the part id for the given table.
     *
     * @param table
     */
    private void incrementFilePartId(LocalTables table) {
        int part = getFilePartId(table);
        Log.d("DATA UPLOAD SERVICE", "INCREASING PART COUNT " + Integer.toString(part+1));

        ContentValues val = new ContentValues();
        val.put(UploaderUtilityTable.KEY_UPLOADER_UTILITY_FILE_PART, part+1);
        String clause = UploaderUtilityTable.KEY_UPLOADER_UTILITY_TABLE + " = \"" + LocalDbUtility.getTableName(table)  + "\"";

        localController.update(UploaderUtilityTable.TABLE_UPLOADER_UTILITY, val, clause);
    }

    /**
     * Utility function to update the record id in the given table.
     *
     * @param table
     * @param recordId
     */
    private void updateRecordId(LocalTables table, int recordId) {
        String clause = UploaderUtilityTable.KEY_UPLOADER_UTILITY_TABLE + " = \"" + LocalDbUtility.getTableName(table) + "\"";
        ContentValues val = new ContentValues();
        val.put(UploaderUtilityTable.KEY_UPLOADER_UTILITY_RECORD_ID, recordId);

        localController.update(UploaderUtilityTable.TABLE_UPLOADER_UTILITY, val, clause);
    }

    /**
     * Utility function to update the date of all tables.
     *
     * @param date
     */
    private void updateDate(String date) {
        String clause;
        ContentValues val;
        for(int i = 0; i < LocalTables.values().length; i++) {
            val = new ContentValues();
            val.put(UploaderUtilityTable.KEY_UPLOADER_UTILITY_DATE, date);
            clause = UploaderUtilityTable.KEY_UPLOADER_UTILITY_TABLE + " = \"" + LocalDbUtility.getTableName(LocalTables.values()[i]) + "\"";

            localController.update(UploaderUtilityTable.TABLE_UPLOADER_UTILITY, val, clause);
        }
    }

    /**
     * Utility function to get the file part of the given table.
     *
     * @param table
     * @return
     */
    private int getFilePartId(LocalTables table) {
        Cursor c = localController.rawQuery("SELECT * FROM " + UploaderUtilityTable.TABLE_UPLOADER_UTILITY + " WHERE " + UploaderUtilityTable.KEY_UPLOADER_UTILITY_TABLE + " = \"" + LocalDbUtility.getTableName(table) + "\"", null);
        c.moveToNext();
        return c.getInt(4);
    }

    private int getRecordId(LocalTables table) {
        Cursor c = localController.rawQuery("SELECT * FROM " + UploaderUtilityTable.TABLE_UPLOADER_UTILITY + " WHERE " + UploaderUtilityTable.KEY_UPLOADER_UTILITY_TABLE + " = \"" + LocalDbUtility.getTableName(table) + "\"", null);
        c.moveToNext();
        return c.getInt(3);
    }

    /**
     * Utility function to clean the file part of all tables.
     */
    private void cleanFileParts() {
        String clause;
        ContentValues val;
        for(int i = 0; i < LocalTables.values().length; i++) {
            clause = UploaderUtilityTable.KEY_UPLOADER_UTILITY_TABLE + " = \"" + LocalDbUtility.getTableName(LocalTables.values()[i]) + "\"";
            val = new ContentValues();
            val.put(UploaderUtilityTable.KEY_UPLOADER_UTILITY_FILE_PART, 0);
            localController.update(UploaderUtilityTable.TABLE_UPLOADER_UTILITY, val, clause);
        }
    }

    /**
     * Build the query to remove the records from the given table, where primary key id in ]start, end].
     *
     * @param table
     * @param start
     * @param end
     */
    private void removeRecords(LocalTables table, int start, int end) {
        Log.d("UPLOAD DATA SERVICE", "Removing from " + Integer.toString(start) + " to " + Integer.toString(end));

        String clause = LocalDbUtility.getTableColumns(table)[0] + " > " + Integer.toString(start) + " AND " +
                LocalDbUtility.getTableColumns(table)[0] + " <= " + Integer.toString(end);
        localController.delete(LocalDbUtility.getTableName(table), clause);
    }

//    /**
//     * Build the query to select all records from the given table.
//     *
//     * @param table
//     * @return
//     */
//    private Cursor getRecords(LocalTables table) {
//        String query = "SELECT * FROM " + LocalDbUtility.getTableName(table) +
//                " WHERE " + LocalDbUtility.getTableColumns(table)[0] + " > " + Integer.toString(getRecordId(table));
//        return localController.rawQuery(query, null);
//    }

    /**
     * Build the file name.
     *
     * <subjectid>_<date>_<table>_part<nbPart>.csv
     *
     * @param table
     * @return
     */
    private String buildFileName(LocalTables table) {
        //get current date
        String today = buildDate();
        return userId + "_" + today + "_" + LocalDbUtility.getTableName(table) + ".csv";
    }



    /**
     * Utility function to get the string representation of the today date.
     *
     * @return
     */
    private String buildDate() {
        Calendar calendar = Calendar.getInstance();
        SimpleDateFormat mdformat = new SimpleDateFormat("MM-dd-yyyy");
        return mdformat.format(calendar.getTime());
    }

    /**
     * Generate the csv data from the given cursor.
     *
     * @param records
     * @param table
     * @return
     */
    private String toCSV(Cursor records, LocalTables table) {
        String csv = "";
        String[] columns = LocalDbUtility.getTableColumns(table);

        if(records.getCount() == 1){
            for(int i = 0; i < columns.length; i++) {
                csv += columns[i] + ",";
            }

            records.moveToFirst();
            csv = csv.substring(0, csv.length()-1);
            csv += "\n";

            for(int i = 0; i < columns.length; i++) {
                csv += records.getString(i) + ",";
            }
            csv = csv.substring(0, csv.length()-1);

            Log.d("UPLOADER","Table with only one column: " + table.getTableName());
        }else{
            for(int i = 0; i < columns.length; i++) {
                csv += columns[i] + ",";
            }

            csv = csv.substring(0, csv.length()-1);
            csv += "\n";

            records.moveToFirst();
            do {
                for(int i = 0; i < columns.length; i++) {
                    csv += records.getString(i) + ",";
                }
                csv = csv.substring(0, csv.length()-1);
                csv += "\n";
            } while(records.moveToNext());
            csv = csv.substring(0, csv.length()-1);
        }
        return csv;
    }


//    /**
//     * Generate the csv data from the given cursor.
//     *
//     * @param records
//     * @param table
//     * @return
//     */
//    private String toCSV(Cursor records, DailyTables table) {
//        String csv = "";
//        String[] columns = LocalDbUtility.getDailyTableColumns(table);
//
//        for(int i = 0; i < columns.length; i++) {
//            csv += columns[i] + ",";
//        }
//
//        csv = csv.substring(0, csv.length()-1);
//        csv += "\n";
//
//        do {
//            for(int i = 0; i < columns.length; i++) {
//                csv += records.getString(i) + ",";
//            }
//            csv = csv.substring(0, csv.length()-1);
//            csv += "\n";
//        } while(records.moveToNext());
//        csv = csv.substring(0, csv.length()-1);
//        return csv;
//    }

    @Override
    public void onTransferCompleted(String fileName, int status) {
        TableInfo info = map.get(fileName);
        Log.d("UPLOADER", "Got transfer event for file " + fileName);

        if(fileName.contains("usersTable")){
            if(status >= 200 && status <= 207) {
                Log.d("UPLOADER", "Got transfer event for file " + fileName);
            } else {
                Log.d("DATA UPLOAD SERVICE", "Something went wrong, Owncould's response: " + Integer.toString(status));
            }
        }else if(info != null && info.isSurvey) {
            if(status >= 200 && status <= 207) {
                Log.d("UPLOADER", "Got transfer event for file " + fileName);
//                markSurveys(info.surveysId);
            } else {
                Log.d("DATA UPLOAD SERVICE", "Something went wrong, Owncould's response: " + Integer.toString(status));
            }
        }else {
            if(status >= 200 && status <= 207) {
                //delete from the db the records where id > startId and id <= endId
                if(info.table != LocalTables.TABLE_SIMPLE_MOOD) {
                    removeRecords(info.table, info.startId, info.endId);
                }

                incrementFilePartId(info.table);
                updateRecordId(info.table, info.endId);
            } else {
                Log.d("DATA UPLOAD SERVICE", "Something went wrong, Owncould's response: " + Integer.toString(status));
            }
        }

        map.remove(fileName);
    }

    private class TableInfo {
        public boolean isSurvey;
//        public SurveyType survey;
        public LocalTables table;
        public int startId;
        public int endId;
        public List<Long> surveysId;
    }
}


