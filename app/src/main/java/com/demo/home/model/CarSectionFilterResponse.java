package com.demo.home.model;

import androidx.annotation.Keep;

import com.google.gson.annotations.SerializedName;

import java.io.Serializable;
import java.util.List;

@Keep
public class CarSectionFilterResponse implements Serializable {

    @Keep
    public class Demomenu implements Serializable{
        @SerializedName("MenuID")
        public String menuID;

        public String getMenuID() {
            return menuID;
        }

        public void setMenuID(String menuID) {
            this.menuID = menuID;
        }

        public String getMenuName() {
            return menuName;
        }

        public void setMenuName(String menuName) {
            this.menuName = menuName;
        }

        public String getMenuIcon() {
            return menuIcon;
        }

        public void setMenuIcon(String menuIcon) {
            this.menuIcon = menuIcon;
        }

        @SerializedName("MenuName")
        public String menuName;
        @SerializedName("MenuIcon")
        public String menuIcon;
    }

        @SerializedName("ResponseCode")
        public String responseCode;
        @SerializedName("Descriptions")
        public String descriptions;
        public List<Demomenu> demomenu;

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

    public List<Demomenu> getDemomenu() {
        return demomenu;
    }

    public void setDemomenu(List<Demomenu> demomenu) {
        this.demomenu = demomenu;
    }
}
