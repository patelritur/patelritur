package com.demo.home.booking.model;

import androidx.annotation.Keep;

import java.io.Serializable;

@Keep
public class ReviewRequestModel implements Serializable {

    private float ReviewRating;
    private String  ReviewDesc;
    private String BookingID;

    public String getSpecialListID() {
        return SpecialListID;
    }

    public void setSpecialListID(String specialListID) {
        SpecialListID = specialListID;
    }

    public String getUserID() {
        return UserID;
    }

    public void setUserID(String userID) {
        UserID = userID;
    }

    private String SpecialListID;
    private String UserID;

    public float getReviewRating() {
        return ReviewRating;
    }

    public void setReviewRating(float reviewRating) {
        ReviewRating = reviewRating;
    }

    public String getReviewDesc() {
        return ReviewDesc;
    }

    public void setReviewDesc(String reviewDesc) {
        ReviewDesc = reviewDesc;
    }

    public String getBookingID() {
        return BookingID;
    }

    public void setBookingID(String bookingID) {
        BookingID = bookingID;
    }

    public String getMeetID() {
        return MeetID;
    }

    public void setMeetID(String meetID) {
        MeetID = meetID;
    }

    private String MeetID;

}
