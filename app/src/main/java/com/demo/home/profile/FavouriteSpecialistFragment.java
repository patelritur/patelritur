package com.demo.home.profile;

import android.graphics.Color;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.TranslateAnimation;
import android.widget.ShareActionProvider;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.databinding.DataBindingUtil;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentContainerView;

import com.demo.R;
import com.demo.databinding.FragmentFavouriteDemoSpecialistBinding;
import com.demo.faq.model.FAQRequestModel;
import com.demo.home.booking.BookingFeedbackFragment;
import com.demo.home.profile.model.FavoritespecialistModel;
import com.demo.utils.Constants;
import com.demo.utils.PrintLog;
import com.demo.utils.SharedPrefUtils;
import com.demo.utils.Utils;
import com.demo.webservice.ApiResponseListener;
import com.demo.webservice.RestClient;
import com.google.android.material.bottomsheet.BottomSheetBehavior;

import java.util.ArrayList;

import retrofit2.Call;

public class FavouriteSpecialistFragment extends Fragment implements ApiResponseListener {
    FragmentFavouriteDemoSpecialistBinding fragmentFavouriteDemoSpecialistBinding;
    private BottomSheetBehavior<FragmentContainerView> behavior;

    public static Fragment newInstance(int position) {
        FavouriteSpecialistFragment fragmentFirst = new FavouriteSpecialistFragment();
        Bundle args = new Bundle();
        args.putInt("pos", position);
        fragmentFirst.setArguments(args);
        return fragmentFirst;

    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        fragmentFavouriteDemoSpecialistBinding = DataBindingUtil.inflate(inflater, R.layout.fragment_favourite_demo_specialist,container,false);
        callFavouriteSpecialistApi();
        return fragmentFavouriteDemoSpecialistBinding.getRoot();

    }

    private void callFavouriteSpecialistApi() {
        FAQRequestModel faqRequestModel = new FAQRequestModel();
        faqRequestModel.setUserID(new SharedPrefUtils(getActivity()).getStringData(Constants.USER_ID));
        Call objectCall = RestClient.getApiService().getFavouriteSpecialist(faqRequestModel);
        RestClient.makeApiRequest(getActivity(), objectCall, this, 1, true);
    }

    public void showFragment(SpecialistFeedbackFragment bookingFeedbackFragment) {
        behavior = BottomSheetBehavior.from(fragmentFavouriteDemoSpecialistBinding.fragmentContainerView);
        behavior.setState(BottomSheetBehavior.STATE_COLLAPSED);

        fragmentFavouriteDemoSpecialistBinding.coordinator11.setVisibility(View.VISIBLE);
        fragmentFavouriteDemoSpecialistBinding.fragmentContainerView.removeAllViews();
        slideUp(fragmentFavouriteDemoSpecialistBinding.coordinator11);
        getChildFragmentManager().beginTransaction()
                .replace(R.id.fragment_container_view, bookingFeedbackFragment)
                .commit();
        fragmentFavouriteDemoSpecialistBinding.coordinator11.setMinimumHeight( fragmentFavouriteDemoSpecialistBinding.recyclerviewFavouritespecialist.getMeasuredHeight());


    }
    public void collapseBehavior(){
        behavior.setState(BottomSheetBehavior.STATE_COLLAPSED);
        behavior.setState(BottomSheetBehavior.STATE_HIDDEN);
        fragmentFavouriteDemoSpecialistBinding.coordinator11.setVisibility(View.GONE);

    }
    public void slideUp(View view){
        view.setVisibility(View.VISIBLE);
        TranslateAnimation animate = new TranslateAnimation(
                0,                 // fromXDelta
                0,                 // toXDelta
                view.getHeight(),  // fromYDelta
                0);                // toYDelta
        animate.setDuration(500);
        animate.setFillAfter(true);
        view.startAnimation(animate);
    }

    @Override
    public void onApiResponse(Call<Object> call, Object response, int reqCode) throws Exception {
        FavoritespecialistModel favoritespecialistModel = (FavoritespecialistModel) response;
        if(favoritespecialistModel.getResponseCode().equalsIgnoreCase("104")){
            Utils.showToast(getActivity(),favoritespecialistModel.getDescriptions());
        }
        fragmentFavouriteDemoSpecialistBinding.recyclerviewFavouritespecialist.setAdapter(new FavouriteSpecialistsListAdapter(getActivity(),FavouriteSpecialistFragment.this, (ArrayList<FavoritespecialistModel.Favoritespecialist>)favoritespecialistModel.getFavoritespecialist()));


    }

    @Override
    public void onApiError(Call<Object> call, Object response, int reqCode) throws Exception {

    }

    public void setPeekheight(int measuredHeight) {
        behavior.setPeekHeight(measuredHeight);
        fragmentFavouriteDemoSpecialistBinding.coordinator11.setMinimumHeight(measuredHeight);
    }

}
