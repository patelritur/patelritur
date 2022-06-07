package com.demo.registrationLogin.model;


public class LoginResponseModel {
/*    "ResponseCode": "103",
            "Descriptions": "OTP has successfully sent.",
            "SessionID": "19"*/

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

    public String getSessionID() {
        return SessionID;
    }

    public void setSessionID(String sessionID) {
        this.SessionID = sessionID;
    }

    public String ResponseCode;
    public String Descriptions;
    public String SessionID;

    public String getMobile() {
        return Mobile;
    }

    public void setMobile(String mobile) {
        Mobile = mobile;
    }

    public String Mobile;

    public String getTNCLink() {
        return TNCLink;
    }

    public void setTNCLink(String TNCLink) {
        this.TNCLink = TNCLink;
    }

    public String TNCLink;

    public String OTPStatus;

}
