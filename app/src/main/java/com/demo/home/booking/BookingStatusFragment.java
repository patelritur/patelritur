package com.demo.home.booking;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.Bundle;
import android.os.Handler;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.databinding.DataBindingUtil;
import androidx.fragment.app.Fragment;

import com.demo.R;
import com.demo.databinding.LayoutStatusDemoBookingBinding;
import com.demo.home.HomeActivity;
import com.demo.home.booking.model.BookingAcceptModel;
import com.demo.home.booking.model.BookingRequestModel;
import com.demo.home.booking.model.BookingResponseModel;
import com.demo.home.booking.model.CancelBookingModel;
import com.demo.home.booking.model.CurrentStatuSModel;
import com.demo.home.booking.model.StatusRequestModel;
import com.demo.utils.Constants;
import com.demo.utils.PrintLog;
import com.demo.utils.SharedPrefUtils;
import com.demo.utils.Utils;
import com.demo.webservice.ApiResponseListener;
import com.demo.webservice.RestClient;

import java.util.Objects;

import retrofit2.Call;

public class BookingStatusFragment  extends Fragment implements  ApiResponseListener
{
    private static final int CANCEL_BOOKING = 3;
    private static final int DETAIL_API = 1;
    private static final int STATUS_API = 2;
    private LayoutStatusDemoBookingBinding layoutStatusDemoBookingBinding;
    private BroadcastReceiver br = new MyReceiver();
    private int count;
    private String bookingid;
    private String bookingTimeType;
    private String meetingId;
    private int maxCount;
    private boolean isCancel;
    private BookingResponseModel bookingAcceptModel;
    private boolean callDetailApi = false;

    public BookingStatusFragment(boolean b) {
        isCancel = b;
    }

    public BookingStatusFragment(boolean b, BookingResponseModel bookingAcceptModel) {
        isCancel = b;
        this.bookingAcceptModel = bookingAcceptModel;

    }

    public BookingStatusFragment(String bookingId,String booking_time_type) {
        callDetailApi=true;
        bookingid=bookingId;
        bookingTimeType = booking_time_type;

    }

    private void callBookingDetailApi(String stringData) {
        StatusRequestModel statusRequestModel = new StatusRequestModel();

        if (((HomeActivity) requireActivity()).getLocation() != null) {
            statusRequestModel.setLatitude(String.valueOf(((HomeActivity) getActivity()).getLocation().getLatitude()));
            statusRequestModel.setLongitude(String.valueOf(((HomeActivity) getActivity()).getLocation().getLongitude()));
        } else {
            statusRequestModel.setLatitude(Constants.LATITUDE != null ? Constants.LATITUDE : "0.0");
            statusRequestModel.setLongitude(Constants.LONGITUDE != null ? Constants.LONGITUDE : "0.0");
        }
        statusRequestModel.setUserID(new SharedPrefUtils(getActivity()).getStringData(Constants.USER_ID));
        Call objectCall;
        if (Constants.BOOK_TYPE.equalsIgnoreCase("Demo")) {
            statusRequestModel.setBookingID(stringData);
            objectCall = RestClient.getApiService().getBookingDetails(statusRequestModel);
        }
        else {
            statusRequestModel.setMeetingID(stringData);
            objectCall = RestClient.getApiService().getMeetingDetails(statusRequestModel);
        }

        RestClient.makeApiRequest(getActivity(), objectCall, this, 10, true);

    }


    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        layoutStatusDemoBookingBinding = DataBindingUtil.inflate(inflater, R.layout.layout_status_demo_booking,container,false);
        layoutStatusDemoBookingBinding.avi.setIndicator("BallSpinFadeLoaderIndicator");
        layoutStatusDemoBookingBinding.avi.smoothToShow();
      //  new SharedPrefUtils(getActivity()).saveData(Constants.LEFT_HOME,false);

        return layoutStatusDemoBookingBinding.getRoot();

    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        count=1;
        if(isCancel){

            extracted((BookingResponseModel) bookingAcceptModel);
        }
        else if(callDetailApi){
            callBookingDetailApi(bookingid);
        }
        else {
            if (Constants.BOOK_TYPE.equalsIgnoreCase("Demo"))
                callBookingApi(Constants.BOOKING_PLACE_TYPE_ID);
            else
                callMeetingApi(Constants.MEETING_PLACE_TYPE_ID);
        }
        layoutStatusDemoBookingBinding.cancel.setOnClickListener(view1 -> {

            callCancelBookingApi();

        });
    }

    private void callCancelBookingApi() {

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

    @Override
    public void onApiResponse(Call<Object> call, Object response, int reqCode) throws Exception {
        if(reqCode==DETAIL_API) {
            extracted((BookingResponseModel) response);
            ((HomeActivity)getActivity()).setBehavior(false);
            ((HomeActivity) getActivity()).setPeekheight(layoutStatusDemoBookingBinding.parentLl.getMeasuredHeight());
        }
        else if(reqCode==STATUS_API)
        {

            CurrentStatuSModel currentStatuSModel = (CurrentStatuSModel) response;
            layoutStatusDemoBookingBinding.setHeaderMessage(currentStatuSModel.getBookingMessage());
            layoutStatusDemoBookingBinding.setStatusMessage(currentStatuSModel.getBookingSubMessage());
            layoutStatusDemoBookingBinding.setImageUrl(currentStatuSModel.getBookingMessageImage());
            layoutStatusDemoBookingBinding.executePendingBindings();
            if(count==maxCount)
                try{
                    PrintLog.v("unreg 5");
                    getActivity().unregisterReceiver(br);
                }
                catch (Exception e){
                    e.printStackTrace();
                }
            else
                count++;
            if(currentStatuSModel.getResponseCode().equalsIgnoreCase("200")) {
                if (currentStatuSModel.getBookingStatus().equalsIgnoreCase("Booked") ||currentStatuSModel.getBookingStatus().equalsIgnoreCase("Schedule"))
                {

                    Utils.cancelJob(getActivity());
                    ((HomeActivity) getActivity()).setBehavior(true);
                    ((HomeActivity) getActivity()).showFragment(new BookingConfirmedFragment());
                    try{
                        PrintLog.v("unreg");
                        getActivity().unregisterReceiver(br);
                    }
                    catch (Exception e){
                        e.printStackTrace();
                    }
                }
                else if(currentStatuSModel.getBookingStatus().equalsIgnoreCase("Not Accepted") && !((HomeActivity)getActivity()).specialistId.equalsIgnoreCase("0")){
                    Utils.cancelJob(getActivity());
                    layoutStatusDemoBookingBinding.continueOtherSpecialists.setVisibility(View.VISIBLE);
                    layoutStatusDemoBookingBinding.avi.setVisibility(View.GONE);
                    layoutStatusDemoBookingBinding.continueOtherSpecialists.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            ((HomeActivity)getActivity()).specialistId="0";
                            layoutStatusDemoBookingBinding.continueOtherSpecialists.setVisibility(View.GONE);
                            layoutStatusDemoBookingBinding.avi.setVisibility(View.VISIBLE);
                            if (Constants.BOOK_TYPE.equalsIgnoreCase("Demo"))
                                callBookingApi(Constants.BOOKING_PLACE_TYPE_ID);
                            else
                                callMeetingApi(Constants.MEETING_PLACE_TYPE_ID);

                        }
                    });


                }
                else if(currentStatuSModel.getBookingStatus().equalsIgnoreCase("Not Accepted")){
                    Utils.cancelJob(getActivity());
                    layoutStatusDemoBookingBinding.continueOtherSpecialists.setVisibility(View.VISIBLE);
                    layoutStatusDemoBookingBinding.cancel.setVisibility(View.GONE);
                    layoutStatusDemoBookingBinding.continueOtherSpecialists.setText("Schedule Now");
                    layoutStatusDemoBookingBinding.avi.setVisibility(View.GONE);
                    layoutStatusDemoBookingBinding.continueOtherSpecialists.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            Constants.BOOKING_TYPE_ID="Schedule";
                            Constants.MEETING_TYPE_ID="Schedule";
                            ((HomeActivity) getActivity()).showFragment(new ScheduleLaterFragment());

                        }
                    });

                    try{
                        PrintLog.v("unreg 6");
                        getActivity().unregisterReceiver(br);
                    }
                    catch (Exception e){
                        e.printStackTrace();
                    }
                }
            }


        }
        else if(reqCode==CANCEL_BOOKING){
            Utils.cancelJob(getActivity());
            try{
                PrintLog.v("unreg 2");
                getActivity().unregisterReceiver(br);
            }
            catch (Exception e){
                e.printStackTrace();
            }
            getActivity().startActivity(new Intent(getActivity(),HomeActivity.class));
            getActivity().finishAffinity();
        }
        else if(reqCode==10){
            BookingAcceptModel bookingAcceptModel = (BookingAcceptModel)response;
            Constants.TIME = bookingAcceptModel.getBookingTime();
            Constants.DATE = bookingAcceptModel.getBookingDate();
            Constants.DEMOADDRESS = bookingAcceptModel.getBookingdetails().getCustomerAddress();
            Constants.CARID = bookingAcceptModel.getBookingdetails().getCarID();
            if(Constants.BOOK_TYPE.equalsIgnoreCase("Demo")) {
                Constants.BOOKING_TYPE_ID = bookingTimeType;
                Constants.BOOKING_PLACE_TYPE_ID = bookingAcceptModel.getBookingdetails().getDemoType().equalsIgnoreCase("At Dealership")?"1":"2";
                callBookingApi(Constants.BOOKING_PLACE_TYPE_ID);
            }
            else{
                Constants.MEETING_TYPE_ID = bookingTimeType;
                Constants.MEETING_PLACE_TYPE_ID =  bookingAcceptModel.getBookingdetails().getDemoType().equalsIgnoreCase("At Dealership")?"1":"2";
                if(bookingAcceptModel.getBookingdetails().getVirtualMeetType()!=null)
                    Constants.VIRTUAL_MEET_TYPE = bookingAcceptModel.getBookingdetails().getVirtualMeetType();
                callMeetingApi(Constants.MEETING_PLACE_TYPE_ID);
            }

        }

    }

    private void extracted(BookingResponseModel response) {
        BookingResponseModel bookingResponseModel = response;

        if(Constants.BOOK_TYPE.equalsIgnoreCase("Demo")) {
            bookingid = bookingResponseModel.bookingID;
            Constants.BOOKING_ID = bookingid;

        }
        else{
            meetingId = bookingResponseModel.getBookingID();
            Constants.MEETING_ID = meetingId;
        }
        layoutStatusDemoBookingBinding.setHeaderMessage(bookingResponseModel.getBookingMessage());
        layoutStatusDemoBookingBinding.setStatusMessage(bookingResponseModel.getBookingSubMessage());
        layoutStatusDemoBookingBinding.setImageUrl(bookingResponseModel.getBookingMessageImage());
        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                ((HomeActivity)getActivity()).setBehavior(false);
                ((HomeActivity) getActivity()).setPeekheight(layoutStatusDemoBookingBinding.parentLl.getMeasuredHeight());

            }
        },500);
      if(bookingResponseModel.getResponseCode().equalsIgnoreCase("200")){
            maxCount = Integer.parseInt(bookingResponseModel.meetingWaitingMsgCount);
            count=1;
            IntentFilter filter = new IntentFilter();
            filter.addAction("callStatusService");
            filter.addAction("receiveNotification");
            getActivity().registerReceiver(br, filter);
            Utils.scheduleJob(getActivity(), Integer.parseInt(bookingResponseModel.BookingWaitingTimeInMins),maxCount);
            layoutStatusDemoBookingBinding.availableSpecialist.setText(bookingResponseModel.getLocationlist().size()>1?bookingResponseModel.getLocationlist().size()+" Demo Specialists available":bookingResponseModel.getLocationlist().size()+" Demo Specialist available");
            ((HomeActivity) getActivity()).locationUtils.setLocationOnMapForDealer(bookingResponseModel.getLocationlist());
        }
        else if(bookingResponseModel.getResponseCode().equalsIgnoreCase("201") && !((HomeActivity)getActivity()).specialistId.equalsIgnoreCase("0")){
            layoutStatusDemoBookingBinding.continueOtherSpecialists.setVisibility(View.VISIBLE);
            layoutStatusDemoBookingBinding.avi.setVisibility(View.GONE);
            layoutStatusDemoBookingBinding.continueOtherSpecialists.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    ((HomeActivity)getActivity()).specialistId="0";
                    layoutStatusDemoBookingBinding.continueOtherSpecialists.setVisibility(View.GONE);
                    layoutStatusDemoBookingBinding.avi.setVisibility(View.VISIBLE);
                    if (Constants.BOOK_TYPE.equalsIgnoreCase("Demo"))
                        callBookingApi(Constants.BOOKING_PLACE_TYPE_ID);
                    else
                        callMeetingApi(Constants.MEETING_PLACE_TYPE_ID);

                }
            });

        }
        else if(bookingResponseModel.getResponseCode().equalsIgnoreCase("201") ){
            layoutStatusDemoBookingBinding.continueOtherSpecialists.setVisibility(View.VISIBLE);
            layoutStatusDemoBookingBinding.cancel.setVisibility(View.GONE);
            layoutStatusDemoBookingBinding.continueOtherSpecialists.setText("Schedule Now");
            layoutStatusDemoBookingBinding.avi.setVisibility(View.GONE);
            layoutStatusDemoBookingBinding.continueOtherSpecialists.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    ((HomeActivity) getActivity()).showFragment(new ScheduleLaterFragment());

                }
            });

        }
    }

    @Override
    public void onApiError(Call<Object> call, Object response, int reqCode) throws Exception {

    }

    private void callMeetingApi(String meet_type_id) {
        BookingRequestModel bookingRequestModel = new BookingRequestModel();
        bookingRequestModel.setUserID(((HomeActivity)getActivity()).userId);
        bookingRequestModel.setMeetTime(Constants.TIME);
        bookingRequestModel.setMeetDate(Constants.DATE);
        bookingRequestModel.setMeetBookingType(Constants.MEETING_TYPE_ID);
        bookingRequestModel.setMeetTypeID(meet_type_id);
        bookingRequestModel.setUserAddress(Constants.DEMOADDRESS);
        bookingRequestModel.setAddressType(Constants.DEMOLOCATIONTYPE);
        if(Constants.VIRTUAL_MEET_TYPE!=null)
        bookingRequestModel.setMeetCallType(Constants.VIRTUAL_MEET_TYPE);
        if(((HomeActivity)getActivity()).getLocation()!=null) {
            bookingRequestModel.setLatitude(String.valueOf(((HomeActivity)getActivity()).getLocation().getLatitude()));
            bookingRequestModel.setLongitude(String.valueOf(((HomeActivity)getActivity()).getLocation().getLongitude()));
        }
        else
        {
            bookingRequestModel.setLatitude("0.0");
            bookingRequestModel.setLongitude("0.0");

        }
        bookingRequestModel.setCarID(Constants.CARID);
        bookingRequestModel.setSpecialistID(((HomeActivity)getActivity()).specialistId);
        Call objectCall = RestClient.getApiService().meetingBooking(bookingRequestModel);
        RestClient.makeApiRequest(getActivity(), objectCall, this, DETAIL_API, true);

    }



    private void callBookingApi(String menuID) {
        BookingRequestModel bookingRequestModel = new BookingRequestModel();
        bookingRequestModel.setUserID(new SharedPrefUtils(getActivity()).getStringData(Constants.USER_ID));
        bookingRequestModel.setDemoTime(Constants.TIME);
        bookingRequestModel.setDemoDate(Constants.DATE);
        bookingRequestModel.setDemoTypeID(menuID);
        bookingRequestModel.setDemoBookingType(Constants.BOOKING_TYPE_ID);
        bookingRequestModel.setCarID(Constants.CARID);
        bookingRequestModel.setUserAddress(Constants.DEMOADDRESS);
        bookingRequestModel.setAddressType(Constants.DEMOLOCATIONTYPE);
        bookingRequestModel.setSpecialistID(((HomeActivity)getActivity()).specialistId);
        if(((HomeActivity)getActivity()).getLocation()!=null) {
            bookingRequestModel.setLatitude(String.valueOf(((HomeActivity)getActivity()).getLocation().getLatitude()));
            bookingRequestModel.setLongitude(String.valueOf(((HomeActivity)getActivity()).getLocation().getLongitude()));
        }
        else
        {
            bookingRequestModel.setLatitude("0.0");
            bookingRequestModel.setLongitude("0.0");

        }

        Call objectCall = RestClient.getApiService().carbooking(bookingRequestModel);
        RestClient.makeApiRequest(getActivity(), objectCall, this, DETAIL_API, true);

    }

    public class MyReceiver extends BroadcastReceiver{

        @Override
        public void onReceive(Context context, Intent intent) {
            PrintLog.v("onReceive");
            if(intent.getAction().equalsIgnoreCase("receiveNotification") && intent.getExtras().getString("notificationtype").contains("Accept")){
                Utils.cancelJob(getActivity());

                ((HomeActivity) getActivity()).setBehavior(true);
                ((HomeActivity) getActivity()).showFragment(new BookingConfirmedFragment());
                try{
                    PrintLog.v("unreg 3");
                    getActivity().unregisterReceiver(br);
                }
                catch (Exception e){
                    e.printStackTrace();
                }

            }
            else
                callStatusApi();
        }
    }

    private void callStatusApi() {
        StatusRequestModel statusRequestModel = new StatusRequestModel();
        if(Constants.BOOK_TYPE.equalsIgnoreCase("Demo"))
            statusRequestModel.setBookingID(Constants.BOOKING_ID);
        else
            statusRequestModel.setMeetingID(Constants.MEETING_ID);
        statusRequestModel.setUserID(((HomeActivity) requireActivity()).userId);
        statusRequestModel.setAPICallCount(String.valueOf(count));
        statusRequestModel.setFinalAPICallStatus("N");
        if(count==maxCount) {
            Utils.cancelJob(requireActivity());
            ((HomeActivity)getActivity()).setBehavior(true);
            statusRequestModel.setFinalAPICallStatus("Y");

        }

        Call objectCall;
        if(Constants.BOOK_TYPE.equalsIgnoreCase("Demo"))
            objectCall = RestClient.getApiService().getCurrentStatus(statusRequestModel);
        else
            objectCall = RestClient.getApiService().getMeetingCurrentStatus(statusRequestModel);

        RestClient.makeApiRequest(getActivity(), objectCall, this, STATUS_API, true);


    }

    @Override
    public void onStop() {
        super.onStop();
        try {
            PrintLog.v("unreg 4");
            getActivity().unregisterReceiver(br);
        }
        catch (Exception e){
            e.printStackTrace();

        }
    }

    @Override
    public void onResume() {
        super.onResume();
        IntentFilter filter = new IntentFilter();
        filter.addAction("callStatusService");
        filter.addAction("receiveNotification");
        getActivity().registerReceiver(br, filter);
    }
}
