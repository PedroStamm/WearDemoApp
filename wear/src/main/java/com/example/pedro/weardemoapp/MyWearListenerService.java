package com.example.pedro.weardemoapp;

import android.app.Notification;
import android.app.PendingIntent;
import android.content.Intent;
import android.support.v4.app.NotificationCompat;
import android.support.v4.app.NotificationManagerCompat;
import android.util.Log;

import com.google.android.gms.wearable.MessageEvent;
import com.google.android.gms.wearable.WearableListenerService;

import java.nio.ByteBuffer;

/**
 * Created by Pedro on 27/11/2016.
 */

public class MyWearListenerService extends WearableListenerService {
    private String START_ACTIVITY_PATH = "/start/WearActivity";

    @Override
    public void onMessageReceived(MessageEvent messageEvent) {
        super.onMessageReceived(messageEvent);
        Log.d("Listener", "Message received");
        if(messageEvent.getPath().equals(START_ACTIVITY_PATH)){
            Log.d("Listener", "It's the one we want");
            int notificationId=1;
            byte[] data = messageEvent.getData();
            long cur = bytesToLong(data);
            // Create an intent for the reply action
            Intent actionIntent = new Intent(this, WearActivity.class).putExtra("notifId", notificationId).putExtra("date", cur);
            PendingIntent actionPendingIntent =
                    PendingIntent.getActivity(this, 0, actionIntent,
                            PendingIntent.FLAG_UPDATE_CURRENT);
            // Create the action
            NotificationCompat.Action action =
                    new NotificationCompat.Action.Builder(R.drawable.ic_action,
                            "Report back!", actionPendingIntent)
                            .build();
            Notification notification = new NotificationCompat.Builder(getApplication())
                .setSmallIcon(R.mipmap.ic_launcher)
                .setContentTitle("Insulin Dose!")
                .setContentText("Time to take your insulin")
                .extend(new NotificationCompat.WearableExtender().addAction(action))
                .build();
            NotificationManagerCompat notificationManager = NotificationManagerCompat.from(this);
            notificationManager.notify(notificationId, notification);
        }


    }

    public long bytesToLong(byte[] bytes) {
        ByteBuffer buffer = ByteBuffer.allocate(Long.SIZE);
        buffer.put(bytes);
        buffer.flip();//need flip
        return buffer.getLong();
    }
}
