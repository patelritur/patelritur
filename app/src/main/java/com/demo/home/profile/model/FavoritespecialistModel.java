package com.demo.home.profile.model;

import androidx.annotation.Keep;

import com.google.gson.annotations.SerializedName;

import java.util.List;

@Keep
public class FavoritespecialistModel {
    // import com.fasterxml.jackson.databind.ObjectMapper; // version 2.11.1
// import com.fasterxml.jackson.annotation.SerializedName; // version 2.11.1
/* ObjectMapper om = new ObjectMapper();
Root root = om.readValue(myJsonString), Root.class); */
    @Keep
    public class Favoritespecialist{
        @SerializedName("SpecialistID")
        public String specialistID;
        @SerializedName("SpecialistName")
        public String specialistName;
        @SerializedName("SpecialistMobile")
        public String specialistMobile;
        @SerializedName("SpecialistImage")
        public String specialistImage;

        public String getSpecialistID() {
            return specialistID;
        }

        public void setSpecialistID(String specialistID) {
            this.specialistID = specialistID;
        }

        public String getSpecialistName() {
            return specialistName;
        }

        public void setSpecialistName(String specialistName) {
            this.specialistName = specialistName;
        }

        public String getSpecialistMobile() {
            return specialistMobile;
        }

        public void setSpecialistMobile(String specialistMobile) {
            this.specialistMobile = specialistMobile;
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

        public String getSpecialistDemoMsg() {
            return specialistDemoMsg;
        }

        public void setSpecialistDemoMsg(String specialistDemoMsg) {
            this.specialistDemoMsg = specialistDemoMsg;
        }

        @SerializedName("SpecialistRating")
        public String specialistRating;
        @SerializedName("SpecialistTotalDemo")
        public String specialistTotalDemo;
        @SerializedName("CompanyName")
        public String companyName;
        @SerializedName("SpecialistDemoMsg")
        public String specialistDemoMsg;
    }

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

    public List<Favoritespecialist> getFavoritespecialist() {
        return favoritespecialist;
    }

    public void setFavoritespecialist(List<Favoritespecialist> favoritespecialist) {
        this.favoritespecialist = favoritespecialist;
    }

    @SerializedName("ResponseCode")
        public String responseCode;
        @SerializedName("Descriptions")
        public String descriptions;
        public List<Favoritespecialist> favoritespecialist;


}
