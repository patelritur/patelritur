package com.demo.utils;

import android.content.Context;
import android.util.Log;
import android.widget.Toast;

import androidx.annotation.NonNull;

import com.cometchat.pro.core.CometChat;
import com.cometchat.pro.exceptions.CometChatException;
import com.demo.registrationLogin.model.AddTokenRequestModel;
import com.demo.webservice.ApiResponseListener;
import com.demo.webservice.RestClient;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.messaging.FirebaseMessaging;

import retrofit2.Call;

public class NotificationUtils
{
    public static void setUpFCMNotifiction(Context context, String userId,String type) {
        FirebaseMessaging.getInstance().getToken()
                .addOnCompleteListener(new OnCompleteListener<String>() {


                    @Override
                    public void onComplete(@NonNull Task<String> task) {
                        if (!task.isSuccessful()) {
                            return;

                        }
                        AddTokenRequestModel addTokenRequestModel = new AddTokenRequestModel();
                        addTokenRequestModel.setUserID(userId);
                        addTokenRequestModel.setTokenType(type);

                        // Get new FCM registration token
                       String  token = task.getResult();
                        addTokenRequestModel.setDeviceToken(token);
                        Call objectCall = RestClient.getApiService().addNotificationToken(addTokenRequestModel);
                        RestClient.makeApiRequest(context, objectCall, new ApiResponseListener() {
                            @Override
                            public void onApiResponse(Call<Object> call, Object response, int reqCode) throws Exception {
                                Utils.showToast(context,"Notification Token Added");
                                CometChat.registerTokenForPushNotification(token, new CometChat.CallbackListener<String>() {
                                    @Override
                                    public void onSuccess(String s) {
                                        Log.e("onSuccess: ",s.toString() );
                                    }

                                    @Override
                                    public void onError(CometChatException e) {
                                        Log.e("onErrorPN: ",e.getMessage() );
                                    }
                                });
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
