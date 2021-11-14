package com.demo.rewards.s.model;

import androidx.annotation.Keep;

import com.google.gson.annotations.SerializedName;

import java.io.Serializable;
import java.util.List;

@Keep
public class RewardsResponseModel implements Serializable {

    @Keep
    public class Offerrewardslist implements Serializable{
        @SerializedName("ThumbBanner")
        public String thumbBanner;
        @SerializedName("PopUpBanner")
        public String popUpBanner;
        @SerializedName("BannerType")
        public String bannerType;

        public String getThumbBanner() {
            return thumbBanner;
        }

        public void setThumbBanner(String thumbBanner) {
            this.thumbBanner = thumbBanner;
        }

        public String getPopUpBanner() {
            return popUpBanner;
        }

        public void setPopUpBanner(String popUpBanner) {
            this.popUpBanner = popUpBanner;
        }

        public String getBannerType() {
            return bannerType;
        }

        public void setBannerType(String bannerType) {
            this.bannerType = bannerType;
        }

        public String getBannerURL() {
            return bannerURL;
        }

        public void setBannerURL(String bannerURL) {
            this.bannerURL = bannerURL;
        }

        @SerializedName("BannerURL")
        public String bannerURL;
    }

        @SerializedName("ResponseCode")
        public String responseCode;
        @SerializedName("Descriptions")
        public String descriptions;

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

    public List<Offerrewardslist> getOfferrewardslist() {
        return offerrewardslist;
    }

    public void setOfferrewardslist(List<Offerrewardslist> offerrewardslist) {
        this.offerrewardslist = offerrewardslist;
    }

    public List<Offerrewardslist> offerrewardslist;


}
