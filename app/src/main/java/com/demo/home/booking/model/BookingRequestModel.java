package com.demo.home.booking.model;

import androidx.annotation.Keep;

@Keep
public class BookingRequestModel {
    public String UserID;
    public String Latitude;
    public String Longitude;


    public String UserAddress;

    public String getUserAddress() {
        return UserAddress;
    }

    public void setUserAddress(String userAddress) {
        UserAddress = userAddress;
    }

    public String getAddressType() {
        return AddressType;
    }

    public void setAddressType(String addressType) {
        AddressType = addressType;
    }

    public String AddressType;
    public String MeetTypeID;
    public String MeetDate;
    public String MeetTime;
    public String MeetBookingType;
    public String MeetCallType="";

    public String getSpecialistID() {
        return SpecialistID;
    }

    public void setSpecialistID(String specialistID) {
        SpecialistID = specialistID;
    }

    public String SpecialistID;

    public String getMeetTypeID() {
        return MeetTypeID;
    }

    public void setMeetTypeID(String meetTypeID) {
        MeetTypeID = meetTypeID;
    }

    public String getMeetDate() {
        return MeetDate;
    }

    public void setMeetDate(String meetDate) {
        MeetDate = meetDate;
    }

    public String getMeetTime() {
        return MeetTime;
    }

    public void setMeetTime(String meetTime) {
        MeetTime = meetTime;
    }

    public String getMeetBookingType() {
        return MeetBookingType;
    }

    public void setMeetBookingType(String meetBookingType) {
        MeetBookingType = meetBookingType;
    }

    public String getMeetCallType() {
        return MeetCallType;
    }

    public void setMeetCallType(String meetCallType) {
        MeetCallType = meetCallType;
    }

    public String CarID;
    public String DemoTypeID;
    public String DemoDate;
    public String DemoTime;
    public String DemoBookingType;

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

    public String getDemoTypeID() {
        return DemoTypeID;
    }

    public void setDemoTypeID(String demoTypeID) {
        DemoTypeID = demoTypeID;
    }

    public String getDemoDate() {
        return DemoDate;
    }

    public void setDemoDate(String demoDate) {
        DemoDate = demoDate;
    }

    public String getDemoTime() {
        return DemoTime;
    }

    public void setDemoTime(String demoTime) {
        DemoTime = demoTime;
    }

    public String getDemoBookingType() {
        return DemoBookingType;
    }

    public void setDemoBookingType(String demoBookingType) {
        DemoBookingType = demoBookingType;
    }
}
