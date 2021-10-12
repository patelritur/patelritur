package com.demo.registrationLogin.model;


public class RegistrationResponse {

    /* "ResponseCode": "200",
             "Descriptions": "Thank you for registering with us.",
             "UserID": "4"*/
    public String ResponseCode;
    public String Descriptions;

    public String getResponseCode() {
        return ResponseCode;
    }

    public void setResponseCode(String responseCode) {
        this.ResponseCode = responseCode;
    }

    public String getDescriptions() {
        return Descriptions;
    }

    public void setDescriptions(String descriptions) {
        this.Descriptions = descriptions;
    }

    public String getUserID() {
        return UserID;
    }

    public void setUserID(String userID) {
        this.UserID = userID;
    }

    public String UserID;
}
