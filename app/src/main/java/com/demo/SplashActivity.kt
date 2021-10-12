package com.demo

import android.content.Context
import android.content.Intent
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import com.demo.home.HomeActivity
import com.demo.tutorial.TutorialActivity
import com.demo.utils.Constants
import com.demo.utils.SharedPrefUtils


class SplashActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_splash)
        NavigateToNextActivity(this)
    }


    private fun NavigateToNextActivity(context: Context) {
        if ( SharedPrefUtils(this).getStringData(context, Constants.USER_ID)==null) {
            //User is not logged in start tutorial
            val intent = Intent(this, TutorialActivity::class.java)
            startActivity(intent)
            finish()

        } else {
            val intent = Intent(this, HomeActivity::class.java)
            startActivity(intent)
            finish()

        }

    }
}