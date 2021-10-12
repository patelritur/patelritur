package com.demo

import android.content.Intent
import android.os.Bundle
import android.view.View
import androidx.appcompat.app.AppCompatActivity
import com.demo.utils.ClickHandlers

abstract class BaseActivity : AppCompatActivity() , ClickHandlers{
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
    }

    fun NavigateToActivity( intent:Intent?) {
        startActivity(intent)
    }




    // Observe the LiveData, passing in this activity as the LifecycleOwner and the observer.

    // Observe the LiveData, passing in this activity as the LifecycleOwner and the observer.
}