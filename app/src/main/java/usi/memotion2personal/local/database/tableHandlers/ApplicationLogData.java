package usi.memotion2personal.local.database.tableHandlers;

import android.content.ContentValues;
import android.database.Cursor;

import usi.memotion2personal.local.database.db.LocalDbUtility;
import usi.memotion2personal.local.database.db.LocalTables;
import usi.memotion2personal.local.database.tables.ApplicationLogsTable;

/**
 * Created by shkurtagashi on 9/20/18.
 */

public class ApplicationLogData extends TableHandler {

    private static LocalTables table = LocalTables.TABLE_APPLICATION_LOGS;

    private long totalTimeInForeground;
    private long firstTimestamp;
    private long lastTimestamp;
    private long lastTimeUsed;
    private String app_package_name;

    public ApplicationLogData(boolean isNewRecord) {
        super(isNewRecord);
        columns = LocalDbUtility.getTableColumns(table);
        id = -1;
    }

    @Override
    public void setAttributes(ContentValues attributes) {
        if(attributes.containsKey(columns[0])) {
            id = attributes.getAsLong(columns[0]);
        }

        if(attributes.containsKey(columns[1])) {
            totalTimeInForeground = attributes.getAsLong(columns[1]);
        }

        if(attributes.containsKey(columns[2])) {
            firstTimestamp = attributes.getAsLong(columns[2]);
        }

        if(attributes.containsKey(columns[3])) {
            lastTimestamp = attributes.getAsLong(columns[3]);
        }

        if(attributes.containsKey(columns[4])) {
            lastTimeUsed = attributes.getAsLong(columns[4]);
        }


        if(attributes.containsKey(columns[5])) {
            app_package_name = attributes.getAsString(columns[5]);
        }
    }

    @Override
    public ContentValues getAttributes() {
        ContentValues attributes = new ContentValues();
        if(id >= 0) {
            attributes.put(columns[0], id);
        }
        attributes.put(columns[1], totalTimeInForeground);
        attributes.put(columns[2], firstTimestamp);
        attributes.put(columns[3], lastTimestamp);
        attributes.put(columns[4], lastTimeUsed);
        attributes.put(columns[5], app_package_name);


        return attributes;
    }


    @Override
    public ContentValues getAttributesFromCursor(Cursor cursor) {
        ContentValues attributes = new ContentValues();
        attributes.put(columns[0], cursor.getLong(0));
        attributes.put(columns[1], cursor.getLong(1));
        attributes.put(columns[2], cursor.getLong(2));
        attributes.put(columns[3], cursor.getLong(3));
        attributes.put(columns[4], cursor.getLong(4));
        attributes.put(columns[5], cursor.getString(5));

        return attributes;
    }

    @Override
    public void save() {
        String tableName = LocalDbUtility.getTableName(table);

        if(isNewRecord) {
            id = localController.insertRecord(tableName, getAttributes());
        } else {
            String columnId = columns[0];
            localController.update(tableName, getAttributes(), columnId + " = " + id);
        }
    }

    @Override
    public void setAttribute(String attributeName, ContentValues attribute) {
        super.setAttribute(attributeName, attribute);

        switch(attributeName) {
            case ApplicationLogsTable.KEY_APPLICATION_ID:
                id = attribute.getAsLong(ApplicationLogsTable.KEY_APPLICATION_ID);
                break;
            case ApplicationLogsTable.KEY_APP_TOTAL_TIME_FOREGROUND:
                totalTimeInForeground = attribute.getAsLong(ApplicationLogsTable.KEY_APP_TOTAL_TIME_FOREGROUND);
                break;
            case ApplicationLogsTable.KEY_APP_FIRST_TIMESTAMP:
                firstTimestamp = attribute.getAsLong(ApplicationLogsTable.KEY_APP_FIRST_TIMESTAMP);
                break;
            case ApplicationLogsTable.KEY_APP_LAST_TIMESTAMP:
                lastTimestamp = attribute.getAsLong(ApplicationLogsTable.KEY_APP_LAST_TIMESTAMP);
                break;
            case ApplicationLogsTable.KEY_APP_LAST_TIME_USED:
                lastTimestamp = attribute.getAsLong(ApplicationLogsTable.KEY_APP_LAST_TIME_USED);
                break;
            case ApplicationLogsTable.KEY_APP_PACKAGE_NAME:
                app_package_name = attribute.getAsString(ApplicationLogsTable.KEY_APP_PACKAGE_NAME);
                break;
        }
    }

    @Override
    public String[] getColumns() {
        return columns;
    }

    @Override
    public String getTableName() {
        return table.getTableName();
    }

    @Override
    public void delete() {
        localController.delete(table.getTableName(), columns[0] + " = " + id);
    }


    @Override
    public String toString() {
        return "Notification(id: " + id +  ", totalTimeForeground: " + totalTimeInForeground + ", first_timestamp: "  + firstTimestamp +
                ", last_timestamp: "  + lastTimestamp + ", lastTimeUsed: "  + lastTimeUsed +
                ", app_package: " + app_package_name + ")\n";
    }


    public long getId(){return this.id;}
    public long getTotalTimeInForeground(){return this.totalTimeInForeground;}
    public long getFirstTimestamp(){return this.firstTimestamp;}
    public long getLastTimestamp(){return this.lastTimestamp;}
    public long getLastTimeUsed(){return this.lastTimeUsed;}
    public String getAppPackageName() {
        return this.app_package_name;
    }

    public void setId(long id){this.id = id;}
    public void setTotalTimeInForeground(long t){this.totalTimeInForeground = t;}
    public void setFirstTimestamp(long t){this.firstTimestamp = t;}
    public void setLastTimestamp(long t){this.lastTimestamp = t;}
    public void setLastTimeUsed(long t){this.lastTimeUsed = t;}
    public void setAppPackage(String app_package) {
        this.app_package_name = app_package;
    }


}
