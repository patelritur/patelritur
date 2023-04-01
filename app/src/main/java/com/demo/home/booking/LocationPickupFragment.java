package com.demo.home.booking;

import static android.app.Activity.RESULT_OK;

import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.RadioGroup;
import android.widget.Toast;

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
import com.google.android.libraries.places.api.model.Place;
import com.rtchagas.pingplacepicker.PingPlacePicker;

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
        dialogCustomerLocationPickupBinding.customerLocation.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                showPlacePicker();
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

    private void showPlacePicker() {
        PingPlacePicker.IntentBuilder builder = new PingPlacePicker.IntentBuilder();
        builder.setAndroidApiKey("AIzaSyCUFs0ot4kLDWcYm6ZdoR9Ydgthtk-5Imw")
                .setMapsApiKey("AIzaSyCUFs0ot4kLDWcYm6ZdoR9Ydgthtk-5Imw");

        // If you want to set a initial location rather then the current device location.
        // NOTE: enable_nearby_search MUST be true.
        // builder.setLatLng(new LatLng(37.4219999, -122.0862462))

        try {
            Intent placeIntent = builder.build(getActivity());
            startActivityForResult(placeIntent, 1);
        }
        catch (Exception ex) {
            // Google Play services is not available...
        }
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        if ((requestCode == 1) && (resultCode == RESULT_OK)) {
            Place place = PingPlacePicker.getPlace(data);
            if (place != null) {
                Toast.makeText(getActivity(), "You selected the place: " + place.getName(), Toast.LENGTH_SHORT).show();
            }
        }
    }

}
