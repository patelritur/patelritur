package com.demo.home.model;


import androidx.annotation.Keep;

import com.google.gson.annotations.SerializedName;

import java.io.Serializable;
import java.util.List;

@Keep
public class AppContentModel implements Serializable {
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

    @SerializedName("Descriptions")
    public String Descriptions;

    public List<Label> getLabels() {
        return Labels;
    }

    public void setLabels(List<Label> labels) {
        Labels = labels;
    }

    @SerializedName("Labels")
    public List<Label> Labels;

    @Keep
    public class Label implements  Serializable
    {
        @SerializedName("Page")
        public String Page;

        public boolean isChecked() {
            return isChecked;
        }

        public void setChecked(boolean checked) {
            isChecked = checked;
        }

        public boolean isChecked;

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

        @SerializedName("LabelName")
        public String LabelName;
        @SerializedName("LabelInLanguage")
        public String LabelInLanguage;
        @SerializedName("LabelImage")
        public String LabelImage;
    }
}
