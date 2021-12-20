package com.demo.home.booking;

import android.content.Context;
import android.os.Bundle;
import android.text.Html;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.databinding.DataBindingUtil;
import androidx.fragment.app.Fragment;

import com.demo.R;
import com.demo.carDetails.model.CarDetailRequest;
import com.demo.databinding.FragmentScheduleDateBinding;
import com.demo.databinding.ItemPersonalisedCarOptionsBinding;
import com.demo.home.HomeActivity;
import com.demo.home.booking.datePicker.Time;
import com.demo.home.booking.model.LaterOptionModel;
import com.demo.utils.Constants;
import com.demo.utils.PrintLog;
import com.demo.utils.Utils;
import com.demo.webservice.ApiResponseListener;
import com.demo.webservice.RestClient;

import org.joda.time.DateTime;

import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;

import retrofit2.Call;

public class ScheduleLaterFragment extends Fragment implements ApiResponseListener {
    FragmentScheduleDateBinding fragmentScheduleDateBinding;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        fragmentScheduleDateBinding = DataBindingUtil.inflate(inflater,R.layout.fragment_schedule_date,container,false);

        callLaterOptionApi();
        fragmentScheduleDateBinding.textviewSchedule.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm a");
                try {
                    Date date =  simpleDateFormat.parse(Constants.DATE+" "+Constants.TIME);
                    if(date.compareTo( Calendar.getInstance().getTime())>0){
                        ((HomeActivity) getActivity()).showFragment(new BookingStatusFragment(false));
                    }
                    else
                    {
                        Utils.showToast(getActivity(),"Please select valid date");
                    }
                } catch (ParseException e) {
                    e.printStackTrace();
                }
            }
        });
        return fragmentScheduleDateBinding.getRoot();
    }



    private void callLaterOptionApi() {
        CarDetailRequest carDetailRequest = new CarDetailRequest();
        carDetailRequest.setUserID(((HomeActivity)getActivity()).userId);
        Call objectCall;
        if(Constants.BOOK_TYPE.equalsIgnoreCase("Demo"))
            objectCall = RestClient.getApiService().getLaterTimeOptions(carDetailRequest);
        else
            objectCall = RestClient.getApiService().getMeetingLaterTimeOptions(carDetailRequest);
        RestClient.makeApiRequest(getActivity(), objectCall, this, 1, true);

    }

    private void getDate() {

        DateFormat dateFormat = new SimpleDateFormat("dd MMMM");
        DateFormat dateFormat1 = new SimpleDateFormat("YYYY");
        Date date = new Date();
        String text = "<font color=#393939>Today, </font>";
        String text1 = "<font color=#cc0029>"+dateFormat.format(date)+"</font>";
        String text2 = "<font color=#393939>"+" "+dateFormat1.format(date)+"</font>";
        fragmentScheduleDateBinding.todayDate.setText(Html.fromHtml(text+text1+text2));
        // fragmentScheduleDateBinding.todayDate.append( "Today, "+getColoredString(dateFormat.format(date), getContext().getColor(R.color.color_E8505B))+" " +dateFormat1.format(date));

    }



    @Override
    public void onApiResponse(Call<Object> call, Object response, int reqCode) throws Exception {
        LaterOptionModel laterOptionModel = (LaterOptionModel)response;
        getDate();
        fragmentScheduleDateBinding.horizontalDatepicker.init(Integer.parseInt(laterOptionModel.getLaterbookingoption().getNoDaysForBooking()));
        fragmentScheduleDateBinding.horizontalDatepicker.setDate(new DateTime());
        setTimeSlot(laterOptionModel);
    }

    private void setTimeSlot(LaterOptionModel laterOptionModel) {
        ArrayList<String> timeSlotList = new Time().getNextTime(laterOptionModel.getLaterbookingoption().getBookingStartTime(),laterOptionModel.getLaterbookingoption().getBookingEndTime());
        PrintLog.v("",""+timeSlotList.toString());
        final int[] previousI = {-1};
        for( int i = 0; i<timeSlotList.size()-1; i++)
        {
            LayoutInflater inflater = (LayoutInflater)getActivity().getSystemService(Context.LAYOUT_INFLATER_SERVICE);

            ItemPersonalisedCarOptionsBinding itemPersonalisedCarOptionsBinding = DataBindingUtil.inflate(inflater,R.layout.item_personalised_car_options,null,false);
            itemPersonalisedCarOptionsBinding.imageviewFilterIcon.setVisibility(View.GONE);
            itemPersonalisedCarOptionsBinding.name.setText(timeSlotList.get(i)+"-"+timeSlotList.get(i+1));
            int finalI = i;

            itemPersonalisedCarOptionsBinding.llItemPersonalisedCar.setOnClickListener(new View.OnClickListener() {


                @Override
                public void onClick(View v) {
                    Constants.TIME = timeSlotList.get(finalI);
                    selectItemBackground(true,finalI);
                    selectItemBackground(false, previousI[0]);
                    previousI[0] = finalI;
                    itemPersonalisedCarOptionsBinding.llItemPersonalisedCar.setBackgroundResource(R.drawable.border_red_rounded_corner);

                }

            });
            Constants.TIME = timeSlotList.get(0);

            fragmentScheduleDateBinding.llTimeSlot.addView(itemPersonalisedCarOptionsBinding.getRoot());
            selectItemBackground(true,0);
            previousI[0] = 0;
        }


    }

    private void selectItemBackground(boolean b, int i) {

        if(b)

            fragmentScheduleDateBinding.llTimeSlot.getChildAt(i).findViewById(R.id.ll_item_personalised_car).setBackgroundResource(R.drawable.border_red_rounded_corner);
        else
            fragmentScheduleDateBinding.llTimeSlot.getChildAt(i).findViewById(R.id.ll_item_personalised_car).setBackgroundResource(R.drawable.white_border);

    }

    @Override
    public void onApiError(Call<Object> call, Object response, int reqCode) throws Exception {

    }



}
