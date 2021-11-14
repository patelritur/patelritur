/*
package com.demo

import android.content.Context
import android.content.Intent
import android.content.res.Configuration
import android.os.Bundle
import android.view.View
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.ViewModelProviders
import com.demo.home.HomeMenuAdapter
import com.demo.home.model.AppContentModel
import com.demo.home.model.HomeModel
import com.demo.home.model.viewmodel.AppContentViewModel
import com.demo.home.model.viewmodel.AppContentViewModelFactory
import com.demo.utils.ClickHandlers
import com.demo.utils.Constants
import com.demo.utils.SharedPrefUtils
import com.demo.utils.Utils
import java.util.ArrayList

abstract class BaseActivity() : AppCompatActivity() , ClickHandlers{
     var homeModel: HomeModel? = null
     public var sharedPrefUtils: SharedPrefUtils? = null
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        sharedPrefUtils = SharedPrefUtils(this)
        homeModel = HomeModel(this)
        homeModel?.fName = sharedPrefUtils?.getStringData(Constants.FNAME)
        homeModel?.lName = sharedPrefUtils?.getStringData(Constants.LNAME)

        homeModel?.image = sharedPrefUtils?.getStringData(Constants.IMAGE)

        homeModel?.greetingMessage = Utils.getGreetingMessage(this)


        setBottomMenuLabels()
        setHomeMenuLabels()
        setMenuLabels()
    }

     open fun setHomeMenuLabels() : HomeModel? {
        viewModelStore.clear()
        val factory = AppContentViewModelFactory(this.application, Constants.HOME_MENU)
        val appContentViewModel = ViewModelProviders.of(this, factory).get(
            AppContentViewModel::class.java
        )
        appContentViewModel.homeMenuLiveData.observe(this,
            { item: AppContentModel ->
                homeModel!!.setHomeMenuFirstName(item.labels[0].labelInLanguage)
                homeModel!!.setHomeMenuFirstImage(item.labels[0].labelImage)
                homeModel!!.setHomeMenuSecondName(item.labels[1].labelInLanguage)
                homeModel!!.setHomeMenuSecondImage(item.labels[1].labelImage)
                homeModel
            })
         return null
    }


    open fun setMenuLabels() {
        viewModelStore.clear()
        val factory = AppContentViewModelFactory(this.application, Constants.LEFT_MENU)
        val appContentViewModel = ViewModelProviders.of(this, factory).get(
            AppContentViewModel::class.java
        )
        appContentViewModel.menuLiveData.observe(this, { item: AppContentModel ->
          */
/*  activityHomeBinding.leftMenu.setHomeModel(homeModel)
            activityHomeBinding.leftMenu.menuRecyclerview.setAdapter(
                HomeMenuAdapter(
                    this,
                    item.labels as ArrayList<AppContentModel.Label?>
                )
            )
            activityHomeBinding.executePendingBindings()*//*

        })
    }

     open fun setBottomMenuLabels() {
        val factory = AppContentViewModelFactory(this.application, Constants.MAIN_MENU)
        val appContentViewModel = ViewModelProviders.of(this, factory).get(
            AppContentViewModel::class.java
        )
        appContentViewModel.bottomMenuLiveData.observe(this,
            { item: AppContentModel ->
                homeModel!!.bottomMenuFirstName = item.labels[0].labelInLanguage
                homeModel!!.bottomMenuFirstImage = item.labels[0].labelImage
                homeModel!!.bottomMenuSecondName = item.labels[1].labelInLanguage
                homeModel!!.bottomMenuSecondImage = item.labels[1].labelImage
                homeModel!!.bottomMenuThirdName = item.labels[2].labelInLanguage
                homeModel!!.bottomMenuThirdImage = item.labels[2].labelImage
               // activityHomeBinding.llBottom.setHomeModel(homeModel)
              //  activityHomeBinding.executePendingBindings()
            })
    }
    fun NavigateToActivity( intent:Intent?) {
        startActivity(intent)
    }


    override fun attachBaseContext(newBase: Context?) {

        val newOverride = Configuration(newBase?.resources?.configuration)
        newOverride.fontScale = 1.0f
        applyOverrideConfiguration(newOverride)

        super.attachBaseContext(newBase)
    }
    // Observe the LiveData, passing in this activity as the LifecycleOwner and the observer.

    // Observe the LiveData, passing in this activity as the LifecycleOwner and the observer.
}*/
