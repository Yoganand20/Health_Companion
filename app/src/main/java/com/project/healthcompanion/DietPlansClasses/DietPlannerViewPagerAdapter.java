package com.project.healthcompanion.DietPlansClasses;

/*
public class DietPlannerPagerAdapter extends FragmentPagerAdapter {

    private int numOfTabs;

    public DietPlannerPagerAdapter(FragmentManager fm, int numOfTabs) {
        super(fm);

        this.numOfTabs = numOfTabs;
    }

    @Override
    public Fragment getItem(int position) {

        switch (position) {
            case 0:
                return new BreakfastFragment();
            case 1:
                return new LunchFragment();
            case 2:
                return new DinnerFragment();
            case 3:
                return new SnacksFragment();
            default:
                return null;
        }

    }

    @Override
    public int getCount() {
        return numOfTabs;
    }
}*/

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentActivity;
import androidx.viewpager2.adapter.FragmentStateAdapter;

import org.jetbrains.annotations.NotNull;

public class DietPlannerViewPagerAdapter extends FragmentStateAdapter {

    private static int tabCount = 4;
    private final String[] mealNames;

    public DietPlannerViewPagerAdapter(FragmentActivity fragmentActivity, String[] mealNames) {
        super(fragmentActivity);
        this.mealNames = mealNames;
        tabCount = mealNames.length;
    }

    @NonNull
    @NotNull
    @Override
    public Fragment createFragment(int position) {
        return new LunchFragment(mealNames[position]);
    }

    @Override
    public int getItemCount() {
        return tabCount;
    }
}