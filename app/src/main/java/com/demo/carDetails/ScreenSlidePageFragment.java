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

import com.demo.R;
import com.demo.databinding.ItemImageviewBinding;
import com.demo.databinding.OnboardingScreenBinding;

public class ScreenSlidePageFragment extends Fragment {
    String imageurl;

    public static Fragment newInstance(String carDetailResponse) {
        ScreenSlidePageFragment fragmentFirst = new ScreenSlidePageFragment();
        Bundle args = new Bundle();
        args.putString("image",carDetailResponse);
        fragmentFirst.setArguments(args);
        return fragmentFirst;
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        imageurl = getArguments().getString("image");
        Log.v("imageurl",""+imageurl);

    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        super.onCreateView(inflater,container, savedInstanceState);
        ItemImageviewBinding onboardingScreenBinding = DataBindingUtil.inflate(inflater,R.layout.item_imageview,container,false);
        Log.v("imageurl",""+imageurl);
        onboardingScreenBinding.setImageUrl(imageurl);
        onboardingScreenBinding.executePendingBindings();
        return onboardingScreenBinding.getRoot();
    }
}
