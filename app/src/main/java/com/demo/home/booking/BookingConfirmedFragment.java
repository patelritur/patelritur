package com.demo.home.booking;

import android.Manifest;
import android.annotation.SuppressLint;
import android.app.Activity;
import android.app.Dialog;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.pm.PackageManager;
import android.location.Location;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.os.Environment;
import android.os.Handler;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.widget.AppCompatTextView;
import androidx.databinding.DataBindingUtil;
import androidx.fragment.app.Fragment;

import com.cometchat.pro.constants.CometChatConstants;
import com.cometchat.pro.core.CometChat;
import com.cometchat.pro.exceptions.CometChatException;
import com.cometchat.pro.models.User;
import com.demo.DemoApp;
import com.demo.R;
import com.demo.carDetails.CarDetailsActivity;
import com.demo.carDetails.model.CarDetailRequest;
import com.demo.carDetails.model.CarPorfomaInvoiceModel;
import com.demo.databinding.DialogReviewsBinding;
import com.demo.databinding.FragmentBookingBookedBinding;
import com.demo.databinding.ItemCarFacilitiesBinding;
import com.demo.home.HomeActivity;
import com.demo.home.TakeADemoFragment;
import com.demo.home.booking.model.BookingAcceptModel;
import com.demo.home.booking.model.LocationResponse;
import com.demo.home.booking.model.StatusRequestModel;
import com.demo.registrationLogin.model.RegistrationResponse;
import com.demo.utils.Constants;
import com.demo.utils.DialogUtils;
import com.demo.utils.DownloadFileFromURL;
import com.demo.utils.FileDownloadReady;
import com.demo.utils.ParserTask;
import com.demo.utils.Permissionsutils;
import com.demo.utils.PrintLog;
import com.demo.utils.RecorderUtils;
import com.demo.utils.SharedPrefUtils;
import com.demo.utils.Utils;
import com.demo.utils.comectChat.utils.CallUtils;
import com.demo.webservice.ApiResponseListener;
import com.demo.webservice.RestClient;
import com.google.android.gms.maps.model.LatLng;

import java.io.BufferedReader;
import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.Timer;
import java.util.TimerTask;

import okhttp3.MediaType;
import okhttp3.MultipartBody;
import okhttp3.RequestBody;
import retrofit2.Call;

public class BookingConfirmedFragment extends Fragment implements ApiResponseListener, View.OnClickListener {
    private FragmentBookingBookedBinding fragmentBookingBookedBinding;
    private boolean isRecording;
    private RecorderUtils recorderUtils;
    private int time=0;
    private CountDownTimer countDownTimer;
    private boolean fromNotification;
    private BroadcastReceiver notificationReceiver = new NotificationReceiver();
    private BookingAcceptModel bookingAcceptModel;
    private DialogReviewsBinding binding;
    private CarPorfomaInvoiceModel carPorfomaInvoiceModel;
    private static long destinationTime;
    private Timer timer;
    private TimerTask hourlyTask;
    private String specialistId;
    private int count=0;
    public  BookingConfirmedFragment(boolean fromNotification){
        this.fromNotification = true;
    }
    public  BookingConfirmedFragment(){

    }


    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        fragmentBookingBookedBinding = DataBindingUtil.inflate(inflater, R.layout.fragment_booking_booked,container,false);
        Utils.cancelJob(getActivity());
        timer = new Timer ();

        return fragmentBookingBookedBinding.getRoot();
    }

    private void setTask() {

        hourlyTask = new TimerTask () {
            @Override
            public void run () {
                switch (count){
                    case 0:
                        getActivity().runOnUiThread(new Runnable() {
                            @Override
                            public void run() {
                                fragmentBookingBookedBinding.status.setText("On Time");
                                count++;
                            }
                        });

                        break;
                    case 1:
                        getActivity().runOnUiThread(new Runnable() {
                            @Override
                            public void run() {
                                fragmentBookingBookedBinding.status.setText("Arriving Soon");
                                count++;
                            }
                        });

                        break;
                    case 2:
                        getActivity().runOnUiThread(new Runnable() {
                            @Override
                            public void run() {
                                fragmentBookingBookedBinding.status.setText("Arrived");
                                count++;
                            }
                        });

                        break;
                    case 3:
                        getActivity().runOnUiThread(new Runnable() {
                            @Override
                            public void run() {
                                fragmentBookingBookedBinding.status.setText("Waiting");
                                count++;
                            }
                        });

                        break;


                }

//                Call objectCall = RestClient.getApiService().getSpecialistLocation(specialistId);
//                RestClient.makeApiRequest(getActivity(),objectCall,BookingConfirmedFragment.this,3,false);
            }
        };

    }


    private void endTask() {
        if(hourlyTask!=null)
            hourlyTask.cancel();
        if(timer!=null)
            timer.cancel();
        timer=null;

    }




    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        fragmentBookingBookedBinding.performaInvoice.setOnClickListener(this);
        fragmentBookingBookedBinding.call.setOnClickListener(this);
        fragmentBookingBookedBinding.bookDemo.setOnClickListener(this);
        fragmentBookingBookedBinding.joinCall.setOnClickListener(this);
        fragmentBookingBookedBinding.tvCancel.setOnClickListener(this);
        fragmentBookingBookedBinding.recordAudio.setOnClickListener(this);


        if(Constants.BOOK_TYPE.equalsIgnoreCase("Demo"))
            callBookingDetailApi();
        else
            callMeetingDetailApi();
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
            statusRequestModel.setLatitude(Constants.LATITUDE!=null ?Constants.LATITUDE:"0.0");
            statusRequestModel.setLongitude(Constants.LONGITUDE !=null ? Constants.LONGITUDE : "0.0");
        }
        statusRequestModel.setUserID(((HomeActivity)getActivity()).userId);
        Call objectCall = RestClient.getApiService().getMeetingDetails(statusRequestModel);
        RestClient.makeApiRequest(getActivity(), objectCall, this, 1, true);

    }



    private void callBookingDetailApi() {
        StatusRequestModel statusRequestModel = new StatusRequestModel();
        statusRequestModel.setBookingID(Constants.BOOKING_ID);
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



    private void callPorfomaInvoiceApi() {
        CarDetailRequest carDetailRequest = new CarDetailRequest();
        carDetailRequest.setUserID(new SharedPrefUtils(getActivity()).getStringData(Constants.USER_ID));
        carDetailRequest.setLatitude(String.valueOf(Constants.LATITUDE));
        carDetailRequest.setLongitude(String.valueOf(Constants.LONGITUDE));
        carDetailRequest.setCarID(bookingAcceptModel.getBookingdetails().getCarID());
        Call objectCall = RestClient.getApiService().getCarPorfomainvoice(carDetailRequest);
        RestClient.makeApiRequest(getActivity(), objectCall, this, 4, true);
    }





    private void updateCarCheckList() {
        fragmentBookingBookedBinding.flowLayout.removeAllViews();
        for(int i=0;i<bookingAcceptModel.getBookingdetails().getCarchecklist().size();i++){
            ItemCarFacilitiesBinding itemCarFacilitiesBinding =DataBindingUtil.inflate(this.getLayoutInflater(),R.layout.item_car_facilities,null,false);
            itemCarFacilitiesBinding.setCarCheckList(bookingAcceptModel.getBookingdetails().getCarchecklist().get(i));
            fragmentBookingBookedBinding.flowLayout.addView(itemCarFacilitiesBinding.getRoot());
        }
    }

    private void joinCall(BookingAcceptModel bookingAcceptModel) {
        if(bookingAcceptModel.getBookingdetails().getVirtualMeetType().equalsIgnoreCase("Audio")){

            CometChat.login(((HomeActivity)getActivity()).userId, Constants.AUTH_KEY, new CometChat.CallbackListener<User>() {
                @Override
                public void onSuccess(User user) {
                    CallUtils.initiateCall(getActivity(), bookingAcceptModel.getBookingdetails().getSpecialistID()
                            , CometChatConstants.RECEIVER_TYPE_USER,"audio");


                    Log.d("TAG", "Login Successful : " + user.toString());
                }

                @Override
                public void onError(CometChatException e) {
                    Log.d("TAG", "Login failed with exception: " + e.getMessage());
                }
            });
        }
        else{
            CometChat.login(((HomeActivity)getActivity()).userId, Constants.AUTH_KEY, new CometChat.CallbackListener<User>() {

                @Override
                public void onSuccess(User user) {
                    CallUtils.initiateCall(getActivity(), bookingAcceptModel.getBookingdetails().getSpecialistID(),CometChatConstants.RECEIVER_TYPE_USER,"video");
                    Log.d("TAG", "Login Successful : " + user.toString());
                }

                @Override
                public void onError(CometChatException e) {
                    Log.d("TAG", "Login failed with exception: " + e.getMessage());
                }
            });

        }
    }


    @NonNull
    private Dialog getDialog() {
        Dialog dialog = new Dialog(getActivity());
        binding = DataBindingUtil.inflate(LayoutInflater.from(getActivity()), R.layout. dialog_reviews, null, false);
        dialog.setContentView(binding.getRoot());
        binding.close.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dialog.dismiss();
            }
        });
        dialog.getWindow().setLayout(WindowManager.LayoutParams.MATCH_PARENT, WindowManager.LayoutParams.WRAP_CONTENT);

        WindowManager.LayoutParams lp = dialog.getWindow().getAttributes();
        dialog.getWindow().setBackgroundDrawableResource(android.R.color.transparent);
        lp.dimAmount = 0.8f;
        dialog.getWindow().addFlags(WindowManager.LayoutParams.FLAG_DIM_BEHIND);

        return dialog;
    }



    private void showPorfomaInvoiceDialog(CarPorfomaInvoiceModel carPorfomaInvoiceModel)
    {
        Dialog dialog = getDialog();
        binding.textviewTitle.setText(getString(R.string.porfoma_invoice));
        binding.setCarInvoice(carPorfomaInvoiceModel.getPorfomainvoice());
        this.carPorfomaInvoiceModel = carPorfomaInvoiceModel;
        binding.layoutInvoice.getRoot().setVisibility(View.VISIBLE);
        binding.recyclerviewReview.setVisibility(View.GONE);
        binding.imageviewDownload.setVisibility(View.VISIBLE);
        binding.imageviewDownload.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (!Permissionsutils.CheckForStoragePermission(getActivity())) {
                    requestPermissions(new String[]{Manifest.permission.WRITE_EXTERNAL_STORAGE,Manifest.permission.READ_EXTERNAL_STORAGE}, 2);
                } else {
                    DownloadFileFromURL();
                }


            }
        });
        binding.executePendingBindings();
        dialog.show();
    }

    private void DownloadFileFromURL() {
        new DownloadFileFromURL(carPorfomaInvoiceModel.getPorfomainvoice().downloadInvoiceURL, carPorfomaInvoiceModel.getPorfomainvoice().getCarName(), new FileDownloadReady() {
            @Override
            public void onFileDownloaded(String fileId) {
                Utils.showToast(getActivity(),"Performa invoice is downloaded successfullly");
            }
        }).execute();
    }

    private long getNextTime(int format) {
        Calendar date = Calendar.getInstance();
        System.out.println("Current Date and TIme : " + date.getTime());
        long timeInSecs = date.getTimeInMillis();
        Date afterAdding10Mins = new Date(timeInSecs + (format* 60 * 1000));

        return afterAdding10Mins.getTime();

    }

    private void getDestinationInfo(String specialistLatitude, String specialistLongitude) {
        float[] results = new float[1];
        Location.distanceBetween(((HomeActivity)getActivity()).locationUtils.getLoc().getLatitude(), ((HomeActivity)getActivity()).locationUtils.getLoc().getLongitude(),
                Double.parseDouble(specialistLatitude), Double.parseDouble(specialistLongitude), results);
        PrintLog.v("o"+((HomeActivity)getActivity()).locationUtils.getLoc().getLatitude());
        PrintLog.v("o"+((HomeActivity)getActivity()).locationUtils.getLoc().getLongitude());
        PrintLog.v("d"+specialistLatitude);
        PrintLog.v("d"+specialistLongitude);
        PrintLog.v("res"+results[0]);

        Float duration = (results[0]*60)/(40*1000);
        PrintLog.v("duration"+duration);
        if(destinationTime==0)
            destinationTime = getNextTime(Math.round(duration));
        PrintLog.v("ffff d"+destinationTime);
        PrintLog.v("ffff n"+getNextTime(Math.round(duration)));
        if(duration<=1){
            fragmentBookingBookedBinding.status.setText("Arriving Soon");
        }
        else if((getNextTime(Math.round(duration))-destinationTime)<60000)
            fragmentBookingBookedBinding.status.setText("On Time");
        else
            fragmentBookingBookedBinding.status.setText("Delayed");

        //  arrivingTime.setText(Math.round(duration) + " mins left");

        //  fragmentBookingBookedBinding.arrivingTimestamp.setText("Arriving time:"+getNextTime(Math.round(duration)));
       /* String url =  getDirectionsUrl(new LatLng(((HomeActivity)getActivity()).getLocation().getLatitude(),((HomeActivity)getActivity()).getLocation().getLongitude()),new LatLng(Double.parseDouble(specialistLatitude),Double.parseDouble(specialistLatitude)));
        PrintLog.v("="+url);
        DownloadTask downloadTask =new DownloadTask();
        downloadTask.execute(url);
*/
    }
    private void RecordAudio() {
        if (!isRecording) {
            if (Permissionsutils.checkForRecordPermission(getActivity())) {
                recorderUtils=   new RecorderUtils();
                isRecording = true;
                recorderUtils.startRecording();
                startTimer();
            } else
                Permissionsutils.askForRecordPermission(getActivity());

        } else {
            countDownTimer.cancel();
            fragmentBookingBookedBinding.recordAudio.setClickable(true);
            fragmentBookingBookedBinding.recordAudio.setImageDrawable(getContext().getDrawable(R.drawable.ic_record));
            countDownTimer.onFinish();

        }
    }
    private void startTimer() {
        countDownTimer =  new CountDownTimer(30000, 1000) {

            public void onTick(long millisUntilFinished) {
                fragmentBookingBookedBinding.textTimer.setText("0:"+checkDigit(time));
                time++;
            }

            public void onFinish() {
                fragmentBookingBookedBinding.textTimer.setText("Done");
                recorderUtils.stopRecording();
                String mFileName = Environment.getExternalStorageDirectory().getAbsolutePath();
                mFileName += "/AudioRecording.mp3";

                uploadFileToServer(new File(mFileName));
            }

        }.start();
    }

    String checkDigit(int number) {
        return number <= 9 ? "0" + number : String.valueOf(number);
    }

    private void uploadFileToServer(File destination) {
        MultipartBody.Part filePart = MultipartBody.Part.createFormData("File", destination.getName(), RequestBody.create(MediaType.parse("image/*"), destination));
        Call<RegistrationResponse> call = RestClient.getApiService().uploadVoiceMessage(filePart,  RequestBody.create( MediaType.parse("text/plain"),((HomeActivity)getActivity()).userId),RequestBody.create( MediaType.parse("text/plain"),Constants.BOOKING_ID) );
        RestClient.makeApiRequest(getActivity(), call, this, 2, true);
    }


    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.call:
                CometChat.login(((HomeActivity)getActivity()).userId, Constants.AUTH_KEY, new CometChat.CallbackListener<User>() {
                    @Override
                    public void onSuccess(User user) {
                        CallUtils.initiateCall(getActivity(), bookingAcceptModel.getBookingdetails().getSpecialistID()
                                , CometChatConstants.RECEIVER_TYPE_USER,"audio");


                        Log.d("TAG", "Login Successful : " + user.toString());
                    }

                    @Override
                    public void onError(CometChatException e) {
                        Log.d("TAG", "Login failed with exception: " + e.getMessage());
                    }
                });
                break;
            case R.id.join_call:
                if(bookingAcceptModel!=null &&  bookingAcceptModel.getBookingdetails().getMeetingType()!=null && bookingAcceptModel.getBookingdetails().getVirtualMeetStatus().equalsIgnoreCase("Y") ){
                    joinCall(bookingAcceptModel);
                }
                break;
            case R.id.tv_cancel:
                ((HomeActivity) getActivity()).showFragment(new CancelDemoFragment());
                break;
            case R.id.performa_invoice:
                callPorfomaInvoiceApi();
                break;
            case R.id.book_demo:
                ((HomeActivity) getActivity()).showFragment(new TakeADemoFragment());
                break;
            case R.id.record_audio:
                RecordAudio();
                break;
        }

    }

    @SuppressLint("SetTextI18n")
    @Override
    public void onApiResponse(Call<Object> call, Object response, int reqCode) throws Exception {

        if(reqCode==1) {
            bookingAcceptModel = (BookingAcceptModel) response;
            if (bookingAcceptModel.getResponseCode().equalsIgnoreCase("200")) {
                fragmentBookingBookedBinding.setBookingDetail(bookingAcceptModel);
                fragmentBookingBookedBinding.submessage.setText(bookingAcceptModel.getBookingSubMessage());
                specialistId = bookingAcceptModel.getBookingdetails().getSpecialistID();
                updateCarCheckList();
                fragmentBookingBookedBinding.executePendingBindings();
                fragmentBookingBookedBinding.status.setText("Booked");
               /* if(hourlyTask==null) {
                    setTask();
                    timer.schedule(hourlyTask, 0l, 1000 * 30);
                }*/
                switch (bookingAcceptModel.getBookingdetails().getDemoStatusId()){
                    case "5":
                        new Handler().postDelayed(new Runnable() {
                            @Override
                            public void run() {
                                endTask();
                                ((HomeActivity)getActivity()).showFragment(new BookingFeedbackFragment(bookingAcceptModel.getBookingdetails().getSpecialistName()));
                            }
                        },5000);
                        break;
                    case "2":
                    case "3":
                        if(hourlyTask==null) {
                            setTask();
                            timer.schedule(hourlyTask, 0l, 1000 * 30);
                        }
                        fragmentBookingBookedBinding.call.setVisibility(View.GONE);
                        fragmentBookingBookedBinding.arrivingTime.setVisibility(View.GONE);
                        break;
                    case "4":
//                        fragmentBookingBookedBinding.status.setText(getString(R.string.arrived));
                        fragmentBookingBookedBinding.status.setText("Started");
                        endTask();
                        break;
                    case "7":
                        endTask();
                        fragmentBookingBookedBinding.status.setText(getString(R.string.rejected));
                        fragmentBookingBookedBinding.bookDemo.setVisibility(View.VISIBLE);
                        fragmentBookingBookedBinding.joinCall.setVisibility(View.GONE);
                        if(Constants.BOOK_TYPE.equalsIgnoreCase("Demo"))
                            fragmentBookingBookedBinding.bookDemo.setText(getString(R.string.book_a_demo));
                        else
                            fragmentBookingBookedBinding.bookDemo.setText(getString(R.string.book_a_meeting));

                        break;
                    case "9":
                        fragmentBookingBookedBinding.call.setVisibility(View.GONE);
                        fragmentBookingBookedBinding.arrivingTime.setVisibility(View.GONE);
                        break;
                    case "10":
                        endTask();
                        fragmentBookingBookedBinding.call.setVisibility(View.VISIBLE);
                        fragmentBookingBookedBinding.status.setText(getString(R.string.arrived));
                        break;
                    default:
                        if (!fromNotification && bookingAcceptModel.getBookingdetails().getMeetingType() != null && bookingAcceptModel.getBookingdetails().getMeetingType().equalsIgnoreCase("now") && bookingAcceptModel.getBookingdetails().getVirtualMeetStatus().equalsIgnoreCase("Y")) {
                            if (bookingAcceptModel.getBookingdetails().getVirtualMeetStatus().equalsIgnoreCase("Y"))
                                fragmentBookingBookedBinding.joinCall.setVisibility(View.VISIBLE);
                        }
                        break;


                }

                ((HomeActivity) getActivity()).locationUtils.drawOnMap(bookingAcceptModel.bookingdetails.getSpecialistLatitude(), bookingAcceptModel.bookingdetails.getSpecialistLongitude());
                ((HomeActivity) getActivity()).setPeekheight(fragmentBookingBookedBinding.parentLl.getMeasuredHeight());

            }
        }else if(reqCode==2){
            fragmentBookingBookedBinding.recordingCardview.setVisibility(View.GONE);
            RegistrationResponse registrationResponse = (RegistrationResponse) response;
            Utils.showToast(getActivity(),registrationResponse.getDescriptions());
        }
        else if(reqCode==3){
            LocationResponse bookingAcceptModel = ((LocationResponse) response);
            getDestinationInfo(bookingAcceptModel.getUserLatitude(),bookingAcceptModel.getUserLongitude());

        }
        else if(reqCode==4){
            CarPorfomaInvoiceModel carDetailReviewModel = (CarPorfomaInvoiceModel) response;
            showPorfomaInvoiceDialog(carDetailReviewModel);
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
            case 2:
                if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                    //resume tasks needing this permission
                    DownloadFileFromURL();
                } else {

                    DialogUtils.showAlertInfo(getActivity(), "Please accept storage permission to download invoice.");
                }
        }
    }



    private class NotificationReceiver extends BroadcastReceiver {
        @Override
        public void onReceive(Context context, Intent intent) {
            if(intent.getAction().equalsIgnoreCase("receiveNotification")){
                if (intent.getExtras().getString("notificationtype").equalsIgnoreCase("ChangeDemoStatus")) {
                    Constants.BOOKING_ID = intent.getExtras().getString("demoid");
                    Constants.BOOK_TYPE = "Demo";
                    callBookingDetailApi();
                } else if(intent.getExtras().getString("notificationtype").equalsIgnoreCase("ChangeMeetingStatus")){
                    Constants.MEETING_ID = intent.getExtras().getString("demoid");
                    Constants.BOOK_TYPE = "Meeting";
                    callMeetingDetailApi();
                }

            }

        }
    }

    @Override
    public void onStop() {
        super.onStop();
        getActivity().unregisterReceiver(notificationReceiver);
    }

    @Override
    public void onResume() {
        super.onResume();
        IntentFilter intentFilter  =new IntentFilter();
        intentFilter.addAction("receiveNotification");
        getActivity().registerReceiver(notificationReceiver,intentFilter);
    }
    private String getDirectionsUrl(LatLng origin, LatLng dest) {

// Origin of route
        String str_origin = "origin=" + origin.latitude + "," + origin.longitude;

        String str_dest = "destination=" + dest.latitude + "," + dest.longitude;

        String parameters = str_origin + "&" + str_dest ;

        String output = "json";
        String url = "https://maps.googleapis.com/maps/api/directions/" + output + "?" + parameters +"&api_key=AIzaSyC28MfWwLdKr-4uxHpMAnYEz1lQS0i7BOk";

        return url;
    }

    public class DownloadTask extends AsyncTask<String, Void, String> {

        // Downloading data in non-ui thread
        @Override
        protected String doInBackground(String... url) {

// For storing data from web service
            String data = "";

            try{
// Fetching the data from web service
                data = downloadUrl(url[0]);
            }catch(Exception e){
                Log.d("Background Task",e.toString());
            }
            return data;
        }

        // Executes in UI thread, after the execution of
// doInBackground()
        @Override
        protected void onPostExecute(String result) {
            super.onPostExecute(result);

            ParserTask parserTask = new ParserTask(fragmentBookingBookedBinding.arrivingTime);

// Invokes the thread for parsing the JSON data
            parserTask.execute(result);

        }
    }
    private String downloadUrl(String strUrl) throws IOException {
        String data = "";
        InputStream iStream = null;
        HttpURLConnection urlConnection = null;
        try{
            URL url = new URL(strUrl);

// Creating an http connection to communicate with url
            urlConnection = (HttpURLConnection) url.openConnection();

// Connecting to url
            urlConnection.connect();

// Reading data from url
            iStream = urlConnection.getInputStream();

            BufferedReader br = new BufferedReader(new InputStreamReader(iStream));

            StringBuffer sb = new StringBuffer();

            String line = "";
            while( ( line = br.readLine()) != null){
                sb.append(line);
            }

            data = sb.toString();

            br.close();

        }catch(Exception e){
            Log.d("Exception while downloading url", e.toString());
        }finally{
            iStream.close();
            urlConnection.disconnect();
        }
        return data;
    }

}
