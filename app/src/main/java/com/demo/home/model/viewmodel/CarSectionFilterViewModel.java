package com.demo.home.model.viewmodel;

import android.app.Application;
import android.content.Context;

import androidx.annotation.NonNull;
import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;

import com.demo.home.model.CarFilterResponse;
import com.demo.home.model.CarSectionFilterResponse;
import com.demo.utils.PrintLog;
import com.demo.webservice.RestClient;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class CarSectionFilterViewModel extends AndroidViewModel {

    private LiveData<CarSectionFilterResponse> sectionListData;

    public CarSectionFilterViewModel(@NonNull Application application) {
        super(application);
        sectionListData =getSectionListData(application);


    }

    private LiveData<CarSectionFilterResponse> getSectionListData(Application application) {
        final MutableLiveData<CarSectionFilterResponse> data = new MutableLiveData<>();
        Call objectCall = RestClient.getApiService().carFilterSection();

        objectCall.enqueue(new Callback() {
            @Override
            public void onResponse(Call call, Response response) {
                PrintLog.v("","=====onApiResponse");
                data.postValue((CarSectionFilterResponse) response.body());
                sectionListData = data;
            }

            @Override
            public void onFailure(Call call, Throwable t) {

            }
        });

        return data;
    }




    public LiveData<CarSectionFilterResponse> getSectionListData() {
        if (sectionListData == null) {
            sectionListData = new MutableLiveData<>();
        }
        return sectionListData;
    }








// Rest of the ViewModel...
}

