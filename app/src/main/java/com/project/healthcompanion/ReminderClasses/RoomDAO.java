package com.project.healthcompanion.ReminderClasses;

import androidx.room.Dao;
import androidx.room.Delete;
import androidx.room.Insert;
import androidx.room.Query;

import java.util.List;

@Dao
public interface RoomDAO {
    @Insert
    void Insert(Reminders... reminders);

    @Delete
    void Delete(Reminders reminders);

    @Query("Select * from reminder order by remindDate")
    List<Reminders> orderThetable();

    @Query("Select * from reminder")
    List<Reminders> getAll();
}
