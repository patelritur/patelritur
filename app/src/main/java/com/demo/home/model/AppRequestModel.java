package com.demo.home.model;

import androidx.annotation.Keep;

import com.demo.utils.Constants;

@Keep
public class AppRequestModel {
   /* {
        "LanguageID":"1",
            "Page":"AppStartInfo",
            "UserType":"1"
    }*/

    public String getLanguageID() {
        return LanguageID;
    }

    public void setLanguageID(String languageID) {
        LanguageID = languageID;
    }

    public String getPage() {
        return Page;
    }

    public void setPage(String page) {
        Page = page;
    }

    public String getUserType() {
        return UserType;
    }

    public void setUserType(String userType) {
        UserType = userType;
    }

    String LanguageID="1",Page,UserType = Constants.USER_TYPE;
}
