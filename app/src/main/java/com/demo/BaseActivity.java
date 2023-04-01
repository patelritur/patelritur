package com.demo;

import android.content.Context;
import android.content.Intent;
import android.content.res.Configuration;
import android.net.Uri;
import android.os.Bundle;
import android.view.View;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.app.AppCompatDelegate;
import androidx.core.view.GravityCompat;
import androidx.drawerlayout.widget.DrawerLayout;
import androidx.lifecycle.ViewModelProviders;
import androidx.recyclerview.widget.RecyclerView;

import com.cometchat.pro.core.CometChat;
import com.cometchat.pro.exceptions.CometChatException;
import com.demo.databinding.BottomMenuBinding;
import com.demo.home.HomeActivity;
import com.demo.home.HomeMenuAdapter;
import com.demo.home.model.AppContentModel;
import com.demo.home.model.HomeModel;
import com.demo.home.model.viewmodel.AppContentViewModel;
import com.demo.home.model.viewmodel.AppContentViewModelFactory;
import com.demo.home.profile.MyDemoActivity;
import com.demo.registrationLogin.LoginActivity;
import com.demo.utils.ClickHandlers;
import com.demo.utils.Constants;
import com.demo.utils.NotificationUtils;
import com.demo.utils.PrintLog;
import com.demo.utils.SharedPrefUtils;
import com.demo.utils.Utils;
import com.google.android.material.snackbar.Snackbar;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

public abstract class BaseActivity extends AppCompatActivity implements ClickHandlers {

    public SharedPrefUtils sharedPrefUtils;
    public HomeModel homeModel;


    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_NO);
        sharedPrefUtils = new SharedPrefUtils(this);
        homeModel = new HomeModel(this);
        homeModel.setfName(sharedPrefUtils.getStringData(Constants.FNAME));
        homeModel.setlName(sharedPrefUtils.getStringData(Constants.LNAME));
        homeModel.setImage(sharedPrefUtils.getStringData(Constants.IMAGE));
        if(sharedPrefUtils.getStringData(Constants.IMAGE_FILE)!=null && !sharedPrefUtils.getStringData(Constants.IMAGE_FILE).equalsIgnoreCase("IMAGE_FILE")) {
            homeModel.setImage(sharedPrefUtils.getStringData(Constants.IMAGE_FILE));
        }
        homeModel.setGreetingMessage(Utils.getGreetingMessage(this));
    }

    public List<AppContentModel.Label> setMenuLabels(RecyclerView menuRecyclerview) {
        getViewModelStore().clear();
        AppContentViewModelFactory factory = new AppContentViewModelFactory(this.getApplication(), Constants.LEFT_MENU);
        AppContentViewModel appContentViewModel = ViewModelProviders.of(this, factory).get(AppContentViewModel.class);

        appContentViewModel.getMenuLiveData().observe(this, item -> {
            menuRecyclerview.setAdapter(new HomeMenuAdapter(this, (ArrayList<AppContentModel.Label>) item.getLabels()));


        });
        return null;
    }

    public void setBottomMenuLabels(BottomMenuBinding bottomMenuLabels) {
        AppContentViewModelFactory factory = new AppContentViewModelFactory(this.getApplication(), Constants.MAIN_MENU);
        AppContentViewModel appContentViewModel = ViewModelProviders.of(this, factory).get(AppContentViewModel.class);

        appContentViewModel.getBottomMenuLiveData().observe(this, item -> {
            this.homeModel.setBottomMenuFirstName(item.getLabels().get(0).getLabelInLanguage());
            this.homeModel.setBottomMenuFirstImage(item.getLabels().get(0).getLabelImage());
            this.homeModel.setBottomMenuSecondName(item.getLabels().get(1).getLabelInLanguage());
            this.homeModel.setBottomMenuSecondImage(item.getLabels().get(1).getLabelImage());
            this.homeModel.setBottomMenuThirdName(item.getLabels().get(2).getLabelInLanguage());
            this.homeModel.setBottomMenuThirdImage(item.getLabels().get(2).getLabelImage());
            bottomMenuLabels.setHomeModel(this.homeModel);
        });
    }



    public void NavigateToActivity(Intent intent) {
        startActivity(intent);
    }

    @Override
    public void  attachBaseContext( Context context) {

        Configuration newOverride = new Configuration();
        newOverride.fontScale = 1.0f;
        applyOverrideConfiguration(newOverride);

        super.attachBaseContext(context);
    }

    public void performLogout() {

        NotificationUtils.setUpFCMNotifiction(this,sharedPrefUtils.getStringData(Constants.USER_ID),"Remove");

        CometChat.logout(new CometChat.CallbackListener<String>() {
            @Override
            public void onSuccess(String s) {
            }

            @Override
            public void onError(CometChatException e) {
            }
        });
        sharedPrefUtils.clearData(this);
        Intent intent = new Intent(this, LoginActivity.class);
        intent.addFlags( Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_NEW_TASK);
        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
        NavigateToActivity(intent);
        finish();
    }

    @Override
    protected void onResume() {
        super.onResume();

        if(homeModel!=null)
        if(sharedPrefUtils.getStringData(Constants.IMAGE_FILE)!=null && !sharedPrefUtils.getStringData(Constants.IMAGE_FILE).equalsIgnoreCase("IMAGE_FILE")) {
            PrintLog.v("homemodel");
            homeModel.setImage(sharedPrefUtils.getStringData(Constants.IMAGE_FILE));
        }
    }

}
