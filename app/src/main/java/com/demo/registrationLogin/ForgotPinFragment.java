/*
package com.demo.registrationLogin;

import android.os.Bundle;
import android.text.method.PasswordTransformationMethod;
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
import com.mindorks.editdrawabletext.DrawablePosition;
import com.mindorks.editdrawabletext.onDrawableClickListener;

public class ForgotPinFragment extends Fragment  {

    private FragmentForgotPinBinding fragmentForgotPinBinding;
    private boolean isShowPIN=true;


    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        super.onCreateView(inflater, container, savedInstanceState);
        fragmentForgotPinBinding = DataBindingUtil.inflate(inflater,R.layout.fragment_forgot_pin,container,false);
        HeaderModel headerModel = getHeaderModelforVerificationActivity();
        fragmentForgotPinBinding.setHeadermodel(headerModel);
        fragmentForgotPinBinding.setHandlers(this);
        return fragmentForgotPinBinding.getRoot();    }

    public HeaderModel getHeaderModelforVerificationActivity() {
        HeaderModel headerModel = new HeaderModel();
        headerModel.setSecondImage(R.mipmap.forgot_password);
        headerModel.setTitle(getString(R.string.enter_verification_code));

        fragmentForgotPinBinding.edittextMobile.setDrawableClickListener(new onDrawableClickListener() {


            @Override
            public void onClick(@NonNull DrawablePosition drawablePosition) {
                if(drawablePosition==DrawablePosition.RIGHT) {
                    if (isShowPIN) {
                        isShowPIN = false;
                        fragmentForgotPinBinding.edittextMobile.setTransformationMethod(null);
                        fragmentForgotPinBinding.edittextMobile.setCompoundDrawablesWithIntrinsicBounds(0, 0, R.drawable.ic_outline_visibility_off_24, 0);
                    }
                    else{
                        isShowPIN = true;
                        fragmentForgotPinBinding.edittextMobile.setTransformationMethod(new PasswordTransformationMethod());
                        fragmentForgotPinBinding.edittextMobile.setCompoundDrawablesWithIntrinsicBounds(0, 0, R.drawable.ic_outline_visibility_24, 0);

                    }
                }
            }
        });
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
*/
