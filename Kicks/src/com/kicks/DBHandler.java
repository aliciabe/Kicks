package com.kicks;



import java.util.Date;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.SQLException;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

public class DBHandler {

    public static final String KEY_DAY = "day";
    public static final String KEY_TIME = "time";
    public static final String KEY_ROWID = "_id";
    
    private static final String TAG = "DBHandler";
    private DatabaseHelper mDbHelper;
    private SQLiteDatabase mDb;

    /**
     * Database creation sql statement
     */
    private static final String DATABASE_CREATE_FIRST =
        "create table first_try (_id INTEGER primary key autoincrement, "
        + "day TEXT not null, time INTEGER not null);";
    
    private static final String DATABASE_CREATE_SECOND =
        "create table second_try (_id INTEGER primary key autoincrement, "
        + "day TEXT not null, time INTEGER not null);";

    private static final String DATABASE_NAME = "data";
    private static final int DATABASE_VERSION = 2;

    private final Context mCtx;
    
    private static class DatabaseHelper extends SQLiteOpenHelper {

        DatabaseHelper(Context context) {
            super(context, DATABASE_NAME, null, DATABASE_VERSION);
        }

        @Override
        public void onCreate(SQLiteDatabase db) {

            db.execSQL(DATABASE_CREATE_FIRST);
            db.execSQL(DATABASE_CREATE_SECOND);
        }

        @Override
        public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
            Log.w(TAG, "Upgrading database from version " + oldVersion + " to "
                    + newVersion + ", which will destroy all old data");
            db.execSQL("DROP TABLE IF EXISTS first_try");
            db.execSQL("DROP TABLE IF EXISTS second_try");
            onCreate(db);
        }
			
		}
    
    //DBHandler constructor.  Initializest the context.
    public DBHandler(Context ctx){
    	this.mCtx = ctx;
    }
    
    //Open the database, if no database create the database
    public DBHandler open() throws SQLException {
        mDbHelper = new DatabaseHelper(mCtx);
        mDb = mDbHelper.getWritableDatabase();
        return this;
    }
    
    //Close the connection to the database
    public void close() {
        mDbHelper.close();
    }
    
    //Insert a record
    public long createRecord(String day, int time, String table) {
        ContentValues initialValues = new ContentValues();
        initialValues.put(KEY_DAY, day);
        initialValues.put(KEY_TIME, time);

        return mDb.insert(table, null, initialValues);
    }
    
    public boolean deleteRecord(long rowId, String table) {

        return mDb.delete(table, KEY_ROWID + "=" + rowId, null) > 0;
    }
    
    public boolean deleteAll(String table) {

        return mDb.delete(table, null, null) > 0;
    }
    
    public Cursor fetchAllRecords(String table) {

        return mDb.query(table, new String[] {KEY_ROWID, KEY_DAY,
                KEY_TIME}, null, null, null, null, null);
    }
    

}
