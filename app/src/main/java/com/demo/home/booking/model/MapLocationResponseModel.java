package com.demo.home.booking.model;

import androidx.annotation.Keep;

import com.google.gson.annotations.SerializedName;

import java.util.List;

@Keep
public class MapLocationResponseModel {

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

    public List<Maplocationlist> getMaplocationlist() {
        return maplocationlist;
    }

    public void setMaplocationlist(List<Maplocationlist> maplocationlist) {
        this.maplocationlist = maplocationlist;
    }

    @SerializedName("Descriptions")
    public String descriptions;
    @SerializedName("maplocationlist")
    public List<Maplocationlist> maplocationlist;

    @Keep
    public class Maplocationlist{
        @SerializedName("LocationID")
        public String locationID;
        @SerializedName("Latitude")
        public String latitude;
        @SerializedName("Longitude")
        public String longitude;

        public String getLocationID() {
            return locationID;
        }

        public void setLocationID(String locationID) {
            this.locationID = locationID;
        }

        public String getLatitude() {
            return latitude;
        }

        public void setLatitude(String latitude) {
            this.latitude = latitude;
        }

        public String getLongitude() {
            return longitude;
        }

        public void setLongitude(String longitude) {
            this.longitude = longitude;
        }

        public String getLocationType() {
            return locationType;
        }

        public void setLocationType(String locationType) {
            this.locationType = locationType;
        }

        public String getMapIcon() {
            return mapIcon;
        }

        public void setMapIcon(String mapIcon) {
            this.mapIcon = mapIcon;
        }

        @SerializedName("LocationType")
        public String locationType;
        @SerializedName("MapIcon")
        public String mapIcon;
    }
}
