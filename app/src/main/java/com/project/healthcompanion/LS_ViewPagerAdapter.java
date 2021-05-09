package com.project.healthcompanion;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentActivity;
import androidx.viewpager2.adapter.FragmentStateAdapter;


public class LS_ViewPagerAdapter extends FragmentStateAdapter {
    private final static int tabCount=2;

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
