package com.demo.launch;

import android.content.Intent;
import android.graphics.Typeface;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.TextView;

import androidx.annotation.Nullable;
import androidx.appcompat.widget.LinearLayoutCompat;
import androidx.core.view.GravityCompat;
import androidx.databinding.DataBindingUtil;
import androidx.fragment.app.FragmentStatePagerAdapter;

import com.demo.BaseActivity;
import com.demo.R;
import com.demo.databinding.ActivityLaunchBinding;
import com.demo.home.HomeActivity;
import com.demo.home.profile.MyDemoActivity;
import com.demo.home.profile.MyProfileActivity;
import com.demo.utils.Constants;
import com.demo.utils.PrintLog;
import com.google.android.material.tabs.TabLayout;

public class LaunchActivity extends BaseActivity {

    private ActivityLaunchBinding activityLaunchBinding;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        activityLaunchBinding =  DataBindingUtil.setContentView(this,R.layout.activity_launch);
        FragmentStatePagerAdapter adapterViewPager = new LaunchPagerAdapter(getSupportFragmentManager(),2);

       setBottomMenuLabels( activityLaunchBinding.llBottom);
        setMenuLabels(activityLaunchBinding.leftMenu.menuRecyclerview);

        activityLaunchBinding.setHomeModel(homeModel);
        activityLaunchBinding.executePendingBindings();
        activityLaunchBinding.pager.setAdapter(adapterViewPager);
        activityLaunchBinding.tabLayout.setupWithViewPager( activityLaunchBinding.pager);

        setUpTabLayout();
        activityLaunchBinding.tabLayout.setOnTabSelectedListener(new TabLayout.OnTabSelectedListener() {
            @Override
            public void onTabSelected(TabLayout.Tab tab) {
                Typeface typeface = getResources().getFont(R.font.montserrat_bold);
                ((TextView) tab.getCustomView().findViewById(R.id.textview)).setTypeface(typeface);

            }

            @Override
            public void onTabUnselected(TabLayout.Tab tab) {
                Typeface typeface = getResources().getFont(R.font.montserrat_regular);
                ((TextView) tab.getCustomView().findViewById(R.id.textview)).setTypeface(typeface);
            }

            @Override
            public void onTabReselected(TabLayout.Tab tab) {

            }
        });

    }
    private void setUpTabLayout() {
        for (int i = 0; i < 2; i++) {
            LinearLayoutCompat tab = (LinearLayoutCompat) LayoutInflater.from(this).inflate(R.layout.item_status_tab_layout, null);
            TextView tab_label = (TextView) tab.findViewById(R.id.textview);
            if(i==0) {
                tab_label.setText("Upcoming Launch");
                tab_label.setTypeface(Typeface.defaultFromStyle(Typeface.BOLD));
            }
            else
                tab_label.setText("Recent Launch");
            activityLaunchBinding.tabLayout.getTabAt(i).setCustomView(tab);

        }

    }
    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.ll_home:
                   startActivity(new Intent(this, HomeActivity.class));
                   finish();
                break;
            case R.id.ll_mydemos:
                startActivity(new Intent(this, MyDemoActivity.class));
                finish();
                break;
            case R.id.ll_takedemo:
                startActivity(new Intent(this, HomeActivity.class).putExtra("comeFrom","takeAdemo"));
                finish();
                break;
            case R.id.imageview_menu:
                activityLaunchBinding.drawerLy.openDrawer(GravityCompat.END);
                break;
            case R.id.tv_logout:
                performLogout();
                break;
            case R.id.see_profile:
                startActivity(new Intent(this, MyProfileActivity.class));
                break;
        }
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        finish();
    }
    @Override
    protected void onResume() {
        super.onResume();

        if(homeModel!=null)
            if(sharedPrefUtils.getStringData(Constants.IMAGE_FILE)!=null && !sharedPrefUtils.getStringData(Constants.IMAGE_FILE).equalsIgnoreCase("IMAGE_FILE")) {
                PrintLog.v("homemodel mydeo");
                homeModel.setImage(sharedPrefUtils.getStringData(Constants.IMAGE_FILE));
                activityLaunchBinding.setHomeModel(homeModel);
                activityLaunchBinding.executePendingBindings();

            }
    }
}
