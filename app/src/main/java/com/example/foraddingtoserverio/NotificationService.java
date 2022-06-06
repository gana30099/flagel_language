package com.example.foraddingtoserverio;

import android.app.Notification;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.app.Service;
import android.content.Intent;
import android.os.Binder;
import android.os.Build;
import android.os.IBinder;

import androidx.annotation.Nullable;

import java.net.URISyntaxException;

import io.socket.client.IO;
import io.socket.client.Socket;
import io.socket.emitter.Emitter;

import static io.socket.client.Socket.EVENT_CONNECT;

public class NotificationService extends Service {

    Socket mySocket;
    // Binder given to clients
    private final IBinder binder = new LocalBinder();

    public class LocalBinder extends Binder {
        NotificationService getService() {
            // Return this instance of LocalService so clients can call public methods
            return NotificationService.this;
        }
    }




    @Nullable
    @Override
    public IBinder onBind(Intent intent) {
        return binder;
    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        super.onStartCommand(intent, flags, startId);
        //notif("3");
        try {
            mySocket = IO.socket("http://10.0.2.2:3000");
        } catch (URISyntaxException e) {
            e.printStackTrace();
        }
        mySocket.connect();



        mySocket.once(EVENT_CONNECT, new Emitter.Listener() {
            @Override
            public void call(Object... args) {
                String id = mySocket.id();

                System.out.println(id);

                mySocket.emit("id", id);
                System.out.println("connected on service");



            }
        });

        mySocket.on("notify", new Emitter.Listener() {
            @Override
            public void call(Object... args) {
                System.out.println("notif number -----------------" + args[0]);
                notif(args[0].toString());
            }
        });

        return START_STICKY;

    }

    private void notif(String number) {
        int num = Integer.parseInt(number);

        Intent notifyIntent = new Intent(this, DeleteSentences.class);
        notifyIntent.putExtra("number_to_delete", num);
        // Set the Activity to start in a new, empty task
        notifyIntent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK
                | Intent.FLAG_ACTIVITY_CLEAR_TASK);
        // Create the PendingIntent
        PendingIntent notifyPendingIntent = PendingIntent.getActivity(
                this, 0, notifyIntent,
                PendingIntent.FLAG_UPDATE_CURRENT | PendingIntent.FLAG_IMMUTABLE
        );

        Notification.Builder builder = new Notification.Builder(this, NotificationChannel.EDIT_CONVERSATION)
                .setSmallIcon(R.drawable.ic_launcher_background)
                .setContentTitle("Hi Gaëtan")
                .setContentText("You have " + number + " new sentences")
                .setPriority(Notification.PRIORITY_HIGH)
                .setContentIntent(notifyPendingIntent);

        Notification not = builder.build();


        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            CharSequence name = getString(R.string.channel_name);
            String description = getString(R.string.channel_description);
            int importance = NotificationManager.IMPORTANCE_DEFAULT;
            NotificationChannel channel = new NotificationChannel(NotificationChannel.EDIT_CONVERSATION, name, importance);
            channel.setDescription(description);
            // Register the channel with the system; you can't change the importance
            // or other notification behaviors after this
            NotificationManager notificationManager = getSystemService(NotificationManager.class);
            notificationManager.createNotificationChannel(channel);
            boolean boolean1 = notificationManager.areNotificationsEnabled();
            notificationManager.notify(1, not);
            startForeground(1, not);

            startDeleteActivity(number);

        }
    }

    private void startDeleteActivity(String number) {



    }


}
