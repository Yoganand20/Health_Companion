package com.project.healthcompanion.DashboardClasses;

import android.app.Activity;
import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;

import androidx.activity.result.ActivityResult;
import androidx.activity.result.ActivityResultCallback;
import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.ListView;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FieldValue;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.SetOptions;
import com.google.type.DateTime;
import com.project.healthcompanion.Model.Food;
import com.project.healthcompanion.R;
import com.project.healthcompanion.SearchFoodActivity;

import org.eazegraph.lib.models.PieModel;
import org.jetbrains.annotations.NotNull;

import java.lang.reflect.Array;
import java.math.BigDecimal;
import java.math.RoundingMode;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.function.DoubleConsumer;

public class DashBreakfastFragment extends Fragment {

    SimpleDateFormat simpleDateFormat;
    static SimpleDateFormat simpleDateFormatStatic;

    Date date;
    static Date dateStatic;

    Button add_breakfast;

    String BFood[];

    static Double CalVal;
    static Double proteinVal;
    static Double carbsVal;
    static Double fatsVal;

    static ArrayList<String[]> BreakfastFood = new ArrayList<>();

    static ListView BreakfastEntrylist;
    static ArrayList<String> BreakfastEntryItems;
    static DashBreakfastDispAdapter BreakfastEntryAdapter;

    FirebaseFirestore firebaseFirestore;
    static FirebaseFirestore firebaseFirestoreDel;

    String currentUser = FirebaseAuth.getInstance().getCurrentUser().getUid();
    static String currentUserStatic = FirebaseAuth.getInstance().getCurrentUser().getUid();

    public DashBreakfastFragment() {
        // Required empty public constructor
    }

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
                    BreakfastFood.get(flag)[2] = String.valueOf(Double.parseDouble(BreakfastFood.get(flag)[2]) + (food.getCalories() * Double.parseDouble(String.valueOf(qty))));
                    BreakfastFood.get(flag)[3] = String.valueOf(Double.parseDouble(BreakfastFood.get(flag)[3]) + (food.getProtein() * Double.parseDouble(String.valueOf(qty))));
                    BreakfastFood.get(flag)[4] = String.valueOf(Double.parseDouble(BreakfastFood.get(flag)[4]) + (food.getTotalCarbohydrate() * Double.parseDouble(String.valueOf(qty))));
                    BreakfastFood.get(flag)[5] = String.valueOf(Double.parseDouble(BreakfastFood.get(flag)[5]) + (food.getTotalFat() * Double.parseDouble(String.valueOf(qty))));
                    Log.d("ExisitngFood", "Qty:" + BreakfastFood.get(flag)[1] + " Cals:" + BreakfastFood.get(flag)[2] + "Prots:" + BreakfastFood.get(flag)[3] + "Carbs:" + BreakfastFood.get(flag)[4] + "Fats:" + BreakfastFood.get(flag)[5]);
                    //BF.get(flag)[1] = String.valueOf(Integer.parseInt(BreakfastFood.get(flag)[1]) + qty);
                }
                else { //if food item is new, its name is added to the BreakfastItems array and its name & qty are added to BreakfastFood
                    //BreakfastItems.add(food.getFood_name());
                    BreakfastFood.add(new String[] {food.getFood_name(), String.valueOf(qty), String.valueOf(food.getCalories() * qty), String.valueOf(food.getProtein() * qty), String.valueOf(food.getTotalCarbohydrate() * qty), String.valueOf(food.getTotalFat() * qty)});
                    //BF.add(new String[] {food.getFood_name(), String.valueOf(qty)});
                }

                Log.d("BreakfastFoodSize", String.valueOf(BreakfastFood.size()));

                firebaseFirestore.collection("Dashboard").document(currentUser).collection("DashboardRecords").document(simpleDateFormat.format(date))
                        .get()
                        .addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
                            @Override
                            public void onComplete(@NonNull @NotNull Task<DocumentSnapshot> task) {
                                if(task.isSuccessful()) {
                                    DocumentSnapshot documentSnapshot = task.getResult();
                                    if(documentSnapshot.exists()) {
                                        CalVal = documentSnapshot.getDouble("Calories");
                                        proteinVal = documentSnapshot.getDouble("Proteins");
                                        carbsVal = documentSnapshot.getDouble("Carbs");
                                        fatsVal = documentSnapshot.getDouble("Fats");

                                        Log.d("ExistBMacros:", CalVal + ", " + proteinVal + ", " + carbsVal + ", " + fatsVal);

                                        CalVal = CalVal + (food.getCalories() * qty);
                                        proteinVal = proteinVal + (food.getProtein() * qty);
                                        carbsVal = carbsVal + (food.getTotalCarbohydrate() * qty);
                                        fatsVal = fatsVal + (food.getTotalFat() * qty);

                                        Log.d("ExistBMacrosUpdated:", CalVal + ", " + proteinVal + ", " + carbsVal + ", " + fatsVal);
                                    }
                                    else {

                                        CalVal = 0.0;
                                        proteinVal = 0.0;
                                        carbsVal = 0.0;
                                        fatsVal = 0.0;

                                        Log.d("NoExistBMacros:", CalVal + ", " + proteinVal + ", " + carbsVal + ", " + fatsVal);

                                        CalVal = food.getCalories() * qty;
                                        proteinVal = food.getProtein() * qty;
                                        carbsVal = food.getTotalCarbohydrate() * qty;
                                        fatsVal = food.getTotalFat() * qty;

                                        Log.d("NoExistBMacrosUpdated:", CalVal + ", " + proteinVal + ", " + carbsVal + ", " + fatsVal);
                                    }
                                }
                            }
                        });

                /*CalVal = CalVal + (food.getCalories() * qty);
                proteinVal = proteinVal + (food.getProtein() * qty);
                carbsVal = carbsVal + (food.getTotalCarbohydrate() * qty);
                fatsVal = fatsVal + (food.getTotalFat() * qty);*/

                //BUploadData();

                /*DietPlanner DP = (DietPlanner) getActivity();

                //to show all elements in BreakfastFood array
                for(int i=0; i<BreakfastFood.size(); ++i) {
                    Log.d("BreakfastList", "#" + i + ": " + BreakfastFood.get(i)[0] + "|Qty:" + BreakfastFood.get(i)[1] + "|Cal:" + BreakfastFood.get(i)[2] + "|Prot:" + BreakfastFood.get(i)[3] + "|Carbs:" + BreakfastFood.get(i)[4] + "|Fats:" + BreakfastFood.get(i)[5]);

                    DP.ReceiveBreakfast(BreakfastFood.get(i)[0], BreakfastFood.get(i)[1], BreakfastFood.get(i)[2], BreakfastFood.get(i)[3], BreakfastFood.get(i)[4], BreakfastFood.get(i)[5]);
                }

                DP.RecieveBMacros(CalVal, proteinVal, carbsVal, fatsVal);*/

                //setChart();

                //Intent intent1 = new Intent(getActivity(), DietPlanner.class);
                //intent1.putExtra("BreakfastFood", BreakfastFood);


                BreakfastEntrylist.setAdapter(null);
                Log.d("BeforeClearedBEI", BreakfastEntryItems.toString());
                BreakfastEntryItems.clear();
                Log.d("AfterClearedBEI", BreakfastEntryItems.toString());
                for(int i=0; i<BreakfastFood.size(); ++i) {
                    BreakfastEntryItems.add(BreakfastFood.get(i)[0] + " x " + BreakfastFood.get(i)[1]);
                }
                Log.d("BreakfastEntryItems", BreakfastEntryItems.toString());
                BreakfastEntrylist.setAdapter(BreakfastEntryAdapter);


                firebaseFirestore.collection("Dashboard").document(currentUser).collection("DashboardRecords").document(simpleDateFormat.format(date)).collection("Meals").document("Breakfast")
                        .get()
                        .addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
                            @Override
                            public void onComplete(@NonNull @NotNull Task<DocumentSnapshot> task) {
                                if(task.isSuccessful()) {
                                    DocumentSnapshot documentSnapshot = task.getResult();
                                    if(documentSnapshot.exists()) {
                                        Log.d("DashBreakfast", "Breakfast does exist");

                                        Map<String, Object> DashBreakfast = new HashMap<>();

                                        //BreakfastData.put("Food IDs", BreakfastItems);

                                        for(int i=0; i<BreakfastFood.size(); ++i) {
                                            BFood = BreakfastFood.get(i);
                                            DashBreakfast.put(BreakfastFood.get(i)[0], Arrays.asList(BFood));
                                            Log.d("BFStoredIntoDB", BreakfastFood.get(i)[0] + " x " + Arrays.asList(BFood));
                                        }


                                        firebaseFirestore.collection("Dashboard").document(currentUser).collection("DashboardRecords").document(simpleDateFormat.format(date)).collection("Meals").document("Breakfast")
                                                .set(DashBreakfast, SetOptions.merge());

                                        firebaseFirestore.collection("Dashboard").document(currentUser).collection("DashboardRecords").document(simpleDateFormat.format(date))
                                                .get()
                                                .addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
                                                    @Override
                                                    public void onComplete(@NonNull @NotNull Task<DocumentSnapshot> task) {
                                                        if(task.isSuccessful()) {
                                                            DocumentSnapshot documentSnapshot1 = task.getResult();
                                                            if(documentSnapshot1.exists()) {
                                                                Log.d("DashBreakfast", "Current user does exist");

                                                                Map<String, Object> TotalMacros = new HashMap<>();
                                                                TotalMacros.put("Calories", roundTo2Decs( /*Double.parseDouble(documentSnapshot1.get("Calories").toString()) +*/ CalVal));
                                                                TotalMacros.put("Proteins", roundTo2Decs(/*Double.parseDouble(documentSnapshot1.get("Proteins").toString()) +*/ proteinVal));
                                                                TotalMacros.put("Carbs", roundTo2Decs(/*Double.parseDouble(documentSnapshot1.get("Carbs").toString()) +*/ carbsVal));
                                                                TotalMacros.put("Fats", roundTo2Decs(/*Double.parseDouble(documentSnapshot1.get("Fats").toString()) +*/ fatsVal));
                                                                firebaseFirestore.collection("Dashboard").document(currentUser).collection("DashboardRecords").document(simpleDateFormat.format(date))
                                                                        .set(TotalMacros);

                                                                DashboardActivity D = (DashboardActivity) getActivity();
                                                                //D.setChart();

                                                                D.cals.setText(Double.toString(roundTo2Decs(CalVal)));
                                                                D.proteins.setText(Double.toString(roundTo2Decs(proteinVal)));
                                                                D.carbs.setText(Double.toString(roundTo2Decs(carbsVal)));
                                                                D.fats.setText(Double.toString(roundTo2Decs(fatsVal)));

                                                                D.pieChart.clearChart();

                                                                D.pieChart.addPieSlice(new PieModel("Protein", Float.parseFloat(D.proteins.getText().toString()), Color.parseColor("#ff0000")));
                                                                D.pieChart.addPieSlice(new PieModel("Carbohydrates", Float.parseFloat(D.carbs.getText().toString()), Color.parseColor("#87ceeb")));
                                                                D.pieChart.addPieSlice(new PieModel("Fats", Float.parseFloat(D.fats.getText().toString()), Color.parseColor("#fff700")));

                                                                D.pieChart.startAnimation();
                                                            }
                                                            else {
                                                                Log.d("DashBreakfast", "Current user doesn't exist");
                                                                Map<String, Object> TotalMacros = new HashMap<>();
                                                                TotalMacros.put("Calories", roundTo2Decs(Double.parseDouble(BreakfastFood.get(0)[2])));
                                                                TotalMacros.put("Proteins", roundTo2Decs(Double.parseDouble(BreakfastFood.get(0)[3])));
                                                                TotalMacros.put("Carbs", roundTo2Decs(Double.parseDouble(BreakfastFood.get(0)[4])));
                                                                TotalMacros.put("Fats", roundTo2Decs(Double.parseDouble(BreakfastFood.get(0)[5])));
                                                                firebaseFirestore.collection("Dashboard").document(currentUser).collection("DashboardRecords").document(simpleDateFormat.format(date))
                                                                        .set(TotalMacros);

                                                                DashboardActivity D = (DashboardActivity) getActivity();
                                                                //D.setChart();
                                                                D.cals.setText(Double.toString(roundTo2Decs(CalVal)));
                                                                D.proteins.setText(Double.toString(roundTo2Decs(proteinVal)));
                                                                D.carbs.setText(Double.toString(roundTo2Decs(carbsVal)));
                                                                D.fats.setText(Double.toString(roundTo2Decs(fatsVal)));

                                                                D.pieChart.clearChart();

                                                                D.pieChart.addPieSlice(new PieModel("Protein", Float.parseFloat(D.proteins.getText().toString()), Color.parseColor("#ff0000")));
                                                                D.pieChart.addPieSlice(new PieModel("Carbohydrates", Float.parseFloat(D.carbs.getText().toString()), Color.parseColor("#87ceeb")));
                                                                D.pieChart.addPieSlice(new PieModel("Fats", Float.parseFloat(D.fats.getText().toString()), Color.parseColor("#fff700")));

                                                                D.pieChart.startAnimation();
                                                            }
                                                        }
                                                    }
                                                });


                                    }
                                    else {
                                        Log.d("DashBreakfast", "Breakfast doesn't exist");
                                        Map<String, Object> DashBreakfast = new HashMap<>();

                                        //BreakfastData.put("Food IDs", BreakfastItems);

                                        for(int i=0; i<BreakfastFood.size(); ++i) {
                                            BFood = BreakfastFood.get(i);
                                            DashBreakfast.put(BreakfastFood.get(i)[0], Arrays.asList(BFood));
                                            Log.d("BFStoredIntoDB", BreakfastFood.get(i)[0] + " x " + Arrays.asList(BFood));
                                        }

                                        firebaseFirestore.collection("Dashboard").document(currentUser).collection("DashboardRecords").document(simpleDateFormat.format(date)).collection("Meals").document("Breakfast")
                                                .set(DashBreakfast);

                                        Map<String, Object> TotalMacros = new HashMap<>();
                                        TotalMacros.put("Calories", roundTo2Decs(CalVal));
                                        TotalMacros.put("Proteins", roundTo2Decs(proteinVal));
                                        TotalMacros.put("Carbs", roundTo2Decs(carbsVal));
                                        TotalMacros.put("Fats", roundTo2Decs(fatsVal));
                                        firebaseFirestore.collection("Dashboard").document(currentUser).collection("DashboardRecords").document(simpleDateFormat.format(date))
                                                .set(TotalMacros);

                                        DashboardActivity D = (DashboardActivity) getActivity();
                                        //D.setChart();
                                        D.cals.setText(Double.toString(roundTo2Decs(CalVal)));
                                        D.proteins.setText(Double.toString(roundTo2Decs(proteinVal)));
                                        D.carbs.setText(Double.toString(roundTo2Decs(carbsVal)));
                                        D.fats.setText(Double.toString(roundTo2Decs(fatsVal)));

                                        D.pieChart.clearChart();

                                        D.pieChart.addPieSlice(new PieModel("Protein", Float.parseFloat(D.proteins.getText().toString()), Color.parseColor("#ff0000")));
                                        D.pieChart.addPieSlice(new PieModel("Carbohydrates", Float.parseFloat(D.carbs.getText().toString()), Color.parseColor("#87ceeb")));
                                        D.pieChart.addPieSlice(new PieModel("Fats", Float.parseFloat(D.fats.getText().toString()), Color.parseColor("#fff700")));

                                        D.pieChart.startAnimation();
                                    }
                                }
                            }
                        });



            }
        }
    });

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {

        View view = inflater.inflate(R.layout.fragment_dash_breakfast, container, false);

        simpleDateFormat = new SimpleDateFormat("dd-MM-yyyy");
        date = new Date();

        simpleDateFormatStatic = new SimpleDateFormat("dd-MM-yyyy");
        dateStatic = new Date();


        //today.set(calendar.get(Calendar.YEAR), calendar.get(Calendar.MONTH), calendar.get(Calendar.DAY_OF_MONTH));

        Log.d("Today", simpleDateFormat.format(date));

        add_breakfast = (Button) view.findViewById(R.id.add_dash_breakfast);

        BreakfastEntrylist = view.findViewById(R.id.breakfast_list);
        BreakfastEntryItems = new ArrayList<>();
        BreakfastEntryAdapter = new DashBreakfastDispAdapter(getActivity().getApplicationContext(), BreakfastEntryItems);

        firebaseFirestore = FirebaseFirestore.getInstance();
        firebaseFirestoreDel = FirebaseFirestore.getInstance();

        add_breakfast.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Log.d("DashBFragmentAddBtn", "pressed");
                addBreakfast();
            }
        });

        firebaseFirestore.collection("Dashboard").document(currentUser).collection("DashboardRecords").document(simpleDateFormat.format(date)).collection("Meals").document("Breakfast")
                .get()
                .addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
                    @Override
                    public void onComplete(@NonNull Task<DocumentSnapshot> task) {
                        if(task.isSuccessful()) {
                            DocumentSnapshot BreakfastSnapshot = task.getResult();
                            if(BreakfastSnapshot.exists()) {
                                Map<String, Object> map = BreakfastSnapshot.getData();
                                Log.d("Breakfast", "Breakfast:" + map);

                                List<String> BfoodItem;

                                //BreakfastFood.clear();

                                //int j=0;

                                for (Map.Entry<String, Object> FoodIDsQuantitiesMapField : map.entrySet())
                                {
                                    Log.d("FoodIDsQtyMapField", "\n FoodIDsQuantitiesMapField.getKey():" + FoodIDsQuantitiesMapField.getKey() + "\n FoodIDsQuantitiesMapField.getValue():" + FoodIDsQuantitiesMapField.getValue());

                                    BfoodItem = (List<String>) BreakfastSnapshot.get(FoodIDsQuantitiesMapField.getKey());

                                    //if(!FoodIDsQuantitiesMapField.getKey().equals("Food IDs")) {
                                    BreakfastEntryItems.add(FoodIDsQuantitiesMapField.getKey() + " x " + BfoodItem.get(1));
                                    BreakfastEntrylist.setAdapter(BreakfastEntryAdapter);

                                    //for(int i=0; i<BfoodItem.size(); ++i) {
                                    //BreakfastFood.get(j) = (Array<String[]>) BfoodItem;
                                    //}

                                    //++j;

                                    Log.d("GetValue", FoodIDsQuantitiesMapField.getValue().toString());
                                    Log.d("GetBfoodItem", BfoodItem.get(1));
                                    //Log.d("LoadedBreakfastFood", BreakfastFood.toString());

                                    //}
                                }

                                firebaseFirestore.collection("Dashboard").document(currentUser).collection("DashboardRecords").document(simpleDateFormat.format(date))
                                        .get()
                                        .addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
                                            @Override
                                            public void onComplete(@NonNull @NotNull Task<DocumentSnapshot> task) {
                                                if(task.isSuccessful()) {
                                                    DocumentSnapshot documentSnapshot1 = task.getResult();
                                                    if(documentSnapshot1.exists()) {

                                                        CalVal = documentSnapshot1.getDouble("Calories");
                                                        proteinVal = documentSnapshot1.getDouble("Proteins");
                                                        carbsVal = documentSnapshot1.getDouble("Carbs");
                                                        fatsVal = documentSnapshot1.getDouble("Fats");

                                                        DashboardActivity D = (DashboardActivity) getActivity();
                                                        //D.setChart();

                                                        D.cals.setText(Double.toString(roundTo2Decs(CalVal)));
                                                        D.proteins.setText(Double.toString(roundTo2Decs(proteinVal)));
                                                        D.carbs.setText(Double.toString(roundTo2Decs(carbsVal)));
                                                        D.fats.setText(Double.toString(roundTo2Decs(fatsVal)));

                                                        D.pieChart.clearChart();

                                                        D.pieChart.addPieSlice(new PieModel("Protein", Float.parseFloat(D.proteins.getText().toString()), Color.parseColor("#ff0000")));
                                                        D.pieChart.addPieSlice(new PieModel("Carbohydrates", Float.parseFloat(D.carbs.getText().toString()), Color.parseColor("#87ceeb")));
                                                        D.pieChart.addPieSlice(new PieModel("Fats", Float.parseFloat(D.fats.getText().toString()), Color.parseColor("#fff700")));

                                                        D.pieChart.startAnimation();
                                                    }
                                                }
                                            }
                                        });
                            }
                            else {
                                CalVal = 0.0;
                                proteinVal = 0.0;
                                carbsVal = 0.0;
                                fatsVal = 0.0;
                                Log.d("ReadBreakfast", "No Breakfast :/ |", task.getException());
                            }
                        }
                        else {
                            Log.d("ReadBreakfast", "No Breakfast :/ |", task.getException());
                        }
                    }
                });

        return view;
    }

    public void addBreakfast() {
        Intent intent = new Intent(getActivity(), SearchFoodActivity.class);
        foodResultLauncherForBreakfast.launch(intent);
    }

    public static void removeDashBreakfast(int remove, String Name) {
        BreakfastEntryItems.remove(remove);
        BreakfastEntrylist.setAdapter(BreakfastEntryAdapter);

        String N = Name;

        Log.d("TestDel", String.valueOf(N.length()));

        int StartIndex = N.indexOf(" x ");

        Log.d("StartIndex", String.valueOf(StartIndex));

        int EndIndex = N.length();

        Log.d("StartIndex", String.valueOf(EndIndex));

        StringBuffer SB = new StringBuffer(N);

        SB.delete(StartIndex, EndIndex);

        Log.d("EditedName", SB.toString());

        firebaseFirestoreDel.collection("Dashboard").document(currentUserStatic).collection("DashboardRecords").document(simpleDateFormatStatic.format(dateStatic)).collection("Meals").document("Breakfast")
                .get()
                .addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
                    @Override
                    public void onComplete(@NonNull @NotNull Task<DocumentSnapshot> task) {
                        if(task.isSuccessful()) {
                            DocumentSnapshot documentSnapshot = task.getResult();
                            if(documentSnapshot.exists()) {

                                Log.d("EditedName1", SB.toString());

                                List<String> BDelItem;
                                BDelItem = (List<String>) documentSnapshot.get(SB.toString());

                                Log.d("BDelItem", BDelItem.toString());

                                firebaseFirestoreDel.collection("Dashboard").document(currentUserStatic).collection("DashboardRecords").document(simpleDateFormatStatic.format(dateStatic))
                                        .get()
                                        .addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
                                            @Override
                                            public void onComplete(@NonNull @NotNull Task<DocumentSnapshot> task) {
                                                if(task.isSuccessful()) {
                                                    DocumentSnapshot documentSnapshot1 = task.getResult();
                                                    if(documentSnapshot1.exists()) {

                                                        Double CalValDel = documentSnapshot1.getDouble("Calories");
                                                        Double proteinValDel = documentSnapshot1.getDouble("Proteins");
                                                        Double carbsValDel = documentSnapshot1.getDouble("Carbs");
                                                        Double fatsValDel = documentSnapshot1.getDouble("Fats");

                                                        Log.d("DBMacros", "Cal:" + CalValDel + "Prot:" + proteinValDel + "Carb:" + carbsValDel + "Fat:" + fatsValDel);

                                                        Log.d("DelMacros", "Cal:" + BDelItem + "Prot:" + BDelItem + "Carb:" + BDelItem + "Fat:" + BDelItem);

                                                        CalVal = CalValDel - Double.parseDouble(BDelItem.get(2));
                                                        proteinVal = proteinValDel - Double.parseDouble(BDelItem.get(3));
                                                        carbsVal = carbsValDel - Double.parseDouble(BDelItem.get(4));
                                                        fatsVal = fatsValDel - Double.parseDouble(BDelItem.get(5));

                                                        Log.d("newMacros", "Cal:" + CalVal + "Prot:" + proteinVal + "Carb:" + carbsVal + "Fat:" + fatsVal);

                                                        Map<String, Object> TotalMacros = new HashMap<>();
                                                        TotalMacros.put("Calories", CalVal);
                                                        TotalMacros.put("Proteins", proteinVal);
                                                        TotalMacros.put("Carbs", carbsVal);
                                                        TotalMacros.put("Fats", fatsVal);
                                                        firebaseFirestoreDel.collection("Dashboard").document(currentUserStatic).collection("DashboardRecords").document(simpleDateFormatStatic.format(dateStatic))
                                                                .set(TotalMacros);

                                                    }
                                                }
                                                Map<String, Object> DeleteB = new HashMap<>();
                                                DeleteB.put(SB.toString(), FieldValue.delete());

                                                Log.d("DeletedBreakfast", SB.toString());

                                                firebaseFirestoreDel.collection("Dashboard").document(currentUserStatic).collection("DashboardRecords").document(simpleDateFormatStatic.format(dateStatic)).collection("Meals").document("Breakfast").update(DeleteB);

                                            }
                                        });
                            }
                        }
                    }
                });

        for(int i=0; i<BreakfastFood.size(); ++i) {
            if(BreakfastFood.get(i)[0].equals(SB)) {
                BreakfastFood.remove(remove);
            }
        }
    }

    private double roundTo2Decs(double value) {
        BigDecimal bd = new BigDecimal(value);
        bd = bd.setScale(2, RoundingMode.HALF_UP);
        return bd.doubleValue();
    }
}