package com.demo.home.model.viewmodel;

import android.app.Application;

import androidx.lifecycle.ViewModel;
import androidx.lifecycle.ViewModelProvider;

public class AppContentViewModelFactory implements ViewModelProvider.Factory {
    private Application mApplication;
    private String mParam;


    public AppContentViewModelFactory(Application application, String param) {
        mApplication = application;
        mParam = param;
    }


    @Override
    public <T extends ViewModel> T create(Class<T> modelClass) {
        return (T) new AppContentViewModel(mApplication, mParam);
    }
}
