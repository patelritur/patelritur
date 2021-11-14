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

    private LiveData<MenuResponse> meetspecalistLiveData;
    private LiveData<MenuResponse> bookDemoListLiveData;

    public MeetingPlaceViewModel(@NonNull Application application) {
        super(application);
        meetspecalistLiveData =getMenuList();
        bookDemoListLiveData = getBookDemoListLiveData();



    }

    private LiveData<MenuResponse> getBookDemoListLiveData() {
        final MutableLiveData<MenuResponse> data = new MutableLiveData<>();
        Call objectCall = RestClient.getApiService().demotype();

        objectCall.enqueue(new Callback() {
            @Override
            public void onResponse(Call call, Response response) {
                PrintLog.v("","=====onApiResponse");
                data.postValue((MenuResponse) response.body());
                bookDemoListLiveData= data;
            }

            @Override
            public void onFailure(Call call, Throwable t) {

            }
        });

        return data;
    }

    private LiveData<MenuResponse> getMenuList() {
        final MutableLiveData<MenuResponse> data = new MutableLiveData<>();
        Call objectCall = RestClient.getApiService().meettype();

        objectCall.enqueue(new Callback() {
            @Override
            public void onResponse(Call call, Response response) {
                PrintLog.v("","=====onApiResponse");
                data.postValue((MenuResponse) response.body());
                meetspecalistLiveData = data;
            }

            @Override
            public void onFailure(Call call, Throwable t) {

            }
        });

        return data;
    }






    public LiveData<MenuResponse> getMenuType() {
        if (meetspecalistLiveData == null) {
            meetspecalistLiveData = new MutableLiveData<>();
        }
        return meetspecalistLiveData;
    }

    public LiveData<MenuResponse> getBookingType() {
        if (bookDemoListLiveData == null) {
            bookDemoListLiveData = new MutableLiveData<>();
        }
        return bookDemoListLiveData;
    }



// Rest of the ViewModel...
}


