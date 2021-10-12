package com.demo.registrationLogin.model;


public class CommanRequestModel {

    private static CommanRequestModel instance;


    public String mobile;
    public String userType;
    public String moduleName;
    public String SocialLogin;
    public String SocialLoginID;
    public String OTP;
    public String MobileIdentity;


    public String getSocialLogin() {
        return SocialLogin;
    }

    public void setSocialLogin(String socialLogin) {
        this.SocialLogin = socialLogin;
    }

    public String getSocialLoginId() {
        return SocialLoginID;
    }

    public void setSocialLoginId(String socialLoginId) {
        this.SocialLoginID = socialLoginId;
    }

    public String Pin;

    public String getPin() {
        return Pin;
    }

    public void setPin(String pin) {
        Pin = pin;
    }

    public String getOTP() {
        return OTP;
    }

    public void setOTP(String OTP) {
        this.OTP = OTP;
    }

    public String getMobileIdentity() {
        return MobileIdentity;
    }

    public void setMobileIdentity(String mobileIdentity) {
        MobileIdentity = mobileIdentity;
    }

    public String getMobile() {
        return mobile;
    }

    public void setMobile(String mobile) {
        this.mobile = mobile;
    }

    public String getUserType() {
        return userType;
    }

    public void setUserType(String userType) {
        this.userType = userType;
    }

    public String getModuleName() {
        return moduleName;
    }

    public void setModuleName(String moduleName) {
        this.moduleName = moduleName;
    }



}
