package com.project.healthcompanion;

import androidx.activity.result.ActivityResult;
import androidx.activity.result.ActivityResultCallback;
import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.ContextCompat;
import androidx.drawerlayout.widget.DrawerLayout;
import androidx.room.Update;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.res.Resources;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.media.Image;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import android.view.KeyEvent;
import android.view.MotionEvent;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.bumptech.glide.Registry;
import com.bumptech.glide.annotation.GlideModule;
import com.bumptech.glide.module.AppGlideModule;
import com.firebase.ui.storage.images.FirebaseImageLoader;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.storage.FileDownloadTask;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.project.healthcompanion.DietPlansClasses.DietPlans;
import com.project.healthcompanion.ReminderClasses.Reminder_main;

import org.jetbrains.annotations.NotNull;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.math.BigDecimal;
import java.math.RoundingMode;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;
import java.util.Map;

public class Profile extends AppCompatActivity {

    DrawerLayout drawerLayout;

    TextView username, actlvl, weight, SC, Pace, PaceVal, TDEE;

    EditText height, BMI, PAL, Goal;

    String fn, ln, wt, ht, bmi, tdee, goal, pace;

    Button updateht, updaterest;

    Double pal;

    String[] measures = new String[]{};
    String[] deficitmeasure = new String[]{};
    String[] surplusmeasure = new String[]{};
    Spinner activityLevelSpinner, deficitspinner, surplusspinner;
    String activityLevel;

    ImageView pfp;

    FirebaseFirestore firebaseFirestore;

    DocumentReference personalDocref, physiqueDocref, setgoalDocref, suggcalDocref;

    StorageReference storageReference;

    String currentUser = FirebaseAuth.getInstance().getCurrentUser().getUid();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_profile);

        drawerLayout = findViewById(R.id.drawer_Layout);

        username = findViewById(R.id.Profile_username);
        weight = findViewById(R.id.Profile_Weight);
        height = findViewById(R.id.Profile_Height);
        BMI = findViewById(R.id.Profile_BMI);
        TDEE = findViewById(R.id.Profile_TDEE);
        Goal = findViewById(R.id.Profile_Goal);
        actlvl = findViewById(R.id.Profile_ActivityLevel);
        activityLevelSpinner = findViewById(R.id.activityLevelSpinner);
        deficitspinner = findViewById(R.id.DefeceitSpinner);
        surplusspinner = findViewById(R.id.SurplusSpinner);
        SC = findViewById(R.id.Profile_suggestedCalories);
        Pace = findViewById(R.id.Pace);
        PaceVal = findViewById(R.id.PaceVal);

        updateht = findViewById(R.id.UpdateHt);
        updaterest = findViewById(R.id.UpdateRest);

        measures = getResources().getStringArray(R.array.activityLevels);
        List<String> list_measures = new ArrayList<>(Arrays.asList(measures));
        ArrayAdapter<String> adapter = new ArrayAdapter<String>(this, android.R.layout.simple_spinner_item, getResources().getStringArray(R.array.activityLevels));
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        activityLevelSpinner.setAdapter(adapter);

        deficitmeasure = getResources().getStringArray(R.array.defeceitOptions);
        List<String> deficit_list = new ArrayList<>(Arrays.asList(deficitmeasure));
        ArrayAdapter<String> deficitadapter = new ArrayAdapter<>(this, android.R.layout.simple_spinner_item, getResources().getStringArray(R.array.defeceitOptions));
        deficitadapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        deficitspinner.setAdapter(deficitadapter);

        surplusmeasure = getResources().getStringArray(R.array.surplusOptions);
        List<String> surplus_list = new ArrayList<>(Arrays.asList(surplusmeasure));
        ArrayAdapter<String> surplusadapter = new ArrayAdapter<>(this, android.R.layout.simple_spinner_item, getResources().getStringArray(R.array.surplusOptions));
        surplusadapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        surplusspinner.setAdapter(surplusadapter);

        pfp = findViewById(R.id.Profile_imageView_profilePic);

        firebaseFirestore = FirebaseFirestore.getInstance();

        personalDocref = firebaseFirestore.collection("profiles").document(currentUser);

        physiqueDocref = firebaseFirestore.collection("profiles").document(currentUser).collection("profile categories").document("physical");

        setgoalDocref = firebaseFirestore.collection("profiles").document(currentUser).collection("profile categories").document("goal");

        suggcalDocref = firebaseFirestore.collection("profiles").document(currentUser).collection("profile categories").document("suggested calories");

        storageReference = FirebaseStorage.getInstance().getReference();

        loadDetails();

        updateht.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                UpdateHt();
            }
        });

        updaterest.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                UpdateRest();
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

    public void ClickProfile(View view) { HomePage.closeDrawer(drawerLayout); }

    public void ClickHome(View view) {
        HomePage.redirectActivity(this, HomePage.class);
    }

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

    public void ClickLogout(View view) {
        HomePage.logout(this);
    }

    @Override
    protected void onPause() {
        super.onPause();
        HomePage.closeDrawer(drawerLayout);
    }
    //end of navigation drawer

    public void loadDetails() {
        //loads user's firstname and lastname
        personalDocref.get().addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
            @Override
            public void onComplete(@NonNull @NotNull Task<DocumentSnapshot> task) {
                if(task.isSuccessful()) {
                    DocumentSnapshot documentSnapshot = task.getResult();
                    if(documentSnapshot.exists()) {
                        fn = documentSnapshot.getString("First name");
                        ln = documentSnapshot.getString("Last name");
                        username.setText(fn + " " + ln);
                    }
                }
            }
        });

        //loads physique details
        physiqueDocref.get().addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
            @Override
            public void onComplete(@NonNull @NotNull Task<DocumentSnapshot> task) {
                if(task.isSuccessful()) {
                    DocumentSnapshot documentSnapshot = task.getResult();
                    if(documentSnapshot.exists()) {
                        wt = documentSnapshot.getDouble("Weight").toString();
                        weight.setText(wt);
                        ht = documentSnapshot.getDouble("Height").toString();
                        height.setText(ht);
                        bmi = documentSnapshot.getDouble("BMI").toString();
                        Log.d("BMITest", "Load BMI:" + String.valueOf(bmi));
                        BMI.setText(bmi);
                        pal = roundTo2Decs(documentSnapshot.getDouble("PAL"));

                        if(pal == 1.53) {
                            Log.d("PAL", "light");
                            actlvl.setText("Light Activity Lifestyle");
                            activityLevelSpinner.setSelection(0);
                        }
                        if(pal == 1.76) {
                            Log.d("PAL", "moderate");
                            actlvl.setText("Moderate Activity Lifestyle");
                            activityLevelSpinner.setSelection(1);
                        }
                        if(pal == 2.25) {
                            Log.d("PAL", "vigorous");
                            actlvl.setText("Vigorous Activity Lifestyle");
                            activityLevelSpinner.setSelection(2);
                        }
                        tdee = documentSnapshot.getDouble("TDEE").toString();
                        TDEE.setText(tdee);
                    }
                }
            }
        });

        //loads goal weight
        setgoalDocref.get().addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
            @Override
            public void onComplete(@NonNull @NotNull Task<DocumentSnapshot> task) {
                if(task.isSuccessful()) {
                    DocumentSnapshot documentSnapshot = task.getResult();
                    if(documentSnapshot.exists()) {
                        goal = documentSnapshot.getDouble("goal weight").toString();
                        Goal.setText(goal);
                    }
                }
            }
        });

        //loads activity level
        physiqueDocref.get().addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
            @Override
            public void onComplete(@NonNull @NotNull Task<DocumentSnapshot> task) {
                if(task.isSuccessful()) {
                    DocumentSnapshot documentSnapshot = task.getResult();
                    Log.d("ActivityLevelSet", "Pre-set");
                    if(documentSnapshot.exists()) {
                        pal = roundTo2Decs(documentSnapshot.getDouble("PAL"));

                        if(pal.equals(1.53)) {
                            activityLevelSpinner.setSelection(1);
                        }
                        else if(pal.equals(1.76)) {
                            activityLevelSpinner.setSelection(2);
                        }
                        else if(pal.equals(2.25)) {
                            activityLevelSpinner.setSelection(3);
                        }
                    }
                }
            }
        });

        //sets activity level
        activityLevelSpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                Object item = parent.getItemAtPosition(position);

                Log.d("Test", "item:" + item + "|position:" + position);

                personalDocref.get().addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
                    @Override
                    public void onComplete(@NonNull @NotNull Task<DocumentSnapshot> task) {
                        if(task.isSuccessful()){
                            DocumentSnapshot documentSnapshot1 = task.getResult();
                            if(documentSnapshot1.exists()) {
                                String gender = documentSnapshot1.getString("gender");
                                Date dob = documentSnapshot1.getDate("DOB");
                                physiqueDocref.get().addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
                                    @Override
                                    public void onComplete(@NonNull @NotNull Task<DocumentSnapshot> task) {
                                        if(task.isSuccessful()) {
                                            DocumentSnapshot documentSnapshot = task.getResult();
                                            if(documentSnapshot.exists()) {
                                                Double BMR, W, H;
                                                Double B, T; //B => BMI | T => TDEE

                                                Date thisyear = Calendar.getInstance().getTime();
                                                W = documentSnapshot.getDouble("Weight");
                                                H = documentSnapshot.getDouble("Height");

                                                SimpleDateFormat yearFormat = new SimpleDateFormat("yyyy", Locale.getDefault());

                                                String year = yearFormat.format(thisyear);
                                                String dobyear = yearFormat.format(dob);

                                                Float age = Float.parseFloat(year) - Float.parseFloat(dobyear);
                                                Log.d("ProfileAge", String.valueOf(age));

                                                if(gender.equals("Male")) {
                                                    BMR = (W * 10.0f) + (H * 6.25f) - (age * 5.0f) + 5.0f;
                                                    if (item.equals(getResources().getString(R.string.light_activity_lifestyle))) {
                                                        pal = roundTo2Decs(1.53);
                                                        physiqueDocref.update("PAL", pal);
                                                        actlvl.setText("Light Activity Lifestyle");
                                                        T = BMR * pal;
                                                        physiqueDocref.update("TDEE", T);
                                                        loadDetails();
                                                    } else if (item.equals(getResources().getString(R.string.moderate_activity_lifestyle))) {
                                                        pal = roundTo2Decs(1.76);
                                                        physiqueDocref.update("PAL", pal);
                                                        actlvl.setText("Moderate Activity Lifestyle");
                                                        T = BMR * pal;
                                                        physiqueDocref.update("TDEE", T);
                                                        loadDetails();
                                                    } else if (item.equals(getResources().getString(R.string.vigorous_activity_lifestyle))) {
                                                        pal = roundTo2Decs(2.25);
                                                        physiqueDocref.update("PAL", pal);
                                                        actlvl.setText("Vigorous Activity Lifestyle");
                                                        T = BMR * pal;
                                                        physiqueDocref.update("TDEE", T);
                                                        loadDetails();
                                                    }
                                                }
                                                if(gender.equals("Female")) {
                                                    BMR = (W * 10.0f) + (H * 6.25f) - (age * 5.0f) - 161.0f;
                                                    if (item.equals(getResources().getString(R.string.light_activity_lifestyle))) {
                                                        pal = roundTo2Decs(1.53);
                                                        physiqueDocref.update("PAL", pal);
                                                        actlvl.setText("Light Activity Lifestyle");
                                                        T = BMR * pal;
                                                        physiqueDocref.update("TDEE", T);
                                                        loadDetails();

                                                    } else if (item.equals(getResources().getString(R.string.moderate_activity_lifestyle))) {
                                                        pal = roundTo2Decs(1.76);
                                                        physiqueDocref.update("PAL", pal);
                                                        actlvl.setText("Moderate Activity Lifestyle");
                                                        T = BMR * pal;
                                                        physiqueDocref.update("TDEE", T);
                                                        loadDetails();
                                                    } else if (item.equals(getResources().getString(R.string.vigorous_activity_lifestyle))) {
                                                        pal = roundTo2Decs(2.25);
                                                        physiqueDocref.update("PAL", pal);
                                                        actlvl.setText("Vigorous Activity Lifestyle");
                                                        T = BMR * pal;
                                                        physiqueDocref.update("TDEE", T);
                                                        loadDetails();
                                                    }
                                                }
                                            }
                                        }
                                    }
                                });
                            }
                        }
                    }
                });
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });

        //loads profile picture
        storageReference = FirebaseStorage.getInstance().getReference().child(currentUser);

        try {
            final File localFile = File.createTempFile(currentUser, "jpg");
            storageReference.getFile(localFile)
                    .addOnSuccessListener(new OnSuccessListener<FileDownloadTask.TaskSnapshot>() {
                        @Override
                        public void onSuccess(FileDownloadTask.TaskSnapshot taskSnapshot) {
                            Log.d("Retrieve image", "Success!");
                            Bitmap bitmap = BitmapFactory.decodeFile(localFile.getAbsolutePath());
                            ((ImageView)findViewById(R.id.Profile_imageView_profilePic)).setImageBitmap(bitmap);
                        }
                    }).addOnFailureListener(new OnFailureListener() {
                @Override
                public void onFailure(@NonNull @NotNull Exception e) {
                    Log.d("Retrieve image", "Failure!");
                }
            });
        }catch (IOException e) {
            e.printStackTrace();
        }

        //load suggested calories
        /*physiqueDocref.get().addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
            @Override
            public void onComplete(@NonNull @NotNull Task<DocumentSnapshot> task) {
                if(task.isSuccessful()) {
                    DocumentSnapshot PdocumentSnapshot = task.getResult();
                    if(PdocumentSnapshot.exists()) {
                        W = PdocumentSnapshot.getDouble("Weight");
                        Log.d("Profile_Current weight", String.valueOf(W));
                        setgoalDocref.get().addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
                            @Override
                            public void onComplete(@NonNull @NotNull Task<DocumentSnapshot> task) {
                                if(task.isSuccessful()) {
                                    DocumentSnapshot SdocumentSnapshot = task.getResult();
                                    if(SdocumentSnapshot.exists()) {
                                        G = SdocumentSnapshot.getDouble("goal weight");
                                        Log.d("Profile_Goal weight", String.valueOf(G));
                                        WC = Math.abs(W - G);
                                        Log.d("Weight difference", String.valueOf(WC));
                                        WCC = WC * 7700;
                                        Log.d("Weight change calories", String.valueOf(WCC));
                                        if(W.equals(G)) {
                                            Log.d("Journey", "Maintenance selected");
                                            deficitspinner.setVisibility(View.INVISIBLE);
                                            surplusspinner.setVisibility(View.INVISIBLE);

                                            Map<String, Object> SuggestedCal = new HashMap<>();
                                            Double maintenanceCal = PdocumentSnapshot.getDouble("TDEE");
                                            SuggestedCal.put("SuggestedCal", maintenanceCal);

                                            suggcalDocref.set(SuggestedCal);
                                            SC.setText("Consume " + maintenanceCal + " calories to maintain " + W + "kgs");
                                            //loadDetails();
                                        }
                                        else if(W > G) {
                                            Log.d("Journey", "Deficit selected");
                                            deficitspinner.setVisibility(View.VISIBLE);
                                            surplusspinner.setVisibility(View.INVISIBLE);

                                            suggcalDocref.get().addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
                                                @Override
                                                public void onComplete(@NonNull @NotNull Task<DocumentSnapshot> task) {
                                                    if(task.isSuccessful()) {
                                                        DocumentSnapshot documentSnapshot = task.getResult();
                                                        Log.d("DeficitSet", "Pre-set");
                                                        if(documentSnapshot.exists()) {
                                                            pace = documentSnapshot.getString("pace");

                                                            if(pace.equals("slow")) {
                                                                deficitspinner.setSelection(1);
                                                            }
                                                            else if(pace.equals("moderate")) {
                                                                deficitspinner.setSelection(2);
                                                            }
                                                            else {
                                                                deficitspinner.setSelection(3);
                                                            }
                                                        }
                                                    }
                                                }
                                            });

                                            deficitspinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
                                                @Override
                                                public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                                                    Object item = parent.getItemAtPosition(position);

                                                    Log.d("DeficitSpinner", "item:" + item + "|position:" + position);


                                                    Double maintenanceCal = PdocumentSnapshot.getDouble("TDEE");
                                                    Log.d("Maintenance Calories", String.valueOf(maintenanceCal));
                                                    Double deficit1 = roundTo2Decs(maintenanceCal - 150);
                                                    Double deficit2 = roundTo2Decs(maintenanceCal - 300);
                                                    Double deficit3 = roundTo2Decs(maintenanceCal - 600);

                                                    if (item.equals("Lose weight slowly")) {
                                                        if(WCC % 150 != 0) {
                                                            days = (int) ((WCC / 150) + 1);
                                                            Log.d("Days", String.valueOf(days));
                                                        }
                                                        else {
                                                            days = (int) (WCC / 150);
                                                            Log.d("Days", String.valueOf(days));
                                                        }

                                                        Map<String, Object> SuggestedCal = new HashMap<>();
                                                        SuggestedCal.put("SuggestedCal", deficit1);
                                                        Log.d("Suggested calories", String.valueOf(deficit1));

                                                        suggcalDocref.set(SuggestedCal);
                                                        suggcalDocref.update("pace", "slow");
                                                        SC.setText("It should take approximately " + days + " days to reach " + G + "kgs at a consumption of  " + deficit1 + "cals per day");
                                                        //loadDetails();
                                                    }
                                                    else if (item.equals("Lose weight moderately")) {
                                                        if(WCC % 300 != 0) {
                                                            days = (int) ((WCC / 300) + 1);
                                                            Log.d("Days", String.valueOf(days));
                                                        }
                                                        else {
                                                            days = (int) (WCC / 300);
                                                            Log.d("Days", String.valueOf(days));
                                                        }

                                                        Map<String, Object> SuggestedCal = new HashMap<>();
                                                        SuggestedCal.put("SuggestedCal", deficit2);
                                                        Log.d("Suggested calories", String.valueOf(deficit2));

                                                        suggcalDocref.set(SuggestedCal);
                                                        suggcalDocref.update("pace", "moderate");
                                                        SC.setText("It should take approximately " + days + " days to reach " + G + "kgs at a consumption of " + deficit2 + "cals per day");
                                                        //loadDetails();
                                                    }
                                                    else if (item.equals("Lose weight quickly")) {
                                                        if(WCC % 600 != 0) {
                                                            days = (int) ((WCC / 600) + 1);
                                                            Log.d("Days", String.valueOf(days));
                                                        }
                                                        else {
                                                            days = (int) (WCC / 600);
                                                            Log.d("Days", String.valueOf(days));
                                                        }

                                                        Map<String, Object> SuggestedCal = new HashMap<>();
                                                        SuggestedCal.put("SuggestedCal", deficit3);
                                                        Log.d("Suggested calories", String.valueOf(deficit3));

                                                        suggcalDocref.set(SuggestedCal);
                                                        suggcalDocref.update("pace", "quick");
                                                        SC.setText("It should take approximately " + days + " days to reach " + G + "kgs at a consumption of " + deficit3 + "cals per day");
                                                        //loadDetails();
                                                    }
                                                }

                                                @Override
                                                public void onNothingSelected(AdapterView<?> parent) {

                                                }
                                            });
                                        }
                                        else if(W < G) {
                                            Log.d("Journey", "Surplus selected");
                                            deficitspinner.setVisibility(View.INVISIBLE);
                                            surplusspinner.setVisibility(View.VISIBLE);

                                            suggcalDocref.get().addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
                                                @Override
                                                public void onComplete(@NonNull @NotNull Task<DocumentSnapshot> task) {
                                                    if(task.isSuccessful()) {
                                                        DocumentSnapshot documentSnapshot = task.getResult();
                                                        Log.d("DeficitSet", "Pre-set");
                                                        if(documentSnapshot.exists()) {
                                                            pace = documentSnapshot.getString("pace");

                                                            if(pace.equals("slow")) {
                                                                surplusspinner.setSelection(1);
                                                            }
                                                            else if(pace.equals("moderate")) {
                                                                surplusspinner.setSelection(2);
                                                            }
                                                            else {
                                                                surplusspinner.setSelection(3);
                                                            }
                                                        }
                                                    }
                                                }
                                            });

                                            surplusspinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
                                                @Override
                                                public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                                                    Object item = parent.getItemAtPosition(position);

                                                    Log.d("DefeceitSpinner", "item:" + item + "|position:" + position);


                                                    Double maintenanceCal = PdocumentSnapshot.getDouble("TDEE");
                                                    Double surplus1 = roundTo2Decs(maintenanceCal + 200);
                                                    Double surplus2 = roundTo2Decs(maintenanceCal + 400);
                                                    Double surplus3 = roundTo2Decs(maintenanceCal + 500);

                                                    if (item.equals("Gain weight slowly")) {
                                                        if(WCC % surplus1 != 0) {
                                                            days = (int) ((WCC / surplus1) + 1);
                                                            Log.d("Days", String.valueOf(days));
                                                        }
                                                        else {
                                                            days = (int) (WCC / surplus1);
                                                            Log.d("Days", String.valueOf(days));
                                                        }

                                                        Map<String, Object> SuggestedCal = new HashMap<>();
                                                        SuggestedCal.put("SuggestedCal", surplus1);
                                                        Log.d("Suggested calories", String.valueOf(surplus1));

                                                        suggcalDocref.set(SuggestedCal);
                                                        suggcalDocref.update("pace", "slow");
                                                        SC.setText("It should take approximately " + days + " days to reach " + G + "kgs at a consumption of " + surplus1 + "cals per day");
                                                        //loadDetails();
                                                    }
                                                    else if (item.equals("Gain weight moderately")) {
                                                        if(WCC % surplus2 != 0) {
                                                            days = (int) ((WCC / surplus2) + 1);
                                                            Log.d("Days", String.valueOf(days));
                                                        }
                                                        else {
                                                            days = (int) (WCC / surplus2);
                                                            Log.d("Days", String.valueOf(days));
                                                        }

                                                        Map<String, Object> SuggestedCal = new HashMap<>();
                                                        SuggestedCal.put("SuggestedCal", surplus2);
                                                        Log.d("Suggested calories", String.valueOf(surplus2));

                                                        suggcalDocref.set(SuggestedCal);
                                                        suggcalDocref.update("pace", "moderate");
                                                        SC.setText("It should take approximately " + days + " days to reach " + G + "kgs at a consumption of " + surplus2 + "cals per day");
                                                        //loadDetails();
                                                    }
                                                    else if (item.equals("Gain weight quickly")) {
                                                        if(WCC % surplus3 != 0) {
                                                            days = (int) ((WCC / surplus3) + 1);
                                                            Log.d("Days", String.valueOf(days));
                                                        }
                                                        else {
                                                            days = (int) (WCC / surplus3);
                                                            Log.d("Days", String.valueOf(days));
                                                        }

                                                        Map<String, Object> SuggestedCal = new HashMap<>();
                                                        SuggestedCal.put("SuggestedCal", surplus3);
                                                        Log.d("Suggested calories", String.valueOf(surplus3));

                                                        suggcalDocref.set(SuggestedCal);
                                                        suggcalDocref.update("pace", "quick");
                                                        SC.setText("It should take approximately " + days + " days to reach " + G + "kgs at a consumption of " + surplus3 + "cals per day");
                                                        //loadDetails();
                                                    }
                                                }

                                                @Override
                                                public void onNothingSelected(AdapterView<?> parent) {

                                                }
                                            });
                                        }
                                    }
                                }
                            }
                        });
                    }
                }
            }
        });*/

        setJourney();
    }

    public void UpdateHt() {
        Log.d("UpdateHt", "called!");
        Double Wt, Ht, Bmi, GOAL;
        String AL;
        Wt = Double.parseDouble(weight.getText().toString());
        Ht = Double.parseDouble(height.getText().toString());
        AL = actlvl.getText().toString();

        physiqueDocref.update("Height", Ht);

        personalDocref.get().addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
            @Override
            public void onComplete(@NonNull @NotNull Task<DocumentSnapshot> task) {
                if(task.isSuccessful()){
                    DocumentSnapshot documentSnapshot1 = task.getResult();
                    if(documentSnapshot1.exists()) {
                        String gender = documentSnapshot1.getString("gender");
                        Date dob = documentSnapshot1.getDate("DOB");
                        physiqueDocref.get().addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
                            @Override
                            public void onComplete(@NonNull @NotNull Task<DocumentSnapshot> task) {
                                if(task.isSuccessful()) {
                                    DocumentSnapshot documentSnapshot = task.getResult();
                                    if(documentSnapshot.exists()) {
                                        Double BMR, W, H;
                                        Double P;
                                        Double B, T; //B => BMI | T => TDEE

                                        Date thisyear = Calendar.getInstance().getTime();
                                        W = documentSnapshot.getDouble("Weight");
                                        H = documentSnapshot.getDouble("Height");
                                        P = roundTo2Decs(documentSnapshot.getDouble("PAL"));

                                        SimpleDateFormat yearFormat = new SimpleDateFormat("yyyy", Locale.getDefault());

                                        String year = yearFormat.format(thisyear);
                                        String dobyear = yearFormat.format(dob);

                                        Float age = Float.parseFloat(year) - Float.parseFloat(dobyear);
                                        Log.d("ProfileAge", String.valueOf(age));

                                        B = (W / ((H / 100) * (H / 100)));
                                        Log.d("ProfileBMI", String.valueOf(B));
                                        physiqueDocref.update("BMI", B);

                                        if(gender.equals("Male")) {
                                            BMR = (W * 10.0f) + (H * 6.25f) - (age * 5.0f) + 5.0f;
                                            if (P.equals(1.53)) {
                                                T = BMR * P;
                                                physiqueDocref.update("TDEE", T);
                                                loadDetails();
                                                //setJourney();
                                            }
                                            else if(P.equals(1.76)) {
                                                T = BMR * P;
                                                physiqueDocref.update("TDEE", T);
                                                loadDetails();
                                                //setJourney();
                                            }
                                            else if(P.equals(2.25)) {
                                                T = BMR * P;
                                                physiqueDocref.update("TDEE", T);
                                                loadDetails();
                                                //setJourney();
                                            }
                                        }
                                        if(gender.equals("Female")) {
                                            BMR = (W * 10.0f) + (H * 6.25f) - (age * 5.0f) - 161.0f;
                                            if (P.equals(1.53)) {
                                                T = BMR * P;
                                                physiqueDocref.update("TDEE", T);
                                                loadDetails();
                                                //setJourney();
                                            }
                                            else if(P.equals(1.76)) {
                                                T = BMR * P;
                                                physiqueDocref.update("TDEE", T);
                                                loadDetails();
                                                //setJourney();
                                            }
                                            else if(P.equals(2.25)) {
                                                T = BMR * P;
                                                physiqueDocref.update("TDEE", T);
                                                loadDetails();
                                                //setJourney();
                                            }
                                        }
                                    }
                                }
                            }
                        });
                    }
                }
            }
        });
    }

    public void UpdateRest() {
        Double GOAL;

        physiqueDocref.get().addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
            @Override
            public void onComplete(@NonNull @NotNull Task<DocumentSnapshot> task) {
                if (task.isSuccessful()) {
                    DocumentSnapshot documentSnapshot = task.getResult();
                    if(documentSnapshot.exists()) {
                        Double Bmi, Tdee, Pal, Height, Weight;

                        Pal = documentSnapshot.getDouble("PAL");

                        Bmi = Double.parseDouble(BMI.getText().toString());
                        Log.d("BMITest", "BMI.getText().toString():" + BMI.getText().toString());
                        Log.d("BMITest", "Set BMI:" + String.valueOf(Bmi));
                        Tdee = Double.parseDouble(TDEE.getText().toString());
                        Height = Double.parseDouble(height.getText().toString());
                        Weight = Double.parseDouble(weight.getText().toString());

                        /*Map<String, Object> physique = new HashMap<>();
                        physique.put("BMI", Bmi);
                        physique.put("TDEE", Tdee);
                        physique.put("PAL", Pal);
                        physique.put("Height", Height);
                        physique.put("Weight", Weight);

                        physiqueDocref.set(physique);*/

                        physiqueDocref.update("BMI", Bmi);
                        physiqueDocref.update("TDEE", Tdee);

                        BMI.setText(Bmi.toString());
                        TDEE.setText(Tdee.toString());
                    }
                }
            }
        });

        GOAL = Double.parseDouble(Goal.getText().toString());

        setgoalDocref.update("goal weight", GOAL);

        setJourney();
    }

    Double W, G, WC, WCC;
    int days;

    public void setJourney() {
        physiqueDocref.get().addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
            @Override
            public void onComplete(@NonNull @NotNull Task<DocumentSnapshot> task) {
                if(task.isSuccessful()) {
                    DocumentSnapshot PdocumentSnapshot = task.getResult();
                    if(PdocumentSnapshot.exists()) {
                        W = PdocumentSnapshot.getDouble("Weight");
                        Log.d("Profile_Current weight", String.valueOf(W));
                        setgoalDocref.get().addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
                            @Override
                            public void onComplete(@NonNull @NotNull Task<DocumentSnapshot> task) {
                                if(task.isSuccessful()) {
                                    DocumentSnapshot SdocumentSnapshot = task.getResult();
                                    if(SdocumentSnapshot.exists()) {
                                        G = SdocumentSnapshot.getDouble("goal weight");
                                        Log.d("Profile_Goal weight", String.valueOf(G));
                                        WC = Math.abs(W - G);
                                        Log.d("Weight difference", String.valueOf(WC));
                                        WCC = WC * 7700;
                                        Log.d("Weight change calories", String.valueOf(WCC));
                                        if(W.equals(G)) {
                                            Log.d("Journey", "Maintenance selected");
                                            deficitspinner.setVisibility(View.INVISIBLE);
                                            surplusspinner.setVisibility(View.INVISIBLE);
                                            Pace.setVisibility(View.INVISIBLE);
                                            PaceVal.setVisibility(View.INVISIBLE);

                                            //Map<String, Object> SuggestedCal = new HashMap<>();
                                            Double maintenanceCal = PdocumentSnapshot.getDouble("TDEE");
                                            //SuggestedCal.put("SuggestedCal", maintenanceCal);

                                            suggcalDocref.update("SuggestedCal", maintenanceCal);
                                            suggcalDocref.update("pace", "quick");
                                            SC.setText("Consume " + maintenanceCal + " calories to maintain " + W + "kgs");
                                            //loadDetails();
                                        }
                                        else if(W > G) {
                                            Log.d("Journey", "Deficit selected");
                                            deficitspinner.setVisibility(View.VISIBLE);
                                            surplusspinner.setVisibility(View.INVISIBLE);

                                            suggcalDocref.get().addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
                                                @Override
                                                public void onComplete(@NonNull @NotNull Task<DocumentSnapshot> task) {
                                                    if(task.isSuccessful()) {
                                                        DocumentSnapshot documentSnapshot = task.getResult();
                                                        Log.d("DeficitSet", "Pre-set");
                                                        if(documentSnapshot.exists()) {
                                                            pace = documentSnapshot.getString("pace");

                                                            if(pace.equals("slow")) {
                                                                PaceVal.setText("Lose weight slowly");
                                                                deficitspinner.setSelection(1);
                                                            }
                                                            else if(pace.equals("moderate")) {
                                                                PaceVal.setText("Lose weight moderately");
                                                                deficitspinner.setSelection(2);
                                                            }
                                                            else {
                                                                PaceVal.setText("Lose weight quickly");
                                                                deficitspinner.setSelection(3);
                                                            }
                                                        }
                                                    }
                                                }
                                            });

                                            deficitspinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
                                                @Override
                                                public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                                                    Object item = parent.getItemAtPosition(position);

                                                    Log.d("DeficitSpinner", "item:" + item + "|position:" + position);


                                                    Double maintenanceCal = PdocumentSnapshot.getDouble("TDEE");
                                                    Log.d("Maintenance Calories", String.valueOf(maintenanceCal));
                                                    Double deficit1 = roundTo2Decs(maintenanceCal - 150);
                                                    Double deficit2 = roundTo2Decs(maintenanceCal - 300);
                                                    Double deficit3 = roundTo2Decs(maintenanceCal - 600);

                                                    if (item.equals("Lose weight slowly")) {
                                                        if(WCC % 150 != 0) {
                                                            days = (int) ((WCC / 150) + 1);
                                                            Log.d("Days", String.valueOf(days));
                                                        }
                                                        else {
                                                            days = (int) (WCC / 150);
                                                            Log.d("Days", String.valueOf(days));
                                                        }

                                                        //Map<String, Object> SuggestedCal = new HashMap<>();
                                                        //SuggestedCal.put("SuggestedCal", deficit1);
                                                        //Log.d("Suggested calories", String.valueOf(deficit1));

                                                        //suggcalDocref.set(SuggestedCal);
                                                        suggcalDocref.update("SuggestedCal", deficit1);
                                                        suggcalDocref.update("pace", "slow");
                                                        SC.setText("It should take approximately " + days + " days to reach " + G + "kgs at a consumption of  " + deficit1 + "cals per day");
                                                        //loadDetails();
                                                        PaceVal.setText("Lose weight slowly");
                                                        deficitspinner.setSelection(0);
                                                    }
                                                    else if (item.equals("Lose weight moderately")) {
                                                        if(WCC % 300 != 0) {
                                                            days = (int) ((WCC / 300) + 1);
                                                            Log.d("Days", String.valueOf(days));
                                                        }
                                                        else {
                                                            days = (int) (WCC / 300);
                                                            Log.d("Days", String.valueOf(days));
                                                        }

                                                        //Map<String, Object> SuggestedCal = new HashMap<>();
                                                        //SuggestedCal.put("SuggestedCal", deficit2);
                                                        //Log.d("Suggested calories", String.valueOf(deficit2));

                                                        //suggcalDocref.set(SuggestedCal);
                                                        suggcalDocref.update("SuggestedCal", deficit2);
                                                        suggcalDocref.update("pace", "moderate");
                                                        SC.setText("It should take approximately " + days + " days to reach " + G + "kgs at a consumption of " + deficit2 + "cals per day");
                                                        //loadDetails();
                                                        PaceVal.setText("Lose weight moderately");
                                                        deficitspinner.setSelection(0);
                                                    }
                                                    else if (item.equals("Lose weight quickly")) {
                                                        if(WCC % 600 != 0) {
                                                            days = (int) ((WCC / 600) + 1);
                                                            Log.d("Days", String.valueOf(days));
                                                        }
                                                        else {
                                                            days = (int) (WCC / 600);
                                                            Log.d("Days", String.valueOf(days));
                                                        }

                                                        //Map<String, Object> SuggestedCal = new HashMap<>();
                                                        //SuggestedCal.put("SuggestedCal", deficit3);
                                                        //Log.d("Suggested calories", String.valueOf(deficit3));

                                                        //suggcalDocref.set(SuggestedCal);
                                                        suggcalDocref.update("SuggestedCal", deficit3);
                                                        suggcalDocref.update("pace", "quick");
                                                        SC.setText("It should take approximately " + days + " days to reach " + G + "kgs at a consumption of " + deficit3 + "cals per day");
                                                        //loadDetails();
                                                        PaceVal.setText("Lose weight quickly");
                                                        deficitspinner.setSelection(0);
                                                    }
                                                }

                                                @Override
                                                public void onNothingSelected(AdapterView<?> parent) {

                                                }
                                            });
                                        }
                                        else if(W < G) {
                                            Log.d("Journey", "Surplus selected");
                                            deficitspinner.setVisibility(View.INVISIBLE);
                                            surplusspinner.setVisibility(View.VISIBLE);

                                            suggcalDocref.get().addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
                                                @Override
                                                public void onComplete(@NonNull @NotNull Task<DocumentSnapshot> task) {
                                                    if(task.isSuccessful()) {
                                                        DocumentSnapshot documentSnapshot = task.getResult();
                                                        Log.d("DeficitSet", "Pre-set");
                                                        if(documentSnapshot.exists()) {
                                                            pace = documentSnapshot.getString("pace");

                                                            if(pace.equals("slow")) {
                                                                surplusspinner.setSelection(1);
                                                                PaceVal.setText("Gain weight slowly");
                                                            }
                                                            else if(pace.equals("moderate")) {
                                                                surplusspinner.setSelection(2);
                                                                PaceVal.setText("Gain weight moderately");
                                                            }
                                                            else {
                                                                surplusspinner.setSelection(3);
                                                                PaceVal.setText("Gain weight quickly");
                                                            }
                                                        }
                                                    }
                                                }
                                            });

                                            surplusspinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
                                                @Override
                                                public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                                                    Object item = parent.getItemAtPosition(position);

                                                    Log.d("DefeceitSpinner", "item:" + item + "|position:" + position);


                                                    Double maintenanceCal = PdocumentSnapshot.getDouble("TDEE");
                                                    Double surplus1 = roundTo2Decs(maintenanceCal + 200);
                                                    Double surplus2 = roundTo2Decs(maintenanceCal + 400);
                                                    Double surplus3 = roundTo2Decs(maintenanceCal + 500);

                                                    if (item.equals("Gain weight slowly")) {
                                                        if(WCC % surplus1 != 0) {
                                                            days = (int) ((WCC / surplus1) + 1);
                                                            Log.d("Days", String.valueOf(days));
                                                        }
                                                        else {
                                                            days = (int) (WCC / surplus1);
                                                            Log.d("Days", String.valueOf(days));
                                                        }

                                                        //Map<String, Object> SuggestedCal = new HashMap<>();
                                                        //SuggestedCal.put("SuggestedCal", surplus1);
                                                        //Log.d("Suggested calories", String.valueOf(surplus1));

                                                        //suggcalDocref.set(SuggestedCal);
                                                        suggcalDocref.update("SuggestedCal", surplus1);
                                                        suggcalDocref.update("pace", "slow");
                                                        SC.setText("It should take approximately " + days + " days to reach " + G + "kgs at a consumption of " + surplus1 + "cals per day");
                                                        //loadDetails();
                                                        PaceVal.setText("Gain weight slowly");
                                                        surplusspinner.setSelection(0);
                                                    }
                                                    else if (item.equals("Gain weight moderately")) {
                                                        if(WCC % surplus2 != 0) {
                                                            days = (int) ((WCC / surplus2) + 1);
                                                            Log.d("Days", String.valueOf(days));
                                                        }
                                                        else {
                                                            days = (int) (WCC / surplus2);
                                                            Log.d("Days", String.valueOf(days));
                                                        }

                                                        //Map<String, Object> SuggestedCal = new HashMap<>();
                                                        //SuggestedCal.put("SuggestedCal", surplus2);
                                                        //Log.d("Suggested calories", String.valueOf(surplus2));

                                                        //suggcalDocref.set(SuggestedCal);
                                                        suggcalDocref.update("SuggestedCal", surplus2);
                                                        suggcalDocref.update("pace", "moderate");
                                                        SC.setText("It should take approximately " + days + " days to reach " + G + "kgs at a consumption of " + surplus2 + "cals per day");
                                                        //loadDetails();
                                                        PaceVal.setText("Gain weight moderately");
                                                        surplusspinner.setSelection(0);
                                                    }
                                                    else if (item.equals("Gain weight quickly")) {
                                                        if(WCC % surplus3 != 0) {
                                                            days = (int) ((WCC / surplus3) + 1);
                                                            Log.d("Days", String.valueOf(days));
                                                        }
                                                        else {
                                                            days = (int) (WCC / surplus3);
                                                            Log.d("Days", String.valueOf(days));
                                                        }

                                                        //Map<String, Object> SuggestedCal = new HashMap<>();
                                                        //SuggestedCal.put("SuggestedCal", surplus3);
                                                        //Log.d("Suggested calories", String.valueOf(surplus3));

                                                        //suggcalDocref.set(SuggestedCal);
                                                        suggcalDocref.update("SuggestedCal", surplus3);
                                                        suggcalDocref.update("pace", "quick");
                                                        SC.setText("It should take approximately " + days + " days to reach " + G + "kgs at a consumption of " + surplus3 + "cals per day");
                                                        //loadDetails();
                                                        PaceVal.setText("Gain weight quickly");
                                                        surplusspinner.setSelection(0);
                                                    }
                                                }

                                                @Override
                                                public void onNothingSelected(AdapterView<?> parent) {

                                                }
                                            });
                                        }
                                    }
                                }
                            }
                        });
                    }
                }
            }
        });
    }

    private double roundTo2Decs(double value) {
        BigDecimal bd = new BigDecimal(value);
        bd = bd.setScale(2, RoundingMode.HALF_UP);
        return bd.doubleValue();
    }
}