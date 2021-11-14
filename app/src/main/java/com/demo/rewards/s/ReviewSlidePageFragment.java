package com.demo.rewards.s;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import androidx.annotation.Nullable;
import androidx.databinding.DataBindingUtil;
import androidx.fragment.app.Fragment;

import com.demo.R;
import com.demo.databinding.ItemImageviewBinding;
import com.demo.rewards.s.model.RewardsResponseModel;
import com.demo.utils.Utils;

import java.io.Serializable;

public class ReviewSlidePageFragment extends Fragment {
    private RewardsResponseModel rewardsResponseModel;
    private int position;

    public static Fragment newInstance(RewardsResponseModel carDetailResponse,int position) {
        ReviewSlidePageFragment fragmentFirst = new ReviewSlidePageFragment();
        Bundle args = new Bundle();
        args.putSerializable("image", (Serializable) carDetailResponse);
        args.putInt("position",position);
        fragmentFirst.setArguments(args);
        return fragmentFirst;
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        rewardsResponseModel = (RewardsResponseModel) getArguments().getSerializable("image");
        position = getArguments().getInt("position");

    }



    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        super.onCreateView(inflater,container, savedInstanceState);
        ItemImageviewBinding onboardingScreenBinding = DataBindingUtil.inflate(inflater,R.layout.item_imageview,container,false);
        onboardingScreenBinding.setImageUrl(rewardsResponseModel.getOfferrewardslist().get(position).getThumbBanner());
        if(rewardsResponseModel.getOfferrewardslist().get(position).getBannerType().equalsIgnoreCase("Video")) {
            onboardingScreenBinding.thumb.setVisibility(View.VISIBLE);
        }
        onboardingScreenBinding.imageView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(rewardsResponseModel.getOfferrewardslist().get(position).getBannerType().equalsIgnoreCase("Video")) {
                    Utils.openChromewithUrl(getActivity(),rewardsResponseModel.getOfferrewardslist().get(position).getPopUpBanner());

                }
                else
                {
                    if(rewardsResponseModel.getOfferrewardslist().get(position).getBannerURL().trim().length()>0)
                    {
                        Utils.openChromewithUrl(getActivity(),rewardsResponseModel.getOfferrewardslist().get(position).getBannerURL());
                    }
                }
            }
        });
        onboardingScreenBinding.imageView.setScaleType(ImageView.ScaleType.FIT_CENTER);
        onboardingScreenBinding.executePendingBindings();
        return onboardingScreenBinding.getRoot();
    }

}
