package com.demo.carDetails.model;

import androidx.annotation.Keep;

import com.demo.utils.PrintLog;
import com.demo.utils.Utils;
import com.google.gson.annotations.SerializedName;

import java.text.DecimalFormat;

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
        public String getFinalPrice() {
            String convertedFinalPrice =new DecimalFormat("0.00").format((Float.parseFloat(onRoadPrice))/100000);
            return convertedFinalPrice; //one hundred and sixty-five

        }

        public void setFinalPrice(String finalPrice) {
            this.finalPrice = finalPrice;
        }

        public String finalPrice;
        @SerializedName("CarName")
        public String carName;
        @SerializedName("CarModel")
        public String carModel;
        @SerializedName("Brand")
        public String brand;

        @SerializedName("PriceStateName")
        public String PriceStateName;

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

        @SerializedName("PriceEffectiveDate")
        public String PriceEffectiveDate;



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
            return String.valueOf(Math.round(Float.parseFloat(exShowroomPrice)));
        }

        public void setExShowroomPrice(String exShowroomPrice) {
            this.exShowroomPrice = exShowroomPrice;
        }

        public String getCashDiscount() {
            return String.valueOf(Math.round(Float.parseFloat(cashDiscount)));
        }

        public void setCashDiscount(String cashDiscount) {
            this.cashDiscount = cashDiscount;
        }

        public String getExchangeBonus() {
            return String.valueOf(Math.round(Float.parseFloat(exchangeBonus)));
        }

        public void setExchangeBonus(String exchangeBonus) {
            this.exchangeBonus = exchangeBonus;
        }

        public String getCorporateGovEmpOffer() {
            return String.valueOf(Math.round(Float.parseFloat(corporateGovEmpOffer)));
        }

        public void setCorporateGovEmpOffer(String corporateGovEmpOffer) {
            this.corporateGovEmpOffer = corporateGovEmpOffer;
        }

        public String getInsurance() {
            return String.valueOf(Math.round(Float.parseFloat(insurance)));
        }

        public void setInsurance(String insurance) {
            this.insurance = insurance;
        }

        public String getExtendedWarranty() {
            return String.valueOf(Math.round(Float.parseFloat(extendedWarranty)));
        }

        public void setExtendedWarranty(String extendedWarranty) {
            this.extendedWarranty = extendedWarranty;
        }



        public String getBasicKit() {
            return String.valueOf(Math.round(Float.parseFloat(basicKit)));
        }

        public void setBasicKit(String basicKit) {
            this.basicKit = basicKit;
        }

        public String getMcdParkingCharges() {
            return String.valueOf(Math.round(Float.parseFloat(mcdParkingCharges)));
        }

        public void setmCDParkingCharges(String mCDParkingCharges) {
            this.mcdParkingCharges = mCDParkingCharges;
        }

        public String getFastag() {
            return String.valueOf(Math.round(Float.parseFloat(fastag)));
        }

        public void setFastag(String fastag) {
            this.fastag = fastag;
        }

        public String getTotalBenefits() {
            return String.valueOf(Math.round(Float.parseFloat(totalBenefits)));
        }

        public void setTotalBenefits(String totalBenefits) {
            this.totalBenefits = totalBenefits;
        }

        public String getOnRoadPrice() {
            return String.format("%.2f", Float.parseFloat(onRoadPrice)).toString();
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

        public String getRto() {
            return String.valueOf(Math.round(Float.parseFloat(rto)));
        }

        public void setRto(String rto) {
            this.rto = rto;
        }

        @SerializedName("RTO")
        public String rto;
        @SerializedName("BasicKit")
        public String basicKit;


        @SerializedName("MCDParkingCharges")
        public String mcdParkingCharges;
        @SerializedName("Fastag")
        public String fastag;
        @SerializedName("TotalBenefits")
        public String totalBenefits;
        @SerializedName("OnRoadPrice")
        public String onRoadPrice;
        @SerializedName("DownloadInvoiceURL")
        public String downloadInvoiceURL;

        public String getDisclaimer() {
            return Disclaimer;
        }

        public void setDisclaimer(String disclaimer) {
            Disclaimer = disclaimer;
        }

        @SerializedName("Disclaimer")
        public String Disclaimer;


    }
}
