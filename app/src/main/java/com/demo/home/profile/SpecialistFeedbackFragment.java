package com.demo.home.profile;

import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.ViewTreeObserver;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.databinding.DataBindingUtil;
import androidx.fragment.app.Fragment;

import com.demo.R;
import com.demo.databinding.FragmentFeedbackBinding;
import com.demo.home.HomeActivity;
import com.demo.home.booking.model.ReviewRequestModel;
import com.demo.registrationLogin.model.RegistrationResponse;
import com.demo.utils.Constants;
import com.demo.utils.SharedPrefUtils;
import com.demo.utils.Utils;
import com.demo.webservice.ApiResponseListener;
import com.demo.webservice.RestClient;

import retrofit2.Call;

public class SpecialistFeedbackFragment extends Fragment implements ApiResponseListener {

    private SpecialistFeedbackFragment context;
    private FragmentFeedbackBinding fragmentFeedbackBinding;
    private ReviewRequestModel reviewRequestModel = new ReviewRequestModel();
    private String specialistName,specialistId;

    public SpecialistFeedbackFragment(String specialistName,String specialistId) {
       this.specialistName = specialistName;
       this.specialistId  = specialistId;

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


        fragmentFeedbackBinding.setSpecialistName(specialistName);
        fragmentFeedbackBinding.setReviewRequest(reviewRequestModel);
        reviewRequestModel.setUserID(new SharedPrefUtils(getActivity()).getStringData(Constants.USER_ID));
        reviewRequestModel.setSpecialListID(specialistId);
        fragmentFeedbackBinding.parentLl.getViewTreeObserver().addOnGlobalLayoutListener(new ViewTreeObserver.OnGlobalLayoutListener() {
            @Override
            public void onGlobalLayout() {
                if( ((FavouriteSpecialistFragment)getParentFragment())!=null)
                ((FavouriteSpecialistFragment)getParentFragment()).setPeekheight(fragmentFeedbackBinding.parentLl.getMeasuredHeight());
                fragmentFeedbackBinding.parentLl.getViewTreeObserver().removeOnGlobalLayoutListener(this::onGlobalLayout);
            }
        });

        fragmentFeedbackBinding.submit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(reviewRequestModel.getReviewDesc()!=null && reviewRequestModel.getReviewDesc().trim().length()>0 && reviewRequestModel.getReviewRating()!=0) {
                    Call objectCall = RestClient.getApiService().addSpecialistFeedback(reviewRequestModel);
                    RestClient.makeApiRequest(getActivity(), objectCall, context, 1, true);
                }
                else {
                    Utils.showToast(getActivity(),"Please provide both Ratings & Review");
                }

            }
        });
        fragmentFeedbackBinding.skip.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ((FavouriteSpecialistFragment)getParentFragment()).collapseBehavior();


            }
        });

    }


    @Override
    public void onApiResponse(Call<Object> call, Object response, int reqCode) throws Exception {
        RegistrationResponse registrationResponse = (RegistrationResponse) response;
        ((FavouriteSpecialistFragment)getParentFragment()).collapseBehavior();
        Utils.showToast(getActivity(),registrationResponse.getDescriptions());
    }

    @Override
    public void onApiError(Call<Object> call, Object response, int reqCode) throws Exception {

    }
}
