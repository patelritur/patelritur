package com.demo.launch.model;

import androidx.annotation.Keep;

import com.google.gson.annotations.SerializedName;

import java.io.Serializable;
import java.util.List;

@Keep
public class LaunchResponseModel {

    @SerializedName("ResponseCode")
    public String responseCode;

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

    public List<Latestlaunchlist> getLatestlaunchlist() {
        return latestlaunchlist;
    }

    public void setLatestlaunchlist(List<Latestlaunchlist> latestlaunchlist) {
        this.latestlaunchlist = latestlaunchlist;
    }

    @SerializedName("Descriptions")
    public String descriptions;
    public List<Latestlaunchlist> latestlaunchlist;

    @Keep
    public class Latestlaunchlist{
        @SerializedName("CarID")
        public String carID;

        public String getCarID() {
            return carID;
        }

        public void setCarID(String carID) {
            this.carID = carID;
        }

        public String getCarName() {
            return carName;
        }

        public void setCarName(String carName) {
            this.carName = carName;
        }

        public String getLaunchDate() {
            return launchDate;
        }

        public void setLaunchDate(String launchDate) {
            this.launchDate = launchDate;
        }

        public String getCarDescription() {
            return carDescription;
        }

        public void setCarDescription(String carDescription) {
            this.carDescription = carDescription;
        }

        public String getBookingMsg() {
            return bookingMsg;
        }

        public void setBookingMsg(String bookingMsg) {
            this.bookingMsg = bookingMsg;
        }

        public String getBookingBtnStatus() {
            return bookingBtnStatus;
        }

        public void setBookingBtnStatus(String bookingBtnStatus) {
            this.bookingBtnStatus = bookingBtnStatus;
        }

        public String getBookingStatus() {
            return bookingStatus;
        }

        public void setBookingStatus(String bookingStatus) {
            this.bookingStatus = bookingStatus;
        }

        public String getPreBookingStartDate() {
            return preBookingStartDate;
        }

        public void setPreBookingStartDate(String preBookingStartDate) {
            this.preBookingStartDate = preBookingStartDate;
        }

        public String getBookingDate() {
            return bookingDate;
        }

        public void setBookingDate(String bookingDate) {
            this.bookingDate = bookingDate;
        }

        public String getBookingTime() {
            return bookingTime;
        }

        public void setBookingTime(String bookingTime) {
            this.bookingTime = bookingTime;
        }

        public String getBookingType() {
            return bookingType;
        }

        public void setBookingType(String bookingType) {
            this.bookingType = bookingType;
        }

        public List<Bannerlist> getBannerlist() {
            return bannerlist;
        }

        public void setBannerlist(List<Bannerlist> bannerlist) {
            this.bannerlist = bannerlist;
        }

        @SerializedName("CarName")
        public String carName;
        @SerializedName("LaunchDate")
        public String launchDate;
        @SerializedName("CarDescription")
        public String carDescription;
        @SerializedName("BookingMsg")
        public String bookingMsg;
        @SerializedName("BookingBtnStatus")
        public String bookingBtnStatus;
        @SerializedName("BookingStatus")
        public String bookingStatus;
        @SerializedName("PreBookingStartDate")
        public String preBookingStartDate;
        @SerializedName("BookingDate")
        public String bookingDate;
        @SerializedName("BookingTime")
        public String bookingTime;
        @SerializedName("BookingType")
        public String bookingType;
        public List<Bannerlist> bannerlist;
    }

    @Keep
    public class Bannerlist implements Serializable {
        @SerializedName("Banner")
        public String banner;

        public String getBanner() {
            return banner;
        }

        public void setBanner(String banner) {
            this.banner = banner;
        }

        public String getBannerType() {
            return bannerType;
        }

        public void setBannerType(String bannerType) {
            this.bannerType = bannerType;
        }

        @SerializedName("BannerType")
        public String bannerType;
    }


}
