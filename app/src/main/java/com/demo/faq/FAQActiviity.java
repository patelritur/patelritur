package com.demo.faq;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;

import androidx.annotation.Nullable;
import androidx.core.view.GravityCompat;
import androidx.databinding.DataBindingUtil;
import androidx.lifecycle.ViewModelProviders;

import com.demo.BaseActivity;
import com.demo.R;
import com.demo.databinding.ActivityFaqsBinding;
import com.demo.home.HomeActivity;
import com.demo.home.model.HomeModel;
import com.demo.home.model.viewmodel.AppContentViewModel;
import com.demo.home.model.viewmodel.AppContentViewModelFactory;
import com.demo.home.profile.MyDemoActivity;
import com.demo.notifications.NotificationActivity;
import com.demo.utils.Constants;
import com.demo.utils.Utils;

public class FAQActiviity extends BaseActivity {

    private ActivityFaqsBinding activityFaqsBinding;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        activityFaqsBinding =  DataBindingUtil.setContentView(this, R.layout.activity_faqs);

        setBottomMenuLabels(activityFaqsBinding.llBottom);
        setMenuLabels(activityFaqsBinding.leftMenu.menuRecyclerview);
        activityFaqsBinding.setHomeModel(homeModel);
        if(getIntent().getExtras()!=null){
            activityFaqsBinding.middleLayout.setVisibility(View.GONE);
            activityFaqsBinding.fragmentFaq.setVisibility(View.VISIBLE);
        }
        callSupportEmail();
    }
    private void callSupportEmail() {
        getViewModelStore().clear();
        AppContentViewModelFactory factory = new AppContentViewModelFactory(this.getApplication(), Constants.MYDEMO_MENU);
        AppContentViewModel appContentViewModel = ViewModelProviders.of(this, factory).get(AppContentViewModel.class);

        appContentViewModel.getSupportEmailLiveData().observe(this, item -> {
            activityFaqsBinding.emailSupport.setText(item.getLabels().get(0).getLabelInLanguage());
        });
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.faqs:
                activityFaqsBinding.middleLayout.setVisibility(View.GONE);
                activityFaqsBinding.fragmentFaq.setVisibility(View.VISIBLE);
                break;
            case R.id.ll_home:
                startActivity(new Intent(this, HomeActivity.class));
                finish();
                break;
            case R.id.ll_mydemos:
                startActivity(new Intent(this, MyDemoActivity.class));
                finish();
                break;
            case R.id.ll_takedemo:
                Utils.showToastComingSoon(this);
                break;
            case R.id.imageview_menu:
                activityFaqsBinding.drawerLy.openDrawer(GravityCompat.END);
                break;
            case R.id.iv_notification:
                startActivity(new Intent(this, NotificationActivity.class));
                break;
        }

    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();

    }
}
