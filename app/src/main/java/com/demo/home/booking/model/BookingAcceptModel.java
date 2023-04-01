package com.demo.home.booking.model;

import android.text.format.DateUtils;

import androidx.annotation.Keep;

import com.google.gson.annotations.SerializedName;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;

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
        StringBuilder sb = new StringBuilder(bookingMessage);
        if(bookingMessage!=null) {
            sb.setCharAt(0, Character.toUpperCase(sb.charAt(0)));
            return sb.toString();
        }
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

    public String getBookingOTP() {
        return bookingOTP;
    }

    public void setBookingOTP(String bookingOTP) {
        this.bookingOTP = bookingOTP;
    }

    @SerializedName(value = "BookingOTP",alternate = "MeetingOTP")
    public String bookingOTP;
    public String getBookingTime() {
        return BookingTime;
    }

    public void setBookingTime(String bookingTime) {
        BookingTime = bookingTime;
    }

    @SerializedName(value = "BookingTime",alternate = "MeetingTime")
    public String BookingTime;

    public String getFormattedBookingDate() {
        return BookingDate!=null?getBookingDateFormat():null;
    }

    private String getBookingDateFormat() {
        SimpleDateFormat sdf = new SimpleDateFormat("MMM d, yyyy");
        try {
            Date strDate= sdf.parse(BookingDate);
            Date dCurrentDate = sdf.parse(sdf.format(new Date()));
            if ( DateUtils.isToday(strDate.getTime() - DateUtils.DAY_IN_MILLIS)) {
                return "Tomorrow";
            }
            else if(dCurrentDate.compareTo(strDate)==0)
                return "Today";

        } catch (ParseException e) {
            e.printStackTrace();
        }

        return BookingDate;
    }

    public String getBookingDate() {
        return BookingDate;
    }

    public void setBookingDate(String bookingDate) {
        BookingDate = bookingDate;
    }

    @SerializedName(value = "BookingDate",alternate = "MeetingDate")
    public String BookingDate;




    public String getBookingSubMessage() {
        StringBuilder sb = new StringBuilder(bookingSubMessage);
        if(bookingSubMessage!=null && bookingSubMessage.toString().trim().length()>0) {
            sb.setCharAt(0, Character.toUpperCase(sb.charAt(0)));
            return sb.toString();
        }
        return bookingSubMessage;
    }

    public void setBookingSubMessage(String bookingSubMessage) {
        this.bookingSubMessage = bookingSubMessage;
    }

    @SerializedName(value = "BookingSubMessage",alternate = "MeetingSubMessage")
    public String bookingSubMessage;
    @SerializedName(value = "bookingdetails", alternate = "meetingdetails")
    public Bookingdetails bookingdetails;

    @Keep
    public class Bookingdetails{
        public String getCustomerAddress() {
            return CustomerAddress;
        }

        public void setCustomerAddress(String customerAddress) {
            CustomerAddress = customerAddress;
        }

        @SerializedName("CustomerAddress")
        public String CustomerAddress;

        public String getCarID() {
            return CarID;
        }

        public void setCarID(String carID) {
            CarID = carID;
        }

        public String CarID;

        public String getBookingOTP() {
            return BookingOTP;
        }

        public void setBookingOTP(String bookingOTP) {
            BookingOTP = bookingOTP;
        }

        public String BookingOTP;

        public String getVoiceRecordingAudio() {
            return VoiceRecordingAudio;
        }

        public void setVoiceRecordingAudio(String voiceRecordingAudio) {
            VoiceRecordingAudio = voiceRecordingAudio;
        }

        public String VoiceRecordingAudio;

        public ArrayList<CarCheckList> getCarchecklist() {
            return carchecklist;
        }

        public void setCarchecklist(ArrayList<CarCheckList> carchecklist) {
            this.carchecklist = carchecklist;
        }

        public ArrayList<CarCheckList> carchecklist;
        public String specialistFirstName(){

            return specialistName.split(" ")[0]+"'s";
        }

        public boolean isShowTemp(){

            return carchecklist.size()>0;
        }
        @SerializedName("UserID")
        public String userID;
        @SerializedName("SpecialistID")
        public String specialistID;
        @SerializedName("CarName")
        public String carName;

        public String getMeetingType() {
            return MeetingType;
        }

        public void setMeetingType(String meetingType) {
            MeetingType = meetingType;
        }

        @SerializedName("MeetingType")
        public String MeetingType;

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
            StringBuilder sb = new StringBuilder(specialistName);
            if(specialistName!=null && specialistName.trim().length()>0) {
                sb.setCharAt(0, Character.toUpperCase(sb.charAt(0)));
                return sb.toString();
            }
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

        public String getDemoType() {
            return DemoType;
        }

        public void setDemoType(String demoType) {
            DemoType = demoType;
        }

        @SerializedName(value ="DemoType",alternate = "MeetBookingType")
        public String DemoType;


        @SerializedName(value = "SpecialistTotalBooking",alternate = "SpecialistTotalMeeting")
        public String specialistTotalBooking;

        public String getDemoStatusId() {
            return demoStatusId;
        }

        public void setDemoStatusId(String demoStatusId) {
            this.demoStatusId = demoStatusId;
        }

        @SerializedName(value = "DemoStatusID",alternate = "MeetStatusID")
        public String demoStatusId;

        public String getCustomDemoStatusId() {
            return customDemoStatusId;
        }

        public void setCustomDemoStatusId(String customDemoStatusId) {
            this.customDemoStatusId = customDemoStatusId;
        }

        public String customDemoStatusId;
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
/*
        public String getMeetStatusID() {
            return MeetStatusID;
        }

        public void setMeetStatusID(String meetStatusID) {
            MeetStatusID = meetStatusID;
        }*/

        @SerializedName("VirtualMeetStatus")
        public String VirtualMeetStatus;
        @Keep
        public class CarCheckList {
            @SerializedName("CheckListValue")
            public String CheckListValue;
            @SerializedName("CheckListIcon")
            public String  CheckListIcon;

            public String getCheckListValue() {
                return CheckListValue;
            }

            public void setCheckListValue(String checkListValue) {
                CheckListValue = checkListValue;
            }

            public String getCheckListIcon() {
                return CheckListIcon;
            }

            public void setCheckListIcon(String checkListIcon) {
                CheckListIcon = checkListIcon;
            }
        }
        /*@SerializedName("MeetStatusID")
        public String MeetStatusID;*/

    }


}
