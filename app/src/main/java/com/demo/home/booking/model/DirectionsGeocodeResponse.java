package com.demo.home.booking.model;

import androidx.annotation.Keep;

import java.util.ArrayList;

@Keep
public class DirectionsGeocodeResponse {

    public ArrayList<Result> results;
    public String status;


    public ArrayList<Result> getResults() {
        return results;
    }

    public void setResults(ArrayList<Result> results) {
        this.results = results;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    @Keep
    public class Result{
        public String formatted_address;
        public String place_id;


        public String getFormatted_address() {
            return formatted_address;
        }

        public void setFormatted_address(String formatted_address) {
            this.formatted_address = formatted_address;
        }



        public String getPlace_id() {
            return place_id;
        }

        public void setPlace_id(String place_id) {
            this.place_id = place_id;
        }



        public ArrayList<String> getTypes() {
            return types;
        }

        public void setTypes(ArrayList<String> types) {
            this.types = types;
        }

        public ArrayList<String> types;
    }



}
