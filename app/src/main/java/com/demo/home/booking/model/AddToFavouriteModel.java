package com.demo.home.booking.model;

import androidx.annotation.Keep;

import com.google.gson.annotations.SerializedName;

import java.io.Serializable;

@Keep
public class AddToFavouriteModel implements Serializable {



    @SerializedName("SpecialistID")
    private String SpecialListID;
    @SerializedName("UserID")
    private String UserID;

    public String getSpecialListID() {
        return SpecialListID;
    }

    public void setSpecialListID(String specialListID) {
        SpecialListID = specialListID;
    }

    public String getUserID() {
        return UserID;
    }

    public void setUserID(String userID) {
        UserID = userID;
    }
}
