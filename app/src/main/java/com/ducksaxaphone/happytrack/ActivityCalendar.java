package com.ducksaxaphone.happytrack;

import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.view.Gravity;
import android.view.View;
import android.support.design.widget.NavigationView;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.CalendarView;
import android.widget.TextView;

import java.text.SimpleDateFormat;
import java.util.Date;

import com.github.sundeepk.compactcalendarview.CompactCalendarView;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

public class ActivityCalendar extends BaseActivity
        implements CompactCalendarView.CompactCalendarViewListener {

    private DayDataSource dayData;
    @BindView(R.id.TodayView) DayView todayView;
    @BindView(R.id.CalendarView) CompactCalendarView mCalendar;
    @BindView(R.id.MonthLabel) TextView monthLabel;

    @Override
    protected void onCreate(Bundle savedInstanceState) {


        //open database for checking dates
        dayData=new DayDataSource(this);
        dayData.open();

        //usualview set up, with menu bit
        setContentView(R.layout.activity_calendar);
        ButterKnife.bind(this);

        //get currentDay's date and send to database. It'll return a new day object or one from database
        String date = new SimpleDateFormat("dd MMMM yyyy").format(new Date());
        Day today = dayData.getDay(date);
        todayView = (DayView) findViewById(R.id.TodayView);
        todayView.setDay(today);

        date = new SimpleDateFormat("MMMM yyyy").format(new Date());
        monthLabel.setText(date);
        //set up calendar
        mCalendar.setListener(this);

        //go last so view is set before we try to make drawer work
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



    @Override
    public void onDayClick(Date dateClicked) {
        //save before changing day
        dayData.updateDay(todayView.currentDay);
        //format calendar date to app date
        String date = new SimpleDateFormat("dd MMMM yyyy").format(dateClicked);

        //standard logic to get a day object from database and set the view to it.
        Day today = dayData.getDay(date);
        todayView = (DayView) findViewById(R.id.TodayView);
        todayView.setDay(today);

        //update calendar
        mCalendar.setCurrentDate(dateClicked);
    }

    @Override
    public void onMonthScroll(Date firstDayOfNewMonth) {
        //format date as just month and year.
        String date = new SimpleDateFormat("MMMM yyyy").format(firstDayOfNewMonth);
        //change calendar labels
        monthLabel.setText(date);
    }
}
