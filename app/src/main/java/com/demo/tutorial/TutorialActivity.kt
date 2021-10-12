package com.demo.tutorial

import android.content.Intent
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.databinding.DataBindingUtil
import androidx.lifecycle.ViewModelProviders
import com.demo.R
import com.demo.databinding.ActivityTutorialBinding
import com.demo.home.model.AppContentModel
import com.demo.home.model.viewmodel.AppContentViewModel
import com.demo.home.model.viewmodel.AppContentViewModelFactory
import com.demo.registrationLogin.LoginActivity
import com.demo.utils.Constants

class TutorialActivity : AppCompatActivity() {
    /*val images: IntArray = intArrayOf(R.mipmap.tutorial_one, R.mipmap.tutorial_two, R.mipmap.tutorial_three, R.mipmap.tutorial_four
        ,R.mipmap.tutorial_five, R.mipmap.tutorial_six)*/
    val images: ArrayList<String> = ArrayList()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        val activityTutorialBinding: ActivityTutorialBinding =  DataBindingUtil.setContentView(
            this, R.layout.activity_tutorial)
        val factory = AppContentViewModelFactory(this.application, Constants.APP_START)
        val appContentViewModel = ViewModelProviders.of(this, factory).get(
            AppContentViewModel::class.java
        )

        appContentViewModel.tutorialMenuLiveData.observe(this,
            { item: AppContentModel ->
                for (ii: AppContentModel.Label in item.Labels) {
                    // ...
                    images.add(ii.LabelImage)
                    val adapterViewPager = TutorialPagerAdapter(this, images)
                    activityTutorialBinding.vpPager.adapter = adapterViewPager
                }
            })

        activityTutorialBinding.tablayout.setupWithViewPager(activityTutorialBinding.vpPager);

        activityTutorialBinding.imageviewNext.setOnClickListener {
            if(activityTutorialBinding.vpPager.currentItem == images.size-1)
            {
                StartRegistrationLoginActivity()
            }
            else
                activityTutorialBinding.vpPager.setCurrentItem(activityTutorialBinding.vpPager.currentItem+1)
        }
        activityTutorialBinding.textviewSkip.setOnClickListener {
            StartRegistrationLoginActivity()
        }
    }

    private fun StartRegistrationLoginActivity() {
        val intent = Intent(this, LoginActivity::class.java)
        startActivity(intent)
        finish()
    }

}