package com.demo.carDetails;

import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.databinding.DataBindingUtil;
import androidx.fragment.app.Fragment;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.resource.bitmap.RoundedCorners;
import com.bumptech.glide.request.RequestOptions;
import com.demo.R;
import com.demo.databinding.ItemImageviewBinding;
import com.demo.databinding.OnboardingScreenBinding;
import com.demo.launch.model.LaunchResponseModel;
import com.demo.utils.Utils;

import java.io.Serializable;

public class ScreenSlidePageFragment extends Fragment {
    private String imageurl;
    private LaunchResponseModel.Bannerlist bannerlist;

    public static Fragment newInstance(String carDetailResponse) {
        ScreenSlidePageFragment fragmentFirst = new ScreenSlidePageFragment();
        Bundle args = new Bundle();
        args.putString("image",carDetailResponse);
        fragmentFirst.setArguments(args);
        return fragmentFirst;
    }

    public static Fragment newInstance(LaunchResponseModel.Bannerlist bannerlist) {
        ScreenSlidePageFragment fragmentFirst = new ScreenSlidePageFragment();
        Bundle args = new Bundle();
        args.putSerializable("bannerList", (Serializable) bannerlist);
        fragmentFirst.setArguments(args);
        return fragmentFirst;
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        imageurl = getArguments().getString("image");
        if(getArguments().getSerializable("bannerList")!=null){
            bannerlist = (LaunchResponseModel.Bannerlist) getArguments().getSerializable("bannerList");
        }

    }



    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        super.onCreateView(inflater,container, savedInstanceState);
        ItemImageviewBinding onboardingScreenBinding = DataBindingUtil.inflate(inflater,R.layout.item_imageview,container,false);

        if(bannerlist!=null) {
            String vdideoId = bannerlist.getBanner().replace("https://youtu.be/", "");
            String youTubeThumbnail = "https://img.youtube.com/vi/" + vdideoId + "/0.jpg";
            onboardingScreenBinding.setImageUrl(youTubeThumbnail);
            if(bannerlist.getBannerType().equalsIgnoreCase("Video")){
                onboardingScreenBinding.thumb.setVisibility(View.VISIBLE);
            }
            onboardingScreenBinding.imageView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if(bannerlist.getBannerType().equalsIgnoreCase("Video")) {
                        Utils.openChromewithUrl(getActivity(), bannerlist.getBanner());
                    }
                }
            });
        }
        else
        {
            Log.v("imageurl",""+imageurl);
            onboardingScreenBinding.setImageUrl(imageurl);
        }


        onboardingScreenBinding.executePendingBindings();
        return onboardingScreenBinding.getRoot();
    }
}
