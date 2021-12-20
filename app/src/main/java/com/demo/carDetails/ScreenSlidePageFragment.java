package com.demo.carDetails;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.webkit.WebView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.databinding.DataBindingUtil;
import androidx.databinding.ViewDataBinding;
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

public class ScreenSlidePageFragment extends Fragment  {
    private String imageurl;
    private LaunchResponseModel.Bannerlist bannerlist;
    private CarDetailResponse.Carbanner carbanner;
    private YouTubePlayer youTubePlayer;
    private YouTubePlayerView youTubePlayerView;
    private ItemImageviewBinding onboardingScreenBinding;

    public static ScreenSlidePageFragment newInstance(CarDetailResponse.Carbanner carDetailResponse) {
        ScreenSlidePageFragment fragmentFirst = new ScreenSlidePageFragment();
        Bundle args = new Bundle();
        args.putSerializable("carbanner",(Serializable) carDetailResponse);
        fragmentFirst.setArguments(args);
        return fragmentFirst;
    }

    public static ScreenSlidePageFragment newInstance(String carDetailResponse) {
        ScreenSlidePageFragment fragmentFirst = new ScreenSlidePageFragment();
        Bundle args = new Bundle();
        args.putString("image", carDetailResponse);
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
            bannerlist = (LaunchResponseModel.Bannerlist) getArguments().getSerializable("bannerList");
        }
        else  if(getArguments().getSerializable("carbanner")!=null){
            carbanner = (CarDetailResponse.Carbanner) getArguments().getSerializable("carbanner");
        }
        else{
            imageurl = getArguments().getString("image");
        }

    }



    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        super.onCreateView(inflater,container, savedInstanceState);
         onboardingScreenBinding = DataBindingUtil.inflate(inflater,R.layout.item_imageview,container,false);

        if(bannerlist!=null) {
            String videoId = bannerlist.getBanner().replace("https://youtu.be/", "");
            String youTubeThumbnail = "https://img.youtube.com/vi/" + videoId + "/0.jpg";
            onboardingScreenBinding.setImageUrl(youTubeThumbnail);
            if(bannerlist.getBannerType().equalsIgnoreCase("Video")){
                onboardingScreenBinding.thumb.setVisibility(View.VISIBLE);
            }
            onboardingScreenBinding.imageView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if(bannerlist.getBannerType().equalsIgnoreCase("Video")) {
                      /*  webview = onboardingScreenBinding.webview;
                        webview.onResume();
                        onboardingScreenBinding.webview.setVisibility(View.VISIBLE);
                        showIframeVideo(onboardingScreenBinding.webview,videoId);*/
                        youTubePlayerView = onboardingScreenBinding.youtubePlayerView;
                        showYoutubeVideo(videoId,onboardingScreenBinding.youtubePlayerView);
                        onboardingScreenBinding.youtubePlayerView.setVisibility(View.VISIBLE);
                    }
                }
            });
        }
        else if(carbanner!=null) {
            if (carbanner.getBannerType().equalsIgnoreCase("Video")) {
                String videoId = carbanner.getBannerImage().replace("https://youtu.be/", "");
                String youTubeThumbnail = "https://img.youtube.com/vi/" + videoId + "/0.jpg";
                onboardingScreenBinding.setImageUrl(youTubeThumbnail);

                onboardingScreenBinding.thumb.setVisibility(View.VISIBLE);

                onboardingScreenBinding.imageView.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        if (carbanner.getBannerType().equalsIgnoreCase("Video")) {
                            youTubePlayerView = onboardingScreenBinding.youtubePlayerView;
                            showYoutubeVideo(videoId,onboardingScreenBinding.youtubePlayerView);
                           /* webview = onboardingScreenBinding.webview;
                            webview.onResume();*/

                            onboardingScreenBinding.youtubePlayerView.setVisibility(View.VISIBLE);
                           // showIframeVideo(onboardingScreenBinding.webview,videoId);
                        }
                    }
                });
            } else {

                onboardingScreenBinding.setImageUrl(carbanner.getBannerImage());

            }
        }
        else
        {
            PrintLog.v("=="+imageurl);
            onboardingScreenBinding.setImageUrl(imageurl);
        }
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
                youTubePlayer = youtubePlayer;
                youtubePlayer.loadVideo(videoId, 0);
            }
        });

    }


    private void showIframeVideo(WebView webView, String videoId){
        webView.getSettings().setJavaScriptEnabled(true);

        String path="https://www.youtube.com/embed/"+videoId+"?autoplay=1";
        webView.loadUrl(path);

    }


    public void updateView(String carbannerStr) {
        if(carbannerStr.equalsIgnoreCase("Video")){
            if(youTubePlayer!=null){
                onboardingScreenBinding.youtubePlayerView.setVisibility(View.VISIBLE);
                youTubePlayer.play();
            }
            else{
                showYoutubeVideo(carbanner.getBannerImage().replace("https://youtu.be/", ""),onboardingScreenBinding.youtubePlayerView);
                onboardingScreenBinding.youtubePlayerView.setVisibility(View.VISIBLE);
            }
        }
        else {
            if (onboardingScreenBinding.youtubePlayerView != null && youTubePlayer != null)  {
                onboardingScreenBinding.youtubePlayerView.setVisibility(View.GONE);
                youTubePlayer.pause();
            }
        }

    }

}

