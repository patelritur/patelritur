package com.demo.registrationLogin;

import static com.demo.utils.Constants.MODULE_TYPE_LOGIN;
import static com.demo.utils.Constants.MODULE_TYPE_REGISTER;

import android.content.Intent;
import android.graphics.Paint;
import android.os.Bundle;
import android.util.Log;
import android.view.View;

import androidx.annotation.Nullable;
import androidx.databinding.DataBindingUtil;

import com.demo.BaseActivity;
import com.demo.R;
import com.demo.databinding.ActivityLoginBinding;
import com.demo.registrationLogin.model.CommanRequestModel;
import com.demo.registrationLogin.model.LoginResponseModel;
import com.demo.utils.Constants;
import com.demo.utils.PrintLog;
import com.demo.utils.SharedPrefUtils;
import com.demo.utils.Utils;
import com.demo.webservice.ApiResponseListener;
import com.demo.webservice.RestClient;
import com.facebook.CallbackManager;
import com.facebook.FacebookCallback;
import com.facebook.FacebookException;
import com.facebook.GraphRequest;
import com.facebook.GraphResponse;
import com.facebook.login.LoginManager;
import com.facebook.login.LoginResult;
import com.google.android.gms.auth.api.signin.GoogleSignIn;
import com.google.android.gms.auth.api.signin.GoogleSignInAccount;
import com.google.android.gms.auth.api.signin.GoogleSignInClient;
import com.google.android.gms.auth.api.signin.GoogleSignInOptions;
import com.google.android.gms.common.api.ApiException;
import com.google.android.gms.tasks.Task;

import org.json.JSONObject;

import java.util.Arrays;

import retrofit2.Call;

public class LoginActivity extends BaseActivity implements ApiResponseListener {
    ActivityLoginBinding activityLoginBinding;
    private final int RC_SIGN_IN=1;
    private CommanRequestModel commanRequestModel;
    SharedPrefUtils sharedPrefUtils;
    private CallbackManager callbackManager;
    private final int MOBILE_REQ_CODE=1;
    private final int SOCIAL_REQ_CODE=2;


    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        sharedPrefUtils = new SharedPrefUtils(this);
        activityLoginBinding = DataBindingUtil.setContentView(this, R.layout.activity_login);
        commanRequestModel = new CommanRequestModel();
        commanRequestModel.setSocialLogin("");
        commanRequestModel.setSocialLoginId("");
        commanRequestModel.setMobileIdentity(Utils.getDeviceUniqueID(this));
        commanRequestModel.setUserType("1");
        activityLoginBinding.setLoginrequestmodel(commanRequestModel);
        activityLoginBinding.setHandlers(this);
        activityLoginBinding.setIsregister(true);
        activityLoginBinding.underlineText.setPaintFlags(activityLoginBinding.underlineText.getPaintFlags() | Paint.UNDERLINE_TEXT_FLAG);
        callbackManager = CallbackManager.Factory.create();

    }



    public void onClickSignInSignUp(View view)
    {
        activityLoginBinding.setIsregister(!activityLoginBinding.getIsregister());
        activityLoginBinding.executePendingBindings();
    }
    public void onClickSignup(View view)
    {
        if(allFieldValidated())
        {
            callLoginApi(MOBILE_REQ_CODE);
        }

    }

    private boolean allFieldValidated() {
        if(activityLoginBinding.edittextMobile.getText().toString()== null || activityLoginBinding.edittextMobile.getText().toString().trim().length()==0 ||activityLoginBinding.edittextMobile.getText().toString().trim().length()<10 )
        {
            Utils.showToast(LoginActivity.this,getString(R.string.validation_enter_mobile_number));
            return false;

        }
        return true;
    }

    public void onFacebookClick(View view)
    {

        LoginManager.getInstance().logInWithReadPermissions(this, Arrays.asList("public_profile","email"));

        LoginManager.getInstance().registerCallback(callbackManager,
                new FacebookCallback<LoginResult>() {
                    @Override
                    public void onSuccess(LoginResult loginResult) {
                        // App code
                        PrintLog.v("email", String.valueOf(loginResult.getAccessToken().getUserId()));
                        GraphRequest request = GraphRequest.newMeRequest(
                                loginResult.getAccessToken(),
                                new GraphRequest.GraphJSONObjectCallback() {
                                    @Override
                                    public void onCompleted(JSONObject object, GraphResponse response) {
                                        // Insert your code here
                                        PrintLog.v("",""+object. optString("email"));
                                        extracted(object. optString("id"),object. optString("first_name"),object. optString("last_name"),object. optString("email"),"facebook");

                                    }
                                });

                        Bundle parameters = new Bundle();
                        parameters.putString("fields", "id,last_name,first_name,email");
                        request.setParameters(parameters);
                        request.executeAsync();
                    }

                    @Override
                    public void onCancel() {
                        // App code
                    }

                    @Override
                    public void onError(FacebookException exception) {
                        // App code
                        PrintLog.v("email", exception.getMessage());
                    }
                });
    }
    public void onGoogleSignIn(View view)
    {
        GoogleSignInOptions gso = new GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
                .requestEmail()
                .build();
       GoogleSignInClient mGoogleSignInClient = GoogleSignIn.getClient(this, gso);


        Intent signInIntent = mGoogleSignInClient.getSignInIntent();
        startActivityForResult(signInIntent, RC_SIGN_IN);
    }
    private void handleSignInResult(Task<GoogleSignInAccount> completedTask) {
        try {
            GoogleSignInAccount account = completedTask.getResult(ApiException.class);
            extracted(account.getId(),account.getDisplayName(),account.getFamilyName(),account.getEmail(),"google");
            // Signed in successfully, show authenticated UI.
        } catch (ApiException e) {
            // The ApiException status code indicates the detailed failure reason.
            // Please refer to the GoogleSignInStatusCodes class reference for more information.
            Utils.showToast(this,"Error occured!please try again!");
            Log.w("TAG", "signInResult:failed code=" + e.getStatusCode());
        }
    }

    private void extracted(String id, String fName,String lName,String email,String type) {
        commanRequestModel.setSocialLogin(type);
        commanRequestModel.setSocialLoginId(id);
        commanRequestModel.setMobile("");
        sharedPrefUtils.saveData(Constants.EMAIL, email);
        sharedPrefUtils.saveData(Constants.FNAME,fName);
        sharedPrefUtils.saveData(Constants.LNAME, lName);
        sharedPrefUtils.saveData(Constants.ID, id);
        sharedPrefUtils.saveData(Constants.SOCIAL_TYPE,type);
        callLoginApi(SOCIAL_REQ_CODE);
    }

    private void callLoginApi(int REQ_CODE) {
        Call objectCall = RestClient.getApiService().loginUser(commanRequestModel);
        RestClient.makeApiRequest(this, objectCall, this, REQ_CODE, true);
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        callbackManager.onActivityResult(requestCode, resultCode, data);
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == RC_SIGN_IN) {
            // The Task returned from this call is always completed, no need to attach
            // a listener.
            Task<GoogleSignInAccount> task = GoogleSignIn.getSignedInAccountFromIntent(data);
            handleSignInResult(task);
        }

    }

    @Override
    public void onApiResponse(Call<Object> call, Object response, int reqCode) throws Exception {
        //{"ResponseCode":"103","Descriptions":"OTP has successfully sent.","SessionID":"141","Mobile":"1111111112"} for success response with user exists
        LoginResponseModel loginResponseModel = (LoginResponseModel) response;

        if(reqCode==MOBILE_REQ_CODE) {
            sharedPrefUtils.saveData(Constants.TNC,loginResponseModel.getTNCLink());
            PrintLog.v("",""+loginResponseModel.toString());
            if (loginResponseModel.getResponseCode().equalsIgnoreCase("102")) {
                Utils.showToast(this,loginResponseModel.getDescriptions());
                navigateToVerificationCodeActivity(MODULE_TYPE_REGISTER, commanRequestModel.getMobile());
            } else {
                if(activityLoginBinding.getIsregister())
                 Utils.showToast(this,getString(R.string.already_user_pin_msg));
                navigateToVerificationCodeActivity(MODULE_TYPE_LOGIN,commanRequestModel.getMobile());
            }
        }
        else if(reqCode==SOCIAL_REQ_CODE)
        {
            sharedPrefUtils.saveData(Constants.TNC,loginResponseModel.getTNCLink());
            if (loginResponseModel.getResponseCode().equalsIgnoreCase("102")) {
                Utils.showToast(this,loginResponseModel.getDescriptions());
                onClickSignup(null);
                updateUI();
            } else {

                navigateToVerificationCodeActivity(MODULE_TYPE_LOGIN,loginResponseModel.getMobile());
            }
        }
    }


    private void updateUI() {
        activityLoginBinding.llSocial.setVisibility(View.GONE);
        activityLoginBinding.textTitle.setText(getString(R.string.please_enter_mobile_number));
    }


    private void navigateToVerificationCodeActivity(String moduleName, String mobile) {


        Intent intent = new Intent(this, VerificationCodeActivity.class);
        intent.putExtra(getString(R.string.mobile_number),mobile);
        intent.putExtra(getString(R.string.module_name),moduleName);

        sharedPrefUtils.saveData( Constants.MOBILE_NO,activityLoginBinding.edittextMobile.getText().toString());
        NavigateToActivity(intent);
        finish();

    }



    @Override
    public void onApiError(Call<Object> call, Object response, int reqCode) throws Exception {

    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        finish();
    }

    @Override
    public void onClick(View view) {

    }
}
