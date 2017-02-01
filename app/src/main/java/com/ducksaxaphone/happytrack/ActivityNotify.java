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

import java.util.Calendar;
import java.util.Date;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;


public class ActivityNotify extends BaseActivity {
    @BindView(R.id.ButtonDaily) Button dailyButton;
    @BindView(R.id.ButtonNever) Button neverButton;
    @BindView(R.id.ReminderClock) TimePicker reminderClock;

    public static final String PREFS_NAME= "NotificationPrefs";
    private boolean reminders,timeChosenFirst;
    long targetMilliseconds;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        //usualview set up, with menu bit
        setContentView(R.layout.activity_notify);
        ButterKnife.bind(this);

        // Restore daily/never notification prefs.
        SharedPreferences settings = getSharedPreferences(PREFS_NAME, 0);
        reminders = settings.getBoolean("reminders", false);
        if (reminders){
            onDailyClick();
        }
        else{
            onNeverClick();
        }

        reminderClock.setCurrentHour(settings.getInt("hours",22));
        reminderClock.setCurrentMinute(settings.getInt("minutes",0));

        timeChosenFirst=reminders;
        //set up clock
        reminderClock.setOnTimeChangedListener(new TimePicker.OnTimeChangedListener() {
            @Override
            public void onTimeChanged(TimePicker timePicker, int i, int i1) {
                //i is in hours and i1 in minutes
                targetMilliseconds = 60000*((60*i)+i1);
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

    @OnClick(R.id.ButtonDaily)
    public void onDailyClick(){

        //change buttons to look like daily is selected
        dailyButton.setBackgroundColor(getResources().getColor(R.color.colorPrimaryLight));
        neverButton.setBackgroundColor(getResources().getColor(R.color.colorIcons));

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

    @OnClick(R.id.ButtonNever)
    public void onNeverClick(){
        neverButton.setBackgroundColor(getResources().getColor(R.color.colorPrimaryLight));
        dailyButton.setBackgroundColor(getResources().getColor(R.color.colorIcons));

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
        long currentTime= (calendar.get(Calendar.HOUR_OF_DAY)*60)+calendar.get(Calendar.MINUTE);
        currentTime=currentTime*60*1000;

        //if we're past the target time, need to wait for tomorrow so calculate time til midnight
        //then add the target time
        if (currentTime > targetMilliseconds){
            System.out.println(currentTime);
            System.out.println(targetMilliseconds);
            //24 hours
            long tomorrow;
            tomorrow=24*60*60*1000;
            waitTime=(tomorrow-currentTime)+targetMilliseconds;
        }
        //if we're still earlier than target, can just do target-current to get wait interval
        else{
            waitTime=targetMilliseconds-currentTime;
        }

        System.out.println(waitTime);

        //intent: what will happen when alarm goes off. This is an internal alarm, it calls the class
        //Alarm reciever which puts a notification on user screen saying to come rate their day
        Intent alarmIntent = new Intent(this, AlarmReceiver.class);
        PendingIntent pendingIntent = PendingIntent.getBroadcast(this.getApplicationContext(), 0, alarmIntent, PendingIntent.FLAG_UPDATE_CURRENT);

        //alarm managers are a system thing. Set up a repeating alarm that will go off at a certain time. from now and then every day after
        AlarmManager alarmManager = (AlarmManager) getSystemService(ALARM_SERVICE);

        alarmManager.setRepeating(AlarmManager.RTC_WAKEUP, waitTime, AlarmManager.INTERVAL_DAY, pendingIntent);
        System.out.println("created alarm");
    }

}