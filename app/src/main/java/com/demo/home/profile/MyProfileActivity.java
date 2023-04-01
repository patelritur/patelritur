package com.demo.home.profile;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.Dialog;
import android.app.DownloadManager;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.provider.MediaStore;
import android.view.LayoutInflater;
import android.view.View;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.widget.AppCompatEditText;
import androidx.core.content.ContextCompat;
import androidx.core.content.FileProvider;
import androidx.core.graphics.drawable.DrawableCompat;
import androidx.databinding.DataBindingUtil;

import com.bumptech.glide.Glide;
import com.bumptech.glide.request.RequestOptions;
import com.demo.BaseActivity;
import com.demo.R;
import com.demo.databinding.DialogUploadDlBinding;
import com.demo.databinding.FragmentCustomerProfileBinding;
import com.demo.home.HomeActivity;
import com.demo.home.model.DrivingLicenseDataResponse;
import com.demo.home.model.ProfileModel;
import com.demo.registrationLogin.model.RegistrationResponse;
import com.demo.utils.Constants;
import com.demo.utils.Permissionsutils;
import com.demo.utils.SharedPrefUtils;
import com.demo.utils.UriUtils;
import com.demo.utils.Utils;
import com.demo.webservice.ApiResponseListener;
import com.demo.webservice.RestClient;

import java.io.File;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Date;

import okhttp3.MediaType;
import okhttp3.MultipartBody;
import okhttp3.RequestBody;
import retrofit2.Call;

public class MyProfileActivity extends BaseActivity implements View.OnClickListener, ApiResponseListener {
    private static final int PICK_IMAGE_CAMERA = 1;
    private static final int PICK_IMAGE_GALLERY = 2;
    private static final int UPLOAD_DOC = 2;
    private static final int UPLOAD_DRIVING_LICENSE = 3;
    private static final int UPDATE_DATA = 1;
    private static final int DRIVING_LICENSE_DATA = 4;
    private FragmentCustomerProfileBinding fragmentCustomerProfileBinding;
    private SharedPrefUtils sharedPrefUtils;
    private ProfileModel profileModel;
    private String fullFilePath;
    private DialogUploadDlBinding dialogUploadDlBinding;
    private Dialog uploadDlDialog;
    private String dlDocument;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        sharedPrefUtils = new SharedPrefUtils(this);
        profileModel = new ProfileModel();

        fragmentCustomerProfileBinding = DataBindingUtil.setContentView(this, R.layout.fragment_customer_profile);

        profileModel.setEmail(sharedPrefUtils.getStringData(Constants.EMAIL));
        profileModel.setMobile(sharedPrefUtils.getStringData(Constants.MOBILE_NO));
        profileModel.setFirstName(sharedPrefUtils.getStringData(Constants.FNAME).trim());
        profileModel.setLastName(sharedPrefUtils.getStringData(Constants.LNAME).trim());
        profileModel.setUserID(sharedPrefUtils.getStringData(Constants.USER_ID));
        profileModel.setAddress(sharedPrefUtils.getStringData(Constants.ADDRESS));
        if(sharedPrefUtils.getStringData(Constants.IMAGE)!=null){
            profileModel.setImage(sharedPrefUtils.getStringData(Constants.IMAGE));
        }
        if(sharedPrefUtils.getStringData(Constants.IMAGE_FILE)!=null && !sharedPrefUtils.getStringData(Constants.IMAGE_FILE).equalsIgnoreCase("IMAGE_FILE")) {
            File file = new File(sharedPrefUtils.getStringData(Constants.IMAGE_FILE));
            Uri imageUri = Uri.fromFile(file);
            showImage(imageUri);
        }
        fragmentCustomerProfileBinding.setProfile(profileModel);
        if(sharedPrefUtils.getStringData(Constants.IsDLUploadStatus)!=null && sharedPrefUtils.getStringData(Constants.IsDLUploadStatus).equalsIgnoreCase("Y"))
            fragmentCustomerProfileBinding.downloadDl.setVisibility(View.VISIBLE);
        fragmentCustomerProfileBinding.editProfilename.setOnClickListener(this);
        fragmentCustomerProfileBinding.editProfilepicture.setOnClickListener(this);
        fragmentCustomerProfileBinding.editAddress.setOnClickListener(this);
        fragmentCustomerProfileBinding.editEmail.setOnClickListener(this);
        fragmentCustomerProfileBinding.editMobile.setOnClickListener(this);
        fragmentCustomerProfileBinding.saveContinue.setOnClickListener(this);
        fragmentCustomerProfileBinding.backIcon.setOnClickListener(this);
        fragmentCustomerProfileBinding.downloadDl.setOnClickListener(this);
        fragmentCustomerProfileBinding.upload.setOnClickListener(this);

    }

    private void showImage(Uri imageUri) {
        RequestOptions requestOptions = new RequestOptions();
        requestOptions.placeholder(R.drawable.user_default);
        requestOptions.circleCrop();

        Glide.with(this)
                .load(imageUri)
                .apply(requestOptions)
                .into(fragmentCustomerProfileBinding.profileImage);



    }

    @Override
    public void onClick(View view) {
        switch (view.getId()){
            case R.id.edit_address:
                fragmentCustomerProfileBinding.edittextAddress.requestFocus();
                changeBackgroundTint(fragmentCustomerProfileBinding.edittextAddress);
                break;
            case R.id.back_icon:
                onBackPressed();
                break;
            case R.id.edit_email:
                fragmentCustomerProfileBinding.edittextEmail.requestFocus();
                changeBackgroundTint(fragmentCustomerProfileBinding.edittextEmail);
                break;
            case R.id.edit_profilename:
                fragmentCustomerProfileBinding.edittextFname.requestFocus();
                changeBackgroundTint(fragmentCustomerProfileBinding.edittextFname);
                changeBackgroundTint(fragmentCustomerProfileBinding.edittextLname);

                break;
            case R.id.edit_mobile:
                fragmentCustomerProfileBinding.edittextMobile.requestFocus();
                changeBackgroundTint(fragmentCustomerProfileBinding.edittextMobile);
                break;
            case R.id.edit_profilepicture:
                final CharSequence[] options = {"Take Photo", "Choose From Gallery","Cancel"};
                AlertDialog.Builder builder = new AlertDialog.Builder(this);
                builder.setTitle("Select Option");
                builder.setItems(options, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int item) {
                        if (options[item].equals("Take Photo")) {
                            dialog.dismiss();

                            if (!Permissionsutils.checkForCameraPermission(MyProfileActivity.this)) {
                                Permissionsutils.askForCameraPermission(MyProfileActivity.this,PICK_IMAGE_CAMERA);
                            } else {
                                pickCameraImage();
                            }
                        } else if (options[item].equals("Choose From Gallery")) {
                            dialog.dismiss();

                            if (!Permissionsutils.checkForStoragePermission(MyProfileActivity.this)) {
                                Permissionsutils.askForStoragePermission(MyProfileActivity.this,PICK_IMAGE_GALLERY);
                            } else {
                                pickImage();
                            }
                        } else if (options[item].equals("Cancel")) {
                            dialog.dismiss();
                        }
                    }
                });
                builder.show();



                break;
            /*case R.id.upload:
                showUploadDlDialog();
                break;*/
         /*   case R.id.download_dl:
                showDownloadDLDialog();
                break;*/
            case R.id.save_continue:
                callSaveContinueApi();
                break;

        }

    }

    private void showDownloadDLDialog() {

        Call objectCall = RestClient.getApiService().getDrivingLicenseData(profileModel.userID);
        RestClient.makeApiRequest(this,objectCall,this, DRIVING_LICENSE_DATA,true);


    }

    private void showUploadDlDialog() {

        uploadDlDialog = new Dialog(this);
        dialogUploadDlBinding = DataBindingUtil.inflate(LayoutInflater.from(this), R.layout.dialog_upload_dl, null, false);
        uploadDlDialog.setContentView(dialogUploadDlBinding.getRoot());
        dialogUploadDlBinding.close.setOnClickListener(view -> {
            uploadDlDialog.dismiss();


        });
        dialogUploadDlBinding.uplaodDl.setOnClickListener(view -> {

//            if (!Permissionsutils.checkForStoragePermission(MyProfileActivity.this)) {
//                Permissionsutils.askForStoragePermission(MyProfileActivity.this,Constants.DL);
//            } else {
//                pickDL();
//            }


        });
        Utils.showDilaog(uploadDlDialog);

    }

    private void pickDL() {
//        Intent intent = new Intent();
//        intent.setAction(Intent.ACTION_GET_CONTENT);
//        intent.setType("image/*");
//        startActivityForResult(intent, Constants.DL);
    }

    private void changeBackgroundTint(AppCompatEditText edittextAddress) {
        Utils.showKeyboard(this);
        edittextAddress.setEnabled(true);
        DrawableCompat.setTint(edittextAddress.getBackground(), ContextCompat.getColor(this, R.color.color_3d3d3d));
        edittextAddress.setSelection(edittextAddress.getText().length());

    }

    public void pickImage() {
        Intent intent = new Intent(Intent.ACTION_GET_CONTENT);
        intent.setType("image/*");
        startActivityForResult(intent, PICK_IMAGE_GALLERY);
    }


    private File createImageFile() throws IOException {
        // Create an image file name
        String timeStamp = new SimpleDateFormat("yyyyMMdd_HHmmss").format(new Date());
        String imageFileName = "JPEG_" + timeStamp + "_";
        File storageDir = getExternalFilesDir(Environment.DIRECTORY_PICTURES);
        File image = File.createTempFile(
                imageFileName,  /* prefix */
                ".jpg",         /* suffix */
                storageDir      /* directory */
        );

        // Save a file: path for use with ACTION_VIEW intents
        fullFilePath = image.getAbsolutePath();
        return image;
    }

    private void pickCameraImage() {
        Intent takePictureIntent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
        // Ensure that there's a camera activity to handle the intent
        if (takePictureIntent.resolveActivity(getPackageManager()) != null) {
            // Create the File where the photo should go
            File photoFile = null;
            try {
                photoFile = createImageFile();
            } catch (IOException ex) {
                // Error occurred while creating the File
            }
            // Continue only if the File was successfully created
            if (photoFile != null) {
                Uri photoURI = FileProvider.getUriForFile(this,
                        "com.demo.provider",
                        photoFile);
                takePictureIntent.putExtra(MediaStore.EXTRA_OUTPUT, photoURI);
                startActivityForResult(takePictureIntent, PICK_IMAGE_CAMERA);
            }
        }
    }

    private void callSaveContinueApi() {
        if(allFieldsEntered()){
            Call objectCall = RestClient.getApiService().updateProfile(profileModel);
            RestClient.makeApiRequest(this,objectCall,this, UPDATE_DATA,true);
        }

    }

    private void uploadDrivingLicenseToServer(File destination,String dlNumber) {
        MultipartBody.Part filePart = MultipartBody.Part.createFormData("File", destination.getName(), RequestBody.create(MediaType.parse("image/*"), destination));
        Call<RegistrationResponse> call;
        call = RestClient.getApiService().uploadDrivingLicense(filePart, RequestBody.create(MediaType.parse("text/plain"), profileModel.getUserID()), RequestBody.create(MediaType.parse("text/plain"), dlNumber));

        RestClient.makeApiRequest(this, call, this, UPLOAD_DRIVING_LICENSE, true);
    }

    private boolean allFieldsEntered() {
        if(profileModel.firstName==null ||profileModel.firstName.trim().length()==0)
        {
            Utils.showToast(this,getResources().getString(R.string.validation_enter_fname));
            fragmentCustomerProfileBinding.edittextFname.requestFocus();
            return false;
        }
        else if(profileModel.lastName==null ||profileModel.lastName.trim().length()==0)
        {
            Utils.showToast(this,getResources().getString(R.string.validation_enter_lname));
            fragmentCustomerProfileBinding.edittextLname.requestFocus();
            return false;
        }
        if(profileModel.email==null ||profileModel.email.trim().length()==0)
        {
            Utils.showToast(this,getResources().getString(R.string.validation_enter_email));
            fragmentCustomerProfileBinding.edittextEmail.requestFocus();
            return false;
        }
        else if(!Utils.isValidEmail(profileModel.getEmail()))
        {
            Utils.showToast(this,getString(R.string.validation_enter_valid_email));
            fragmentCustomerProfileBinding.edittextEmail.requestFocus();
            return false;
        }
        else  if(profileModel.mobile==null ||profileModel.mobile.trim().length()==0)
        {
            Utils.showToast(this,getResources().getString(R.string.validation_enter_mobile_number));
            fragmentCustomerProfileBinding.edittextMobile.requestFocus();
            return false;
        }

        return true;
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        finish();
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == PICK_IMAGE_CAMERA && resultCode == RESULT_OK) {

            File destination = new File(fullFilePath);
            if(destination.exists())
            {
                showImage(Uri.fromFile(destination));
                uploadFileToServer(destination);
            }

          /*  Bundle extras = data.getExtras();
            Bitmap imageBitmap = (Bitmap) extras.get("data");
            fragmentCustomerProfileBinding.profileImage.setImageBitmap(imageBitmap);*/
        }
        else  if (requestCode == PICK_IMAGE_GALLERY && resultCode == Activity.RESULT_OK) {
            if (data == null) {
                //Display an error
                return;
            }
            try {
                Uri uri = data.getData();
                 fullFilePath = UriUtils.getPathFromUri(this,uri);

                File destination = new File(fullFilePath);
                if(destination.exists())
                {
                    showImage(uri);
                    uploadFileToServer(destination);
                }

            } catch (Exception e) {
                e.printStackTrace();
            }
            //Now you can do whatever you want with your inpustream, save it as file, upload to a server, decode a bitmap...
        }
        /*else  if (requestCode == Constants.DL && resultCode == Activity.RESULT_OK) {
            Uri uri = data.getData();
            String fullFilePath = UriUtils.getPathFromUri(this,uri);

            File destination = new File(fullFilePath);
            if(destination.exists())
            {
                if(destination.exists())
                {
                    uploadDrivingLicenseToServer(destination,dialogUploadDlBinding.edittextDl.getText().toString());

                }
            }
        }*/
    }
    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        if(grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED){
            //resume tasks needing this permission
            if(requestCode==PICK_IMAGE_GALLERY)
                 pickImage();
            else if(requestCode==PICK_IMAGE_CAMERA)
                pickCameraImage();
          /*  else if(requestCode==Constants.DL)
                pickDL();*/
            else if(requestCode==10)
                downloadDL();

        }

    }

    private void downloadDL() {
        File file = new File(Environment.getExternalStorageDirectory() + "/DEMO/" + dlDocument.substring(dlDocument.lastIndexOf(".")));

        DownloadManager.Request request = new DownloadManager.Request(Uri.parse(dlDocument))
                .setTitle("Driving License")
                .setDescription("Downloading")
                .setNotificationVisibility(DownloadManager.Request.VISIBILITY_VISIBLE)
                .setNotificationVisibility(DownloadManager.Request.VISIBILITY_VISIBLE_NOTIFY_COMPLETED)
                .setDestinationUri(Uri.fromFile(file))
                .setAllowedOverMetered(true)
                .setAllowedOverRoaming(true);

        DownloadManager downloadManager = (DownloadManager) this.getSystemService(DOWNLOAD_SERVICE);
        long referenceID = downloadManager.enqueue(request);
        Utils.showToast(MyProfileActivity.this, "Driving License is downloaded successfullly");

    }


    private void uploadFileToServer(File destination) {
        MultipartBody.Part filePart = MultipartBody.Part.createFormData("File", destination.getName(), RequestBody.create(MediaType.parse("image/*"), destination));
        Call<RegistrationResponse> call = RestClient.getApiService().uploadProfilePicture(filePart,  RequestBody.create( MediaType.parse("text/plain"), sharedPrefUtils.getStringData(this, Constants.USER_ID)+"") );
        RestClient.makeApiRequest(this, call, this, UPLOAD_DOC, true);
    }

    @Override
    public void onApiResponse(Call<Object> call, Object response, int reqCode) throws Exception {
        if(reqCode== UPDATE_DATA){
            RegistrationResponse registrationResponse = (RegistrationResponse) response;
            if(registrationResponse.getResponseCode().equalsIgnoreCase("200")){
                sharedPrefUtils.saveData(Constants.ADDRESS,profileModel.address);

                sharedPrefUtils.saveData(Constants.FNAME,profileModel.firstName);
                sharedPrefUtils.saveData(Constants.LNAME,profileModel.lastName);
                sharedPrefUtils.saveData(Constants.EMAIL,profileModel.email);
                sharedPrefUtils.saveData(Constants.MOBILE_NO,profileModel.mobile);

                Utils.showToast(this,registrationResponse.getDescriptions());
                startActivity(new Intent(this, HomeActivity.class));
                finish();

            }
        }
        else  if(reqCode==UPLOAD_DOC){
            RegistrationResponse registrationResponse = (RegistrationResponse) response;
            if(registrationResponse.getResponseCode().equalsIgnoreCase("200")){
                sharedPrefUtils.saveData(Constants.IMAGE_FILE,fullFilePath);
                sharedPrefUtils.saveData(Constants.IMAGE,fullFilePath);
                Utils.showToast(this,registrationResponse.getDescriptions());

            }
        }
        else  if(reqCode==UPLOAD_DRIVING_LICENSE){
            RegistrationResponse registrationResponse = (RegistrationResponse) response;
            Utils.showToast(this,registrationResponse.getDescriptions());
            uploadDlDialog.dismiss();
            if(registrationResponse.getResponseCode().equalsIgnoreCase("200")) {
                sharedPrefUtils.saveData(Constants.IsDLUploadStatus, "Y");
            }
        }
        else if(reqCode==DRIVING_LICENSE_DATA){
            DrivingLicenseDataResponse drivingLicenseDataResponse = (DrivingLicenseDataResponse) response;
             dlDocument = drivingLicenseDataResponse.getDLDocument();
            if(drivingLicenseDataResponse.getDLNumber().trim().length()>0) {
                fragmentCustomerProfileBinding.dlNumber.setVisibility(View.VISIBLE);
                fragmentCustomerProfileBinding.dlNumber.setText("Driving License Number: "+drivingLicenseDataResponse.getDLNumber());
            }
            if(drivingLicenseDataResponse.getResponseCode().equalsIgnoreCase("200")){
                if(Permissionsutils.checkForStoragePermission(this)) {
                   downloadDL();
                     }
                else
                    Permissionsutils.askForStoragePermission(this,10);
            }
        }

    }

    @Override
    public void onApiError(Call<Object> call, Object response, int reqCode) throws Exception {

    }
}
