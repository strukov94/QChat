package com.strukov.qchat.utils.notification;

import android.app.Notification;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.support.v4.app.NotificationCompat;
import android.widget.RemoteViews;

import com.squareup.picasso.Picasso;
import com.strukov.qchat.R;
import com.strukov.qchat.messaging.MessagingActivity;
import com.strukov.qchat.utils.ConvertDate;

/**
 * Created by Matthew on 06.01.2018.
 */

public class NotificationUtils {
    public static void setNotification(Context context, String image, String name, long date, String text, int id, PendingIntent intent) {
        RemoteViews remoteViews = new RemoteViews(context.getPackageName(), R.layout.notification);
        remoteViews.setTextViewText(R.id.text_notification, text);
        remoteViews.setTextViewText(R.id.text_notification_name, name);
        remoteViews.setTextViewText(R.id.text_notification_date, ConvertDate.getDate(date));

        NotificationCompat.Builder builder = new NotificationCompat.Builder(context, "Messages")
                .setSmallIcon(R.drawable.ic_launcher_background)
                .setPriority(NotificationCompat.PRIORITY_MAX)
                .setStyle(new android.support.v4.app.NotificationCompat.DecoratedCustomViewStyle())
                .setAutoCancel(true)
                .setLights(Color.CYAN, 300, 100)
                .setContentIntent(intent)
                .setContent(remoteViews);

        Notification notification = builder.build();
        notification.defaults |= Notification.DEFAULT_SOUND;
        notification.defaults |= Notification.DEFAULT_VIBRATE;

        Handler handler = new Handler(Looper.getMainLooper());
        handler.post(() -> Picasso.with(context)
                .load(image)
                .into(remoteViews, R.id.image_notification, id, notification));

        NotificationManager manager = (NotificationManager) context.getSystemService(Context.NOTIFICATION_SERVICE);
        manager.notify(id, notification);
    }
}
