package com.project.healthcompanion;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.RadioGroup;
import android.widget.Spinner;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentActivity;
import androidx.viewpager2.adapter.FragmentStateAdapter;
import androidx.viewpager2.widget.ViewPager2;

import com.tbuonomo.viewpagerdotsindicator.DotsIndicator;

public class GetUserInfo extends AppCompatActivity {

    private Button buttonNext, buttonPrev, buttonDone;
    private EditText editText_fName, editText_lName, editText_DOB, editText_weight, editText_height;
    private RadioGroup rg_Gender;
    private Spinner spinner_activityLevels;
    private ViewPager2 viewPager;
    private DotsIndicator dotsIndicator;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_get_user_info);

        setViews();

        //disable swiping in viewPager
        viewPager.setUserInputEnabled(false);

        //initialize and set adapter to viewPager
        GetUserInfoFragmentAdapter adapter = new GetUserInfoFragmentAdapter(this);
        viewPager.setAdapter(adapter);

        //set dotsIndicator on viewPager
        dotsIndicator.setViewPager2(viewPager);

        buttonNext.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                viewPager.setCurrentItem(viewPager.getCurrentItem() + 1);
            }
        });

        buttonPrev.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                viewPager.setCurrentItem(viewPager.getCurrentItem() - 1);
            }
        });

        buttonDone.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(GetUserInfo.this, HomePage.class);
                startActivity(intent);
            }
        });

        viewPager.registerOnPageChangeCallback(pageChangeCallback);
    }

    private void setViews() {
        //set buttons
        buttonNext = findViewById(R.id.button_next);
        buttonDone = findViewById(R.id.button_done);
        buttonPrev = findViewById(R.id.button_prev);

        //set ViewPager2 and related
        viewPager = findViewById(R.id.viewPager2_UserInfo);
        dotsIndicator = findViewById(R.id.dotsIndicator_uInfoP);

        //set input views from fragments
        editText_fName = findViewById(R.id.editText_FirstName);
        editText_lName = findViewById(R.id.editText_LastName);
        editText_DOB = findViewById(R.id.editText_DOB);
        editText_weight = findViewById(R.id.editText_Weight);
        editText_height = findViewById(R.id.editText_Height);
        rg_Gender = findViewById(R.id.radioGroup_Gender);
        spinner_activityLevels = findViewById(R.id.activityLevelSpinner);

    }

    @Override
    public void onBackPressed() {
        if (viewPager.getCurrentItem() == 0) {
            super.onBackPressed();
            return;
        }
        viewPager.setCurrentItem(viewPager.getCurrentItem() - 1);
    }

    private ViewPager2.OnPageChangeCallback pageChangeCallback = new ViewPager2.OnPageChangeCallback() {
        @Override
        public void onPageSelected(int position) {
            switch (position) {
                case 0:
                    buttonPrev.setVisibility(View.INVISIBLE);
                    buttonDone.setVisibility(View.GONE);
                    buttonNext.setVisibility(View.VISIBLE);
                    break;

                case 1:
                    buttonPrev.setVisibility(View.VISIBLE);
                    buttonDone.setVisibility(View.GONE);
                    buttonNext.setVisibility(View.VISIBLE);
                    break;

                case 2:
                    buttonPrev.setVisibility(View.VISIBLE);
                    buttonDone.setVisibility(View.VISIBLE);
                    buttonNext.setVisibility(View.INVISIBLE);
                    break;
            }
            super.onPageSelected(position);
        }
    };


}


class GetUserInfoFragmentAdapter extends FragmentStateAdapter {

    private static int numberOfFragments = 3;

    public GetUserInfoFragmentAdapter(@NonNull FragmentActivity fragmentActivity) {
        super(fragmentActivity);
    }

    @NonNull
    @Override
    public Fragment createFragment(int position) {

        switch (position) {
            case 0:
                return new Personal_Info();
            case 1:
                return new PhysiqueInfoFragment();
            case 2:
                return new ActivityLevelFragment();
            default:
                return null;
        }
    }

    @Override
    public int getItemCount() {
        return numberOfFragments;
    }
}


