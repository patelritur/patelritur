package com.demo.home;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.databinding.DataBindingUtil;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.LifecycleOwner;
import androidx.lifecycle.ViewModelProvider;
import androidx.lifecycle.ViewModelProviders;

import com.demo.R;
import com.demo.databinding.FragmentBannerBinding;
import com.demo.home.model.viewmodel.AppContentViewModel;
import com.demo.home.model.viewmodel.AppContentViewModelFactory;
import com.demo.home.model.viewmodel.MeetingPlaceViewModel;
import com.demo.utils.Constants;
import com.demo.utils.PrintLog;

public class CarBannerFragment extends Fragment implements LifecycleOwner {
    FragmentBannerBinding fragmentBannerBinding;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, Bundle savedInstanceState) {

     PrintLog.v("fragment_banner","fragment_banner");
          fragmentBannerBinding = DataBindingUtil.inflate(inflater, R.layout.fragment_banner,container,false);

        return fragmentBannerBinding.getRoot();
    }


    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        callProductListApi();

    }

    private void callProductListApi() {
        getActivity().getViewModelStore().clear();
        AppContentViewModelFactory factory = new AppContentViewModelFactory(getActivity().getApplication(), Constants.BANNER);
        AppContentViewModel appContentViewModel = ViewModelProviders.of(requireActivity(), factory).get(AppContentViewModel.class);

        appContentViewModel.getBannerLiveData().observe(getViewLifecycleOwner(), item -> {
            PrintLog.v("fragment_banner","response");
            fragmentBannerBinding.setImageUrl(item.getLabels().get(0).getLabelImage());
            fragmentBannerBinding.executePendingBindings();
        });
    }

}
