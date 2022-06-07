package com.demo.carDetails;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.databinding.DataBindingUtil;
import androidx.fragment.app.Fragment;

import com.demo.R;
import com.demo.carDetails.model.CarDetailResponse;
import com.demo.databinding.ItemImageviewBinding;
import com.demo.launch.model.LaunchResponseModel;
import com.demo.utils.PrintLog;
import com.pierfrancescosoffritti.androidyoutubeplayer.core.player.PlayerConstants;
import com.pierfrancescosoffritti.androidyoutubeplayer.core.player.YouTubePlayer;
import com.pierfrancescosoffritti.androidyoutubeplayer.core.player.listeners.AbstractYouTubePlayerListener;
import com.pierfrancescosoffritti.androidyoutubeplayer.core.player.listeners.YouTubePlayerListener;
import com.pierfrancescosoffritti.androidyoutubeplayer.core.player.views.YouTubePlayerView;

import java.io.Serializable;

public class ScreenSlidePageFragment extends Fragment  {
    private LaunchResponseModel.Bannerlist launchBannerlist;
    private CarDetailResponse.Carbanner carDetailCarBanner;
    private  YouTubePlayer youTubePlayer;
    private ItemImageviewBinding onboardingScreenBinding;

    public static ScreenSlidePageFragment newInstance(CarDetailResponse.Carbanner carDetailResponse) {
        ScreenSlidePageFragment fragmentFirst = new ScreenSlidePageFragment();
        Bundle args = new Bundle();
        args.putSerializable("carbanner",(Serializable) carDetailResponse);
        fragmentFirst.setArguments(args);
        return fragmentFirst;
    }



    public static ScreenSlidePageFragment newInstance( LaunchResponseModel.Bannerlist bannerlist) {
        ScreenSlidePageFragment fragmentFirst = new ScreenSlidePageFragment();
        Bundle args = new Bundle();
        args.putSerializable("bannerList", (Serializable) bannerlist);
        fragmentFirst.setArguments(args);
        return fragmentFirst;
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        if(getArguments().getSerializable("bannerList")!=null){

            launchBannerlist = (LaunchResponseModel.Bannerlist) getArguments().getSerializable("bannerList");
            PrintLog.v("launch change"+launchBannerlist.getBanner());
        }
        else  if(getArguments().getSerializable("carbanner")!=null){
            carDetailCarBanner = (CarDetailResponse.Carbanner) getArguments().getSerializable("carbanner");
        }


    }



    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        super.onCreateView(inflater,container, savedInstanceState);
        onboardingScreenBinding = DataBindingUtil.inflate(inflater,R.layout.item_imageview,container,false);

        if(launchBannerlist !=null) {
            onboardingScreenBinding.setImageUrl(launchBannerlist.getBanner());
        }
        else if(carDetailCarBanner !=null) {
                onboardingScreenBinding.setImageUrl(carDetailCarBanner.getBannerImage());

        }

        onboardingScreenBinding.executePendingBindings();
        return onboardingScreenBinding.getRoot();
    }

    private void showYoutubeVideo(String videoId, YouTubePlayerView youtubePlayerView) {
        getLifecycle().addObserver(youtubePlayerView);
        youtubePlayerView.setVisibility(View.VISIBLE);
        youtubePlayerView.setEnableAutomaticInitialization(true);
        youtubePlayerView.enableBackgroundPlayback(false);
        youtubePlayerView.addYouTubePlayerListener(new AbstractYouTubePlayerListener() {

            @Override
            public void onReady(@NonNull YouTubePlayer youtubePlayer) {
                super.onReady(youtubePlayer);
                youTubePlayer = youtubePlayer;
                youtubePlayer.loadVideo(videoId, 0);
                PrintLog.v("yout change"+videoId);


            }
        });

    }




    public void updateView(String carbannerStr) {

        if(carbannerStr.equalsIgnoreCase("Video")){
            if(youTubePlayer!=null){
                onboardingScreenBinding.youtubePlayerView.setVisibility(View.VISIBLE);
                youTubePlayer.play();
            }
            else{

                if(carDetailCarBanner !=null) {
                    onboardingScreenBinding.youtubePlayerView.setVisibility(View.VISIBLE);
                    showYoutubeVideo(carDetailCarBanner.getBannerImage().replace("https://youtu.be/", ""), onboardingScreenBinding.youtubePlayerView);
                }


            }
        }
        else {
            onboardingScreenBinding.youtubePlayerView.setVisibility(View.GONE);
            if ( youTubePlayer != null)  {
                youTubePlayer.pause();
            }
        }

    }


    public void updateView(String banner, String bannerType) {
        if(bannerType.equalsIgnoreCase("Video")){
            if(youTubePlayer!=null){
                onboardingScreenBinding.youtubePlayerView.setVisibility(View.VISIBLE);
                youTubePlayer.play();
            }
            else {
                if (banner != null) {
                    onboardingScreenBinding.youtubePlayerView.setVisibility(View.VISIBLE);
                    showYoutubeVideo(banner.replace("https://youtu.be/", ""), onboardingScreenBinding.youtubePlayerView);

                }
            }



        }
        else {
            onboardingScreenBinding.youtubePlayerView.setVisibility(View.GONE);
            if ( youTubePlayer != null)  {
                youTubePlayer.pause();
            }
        }
    }
}

