package com.demo.carDetails;

import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentStatePagerAdapter;

import com.demo.carDetails.model.CarDetailResponse;
import com.demo.launch.model.LaunchResponseModel;

import java.util.ArrayList;
import java.util.List;

public  class ScreenSlidePagerAdapter extends FragmentStatePagerAdapter {

    public int count;
    private CarDetailResponse carDetailResponse;
    private LaunchResponseModel.Latestlaunchlist bannerlist;
    private ScreenSlidePageFragment screenSlidePageFragment;
    private ScreenSlidePageVideoFragment screenSlidePageVideoFragment;


    public ScreenSlidePagerAdapter(FragmentManager fragmentManager) {
        super(fragmentManager);

    }



    public ScreenSlidePageVideoFragment getFragment(){

        return screenSlidePageVideoFragment;
    }
    public ScreenSlidePageFragment getFragment(int position){

        return screenSlidePageFragment;
    }
    // Returns the fragment to display for that page
    @Override
    public Fragment getItem(int position) {
        if(carDetailResponse!=null) {
            screenSlidePageFragment = ScreenSlidePageFragment.newInstance(carDetailResponse.getCardetail().getCarbanner().get(position));
            return screenSlidePageFragment;
        }
        else {
            if(bannerlist.getBannerlist().get(position).getBannerType().equalsIgnoreCase("Image"))
            {
                screenSlidePageFragment = ScreenSlidePageFragment.newInstance(bannerlist.getBannerlist().get(position));
                return screenSlidePageFragment;
            }
            else{
                 screenSlidePageVideoFragment = ScreenSlidePageVideoFragment.newInstance(bannerlist.getBannerlist().get(position));
                return screenSlidePageVideoFragment;
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
