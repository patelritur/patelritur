package com.demo.home.profile;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentStatePagerAdapter;

import com.demo.home.model.AppContentModel;

public class MyDemotripsPager extends FragmentStatePagerAdapter {
   private int size;
   private MyDemoTripsListFragment myDemoTripsListFragment1;
    private MyDemoTripsListFragment myDemoTripsListFragment2;
    private MyDemoTripsListFragment myDemoTripsListFragment3;
    private  AppContentModel item;
    public MyDemotripsPager(FragmentManager childFragmentManager, int size, AppContentModel item) {
        super(childFragmentManager);
        this.size = size;
        this.item = item;
    }

    public MyDemoTripsListFragment getFragment(int position){
        switch (position){
            case 0:
                return myDemoTripsListFragment1;
            case 1:
                return myDemoTripsListFragment2;
            case 2:
                return myDemoTripsListFragment3;
        }
        return null;
    }
    @NonNull
    @Override
    public Fragment getItem(int position) {
        switch (position){
            case 0:
                myDemoTripsListFragment1 =  MyDemoTripsListFragment.newInstance(position,item);
                return myDemoTripsListFragment1;
            case 1:
                myDemoTripsListFragment2 =  MyDemoTripsListFragment.newInstance(position,item);
                return myDemoTripsListFragment2;
            case 2:
                myDemoTripsListFragment3 =  MyDemoTripsListFragment.newInstance(position,item);
                return myDemoTripsListFragment3;
        }
        return null;
    }

    @Override
    public int getCount() {
        return size;
    }
}
