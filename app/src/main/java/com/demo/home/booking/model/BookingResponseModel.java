package com.demo.home.booking.model;

import androidx.annotation.Keep;

import com.google.gson.annotations.SerializedName;

import java.util.List;

@Keep
public class BookingResponseModel {

   // {"locationlist":[{"UserID":"0","ShowroomID":"2","Latitude":"28.411566","Longitude":"76.499100","MapIcon":"http://65.2.85.224/Resources/AppIcon/shoroomlocicon.png"},{"UserID":"0","ShowroomID":"1","Latitude":"12.888810","Longitude":"77.549750","MapIcon":"http://65.2.85.224/Resources/AppIcon/shoroomlocicon.png"}],"BookingID":"120","BookingWaitingTimeInMins":"5","BookingSubMessage":"Finding a Demo Specialist for you","BookingMessage":"Connecting now to a demo specialist for Demo at the Dealership","BookingMessageImage":"http://65.2.85.224/Resources/AppIcon/manmsgImg.PNG"}


    @SerializedName("ResponseCode")
    public String responseCode;
    @SerializedName("Descriptions")
    public String descriptions;


    public String getBookingWaitingTimeInMins() {
        return BookingWaitingTimeInMins;
    }

    public void setBookingWaitingTimeInMins(String bookingWaitingTimeInMins) {
        BookingWaitingTimeInMins = bookingWaitingTimeInMins;
    }

    @SerializedName(value = "BookingWaitingTimeInMins",alternate = "MeetingWaitingTimeInMins")
    public String BookingWaitingTimeInMins;


    public String getResponseCode() {
        return responseCode;
    }

    public void setResponseCode(String responseCode) {
        this.responseCode = responseCode;
    }

    public String getDescriptions() {
        return descriptions;
    }

    public void setDescriptions(String descriptions) {
        this.descriptions = descriptions;
    }

    @SerializedName("locationlist")
    public List<Locationlist> getLocationlist() {
        return locationlist;
    }

    public void setLocationlist(List<Locationlist> locationlist) {
        this.locationlist = locationlist;
    }

    public String getBookingID() {
        return bookingID;
    }

    public void setBookingID(String bookingID) {
        this.bookingID = bookingID;
    }

    public String getBookingSubMessage() {
        return bookingSubMessage;
    }

    public void setBookingSubMessage(String bookingSubMessage) {
        this.bookingSubMessage = bookingSubMessage;
    }

    public String getBookingMessage() {
        return bookingMessage;
    }

    public void setBookingMessage(String bookingMessage) {
        this.bookingMessage = bookingMessage;
    }

    public String getBookingMessageImage() {
        return bookingMessageImage;
    }

    public void setBookingMessageImage(String bookingMessageImage) {
        this.bookingMessageImage = bookingMessageImage;
    }

    public List<Locationlist> locationlist;
    @SerializedName(value = "BookingID",alternate = "MeetingID")
    public String bookingID;
    @SerializedName(value = "BookingSubMessage",alternate = "MeetingSubMessage")
    public String bookingSubMessage;
    @SerializedName(value = "BookingMessage",alternate = "MeetingMessage")
    public String bookingMessage;
    @SerializedName(value = "BookingMessageImage",alternate = "MeetingMessageImage")
    public String bookingMessageImage;
    @SerializedName(value = "MeetingWaitingMsgCount",alternate = "BookingWaitingMsgCount")
    public String meetingWaitingMsgCount;



    public String getMeetingWaitingMsgCount() {
        return meetingWaitingMsgCount;
    }

    public void setMeetingWaitingMsgCount(String meetingWaitingMsgCount) {
        this.meetingWaitingMsgCount = meetingWaitingMsgCount;
    }




    @Keep
    public class Locationlist{
        @SerializedName("UserID")
        public String userID;
        @SerializedName("ShowroomID")
        public String showroomID;

        public String getUserID() {
            return userID;
        }

        public void setUserID(String userID) {
            this.userID = userID;
        }

        public String getShowroomID() {
            return showroomID;
        }

        public void setShowroomID(String showroomID) {
            this.showroomID = showroomID;
        }

        public String getLatitude() {
            return latitude;
        }

        public void setLatitude(String latitude) {
            this.latitude = latitude;
        }

        public String getLongitude() {
            return longitude;
        }

        public void setLongitude(String longitude) {
            this.longitude = longitude;
        }

        public String getMapIcon() {
            return mapIcon;
        }

        public void setMapIcon(String mapIcon) {
            this.mapIcon = mapIcon;
        }

        @SerializedName("Latitude")
        public String latitude;
        @SerializedName("Longitude")
        public String longitude;
        @SerializedName("MapIcon")
        public String mapIcon;
    }

}
