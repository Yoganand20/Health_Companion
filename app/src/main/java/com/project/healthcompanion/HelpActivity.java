package com.project.healthcompanion;

import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;
import android.widget.ExpandableListView;
import android.widget.Toast;

import androidx.appcompat.app.ActionBarDrawerToggle;
import androidx.appcompat.app.AppCompatActivity;
import androidx.drawerlayout.widget.DrawerLayout;

import com.ms.square.android.expandabletextview.ExpandableTextView;
import com.project.healthcompanion.DashboardClasses.DashboardActivity;
import com.project.healthcompanion.DietPlansClasses.DietPlans;
import com.project.healthcompanion.ReminderClasses.Reminder_main;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

public class HelpActivity extends AppCompatActivity {

    DrawerLayout drawerLayout;
    ActionBarDrawerToggle toggle;
    //press back twice to exit
    private boolean backPressedOnce = false;
    ExpandableListView expandableListView;

    List<String> listGroup = new ArrayList<>();

    HashMap<String, List<String>> listChild = new HashMap<>();

    HelpAdapter adapter;

    ExpandableTextView expTv1, expTv2, expTv3, expTv4, expTv5, expTv6, expTv7;
    private Toast t;

    //navigation drawer
    public void ClickMenu(View view) { HomePage.openDrawer(drawerLayout); }

    public void ClickLogo(View view) { HomePage.closeDrawer(drawerLayout); }

    public void ClickProfile(View view) { HomePage.redirectActivity(this, Profile.class); }

    public void ClickDashboard(View view) { HomePage.redirectActivity(this, DashboardActivity.class); }

    public void ClickRecords(View view) { HomePage.redirectActivity(this, Records.class); }

    public void ClickDietPlans(View view) { HomePage.redirectActivity(this, DietPlans.class); }

    public void ClickReminders(View view) { HomePage.redirectActivity(this, Reminder_main.class); }

    public void ClickHelp(View view) {HomePage.closeDrawer(drawerLayout);}

    public void ClickLogout(View view) { HomePage.logout(this); }

    @Override
    protected void onPause() {
        super.onPause();
        HomePage.closeDrawer(drawerLayout);
    }
    //end of navigation drawer

    public void initListData() {
        listGroup.add(getString(R.string.text1));
        listGroup.add(getString(R.string.text2));
        listGroup.add(getString(R.string.text3));
        listGroup.add(getString(R.string.text4));
        listGroup.add(getString(R.string.text5));
        listGroup.add(getString(R.string.text6));
        listGroup.add(getString(R.string.text7));
        listGroup.add(getString(R.string.text8));
        listGroup.add(getString(R.string.text9));

        String[] array;

        ArrayList<String> list1 = new ArrayList<>();
        array = getResources().getStringArray(R.array.text1);

        for(String item : array) {
            list1.add(item);
        }

        ArrayList<String> list2 = new ArrayList<>();
        array = getResources().getStringArray(R.array.text2);

        for(String item : array) {
            list2.add(item);
        }

        ArrayList<String> list3 = new ArrayList<>();
        array = getResources().getStringArray(R.array.text3);

        for(String item : array) {
            list3.add(item);
        }

        ArrayList<String> list4 = new ArrayList<>();
        array = getResources().getStringArray(R.array.text4);

        for(String item : array) {
            list4.add(item);
        }

        ArrayList<String> list5 = new ArrayList<>();
        array = getResources().getStringArray(R.array.text5);

        for(String item : array) {
            list5.add(item);
        }

        ArrayList<String> list6 = new ArrayList<>();
        array = getResources().getStringArray(R.array.text6);

        for(String item : array) {
            list6.add(item);
        }

        ArrayList<String> list7 = new ArrayList<>();
        array = getResources().getStringArray(R.array.text7);

        for(String item : array) {
            list7.add(item);
        }

        ArrayList<String> list8 = new ArrayList<>();
        array = getResources().getStringArray(R.array.text8);

        for(String item : array) {
            list8.add(item);
        }

        ArrayList<String> list9 = new ArrayList<>();
        array = getResources().getStringArray(R.array.text9);

        for(String item : array) {
            list9.add(item);
        }

        listChild.put(listGroup.get(0), list1);
        listChild.put(listGroup.get(1), list2);
        listChild.put(listGroup.get(2), list3);
        listChild.put(listGroup.get(3), list4);
        listChild.put(listGroup.get(4), list5);
        listChild.put(listGroup.get(5), list6);
        listChild.put(listGroup.get(6), list7);
        listChild.put(listGroup.get(7), list8);
        listChild.put(listGroup.get(8), list9);
        adapter.notifyDataSetChanged();
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if (toggle.onOptionsItemSelected(item)) {
            return true;
        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_help);

        drawerLayout = findViewById(R.id.drawer_Layout);
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
        expandableListView = findViewById(R.id.ExpLW);


        adapter = new HelpAdapter(this, listGroup, listChild);

        expandableListView.setAdapter(adapter);

        initListData();

        // getting reference of  ExpandableTextView
        //expTv1 = (ExpandableTextView) findViewById(R.id.expand_text_view1).findViewById(R.id.expand_text_view1);

        // calling setText on the ExpandableTextView so that
        // text content will be  displayed to the user
        //expTv1.setText(getString(R.string.expandable_text1));

        //expTv2 = (ExpandableTextView) findViewById(R.id.expand_text_view2).findViewById(R.id.expand_text_view2);
        //expTv2.setText(getString(R.string.expandable_text2));
    }
}