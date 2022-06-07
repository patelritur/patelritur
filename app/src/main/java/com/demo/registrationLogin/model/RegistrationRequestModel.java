package com.demo.registrationLogin.model;

import androidx.databinding.BaseObservable;
import androidx.databinding.Bindable;

import com.google.gson.annotations.SerializedName;

public class RegistrationRequestModel extends BaseObservable {
    String userId;
    @SerializedName("FirstName")
    String firstName;
    @SerializedName("Email")
    String Email;
    @SerializedName("Mobile")
    String mobileNumber;
    String UserType="1";
    String IsVaccinated=getIsVaccinated();
    String IsSendOfferEmail=getIsSendOfferEmail();
    String IsAcceptTNC=getIsAcceptTNC();
    transient boolean isToReceiveOffers;
    transient boolean isTnCAccepted;
    transient boolean isVaccinated;
    @SerializedName("LastName")
    String lastName;
    @SerializedName("Pin")
    String pin;
    String SocialLogin="";
    String SocialLoginID="";

    public String getIsVaccinated() {
        return  isVaccinated()?"Y":"N";
    }

    public void setIsVaccinated(String isVaccinated) {
        IsVaccinated = isVaccinated;
    }

    public String getIsSendOfferEmail() {
        return  isToReceiveOffers()?"Y":"N";
    }

    public void setIsSendOfferEmail(String isSendOfferEmail) {
        IsSendOfferEmail = isSendOfferEmail;
    }

    public String getIsAcceptTNC() {
        return  isTnCAccepted()?"Y":"N";
    }

    public void setIsAcceptTNC(String isAcceptTNC) {
        IsAcceptTNC = isAcceptTNC;
    }

    @Bindable
    public boolean isToReceiveOffers() {
        return isToReceiveOffers;
    }

    public void setToReceiveOffers(boolean toReceiveOffers) {
        isToReceiveOffers = toReceiveOffers;
    }
    @Bindable
    public boolean isTnCAccepted() {
        return isTnCAccepted;
    }

    public void setTnCAccepted(boolean tnCAccepted) {
        isTnCAccepted = tnCAccepted;
    }
    @Bindable
    public boolean isVaccinated() {
        return isVaccinated;
    }

    public void setVaccinated(boolean vaccinated) {
        isVaccinated = vaccinated;
    }
    @Bindable
    public String getUserId() {
        return userId;
    }


    public void setUserId(String userId) {
        this.userId = userId;
    }
    @Bindable
    public String getFirstName() {
        return firstName;
    }

    public void setFirstName(String firstName) {
        this.firstName = firstName;
    }

    @Bindable
    public String getEmail() {
        return Email;
    }

    public void setEmail(String email) {
        Email = email;
    }

    @Bindable
    public String getMobileNumber() {
        return mobileNumber;
    }

    public void setMobileNumber(String mobileNumber) {
        this.mobileNumber = mobileNumber;
    }

    @Bindable
    public String getLastName() {
        return lastName;
    }

    public void setLastName(String lastName) {
        this.lastName = lastName;
    }

    @Bindable
    public String getPin() {
        return pin;
    }

    public void setPin(String pin) {
        this.pin = pin;
    }

    public String getUserType() {
        return UserType;
    }

    public void setUserType(String userType) {
        UserType = userType;
    }

    public String getSocialLogin() {
        return SocialLogin;
    }

    public void setSocialLogin(String socialLogin) {
        SocialLogin = socialLogin;
    }

    public String getSocialLoginID() {
        return SocialLoginID;
    }

    public void setSocialLoginID(String socialLoginID) {
        SocialLoginID = socialLoginID;
    }
}
