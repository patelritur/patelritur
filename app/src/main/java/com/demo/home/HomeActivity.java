package com.demo.home;

import static com.google.android.material.bottomsheet.BottomSheetBehavior.STATE_COLLAPSED;
import static com.google.android.material.bottomsheet.BottomSheetBehavior.STATE_DRAGGING;
import static com.google.android.material.bottomsheet.BottomSheetBehavior.STATE_EXPANDED;
import static com.google.android.material.bottomsheet.BottomSheetBehavior.STATE_HIDDEN;

import android.app.Dialog;
import android.content.Intent;
import android.graphics.Color;
import android.location.Location;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.ViewTreeObserver;
import android.view.WindowManager;
import android.view.animation.TranslateAnimation;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.core.view.GravityCompat;
import androidx.databinding.DataBindingUtil;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.LifecycleOwner;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProviders;

import com.cometchat.pro.core.CometChat;
import com.cometchat.pro.exceptions.CometChatException;
import com.cometchat.pro.models.User;
import com.demo.BaseActivity;
import com.demo.R;
import com.demo.carDetails.model.CarDetailRequest;
import com.demo.databinding.ActivityHomeBinding;
import com.demo.databinding.DialogHomeKnowMoreBinding;
import com.demo.home.booking.BookingConfirmedFragment;
import com.demo.home.booking.BookingFeedbackFragment;
import com.demo.home.booking.BookingStatusFragment;
import com.demo.home.booking.DemoPlaceBookingFragment;
import com.demo.home.booking.ScheduleBookingFragment;
import com.demo.home.booking.model.MapLocationResponseModel;
import com.demo.home.meeting.MeetingPlaceFragment;
import com.demo.home.meeting.VirtualMeetFragment;
import com.demo.home.model.CarSearchRequestModel;
import com.demo.home.model.viewmodel.AppContentViewModel;
import com.demo.home.model.viewmodel.AppContentViewModelFactory;
import com.demo.home.model.viewmodel.SearchResultViewModel;
import com.demo.home.model.viewmodel.SearchResultViewModelFactory;
import com.demo.home.model.viewmodel.WeatherViewModel;
import com.demo.home.model.viewmodel.WeatherViewModelFactory;
import com.demo.home.profile.MyDemoActivity;
import com.demo.home.profile.MyProfileActivity;
import com.demo.utils.Constants;
import com.demo.utils.LocationUtils;
import com.demo.utils.Permissionsutils;
import com.demo.utils.PrintLog;
import com.demo.utils.Utils;
import com.demo.webservice.ApiResponseListener;
import com.demo.webservice.RestClient;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.material.bottomsheet.BottomSheetBehavior;

import java.util.ArrayList;

import retrofit2.Call;

public class HomeActivity extends BaseActivity implements LifecycleOwner, ApiResponseListener {
    private ActivityHomeBinding activityHomeBinding;
    private BottomSheetBehavior<View> behavior,behavior1,behavior2;
    public LocationUtils locationUtils;
    protected ArrayList<String> budgetSelectedId = new ArrayList<>();
    protected ArrayList<String> brandSelectedId = new ArrayList<>();
    protected ArrayList<String> segmentSelectedId = new ArrayList<>();
    protected ArrayList<String> fuelSelectedId = new ArrayList<>();
    public CarSearchRequestModel carSearchRequestModel = new CarSearchRequestModel();
    private TakeADemoFragment takeAdemoFragment;
    public String userId;
    private Fragment fragment;
    private int height,deviceHeight;
    private ViewTreeObserver.OnGlobalLayoutListener globalListener = null;
    public String specialistId="0";
    private SupportMapFragment mapFragment;
    private DisplayMetrics displayMetrics;
    private ViewGroup.LayoutParams params;
    private int expandedHeight;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getWindow().addFlags(WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON);
        activityHomeBinding = DataBindingUtil.setContentView(this, R.layout.activity_home);

        userId = sharedPrefUtils.getStringData(Constants.USER_ID);

        if(sharedPrefUtils.getStringData(Constants.BOOKING_ONGOING)!=null && !sharedPrefUtils.getStringData(Constants.BOOKING_ONGOING).equalsIgnoreCase("null")) {
            activityHomeBinding.bookingOngoing.setVisibility(View.VISIBLE);
        }
        activityHomeBinding.bookingOngoing.setOnClickListener(view -> {
            Constants.BOOK_TYPE = sharedPrefUtils.getStringData(Constants.BOOK_TYPE_S);
            if (sharedPrefUtils.getStringData(Constants.BOOK_TYPE_S).equalsIgnoreCase("Demo"))
                Constants.BOOKING_ID = sharedPrefUtils.getStringData(Constants.BOOKING_ONGOING);
            else
                Constants.MEETING_ID = sharedPrefUtils.getStringData(Constants.BOOKING_ONGOING);

            showFragment(new BookingConfirmedFragment());
        });




         mapFragment = (SupportMapFragment) getSupportFragmentManager()
                .findFragmentById(R.id.map);
         params = mapFragment.getView().getLayoutParams();
         displayMetrics = new DisplayMetrics();
        getWindowManager().getDefaultDisplay().getMetrics(displayMetrics);
        deviceHeight = displayMetrics.heightPixels;

        locationUtils = new LocationUtils(this,mapFragment);
        setBottomMenuLabels(activityHomeBinding.llBottom);
        setMenuLabels(activityHomeBinding.leftMenu.menuRecyclerview);
        setHomeMenuLabels();
        activityHomeBinding.setHomeModel(homeModel);
        activityHomeBinding.llName.setHomeModel(homeModel);
        activityHomeBinding.layoutOptionsDemo.setHomeModel(homeModel);
        activityHomeBinding.executePendingBindings();
        setBottomSheetBehaviour();
        if (CometChat.getLoggedInUser()==null) {
            User user = new User();
            user.setUid(userId);
            user.setName(sharedPrefUtils.getStringData(Constants.FNAME));
            login(user);
        }
//        getWeatherData();
        locationUtils.getMutableLoc().observe(this,new Observer<Location>() {
            @Override
            public void onChanged(Location changedValue) {
                //Do something with the changed value
                if(changedValue==null)
                    return;
                Constants.LATITUDE = changedValue.getLatitude()+"";
                Constants.LONGITUDE = changedValue.getLongitude()+"";
                getWeatherData();


            }
        });
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) {
            if(Permissionsutils.CheckForLocationPermission(this) && !Permissionsutils.checkForNotificationPermission(this))
                Permissionsutils.askForNotificationPermission(this);
        }

        if(getIntent().getExtras()!=null)
        {
            Utils.cancelJob(this);
            if(getIntent().getExtras().getString("notificationtype")!=null && getIntent().getExtras().getString("comeFrom")==null) {
                PrintLog.v("comple1");
                if (getIntent().getExtras().getString("notificationtype").equalsIgnoreCase("AcceptDemoRequest")) {
                    Constants.BOOKING_ID = getIntent().getExtras().getString("demoid");
                    Constants.BOOK_TYPE = "Demo";
                    if(getIntent().getExtras().getString("bookingtype").equalsIgnoreCase("schedule") ||
                            getIntent().getExtras().getString("bookingtype").equalsIgnoreCase("After30Min"))
                    {
                        startActivity(new Intent(this, MyDemoActivity.class));
//                        showFragment(new BookingConfirmedFragment(true));
                    }
                } else if(getIntent().getExtras().getString("notificationtype").equalsIgnoreCase("AcceptMeetRequest")){
                    Constants.MEETING_ID = getIntent().getExtras().getString("demoid");
                    Constants.BOOK_TYPE = "Meeting";
                    if(getIntent().getExtras().getString("bookingtype").equalsIgnoreCase("schedule") ||
                            getIntent().getExtras().getString("bookingtype").equalsIgnoreCase("After30Min"))
                    {
                        showFragment(new BookingConfirmedFragment(true));
                    }
                }

                if(getIntent().getExtras().getString("message")!=null && (getIntent().getExtras().getString("message").contains("completed") || getIntent().getExtras().getString("message").contains("Completed") )){
                    PrintLog.v("comple");
                    showFragment(new BookingFeedbackFragment(sharedPrefUtils.getStringData(Constants.SNAME),getIntent().getExtras().getString("customerid")));
                }
                else if(getIntent().getExtras().getString("message")!=null && (getIntent().getExtras().getString("message").contains("rejected") || getIntent().getExtras().getString("message").contains("Rejected") )){
                    if(getIntent().getExtras().getString("bookingtype").equalsIgnoreCase("schedule") ||
                            getIntent().getExtras().getString("bookingtype").equalsIgnoreCase("After30Min"))
                        showFragment(new BookingStatusFragment(getIntent().getExtras().getString("demoid"),getIntent().getExtras().getString("bookingtype")));
                    else
                        showFragment(new BookingConfirmedFragment());
                }
                if(sharedPrefUtils.getStringData(Constants.BOOKING_ONGOING)!=null && !sharedPrefUtils.getStringData(Constants.BOOKING_ONGOING).equalsIgnoreCase("null"))
                    showFragment(new BookingConfirmedFragment());
            }
            else if(getIntent().getExtras().getString("comeFrom").equalsIgnoreCase("LocalNotification")){
                sharedPrefUtils.saveData(Constants.BOOK_TYPE_S,getIntent().getExtras().getString("demoType"));
                sharedPrefUtils.saveData(Constants.BOOKING_ONGOING,getIntent().getExtras().getString("bookingId"));
                Constants.BOOK_TYPE=getIntent().getExtras().getString("demoType");
                Constants.BOOKING_ID=getIntent().getExtras().getString("bookingId");
                Constants.MEETING_ID=getIntent().getExtras().getString("bookingId");
                showFragment(new BookingConfirmedFragment(false));
            }
            else if(getIntent().getExtras().getString("comeFrom").equalsIgnoreCase("LocalNotification15")){
                Constants.BOOK_TYPE=getIntent().getExtras().getString("demoType");
                Constants.BOOKING_ID=getIntent().getExtras().getString("bookingId");
                Constants.MEETING_ID=getIntent().getExtras().getString("bookingId");
                showFragment(new BookingConfirmedFragment(true));
            }
            else if(getIntent().getExtras().getString("comeFrom").equalsIgnoreCase("notifications")){
                showFragment(new BookingConfirmedFragment(true));

            }
            else if(getIntent().getExtras().getString("comeFrom").equalsIgnoreCase("takeAdemo")){
                activityHomeBinding.layoutOptionsDemo.llDrivemydemo.performClick();
            }
            else if(getIntent().getExtras().getString("comeFrom").equalsIgnoreCase("launch")){
                if(Constants.BOOK_TYPE.equalsIgnoreCase("Demo"))
                    showFragment(new DemoPlaceBookingFragment(getIntent().getExtras().getString("bookdate")));
                else
                    showFragment(new MeetingPlaceFragment(getIntent().getExtras().getString("bookdate")));
            }
            else if(getIntent().getExtras().getString("comeFrom").equalsIgnoreCase("favourite")){

                specialistId =getIntent().getExtras().getString("specialistId");
                PrintLog.v("spe"+specialistId);
                setEmptyValues();
                activityHomeBinding.layoutOptionsDemo.llMeetspecialists.setBackgroundResource(R.drawable.border_red_rounded_corner);
                activityHomeBinding.layoutOptionsDemo.llDrivemydemo.setBackgroundResource(R.drawable.white_border);
                //    showFragment(new MeetingPlaceFragment());
                takeAdemoFragment = new TakeADemoFragment(specialistId);
                showFragment(takeAdemoFragment);

            }
            else if(getIntent().getExtras().getString("comeFrom").equalsIgnoreCase("Details")){
                callAllMapLocationApi();

                specialistId = getIntent().getExtras().getString("specialistId");
                if(Constants.BOOK_TYPE.equalsIgnoreCase("Demo")) {
                    showFragment(new DemoPlaceBookingFragment());
                }

                else
                {
                    showFragment(new MeetingPlaceFragment());
                }
            }

        }

    }

    private void login(User user) {
        CometChat.login(user.getUid(), Constants.AUTH_KEY, new CometChat.CallbackListener<User>() {

            @Override
            public void onSuccess(User user) {
                Log.e("TAG", "Login Successful : " + user.toString());

                //    MyFirebaseMessagingService.subscribeUserNotification(user.getUid());
            }

            @Override
            public void onError(CometChatException e) {
                Log.e("TAG", "Login failed with exception: " + e.getMessage());
            }
        });
    }


    private void getWeatherData() {


        WeatherViewModelFactory factory = new WeatherViewModelFactory(this.getApplication(), Constants.LATITUDE+","+Constants.LONGITUDE);

        WeatherViewModel appContentViewModel = ViewModelProviders.of(this,factory).get(WeatherViewModel.class);

        appContentViewModel.getWeatherResposneodelLiveData().observe(this, item -> {
            sharedPrefUtils.saveData(Constants.STATENAME,item.location.region);
            homeModel.setTemp_c(item.current.temp_c+"°");
            homeModel.setDayOfWeek(Utils.getDayOfWeek());
            activityHomeBinding.setHomeModel(homeModel);
           /* if(currentLocation==null)
            callGeoCodeApi();*/

        });
    }


    public void setHomeMenuLabels() {
        getViewModelStore().clear();
        AppContentViewModelFactory factory = new AppContentViewModelFactory(this.getApplication(), Constants.HOME_MENU);
        AppContentViewModel appContentViewModel = ViewModelProviders.of(this, factory).get(AppContentViewModel.class);

        appContentViewModel.getHomeMenuLiveData().observe(this, item -> {
            homeModel.setHomeMenuFirstName(item.getLabels().get(0).getLabelInLanguage());
            homeModel.setHomeMenuFirstImage(item.getLabels().get(0).getLabelImage());
            homeModel.setHomeMenuSecondName(item.getLabels().get(1).getLabelInLanguage());
            homeModel.setHomeMenuSecondImage(item.getLabels().get(1).getLabelImage());
            activityHomeBinding.setHomeModel(homeModel);
        });
    }

    public void setBottomSheetBehaviour() {
        ViewTreeObserver viewTreeObserver = activityHomeBinding.llBottom.getRoot().getViewTreeObserver();
        behavior = BottomSheetBehavior.from(activityHomeBinding.fragmentContainerView);
        behavior2 = BottomSheetBehavior.from(activityHomeBinding.llContainerName);
        behavior2.setDraggable(true);
        behavior1 = BottomSheetBehavior.from(activityHomeBinding.scheduleFragmentContainerView);

        globalListener = new ViewTreeObserver.OnGlobalLayoutListener() {


            @Override
            public void onGlobalLayout() {

                height = activityHomeBinding.coordinatorName.getMeasuredHeight();
                int peekHeight=deviceHeight/4;
                int maxheight= Math.min((deviceHeight / 2), activityHomeBinding.coordinatorName.getHeight());
                behavior2.setPeekHeight(peekHeight);
                behavior2.setMaxHeight(maxheight);
                behavior.setPeekHeight(peekHeight);
                behavior.setMaxHeight(deviceHeight / 2);
                behavior1.setPeekHeight(peekHeight);
                behavior1.setMaxHeight(deviceHeight / 2);
                expandedHeight = deviceHeight-activityHomeBinding.rlTop.getRoot().getHeight()-activityHomeBinding.llBottom.getRoot().getHeight()-40-behavior2.getMaxHeight();



            }
        };
        //  behavior.setState(BottomSheetBehavior.STATE_HIDDEN);
        viewTreeObserver.addOnGlobalLayoutListener(globalListener);
        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                behavior2.setState(BottomSheetBehavior.STATE_EXPANDED);
            }
        },200);

//        params.height = expandedHeight;
//        mapFragment.getView().setLayoutParams(params);
        behavior.addBottomSheetCallback(new BottomSheetBehavior.BottomSheetCallback() {
            @Override
            public void onStateChanged(@NonNull View bottomSheet, int newState) {
                if(newState==STATE_HIDDEN){
                    new Handler().postDelayed(new Runnable() {
                        @Override
                        public void run() {
                            behavior2.setState(BottomSheetBehavior.STATE_EXPANDED);
                        }
                    },200);
                    if(sharedPrefUtils!=null && sharedPrefUtils.getStringData(Constants.BOOKING_ONGOING)!=null && !sharedPrefUtils.getStringData(Constants.BOOKING_ONGOING).equalsIgnoreCase("null")) {
                        activityHomeBinding.bookingOngoing.setVisibility(View.VISIBLE);
                    }
                    else
                        activityHomeBinding.bookingOngoing.setVisibility(View.GONE);
                    /*if(fragment.toString().contains("ScheduleBookingFragment")){
                        showFragment(new DemoPlaceBookingFragment());
                    }
                    else */

                    if(fragment.toString().contains("ScheduleLaterFragment")){
                        if(!getIntent().getExtras().getString("comeFrom").equalsIgnoreCase("launch"))
                            showScheduleFragment(new ScheduleBookingFragment());
                        else {
                            PrintLog.v("hide===");
                            activityHomeBinding.coordinatorSchedule.setBackgroundColor(Color.TRANSPARENT);
                            activityHomeBinding.coordinator11.setBackgroundColor(Color.TRANSPARENT);
                            activityHomeBinding.coordinatorSchedule.setVisibility(View.GONE);
                            activityHomeBinding.scheduleFragmentContainerView.setVisibility(View.GONE);
                            activityHomeBinding.coordinator11.setVisibility(View.GONE);

                        }
                    }
                    else if(fragment.toString().contains("DemoPlaceBookingFragment") ||fragment.toString().contains("MeetingPlaceFragment")  ){
                        HomeActivity.super.onBackPressed();

                    }
                    else if(fragment.toString().contains("CancelDemoFragment")){
                        activityHomeBinding.bookingOngoing.performClick();
                    }

                }
                else if(newState==STATE_COLLAPSED){
//                    params.height = (int) (displayMetrics.heightPixels/2+100);
                    params.height = deviceHeight-activityHomeBinding.rlTop.getRoot().getHeight()-activityHomeBinding.llBottom.getRoot().getHeight()-behavior.getPeekHeight();
                    mapFragment.getView().setLayoutParams(params);
//                    behavior.setPeekHeight(300);
                }
                else if(newState==STATE_EXPANDED){
                    behavior.setPeekHeight(Math.min(activityHomeBinding.fragmentContainerView.getHeight(),behavior.getPeekHeight()));
                    params.height = deviceHeight-activityHomeBinding.rlTop.getRoot().getHeight()-activityHomeBinding.llBottom.getRoot().getHeight()-activityHomeBinding.fragmentContainerView.getHeight()-40;
                    mapFragment.getView().setLayoutParams(params);
//                    behavior.setPeekHeight(displayMetrics.heightPixels/2);
                }


            }

            @Override
            public void onSlide(@NonNull View bottomSheet, float slideOffset) {
                if(slideOffset==-1)
                {
                    activityHomeBinding.layoutOptionsDemo.llMeetspecialists.setBackgroundResource(R.drawable.white_border);
                    activityHomeBinding.layoutOptionsDemo.llDrivemydemo.setBackgroundResource(R.drawable.white_border);

                }
            }
        });
        behavior1.addBottomSheetCallback(new BottomSheetBehavior.BottomSheetCallback() {
            @Override
            public void onStateChanged(@NonNull View bottomSheet, int newState) {
                if(newState==STATE_HIDDEN){
                    if(sharedPrefUtils!=null && sharedPrefUtils.getStringData(Constants.BOOKING_ONGOING)!=null && !sharedPrefUtils.getStringData(Constants.BOOKING_ONGOING).equalsIgnoreCase("null")) {
                        activityHomeBinding.bookingOngoing.setVisibility(View.VISIBLE);
                    }
                    else
                        activityHomeBinding.bookingOngoing.setVisibility(View.GONE);
                    if(locationUtils!=null)
                        locationUtils.clearmap();
                    if(fragment.toString().contains("ScheduleBookingFragment")){
                        PrintLog.v("hidden");
                        if(Constants.BOOK_TYPE.equalsIgnoreCase("Demo")) {

                            showFragment(new DemoPlaceBookingFragment());
                        }

                        else
                            showFragment(new MeetingPlaceFragment());
                    }

                    else if(fragment.toString().contains("VirtualMeetFragment")){
                        PrintLog.v("hidden");
                        showFragment(new MeetingPlaceFragment());
                    }


                }
                else if(newState==STATE_COLLAPSED){
                    params.height = (int) (displayMetrics.heightPixels/2);
                    mapFragment.getView().setLayoutParams(params);
//                    behavior.setPeekHeight(300);
                }
                else if(newState==STATE_EXPANDED){
                    params.height = (int) (displayMetrics.heightPixels/3);
                    mapFragment.getView().setLayoutParams(params);
//                    behavior.setPeekHeight(displayMetrics.heightPixels/2);
                }

            }

            @Override
            public void onSlide(@NonNull View bottomSheet, float slideOffset) {

            }
        });
        behavior2.addBottomSheetCallback(new BottomSheetBehavior.BottomSheetCallback() {
            @Override
            public void onStateChanged(@NonNull View bottomSheet, int newState) {
                if(newState==STATE_COLLAPSED || newState==STATE_HIDDEN){
                    params.height = deviceHeight-activityHomeBinding.rlTop.getRoot().getHeight()-activityHomeBinding.llBottom.getRoot().getHeight()-40-behavior2.getPeekHeight();
                    mapFragment.getView().setLayoutParams(params);
//                    behavior.setPeekHeight(300);
                }
                else if(newState==STATE_EXPANDED){
                    params.height = deviceHeight-activityHomeBinding.rlTop.getRoot().getHeight()-activityHomeBinding.llBottom.getRoot().getHeight()-40-activityHomeBinding.coordinatorName.getHeight();
                    mapFragment.getView().setLayoutParams(params);
//                    behavior.setPeekHeight(displayMetrics.heightPixels/2);
                }
            }

            @Override
            public void onSlide(@NonNull View bottomSheet, float slideOffset) {

            }
        });


    }



    @Override
    public void onClick(View view) {
        switch (view.getId())
        {
            case R.id.see_profile:
                startActivity(new Intent(this, MyProfileActivity.class));
                break;
            case R.id.ll_home:
                break;
            case R.id.ll_mydemos:
                startActivity(new Intent(this, MyDemoActivity.class));
                break;
            case R.id.ll_takedemo:
                activityHomeBinding.layoutOptionsDemo.llDrivemydemo.performClick();
                break;
            case R.id.imageview_menu:
                activityHomeBinding.drawerLy.openDrawer(GravityCompat.END);
                break;
            case R.id.ll_meetspecialists:
                Constants.BOOK_TYPE="Meeting";
                setEmptyValues();
                activityHomeBinding.layoutOptionsDemo.llMeetspecialists.setBackgroundResource(R.drawable.border_red_rounded_corner);
                activityHomeBinding.layoutOptionsDemo.llDrivemydemo.setBackgroundResource(R.drawable.white_border);
                //    showFragment(new MeetingPlaceFragment());
                takeAdemoFragment = new TakeADemoFragment(specialistId);
                showFragment(takeAdemoFragment);
                break;
            case R.id.ll_drivemydemo:
                Constants.BOOK_TYPE="Demo";
                setEmptyValues();
                activityHomeBinding.layoutOptionsDemo.llDrivemydemo.setBackgroundResource(R.drawable.border_red_rounded_corner);
                activityHomeBinding.layoutOptionsDemo.llMeetspecialists.setBackgroundResource(R.drawable.white_border);
                takeAdemoFragment = new TakeADemoFragment(specialistId);
                showFragment(takeAdemoFragment);
                break;
            case R.id.know_more_demo:
                showDialogKnowMore("demo");
                break;
            case R.id.know_more_specialist:
                showDialogKnowMore("specialist");
                break;
            case R.id.tv_logout:
                performLogout();
                break;
            case R.id.booking_ongoing:
                Utils.showToast(this,"eefef");
                if(sharedPrefUtils.getStringData(Constants.BOOK_TYPE).equalsIgnoreCase("Demo"))
                    Constants.BOOKING_ID = sharedPrefUtils.getStringData(Constants.BOOKING_ONGOING);
                else
                    Constants.MEETING_ID = sharedPrefUtils.getStringData(Constants.BOOKING_ONGOING);

                showFragment(new BookingConfirmedFragment());
                break;

        }

    }

    private void showDialogKnowMore(String type) {
        Dialog dialog = new Dialog(this);
        DialogHomeKnowMoreBinding binding = DataBindingUtil.inflate(LayoutInflater.from(this), R.layout. dialog_home_know_more, null, false);
        dialog.setContentView(binding.getRoot());

        binding.setType(type);
        getViewModelStore().clear();
        if(type.equalsIgnoreCase("demo")) {
            AppContentViewModelFactory factory = new AppContentViewModelFactory(this.getApplication(), Constants.MYDEMO_DESCRIPTIONS);
            AppContentViewModel appContentViewModel = ViewModelProviders.of(this, factory).get(AppContentViewModel.class);

            appContentViewModel.getMyDemoDescriptionsLiveData().observe(this, item -> {
                homeModel.setDescriptions(item.getLabels().get(0).getLabelInLanguage());
                binding.setHomeModel(homeModel);
            });
        }
        else{
            AppContentViewModelFactory factory = new AppContentViewModelFactory(this.getApplication(), Constants.SPECIALIST_DESCRIPTIONS);
            AppContentViewModel appContentViewModel = ViewModelProviders.of(this, factory).get(AppContentViewModel.class);

            appContentViewModel.getSpecialistsDescriptionsLiveData().observe(this, item -> {
                homeModel.setDescriptions(item.getLabels().get(0).getLabelInLanguage());
                binding.setHomeModel(homeModel);
            });
        }
        binding.knowMore.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dialog.dismiss();
                if(type.equalsIgnoreCase("demo"))
                    activityHomeBinding.layoutOptionsDemo.llDrivemydemo.performClick();
                else
                    activityHomeBinding.layoutOptionsDemo.llMeetspecialists.performClick();
            }
        });
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
        dialog.show();

    }


    private void setEmptyValues() {
        if(locationUtils!=null)
            locationUtils.clearmap();
        Constants.TIME="";
        Constants.DATE="";
        Constants.VIRTUAL_MEET_TYPE="";
        Constants.BOOKING_TYPE_ID="";
        Constants.BOOKING_PLACE_TYPE_ID="";
        Constants.MEETING_ID="";
        Constants.BOOKING_ID ="";
        Constants.MEETING_TYPE_ID="";
        Constants.MEETING_PLACE_TYPE_ID="";
    }

    public void showScheduleFragment(Fragment fragment) {
        behavior.setState(STATE_HIDDEN);
        behavior1.setState(BottomSheetBehavior.STATE_EXPANDED);
        behavior2.setState(STATE_COLLAPSED);


        activityHomeBinding.coordinatorSchedule.setBackgroundColor(Color.parseColor("#D9000000"));

        this.fragment = fragment;
        activityHomeBinding.coordinatorSchedule.setVisibility(View.VISIBLE);
        activityHomeBinding.scheduleFragmentContainerView.setVisibility(View.VISIBLE);
        activityHomeBinding.coordinator11.setVisibility(View.GONE);

        activityHomeBinding.scheduleFragmentContainerView.removeAllViews();
        slideUp(activityHomeBinding.coordinatorSchedule);

        getSupportFragmentManager().beginTransaction()
                .replace(R.id.schedule_fragment_container_view, fragment)
                .commit();

    }


    public void showFragment(Fragment fragment) {
//        behavior1.setState(BottomSheetBehavior.STATE_COLLAPSED);
        activityHomeBinding.getRoot().getViewTreeObserver().removeOnGlobalLayoutListener(globalListener);



        activityHomeBinding.coordinatorSchedule.setBackgroundColor(Color.TRANSPARENT);
        activityHomeBinding.coordinatorSchedule.setVisibility(View.GONE);
        activityHomeBinding.scheduleFragmentContainerView.setVisibility(View.GONE);
        if(fragment.toString().contains("ScheduleLater")){
            activityHomeBinding.coordinator11.setBackgroundColor(Color.parseColor("#D9000000"));
        }
        else
        {
            activityHomeBinding.coordinator11.setBackgroundColor(Color.TRANSPARENT);

        }
        this.fragment = fragment;
        activityHomeBinding.coordinator11.setVisibility(View.VISIBLE);
        activityHomeBinding.fragmentContainerView.removeAllViews();
        slideUp(activityHomeBinding.coordinator11);
        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                behavior.setState(BottomSheetBehavior.STATE_EXPANDED);
            }
        },500);

        behavior2.setState(BottomSheetBehavior.STATE_COLLAPSED);
        behavior1.setState(STATE_COLLAPSED);
        getSupportFragmentManager().beginTransaction()
                .replace(R.id.fragment_container_view, fragment)
                .commit();

        //activityHomeBinding.fragmentContainerView.setMinimumHeight(height/3);
        //behavior.setPeekHeight(height/3);
    }

    public void slideUp(View view){
        view.setVisibility(View.VISIBLE);
        TranslateAnimation animate = new TranslateAnimation(
                0,                 // fromXDelta
                0,                 // toXDelta
               100,  // fromYDelta
                0);                // toYDelta
        animate.setDuration(500);
        animate.setFillAfter(true);
        view.startAnimation(animate);
    }
    /**
     * Handles the result of the request for location permissions.
     */
    @Override
    public void onRequestPermissionsResult(int requestCode,
                                           @NonNull String[] permissions,
                                           @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        //100 for camera
        if(requestCode==100){
            ((VirtualMeetFragment)this.fragment).onRequestPermissionsResult(requestCode,permissions,grantResults);
        }
        /*else if (requestCode==Constants.DL){
            ((BookingConfirmedFragment)fragment).onRequestPermissionsResult(requestCode,permissions,grantResults);

        }*/
        else if(requestCode==1) {
            locationUtils.onRequestPermissionsResult(requestCode, permissions, grantResults);
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) {
                if(!Permissionsutils.checkForNotificationPermission(this))
                    Permissionsutils.askForNotificationPermission(this);
            }
        }


    }


    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if(requestCode==5)
        {
            locationUtils.onActivityResult(requestCode,resultCode,data);
        }

       /* else if(requestCode==Constants.DL){
            ((BookingConfirmedFragment)fragment).onActivityResult(requestCode,resultCode,data);
        }*/

    }

    private void callAllMapLocationApi() {
        CarDetailRequest mapRequest = new CarDetailRequest();
        mapRequest.setCarID(Constants.CARID);
        mapRequest.setUserID(userId);
        if(locationUtils.getLoc()!=null) {
            mapRequest.setLatitude(String.valueOf(locationUtils.getLoc().getLatitude()));
            mapRequest.setLongitude(String.valueOf(locationUtils.getLoc().getLongitude()));
        }
        else
        {
            mapRequest.setLatitude("0.0");
            mapRequest.setLongitude("0.0");

        }
        Call objectCall = RestClient.getApiService().allmaplocation(mapRequest);
        RestClient.makeApiRequest(this, objectCall, this, 1, true);

    }

    @Override
    public void onBackPressed() {
        if(fragment==null) {
            super.onBackPressed();
            finish();
        }
        else if(fragment.toString().contains("BookingStatusFragment") && !behavior.isDraggable()){
            return;
        }
        else if(behavior.getState() == STATE_EXPANDED || behavior.getState() == BottomSheetBehavior.STATE_COLLAPSED)
        {
            behavior.setState(BottomSheetBehavior.STATE_HIDDEN);
        }
        else {
            super.onBackPressed();
            finish();
        }
    }

    public Location getLocation()
    {
        if(locationUtils.getLoc()!=null)
            return locationUtils.getLoc();
        return null;
    }


    public void callSearchApi(String vehilceType,SearchResultInterface searchResultInterface)
    {
        if(locationUtils.getLoc()!=null) {
            carSearchRequestModel.setLatitude(String.valueOf(locationUtils.getLoc().getLatitude()));
            carSearchRequestModel.setLongitude(String.valueOf(locationUtils.getLoc().getLongitude()));
        }
        else
        {
            carSearchRequestModel.setLatitude("0.0");
            carSearchRequestModel.setLongitude("0.0");

        }
        carSearchRequestModel.setUserID(userId);
        carSearchRequestModel.setCarSectionType("0");
        //vehicleType=1 2 wheeler, 2 - four wheeler
        carSearchRequestModel.setVehicleType(vehilceType);
        carSearchRequestModel.setSpecialistID(specialistId);
        getViewModelStore().clear();
        SearchResultViewModelFactory factory = new SearchResultViewModelFactory(getApplication(), carSearchRequestModel);
        SearchResultViewModel searchResultViewModel = ViewModelProviders.of(this, factory).get(SearchResultViewModel.class);


        searchResultViewModel.getSearchResultLiveData().observe(this, item -> {
            searchResultInterface.onSearch(item);
            if(takeAdemoFragment==null)
                takeAdemoFragment = new TakeADemoFragment(specialistId);
            takeAdemoFragment.getFragment().updateCarList(item);


        });
    }

    public void showSelectFilerCars() {
        takeAdemoFragment = new TakeADemoFragment(specialistId);
        showFragment(takeAdemoFragment);
       // behavior.setState(STATE_EXPANDED);

    }

    @Override
    public void onApiResponse(Call<Object> call, Object response, int reqCode) throws Exception {
        if(reqCode==1)
        {
            MapLocationResponseModel mapLocationResponseModel = (MapLocationResponseModel) response;
            locationUtils.setLocationOnMap(mapLocationResponseModel.getMaplocationlist());
        }
     /*   else if(reqCode==2){
            DirectionsGeocodeResponse directionsGeocodeResponse = (DirectionsGeocodeResponse) response;
             currentLocation = directionsGeocodeResponse.getResults().get(0).getFormatted_address();
            if(sharedPrefUtils.getStringData(Constants.ADDRESS).trim().length()==0 || sharedPrefUtils.getStringData(Constants.ADDRESS).equalsIgnoreCase("ADDRESS"))
            sharedPrefUtils.saveData(Constants.ADDRESS,currentLocation);
        }*/

    }

    @Override
    public void onApiError(Call<Object> call, Object response, int reqCode) throws Exception {

    }

    public void setBehavior(boolean draggable) {
        behavior.setHideable(draggable);
    }

    public void setPeekheight(int measuredHeight) {
       /* PrintLog.v(measuredHeight+"=== "+height);
        if(measuredHeight!=0 && measuredHeight>height)
            behavior.setPeekHeight(measuredHeight+100);
        else
            behavior.setPeekHeight(height+100);*/

//        behavior.setPeekHeight(activityHomeBinding.layoutOptionsDemo.llOptions.getMeasuredHeight());
//        activityHomeBinding.fragmentContainerView.setMinimumHeight(behavior.getPeekHeight());
    }

    public void setPeekheightBookingConfirmed() {
//            behavior.setPeekHeight(activityHomeBinding.layoutOptionsDemo.llOptions.getMeasuredHeight());
//        activityHomeBinding.fragmentContainerView.setMinimumHeight(behavior.getPeekHeight());
    }

    public void hideBottomSheet() {
        behavior.setState(STATE_HIDDEN);
    }




    @Override
    protected void onResume() {
        super.onResume();
        PrintLog.v("onresume");

        if(sharedPrefUtils!=null && sharedPrefUtils.getStringData(Constants.BOOKING_ONGOING)!=null && !sharedPrefUtils.getStringData(Constants.BOOKING_ONGOING).equalsIgnoreCase("null")) {
            activityHomeBinding.bookingOngoing.setVisibility(View.VISIBLE);
        }
        else
            activityHomeBinding.bookingOngoing.setVisibility(View.GONE);
        if(homeModel!=null)
            if(sharedPrefUtils.getStringData(Constants.IMAGE_FILE)!=null && !sharedPrefUtils.getStringData(Constants.IMAGE_FILE).equalsIgnoreCase("IMAGE_FILE")) {
                homeModel.setImage(sharedPrefUtils.getStringData(Constants.IMAGE_FILE));
                activityHomeBinding.setHomeModel(homeModel);
                activityHomeBinding.executePendingBindings();

            }
    }
}
