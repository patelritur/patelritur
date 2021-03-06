package com.demo.home;

import android.os.Bundle;
import android.text.Html;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.databinding.DataBindingUtil;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;
import androidx.lifecycle.ViewModelProviders;
import androidx.recyclerview.widget.DividerItemDecoration;
import androidx.recyclerview.widget.LinearLayoutManager;

import com.demo.R;
import com.demo.databinding.FragmentPersonalisedOptionsBinding;
import com.demo.home.model.CarSearchRequestModel;
import com.demo.home.model.CarSearchResultModel;
import com.demo.home.model.viewmodel.CarFilterViewModel;
import com.demo.home.model.viewmodel.SearchResultViewModel;
import com.demo.home.model.viewmodel.SearchResultViewModelFactory;
import com.demo.utils.Constants;
import com.demo.utils.PrintLog;
import com.demo.utils.SharedPrefUtils;

import java.util.ArrayList;
import java.util.stream.Collectors;

public class PersonalisedCarOptionsFragment extends Fragment {
    private FragmentPersonalisedOptionsBinding fragmentPersonalisedOptionsBinding;
    private int pos;
    private CarFilterViewModel model;
    private SharedPrefUtils sharedPrefUtils;


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
        sharedPrefUtils = new SharedPrefUtils(getActivity());

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
                    if(((HomeActivity)getActivity()).BudgetSelectedId.contains(selectedName)) {
                        ((HomeActivity)getActivity()).BudgetSelectedId.remove(selectedName);
                        updateView(view, R.drawable.white_border, 0.5f);

                    }
                    else
                    {
                        ((HomeActivity)getActivity()).BudgetSelectedId.add(selectedName);
                        view.findViewById(R.id.ll_item_personalised_car).setAlpha(1);
                    }

                }
                else if(pos==1){
                    if(((HomeActivity)getActivity()).SegmentSelectedId.contains(selectedName)) {
                        ((HomeActivity)getActivity()).SegmentSelectedId.remove(selectedName);
                        updateView(view, R.drawable.white_border, 0.5f);
                    }
                    else
                    {
                        ((HomeActivity)getActivity()).SegmentSelectedId.add(selectedName);
                        view.findViewById(R.id.ll_item_personalised_car).setAlpha(1);
                    }

                }
                else if(pos==2){
                    if(((HomeActivity)getActivity()).BrandSelectedId.contains(selectedName)) {
                        ((HomeActivity)getActivity()).BrandSelectedId.remove(selectedName);
                        updateView(view, R.drawable.white_border, 0.5f);
                    }
                    else
                    {
                        ((HomeActivity)getActivity()).BrandSelectedId.add(selectedName);
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
        String commaseparatedlist = ((HomeActivity)getActivity()).BudgetSelectedId.toString();
        String commaSeperatedBrandLit = ((HomeActivity)getActivity()).BrandSelectedId.toString();
        String commmaSeperatedSegmetList = ((HomeActivity)getActivity()).SegmentSelectedId.toString();

        commaseparatedlist
                = commaseparatedlist.replace("[", "")
                .replace("]", "")
                .replace(" ", "");
        commaSeperatedBrandLit
                = commaSeperatedBrandLit.replace("[", "")
                .replace("]", "")
                .replace(" ", "");
        commmaSeperatedSegmetList
                = commmaSeperatedSegmetList.replace("[", "")
                .replace("]", "")
                .replace(" ", "");
        ((HomeActivity)getActivity()).carSearchRequestModel.setBrandFilter(commaSeperatedBrandLit);
        ((HomeActivity)getActivity()).carSearchRequestModel.setSegmentFilter(commmaSeperatedSegmetList);
        ((HomeActivity)getActivity()).carSearchRequestModel.setPriceFilter(commaseparatedlist);
        ((HomeActivity)getActivity()).carSearchRequestModel.setSearchValue("");
        ((HomeActivity)getActivity()).callSearchApi(new SearchResultInterface() {
            @Override
            public void onSearch(CarSearchResultModel carSearchRequestModel) {

            }
        });
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
        }

    }
}
