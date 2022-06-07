package com.demo.carDetails.model;

import androidx.annotation.Keep;

@Keep
public class CarDetailRequest {
   /* {
        "UserID":"1",
            "Latitude":"28.591260",
            "Longitude":"77.224000",
            "CarID":"1"
    }
    */

    public String UserID;
    public String Latitude="0.0";
    public String Longitude="0.0";
    public String StateName;

    public String getStateName() {
        return StateName;
    }

    public void setStateName(String stateName) {
        StateName = stateName;
    }

    public String getUserID() {
        return UserID;
    }

    public void setUserID(String userID) {
        UserID = userID;
    }

    public String getLatitude() {
        return Latitude;
    }

    public void setLatitude(String latitude) {
        Latitude = latitude;
    }

    public String getLongitude() {
        return Longitude;
    }

    public void setLongitude(String longitude) {
        Longitude = longitude;
    }

    public String getCarID() {
        return CarID;
    }

    public void setCarID(String carID) {
        CarID = carID;
    }

    public String CarID;
}
