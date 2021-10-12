package com.demo.carDetails;

import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentStatePagerAdapter;

import com.demo.carDetails.model.CarDetailResponse;

public  class ScreenSlidePagerAdapter extends FragmentStatePagerAdapter {

    int count;
    private CarDetailResponse carDetailResponse;


    public ScreenSlidePagerAdapter(FragmentManager fragmentManager) {
        super(fragmentManager);

    }

    // Returns the fragment to display for that page
    @Override
    public Fragment getItem(int position) {
        return ScreenSlidePageFragment.newInstance(carDetailResponse.getCardetail().getCarbanner().get(position).getBannerImage());

    }

    @Override
    public int getCount() {
        return count;
    }


    public void setCarDetailResponse(CarDetailResponse carDetailResponse) {
        this.carDetailResponse = carDetailResponse;

    }
}
