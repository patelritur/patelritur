package com.demo

import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.os.Handler
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.app.AppCompatDelegate
import com.demo.home.HomeActivity
import com.demo.tutorial.TutorialActivity
import com.demo.utils.Constants
import com.demo.utils.PrintLog
import com.demo.utils.SharedPrefUtils
import com.demo.utils.Utils
import com.skyfishjy.library.RippleBackground


class SplashActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_NO);
        setContentView(R.layout.activity_splash)
        findViewById<RippleBackground>(R.id.content).startRippleAnimation()

        val mHandler = Handler()
        mHandler.postDelayed(Runnable {

            NavigateToNextActivity(this)


        }, 3000L)
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