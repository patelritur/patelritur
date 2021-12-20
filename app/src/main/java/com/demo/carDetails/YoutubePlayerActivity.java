/*
package com.demo.carDetails;

import android.os.Bundle;
import android.view.ViewGroup;
import android.widget.FrameLayout;

import com.demo.R;
import com.google.android.youtube.player.YouTubeBaseActivity;
import com.google.android.youtube.player.YouTubeInitializationResult;
import com.google.android.youtube.player.YouTubePlayer;
import com.google.android.youtube.player.YouTubePlayerView;


public class YoutubePlayerActivity extends YouTubeBaseActivity {

    @Override
    protected void onCreate(Bundle bundle) {
        super.onCreate(bundle);
        setContentView(R.layout.dialog_youtube);
        String videoId = getIntent().getExtras().getString("videoId");
        YouTubePlayerView youTubePlayerView = findViewById(R.id.youtube_view);

        youTubePlayerView.initialize("AIzaSyAxa2b_8XFwtBB24Z46pw1ixsQ98hay5dQ",
                new YouTubePlayer.OnInitializedListener() {
                    @Override
                    public void onInitializationSuccess(YouTubePlayer.Provider provider,
                                                        YouTubePlayer youTubePlayer, boolean b) {

                        // do any work here to cue video, play video, etc.
                        youTubePlayer.loadVideo(videoId);
                        youTubePlayer.play();
                    }
                    @Override
                    public void onInitializationFailure(YouTubePlayer.Provider provider,
                                                        YouTubeInitializationResult youTubeInitializationResult) {

                    }
                });
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
    }
}
*/
