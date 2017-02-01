package com.ducksaxaphone.happytrack;

import android.app.NotificationManager;
import android.app.PendingIntent;
import android.app.TaskStackBuilder;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.support.v4.app.NotificationCompat;

import com.ducksaxaphone.happytrack.ActivityTrack;
import com.ducksaxaphone.happytrack.R;

import java.util.Calendar;
import java.util.GregorianCalendar;

/**
 * Created by jrh on 26/01/17.
 */

public class AlarmReceiver extends BroadcastReceiver {

    @Override
    public void onReceive(Context context, Intent intent) {
       System.out.println("recieved");
        //create a notification with icon, text and title.
        NotificationCompat.Builder mBuilder = new NotificationCompat.Builder(context)
                        .setSmallIcon(R.drawable.happyface1)
                        .setContentTitle("Time to rate your day!")
                        .setContentText("I hope it was a good one")
                        .setAutoCancel(true);
        //tell it what we want to do when clicked. In this case, open the track activity
        Intent resultIntent = new Intent(context, ActivityTrack.class);
        TaskStackBuilder stackBuilder = TaskStackBuilder.create(context);
        stackBuilder.addParentStack(ActivityTrack.class);
        stackBuilder.addNextIntent(resultIntent);
        PendingIntent resultPendingIntent = stackBuilder.getPendingIntent(0, PendingIntent.FLAG_UPDATE_CURRENT);
        mBuilder.setContentIntent(resultPendingIntent);
        NotificationManager mNotificationManager = (NotificationManager) context.getSystemService(Context.NOTIFICATION_SERVICE);
        mNotificationManager.notify(1, mBuilder.build());

    }
}