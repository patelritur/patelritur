package com.demo.news;

import androidx.annotation.Keep;

import com.google.gson.annotations.SerializedName;

import java.io.Serializable;
import java.util.List;

@Keep
public class NewsResponseModel  implements Serializable {
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

    public List<Newslist> getNewslist() {
        return newslist;
    }

    public void setNewslist(List<Newslist> newslist) {
        this.newslist = newslist;
    }

    @SerializedName("Descriptions")
    public String descriptions;
    public List<Newslist> newslist;
    @Keep
    public class Newslist implements Serializable{
        @SerializedName("NewsTitle")
        public String newsTitle;
        @SerializedName("NewsShortDesc")
        public String newsShortDesc;
        @SerializedName("NewsDesc")
        public String newsDesc;

        public String getNewsTitle() {
            return newsTitle;
        }

        public void setNewsTitle(String newsTitle) {
            this.newsTitle = newsTitle;
        }

        public String getNewsShortDesc() {
            return newsShortDesc;
        }

        public void setNewsShortDesc(String newsShortDesc) {
            this.newsShortDesc = newsShortDesc;
        }

        public String getNewsDesc() {
            return newsDesc;
        }

        public void setNewsDesc(String newsDesc) {
            this.newsDesc = newsDesc;
        }

        public String getNewsImage() {
            return newsImage;
        }

        public void setNewsImage(String newsImage) {
            this.newsImage = newsImage;
        }

        public String getNewsPostDate() {
            return newsPostDate;
        }

        public void setNewsPostDate(String newsPostDate) {
            this.newsPostDate = newsPostDate;
        }

        @SerializedName("NewsImage")
        public String newsImage;
        @SerializedName("NewsPostDate")
        public String newsPostDate;
    }

  

}
