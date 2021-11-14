package com.demo.home.booking.model;

import androidx.annotation.Keep;

import com.google.gson.annotations.SerializedName;

@Keep
public class CurrentStatuSModel {

    @SerializedName("ResponseCode")
    public String ResponseCode;

    @SerializedName(value = "BookingStatus",alternate = "MeetingStatus")
    public String BookingStatus;
    @SerializedName(value = "BookingMessageImage",alternate = "MeetingMessageImage")
    public String BookingMessageImage;

    @SerializedName(value = "BookingSubMessage",alternate = "MeetingSubMessage")
    public String BookingSubMessage;

    @SerializedName(value = "BookingMessage", alternate = "MeetingMessage")
    public String BookingMessage;


    @SerializedName("Descriptions")
    public String Descriptions;



    public String getResponseCode() {
        return ResponseCode;
    }

    public void setResponseCode(String responseCode) {
        ResponseCode = responseCode;
    }

    public String getBookingStatus() {
        return BookingStatus;
    }

    public void setBookingStatus(String bookingStatus) {
        BookingStatus = bookingStatus;
    }

    public String getBookingMessageImage() {
        return BookingMessageImage;
    }

    public void setBookingMessageImage(String bookingMessageImage) {
        BookingMessageImage = bookingMessageImage;
    }

    public String getBookingSubMessage() {
        return BookingSubMessage;
    }

    public void setBookingSubMessage(String bookingSubMessage) {
        BookingSubMessage = bookingSubMessage;
    }

    public String getBookingMessage() {
        return BookingMessage;
    }

    public void setBookingMessage(String bookingMessage) {
        BookingMessage = bookingMessage;
    }

    public String getDescriptions() {
        return Descriptions;
    }

    public void setDescriptions(String descriptions) {
        Descriptions = descriptions;
    }
}
