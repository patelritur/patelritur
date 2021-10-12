package com.demo.registrationLogin;

import android.content.Context;
import android.view.View;
import android.widget.EditText;

import com.demo.utils.Utils;

class GenericFocusChangeListener
        implements View.OnFocusChangeListener
{
    private final Context context;

    GenericFocusChangeListener(View view, Context context) {
        this.context = context;
    }

    @Override
    public void onFocusChange(View v, boolean hasFocus) {
        if(hasFocus )
            Utils.setFocusedPinBg((EditText) v, context);
        else if(!(((EditText) v).getText().toString().length() ==1))
            Utils.setNormalPinBg((EditText) v, context);


    }
}