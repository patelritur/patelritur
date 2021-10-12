package com.demo.home.model;

import androidx.annotation.Keep;

import java.util.List;

@Keep
public class MenuResponse {
    public String ResponseCode;
    public String Descriptions;
    public List<Demomenu> demomenu;

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

    public List<Demomenu> getDemomenu() {
        return demomenu;
    }

    public void setDemomenu(List<Demomenu> demomenu) {
        this.demomenu = demomenu;
    }


}
