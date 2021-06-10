package com.project.healthcompanion.ReminderClasses;

import android.content.Context;

import androidx.room.Database;
import androidx.room.Room;
import androidx.room.RoomDatabase;
import androidx.room.TypeConverters;

@Database(entities = {Reminders.class}, version = 1)
@TypeConverters({DateTypeConverter.class})
public abstract class Reminder_database extends RoomDatabase {

    private static Reminder_database INSTANCE = null;
    public abstract RoomDAO getRoomDAO();

    public static Reminder_database getAppDatabase(Context context){
        if(INSTANCE == null){
            INSTANCE = Room.databaseBuilder(context.getApplicationContext(), Reminder_database.class, "users").allowMainThreadQueries().build();
        }
        return INSTANCE;
    }

    public static void destroyInstance(){INSTANCE = null;}

}
