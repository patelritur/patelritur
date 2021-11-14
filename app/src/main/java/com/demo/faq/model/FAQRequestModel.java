package com.demo.faq.model;

import androidx.annotation.Keep;

@Keep
public class FAQRequestModel {
    public String getLanguageID() {
        return LanguageID;
    }

    public void setLanguageID(String languageID) {
        LanguageID = languageID;
    }

    public String getSearchValue() {
        return SearchValue;
    }

    public void setSearchValue(String searchValue) {
        SearchValue = searchValue;
    }

    public String getUserType() {
        return UserType;
    }

    public void setUserType(String userType) {
        UserType = userType;
    }

    private String LanguageID;
    private String SearchValue;
    private String UserType;
    private String UserID;

    public String getUserID() {
        return UserID;
    }

    public void setUserID(String userID) {
        UserID = userID;
    }
}
