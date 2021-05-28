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
import androidx.core.view.GravityCompat;
import androidx.drawerlayout.widget.DrawerLayout;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

public class HomePage extends AppCompatActivity {

    DrawerLayout drawerLayout;
    private static FirebaseAuth mAuth;


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

    public void ClickProfile(View view) {
        //redirectActivity(this, Profile.class);
    }

    public void ClickHome(View view) {
        closeDrawer(drawerLayout);
    }


    public void ClickDashboard(View view) {
        //redirectActivity(this, Dashboard.class);
    }

    public void ClickGraphs(View view) {
        //redirectActivity(this, Graphs.class);
    }

    public void ClickDietPlans(View view) {
        //redirectActivity(this, DietPlans.class);
    }


    public void ClickRecipes(View view) {
        //redirectActivity(this, Recipes.class);
    }

    public void ClickReminders(View view) {
        redirectActivity(this, Reminders.class);
    }

    public void ClickSocial(View view) {
        //redirectActivity(this, Social.class);
    }


    public void ClickLogout(View view) {
        logout(this);
    }

    public static void logout(Activity activity) {
        AlertDialog.Builder builder = new AlertDialog.Builder(activity);
        builder.setTitle("Logout");
        builder.setMessage("Are you sure you want to log out?");
        builder.setPositiveButton("YES", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                mAuth.signOut();
                //activity.finishAffinity();
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

    //press back twice to exit
    private boolean backPressedOnce = false;
    private Toast t;

    @Override
    public void onBackPressed() {
        if (backPressedOnce) {
            t.cancel();
            finish();
        }
        backPressedOnce = true;
        t = Toast.makeText(this, "Press back again to exit", Toast.LENGTH_SHORT);


        new Handler(Looper.getMainLooper()).postDelayed(new Runnable() {
            @Override
            public void run() {
                backPressedOnce = false;
            }
        }, 2000);
    }
}