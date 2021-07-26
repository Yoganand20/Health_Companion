package com.project.healthcompanion.DietPlansClasses;

import android.app.Activity;
import android.content.Intent;
import android.media.Image;
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
import android.widget.CalendarView;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;

import com.project.healthcompanion.Model.DietPlan;
import com.project.healthcompanion.Model.Food;
import com.project.healthcompanion.R;
import com.project.healthcompanion.SearchFoodActivity;
import com.project.healthcompanion.databinding.FragmentBreakfastBinding;

import org.jetbrains.annotations.NotNull;

import java.sql.Ref;
import java.util.ArrayList;
import java.util.List;

public class BreakfastFragment extends Fragment {

    Button add_breakfast;

    ImageButton Refresh;

    static Double CalVal = 0.0;
    static Double proteinVal = 0.0;
    static Double carbsVal = 0.0;
    static Double fatsVal = 0.0;

    static List<String[]> BreakfastFood = new ArrayList<String[]>();

    static ListView BreakfastEntrylist;
    static ArrayList<String> BreakfastEntryItems;
    static BreakfastDisplayAdapter BreakfastEntryAdapter;

    public BreakfastFragment() {
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
                    //BF.get(flag)[1] = String.valueOf(Integer.parseInt(BreakfastFood.get(flag)[1]) + qty);
                }
                else { //if food item is new, its name is added to the BreakfastItems array and its name & qty are added to BreakfastFood
                    //BreakfastItems.add(food.getFood_name());
                    BreakfastFood.add(new String[] {food.getFood_name(), String.valueOf(qty), String.valueOf(food.getCalories()), String.valueOf(food.getProtein()), String.valueOf(food.getTotalCarbohydrate()), String.valueOf(food.getTotalFat())});
                    //BF.add(new String[] {food.getFood_name(), String.valueOf(qty)});
                }

                Log.d("BreakfastFoodSize", String.valueOf(BreakfastFood.size()));

                CalVal = CalVal + (food.getCalories() * qty);
                proteinVal = proteinVal + (food.getProtein() * qty);
                carbsVal = carbsVal + (food.getTotalCarbohydrate() * qty);
                fatsVal = fatsVal + (food.getTotalFat() * qty);

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
                    BreakfastEntryItems.add(BreakfastFood.get(i)[0] + " x" + BreakfastFood.get(i)[1]);
                }
                Log.d("BreakfastEntryItems", BreakfastEntryItems.toString());
                BreakfastEntrylist.setAdapter(BreakfastEntryAdapter);
            }
        }
    });

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        Log.d("InsideBreakfastFragment", "true");

        View view = inflater.inflate(R.layout.fragment_breakfast, container, false);

        add_breakfast = (Button) view.findViewById(R.id.add_breakfast);

        Refresh = (ImageButton) view.findViewById(R.id.breakfastrefresh);

        BreakfastEntrylist = view.findViewById(R.id.breakfast_entry_list);
        BreakfastEntryItems = new ArrayList<>();
        BreakfastEntryAdapter = new BreakfastDisplayAdapter(getActivity().getApplicationContext(), BreakfastEntryItems);

        add_breakfast.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Log.d("BreakfastFragmentAddBtn", "pressed");
                addBreakfast();
            }
        });

        Refresh.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                BUploadData();
            }
        });

        /*binding = FragmentBreakfastBinding.inflate(getLayoutInflater());

        View view = inflater.inflate(R.layout.fragment_breakfast, container, false);

        binding.addBreakfast.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Log.d("BreakfastFragmentAddBtn", "pressed");
                Intent intent = new Intent(getActivity(), SearchFoodActivity.class);
                foodResultLauncherForBreakfast.launch(intent);
            }
        });*/

        return view;
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();

        BreakfastEntrylist.setAdapter(null);
        BreakfastEntryItems.clear();
    }

    public void addBreakfast() {

        Intent intent = new Intent(getActivity(), SearchFoodActivity.class);
        foodResultLauncherForBreakfast.launch(intent);

    }

    public void BUploadData() {

        DietPlanner DP = (DietPlanner) getActivity();

        for(int i=0; i<BreakfastFood.size(); ++i) {
            Log.d("BreakfastListPostDel", "#" + i + ": " + BreakfastFood.get(i)[0] + "|" + BreakfastFood.get(i)[1]);

            DP.ReceiveBreakfast(BreakfastFood.get(i)[0], BreakfastFood.get(i)[1]); //BreakfastFood.get(i)[2], BreakfastFood.get(i)[3], BreakfastFood.get(i)[4], BreakfastFood.get(i)[5]);
        }

        DP.RecieveBMacros(CalVal, proteinVal, carbsVal, fatsVal);
    }

    public static void removeBreakfast(int remove, String Name) {
        BreakfastEntryItems.remove(remove);
        BreakfastEntrylist.setAdapter(BreakfastEntryAdapter);

        //firebaseFirestoreDel.collection("Diet Plans").document(currentUserStatic).collection("DietPlanner").document(diet_name_static.getText().toString()).collection("Meals").document("Breakfast").update("Food IDs", FieldValue.arrayRemove(Name));
        /*for(int i=0; i<BF.size(); ++i) {
            if(BF.get(i)[0].equals(Name)) {

            }
        }*/

        CalVal = CalVal - (Double.parseDouble(BreakfastFood.get(remove)[2]) * Double.parseDouble(BreakfastFood.get(remove)[1]));
        proteinVal = proteinVal - (Double.parseDouble(BreakfastFood.get(remove)[3]) * Double.parseDouble(BreakfastFood.get(remove)[1]));
        carbsVal = carbsVal - (Double.parseDouble(BreakfastFood.get(remove)[4]) * Double.parseDouble(BreakfastFood.get(remove)[1]));
        fatsVal = fatsVal - (Double.parseDouble(BreakfastFood.get(remove)[5]) * Double.parseDouble(BreakfastFood.get(remove)[1]));
        BreakfastFood.remove(remove);

        //BUploadData();
    }
}