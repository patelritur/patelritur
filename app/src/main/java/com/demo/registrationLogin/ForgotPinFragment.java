package com.demo.registrationLogin;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.databinding.DataBindingUtil;
import androidx.fragment.app.Fragment;

import com.demo.R;
import com.demo.databinding.FragmentForgotPinBinding;
import com.demo.registrationLogin.model.CommanRequestModel;
import com.demo.registrationLogin.model.HeaderModel;

public class ForgotPinFragment extends Fragment  {

    private FragmentForgotPinBinding fragmentForgotPinBinding;



    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        super.onCreateView(inflater, container, savedInstanceState);
        CommanRequestModel commanRequestModel = new CommanRequestModel();
        fragmentForgotPinBinding = DataBindingUtil.inflate(inflater,R.layout.fragment_forgot_pin,container,false);
        HeaderModel headerModel = getHeaderModelforVerificationActivity();
        fragmentForgotPinBinding.setHeadermodel(headerModel);
        fragmentForgotPinBinding.setHandlers(this);
        return fragmentForgotPinBinding.getRoot();    }

    public HeaderModel getHeaderModelforVerificationActivity() {
        HeaderModel headerModel = new HeaderModel();
        headerModel.setSecondImage(R.mipmap.forgot_password);
        headerModel.setTitle(getString(R.string.enter_verification_code));
        return headerModel;
    }

    private boolean fieldValidated(){
        //Utils.showToast(this,getString(R.string.validation_enter_mobile_number));
        return fragmentForgotPinBinding.edittextMobile.getText().toString() != null &&
                fragmentForgotPinBinding.edittextMobile.getText().toString().trim().length() != 0 &&
                fragmentForgotPinBinding.edittextMobile.getText().toString().trim().length() >= 10;
    }

    public void onClickSendOTP(View view){
        if(fieldValidated()){
            ((VerificationCodeActivity)getActivity()).sendOTP(4);

        }
    }

}
