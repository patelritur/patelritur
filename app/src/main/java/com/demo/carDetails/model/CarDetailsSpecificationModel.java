package com.demo.carDetails.model;

import androidx.annotation.Keep;

import com.google.gson.annotations.SerializedName;

import java.util.List;

@Keep
public class CarDetailsSpecificationModel {
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

    public List<Carspecification> getCarspecification() {
        return carspecification;
    }

    public void setCarspecification(List<Carspecification> carspecification) {
        this.carspecification = carspecification;
    }

    @SerializedName("Descriptions")
    public String descriptions;
    @SerializedName("carspecification")
    public List<Carspecification> carspecification;

    @Keep
    public class Carspecification{
        public String SpecificationImage;
        public String SpecificationTitle;
        public String SpecificationValue;

        public String getSpecificationImage() {
            return SpecificationImage;
        }

        public void setSpecificationImage(String specificationImage) {
            SpecificationImage = specificationImage;
        }

        public String getSpecificationTitle() {
            return SpecificationTitle;
        }

        public void setSpecificationTitle(String specificationTitle) {
            SpecificationTitle = specificationTitle;
        }

        public String getSpecificationValue() {
            return SpecificationValue;
        }

        public void setSpecificationValue(String specificationValue) {
            SpecificationValue = specificationValue;
        }
    }

}
