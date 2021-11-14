package com.demo.home.booking.datePicker;


import org.joda.time.DateTime;

public interface DatePickerListener {
    void onDateSelected(DateTime dateSelected);
}