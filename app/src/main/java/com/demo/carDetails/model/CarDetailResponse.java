package com.demo.carDetails.model;

import android.view.View;

import androidx.annotation.Keep;

import com.demo.carDetails.CarDetailsActivity;
import com.demo.utils.ClickHandlers;
import com.demo.utils.Utils;
import com.google.gson.annotations.SerializedName;

import java.io.Serializable;
import java.text.DecimalFormat;
import java.util.ArrayList;
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
    public class Carbanner implements Serializable {
        public String BannerImage;
        public String BannerUrl;

        public String getBannerType() {
            return BannerType;
        }

        public void setBannerType(String bannerType) {
            BannerType = bannerType;
        }

        public String BannerType;

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
    public class colorlist{
        String Color;
        String HexColorCode;
        String ColorImage;

        public String getColor() {
            return Color;
        }

        public void setColor(String color) {
            Color = color;
        }

        public String getHexColorCode() {
            return HexColorCode;
        }

        public void setHexColorCode(String hexColorCode) {
            HexColorCode = hexColorCode;
        }

        public String getColorImage() {
            return ColorImage;
        }

        public void setColorImage(String colorImage) {
            ColorImage = colorImage;
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

        public String getBannerType() {
            return BannerType;
        }

        public void setBannerType(String bannerType) {
            BannerType = bannerType;
        }

        public String BannerType;


        public String getBannerYear() {
            return BannerYear;
        }

        public void setBannerYear(String bannerYear) {
            BannerYear = bannerYear;
        }

        public String BannerYear;

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
    public class Featurelist{
        @SerializedName("FeatureText")
        public String featureText;
        @SerializedName("FeatureIcon")
        public String featureIcon;

        public String getFeatureText() {
            return featureText;
        }

        public void setFeatureText(String featureText) {
            this.featureText = featureText;
        }

        public String getFeatureIcon() {
            return featureIcon;
        }

        public void setFeatureIcon(String featureIcon) {
            this.featureIcon = featureIcon;
        }
    }
    @Keep
    public class Cardetail{
        public String CarID;
        public String CarName;

        public String PriceStateName;
        public String PriceEffectiveDate;
        public String CarFeatureTitle;
        public String getPriceStateName() {
            return PriceStateName;
        }

        public void setPriceStateName(String priceStateName) {
            PriceStateName = priceStateName;
        }

        public String getPriceEffectiveDate() {
            return Utils.DateFormater( PriceEffectiveDate.split(" ")[0]);

        }

        public void setPriceEffectiveDate(String priceEffectiveDate) {
            PriceEffectiveDate = priceEffectiveDate;
        }

        public String getStateName() {
            return StateName;
        }

        public void setStateName(String stateName) {
            StateName = stateName;
        }

        public String StateName;

        public String getCarDescription() {
            return CarDescription;
        }

        public void setCarDescription(String carDescription) {
            CarDescription = carDescription;
        }

        public String CarDescription;
        public String CarModel;
        public String Brand;
        public String CarRateing;
        public String CarPrice;
        public String CarFeatures;

        public String getReviewCount() {
            return ReviewCount;
        }

        public void setReviewCount(String reviewCount) {
            ReviewCount = reviewCount;
        }

        public String CarColors;
        public String ReviewCount;

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
            String convertedFinalPrice =new DecimalFormat("0.00").format((Float.parseFloat(CarPrice))/100000);
            return convertedFinalPrice; //one hundred and sixty-five

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
        public String CarOfferBannerTitle;
        public String CarAwardBannerTitle;

        public String getCarOfferBannerTitle() {
            return CarOfferBannerTitle;
        }

        public void setCarOfferBannerTitle(String carOfferBannerTitle) {
            CarOfferBannerTitle = carOfferBannerTitle;
        }

        public String getCarAwardBannerTitle() {
            return CarAwardBannerTitle;
        }

        public void setCarAwardBannerTitle(String carAwardBannerTitle) {
            CarAwardBannerTitle = carAwardBannerTitle;
        }

        public List<CarDetailResponse.colorlist> getColorlist() {
            return colorlist;
        }

        public void setColorlist(List<CarDetailResponse.colorlist> colorlist) {
            this.colorlist = colorlist;
        }

        public List<colorlist> colorlist;


        public List<Carawardbanner> carawardbanner;

        public String getCarFeatureTitle() {
            return CarFeatureTitle;
        }

        public void setCarFeatureTitle(String carFeatureTitle) {
            CarFeatureTitle = carFeatureTitle;
        }

        public ArrayList<Featurelist> getFeaturelist() {
            return featurelist;
        }

        public void setFeaturelist(ArrayList<Featurelist> featurelist) {
            this.featurelist = featurelist;
        }

        public ArrayList<Featurelist> featurelist;
    }

}
