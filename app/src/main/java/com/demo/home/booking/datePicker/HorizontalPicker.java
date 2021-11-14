package com.demo.home.booking.datePicker;

import android.content.Context;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.graphics.drawable.Drawable;
import android.util.AttributeSet;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.ColorInt;
import androidx.annotation.Nullable;

import com.demo.R;
import com.demo.utils.Constants;
import com.demo.utils.PrintLog;

import org.joda.time.DateTime;

import java.text.SimpleDateFormat;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.Date;
import java.util.Locale;

public class HorizontalPicker extends LinearLayout implements HorizontalPickerListener {

    private static final int NO_SETTED = -1;
    private TextView tvToday;
    private DatePickerListener listener;
    private OnTouchListener monthListener;
    private HorizontalPickerRecyclerView rvDays;
    private int days;
    private int offset;
    private int mDateSelectedColor = -1;
    private int mDateSelectedTextColor = -1;
    private int mMonthAndYearTextColor = -1;
    private int mTodayButtonTextColor = -1;
    private boolean showTodayButton = true;
    private String mMonthPattern = "";
    private int mTodayDateTextColor = -1;
    private int mTodayDateBackgroundColor = -1;
    private int mDayOfWeekTextColor = -1;
    private int mUnselectedDayTextColor = -1;

    public HorizontalPicker(Context context) {
        super(context);
        internInit();
    }

    public HorizontalPicker(Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
        internInit();

    }

    public HorizontalPicker(Context context, @Nullable AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
        internInit();
    }

    private void internInit() {
        this.days = NO_SETTED;
        this.offset = NO_SETTED;
    }

    public HorizontalPicker setListener(DatePickerListener listener) {
        this.listener = listener;
        return this;
    }

    public HorizontalPicker setMonthListener(OnTouchListener listener) {
        this.monthListener = listener;
        return this;
    }

    public void setDate(final DateTime date) {
        rvDays.post(new Runnable() {
            @Override
            public void run() {
                rvDays.setDate(date);
            }
        });
    }

    public void init(int daystoplus) {
        inflate(getContext(), R.layout.horizontal_picker, this);
        rvDays = (HorizontalPickerRecyclerView) findViewById(R.id.rvDays);
        int DEFAULT_DAYS_TO_PLUS = daystoplus;
        int finalDays = days == NO_SETTED ? DEFAULT_DAYS_TO_PLUS : days;
        int DEFAULT_INITIAL_OFFSET = 0;
        int finalOffset = offset == NO_SETTED ? DEFAULT_INITIAL_OFFSET : offset;

        /*tvMonth = (TextView) findViewById(R.id.tvMonth);
        if (monthListener != null) {
            tvMonth.setClickable(true);
            tvMonth.setOnTouchListener(monthListener);
        }*/


        tvToday = (TextView) findViewById(R.id.tvToday);
        rvDays.setListener(this);
        tvToday.setOnClickListener(rvDays);
       // tvMonth.setTextColor(mMonthAndYearTextColor != -1 ? mMonthAndYearTextColor : getColor(R.color.primaryTextColor));
        int mBackgroundColor = getBackgroundColor();
        //setBackgroundColor(mBackgroundColor != Color.TRANSPARENT ? mBackgroundColor : Color.WHITE);
        //mDateSelectedColor = mDateSelectedColor == -1 ? getColor(R.color.color_E8505B) : mDateSelectedColor;
        //mDateSelectedTextColor = mDateSelectedTextColor == -1 ? getColor(R.color.color_3d3d3d) : mDateSelectedTextColor;
        //mTodayDateTextColor = mTodayDateTextColor == -1 ? getColor(R.color.black) : mTodayDateTextColor;
        //mDayOfWeekTextColor = mDayOfWeekTextColor == -1 ? getColor(R.color.color_3d3d3d) : mDayOfWeekTextColor;
        //mUnselectedDayTextColor = mUnselectedDayTextColor == -1 ? getColor(R.color.color_3d3d3d) : mUnselectedDayTextColor;
        rvDays.init(
                getContext(),
                finalDays,
                finalOffset,
                mBackgroundColor);
    }


    public int getBackgroundColor() {
        int color = Color.TRANSPARENT;
        return color;
    }

    @Override
    public boolean post(Runnable action) {
        return rvDays.post(action);
    }

    @Override
    public void onStopDraggingPicker() {
    }

    @Override
    public void onDraggingPicker() {
    }

    @Override
    public void onDateSelected(Day item) {
      //  tvMonth.setText(item.getMonth(mMonthPattern));
        if (showTodayButton)
            tvToday.setVisibility(item.isToday() ? INVISIBLE : VISIBLE);
        if (listener != null) {
            listener.onDateSelected(item.getDate());
        }

        Constants.DATE = item.getDate().toString().substring(0,10);
    }

    public HorizontalPicker setDays(int days) {
        this.days = days;
        return this;
    }

    public int getDays() {
        return days;
    }

    public HorizontalPicker setOffset(int offset) {
        this.offset = offset;
        return this;
    }

    public int getOffset() {
        return offset;
    }

   /* public HorizontalPicker setDateSelectedColor(@ColorInt int color) {
        mDateSelectedColor = color;
        return this;
    }

    public HorizontalPicker setDateSelectedTextColor(@ColorInt int color) {
        mDateSelectedTextColor = color;
        return this;
    }

    public HorizontalPicker setMonthAndYearTextColor(@ColorInt int color) {
        mMonthAndYearTextColor = color;
        return this;
    }

    public HorizontalPicker setTodayButtonTextColor(@ColorInt int color) {
        mTodayButtonTextColor = color;
        return this;
    }

    public HorizontalPicker showTodayButton(boolean show) {
        showTodayButton = show;
        return this;
    }

    public HorizontalPicker setTodayDateTextColor(int color) {
        mTodayDateTextColor = color;
        return this;
    }

    public HorizontalPicker setTodayDateBackgroundColor(@ColorInt int color) {
        mTodayDateBackgroundColor = color;
        return this;
    }

    public HorizontalPicker setDayOfWeekTextColor(@ColorInt int color) {
        mDayOfWeekTextColor = color;
        return this;
    }

    public HorizontalPicker setUnselectedDayTextColor(@ColorInt int color) {
        mUnselectedDayTextColor = color;
        return this;
    }*/

    public HorizontalPicker setMonthPattern(String pattern) {
        mMonthPattern = pattern;
        return this;
    }
}