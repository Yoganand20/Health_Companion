package com.project.healthcompanion.DietPlansClasses;

import android.graphics.Color;
import android.os.Bundle;
import android.view.View;

import androidx.appcompat.app.AppCompatActivity;
import androidx.drawerlayout.widget.DrawerLayout;
import androidx.viewpager2.adapter.FragmentStateAdapter;

import com.google.android.material.tabs.TabLayoutMediator;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.FirebaseFirestore;
import com.project.healthcompanion.DashboardActivity;
import com.project.healthcompanion.HelpActivity;
import com.project.healthcompanion.HomePage;
import com.project.healthcompanion.Model.Meal;
import com.project.healthcompanion.Profile;
import com.project.healthcompanion.R;
import com.project.healthcompanion.Records;
import com.project.healthcompanion.ReminderClasses.Reminder_main;
import com.project.healthcompanion.databinding.ActivityDietPlannerBinding;

import org.eazegraph.lib.models.PieModel;

import java.util.ArrayList;

public class DietPlannerActivity extends AppCompatActivity implements
        IMealUpdater {

    DrawerLayout drawerLayout;

    final String[] mealNames = new String[]{
            "Breakfast", "Lunch", "Dinner", "Snacks"
    };

    ArrayList<Meal> dietPlan = new ArrayList<>();
    String dietPlanName;
    //view binding
    ActivityDietPlannerBinding binding;

    String currentUser = FirebaseAuth.getInstance().getCurrentUser().getUid();

    FirebaseFirestore firebaseFirestore;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityDietPlannerBinding.inflate(getLayoutInflater());
        View view = binding.getRoot();
        setContentView(view);

        Bundle extras = getIntent().getExtras();
        if (extras != null) {
            dietPlanName = extras.getString("diet plan name");
            binding.editTextDietName.setText(dietPlanName);
        }
        //seting meal names
        dietPlan = new ArrayList<>();
        for (int i = 0; i < 4; i++) {
            dietPlan.add(new Meal(mealNames[i]));
        }


        //TODO: add code here to initaite piechart it isn't displayed at the start of the activity
        binding.piechart.addPieSlice(new PieModel("Default", 100, Color.parseColor("#ffffff")));
        binding.piechart.startAnimation();

        firebaseFirestore = FirebaseFirestore.getInstance();
        drawerLayout = findViewById(R.id.drawer_Layout);


        //create instance of fragmentStateAdapter 
        FragmentStateAdapter fragmentStateAdapter = new DietPlannerViewPagerAdapter(this, mealNames);
        //set adapter to viewPager
        binding.viewPagerDietPlans.setAdapter(fragmentStateAdapter);


        //set onPageChangeCallback to know which fragment is displayed
        //oly used if showing piechart for each meal
        /*
        binding.viewPagerDietPlans.registerOnPageChangeCallback(new ViewPager2.OnPageChangeCallback() {
            @Override
            public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {
                super.onPageScrolled(position, positionOffset, positionOffsetPixels);
            }

            @Override
            public void onPageSelected(int position) {
                updatePieChart(dietPlan.get(position));
                super.onPageSelected(position);
            }

            @Override
            public void onPageScrollStateChanged(int state) {
                super.onPageScrollStateChanged(state);
            }
        });
         */

        //set tabLayoutMediator to viewPager and tabLayout view
        new TabLayoutMediator(binding.tabLayoutDietPlans, binding.viewPagerDietPlans, ((tab, position) -> tab.setText(mealNames[position]))).attach();


        binding.dpConfirm.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dietPlanName = binding.editTextDietName.getText().toString();
                for (Meal meal :
                        dietPlan) {
                    //use this for loop to access each meal
                    meal.generateFoodMap(); //this will generate and return a Map<String,Object> object where string is food name and object is quantity
                }

                //save to db
            }
        });

    }


    //navigation drawer
    public void ClickMenu(View view) {
        HomePage.openDrawer(drawerLayout);
    }

    public void ClickLogo(View view) {
        HomePage.closeDrawer(drawerLayout);
    }

    public void ClickProfile(View view) {
        HomePage.redirectActivity(this, Profile.class);
    }

    public void ClickDashboard(View view) {
        HomePage.redirectActivity(this, DashboardActivity.class);
    }

    public void ClickRecords(View view) {
        HomePage.redirectActivity(this, Records.class);
    }

    public void ClickDietPlans(View view) {
        HomePage.redirectActivity(this, DietPlans.class);
    }

    public void ClickReminders(View view) {
        HomePage.redirectActivity(this, Reminder_main.class);
    }

    public void ClickHelp(View view) {
        HomePage.redirectActivity(this, HelpActivity.class);
    }

    public void ClickLogout(View view) {
        HomePage.logout(this);
    }

    @Override
    protected void onPause() {
        super.onPause();
        HomePage.closeDrawer(drawerLayout);
    }
    //end of navigation drawer


    @Override
    public void updateMeal(Meal meal) {
        for (int i = 0; i < 4; i++) {
            if (dietPlan.get(i).getMealName().equals(meal.getMealName())) {
                dietPlan.set(i, meal);
                //updatePieChart(meal);
                updatePieChart();
            }
        }
    }

    //piechart for whole meal
    private void updatePieChart() {
        Float totalCal = 0.0f, totalProts = 0.0f, totalCarbs = 0.0f, totalFats = 0.0f;
        for (int i = 0; i < dietPlan.size(); i++) {
            totalCal += dietPlan.get(i).getTotalCalories();
            totalProts += dietPlan.get(i).getTotalProts();
            totalCarbs += dietPlan.get(i).getTotalCarbs();
            totalFats += dietPlan.get(i).getTotalFats();
        }

        binding.totCal.setText(totalCal.toString());
        binding.Protein.setText(totalProts.toString());
        binding.Carbs.setText(totalCarbs.toString());
        binding.Fats.setText(totalFats.toString());

        binding.piechart.clearChart();

        binding.piechart.addPieSlice(new PieModel("Protein", totalProts, Color.parseColor("#ff0000")));
        binding.piechart.addPieSlice(new PieModel("Carbohydrates", totalCarbs, Color.parseColor("#87ceeb")));
        binding.piechart.addPieSlice(new PieModel("Fats", totalFats, Color.parseColor("#fff700")));
        binding.piechart.startAnimation();


    }

    //update piechart for one meal only
    private void updatePieChart(Meal meal) {

        String totalCal = meal.getTotalCalories().toString();
        String totalProts = meal.getTotalProts().toString();
        String totalCarbs = meal.getTotalCarbs().toString();
        String totalFats = meal.getTotalFats().toString();

        binding.totCal.setText(totalCal);
        binding.Protein.setText(totalProts);
        binding.Carbs.setText(totalCarbs);
        binding.Fats.setText(totalFats);
        //cals.setText(Double.toString(roundTo2Decs(CalVal)));
        //protein.setText(Double.toString(roundTo2Decs(proteinVal)));
        // carbs.setText(Double.toString(roundTo2Decs(carbsVal)));
        // fats.setText(Double.toString(roundTo2Decs(fatsVal)));
        binding.piechart.clearChart();
        binding.piechart.addPieSlice(new PieModel("Protein", Float.parseFloat(totalProts), Color.parseColor("#ff0000")));
        binding.piechart.addPieSlice(new PieModel("Carbohydrates", Float.parseFloat(totalCarbs), Color.parseColor("#87ceeb")));
        binding.piechart.addPieSlice(new PieModel("Fats", Float.parseFloat(totalFats), Color.parseColor("#fff700")));


        //pieChart.addPieSlice(new PieModel("Protein", Float.parseFloat(protein.getText().toString()), Color.parseColor("#ff0000")));
        //pieChart.addPieSlice(new PieModel("Carbohydrates", Float.parseFloat(carbs.getText().toString()), Color.parseColor("#87ceeb")));
        // pieChart.addPieSlice(new PieModel("Fats", Float.parseFloat(fats.getText().toString()), Color.parseColor("#fff700")));


        binding.piechart.startAnimation();
        //pieChart.startAnimation();

    }

    @Override
    protected void onDestroy() {
        super.onDestroy();

    }
}
///original code is in file x
