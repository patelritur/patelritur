package com.demo.carDetails.model;

import android.view.View;

import androidx.annotation.Keep;

import com.demo.carDetails.CarDetailsActivity;
import com.demo.utils.ClickHandlers;

import java.util.List;

@Keep
public class CarDetailResponse {
    public String ResponseCode;
    public String Descriptions;
    public void onClick(View view)
    {
        clickHandlers.onClick(view);
    }

    public ClickHandlers getClickHandlers() {
        return clickHandlers;
    }

    public void setClickHandlers(ClickHandlers clickHandlers) {
        this.clickHandlers = clickHandlers;
    }

    private ClickHandlers clickHandlers;
    public CarDetailResponse(CarDetailsActivity carDetailsActivity) {
        clickHandlers = (ClickHandlers) carDetailsActivity;
    }

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

    public Cardetail getCardetail() {
        return cardetail;
    }

    public void setCardetail(Cardetail cardetail) {
        this.cardetail = cardetail;
    }

    public Cardetail cardetail;

    @Keep
    public class Carbanner{
        public String BannerImage;
        public String BannerUrl;

        public String getBannerImage() {
            return BannerImage;
        }

        public void setBannerImage(String bannerImage) {
            BannerImage = bannerImage;
        }

        public String getBannerUrl() {
            return BannerUrl;
        }

        public void setBannerUrl(String bannerUrl) {
            BannerUrl = bannerUrl;
        }
    }

    @Keep
    public class Carofferbanner{
        public String BannerImage;
        public String BannerUrl;

        public String getBannerImage() {
            return BannerImage;
        }

        public void setBannerImage(String bannerImage) {
            BannerImage = bannerImage;
        }

        public String getBannerUrl() {
            return BannerUrl;
        }

        public void setBannerUrl(String bannerUrl) {
            BannerUrl = bannerUrl;
        }
    }

    @Keep
    public class Carawardbanner{
        public String BannerImage;
        public String BannerUrl;

        public String getBannerImage() {
            return BannerImage;
        }

        public void setBannerImage(String bannerImage) {
            BannerImage = bannerImage;
        }

        public String getBannerUrl() {
            return BannerUrl;
        }

        public void setBannerUrl(String bannerUrl) {
            BannerUrl = bannerUrl;
        }
    }

    @Keep
    public class Cardetail{
        public String CarID;
        public String CarName;
        public String CarModel;
        public String Brand;
        public String CarRateing;
        public String CarPrice;
        public String CarFeatures;
        public String CarColors;

        public String getCarID() {
            return CarID;
        }

        public void setCarID(String carID) {
            CarID = carID;
        }

        public String getCarName() {
            return CarName;
        }

        public void setCarName(String carName) {
            CarName = carName;
        }

        public String getCarModel() {
            return CarModel;
        }

        public void setCarModel(String carModel) {
            CarModel = carModel;
        }

        public String getBrand() {
            return Brand;
        }

        public void setBrand(String brand) {
            Brand = brand;
        }

        public String getCarRateing() {
            return CarRateing;
        }

        public void setCarRateing(String carRateing) {
            CarRateing = carRateing;
        }

        public String getCarPrice() {
            return CarPrice;
        }

        public void setCarPrice(String carPrice) {
            CarPrice = carPrice;
        }

        public String getCarFeatures() {
            return CarFeatures;
        }

        public void setCarFeatures(String carFeatures) {
            CarFeatures = carFeatures;
        }

        public String getCarColors() {
            return CarColors;
        }

        public void setCarColors(String carColors) {
            CarColors = carColors;
        }

        public String getDemoCount() {
            return DemoCount;
        }

        public void setDemoCount(String demoCount) {
            DemoCount = demoCount;
        }

        public List<Carbanner> getCarbanner() {
            return carbanner;
        }

        public void setCarbanner(List<Carbanner> carbanner) {
            this.carbanner = carbanner;
        }

        public List<Carofferbanner> getCarofferbanner() {
            return carofferbanner;
        }

        public void setCarofferbanner(List<Carofferbanner> carofferbanner) {
            this.carofferbanner = carofferbanner;
        }

        public List<Carawardbanner> getCarawardbanner() {
            return carawardbanner;
        }

        public void setCarawardbanner(List<Carawardbanner> carawardbanner) {
            this.carawardbanner = carawardbanner;
        }

        public String DemoCount;
        public List<Carbanner> carbanner;
        public List<Carofferbanner> carofferbanner;
        public List<Carawardbanner> carawardbanner;
    }

}
