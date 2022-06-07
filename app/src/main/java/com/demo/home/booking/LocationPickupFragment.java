package com.demo.home.booking;

import android.app.Dialog;
import android.content.Context;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.RadioGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.widget.LinearLayoutCompat;
import androidx.databinding.DataBindingUtil;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;

import com.demo.R;
import com.demo.databinding.DialogCustomerLocationBinding;
import com.demo.databinding.DialogCustomerLocationPickupBinding;
import com.demo.databinding.ItemDemoPlaceBinding;
import com.demo.databinding.LayoutDemoPlaceBinding;
import com.demo.home.HomeActivity;
import com.demo.home.booking.model.DirectionsGeocodeResponse;
import com.demo.home.model.MenuResponse;
import com.demo.home.model.viewmodel.MeetingPlaceViewModel;
import com.demo.utils.Constants;
import com.demo.utils.Utils;
import com.demo.webservice.ApiResponseListener;
import com.demo.webservice.RestClient;

import retrofit2.Call;


public class LocationPickupFragment extends Fragment implements ApiResponseListener {

    private static final int GET_GEOCODE = 1;
    private DialogCustomerLocationPickupBinding dialogCustomerLocationPickupBinding;
    private boolean isSchedule;


    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
         dialogCustomerLocationPickupBinding = DataBindingUtil.inflate(inflater, R.layout.dialog_customer_location_pickup, container, false);

        dialogCustomerLocationPickupBinding.backIcon.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ((HomeActivity)getActivity()).hideBottomSheet();

            }
        });
        return dialogCustomerLocationPickupBinding.getRoot();

    }


    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
    }


    private void callGeoCodeApi() {
        if(((HomeActivity) getActivity()).locationUtils.getLoc()!=null){
            Call objectCall = RestClient.getApiService().getGeocode(((HomeActivity) getActivity()).locationUtils.getLoc().getLatitude() + "," + ((HomeActivity) getActivity()).locationUtils.getLoc().getLongitude());
            RestClient.makeApiRequest(getActivity(), objectCall, this, GET_GEOCODE, true);
        }
        else{
            Call objectCall = RestClient.getApiService().getGeocode(Constants.LATITUDE + "," + Constants.LONGITUDE);
            RestClient.makeApiRequest(getActivity(), objectCall, this, GET_GEOCODE, true);

        }

    }


    @Override
    public void onApiResponse(Call<Object> call, Object response, int reqCode) throws Exception {
        if(reqCode==GET_GEOCODE){
            DirectionsGeocodeResponse directionsGeocodeResponse = (DirectionsGeocodeResponse) response;

            if ( Constants.BOOKING_PLACE_TYPE_ID.equalsIgnoreCase("2")) {
                dialogCustomerLocationPickupBinding.customerLocation.setText(directionsGeocodeResponse.getResults().get(0).getFormatted_address());
            }
            else{
                Constants.DEMOADDRESS =directionsGeocodeResponse.getResults().get(0).getFormatted_address();
                Constants.DEMOLOCATIONTYPE ="Home";
                if(!isSchedule)
                    ((HomeActivity) getActivity()).showFragment(new BookingStatusFragment(false));
                else
                    ((HomeActivity)getActivity()).showScheduleFragment(new ScheduleBookingFragment());

            }
        }
    }

    @Override
    public void onApiError(Call<Object> call, Object response, int reqCode) throws Exception {

    }
}
