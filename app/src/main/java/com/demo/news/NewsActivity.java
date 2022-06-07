package com.demo.news;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;

import androidx.annotation.Nullable;
import androidx.core.view.GravityCompat;
import androidx.databinding.DataBindingUtil;

import com.demo.BaseActivity;
import com.demo.R;
import com.demo.databinding.ActivityNewsBinding;
import com.demo.faq.model.FAQRequestModel;
import com.demo.home.HomeActivity;
import com.demo.home.profile.MyDemoActivity;
import com.demo.home.profile.MyProfileActivity;
import com.demo.utils.Constants;
import com.demo.utils.PrintLog;
import com.demo.utils.SharedPrefUtils;
import com.demo.webservice.ApiResponseListener;
import com.demo.webservice.RestClient;

import java.util.ArrayList;

import retrofit2.Call;

public class NewsActivity extends BaseActivity implements ApiResponseListener {
    private ActivityNewsBinding activityNewsBinding;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        activityNewsBinding =  DataBindingUtil.setContentView(this, R.layout.activity_news);
        setBottomMenuLabels( activityNewsBinding.llBottom);
        setMenuLabels(activityNewsBinding.leftMenu.menuRecyclerview);
        activityNewsBinding.setHomeModel(homeModel);
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
    public void onBackPressed() {
        super.onBackPressed();
        finish();
    }

    @Override
    public void onApiResponse(Call<Object> call, Object response, int reqCode) throws Exception {
        NewsResponseModel newsResponseModel = (NewsResponseModel) response;
        activityNewsBinding.newsRecyclerview.setAdapter(new NewsListAdapter(this, (ArrayList<NewsResponseModel.Newslist>) newsResponseModel.getNewslist()));


    }

    @Override
    public void onApiError(Call<Object> call, Object response, int reqCode) throws Exception {

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
