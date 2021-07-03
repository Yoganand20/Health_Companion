package com.project.healthcompanion.DietPlansClasses;

import android.app.Dialog;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.drawerlayout.widget.DrawerLayout;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;


import com.github.mikephil.charting.data.Entry;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FieldValue;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.Query;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;
import com.project.healthcompanion.DashboardActivity;
import com.project.healthcompanion.HomePage;
import com.project.healthcompanion.R;
import com.project.healthcompanion.Records;
import com.project.healthcompanion.ReminderClasses.Reminder_main;

import org.jetbrains.annotations.NotNull;
import org.w3c.dom.Document;
import org.w3c.dom.Text;

import java.lang.reflect.Array;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Map;

public class DietPlans extends AppCompatActivity {

    DrawerLayout drawerLayout;

    private TextView empty;     //Text that appear when the page is empty
    private ImageView add;  //plus button at top right
    EditText new_diet_plan_name;

    static ListView listView;
    static ArrayList<String> listViewItems;
    static DietPlansListAdapter listViewAdapter;

    static FirebaseFirestore firebaseFirestoreDel;
    FirebaseFirestore firebaseFirestore;
    CollectionReference DPRefName, DPRefMeals;

    Dialog dp_dialog, nam_dialog;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_diet_plans);

        drawerLayout = findViewById(R.id.drawer_Layout);

        add = findViewById(R.id.add_dietplan);
        new_diet_plan_name = findViewById(R.id.new_diet_plan_name);
        empty = findViewById(R.id.deitplan_empty_text);

        listView = findViewById(R.id.diet_plan_list);
        listViewItems = new ArrayList<>();
        listViewAdapter = new DietPlansListAdapter(getApplication(), listViewItems);


        add.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                addDietPlans();
            }
        });

        //String currentUser = FirebaseAuth.getInstance().getCurrentUser().getUid();

        firebaseFirestoreDel = FirebaseFirestore.getInstance();
        firebaseFirestore = FirebaseFirestore.getInstance();
        DPRefName = firebaseFirestore.collection("Diet Plans").document("UID Generated").collection("Diet Planner");
        DPRefMeals = firebaseFirestore.collection("Diet Plans").document("UID Generated").collection("Diet Planner").document("Unique Diet Plan Name").collection("Meals");

        /*City city = new City("Los Angeles", "CA", "USA",
                false, 5000000L, Arrays.asList("west_coast", "sorcal"));

        firebaseFirestore.collection("Diet Plans").document("Current User").set(city);
        Log.d("CITY", "Name:" + city.name + "State:" + city.state + "Country:" + city.country + "Capital:" + city.capital + "Pop:" + city.population);*/

        //firebaseFirestore.collection("Diet Plans").document("Current User").update("array of objects.Food ID", FieldValue.arrayRemove("g34gf934f"));

        //Object object = new Object("abcdef", 19);
        //firebaseFirestore.collection("Dier Plans").document("Current User").update({View 'array of objects': firestore.FieldValue.arrayRemove(object)});

        //deleteItem();

        setItemsInDietPlans();
    }

    //navigation drawer
    public void ClickMenu(View view) {
        HomePage.openDrawer(drawerLayout);
    }

    public void ClickLogo(View view) {
        HomePage.closeDrawer(drawerLayout);
    }

    public void ClickProfile(View view) { /*HomePage.redirectActivity(this, Profile.class);*/ }

    public void ClickHome(View view) {
        HomePage.redirectActivity(this, HomePage.class);
    }

    public void ClickDashboard(View view) { HomePage.redirectActivity(this, DashboardActivity.class); }

    public void ClickRecords(View view) {
        HomePage.redirectActivity(this, Records.class);
    }

    public void ClickDietPlans(View view) {
        HomePage.closeDrawer(drawerLayout);
    }

    public void ClickReminders(View view) {
        HomePage.redirectActivity(this, Reminder_main.class);
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

    //adds diet plans
    public void addDietPlans() {

        firebaseFirestore.collection("Diet Plans").document("UID Generated Test").get().addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
            @Override
            public void onComplete(@NonNull Task<DocumentSnapshot> task) {
                int flag=0;
                int i=0;
                if (task.isSuccessful()) {
                    Log.d("IfSuccessful", "Check if doc access is successful");
                    DocumentSnapshot document = task.getResult();
                    if (document.exists()) {
                        Log.d("IfExists", "Check if doc exists");
                        List<Object> check_diet_plans_names = (List<Object>) document.get("Diet Plan Names");
                        for (Object o : check_diet_plans_names) {
                            //flag = 0;
                            Log.d("CheckDPName", "List element " + o.toString() + "|Entered name:" + new_diet_plan_name.getText().toString());
                            if(o.toString().equals(new_diet_plan_name.getText().toString())) {
                                Log.d("CheckDPName", "This diet plan already exists");
                                Toast.makeText(DietPlans.this, "Diet Plan already exists!", Toast.LENGTH_SHORT).show();
                                break;
                            }
                            else {
                                Log.d("CheckDPName", "This is a new diet plan!");
                                firebaseFirestore.collection("Diet Plans").document("UID Generated Test").update("Diet Plan Names", FieldValue.arrayUnion(new_diet_plan_name.getText().toString()));
                                ++flag;
                            }
                            ++i;
                        }
                        Log.d("CheckEntry", "Before external if statement");
                        if(i == check_diet_plans_names.size()) {
                            Log.d("CheckEntry", "Entered external if statement");
                            if(flag>0) {
                                Log.d("CheckEntry", "Entered external if statement's internal if statement");
                                Intent intent = new Intent(DietPlans.this, DietPlanner.class);
                                intent.putExtra("diet plan name", new_diet_plan_name.getText().toString());
                                startActivity(intent);
                            }
                        }
                    }
                    else {
                        Log.d("CheckDPName", "***No such document");
                    }
                }
                else {
                    Log.d("CheckDPName", "***Get failed with ", task.getException());
                }
            }
        });


        /*Intent intent = new Intent(DietPlans.this, DietPlanner.class);
        intent.putExtra("diet plan name", new_diet_plan_name.getText().toString());
        startActivity(intent);*/
    }

    //checks of there is a diet plan or not. if there is a diet plan, it makes the dietplan_empty_text invisible
    public void setItemsInDietPlans() {
        /*if(temp.size > 0) {
            empty.setVisibility(View.INVISIBLE);
            recyclerView.setVisibility(View.VISIBLE);
        }*/

        //adapterDietPlans = new AdapterDietPlans(temp);
        //recyclerView.setAdapter(adapterDietPlans);

        /*DPRefName.get().addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
            @Override
            public void onComplete(@NonNull @NotNull Task<QuerySnapshot> task) {
                //ArrayList<Entry> lineEntries = new ArrayList<Entry>();

                if(task.isSuccessful()) {
                    empty.setVisibility(View.INVISIBLE);
                    //recyclerView.setVisibility(View.VISIBLE);
                    for(QueryDocumentSnapshot document : task.getResult()) {

                        String Nam = document.getString("Name");

                        Log.d("ReadName", document.getId() + " => " + document.getData() + "Dietplan name:" + Nam);
                        //Log.d("ReadName", "After diet plan name");




                        //Log.d("ReadName", "After reading all meals");
                    }
                }
                else {
                    Log.d("ReadName", "No diet plans :/", task.getException());

                    empty.setVisibility(View.VISIBLE);
                    //recyclerView.setVisibility(View.INVISIBLE);
                }
            }
        });*/

        //ArrayList<String> DietPlanNames1=new ArrayList<>();

        firebaseFirestore.collection("Diet Plans").document("UID Generated Test")
                .get()
                .addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
                    @Override
                    public void onComplete(@NonNull Task<DocumentSnapshot> task) {
                        if(task.isSuccessful()) {
                            empty.setVisibility(View.INVISIBLE);
                            DocumentSnapshot documentSnapshot = task.getResult();
                            ArrayList<String> DietPlanNames = (ArrayList<String>) documentSnapshot.get("Diet Plan Names");
                            Log.d("ReadName", documentSnapshot.getId() + " => " + documentSnapshot.getData() /*+ "arraylist:" + DietPlanNames + "Array Size:" + DietPlanNames.size()*/);

                            Log.d("ReadName", "Number of diet plans:" + DietPlanNames.size());
                            Log.d("ReadName", "Diet Plan names:");
                            for(int i=0; i<DietPlanNames.size(); ++i) {
                                Log.d("ReadName", DietPlanNames.get(i));
                                //DietPlanNames1.add(DietPlanNames.get(i));

                                //start of list view code
                                listViewItems.add(DietPlanNames.get(i));
                                listView.setAdapter(listViewAdapter);


                                listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                                    @Override
                                    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                                        //Toast.makeText(DietPlans.this, "Dietplan name: \n" + listViewAdapter.getItem(position), Toast.LENGTH_SHORT).show();

                                        Intent dispDP = new Intent(DietPlans.this, DisplayDietPlan.class);
                                        String dpnam = listViewAdapter.getItem(position);
                                        dispDP.putExtra("display diet plan name", dpnam);
                                        startActivity(dispDP);

                                        //DocumentReference CheckMeals = firebaseFirestore.collection("Diet Plans").document("UID Generated Test").collection("Diet Planner").document(listViewAdapter.getItem(position));


                                        /*firebaseFirestore.collection("Diet Plans").document("UID Generated Test").collection("Diet Planner").document(listViewAdapter.getItem(position)).collection("Meals").document("Breakfast")
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
                                                                    Log.d("FoodIDsQuantitiesMapField Test", "\n FoodIDsQuantitiesMapField.getKey():" + FoodIDsQuantitiesMapField.getKey() + "\n FoodIDsQuantitiesMapField.getValue():" + FoodIDsQuantitiesMapField.getValue());

                                                                    String foodID;

                                                                    if(!FoodIDsQuantitiesMapField.getKey().equals("Food IDs")) {
                                                                        foodID = FoodIDsQuantitiesMapField.getKey();
                                                                        Log.d("foodID", foodID);

                                                                        ArrayList<String> FoodIDArrayQtyValue = (ArrayList<String>) FoodIDsQuantitiesMapField.getValue();
                                                                        for(int i=0; i<FoodIDArrayQtyValue.size(); ++i)
                                                                        {
                                                                            if(!FoodIDsQuantitiesMapField.getKey().equals("Food Items")) {
                                                                                //Log.d("FoodIDArrayQtyValue", FoodIDArrayQtyValue.get(i) + "| Size:" + FoodIDArrayQtyValue.size());
                                                                                Log.d("FoodIDArrayQtyValue", FoodIDArrayQtyValue.get(i));
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

                                        /*dp_dialog = new Dialog(DietPlans.this);
                                        dp_dialog.setContentView(R.layout.diet_planner_list);

                                        //TODO: try this shit out v
                                        TextView dpheader, breakfastitems;
                                        dpheader = findViewById(R.id.dp_list_item_header);
                                        breakfastitems = findViewById(R.id.breakfast_items);

                                        dpheader.setText(listViewAdapter.getItem(position));

                                        //dpheader = DietPlanNames.get(i);
                                        //breakfastitems =  BreakfastItems;
                                        //breakfastitems.append(BreakfastItems);

                                        dp_dialog.show();*/
                                    }
                                });
                                //end of list view code

                                /*firebaseFirestore.collection("Diet Plans").document("UID Generated Test").collection("Diet Planner").document(DietPlanNames.get(i)).collection("Meals").document("Breakfast")
                                        .get()
                                        .addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
                                            @Override
                                            public void onComplete(@NonNull Task<DocumentSnapshot> task) {
                                                if(task.isSuccessful()) {
                                                    DocumentSnapshot BreakfastSnapshot = task.getResult();

                                                    Map<String, Object> map = BreakfastSnapshot.getData();
                                                    Log.d("Breakfast", "Breakfast:" + map);

                                                        for (Map.Entry<String, Object> FoodIDsQuantitiesMapField : map.entrySet())
                                                        {
                                                            Log.d("FoodIDsQuantitiesMapField Test", "\n FoodIDsQuantitiesMapField.getKey():" + FoodIDsQuantitiesMapField.getKey() + "\n FoodIDsQuantitiesMapField.getValue():" + FoodIDsQuantitiesMapField.getValue());

                                                            ArrayList<String> FoodIDArrayQtyValue = (ArrayList<String>) FoodIDsQuantitiesMapField.getValue();
                                                            for(int i=0; i<FoodIDArrayQtyValue.size(); ++i)
                                                            {
                                                                Log.d("FoodIDArrayQtyValue", FoodIDArrayQtyValue.get(i));
                                                            }
                                                        }
                                                }
                                            }
                                        });*/

                                //To retrieve breakfast Old edition
                                /*firebaseFirestore.collection("Diet Plans").document("UID Generated").collection("Diet Planner").document(DietPlanNames.get(i)).collection("Meals").document("Breakfast")
                                        .get()
                                        .addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
                                            @Override
                                            public void onComplete(@NonNull @NotNull Task<DocumentSnapshot> task) {
                                                DocumentSnapshot dS = task.getResult();
                                        /*ArrayList<String> BreakfastItems = (ArrayList<String>) dS.get("Breakfast");
                                        Log.d("BreakfastCollection", dS.getId() + " => " + dS.getData() + "arraylist:" + BreakfastItems + "Array Size:" + BreakfastItems.size());

                                        Log.d("BreakfastCollection", "Number of diet plans:" + BreakfastItems.size());
                                        Log.d("BreakfastCollection", "Diet Plan names:");
                                        for(int i=0; i<BreakfastItems.size(); ++i) {
                                            Log.d("BreakfastCollection", BreakfastItems.get(i));
                                        }*/



                                                /*Map<String, Object> Breakfast = dS.getData();
                                                Log.d("Breakfast Test", "Breakfast:" + Breakfast);
                                                for (Map.Entry<String, Object> entry : Breakfast.entrySet()) {
                                                    if (entry.getKey().equals("Food IDs Quantities")) {
                                                        Map<String, Object> FoodIDsQuantitiesMap = (Map<String, Object>) entry.getValue();
                                                        Log.d("FoodIDsQuantitiesMap Test", "FoodIDsQuantitiesMap:" + FoodIDsQuantitiesMap);
                                                        for (Map.Entry<String, Object> FoodIDsQuantitiesMapField : FoodIDsQuantitiesMap.entrySet()) {
                                                            Log.d("FoodIDsQuantitiesMapField Test", "\n FoodIDsQuantitiesMapField.getKey():" + FoodIDsQuantitiesMapField.getKey() + "\n FoodIDsQuantitiesMapField.getValue():" + FoodIDsQuantitiesMapField.getValue());
                                                            //if (e.getKey().equals("newFriend0")) {
                                                                //ArrayList fNameMap = (ArrayList) e.getValue();
                                                                ArrayList<String> FoodIDArrayQtyValue = (ArrayList<String>) FoodIDsQuantitiesMapField.getValue();
                                                                for(int i=0; i<FoodIDArrayQtyValue.size(); ++i)
                                                                /*for (Map.Entry<String, Object> dataEntry : fNameMap.entrySet())*/ /*{
                                                                    //if (dataEntry.getKey().equals("fName")) {
                                                                        Log.d("breakfastfoodidqty (FoodIDArrayQtyValue)", "element no. " + i + ": " + FoodIDArrayQtyValue.get(i));
                                                                    //}
                                                               }

                                                            //}
                                                            //ArrayList<String> BreakfastFoodIDArrays = (ArrayList<String>) dS.get(e.getKey());
                                                            //Log.d("breakfastfoodidarrays", "BreakfastFoodIDArrays:" + BreakfastFoodIDArrays);
                                                        }
                                                    }
                                                }



                                                ArrayList<String> BreakfastItems = (ArrayList<String>) dS.get("Food IDs");
                                                //ArrayList<Number> BreakfastQty = (ArrayList<Number>) dS.get("Food IDs Quantities");
                                                Log.d("BreakfastItems", "Breakfast Items:" + BreakfastItems + "\n Breakfast quantities:" /*+ BreakfastQty*//*);

                                                listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                                                    @Override
                                                    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                                                        Toast.makeText(DietPlans.this, "Dietplan name: \n" + listViewAdapter.getItem(position), Toast.LENGTH_SHORT).show();
                                                        Toast.makeText(DietPlans.this, "Breakfast items: \n" + BreakfastItems + "\n Breakfast quantities: \n" /*+ BreakfastQty*//*, Toast.LENGTH_SHORT).show();

                                                        dp_dialog = new Dialog(DietPlans.this);
                                                        dp_dialog.setContentView(R.layout.diet_planner_list);

                                                        //TODO: try this shit out v
                                                        TextView dpheader, breakfastitems;
                                                        dpheader = findViewById(R.id.dp_item_header);
                                                        breakfastitems = findViewById(R.id.breakfast_items);*/

                                //dpheader.setText(/*listViewAdapter.getItem(position)*/"hello");

                                //dpheader = DietPlanNames.get(i);
                                //breakfastitems =  BreakfastItems;
                                //breakfastitems.append(BreakfastItems);

                                                        /*dp_dialog.show();
                                                    }
                                                });
                                            }
                                        });*/
                            }



                        /*for(int i=0; i<DietPlanNames1.size(); ++i) {
                            Log.d("ReadName-Copy", DietPlanNames1.get(i));
                        }
                        Log.d("ReadName-Copy", "DietPlanNames1 Size:" + DietPlanNames1.size());*/
                        }
                        else {
                            Log.d("ReadName", "No diet plans :/", task.getException());

                            empty.setVisibility(View.VISIBLE);
                        }
                    }
                });

        /*Log.d("ReadName-Copy", "DietPlanNames1 Size:" + DietPlanNames1.size());
        for(int i=0; i<DietPlanNames1.size(); ++i) {
            Log.d("ReadName-Copy", DietPlanNames1.get(i));
        }*/
    }

    //used to delete diet plans from the DB and UI
    public static void removeItem(int remove, String name) {
        listViewItems.remove(remove);
        listView.setAdapter(listViewAdapter);
        firebaseFirestoreDel.collection("Diet Plans").document("UID Generated Test").update("Diet Plan Names", FieldValue.arrayRemove(name));
        firebaseFirestoreDel.collection("Diet Plans").document("UID Generated Test").collection("Diet Planner").document(name).collection("Meals").document("Breakfast")
                .get()
                .addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
                    @Override
                    public void onComplete(@NonNull @NotNull Task<DocumentSnapshot> task) {
                        if (task.isSuccessful()) {
                            DocumentSnapshot BSnapshot = task.getResult();
                            if(BSnapshot.exists()) {
                                firebaseFirestoreDel.collection("Diet Plans").document("UID Generated Test").collection("Diet Planner").document(name).collection("Meals").document("Breakfast").delete();
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
        firebaseFirestoreDel.collection("Diet Plans").document("UID Generated Test").collection("Diet Planner").document(name).collection("Meals").document("Lunch")
                .get()
                .addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
                    @Override
                    public void onComplete(@NonNull @NotNull Task<DocumentSnapshot> task) {
                        if (task.isSuccessful()) {
                            DocumentSnapshot BSnapshot = task.getResult();
                            if(BSnapshot.exists()) {
                                firebaseFirestoreDel.collection("Diet Plans").document("UID Generated Test").collection("Diet Planner").document(name).collection("Meals").document("Lunch").delete();
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
        firebaseFirestoreDel.collection("Diet Plans").document("UID Generated Test").collection("Diet Planner").document(name).collection("Meals").document("Dinner")
                .get()
                .addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
                    @Override
                    public void onComplete(@NonNull @NotNull Task<DocumentSnapshot> task) {
                        if (task.isSuccessful()) {
                            DocumentSnapshot BSnapshot = task.getResult();
                            if(BSnapshot.exists()) {
                                firebaseFirestoreDel.collection("Diet Plans").document("UID Generated Test").collection("Diet Planner").document(name).collection("Meals").document("Dinner").delete();
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
        firebaseFirestoreDel.collection("Diet Plans").document("UID Generated Test").collection("Diet Planner").document(name).collection("Meals").document("Snacks")
                .get()
                .addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
                    @Override
                    public void onComplete(@NonNull @NotNull Task<DocumentSnapshot> task) {
                        if (task.isSuccessful()) {
                            DocumentSnapshot BSnapshot = task.getResult();
                            if(BSnapshot.exists()) {
                                firebaseFirestoreDel.collection("Diet Plans").document("UID Generated Test").collection("Diet Planner").document(name).collection("Meals").document("Snacks").delete();
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
    }

    /*public void deleteItem() {
        firebaseFirestoreDel.collection("Diet Plans").document("UID Generated Test").update("Diet Plan Names", FieldValue.arrayRemove("Dirt of the earth"));
    }*/
}