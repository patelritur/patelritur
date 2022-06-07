package com.demo.home.model.viewmodel;

import android.app.Application;

import androidx.annotation.NonNull;
import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;

import com.demo.home.model.WeatherResposneModel;
import com.demo.utils.PrintLog;
import com.demo.webservice.RestClient;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class WeatherViewModel extends AndroidViewModel {

    private LiveData<WeatherResposneModel> weatherResposneodelLiveData;

    public WeatherViewModel(@NonNull Application application,String location) {
        super(application);
        weatherResposneodelLiveData =getWeatherData(application,location);


    }


    private LiveData<WeatherResposneModel> getWeatherData(Application application, String location) {
        final MutableLiveData<WeatherResposneModel> data = new MutableLiveData<>();
        Call objectCall = RestClient.getApiService().getWeatherApi(location);

        objectCall.enqueue(new Callback() {
            @Override
            public void onResponse(Call call, Response response) {
                PrintLog.v("","=====onApiResponse");
                data.postValue((WeatherResposneModel) response.body());
                weatherResposneodelLiveData = data;
            }

            @Override
            public void onFailure(Call call, Throwable t) {

            }
        });

        return data;
    }

    public LiveData<WeatherResposneModel> getWeatherResposneodelLiveData() {
        if (weatherResposneodelLiveData == null) {
            weatherResposneodelLiveData = new MutableLiveData<WeatherResposneModel>();
        }
        return weatherResposneodelLiveData;
    }



}

