package com.demo.home.profile;

import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CompoundButton;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.databinding.DataBindingUtil;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentStatePagerAdapter;
import androidx.lifecycle.ViewModelProviders;

import com.demo.R;
import com.demo.databinding.FragmentMyDemoTripsBinding;
import com.demo.databinding.FragmentMyDemoTripsPagerBinding;
import com.demo.faq.model.FAQRequestModel;
import com.demo.home.HomeActivity;
import com.demo.home.model.AppContentModel;
import com.demo.home.model.viewmodel.AppContentViewModel;
import com.demo.home.model.viewmodel.AppContentViewModelFactory;
import com.demo.home.profile.model.DemoTripResponseModel;
import com.demo.utils.Constants;
import com.demo.utils.SharedPrefUtils;
import com.demo.utils.Utils;
import com.demo.webservice.ApiResponseListener;
import com.demo.webservice.RestClient;

import java.util.ArrayList;

import retrofit2.Call;

public class MyDemoTripsListFragment extends Fragment implements ApiResponseListener {
    private FragmentMyDemoTripsPagerBinding fragmentMyDemoTripsBinding;
    private int position;

    public static Fragment newInstance(int position) {
        MyDemoTripsListFragment fragmentFirst = new MyDemoTripsListFragment();
        Bundle args = new Bundle();
        args.putInt("pos", position);
        fragmentFirst.setArguments(args);
        return fragmentFirst;
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        position = getArguments().getInt("pos");
        fragmentMyDemoTripsBinding = DataBindingUtil.inflate(inflater, R.layout.fragment_my_demo_trips_pager,container,false);
        callMyDemoOptionsApi();
        callMyDemoListApi();
        fragmentMyDemoTripsBinding.firstCheckbox.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                callMyDemoListApi();
            }
        });
        fragmentMyDemoTripsBinding.secondCheckbox.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                callMyDemoListApi();
            }
        });
        fragmentMyDemoTripsBinding.thirdCheckbox.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                callMyDemoListApi();
            }
        });
        return fragmentMyDemoTripsBinding.getRoot();

    }

    private void callMyDemoListApi() {
        FAQRequestModel faqRequestModel = new FAQRequestModel();
        faqRequestModel.setSearchValue(getSearchValue());
        faqRequestModel.setUserID((new SharedPrefUtils(getActivity()).getStringData(Constants.USER_ID)));
        Call objectCall = RestClient.getApiService().getMyDemoTrips(faqRequestModel);
        RestClient.makeApiRequest(getActivity(), objectCall, this, 1, true);

    }

    private String getSearchValue() {
        String searchValue="";
        if(fragmentMyDemoTripsBinding.firstCheckbox.isChecked())
            searchValue =  fragmentMyDemoTripsBinding.firstCheckbox.getText().toString();
        else if(fragmentMyDemoTripsBinding.secondCheckbox.isChecked())
            searchValue = searchValue+","+ fragmentMyDemoTripsBinding.secondCheckbox.getText().toString();
        else if(fragmentMyDemoTripsBinding.thirdCheckbox.isChecked())
            searchValue = searchValue+","+ fragmentMyDemoTripsBinding.thirdCheckbox.getText().toString();

        return searchValue;
    }


    private void callMyDemoOptionsApi() {
        getViewModelStore().clear();
        AppContentViewModelFactory factory = new AppContentViewModelFactory(getActivity().getApplication(), Constants.MYDEMO_TRIPS_FILTER);
        AppContentViewModel appContentViewModel = ViewModelProviders.of(this, factory).get(AppContentViewModel.class);

        appContentViewModel.getMyDemoTripsFilterLiveData().observe(requireActivity(), item -> {
            fragmentMyDemoTripsBinding.setFilter(item);
            fragmentMyDemoTripsBinding.executePendingBindings();
        });
    }

    @Override
    public void onApiResponse(Call<Object> call, Object response, int reqCode) throws Exception {
        if(reqCode==1){
            DemoTripResponseModel demoTripResponseModel = (DemoTripResponseModel) response;
            //{"ResponseCode":"104","Descriptions":"My demo trips not present currently."}
            if(demoTripResponseModel.getResponseCode().equalsIgnoreCase("104")){
                Utils.showToast(getActivity(),demoTripResponseModel.getDescriptions());
                Intent intent = new Intent("calldemocount");
                intent.putExtra("count", "0");
                getActivity().sendBroadcast(intent);
            }
            else if(demoTripResponseModel.getResponseCode().equalsIgnoreCase("200")) {

                Intent intent = new Intent("calldemocount");
                intent.putExtra("count", demoTripResponseModel.getTotalDemoCount());
                getActivity().sendBroadcast(intent);
                ArrayList<DemoTripResponseModel.Demotrip> completedTrip = new ArrayList<>();
                ArrayList<DemoTripResponseModel.Demotrip> cancelledTrip = new ArrayList<>();
                for (DemoTripResponseModel.Demotrip demotrip : demoTripResponseModel.getDemotrips()) {
                    if (demotrip.getDemoStatus().equalsIgnoreCase("Completed")) {
                        completedTrip.add(demotrip);

                    }
                    if (demotrip.getDemoStatus().equalsIgnoreCase("cancelled")) {
                        cancelledTrip.add(demotrip);

                    }
                }
                fragmentMyDemoTripsBinding.recyclerviewMydemotrips.removeAllViews();
                if (position == 0) {
                    fragmentMyDemoTripsBinding.recyclerviewMydemotrips.setAdapter(new DemoTripsListAdapter(getContext(), completedTrip,getChildFragmentManager()));
                } else
                    fragmentMyDemoTripsBinding.recyclerviewMydemotrips.setAdapter(new DemoTripsListAdapter(getContext(), cancelledTrip,getChildFragmentManager()));
            }
        }
    }

    @Override
    public void onApiError(Call<Object> call, Object response, int reqCode) throws Exception {

    }
}
