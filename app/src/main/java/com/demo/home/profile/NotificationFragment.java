package com.demo.home.profile;

import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.databinding.DataBindingUtil;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.DividerItemDecoration;

import com.demo.R;
import com.demo.carDetails.model.CarDetailRequest;
import com.demo.databinding.FragmentNotificationsBinding;
import com.demo.notifications.NotificationsAdapter;
import com.demo.notifications.model.NotificationResponseModel;
import com.demo.utils.Constants;
import com.demo.utils.PrintLog;
import com.demo.utils.SharedPrefUtils;
import com.demo.utils.Utils;
import com.demo.webservice.ApiResponseListener;
import com.demo.webservice.RestClient;

import java.util.ArrayList;

import retrofit2.Call;

public class NotificationFragment extends Fragment implements ApiResponseListener {

    FragmentNotificationsBinding fragmentNotificationsBinding;
    SharedPrefUtils sharedPrefUtils;

    public static Fragment newInstance(int position) {
        NotificationFragment fragmentFirst = new NotificationFragment();
        Bundle args = new Bundle();
        args.putInt("pos", position);
        fragmentFirst.setArguments(args);
        return fragmentFirst;

    }


    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {

        fragmentNotificationsBinding = DataBindingUtil.inflate(inflater, R.layout.fragment_notifications,container,false);
        sharedPrefUtils = new SharedPrefUtils(getActivity());
        callNotificationApi();
        return fragmentNotificationsBinding.getRoot();
    }

    private void callNotificationApi() {
        CarDetailRequest carDetailRequest = new CarDetailRequest();
        carDetailRequest.setUserID(sharedPrefUtils.getStringData(Constants.USER_ID));
        carDetailRequest.setLatitude(String.valueOf(Constants.LATITUDE));
        carDetailRequest.setLongitude(String.valueOf(Constants.LONGITUDE));

        Call objectCall = RestClient.getApiService().getNotifications(carDetailRequest);
        RestClient.makeApiRequest(getActivity(), objectCall, this, 1, true);

    }

    @Override
    public void onApiResponse(Call<Object> call, Object response, int reqCode) throws Exception {
        NotificationResponseModel notificationResponseModel = (NotificationResponseModel) response;
        if(notificationResponseModel.getResponseCode().equalsIgnoreCase("104")){
            PrintLog.v("===broadcast00");
            Utils.showToast(getActivity(),notificationResponseModel.getDescriptions());
            sharedPrefUtils.saveData(Constants.NOTIFICATION_COUNT, "0");

        }
        else if(notificationResponseModel.getResponseCode().equalsIgnoreCase("200")) {
            PrintLog.v("===broadcast");
            if (sharedPrefUtils.getStringData(Constants.NOTIFICATION_COUNT) != null && !sharedPrefUtils.getStringData(Constants.NOTIFICATION_COUNT).equalsIgnoreCase(notificationResponseModel.notificationCount))
            {
                PrintLog.v("visible");
                ((MyDemoActivity)getActivity()).updateVisibleDot();

            }
            sharedPrefUtils.saveData(Constants.NOTIFICATION_COUNT, notificationResponseModel.notificationCount);
            fragmentNotificationsBinding.recyclerview.setAdapter(new NotificationsAdapter(getActivity(), (ArrayList<NotificationResponseModel.Notification>) notificationResponseModel.getNotification()));
            fragmentNotificationsBinding.recyclerview.addItemDecoration(new DividerItemDecoration(getActivity(), DividerItemDecoration.VERTICAL));
        }
    }

    @Override
    public void onApiError(Call<Object> call, Object response, int reqCode) throws Exception {

    }


}
