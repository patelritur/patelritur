package com.demo.registrationLogin;

import android.app.Activity;
import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.Window;

import androidx.annotation.NonNull;
import androidx.databinding.DataBindingUtil;

import com.demo.R;
import com.demo.databinding.DialogUploadCertificateBinding;

public class UploadDocDialog extends Dialog  {
    DialogUploadCertificateBinding dialogUploadCertificateBinding;
    Context context;
    public UploadDocDialog(@NonNull Context context) {

        super(context,R.style.NewDialog);
        this.context = context;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        dialogUploadCertificateBinding = DataBindingUtil.inflate(LayoutInflater.from(getContext()), R.layout. dialog_upload_certificate, null, false);
        setContentView(dialogUploadCertificateBinding.getRoot());
        dialogUploadCertificateBinding.setHandler(this);


    }

    public void onUploadDocClick(View view)
    {

        Intent intent = new Intent();
        intent.setAction(Intent.ACTION_GET_CONTENT);

        intent.setType("application/pdf");
        ((Activity)context).startActivityForResult(intent, 1);


    }

    public void onSkipClick(View view)
    {
        this.dismiss();
    }




}
