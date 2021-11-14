package com.demo.carDetails;

import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentStatePagerAdapter;

import com.demo.carDetails.model.CarDetailResponse;
import com.demo.launch.model.LaunchResponseModel;

public  class ScreenSlidePagerAdapter extends FragmentStatePagerAdapter {

    public int count;
    private CarDetailResponse carDetailResponse;
    private LaunchResponseModel.Latestlaunchlist bannerlist;


    public ScreenSlidePagerAdapter(FragmentManager fragmentManager) {
        super(fragmentManager);

    }


    // Returns the fragment to display for that page
    @Override
    public Fragment getItem(int position) {
        if(carDetailResponse!=null)
            return ScreenSlidePageFragment.newInstance(carDetailResponse.getCardetail().getCarbanner().get(position).getBannerImage());
        else {
            if(bannerlist.getBannerlist().get(position).getBannerType().equalsIgnoreCase("Image"))
                return ScreenSlidePageFragment.newInstance(bannerlist.getBannerlist().get(position).getBanner());
            else {


                return ScreenSlidePageFragment.newInstance(bannerlist.getBannerlist().get(position));
            }
        }

    }

    @Override
    public int getCount() {
        return count;
    }


    public void setBannerlist(LaunchResponseModel.Latestlaunchlist carDetailResponse) {
        this.bannerlist = carDetailResponse;

    }

    public void setCarDetailResponse(CarDetailResponse carDetailResponse) {
        this.carDetailResponse = carDetailResponse;

    }
}
