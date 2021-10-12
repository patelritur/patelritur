package com.demo.home;


import android.view.ViewGroup;

import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentPagerAdapter;
import androidx.fragment.app.FragmentStatePagerAdapter;

import com.demo.utils.customviews.AdjustingViewPager;

public  class MyPagerAdapter extends FragmentStatePagerAdapter {
    private static int NUM_ITEMS = 3;
    private int mCurrentPosition = -1;

    public MyPagerAdapter(FragmentManager fragmentManager) {
        super(fragmentManager);
    }



    // Returns total number of pages
    @Override
    public int getCount() {
        return NUM_ITEMS;
    }

    // Returns the fragment to display for that page
    @Override
    public Fragment getItem(int position) {
        return PersonalisedCarOptionsFragment.newInstance(position);

    }
    @Override
    public void setPrimaryItem(ViewGroup container, int position, Object object) {
        super.setPrimaryItem(container, position, object);

        if (position != mCurrentPosition && container instanceof AdjustingViewPager) {
            Fragment fragment = (Fragment) object;
            AdjustingViewPager pager = (AdjustingViewPager) container;

            if (fragment != null && fragment.getView() != null) {
                mCurrentPosition = position;
                pager.measureCurrentView(fragment.getView());
            }
        }
    }



}
