package com.demo.home;

import android.os.Bundle;
import android.text.Html;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.databinding.DataBindingUtil;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;

import com.demo.R;
import com.demo.databinding.FragmentPersonalisedOptionsBinding;
import com.demo.home.model.CarSearchResultModel;
import com.demo.home.model.viewmodel.CarFilterViewModel;

import java.util.ArrayList;

public class PersonalisedCarOptionsFragment extends Fragment {
    private FragmentPersonalisedOptionsBinding fragmentPersonalisedOptionsBinding;
    private int pos;
    private CarFilterViewModel model;


    public static PersonalisedCarOptionsFragment newInstance(int pos) {
        PersonalisedCarOptionsFragment fragmentFirst = new PersonalisedCarOptionsFragment();
        Bundle args = new Bundle();
        args.putInt("pos", pos);
        fragmentFirst.setArguments(args);
        return fragmentFirst;
    }



    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        pos = getArguments().getInt("pos");
        model = new ViewModelProvider(requireActivity()).get(CarFilterViewModel.class);

    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        fragmentPersonalisedOptionsBinding = DataBindingUtil.inflate(inflater, R.layout.fragment_personalised_options,container,false);
        //  fragmentPersonalisedOptionsBinding.gridviewPerCar.setAdapter(new PersonalisedCarAdapter(getActivity(),chooseOptionsList));
        fragmentPersonalisedOptionsBinding.gridviewPerCar.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                updateView(view, R.drawable.border_red_rounded_corner, 1);
                String selectedName = view.getTag().toString();
                if(pos==0){
                    if(((HomeActivity)getActivity()).budgetSelectedId.contains(selectedName)) {
                        ((HomeActivity)getActivity()).budgetSelectedId.remove(selectedName);
                        updateView(view, R.drawable.white_border, 0.5f);

                    }
                    else
                    {
                        ((HomeActivity)getActivity()).budgetSelectedId.add(selectedName);
                        view.findViewById(R.id.ll_item_personalised_car).setAlpha(1);
                    }

                }
                else if(pos==1){
                    if(((HomeActivity)getActivity()).segmentSelectedId.contains(selectedName)) {
                        ((HomeActivity)getActivity()).segmentSelectedId.remove(selectedName);
                        updateView(view, R.drawable.white_border, 0.5f);
                    }
                    else
                    {
                        ((HomeActivity)getActivity()).segmentSelectedId.add(selectedName);
                        view.findViewById(R.id.ll_item_personalised_car).setAlpha(1);
                    }

                }
                else if(pos==2){
                    if(((HomeActivity)getActivity()).brandSelectedId.contains(selectedName)) {
                        ((HomeActivity)getActivity()).brandSelectedId.remove(selectedName);
                        updateView(view, R.drawable.white_border, 0.5f);
                    }
                    else
                    {
                        ((HomeActivity)getActivity()).brandSelectedId.add(selectedName);
                        view.findViewById(R.id.ll_item_personalised_car).setAlpha(1);
                    }

                }
                else if(pos==3){
                    if(((HomeActivity)getActivity()).fuelSelectedId.contains(selectedName)) {
                        ((HomeActivity)getActivity()).fuelSelectedId.remove(selectedName);
                        updateView(view, R.drawable.white_border, 0.5f);
                    }
                    else
                    {
                        ((HomeActivity)getActivity()).fuelSelectedId.add(selectedName);
                        view.findViewById(R.id.ll_item_personalised_car).setAlpha(1);
                    }

                }
                callSearchApi();
            }

            private void updateView(View view, int p, float v) {
                view.findViewById(R.id.ll_item_personalised_car).setBackgroundResource(p);
                view.findViewById(R.id.ll_item_personalised_car).setAlpha(v);
            }
        });


        return fragmentPersonalisedOptionsBinding.getRoot();

    }

    private void callSearchApi() {
        getViewModelStore().clear();

       String commaSeperatedBudgetList =  replaceCommaSepratedList(((HomeActivity)getActivity()).budgetSelectedId);
        String commaSeperatedBrandLit =  replaceCommaSepratedList(((HomeActivity)getActivity()).brandSelectedId);
        String commmaSeperatedSegmetList =  replaceCommaSepratedList(((HomeActivity)getActivity()).segmentSelectedId);
        String commmaSeperatedFuelList =  replaceCommaSepratedList(((HomeActivity)getActivity()).fuelSelectedId);

        ((HomeActivity)getActivity()).carSearchRequestModel.setBrandFilter(commaSeperatedBrandLit);
        ((HomeActivity)getActivity()).carSearchRequestModel.setSegmentFilter(commmaSeperatedSegmetList);
        ((HomeActivity)getActivity()).carSearchRequestModel.setPriceFilter(commaSeperatedBudgetList);
        ((HomeActivity)getActivity()).carSearchRequestModel.setFuelType(commmaSeperatedFuelList);

        ((HomeActivity)getActivity()).carSearchRequestModel.setSearchValue("");
        ((HomeActivity)getActivity()).callSearchApi("2",new SearchResultInterface() {
            @Override
            public void onSearch(CarSearchResultModel carSearchRequestModel) {

            }
        });
    }

    private String replaceCommaSepratedList(ArrayList<String> stringArrayList) {
       String commaseparatedlist
                = stringArrayList.toString().replace("[", "")
                .replace("]", "")
                .replace(" ", "");
        return commaseparatedlist;
    }


    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        switch (pos)
        {
            case 0:
                fragmentPersonalisedOptionsBinding.textviewTitle.setText(Html.fromHtml("Choose Your <b>Budget</b>"));
                model.getBudgetListData().observe(getViewLifecycleOwner(), item -> {
                    fragmentPersonalisedOptionsBinding.gridviewPerCar.setAdapter(new PersonalisedCarAdapter(getActivity(), item,pos));

                });
                break;
            case 1:

                fragmentPersonalisedOptionsBinding.textviewTitle.setText(Html.fromHtml("Choose Your <b>Segment</b>"));
                model.getSegmentListData().observe(getViewLifecycleOwner(),item ->{
                    fragmentPersonalisedOptionsBinding.gridviewPerCar.setAdapter(new PersonalisedCarAdapter(getActivity(), item,pos));

                });
                break;
            case 2:
                fragmentPersonalisedOptionsBinding.textviewTitle.setText(Html.fromHtml("Choose Your <b>Brand</b>"));
                model.getBrandListData().observe(getViewLifecycleOwner(),item ->{
                    fragmentPersonalisedOptionsBinding.gridviewPerCar.setAdapter(new PersonalisedCarAdapter(getActivity(), item,pos));

                });
                break;
            case 3:
                fragmentPersonalisedOptionsBinding.textviewTitle.setText(Html.fromHtml("<b>Fuel</b> Options"));
                model.getFuelListData().observe(getViewLifecycleOwner(),item ->{
                    fragmentPersonalisedOptionsBinding.gridviewPerCar.setAdapter(new PersonalisedCarAdapter(getActivity(), item,pos));

                });
                break;
        }

    }
}
