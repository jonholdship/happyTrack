package com.ducksaxaphone.happytrack;

import android.app.AlarmManager;
import android.app.Notification;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Intent;
import android.content.SharedPreferences;
import android.icu.util.TimeUnit;
import android.os.Bundle;
import android.provider.Settings;
import android.support.v4.app.NotificationCompat;
import android.widget.Button;
import android.widget.TimePicker;

import org.w3c.dom.Text;

import java.util.Calendar;
import java.util.Date;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;


public class ActivityNotify extends BaseActivity implements TextSwitch.TextSwitchListener{
    @BindView(R.id.ReminderSwitch) TextSwitch reminderSwitch;
    @BindView(R.id.ReminderClock) TimePicker reminderClock;

    public static final String PREFS_NAME= "NotificationPrefs";
    private boolean reminders,timeChosenFirst;
    int alarmHour, alarmMinute;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        //usualview set up, with menu bit
        setContentView(R.layout.activity_notify);
        ButterKnife.bind(this);
        reminderSwitch.setListener(this);
        // Restore daily/never notification prefs.
        SharedPreferences settings = getSharedPreferences(PREFS_NAME, 0);
        reminders = settings.getBoolean("reminders", false);
        if (reminders){
            rightButtonPressed();
        }
        else{
            leftButtonPressed();
        }

        reminderClock.setCurrentHour(settings.getInt("hours",22));
        reminderClock.setCurrentMinute(settings.getInt("minutes",0));

        timeChosenFirst=reminders;
        //set up clock
        reminderClock.setOnTimeChangedListener(new TimePicker.OnTimeChangedListener() {
            @Override
            public void onTimeChanged(TimePicker timePicker, int i, int i1) {
                //i is in hours and i1 in minutes
                alarmHour = i;
                alarmMinute = i1;
                if (reminders){
                createNotifcation();
                }
                //button to set reminders on or off uses these to see if it should create a notification
                timeChosenFirst=true;

                SharedPreferences settings = getSharedPreferences(PREFS_NAME, 0);
                SharedPreferences.Editor editor = settings.edit();
                editor.putInt("hours", i);
                editor.putInt("minutes",i1);
                editor.commit();

            }
        });


        //this goes last so that the contentview is set before we try to do things with it in base
        super.onCreate(savedInstanceState);

    }

    protected void onPause(){
        super.onPause();
    }

    public void rightButtonPressed(){

        //create notification
        reminders=true;
        if (timeChosenFirst) {
            createNotifcation();
        }

        SharedPreferences settings = getSharedPreferences(PREFS_NAME, 0);
        SharedPreferences.Editor editor = settings.edit();
        editor.putBoolean("reminders", reminders);
        editor.commit();
    }

    public void leftButtonPressed(){
        reminders=false;

        SharedPreferences settings = getSharedPreferences(PREFS_NAME, 0);
        SharedPreferences.Editor editor = settings.edit();
        editor.putBoolean("reminders", reminders);
        editor.commit();
    }


    public void createNotifcation() {
        long waitTime;//how many milliseconds until alarm will first be called

        //unbelievably complicated way to get current time of day in milliseconds
        Calendar calendar= Calendar.getInstance();
        calendar.set(Calendar.HOUR_OF_DAY,alarmHour);
        calendar.set(Calendar.MINUTE,alarmMinute);

        if (calendar.before(Calendar.getInstance())){
            calendar.add(Calendar.DAY_OF_MONTH,1);
        }


        //intent: what will happen when alarm goes off. This is an internal alarm, it calls the class
        //Alarm reciever which puts a notification on user screen saying to come rate their day
        Intent alarmIntent = new Intent(this, AlarmReceiver.class);
        PendingIntent pendingIntent = PendingIntent.getBroadcast(this.getApplicationContext(), 0, alarmIntent, PendingIntent.FLAG_UPDATE_CURRENT);

        //alarm managers are a system thing. Set up a repeating alarm that will go off at a certain time. from now and then every day after
        AlarmManager alarmManager = (AlarmManager) getSystemService(ALARM_SERVICE);

        alarmManager.setRepeating(AlarmManager.RTC_WAKEUP, calendar.getTimeInMillis(), AlarmManager.INTERVAL_DAY, pendingIntent);
        System.out.println("created alarm");
    }

}
