package com.demo.home.meeting;

import android.annotation.SuppressLint;
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
import com.demo.home.booking.BookingStatusFragment;
import com.demo.home.booking.ScheduleBookingFragment;
import com.demo.home.booking.ScheduleLaterFragment;
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

public class MeetingPlaceFragment extends Fragment implements ApiResponseListener {
    private static final int GET_GEOCODE = 1;
    private LayoutDemoPlaceBinding layoutDemoPlaceBinding;
    private String bookDate;
    private boolean preBook;
    private DialogCustomerLocationPickupBinding binding;
    private boolean isCurrentLocation;
    private boolean isSchedule;
    private String currentLocation;

    public MeetingPlaceFragment(String bookdate) {
        this.bookDate = bookdate;

        if (Utils.DateAfter(bookDate)) {
            preBook = true;
        }

    }

    public MeetingPlaceFragment() {

    }

    @SuppressLint("SetTextI18n")
    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        layoutDemoPlaceBinding = DataBindingUtil.inflate(inflater,R.layout.layout_demo_place,container,false);
        layoutDemoPlaceBinding.titleText.setText(getString(R.string.where_do_you_want_meeting));
        layoutDemoPlaceBinding.backIcon.setOnClickListener(v -> ((HomeActivity)getActivity()).hideBottomSheet());
        return layoutDemoPlaceBinding.getRoot();

    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        getViewModelStore().clear();
        new ViewModelProvider(requireActivity()).get(MeetingPlaceViewModel.class).getMenuType().observe(getViewLifecycleOwner(), this::updateUIForPlace);
    }

    private void updateUIForPlace(MenuResponse item) {
        for(int i=0;i<item.getDemomenu().size();i++)
        {
            LayoutInflater inflater = (LayoutInflater)getActivity().getSystemService(Context.LAYOUT_INFLATER_SERVICE);

            ItemDemoPlaceBinding itemDemoPlaceBinding = DataBindingUtil.inflate(inflater,R.layout.item_demo_place,null,false);
            itemDemoPlaceBinding.setMenuModel(item.getDemomenu().get(i));
            int finalI = i;

            itemDemoPlaceBinding.llBookDemo.setOnClickListener(v -> {

                Constants.MEETING_PLACE_TYPE_ID = item.getDemomenu().get(finalI).getMenuID();

                if(finalI ==2)
                {
                    if(preBook) {
                        Constants.MEETING_TYPE_ID = "Schedule";
                    }

                    ((HomeActivity)getActivity()).showScheduleFragment(new VirtualMeetFragment(bookDate));


                }
                else {
                   /* if (preBook) {
                        Constants.MEETING_TYPE_ID = "Schedule";
                        ((HomeActivity) getActivity()).showFragment(new ScheduleLaterFragment(bookDate));
                    } else */{
                        if (item.getDemomenu().get(finalI).getMenuID().equalsIgnoreCase("2")) {
                            showLocationPickerDialog(false);
                        } else {
                            callGeoCodeApi();
                        }
                        Constants.MEETING_TYPE_ID = "Now";
                        Constants.TIME = "";
                        Constants.DATE = "";
                      //  ((HomeActivity) getActivity()).showFragment(new BookingStatusFragment(false));
                    }
                }
            });
            itemDemoPlaceBinding.llSchedule.setOnClickListener(v -> {
                Constants.MEETING_PLACE_TYPE_ID = item.getDemomenu().get(finalI).getMenuID();
               /* if(preBook)
                {
                    Constants.MEETING_TYPE_ID = "Schedule";
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
//                    ((HomeActivity)getActivity()).showScheduleFragment(new ScheduleBookingFragment());


            });
            layoutDemoPlaceBinding.llPlacetype.setOrientation(LinearLayoutCompat.VERTICAL);
            layoutDemoPlaceBinding.llPlacetype.addView(itemDemoPlaceBinding.getRoot());
        }
    }

    private void showLocationPickerDialog(boolean isSchedule) {
        BottomSheetDialog dialog = new BottomSheetDialog(getActivity());
         binding = DataBindingUtil.inflate(LayoutInflater.from(getActivity()), R.layout. dialog_customer_location_pickup, null, false);
        dialog.setContentView(binding.getRoot());
        callGeoCodeApi();
        isCurrentLocation=true;
        binding.pickCurrentLocation.setBackgroundResource(R.drawable.border_red_rounded_corner);
        binding.enterLocation.setBackgroundResource(R.drawable.white_border);
        binding.backIcon.setOnClickListener((View v) -> dialog.dismiss());

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
                    ((HomeActivity) getActivity()).showFragment(new BookingStatusFragment(false));
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
        Call objectCall;
        if(((HomeActivity) Objects.requireNonNull(getActivity())).locationUtils.getLoc()!=null){
             objectCall = RestClient.getApiService().getGeocode(((HomeActivity) getActivity()).locationUtils.getLoc().getLatitude() + "," + ((HomeActivity) getActivity()).locationUtils.getLoc().getLongitude());
        }
        else{
             objectCall = RestClient.getApiService().getGeocode(Constants.LATITUDE + "," + Constants.LONGITUDE);
        }
        RestClient.makeApiRequest(getActivity(), objectCall, this, GET_GEOCODE, true);


    }


    @Override
    public void onApiResponse(Call<Object> call, Object response, int reqCode) throws Exception {
        if(reqCode==GET_GEOCODE){
            DirectionsGeocodeResponse directionsGeocodeResponse = (DirectionsGeocodeResponse) response;

            if ( Constants.MEETING_PLACE_TYPE_ID.equalsIgnoreCase("2")) {
                currentLocation = directionsGeocodeResponse.getResults().get(0).getFormatted_address();
                binding.customerLocation.setText(directionsGeocodeResponse.getResults().get(0).getFormatted_address());
            }
            else{
                Constants.DEMOADDRESS =directionsGeocodeResponse.getResults().get(0).getFormatted_address();
                Constants.DEMOLOCATIONTYPE ="Home";
                if(preBook)
                {
                    Constants.MEETING_TYPE_ID = "Schedule";
                    ((HomeActivity)getActivity()).showFragment(new ScheduleLaterFragment(bookDate));
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
