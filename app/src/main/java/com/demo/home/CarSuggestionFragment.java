
package com.demo.home;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CompoundButton;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.Nullable;
import androidx.databinding.DataBindingUtil;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;
import androidx.lifecycle.ViewModelProviders;

import com.demo.R;
import com.demo.carDetails.CarDetailsActivity;
import com.demo.databinding.FragmentCarSuggestionBinding;
import com.demo.databinding.ItemSuggestionCarsBinding;
import com.demo.home.model.CarFilterResponse;
import com.demo.home.model.CarSearchRequestModel;
import com.demo.home.model.CarSearchResultModel;
import com.demo.home.model.viewmodel.FuelFilterViewModel;
import com.demo.home.model.viewmodel.SearchResultViewModel;
import com.demo.home.model.viewmodel.SearchResultViewModelFactory;
import com.demo.utils.Utils;
import com.google.android.material.checkbox.MaterialCheckBox;

import java.util.ArrayList;

public class CarSuggestionFragment extends Fragment implements SearchResultInterface {
    private FragmentCarSuggestionBinding fragmentCarSuggestionBinding;
    private ViewGroup viewGroup;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, Bundle savedInstanceState) {
        fragmentCarSuggestionBinding  = DataBindingUtil.inflate(inflater, R.layout.fragment_car_suggestion,container,false);
        viewGroup = container;
        return fragmentCarSuggestionBinding.getRoot();

    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        setSuggestionsForCarView();
       // setFuelTypeLayout();

    }
    private void setSuggestionsForCarView() {
        SearchResultViewModelFactory factory = new SearchResultViewModelFactory(getActivity().getApplication(), getSearchModelForWord());
        SearchResultViewModel searchResultViewModel = ViewModelProviders.of(this, factory).get(SearchResultViewModel.class);

        searchResultViewModel.getSearchResultLiveData().observe(getViewLifecycleOwner(), item -> {
            updateCarList(item);

        });



    }

    public void updateCarList(CarSearchResultModel item) {
        fragmentCarSuggestionBinding.textviewTitle.setText(item.getCarListTitle());
        fragmentCarSuggestionBinding.horizontalScrollview.removeAllViews();
        if(!item.getResponseCode().equalsIgnoreCase("200")) {
            fragmentCarSuggestionBinding.textviewTitle.setText(item.getDescriptions());
            return;
        }

        for(int i = 0; i< item.getCarlist().size(); i++) {
            LayoutInflater inflater = (LayoutInflater)getActivity().getSystemService(Context.LAYOUT_INFLATER_SERVICE);

            ItemSuggestionCarsBinding itemSuggestionCarsBinding = DataBindingUtil.inflate(inflater,R.layout.item_suggestion_cars,viewGroup,false);
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

    private CarSearchRequestModel getSearchModelForWord() {
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
        carSearchRequestModel.setUserID(((HomeActivity)getActivity()).userId);
        carSearchRequestModel.setSpecialistID(((HomeActivity)getActivity()).specialistId);
        return carSearchRequestModel;

    }

    private void setFuelTypeLayout() {
        new ViewModelProvider(requireActivity()).get(FuelFilterViewModel.class).getFuelTypeListData().observe(getViewLifecycleOwner(), item -> {
            updateUIForFuel(item);
        });


    }

    private void updateUIForFuel(CarFilterResponse carFilterViewModel) {
        ArrayList<String> checkedArraylist = new ArrayList<>();
        fragmentCarSuggestionBinding.flowlayoutFueltype.removeAllViews();
        for (int i = 0; i < carFilterViewModel.getFilter().size(); i++) {
            View view = this.getLayoutInflater().inflate(R.layout.item_fueltype, null);
            TextView textView = (TextView) view.findViewById(R.id.textview_fueltype) ;
            textView.setText(carFilterViewModel.getFilter().get(i).getFilterName());
            MaterialCheckBox materialCheckBox = (MaterialCheckBox) view.findViewById(R.id.checkbox_fueltype) ;
            fragmentCarSuggestionBinding.flowlayoutFueltype.addView(view);

            int finalI = i;
            materialCheckBox.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {


                @Override
                public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {

                    if(isChecked)
                        checkedArraylist.add(carFilterViewModel.getFilter().get(finalI).getFilterName());
                    else
                    if(checkedArraylist.contains(carFilterViewModel.getFilter().get(finalI).getFilterName()))
                        checkedArraylist.remove(carFilterViewModel.getFilter().get(finalI).getFilterName());
                    ((HomeActivity)getActivity()).carSearchRequestModel.setFuelType(checkedArraylist.toString().replace("[", "")
                            .replace("]", "")
                            .replace(" ", ""));
                    ((HomeActivity)getActivity()).carSearchRequestModel.setSearchValue("");
                    ((HomeActivity)getActivity()).callSearchApi("2",new SearchResultInterface() {
                        @Override
                        public void onSearch(CarSearchResultModel carSearchRequestModel) {

                            updateCarList(carSearchRequestModel);
                        }
                    });
                }
            });

        }

    }


    @Override
    public void onSearch(CarSearchResultModel carSearchRequestModel) {

    }
}
