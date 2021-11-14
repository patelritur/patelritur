package com.demo.home.profile;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ShareActionProvider;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.databinding.DataBindingUtil;
import androidx.fragment.app.Fragment;

import com.demo.R;
import com.demo.databinding.FragmentFavouriteDemoSpecialistBinding;
import com.demo.faq.model.FAQRequestModel;
import com.demo.home.profile.model.FavoritespecialistModel;
import com.demo.utils.Constants;
import com.demo.utils.SharedPrefUtils;
import com.demo.utils.Utils;
import com.demo.webservice.ApiResponseListener;
import com.demo.webservice.RestClient;

import java.util.ArrayList;

import retrofit2.Call;

public class FavouriteSpecialistFragment extends Fragment implements ApiResponseListener {
    FragmentFavouriteDemoSpecialistBinding fragmentFavouriteDemoSpecialistBinding;
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

    @Override
    public void onApiResponse(Call<Object> call, Object response, int reqCode) throws Exception {
        FavoritespecialistModel favoritespecialistModel = (FavoritespecialistModel) response;
        if(favoritespecialistModel.getResponseCode().equalsIgnoreCase("104")){
            Utils.showToast(getActivity(),favoritespecialistModel.getDescriptions());
        }
        fragmentFavouriteDemoSpecialistBinding.recyclerviewFavouritespecialist.setAdapter(new FavouriteSpecialistsListAdapter(getActivity(), (ArrayList<FavoritespecialistModel.Favoritespecialist>)favoritespecialistModel.getFavoritespecialist()));


    }

    @Override
    public void onApiError(Call<Object> call, Object response, int reqCode) throws Exception {

    }
}
