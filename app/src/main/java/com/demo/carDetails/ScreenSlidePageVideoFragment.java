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
import com.pierfrancescosoffritti.androidyoutubeplayer.core.player.YouTubePlayer;
import com.pierfrancescosoffritti.androidyoutubeplayer.core.player.listeners.AbstractYouTubePlayerListener;
import com.pierfrancescosoffritti.androidyoutubeplayer.core.player.views.YouTubePlayerView;

import java.io.Serializable;

public class ScreenSlidePageVideoFragment extends Fragment  {
    private LaunchResponseModel.Bannerlist launchBannerlist;
    private  YouTubePlayer youTubePlayer;
    private ItemImageviewBinding onboardingScreenBinding;




    public static ScreenSlidePageVideoFragment newInstance(LaunchResponseModel.Bannerlist bannerlist) {
        ScreenSlidePageVideoFragment fragmentFirst = new ScreenSlidePageVideoFragment();
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


    }



    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        super.onCreateView(inflater,container, savedInstanceState);
        onboardingScreenBinding = DataBindingUtil.inflate(inflater,R.layout.item_imageview,container,false);

        onboardingScreenBinding.executePendingBindings();
        return onboardingScreenBinding.getRoot();
    }

    private void showYoutubeVideo(String videoId, YouTubePlayerView youtubePlayerView) {
        getLifecycle().addObserver(youtubePlayerView);

        youtubePlayerView.setEnableAutomaticInitialization(true);
        youtubePlayerView.enableBackgroundPlayback(false);
        youtubePlayerView.addYouTubePlayerListener(new AbstractYouTubePlayerListener() {

            @Override
            public void onReady(@NonNull YouTubePlayer youtubePlayer) {
                super.onReady(youtubePlayer);
                youTubePlayer = youtubePlayer;
                        youtubePlayerView.setVisibility(View.VISIBLE);
                        youtubePlayer.loadVideo(videoId, 0);
                        PrintLog.v("yout change"+videoId);


            }
        });

    }



    public void updateView(String banner, String bannerType) {
        if(bannerType.equalsIgnoreCase("Video")){
            if(youTubePlayer!=null){
                PrintLog.v("changeplay");
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
                PrintLog.v("changepause");
                youTubePlayer.pause();
            }
        }
    }


}

