package com.demo.faq.model;

import androidx.annotation.Keep;

import com.google.gson.annotations.SerializedName;

import java.util.List;

@Keep
public class FAQResponseModel {

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

    public List<Faq> getFaq() {
        return faq;
    }

    public void setFaq(List<Faq> faq) {
        this.faq = faq;
    }

    @SerializedName("Descriptions")
    public String descriptions;
    @SerializedName("faq")
    public List<Faq> faq;


    @Keep
    public class Faq{
        @SerializedName("Question")
        public String question;
        @SerializedName("Answer")
        public String answer;

        public String getQuestion() {
            return question;
        }

        public void setQuestion(String question) {
            this.question = question;
        }

        public String getAnswer() {
            return answer;
        }

        public void setAnswer(String answer) {
            this.answer = answer;
        }
    }


}
