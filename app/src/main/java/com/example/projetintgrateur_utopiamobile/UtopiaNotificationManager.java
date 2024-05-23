package com.example.projetintgrateur_utopiamobile;

import android.app.Notification;
import android.app.NotificationChannel;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.graphics.BitmapFactory;
import android.os.Build;

import java.util.Date;

public class UtopiaNotificationManager {
    public static void postNotification(Context context, String title, String text, int group) {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            String CHANNEL_ID = "Utopia";
            String GROUP_ID = Integer.toString(group);

            NotificationChannel notificationChannel = new NotificationChannel(CHANNEL_ID,"name", android.app.NotificationManager.IMPORTANCE_LOW);
            Notification notification = new Notification.Builder(context,CHANNEL_ID)
                    .setContentTitle(title)
                    .setContentText(text)
                    .setChannelId(CHANNEL_ID)
                    .setSmallIcon(R.drawable.letter_u)
                    .setLargeIcon(BitmapFactory.decodeResource(context.getResources(), R.drawable.chat))
                    .setGroup(Integer.toString(group))
                    .build();

            Notification summaryNotification =
                    new Notification.Builder(context, CHANNEL_ID)
                            .setContentTitle(context.getString(R.string.conversationAvec) + " " + title)
                            .setSmallIcon(R.drawable.letter_u)
                            .setGroup(GROUP_ID)
                            .setGroupSummary(true)
                            .build();

            android.app.NotificationManager notificationManager = (android.app.NotificationManager) context.getSystemService(Context.NOTIFICATION_SERVICE);
            notificationManager.createNotificationChannel(notificationChannel);
            notificationManager.notify((int) new Date().getTime(), notification);
            notificationManager.notify((int) new Date().getTime() + 1, summaryNotification);
        }
    }
}
