package usi.memotion2personal.local.database.tableHandlers;

import android.content.ContentValues;
import android.database.Cursor;
import usi.memotion2personal.local.database.db.LocalDbUtility;
import usi.memotion2personal.local.database.db.LocalTables;
import usi.memotion2personal.local.database.tables.NotificationsTable;


/**
 * Created by shkurtagashi on  9/20/18.
 */

public class NotificationData extends TableHandler
{
	private static LocalTables table = LocalTables.TABLE_NOTIFICATIONS;

	private String tag;
	private String key;
	private int priority;
	private String title;
	private long arrivalTime;
	private long removalTime;
	private int clicked;
//	private boolean led;
//	private boolean vibrate;
//	private boolean sound;
//	private boolean unique_sound;
	private String app_name; 
	private String app_package;

	public NotificationData(boolean isNewRecord) {
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
			tag = attributes.getAsString(columns[1]);
		}

		if(attributes.containsKey(columns[2])) {
			key = attributes.getAsString(columns[2]);
		}

		if(attributes.containsKey(columns[3])) {
			priority = attributes.getAsInteger(columns[3]);
		}

		if(attributes.containsKey(columns[4])) {
			title = attributes.getAsString(columns[4]);
		}

		if(attributes.containsKey(columns[5])) {
			arrivalTime = attributes.getAsLong(columns[5]);
		}

		if(attributes.containsKey(columns[6])) {
			removalTime = attributes.getAsLong(columns[6]);
		}


		if(attributes.containsKey(columns[7])) {
			clicked = attributes.getAsInteger(columns[7]);
		}

//		if(attributes.containsKey(columns[8])) {
//			led = attributes.getAsBoolean(columns[8]);
//		}
//
//		if(attributes.containsKey(columns[9])) {
//			vibrate = attributes.getAsBoolean(columns[9]);
//		}
//
//		if(attributes.containsKey(columns[10])) {
//			sound = attributes.getAsBoolean(columns[10]);
//		}
//
//		if(attributes.containsKey(columns[11])) {
//			unique_sound = attributes.getAsBoolean(columns[11]);
//		}

		if(attributes.containsKey(columns[8])) {
			app_name = attributes.getAsString(columns[8]);
		}

		if(attributes.containsKey(columns[9])) {
			app_package = attributes.getAsString(columns[9]);
		}
	}

	@Override
	public ContentValues getAttributes() {
		ContentValues attributes = new ContentValues();
		if(id >= 0) {
			attributes.put(columns[0], id);
		}
		attributes.put(columns[1], tag);
		attributes.put(columns[2], key);
		attributes.put(columns[3], priority);
		attributes.put(columns[4], title);
		attributes.put(columns[5], arrivalTime);
		attributes.put(columns[6], removalTime);
		attributes.put(columns[7], clicked);
//		attributes.put(columns[8], led);
//		attributes.put(columns[9], vibrate);
//		attributes.put(columns[10], sound);
//		attributes.put(columns[11], unique_sound);
		attributes.put(columns[8], app_name);
		attributes.put(columns[9], app_package);


		return attributes;
	}


	@Override
	public ContentValues getAttributesFromCursor(Cursor cursor) {
		ContentValues attributes = new ContentValues();
		attributes.put(columns[0], cursor.getLong(0));
		attributes.put(columns[1], cursor.getString(1));
		attributes.put(columns[2], cursor.getString(2));
		attributes.put(columns[3], cursor.getInt(3));
		attributes.put(columns[4], cursor.getString(4));
		attributes.put(columns[5], cursor.getLong(5));
		attributes.put(columns[6], cursor.getLong(6));
		attributes.put(columns[7], cursor.getInt(7));
//		attributes.put(columns[8], cursor.getInt(8));
//		attributes.put(columns[9], cursor.getInt(9));
//		attributes.put(columns[10], cursor.getInt(10));
//		attributes.put(columns[11], cursor.getInt(11));
		attributes.put(columns[8], cursor.getString(8));
		attributes.put(columns[9], cursor.getString(9));


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
			case NotificationsTable.KEY_NOTIF_ID:
				id = attribute.getAsLong(NotificationsTable.KEY_NOTIF_ID);
				break;
			case NotificationsTable.KEY_NOTIF_TAG:
				tag = attribute.getAsString(NotificationsTable.KEY_NOTIF_TAG);
				break;
			case NotificationsTable.KEY_NOTIF_KEY:
				key = attribute.getAsString(NotificationsTable.KEY_NOTIF_KEY);
				break;
			case NotificationsTable.KEY_NOTIF_PRIORITY:
				priority = attribute.getAsInteger(NotificationsTable.KEY_NOTIF_PRIORITY);
				break;
			case NotificationsTable.KEY_NOTIF_TITLE:
				title = attribute.getAsString(NotificationsTable.KEY_NOTIF_TITLE);
				break;
			case NotificationsTable.KEY_NOTIF_ARRIVAL_TIME:
				arrivalTime = attribute.getAsLong(NotificationsTable.KEY_NOTIF_ARRIVAL_TIME);
				break;
			case NotificationsTable.KEY_NOTIF_REMOVAL_TIME:
				removalTime = attribute.getAsLong(NotificationsTable.KEY_NOTIF_REMOVAL_TIME);
				break;
			case NotificationsTable.KEY_NOTIF_CLICKED:
				clicked = attribute.getAsInteger(NotificationsTable.KEY_NOTIF_CLICKED);
				break;
//			case NotificationsTable.KEY_NOTIF_LED:
//				led = attribute.getAsBoolean(NotificationsTable.KEY_NOTIF_LED);
//				break;
//			case NotificationsTable.KEY_NOTIF_VIBRATE:
//				vibrate = attribute.getAsBoolean(NotificationsTable.KEY_NOTIF_VIBRATE);
//				break;
//			case NotificationsTable.KEY_NOTIF_SOUND:
//				sound = attribute.getAsBoolean(NotificationsTable.KEY_NOTIF_SOUND);
//				break;
//			case NotificationsTable.KEY_NOTIF_UNIQUE_SOUND:
//				unique_sound = attribute.getAsBoolean(NotificationsTable.KEY_NOTIF_UNIQUE_SOUND);
//				break;
			case NotificationsTable.KEY_NOTIF_APP_NAME:
				app_name = attribute.getAsString(NotificationsTable.KEY_NOTIF_APP_NAME);
				break;
			case NotificationsTable.KEY_NOTIF_APP_PACKAGE:
				app_package = attribute.getAsString(NotificationsTable.KEY_NOTIF_APP_PACKAGE);
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
		return "Notification(id: " + id +  ", tag: " + tag + ", key: "  + key + ", priority: " + priority + ", title: " + title +
				", arrivalTime: " + arrivalTime + ", removalTime: " + removalTime + ", clicked: " + clicked + ", app_name: " + app_name +
				", app_package: " + app_package + ")\n";
	}



	public String getTag() {
		return tag;
	}
	public String getKey() {
		return key;
	}
	public int getPriority() {
		return priority;
	}
	public String getTitle() {
		return title;
	}
	public long getArrivalTime() {
		return arrivalTime;
	}
	public long getRemovalTime() {
		return removalTime;
	}


	/**
	 * 
	 * @return (int) -1 refers to click from other device, 0 for dismissal, and 1 for acceptance
	 */
	public int getClicked() {
		return clicked;
	}

//
//	public boolean isLed() {
//		return led;
//	}
//
//	public boolean isVibrate() {
//		return vibrate;
//	}
//	public boolean isSound() {
//		return sound;
//	}
//	public boolean isUnique_sound() {
//		return unique_sound;
//	}
	public String getAppName() {
		return app_name;
	}
	public String getAppPackageName() {
		return app_package;
	}


	public void setTag(String tag) {
		this.tag = tag;
	}
	public void setKey(String key) {
		this.key = key;
	}
	public void setPriority(int priority) {
		this.priority = priority;
	}
	public void setTitle(String title) {
		this.title = title;
	}
	public void setArrivalTime(long arrivalTime) {
		this.arrivalTime = arrivalTime;
	}
	public void setRemovalTime(long removalTime) {
		this.removalTime = removalTime;
	}
	public void setClicked(int clicked) {
		this.clicked = clicked;
	}

//	public void setLed(boolean led) {
//		this.led = led;
//	}
//	public void setVibrate(boolean vibrate) {
//		this.vibrate = vibrate;
//	}
//	public void setSound(boolean sound) {
//		this.sound = sound;
//	}
//	public void setUnique_sound(boolean unique_sound) {
//		this.unique_sound = unique_sound;
//	}
	public void setAppName(String app_name) {
		this.app_name = app_name;
	}
	public void setAppPackage(String app_package) {
		this.app_package = app_package;
	}


	public long getId(){return this.id;}
	public void setId(long id){this.id = id;}


}
