package com.kicks;



import java.util.Date;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.SQLException;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

public class DBSettings {

	//Alarm Settings
    public static final String KEY_HOUR = "hour";
    public static final String KEY_MINUTE = "minute";
    public static final String KEY_VIBRATE = "vibrate";
    public static final String KEY_SOUND = "sound";
    public static final String KEY_ON = "is_on";
    public static final String KEY_ROWID = "_id";
    public static final String KEY_ALARM_TABLE = "alarm_settings";
    
    //Notification Settings (uses KEY_ON, KEY_SOUND, KEY_VIBRATE)
    public static final String KEY_NOTIFICATION_TABLE = "notification_settings";
    
    
    private static final String TAG = "DBSettings";
    private DatabaseHelper mDbHelper;
    private SQLiteDatabase mDb;

    /**
     * Database creation sql statement
     */
    private static final String DATABASE_CREATE_ALARM_TABLE =
        "create table alarm_settings (_id INTEGER primary key autoincrement, "
        + "hour INTEGER not null, minute INTEGER not null, vibrate INTEGER not null, sound INTEGER not null, is_on INTEGER not null);";
    
    private static final String DATABASE_CREATE_NOTIFICATION_TABLE =
        "create table notification_settings (_id INTEGER primary key autoincrement, "
        + "vibrate INTEGER not null, sound INTEGER not null, is_on INTEGER not null);";
    

    private static final String DATABASE_NAME = "Settings";
    private static final int DATABASE_VERSION = 2;

    private final Context mCtx;
    
    private static class DatabaseHelper extends SQLiteOpenHelper {

        DatabaseHelper(Context context) {
            super(context, DATABASE_NAME, null, DATABASE_VERSION);
        }

        @Override
        public void onCreate(SQLiteDatabase db) {

            db.execSQL(DATABASE_CREATE_ALARM_TABLE);
            db.execSQL(DATABASE_CREATE_NOTIFICATION_TABLE);
        }

        @Override
        public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
            Log.w(TAG, "Upgrading database from version " + oldVersion + " to "
                    + newVersion + ", which will destroy all old data");
            db.execSQL("DROP TABLE IF EXISTS alarm_settings");
            db.execSQL("DROP TABLE IF EXISTS notification_settings");
            onCreate(db);
        }
			
		}
    
    //DBHandler constructor.  Initializes the context.
    public DBSettings(Context ctx){
    	this.mCtx = ctx;
    }
    
    //Open the database, if no database create the database
    public DBSettings open() throws SQLException {
        mDbHelper = new DatabaseHelper(mCtx);
        mDb = mDbHelper.getWritableDatabase();
        return this;
    }
    
    //Close the connection to the database
    public void close() {
        mDbHelper.close();
    }
    
    //Insert a record
    public long createAlarmRecord(int hour, int min, boolean vibrate, boolean sound, boolean on) {
        ContentValues initialValues = new ContentValues();
        initialValues.put(KEY_MINUTE, min);
        initialValues.put(KEY_HOUR, hour);
        initialValues.put(KEY_VIBRATE, vibrate);
        initialValues.put(KEY_SOUND, sound);
        initialValues.put(KEY_ON, on);

        return mDb.insert(KEY_ALARM_TABLE, null, initialValues);
    }
    
    public long createNotificationRecord(boolean vibrate, boolean sound, boolean on) {
        ContentValues initialValues = new ContentValues();
        initialValues.put(KEY_VIBRATE, vibrate);
        initialValues.put(KEY_SOUND, sound);
        initialValues.put(KEY_ON, on);

        return mDb.insert(KEY_NOTIFICATION_TABLE, null, initialValues);
    }
    
    public boolean deleteAlarmRecord(long rowId) {

        return mDb.delete(KEY_ALARM_TABLE, KEY_ROWID + "=" + rowId, null) > 0;
    }
    
    public boolean deleteNotificationRecord(long rowId) {

        return mDb.delete(KEY_NOTIFICATION_TABLE, KEY_ROWID + "=" + rowId, null) > 0;
    }
    
    public boolean deleteAllAlarm() {

        return mDb.delete(KEY_ALARM_TABLE, null, null) > 0;
    }
    
    public boolean deleteAllNotification() {

        return mDb.delete(KEY_NOTIFICATION_TABLE, null, null) > 0;
    }
    
    public Cursor fetchAllAlarmRecords() {

        return mDb.query(KEY_ALARM_TABLE, new String[] {KEY_ROWID, KEY_MINUTE,
                KEY_HOUR, KEY_VIBRATE, KEY_SOUND, KEY_ON	}, null, null, null, null, KEY_ROWID);
    }
    
    public Cursor fetchAllNotificationRecords() {

        return mDb.query(KEY_NOTIFICATION_TABLE, new String[] {KEY_ROWID, KEY_MINUTE,
                KEY_HOUR, KEY_VIBRATE, KEY_SOUND, KEY_ON	}, null, null, null, null, KEY_ROWID);
    }
    

}

