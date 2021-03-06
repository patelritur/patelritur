package com.demo.registrationLogin;

import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.Bundle;
import android.view.View;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.databinding.DataBindingUtil;

import com.demo.BaseActivity;
import com.demo.R;
import com.demo.databinding.ActivityRegistrationBinding;
import com.demo.home.HomeActivity;
import com.demo.registrationLogin.model.HeaderModel;
import com.demo.registrationLogin.model.RegistrationRequestModel;
import com.demo.registrationLogin.model.RegistrationResponse;
import com.demo.utils.Constants;
import com.demo.utils.Permissionsutils;
import com.demo.utils.SharedPrefUtils;
import com.demo.utils.UriUtils;
import com.demo.utils.Utils;
import com.demo.webservice.ApiResponseListener;
import com.demo.webservice.RestClient;

import java.io.File;

import okhttp3.MediaType;
import okhttp3.MultipartBody;
import okhttp3.RequestBody;
import retrofit2.Call;

public class RegistrationActivity  extends BaseActivity implements ApiResponseListener {
    private RegistrationRequestModel registrationRequestModel;
    private ActivityRegistrationBinding activityRegistrationBinding;
    private Context context;
    private final int UPLOAD_DOC=2;
    private final int  SETUP_ACCOUT=1;
    private  SharedPrefUtils sharedPrefUtils;
    private UploadDocDialog uploadDocDialog;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        context = this;
        new RestClient(this);
        sharedPrefUtils = new SharedPrefUtils(this);
        activityRegistrationBinding = DataBindingUtil.setContentView(this, R.layout.activity_registration);
        registrationRequestModel = new RegistrationRequestModel();
        registrationRequestModel.setSocialLoginID(sharedPrefUtils.getStringData(this,"id")!=null ? sharedPrefUtils.getStringData(this,"id") :"");
        registrationRequestModel.setSocialLogin(sharedPrefUtils.getStringData(this,"type") !=null ? sharedPrefUtils.getStringData(this,"type") :"");
        registrationRequestModel.setFirstName(sharedPrefUtils.getStringData(this,"fname"));
        registrationRequestModel.setLastName(sharedPrefUtils.getStringData(this,"lname"));
        registrationRequestModel.setEmail(sharedPrefUtils.getStringData(this,"email"));
        activityRegistrationBinding.setRegistrationmodel(registrationRequestModel);
        if(getIntent().getExtras()!=null)
            registrationRequestModel.setMobileNumber(getIntent().getExtras().getString(getString(R.string.mobile_number)));
        HeaderModel headerModel = getHeaderModelforRegistrationActivity();
        activityRegistrationBinding.setHeadermodel(headerModel);
        activityRegistrationBinding.setHandlers(this);
    }

    private HeaderModel getHeaderModelforRegistrationActivity() {
        HeaderModel headerModel = new HeaderModel();
        headerModel.setSecondImage(R.mipmap.setup_account);
        headerModel.setTitle(getString(R.string.set_up_your_account));
        return headerModel;
    }
    public void onClickContinue(View view)
    {
        if(allFieldValidated()) {
            registrationRequestModel.setIsAcceptTNC(registrationRequestModel.getIsAcceptTNC());
            registrationRequestModel.setIsSendOfferEmail(registrationRequestModel.getIsSendOfferEmail());
            registrationRequestModel.setIsVaccinated(registrationRequestModel.getIsVaccinated());
            Call objectCall = RestClient.getApiService().newSignUpUser(registrationRequestModel);
            RestClient.makeApiRequest(this, objectCall, this, SETUP_ACCOUT, true);
        }

    }

    private boolean allFieldValidated() {
        if(registrationRequestModel.getFirstName()==null || registrationRequestModel.getFirstName().trim().length()==0)
        {
            Utils.showToast(context,getString(R.string.validation_enter_fname));
            activityRegistrationBinding.edittextFname.requestFocus();
            return false;
        }
        else if(registrationRequestModel.getLastName()==null || registrationRequestModel.getLastName().trim().length()==0)
        {
            Utils.showToast(context,getString(R.string.validation_enter_lname));
            activityRegistrationBinding.edittextLname.requestFocus();
            return false;

        }
        else if(registrationRequestModel.getEmail()==null || registrationRequestModel.getEmail().trim().length()==0)
        {
            Utils.showToast(context,getString(R.string.validation_enter_email));
            activityRegistrationBinding.edittextEmail.requestFocus();
            return false;
        }
        else if(!Utils.isValidEmail(registrationRequestModel.getEmail()))
        {
            Utils.showToast(context,getString(R.string.validation_enter_valid_email));
            activityRegistrationBinding.edittextEmail.requestFocus();
            return false;
        }
        else if(registrationRequestModel.getPin() == null || registrationRequestModel.getPin().trim().length()==0)
        {
            Utils.showToast(context,getString(R.string.validation_enter_pin));
            activityRegistrationBinding.edittextPin.requestFocus();
            return false;
//            activityRegistrationBinding.textinputLayoutPin.setError(getString(R.string.validation_enter_pin));
        }
        else if(registrationRequestModel.getPin().length()<4 )
        {
            Utils.showToast(context,getString(R.string.validation_enter_valid_pin));
            activityRegistrationBinding.edittextPin.requestFocus();
            return false;
//            activityRegistrationBinding.textinputLayoutPin.setError(getString(R.string.validation_enter_pin));
        }

        else if(!registrationRequestModel.isTnCAccepted())
        {
            Utils.showToast(context,"Please accept tnc");
            return false;
        }
        return true;
    }


    @Override
    public void onApiError(Call<Object> call, Object response, int reqCode) throws Exception {

    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (resultCode == RESULT_OK) {
            if(uploadDocDialog!=null)
            {
                uploadDocDialog.dismiss();
            }
            // Get the Uri of the selected file
            Uri uri = data.getData();
            String fullFilePath = UriUtils.getPathFromUri(this,uri);

            File destination = new File(fullFilePath);
            if(destination.exists())
            {
                uploadFileToServer(destination);
            }

        }


    }

    private void uploadFileToServer(File destination) {
        MultipartBody.Part filePart = MultipartBody.Part.createFormData("File", destination.getName(), RequestBody.create(MediaType.parse("image/*"), destination));
        Call<RegistrationResponse> call = RestClient.getApiService().uploadAttachment(filePart,  RequestBody.create( MediaType.parse("text/plain"), sharedPrefUtils.getStringData(this, Constants.USER_ID)+"") );
        RestClient.makeApiRequest(this, call, this, UPLOAD_DOC, true);
    }

    @Override
    public void onApiResponse(Call<Object> call, Object response, int reqCode) throws Exception {
        RegistrationResponse registrationResponse = (RegistrationResponse) response;
        if (reqCode == UPLOAD_DOC) {
            Utils.showToast(context, registrationResponse.getDescriptions());
            if(uploadDocDialog!=null)
            {
                uploadDocDialog.dismiss();
            }
            startHomeActivity();
        } else if (reqCode == SETUP_ACCOUT) {
            if (registrationResponse.getResponseCode().equalsIgnoreCase("200")) {
                Utils.showToast(context, registrationResponse.getDescriptions());
                if(registrationRequestModel.isVaccinated()) {
                    sharedPrefUtils.saveData( Constants.USER_ID, registrationResponse.getUserID());
                    sharedPrefUtils.saveData( Constants.FNAME, registrationRequestModel.getFirstName());
                    sharedPrefUtils.saveData( Constants.LNAME, registrationRequestModel.getLastName());
                    if (!Permissionsutils.CheckForStoragePermission(context)) {
                        Permissionsutils.askForStoragePermission(context);
                    } else {
                        showDialog();
                    }
                }
                else
                {
                    startHomeActivity();
                }
            }

        }
    }

    private void startHomeActivity() {
        Intent intent = new Intent(this, HomeActivity.class);
        intent.addFlags( Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_NEW_TASK);
        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
        NavigateToActivity(intent);
        finish();
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        if(grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED){
            //resume tasks needing this permission
            if(context!=null) {

                showDialog();
            }
        }
        else
        {
            startHomeActivity();
           // DialogUtils.showAlertInfo(context, "Please accept storage permission to upload vaccine certificate.");
        }
    }

    private void showDialog() {
         uploadDocDialog = new UploadDocDialog(this);
        uploadDocDialog.show();
    }

    @Override
    public void onClick(View view) {

    }
}
