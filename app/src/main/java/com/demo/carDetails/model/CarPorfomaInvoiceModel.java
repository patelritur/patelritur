package com.demo.carDetails.model;

import androidx.annotation.Keep;

import com.google.gson.annotations.SerializedName;

@Keep
public class CarPorfomaInvoiceModel
{

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

    public Porfomainvoice getPorfomainvoice() {
        return porfomainvoice;
    }

    public void setPorfomainvoice(Porfomainvoice porfomainvoice) {
        this.porfomainvoice = porfomainvoice;
    }

    @SerializedName("Descriptions")
    public String descriptions;
    @SerializedName("porfomainvoice")
    public Porfomainvoice porfomainvoice;

    @Keep
    public class Porfomainvoice{
        @SerializedName("CarName")
        public String carName;
        @SerializedName("CarModel")
        public String carModel;
        @SerializedName("Brand")
        public String brand;

        public String getCarName() {
            return carName;
        }

        public void setCarName(String carName) {
            this.carName = carName;
        }

        public String getCarModel() {
            return carModel;
        }

        public void setCarModel(String carModel) {
            this.carModel = carModel;
        }

        public String getBrand() {
            return brand;
        }

        public void setBrand(String brand) {
            this.brand = brand;
        }

        public String getFuelType() {
            return fuelType;
        }

        public void setFuelType(String fuelType) {
            this.fuelType = fuelType;
        }

        public String getCarImage() {
            return carImage;
        }

        public void setCarImage(String carImage) {
            this.carImage = carImage;
        }

        public String getExShowroomPrice() {
            return exShowroomPrice;
        }

        public void setExShowroomPrice(String exShowroomPrice) {
            this.exShowroomPrice = exShowroomPrice;
        }

        public String getCashDiscount() {
            return cashDiscount;
        }

        public void setCashDiscount(String cashDiscount) {
            this.cashDiscount = cashDiscount;
        }

        public String getExchangeBonus() {
            return exchangeBonus;
        }

        public void setExchangeBonus(String exchangeBonus) {
            this.exchangeBonus = exchangeBonus;
        }

        public String getCorporateGovEmpOffer() {
            return corporateGovEmpOffer;
        }

        public void setCorporateGovEmpOffer(String corporateGovEmpOffer) {
            this.corporateGovEmpOffer = corporateGovEmpOffer;
        }

        public String getInsurance() {
            return insurance;
        }

        public void setInsurance(String insurance) {
            this.insurance = insurance;
        }

        public String getExtendedWarranty() {
            return extendedWarranty;
        }

        public void setExtendedWarranty(String extendedWarranty) {
            this.extendedWarranty = extendedWarranty;
        }

        public String getrTO() {
            return rTO;
        }

        public void setrTO(String rTO) {
            this.rTO = rTO;
        }

        public String getBasicKit() {
            return basicKit;
        }

        public void setBasicKit(String basicKit) {
            this.basicKit = basicKit;
        }

        public String getmCDParkingCharges() {
            return mCDParkingCharges;
        }

        public void setmCDParkingCharges(String mCDParkingCharges) {
            this.mCDParkingCharges = mCDParkingCharges;
        }

        public String getFastag() {
            return fastag;
        }

        public void setFastag(String fastag) {
            this.fastag = fastag;
        }

        public String getTotalBenefits() {
            return totalBenefits;
        }

        public void setTotalBenefits(String totalBenefits) {
            this.totalBenefits = totalBenefits;
        }

        public String getOnRoadPrice() {
            return onRoadPrice;
        }

        public void setOnRoadPrice(String onRoadPrice) {
            this.onRoadPrice = onRoadPrice;
        }

        public String getDownloadInvoiceURL() {
            return downloadInvoiceURL;
        }

        public void setDownloadInvoiceURL(String downloadInvoiceURL) {
            this.downloadInvoiceURL = downloadInvoiceURL;
        }

        @SerializedName("FuelType")
        public String fuelType;
        @SerializedName("CarImage")
        public String carImage;
        @SerializedName("ExShowroomPrice")
        public String exShowroomPrice;
        @SerializedName("CashDiscount")
        public String cashDiscount;
        @SerializedName("ExchangeBonus")
        public String exchangeBonus;
        @SerializedName("CorporateGovEmpOffer")
        public String corporateGovEmpOffer;
        @SerializedName("Insurance")
        public String insurance;
        @SerializedName("ExtendedWarranty")
        public String extendedWarranty;
        @SerializedName("RTO")
        public String rTO;
        @SerializedName("BasicKit")
        public String basicKit;
        @SerializedName("MCDParkingCharges")
        public String mCDParkingCharges;
        @SerializedName("Fastag")
        public String fastag;
        @SerializedName("TotalBenefits")
        public String totalBenefits;
        @SerializedName("OnRoadPrice")
        public String onRoadPrice;
        @SerializedName("DownloadInvoiceURL")
        public String downloadInvoiceURL;
    }
}
