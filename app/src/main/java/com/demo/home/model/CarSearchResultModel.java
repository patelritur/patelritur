package com.demo.home.model;

import androidx.annotation.Keep;
import androidx.databinding.BaseObservable;
import androidx.databinding.Bindable;

import com.demo.BR;

import java.util.List;

@Keep
public class CarSearchResultModel extends BaseObservable {

    public String ResponseCode;
    public String Descriptions;

    @Bindable
    public String getResponseCode() {
        return ResponseCode;
    }

    public void setResponseCode(String responseCode) {
        ResponseCode = responseCode;
        notifyPropertyChanged(BR.responseCode);

    }

    @Bindable
    public String getDescriptions() {
        return Descriptions;
    }

    public void setDescriptions(String descriptions) {
        Descriptions = descriptions;
        notifyPropertyChanged(BR.descriptions);
    }

    @Bindable
    public String getCarListTitle() {
        return CarListTitle;
    }

    public void setCarListTitle(String carListTitle) {
        CarListTitle = carListTitle;
        notifyPropertyChanged(BR.carListTitle);
    }

    @Bindable
    public List<Carlist> getCarlist() {
        return carlist;
    }

    public void setCarlist(List<Carlist> carlist) {
        this.carlist = carlist;
        notifyPropertyChanged(BR.carlist);
    }

    public String CarListTitle;
    public List<Carlist> carlist;

    @Keep
    public class Carlist extends BaseObservable{
        public String CarName;
        public String Brand;

        public String getCarModel() {
            return CarModel;
        }

        public void setCarModel(String carModel) {
            CarModel = carModel;
        }

        public String CarModel;

        public String getDemoCount() {
            return DemoCount;
        }

        public void setDemoCount(String demoCount) {
            DemoCount = demoCount;
        }

        public String DemoCount;

        @Bindable
        public String getCarName() {
            return CarName;
        }

        public void setCarName(String carName) {
            CarName = carName;
            notifyPropertyChanged(BR.carName);
        }

        @Bindable
        public String getBrand() {
            return Brand;
        }

        public void setBrand(String brand) {
            Brand = brand;
            notifyPropertyChanged(BR.brand);
        }

        @Bindable
        public String getCarRateing() {
            return CarRateing;
        }

        public void setCarRateing(String carRateing) {
            CarRateing = carRateing;
            notifyPropertyChanged(BR.carRateing);
        }

        @Bindable
        public String getCarPrice() {
            return CarPrice;
        }

        public void setCarPrice(String carPrice) {
            CarPrice = carPrice;
            notifyPropertyChanged(BR.carPrice);
        }

        @Bindable
        public String getNewLaunch() {
            return NewLaunch;
        }

        public void setNewLaunch(String newLaunch) {
            NewLaunch = newLaunch;
            notifyPropertyChanged(BR.newLaunch);
        }

        @Bindable
        public String getCarImage() {
            return CarImage;
        }

        public void setCarImage(String carImage) {
            CarImage = carImage;
            notifyPropertyChanged(BR.carImage);
        }

        @Bindable
        public String getFuelType() {
            return FuelType;
        }

        public void setFuelType(String fuelType) {
            FuelType = fuelType;
            notifyPropertyChanged(BR.fuelType);
        }

        public String CarRateing;
        public String CarPrice;
        public String NewLaunch;
        public String CarImage;
        public String FuelType;

        public String getCarID() {
            return CarID;
        }

        public void setCarID(String carID) {
            CarID = carID;
        }

        public String CarID;
    }


}
