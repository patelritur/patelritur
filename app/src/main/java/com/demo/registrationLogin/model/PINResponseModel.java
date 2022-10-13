package com.demo.registrationLogin.model;


import androidx.annotation.Keep;

@Keep
public class PINResponseModel {

        public String ResponseCode;
        public String Descriptions;
        public String SessionID;

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

    public String getSessionID() {
        return SessionID;
    }

    public void setSessionID(String sessionID) {
        SessionID = sessionID;
    }

    public PINResponseModel.UsersInfo getUsersInfo() {
        return UsersInfo;
    }

    public void setUsersInfo(PINResponseModel.UsersInfo usersInfo) {
        UsersInfo = usersInfo;
    }

        public UsersInfo UsersInfo;

    @Keep
    public class UsersInfo{
        public long UserID;
        public String Type;
        public String UserTypeID;
        public String FirstName;
        public String LastName;
        public String Email;
        public String ShowroomName;
        public String CompanyName;
        public String ShowroomID;
        public String EmployeeID;
        public String DOB;

        public String getIsDLUploadStatus() {
            return IsDLUploadStatus;
        }

        public void setIsDLUploadStatus(String isDLUploadStatus) {
            IsDLUploadStatus = isDLUploadStatus;
        }

        public String IsDLUploadStatus;

        public String getIsVaccinated() {
            return IsVaccinated;
        }

        public void setIsVaccinated(String isVaccinated) {
            IsVaccinated = isVaccinated;
        }

        public String State;
        public String IsVaccinated;

        public String getUserProfileImagel() {
            return UserProfileImage;
        }

        public void setUserProfileImagel(String userProfileImagel) {
            UserProfileImage = userProfileImagel;
        }

        public String UserProfileImage;
        public long getUserID() {
            return UserID;
        }

        public void setUserID(long userID) {
            UserID = userID;
        }

        public String getType() {
            return Type;
        }

        public void setType(String type) {
            Type = type;
        }

        public String getUserTypeID() {
            return UserTypeID;
        }

        public void setUserTypeID(String userTypeID) {
            UserTypeID = userTypeID;
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

        public String getShowroomName() {
            return ShowroomName;
        }

        public void setShowroomName(String showroomName) {
            ShowroomName = showroomName;
        }

        public String getCompanyName() {
            return CompanyName;
        }

        public void setCompanyName(String companyName) {
            CompanyName = companyName;
        }

        public String getShowroomID() {
            return ShowroomID;
        }

        public void setShowroomID(String showroomID) {
            ShowroomID = showroomID;
        }

        public String getEmployeeID() {
            return EmployeeID;
        }

        public void setEmployeeID(String employeeID) {
            EmployeeID = employeeID;
        }

        public String getDOB() {
            return DOB;
        }

        public void setDOB(String DOB) {
            this.DOB = DOB;
        }

        public String getState() {
            return State;
        }

        public void setState(String state) {
            State = state;
        }

        public String getDistrict() {
            return District;
        }

        public void setDistrict(String district) {
            District = district;
        }

        public String getCity() {
            return City;
        }

        public void setCity(String city) {
            City = city;
        }

        public String getAddress() {
            return Address;
        }

        public void setAddress(String address) {
            Address = address;
        }

        public String getLandMark() {
            return LandMark;
        }

        public void setLandMark(String landMark) {
            LandMark = landMark;
        }

        public String getPincode() {
            return Pincode;
        }

        public void setPincode(String pincode) {
            Pincode = pincode;
        }

        public String getISPushNotificationOn() {
            return ISPushNotificationOn;
        }

        public void setISPushNotificationOn(String ISPushNotificationOn) {
            this.ISPushNotificationOn = ISPushNotificationOn;
        }

        public String getUserLatitude() {
            return UserLatitude;
        }

        public void setUserLatitude(String userLatitude) {
            UserLatitude = userLatitude;
        }

        public String getUserLongitude() {
            return UserLongitude;
        }

        public void setUserLongitude(String userLongitude) {
            UserLongitude = userLongitude;
        }

        public String District;
        public String City;
        public String Address;
        public String LandMark;
        public String Pincode;
        public String ISPushNotificationOn;
        public String UserLatitude;
        public String UserLongitude;
    }


}
