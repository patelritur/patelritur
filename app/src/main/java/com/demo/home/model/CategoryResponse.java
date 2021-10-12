package com.demo.home.model;

import androidx.annotation.Keep;

import java.util.List;

@Keep
public class CategoryResponse {

    public String ResponseCode;
    public String Descriptions;

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

    public List<CategoryResponse.productcategory> getProductcategory() {
        return productcategory;
    }

    public void setProductcategory(List<CategoryResponse.productcategory> productcategory) {
        this.productcategory = productcategory;
    }

    public List<productcategory> productcategory;


    @Keep
    public class productcategory{
        public String CategoryID;
        public String Category;

        public String getCategoryID() {
            return CategoryID;
        }

        public void setCategoryID(String categoryID) {
            CategoryID = categoryID;
        }

        public String getCategory() {
            return Category;
        }

        public void setCategory(String category) {
            Category = category;
        }
    }

}
