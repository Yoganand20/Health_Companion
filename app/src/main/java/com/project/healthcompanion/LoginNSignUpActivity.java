package com.project.healthcompanion;

import android.graphics.Rect;
import android.os.Bundle;
import android.view.View;
import android.view.ViewTreeObserver;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentActivity;
import androidx.viewpager2.adapter.FragmentStateAdapter;

import com.google.android.material.tabs.TabLayoutMediator;
import com.project.healthcompanion.databinding.ActivityLoginNSignUpBinding;

public class LoginNSignUpActivity extends AppCompatActivity {
    private ActivityLoginNSignUpBinding binding;

    private final String[] Titles = new String[]{
            "Login", "Sign Up"
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityLoginNSignUpBinding.inflate(getLayoutInflater());
        View view = binding.getRoot();
        setContentView(view);

        /*Setting Tabs and fragments*/
        //initialise and set adapter object to viewPager view
        FragmentStateAdapter fragmentStateAdapter = new LS_ViewPagerAdapter(this);
        binding.viewPagerLs.setAdapter(fragmentStateAdapter);

        //set tabLayoutMediator to viewPager and tabLayout view
        new TabLayoutMediator(binding.tabLayoutLs, binding.viewPagerLs, ((tab, position) -> tab.setText(Titles[position]))).attach();


        /*Hide and show google sign in option when keyboard is opened or closed*/
        //set global layout listener on outer-most constraint layout
        binding.constraintlayoutLs.getViewTreeObserver().addOnGlobalLayoutListener(new ViewTreeObserver.OnGlobalLayoutListener() {
            @Override
            public void onGlobalLayout() {
                Rect r = new Rect();
                binding.constraintlayoutLs.getWindowVisibleDisplayFrame(r);
                int screenHeight = binding.constraintlayoutLs.getRootView().getHeight();
                int keypadHeight = screenHeight - r.bottom;
                if (keypadHeight > screenHeight * 0.15) {
                    binding.constraintLayoutLoginOptions.setVisibility(View.GONE);
                } else {
                    binding.constraintLayoutLoginOptions.setVisibility(View.VISIBLE);
                }
            }});

    }

    @Override
    public void onBackPressed() {
        if (binding.viewPagerLs.getCurrentItem() == 0) {
            super.onBackPressed();
        } else {
            binding.viewPagerLs.setCurrentItem(binding.viewPagerLs.getCurrentItem() - 1);
        }
    }

}


class LS_ViewPagerAdapter extends FragmentStateAdapter {
    private final static int tabCount = 2;

    //default constructor
    public LS_ViewPagerAdapter(@NonNull FragmentActivity fragmentActivity) {
        super(fragmentActivity);
    }

    //set fragments to load here
    @NonNull
    @Override
    public Fragment createFragment(int position) {
        switch (position) {
            case 0:
                return new Login_Fragment();
            case 1:
                return new SignUp_Fragment();
            default:
                return null;
        }
    }

    //returns number of tabs
    @Override
    public int getItemCount() {
        return tabCount;
    }
}