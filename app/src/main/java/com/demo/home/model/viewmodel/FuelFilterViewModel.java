package com.demo.home.model.viewmodel;

import android.app.Application;
import android.content.Context;

import androidx.annotation.NonNull;
import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;

import com.demo.home.model.CarFilterResponse;
import com.demo.utils.PrintLog;
import com.demo.webservice.RestClient;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class FuelFilterViewModel extends AndroidViewModel {

    private LiveData<CarFilterResponse> fuelTypeListData;

    public FuelFilterViewModel(@NonNull Application application) {
        super(application);
        fuelTypeListData =getFuelList(application);


    }


    private LiveData<CarFilterResponse> getFuelList(Application application) {
        final MutableLiveData<CarFilterResponse> data = new MutableLiveData<>();
        Call objectCall = RestClient.getApiService().fuelFilter();

        objectCall.enqueue(new Callback() {
            @Override
            public void onResponse(Call call, Response response) {
                PrintLog.v("","=====onApiResponse");
                data.postValue((CarFilterResponse) response.body());
                fuelTypeListData = data;
            }

            @Override
            public void onFailure(Call call, Throwable t) {

            }
        });

        return data;
    }

    public LiveData<CarFilterResponse> getFuelTypeListData() {
        if (fuelTypeListData == null) {
            fuelTypeListData = new MutableLiveData<CarFilterResponse>();
        }
        return fuelTypeListData;
    }



}

