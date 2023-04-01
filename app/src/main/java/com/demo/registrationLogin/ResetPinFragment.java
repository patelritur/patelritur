package com.demo.registrationLogin;

import android.content.Intent;
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
import com.demo.databinding.FragmentResetPinBinding;
import com.demo.registrationLogin.model.CommanRequestModel;
import com.demo.registrationLogin.model.HeaderModel;
import com.demo.registrationLogin.model.PINResponseModel;
import com.demo.utils.Constants;
import com.demo.utils.SharedPrefUtils;
import com.demo.utils.Utils;
import com.demo.webservice.ApiResponseListener;
import com.demo.webservice.RestClient;
import com.mindorks.editdrawabletext.DrawablePosition;
import com.mindorks.editdrawabletext.onDrawableClickListener;

import retrofit2.Call;

public class ResetPinFragment extends Fragment implements ApiResponseListener {
    FragmentResetPinBinding fragmentResetPinBinding;
    private boolean isPinRest=false;
    private boolean isShowPIN,isShowConfirmPIN = true;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        super.onCreateView(inflater, container, savedInstanceState);
        fragmentResetPinBinding = DataBindingUtil.inflate(inflater, R.layout.fragment_reset_pin,container,false);
        HeaderModel headerModel = getHeaderModelforResetPin();
        fragmentResetPinBinding.setHeadermodel(headerModel);
        fragmentResetPinBinding.setHandlers(this);
        return fragmentResetPinBinding.getRoot();    }

    private HeaderModel getHeaderModelforResetPin() {
        HeaderModel headerModel = new HeaderModel();
        headerModel.setSecondImage(R.mipmap.reset_password);
        headerModel.setTitle(getString(R.string.reset_password));
        headerModel.setButtonText(getString(R.string.continue_));

        fragmentResetPinBinding.newPassword.setDrawableClickListener(new onDrawableClickListener() {


            @Override
            public void onClick(@NonNull DrawablePosition drawablePosition) {
                if(drawablePosition==DrawablePosition.RIGHT) {
                    if (isShowPIN) {
                        isShowPIN = false;
                        fragmentResetPinBinding.newPassword.setTransformationMethod(null);
                        fragmentResetPinBinding.newPassword.setCompoundDrawablesWithIntrinsicBounds(0, 0, R.drawable.ic_outline_visibility_off_24, 0);
                    }
                    else{
                        isShowPIN = true;
                        fragmentResetPinBinding.newPassword.setTransformationMethod(new PasswordTransformationMethod());
                        fragmentResetPinBinding.newPassword.setCompoundDrawablesWithIntrinsicBounds(0, 0, R.drawable.ic_outline_visibility_24, 0);

                    }
                }
            }
        });

        fragmentResetPinBinding.confirmPassword.setDrawableClickListener(new onDrawableClickListener() {


            @Override
            public void onClick(@NonNull DrawablePosition drawablePosition) {
                if(drawablePosition==DrawablePosition.RIGHT) {
                    if (isShowConfirmPIN) {
                        isShowConfirmPIN = false;
                        fragmentResetPinBinding.confirmPassword.setTransformationMethod(null);
                        fragmentResetPinBinding.confirmPassword.setCompoundDrawablesWithIntrinsicBounds(0, 0, R.drawable.ic_outline_visibility_off_24, 0);
                    }
                    else{
                        isShowConfirmPIN = true;
                        fragmentResetPinBinding.confirmPassword.setTransformationMethod(new PasswordTransformationMethod());
                        fragmentResetPinBinding.confirmPassword.setCompoundDrawablesWithIntrinsicBounds(0, 0, R.drawable.ic_outline_visibility_24, 0);

                    }
                }
            }
        });


        return headerModel;
    }
    private void updateUIForSuccessPinChange() {
        isPinRest=true;
        fragmentResetPinBinding.llPassword.setVisibility(View.GONE);
        HeaderModel headerModel = new HeaderModel();
        headerModel.setSecondImage(R.mipmap.congratulations);
        headerModel.setTitle(null);
        fragmentResetPinBinding.secondText.setText(getString(R.string.your_password_has_been_reset));
        headerModel.setButtonText(getString(R.string.login));
        fragmentResetPinBinding.setHeadermodel(headerModel);
    }
    public void onClickContinue(View view)
    {
        if(!isPinRest) {
            fragmentResetPinBinding.inputlayoutPwd.setError(null);
            fragmentResetPinBinding.inputlayoutConfirmpwd.setError(null);
            if (bothFieldValidated()) {
                CommanRequestModel commanRequestModel = new CommanRequestModel();
                commanRequestModel.setMobile(new SharedPrefUtils(getActivity()).getStringData(getActivity(), Constants.MOBILE_NO));
                commanRequestModel.setUserType(Constants.USER_TYPE);
                commanRequestModel.setPin(fragmentResetPinBinding.newPassword.getText().toString());

                Call objectCall = RestClient.getApiService().updatePin(commanRequestModel);
                RestClient.makeApiRequest(getActivity(), objectCall, this, 1, true);

            }
        }
        else
        {
            Intent intent =new Intent(getActivity(), LoginActivity.class);
            startActivity(intent);
            getActivity().finish();
        }
    }

    private boolean bothFieldValidated() {
        if(fragmentResetPinBinding.newPassword.getText().toString()==null )
        {
            fragmentResetPinBinding.inputlayoutPwd.setError(getString(R.string.validation_new_password));
            return false;
        }
        else if( fragmentResetPinBinding.confirmPassword.getText()==null)
        {
            fragmentResetPinBinding.confirmPassword.setError(getString(R.string.validation_confirm_password));
            return false;
        }
        else if( fragmentResetPinBinding.confirmPassword.getText().toString().trim().length()<4 )
        {
            fragmentResetPinBinding.confirmPassword.setError(getString(R.string.validation_password_length));

            return false;
        }
        else if( fragmentResetPinBinding.newPassword.getText().toString().trim().length()<4){
            fragmentResetPinBinding.newPassword.setError(getString(R.string.validation_password_length));
            return false;
        }
        else if(! fragmentResetPinBinding.confirmPassword.getText().toString().equalsIgnoreCase(fragmentResetPinBinding.newPassword.getText().toString()))
        {
            Utils.showToast(getActivity(),getString(R.string.validation_same_password));
            fragmentResetPinBinding.confirmPassword.setError(getString(R.string.validation_same_password));

            return false;
        }
        return true;
    }

    @Override
    public void onApiResponse(Call<Object> call, Object response, int reqCode) throws Exception {
        PINResponseModel pinResponseModel = (PINResponseModel) response;
        if(pinResponseModel.getResponseCode().equalsIgnoreCase("108"))
        {
            Utils.showToast(getActivity(),pinResponseModel.getDescriptions());
            updateUIForSuccessPinChange();
        }


    }



    @Override
    public void onApiError(Call<Object> call, Object response, int reqCode) throws Exception {

    }
}
