package com.demo.utils;

import android.annotation.SuppressLint;
import android.content.Context;
import android.os.AsyncTask;
import android.os.Environment;
import android.text.TextUtils;


import java.io.BufferedInputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.URL;
import java.net.URLConnection;

/**
 * The type Download file from url.
 * this class is used fopr downloading csv file for ride detail for every bike
 * once file is downloaded it will be saved as fileName value in that object
 * internet check is checked before executing this class
 */
@SuppressLint("StaticFieldLeak")
public class DownloadFileFromURL extends AsyncTask<Void, Void, String> {

    private final String csvFileUrl;
    private final String csvFileName;
    private final FileDownloadReady fileDownloadReady;
    public static final String FOLDER = "/DEMO";

    /**
     * Instantiates mapView new Download file from url.
     *
     * @param csvFileUrl        the csv file url
     * @param csvFileName       the csv file name
     * @param fileDownloadReady the file download ready
     */
    public DownloadFileFromURL(String csvFileUrl, String csvFileName, FileDownloadReady fileDownloadReady) {
        this.csvFileUrl = csvFileUrl;
        this.csvFileName = csvFileName;
        this.fileDownloadReady = fileDownloadReady;
    }

    /**
     * Before starting background thread
     * Show Progress Bar Dialog
     */
    @Override
    protected void onPreExecute() {
        super.onPreExecute();
    }

    /**
     * Downloading file in background thread
     */
    @Override
    protected String doInBackground(Void... voids) {
        int count;
        if(new File(csvFileName).exists())
            return "";
        try {
            final URL url = new URL(csvFileUrl);
            final URLConnection connection = url.openConnection();
            connection.connect();

            final InputStream input = new BufferedInputStream(url.openStream(), 8192);
            final OutputStream output = new FileOutputStream(new File(getFilepath(csvFileName + "")));
            final byte data[] = new byte[1024];
            while ((count = input.read(data)) != -1) {
                output.write(data, 0, count);
            }

            // flushing output
            output.flush();

            // closing streams
            output.close();
            input.close();

            return "";
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }

    private String getFilepath(String filename) {
        File demoDirectory = new File(Environment.getExternalStorageDirectory() + FOLDER);
// have the object build the directory structure, if needed.
        if (!demoDirectory.mkdirs())
            demoDirectory.mkdirs();
        return Environment.getExternalStorageDirectory() + FOLDER + File.separator + filename;

    }

    /**
     * After completing background task
     * Dismiss the progress dialog
     **/
    @Override
    protected void onPostExecute(String result) {
        if (TextUtils.isEmpty(result)) {
            //openAnalyseActivity();
            fileDownloadReady.onFileDownloaded(csvFileName);
        }
    }


}

