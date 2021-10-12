package com.demo.home.model.viewmodel;

import android.app.Application;

import androidx.annotation.NonNull;
import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;

import com.demo.home.model.MenuResponse;
import com.demo.utils.PrintLog;
import com.demo.webservice.RestClient;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class MeetingPlaceViewModel extends AndroidViewModel {

    private LiveData<MenuResponse> menuResponseLiveData;

    public MeetingPlaceViewModel(@NonNull Application application) {
        super(application);
        menuResponseLiveData =getMenuList();


    }

    private LiveData<MenuResponse> getMenuList() {
        final MutableLiveData<MenuResponse> data = new MutableLiveData<>();
        Call objectCall = RestClient.getApiService().meettype();

        objectCall.enqueue(new Callback() {
            @Override
            public void onResponse(Call call, Response response) {
                PrintLog.v("","=====onApiResponse");
                data.postValue((MenuResponse) response.body());
                menuResponseLiveData = data;
            }

            @Override
            public void onFailure(Call call, Throwable t) {

            }
        });

        return data;
    }






    public LiveData<MenuResponse> getMenuType() {
        if (menuResponseLiveData == null) {
            menuResponseLiveData = new MutableLiveData<>();
        }
        return menuResponseLiveData;
    }


// Rest of the ViewModel...
}


