package com.demo.home.profile;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentStatePagerAdapter;

public class MyDemotripsPager extends FragmentStatePagerAdapter {
    int size;
    public MyDemotripsPager(FragmentManager childFragmentManager, int size) {
        super(childFragmentManager);
        this.size = size;
    }

    @NonNull
    @Override
    public Fragment getItem(int position) {

        return MyDemoTripsListFragment.newInstance(position);
    }

    @Override
    public int getCount() {
        return size;
    }
}
