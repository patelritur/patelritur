/*
package com.demo.carDetails;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.Nullable;

import com.demo.R;
import com.google.android.youtube.player.YouTubeInitializationResult;
import com.google.android.youtube.player.YouTubePlayer;
import com.google.android.youtube.player.YouTubePlayerFragment;
import com.google.android.youtube.player.YouTubePlayerView;

public class YouTubedialogFragment extends YouTubePlayerFragment {


        public YouTubedialogFragment() {
            // Empty constructor is required for DialogFragment
            // Make sure not to add arguments to the constructor
            // Use `newInstance` instead as shown below
        }

        public static YouTubedialogFragment newInstance(String videoId) {
            YouTubedialogFragment frag = new YouTubedialogFragment();
            Bundle args = new Bundle();
            args.putString("videoId", videoId);
            frag.setArguments(args);
            return frag;
        }

        @Override
        public View onCreateView(LayoutInflater inflater, ViewGroup container,
                                 Bundle savedInstanceState) {
            return inflater.inflate(R.layout.dialog_youtube, container);
        }

        @Override
        public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
            super.onViewCreated(view, savedInstanceState);
            // Get field from view
            // Fetch arguments from bundle and set title
            String videoId = getArguments().getString("videoId", "Enter Name");
            YouTubePlayerView youTubePlayerView =
                    (YouTubePlayerView) view.findViewById(R.id.youtube_view);

            youTubePlayerView.initialize("AIzaSyAxa2b_8XFwtBB24Z46pw1ixsQ98hay5dQ",
                    new YouTubePlayer.OnInitializedListener() {
                        @Override
                        public void onInitializationSuccess(YouTubePlayer.Provider provider,
                                                            YouTubePlayer youTubePlayer, boolean b) {

                            // do any work here to cue video, play video, etc.
                            youTubePlayer.cueVideo(videoId);
                        }
                        @Override
                        public void onInitializationFailure(YouTubePlayer.Provider provider,
                                                            YouTubeInitializationResult youTubeInitializationResult) {

                        }
                    });

            // Show soft keyboard automatically and request focus to field
        }

}
*/
