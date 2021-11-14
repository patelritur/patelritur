package com.demo.utils;

import android.content.Context;

import androidx.annotation.NonNull;

import com.demo.registrationLogin.model.AddTokenRequestModel;
import com.demo.webservice.ApiResponseListener;
import com.demo.webservice.RestClient;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.messaging.FirebaseMessaging;

import retrofit2.Call;

public class NotificationUtils
{
    public static void setUpFCMNotifiction(Context context, String userId) {

        FirebaseMessaging.getInstance().getToken()
                .addOnCompleteListener(new OnCompleteListener<String>() {
                    @Override
                    public void onComplete(@NonNull Task<String> task) {
                        if (!task.isSuccessful()) {
                            return;
                        }
                        AddTokenRequestModel addTokenRequestModel = new AddTokenRequestModel();
                        addTokenRequestModel.setUserID(userId);
                        addTokenRequestModel.setTokenType("Add");

                        // Get new FCM registration token
                        String token = task.getResult();
                        addTokenRequestModel.setDeviceToken(token);
                        Call objectCall = RestClient.getApiService().addNotificationToken(addTokenRequestModel);
                        RestClient.makeApiRequest(context, objectCall, new ApiResponseListener() {
                            @Override
                            public void onApiResponse(Call<Object> call, Object response, int reqCode) throws Exception {
                            }

                            @Override
                            public void onApiError(Call<Object> call, Object response, int reqCode) throws Exception {

                            }
                        }, 2, true);
                        // Log and toast
                    }
                });
    }


}
