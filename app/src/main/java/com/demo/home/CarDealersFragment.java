
package com.demo.home;

import androidx.databinding.ViewDataBinding;
import androidx.fragment.app.Fragment;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.Nullable;
import androidx.databinding.DataBindingUtil;
import androidx.lifecycle.ViewModelProviders;
import androidx.recyclerview.widget.DividerItemDecoration;
import androidx.recyclerview.widget.LinearLayoutManager;

import com.demo.R;
import com.demo.databinding.FragmentTopDealersBinding;
import com.demo.home.model.CarDealerModel;
import com.demo.home.model.CarDealerResponseModel;
import com.demo.home.model.CarSearchRequestModel;
import com.demo.home.model.viewmodel.AppContentViewModel;
import com.demo.home.model.viewmodel.AppContentViewModelFactory;
import com.demo.utils.Constants;
import com.demo.webservice.ApiResponseListener;
import com.demo.webservice.RestClient;

import java.util.ArrayList;

import retrofit2.Call;

public class CarDealersFragment extends Fragment implements ApiResponseListener {

    private CarSearchRequestModel carSearchRequestModel = new CarSearchRequestModel();
    private FragmentTopDealersBinding fragmentTopDealersBinding;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, Bundle savedInstanceState) {
        callDelaerApi();


        fragmentTopDealersBinding = DataBindingUtil.inflate(inflater, R.layout.fragment_top_dealers,container,false);
       return fragmentTopDealersBinding.getRoot();

    }


    private void callDelaerApi() {
        if(((HomeActivity)getActivity()).locationUtils.getLoc()!=null) {
            carSearchRequestModel.setLatitude(String.valueOf(((HomeActivity) getActivity()).locationUtils.getLoc().getLatitude()));
            carSearchRequestModel.setLongitude(String.valueOf(((HomeActivity) getActivity()).locationUtils.getLoc().getLongitude()));
        }
        else
        {
            carSearchRequestModel.setLatitude("0.0");
            carSearchRequestModel.setLongitude("0.0");

        }
        carSearchRequestModel.setUserID(((HomeActivity)getActivity()).userId);

        Call objectCall = RestClient.getApiService().getCarDealer(carSearchRequestModel);
        RestClient.makeApiRequest(getActivity(), objectCall, this, 1, true);
    }

    @Override
    public void onApiResponse(Call<Object> call, Object response, int reqCode) throws Exception {
        CarDealerResponseModel carDealerResponseModel = (CarDealerResponseModel) response;
        fragmentTopDealersBinding.recyclerviewTopdealers.setAdapter(new CarDealerAdapter(getActivity(), (ArrayList<CarDealerResponseModel.Demospecialist>) carDealerResponseModel.getDemospecialist()));
        fragmentTopDealersBinding.recyclerviewTopdealers.setLayoutManager(new LinearLayoutManager(getActivity()));
        fragmentTopDealersBinding.recyclerviewTopdealers.addItemDecoration(new DividerItemDecoration(getActivity(),DividerItemDecoration.VERTICAL));

    }

    @Override
    public void onApiError(Call<Object> call, Object response, int reqCode) throws Exception {

    }
}
