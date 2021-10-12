package com.demo.home;

import static com.google.android.material.bottomsheet.BottomSheetBehavior.STATE_EXPANDED;

import android.content.Intent;
import android.location.Location;
import android.os.Bundle;
import android.view.View;
import android.view.ViewTreeObserver;
import android.view.animation.TranslateAnimation;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.core.view.GravityCompat;
import androidx.databinding.DataBindingUtil;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.LifecycleOwner;
import androidx.lifecycle.ViewModelProviders;

import com.demo.BaseActivity;
import com.demo.R;
import com.demo.databinding.ActivityHomeBinding;
import com.demo.home.model.CarSearchRequestModel;
import com.demo.home.model.HomeModel;
import com.demo.home.model.viewmodel.AppContentViewModel;
import com.demo.home.model.viewmodel.AppContentViewModelFactory;
import com.demo.home.model.viewmodel.SearchResultViewModel;
import com.demo.home.model.viewmodel.SearchResultViewModelFactory;
import com.demo.utils.Constants;
import com.demo.utils.LocationUtils;
import com.demo.utils.PrintLog;
import com.demo.utils.SharedPrefUtils;
import com.demo.utils.Utils;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.material.bottomsheet.BottomSheetBehavior;

import java.util.ArrayList;

public class HomeActivity extends BaseActivity implements LifecycleOwner {
    private ActivityHomeBinding activityHomeBinding;
    SharedPrefUtils sharedPrefUtils;
    private BottomSheetBehavior<View> behavior;
    LocationUtils locationUtils;
    private int height;
    protected HomeModel homeModel;
    protected ArrayList<String> BudgetSelectedId = new ArrayList<>();
    protected ArrayList<String> BrandSelectedId = new ArrayList<>();
    protected ArrayList<String> SegmentSelectedId = new ArrayList<>();
    CarSearchRequestModel carSearchRequestModel = new CarSearchRequestModel();
    private TakeADemoFragment takeAdemoFragment;


    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        activityHomeBinding = DataBindingUtil.setContentView(this, R.layout.activity_home);
        sharedPrefUtils = new SharedPrefUtils(this);
        homeModel = new HomeModel(this);
        homeModel.setfName(sharedPrefUtils.getStringData(Constants.FNAME));
        homeModel.setlName(sharedPrefUtils.getStringData(Constants.LNAME));
        homeModel.setGreetingMessage(Utils.getGreetingMessage(this));

        setBottomMenuLabels();
        setHomeMenuLabels();
        activityHomeBinding.setHomeModel(homeModel);
        activityHomeBinding.llName.setHomeModel(homeModel);
        activityHomeBinding.layoutOptionsDemo.setHomeModel(homeModel);
        SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager()
                .findFragmentById(R.id.map);
        locationUtils = new LocationUtils(this,mapFragment);
        setBottomSheetBehaviour();

    }

    private void setBottomMenuLabels() {
        AppContentViewModelFactory factory = new AppContentViewModelFactory(this.getApplication(), "MainMenu");
        AppContentViewModel appContentViewModel = ViewModelProviders.of(this, factory).get(AppContentViewModel.class);

        appContentViewModel.getBottomMenuLiveData().observe(this, item -> {
            homeModel.setBottomMenuFirstName(item.getLabels().get(0).getLabelInLanguage());
            homeModel.setBottomMenuFirstImage(item.getLabels().get(0).getLabelImage());
            homeModel.setBottomMenuSecondName(item.getLabels().get(1).getLabelInLanguage());
            homeModel.setBottomMenuSecondImage(item.getLabels().get(1).getLabelImage());
            homeModel.setBottomMenuThirdName(item.getLabels().get(2).getLabelInLanguage());
            homeModel.setBottomMenuThirdImage(item.getLabels().get(2).getLabelImage());

            activityHomeBinding.llBottom.setHomeModel(homeModel);
            activityHomeBinding.executePendingBindings();
        });
    }

    private void setHomeMenuLabels() {
        getViewModelStore().clear();
        AppContentViewModelFactory factory = new AppContentViewModelFactory(this.getApplication(), "HomeMenu");
        AppContentViewModel appContentViewModel = ViewModelProviders.of(this, factory).get(AppContentViewModel.class);

        appContentViewModel.getHomeMenuLiveData().observe(this, item -> {
            homeModel.setHomeMenuFirstName(item.getLabels().get(0).getLabelInLanguage());
            homeModel.setHomeMenuFirstImage(item.getLabels().get(0).getLabelImage());
            homeModel.setHomeMenuSecondName(item.getLabels().get(1).getLabelInLanguage());
            homeModel.setHomeMenuSecondImage(item.getLabels().get(1).getLabelImage());
            activityHomeBinding.layoutOptionsDemo.setHomeModel(homeModel);
            activityHomeBinding.executePendingBindings();
        });
    }

    private void setBottomSheetBehaviour() {
        ViewTreeObserver viewTreeObserver = activityHomeBinding.layoutOptionsDemo.getRoot().getViewTreeObserver();
        behavior = BottomSheetBehavior.from(activityHomeBinding.ll1);
        behavior.setState(BottomSheetBehavior.STATE_HIDDEN);
        viewTreeObserver.addOnGlobalLayoutListener(new ViewTreeObserver.OnGlobalLayoutListener() {
            @Override
            public void onGlobalLayout() {
                activityHomeBinding.layoutOptionsDemo.getRoot().getViewTreeObserver().removeOnGlobalLayoutListener(this);
                height = activityHomeBinding.layoutOptionsDemo.getRoot().getMeasuredHeight()+activityHomeBinding.llName.getRoot().getMeasuredHeight();
                behavior.setPeekHeight(height);


            }
        });
        behavior.addBottomSheetCallback(new BottomSheetBehavior.BottomSheetCallback() {
            @Override
            public void onStateChanged(@NonNull View bottomSheet, int newState) {

            }

            @Override
            public void onSlide(@NonNull View bottomSheet, float slideOffset) {
                PrintLog.v("sds"+slideOffset);
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
            case R.id.ll_meetspecialists:
                activityHomeBinding.layoutOptionsDemo.llMeetspecialists.setBackgroundResource(R.drawable.border_red_rounded_corner);
                activityHomeBinding.layoutOptionsDemo.llDrivemydemo.setBackgroundResource(R.drawable.white_border);
                showFragment(new MeetingPlaceFragment());
                break;
            case R.id.ll_drivemydemo:

                activityHomeBinding.layoutOptionsDemo.llDrivemydemo.setBackgroundResource(R.drawable.border_red_rounded_corner);
                activityHomeBinding.layoutOptionsDemo.llMeetspecialists.setBackgroundResource(R.drawable.white_border);
              takeAdemoFragment = new TakeADemoFragment();
                showFragment(takeAdemoFragment);
                break;
            case R.id.imageview_menu:
                activityHomeBinding.drawerLy.openDrawer(GravityCompat.END);
                break;

        }

    }

    private void showFragment(Fragment fragment) {
        activityHomeBinding.coordinator11.setVisibility(View.VISIBLE);
        behavior.setState(BottomSheetBehavior.STATE_COLLAPSED);
        activityHomeBinding.ll1.removeAllViews();
        slideUp(activityHomeBinding.coordinator11);
        getSupportFragmentManager().beginTransaction()
                .add(R.id.ll1, fragment)
                .commit();
    }

    public void slideUp(View view){
        view.setVisibility(View.VISIBLE);
        TranslateAnimation animate = new TranslateAnimation(
                0,                 // fromXDelta
                0,                 // toXDelta
                view.getHeight(),  // fromYDelta
                0);                // toYDelta
        animate.setDuration(1000);
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
            carSearchRequestModel.setLatitude(String.valueOf(locationUtils.getLoc().getLongitude()));
        }
        else
        {
            carSearchRequestModel.setLatitude("0.0");
            carSearchRequestModel.setLatitude("0.0");

        }
        carSearchRequestModel.setUserID(sharedPrefUtils.getStringData(Constants.USER_ID));
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
}
