package com.project.healthcompanion.DietPlansClasses;

import android.app.Activity;
import android.app.Dialog;
import android.content.Intent;
import android.graphics.Color;
import android.hardware.camera2.DngCreator;
import android.media.Image;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;

import androidx.activity.result.ActivityResult;
import androidx.activity.result.ActivityResultCallback;
import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.view.GravityCompat;
import androidx.drawerlayout.widget.DrawerLayout;
import androidx.recyclerview.widget.RecyclerView;

import com.android.volley.RequestQueue;
import com.android.volley.toolbox.Volley;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FieldValue;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QuerySnapshot;
import com.google.rpc.Help;
import com.project.healthcompanion.DashboardActivity;
import com.project.healthcompanion.HelpActivity;
import com.project.healthcompanion.HomePage;
import com.project.healthcompanion.Model.Food;
import com.project.healthcompanion.Profile;
import com.project.healthcompanion.R;
import com.project.healthcompanion.Records;
import com.project.healthcompanion.ReminderClasses.Reminder_main;

import org.eazegraph.lib.charts.PieChart;
import org.eazegraph.lib.models.PieModel;
import org.jetbrains.annotations.NotNull;

import android.os.Bundle;
import android.widget.Toast;

import com.android.volley.RequestQueue;
import com.android.volley.toolbox.Volley;
import com.project.healthcompanion.SearchFoodActivity;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class DietPlanner extends AppCompatActivity {

    DrawerLayout drawerLayout;

    //EditText diet_name;

    TextView diet_name, cals, protein, carbs, fats;  //displays the total proteins, carbs and fats
    static TextView diet_name_static;

    PieChart pieChart;  //displays the piechart

    Double CalVal = 0.0;
    Double proteinVal = 0.0;
    Double carbsVal = 0.0;
    Double fatsVal = 0.0;

    Button add_breakfast, add_lunch, add_dinner, add_snacks, dp_confirm; //buttons for adding breakfast, lunch, dinner and snacks

    List<String[]> BreakfastFood = new ArrayList<String[]>();
    List<String> BreakfastItems = new ArrayList<String>();
    List<String[]> LunchFood = new ArrayList<String[]>();
    List<String[]> DinnerFood = new ArrayList<String[]>();
    List<String[]> SnacksFood = new ArrayList<String[]>();

    static ListView BreakfastEntrylist;
    static ArrayList<String> BreakfastEntryItems;
    static BreakfastDisplayAdapter BreakfastEntryAdapter;

    private TextView empty;
    private RecyclerView recyclerView;

    String currentUser = FirebaseAuth.getInstance().getCurrentUser().getUid();
    static String currentUserStatic = FirebaseAuth.getInstance().getCurrentUser().getUid();

    FirebaseFirestore firebaseFirestore;
    static FirebaseFirestore firebaseFirestoreDel;
    CollectionReference BreakfastRef;
    DocumentReference Ref;

    ActivityResultLauncher<Intent> foodResultLauncherForBreakfast = registerForActivityResult(new ActivityResultContracts.StartActivityForResult(), new ActivityResultCallback<ActivityResult>() {
        @Override
        public void onActivityResult(ActivityResult result) {
            if (result.getResultCode() == Activity.RESULT_OK) {
                Intent intent = result.getData();
                assert intent != null;
                Log.d("RetrieveBreakfastTest", "Before variables");
                Food food = (Food) intent.getSerializableExtra("selectedFood");

                int qty = intent.getIntExtra("quantity", 1);

                Log.d("RetrieveBreakfastTest", food.getFood_name() + " | Quantity:" + qty + " | Protein:" + food.getProtein() + " | Carbs" + food.getTotalCarbohydrate() + " | Fats:" + food.getTotalFat());

                //add elements to BreakfastFood array. if an element already exists, the corresponding existing element gets updated.
                int flag = -1;
                for(int i=0; i<BreakfastFood.size(); ++i) {
                    if(BreakfastFood.get(i)[0].equals(food.getFood_name())) {
                        Log.d("BFoodItemExists", "yes");
                        flag = i;
                        //BreakfastFood.get(i)[1] = String.valueOf(Integer.parseInt(BreakfastFood.get(i)[1]) + qty);
                    }
                }

                Log.d("FlagVal", String.valueOf(flag));

                if(flag > -1) { //if food item exists, its qty is added to the existing instance
                    BreakfastFood.get(flag)[1] = String.valueOf(Integer.parseInt(BreakfastFood.get(flag)[1]) + qty);
                }
                else { //if food item is new, its name is added to the BreakfastItems array and its name & qty are added to BreakfastFood
                    BreakfastItems.add(food.getFood_name());
                    BreakfastFood.add(new String[] {food.getFood_name(), String.valueOf(qty)});
                }

                Log.d("BreakfastFoodSize", String.valueOf(BreakfastFood.size()));

                CalVal = CalVal + (food.getCalories() * qty);
                proteinVal = proteinVal + (food.getProtein() * qty);
                carbsVal = carbsVal + (food.getTotalCarbohydrate() * qty);
                fatsVal = fatsVal + (food.getTotalFat() * qty);

                //to show all elements in BreakfastFood array
                for(int i=0; i<BreakfastFood.size(); ++i) {
                    Log.d("BreakfastList", "#" + i + ": " + BreakfastFood.get(i)[0] + "|" + BreakfastFood.get(i)[1]);
                }

                setChart();

                BreakfastEntryItems.clear();
                //BreakfastEntrylist.setAdapter(null);
                for(int i=0; i<BreakfastFood.size(); ++i) {
                    BreakfastEntryItems.add(BreakfastFood.get(i)[0] + "   |   Quantity: " + BreakfastFood.get(i)[1]);
                    Log.d("BreakfastEntryItems", BreakfastEntryItems.toString());
                    BreakfastEntrylist.setAdapter(BreakfastEntryAdapter);
                }
            }
        }
    });

    ActivityResultLauncher<Intent> searchResultLauncherForLunch =
            registerForActivityResult(new ActivityResultContracts.StartActivityForResult(), new ActivityResultCallback<ActivityResult>() {
                @Override
                public void onActivityResult(ActivityResult result) {
                    Intent intent = result.getData();
                    if (intent == null) {
                        Toast.makeText(DietPlanner.this, "Error getting search result", Toast.LENGTH_SHORT).show();
                        return;
                    }
                    Food selectedFood = intent.getParcelableExtra("SearchResult");
                }
            });

    ActivityResultLauncher<Intent> searchResultLauncherForDinner =
            registerForActivityResult(new ActivityResultContracts.StartActivityForResult(), new ActivityResultCallback<ActivityResult>() {
                @Override
                public void onActivityResult(ActivityResult result) {
                    Intent intent = result.getData();
                    if (intent == null) {
                        Toast.makeText(DietPlanner.this, "Error getting search result", Toast.LENGTH_SHORT).show();
                        return;
                    }
                    Food selectedFood = intent.getParcelableExtra("SearchResult");
                }
            });

    ActivityResultLauncher<Intent> searchResultLauncherForSnacks =
            registerForActivityResult(new ActivityResultContracts.StartActivityForResult(), new ActivityResultCallback<ActivityResult>() {
                @Override
                public void onActivityResult(ActivityResult result) {
                    Intent intent = result.getData();
                    if (intent == null) {
                        Toast.makeText(DietPlanner.this, "Error getting search result", Toast.LENGTH_SHORT).show();
                        return;
                    }
                    Food selectedFood = intent.getParcelableExtra("SearchResult");
                    Log.d("SelectedFoodName", selectedFood.getFood_name());
                }
            });

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_diet_planner);

        drawerLayout = findViewById(R.id.drawer_Layout);

        diet_name = findViewById(R.id.diet_name);
        Intent incomingIntent = getIntent();
        String incomingName = incomingIntent.getStringExtra("diet plan name");
        diet_name.setText(incomingName);
        //diet_name_static.setText(incomingName);

        cals = findViewById(R.id.tot_cal);
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



        firebaseFirestore = FirebaseFirestore.getInstance();
        //                                                                                                                                                             diet_name.getText().toString()
        BreakfastRef = firebaseFirestore.collection("Diet Plans").document(currentUser).collection("Diet Planner").document(diet_name.getText().toString()).collection("Meals");

        Ref = firebaseFirestore.collection("Diet Plans").document(currentUser).collection("Diet Planner").document(diet_name.getText().toString()).collection("Meals").document("Breakfast");

        //DispBreakfast();

        setChart();
    }

    //navigation drawer
    public void ClickMenu(View view) { HomePage.openDrawer(drawerLayout); }

    public void ClickLogo(View view) { HomePage.closeDrawer(drawerLayout); }

    public void ClickProfile(View view) { HomePage.redirectActivity(this, Profile.class); }

    public void ClickDashboard(View view) { HomePage.redirectActivity(this, DashboardActivity.class); }

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

    //Piechart
    private void setChart() {
        //setting predefined values

        cals.setText(Double.toString(CalVal));
        protein.setText(Double.toString(proteinVal));
        carbs.setText(Double.toString(carbsVal));
        fats.setText(Double.toString(fatsVal));

        //creating pie divisions and assigning colours to them
        pieChart.addPieSlice(new PieModel("Protein", Float.parseFloat(protein.getText().toString()), Color.parseColor("#ff0000")));
        pieChart.addPieSlice(new PieModel("Carbohydrates", Float.parseFloat(carbs.getText().toString()), Color.parseColor("#87ceeb")));
        pieChart.addPieSlice(new PieModel("Fats", Float.parseFloat(fats.getText().toString()), Color.parseColor("#fff700")));
    }

    public void addBreakfast() {
        //dialog = new Dialog(DietPlanner.this);
        //dialog.setContentView(R.layout.diet_planner_popup);

        EditText dp_search;     //contains search text
        EditText dp_qty;
        ImageView dp_search_btn;
        TextView textView;

        //String nam, qty;

        //dp_qty = findViewById(R.id.dp_qty);
        //dp_search_btn = findViewById(R.id.dp_search_btn);

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

        Intent intent = new Intent(DietPlanner.this, SearchFoodActivity.class);
        foodResultLauncherForBreakfast.launch(intent);

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

        //dialog.show();
    }

    public void DispBreakfast() {
        //display breakfast items
        /*BreakfastEntrylist.setAdapter(null);
        for(int i=0; i<BreakfastFood.size(); ++i) {
            BreakfastEntryItems.add(BreakfastFood.get(i)[0] + "   |   Quantity: " + BreakfastFood.get(i)[1]);
            BreakfastEntrylist.setAdapter(BreakfastEntryAdapter);
        }*/

        /*firebaseFirestore.collection("Diet Plans").document(currentUser).collection("Diet Planner").document(diet_name.getText().toString()).collection("Meals").document("Breakfast")
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

                                                BreakfastEntryItems.add(foodID + "   |   Quantity: " + FoodIDArrayQtyValue.get(i));
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
                });*/
    }

    public static void removeBreakfast(int remove, String Name) {
        BreakfastEntryItems.remove(remove);
        BreakfastEntrylist.setAdapter(BreakfastEntryAdapter);

        firebaseFirestoreDel.collection("Diet Plans").document(currentUserStatic).collection("DietPlanner").document(diet_name_static.getText().toString()).collection("Meals").document("Breakfast").update("Food IDs", FieldValue.arrayRemove(Name));
    }

    public void ConfirmDietPlan() {

        if(BreakfastFood.size() + LunchFood.size() + DinnerFood.size() + SnacksFood.size() != 0)
        {
            //add breakfast items
            firebaseFirestore.collection("Diet Plans").document(currentUser).collection("Diet Planner").document(diet_name.getText().toString()).collection("Meals").document("Breakfast")
                    .get()
                    .addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
                        @Override
                        public void onComplete(@NonNull Task<DocumentSnapshot> task) {
                            if(task.isSuccessful()) {
                                DocumentSnapshot BreakfastSnapshot = task.getResult();
                            /*//if breakfast exists
                            if(BreakfastSnapshot.exists()) {
                                Map<String, Object> BreakfastData2 = new HashMap<>();

                                //BreakfastData2.put("Food IDs", FieldValue.arrayUnion(food.getFood_name()));
                                //BreakfastData2.put(food.getFood_name(), FieldValue.arrayUnion(String.valueOf(qty)));
                                //Ref.update(BreakfastData2);
                                //Log.d("Breakfast Exists", "New Item: " + food.getFood_name() + "|Qty:" + String.valueOf(qty));
                            }*/

                                //if breakfast doesn't exist
                                //else {
                                //Log.d("ReadName", "No Breakfast :/ |", task.getException());

                                firebaseFirestore.collection("Diet Plans").document(currentUser).update("Diet Plan Names", FieldValue.arrayUnion(diet_name.getText().toString()));

                                Map<String, Object> BreakfastData = new HashMap<>();

                                BreakfastData.put("Food IDs", BreakfastItems);

                                for(int i=0; i<BreakfastFood.size(); ++i) {
                                    //BreakfastData.put("Food IDs", FieldValue.arrayUnion(BreakfastFood.get(i)[0]));
                                    BreakfastData.put(BreakfastFood.get(i)[0], BreakfastFood.get(i)[1]);
                                }

                                firebaseFirestore.collection("Diet Plans").document(currentUser).collection("Diet Planner").document(diet_name.getText().toString()).collection("Meals").document("Breakfast")
                                        .set(BreakfastData);

                                /*BreakfastData1.put("Food IDs", FieldValue.arrayUnion(food.getFood_name()));
                                BreakfastData1.put("food.getFood_name()", FieldValue.arrayUnion(String.valueOf(qty)));
                                BreakfastRef.document("Breakfast")
                                        .set(BreakfastData1)
                                        .addOnSuccessListener(new OnSuccessListener<Void>() {
                                            @Override
                                            public void onSuccess(Void v) {
                                                Log.d("Breakfast Doesn't Exist", "New Item: " + food.getFood_name() + "|Qty" + String.valueOf(qty));
                                            }
                                        });*/
                                //}
                            }
                            else {
                                Log.d("ReadName", "No Breakfast :/ |", task.getException());
                            }
                        }
                    });
        }



        Log.d("ConfirmDietPlan", "Diet plan confirmed!");
        Intent intent = new Intent(this, DietPlans.class);
        startActivity(intent);
    }

    public void onBackPressed() {
        firebaseFirestore.collection("Diet Plans").document(currentUser).collection("Diet Planner").document(diet_name.getText().toString())
                .get()
                .addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
                    @Override
                    public void onComplete(@NonNull @NotNull Task<DocumentSnapshot> task) {
                        if(task.isSuccessful()) {
                            DocumentSnapshot backdel = task.getResult();
                            firebaseFirestore.collection("Diet Plans").document(currentUser).update("Diet Plan Names", FieldValue.arrayRemove(diet_name.getText().toString()));
                        }
                    }
                });
        //delete breakfast
        firebaseFirestore.collection("Diet Plans").document(currentUser).collection("Diet Planner").document(diet_name.getText().toString()).collection("Meals").document("Breakfast")
                .get()
                .addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
                    @Override
                    public void onComplete(@NonNull @NotNull Task<DocumentSnapshot> task) {
                        if (task.isSuccessful()) {
                            DocumentSnapshot BSnapshot = task.getResult();
                            if(BSnapshot.exists()) {
                                firebaseFirestore.collection("Diet Plans").document(currentUser).collection("Diet Planner").document(diet_name.getText().toString()).collection("Meals").document("Breakfast").delete();
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
        firebaseFirestore.collection("Diet Plans").document(currentUser).collection("Diet Planner").document(diet_name.getText().toString()).collection("Meals").document("Lunch")
                .get()
                .addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
                    @Override
                    public void onComplete(@NonNull @NotNull Task<DocumentSnapshot> task) {
                        if (task.isSuccessful()) {
                            DocumentSnapshot LSnapshot = task.getResult();
                            if(LSnapshot.exists()) {
                                firebaseFirestore.collection("Diet Plans").document(currentUser).collection("Diet Planner").document(diet_name.getText().toString()).collection("Meals").document("Lunch").delete();
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
        firebaseFirestore.collection("Diet Plans").document(currentUser).collection("Diet Planner").document(diet_name.getText().toString()).collection("Meals").document("Dinner")
                .get()
                .addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
                    @Override
                    public void onComplete(@NonNull @NotNull Task<DocumentSnapshot> task) {
                        if (task.isSuccessful()) {
                            DocumentSnapshot DSnapshot = task.getResult();
                            if(DSnapshot.exists()) {
                                firebaseFirestore.collection("Diet Plans").document(currentUser).collection("Diet Planner").document(diet_name.getText().toString()).collection("Meals").document("Dinner").delete();
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
        firebaseFirestore.collection("Diet Plans").document(currentUser).collection("Diet Planner").document(diet_name.getText().toString()).collection("Meals").document("Snacks")
                .get()
                .addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
                    @Override
                    public void onComplete(@NonNull @NotNull Task<DocumentSnapshot> task) {
                        if (task.isSuccessful()) {
                            DocumentSnapshot SSnapshot = task.getResult();
                            if(SSnapshot.exists()) {
                                firebaseFirestore.collection("Diet Plans").document(currentUser).collection("Diet Planner").document(diet_name.getText().toString()).collection("Meals").document("Snacks").delete();
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