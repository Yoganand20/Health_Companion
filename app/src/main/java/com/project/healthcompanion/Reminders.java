package com.project.healthcompanion;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.view.GravityCompat;
import androidx.drawerlayout.widget.DrawerLayout;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

import static com.project.healthcompanion.HomePage.closeDrawer;
import static com.project.healthcompanion.HomePage.logout;
import static com.project.healthcompanion.HomePage.openDrawer;
import static com.project.healthcompanion.HomePage.redirectActivity;

public class Reminders extends AppCompatActivity implements View.OnClickListener{

    DrawerLayout drawerLayout;

    Button medi_btn;
    Button food_btn;
    Button watr_btn;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_reminders);

        drawerLayout = findViewById(R.id.drawer_Layout);

        medi_btn = findViewById(R.id.medication_button);
        medi_btn.setOnClickListener(this);

        food_btn = findViewById(R.id.food_button);
        food_btn.setOnClickListener(this);

        watr_btn = findViewById(R.id.water_button);
        watr_btn.setOnClickListener(this);
    }

    public void ClickMenu(View view){
        HomePage.openDrawer(drawerLayout);
    }

    public void ClickLogo(View view){
        HomePage.closeDrawer(drawerLayout);
    }

    public void ClickProfile(View view){
        //HomePage.redirectActivity(this, Profile.class);
    }

    public void ClickHome(View view){
        HomePage.redirectActivity(this, HomePage.class);
    }

    public void ClickDashboard(View view){
        //HomePage.redirectActivity(this, Dashboard.class);
    }

    public void ClickGraphs(View view){
        //HomePage.redirectActivity(this, Graphs.class);
    }

    public void ClickDietPlans(View view){
        //HomePage.redirectActivity(this, DietPlans.class);
    }

    public void ClickRecipes(View view){
        //HomePage.redirectActivity(this, Recipes.class);
    }

    public void ClickReminders(View view){
        //HomePage.redirectActivity(this, Reminders.class);
    }

    public void ClickSocial(View view){
        //HomePage.redirectActivity(this, Social.class);
    }

    public void ClickLogout(View view){
        HomePage.logout(this);
    }

    @Override
    public void onClick(View v) {
        if(v.getId()==R.id.medication_button)
        {
            getSupportFragmentManager().beginTransaction().replace(R.id.reminder_display, new Medication_fragment()).commit();
        }

        else if(v.getId()==R.id.food_button)
        {
            getSupportFragmentManager().beginTransaction().replace(R.id.reminder_display, new Food_fragment()).commit();
        }

        else if(v.getId()==R.id.water_button)
        {
            getSupportFragmentManager().beginTransaction().replace(R.id.reminder_display, new Water_fragment()).commit();
        }
    }
}