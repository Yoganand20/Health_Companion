package com.project.healthcompanion.DashboardClasses;

import android.app.Activity;
import android.app.AlarmManager;
import android.app.Dialog;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.activity.result.ActivityResult;
import androidx.activity.result.ActivityResultCallback;
import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.drawerlayout.widget.DrawerLayout;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;
import androidx.recyclerview.widget.RecyclerView;
import androidx.viewpager.widget.ViewPager;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.tabs.TabItem;
import com.google.android.material.tabs.TabLayout;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.project.healthcompanion.DietPlansClasses.DietPlans;
import com.project.healthcompanion.HelpActivity;
import com.project.healthcompanion.HomePage;
import com.project.healthcompanion.Profile;
import com.project.healthcompanion.R;
import com.project.healthcompanion.Records;
import com.project.healthcompanion.ReminderClasses.Reminder_main;

import org.eazegraph.lib.charts.PieChart;
import org.eazegraph.lib.models.PieModel;
import org.jetbrains.annotations.NotNull;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.sql.Time;
import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.time.LocalTime;
import java.time.temporal.ChronoUnit;
import java.util.Calendar;
import java.util.Date;
import java.util.Timer;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;

public class DashboardActivity extends AppCompatActivity {

    DrawerLayout drawerLayout;

    SimpleDateFormat simpleDateFormat;

    Date date;

    TextView cals, proteins, carbs, fats;

    TextView PlannedCals;

    PieChart pieChart;

    Double CalVal = 0.0;
    Double proteinVal = 0.0;
    Double carbsVal = 0.0;
    Double fatsVal = 0.0;

    String currentUser = FirebaseAuth.getInstance().getCurrentUser().getUid();

    FirebaseFirestore firebaseFirestore;

    TabLayout tabLayout;

    TabItem BreakfastTab;
    TabItem LunchTab;
    TabItem DinnerTab;
    TabItem SnacksTab;

    ViewPager viewPager;

    DashboardPagerAdapter DashPagerAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_dashboard);

        drawerLayout = findViewById(R.id.drawer_Layout);

        pieChart = findViewById(R.id.DashboardPiechart);

        tabLayout = findViewById(R.id.DashtabBar);

        BreakfastTab = findViewById(R.id.DashBreakfastTab);
        LunchTab = findViewById(R.id.DashLunchTab);
        DinnerTab = findViewById(R.id.DashDinnerTab);
        SnacksTab = findViewById(R.id.DashSnacksTab);

        viewPager = findViewById(R.id.DashViewPager);

        PlannedCals = findViewById(R.id.PlannedCals);

        simpleDateFormat = new SimpleDateFormat("dd-MM-yyyy");
        date = new Date();

        DashPagerAdapter = new DashboardPagerAdapter(getSupportFragmentManager(), tabLayout.getTabCount());

        viewPager.setAdapter(DashPagerAdapter);

        tabLayout.addOnTabSelectedListener(new TabLayout.OnTabSelectedListener() {
            @Override
            public void onTabSelected(TabLayout.Tab tab) {
                viewPager.setCurrentItem(tab.getPosition());
            }

            @Override
            public void onTabUnselected(TabLayout.Tab tab) {

            }

            @Override
            public void onTabReselected(TabLayout.Tab tab) {

            }
        });

        cals = findViewById(R.id.ConsumedCals);
        proteins = findViewById(R.id.DashProtein);
        carbs = findViewById(R.id.DashCarbs);
        fats = findViewById(R.id.DashFats);

        firebaseFirestore = FirebaseFirestore.getInstance();

        /*Calendar calendar = Calendar.getInstance();
        calendar.set(calendar.get(Calendar.YEAR), calendar.get(Calendar.MONTH), calendar.get(Calendar.DAY_OF_MONTH), 22, 00, 00);
                                                                                                                    //sets dashboard reset timing^
        setAlarm(calendar.getTimeInMillis());*/

        setSuggCal();

        setChart();
    }

    //navigation drawer
    public void ClickMenu(View view) { HomePage.openDrawer(drawerLayout); }

    public void ClickLogo(View view) { HomePage.closeDrawer(drawerLayout); }

    public void ClickProfile(View view) { HomePage.redirectActivity(this, Profile.class); }

    public void ClickDashboard(View view) { HomePage.closeDrawer(drawerLayout); }

    public void ClickRecords(View view) { HomePage.redirectActivity(this, Records.class); }

    public void ClickDietPlans(View view) { HomePage.redirectActivity(this, DietPlans.class); }

    public void ClickReminders(View view) { HomePage.redirectActivity(this, Reminder_main.class); }

    public void ClickHelp(View view) {HomePage.redirectActivity(this, HelpActivity.class);}

    public void ClickLogout(View view) { HomePage.logout(this); }

    @Override
    protected void onPause() {
        super.onPause();
        HomePage.closeDrawer(drawerLayout);
    }
    //end of navigation drawer

    public void DelDash() {
        /*firebaseFirestore.collection("Dashbaord").document(currentUser).collection("Meals").document("Brealfast").delete();

                firebaseFirestore.collection("Dashbaord").document(currentUser).collection("Meals").document("Lunch").delete();

                firebaseFirestore.collection("Dashbaord").document(currentUser).collection("Meals").document("Dinner").delete();

                firebaseFirestore.collection("Dashbaord").document(currentUser).collection("Meals").document("Snacks").delete();

                firebaseFirestore.collection("Dashbaord").document(currentUser).delete();*/

        Toast.makeText(this, "DelDB", Toast.LENGTH_SHORT).show();
    }

    public void setSuggCal() {
        firebaseFirestore.collection("profiles").document(currentUser).collection("profile categories").document("suggested calories")
                .get()
                .addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
                    @Override
                    public void onComplete(@NonNull @NotNull Task<DocumentSnapshot> task) {
                        if(task.isSuccessful()) {
                            DocumentSnapshot documentSnapshot = task.getResult();
                            if(documentSnapshot.exists()) {
                                Double SuggCal = documentSnapshot.getDouble("SuggestedCal");
                                PlannedCals.setText(SuggCal.toString());
                            }
                        }
                    }
                });
    }

    public void setChart() {
        /*CalVal = BrCal + LuCal + DiCal + SnCal;
        proteinVal = BrProt + LuProt + DiProt + SnProt;
        carbsVal = BrCarb + LuCarb + DiCarb + SnCarb;
        fatsVal = BrFat + LuFat + DiFat + SnFat;*/

        firebaseFirestore.collection("Dashboard").document(currentUser).collection("DashboardRecords").document(simpleDateFormat.format(date))
                .get()
                .addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
                    @Override
                    public void onComplete(@NonNull @NotNull Task<DocumentSnapshot> task) {
                        if(task.isSuccessful()) {
                            DocumentSnapshot documentSnapshot = task.getResult();
                            if(documentSnapshot.exists()) {
                                CalVal = Double.parseDouble(documentSnapshot.get("Calories").toString());
                                proteinVal = Double.parseDouble(documentSnapshot.getDouble("Proteins").toString());
                                carbsVal = Double.parseDouble(documentSnapshot.getDouble("Carbs").toString());
                                fatsVal = Double.parseDouble(documentSnapshot.getDouble("Fats").toString());
                            }
                        }
                    }
                });

        cals.setText(Double.toString(roundTo2Decs(CalVal)));
        proteins.setText(Double.toString(roundTo2Decs(proteinVal)));
        carbs.setText(Double.toString(roundTo2Decs(carbsVal)));
        fats.setText(Double.toString(roundTo2Decs(fatsVal)));

        //creating pie divisions and assigning colours to them
        pieChart.clearChart();

        pieChart.addPieSlice(new PieModel("Protein", Float.parseFloat(proteins.getText().toString()), Color.parseColor("#ff0000")));
        pieChart.addPieSlice(new PieModel("Carbohydrates", Float.parseFloat(carbs.getText().toString()), Color.parseColor("#87ceeb")));
        pieChart.addPieSlice(new PieModel("Fats", Float.parseFloat(fats.getText().toString()), Color.parseColor("#fff700")));

        pieChart.startAnimation();

    }

    /*private void setAlarm(long timeInMillis) {
        AlarmManager alarmManager = (AlarmManager) getSystemService(Context.ALARM_SERVICE);
        Intent intent = new Intent(this, DashDelTime.class);

        PendingIntent pendingIntent = PendingIntent.getBroadcast(this, 0, intent, 0);

        alarmManager.setRepeating(AlarmManager.RTC, timeInMillis, AlarmManager.INTERVAL_DAY, pendingIntent);
    }*/

    private double roundTo2Decs(double value) {
        BigDecimal bd = new BigDecimal(value);
        bd = bd.setScale(2, RoundingMode.HALF_UP);
        return bd.doubleValue();
    }
}