package com.demo.home.model;

import androidx.annotation.Keep;

import com.google.gson.annotations.SerializedName;

@Keep
public class ProfileModel {

    private String image;

    public String getUserID() {
        return userID;
    }

    public void setUserID(String userID) {
        this.userID = userID;
    }

    public String getFirstName() {
        return firstName;
    }

    public void setFirstName(String firstName) {
        this.firstName = firstName;
    }

    public String getLastName() {
        return lastName;
    }

    public void setLastName(String lastName) {
        this.lastName = lastName;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public String getLandmark() {
        return landmark;
    }

    public void setLandmark(String landmark) {
        this.landmark = landmark;
    }

    public String getCity() {
        return city;
    }

    public void setCity(String city) {
        this.city = city;
    }

    public String getDistrictID() {
        return districtID;
    }

    public void setDistrictID(String districtID) {
        this.districtID = districtID;
    }

    public String getStateID() {
        return stateID;
    }

    public void setStateID(String stateID) {
        this.stateID = stateID;
    }

    public String getPincode() {
        return pincode;
    }

    public void setPincode(String pincode) {
        this.pincode = pincode;
    }

    public String getMobile() {
        return mobile;
    }

    public void setMobile(String mobile) {
        this.mobile = mobile;
    }

    @SerializedName("UserID")
    public String userID;
    @SerializedName("FirstName")
    public String firstName;
    @SerializedName("LastName")
    public String lastName;
    @SerializedName("Email")
    public String email;
    @SerializedName("Address")
    public String address;
    @SerializedName("Landmark")
    public String landmark="";
    @SerializedName("City")
    public String city="";
    @SerializedName("DistrictID")
    public String districtID="";
    @SerializedName("StateID")
    public String stateID="";
    @SerializedName("Pincode")
    public String pincode="";
    @SerializedName("Mobile")
    public String mobile;

    public void setImage(String image) {
        this.image = image;
    }

    public String getImage() {
        return image;
    }
}
