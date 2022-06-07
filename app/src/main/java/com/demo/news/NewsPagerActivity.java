package com.demo.news;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;

import androidx.annotation.Nullable;
import androidx.core.view.GravityCompat;
import androidx.databinding.DataBindingUtil;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentStatePagerAdapter;

import com.demo.BaseActivity;
import com.demo.R;
import com.demo.databinding.ActivityNewsPagerBinding;
import com.demo.faq.model.FAQRequestModel;
import com.demo.home.HomeActivity;
import com.demo.home.profile.MyDemoActivity;
import com.demo.home.profile.MyProfileActivity;
import com.demo.utils.Constants;
import com.demo.utils.PrintLog;
import com.demo.utils.SharedPrefUtils;
import com.demo.webservice.ApiResponseListener;
import com.demo.webservice.RestClient;

import retrofit2.Call;

public class NewsPagerActivity extends BaseActivity implements ApiResponseListener {
    private ActivityNewsPagerBinding activityNewsBinding;
    private int position;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        activityNewsBinding =  DataBindingUtil.setContentView(this, R.layout.activity_news_pager);
        setBottomMenuLabels( activityNewsBinding.llBottom);
        setMenuLabels(activityNewsBinding.leftMenu.menuRecyclerview);
        activityNewsBinding.setHomeModel(homeModel);
         position = getIntent().getExtras().getInt("pos");
        activityNewsBinding.previous.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(activityNewsBinding.pager.getCurrentItem()>0)
                activityNewsBinding.pager.setCurrentItem(activityNewsBinding.pager.getCurrentItem()-1);
            }
        });
        activityNewsBinding.next.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(activityNewsBinding.pager.getCurrentItem()<activityNewsBinding.pager.getChildCount())
                    activityNewsBinding.pager.setCurrentItem(activityNewsBinding.pager.getCurrentItem()+1);
            }
        });
        callNewsApi();
    }

    private void callNewsApi() {
        FAQRequestModel faqRequestModel = new FAQRequestModel();
        faqRequestModel.setUserID(new SharedPrefUtils(this).getStringData(Constants.USER_ID));
        Call objectCall = RestClient.getApiService().getNews(faqRequestModel);
        RestClient.makeApiRequest(this, objectCall, this, 1, true);


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
                activityNewsBinding.drawerLy.openDrawer(GravityCompat.END);
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
    public void onApiResponse(Call<Object> call, Object response, int reqCode) throws Exception {
        NewsResponseModel newsResponseModel = (NewsResponseModel) response;
        ScreenSlidePagerAdapter screenSlidePagerAdapter = new ScreenSlidePagerAdapter(getSupportFragmentManager(),newsResponseModel);

        activityNewsBinding.pager.setAdapter(screenSlidePagerAdapter);
        activityNewsBinding.pager.setCurrentItem(position);


    }

    @Override
    public void onApiError(Call<Object> call, Object response, int reqCode) throws Exception {

    }

    public  class ScreenSlidePagerAdapter extends FragmentStatePagerAdapter {

        private int count;
        private NewsResponseModel rewardsResponseModel;


        public ScreenSlidePagerAdapter(FragmentManager fragmentManager, NewsResponseModel rewardsResponseModel) {
            super(fragmentManager);
            this.count = rewardsResponseModel.getNewslist().size();
            this.rewardsResponseModel = rewardsResponseModel;
        }


        // Returns the fragment to display for that page
        @Override
        public Fragment getItem(int position) {
            return  NewsPagerFragment.newInstance(rewardsResponseModel,position);

        }

        @Override
        public int getCount() {
            return count;
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
                activityNewsBinding.setHomeModel(homeModel);
                activityNewsBinding.executePendingBindings();

            }
    }

}
