package com.demo.home.model.viewmodel;

import android.app.Application;

import androidx.lifecycle.ViewModel;
import androidx.lifecycle.ViewModelProvider;

import com.demo.home.model.CarSearchRequestModel;
import com.demo.home.model.CarSearchResultModel;

public class SearchResultViewModelFactory implements ViewModelProvider.Factory {
    private Application mApplication;
    private CarSearchRequestModel mParam;


    public SearchResultViewModelFactory(Application application, CarSearchRequestModel param) {
        mApplication = application;
        mParam = param;
    }


    @Override
    public <T extends ViewModel> T create(Class<T> modelClass) {
        return (T) new SearchResultViewModel(mApplication, mParam);
    }
}
