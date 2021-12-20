package com.demo.home.profile;

import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CompoundButton;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.databinding.DataBindingUtil;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProviders;

import com.demo.R;
import com.demo.databinding.FragmentMyDemoTripsPagerBinding;
import com.demo.faq.model.FAQRequestModel;
import com.demo.home.model.AppContentModel;
import com.demo.home.model.viewmodel.AppContentViewModel;
import com.demo.home.model.viewmodel.AppContentViewModelFactory;
import com.demo.home.profile.model.DemoTripResponseModel;
import com.demo.utils.Constants;
import com.demo.utils.PrintLog;
import com.demo.utils.SharedPrefUtils;
import com.demo.utils.Utils;
import com.demo.webservice.ApiResponseListener;
import com.demo.webservice.RestClient;

import java.io.Serializable;
import java.util.ArrayList;

import retrofit2.Call;

public class MyDemoTripsListFragment extends Fragment implements ApiResponseListener {
    private FragmentMyDemoTripsPagerBinding fragmentMyDemoTripsBinding;
    private int position;
   private ArrayList<String> searchValue=new ArrayList<>();
   private AppContentModel appContentModel;


    public static MyDemoTripsListFragment newInstance(int position, AppContentModel item) {

        MyDemoTripsListFragment fragmentFirst = new MyDemoTripsListFragment();
        Bundle args = new Bundle();
        args.putInt("pos", position);
        args.putSerializable("item", (Serializable) item);
        fragmentFirst.setArguments(args);
        return fragmentFirst;
    }



    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        position = getArguments().getInt("pos");
        appContentModel = (AppContentModel) getArguments().getSerializable("item");
        fragmentMyDemoTripsBinding = DataBindingUtil.inflate(inflater, R.layout.fragment_my_demo_trips_pager,container,false);
        callMyDemoOptionsApi();
        callMyDemoListApi();

        fragmentMyDemoTripsBinding.firstCheckbox.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if(isChecked ){
                    if(!searchValue.contains(buttonView.getText().toString()))
                    searchValue.add(buttonView.getText().toString().trim());
                }
                else{
                    searchValue.remove(buttonView.getText().toString());

                }
                MyDemoTripsFragment parentFrag = ((MyDemoTripsFragment)MyDemoTripsListFragment.this.getParentFragment());
                parentFrag.setSearchValue(searchValue);
                callMyDemoListApi();
            }
        });
        fragmentMyDemoTripsBinding.secondCheckbox.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if(isChecked){
                    if(!searchValue.contains(buttonView.getText().toString()))
                        searchValue.add(buttonView.getText().toString().trim());
                }
                else{
                    searchValue.remove(buttonView.getText().toString());

                }
                MyDemoTripsFragment parentFrag = ((MyDemoTripsFragment)MyDemoTripsListFragment.this.getParentFragment());
                parentFrag.setSearchValue(searchValue);
                callMyDemoListApi();
            }
        });
        fragmentMyDemoTripsBinding.thirdCheckbox.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if(isChecked){
                    if(!searchValue.contains(buttonView.getText().toString()))
                        searchValue.add(buttonView.getText().toString().trim());
                }
                else{
                    searchValue.remove(buttonView.getText().toString());

                }
                MyDemoTripsFragment parentFrag = ((MyDemoTripsFragment)MyDemoTripsListFragment.this.getParentFragment());
                parentFrag.setSearchValue(searchValue);
                callMyDemoListApi();
            }
        });
        return fragmentMyDemoTripsBinding.getRoot();

    }

    private void callMyDemoListApi() {
        FAQRequestModel faqRequestModel = new FAQRequestModel();
        faqRequestModel.setSearchValue(searchValue.toString().replace("[","").replace("]","").replace(", ",","));
        faqRequestModel.setUserID((new SharedPrefUtils(getActivity()).getStringData(Constants.USER_ID)));
        Call objectCall = RestClient.getApiService().getMyDemoTrips(faqRequestModel);
        RestClient.makeApiRequest(getActivity(), objectCall, this, 1, true);

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
                ArrayList<ArrayList<DemoTripResponseModel.Demotrip>> parentList = new ArrayList<>();
                for(int i=0;i<appContentModel.getLabels().size();i++){
                    parentList.add(new ArrayList<DemoTripResponseModel.Demotrip>());
                }

                for (DemoTripResponseModel.Demotrip demotrip : demoTripResponseModel.getDemotrips()) {
                    for(int i=0;i<appContentModel.getLabels().size();i++){
                        if (demotrip.getDemoStatus().equalsIgnoreCase(appContentModel.getLabels().get(i).getLabelInLanguage()))
                        parentList.get(i).add(demotrip);
                    }


                }

                fragmentMyDemoTripsBinding.recyclerviewMydemotrips.removeAllViews();
                fragmentMyDemoTripsBinding.recyclerviewMydemotrips.setAdapter(new DemoTripsListAdapter(getContext(), parentList.get(position),getChildFragmentManager()));


            }
        }
    }

    @Override
    public void onApiError(Call<Object> call, Object response, int reqCode) throws Exception {

    }

    public void notifyList(ArrayList<String> searchValue) {
         if(!this.searchValue.equals(searchValue)) {
             this.searchValue = searchValue;
             callMyDemoListApi();
         }
        setCheckBox();
    }

    private void setCheckBox() {
        fragmentMyDemoTripsBinding.firstCheckbox.setChecked(searchValue.contains(fragmentMyDemoTripsBinding.firstCheckbox.getText().toString()));
        fragmentMyDemoTripsBinding.secondCheckbox.setChecked(searchValue.contains(fragmentMyDemoTripsBinding.secondCheckbox.getText().toString()));
        fragmentMyDemoTripsBinding.thirdCheckbox.setChecked(searchValue.contains(fragmentMyDemoTripsBinding.thirdCheckbox.getText().toString()));
    }
}
