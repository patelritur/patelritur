package com.demo.utils;

import android.app.job.JobParameters;
import android.app.job.JobService;
import android.content.Intent;

public class statusJobService extends JobService {
    private static final String TAG = "SyncService";

    @Override
    public boolean onStartJob(JobParameters params) {
       PrintLog.v("=====job Satrted");

        Utils.scheduleJob(getApplicationContext(), params.getExtras().getInt("count"), params.getExtras().getInt("waitCount"));

            Intent intent = new Intent();
            intent.setAction("callStatusService");
            sendBroadcast(intent);

        return true;
    }

    @Override
    public boolean onStopJob(JobParameters params) {
        return true;
    }

}
