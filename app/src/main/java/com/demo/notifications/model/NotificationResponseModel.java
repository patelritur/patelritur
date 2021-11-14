package com.demo.notifications.model;

import android.view.View;

import androidx.annotation.Keep;

import com.demo.utils.ClickHandlers;
import com.google.gson.annotations.SerializedName;

import java.util.List;

@Keep
public class NotificationResponseModel {
    @SerializedName("ResponseCode")
    public String responseCode;
    @SerializedName("Descriptions")
    public String descriptions;

    public String image;
    public ClickHandlers clickHandlers;

    public void onClick(View view)
    {
        clickHandlers.onClick(view);
    }


    public ClickHandlers getClickHandlers() {
        return clickHandlers;
    }

    public void setClickHandlers(ClickHandlers clickHandlers) {
        this.clickHandlers = clickHandlers;
    }

    public String getImage() {
        return image;
    }

    public void setImage(String image) {
        this.image = image;
    }

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

    public List<Notification> getNotification() {
        return notification;
    }

    public void setNotification(List<Notification> notification) {
        this.notification = notification;
    }

    public int getNotificationCount() {
        return Integer.parseInt(notificationCount);
    }

    public void setNotificationCount(String notificationCount) {
        this.notificationCount = notificationCount;
    }

    @SerializedName("Notification")
    public List<Notification> notification;
    @SerializedName("NotificationCount")
    public String notificationCount;

    @Keep
    public class Notification{
        @SerializedName("ID")
        public String iD;
        @SerializedName("Notification")
        public String notification;
        @SerializedName("NotificationSummary")
        public String notificationSummary;
        @SerializedName("NotificationDateTime")
        public String notificationDateTime;

        public String getiD() {
            return iD;
        }

        public void setiD(String iD) {
            this.iD = iD;
        }

        public String getNotification() {
            return notification;
        }

        public void setNotification(String notification) {
            this.notification = notification;
        }

        public String getNotificationSummary() {
            return notificationSummary;
        }

        public void setNotificationSummary(String notificationSummary) {
            this.notificationSummary = notificationSummary;
        }

        public String getNotificationDateTime() {
            return notificationDateTime;
        }

        public void setNotificationDateTime(String notificationDateTime) {
            this.notificationDateTime = notificationDateTime;
        }

        public String getNotificationUrlID() {
            return notificationUrlID;
        }

        public void setNotificationUrlID(String notificationUrlID) {
            this.notificationUrlID = notificationUrlID;
        }

        @SerializedName("NotificationUrlID")
        public String notificationUrlID;
    }
    
}
