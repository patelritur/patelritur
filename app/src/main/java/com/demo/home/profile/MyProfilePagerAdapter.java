package com.demo.home.profile;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentStatePagerAdapter;


public class MyProfilePagerAdapter extends FragmentStatePagerAdapter {
    int size;
    public MyProfilePagerAdapter(FragmentManager supportFragmentManager, int size) {
        super(supportFragmentManager);
        this.size = size;
    }

    @NonNull
    @Override
    public Fragment getItem(int position) {
        if(position==0){
            return MyDemoTripsFragment.newInstance(position);

        }
        else if(position==1){
            return FavouriteSpecialistFragment.newInstance(position);
        }
        else if(position==2)
        {
            return NotificationFragment.newInstance(position);
        }
        return null;
}

    @Override
    public int getCount() {
        return 3;
    }
}
