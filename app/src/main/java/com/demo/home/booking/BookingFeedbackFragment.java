package com.demo.home.booking;

import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.databinding.DataBindingUtil;
import androidx.fragment.app.Fragment;

import com.demo.R;
import com.demo.databinding.FragmentFeedbackBinding;
import com.demo.home.HomeActivity;
import com.demo.home.booking.model.AddToFavouriteModel;
import com.demo.home.booking.model.ReviewRequestModel;
import com.demo.registrationLogin.model.RegistrationResponse;
import com.demo.utils.Constants;
import com.demo.utils.SharedPrefUtils;
import com.demo.utils.Utils;
import com.demo.webservice.ApiResponseListener;
import com.demo.webservice.RestClient;

import retrofit2.Call;

public class BookingFeedbackFragment extends Fragment implements ApiResponseListener {

    private BookingFeedbackFragment context;
    private FragmentFeedbackBinding fragmentFeedbackBinding;
    private ReviewRequestModel reviewRequestModel = new ReviewRequestModel();
    private String specialistName,specialistId;

    public BookingFeedbackFragment(String specialistName, String specialistId) {
       this.specialistName = specialistName;
       this.specialistId = specialistId;

    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
       fragmentFeedbackBinding = DataBindingUtil.inflate(inflater, R.layout.fragment_feedback,container,false);
       context = this;
        return fragmentFeedbackBinding.getRoot();
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        if(Constants.BOOK_TYPE.equalsIgnoreCase("Demo")){
            reviewRequestModel.setBookingID(Constants.BOOKING_ID);
        }
        else
            reviewRequestModel.setMeetID(Constants.MEETING_ID);
        ((HomeActivity) getActivity()).setPeekheight(fragmentFeedbackBinding.parentLl.getMeasuredHeight());

        fragmentFeedbackBinding.setSpecialistName(specialistName);
        fragmentFeedbackBinding.setReviewRequest(reviewRequestModel);
        fragmentFeedbackBinding.submit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(reviewRequestModel.getReviewDesc()!=null && reviewRequestModel.getReviewDesc().trim().length()>0 && reviewRequestModel.getReviewRating()!=0) {
                    if(Constants.BOOK_TYPE.equalsIgnoreCase("Demo")) {
                        Call objectCall = RestClient.getApiService().insertDemoBookingReview(reviewRequestModel);
                        RestClient.makeApiRequest(getActivity(), objectCall, context, 1, true);
                    }
                    else{
                        Call objectCall = RestClient.getApiService().addMeetingBookingReview(reviewRequestModel);
                        RestClient.makeApiRequest(getActivity(), objectCall, context, 1, true);
                    }
                }
                else {
                    Utils.showToast(getActivity(),"Please provide both Ratings & Review");
                }

            }
        });
        fragmentFeedbackBinding.skip.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(fragmentFeedbackBinding.favouriteSpecialistcheckbox.isChecked()){
                    callAddToFavouriteSpecialistApi();
                }
                else
                    startHomeActivity();
            }
        });

    }

    private void startHomeActivity() {
        startActivity(new Intent(getActivity(), HomeActivity.class).addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK).addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP).addFlags(Intent.FLAG_ACTIVITY_NEW_TASK));
        getActivity().finish();
    }

    @Override
    public void onApiResponse(Call<Object> call, Object response, int reqCode) throws Exception {
        RegistrationResponse registrationResponse = (RegistrationResponse) response;
        if(reqCode==1) {
            if(fragmentFeedbackBinding.favouriteSpecialistcheckbox.isChecked()){
                callAddToFavouriteSpecialistApi();
            }
            else{
                if (registrationResponse.getResponseCode().equalsIgnoreCase("105")) {
                    Utils.showToast(getActivity(), registrationResponse.getDescriptions());
                    startHomeActivity();
                } else
                    Utils.showToast(getActivity(), registrationResponse.getDescriptions());
            }

        }
        else if(reqCode==2){
            if (registrationResponse.getResponseCode().equalsIgnoreCase("200")) {
                Utils.showToast(getActivity(), registrationResponse.getDescriptions());
            }
            startHomeActivity();
        }
    }

    private void callAddToFavouriteSpecialistApi() {
        AddToFavouriteModel reviewRequestModel = new AddToFavouriteModel();
        reviewRequestModel.setUserID(new SharedPrefUtils(getActivity()).getStringData(Constants.USER_ID));
        reviewRequestModel.setSpecialListID(specialistId);
        Call objectCall = RestClient.getApiService().addToFavouriteSpecialist(reviewRequestModel);
        RestClient.makeApiRequest(getActivity(), objectCall, context, 2, true);

    }

    @Override
    public void onApiError(Call<Object> call, Object response, int reqCode) throws Exception {

    }
}
