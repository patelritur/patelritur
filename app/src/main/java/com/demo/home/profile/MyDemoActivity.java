package com.demo.home.profile;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.graphics.Typeface;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.FrameLayout;
import android.widget.TextView;

import androidx.annotation.Nullable;
import androidx.appcompat.widget.AppCompatImageView;
import androidx.core.view.GravityCompat;
import androidx.databinding.DataBindingUtil;
import androidx.fragment.app.FragmentStatePagerAdapter;
import androidx.lifecycle.ViewModelProviders;

import com.bumptech.glide.Glide;
import com.bumptech.glide.request.RequestOptions;
import com.demo.BaseActivity;
import com.demo.R;
import com.demo.databinding.FragmentProfileBinding;
import com.demo.home.HomeActivity;
import com.demo.home.model.AppContentModel;
import com.demo.home.model.viewmodel.AppContentViewModel;
import com.demo.home.model.viewmodel.AppContentViewModelFactory;
import com.demo.utils.Constants;
import com.demo.utils.PrintLog;
import com.demo.utils.SharedPrefUtils;
import com.google.android.material.tabs.TabLayout;

public class MyDemoActivity extends BaseActivity {
   private FragmentProfileBinding fragmentProfileBinding;
   private SharedPrefUtils sharedPrefUtils;
    private AppCompatImageView dotImageview;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        fragmentProfileBinding = DataBindingUtil.setContentView(this, R.layout.fragment_profile);
        sharedPrefUtils = new SharedPrefUtils(this);
        fragmentProfileBinding.name.setText(sharedPrefUtils.getStringData(Constants.FNAME)+" "+sharedPrefUtils.getStringData(Constants.LNAME));
        fragmentProfileBinding.email.setText(sharedPrefUtils.getStringData(Constants.EMAIL));
        fragmentProfileBinding.vaccinated.setText(sharedPrefUtils.getStringData(Constants.ISVACCINATED).equalsIgnoreCase("Y") ?"Vaccinated" : "Not Vaccinated");
        RequestOptions requestOptions = new RequestOptions();
        requestOptions.placeholder(R.drawable.user_default);
        requestOptions.circleCrop();
        PrintLog.v(sharedPrefUtils.getStringData(Constants.IMAGE));
        Glide.with(this).load(sharedPrefUtils.getStringData(Constants.IMAGE)).apply(requestOptions).into(fragmentProfileBinding.profileImage);
        fragmentProfileBinding.setHomeModel(homeModel);
        setBottomMenuLabels(fragmentProfileBinding.llBottom);
        setMenuLabels(fragmentProfileBinding.leftMenu.menuRecyclerview);


        callMyDemoOptionsApi();

    }


    private void setUpViewPagerFragment(AppContentModel item) {

        FragmentStatePagerAdapter adapterViewPager = new MyProfilePagerAdapter(getSupportFragmentManager(),item.getLabels().size());
        fragmentProfileBinding.pager.setAdapter(adapterViewPager);
        fragmentProfileBinding.pager.setOffscreenPageLimit(3);
        fragmentProfileBinding.tabLayout.setupWithViewPager(fragmentProfileBinding.pager);


        fragmentProfileBinding.tabLayout.setOnTabSelectedListener(new TabLayout.OnTabSelectedListener() {
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

    private void callMyDemoOptionsApi() {
        getViewModelStore().clear();
        AppContentViewModelFactory factory = new AppContentViewModelFactory(this.getApplication(), Constants.MYDEMO_MENU);
        AppContentViewModel appContentViewModel = ViewModelProviders.of(this, factory).get(AppContentViewModel.class);

        appContentViewModel.getMyDemoMenuLiveData().observe(this, item -> {
            setUpViewPagerFragment(item);
            setUpTabLayout(item);
        });
    }

    private void setUpTabLayout(AppContentModel item) {
        for (int i = 0; i < 3; i++) {
            // inflate the Parent LinearLayout Container for the tab
            // from the layout nav_tab.xml file that we created 'R.layout.nav_tab
            FrameLayout tab = (FrameLayout) LayoutInflater.from(this).inflate(R.layout.item_tab_layout, null);
            TextView tab_label = (TextView) tab.findViewById(R.id.textview);
            if(i==fragmentProfileBinding.pager.getCurrentItem()-1){
                Typeface typeface = getResources().getFont(R.font.montserrat_bold);
               tab_label.setTypeface(typeface);
            }
            if(i==2){
                 dotImageview = (AppCompatImageView) tab.findViewById(R.id.dot_iamgeview);
            }
            AppCompatImageView tab_icon = (AppCompatImageView) tab.findViewById(R.id.iamgeview);
            tab_label.setText(item.getLabels().get(i).getLabelInLanguage());
            Glide.with(this)
                    .load(item.getLabels().get(i).getLabelImage())
                    .into(tab_icon);
            // finally publish this custom view to navigation tab
            fragmentProfileBinding.tabLayout.getTabAt(i).setCustomView(tab);

        }

        if(getIntent().getExtras()!=null && getIntent().getExtras().getString("comeFrom")!=null){
            fragmentProfileBinding.pager.setCurrentItem(2);
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
               // startActivity(new Intent(this, MyDemoActivity.class));
                break;
            case R.id.ll_takedemo:
                startActivity(new Intent(this, HomeActivity.class).putExtra("comeFrom","takeAdemo"));
                finish();
                break;
            case R.id.imageview_menu:
                fragmentProfileBinding.drawerLy.openDrawer(GravityCompat.END);
                break;
            case R.id.tv_logout:
                performLogout();
                break;

        }
    }

    public void updateVisibleDot() {
        PrintLog.v("visible");
        dotImageview.setVisibility(View.VISIBLE);
    }



    @Override
    public void onBackPressed() {
        super.onBackPressed();
        finish();
    }
}
