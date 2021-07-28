package com.project.healthcompanion.DashboardClasses;

import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentPagerAdapter;

public class DashboardPagerAdapter extends FragmentPagerAdapter {
    private int numOfTabs;

    public DashboardPagerAdapter(FragmentManager fm, int numOfTabs) {
        super(fm);

        this.numOfTabs = numOfTabs;
    }

    @Override
    public Fragment getItem(int position) {

        switch (position) {
            case 0:
                return new DashBreakfastFragment();
            case 1:
                return new com.project.healthcompanion.DashboardClasses.DashLunchFragment();
            case 2:
                return new DashDinnerFragment();
            case 3:
                return new DashSnacksFragment();
            default:
                return null;
        }

    }

    @Override
    public int getCount() {
        return numOfTabs;
    }
}
