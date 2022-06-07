package com.demo.home.model.viewmodel;

import android.app.Application;
import android.content.Context;

import androidx.annotation.NonNull;
import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import com.demo.home.model.CarFilterResponse;
import com.demo.registrationLogin.model.RegistrationRequestModel;
import com.demo.utils.PrintLog;
import com.demo.webservice.RestClient;

import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class CarFilterViewModel extends AndroidViewModel {

    private LiveData<CarFilterResponse> budgetListData;
    private LiveData<CarFilterResponse> segmentListData;
    private LiveData<CarFilterResponse> brandListData;
    private LiveData<CarFilterResponse> fuelListData;

    public CarFilterViewModel(@NonNull Application application) {
        super(application);
        budgetListData =getBudgetList(application);
        segmentListData =getSegmentList(application);
        brandListData =getBrandList(application);
        fuelListData = getFuelList(application);


    }

    private LiveData<CarFilterResponse> getFuelList(Application application) {
        final MutableLiveData<CarFilterResponse> data = new MutableLiveData<>();
        Call objectCall = RestClient.getApiService().fuelFilter();

        objectCall.enqueue(new Callback() {
            @Override
            public void onResponse(Call call, Response response) {
                PrintLog.v("","=====onApiResponse");
                data.postValue((CarFilterResponse) response.body());
                fuelListData = data;
            }

            @Override
            public void onFailure(Call call, Throwable t) {

            }
        });

        return data;
    }

    private LiveData<CarFilterResponse> getBrandList(Application application) {
        final MutableLiveData<CarFilterResponse> data = new MutableLiveData<>();
        Call objectCall = RestClient.getApiService().carFilterBrand();

        objectCall.enqueue(new Callback() {
            @Override
            public void onResponse(Call call, Response response) {
                PrintLog.v("","=====onApiResponse");
                data.postValue((CarFilterResponse) response.body());
                brandListData = data;
            }

            @Override
            public void onFailure(Call call, Throwable t) {

            }
        });

        return data;
    }


    private LiveData<CarFilterResponse> getSegmentList(Application application) {
        final MutableLiveData<CarFilterResponse> data = new MutableLiveData<>();
        Call objectCall = RestClient.getApiService().carFilterSegment();

        objectCall.enqueue(new Callback() {
            @Override
            public void onResponse(Call call, Response response) {
                PrintLog.v("","=====onApiResponse");
                data.postValue((CarFilterResponse) response.body());
                segmentListData = data;
            }

            @Override
            public void onFailure(Call call, Throwable t) {

            }
        });

        return data;

    }


    public LiveData<CarFilterResponse> getBudgetListData() {
        if (budgetListData == null) {
            budgetListData = new MutableLiveData<CarFilterResponse>();
        }
        return budgetListData;
    }

    public LiveData<CarFilterResponse> getSegmentListData() {
        if (segmentListData == null) {
            segmentListData = new MutableLiveData<CarFilterResponse>();
        }
        return segmentListData;
    }

    public LiveData<CarFilterResponse> getBrandListData() {
        if (brandListData == null) {
            brandListData = new MutableLiveData<>();
        }
        return brandListData;
    }


    public LiveData<CarFilterResponse> getFuelListData() {
        if (fuelListData == null) {
            fuelListData = new MutableLiveData<>();
        }
        return fuelListData;
    }


    public LiveData<CarFilterResponse> getBudgetList(Context context) {
        final MutableLiveData<CarFilterResponse> data = new MutableLiveData<>();
        Call objectCall = RestClient.getApiService().carFilterPrice();

        objectCall.enqueue(new Callback() {
            @Override
            public void onResponse(Call call, Response response) {
                PrintLog.v("","=====onApiResponse");
                data.postValue((CarFilterResponse) response.body());
                budgetListData = data;
            }

            @Override
            public void onFailure(Call call, Throwable t) {

            }
        });
//        RestClient.makeApiRequest(context.getApplicationContext(), objectCall, new ApiResponseListener() {
//            @Override
//            public void onApiResponse(Call<Object> call, Object response, int reqCode) throws Exception {
//                PrintLog.v("","=====onApiResponse");
//                data.postValue(((CarFilterResponse) response));
//            }
//
//            @Override
//            public void onApiError(Call<Object> call, Object response, int reqCode) throws Exception {
//                PrintLog.v("","=====onApiError");
//            }
//        }, 1, true);


        return data;



    }

// Rest of the ViewModel...
}

