package com.demo.home.profile.model;

import androidx.annotation.Keep;

import com.google.gson.annotations.SerializedName;

import java.util.List;

@Keep
public class DemoTripResponseModel {

    @SerializedName("ResponseCode")
    public String responseCode;
    @SerializedName("Descriptions")
    public String descriptions;
    public List<Demotrip> demotrips;

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

    public List<Demotrip> getDemotrips() {
        return demotrips;
    }

    public void setDemotrips(List<Demotrip> demotrips) {
        this.demotrips = demotrips;
    }

    public String getTotalDemoCount() {
        return totalDemoCount;
    }

    public void setTotalDemoCount(String totalDemoCount) {
        this.totalDemoCount = totalDemoCount;
    }

    @SerializedName("TotalDemoCount")
    public String totalDemoCount;

    @Keep
    public class Demotrip{
        @SerializedName("DemoID")
        public String demoID;
        @SerializedName("CarName")
        public String carName;
        @SerializedName("CarImage")
        public String carImage;
        @SerializedName("FuelType")
        public String fuelType;
        @SerializedName("FaceliftDesc")
        public String faceliftDesc;
        @SerializedName("DemoDate")
        public String demoDate;
        @SerializedName("SpecialistName")
        public String specialistName;
        @SerializedName("SpecialistImage")
        public String specialistImage;
        @SerializedName("SpecialistRating")
        public String specialistRating;
        @SerializedName("SpecialistTotalDemo")
        public String specialistTotalDemo;
        @SerializedName("CompanyName")
        public String companyName;
        @SerializedName("CompanyLogo")
        public String companyLogo;

        public String getDemoID() {
            return demoID;
        }

        public void setDemoID(String demoID) {
            this.demoID = demoID;
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

        public String getFuelType() {
            return fuelType;
        }

        public void setFuelType(String fuelType) {
            this.fuelType = fuelType;
        }

        public String getFaceliftDesc() {
            return faceliftDesc;
        }

        public void setFaceliftDesc(String faceliftDesc) {
            this.faceliftDesc = faceliftDesc;
        }

        public String getDemoDate() {
            return demoDate;
        }

        public void setDemoDate(String demoDate) {
            this.demoDate = demoDate;
        }

        public String getSpecialistName() {
            return specialistName;
        }

        public void setSpecialistName(String specialistName) {
            this.specialistName = specialistName;
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

        public String getSpecialistTotalDemo() {
            return specialistTotalDemo;
        }

        public void setSpecialistTotalDemo(String specialistTotalDemo) {
            this.specialistTotalDemo = specialistTotalDemo;
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
            return specialistLatitude;
        }

        public void setSpecialistLatitude(String specialistLatitude) {
            this.specialistLatitude = specialistLatitude;
        }

        public String getSpecialistLongitude() {
            return specialistLongitude;
        }

        public void setSpecialistLongitude(String specialistLongitude) {
            this.specialistLongitude = specialistLongitude;
        }

        public String getDemoStatus() {
            return demoStatus;
        }

        public void setDemoStatus(String demoStatus) {
            this.demoStatus = demoStatus;
        }

        public String getDemoType() {
            return demoType;
        }

        public void setDemoType(String demoType) {
            this.demoType = demoType;
        }

        @SerializedName("UserLatitude")
        public String userLatitude;
        @SerializedName("UserLongitude")
        public String userLongitude;
        @SerializedName("SpecialistLatitude")
        public String specialistLatitude;
        @SerializedName("SpecialistLongitude")
        public String specialistLongitude;
        @SerializedName("DemoStatus")
        public String demoStatus;
        @SerializedName("DemoType")
        public String demoType;

        public String getBookingType() {
            return BookingType;
        }

        public void setBookingType(String bookingType) {
            BookingType = bookingType;
        }

        @SerializedName("BookingType")
        public String BookingType;
    }
    
}
