package com.demo.home.model;

import androidx.annotation.Keep;

import java.util.List;

@Keep
public class CarDealerResponseModel {
    public String ResponseCode;
    public String Descriptions;

    public String getResponseCode() {
        return ResponseCode;
    }

    public void setResponseCode(String responseCode) {
        ResponseCode = responseCode;
    }

    public String getDescriptions() {
        return Descriptions;
    }

    public void setDescriptions(String descriptions) {
        Descriptions = descriptions;
    }

    public List<Demospecialist> getDemospecialist() {
        return demospecialist;
    }

    public void setDemospecialist(List<Demospecialist> demospecialist) {
        this.demospecialist = demospecialist;
    }

    public List<Demospecialist> demospecialist;

    @Keep
    public class Demospecialist{
        public String SpecialistName;
        public String SpecialistProfileImg;
        public String SpecialistCompanyLogo;
        public String SpecialistCompanyName;
        public String SpecialistRateing;
        public String TotalBooking;


        public String getSpecialistName() {
            return SpecialistName;
        }

        public void setSpecialistName(String specialistName) {
            SpecialistName = specialistName;
        }

        public String getSpecialistProfileImg() {
            return SpecialistProfileImg;
        }

        public void setSpecialistProfileImg(String specialistProfileImg) {
            SpecialistProfileImg = specialistProfileImg;
        }

        public String getSpecialistCompanyLogo() {
            return SpecialistCompanyLogo;
        }

        public void setSpecialistCompanyLogo(String specialistCompanyLogo) {
            SpecialistCompanyLogo = specialistCompanyLogo;
        }

        public String getSpecialistCompanyName() {
            return SpecialistCompanyName;
        }

        public void setSpecialistCompanyName(String specialistCompanyName) {
            SpecialistCompanyName = specialistCompanyName;
        }

        public String getSpecialistRateing() {
            return SpecialistRateing;
        }

        public void setSpecialistRateing(String specialistRateing) {
            SpecialistRateing = specialistRateing;
        }

        public String getTotalBooking() {
            return TotalBooking;
        }

        public void setTotalBooking(String totalBooking) {
            TotalBooking = totalBooking;
        }



    }

}
