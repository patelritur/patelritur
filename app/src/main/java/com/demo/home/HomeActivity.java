package com.demo.home;

import static com.google.android.material.bottomsheet.BottomSheetBehavior.STATE_EXPANDED;
import static com.google.android.material.bottomsheet.BottomSheetBehavior.STATE_HIDDEN;

import android.content.Intent;
import android.graphics.Color;
import android.location.Location;
import android.os.Bundle;
import android.view.View;
import android.view.ViewTreeObserver;
import android.view.animation.TranslateAnimation;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.core.view.GravityCompat;
import androidx.databinding.DataBindingUtil;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.LifecycleOwner;
import androidx.lifecycle.ViewModelProviders;

import com.demo.BaseActivity;
import com.demo.R;
import com.demo.carDetails.model.CarDetailRequest;
import com.demo.databinding.ActivityHomeBinding;
import com.demo.home.booking.BookingConfirmedFragment;
import com.demo.home.booking.DemoPlaceBookingFragment;
import com.demo.home.booking.ScheduleBookingFragment;
import com.demo.home.booking.model.MapLocationResponseModel;
import com.demo.home.meeting.MeetingPlaceFragment;
import com.demo.home.model.CarSearchRequestModel;
import com.demo.home.model.viewmodel.AppContentViewModel;
import com.demo.home.model.viewmodel.AppContentViewModelFactory;
import com.demo.home.model.viewmodel.SearchResultViewModel;
import com.demo.home.model.viewmodel.SearchResultViewModelFactory;
import com.demo.home.profile.MyDemoActivity;
import com.demo.notifications.NotificationActivity;
import com.demo.registrationLogin.LoginActivity;
import com.demo.utils.Constants;
import com.demo.utils.LocationUtils;
import com.demo.utils.NotificationUtils;
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
    private BottomSheetBehavior<View> behavior,behavior1;
    public LocationUtils locationUtils;
    protected ArrayList<String> BudgetSelectedId = new ArrayList<>();
    protected ArrayList<String> BrandSelectedId = new ArrayList<>();
    protected ArrayList<String> SegmentSelectedId = new ArrayList<>();
    public CarSearchRequestModel carSearchRequestModel = new CarSearchRequestModel();
    private TakeADemoFragment takeAdemoFragment;
    public String userId;
    private Fragment fragment;
    private int height;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        activityHomeBinding = DataBindingUtil.setContentView(this, R.layout.activity_home);
       
        userId = sharedPrefUtils.getStringData(Constants.USER_ID);
        /*homeModel.setfName(sharedPrefUtils.getStringData(Constants.FNAME));
        homeModel.setlName(sharedPrefUtils.getStringData(Constants.LNAME));
        homeModel.setImage(sharedPrefUtils.getStringData(Constants.IMAGE));
        homeModel.setGreetingMessage(Utils.getGreetingMessage(this));
        setBottomMenuLabels();
        setHomeMenuLabels();
        setMenuLabels();*/
        setBottomMenuLabels(activityHomeBinding.llBottom);

        setMenuLabels(activityHomeBinding.leftMenu.menuRecyclerview);
        setHomeMenuLabels();
//        activityHomeBinding.leftMenu.menuRecyclerview.setAdapter(new HomeMenuAdapter(this, (ArrayList<AppContentModel.Label>) setMenuLabels()));

        NotificationUtils.setUpFCMNotifiction(this,userId);

        activityHomeBinding.setHomeModel(homeModel);
        activityHomeBinding.llName.setHomeModel(homeModel);
        activityHomeBinding.executePendingBindings();
        activityHomeBinding.layoutOptionsDemo.setHomeModel(homeModel);
        SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager()
                .findFragmentById(R.id.map);
        locationUtils = new LocationUtils(this,mapFragment);
        setBottomSheetBehaviour();
        if(getIntent().getExtras()!=null)
        {
            Utils.cancelJob(this);
            Constants.BOOKINGID = getIntent().getExtras().getString("demoid");
            showFragment(new BookingConfirmedFragment());
        }
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
        ViewTreeObserver viewTreeObserver = activityHomeBinding.layoutOptionsDemo.getRoot().getViewTreeObserver();
        behavior = BottomSheetBehavior.from(activityHomeBinding.fragmentContainerView);
        behavior1 = BottomSheetBehavior.from(activityHomeBinding.scheduleFragmentContainerView);
        //  behavior.setState(BottomSheetBehavior.STATE_HIDDEN);
        viewTreeObserver.addOnGlobalLayoutListener(new ViewTreeObserver.OnGlobalLayoutListener() {
            @Override
            public void onGlobalLayout() {
                activityHomeBinding.layoutOptionsDemo.getRoot().getViewTreeObserver().removeOnGlobalLayoutListener(this);
                height = activityHomeBinding.layoutOptionsDemo.getRoot().getMeasuredHeight() + activityHomeBinding.llName.getRoot().getMeasuredHeight();
                behavior.setPeekHeight(height);
                //  activityHomeBinding.fragmentContainerView.setMinimumHeight(height);

            }
        });
        behavior1.addBottomSheetCallback(new BottomSheetBehavior.BottomSheetCallback() {
            @Override
            public void onStateChanged(@NonNull View bottomSheet, int newState) {
                if(newState==STATE_HIDDEN){

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
            }

            @Override
            public void onSlide(@NonNull View bottomSheet, float slideOffset) {

            }
        });
        behavior.addBottomSheetCallback(new BottomSheetBehavior.BottomSheetCallback() {
            @Override
            public void onStateChanged(@NonNull View bottomSheet, int newState) {
                if(newState==STATE_HIDDEN){

                    /*if(fragment.toString().contains("ScheduleBookingFragment")){
                        showFragment(new DemoPlaceBookingFragment());
                    }
                    else */if(fragment.toString().contains("ScheduleLaterFragment")){
                        showScheduleFragment(new ScheduleBookingFragment());
                    }
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

    }



    @Override
    public void onClick(View view) {
        switch (view.getId())
        {
            case R.id.ll_home:
                   startActivity(new Intent(this, HomeActivity.class));

                break;
            case R.id.ll_mydemos:
                startActivity(new Intent(this, MyDemoActivity.class));
                break;
            case R.id.ll_takedemo:
                Utils.showToastComingSoon(this);
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
                takeAdemoFragment = new TakeADemoFragment();
                showFragment(takeAdemoFragment);
                break;
            case R.id.ll_drivemydemo:
                Constants.BOOK_TYPE="Demo";
                setEmptyValues();
                activityHomeBinding.layoutOptionsDemo.llDrivemydemo.setBackgroundResource(R.drawable.border_red_rounded_corner);
                activityHomeBinding.layoutOptionsDemo.llMeetspecialists.setBackgroundResource(R.drawable.white_border);
                takeAdemoFragment = new TakeADemoFragment();
                showFragment(takeAdemoFragment);
                break;

            case R.id.iv_notification:
                startActivity(new Intent(this, NotificationActivity.class));
                break;
            case R.id.tv_logout:
                sharedPrefUtils.clearData(this);
                Intent intent = new Intent(this, LoginActivity.class);
                intent.addFlags( Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_NEW_TASK);
                intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
                NavigateToActivity(intent);
                finish();
                break;

        }

    }



    private void setEmptyValues() {
        locationUtils.clearmap();
        Constants.TIME="";
        Constants.DATE="";
        Constants.VIRTUAL_MEET_TYPE="";
        Constants.BOOKING_TYPE_ID="";
        Constants.BOOKING_PLACE_TYPE_ID="";
        Constants.MEETING_ID="";
        Constants.BOOKINGID="";
        Constants.MEETING_TYPE_ID="";
        Constants.MEETING_PLACE_TYPE_ID="";
    }

    public void showScheduleFragment(Fragment fragment) {
        behavior.setState(BottomSheetBehavior.STATE_COLLAPSED);
        behavior1.setState(BottomSheetBehavior.STATE_COLLAPSED);

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
        behavior.setState(BottomSheetBehavior.STATE_COLLAPSED);
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

        if(fragment!=null)
            getSupportFragmentManager().beginTransaction().
                    remove( fragment).commit();

        getSupportFragmentManager().beginTransaction()
                .replace(R.id.fragment_container_view, fragment)
                .commit();
        behavior1.setState(BottomSheetBehavior.STATE_COLLAPSED);

        activityHomeBinding.fragmentContainerView.setMinimumHeight(height);
        behavior.setPeekHeight(height);
        PrintLog.v("EEE"+fragment,"EEE"+activityHomeBinding.fragmentContainerView.getMeasuredHeight());
    }

    public void slideUp(View view){
        view.setVisibility(View.VISIBLE);
        TranslateAnimation animate = new TranslateAnimation(
                0,                 // fromXDelta
                0,                 // toXDelta
                view.getHeight(),  // fromYDelta
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
        locationUtils.onRequestPermissionsResult(requestCode,permissions,grantResults);


    }


    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if(requestCode==5)
        {
            locationUtils.onActivityResult(requestCode,resultCode,data);
        }
        if(resultCode==100)
        {
            callAllMapLocationApi();
            if(Constants.BOOK_TYPE.equalsIgnoreCase("Demo")) {
                showFragment(new DemoPlaceBookingFragment());
            }
            else
            {
                showFragment(new MeetingPlaceFragment());
            }
        }
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

        if(behavior.getState() == STATE_EXPANDED || behavior.getState() == BottomSheetBehavior.STATE_COLLAPSED)
        {
            behavior.setState(BottomSheetBehavior.STATE_HIDDEN);
            return;
        }
        super.onBackPressed();
    }

    public Location getLocation()
    {
        if(locationUtils.getLoc()!=null)
            return locationUtils.getLoc();
        return null;
    }


    public void callSearchApi(SearchResultInterface searchResultInterface)
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
        carSearchRequestModel.setVehicleType("2");
        getViewModelStore().clear();
        SearchResultViewModelFactory factory = new SearchResultViewModelFactory(getApplication(), carSearchRequestModel);
        SearchResultViewModel searchResultViewModel = ViewModelProviders.of(this, factory).get(SearchResultViewModel.class);


        searchResultViewModel.getSearchResultLiveData().observe(this, item -> {
            searchResultInterface.onSearch(item);
            takeAdemoFragment.getFragment().updateCarList(item);

        });
    }

    public void showSelectFilerCars() {
        takeAdemoFragment = new TakeADemoFragment();
        showFragment(takeAdemoFragment);
        behavior.setState(STATE_EXPANDED);

    }

    @Override
    public void onApiResponse(Call<Object> call, Object response, int reqCode) throws Exception {
        if(reqCode==1)
        {
            MapLocationResponseModel mapLocationResponseModel = (MapLocationResponseModel) response;
            locationUtils.setLocationOnMap(mapLocationResponseModel.getMaplocationlist());
        }

    }

    @Override
    public void onApiError(Call<Object> call, Object response, int reqCode) throws Exception {

    }

    public void setBehavior(boolean draggable) {
        behavior.setDraggable(draggable);
    }

    public void setPeekheight(int measuredHeight) {
        if(measuredHeight!=0)
            behavior.setPeekHeight(measuredHeight+100);
        else
            behavior.setPeekHeight(height+100);
    }

    public void hideBottomSheet() {
        behavior.setState(STATE_HIDDEN);
    }
}
