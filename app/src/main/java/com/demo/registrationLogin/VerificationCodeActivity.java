package com.demo.registrationLogin;

import static com.demo.utils.Constants.MODULE_TYPE_REGISTER;
import static com.demo.utils.Constants.USER_TYPE;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.View;

import androidx.annotation.Nullable;
import androidx.databinding.DataBindingUtil;
import androidx.fragment.app.FragmentTransaction;

import com.cometchat.pro.core.CometChat;
import com.cometchat.pro.exceptions.CometChatException;
import com.cometchat.pro.models.User;
import com.demo.BaseActivity;
import com.demo.R;
import com.demo.databinding.EnterVerificationCodeBinding;
import com.demo.home.HomeActivity;
import com.demo.registrationLogin.model.CommanRequestModel;
import com.demo.registrationLogin.model.HeaderModel;
import com.demo.registrationLogin.model.LoginResponseModel;
import com.demo.registrationLogin.model.OtpResponseModel;
import com.demo.registrationLogin.model.PINResponseModel;
import com.demo.utils.Constants;
import com.demo.utils.NotificationUtils;
import com.demo.utils.SharedPrefUtils;
import com.demo.utils.Utils;
import com.demo.webservice.ApiResponseListener;
import com.demo.webservice.RestClient;

import java.util.Objects;

import retrofit2.Call;

public class VerificationCodeActivity extends BaseActivity implements ApiResponseListener {
    EnterVerificationCodeBinding enterVerificationCodeBinding;
    Context context;

    private boolean isShowEnterPIN;
    String mobileNumber,moduleName="forgetpassword";
    private final int PIN_VALIDATION=2;
    private final int OTP_VALIDATION=1;
    private final int SEND_OTP=3;
    private CommanRequestModel commanRequestModel;
    private  SharedPrefUtils sharedPrefUtils;
    private boolean isPinForgot;
    private CountDownTimer countDownTimer;
    private int isOTPResent=0;
    private boolean isShowPIN=true;

    @SuppressLint("SuspiciousIndentation")
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        context = this;
        enterVerificationCodeBinding = DataBindingUtil.setContentView(this, R.layout.enter_verification_code);
        sharedPrefUtils = new SharedPrefUtils(this);
        commanRequestModel=new CommanRequestModel();
        commanRequestModel.setSocialLoginId(sharedPrefUtils.getStringData(this,"id")!=null? sharedPrefUtils.getStringData(this,"id") : "");
        commanRequestModel.setSocialLogin(sharedPrefUtils.getStringData(this,"type")!=null ? sharedPrefUtils.getStringData(this,"type") :"");
        enterVerificationCodeBinding.setHandlers(this);
        setListeners();

        if(getIntent().getExtras()!=null) {
            mobileNumber = getIntent().getExtras().getString(getString(R.string.mobile_number));
            moduleName = getIntent().getExtras().getString(getString(R.string.module_name));
            enterVerificationCodeBinding.setHeadermodel(getHeaderModelforVerificationActivity());
            if(moduleName.equalsIgnoreCase(MODULE_TYPE_REGISTER))
                sendOTP(SEND_OTP);
            else
            {

              //  sendOTP(SEND_OTP);
            //    enterVerificationCodeBinding.setHeadermodel(getHeaderModelforPINVerificationActivity());
            }
        }
      /*  else
        {
            mobileNumber = sharedPrefUtils.getStringData(context,Constants.MOBILE_NO);
            enterVerificationCodeBinding.setHeadermodel(getHeaderModelforPINVerificationActivity());
        }*/

    }

    private void setListeners() {
        enterVerificationCodeBinding.otpEditBox1.addTextChangedListener(new GenericTextWatcher(enterVerificationCodeBinding.otpEditBox1));
        enterVerificationCodeBinding.otpEditBox2.addTextChangedListener(new GenericTextWatcher(enterVerificationCodeBinding.otpEditBox2));
        enterVerificationCodeBinding.otpEditBox3.addTextChangedListener(new GenericTextWatcher(enterVerificationCodeBinding.otpEditBox3));
        enterVerificationCodeBinding.otpEditBox4.addTextChangedListener(new GenericTextWatcher(enterVerificationCodeBinding.otpEditBox4));
        enterVerificationCodeBinding.otpEditBox4.setOnFocusChangeListener(new GenericFocusChangeListener(enterVerificationCodeBinding.otpEditBox4,this));
        enterVerificationCodeBinding.otpEditBox3.setOnFocusChangeListener(new GenericFocusChangeListener(enterVerificationCodeBinding.otpEditBox3,this));
        enterVerificationCodeBinding.otpEditBox2.setOnFocusChangeListener(new GenericFocusChangeListener(enterVerificationCodeBinding.otpEditBox2,this));
        enterVerificationCodeBinding.otpEditBox1.setOnFocusChangeListener(new GenericFocusChangeListener(enterVerificationCodeBinding.otpEditBox1,this));
        Utils.showKeyboard(this);
    }

    private HeaderModel getHeaderModelforVerificationActivity() {
        isShowEnterPIN=false;
        HeaderModel headerModel = new HeaderModel();
        headerModel.setSecondImage(R.drawable.ic_verification);
        enterVerificationCodeBinding.rootOtpLayout.setVisibility(View.VISIBLE);
        enterVerificationCodeBinding.timerOtp.setVisibility(View.VISIBLE);
        enterVerificationCodeBinding.enterPassword.setVisibility(View.GONE);
        enterVerificationCodeBinding.inputlayoutPassword.setVisibility(View.GONE);
        enterVerificationCodeBinding.didntReceiveOtp.setVisibility(View.VISIBLE);
        enterVerificationCodeBinding.commanHeaderLayout.tootlipImageview.setVisibility(View.GONE);
        headerModel.setTitle(getString(R.string.enter_verification_code));
        headerModel.setButtonText(getString(R.string.verify_proceed));
        headerModel.setBottomText(getString(R.string.resend_otp));
        return headerModel;
    }

   /* private HeaderModel getHeaderModelforPINVerificationActivity() {
        isShowEnterPIN = true;
        enterVerificationCodeBinding.rootOtpLayout.setVisibility(View.GONE);
        enterVerificationCodeBinding.timerOtp.setVisibility(View.GONE);
        enterVerificationCodeBinding.enterPassword.setVisibility(View.VISIBLE);
        enterVerificationCodeBinding.enterPassword.setDrawableClickListener(new onDrawableClickListener() {


            @Override
            public void onClick(@NonNull DrawablePosition drawablePosition) {
                if(drawablePosition==DrawablePosition.RIGHT) {
                    if (isShowPIN) {
                        isShowPIN = false;
                        enterVerificationCodeBinding.enterPassword.setTransformationMethod(null);
                        enterVerificationCodeBinding.enterPassword.setCompoundDrawablesWithIntrinsicBounds(0, 0, R.drawable.ic_outline_visibility_off_24, 0);
                    }
                    else{
                        isShowPIN = true;
                        enterVerificationCodeBinding.enterPassword.setTransformationMethod(new PasswordTransformationMethod());
                        enterVerificationCodeBinding.enterPassword.setCompoundDrawablesWithIntrinsicBounds(0, 0, R.drawable.ic_outline_visibility_24, 0);

                    }
                }
            }
        });
        enterVerificationCodeBinding.inputlayoutPassword.setVisibility(View.VISIBLE);
        HeaderModel headerModel = new HeaderModel();
        headerModel.setSecondImage(R.drawable.ic_verification);
        Drawable img = getResources().getDrawable(R.drawable.ic_baseline_info_24);
        img.setBounds(0, 0, 60, 60);
        enterVerificationCodeBinding.commanHeaderLayout.tootlipImageview.setVisibility(View.VISIBLE);
        enterVerificationCodeBinding.commanHeaderLayout.tootlipImageview.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                new SimpleTooltip.Builder(VerificationCodeActivity.this)
                        .anchorView(enterVerificationCodeBinding.commanHeaderLayout.tootlipImageview)
                        .text("Password")
                        .gravity(Gravity.BOTTOM)
                        .animated(false)
                        .transparentOverlay(true)
                        .build()
                        .show();
            }
        });
        headerModel.setTitle(getString(R.string.enter_pin));
        headerModel.setButtonText(getString(R.string.proceed));
        headerModel.setBottomText(getString(R.string.forgot_pin));
        enterVerificationCodeBinding.didntReceiveOtp.setVisibility(View.INVISIBLE);

        enterVerificationCodeBinding.enterPassword.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
                enterVerificationCodeBinding.inputlayoutPassword.setError(null);
            }

            @Override
            public void afterTextChanged(Editable editable) {

            }
        });
        return headerModel;
    }*/



    public void resendOtp(View view){
        if(!isShowEnterPIN){
            isOTPResent=0;
            enterVerificationCodeBinding.incorrectOtp.setVisibility(View.GONE);
            if (countDownTimer!=null)
            countDownTimer.cancel();
            sendOTP(SEND_OTP);
        } else {
            //Change in flow ask for OTP in case of PIN forgot
            isPinForgot = true;
            enterVerificationCodeBinding.setHeadermodel(getHeaderModelforVerificationActivity());
            sendOTP(SEND_OTP);

            /*FragmentTransaction ft = getSupportFragmentManager().beginTransaction();
            ft.add(R.id.forgot_pin_container, new ResetPinFragment());
            ft.commit();
            enterVerificationCodeBinding.forgotPinContainer.setVisibility(View.VISIBLE);*/
        }
    }
    public void onClickVerify(View view)
    {

            enterVerificationCodeBinding.inputlayoutPassword.setError(null);

//            if (moduleName.equalsIgnoreCase(MODULE_TYPE_REGISTER))
            {
                if (isOTPEntered()) {
                    if(countDownTimer!=null)
                        countDownTimer.cancel();
                    commanRequestModel.setMobile(mobileNumber);
                    commanRequestModel.setModuleName(moduleName);
                    final StringBuilder builder = new StringBuilder();
                    builder.append(enterVerificationCodeBinding.otpEditBox1.getText().toString().trim());
                    builder.append(enterVerificationCodeBinding.otpEditBox2.getText().toString().trim());
                    builder.append(enterVerificationCodeBinding.otpEditBox3.getText().toString().trim());
                    builder.append(enterVerificationCodeBinding.otpEditBox4.getText().toString().trim());
                    builder.trimToSize();
                    commanRequestModel.setOTP(builder.toString());

                    Call objectCall = RestClient.getApiService().otpValidation(commanRequestModel);
                    RestClient.makeApiRequest(this, objectCall, this, OTP_VALIDATION, true);
                }
            }


    }

    void sendOTP(int reqCode) {
        commanRequestModel.setMobile(mobileNumber);
        commanRequestModel.setUserType(USER_TYPE);
        commanRequestModel.setModuleName(moduleName);
        Call objectCall = RestClient.getApiService().sendOTP(commanRequestModel);
        RestClient.makeApiRequest(this, objectCall, this, reqCode, true);
    }
    private boolean isOTPEntered() {
        Objects.requireNonNull(enterVerificationCodeBinding.otpEditBox1.getText()).toString();
        Objects.requireNonNull(enterVerificationCodeBinding.otpEditBox2.getText()).toString();
        Objects.requireNonNull(enterVerificationCodeBinding.otpEditBox3.getText()).toString();
        Objects.requireNonNull(enterVerificationCodeBinding.otpEditBox4.getText()).toString();
        if(enterVerificationCodeBinding.otpEditBox1.getText().toString().trim().length() == 0
                ||enterVerificationCodeBinding.otpEditBox2.getText().toString().trim().length() == 0
                ||enterVerificationCodeBinding.otpEditBox3.getText().toString().trim().length() == 0
                ||enterVerificationCodeBinding.otpEditBox4.getText().toString().trim().length() == 0
        )
        {
            Utils.showToast(context,getString(R.string.validation_enter_otp));
            return false;
        }

        return true;
    }


    private void clearAll4Field() {
        enterVerificationCodeBinding.otpEditBox1.getText().clear();
        enterVerificationCodeBinding.otpEditBox2.getText().clear();
        enterVerificationCodeBinding.otpEditBox3.getText().clear();
        enterVerificationCodeBinding.otpEditBox4.getText().clear();
        enterVerificationCodeBinding.otpEditBox1.requestFocus();
    }





    private void changeFocusedPinColor() {
        if(enterVerificationCodeBinding.otpEditBox1.hasFocus() || enterVerificationCodeBinding.otpEditBox1.getText().length()==1)
            Utils.setFocusedPinBg(enterVerificationCodeBinding.otpEditBox1, context);
        else
            Utils.setNormalPinBg(enterVerificationCodeBinding.otpEditBox1, context);
        if(enterVerificationCodeBinding.otpEditBox2.hasFocus() || enterVerificationCodeBinding.otpEditBox2.getText().length()==1)
            Utils.setFocusedPinBg(enterVerificationCodeBinding.otpEditBox2, context);
        else
            Utils.setNormalPinBg(enterVerificationCodeBinding.otpEditBox2, context);
        if(enterVerificationCodeBinding.otpEditBox3.hasFocus() || enterVerificationCodeBinding.otpEditBox3.getText().length()==1)
            Utils.setFocusedPinBg(enterVerificationCodeBinding.otpEditBox3, context);
        else
            Utils.setNormalPinBg(enterVerificationCodeBinding.otpEditBox3, context);
        if(enterVerificationCodeBinding.otpEditBox4.hasFocus() || enterVerificationCodeBinding.otpEditBox4.getText().length()==1)
            Utils.setFocusedPinBg(enterVerificationCodeBinding.otpEditBox4, context);
        else
            Utils.setNormalPinBg(enterVerificationCodeBinding.otpEditBox4, context);

    }

    private void startRegistrationActivity() {
        Intent intent = new Intent(this,RegistrationActivity.class);
        intent.putExtra(getString(R.string.mobile_number),mobileNumber);
        NavigateToActivity(intent);
        finish();
    }

    @Override
    public void onApiError(Call<Object> call, Object response, int reqCode) throws Exception {

    }


    @Override
    public void onApiResponse(Call<Object> call, Object response, int reqCode) throws Exception {

        if(reqCode==OTP_VALIDATION) {
            LoginResponseModel loginResponseModel = (LoginResponseModel) response;
            if (loginResponseModel.getResponseCode().equalsIgnoreCase("200")) {
                if(isPinForgot){
                    FragmentTransaction ft = getSupportFragmentManager().beginTransaction();
                    ft.add(R.id.forgot_pin_container, new ResetPinFragment());
                    ft.commit();
                    enterVerificationCodeBinding.forgotPinContainer.setVisibility(View.VISIBLE);
                }
                else {
                    if (moduleName.equalsIgnoreCase(Constants.MODULE_TYPE_LOGIN)) {
//                        clearAll4Field();
//                        enterVerificationCodeBinding.setHeadermodel(getHeaderModelforPINVerificationActivity());
                            if(mobileNumber==null || mobileNumber.trim().length()==0)
                                mobileNumber = sharedPrefUtils.getStringData(context,Constants.MOBILE_NO);
                            final StringBuilder builder = new StringBuilder();
                            builder.append(enterVerificationCodeBinding.otpEditBox1.getText().toString().trim());
                            builder.append(enterVerificationCodeBinding.otpEditBox2.getText().toString().trim());
                            builder.append(enterVerificationCodeBinding.otpEditBox3.getText().toString().trim());
                            builder.append(enterVerificationCodeBinding.otpEditBox4.getText().toString().trim());
                            builder.trimToSize();
                            commanRequestModel.setOTP(builder.toString());
                            commanRequestModel.setMobile(mobileNumber);
                            commanRequestModel.setUserType(Constants.USER_TYPE);
                            commanRequestModel.setPin("0000");
                            commanRequestModel.setMobileIdentity(Utils.getDeviceUniqueID(this));

                            Call objectCall = RestClient.getApiService().userloginbypin(commanRequestModel);
                            RestClient.makeApiRequest(this, objectCall, this, PIN_VALIDATION, true);

                    } else {

                        startRegistrationActivity();
                    }
                }
            } else if (loginResponseModel.getResponseCode().equalsIgnoreCase("300")) {
                Utils.showToast(context, loginResponseModel.getDescriptions());
                clearAll4Field();
                enterVerificationCodeBinding.incorrectOtp.setVisibility(View.VISIBLE);
                enterVerificationCodeBinding.didntReceiveOtp.setVisibility(View.VISIBLE);
                enterVerificationCodeBinding.getHeadermodel().setBottomText(getString(R.string.resend_otp));
                enterVerificationCodeBinding.setHeadermodel(enterVerificationCodeBinding.getHeadermodel());
            }
        }
        else if(reqCode==PIN_VALIDATION){
            PINResponseModel pinResponseModel = (PINResponseModel) response;
            if (pinResponseModel.getResponseCode().equalsIgnoreCase("200")) {
                NotificationUtils.setUpFCMNotifiction(VerificationCodeActivity.this,pinResponseModel.getUsersInfo().getUserID()+"","Add");
                User user = new User();
                user.setName(pinResponseModel.getUsersInfo().getFirstName());
                user.setUid(pinResponseModel.getUsersInfo().getUserID()+"");
                CometChat.createUser(user, Constants.AUTH_KEY, new CometChat.CallbackListener<User>() {
                    @Override
                    public void onSuccess(User user) {
                        if (CometChat.getLoggedInUser() == null)
                            login(user);

                    }

                    @Override
                    public void onError(CometChatException e) {
                        if (CometChat.getLoggedInUser() == null)
                            login(user);


                    }
                });
                sharedPrefUtils.saveData( Constants.USER_ID,pinResponseModel.getUsersInfo().getUserID()+"");
                sharedPrefUtils.saveData(Constants.MOBILE_NO,mobileNumber);
                sharedPrefUtils.saveData(Constants.FNAME,pinResponseModel.getUsersInfo().getFirstName());
                sharedPrefUtils.saveData(Constants.LNAME,pinResponseModel.getUsersInfo().getLastName());
                sharedPrefUtils.saveData(Constants.EMAIL,pinResponseModel.getUsersInfo().getEmail());
                sharedPrefUtils.saveData(Constants.IMAGE,pinResponseModel.getUsersInfo().getUserProfileImagel());
                sharedPrefUtils.saveData(Constants.ISVACCINATED,pinResponseModel.getUsersInfo().getIsVaccinated());
                sharedPrefUtils.saveData(Constants.ADDRESS,pinResponseModel.getUsersInfo().getAddress());
                sharedPrefUtils.saveData(Constants.IsDLUploadStatus,pinResponseModel.getUsersInfo().getIsDLUploadStatus());

                Intent intent = new Intent(this, HomeActivity.class);
                intent.addFlags( Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_NEW_TASK);
                intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
                if(countDownTimer!=null)
                    countDownTimer.cancel();
                NavigateToActivity(intent);
                finish();
            }
            else if (pinResponseModel.getResponseCode().equalsIgnoreCase("100")) {
                Utils.showToast(context, pinResponseModel.getDescriptions());
            }
        }
        else if(reqCode==SEND_OTP)
        {
            OtpResponseModel otpResponseModel = (OtpResponseModel) response;
             startOTPTimer();
            if(otpResponseModel.getResponseCode().equalsIgnoreCase("103"))
                Utils.showToast(context,otpResponseModel.getDescriptions());

        }

    }

    private void startOTPTimer() {
        isOTPResent++;
        countDownTimer =    new CountDownTimer(150000, 1000) {

            public void onTick(long millisUntilFinished) {
                long minutes = (millisUntilFinished / 1000)  / 60;
                int seconds = (int)((millisUntilFinished / 1000) % 60);
                enterVerificationCodeBinding.timerOtp.setText("OTP valid for: " + minutes+":"+seconds);
                //here you can have your logic to set text to edittext
            }

            public void onFinish() {
                enterVerificationCodeBinding.timerOtp.setText("OTP Expired!");
                if(isOTPResent<3)
                 sendOTP(SEND_OTP);
            }

        }.start();
    }

    private void login(User user) {
        CometChat.login(user.getUid(), Constants.AUTH_KEY, new CometChat.CallbackListener<User>() {

            @Override
            public void onSuccess(User user) {
                Log.e("TAG", "Login Successful : " + user.toString());

                //    MyFirebaseMessagingService.subscribeUserNotification(user.getUid());
            }

            @Override
            public void onError(CometChatException e) {
                Log.e("TAG", "Login failed with exception: " + e.getMessage());
            }
        });
    }

    @Override
    public void onClick(View view) {


    }

    class GenericTextWatcher implements TextWatcher {

        private final View view;

        GenericTextWatcher(View view) {
            this.view = view;
        }

        @Override
        public void beforeTextChanged(CharSequence s, int start, int count, int after) {

        }

        @Override
        public void onTextChanged(CharSequence s, int start, int before, int count) {

        }

        @SuppressLint("NonConstantResourceId")
        @Override
        public void afterTextChanged(Editable editable) {
            String text = editable.toString();
            switch (view.getId()) {
                case R.id.otp_edit_box1:

                    if (text.length() == 1) {
                        enterVerificationCodeBinding.otpEditBox2.requestFocus();
                        Utils.setFocusedPinBg(enterVerificationCodeBinding.otpEditBox1, context);
                    }
                    break;
                case R.id.otp_edit_box2:
                    if (text.length() == 1) {
                        enterVerificationCodeBinding.otpEditBox3.requestFocus();
                        Utils.setFocusedPinBg(enterVerificationCodeBinding.otpEditBox2, context);
                    } else {

                        enterVerificationCodeBinding.otpEditBox1.requestFocus();

                    }
                    break;
                case R.id.otp_edit_box3:
                    if (text.length() == 1) {
                        enterVerificationCodeBinding.otpEditBox4.requestFocus();
                        Utils.setFocusedPinBg(enterVerificationCodeBinding.otpEditBox3, context);

                    } else {

                        enterVerificationCodeBinding.otpEditBox2.requestFocus();
                    }
                    break;
                case R.id.otp_edit_box4:
                    if (text.length() == 1) {
                        Utils.setFocusedPinBg(enterVerificationCodeBinding.otpEditBox4, context);
                        Utils.hideSoftKeyboard(view);
                        //  fragNewVerifyOtpBinding.edtOtp5.requestFocus();
                    } else {
                        enterVerificationCodeBinding.otpEditBox3.requestFocus();

                    }
                    break;

                default:

                    break;

            }
            changeFocusedPinColor();
        }

    }

}
