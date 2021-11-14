package com.demo.home.booking;

import static com.facebook.FacebookSdk.getApplicationContext;

import android.content.pm.PackageManager;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.os.Environment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.databinding.DataBindingUtil;
import androidx.fragment.app.Fragment;

import com.demo.R;
import com.demo.databinding.FragmentBookingBookedBinding;
import com.demo.home.HomeActivity;
import com.demo.home.booking.model.BookingAcceptModel;
import com.demo.home.booking.model.StatusRequestModel;
import com.demo.registrationLogin.model.RegistrationResponse;
import com.demo.utils.Constants;
import com.demo.utils.Permissionsutils;
import com.demo.utils.PrintLog;
import com.demo.utils.RecorderUtils;
import com.demo.utils.SharedPrefUtils;
import com.demo.utils.Utils;
import com.demo.webservice.ApiResponseListener;
import com.demo.webservice.RestClient;

import java.io.File;

import okhttp3.MediaType;
import okhttp3.MultipartBody;
import okhttp3.RequestBody;
import retrofit2.Call;

public class BookingConfirmedFragment extends Fragment implements ApiResponseListener {
    private FragmentBookingBookedBinding fragmentBookingBookedBinding;
    private boolean isRecording;
    private RecorderUtils recorderUtils;
    int time=0;
    private CountDownTimer countDownTimer;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        fragmentBookingBookedBinding = DataBindingUtil.inflate(inflater, R.layout.fragment_booking_booked,container,false);
        Utils.cancelJob(getActivity());


        return fragmentBookingBookedBinding.getRoot();
    }



    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        fragmentBookingBookedBinding.tvCancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ((HomeActivity) getActivity()).showFragment(new CancelDemoFragment());
            }
        });
        fragmentBookingBookedBinding.recordAudio.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                RecordAudio();



            }
        });

        if(Constants.BOOK_TYPE.equalsIgnoreCase("Demo"))
            callBookingDetailApi();
        else
            callMeetingDetailApi();
    }

    private void startTimer() {
        countDownTimer =  new CountDownTimer(30000, 1000) {

            public void onTick(long millisUntilFinished) {
                fragmentBookingBookedBinding.textTimer.setText("0:"+checkDigit(time));
                time++;
            }

            public void onFinish() {
                fragmentBookingBookedBinding.textTimer.setText("Sending");
            }

        }.start();
    }

    String checkDigit(int number) {
        return number <= 9 ? "0" + number : String.valueOf(number);
    }


    private void callMeetingDetailApi() {
        StatusRequestModel statusRequestModel = new StatusRequestModel();
        statusRequestModel.setMeetingID(Constants.MEETING_ID);
        if(((HomeActivity)getActivity()).getLocation() !=null) {
            statusRequestModel.setLatitude(String.valueOf(((HomeActivity) getActivity()).getLocation().getLatitude()));
            statusRequestModel.setLongitude(String.valueOf(((HomeActivity) getActivity()).getLocation().getLongitude()));
        }
        else
        {
            statusRequestModel.setLatitude(String.valueOf(Constants.LATITUDE));
            statusRequestModel.setLongitude(Constants.LONGITUDE);
        }
        statusRequestModel.setUserID(((HomeActivity)getActivity()).userId);
        Call objectCall = RestClient.getApiService().getMeetingDetails(statusRequestModel);
        RestClient.makeApiRequest(getActivity(), objectCall, this, 1, true);

    }



    private void callBookingDetailApi() {
        StatusRequestModel statusRequestModel = new StatusRequestModel();
        statusRequestModel.setBookingID(Constants.BOOKINGID);
        if(((HomeActivity)getActivity()).getLocation() !=null) {
            statusRequestModel.setLatitude(String.valueOf(((HomeActivity) getActivity()).getLocation().getLatitude()));
            statusRequestModel.setLongitude(String.valueOf(((HomeActivity) getActivity()).getLocation().getLongitude()));
        }
        else
        {
            statusRequestModel.setLatitude(Constants.LATITUDE!=null ?Constants.LATITUDE:"0.0");
            statusRequestModel.setLongitude(Constants.LONGITUDE !=null ? Constants.LONGITUDE : "0.0");
        }
        statusRequestModel.setUserID(((HomeActivity)getActivity()).userId);
        Call objectCall = RestClient.getApiService().getBookingDetails(statusRequestModel);
        RestClient.makeApiRequest(getActivity(), objectCall, this, 1, true);

    }

    @Override
    public void onApiResponse(Call<Object> call, Object response, int reqCode) throws Exception {

        if(reqCode==1) {
            BookingAcceptModel bookingAcceptModel = (BookingAcceptModel) response;
            if (bookingAcceptModel.getResponseCode().equalsIgnoreCase("200")) {
                fragmentBookingBookedBinding.setBookingDetail(bookingAcceptModel);
                fragmentBookingBookedBinding.executePendingBindings();
                ((HomeActivity) getActivity()).locationUtils.drawOnMap(bookingAcceptModel.bookingdetails.getSpecialistLatitude(), bookingAcceptModel.bookingdetails.getSpecialistLongitude());
                ((HomeActivity) getActivity()).setPeekheight(fragmentBookingBookedBinding.parentLl.getMeasuredHeight());

            }
        }else if(reqCode==2){
            fragmentBookingBookedBinding.recordingCardview.setVisibility(View.GONE);
            RegistrationResponse registrationResponse = (RegistrationResponse) response;
            Utils.showToast(getActivity(),registrationResponse.getDescriptions());
        }


    }

    @Override
    public void onApiError(Call<Object> call, Object response, int reqCode) throws Exception {

    }

    @Override
    public void onRequestPermissionsResult(int requestCode, String[] permissions, int[] grantResults) {
        // this method is called when user will
        // grant the permission for audio recording.
        switch (requestCode) {
            case 1:
                if (grantResults.length > 0) {
                    boolean permissionToRecord = grantResults[0] == PackageManager.PERMISSION_GRANTED;
                    boolean permissionToStore = grantResults[1] == PackageManager.PERMISSION_GRANTED;
                    if (permissionToRecord && permissionToStore) {
                        RecordAudio();
                    }
                }
                break;
        }
    }

    private void RecordAudio() {
        if (!isRecording) {
            if (Permissionsutils.checkForRecordPermission()) {
                fragmentBookingBookedBinding.recordAudio.setClickable(false);
                fragmentBookingBookedBinding.recordAudio.setEnabled(false);
                recorderUtils=   new RecorderUtils();
                isRecording = true;
                recorderUtils.startRecording();
                startTimer();
            } else
                Permissionsutils.askForRecordPermission(getActivity());

        } else {
            recorderUtils.stopRecording();
            fragmentBookingBookedBinding.recordAudio.setClickable(true);
            fragmentBookingBookedBinding.recordAudio.setImageDrawable(getContext().getDrawable(R.drawable.ic_record));
            countDownTimer.onFinish();
            String mFileName = Environment.getExternalStorageDirectory().getAbsolutePath();
            mFileName += "/AudioRecording.mp3";
            uploadFileToServer(new File(mFileName));

        }
    }
    private void uploadFileToServer(File destination) {
        MultipartBody.Part filePart = MultipartBody.Part.createFormData("File", destination.getName(), RequestBody.create(MediaType.parse("image/*"), destination));
        Call<RegistrationResponse> call = RestClient.getApiService().uploadVoiceMessage(filePart,  RequestBody.create( MediaType.parse("text/plain"),((HomeActivity)getActivity()).userId),RequestBody.create( MediaType.parse("text/plain"),Constants.BOOKINGID) );
        RestClient.makeApiRequest(getActivity(), call, this, 2, true);
    }

}
