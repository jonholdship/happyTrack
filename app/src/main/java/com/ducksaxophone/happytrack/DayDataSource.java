package com.ducksaxophone.happytrack;

/**
 * Created by jrh on 22/01/17.
 */

import java.util.ArrayList;
import java.util.List;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.SQLException;
import android.database.sqlite.SQLiteDatabase;

public class DayDataSource {

    // Database fields
    private SQLiteDatabase database;
    private MySQLiteHelper dbHelper;
    private String[] allColumns = { MySQLiteHelper.COLUMN_ID,
            MySQLiteHelper.COLUMN_DATE, MySQLiteHelper.COLUMN_RATING, MySQLiteHelper.COLUMN_NOTES };

    public DayDataSource(Context context) {
        dbHelper = new MySQLiteHelper(context);
    }

    public void open() throws SQLException {
        database = dbHelper.getWritableDatabase();
    }

    public void close() {
        dbHelper.close();
    }

    /* class adds days to database*/
    public Day createDay(String date) {
        ContentValues values = new ContentValues();
        values.put(MySQLiteHelper.COLUMN_DATE, date);
        values.put(MySQLiteHelper.COLUMN_NOTES, "");
        values.put(MySQLiteHelper.COLUMN_RATING, 5);
        long insertId = database.insert(MySQLiteHelper.TABLE_NAME, null,
                values);
        Cursor cursor = database.query(MySQLiteHelper.TABLE_NAME,
                allColumns, MySQLiteHelper.COLUMN_ID + " = " + insertId, null,
                null, null, null);
        cursor.moveToFirst();
        Day newDay = cursorToDay(cursor);
        cursor.close();
        return newDay;
    }

    public void deleteDay(Day day) {
        long id = day.getId();
        System.out.println("Day deleted with id: " + id);
        database.delete(MySQLiteHelper.TABLE_NAME, MySQLiteHelper.COLUMN_ID
                + " = " + id, null);
    }

    public void updateDay(Day day){
        // define the new value you want
        ContentValues newValues = new ContentValues();
        newValues.put(MySQLiteHelper.COLUMN_DATE, day.getDate());
        newValues.put(MySQLiteHelper.COLUMN_NOTES, day.getNotes());
        newValues.put(MySQLiteHelper.COLUMN_RATING, day.getRating());
        // you can .put() even more here if you want to update more than 1 row

        // define the WHERE clause w/o the WHERE and replace variables by ?
        // Note: there are no ' ' around ? - they are added automatically
        String whereClause = MySQLiteHelper.COLUMN_ID+" == ?";

        // now define what those ? should be
        String[] whereArgs = new String[] {
                // in order the ? appear
                String.valueOf(day.getId())
        };

        int amountOfUpdatedColumns = database.update(MySQLiteHelper.TABLE_NAME, newValues, whereClause, whereArgs);
    }

    public Day getDay(String date){
        Day day;
        Cursor cursor=database.rawQuery("select * from "+MySQLiteHelper.TABLE_NAME+" where "+MySQLiteHelper.COLUMN_DATE+"='"+date+"'",null);
        cursor.moveToFirst();
        if (cursor.isBeforeFirst()) {
            day = createDay(date);
        }
        else{
            day = cursorToDay(cursor);
        }
        return day;
    }

    public List<Day> getAllDays() {
        List<Day> comments = new ArrayList<Day>();

        Cursor cursor = database.query(MySQLiteHelper.TABLE_NAME,
                allColumns, null, null, null, null, null);

        cursor.moveToFirst();
        while (!cursor.isAfterLast()) {
            Day day = cursorToDay(cursor);
            comments.add(day);
            cursor.moveToNext();
        }
        // make sure to close the cursor
        cursor.close();
        return comments;
    }

    private Day cursorToDay(Cursor cursor) {
        Day day = new Day();
        day.setId(cursor.getLong(0));
        day.setDate(cursor.getString(1));
        day.setRating(cursor.getInt(2));
        day.setNotes(cursor.getString(3));
        return day;
    }
}
