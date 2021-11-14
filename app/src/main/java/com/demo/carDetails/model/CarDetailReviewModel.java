package com.demo.carDetails.model;

import androidx.annotation.Keep;

import java.util.List;

@Keep
public class CarDetailReviewModel {

    @Keep
    public class Carreview{
        public String UserName;
        public String ProfileImage;
        public String ReviewDesc;

        public String getUserName() {
            return UserName;
        }

        public void setUserName(String userName) {
            UserName = userName;
        }

        public String getProfileImage() {
            return ProfileImage;
        }

        public void setProfileImage(String profileImage) {
            ProfileImage = profileImage;
        }

        public String getReviewDesc() {
            return ReviewDesc;
        }

        public void setReviewDesc(String reviewDesc) {
            ReviewDesc = reviewDesc;
        }
    }
    public String ResponseCode;

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

    public List<Carreview> getCarreview() {
        return carreview;
    }

    public void setCarreview(List<Carreview> carreview) {
        this.carreview = carreview;
    }

    public String Descriptions;
    public List<Carreview> carreview;

}
