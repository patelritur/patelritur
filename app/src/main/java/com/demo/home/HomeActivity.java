package com.demo.home;

import static com.google.android.material.bottomsheet.BottomSheetBehavior.STATE_COLLAPSED;
import static com.google.android.material.bottomsheet.BottomSheetBehavior.STATE_EXPANDED;
import static com.google.android.material.bottomsheet.BottomSheetBehavior.STATE_HIDDEN;

import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.location.Location;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewTreeObserver;
import android.view.WindowManager;
import android.view.animation.TranslateAnimation;
import android.widget.ImageView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.core.view.GravityCompat;
import androidx.databinding.DataBindingUtil;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.LifecycleOwner;
import androidx.lifecycle.ViewModelProviders;
import androidx.viewpager.widget.ViewPager;

import com.demo.BaseActivity;
import com.demo.R;
import com.demo.carDetails.ImageAdapter;
import com.demo.carDetails.model.CarDetailRequest;
import com.demo.carDetails.model.CarDetailResponse;
import com.demo.databinding.ActivityHomeBinding;
import com.demo.databinding.DialogColorBinding;
import com.demo.databinding.DialogHomeKnowMoreBinding;
import com.demo.databinding.ItemColorBinding;
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
import com.demo.utils.Constants;
import com.demo.utils.LocationUtils;
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
    private ViewTreeObserver.OnGlobalLayoutListener globalListener = null;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        activityHomeBinding = DataBindingUtil.setContentView(this, R.layout.activity_home);

        userId = sharedPrefUtils.getStringData(Constants.USER_ID);

        setBottomMenuLabels(activityHomeBinding.llBottom);
        setMenuLabels(activityHomeBinding.leftMenu.menuRecyclerview);
        setHomeMenuLabels();
        activityHomeBinding.setHomeModel(homeModel);
        activityHomeBinding.llName.setHomeModel(homeModel);
        activityHomeBinding.layoutOptionsDemo.setHomeModel(homeModel);
        activityHomeBinding.executePendingBindings();
        setBottomSheetBehaviour();

        SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager()
                .findFragmentById(R.id.map);
        locationUtils = new LocationUtils(this,mapFragment);
        if(getIntent().getExtras()!=null)
        {
            Utils.cancelJob(this);
            if(getIntent().getExtras().getString("notificationtype")!=null && getIntent().getExtras().getString("comeFrom")==null) {
                if (getIntent().getExtras().getString("notificationtype").equalsIgnoreCase("AcceptDemoRequest")) {
                    Constants.BOOKING_ID = getIntent().getExtras().getString("demoid");
                    Constants.BOOK_TYPE = "Demo";
                } else if(getIntent().getExtras().getString("notificationtype").equalsIgnoreCase("AcceptMeetRequest")){
                    Constants.MEETING_ID = getIntent().getExtras().getString("demoid");
                    Constants.BOOK_TYPE = "Meeting";
                }
                showFragment(new BookingConfirmedFragment());
            }
            else if(getIntent().getExtras().getString("comeFrom").equalsIgnoreCase("notifications")){
                showFragment(new BookingConfirmedFragment(true));

            }
            else if(getIntent().getExtras().getString("comeFrom").equalsIgnoreCase("takeAdemo")){
                activityHomeBinding.layoutOptionsDemo.llDrivemydemo.performClick();
            }



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
        ViewTreeObserver viewTreeObserver = activityHomeBinding.layoutOptionsDemo.llOptions.getViewTreeObserver();
        behavior = BottomSheetBehavior.from(activityHomeBinding.fragmentContainerView);
        behavior1 = BottomSheetBehavior.from(activityHomeBinding.scheduleFragmentContainerView);
        globalListener = new ViewTreeObserver.OnGlobalLayoutListener() {
            @Override
            public void onGlobalLayout() {
                height = activityHomeBinding.layoutOptionsDemo.llOptions.getMeasuredHeight() + activityHomeBinding.llName.getRoot().getMeasuredHeight();
                behavior.setPeekHeight(height);
                activityHomeBinding.fragmentContainerView.setMinimumHeight(height);



            }
        };
        //  behavior.setState(BottomSheetBehavior.STATE_HIDDEN);
            viewTreeObserver.addOnGlobalLayoutListener(globalListener);
        behavior1.addBottomSheetCallback(new BottomSheetBehavior.BottomSheetCallback() {
            @Override
            public void onStateChanged(@NonNull View bottomSheet, int newState) {
                if(newState==STATE_HIDDEN){
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
                    if(locationUtils!=null)
                        locationUtils.clearmap();
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
            case R.id.know_more_demo:
                showDialogKnowMore("demo");
                break;
            case R.id.know_more_specialist:
                showDialogKnowMore("specialist");
                break;


            case R.id.tv_logout:
                performLogout();
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
        activityHomeBinding.getRoot().getViewTreeObserver().removeOnGlobalLayoutListener(globalListener);
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


        getSupportFragmentManager().beginTransaction()
                .replace(R.id.fragment_container_view, fragment)
                .commit();
        behavior1.setState(BottomSheetBehavior.STATE_COLLAPSED);

        activityHomeBinding.fragmentContainerView.setMinimumHeight(height+100);
           behavior.setPeekHeight(height+100);
        PrintLog.v("=="+height);
        PrintLog.v("===EE"+fragment,"EEE"+activityHomeBinding.fragmentContainerView.getMeasuredHeight());
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
        if(fragment==null)
            super.onBackPressed();
        if(fragment.toString().contains("BookingStatusFragment")){
            return;
        }
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
        PrintLog.v(measuredHeight+"=== "+height);
        if(measuredHeight!=0 && measuredHeight>height)
            behavior.setPeekHeight(measuredHeight+100);
        else
            behavior.setPeekHeight(height+100);
        activityHomeBinding.fragmentContainerView.setMinimumHeight(behavior.getPeekHeight());
    }

    public void hideBottomSheet() {
        behavior.setState(STATE_HIDDEN);
    }
}
