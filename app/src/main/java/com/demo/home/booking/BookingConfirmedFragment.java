package com.demo.home.booking;

import android.annotation.SuppressLint;
import android.app.Dialog;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.pm.PackageManager;
import android.content.res.ColorStateList;
import android.media.AudioAttributes;
import android.media.MediaPlayer;
import android.os.Bundle;
import android.os.Environment;
import android.os.Handler;
import android.util.Log;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.view.inputmethod.EditorInfo;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
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
import java.util.ArrayList;
import java.util.List;
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
    private static final int UPDATE_STATUS = 6;
    private static final int GET_ROUTE = 7;
    private static final int GET_ONLY_ROUTE = 8;
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
    private DirectionResults.Location startLocation,endLocation;

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
        fragmentBookingBookedBinding.recordAudio.setOnClickListener(this);
        fragmentBookingBookedBinding.playVoiceMessage.setOnClickListener(this);
        fragmentBookingBookedBinding.tvLeftHome.setOnClickListener(this);


        if (Constants.BOOK_TYPE.equalsIgnoreCase("Demo"))
            callBookingDetailApi();
        else
            callMeetingDetailApi();
    }

    private void callMeetingDetailApi() {
        StatusRequestModel statusRequestModel = new StatusRequestModel();
        statusRequestModel.setMeetingID(Constants.MEETING_ID);
        if (((HomeActivity) getActivity()).getLocation() != null) {
            statusRequestModel.setLatitude(String.valueOf(((HomeActivity) getActivity()).getLocation().getLatitude()));
            statusRequestModel.setLongitude(String.valueOf(((HomeActivity) getActivity()).getLocation().getLongitude()));
        } else {
            statusRequestModel.setLatitude(Constants.LATITUDE != null ? Constants.LATITUDE : "0.0");
            statusRequestModel.setLongitude(Constants.LONGITUDE != null ? Constants.LONGITUDE : "0.0");
        }
        statusRequestModel.setUserID(userId);
        Call objectCall = RestClient.getApiService().getMeetingDetails(statusRequestModel);
        RestClient.makeApiRequest(getActivity(), objectCall, this, BOOKING_DETAILS, true);

    }


    private void callBookingDetailApi() {
        StatusRequestModel statusRequestModel = new StatusRequestModel();
        statusRequestModel.setBookingID(Constants.BOOKING_ID);
        if (((HomeActivity) getActivity()).getLocation() != null) {
            statusRequestModel.setLatitude(String.valueOf(((HomeActivity) getActivity()).getLocation().getLatitude()));
            statusRequestModel.setLongitude(String.valueOf(((HomeActivity) getActivity()).getLocation().getLongitude()));
        } else {
            statusRequestModel.setLatitude(Constants.LATITUDE != null ? Constants.LATITUDE : "0.0");
            statusRequestModel.setLongitude(Constants.LONGITUDE != null ? Constants.LONGITUDE : "0.0");
        }
        statusRequestModel.setUserID(userId);
        Call objectCall = RestClient.getApiService().getBookingDetails(statusRequestModel);
        RestClient.makeApiRequest(getActivity(), objectCall, this, BOOKING_DETAILS, true);

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


    @NonNull
    private Dialog getDialog() {
        Dialog dialog = new Dialog(getActivity());
        binding = DataBindingUtil.inflate(LayoutInflater.from(getActivity()), R.layout.dialog_reviews, null, false);
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


    private void showPorfomaInvoiceDialog(CarPorfomaInvoiceModel carPorfomaInvoiceModel) {
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
                if (!Permissionsutils.checkForStoragePermission(getActivity())) {
                    Permissionsutils.askForStoragePermission(getActivity());
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
                Utils.showToast(getActivity(), "Performa invoice is downloaded successfullly");
            }
        }).execute();
    }


    private void calculateDistance(int getOnlyRoute, String specialistLatitude, String specialistLongitude) {

        Call objectCall = RestClient.getApiService().getRoute(((HomeActivity) getActivity()).locationUtils.getLoc().getLatitude() + "," + ((HomeActivity) getActivity()).locationUtils.getLoc().getLongitude(), specialistLatitude + "," + specialistLongitude);
        RestClient.makeApiRequest(getActivity(), objectCall, this, getOnlyRoute, true);


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
            }
        });
        Toast.makeText(getActivity(), "Audio started playing..", Toast.LENGTH_SHORT).show();
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
                if(recorderUtils!=null) {
                    dialogRecordBinding.statusRecord.setText("Done");
                    recorderUtils.stopRecording();
                    isRecording = false;
                    String mFileName = Environment.getExternalStorageDirectory().getAbsolutePath();
                    mFileName += "/AudioRecording.mp3";

                    uploadFileToServer(new File(mFileName));
                }
            }
        });
        dialogRecordBinding.remove.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dialogRecordBinding.recordIv.setImageResource(R.drawable.ic_record);
                dialogRecordBinding.statusRecord.setText("Record");
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
                        recorderUtils.startRecording();
//                        startTimer();
                    } else
                        Permissionsutils.askForRecordPermission(getActivity());

                } else {
                    dialogRecordBinding.recordIv.setImageResource(R.mipmap.record_stop);
//                    countDownTimer.cancel();
//                    fragmentBookingBookedBinding.recordAudio.setClickable(true);
//                    fragmentBookingBookedBinding.recordAudio.setImageDrawable(getContext().getDrawable(R.drawable.ic_record));
//                    countDownTimer.onFinish();

                }

            }
        });
        recordDialog.getWindow().setLayout(WindowManager.LayoutParams.MATCH_PARENT, WindowManager.LayoutParams.WRAP_CONTENT);

        WindowManager.LayoutParams lp = recordDialog.getWindow().getAttributes();
        recordDialog.getWindow().setBackgroundDrawableResource(android.R.color.transparent);
        lp.dimAmount = 0.8f;
        recordDialog.getWindow().addFlags(WindowManager.LayoutParams.FLAG_DIM_BEHIND);
        recordDialog.show();
    }




    private void uploadFileToServer(File destination) {
        MultipartBody.Part filePart = MultipartBody.Part.createFormData("File", destination.getName(), RequestBody.create(MediaType.parse("image/*"), destination));
        Call<RegistrationResponse> call;
        if (Constants.BOOK_TYPE.equalsIgnoreCase("Demo"))
            call = RestClient.getApiService().uploadVoiceMessage(filePart, RequestBody.create(MediaType.parse("text/plain"), userId), RequestBody.create(MediaType.parse("text/plain"), Constants.BOOKING_ID));
        else
            call = RestClient.getApiService().uploadMeetingVoiceMessage(filePart, RequestBody.create(MediaType.parse("text/plain"), userId), RequestBody.create(MediaType.parse("text/plain"), Constants.MEETING_ID));

        RestClient.makeApiRequest(getActivity(), call, this, RECORD_AUDIO, true);
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
                ((HomeActivity) getActivity()).showFragment(new CancelDemoFragment());
                break;
            case R.id.performa_invoice:
                callPorfomaInvoiceApi();
                break;
            case R.id.book_demo:
                ((HomeActivity) getActivity()).showFragment(new TakeADemoFragment());
                break;
            case R.id.record_audio:
                showRecordDialog();
                break;
            case R.id.play_voice_message:
                if (bookingAcceptModel.getBookingdetails()!=null && bookingAcceptModel.getBookingdetails().getVoiceRecordingAudio() != null && bookingAcceptModel.getBookingdetails().getVoiceRecordingAudio().trim().length() > 0)
                    playAudio();
                else
                if (Constants.BOOK_TYPE.equalsIgnoreCase("Demo"))
                    callBookingDetailApi();
                else
                    callMeetingDetailApi();
                break;
            case R.id.tv_left_home:
                if (hourlyTask == null && timer!=null && !bookingAcceptModel.getBookingdetails().getDemoType().equalsIgnoreCase("At Home")) {
                    setTask();
                    timer.schedule(hourlyTask, 0l, Constants.WAIT_MINUTE);
                    sharedPrefUtils.saveData(Constants.LEFT_HOME,true);
                    fragmentBookingBookedBinding.tvLeftHome.setVisibility(View.GONE);
                }
                break;
        }

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
                fragmentBookingBookedBinding.submessage.setText(bookingAcceptModel.getBookingSubMessage());
                specialistId = bookingAcceptModel.getBookingdetails().getSpecialistID();
                updateCarCheckList();
                fragmentBookingBookedBinding.executePendingBindings();
                if(sharedPrefUtils.getBooleanData(Constants.LEFT_HOME))
                    fragmentBookingBookedBinding.tvLeftHome.setVisibility(View.GONE);


                if (hourlyTask == null && (!fromNotification && bookingAcceptModel.getBookingdetails().getDemoType()!=null   &&bookingAcceptModel.getBookingdetails().getDemoType().equalsIgnoreCase("At Home"))) {
                    setTask();
                    PrintLog.v("===setteask");
                    calculateDistance(GET_ONLY_ROUTE,bookingAcceptModel.getBookingdetails().getSpecialistLatitude(), bookingAcceptModel.getBookingdetails().getSpecialistLongitude());
                    getHomeAddress();
                    new Handler().postDelayed(new Runnable() {
                        @Override
                        public void run() {
                            if (timer != null)
                                timer.schedule(hourlyTask, 0, Constants.WAIT_MINUTE);
                        }
                    }, Constants.WAIT_MINUTE);
                }
                switch (bookingAcceptModel.getBookingdetails().getDemoStatusId()) {
                    case "5":
                        fragmentBookingBookedBinding.status.setText("Completed");
                        new Handler().postDelayed(new Runnable() {
                            @Override
                            public void run() {
                                endTask();
                                sharedPrefUtils.saveData(Constants.BOOKING_ONGOING,"null");
                                sharedPrefUtils.saveData(Constants.BOOK_TYPE_S,"null");
                                ((HomeActivity) getActivity()).showFragment(new BookingFeedbackFragment(bookingAcceptModel.getBookingdetails().getSpecialistName(), bookingAcceptModel.getBookingdetails().getSpecialistID()));
                            }
                        }, 5000);
                        break;
                    case "2":
                    case "3":

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
                        fragmentBookingBookedBinding.tvLeftHome.setVisibility(View.GONE);
                        fragmentBookingBookedBinding.joinCall.setVisibility(View.GONE);
                        if (Constants.BOOK_TYPE.equalsIgnoreCase("Demo"))
                            fragmentBookingBookedBinding.bookDemo.setText(getString(R.string.takedemos));
                        else
                            fragmentBookingBookedBinding.bookDemo.setText(getString(R.string.book_a_meeting));

                        break;
                    case "9":
                        fragmentBookingBookedBinding.status.setText("Arriving Soon");
                        fragmentBookingBookedBinding.call.setVisibility(View.GONE);
                        fragmentBookingBookedBinding.arrivingTime.setVisibility(View.GONE);
                        break;
                    case "10":
                        endTask();
                        fragmentBookingBookedBinding.call.setVisibility(View.VISIBLE);
                        fragmentBookingBookedBinding.status.setText(getString(R.string.arrived));
                        break;

                    case "13":
                        fragmentBookingBookedBinding.status.setText(getString(R.string.delayed));
                        break;

                    default:
                        PrintLog.v("default====");
                        if (!fromNotification && bookingAcceptModel.getBookingdetails().getMeetingType() != null && bookingAcceptModel.getBookingdetails().getMeetingType().equalsIgnoreCase("now") && bookingAcceptModel.getBookingdetails().getVirtualMeetStatus().equalsIgnoreCase("Y")) {
                            if (bookingAcceptModel.getBookingdetails().getVirtualMeetStatus().equalsIgnoreCase("Y"))
                                fragmentBookingBookedBinding.joinCall.setVisibility(View.VISIBLE);
                            PrintLog.v("inner====");
                        }
                        break;


                }

                //  ((HomeActivity) getActivity()).locationUtils.drawOnMap(bookingAcceptModel.bookingdetails.getSpecialistLatitude(), bookingAcceptModel.bookingdetails.getSpecialistLongitude());
                ((HomeActivity) getActivity()).setPeekheight(fragmentBookingBookedBinding.parentLl.getMeasuredHeight());

            }
        } else if (reqCode == RECORD_AUDIO) {
//            fragmentBookingBookedBinding.recordingCardview.setVisibility(View.GONE);
            RegistrationResponse registrationResponse = (RegistrationResponse) response;
            recordDialog.dismiss();
            Utils.showToast(getActivity(), registrationResponse.getDescriptions());
        } else if (reqCode == GET_SPECIALIST_LOCATION) {
            LocationResponse bookingAcceptModel = ((LocationResponse) response);
            calculateDistance(GET_ROUTE, bookingAcceptModel.getUserLatitude(), bookingAcceptModel.getUserLongitude());

        } else if (reqCode == PERFORMA_INVOICE) {
            CarPorfomaInvoiceModel carDetailReviewModel = (CarPorfomaInvoiceModel) response;
            showPorfomaInvoiceDialog(carDetailReviewModel);
        } else if (reqCode == UPDATE_LOCATION) {

            calculateDistance(GET_ROUTE, bookingAcceptModel.getBookingdetails().getSpecialistLatitude(), bookingAcceptModel.getBookingdetails().getSpecialistLongitude());

        } else if (reqCode == GET_ROUTE || reqCode==GET_ONLY_ROUTE) {
            DirectionResults directionResults = (DirectionResults) response;
            ArrayList<LatLng> routelist = new ArrayList<LatLng>();
            if (directionResults.getRoutes().size() > 0) {
                ArrayList<LatLng> decodelist;
                DirectionResults.Route routeA = directionResults.getRoutes().get(0);
                if (routeA.getLegs().size() > 0) {
                    List<DirectionResults.Steps> steps = routeA.getLegs().get(0).getSteps();
                    fragmentBookingBookedBinding.distanceText.setText("("+routeA.getLegs().get(0).getDistance().getText()+" away, "+routeA.getLegs().get(0).getDuration().getText()+")");
                    duration = routeA.getLegs().get(0).getDuration().getValue();//seconds
                    distance = routeA.getLegs().get(0).getDistance().getValue();//meters
                    if(fragmentBookingBookedBinding.address.getText().toString().trim().length()==0)
                        fragmentBookingBookedBinding.address.setText(routeA.getLegs().get(0).getStart_address());
                    startLocation = routeA.getLegs().get(0).getStartLocation();
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
            }
            if (routelist.size() > 0) {
                PolylineOptions rectLine = new PolylineOptions().width(10).color(
                        R.color.color_241e61);

                for (int i = 0; i < routelist.size(); i++) {
                    rectLine.add(routelist.get(i));
                }
                ((HomeActivity)getActivity()).locationUtils.addPolyLine(rectLine,startLocation,endLocation,bookingAcceptModel.getBookingdetails().getDemoType());

            }


            if(reqCode==GET_ROUTE && bookingAcceptModel.getBookingdetails().getDemoType().equalsIgnoreCase("At Home")) {
                if (destinationTime == 0)
                    destinationTime = Utils.getNextTime(Math.toIntExact(duration));
                if (distance <= 200) {
                    fragmentBookingBookedBinding.status.setText("Arrived");
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
                    boolean permissionToStore = grantResults[1] == PackageManager.PERMISSION_GRANTED;
                    if (permissionToRecord && permissionToStore) {
                        dialogRecordBinding.recordIv.setImageResource(R.mipmap.record_pause);
                        dialogRecordBinding.statusRecord.setText("Recording");
                        recorderUtils=   new RecorderUtils();
                        isRecording = true;
                        recorderUtils.startRecording();
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
                if (intent.getExtras().getString("notificationtype").equalsIgnoreCase("ChangeDemoStatus")
                || intent.getExtras().getString("notificationtype").equalsIgnoreCase("SpecialistDemoVoiceRecording")) {
                    Constants.BOOKING_ID = intent.getExtras().getString("demoid");
                    Constants.BOOK_TYPE = "Demo";
                    callBookingDetailApi();
                } else if(intent.getExtras().getString("notificationtype").equalsIgnoreCase("ChangeMeetingStatus")
                || intent.getExtras().getString("notificationtype").equalsIgnoreCase("SpecialistMeetVoiceRecording")) {
                    Constants.MEETING_ID = intent.getExtras().getString("demoid");
                    Constants.BOOK_TYPE = "Meeting";
                    callMeetingDetailApi();
                }
                if(intent.getExtras().getString("notificationtype").contains("VoiceRecording")){
                    fragmentBookingBookedBinding.newVoiceMessage.setVisibility(View.VISIBLE);
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


}
