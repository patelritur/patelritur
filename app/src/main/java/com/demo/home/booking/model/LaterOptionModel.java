package com.demo.home.booking.model;

import androidx.annotation.Keep;

import com.google.gson.annotations.SerializedName;

@Keep
public class LaterOptionModel {

    @SerializedName("ResponseCode")
    public String ResponseCode;

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

    public laterbookingoption getLaterbookingoption() {
        return laterbookingoption;
    }

    public void setLaterbookingoption(laterbookingoption laterbookingoption) {
        this.laterbookingoption = laterbookingoption;
    }

    @SerializedName("Descriptions")
    public String Descriptions;
    @SerializedName("laterbookingoption")
    public laterbookingoption laterbookingoption;


    @Keep
    public class laterbookingoption{
        @SerializedName("NoDaysForBooking")
        public String NoDaysForBooking;
        @SerializedName("BookingStartTime")
        public String BookingStartTime;

        public String getNoDaysForBooking() {
            return NoDaysForBooking;
        }

        public void setNoDaysForBooking(String noDaysForBooking) {
            this.NoDaysForBooking = noDaysForBooking;
        }

        public String getBookingStartTime() {
            return BookingStartTime;
        }

        public void setBookingStartTime(String bookingStartTime) {
            this.BookingStartTime = bookingStartTime;
        }

        public String getBookingEndTime() {
            return BookingEndTime;
        }

        public void setBookingEndTime(String bookingEndTime) {
            this.BookingEndTime = bookingEndTime;
        }

        @SerializedName("BookingEndTime")
        public String BookingEndTime;
    }
    
    
}
