package com.demo.launch;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentStatePagerAdapter;

public class LaunchPagerAdapter extends FragmentStatePagerAdapter {
    int size;
    public LaunchPagerAdapter(FragmentManager childFragmentManager, int size) {
        super(childFragmentManager);
        this.size = size;
    }

    @NonNull
    @Override
    public Fragment getItem(int position) {

        return LaunchPagerFragment.newInstance(position);
    }

    @Override
    public int getCount() {
        return size;
    }
}
