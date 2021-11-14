package com.demo.registrationLogin.model;

import com.google.gson.annotations.SerializedName;

public class AddTokenRequestModel {

    @SerializedName("TokenType")
            String TokenType;
    @SerializedName("DeviceToken")
    String DeviceToken;
    @SerializedName("UserID")
    String UserID;

    public String getTokenType() {
        return TokenType;
    }

    public void setTokenType(String tokenType) {
        TokenType = tokenType;
    }

    public String getDeviceToken() {
        return DeviceToken;
    }

    public void setDeviceToken(String deviceToken) {
        DeviceToken = deviceToken;
    }

    public String getUserID() {
        return UserID;
    }

    public void setUserID(String userID) {
        UserID = userID;
    }
}
