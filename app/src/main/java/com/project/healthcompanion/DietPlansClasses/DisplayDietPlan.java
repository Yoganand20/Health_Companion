package com.project.healthcompanion.DietPlansClasses;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.drawerlayout.widget.DrawerLayout;

import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.TextView;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.project.healthcompanion.DashboardClasses.DashboardActivity;
import com.project.healthcompanion.HelpActivity;
import com.project.healthcompanion.HomePage;
import com.project.healthcompanion.Profile;
import com.project.healthcompanion.R;
import com.project.healthcompanion.Records;
import com.project.healthcompanion.ReminderClasses.Reminder_main;

import org.eazegraph.lib.charts.PieChart;
import org.eazegraph.lib.models.PieModel;
import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;
import java.util.Map;

public class DisplayDietPlan extends AppCompatActivity {

    DrawerLayout drawerLayout;

    FirebaseFirestore firebaseFirestore;

    TextView breakfast_id_disp, lunch_id_disp, dinner_id_disp, snacks_id_disp;
    TextView breakfast_qty_disp,lunch_qty_disp, dinner_qty_disp, snacks_qty_disp;

    TextView protein, carbs, fats, tot_cal;

    PieChart pieChart;

    String currentUser = FirebaseAuth.getInstance().getCurrentUser().getUid();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_display_diet_plan);

        drawerLayout = findViewById(R.id.drawer_Layout);

        protein = findViewById(R.id.Protein);
        carbs = findViewById(R.id.Carbs);
        fats = findViewById(R.id.Fats);
        tot_cal = findViewById(R.id.tot_cal);
        pieChart = findViewById(R.id.piechart);

        Intent incomingIntent = getIntent();
        String incomingName = incomingIntent.getStringExtra("display diet plan name");
        TextView Header;

        Log.d("Name Test", incomingName);

        Header = (TextView) findViewById(R.id.dp_list_item_header);
        Header.setText(incomingName);

        breakfast_id_disp = findViewById(R.id.breakfast_id_disp);
        lunch_id_disp = findViewById(R.id.lunch_id_disp);
        dinner_id_disp = findViewById(R.id.dinner_id_disp);
        snacks_id_disp = findViewById(R.id.snacks_id_disp);

        breakfast_qty_disp = findViewById(R.id.breakfast_qty_disp);
        lunch_qty_disp = findViewById(R.id.lunch_qty_disp);
        dinner_qty_disp = findViewById(R.id.dinner_qty_disp);
        snacks_qty_disp = findViewById(R.id.snacks_qty_disp);

        setChart();

        firebaseFirestore = FirebaseFirestore.getInstance();

        //Retrieve macros
        firebaseFirestore.collection("Diet Plans").document(currentUser).collection("Diet Planner").document(incomingName)
                .get()
                .addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
                    @Override
                    public void onComplete(@NonNull @NotNull Task<DocumentSnapshot> task) {
                        if(task.isSuccessful()) {
                            DocumentSnapshot MacroSnapshot = task.getResult();
                            if(MacroSnapshot.exists()) {
                                tot_cal.setText(MacroSnapshot.getDouble("Calories").toString());
                                protein.setText(MacroSnapshot.getDouble("Proteins").toString());
                                carbs.setText(MacroSnapshot.getDouble("Carbs").toString());
                                fats.setText(MacroSnapshot.getDouble("Fats").toString());
                            }
                        }
                    }
                });

        //Retrieve breakfast data
        firebaseFirestore.collection("Diet Plans").document(currentUser).collection("Diet Planner").document(incomingName).collection("Meals").document("Breakfast")
                .get()
                .addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
                    @Override
                    public void onComplete(@NonNull Task<DocumentSnapshot> task) {
                        if(task.isSuccessful()) {
                            DocumentSnapshot BreakfastSnapshot = task.getResult();
                            if(BreakfastSnapshot.exists()) {
                                Map<String, Object> map = BreakfastSnapshot.getData();
                                Log.d("Breakfast", "Breakfast:" + map);

                                for (Map.Entry<String, Object> FoodIDsQuantitiesMapField : map.entrySet())
                                {
                                    Log.d("FoodIDsQtyMapField", "\n FoodIDsQuantitiesMapField.getKey():" + FoodIDsQuantitiesMapField.getKey() + "\n FoodIDsQuantitiesMapField.getValue():" + FoodIDsQuantitiesMapField.getValue());

                                    //if(!FoodIDsQuantitiesMapField.getKey().equals("Food IDs")) {
                                    breakfast_id_disp.append(FoodIDsQuantitiesMapField.getKey() + "\n");
                                    breakfast_qty_disp.append("Quantity:" + FoodIDsQuantitiesMapField.getValue() + "\n");
                                    //}
                                }
                            }
                            else {
                                Log.d("ReadBreakfast", "No Breakfast :/ |", task.getException());
                            }
                        }
                        else {
                            Log.d("ReadBreakfast", "No Breakfast :/ |", task.getException());
                        }
                    }
                });

        //Retrieve lunch data
        firebaseFirestore.collection("Diet Plans").document(currentUser).collection("Diet Planner").document(incomingName).collection("Meals").document("Lunch")
                .get()
                .addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
                    @Override
                    public void onComplete(@NonNull Task<DocumentSnapshot> task) {
                        if(task.isSuccessful()) {
                            DocumentSnapshot LunchSnapshot = task.getResult();
                            if(LunchSnapshot.exists()) {
                                Map<String, Object> map = LunchSnapshot.getData();
                                Log.d("Lunch", "Lunch:" + map);

                                for (Map.Entry<String, Object> FoodIDsQuantitiesMapField : map.entrySet())
                                {



                                                //Log.d("FoodIDArrayQtyValue", FoodIDArrayQtyValue.get(i) + "| Size:" + FoodIDArrayQtyValue.size());
                                                //Log.d("FoodIDArrayQtyValue", FoodIDsQuantitiesMapField.getKey());

                                                lunch_id_disp.append(FoodIDsQuantitiesMapField.getKey() + "\n");
                                                lunch_qty_disp.append("Quantity:" + FoodIDsQuantitiesMapField.getValue() + "\n");




                                }
                            }
                            else {
                                Log.d("ReadLunch", "No Lunch :/ |", task.getException());
                            }
                        }
                        else {
                            Log.d("ReadLunch", "No Lunch :/ |", task.getException());
                        }
                    }
                });

        //Retrieve dinner data
        firebaseFirestore.collection("Diet Plans").document(currentUser).collection("Diet Planner").document(incomingName).collection("Meals").document("Dinner")
                .get()
                .addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
                    @Override
                    public void onComplete(@NonNull Task<DocumentSnapshot> task) {
                        if(task.isSuccessful()) {
                            DocumentSnapshot DinnerSnapshot = task.getResult();
                            if(DinnerSnapshot.exists()) {
                                Map<String, Object> map = DinnerSnapshot.getData();
                                Log.d("Dinner", "Dinner:" + map);

                                for (Map.Entry<String, Object> FoodIDsQuantitiesMapField : map.entrySet())
                                {



                                                //Log.d("FoodIDArrayQtyValue", FoodIDArrayQtyValue.get(i) + "| Size:" + FoodIDArrayQtyValue.size());
                                                //Log.d("FoodIDArrayQtyValue", FoodIDsQuantitiesMapField.getKey());

                                                dinner_id_disp.append(FoodIDsQuantitiesMapField.getKey() + "\n");
                                                dinner_qty_disp.append("Quantity:" + FoodIDsQuantitiesMapField.getValue() + "\n");




                                }
                            }
                            else {
                                Log.d("ReadDinner", "No Dinner :/ |", task.getException());
                            }
                        }
                        else {
                            Log.d("ReadDinner", "No Dinner :/ |", task.getException());
                        }
                    }
                });

        //Retrieve snacks data
        firebaseFirestore.collection("Diet Plans").document(currentUser).collection("Diet Planner").document(incomingName).collection("Meals").document("Snacks")
                .get()
                .addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
                    @Override
                    public void onComplete(@NonNull Task<DocumentSnapshot> task) {
                        if(task.isSuccessful()) {
                            DocumentSnapshot SnacksSnapshot = task.getResult();
                            if(SnacksSnapshot.exists()) {
                                Map<String, Object> map = SnacksSnapshot.getData();
                                Log.d("Snacks", "Snacks:" + map);

                                for (Map.Entry<String, Object> FoodIDsQuantitiesMapField : map.entrySet())
                                {



                                                //Log.d("FoodIDArrayQtyValue", FoodIDArrayQtyValue.get(i) + "| Size:" + FoodIDArrayQtyValue.size());
                                                //Log.d("FoodIDArrayQtyValue", FoodIDsQuantitiesMapField.getKey());

                                                snacks_id_disp.append(FoodIDsQuantitiesMapField.getKey() + "\n");
                                                snacks_qty_disp.append("Quantity:" + FoodIDsQuantitiesMapField.getValue() + "\n");




                                }
                            }
                            else {
                                Log.d("ReadSnacks", "No Snacks :/ |", task.getException());
                            }
                        }
                        else {
                            Log.d("ReadSnacks", "No Snacks :/ |", task.getException());
                        }
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

    public void ClickProfile(View view) { HomePage.redirectActivity(this, Profile.class); }

    public void ClickDashboard(View view) { HomePage.redirectActivity(this, DashboardActivity.class); }

    public void ClickRecords(View view) {
        HomePage.redirectActivity(this, Records.class);
    }

    public void ClickDietPlans(View view) {
        HomePage.redirectActivity(this, DietPlans.class);
    }

    public void ClickReminders(View view) {
        HomePage.redirectActivity(this, Reminder_main.class);
    }

    public void ClickHelp(View view) {HomePage.redirectActivity(this, HelpActivity.class);}

    public void ClickLogout(View view) {
        HomePage.logout(this);
    }

    @Override
    protected void onPause() {
        super.onPause();
        HomePage.closeDrawer(drawerLayout);
    }
    //end of navigation drawer

    private void setChart() {
        //setting predefined values
        protein.setText(Integer.toString(23));
        carbs.setText(Integer.toString(200));
        fats.setText(Integer.toString(10));

        //creating pie divisions and assigning colours to them
        pieChart.addPieSlice(new PieModel("Protein", Integer.parseInt(protein.getText().toString()), Color.parseColor("#ff0000")));
        pieChart.addPieSlice(new PieModel("Carbohydrates", Integer.parseInt(carbs.getText().toString()), Color.parseColor("#87ceeb")));
        pieChart.addPieSlice(new PieModel("Fats", Integer.parseInt(fats.getText().toString()), Color.parseColor("#fff700")));

        pieChart.startAnimation();
    }
}