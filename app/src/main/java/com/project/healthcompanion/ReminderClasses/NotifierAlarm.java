package com.project.healthcompanion.ReminderClasses;

import android.app.Notification;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.app.TaskStackBuilder;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.media.RingtoneManager;
import android.net.Uri;
import android.os.Build;

import androidx.core.app.NotificationCompat;

import com.project.healthcompanion.R;

import java.util.Date;

public class NotifierAlarm extends BroadcastReceiver {
    private Reminder_database reminder_database;

    @Override
    public void onReceive(Context context, Intent intent) {
        reminder_database = Reminder_database.getAppDatabase(context.getApplicationContext());
        RoomDAO roomDAO = reminder_database.getRoomDAO();
        Reminders reminder = new Reminders();
        reminder.setMessage(intent.getStringExtra("Message"));
        reminder.setRemindDate(new Date(intent.getStringExtra("RemindDate")));
        reminder.setId(intent.getIntExtra("id", 0));
        roomDAO.Delete(reminder);
        Reminder_database.destroyInstance();

        Uri alarmsound = RingtoneManager.getDefaultUri(RingtoneManager.TYPE_ALARM);

        Intent intent1 = new Intent(context, Reminder_main.class);
        intent1.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);

        TaskStackBuilder taskStackBuilder = TaskStackBuilder.create(context);
        taskStackBuilder.addParentStack(Reminder_main.class);
        taskStackBuilder.addNextIntent(intent1);

        PendingIntent intent2 = taskStackBuilder.getPendingIntent(1, PendingIntent.FLAG_UPDATE_CURRENT);

        NotificationCompat.Builder builder = new NotificationCompat.Builder(context, "Not1");

        NotificationChannel channel = null;
        if(android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.O){
            channel = new NotificationChannel("my_channel_01", "hello", NotificationManager.IMPORTANCE_HIGH);
        }

        Notification notification = builder.setContentTitle("Reminder")
                .setContentText(intent.getStringExtra("Message")).setAutoCancel(true)
                .setSound(alarmsound).setSmallIcon(R.mipmap.ic_launcher_round)
                .setContentIntent(intent2)
                .setChannelId("my_channel_01")
                .build();

        NotificationManager notificationManager = (NotificationManager) context.getSystemService(Context.NOTIFICATION_SERVICE);
        if(Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            notificationManager.createNotificationChannel(channel);
        }
        notificationManager.notify(1, notification);
    }
}
