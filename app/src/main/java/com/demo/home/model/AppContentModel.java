package com.demo.home.model;


import androidx.annotation.Keep;

import com.google.gson.annotations.SerializedName;

import java.util.List;

@Keep
public class AppContentModel {
    @SerializedName("ResponseCode")
    public String ResponseCode;

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

    public String Descriptions;

    public List<Label> getLabels() {
        return Labels;
    }

    public void setLabels(List<Label> labels) {
        Labels = labels;
    }

    public List<Label> Labels;

    public class Label {
        public String Page;

        public String getPage() {
            return Page;
        }

        public void setPage(String page) {
            Page = page;
        }

        public String getLabelName() {
            return LabelName;
        }

        public void setLabelName(String labelName) {
            LabelName = labelName;
        }

        public String getLabelInLanguage() {
            return LabelInLanguage;
        }

        public void setLabelInLanguage(String labelInLanguage) {
            LabelInLanguage = labelInLanguage;
        }

        public String getLabelImage() {
            return LabelImage;
        }

        public void setLabelImage(String labelImage) {
            LabelImage = labelImage;
        }

        public String LabelName;
        public String LabelInLanguage;
        public String LabelImage;
    }
}
