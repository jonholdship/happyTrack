package com.ducksaxaphone.happytrack;

import android.os.Bundle;
import android.support.design.widget.NavigationView;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.view.Gravity;
import android.view.MenuItem;
import android.widget.ImageButton;

import java.text.SimpleDateFormat;
import java.util.Date;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

public class ActivityTrack extends BaseActivity {

    @BindView(R.id.TodayView) DayView todayView;

    private DayDataSource dayData;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        //open database for checking dates
        dayData=new DayDataSource(this);
        dayData.open();

        //usualview set up, with menu bit
        setContentView(R.layout.activity_track);
        ButterKnife.bind(this);
        //get currentDay's date and send to database. It'll return a new day object or one from database
        String date = new SimpleDateFormat("dd MMMM yyyy").format(new Date());
        Day today = dayData.getDay(date);
        todayView = (DayView) findViewById(R.id.TodayView);
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
