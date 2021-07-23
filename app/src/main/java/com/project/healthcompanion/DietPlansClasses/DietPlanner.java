package com.project.healthcompanion.DietPlansClasses;

import android.app.Dialog;
import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.drawerlayout.widget.DrawerLayout;
import androidx.recyclerview.widget.RecyclerView;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FieldValue;
import com.google.firebase.firestore.FirebaseFirestore;
import com.project.healthcompanion.DashboardActivity;
import com.project.healthcompanion.HomePage;
import com.project.healthcompanion.R;
import com.project.healthcompanion.Records;
import com.project.healthcompanion.ReminderClasses.Reminder_main;

import org.eazegraph.lib.charts.PieChart;
import org.eazegraph.lib.models.PieModel;
import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

public class DietPlanner extends AppCompatActivity {

    DrawerLayout drawerLayout;

    //EditText diet_name;

    TextView diet_name, protein, carbs, fats;  //displays the total proteins, carbs and fats

    PieChart pieChart;  //displays the piechart

    Button add_breakfast, add_lunch, add_dinner, add_snacks, dp_confirm; //buttons for adding breakfast, lunch, dinner and snacks

    Dialog dialog;  //used to show search bar popup

    static ListView BreakfastEntrylist;
    static ArrayList<String> BreakfastEntryItems;
    static BreakfastDisplayAdapter BreakfastEntryAdapter;

    private TextView empty;
    private RecyclerView recyclerView;

    FirebaseFirestore firebaseFirestore;
    CollectionReference BreakfastRef;
    DocumentReference Ref;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_diet_planner);

        drawerLayout = findViewById(R.id.drawer_Layout);

        diet_name = findViewById(R.id.diet_name);
        Intent incomingIntent = getIntent();
        String incomingName = incomingIntent.getStringExtra("diet plan name");
        diet_name.setText(incomingName);

        protein = findViewById(R.id.Protein);
        carbs = findViewById(R.id.Carbs);
        fats = findViewById(R.id.Fats);
        pieChart = findViewById(R.id.piechart);

        add_breakfast = findViewById(R.id.add_breakfast);
        dp_confirm = findViewById(R.id.dp_confirm);

        empty = findViewById(R.id.deitplan_empty_text);

        BreakfastEntrylist = findViewById(R.id.breakfast_entry_list);
        BreakfastEntryItems = new ArrayList<>();
        BreakfastEntryAdapter = new BreakfastDisplayAdapter(getApplicationContext(), BreakfastEntryItems);

        add_breakfast.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                addBreakfast();
            }
        });

        dp_confirm.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ConfirmDietPlan();
            }
        });

        //String currentUser = FirebaseAuth.getInstance().getCurrentUser().getUid();

        firebaseFirestore = FirebaseFirestore.getInstance();
        //                                                                                                                                                             diet_name.getText().toString()
        BreakfastRef = firebaseFirestore.collection("Diet Plans").document("UID Generated Test").collection("Diet Planner").document(diet_name.getText().toString()).collection("Meals");

        Ref = firebaseFirestore.collection("Diet Plans").document("UID Generated Test").collection("Diet Planner").document(diet_name.getText().toString()).collection("Meals").document("Breakfast");

        //DispBreakfast();

        setChart();
    }

    //navigation drawer
    public void ClickMenu(View view) { HomePage.openDrawer(drawerLayout); }

    public void ClickLogo(View view) { HomePage.closeDrawer(drawerLayout); }

    public void ClickProfile(View view) { /*HomePage.redirectActivity(this, Profile.class);*/ }

    public void ClickDashboard(View view) { HomePage.redirectActivity(this, DashboardActivity.class); }

    public void ClickRecords(View view) { HomePage.redirectActivity(this, Records.class); }

    public void ClickDietPlans(View view) { HomePage.redirectActivity(this, DietPlans.class); }

    public void ClickReminders(View view) { HomePage.redirectActivity(this, Reminder_main.class); }

    public void ClickLogout(View view) { HomePage.logout(this); }

    @Override
    protected void onPause() {
        super.onPause();
        HomePage.closeDrawer(drawerLayout);
    }
    //end of navigation drawer

    //Piechart
    private void setChart() {
        //setting predefined values
        protein.setText(Integer.toString(0));
        carbs.setText(Integer.toString(0));
        fats.setText(Integer.toString(0));

        //creating pie divisions and assigning colours to them
        pieChart.addPieSlice(new PieModel("Protein", Integer.parseInt(protein.getText().toString()), Color.parseColor("#ff0000")));
        pieChart.addPieSlice(new PieModel("Carbohydrates", Integer.parseInt(carbs.getText().toString()), Color.parseColor("#87ceeb")));
        pieChart.addPieSlice(new PieModel("Fats", Integer.parseInt(fats.getText().toString()), Color.parseColor("#fff700")));
    }

    public void addBreakfast() {
        dialog = new Dialog(DietPlanner.this);
        dialog.setContentView(R.layout.diet_planner_popup);

        EditText dp_search;     //contains search text
        EditText dp_qty;
        ImageView dp_search_btn;
        TextView textView;

        //String nam, qty;

        dp_search = findViewById(R.id.dp_search);
        //dp_qty = findViewById(R.id.dp_qty);
        //dp_search_btn = findViewById(R.id.dp_search_btn);
        textView = findViewById(R.id.food_list);

        //nam = dp_search.getText().toString();
        //qty = dp_qty.getText().toString();

        //String id = firebaseFirestore.collection("Diet Planner Collection").document().getId();

        //Log.d("AddBreakfast", "Diet Planner id:" + id);

        /*dp_search_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                Map<String, Object> BreakfastData = new HashMap<>();
                BreakfastData.put("Food Items", FieldValue.arrayUnion(dp_search.getText().toString()));
                BreakfastData.put(dp_search.getText().toString(), FieldValue.arrayUnion(dp_qty.getText().toString()));
                BreakfastRef.document("Breakfast")
                        .set(BreakfastData)
                        .addOnSuccessListener(new OnSuccessListener<Void>() {
                            @Override
                            public void onSuccess(Void v) {
                                Log.d("Test1", "Document created :)");
                            }
                        });
            }
        });*/

        //add breakfast items
        firebaseFirestore.collection("Diet Plans").document("UID Generated Test").collection("Diet Planner").document(diet_name.getText().toString()).collection("Meals").document("Breakfast")
                .get()
                .addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
                    @Override
                    public void onComplete(@NonNull Task<DocumentSnapshot> task) {
                        if(task.isSuccessful()) {
                            DocumentSnapshot BreakfastSnapshot = task.getResult();
                            //if breakfast exists
                            if(BreakfastSnapshot.exists()) {
                                Map<String, Object> BreakfastData2 = new HashMap<>();
                                BreakfastData2.put("Food IDs", FieldValue.arrayUnion("lk12ns"));
                                BreakfastData2.put("lk12ns", FieldValue.arrayUnion("3"));
                                Ref.update(BreakfastData2);
                                Log.d("Breakfast Exists", "New Item: lk12ns|3");
                            }
                            //if breakfast doesn't exist
                            else {
                                Log.d("ReadName", "No Breakfast :/ |", task.getException());
                                Map<String, Object> BreakfastData1 = new HashMap<>();
                                BreakfastData1.put("Food IDs", FieldValue.arrayUnion("yu67gt"));
                                BreakfastData1.put("yu67gt", FieldValue.arrayUnion("1"));
                                BreakfastRef.document("Breakfast")
                                        .set(BreakfastData1)
                                        .addOnSuccessListener(new OnSuccessListener<Void>() {
                                            @Override
                                            public void onSuccess(Void v) {
                                                Log.d("Breakfast Doesn't Exist", "New Item: yu67gt|1");
                                            }
                                        });
                            }
                            DispBreakfast();
                            firebaseFirestore.collection("Diet Plans").document("UID Generated Test").update("Diet Plan Names", FieldValue.arrayUnion(diet_name.getText().toString()));
                        }
                        else {
                            Log.d("ReadName", "No Breakfast :/ |", task.getException());
                        }
                    }
                });



        // Automically add to the array field.
        //BreakfastRef.update("Food Items", FieldValue.arrayUnion("69420"));

        // Automically remove from the array field.
        //BreakfastRef.update("Food Items", FieldValue.arrayRemove("69420"));

        //TODO: Change this to a write function for array items
        /*BreakfastRef.set("Breakfast").addOnSuccessListener(new OnSuccessListener<Void>() {
            @Override
            public void onSuccess(@NonNull Task<DocumentSnapshot> task) {
                if (task.isSuccessful()) {
                    DocumentSnapshot document = task.getResult();
                        BreakfastRef.update("Food Items", FieldValue.arrayUnion("69420"));
                        Log.d("Test1", "DocumentSnapshot data: " + document.getData());
                }
                else {
                    Log.d("Test1", "get failed with ", task.getException());
                }
            }
        });*/

        /*Map<String, Object> BreakfastData = new HashMap<>();
        BreakfastData.put("Food Items", FieldValue.arrayUnion("69420"));
        BreakfastData.put("69420", FieldValue.arrayUnion("40"));
        BreakfastRef.document("Breakfast")
                .set(BreakfastData)
                .addOnSuccessListener(new OnSuccessListener<Void>() {
            @Override
            public void onSuccess(Void v) {
                    Log.d("Test1", "Document created :)");
            }
        });*/

        dialog.show();
    }

    public void DispBreakfast() {
        //display breakfast items
        firebaseFirestore.collection("Diet Plans").document("UID Generated Test").collection("Diet Planner").document(diet_name.getText().toString()).collection("Meals").document("Breakfast")
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
                                    Log.d("FoodIDsQtysMapField", "\n FoodIDsQuantitiesMapField.getKey():" + FoodIDsQuantitiesMapField.getKey() + "\n FoodIDsQuantitiesMapField.getValue():" + FoodIDsQuantitiesMapField.getValue());

                                    String foodID;

                                    if(!FoodIDsQuantitiesMapField.getKey().equals("Food IDs")) {
                                        foodID = FoodIDsQuantitiesMapField.getKey();
                                        Log.d("foodID", foodID);

                                        ArrayList<String> FoodIDArrayQtyValue = (ArrayList<String>) FoodIDsQuantitiesMapField.getValue();
                                        for(int i=0; i<FoodIDArrayQtyValue.size(); ++i)
                                        {
                                            if(!FoodIDsQuantitiesMapField.getKey().equals("Food IDs")){
                                                Log.d("FoodIDArrayQtyValue", FoodIDArrayQtyValue.get(i) + "| Size:" + FoodIDArrayQtyValue.size());

                                                BreakfastEntryItems.add(foodID + "   |Quantity: " + FoodIDArrayQtyValue.get(i));
                                                Log.d("List Test", foodID + "   |Quantity: " + FoodIDArrayQtyValue.get(i));
                                                BreakfastEntrylist.setAdapter(BreakfastEntryAdapter);
                                            }

                                        }
                                    }
                                }
                            }
                            else {
                                Log.d("ReadName", "No Breakfast :/ |", task.getException());
                            }
                        }
                        else {
                            Log.d("ReadName", "No Breakfast :/ |", task.getException());
                        }
                    }
                });
    }

    public static void removeBreakfast(int remove) {
        BreakfastEntryItems.remove(remove);
        BreakfastEntrylist.setAdapter(BreakfastEntryAdapter);
    }

    public void ConfirmDietPlan() {
        /*String currentUser = FirebaseAuth.getInstance().getCurrentUser().getUid();

        String DNam = diet_name.getText().toString();

        Map<String, Object> dietplan = new HashMap<>();
        dietplan.put("meal name", DNam);
        dietplan.put("UID", currentUser);

        Log.d("WriteDP", "|MealName:" + DNam + ", UserID:" + currentUser);

        firebaseFirestore.collection("Diet Planner Collection")
                .add(dietplan)
                .addOnSuccessListener(new OnSuccessListener<DocumentReference>() {
                    @Override
                    public void onSuccess(DocumentReference documentReference) {
                        Log.d("WriteDP", "DocumentSnapshot written with ID: " + documentReference.getId() + "|MealName:" + DNam);
                    }
                })
                .addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        Log.w("WriteDP", "Error adding document", e);
                    }
                });*/
        Log.d("ConfirmDietPlan", "Diet plan confirmed!");
        Intent intent = new Intent(this, DietPlans.class);
        startActivity(intent);
    }

    public void onBackPressed() {
        firebaseFirestore.collection("Diet Plans").document("UID Generated Test").collection("Diet Planner").document(diet_name.getText().toString())
                .get()
                .addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
                    @Override
                    public void onComplete(@NonNull @NotNull Task<DocumentSnapshot> task) {
                        if(task.isSuccessful()) {
                            DocumentSnapshot backdel = task.getResult();
                            firebaseFirestore.collection("Diet Plans").document("UID Generated Test").update("Diet Plan Names", FieldValue.arrayRemove(diet_name.getText().toString()));
                        }
                    }
                });
        //delete breakfast
        firebaseFirestore.collection("Diet Plans").document("UID Generated Test").collection("Diet Planner").document(diet_name.getText().toString()).collection("Meals").document("Breakfast")
                .get()
                .addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
                    @Override
                    public void onComplete(@NonNull @NotNull Task<DocumentSnapshot> task) {
                        if (task.isSuccessful()) {
                            DocumentSnapshot BSnapshot = task.getResult();
                            if(BSnapshot.exists()) {
                                firebaseFirestore.collection("Diet Plans").document("UID Generated Test").collection("Diet Planner").document(diet_name.getText().toString()).collection("Meals").document("Breakfast").delete();
                            }
                            else {
                                Log.d("BreakfastDel", "Breakfast Doesn't Exist");
                            }
                        }
                        else {
                            Log.d("BreakfastDel", "Breakfast Doesn't Exist");
                        }
                    }
                });
        //delete lunch
        firebaseFirestore.collection("Diet Plans").document("UID Generated Test").collection("Diet Planner").document(diet_name.getText().toString()).collection("Meals").document("Lunch")
                .get()
                .addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
                    @Override
                    public void onComplete(@NonNull @NotNull Task<DocumentSnapshot> task) {
                        if (task.isSuccessful()) {
                            DocumentSnapshot LSnapshot = task.getResult();
                            if(LSnapshot.exists()) {
                                firebaseFirestore.collection("Diet Plans").document("UID Generated Test").collection("Diet Planner").document(diet_name.getText().toString()).collection("Meals").document("Lunch").delete();
                            }
                            else {
                                Log.d("LunchDel", "Lunch Doesn't Exist");
                            }
                        }
                        else {
                            Log.d("LunchDel", "Lunch Doesn't Exist");
                        }
                    }
                });
        //delete dinner
        firebaseFirestore.collection("Diet Plans").document("UID Generated Test").collection("Diet Planner").document(diet_name.getText().toString()).collection("Meals").document("Dinner")
                .get()
                .addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
                    @Override
                    public void onComplete(@NonNull @NotNull Task<DocumentSnapshot> task) {
                        if (task.isSuccessful()) {
                            DocumentSnapshot DSnapshot = task.getResult();
                            if(DSnapshot.exists()) {
                                firebaseFirestore.collection("Diet Plans").document("UID Generated Test").collection("Diet Planner").document(diet_name.getText().toString()).collection("Meals").document("Dinner").delete();
                            }
                            else {
                                Log.d("SnacksDel", "Dinner Doesn't Exist");
                            }
                        }
                        else {
                            Log.d("SnacksDel", "Dinner Doesn't Exist");
                        }
                    }
                });
        //delete snacks
        firebaseFirestore.collection("Diet Plans").document("UID Generated Test").collection("Diet Planner").document(diet_name.getText().toString()).collection("Meals").document("Snacks")
                .get()
                .addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
                    @Override
                    public void onComplete(@NonNull @NotNull Task<DocumentSnapshot> task) {
                        if (task.isSuccessful()) {
                            DocumentSnapshot SSnapshot = task.getResult();
                            if(SSnapshot.exists()) {
                                firebaseFirestore.collection("Diet Plans").document("UID Generated Test").collection("Diet Planner").document(diet_name.getText().toString()).collection("Meals").document("Snacks").delete();
                            }
                            else {
                                Log.d("SnacksDel", "Snacks Doesn't Exist");
                            }
                        }
                        else {
                            Log.d("SnacksDel", "Snacks Doesn't Exist");
                        }
                    }
                });
        HomePage.redirectActivity(this, DietPlans.class);
    }
}