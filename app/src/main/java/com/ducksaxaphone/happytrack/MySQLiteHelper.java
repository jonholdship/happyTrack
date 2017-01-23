package com.ducksaxaphone.happytrack;

import android.database.sqlite.SQLiteOpenHelper;

/**
 * Created by jrh on 22/01/17.
 */
import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.util.Log;

public class MySQLiteHelper extends SQLiteOpenHelper {

    //name of database, table and columns
    private static final String DATABASE_NAME = "happyData.db";

    public static final String TABLE_NAME = "comments";

    public static final String COLUMN_ID = "_id";
    public static final String COLUMN_DATE = "date";
    public static final String COLUMN_RATING = "rating";
    public static final String COLUMN_NOTES = "notes";

    private static final int DATABASE_VERSION = 1;

    // Database creation sql statement
    private static final String DATABASE_CREATE = "create table "
            + TABLE_NAME + "( "
            + COLUMN_ID + " integer primary key autoincrement, "
            + COLUMN_DATE + " text not null,"
            + COLUMN_RATING + " integer,"
            + COLUMN_NOTES + " text);";

    public MySQLiteHelper(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase database) {
        database.execSQL(DATABASE_CREATE);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        Log.w(MySQLiteHelper.class.getName(),
                "Upgrading database from version " + oldVersion + " to "
                        + newVersion + ", which will destroy all old data");
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_NAME);
        onCreate(db);
    }

}
