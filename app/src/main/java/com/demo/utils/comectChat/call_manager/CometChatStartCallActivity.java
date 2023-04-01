package com.demo.utils.comectChat.call_manager;

import android.app.ActivityManager;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;

import com.cometchat.pro.core.Call;
import com.cometchat.pro.core.CallSettings;
import com.cometchat.pro.core.CometChat;
import com.cometchat.pro.exceptions.CometChatException;
import com.cometchat.pro.models.AudioMode;
import com.cometchat.pro.models.User;
import com.demo.BaseActivity;
import com.demo.R;
import com.demo.utils.CometChatCallActivity;
import com.demo.utils.comectChat.UIKitConstants;
import com.demo.utils.comectChat.call_manager.ongoing_call.OngoingCallService;
import com.demo.utils.comectChat.utils.CometChatError;
import com.google.android.material.snackbar.Snackbar;

import java.util.List;

/**
 * CometChatStartCallActivity is activity class which is used to start a call. It takes sessionID
 * as a parameter and start call for particular sessionID.
 *
 * Created On - 22nd August 2020
 *
 * Modified On -  07th October 2020
 *
 */
public class CometChatStartCallActivity extends BaseActivity {

    public static CometChatStartCallActivity activity;
    public boolean callended;

    private RelativeLayout mainView;

    private String sessionID;

    private String type;

    private CallSettings callSettings;

    private LinearLayout connectingLayout;

    private OngoingCallService ongoingCallService;

    private Intent mServiceIntent;

    private boolean isDefaultCall;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        activity = this;
        setContentView(R.layout.activity_cometchat_start_call);
        ongoingCallService = new OngoingCallService();
        mServiceIntent = new Intent(this,ongoingCallService.getClass());
        isDefaultCall = getIntent().getBooleanExtra(UIKitConstants.IntentStrings.IS_DEFAULT_CALL,false);
        if (isDefaultCall && !isMyServiceRunning(ongoingCallService.getClass())) {
            startService(mServiceIntent);
        }

        mainView = findViewById(R.id.call_view);
        connectingLayout = findViewById(R.id.connecting_to_call);
        sessionID = getIntent().getStringExtra(UIKitConstants.IntentStrings.SESSION_ID);
        type = getIntent().getStringExtra(UIKitConstants.IntentStrings.TYPE);
//        if (type!=null && type.equalsIgnoreCase(CometChatConstants.RECEIVER_TYPE_USER))
            callSettings = new CallSettings.CallSettingsBuilder(this,mainView)
                    .setSessionId(sessionID)
                    .setMode(CallSettings.MODE_SINGLE)
                    .enableDefaultLayout(true)
                    .build();


        CometChatError.init(this);
        Log.e( "startCallActivity: ",sessionID+" "+type);
        CometChat.startCall(callSettings, new CometChat.OngoingCallListener() {
            @Override
            public void onUserListUpdated(List<User> list) {
                Log.e( "onUserListUpdated: ",list.toString() );
            }

            @Override
            public void onAudioModesUpdated(List<AudioMode> list) {
                Log.e("onAudioModesUpdated: ",list.toString() );
            }

            @Override
            public void onRecordingStarted(User user) {

            }

            @Override
            public void onRecordingStopped(User user) {

            }

            @Override
            public void onUserMuted(User user, User user1) {

            }

            @Override
            public void onCallSwitchedToVideo(String s, User user, User user1) {

            }

            @Override
            public void onUserJoined(User user) {
                connectingLayout.setVisibility(View.GONE);
                Snackbar.make(CometChatStartCallActivity.this,
                        mainView, getString(R.string.user_joined)+":"+ user.getName(),
                       1000);
                Log.e("onUserJoined: ", user.getUid());
            }

            @Override
            public void onUserLeft(User user) {
                if (user!=null) {
                    Snackbar.make(CometChatStartCallActivity.this,
                            mainView, getString(R.string.user_left)+":"+ user.getName(),
                            1000);
                    Log.e("onUserLeft: ", user.getUid());
                    if (callSettings.getMode().equals(CallSettings.MODE_SINGLE)) {
                        endCall();
                    }
                } else {
                    Log.e( "onUserLeft: ","triggered" );
                }
            }

            @Override
            public void onError(CometChatException e) {
                stopService(mServiceIntent);
                Log.e("onstartcallError: ", e.getMessage());
                Snackbar.make(CometChatStartCallActivity.this,
                        mainView,CometChatError.localized(e), 1000);
            }

            @Override
            public void onCallEnded(Call call) {
                stopService(mServiceIntent);
                Log.e("TAG", "onCallEnded: ");
                callended=true;
                setResult(RESULT_OK);
                finish();
            }
        });
    }

    private boolean isMyServiceRunning(Class<? extends OngoingCallService> serviceClass) {
        ActivityManager manager = (ActivityManager)getSystemService(Context.ACTIVITY_SERVICE);
        for (ActivityManager.RunningServiceInfo serviceInfo : manager.getRunningServices(Integer.MAX_VALUE)) {
            if (serviceClass.getName().equals(serviceInfo.service.getClassName())) {
                Log.i( "isMyServiceRunning: ","Running");
                return true;
            }
        }
        Log.i("isMyServiceRunning: ","Not Running");
        return false;
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
    }

    private void endCall() {
        CometChat.endCall(sessionID, new CometChat.CallbackListener<Call>() {
            @Override
            public void onSuccess(Call call) {
                finish();
            }

            @Override
            public void onError(CometChatException e) {
                Snackbar.make(CometChatStartCallActivity.this,
                        mainView, CometChatError.localized(e), 1000);
            }
        });
    }

    @Override
    public void onClick(View view) {

    }
}