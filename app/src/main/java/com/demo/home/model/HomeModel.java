package com.demo.home.model;

import android.content.Context;
import android.view.View;

import androidx.annotation.Keep;
import androidx.appcompat.widget.AppCompatImageView;
import androidx.databinding.BaseObservable;
import androidx.databinding.Bindable;

import com.bumptech.glide.Glide;
import com.demo.binding.BindingAdapter;
import com.demo.utils.ClickHandlers;
import com.google.android.gms.common.annotation.KeepForSdk;
import com.google.android.gms.common.annotation.KeepName;

@Keep
@KeepName
public class HomeModel  {
    public String fName,lName;;
    public String image;
    public String descriptions;
    private String dayOfWeek;

    public String getTemp_c() {
        return temp_c;
    }



    public void setTemp_c(String temp_c) {
        this.temp_c = temp_c;
    }

    public String temp_c;

    public String getDescriptions() {
        return descriptions;
    }

    public void setDescriptions(String descriptions) {
        this.descriptions = descriptions;
    }

    public String getImage() {
        return image;
    }

    public void setImage(String image) {
        this.image = image;
    }

    public String homeMenuFirstName;
    private String bottomMenuFirstName;
    private String bottomMenuSecondName;

    public String getBottomMenuSecondName() {
        return bottomMenuSecondName;
    }

    public void setBottomMenuSecondName(String bottomMenuSecondName) {
        this.bottomMenuSecondName = bottomMenuSecondName;
    }

    public String getBottomMenuThirdName() {
        return bottomMenuThirdName;
    }

    public void setBottomMenuThirdName(String bottomMenuThirdName) {
        this.bottomMenuThirdName = bottomMenuThirdName;
    }

    public String getBottomMenuFirstImage() {
        return bottomMenuFirstImage;
    }

    public void setBottomMenuFirstImage(String bottomMenuFirstImage) {
        this.bottomMenuFirstImage = bottomMenuFirstImage;
    }

    public String getBottomMenuSecondImage() {
        return bottomMenuSecondImage;
    }

    public void setBottomMenuSecondImage(String bottomMenuSecondImage) {
        this.bottomMenuSecondImage = bottomMenuSecondImage;
    }

    public String getBottomMenuThirdImage() {
        return bottomMenuThirdImage;
    }

    public void setBottomMenuThirdImage(String bottomMenuThirdImage) {
        this.bottomMenuThirdImage = bottomMenuThirdImage;
    }

    private String bottomMenuThirdName;
    private String bottomMenuFirstImage;
    private String bottomMenuSecondImage;
    private String bottomMenuThirdImage;


    public String getHomeMenuFirstName() {
        return homeMenuFirstName;
    }

    public void setHomeMenuFirstName(String homeMenuFirstName) {
        this.homeMenuFirstName = homeMenuFirstName;
    }

    public String getHomeMenuSecondName() {
        return homeMenuSecondName;
    }

    public void setHomeMenuSecondName(String homeMenuSecondName) {
        this.homeMenuSecondName = homeMenuSecondName;
    }

    public String getHomeMenuFirstImage() {
        return homeMenuFirstImage;
    }

    public void setHomeMenuFirstImage(String homeMenuFirstImage) {
        this.homeMenuFirstImage = homeMenuFirstImage;
    }

    public String getHomeMenuSecondImage() {
        return homeMenuSecondImage;
    }

    public void setHomeMenuSecondImage(String homeMenuSecondImage) {
        this.homeMenuSecondImage = homeMenuSecondImage;
    }

    public ClickHandlers getClickHandlers() {
        return clickHandlers;
    }

    public void setClickHandlers(ClickHandlers clickHandlers) {
        this.clickHandlers = clickHandlers;
    }

    public String homeMenuSecondName;
    public String homeMenuFirstImage,homeMenuSecondImage;


    private ClickHandlers clickHandlers;

    public HomeModel(Context context) {
        clickHandlers = (ClickHandlers) context;
    }

    public String getfName() {
       return fName.substring(0, 1).toUpperCase() + fName.substring(1).toLowerCase();

    }

    public void setfName(String fName) {
        this.fName = fName;
    }

    public String getlName() {
        return lName.substring(0, 1).toUpperCase() + lName.substring(1).toLowerCase();

    }

    public void setlName(String lName) {
        this.lName = lName;
    }

    public String getGreetingMessage() {
        return greetingMessage;
    }

    public void setGreetingMessage(String greetingMessage) {
        this.greetingMessage = greetingMessage;
    }

    String greetingMessage;



    public void onClick(View view)
    {
        clickHandlers.onClick(view);
    }


    public void setBottomMenuFirstName(String bottomMenuFirstName) {
        this.bottomMenuFirstName = bottomMenuFirstName;
    }

    public String getBottomMenuFirstName() {
        return bottomMenuFirstName;
    }


    public void setDayOfWeek(String dayOfWeek) {
        this.dayOfWeek = dayOfWeek;
    }

    public String getDayOfWeek() {
        return dayOfWeek;
    }
}
