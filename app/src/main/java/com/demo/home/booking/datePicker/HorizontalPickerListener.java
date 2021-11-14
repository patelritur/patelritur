package com.demo.home.booking.datePicker;

public interface HorizontalPickerListener {
    void onStopDraggingPicker();
    void onDraggingPicker();
    void onDateSelected(Day item);
}