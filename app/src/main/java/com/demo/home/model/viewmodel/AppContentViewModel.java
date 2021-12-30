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
import com.demo.utils.Constants;
import com.demo.utils.PrintLog;
import com.demo.webservice.RestClient;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class AppContentViewModel extends AndroidViewModel {

    private LiveData<AppContentModel> homeMenuLiveData;
    private LiveData<AppContentModel> menuLiveData;
    private LiveData<AppContentModel> bottomMenuLiveData;
    private LiveData<AppContentModel> tutorialMenuLiveData;
    private LiveData<AppContentModel> schedulemenuLivedata;
    private LiveData<AppContentModel> bannerLiveData;
    private LiveData<AppContentModel> cancelreasonLiveData;
    private LiveData<AppContentModel> myDemoMenuLiveData;
    private LiveData<AppContentModel> myDemoTripsFilterLiveData;
    private LiveData<AppContentModel> supportEmailLiveData;
    private LiveData<AppContentModel> myDemoTripsMenuLiveData;
    private LiveData<AppContentModel> myDemoDescriptionsLiveData;
    private LiveData<AppContentModel> specialistsDescriptionsLiveData;

    public AppContentViewModel(@NonNull Application application, String mParam) {
        super(application);

        if(mParam.equalsIgnoreCase(Constants.MAIN_MENU))
            bottomMenuLiveData = getHomeMenuList(mParam);
        else if(mParam.equalsIgnoreCase(Constants.HOME_MENU))
            homeMenuLiveData =getHomeMenuList(mParam);
        else if(mParam.equalsIgnoreCase(Constants.BANNER))
            bannerLiveData =getHomeMenuList(mParam);
        else if(mParam.equalsIgnoreCase(Constants.APP_START))
            tutorialMenuLiveData =getHomeMenuList(mParam);
        else if(mParam.equalsIgnoreCase(Constants.SCHEDULE_MENU))
            schedulemenuLivedata =getHomeMenuList(mParam);
        else if(mParam.equalsIgnoreCase(Constants.LEFT_MENU))
            menuLiveData =getHomeMenuList(mParam);
        else if(mParam.equalsIgnoreCase(Constants.CANCEL_REASON))
            cancelreasonLiveData =getHomeMenuList(mParam);
        else if(mParam.equalsIgnoreCase(Constants.MYDEMO_MENU))
            myDemoMenuLiveData =getHomeMenuList(mParam);
        else if(mParam.equalsIgnoreCase(Constants.MYDEMO_TRIPS_FILTER))
            myDemoTripsFilterLiveData =getHomeMenuList(mParam);
        else if(mParam.equalsIgnoreCase(Constants.SUPPORT_EMAIL))
            supportEmailLiveData =getHomeMenuList(mParam);
        else if(mParam.equalsIgnoreCase(Constants.MYDEMO_TRIPS_MENU))
            myDemoTripsMenuLiveData = getHomeMenuList(mParam);
        else if(mParam.equalsIgnoreCase(Constants.MYDEMO_DESCRIPTIONS))
            myDemoDescriptionsLiveData = getHomeMenuList(mParam);
        else if(mParam.equalsIgnoreCase(Constants.SPECIALIST_DESCRIPTIONS))
            specialistsDescriptionsLiveData = getHomeMenuList(mParam);

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


    public LiveData<AppContentModel> getMyDemoDescriptionsLiveData() {
        if (myDemoDescriptionsLiveData == null)
        {
            myDemoDescriptionsLiveData = new MutableLiveData<>();
        }
        return myDemoDescriptionsLiveData;
    }
    public LiveData<AppContentModel> getSpecialistsDescriptionsLiveData() {
        if (specialistsDescriptionsLiveData == null)
        {
            specialistsDescriptionsLiveData = new MutableLiveData<>();
        }
        return specialistsDescriptionsLiveData;
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

    public LiveData<AppContentModel> getSchedulemenuLivedata() {
        if (schedulemenuLivedata == null)
        {
            schedulemenuLivedata = new MutableLiveData<>();
        }
        return schedulemenuLivedata;
    }

    public LiveData<AppContentModel> getMenuLiveData() {
        if (menuLiveData == null)
        {
            menuLiveData = new MutableLiveData<>();
        }
        return menuLiveData;
    }

    public LiveData<AppContentModel> getCancelreasonLiveData() {
        if (cancelreasonLiveData == null)
        {
            cancelreasonLiveData = new MutableLiveData<>();
        }
        return cancelreasonLiveData;
    }


    public LiveData<AppContentModel> getMyDemoTripsFilterLiveData() {
        if (myDemoTripsFilterLiveData == null)
        {
            myDemoTripsFilterLiveData = new MutableLiveData<>();
        }
        return myDemoTripsFilterLiveData;
    }

    public LiveData<AppContentModel> getMyDemoMenuLiveData() {
        if (myDemoMenuLiveData == null)
        {
            myDemoMenuLiveData = new MutableLiveData<>();
        }
        return myDemoMenuLiveData;
    }
    public LiveData<AppContentModel> getSupportEmailLiveData() {
        if (supportEmailLiveData == null)
        {
            supportEmailLiveData = new MutableLiveData<>();
        }
        return supportEmailLiveData;
    }

    public LiveData<AppContentModel> getMyDemoTripsMenuLiveData() {
        if (myDemoTripsMenuLiveData == null)
        {
            myDemoTripsMenuLiveData = new MutableLiveData<>();
        }
        return myDemoTripsMenuLiveData;
    }
}


