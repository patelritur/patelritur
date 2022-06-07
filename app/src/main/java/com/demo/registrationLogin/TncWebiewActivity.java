package com.demo.registrationLogin;

import android.os.Bundle;
import android.view.View;
import android.webkit.WebView;

import androidx.annotation.Nullable;

import com.demo.BaseActivity;
import com.demo.R;
import com.demo.utils.Constants;
import com.demo.utils.SharedPrefUtils;

public class TncWebiewActivity extends BaseActivity {

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_tnc);
        WebView webView = (WebView) findViewById(R.id.webview);
        webView.loadUrl(new SharedPrefUtils(this).getStringData(Constants.TNC));
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        finish();
    }

    @Override
    public void onClick(View view) {

    }
}
