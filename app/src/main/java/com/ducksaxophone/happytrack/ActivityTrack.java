package com.ducksaxophone.happytrack;

import android.os.Bundle;

import com.google.firebase.analytics.FirebaseAnalytics;

import java.text.SimpleDateFormat;
import java.util.Date;

import butterknife.BindView;
import butterknife.ButterKnife;

public class ActivityTrack extends BaseActivity {

    @BindView(R.id.TodayView) DayView todayView;

    private DayDataSource dayData;
    private FirebaseAnalytics mFirebaseAnalytics;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        //open database for checking dates
        dayData=new DayDataSource(this);
        dayData.open();

        //firebase for basic app data
        mFirebaseAnalytics = FirebaseAnalytics.getInstance(this);

        //usualview set up, with menu bit
        setContentView(R.layout.activity_track);
        ButterKnife.bind(this);
        //get currentDay's date and send to database. It'll return a new day object or one from database
        String date = new SimpleDateFormat("dd MMMM yyyy").format(new Date());
        Day today = dayData.getDay(date);
        todayView = findViewById(R.id.TodayView);
        todayView.setDay(today);

        //this goes last so that the contentview is set before we try to do things with it in base
        super.onCreate(savedInstanceState);

    }

    @Override
    protected void onPause(){
        dayData.updateDay(todayView.currentDay);
        dayData.close();
        super.onPause();
    }

    @Override
    protected void onResume(){
        dayData.open();
        super.onResume();
    }
}
