package com.demo.home.model;

import androidx.annotation.Keep;

@Keep
public class CarDealerModel {
    private String dealerName="Ritu Patel";
    private String democount="2000";

    public String getDealerName() {
        return dealerName;
    }

    public void setDealerName(String dealerName) {
        this.dealerName = dealerName;
    }

    public String getDemocount() {
        return democount;
    }

    public void setDemocount(String democount) {
        this.democount = democount;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public int getRating() {
        return rating;
    }

    public void setRating(int rating) {
        this.rating = rating;
    }

    private String address="ddMo delhi";
    private int rating=3;

}
