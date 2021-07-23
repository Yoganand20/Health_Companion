package com.project.healthcompanion;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.view.View;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.view.GravityCompat;
import androidx.drawerlayout.widget.DrawerLayout;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.project.healthcompanion.DietPlansClasses.DietPlans;
import com.project.healthcompanion.LogInAndSignUp.LoginNSignUpActivity;
import com.project.healthcompanion.ReminderClasses.Reminder_main;

public class HomePage extends AppCompatActivity {

    DrawerLayout drawerLayout;

    private FirebaseAuth mAuth;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home_page);
        // get instance of firebaseAuth
        mAuth = FirebaseAuth.getInstance();

        //get current user if logged in
        FirebaseUser currentUser = mAuth.getCurrentUser();

        drawerLayout = findViewById(R.id.drawer_Layout);

    }

    //navigation drawer
    public void ClickMenu(View view) {
        openDrawer(drawerLayout);
    }

    public static void openDrawer(DrawerLayout drawerLayout) {
        drawerLayout.openDrawer(GravityCompat.START);
    }


    public void ClickLogo(View view) {
        closeDrawer(drawerLayout);
    }

    public static void closeDrawer(DrawerLayout drawerLayout) {
        if (drawerLayout.isDrawerOpen(GravityCompat.START)) {
            drawerLayout.closeDrawer(GravityCompat.START);
        }
    }

    public void ClickProfile(View view) { redirectActivity(this, Profile.class); }

    public void ClickHome(View view) {
        HomePage.redirectActivity(this, HomePage.class);
    }

    public void ClickDashboard(View view) { redirectActivity(this, DashboardActivity.class); }

    public void ClickRecords(View view) { redirectActivity(this, Records.class); }

    public void ClickDietPlans(View view) { redirectActivity(this, DietPlans.class); }

    public void ClickReminders(View view) { redirectActivity(this, Reminder_main.class); }

    public void ClickLogout(View view) { logout(this); }

    public static void logout(Activity activity) {
        AlertDialog.Builder builder = new AlertDialog.Builder(activity);
        builder.setTitle("Logout");
        builder.setMessage("Are you sure you want to log out?");
        builder.setPositiveButton("YES", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                FirebaseAuth.getInstance().signOut();
                Intent intent = new Intent(activity, LoginNSignUpActivity.class);
                intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
                activity.startActivity(intent);
                //System.exit(0);
            }
        });
        builder.setNegativeButton("NO", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                //dismiss dialogue
                dialog.dismiss();
            }
        });
        builder.show();
    }

    public static void redirectActivity(Activity activity, Class aClass) {
        Intent intent = new Intent(activity, aClass);
        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        activity.startActivity(intent);
    }

    @Override
    protected void onPause() {
        super.onPause();
        closeDrawer(drawerLayout);
    }
    //end of navigation drawer

    //press back twice to exit
    private boolean backPressedOnce = false;
    private Toast t;

    @Override
    public void onBackPressed() {
        if (backPressedOnce) {
            t.cancel();
            ActivityCompat.finishAffinity(HomePage.this);
            finish();
        }
        backPressedOnce = true;
        t = Toast.makeText(this, "Press back again to exit", Toast.LENGTH_SHORT);
        t.show();
        new Handler(Looper.getMainLooper()).postDelayed(new Runnable() {
            @Override
            public void run() {
                backPressedOnce = false;
            }
        }, 2000);
    }
}