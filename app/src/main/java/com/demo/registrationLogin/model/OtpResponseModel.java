package com.demo.registrationLogin.model;


public class OtpResponseModel {
    //            android:onTextChanged="@{watcher.onTextChanged}"
    public class ExistUsersInfo{
        public String FirstName;
        public String LastName;
        public String Email;
        public String CompanyName;
        public String State;

        public long optUserID() {
            return UserID;
        }

        public String getFirstName() {
            return FirstName;
        }

        public void setFirstName(String firstName) {
            FirstName = firstName;
        }

        public String getLastName() {
            return LastName;
        }

        public void setLastName(String lastName) {
            LastName = lastName;
        }

        public String getEmail() {
            return Email;
        }

        public void setEmail(String email) {
            Email = email;
        }

        public String getCompanyName() {
            return CompanyName;
        }

        public void setCompanyName(String companyName) {
            CompanyName = companyName;
        }

        public String getState() {
            return State;
        }

        public void setState(String state) {
            State = state;
        }

        public long getUserID() {
            return UserID;
        }

        public String getDistrict() {
            return District;
        }

        public void setDistrict(String district) {
            District = district;
        }

        public void setUserID(long userID) {
            UserID = userID;
        }

        long UserID;



        public String District;
    }


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

    public OtpResponseModel.ExistUsersInfo getExistUsersInfo() {
        return ExistUsersInfo;
    }

    public void setExistUsersInfo(OtpResponseModel.ExistUsersInfo existUsersInfo) {
        ExistUsersInfo = existUsersInfo;
    }

    public String getShowUserInformation() {
        return ShowUserInformation;
    }

    public void setShowUserInformation(String showUserInformation) {
        ShowUserInformation = showUserInformation;
    }

    public ExistUsersInfo ExistUsersInfo;
    public String ShowUserInformation;
}
