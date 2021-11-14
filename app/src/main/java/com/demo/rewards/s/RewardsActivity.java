package com.demo.rewards.s;

import android.os.Bundle;
import android.view.View;

import androidx.annotation.Nullable;
import androidx.databinding.DataBindingUtil;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentStatePagerAdapter;

import com.demo.BaseActivity;
import com.demo.R;
import com.demo.databinding.ActivityRewardsBinding;
import com.demo.faq.model.FAQRequestModel;
import com.demo.rewards.s.model.RewardsResponseModel;
import com.demo.utils.Constants;
import com.demo.utils.SharedPrefUtils;
import com.demo.webservice.ApiResponseListener;
import com.demo.webservice.RestClient;

import retrofit2.Call;

public class RewardsActivity extends BaseActivity implements ApiResponseListener {
    ActivityRewardsBinding activityRewardsBinding;
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        activityRewardsBinding =  DataBindingUtil.setContentView(this, R.layout.activity_rewards);
        activityRewardsBinding.headerLl.ivNotification.setVisibility(View.INVISIBLE);
        callRewardsApi();
    }

    private void callRewardsApi() {

         FAQRequestModel faqRequestModel = new FAQRequestModel();
         faqRequestModel.setUserID(new SharedPrefUtils(this).getStringData(Constants.USER_ID));
        Call objectCall = RestClient.getApiService().getRewardsOffer(faqRequestModel);
        RestClient.makeApiRequest(this, objectCall, this, 1, true);
    }

    @Override
    public void onClick(View view) {

    }

    @Override
    public void onApiResponse(Call<Object> call, Object response, int reqCode) throws Exception {
        RewardsResponseModel rewardsResponseModel = (RewardsResponseModel) response;
        if(rewardsResponseModel.getResponseCode().equalsIgnoreCase("200")){
            ScreenSlidePagerAdapter screenSlidePagerAdapter = new ScreenSlidePagerAdapter(getSupportFragmentManager(),rewardsResponseModel);
            activityRewardsBinding.pager.setAdapter(screenSlidePagerAdapter);
            activityRewardsBinding.tabLayout.setupWithViewPager(activityRewardsBinding.pager);
        }
    }

    @Override
    public void onApiError(Call<Object> call, Object response, int reqCode) throws Exception {

    }

    public  class ScreenSlidePagerAdapter extends FragmentStatePagerAdapter {

        private int count;
        private RewardsResponseModel rewardsResponseModel;


        public ScreenSlidePagerAdapter(FragmentManager fragmentManager, RewardsResponseModel rewardsResponseModel) {
            super(fragmentManager);
            this.count = rewardsResponseModel.getOfferrewardslist().size();
            this.rewardsResponseModel = rewardsResponseModel;
        }


        // Returns the fragment to display for that page
        @Override
        public Fragment getItem(int position) {
            return  ReviewSlidePageFragment.newInstance(rewardsResponseModel,position);

        }

        @Override
        public int getCount() {
            return count;
        }
    }



}
