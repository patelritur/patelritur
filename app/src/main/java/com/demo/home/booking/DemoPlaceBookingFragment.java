package com.demo.home.booking;

import android.content.Context;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.widget.LinearLayoutCompat;
import androidx.databinding.DataBindingUtil;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;

import com.demo.R;
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
import com.google.android.material.bottomsheet.BottomSheetDialog;

import java.util.Objects;

import retrofit2.Call;


public class DemoPlaceBookingFragment extends Fragment implements ApiResponseListener {

    private static final int GET_GEOCODE = 1;
    private  boolean preBook;
    private LayoutDemoPlaceBinding layoutDemoPlaceBinding;
    private String bookDate;
    private DialogCustomerLocationPickupBinding binding;
    private boolean isSchedule;
    private String currentLocation;
    private boolean isCurrentLocation;

    public DemoPlaceBookingFragment(String bookdate) {
        this.bookDate = bookdate;
        if (Utils.DateAfter(bookDate)) {
            preBook = true;
        }

    }

    public DemoPlaceBookingFragment() {

    }
    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        layoutDemoPlaceBinding = DataBindingUtil.inflate(inflater, R.layout.layout_demo_place, container, false);
        layoutDemoPlaceBinding.titleText.setText(getString(R.string.where_do_yo_want_the_demo));



        layoutDemoPlaceBinding.backIcon.setOnClickListener(v -> ((HomeActivity)getActivity()).hideBottomSheet());
        return layoutDemoPlaceBinding.getRoot();

    }


    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        getViewModelStore().clear();
        new ViewModelProvider(requireActivity()).get(MeetingPlaceViewModel.class).getBookingType().observe(getViewLifecycleOwner(), this::updateUIForPlace);
    }

    private void updateUIForPlace(MenuResponse item) {

        for (int i = 0; i < item.getDemomenu().size(); i++) {
            LayoutInflater inflater = (LayoutInflater) getActivity().getSystemService(Context.LAYOUT_INFLATER_SERVICE);

            ItemDemoPlaceBinding itemDemoPlaceBinding = DataBindingUtil.inflate(inflater, R.layout.item_demo_place, null, false);
            itemDemoPlaceBinding.setMenuModel(item.getDemomenu().get(i));
            int finalI = i;
            itemDemoPlaceBinding.llBookDemo.setOnClickListener(v -> {
                if (item.getDemomenu().get(finalI).getMenuID().equalsIgnoreCase("3")) {
                    Utils.showToast(getActivity(), "Coming Soon");
                    return;
                }

                Constants.BOOKING_PLACE_TYPE_ID = item.getDemomenu().get(finalI).getMenuID();

              /*  if (preBook) {
                    Constants.BOOKING_TYPE_ID = "Schedule";
                    ((HomeActivity) getActivity()).showFragment(new ScheduleLaterFragment(bookDate));
                    return;
                }*/
                if (item.getDemomenu().get(finalI).getMenuID().equalsIgnoreCase("2")) {
                    showLocationPickerDialog(false);
                    //  showLocationDialog();
                } else {
                    callGeoCodeApi();
                }
                Constants.BOOKING_TYPE_ID = "Now";
                Constants.TIME = "";
                Constants.DATE = "";
            });
            itemDemoPlaceBinding.llSchedule.setOnClickListener(v -> {

                Constants.BOOKING_PLACE_TYPE_ID = item.getDemomenu().get(finalI).getMenuID();
               /* if(preBook)
                {
                    Constants.BOOKING_TYPE_ID = "Schedule";
                    ((HomeActivity)getActivity()).showFragment(new ScheduleLaterFragment(bookDate));
                    return;
                }*/
                if (item.getDemomenu().get(finalI).getMenuID().equalsIgnoreCase("2")) {
                    showLocationPickerDialog(true);
                    //  showLocationDialog();
                } else {
                    isSchedule=true;
                    callGeoCodeApi();
                }
            });
            layoutDemoPlaceBinding.llPlacetype.setOrientation(LinearLayoutCompat.VERTICAL);
            layoutDemoPlaceBinding.llPlacetype.addView(itemDemoPlaceBinding.getRoot());
        }

    }

    private void showLocationPickerDialog(Boolean isSchedule) {
        BottomSheetDialog dialog = new BottomSheetDialog(getActivity());
         binding = DataBindingUtil.inflate(LayoutInflater.from(getActivity()), R.layout. dialog_customer_location_pickup, null, false);
        dialog.setContentView(binding.getRoot());
        callGeoCodeApi();
        isCurrentLocation=true;
        binding.pickCurrentLocation.setBackgroundResource(R.drawable.border_red_rounded_corner);
        binding.enterLocation.setBackgroundResource(R.drawable.white_border);
        binding.backIcon.setOnClickListener((View v) -> {
            // do something here
            dialog.dismiss();
        });

        binding.pickCurrentLocation.setOnClickListener((View v)->{
            binding.pickCurrentLocation.setBackgroundResource(R.drawable.border_red_rounded_corner);
            binding.enterLocation.setBackgroundResource(R.drawable.white_border);
            binding.customerLocation.setText(currentLocation);
            isCurrentLocation = true;

        });

        binding.enterLocation.setOnClickListener((View v)->{
            binding.enterLocation.setBackgroundResource(R.drawable.border_red_rounded_corner);
            binding.pickCurrentLocation.setBackgroundResource(R.drawable.white_border);
            binding.customerLocation.setText(null);
            isCurrentLocation = false;
        });


        binding.submit.setOnClickListener((View v) -> {
            if(isCurrentLocation){
                Constants.DEMOADDRESS = Objects.requireNonNull(binding.customerLocation.getText()).toString();
                {
                    Constants.DEMOLOCATIONTYPE = "Home";
                    dialog.dismiss();
                    if(!isSchedule)
                        ((HomeActivity) getActivity()).showFragment(new BookingStatusFragment(false));
                    else
                        ((HomeActivity)getActivity()).showScheduleFragment(new ScheduleBookingFragment());
                }
                dialog.dismiss();
            }
            else {
                if(Objects.requireNonNull(binding.customerLocation.getText()).toString().trim().length()>0) {
                    Constants.DEMOADDRESS = binding.customerLocation.getText().toString();
                    Constants.DEMOLOCATIONTYPE = "Home";
                    if(!isSchedule)
                        ((HomeActivity) getActivity()).showFragment(new BookingStatusFragment(false));
                    else
                        ((HomeActivity)getActivity()).showScheduleFragment(new ScheduleBookingFragment());
                    dialog.dismiss();
                }
                else {
                    Utils.showToast(getActivity(),"Please enter address");
                }
            }

        });

        dialog.getWindow().setLayout(WindowManager.LayoutParams.MATCH_PARENT, WindowManager.LayoutParams.WRAP_CONTENT);
        dialog.setCancelable(false);
        WindowManager.LayoutParams lp = dialog.getWindow().getAttributes();
        dialog.getWindow().setBackgroundDrawableResource(android.R.color.transparent);
        lp.dimAmount = 0.8f;
        dialog.getWindow().addFlags(WindowManager.LayoutParams.FLAG_DIM_BEHIND);
        dialog.show();
    }



    private void callGeoCodeApi() {
        if(((HomeActivity) Objects.requireNonNull(getActivity())).locationUtils.getLoc()!=null){
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
                currentLocation = directionsGeocodeResponse.getResults().get(0).getFormatted_address();
                binding.customerLocation.setText(directionsGeocodeResponse.getResults().get(0).getFormatted_address());
            }
            else{
                Constants.DEMOADDRESS =directionsGeocodeResponse.getResults().get(0).getFormatted_address();
                Constants.DEMOLOCATIONTYPE ="Home";
                if (preBook) {
                    Constants.BOOKING_TYPE_ID = "Schedule";
                    ((HomeActivity) getActivity()).showFragment(new ScheduleLaterFragment(bookDate));
                    return;
                }
                if(!isSchedule)
                    ((HomeActivity) Objects.requireNonNull(getActivity())).showFragment(new BookingStatusFragment(false));
                else
                    ((HomeActivity) Objects.requireNonNull(getActivity())).showScheduleFragment(new ScheduleBookingFragment());

            }
        }
    }

    @Override
    public void onApiError(Call<Object> call, Object response, int reqCode) throws Exception {

    }
}
