package com.ateb2go.paintertool;

import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;

public class AppreciateAdapter extends FragmentPagerAdapter {

    Fragment[] fragments=new Fragment[4];
    String[] tabTitles=new String[]{"Newspeed", "Weekly", "Monthly"};

    public AppreciateAdapter(FragmentManager fm) {
        super(fm);
        fragments[0]=new NewPictureFragment();
        fragments[1]=new WeeklyBestFragment();
        fragments[2]=new MonthlyBestFragment();
    }

    @Override
    public Fragment getItem(int i) {
        return fragments[i];
    }

    @Override
    public int getCount() {
        return 3;
    }

    @Nullable
    @Override
    public CharSequence getPageTitle(int position) {
        return tabTitles[position];
    }
}
