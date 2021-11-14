package com.demo.home.booking.model;

import androidx.annotation.Keep;

import com.google.gson.annotations.SerializedName;

@Keep
public class CancelBookingModel {

   /* {
        "BookingID":"173",
            "CancelReason":"I am not ready to take a demo",
            "BlockDealerStatus":"N"
    }*/

    @SerializedName(value = "BookingID")
    public String bookingID;

    public String getMeetingID() {
        return meetingID;
    }

    public void setMeetingID(String meetingID) {
        this.meetingID = meetingID;
    }

    @SerializedName(value = "MeetingID")
    public String meetingID;


    @SerializedName("CancelReason")
    public String cancelReason;

    public String getBookingID() {
        return bookingID;
    }

    public void setBookingID(String bookingID) {
        this.bookingID = bookingID;
    }

    public String getCancelReason() {
        return cancelReason;
    }

    public void setCancelReason(String cancelReason) {
        this.cancelReason = cancelReason;
    }

    public String getBlockDealerStatus() {
        return blockDealerStatus;
    }

    public void setBlockDealerStatus(String blockDealerStatus) {
        this.blockDealerStatus = blockDealerStatus;
    }

    @SerializedName("BlockDealerStatus")
    public String blockDealerStatus;

}
