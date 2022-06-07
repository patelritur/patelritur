package com.demo.home.booking;

import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.RadioButton;
import android.widget.RadioGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.databinding.DataBindingUtil;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProviders;

import com.demo.R;
import com.demo.databinding.FragmentCancelDemoBinding;
import com.demo.home.HomeActivity;
import com.demo.home.booking.model.BookingResponseModel;
import com.demo.home.booking.model.CancelBookingModel;
import com.demo.home.model.AppContentModel;
import com.demo.home.model.viewmodel.AppContentViewModel;
import com.demo.home.model.viewmodel.AppContentViewModelFactory;
import com.demo.utils.Constants;
import com.demo.utils.SharedPrefUtils;
import com.demo.utils.Utils;
import com.demo.webservice.ApiResponseListener;
import com.demo.webservice.RestClient;

import java.util.ArrayList;

import retrofit2.Call;

public class CancelDemoFragment extends Fragment implements ApiResponseListener {

   private FragmentCancelDemoBinding fragmentCancelDemoBinding;
    private CancelReasonAdapter cancelReasonAdapter=null;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        fragmentCancelDemoBinding = DataBindingUtil.inflate(inflater, R.layout.fragment_cancel_demo,container,false);
        callGetReasonApi();
        fragmentCancelDemoBinding.textviewCancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(cancelReasonAdapter.getSelectedReason()!=null)
                callCancelApi();
                else
                    Utils.showToast(getActivity(),"Please select one valid reason to cancel");
            }
        });

        return fragmentCancelDemoBinding.getRoot();

    }

    private void callCancelApi() {
        CancelBookingModel cancelBookingModel = new CancelBookingModel();
        cancelBookingModel.setCancelReason(cancelReasonAdapter.getSelectedReason());
        cancelBookingModel.setBlockDealerStatus(fragmentCancelDemoBinding.checkboxBlockDealer.isChecked()?"Y":"N");
        Call objectCall;
        if(Constants.BOOK_TYPE.equalsIgnoreCase("Demo")) {
            cancelBookingModel.bookingID = Constants.BOOKING_ID;
            objectCall = RestClient.getApiService().cancelBooking(cancelBookingModel);
        }
        else {
            cancelBookingModel.meetingID = Constants.MEETING_ID;
            objectCall = RestClient.getApiService().cancelMeeting(cancelBookingModel);
        }

        RestClient.makeApiRequest(getActivity(), objectCall, this, 1, true);


    }

    private void callGetReasonApi() {
        getViewModelStore().clear();
        AppContentViewModelFactory factory = new AppContentViewModelFactory(getActivity().getApplication(), Constants.CANCEL_REASON);
        AppContentViewModel appContentViewModel = ViewModelProviders.of(this, factory).get(AppContentViewModel.class);

        appContentViewModel.getCancelreasonLiveData().observe(requireActivity(), item -> {
            cancelReasonAdapter = new CancelReasonAdapter( (ArrayList<AppContentModel.Label>) item.getLabels(),this);
            fragmentCancelDemoBinding.recyclerview.setAdapter(cancelReasonAdapter);
            ((HomeActivity) getActivity()).setPeekheight(fragmentCancelDemoBinding.cancelParentLl.getMeasuredHeight());

        });
    }

    @Override
    public void onApiResponse(Call<Object> call, Object response, int reqCode) throws Exception {
        BookingResponseModel registrationResponse = (BookingResponseModel) response;
        if(registrationResponse.getResponseCode().equalsIgnoreCase("106")){
            new SharedPrefUtils(getActivity()).saveData(Constants.BOOKING_ONGOING,"null");
            Utils.showToast(getActivity(),registrationResponse.getDescriptions());
            getActivity().startActivity(new Intent(getActivity(), HomeActivity.class));
            getActivity().finish();
        }
        else if(registrationResponse.getResponseCode().equalsIgnoreCase("200")|| registrationResponse.getResponseCode().equalsIgnoreCase("201")){
            new SharedPrefUtils(getActivity()).saveData(Constants.BOOKING_ONGOING,"null");
            ((HomeActivity)getActivity()).showFragment(new BookingStatusFragment(true,registrationResponse));


        }
    }

    @Override
    public void onApiError(Call<Object> call, Object response, int reqCode) throws Exception {

    }

    public void checkforDelaerblock(String radioGroup) {
        if(radioGroup.equalsIgnoreCase("Not the preferred Demo Partner")){
            fragmentCancelDemoBinding.llCheckbox.setVisibility(View.VISIBLE);
        }
        else{
            fragmentCancelDemoBinding.llCheckbox.setVisibility(View.GONE);
        }
    }
}
