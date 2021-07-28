package com.project.healthcompanion;

import android.graphics.Color;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBarDrawerToggle;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.drawerlayout.widget.DrawerLayout;

import com.github.mikephil.charting.charts.LineChart;
import com.github.mikephil.charting.components.AxisBase;
import com.github.mikephil.charting.components.XAxis;
import com.github.mikephil.charting.components.YAxis;
import com.github.mikephil.charting.data.Entry;
import com.github.mikephil.charting.data.LineData;
import com.github.mikephil.charting.data.LineDataSet;
import com.github.mikephil.charting.formatter.IAxisValueFormatter;
import com.github.mikephil.charting.interfaces.datasets.ILineDataSet;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.Query;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;
import com.project.healthcompanion.DietPlansClasses.DietPlans;
import com.project.healthcompanion.ReminderClasses.Reminder_main;

import org.jetbrains.annotations.NotNull;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.Locale;
import java.util.Map;

public class Records extends AppCompatActivity {
    ActionBarDrawerToggle toggle;

    DrawerLayout drawerLayout;

    LineChart lineChart;
    LineData lineData;
    LineDataSet lineDataSet;
    ArrayList<ILineDataSet> iLineDataSets = new ArrayList<>();


    EditText current_wt;
    Button add_rec;
    Date rec_date;
    TextView current_wt_disp;
    TextView goal_wt_disp;

    FirebaseFirestore firebaseFirestore;
    CollectionReference Ref;
    DocumentReference personalDocref, physiqueDocref;

    String currentUser = FirebaseAuth.getInstance().getCurrentUser().getUid();
    //press back twice to exit
    private boolean backPressedOnce = false;

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

    public void ClickRecords(View view) { HomePage.closeDrawer(drawerLayout); }

    public void ClickDietPlans(View view) { HomePage.redirectActivity(this, DietPlans.class); }

    public void ClickReminders(View view) { HomePage.redirectActivity(this, Reminder_main.class);}

    public void ClickHelp(View view) {HomePage.redirectActivity(this, HelpActivity.class);}

    public void ClickLogout(View view) { HomePage.logout(this); }

    @Override
    protected void onPause(){
        super.onPause();
        HomePage.closeDrawer(drawerLayout);
    }
    //end of navigation drawer

    private void insert_Data() {

        add_rec.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                rec_date = new Date();
                Float y = Float.parseFloat(current_wt.getText().toString());

                Map<String, Object> data = new HashMap<>();
                data.put("record date", rec_date);
                data.put("weight", y);

                Ref.add(data).addOnSuccessListener(new OnSuccessListener<DocumentReference>() {
                    @Override
                    public void onSuccess(DocumentReference documentReference) {
                        Log.d("WriteRec", "Document Snapshot written with ID: " + documentReference.getId() + " || Record date:" + rec_date + /*", Entry number:" + x +*/ ", Weight:" + y);

                        physiqueDocref.update("Weight", y);

                        current_wt.setText("");

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
                                                            }
                                                            else if(P.equals(1.76)) {
                                                                T = BMR * P;
                                                                physiqueDocref.update("TDEE", T);
                                                            }
                                                            else if(P.equals(2.25)) {
                                                                T = BMR * P;
                                                                physiqueDocref.update("TDEE", T);
                                                            }
                                                        }
                                                        if(gender.equals("Female")) {
                                                            BMR = (W * 10.0f) + (H * 6.25f) - (age * 5.0f) - 161.0f;
                                                            if (P.equals(1.53)) {
                                                                T = BMR * P;
                                                                physiqueDocref.update("TDEE", T);
                                                            }
                                                            else if(P.equals(1.76)) {
                                                                T = BMR * P;
                                                                physiqueDocref.update("TDEE", T);
                                                            }
                                                            else if(P.equals(2.25)) {
                                                                T = BMR * P;
                                                                physiqueDocref.update("TDEE", T);
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
                }).addOnFailureListener(new OnFailureListener() {
                            @Override
                            public void onFailure(@NonNull @NotNull Exception e) {
                                Log.w("WriteRec", "Error adding docuemnt", e);
                            }
                        });

                retrieveData();
            }
        });
    }


    private void retrieveData() {

        Ref.orderBy("record date", Query.Direction.ASCENDING).get().addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
            @Override
            public void onComplete(@NonNull @NotNull Task<QuerySnapshot> task) {
                ArrayList<Entry> lineEntries = new ArrayList<Entry>();

                if(task.isSuccessful()) {
                    float x = 1;
                    String disp_wt;
                    for(QueryDocumentSnapshot document : task.getResult()) {
                        Log.d("ReadRec", document.getId() + " => " + document.getData());

                        Double longy = document.getDouble("weight");
                        Float y = Float.parseFloat(String.valueOf(longy));

                        Log.d("ReadRec", "y = " + y);
                        //Records_datapoints records_datapoints = document.toObject(Records_datapoints.class);
                        lineEntries.add(new Entry(x, y));

                        ++x;

                        disp_wt = Float.toString(y);
                        current_wt_disp.setText(disp_wt);
                    }
                    showChart(lineEntries);
                }
                else {
                    Log.d("Document", "Error getting documents: ", task.getException());
                    lineChart.clear();
                    lineChart.invalidate();
                }
            }
        });
    }

    private void showChart(ArrayList<Entry> lineEntries) {
        lineDataSet.setValues(lineEntries);
        iLineDataSets.add(lineDataSet);
        lineData = new LineData(iLineDataSets);
        lineChart.clear();
        lineChart.setData(lineData);
        lineChart.invalidate();
    }

    private class MyYAxisValueFormatter implements IAxisValueFormatter {
        @Override
        public String getFormattedValue(float value, AxisBase axis) {
            return value + "kg";
        }
    }

    private double roundTo2Decs(double value) {
        BigDecimal bd = new BigDecimal(value);
        bd = bd.setScale(2, RoundingMode.HALF_UP);
        return bd.doubleValue();
    }

    private Toast t;

    @Override
    protected void onCreate(Bundle savedInstance) {
        super.onCreate(savedInstance);
        setContentView(R.layout.activity_records);

        drawerLayout = findViewById(R.id.drawer_Layout);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setHomeButtonEnabled(true);
        toggle = new ActionBarDrawerToggle(
                this,
                drawerLayout,
                R.string.navigation_drawer_open,
                R.string.navigation_drawer_close) {
        };
        drawerLayout.addDrawerListener(toggle);
        toggle.syncState();

        lineChart = findViewById(R.id.lineChart);
        lineDataSet = new LineDataSet(null, null);
        lineData = new LineData(lineDataSet);
        lineChart.setData(lineData);
        lineDataSet.setValueTextColor(Color.BLACK);
        lineDataSet.setLineWidth(2);
        lineDataSet.setColor(Color.MAGENTA);
        lineDataSet.setDrawCircles(true);
        lineDataSet.setValueTextSize(10);
        ArrayList<ILineDataSet> iLineDataSets = new ArrayList<>();

        XAxis xAxis = lineChart.getXAxis();
        YAxis yAxisLeft = lineChart.getAxisLeft();
        YAxis yAxisRight = lineChart.getAxisRight();

        xAxis.setPosition(XAxis.XAxisPosition.BOTTOM);

        yAxisLeft.setValueFormatter(new MyYAxisValueFormatter());
        yAxisRight.setEnabled(false);

        current_wt = findViewById(R.id.records_current_weight);
        add_rec = findViewById(R.id.add_records);
        current_wt_disp = findViewById(R.id.records_current_weight_disp);
        goal_wt_disp = findViewById(R.id.records_goal_weight_disp);

        firebaseFirestore = FirebaseFirestore.getInstance();
        Ref = firebaseFirestore.collection("records").document(currentUser).collection("Graph Data");
        personalDocref = firebaseFirestore.collection("profiles").document(currentUser);
        physiqueDocref = firebaseFirestore.collection("profiles").document(currentUser).collection("profile categories").document("physical");

        firebaseFirestore.collection("profiles").document(currentUser).collection("profile categories").document("goal")
                .get()
                .addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
                    @Override
                    public void onComplete(@NonNull @NotNull Task<DocumentSnapshot> task) {
                        if (task.isSuccessful()) {
                            DocumentSnapshot documentSnapshot = task.getResult();
                            if (documentSnapshot.exists()) {
                                String goal = documentSnapshot.get("goal weight").toString();
                                goal_wt_disp.setText(goal);
                            }
                        }
                    }
                });

        retrieveData();

        insert_Data();
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if (toggle.onOptionsItemSelected(item)) {
            return true;
        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    public void onBackPressed() {
        if (backPressedOnce) {
            t.cancel();
            ActivityCompat.finishAffinity(Records.this);
            finish();
        }
        backPressedOnce = true;
        t = Toast.makeText(this, "Press back again to exit", Toast.LENGTH_SHORT);
        t.show();
        new Handler(Looper.getMainLooper()).postDelayed(new Runnable() {
            @Override
            public void run() {
                backPressedOnce = false;
            }
        }, 2000);
    }
}
