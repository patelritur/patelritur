package com.demo.launch.model;

import androidx.annotation.Keep;

@Keep
public class LaunchRequestModel {


    public String UserID;
    public String Latitude;

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

    public String getTabType() {
        return TabType;
    }

    public void setTabType(String tabType) {
        TabType = tabType;
    }

    public String getLongitude() {
        return Longitude;
    }

    public void setLongitude(String longitude) {
        Longitude = longitude;
    }

    public String getSearchValue() {
        return SearchValue;
    }

    public void setSearchValue(String searchValue) {
        SearchValue = searchValue;
    }

    public String TabType;
    public String Longitude;
    public String SearchValue;

}
