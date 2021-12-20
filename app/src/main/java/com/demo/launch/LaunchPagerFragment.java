package com.demo.launch;

import android.content.Context;
import android.os.Bundle;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.EditorInfo;
import android.view.inputmethod.InputMethodManager;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.databinding.DataBindingUtil;
import androidx.fragment.app.Fragment;

import com.demo.R;
import com.demo.databinding.FragmentPagerLaunchBinding;
import com.demo.launch.model.LaunchRequestModel;
import com.demo.launch.model.LaunchResponseModel;
import com.demo.utils.Constants;
import com.demo.utils.SharedPrefUtils;
import com.demo.utils.Utils;
import com.demo.webservice.ApiResponseListener;
import com.demo.webservice.RestClient;

import java.util.ArrayList;

import retrofit2.Call;

public class LaunchPagerFragment extends Fragment implements ApiResponseListener {
    private FragmentPagerLaunchBinding fragmentPagerLaunchBinding;
    private int position;
    public static Fragment newInstance(int position) {
        LaunchPagerFragment fragmentFirst = new LaunchPagerFragment();
        Bundle args = new Bundle();
        args.putInt("pos", position);
        fragmentFirst.setArguments(args);
        return fragmentFirst;
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        fragmentPagerLaunchBinding = DataBindingUtil.inflate(inflater, R.layout.fragment_pager_launch,container,false);
        position = getArguments().getInt("pos");
        fragmentPagerLaunchBinding.etSearch.setOnEditorActionListener(new TextView.OnEditorActionListener() {
            @Override
            public boolean onEditorAction(TextView v, int actionId, KeyEvent event) {
                if (actionId == EditorInfo.IME_ACTION_DONE) {
                    InputMethodManager imm = (InputMethodManager) v.getContext().getSystemService(Context.INPUT_METHOD_SERVICE);
                    imm.hideSoftInputFromWindow(v.getWindowToken(), 0);
                    callLaunchApi();
                }
                return false;
            }
        });
        callLaunchApi();

        return fragmentPagerLaunchBinding.getRoot();

    }

    private void callLaunchApi() {
        LaunchRequestModel launchRequestModel = new LaunchRequestModel();
        launchRequestModel.setUserID(new SharedPrefUtils(getActivity()).getStringData(Constants.USER_ID));
        if(Constants.LATITUDE==null){
            launchRequestModel.setLatitude("0.0");
            launchRequestModel.setLongitude("0.0");
        }
        else {
            launchRequestModel.setLatitude(Constants.LATITUDE);
            launchRequestModel.setLongitude(Constants.LONGITUDE);
        }
        launchRequestModel.setSearchValue(fragmentPagerLaunchBinding.etSearch.getText().toString());
        if(position==0)
            launchRequestModel.setTabType("Upcoming Launch");
        else
            launchRequestModel.setTabType("Recent Launch");

        Call objectCall = RestClient.getApiService().getLaunch(launchRequestModel);
        RestClient.makeApiRequest(getActivity(), objectCall, this, 1, true);
    }



    @Override
    public void onApiResponse(Call<Object> call, Object response, int reqCode) throws Exception {
        LaunchResponseModel launchResponseModel = (LaunchResponseModel) response;
        if(launchResponseModel.getResponseCode().equalsIgnoreCase("200"))
            fragmentPagerLaunchBinding.recyclerviewLaunch.setAdapter(new LaunchListAdapter(getActivity(),position, getChildFragmentManager(),(ArrayList<LaunchResponseModel.Latestlaunchlist>) launchResponseModel.getLatestlaunchlist()));
        else
            Utils.showToast(getActivity(),launchResponseModel.getDescriptions());

    }

    @Override
    public void onApiError(Call<Object> call, Object response, int reqCode) throws Exception {

    }
}
