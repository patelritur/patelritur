package com.demo.home.booking;

import static android.content.Context.ALARM_SERVICE;

import static com.demo.utils.comectChat.call_manager.CometChatStartCallActivity.activity;

import android.annotation.SuppressLint;
import android.app.AlarmManager;
import android.app.Dialog;
import android.app.PendingIntent;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.pm.PackageManager;
import android.content.res.ColorStateList;
import android.media.AudioAttributes;
import android.media.MediaPlayer;
import android.os.Build;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.os.Environment;
import android.os.Handler;
import android.util.Log;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.AlphaAnimation;
import android.view.animation.Animation;
import android.view.inputmethod.EditorInfo;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.core.content.ContextCompat;
import androidx.databinding.DataBindingUtil;
import androidx.fragment.app.Fragment;

import com.cometchat.pro.constants.CometChatConstants;
import com.cometchat.pro.core.CometChat;
import com.cometchat.pro.exceptions.CometChatException;
import com.cometchat.pro.models.User;
import com.demo.R;
import com.demo.carDetails.model.CarDetailRequest;
import com.demo.carDetails.model.CarPorfomaInvoiceModel;
import com.demo.databinding.DialogRecordBinding;
import com.demo.databinding.DialogReviewsBinding;
import com.demo.databinding.FragmentBookingBookedBinding;
import com.demo.databinding.ItemCarFacilitiesBinding;
import com.demo.home.HomeActivity;
import com.demo.home.TakeADemoFragment;
import com.demo.home.booking.model.BookingAcceptModel;
import com.demo.home.booking.model.BookingResponseModel;
import com.demo.home.booking.model.CancelBookingModel;
import com.demo.home.booking.model.LocationResponse;
import com.demo.home.booking.model.StatusRequestModel;
import com.demo.home.booking.model.UpdateStatusRequestModel;
import com.demo.home.model.DirectionResults;
import com.demo.registrationLogin.model.RegistrationResponse;
import com.demo.utils.Constants;
import com.demo.utils.DialogUtils;
import com.demo.utils.DownloadFileFromURL;
import com.demo.utils.FileDownloadReady;
import com.demo.utils.Permissionsutils;
import com.demo.utils.PrintLog;
import com.demo.utils.RecorderUtils;
import com.demo.utils.SharedPrefUtils;
import com.demo.utils.Utils;
import com.demo.utils.comectChat.utils.CallUtils;
import com.demo.webservice.ApiResponseListener;
import com.demo.webservice.RestClient;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.PolylineOptions;

import java.io.File;
import java.io.IOException;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.Locale;
import java.util.Objects;
import java.util.Timer;
import java.util.TimerTask;

import okhttp3.MediaType;
import okhttp3.MultipartBody;
import okhttp3.RequestBody;
import retrofit2.Call;

public class BookingConfirmedFragment extends Fragment implements ApiResponseListener, View.OnClickListener {
    private static final int GET_SPECIALIST_LOCATION = 3;
    private static final int UPDATE_LOCATION = 5;
    private static final int BOOKING_DETAILS = 1;
    private static final int PERFORMA_INVOICE = 4;
    private static final int RECORD_AUDIO = 2;
    //    private static final int UPLOAD_DRIVING_LICENSE = 10;
    private static final int UPDATE_STATUS = 6;
    private static final int GET_ROUTE = 7;
    private static final int GET_ONLY_ROUTE = 8;
    private static final int PLAY_AUDIO = 9;
    private static final int CANCEL_BOOKING = 11;

    private SharedPrefUtils sharedPrefUtils;
    private FragmentBookingBookedBinding fragmentBookingBookedBinding;
    private boolean isRecording;
    private RecorderUtils recorderUtils;
    private boolean fromNotification;
    private BroadcastReceiver notificationReceiver = new NotificationReceiver();
    private BookingAcceptModel bookingAcceptModel;
    private DialogReviewsBinding binding;
    private CarPorfomaInvoiceModel carPorfomaInvoiceModel;
    private static long destinationTime;
    private Timer timer;
    private TimerTask hourlyTask;
    private String specialistId;
    private String userId;
    private DialogRecordBinding dialogRecordBinding;
    private Dialog recordDialog;
    private Long duration;
    private Long distance;
    private CountUpTimer countuptimer;
    private String durationInMin;
    //    private DialogUploadDlBinding dialogUploadDlBinding;
    private CountDownTimer dlUploadcountDownTimer;
//    private Handler m15MinutesTimer,m5minutesTimer;

    public BookingConfirmedFragment(boolean fromNotification) {
        this.fromNotification = fromNotification;
    }

    public BookingConfirmedFragment() {

    }


    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        fragmentBookingBookedBinding = DataBindingUtil.inflate(inflater, R.layout.fragment_booking_booked, container, false);
        Utils.cancelJob(getActivity());
        sharedPrefUtils = new SharedPrefUtils(getActivity());
        userId = sharedPrefUtils.getStringData(Constants.USER_ID);
        timer = new Timer();

      /*  if(sharedPrefUtils.getBooleanData(Constants.LEFT_HOME) )
            fragmentBookingBookedBinding.tvLeftHome.setVisibility(View.GONE);*/

        return fragmentBookingBookedBinding.getRoot();
    }


    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        fragmentBookingBookedBinding.performaInvoice.setOnClickListener(this);
        fragmentBookingBookedBinding.call.setOnClickListener(this);
        fragmentBookingBookedBinding.bookDemo.setOnClickListener(this);
        fragmentBookingBookedBinding.joinCall.setOnClickListener(this);
        fragmentBookingBookedBinding.tvCancel.setOnClickListener(this);
        fragmentBookingBookedBinding.tvContact.setOnClickListener(this);
        fragmentBookingBookedBinding.recordAudio.setOnClickListener(this);
        fragmentBookingBookedBinding.playVoiceMessage.setOnClickListener(this);

        //   fragmentBookingBookedBinding.tvLeftHome.setOnClickListener(this);

        callBookingDetailApi(BOOKING_DETAILS);
    }

    private void callBookingDetailApi(int reqCode) {
        StatusRequestModel statusRequestModel = new StatusRequestModel();

        if (((HomeActivity) getActivity()).getLocation() != null) {
            statusRequestModel.setLatitude(String.valueOf(((HomeActivity) getActivity()).getLocation().getLatitude()));
            statusRequestModel.setLongitude(String.valueOf(((HomeActivity) getActivity()).getLocation().getLongitude()));
        } else {
            statusRequestModel.setLatitude(Constants.LATITUDE != null ? Constants.LATITUDE : "0.0");
            statusRequestModel.setLongitude(Constants.LONGITUDE != null ? Constants.LONGITUDE : "0.0");
        }
        statusRequestModel.setUserID(userId);
        Call objectCall;
        if(Constants.BOOK_TYPE==null)
            Constants.BOOK_TYPE=sharedPrefUtils.getStringData(Constants.BOOK_TYPE_S);
        if (Constants.BOOK_TYPE.equalsIgnoreCase("Demo")) {
            statusRequestModel.setBookingID(Constants.BOOKING_ID);
            objectCall = RestClient.getApiService().getBookingDetails(statusRequestModel);
        }
        else {
            statusRequestModel.setMeetingID(Constants.MEETING_ID);
            objectCall = RestClient.getApiService().getMeetingDetails(statusRequestModel);
        }

        RestClient.makeApiRequest(getActivity(), objectCall, this, reqCode, true);

    }


    private void callPorfomaInvoiceApi() {
        CarDetailRequest carDetailRequest = new CarDetailRequest();
        carDetailRequest.setUserID(userId);
        carDetailRequest.setLatitude(String.valueOf(Constants.LATITUDE));
        carDetailRequest.setLongitude(String.valueOf(Constants.LONGITUDE));
        carDetailRequest.setCarID(bookingAcceptModel.getBookingdetails().getCarID());
        Call objectCall = RestClient.getApiService().getCarPorfomainvoice(carDetailRequest);
        RestClient.makeApiRequest(getActivity(), objectCall, this, PERFORMA_INVOICE, true);
    }


    private void updateStatusApi(String status) {
        UpdateStatusRequestModel updateStatusRequestModel = new UpdateStatusRequestModel();
        updateStatusRequestModel.setUserID(userId);
        updateStatusRequestModel.setBookingID(bookingAcceptModel.bookingID);
        updateStatusRequestModel.setBookingStatus(status);
        Call objectCall;
        if (Constants.BOOK_TYPE.equalsIgnoreCase("Demo"))
            objectCall = RestClient.getApiService().updateDemoStatus(updateStatusRequestModel);
        else
            objectCall = RestClient.getApiService().updateMeetStatus(updateStatusRequestModel);
        RestClient.makeApiRequest(getActivity(), objectCall, this, UPDATE_STATUS, true);

    }



    private void calculateDistance(int getOnlyRoute, String specialistLatitude, String specialistLongitude) {

        Call objectCall = RestClient.getApiService().getRoute(((HomeActivity) getActivity()).locationUtils.getLoc().getLatitude() + "," + ((HomeActivity) getActivity()).locationUtils.getLoc().getLongitude(), specialistLatitude + "," + specialistLongitude);
        RestClient.makeApiRequest(getActivity(), objectCall, this, getOnlyRoute, true);


    }


    /*private void uploadDrivingLicenseToServer(File destination,String dlNumber) {
        MultipartBody.Part filePart = MultipartBody.Part.createFormData("File", destination.getName(), RequestBody.create(MediaType.parse("image/*"), destination));
        Call<RegistrationResponse> call;
        call = RestClient.getApiService().uploadDrivingLicense(filePart, RequestBody.create(MediaType.parse("text/plain"), userId), RequestBody.create(MediaType.parse("text/plain"), dlNumber));

        RestClient.makeApiRequest(getActivity(), call, this, UPLOAD_DRIVING_LICENSE, true);
    }
*/

    private void uploadFileToServer(File destination) {
        MultipartBody.Part filePart = MultipartBody.Part.createFormData("File", destination.getName(), RequestBody.create(MediaType.parse("image/*"), destination));
        Call<RegistrationResponse> call;
        if (Constants.BOOK_TYPE.equalsIgnoreCase("Demo"))
            call = RestClient.getApiService().uploadVoiceMessage(filePart, RequestBody.create(MediaType.parse("text/plain"), userId), RequestBody.create(MediaType.parse("text/plain"), Constants.BOOKING_ID));
        else
            call = RestClient.getApiService().uploadMeetingVoiceMessage(filePart, RequestBody.create(MediaType.parse("text/plain"), userId), RequestBody.create(MediaType.parse("text/plain"), Constants.MEETING_ID));

        RestClient.makeApiRequest(getActivity(), call, this, RECORD_AUDIO, true);
    }


    private void updateCarCheckList() {
        fragmentBookingBookedBinding.flowLayout.removeAllViews();
        if(bookingAcceptModel.getBookingdetails().getCarchecklist().size()>2){
            fragmentBookingBookedBinding.nextArrow.setVisibility(View.VISIBLE);
        }
        for (int i = 0; i < bookingAcceptModel.getBookingdetails().getCarchecklist().size(); i++) {
            ItemCarFacilitiesBinding itemCarFacilitiesBinding = DataBindingUtil.inflate(this.getLayoutInflater(), R.layout.item_car_facilities, null, false);
            itemCarFacilitiesBinding.setCarCheckList(bookingAcceptModel.getBookingdetails().getCarchecklist().get(i));
            fragmentBookingBookedBinding.flowLayout.addView(itemCarFacilitiesBinding.getRoot());
        }
    }

    private void joinCall(BookingAcceptModel bookingAcceptModel) {
        if (bookingAcceptModel.getBookingdetails().getVirtualMeetType().equalsIgnoreCase("Audio")) {

            CometChat.login(userId, Constants.AUTH_KEY, new CometChat.CallbackListener<User>() {
                @Override
                public void onSuccess(User user) {
                    CallUtils.initiateCall(getActivity(), bookingAcceptModel.getBookingdetails().getSpecialistID()
                            , CometChatConstants.RECEIVER_TYPE_USER, "audio");


                    Log.d("TAG", "Login Successful : " + user.toString());
                }

                @Override
                public void onError(CometChatException e) {
                    Log.d("TAG", "Login failed with exception: " + e.getMessage());
                }
            });
        } else {
            CometChat.login(userId, Constants.AUTH_KEY, new CometChat.CallbackListener<User>() {

                @Override
                public void onSuccess(User user) {
                    CallUtils.initiateCall(getActivity(), bookingAcceptModel.getBookingdetails().getSpecialistID(), CometChatConstants.RECEIVER_TYPE_USER, "video");
                    Log.d("TAG", "Login Successful : " + user.toString());
                }

                @Override
                public void onError(CometChatException e) {
                    Log.d("TAG", "Login failed with exception: " + e.getMessage());
                }
            });

        }
    }




    private void showPorfomaInvoiceDialog(CarPorfomaInvoiceModel carPorfomaInvoiceModel) {
        Dialog dialog = new Dialog(getActivity());
        binding = DataBindingUtil.inflate(LayoutInflater.from(getActivity()), R.layout.dialog_reviews, null, false);
        dialog.setContentView(binding.getRoot());
        binding.close.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dialog.dismiss();
            }
        });
        binding.textviewTitle.setText(getString(R.string.porfoma_invoice));
        binding.setCarInvoice(carPorfomaInvoiceModel.getPorfomainvoice());
        this.carPorfomaInvoiceModel = carPorfomaInvoiceModel;
        binding.layoutInvoice.getRoot().setVisibility(View.VISIBLE);
        binding.recyclerviewReview.setVisibility(View.GONE);
        binding.imageviewDownload.setVisibility(View.VISIBLE);
        binding.imageviewDownload.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (!Permissionsutils.checkForStoragePermission(getActivity())) {
                    Permissionsutils.askForStoragePermission(getActivity());
                } else {
                    DownloadFileFromURL();
                }


            }
        });
        binding.executePendingBindings();
        Utils.showDilaog(dialog);
    }

    private void DownloadFileFromURL() {
        new DownloadFileFromURL(carPorfomaInvoiceModel.getPorfomainvoice().downloadInvoiceURL, carPorfomaInvoiceModel.getPorfomainvoice().getCarName(), new FileDownloadReady() {
            @Override
            public void onFileDownloaded(String fileId) {
                Utils.showToast(getActivity(), "Performa invoice is downloaded successfullly");
            }
        }).execute();
    }




    /*private void showDLUploadDialog() {

        uplodDlDialog = new Dialog(getActivity());
        dialogUploadDlBinding = DataBindingUtil.inflate(LayoutInflater.from(getActivity()), R.layout.dialog_upload_dl, null, false);
        uplodDlDialog.setContentView(dialogUploadDlBinding.getRoot());
        if(dlUploadcountDownTimer!=null)
        {
            dlUploadcountDownTimer.cancel();
        }
        dlUploadcountDownTimer =  start5minutesTimer(dialogUploadDlBinding.warning);
        dialogUploadDlBinding.close.setOnClickListener(view -> {
            uplodDlDialog.dismiss();
            DialogUtils.showConfirmDialog(getActivity(),"Are you sure?if you close it this booking will be cancelled.",(dialogInterface, i) -> {

                dlUploadcountDownTimer.cancel();
                dlUploadcountDownTimer.onFinish();
            },(dialogInterface, i) -> {

                uplodDlDialog.show();

            },"Yes, Cancel it","Upload Driving License");
        });
        dialogUploadDlBinding.uplaodDl.setOnClickListener(view -> {
*//*
            if (!Permissionsutils.checkForStoragePermission(getActivity())) {
                Permissionsutils.askForStoragePermission(getActivity(),Constants.DL);
            } else {
                pickDL();
            }*//*



        });

        Utils.showDilaog(uplodDlDialog);
        uplodDlDialog.setCancelable(false);


    }

    private void pickDL() {
//        Intent intent = new Intent();
//        intent.setAction(Intent.ACTION_GET_CONTENT);
//        intent.setType("image/*");
//        ((Activity)getActivity()).startActivityForResult(intent, Constants.DL);
    }*/


    private void callcancelBookingApi() {

        CancelBookingModel cancelBookingModel = new CancelBookingModel();
        cancelBookingModel.setCancelReason("auto cancel");
        cancelBookingModel.setBlockDealerStatus("N");
        Call objectCall;
        if(Constants.BOOK_TYPE.equalsIgnoreCase("Demo")) {
            cancelBookingModel.bookingID = Constants.BOOKING_ID;
            objectCall = RestClient.getApiService().cancelBooking(cancelBookingModel);
        }
        else {
            cancelBookingModel.meetingID = Constants.MEETING_ID;
            objectCall = RestClient.getApiService().cancelMeeting(cancelBookingModel);
        }

        RestClient.makeApiRequest(getActivity(), objectCall, this, CANCEL_BOOKING, true);



    }

    private void showRecordDialog() {
        recordDialog = new Dialog(getActivity());
        dialogRecordBinding = DataBindingUtil.inflate(LayoutInflater.from(getActivity()), R.layout.dialog_record, null, false);
        recordDialog.setContentView(dialogRecordBinding.getRoot());
        dialogRecordBinding.close.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                recordDialog.dismiss();
            }
        });
        dialogRecordBinding.sendSpecialist.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                try {
                    if (recorderUtils != null) {
                        dialogRecordBinding.statusRecord.setText("Done");
                        recorderUtils.stopRecording();
                        isRecording = false;
                        String mFileName = getActivity().getExternalFilesDir(null).getAbsolutePath();;
                        mFileName += "/AudioRecording.mp3";

                        uploadFileToServer(new File(mFileName));
                    }
                }
                catch (Exception e){
                    e.printStackTrace();
                }
            }
        });
        dialogRecordBinding.remove.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dialogRecordBinding.recordIv.setImageResource(R.drawable.ic_record);
                dialogRecordBinding.statusRecord.setText("Record");
                dialogRecordBinding.sendSpecialist.setVisibility(View.INVISIBLE);
                dialogRecordBinding.remove.setVisibility(View.INVISIBLE);
                dialogRecordBinding.timeRecord.setText("00:00");
                recorderUtils.stopRecording();
                isRecording=false;
                if(countuptimer!=null)
                    countuptimer.cancel();
                countuptimer= null;
            }
        });
        dialogRecordBinding.recordIv.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (!isRecording) {
                    if (Permissionsutils.checkForRecordPermission(getActivity())) {
                        dialogRecordBinding.recordIv.setImageResource(R.mipmap.record_pause);
                        dialogRecordBinding.statusRecord.setText("Recording");
                        recorderUtils = new RecorderUtils();
                        isRecording = true;
                        recorderUtils.startRecording(getActivity());
                        if(countuptimer!=null)
                        {
                            countuptimer.cancel();
                            countuptimer= null;
                        }
                        countuptimer = new CountUpTimer(30000) {
                            public void onTick(int second) {
                                if(second<10)
                                    dialogRecordBinding.timeRecord.setText("00:0"+String.valueOf(second));
                                else
                                    dialogRecordBinding.timeRecord.setText("00:"+String.valueOf(second));
                            }
                        };

                        countuptimer.start();

                    } else
                        Permissionsutils.askForRecordPermission(getActivity());

                } else {
                    dialogRecordBinding.recordIv.setImageResource(R.mipmap.record_stop);
                    dialogRecordBinding.sendSpecialist.setVisibility(View.VISIBLE);
                    dialogRecordBinding.remove.setVisibility(View.VISIBLE);
                    if(countuptimer!=null)
                        countuptimer.cancel();
                    countuptimer= null;
//                    countDownTimer.cancel();
//                    fragmentBookingBookedBinding.recordAudio.setClickable(true);
//                    fragmentBookingBookedBinding.recordAudio.setImageDrawable(getContext().getDrawable(R.drawable.ic_record));
//                    countDownTimer.onFinish();

                }

            }
        });
        Utils.showDilaog(recordDialog);
    }



    private void playAudio() {

        fragmentBookingBookedBinding.playVoiceMessage.setImageResource(R.mipmap.record_pause);
        String audioUrl = bookingAcceptModel.bookingdetails.getVoiceRecordingAudio();
        MediaPlayer mediaPlayer = new MediaPlayer();
        mediaPlayer.setAudioAttributes(
                new AudioAttributes
                        .Builder()
                        .setContentType(AudioAttributes.CONTENT_TYPE_MUSIC)
                        .build());


        try {
            mediaPlayer.setDataSource(audioUrl);
            mediaPlayer.prepare();
            mediaPlayer.start();

        } catch (IOException e) {
            e.printStackTrace();
        }
        mediaPlayer.setOnCompletionListener(new MediaPlayer.OnCompletionListener() {
            @Override
            public void onCompletion(MediaPlayer mediaPlayer) {
                fragmentBookingBookedBinding.playVoiceMessage.setImageResource(R.drawable.ic_play);
                fragmentBookingBookedBinding.newVoiceMessage.setVisibility(View.GONE);
                fragmentBookingBookedBinding.newVoiceMessage.clearAnimation();
            }
        });
        Toast.makeText(getActivity(), "Audio started playing..", Toast.LENGTH_SHORT).show();
    }



    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.call:
                CometChat.login(userId, Constants.AUTH_KEY, new CometChat.CallbackListener<User>() {
                    @Override
                    public void onSuccess(User user) {
                        CallUtils.initiateCall(getActivity(), bookingAcceptModel.getBookingdetails().getSpecialistID()
                                , CometChatConstants.RECEIVER_TYPE_USER, "audio");


                        Log.d("TAG", "Login Successful : " + user.toString());
                    }

                    @Override
                    public void onError(CometChatException e) {
                        Log.d("TAG", "Login failed with exception: " + e.getMessage());
                    }
                });
                break;
            case R.id.join_call:
                if (bookingAcceptModel != null && bookingAcceptModel.getBookingdetails().getMeetingType() != null && bookingAcceptModel.getBookingdetails().getVirtualMeetStatus().equalsIgnoreCase("Y")) {
                    joinCall(bookingAcceptModel);
                }
                break;
            case R.id.tv_cancel:
                showCancelAlert();

                break;
            case R.id.tv_contact:
                fragmentBookingBookedBinding.call.performClick();

                break;
            case R.id.performa_invoice:
                callPorfomaInvoiceApi();
                break;
            case R.id.book_demo:
                ((HomeActivity) getActivity()).showFragment(new TakeADemoFragment("0"));
                break;
            case R.id.record_audio:
                showRecordDialog();
                break;
            case R.id.play_voice_message:
                if (bookingAcceptModel.getBookingdetails()!=null && bookingAcceptModel.getBookingdetails().getVoiceRecordingAudio() != null && bookingAcceptModel.getBookingdetails().getVoiceRecordingAudio().trim().length() > 0)
                    playAudio();
                else
                    callBookingDetailApi(PLAY_AUDIO);
                break;
            /*case R.id.tv_left_home:
                if (hourlyTask == null && timer!=null ) {
                    setTask();
                    timer.schedule(hourlyTask, 0l, Constants.WAIT_MINUTE);

                }
                sharedPrefUtils.saveData(Constants.LEFT_HOME,true);
                fragmentBookingBookedBinding.tvLeftHome.setVisibility(View.GONE);
                break;*/
        }

    }

    private void showCancelAlert() {
        DialogUtils.showConfirmDialog(getActivity(), "Are you sure you want to cancel this " + Constants.BOOK_TYPE+"?", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                ((HomeActivity) getActivity()).showFragment(new CancelDemoFragment());
            }
        }, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {

            }
        },"YES","NO");
    }

    private void setTask() {

        hourlyTask = new TimerTask() {
            @Override
            public void run() {
                if (bookingAcceptModel.getBookingdetails().getDemoType().equalsIgnoreCase("At Home")) {
                    Call objectCall = RestClient.getApiService().getSpecialistLocation(specialistId);
                    RestClient.makeApiRequest(getActivity(), objectCall, BookingConfirmedFragment.this, GET_SPECIALIST_LOCATION, false);
                } else {
                    //update customer location in case of booking at dealership after user has left home
                    StatusRequestModel statusRequestModel = new StatusRequestModel();
                    if (((HomeActivity) getActivity()) != null && ((HomeActivity) getActivity()).getLocation() != null) {
                        statusRequestModel.setLatitude(String.valueOf(((HomeActivity) getActivity()).getLocation().getLatitude()));
                        statusRequestModel.setLongitude(String.valueOf(((HomeActivity) getActivity()).getLocation().getLongitude()));
                    } else {
                        statusRequestModel.setLatitude(Constants.LATITUDE != null ? Constants.LATITUDE : "0.0");
                        statusRequestModel.setLongitude(Constants.LONGITUDE != null ? Constants.LONGITUDE : "0.0");
                    }
                    statusRequestModel.setUserID(userId);
                    Call objectCall = RestClient.getApiService().updateCustomerLocation(statusRequestModel);
                    RestClient.makeApiRequest(getActivity(), objectCall, BookingConfirmedFragment.this, UPDATE_LOCATION, false);
                }
            }
        };

    }


    private void endTask() {
        if (hourlyTask != null)
            hourlyTask.cancel();
        if (timer != null)
            timer.cancel();
        timer = null;

    }


    @SuppressLint("SetTextI18n")
    @Override
    public void onApiResponse(Call<Object> call, Object response, int reqCode) throws Exception {

        if (reqCode == BOOKING_DETAILS) {
            bookingAcceptModel = (BookingAcceptModel) response;
            if (bookingAcceptModel.getResponseCode().equalsIgnoreCase("200")) {
                fragmentBookingBookedBinding.setBookingDetail(bookingAcceptModel);
                Date currentDate = new Date();
                SimpleDateFormat format = new SimpleDateFormat("MMM dd, yyyy hh:mmaa", Locale.US);
                String bookingDate = bookingAcceptModel.BookingDate;
                String bookingTime = bookingAcceptModel.BookingTime;
                String bookingDateTimeString = bookingDate + " " + bookingTime;
                Date  bookingDateTime = format.parse(bookingDateTimeString);

                if (bookingDateTime != null && bookingDateTime.after(currentDate)) {
                    PrintLog.v("tvs vs tvs =====schedule"+bookingDateTime.after(currentDate)+bookingDateTime+currentDate);
                    if(!Utils.dateDiff1Hour(bookingDateTimeString)) {
                        setLocalOnTimeReminder(bookingDateTime);
                        setLocal15MinutesBeforeReminder(bookingDateTime);
                    }

                } else {
                    // The booking date and time is on or before the current date and time
                    PrintLog.v("now");

                    sharedPrefUtils.saveData(Constants.SNAME, bookingAcceptModel.getBookingdetails().getSpecialistName());
                    fragmentBookingBookedBinding.submessage.setText(bookingAcceptModel.getBookingSubMessage());
                    specialistId = bookingAcceptModel.getBookingdetails().getSpecialistID();
                    updateCarCheckList();
//                if(bookingAcceptModel.getBookingdetails().getDemoType().equalsIgnoreCase("At Home")){
//                    fragmentBookingBookedBinding.tvCancel.setText("Contact Demo Specialist");
//                }
                    if (bookingAcceptModel.getBookingdetails() != null && bookingAcceptModel.getBookingdetails().getVoiceRecordingAudio() != null && bookingAcceptModel.getBookingdetails().getVoiceRecordingAudio().trim().length() > 0)
                        fragmentBookingBookedBinding.playVoiceMessage.setVisibility(View.VISIBLE);
                    else
                        fragmentBookingBookedBinding.playVoiceMessage.setVisibility(View.GONE);

                    fragmentBookingBookedBinding.executePendingBindings();
              /*  if(sharedPrefUtils.getBooleanData(Constants.LEFT_HOME) )
                    fragmentBookingBookedBinding.tvLeftHome.setVisibility(View.GONE);*/
                    if (!(bookingAcceptModel.getBookingdetails().getVirtualMeetStatus() != null && bookingAcceptModel.getBookingdetails().getVirtualMeetStatus().equalsIgnoreCase("Y"))) {
                        startTimerForlocationUpdate();
                        //remove driving license need as on 22/12
                       /* if ((sharedPrefUtils.getStringData(Constants.IsDLUploadStatus) != null && sharedPrefUtils.getStringData(Constants.IsDLUploadStatus).equalsIgnoreCase("Y"))) {
                            startTimerForlocationUpdate();
                        } else {
                            showDLUploadDialog();
                        }*/
                    }
                    if(sharedPrefUtils.getBooleanData(Constants.OTP_SHOW)){
                        bookingAcceptModel.bookingdetails.setCustomDemoStatusId("2");
                        fragmentBookingBookedBinding.setBookingDetail(bookingAcceptModel);
                        fragmentBookingBookedBinding.executePendingBindings();
                    }
                    switch (bookingAcceptModel.getBookingdetails().getDemoStatusId()) {

                        case "5":
                            fragmentBookingBookedBinding.status.setText("Completed");
                            new Handler().postDelayed(new Runnable() {
                                @SuppressLint("SuspiciousIndentation")
                                @Override
                                public void run() {
                                    endTask();
                                    sharedPrefUtils.saveData(Constants.BOOKING_ONGOING, "null");
                                    sharedPrefUtils.saveData(Constants.OTP_SHOW, false);
                                    sharedPrefUtils.saveData(Constants.TIMER, "null");
                                    sharedPrefUtils.saveData(Constants.BOOK_TYPE_S, "null");
                                    if ((((HomeActivity) getActivity())) != null)
                                        ((HomeActivity) getActivity()).showFragment(new BookingFeedbackFragment(bookingAcceptModel.getBookingdetails().getSpecialistName(), bookingAcceptModel.getBookingdetails().getSpecialistID()));
                                }
                            }, 200);
                            break;
                        case "2":
                            sharedPrefUtils.saveData(Constants.OTP_SHOW,true);
                            bookingAcceptModel.bookingdetails.setCustomDemoStatusId("2");
                            fragmentBookingBookedBinding.setBookingDetail(bookingAcceptModel);
                            fragmentBookingBookedBinding.executePendingBindings();
                        case "3":

                            fragmentBookingBookedBinding.call.setVisibility(View.GONE);
                            fragmentBookingBookedBinding.arrivingTime.setVisibility(View.GONE);
                            break;
                        case "4":
//                        fragmentBookingBookedBinding.status.setText(getString(R.string.arrived));

                            sharedPrefUtils.saveData(Constants.TIMER,"null");

                            fragmentBookingBookedBinding.status.setText("Started");
                            if (!fromNotification && bookingAcceptModel.getBookingdetails().getMeetingType() != null && bookingAcceptModel.getBookingdetails().getMeetingType().equalsIgnoreCase("now") && bookingAcceptModel.getBookingdetails().getVirtualMeetStatus().equalsIgnoreCase("Y")) {
                                if (bookingAcceptModel.getBookingdetails().getVirtualMeetStatus().equalsIgnoreCase("Y")) {
                                    fragmentBookingBookedBinding.joinCall.setVisibility(View.VISIBLE);
                                }

                            }
                        /*sharedPrefUtils.saveData(Constants.LEFT_HOME,true);
                        fragmentBookingBookedBinding.tvLeftHome.setVisibility(View.GONE);*/
                            ((HomeActivity) requireActivity()).locationUtils.removePolyLine();
                            endTask();
                            break;
                        case "7":
                            endTask();
                            fragmentBookingBookedBinding.status.setText(getString(R.string.rejected));
                            fragmentBookingBookedBinding.bookDemo.setVisibility(View.VISIBLE);
                            fragmentBookingBookedBinding.tvLeftHome.setVisibility(View.GONE);
                            fragmentBookingBookedBinding.joinCall.setVisibility(View.GONE);
                            ((HomeActivity)getActivity()).showFragment(new BookingStatusFragment(bookingAcceptModel.bookingID,Constants.BOOK_TYPE));

                           /* if (Constants.BOOK_TYPE.equalsIgnoreCase("Demo"))
                                fragmentBookingBookedBinding.bookDemo.setText(getString(R.string.takedemos));
                            else
                                fragmentBookingBookedBinding.bookDemo.setText(getString(R.string.book_a_meeting));*/

                            break;
                        case "9":
                            fragmentBookingBookedBinding.status.setText("Arriving Soon");
                            fragmentBookingBookedBinding.call.setVisibility(View.GONE);
                            fragmentBookingBookedBinding.arrivingTime.setVisibility(View.GONE);
                            break;
                        case "10":
                            endTask();
                       /* sharedPrefUtils.saveData(Constants.LEFT_HOME,true);
                        fragmentBookingBookedBinding.tvLeftHome.setVisibility(View.GONE);*/
                            fragmentBookingBookedBinding.call.setVisibility(View.VISIBLE);
                            fragmentBookingBookedBinding.status.setText(getString(R.string.arrived));

                            break;

                        case "13":
                            fragmentBookingBookedBinding.status.setText(getString(R.string.delayed));
                            break;

                        default:
                            PrintLog.v("default====");
                            if (!fromNotification && bookingAcceptModel.getBookingdetails().getMeetingType() != null && bookingAcceptModel.getBookingdetails().getMeetingType().equalsIgnoreCase("now") && bookingAcceptModel.getBookingdetails().getVirtualMeetStatus().equalsIgnoreCase("Y")) {
                                if (bookingAcceptModel.getBookingdetails().getVirtualMeetStatus().equalsIgnoreCase("Y")) {
                                    fragmentBookingBookedBinding.joinCall.setVisibility(View.VISIBLE);
                                }

                            }
                            break;


                    }

                    //  ((HomeActivity) getActivity()).locationUtils.drawOnMap(bookingAcceptModel.bookingdetails.getSpecialistLatitude(), bookingAcceptModel.bookingdetails.getSpecialistLongitude());
                    ((HomeActivity) getActivity()).setPeekheightBookingConfirmed();
                }
            }
        }
        else if(reqCode==PLAY_AUDIO){
            bookingAcceptModel = (BookingAcceptModel) response;
            /*if(sharedPrefUtils.getBooleanData(Constants.LEFT_HOME))
                fragmentBookingBookedBinding.tvLeftHome.setVisibility(View.GONE);*/
            if (bookingAcceptModel.getBookingdetails()!=null && bookingAcceptModel.getBookingdetails().getVoiceRecordingAudio() != null && bookingAcceptModel.getBookingdetails().getVoiceRecordingAudio().trim().length() > 0)
                playAudio();
        }
        else if (reqCode == RECORD_AUDIO) {
//            fragmentBookingBookedBinding.recordingCardview.setVisibility(View.GONE);
            RegistrationResponse registrationResponse = (RegistrationResponse) response;
            recordDialog.dismiss();
            Utils.showToast(getActivity(), registrationResponse.getDescriptions());
        } else if (reqCode == GET_SPECIALIST_LOCATION) {
            LocationResponse locationResponse = ((LocationResponse) response);
            calculateDistance(GET_ROUTE, locationResponse.getUserLatitude(), locationResponse.getUserLongitude());

        } else if (reqCode == PERFORMA_INVOICE) {
            CarPorfomaInvoiceModel carDetailReviewModel = (CarPorfomaInvoiceModel) response;
            showPorfomaInvoiceDialog(carDetailReviewModel);
        } else if (reqCode == UPDATE_LOCATION) {

            calculateDistance(GET_ROUTE, bookingAcceptModel.getBookingdetails().getSpecialistLatitude(), bookingAcceptModel.getBookingdetails().getSpecialistLongitude());

        } else if (reqCode == GET_ROUTE || reqCode==GET_ONLY_ROUTE) {
            DirectionResults directionResults = (DirectionResults) response;
            DirectionResults.Location startLocation = null,endLocation=null;
            ArrayList<LatLng> routelist = new ArrayList<LatLng>();
            if (directionResults.getRoutes().size() > 0) {
                ArrayList<LatLng> decodelist;
                DirectionResults.Route routeA = directionResults.getRoutes().get(0);
                if (routeA.getLegs().size() > 0) {
                    List<DirectionResults.Steps> steps = routeA.getLegs().get(0).getSteps();
                    duration = routeA.getLegs().get(0).getDuration().getValue();//seconds
                    distance = routeA.getLegs().get(0).getDistance().getValue();//meters
                    if(distance>2000) {
                        fragmentBookingBookedBinding.distanceText.setText("(" + routeA.getLegs().get(0).getDistance().getText() + " away, " + routeA.getLegs().get(0).getDuration().getText() + ")");
                    }else {
                        /*PrintLog.v("Timmer===startout"+sharedPrefUtils.getStringData(Constants.TIMER));
                        if(sharedPrefUtils.getStringData(Constants.TIMER)==null || sharedPrefUtils.getStringData(Constants.TIMER).equalsIgnoreCase("null")) {
                            start15minutesTimer();
                            PrintLog.v("Timmer===startloop");
                        }*/
                        fragmentBookingBookedBinding.distanceText.setText("(" +"Reached"+ ")");

//                        fragmentBookingBookedBinding.distanceText.setText("(" + routeA.getLegs().get(0).getDuration().getText() + ")");
                    }
                    durationInMin=routeA.getLegs().get(0).getDuration().getText();
                    if (fragmentBookingBookedBinding.address.getText().toString().trim().length() == 0)
                        fragmentBookingBookedBinding.address.setText(routeA.getLegs().get(0).getStart_address());
                    endLocation = routeA.getLegs().get(0).getEndLocation();
                    DirectionResults.Steps step;
                    DirectionResults.Location location;
                    String polyline;
                    for (int i = 0; i < steps.size(); i++) {
                        step = steps.get(i);
                        location = step.getStart_location();
                        routelist.add(new LatLng(location.getLat(), location.getLng()));
                        polyline = step.getPolyline().getPoints();
                        decodelist = RouteDecode.decodePoly(polyline);
                        routelist.addAll(decodelist);
                        location = step.getEnd_location();
                        routelist.add(new LatLng(location.getLat(), location.getLng()));
                    }
                }

                if (routelist.size() > 0) {
                    PolylineOptions rectLine = new PolylineOptions().width(10).color(
                            ContextCompat.getColor(getActivity(),R.color.color_241e61));

                    for (int i = 0; i < routelist.size(); i++) {
                        rectLine.add(routelist.get(i));
                    }
                    ((HomeActivity) getActivity()).locationUtils.addPolyLine(rectLine,  endLocation, bookingAcceptModel.getBookingdetails().getDemoType(),durationInMin);

                }
            }


            if(reqCode==GET_ROUTE && bookingAcceptModel.getBookingdetails().getDemoType().equalsIgnoreCase("At Home")) {
                if (destinationTime == 0)
                    destinationTime = Utils.getNextTime(Math.toIntExact(duration));
                PrintLog.v("===diff==="+(Utils.getNextTime(Math.round(duration)) - destinationTime));
                PrintLog.v("=====duration="+(Utils.getNextTime(Math.round(duration))) );
                PrintLog.v("===destinationTime===="+ destinationTime);
                if (distance <= 200) {
                    fragmentBookingBookedBinding.status.setText("Arrived");
                   /* sharedPrefUtils.saveData(Constants.LEFT_HOME,true);
                    fragmentBookingBookedBinding.tvLeftHome.setVisibility(View.GONE);*/
                    updateStatusApi("10");

                } else if (distance <= 1000) {
                    fragmentBookingBookedBinding.status.setText("Arriving Soon");
                    updateStatusApi("9");
                } else if ((Utils.getNextTime(Math.round(duration)) - destinationTime) < 60000)
                    fragmentBookingBookedBinding.status.setText("On Time");
                else {
                    fragmentBookingBookedBinding.status.setText("Delayed");
                    updateStatusApi("13");
                }
            }
        }
        /*else if(reqCode==UPLOAD_DRIVING_LICENSE){
            RegistrationResponse registrationResponse = (RegistrationResponse) response;
            if(registrationResponse.getResponseCode().equalsIgnoreCase("200")){
                dlUploadcountDownTimer.cancel();
                dlUploadcountDownTimer=null;
                uplodDlDialog.dismiss();
                sharedPrefUtils.saveData(Constants.IsDLUploadStatus,"Y");
                startTimerForlocationUpdate();
            }
            Utils.showToast(getActivity(),registrationResponse.getDescriptions());
        }*/
        else if(reqCode==CANCEL_BOOKING){
            BookingResponseModel registrationResponse = (BookingResponseModel) response;
            Utils.showToast(getActivity(),registrationResponse.getDescriptions());
            endTask();
            sharedPrefUtils.saveData(Constants.BOOKING_ONGOING,"null");
            sharedPrefUtils.saveData(Constants.OTP_SHOW, false);
            sharedPrefUtils.saveData(Constants.BOOK_TYPE_S,"null");
            sharedPrefUtils.saveData(Constants.TIMER,"null");
            getActivity().startActivity(new Intent(getActivity(), HomeActivity.class));
            getActivity().finish();

        }



    }

    /*private void start15minutesTimer() {
        m15MinutesTimer =   new Handler();
        sharedPrefUtils.saveData(Constants.TIMER,"15");
        PrintLog.v("15 Timmer start==");
        m15MinutesTimer.postDelayed(new Runnable() {
            @Override
            public void run() {
                // call your method here
                sharedPrefUtils.saveData(Constants.TIMER,"5");
                start5MinutesTimer();
                DialogUtils.showConfirmDialog(getActivity(), "Do you want to extend for 5 minutes?" , new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        m15MinutesTimer.removeCallbacksAndMessages(null);
                        sharedPrefUtils.saveData(Constants.TIMER,"5");
                        start5MinutesTimer();
                    }
                }, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        m15MinutesTimer.removeCallbacksAndMessages(null);
                        callcancelBookingApi();
                    }
                },"YES","NO");
            }
        }, 30000);
    }

    private void start5MinutesTimer() {
        PrintLog.v("5 Timmer start==");
        m5minutesTimer =   new Handler();
        m5minutesTimer.postDelayed(new Runnable() {
            @Override
            public void run() {
                // call your method here
                PrintLog.v("5 Timmer finish==");
                callcancelBookingApi();
            }
        }, 1*60000);
    }*/

    private void setLocal15MinutesBeforeReminder(Date bookingDateTime) {
        AlarmManager alarmManager = (AlarmManager) getActivity().getSystemService(ALARM_SERVICE);
        Intent intent = new Intent(getActivity(), My15MinutesLocalReceiver.class);
        intent.putExtra("is15MinutesBefore",true);
        intent.putExtra("bookingId",bookingAcceptModel.bookingID);
        intent.putExtra("demoType",Constants.BOOK_TYPE);
        PendingIntent pendingIntent = PendingIntent.getBroadcast(getActivity(), 0, intent, PendingIntent.FLAG_IMMUTABLE);
        Calendar calendar = Calendar.getInstance();
        calendar.setTime(bookingDateTime);
        calendar.add(Calendar.MINUTE,-15);
        alarmManager.set(AlarmManager.RTC_WAKEUP, calendar.getTime().getTime(), pendingIntent);
    }

    private void setLocalOnTimeReminder(Date bookingDateTime) {
        PrintLog.v("tvs vs tvs =====scheduleontime");
        AlarmManager alarmManager = (AlarmManager) getActivity().getSystemService(ALARM_SERVICE);
        Intent intent = new Intent(getActivity(), MyLocalReceiver.class);
        intent.putExtra("is15MinutesBefore",false);
        intent.putExtra("bookingId",bookingAcceptModel.bookingID);
        intent.putExtra("demoType",Constants.BOOK_TYPE);
        PendingIntent pendingIntent = PendingIntent.getBroadcast(getActivity(), 1, intent, PendingIntent.FLAG_IMMUTABLE);
        Calendar calendar = Calendar.getInstance();
        calendar.setTime(bookingDateTime);
        calendar.add(Calendar.MINUTE,-5);
        alarmManager.set(AlarmManager.RTC_WAKEUP, calendar.getTime().getTime(), pendingIntent);

    }

    private void startTimerForlocationUpdate() {
        if (hourlyTask == null && (!fromNotification /*&& bookingAcceptModel.getBookingdetails().getDemoType() != null && bookingAcceptModel.getBookingdetails().getDemoType().equalsIgnoreCase("At Home"))*/)) {
            setTask();
            calculateDistance(GET_ONLY_ROUTE, bookingAcceptModel.getBookingdetails().getSpecialistLatitude(), bookingAcceptModel.getBookingdetails().getSpecialistLongitude());
            //   getHomeAddress();
            new Handler().postDelayed(new Runnable() {
                @Override
                public void run() {
                    if (timer != null)
                        timer.schedule(hourlyTask, 0, Constants.WAIT_MINUTE);
                }
            },  Constants.WAIT_MINUTE);
        }
    }


    private void getHomeAddress() {
        fragmentBookingBookedBinding.changeAddress.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                fragmentBookingBookedBinding.address.setEnabled(true);
                fragmentBookingBookedBinding.address.requestFocus();
                fragmentBookingBookedBinding.address.setBackgroundTintList( ColorStateList.valueOf( getResources().getColor(R.color.color_474747 )) );
                Utils.showKeyboard(getActivity());
                fragmentBookingBookedBinding.address.setOnEditorActionListener(new TextView.OnEditorActionListener() {
                    @Override
                    public boolean onEditorAction(TextView v, int actionId, KeyEvent event) {
                        if ((event != null && (event.getKeyCode() == KeyEvent.KEYCODE_ENTER)) || (actionId == EditorInfo.IME_ACTION_DONE) ||  (actionId == EditorInfo.IME_ACTION_NEXT)) {
                            fragmentBookingBookedBinding.address.setBackground(null);
                            fragmentBookingBookedBinding.address.setEnabled(false);
                        }
                        return false;
                    }
                });

            }
        });



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
                    boolean permissionToStore;
                    if(Build.VERSION.SDK_INT<Build.VERSION_CODES.R)
                         permissionToStore = grantResults[1] == PackageManager.PERMISSION_GRANTED;
                    else
                         permissionToStore=true;
                    if (permissionToRecord && permissionToStore) {
                        dialogRecordBinding.recordIv.setImageResource(R.mipmap.record_pause);
                        dialogRecordBinding.statusRecord.setText("Recording");
                        recorderUtils=   new RecorderUtils();
                        isRecording = true;
                        recorderUtils.startRecording(getActivity());
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
                break;
          /*  case Constants.DL:
                pickDL();
                break;*/
        }
    }

    public void onCallEnded() {
        if (bookingAcceptModel!=null && bookingAcceptModel.getBookingdetails().getVirtualMeetStatus().equalsIgnoreCase("Y")) {
            fragmentBookingBookedBinding.joinCall.setText("Restart Call");
            fragmentBookingBookedBinding.reconnect.setVisibility(View.VISIBLE);
        }
    }



    private class NotificationReceiver extends BroadcastReceiver {
        @Override
        public void onReceive(Context context, Intent intent) {
            if(intent.getAction().equalsIgnoreCase("receiveNotification")){
                if (intent.getExtras().getString("notificationtype").equalsIgnoreCase("ChangeDemoStatus")) {
                    Constants.BOOKING_ID = intent.getExtras().getString("demoid");
                    Constants.BOOK_TYPE = "Demo";
                    callBookingDetailApi(BOOKING_DETAILS);
                } else if(intent.getExtras().getString("notificationtype").equalsIgnoreCase("ChangeMeetingStatus")) {
                    Constants.MEETING_ID = intent.getExtras().getString("demoid");
                    Constants.BOOK_TYPE = "Meeting";
                    callBookingDetailApi(BOOKING_DETAILS);
                }

                if(intent.getExtras().getString("notificationtype").contains("SpecialistDemoVoiceRecording") ||
                        intent.getExtras().getString("notificationtype").contains("SpecialistMeetVoiceRecording")){
                    if( intent.getExtras().getString("notificationtype").equalsIgnoreCase("SpecialistDemoVoiceRecording")){
                        Constants.BOOKING_ID = intent.getExtras().getString("demoid");
                        Constants.BOOK_TYPE = "Demo";

                    }
                    else{
                        Constants.MEETING_ID = intent.getExtras().getString("demoid");
                        Constants.BOOK_TYPE = "Meeting";
                    }
                    callBookingDetailApi(PLAY_AUDIO);
                    fragmentBookingBookedBinding.newVoiceMessage.setVisibility(View.VISIBLE);
                    Animation anim = new AlphaAnimation(0.0f, 1.0f);
                    anim.setDuration(100); //You can manage the blinking time with this parameter
                    anim.setStartOffset(20);
                    anim.setRepeatMode(Animation.REVERSE);
                    anim.setRepeatCount(Animation.INFINITE);
                    fragmentBookingBookedBinding.newVoiceMessage.startAnimation(anim);
                    fragmentBookingBookedBinding.playVoiceMessage.setVisibility(View.VISIBLE);
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
        if(activity!=null && activity.callended){
            onCallEnded();
        }


    }

  /*  @Override
    public void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        if (resultCode == RESULT_OK) {

            // Get the Uri of the selected file
            Uri uri = data.getData();
            String fullFilePath = UriUtils.getPathFromUri(getActivity(),uri);

            File destination = new File(fullFilePath);
            if(destination.exists())
            {
                uploadDrivingLicenseToServer(destination,dialogUploadDlBinding.edittextDl.getText().toString());

               *//* if(dialogUploadDlBinding.warning.getText().toString().trim().length()>0 &&Utils.isvalidDl(dialogUploadDlBinding.warning.getText().toString()))
                    uploadDrivingLicenseToServer(destination,dialogUploadDlBinding.warning.getText().toString());
                else if(dialogUploadDlBinding.warning.getText().toString().trim().length()>0)
                    Utils.showToast(getActivity(),"Please enter valid Driving License Number");
            *//*}

        }
*/





}
