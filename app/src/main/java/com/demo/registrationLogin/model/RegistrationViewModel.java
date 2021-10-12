package com.demo.registrationLogin.model;

import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

public class RegistrationViewModel extends ViewModel {

    private MutableLiveData<RegistrationRequestModel> currentName;

    public MutableLiveData<RegistrationRequestModel> getCurrentName() {
        if (currentName == null) {
            currentName = new MutableLiveData<RegistrationRequestModel>();
        }
        return currentName;
    }

// Rest of the ViewModel...
}
