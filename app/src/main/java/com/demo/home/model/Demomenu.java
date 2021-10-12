package com.demo.home.model;

import androidx.annotation.Keep;
import androidx.databinding.Bindable;

import com.demo.utils.ClickHandlers;

@Keep
public class Demomenu {

    public String MenuID;

    public String getMenuID() {
        return MenuID;
    }

    public void setMenuID(String menuID) {
        MenuID = menuID;
    }

    public String getMenuName() {
        return MenuName;
    }

    public void setMenuName(String menuName) {
        MenuName = menuName;
    }

    public String getMenuIcon() {
        return MenuIcon;
    }

    public void setMenuIcon(String menuIcon) {
        MenuIcon = menuIcon;
    }

    public String MenuName;
    public String MenuIcon;





}
