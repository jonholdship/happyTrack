package com.ducksaxaphone.happytrack;

import android.os.Bundle;
import android.support.design.widget.NavigationView;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.AppCompatActivity;
import android.view.Gravity;
import android.view.MenuItem;
import android.widget.ImageButton;

import java.text.SimpleDateFormat;
import java.util.Date;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

public class ActivityTrack extends AppCompatActivity
        implements NavigationView.OnNavigationItemSelectedListener {

    @BindView(R.id.MenuButton) ImageButton menuButton;
    @BindView(R.id.drawer_layout) DrawerLayout drawer;
    @BindView(R.id.TodayView) DayView todayView;

    private DayDataSource dayData;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);


        //open database for checking dates
        dayData=new DayDataSource(this);
        dayData.open();

        //usualview set up, with menu bit
        setContentView(R.layout.activity_track);
        ButterKnife.bind(this);
        NavigationView navigationView = (NavigationView) findViewById(R.id.nav_view);
        navigationView.setNavigationItemSelectedListener(this);

        //get currentDay's date and send to database. It'll return a new day object or one from database
        String date = new SimpleDateFormat("dd MMMM yyyy").format(new Date());
        Day today = dayData.getDay(date);
        todayView = (DayView) findViewById(R.id.TodayView);
        todayView.setDay(today);
    }

    @Override
    public void onBackPressed() {

        //stop the app closing if drawer is open and just close drawer
        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        if (drawer.isDrawerOpen(GravityCompat.START)) {
            drawer.closeDrawer(GravityCompat.START);
        } else {
            //save current day before closing
            dayData.updateDay(todayView.currentDay);
            super.onBackPressed();
        }
    }

    @OnClick(R.id.MenuButton)
    public void openDrawer(){
        //save to database when we open drawer so user can't navigate away without saving
        dayData.updateDay(todayView.currentDay);
        drawer.openDrawer(Gravity.LEFT);
    }

    @SuppressWarnings("StatementWithEmptyBody")
    @Override
    public boolean onNavigationItemSelected(MenuItem item) {
        // Handle navigation view item clicks here.
        int id = item.getItemId();

        if (id == R.id.NavRate) {
           drawer.closeDrawer(Gravity.LEFT);
        } else if (id == R.id.NavRemind) {

        } else if (id == R.id.NavCalendar) {

        } else if (id == R.id.NavAnalysis) {

        } else if (id == R.id.NavRate) {

        } else if (id == R.id.NavSettings) {

        }

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        drawer.closeDrawer(GravityCompat.START);
        return true;
    }
}
