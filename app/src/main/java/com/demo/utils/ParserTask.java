package com.demo.utils;

import android.graphics.Color;
import android.os.AsyncTask;

import androidx.appcompat.widget.AppCompatTextView;

import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.PolylineOptions;

import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

/** A class to parse the Google Places in JSON format */
public class ParserTask extends AsyncTask<String, Integer, List<List<HashMap<String,String>>> > {
    AppCompatTextView map;



    public ParserTask(AppCompatTextView googleMap)
     {
         map= googleMap;
     }
    // Parsing the data in non-ui thread
    @Override
    protected List<List<HashMap<String, String>>> doInBackground(String... jsonData) {

        JSONObject jObject;
        List<List<HashMap<String, String>>> routes = null;

        try{
            jObject = new JSONObject(jsonData[0]);
            DirectionsJSONParser parser = new DirectionsJSONParser();

// Starts parsing data
            routes = parser.parse(jObject);
        }catch(Exception e){
            e.printStackTrace();
        }
        return routes;
    }

    // Executes in UI thread, after the parsing process
    @Override
    protected void onPostExecute(List<List<HashMap<String, String>>> result) {

        ArrayList points = null;
        PolylineOptions lineOptions = null;

// Traversing through all the routes
        for(int i=0;i<result.size();i++){
            points = new ArrayList();
            lineOptions = new PolylineOptions();

// Fetching i-th route
            List<HashMap<String, String>> path = result.get(i);

// Fetching all the points in i-th route
            for(int j=0;j <path.size();j++){
                HashMap<String,String> point = path.get(j);

                double lat = Double.parseDouble(point.get("lat"));
                double lng = Double.parseDouble(point.get("lng"));
                PrintLog.v("==path");
                LatLng position = new LatLng(lat, lng);
                if(j==0){    // Get distance from the list
                   String distance = (String)point.get("distance");
                    map.setText("("+distance+") away");
                    continue;
                }else if(j==1){ // Get duration from the list
                  String  duration = (String)point.get("duration");

                    PrintLog.v("==path"+duration);
                    continue;
                }

                points.add(position);
            }

// Adding all the points in the route to LineOptions
            lineOptions.addAll(points);
            lineOptions.width(2);
            lineOptions.color(Color.RED);
        }

// Drawing polyline in the Google Map for the i-th route
    }
}
