package com.project.healthcompanion;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentActivity;
import androidx.viewpager2.adapter.FragmentStateAdapter;
import androidx.viewpager2.widget.ViewPager2;

import com.project.healthcompanion.databinding.ActivityGetUserInfoBinding;

public class GetUserInfo extends AppCompatActivity {
    private ActivityGetUserInfoBinding binding;
    private UserInfoViewModel userInfoViewModel;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityGetUserInfoBinding.inflate(getLayoutInflater());
        View view = binding.getRoot();
        setContentView(view);

        /*
        //set view model
        userInfoViewModel=new ViewModelProvider(this).get(UserInfoViewModel.class);
        userInfoViewModel.getFirstName().observe(this,value->{
            //TOO: save first name
            //value variable has the data
        });
        userInfoViewModel.getLastName().observe(this,value->{
            //TOO: save first name
        });
        //TOO:call other functions and store their data*/

        //disable swiping in viewPager
        binding.viewPager2UserInfo.setUserInputEnabled(false);
        //initialize and set adapter to viewPager
        GetUserInfoFragmentAdapter adapter = new GetUserInfoFragmentAdapter(this);
        binding.viewPager2UserInfo.setAdapter(adapter);

        //set dotsIndicator on viewPager
        binding.dotsIndicatorUInfoP.setViewPager2(binding.viewPager2UserInfo);

        binding.buttonNext.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                binding.viewPager2UserInfo.setCurrentItem(binding.viewPager2UserInfo.getCurrentItem() + 1);
            }
        });

        binding.buttonPrev.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                binding.viewPager2UserInfo.setCurrentItem(binding.viewPager2UserInfo.getCurrentItem() - 1);
            }
        });

        binding.buttonDone.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(GetUserInfo.this, HomePage.class);
                startActivity(intent);
            }
        });

        binding.viewPager2UserInfo.registerOnPageChangeCallback(pageChangeCallback);
    }

    @Override
    public void onBackPressed() {
        if (binding.viewPager2UserInfo.getCurrentItem() == 0) {
            super.onBackPressed();
            return;
        }
        binding.viewPager2UserInfo.setCurrentItem(binding.viewPager2UserInfo.getCurrentItem() - 1);
    }

    private final ViewPager2.OnPageChangeCallback pageChangeCallback = new ViewPager2.OnPageChangeCallback() {
        @Override
        public void onPageSelected(int position) {
            switch (position) {
                case 0:
                    binding.buttonPrev.setVisibility(View.INVISIBLE);
                    binding.buttonDone.setVisibility(View.GONE);
                    binding.buttonNext.setVisibility(View.VISIBLE);
                    break;

                case 1:
                case 2:
                    binding.buttonPrev.setVisibility(View.VISIBLE);
                    binding.buttonDone.setVisibility(View.GONE);
                    binding.buttonNext.setVisibility(View.VISIBLE);

                    break;

                case 3:
                    binding.buttonPrev.setVisibility(View.VISIBLE);
                    binding.buttonDone.setVisibility(View.VISIBLE);
                    binding.buttonNext.setVisibility(View.INVISIBLE);
                    break;
            }

            super.onPageSelected(position);
        }
    };


}

class GetUserInfoFragmentAdapter extends FragmentStateAdapter {

    private final static int numberOfFragments = 4;

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
            case 3:
                return new InfoResultFragment();
            default:
                return null;
        }
    }

    @Override
    public int getItemCount() {
        return numberOfFragments;
    }
}


