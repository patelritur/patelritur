package com.demo.home.model;

import androidx.annotation.Keep;

@Keep
public class CarSearchRequestModel {
    public String UserID = "";
    public String Latitude = "";
    public String Longitude = "";
    public String SpecialistID="0";

    public String getSpecialistID() {
        return SpecialistID;
    }

    public void setSpecialistID(String specialistID) {
        SpecialistID = specialistID;
    }

    public String getLatitude() {
        return Latitude;
    }

    public String getUserID() {
        return UserID;
    }

    public void setUserID(String userID) {
        UserID = userID;
    }

    public String getLongitude() {
        return Longitude;
    }

    public void setLongitude(String longitude) {
        Longitude = longitude;
    }

    public String getCarSectionType() {
        return CarSectionType;
    }

    public void setCarSectionType(String carSectionType) {
        CarSectionType = carSectionType;
    }

    public String getPriceFilter() {
        return PriceFilter;
    }

    public void setPriceFilter(String priceFilter) {
        PriceFilter = priceFilter;
    }

    public String getBrandFilter() {
        return BrandFilter;
    }

    public void setBrandFilter(String brandFilter) {
        BrandFilter = brandFilter;
    }

    public String getSegmentFilter() {
        return SegmentFilter;
    }

    public void setSegmentFilter(String segmentFilter) {
        SegmentFilter = segmentFilter;
    }

    public String getFuelType() {
        return FuelType;
    }

    public void setFuelType(String fuelType) {
        FuelType = fuelType;
    }

    public String getVehicleType() {
        return VehicleType;
    }

    public void setVehicleType(String vehicleType) {
        VehicleType = vehicleType;
    }

    public String getSearchValue() {
        return SearchValue;
    }

    public void setSearchValue(String searchValue) {
        SearchValue = searchValue;
    }

    public void setLatitude(String latitude) {
        Latitude = latitude;
    }

    public String CarSectionType="0";
    public String PriceFilter = "";
    public String BrandFilter = "";
    public String SegmentFilter = "";
    public String FuelType = "";
    public String VehicleType="2";
    public String SearchValue = "";

}

