package com.demo.home.model.viewmodel;

import android.app.Application;
import android.content.Context;

import androidx.annotation.NonNull;
import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;

import com.demo.home.model.AppContentModel;
import com.demo.home.model.AppRequestModel;
import com.demo.home.model.CarFilterResponse;
import com.demo.utils.PrintLog;
import com.demo.webservice.RestClient;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class AppContentViewModel extends AndroidViewModel {

    private LiveData<AppContentModel> homeMenuLiveData;
    private LiveData<AppContentModel> bottomMenuLiveData;
    private LiveData<AppContentModel> tutorialMenuLiveData;
    private LiveData<AppContentModel> bannerLiveData;

    public AppContentViewModel(@NonNull Application application, String mParam) {
        super(application);

        if(mParam.equalsIgnoreCase("MainMenu"))
            bottomMenuLiveData = getHomeMenuList(mParam);
        else if(mParam.equalsIgnoreCase("HomeMenu"))
            homeMenuLiveData =getHomeMenuList(mParam);
        else if(mParam.equalsIgnoreCase("ProductListBanner"))
            bannerLiveData =getHomeMenuList(mParam);
        else if(mParam.equalsIgnoreCase("AppStartInfo"))
            tutorialMenuLiveData =getHomeMenuList(mParam);


    }

    private LiveData<AppContentModel> getHomeMenuList(String page) {
        final MutableLiveData<AppContentModel> data = new MutableLiveData<>();
        AppRequestModel appRequestModel = new AppRequestModel();
        appRequestModel.setPage(page);
        Call objectCall = RestClient.getApiService().getAppContent(appRequestModel);

        objectCall.enqueue(new Callback() {
            @Override
            public void onResponse(Call call, Response response) {
                PrintLog.v("","=====onApiResponse");
                data.postValue((AppContentModel) response.body());
            }

            @Override
            public void onFailure(Call call, Throwable t) {

            }
        });

        return data;
    }



    public LiveData<AppContentModel> getBannerLiveData() {
        if (bannerLiveData == null)
        {
            bannerLiveData = new MutableLiveData<>();
        }
        return bannerLiveData;
    }


    public LiveData<AppContentModel> getHomeMenuLiveData() {
        if (homeMenuLiveData == null)
        {
            homeMenuLiveData = new MutableLiveData<>();
        }
        return homeMenuLiveData;
    }

    public LiveData<AppContentModel> getTutorialMenuLiveData() {
        if (tutorialMenuLiveData == null)
        {
            tutorialMenuLiveData = new MutableLiveData<>();
        }
        return tutorialMenuLiveData;
    }

    public LiveData<AppContentModel> getBottomMenuLiveData() {
        if (bottomMenuLiveData == null)
        {
            bottomMenuLiveData = new MutableLiveData<>();
        }
        return bottomMenuLiveData;
    }
}


