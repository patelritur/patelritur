package com.demo.home.model;


import androidx.annotation.Keep;

@Keep
public class DrivingLicenseDataResponse {

    public String ResponseCode;
    public String Descriptions;
    public String DLDocument;
    public String DLNumber;

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

    public String getDLDocument() {
        return DLDocument;
    }

    public void setDLDocument(String DLDocument) {
        this.DLDocument = DLDocument;
    }

    public String getDLNumber() {
        return DLNumber;
    }

    public void setDLNumber(String DLNumber) {
        this.DLNumber = DLNumber;
    }
}
