package com.demo.tutorial

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.RelativeLayout
import androidx.viewpager.widget.PagerAdapter
import com.demo.databinding.OnboardingScreenBinding
import java.util.*
import kotlin.collections.ArrayList

class TutorialPagerAdapter(// Context object
    var context: Context, // Array of images
    var images: ArrayList<String>
) : PagerAdapter() {


    // Layout Inflater
    var mLayoutInflater: LayoutInflater

    override fun getCount(): Int {
        // return the number of images
        return images.size
    }

    override fun instantiateItem(container: ViewGroup, position: Int): Any {

       /* mLayoutInflater = context.getSystemService(Context.LAYOUT_INFLATER_SERVICE) as LayoutInflater
        val itemView = mLayoutInflater.inflate(R.layout.onboarding_first_screen, container, false)

        Objects.requireNonNull(container).addView(itemView)
        return itemView*/

        mLayoutInflater = context.getSystemService(Context.LAYOUT_INFLATER_SERVICE) as LayoutInflater
        val onboardingFirstScreenBinding: OnboardingScreenBinding = OnboardingScreenBinding.inflate(mLayoutInflater, container, false)
//        onboardingFirstScreenBinding.imageViewMain.setImageResource(images.get(position))
        onboardingFirstScreenBinding.imageUrl = images.get(position)
        onboardingFirstScreenBinding.executePendingBindings()
        Objects.requireNonNull(container).addView(onboardingFirstScreenBinding.root)
        return onboardingFirstScreenBinding.root
    }

    override fun destroyItem(container: ViewGroup, position: Int, `object`: Any) {
        container.removeView(`object` as RelativeLayout)
    }

    override fun isViewFromObject(view: View, `object`: Any): Boolean {
        return view == `object`
    }

    override fun getItemPosition(`object`: Any): Int {
        return POSITION_NONE
    }

    // Viewpager Constructor
    init {
        mLayoutInflater = context.getSystemService(Context.LAYOUT_INFLATER_SERVICE) as LayoutInflater
    }


}