package com.demo.home.model;

import androidx.annotation.Keep;

import java.util.List;

@Keep
public class CarFilterResponse {
    public String ResponseCode;

    public String getResponseCode() {
        return ResponseCode;
    }

    public void setResponseCode(String responseCode) {
        this.ResponseCode = responseCode;
    }

    public String getDescriptions() {
        return Descriptions;
    }

    public void setDescriptions(String Descriptions) {
        this.Descriptions = Descriptions;
    }

    public List<pricefilter> getPricefilter() {
        return pricefilter;
    }

    public void setPricefilter(List<pricefilter> pricefilter) {
        this.pricefilter = pricefilter;
    }


    @Keep
    public class filter{
        public String FilterID;

        public String getFilterID() {
            return FilterID;
        }

        public void setFilterID(String filterID) {
            this.FilterID = filterID;
        }

        public String getFilterName() {
            return FilterName;
        }

        public void setFilterName(String FilterName) {
            this.FilterName = FilterName;
        }

        public String getFilterIcon() {
            return FilterIcon;
        }

        public void setFilterIcon(String filterIcon) {
            this.FilterIcon = filterIcon;
        }

        public String FilterName;
        public String FilterIcon;
    }


    public String Descriptions;
    public List<pricefilter> pricefilter;

    public List<filter> getFilter() {
        return filter;
    }

    public void setFilter(List<filter> filter) {
        this.filter = filter;
    }

    public List<filter> filter;
    @Keep
    public class pricefilter{
        public String FilterName;

        public String getFilterName() {
            return FilterName;
        }

        public void setFilterName(String filterName) {
            this.FilterName = filterName;
        }

        public String getFilterMinValue() {
            return FilterValue;
        }

        public void setFilterMinValue(String filterMinValue) {
            this.FilterValue = filterMinValue;
        }



        public String FilterValue;
    }

}
