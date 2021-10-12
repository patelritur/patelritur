package com.demo.home.model.viewmodel;

import android.app.Application;

import androidx.annotation.NonNull;
import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;

import com.demo.home.CustomMutableLiveData;
import com.demo.home.model.CarSearchRequestModel;
import com.demo.home.model.CarSearchResultModel;
import com.demo.utils.PrintLog;
import com.demo.webservice.RestClient;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class SearchResultViewModel extends AndroidViewModel {
    CustomMutableLiveData<CarSearchResultModel> searchResultViewModelLiveData = new CustomMutableLiveData<>();


    public SearchResultViewModel(@NonNull Application application, CarSearchRequestModel carSearchRequestModel) {
        super(application);
        searchResultViewModelLiveData = getSearchList(carSearchRequestModel);


    }

    private CustomMutableLiveData<CarSearchResultModel> getSearchList(CarSearchRequestModel carSearchRequestModel) {
        final CustomMutableLiveData<CarSearchResultModel> data = new CustomMutableLiveData<>();
        Call objectCall = RestClient.getApiService().getCarSearchList(carSearchRequestModel);

        objectCall.enqueue(new Callback() {
            @Override
            public void onResponse(Call call, Response response) {
                PrintLog.v("","=====onApiResponse");
                data.postValue((CarSearchResultModel) response.body());
                searchResultViewModelLiveData.postValue((CarSearchResultModel) response.body());
            }

            @Override
            public void onFailure(Call call, Throwable t) {

            }
        });

        return data;

    }




    public LiveData<CarSearchResultModel> getSearchResultLiveData() {
        if (searchResultViewModelLiveData == null)
        {
            searchResultViewModelLiveData = new CustomMutableLiveData<>();
        }
        return searchResultViewModelLiveData;
    }
}
