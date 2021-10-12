package com.demo.registrationLogin;

import static com.demo.utils.Constants.MODULE_TYPE_REGISTER;
import static com.demo.utils.Constants.USER_TYPE;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.View;

import androidx.annotation.Nullable;
import androidx.databinding.DataBindingUtil;
import androidx.fragment.app.FragmentTransaction;

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
    private final int SEND_OTP_FORGOT=4;
    private CommanRequestModel commanRequestModel;
    private  SharedPrefUtils sharedPrefUtils;

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
        }
        else
        {
            mobileNumber = sharedPrefUtils.getStringData(context,Constants.MOBILE_NO);
            enterVerificationCodeBinding.setHeadermodel(getHeaderModelforPINVerificationActivity());
        }

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
        HeaderModel headerModel = new HeaderModel();
        headerModel.setSecondImage(R.mipmap.enter_verification_code);
        enterVerificationCodeBinding.enterPassword.setVisibility(View.GONE);
        headerModel.setTitle(getString(R.string.enter_verification_code));
        headerModel.setButtonText(getString(R.string.verify_proceed));
        headerModel.setBottomText(getString(R.string.resend_otp));
        return headerModel;
    }

    private HeaderModel getHeaderModelforPINVerificationActivity() {
        isShowEnterPIN = true;
        enterVerificationCodeBinding.rootOtpLayout.setVisibility(View.GONE);
        enterVerificationCodeBinding.enterPassword.setVisibility(View.VISIBLE);
        HeaderModel headerModel = new HeaderModel();
        headerModel.setSecondImage(R.mipmap.enter_verification_code);
        headerModel.setTitle(getString(R.string.enter_pin));
        headerModel.setButtonText(getString(R.string.proceed));
        headerModel.setBottomText(getString(R.string.forgot_pin));
        enterVerificationCodeBinding.didntReceiveOtp.setVisibility(View.INVISIBLE);
        return headerModel;
    }



    public void resendOtp(View view){
        if(!isShowEnterPIN){
            sendOTP(SEND_OTP);
        } else {
            FragmentTransaction ft = getSupportFragmentManager().beginTransaction();
            ft.add(R.id.forgot_pin_container, new ResetPinFragment());
            ft.commit();
            enterVerificationCodeBinding.forgotPinContainer.setVisibility(View.VISIBLE);
        }
    }
    public void onClickVerify(View view)
    {
        {
            if (!isShowEnterPIN) {
                if (isOTPEntered()) {
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
            } else {
                if(enterVerificationCodeBinding.enterPassword.getText()!=null) {
                    commanRequestModel.setMobile(mobileNumber);
                    commanRequestModel.setUserType(Constants.USER_TYPE);
                    commanRequestModel.setPin(enterVerificationCodeBinding.enterPassword.getText().toString());
                    commanRequestModel.setMobileIdentity(Utils.getDeviceUniqueID(this));

                    Call objectCall = RestClient.getApiService().userloginbypin(commanRequestModel);
                    RestClient.makeApiRequest(this, objectCall, this, PIN_VALIDATION, true);
                }
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
                if (moduleName.equalsIgnoreCase(Constants.MODULE_TYPE_LOGIN)) {
                    clearAll4Field();
                    enterVerificationCodeBinding.setHeadermodel(getHeaderModelforPINVerificationActivity());

                } else {

                    startRegistrationActivity();
                }
            } else if (loginResponseModel.getResponseCode().equalsIgnoreCase("300")) {
                Utils.showToast(context, loginResponseModel.getDescriptions());
                clearAll4Field();
                enterVerificationCodeBinding.didntReceiveOtp.setVisibility(View.GONE);
                enterVerificationCodeBinding.getHeadermodel().setBottomText(getString(R.string.otp_entered_is_incorrect));
                enterVerificationCodeBinding.setHeadermodel(enterVerificationCodeBinding.getHeadermodel());
            }
        }
        else if(reqCode==PIN_VALIDATION){
            PINResponseModel pinResponseModel = (PINResponseModel) response;
            if (pinResponseModel.getResponseCode().equalsIgnoreCase("200")) {
                sharedPrefUtils.saveData( Constants.USER_ID,pinResponseModel.getUsersInfo().getUserID()+"");
                sharedPrefUtils.saveData(Constants.MOBILE_NO,mobileNumber);
                sharedPrefUtils.saveData(Constants.FNAME,pinResponseModel.getUsersInfo().getFirstName());
                sharedPrefUtils.saveData(Constants.LNAME,pinResponseModel.getUsersInfo().getLastName());
                sharedPrefUtils.saveData(Constants.EMAIL,pinResponseModel.getUsersInfo().getEmail());

                Intent intent = new Intent(this, HomeActivity.class);
                intent.addFlags( Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_NEW_TASK);
                intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);

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
            if(otpResponseModel.getResponseCode().equalsIgnoreCase("103"))
                Utils.showToast(context,otpResponseModel.getDescriptions());
        }
        else if(reqCode==SEND_OTP_FORGOT)
        {

        }
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
