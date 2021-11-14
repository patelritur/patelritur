package com.demo.notifications;

import android.os.Bundle;
import android.view.View;

import androidx.annotation.Nullable;
import androidx.databinding.DataBindingUtil;
import androidx.recyclerview.widget.DividerItemDecoration;

import com.demo.BaseActivity;
import com.demo.R;
import com.demo.carDetails.model.CarDetailRequest;
import com.demo.databinding.ActivityNotificationsBinding;
import com.demo.notifications.model.NotificationResponseModel;
import com.demo.utils.Constants;
import com.demo.utils.SharedPrefUtils;
import com.demo.webservice.ApiResponseListener;
import com.demo.webservice.RestClient;

import java.util.ArrayList;

import retrofit2.Call;

public class NotificationActivity extends BaseActivity implements ApiResponseListener {
    ActivityNotificationsBinding activityNotificationsBinding;
    SharedPrefUtils sharedPrefUtils;
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        sharedPrefUtils = new SharedPrefUtils(this);
        activityNotificationsBinding =  DataBindingUtil.setContentView(this, R.layout.activity_notifications);
        callNotificationApi();

    }

    private void callNotificationApi() {
        CarDetailRequest carDetailRequest = new CarDetailRequest();
        carDetailRequest.setUserID(sharedPrefUtils.getStringData(Constants.USER_ID));
        carDetailRequest.setLatitude(String.valueOf(Constants.LATITUDE));
        carDetailRequest.setLongitude(String.valueOf(Constants.LONGITUDE));

        Call objectCall = RestClient.getApiService().getNotifications(carDetailRequest);
        RestClient.makeApiRequest(this, objectCall, this, 1, true);

    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        finish();
    }

    @Override
    public void onClick(View view) {
        if(view.getId() ==  R.id.back_icon){
            onBackPressed();
            finish();
        }
    }

    @Override
    public void onApiResponse(Call<Object> call, Object response, int reqCode) throws Exception {
        NotificationResponseModel notificationResponseModel = (NotificationResponseModel) response;
        notificationResponseModel.setImage(sharedPrefUtils.getStringData(Constants.IMAGE) );
        notificationResponseModel.setClickHandlers(this);

        activityNotificationsBinding.setNotifications(notificationResponseModel);
        activityNotificationsBinding.executePendingBindings();
        activityNotificationsBinding.recyclerview.setAdapter(new NotificationsAdapter(this, (ArrayList<NotificationResponseModel.Notification>) notificationResponseModel.getNotification()));
        activityNotificationsBinding.recyclerview.addItemDecoration(new DividerItemDecoration(this,DividerItemDecoration.VERTICAL));

    }

    @Override
    public void onApiError(Call<Object> call, Object response, int reqCode) throws Exception {

    }
}
