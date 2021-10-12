package com.demo.utils.customviews

import android.content.Context
import android.util.AttributeSet
import android.util.Log
import android.view.View
import androidx.viewpager.widget.ViewPager
import com.demo.utils.PrintLog

class AdjustingViewPager: ViewPager {

    private  var currentViewHeight: Int = 0
    private lateinit var currentView: View  /* current page view in focus */
    private val maxHeight= 480f /* maximum height that we want on screen */
    private val allowedHeight = convertDpToPixel(maxHeight) /* allowed height in pixel */

    constructor(context: Context) : super(context)
    constructor(context: Context, attrs: AttributeSet?) : super(context, attrs)

    override fun onMeasure(widthMeasureSpec: Int, heightMeasureSpec: Int) {
        var newViewHeight = heightMeasureSpec
        val mode = MeasureSpec.getMode(heightMeasureSpec)
        /* until the parent has not assigned the size to the view
           and it is either not determined or at its maximum, assign it
           the new measurement as it comes in focus
         */
        if (mode == MeasureSpec.UNSPECIFIED || mode == MeasureSpec.AT_MOST) {
            super.onMeasure(widthMeasureSpec, heightMeasureSpec)
            try {
                /* measuring the current dimensions */
                currentView.measure(
                    widthMeasureSpec, MeasureSpec.makeMeasureSpec(
                        0,
                        MeasureSpec.UNSPECIFIED
                    )
                )
                 currentViewHeight = currentView.measuredHeight
                PrintLog.v("",""+currentViewHeight);
                /*
                   if the current view height is greater than allowed height then
                   set it to allowed height
                */
                if (currentViewHeight > allowedHeight) {
                    currentViewHeight = allowedHeight.toInt()
                }
                newViewHeight = MeasureSpec.makeMeasureSpec(currentViewHeight, MeasureSpec.EXACTLY)
            } catch (e: NullPointerException) {
                Log.e(AdjustingViewPager::class.simpleName, ": " + e.message)
            }
            catch(e : UninitializedPropertyAccessException)
            {
                Log.e(AdjustingViewPager::class.simpleName, ": " + e.message)

            }
        }
        super.onMeasure(widthMeasureSpec, newViewHeight)
    }

    fun measureCurrentView(view: View) {
        this.currentView = view
        requestLayout()
    }

    private fun convertDpToPixel(dp: Float): Float {
        val metrics = context.resources.displayMetrics
        return dp * (metrics.densityDpi / 160f)
    }

     fun getPHeight() : Int{
        return currentViewHeight
    }
}