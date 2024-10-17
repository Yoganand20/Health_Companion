package com.project.healthcompanion.ReminderClasses;

import android.annotation.SuppressLint;
import android.app.AlarmManager;
import android.app.DatePickerDialog;
import android.app.Dialog;
import android.app.PendingIntent;
import android.app.TimePickerDialog;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBarDrawerToggle;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.drawerlayout.widget.DrawerLayout;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.project.healthcompanion.DashboardClasses.DashboardActivity;
import com.project.healthcompanion.DietPlansClasses.DietPlans;
import com.project.healthcompanion.HelpActivity;
import com.project.healthcompanion.HomePage;
import com.project.healthcompanion.Profile;
import com.project.healthcompanion.R;
import com.project.healthcompanion.Records;

import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.Objects;
import java.util.TimeZone;

public class Reminder_main extends AppCompatActivity {

    DrawerLayout drawerLayout;

    private Dialog dialog;
    private Reminder_database reminder_database;
    private RecyclerView recyclerView;
    private TextView empty;

    ActionBarDrawerToggle toggle;
    //press back twice to exit
    private boolean backPressedOnce = false;
    private Toast t;

    public void ClickMenu(View view) {
        HomePage.openDrawer(drawerLayout);
    }

    public void ClickLogo(View view) {
        HomePage.closeDrawer(drawerLayout);
    }

    public void ClickProfile(View view) {
        HomePage.redirectActivity(this, Profile.class);
    }


    public void ClickDashboard(View view) { HomePage.redirectActivity(this, DashboardActivity.class); }

    public void ClickRecords(View view) {
        HomePage.redirectActivity(this, Records.class);
    }

    public void ClickDietPlans(View view) {
        HomePage.redirectActivity(this, DietPlans.class);
    }

    public void ClickReminders(View view) {
        HomePage.closeDrawer(drawerLayout);
    }

    public void ClickHelp(View view) {HomePage.redirectActivity(this, HelpActivity.class);}

    public void ClickLogout(View view) {
        HomePage.logout(this);
    }
    @Override
    protected void onPause(){
        super.onPause();
        HomePage.closeDrawer(drawerLayout);
    }


    @SuppressLint("ScheduleExactAlarm")
    public void addReminder() {
        dialog = new Dialog(Reminder_main.this);
        dialog.setContentView(R.layout.reminder_popup);

        final TextView textView = dialog.findViewById(R.id.date);
        Button select, add;
        select = dialog.findViewById(R.id.selectDate);
        add = dialog.findViewById(R.id.addButton);
        final EditText message = dialog.findViewById(R.id.message);

        final Calendar newCalendar = Calendar.getInstance();
        select.setOnClickListener(v -> {
            DatePickerDialog dialog = new DatePickerDialog(Reminder_main.this, (view, year, month, dayOfMonth) -> {
                final Calendar newDate = Calendar.getInstance();
                Calendar newTime = Calendar.getInstance();
                TimePickerDialog time = new TimePickerDialog(Reminder_main.this, (view1, hourOfDay, minute) -> {
                    newDate.set(year, month, dayOfMonth, hourOfDay, minute, 0);
                    Calendar tem = Calendar.getInstance();
                    Log.w("TIME", System.currentTimeMillis()+"");
                    if(newDate.getTimeInMillis()-tem.getTimeInMillis()>0)
                        textView.setText(newDate.getTime().toString());
                    else
                        Toast.makeText(Reminder_main.this, "Invalid time", Toast.LENGTH_SHORT).show();
                }, newTime.get(Calendar.HOUR_OF_DAY), newTime.get(Calendar.MINUTE),true);
                time.show();

            }, newCalendar.get(Calendar.YEAR), newCalendar.get(Calendar.MONTH), newCalendar.get(Calendar.DAY_OF_MONTH));

            dialog.getDatePicker().setMinDate(System.currentTimeMillis());
            dialog.show();
        });

        add.setOnClickListener(v -> {
            RoomDAO roomDAO = reminder_database.getRoomDAO();
            Reminders reminders = new Reminders();
            reminders.setMessage(message.getText().toString().trim());
            Date remind = new Date(textView.getText().toString().trim());
            reminders.setRemindDate(remind);
            roomDAO.Insert(reminders);
            List<Reminders> l = roomDAO.getAll();
            reminders = l.get(l.size()-1);
            Log.e("ID needed", reminders.getId()+"");

            Calendar calendar = Calendar.getInstance(TimeZone.getTimeZone("GMT+5:30"));
            calendar.setTime(remind);
            calendar.set(Calendar.SECOND, 0);
            Intent intent = new Intent(Reminder_main.this, NotifierAlarm.class);
            intent.putExtra("Message", reminders.getMessage());
            intent.putExtra("RemindDate", reminders.getRemindDate().toString());
            intent.putExtra("id", reminders.getId());
            PendingIntent intent1 = PendingIntent.getBroadcast(Reminder_main.this, reminders.getId(), intent,PendingIntent.FLAG_IMMUTABLE);
            AlarmManager alarmManager = (AlarmManager)getSystemService(ALARM_SERVICE);
            alarmManager.setExact(AlarmManager.RTC_WAKEUP, calendar.getTimeInMillis(), intent1);

            Toast.makeText(Reminder_main.this, "Reminder added successfully", Toast.LENGTH_SHORT).show();
            setItemsInRecyclerView();
            Reminder_database.destroyInstance();
            dialog.dismiss();
        });

        Objects.requireNonNull(dialog.getWindow()).setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
        dialog.show();
    }

    public void setItemsInRecyclerView() {
        RoomDAO dao = reminder_database.getRoomDAO();
        List<Reminders> temp = dao.orderThetable();
        if (!temp.isEmpty()) {
            empty.setVisibility(View.INVISIBLE);
            recyclerView.setVisibility(View.VISIBLE);
        }
        AdapterReminders adapter = new AdapterReminders(temp);
        recyclerView.setAdapter(adapter);
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        if (toggle.onOptionsItemSelected(item)) {
            return true;
        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.reminder_main);

        drawerLayout = findViewById(R.id.drawer_Layout);
        Objects.requireNonNull(getSupportActionBar()).setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setHomeButtonEnabled(true);
        toggle = new ActionBarDrawerToggle(
                this,
                drawerLayout,
                R.string.navigation_drawer_open,
                R.string.navigation_drawer_close) {
        };
        drawerLayout.addDrawerListener(toggle);
        toggle.syncState();

        reminder_database = Reminder_database.getAppDatabase(Reminder_main.this);

        FloatingActionButton add = findViewById(R.id.AddButton);
        empty = findViewById(R.id.empty_text);

        add.setOnClickListener(v -> addReminder());

        recyclerView = findViewById(R.id.RecyclerView);
        recyclerView.setHasFixedSize(true);
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(Reminder_main.this);
        recyclerView.setLayoutManager(linearLayoutManager);
        setItemsInRecyclerView();
    }

    @SuppressLint("MissingSuperCall")
    @Override
    public void onBackPressed() {
        if (backPressedOnce) {
            t.cancel();
            ActivityCompat.finishAffinity(Reminder_main.this);
            finish();
        }
        backPressedOnce = true;
        t = Toast.makeText(this, "Press back again to exit", Toast.LENGTH_SHORT);
        t.show();
        new Handler(Looper.getMainLooper()).postDelayed(() -> backPressedOnce = false, 2000);
    }
}