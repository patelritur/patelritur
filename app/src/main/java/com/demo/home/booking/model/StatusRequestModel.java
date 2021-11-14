package com.demo.home.booking.model;

import androidx.annotation.Keep;

@Keep
public class StatusRequestModel {

    public String UserID;
    public String BookingID;

    public String getMeetingID() {
        return MeetingID;
    }

    public void setMeetingID(String meetingID) {
        MeetingID = meetingID;
    }

    public String MeetingID;
    public String APICallCount;
    public String FinalAPICallStatus;

    public String Latitude;
    public String Longitude;

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

    public String getUserID() {
        return UserID;
    }

    public void setUserID(String userID) {
        UserID = userID;
    }

    public String getBookingID() {
        return BookingID;
    }

    public void setBookingID(String bookingID) {
        BookingID = bookingID;
    }

    public String getAPICallCount() {
        return APICallCount;
    }

    public void setAPICallCount(String APICallCount) {
        this.APICallCount = APICallCount;
    }

    public String getFinalAPICallStatus() {
        return FinalAPICallStatus;
    }

    public void setFinalAPICallStatus(String finalAPICallStatus) {
        FinalAPICallStatus = finalAPICallStatus;
    }
}
