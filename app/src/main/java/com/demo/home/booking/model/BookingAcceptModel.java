package com.demo.home.booking.model;

import androidx.annotation.Keep;

import com.google.gson.annotations.SerializedName;

@Keep
public class BookingAcceptModel {

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

    public String getBookingID() {
        return bookingID;
    }

    public void setBookingID(String bookingID) {
        this.bookingID = bookingID;
    }

    public String getBookingMessage() {
        return bookingMessage;
    }

    public void setBookingMessage(String bookingMessage) {
        this.bookingMessage = bookingMessage;
    }

    public Bookingdetails getBookingdetails() {
        return bookingdetails;
    }

    public void setBookingdetails(Bookingdetails bookingdetails) {
        this.bookingdetails = bookingdetails;
    }





    @SerializedName("ResponseCode")
    public String responseCode;
    @SerializedName("Descriptions")
    public String descriptions;
    @SerializedName(value = "BookingID",alternate = "MeetingID")
    public String bookingID;
    @SerializedName(value = "BookingMessage",alternate = "MeetingMessage")
    public String bookingMessage;
    @SerializedName(value = "bookingdetails", alternate = "meetingdetails")
    public Bookingdetails bookingdetails;

    @Keep
    public class Bookingdetails{
        @SerializedName("UserID")
        public String userID;
        @SerializedName("SpecialistID")
        public String specialistID;
        @SerializedName("CarName")
        public String carName;

        public String getUserID() {
            return userID;
        }

        public void setUserID(String userID) {
            this.userID = userID;
        }

        public String getSpecialistID() {
            return specialistID;
        }

        public void setSpecialistID(String specialistID) {
            this.specialistID = specialistID;
        }

        public String getCarName() {
            return carName;
        }

        public void setCarName(String carName) {
            this.carName = carName;
        }

        public String getCarImage() {
            return carImage;
        }

        public void setCarImage(String carImage) {
            this.carImage = carImage;
        }

        public String getFuleType() {
            return fuleType;
        }

        public void setFuleType(String fuleType) {
            this.fuleType = fuleType;
        }

        public String getFaceliftDesc() {
            return faceliftDesc;
        }

        public void setFaceliftDesc(String faceliftDesc) {
            this.faceliftDesc = faceliftDesc;
        }

        public String getSpecialistName() {
            return specialistName;
        }

        public void setSpecialistName(String specialistName) {
            this.specialistName = specialistName;
        }

        public String getCompanyName() {
            return companyName;
        }

        public void setCompanyName(String companyName) {
            this.companyName = companyName;
        }

        public String getCompanyLogo() {
            return companyLogo;
        }

        public void setCompanyLogo(String companyLogo) {
            this.companyLogo = companyLogo;
        }

        public String getSpecialistImage() {
            return specialistImage;
        }

        public void setSpecialistImage(String specialistImage) {
            this.specialistImage = specialistImage;
        }

        public String getSpecialistRating() {
            return specialistRating;
        }

        public void setSpecialistRating(String specialistRating) {
            this.specialistRating = specialistRating;
        }

        public String getSpecialistTotalBooking() {
            return specialistTotalBooking;
        }

        public void setSpecialistTotalBooking(String specialistTotalBooking) {
            this.specialistTotalBooking = specialistTotalBooking;
        }

        public String getVoiceMessageStatus() {
            return voiceMessageStatus;
        }

        public void setVoiceMessageStatus(String voiceMessageStatus) {
            this.voiceMessageStatus = voiceMessageStatus;
        }

        public String getCancelBtnStatus() {
            return cancelBtnStatus;
        }

        public void setCancelBtnStatus(String cancelBtnStatus) {
            this.cancelBtnStatus = cancelBtnStatus;
        }

        public String getCancellationMsg() {
            return cancellationMsg;
        }

        public void setCancellationMsg(String cancellationMsg) {
            this.cancellationMsg = cancellationMsg;
        }

        public String getUserLatitude() {
            return userLatitude;
        }

        public void setUserLatitude(String userLatitude) {
            this.userLatitude = userLatitude;
        }

        public String getUserLongitude() {
            return userLongitude;
        }

        public void setUserLongitude(String userLongitude) {
            this.userLongitude = userLongitude;
        }

        public String getSpecialistLatitude() {
            return specialistLatitude.trim().length()>0? specialistLatitude:"0.0";
        }

        public void setSpecialistLatitude(String specialistLatitude) {
            this.specialistLatitude = specialistLatitude;
        }

        public String getSpecialistLongitude() {
            return specialistLongitude.trim().length()>0? specialistLongitude:"0.0";
        }

        public void setSpecialistLongitude(String specialistLongitude) {
            this.specialistLongitude = specialistLongitude;
        }

        public String getCarSanitisedStatus() {
            return carSanitisedStatus;
        }

        public void setCarSanitisedStatus(String carSanitisedStatus) {
            this.carSanitisedStatus = carSanitisedStatus;
        }

        public String getSpecialistTemperature() {
            return specialistTemperature;
        }

        public void setSpecialistTemperature(String specialistTemperature) {
            this.specialistTemperature = specialistTemperature;
        }

        @SerializedName("CarImage")
        public String carImage;
        @SerializedName("FuelType")
        public String fuleType;
        @SerializedName("FaceliftDesc")
        public String faceliftDesc;
        @SerializedName("SpecialistName")
        public String specialistName;
        @SerializedName("CompanyName")
        public String companyName;
        @SerializedName("CompanyLogo")
        public String companyLogo;
        @SerializedName("SpecialistImage")
        public String specialistImage;
        @SerializedName("SpecialistRating")
        public String specialistRating;
        @SerializedName(value = "SpecialistTotalBooking",alternate = "SpecialistTotalMeeting")
        public String specialistTotalBooking;
        @SerializedName("VoiceMessageStatus")
        public String voiceMessageStatus;
        @SerializedName("CancelBtnStatus")
        public String cancelBtnStatus;
        @SerializedName("CancellationMsg")
        public String cancellationMsg;
        @SerializedName("UserLatitude")
        public String userLatitude;
        @SerializedName("UserLongitude")
        public String userLongitude;
        @SerializedName("SpecialistLatitude")
        public String specialistLatitude="0.0";
        @SerializedName("SpecialistLongitude")
        public String specialistLongitude="0.0";
        @SerializedName("CarSanitisedStatus")
        public String carSanitisedStatus;
        @SerializedName("SpecialistTemperature")
        public String specialistTemperature;

        @SerializedName("SpecialistMobile")
        public String SpecialistMobile;

        @SerializedName("CompanyAddress")
        public String CompanyAddress;


        @SerializedName("VirtualMeetType")
        public String VirtualMeetType;

        public String getSpecialistMobile() {
            return SpecialistMobile;
        }

        public void setSpecialistMobile(String specialistMobile) {
            SpecialistMobile = specialistMobile;
        }

        public String getCompanyAddress() {
            return CompanyAddress;
        }

        public void setCompanyAddress(String companyAddress) {
            CompanyAddress = companyAddress;
        }



        public String getVirtualMeetType() {
            return VirtualMeetType;
        }

        public void setVirtualMeetType(String virtualMeetType) {
            VirtualMeetType = virtualMeetType;
        }

        public String getVirtualMeetStatus() {
            return VirtualMeetStatus;
        }

        public void setVirtualMeetStatus(String virtualMeetStatus) {
            VirtualMeetStatus = virtualMeetStatus;
        }

        public String getMeetStatusID() {
            return MeetStatusID;
        }

        public void setMeetStatusID(String meetStatusID) {
            MeetStatusID = meetStatusID;
        }

        @SerializedName("VirtualMeetStatus")
        public String VirtualMeetStatus;
        @SerializedName("MeetStatusID")
        public String MeetStatusID;

    }

    
}
