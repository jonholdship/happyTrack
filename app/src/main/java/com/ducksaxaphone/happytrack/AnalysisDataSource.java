package com.ducksaxaphone.happytrack;

import android.content.Context;
import android.database.Cursor;
import android.database.SQLException;
import android.database.sqlite.SQLiteDatabase;

import com.github.mikephil.charting.data.BarEntry;
import com.github.mikephil.charting.data.Entry;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Collections;
import java.util.Comparator;
import java.util.Date;
import java.util.List;

/**
 * Created by jrh on 05/02/17.
 */

public class AnalysisDataSource {


    // Database fields
    private SQLiteDatabase database;
    private MySQLiteHelper dbHelper;
    private String[] allColumns = { MySQLiteHelper.COLUMN_ID,
            MySQLiteHelper.COLUMN_DATE, MySQLiteHelper.COLUMN_RATING, MySQLiteHelper.COLUMN_NOTES };

    public AnalysisDataSource(Context context) {
        dbHelper = new MySQLiteHelper(context);
    }

    public void open() throws SQLException {
        database = dbHelper.getReadableDatabase();
    }

    public void close() {
        dbHelper.close();
    }

    //method for getting week graph data by looping over whole database and averaging each day's
    //rating. Returns list of Entries which are x,y pairs for graph.
    public List<Float> getWeekSummary(){
        //output list
        List<Float> dayPoints = new ArrayList<>();
        int[] dayRatings,dayCounts;
        dayRatings=new int[7];
        dayCounts=new int[7];


        //get  database cursor with everything
        Cursor cursor=database.rawQuery("select * from "+MySQLiteHelper.TABLE_NAME,null);
        //and integer value of the date column
        int dateCol=cursor.getColumnIndex(MySQLiteHelper.COLUMN_DATE);
        int ratingIndx=cursor.getColumnIndex(MySQLiteHelper.COLUMN_RATING);

        Calendar cal = Calendar.getInstance();

        //loop over database.
        if (cursor.moveToFirst()) {
            //stop when we hit end of database
            while (!cursor.isAfterLast()) {
                //need to turn database dates into days
                SimpleDateFormat inFormat = new SimpleDateFormat("dd MMMM yyyy");
                try{
                Date date = inFormat.parse(cursor.getString(dateCol));
                    cal.setTime(date);
                    //day of week from 1-7, arrays from 0-6. 1=sunday
                    int weekDay=cal.get(Calendar.DAY_OF_WEEK)-1;
                    dayRatings[weekDay]+=cursor.getInt(ratingIndx);
                    dayCounts[weekDay]+=1;

                }
                catch(java.text.ParseException e){
                    System.out.println(e);

                }

                //next iteration
                cursor.moveToNext();
            }

        }
        cursor.close();

        //average and return
        for (int i=0;i<7;i++){
            if (dayCounts[i]>0) {
                float rating = ((float) dayRatings[i]) / ((float)dayCounts[i]);
                dayPoints.add(rating);
            }
            else{
                dayPoints.add(0.0f);
            }
        }

        return dayPoints;
    }

    /*Returns a list of the top 5 hashtags if sent true, and 5 worst if sent false.*/
    public List<Hashtag> getHashtagsSorted(boolean bestTags){
        List<Hashtag> hashtags = new ArrayList<>();
        String notes;
        //every entry in database in a cursor
        Cursor cursor=database.rawQuery("select * from "+MySQLiteHelper.TABLE_NAME,null);
        int notesCol=cursor.getColumnIndex(MySQLiteHelper.COLUMN_NOTES);
        int ratingCol=cursor.getColumnIndex(MySQLiteHelper.COLUMN_RATING);
        int tagIndex;
        float rating;

        //loop over data, without doing anything stupid
        // (eg accessing empty cursor or moving past last entry)
        if (cursor.moveToFirst()) {
            while (!cursor.isAfterLast()) {
                notes = cursor.getString(notesCol);
                //use regex for split. this matches spaces.
                String[] notesSplit = notes.split("[\\s]");
                if (notesSplit.length > 0){
                    for (int i=1; i<notesSplit.length; i++){
                        //only carry on if there's a hashtag
                        if (notesSplit[i].contains("#")) {
                            //if not already in list, add it to the list and add the rating.

                            tagIndex = hashtags.indexOf(notesSplit[i]);
                            if (tagIndex == -1) {
                                Hashtag newTag = new Hashtag(notesSplit[i],(float)cursor.getInt(ratingCol));
                                hashtags.add(newTag);
                            }
                            //otherwise add 1 to count at that index and add the rating to the sum (average before division)
                            else {
                                hashtags.get(tagIndex).counts+=1;
                                hashtags.get(tagIndex).score+=cursor.getInt(ratingCol);
                            }
                        }
                    }
                }

                //next iteration
                cursor.moveToNext();
            }

        }
        cursor.close();

        //actually get average rather than sum
        for (int i =0; i < hashtags.size();i++){
            hashtags.get(i).score/=(float)hashtags.get(i).counts;
        }

        //if we want highest to lowest score.
        if (bestTags){
            Collections.sort(hashtags, Collections.reverseOrder(new Comparator<Hashtag>() {
                @Override
                public int compare(Hashtag o1, Hashtag o2) {
                    return o1.compare(o2);
                }
            }));
        }
        //or if we want worst hashtags
        else{


            Collections.sort(hashtags, new Comparator<Hashtag>() {
                @Override
                public int compare(Hashtag o1, Hashtag o2) {
                    return o1.compare(o2);
                }
            });
        }

        if (hashtags.size()>5){
            return hashtags.subList(0,5);
        }
        else{
            return hashtags;
        }
    }

}
