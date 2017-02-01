package com.ducksaxaphone.happytrack;

import android.content.Intent;
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

/**
 * Created by jrh on 25/01/17.
 */

public class BaseActivity extends AppCompatActivity
        implements NavigationView.OnNavigationItemSelectedListener {

    @BindView(R.id.drawer_layout) DrawerLayout drawer;

    private DayDataSource dayData;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        //usualview set up, with menu bit
        ButterKnife.bind(this);
        NavigationView navigationView = (NavigationView) findViewById(R.id.nav_view);
        navigationView.setNavigationItemSelectedListener(this);
    }

    @Override
    public void onBackPressed() {

        //stop the app closing if drawer is open and just close drawer
        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        if (drawer.isDrawerOpen(GravityCompat.START)) {
            drawer.closeDrawer(GravityCompat.START);
        } else {
            //save current day before closing
            super.onBackPressed();
        }
    }

    @OnClick(R.id.MenuButton)
    public void openDrawer(){
        drawer.openDrawer(Gravity.LEFT);
    }

    @SuppressWarnings("StatementWithEmptyBody")
    @Override
    public boolean onNavigationItemSelected(MenuItem item) {
        // Handle navigation view item clicks here.
        int id = item.getItemId();

        if (id == R.id.NavTrack) {
            //drawer.closeDrawer(Gravity.LEFT);
            Intent i = new Intent(this, ActivityTrack.class);
            startActivity(i);
        } else if (id == R.id.NavRemind) {
            Intent i = new Intent(this, ActivityNotify.class);
            startActivity(i);

        } else if (id == R.id.NavCalendar) {
            Intent i = new Intent(this, ActivityCalendar.class);
            startActivity(i);

        } else if (id == R.id.NavAnalysis) {

        } else if (id == R.id.NavRate) {

        } else if (id == R.id.NavSettings) {

        } else if (id == R.id.NavAbout) {
            Intent i = new Intent(this, ActivityAbout.class);
            startActivity(i);
        }

        this.finish();
        return true;
    }
}


