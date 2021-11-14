package com.demo.utils;

import static android.Manifest.permission.RECORD_AUDIO;
import static android.Manifest.permission.WRITE_EXTERNAL_STORAGE;
import static com.facebook.FacebookSdk.getApplicationContext;

import android.content.pm.PackageManager;
import android.media.MediaRecorder;
import android.os.Environment;
import android.util.Log;

import androidx.core.content.ContextCompat;

import java.io.IOException;

public class RecorderUtils
{

    private MediaRecorder mRecorder;
 public void stopRecording(){
     mRecorder.stop();
 }
    public void startRecording() {
        // check permission method is used to check
        // that the user has granted permission
        // to record nd store the audio.
       {



            // we are here initializing our filename variable
            // with the path of the recorded audio file.
           String mFileName = Environment.getExternalStorageDirectory().getAbsolutePath();
            mFileName += "/AudioRecording.mp3";

            // below method is used to initialize
            // the media recorder clss
            mRecorder = new MediaRecorder();

            // below method is used to set the audio
            // source which we are using a mic.
            mRecorder.setAudioSource(MediaRecorder.AudioSource.MIC);
            mRecorder.setMaxDuration(30000);

            // below method is used to set
            // the output format of the audio.
            mRecorder.setOutputFormat(MediaRecorder.OutputFormat.MPEG_4);

            // below method is used to set the
            // audio encoder for our recorded audio.
            mRecorder.setAudioEncoder(MediaRecorder.AudioEncoder.AAC);

            // below method is used to set the
            // output file location for our recorded audio
            mRecorder.setOutputFile(mFileName);
            try {
                // below mwthod will prepare
                // our audio recorder class
                mRecorder.prepare();
            } catch (IOException e) {
                Log.e("TAG", "prepare() failed");
            }
            // start method will start
            // the audio recording.
            mRecorder.start();
        }
    }



}
