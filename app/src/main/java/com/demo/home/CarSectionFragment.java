package com.demo.home;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.databinding.DataBindingUtil;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProviders;

import com.demo.R;
import com.demo.carDetails.CarDetailsActivity;
import com.demo.databinding.FragmentCarSuggestionBinding;
import com.demo.databinding.ItemSuggestionCarsBinding;
import com.demo.home.model.CarSearchRequestModel;
import com.demo.home.model.CarSearchResultModel;
import com.demo.home.model.viewmodel.SearchResultViewModel;
import com.demo.home.model.viewmodel.SearchResultViewModelFactory;
import com.demo.utils.Utils;

public class CarSectionFragment extends Fragment {
    private FragmentCarSuggestionBinding fragmentCarSuggestionBinding;
    private ViewGroup viewGroup;
   private String carSectionId,carSectionTitle;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        fragmentCarSuggestionBinding  = DataBindingUtil.inflate(inflater, R.layout.fragment_car_suggestion,container,false);
        viewGroup = container;
        fragmentCarSuggestionBinding.flowlayoutFueltype.setVisibility(View.GONE);
        carSectionId=getArguments().getString("carSectionId");
        carSectionTitle = getArguments().getString("carSectionTitle");
        fragmentCarSuggestionBinding.textviewTitle.setText(carSectionTitle);
        getCarListApi();
        return fragmentCarSuggestionBinding.getRoot();
    }

    private void getCarListApi() {
        SearchResultViewModelFactory factory = new SearchResultViewModelFactory(getActivity().getApplication(), getRequestModel());
        SearchResultViewModel searchResultViewModel = ViewModelProviders.of(this, factory).get(SearchResultViewModel.class);

        searchResultViewModel.getSearchResultLiveData().observe(getViewLifecycleOwner(), item -> {
            updateCarList(item);

        });



    }

    public void updateCarList(CarSearchResultModel item) {
        //fragmentCarSuggestionBinding.textviewTitle.setText(item.getCarListTitle());

        fragmentCarSuggestionBinding.horizontalScrollview.removeAllViews();
        if(!item.getResponseCode().equalsIgnoreCase("200")) {
            Utils.showToast(getActivity(), item.getDescriptions());
            return;
        }

        for(int i = 0; i< item.getCarlist().size(); i++) {
            LayoutInflater inflater = (LayoutInflater)getActivity().getSystemService(Context.LAYOUT_INFLATER_SERVICE);

            ItemSuggestionCarsBinding itemSuggestionCarsBinding = DataBindingUtil.inflate(inflater, R.layout.item_suggestion_cars,viewGroup,false);
            itemSuggestionCarsBinding.setCarSearchResult(item.getCarlist().get(i));
            int finalI = i;
            itemSuggestionCarsBinding.getRoot().setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Intent intent = new Intent(getActivity(), CarDetailsActivity.class);
                    intent.putExtra("carId",item.getCarlist().get(finalI).getCarID());
                    getActivity().startActivityForResult(intent,100);
                }
            });

            fragmentCarSuggestionBinding.horizontalScrollview.addView(itemSuggestionCarsBinding.getRoot());
        }
    }

    private CarSearchRequestModel getRequestModel() {
        CarSearchRequestModel carSearchRequestModel = new CarSearchRequestModel();
        if(((HomeActivity)getActivity()).getLocation()!=null) {
            carSearchRequestModel.setLatitude(String.valueOf(((HomeActivity) getActivity()).getLocation().getLatitude()));
            carSearchRequestModel.setLongitude(String.valueOf(((HomeActivity) getActivity()).getLocation().getLongitude()));
        }
        else
        {
            carSearchRequestModel.setLatitude("0.0");
            carSearchRequestModel.setLongitude("0.0");

        }
        carSearchRequestModel.setCarSectionType(carSectionId);
        carSearchRequestModel.setUserID(((HomeActivity)getActivity()).userId);
        carSearchRequestModel.setSpecialistID(((HomeActivity)getActivity()).specialistId);
        return carSearchRequestModel;

    }

}
