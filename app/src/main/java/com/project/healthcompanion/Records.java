package com.project.healthcompanion;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.drawerlayout.widget.DrawerLayout;

import android.app.DatePickerDialog;
import android.app.TimePickerDialog;
import android.graphics.Color;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.TimePicker;
import android.widget.Toast;

import com.github.mikephil.charting.charts.LineChart;
import com.github.mikephil.charting.components.AxisBase;
import com.github.mikephil.charting.components.MarkerView;
import com.github.mikephil.charting.components.XAxis;
import com.github.mikephil.charting.components.YAxis;
import com.github.mikephil.charting.data.Entry;
import com.github.mikephil.charting.data.LineData;
import com.github.mikephil.charting.data.LineDataSet;
import com.github.mikephil.charting.formatter.IAxisValueFormatter;
import com.github.mikephil.charting.highlight.Highlight;
import com.github.mikephil.charting.interfaces.datasets.ILineDataSet;
import com.github.mikephil.charting.utils.MPPointF;
import com.github.mikephil.charting.utils.ViewPortHandler;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.api.Context;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.Query;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;
import com.project.healthcompanion.ReminderClasses.Reminder_main;

import org.jetbrains.annotations.NotNull;

import java.lang.reflect.Type;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.time.Instant;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class Records extends AppCompatActivity {

    DrawerLayout drawerLayout;

    LineChart lineChart;
    LineData lineData;
    LineDataSet lineDataSet;
    ArrayList<ILineDataSet> iLineDataSets = new ArrayList<>();


    EditText current_wt;
    Button add_rec;
    Date rec_date;
    TextView current_wt_disp;

    FirebaseFirestore firebaseFirestore;
    CollectionReference Ref;

    @Override
    protected void onCreate(Bundle savedInstance) {
        super.onCreate(savedInstance);
        setContentView(R.layout.activity_records);

        drawerLayout = findViewById(R.id.drawer_Layout);

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

        String currentUser = FirebaseAuth.getInstance().getCurrentUser().getUid();

        firebaseFirestore = FirebaseFirestore.getInstance();
        Ref = firebaseFirestore.collection("records").document(currentUser).collection("Graph Data");

        retrieveData();

        insert_Data();
    }

    //navigation drawer
    public void ClickMenu(View view) { HomePage.openDrawer(drawerLayout); }

    public void ClickLogo(View view) { HomePage.closeDrawer(drawerLayout); }

    public void ClickProfile(View view) { /*HomePage.redirectActivity(this, Profile.class);*/ }

    public void ClickDashboard(View view) { /*HomePage.redirectActivity(this, Dashboard.class);*/ }

    public void ClickRecords(View view) { HomePage.closeDrawer(drawerLayout); }

    public void ClickDietPlans(View view) { /*HomePage.redirectActivity(this, DietPlans.class);*/ }

    public void ClickReminders(View view) { HomePage.redirectActivity(this, Reminder_main.class);}

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
                    }
                })
                        .addOnFailureListener(new OnFailureListener() {
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
}
