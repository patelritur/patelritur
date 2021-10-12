
package com.demo.home;


import android.app.Activity;
import android.content.Context;
import android.os.Bundle;
import android.util.DisplayMetrics;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.EditorInfo;
import android.view.inputmethod.InputMethodManager;
import android.widget.FrameLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.widget.AppCompatEditText;
import androidx.appcompat.widget.LinearLayoutCompat;
import androidx.databinding.DataBindingUtil;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.LifecycleOwner;
import androidx.lifecycle.ViewModelProviders;
import androidx.recyclerview.widget.DividerItemDecoration;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.demo.R;
import com.demo.databinding.DialogSearchCarBinding;
import com.demo.databinding.FragmentHomeSearchBinding;
import com.demo.home.model.CarFilterResponse;
import com.demo.home.model.CarSearchRequestModel;
import com.demo.home.model.CarSearchResultModel;
import com.demo.home.model.viewmodel.AppContentViewModel;
import com.demo.home.model.viewmodel.AppContentViewModelFactory;
import com.demo.home.model.viewmodel.SearchResultViewModel;
import com.demo.home.model.viewmodel.SearchResultViewModelFactory;
import com.demo.utils.Constants;
import com.demo.utils.SharedPrefUtils;
import com.demo.webservice.ApiResponseListener;
import com.demo.webservice.RestClient;
import com.google.android.material.bottomsheet.BottomSheetBehavior;
import com.google.android.material.bottomsheet.BottomSheetDialog;

import java.util.ArrayList;

import retrofit2.Call;

public class SearchFragment extends Fragment {


    FragmentHomeSearchBinding fragmentHomeSearchBinding;
    private RecyclerView carsearchRecyslerview;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        fragmentHomeSearchBinding = DataBindingUtil.inflate(inflater, R.layout.fragment_home_search,container,false);
        return fragmentHomeSearchBinding.getRoot();

    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        fragmentHomeSearchBinding.etSearch.setOnFocusChangeListener(new View.OnFocusChangeListener() {
            @Override
            public void onFocusChange(View v, boolean hasFocus) {
                if(hasFocus){
                    showBottomSheetDialog();}

            }
        });
    }
    private void showBottomSheetDialog() {

        final BottomSheetDialog bottomSheetDialog = new BottomSheetDialog(getActivity());
        DialogSearchCarBinding dialogSearchCarBinding = DataBindingUtil.inflate(LayoutInflater.from(getContext()), R.layout. dialog_search_car, null, false);
        bottomSheetDialog.setContentView(dialogSearchCarBinding.getRoot());
        callVehicleType(dialogSearchCarBinding);
        carsearchRecyslerview = dialogSearchCarBinding.carsearchRecyclerview;
        dialogSearchCarBinding.textviewPersonalise.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                bottomSheetDialog.dismiss();
                ((HomeActivity)getActivity()).showSelectFilerCars();

            }
        });
        dialogSearchCarBinding.etSearch.setOnEditorActionListener(new TextView.OnEditorActionListener() {
            @Override
            public boolean onEditorAction(TextView v, int actionId, KeyEvent event) {
                if (actionId == EditorInfo.IME_ACTION_DONE) {
                    InputMethodManager imm = (InputMethodManager)v.getContext().getSystemService(Context.INPUT_METHOD_SERVICE);
                    imm.hideSoftInputFromWindow(v.getWindowToken(), 0);

                            ((HomeActivity)getActivity()).carSearchRequestModel.setSearchValue(dialogSearchCarBinding.etSearch.getText().toString());
                            ((HomeActivity)getActivity()).callSearchApi(new SearchResultInterface() {
                                @Override
                                public void onSearch(CarSearchResultModel carSearchResultModel) {
                                    carsearchRecyslerview.setAdapter(new CarSearchResultAdapter(getActivity(), (ArrayList<CarSearchResultModel.Carlist>) carSearchResultModel.getCarlist()));
                                    carsearchRecyslerview.setLayoutManager(new LinearLayoutManager(getActivity()));
                                    carsearchRecyslerview.addItemDecoration(new DividerItemDecoration(getActivity(),DividerItemDecoration.VERTICAL));


                                }
                            });




                    return true;
                }
                return false;
            }
        });
        bottomSheetDialog.findViewById(R.id.iv_close).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                bottomSheetDialog.dismiss();
            }
        });
        bottomSheetDialog.show();
        setupFullHeight(bottomSheetDialog);
    }

    private void callVehicleType(DialogSearchCarBinding dialogSearchCarBinding) {
        Call objectCall = RestClient.getApiService().getVehicleType();
        RestClient.makeApiRequest(getActivity(), objectCall, new ApiResponseListener() {
            @Override
            public void onApiResponse(Call<Object> call, Object response, int reqCode) throws Exception {
                CarFilterResponse carFilterResponse = (CarFilterResponse)response ;
                dialogSearchCarBinding.setCarImage(carFilterResponse.getFilter().get(1).FilterIcon);
                dialogSearchCarBinding.setBikeImage(carFilterResponse.getFilter().get(0).FilterIcon);

            }

            @Override
            public void onApiError(Call<Object> call, Object response, int reqCode) throws Exception {

            }
        }, 1, true);


    }




    private void setupFullHeight(BottomSheetDialog bottomSheetDialog) {

        FrameLayout bottomSheet =  bottomSheetDialog.findViewById(com.google.android.material.R.id.design_bottom_sheet);
        BottomSheetBehavior behavior = BottomSheetBehavior.from(bottomSheet);
        ViewGroup.LayoutParams layoutParams = bottomSheet.getLayoutParams();

        int windowHeight = getWindowHeight();
        if (layoutParams != null) {
            layoutParams.height = windowHeight;
        }
        bottomSheet.setLayoutParams(layoutParams);
        behavior.setState(BottomSheetBehavior.STATE_EXPANDED);
    }

    private int getWindowHeight() {
        // Calculate window height for fullscreen use
        DisplayMetrics displayMetrics = new DisplayMetrics();
        ((Activity) getContext()).getWindowManager().getDefaultDisplay().getMetrics(displayMetrics);
        return displayMetrics.heightPixels;
    }
}
