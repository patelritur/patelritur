package com.demo.home.booking.model;

import androidx.annotation.Keep;

@Keep
public class LocationResponse {

    public String ResponseCode;
    public String Descriptions;
    public String UserLongitude;
    public String UserLatitude;

    public String getResponseCode() {
        return ResponseCode;
    }

    public void setResponseCode(String responseCode) {
        ResponseCode = responseCode;
    }

    public String getDescriptions() {
        return Descriptions;
    }

    public void setDescriptions(String descriptions) {
        Descriptions = descriptions;
    }

    public String getUserLongitude() {
        return UserLongitude;
    }

    public void setUserLongitude(String userLongitude) {
        UserLongitude = userLongitude;
    }

    public String getUserLatitude() {
        return UserLatitude;
    }

    public void setUserLatitude(String userLatitude) {
        UserLatitude = userLatitude;
    }
}
