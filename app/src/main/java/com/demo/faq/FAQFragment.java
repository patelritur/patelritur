package com.demo.faq;

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
import androidx.databinding.ViewDataBinding;
import androidx.fragment.app.Fragment;

import com.demo.R;
import com.demo.databinding.FragmentFaqQuestionBinding;
import com.demo.faq.model.FAQRequestModel;
import com.demo.faq.model.FAQResponseModel;
import com.demo.utils.Constants;
import com.demo.utils.Utils;
import com.demo.webservice.ApiResponseListener;
import com.demo.webservice.RestClient;

import java.util.List;

import retrofit2.Call;

public class FAQFragment extends Fragment implements ApiResponseListener {

    private FragmentFaqQuestionBinding fragmentFaqQuestionBinding;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        fragmentFaqQuestionBinding = DataBindingUtil.inflate(inflater, R.layout.fragment_faq_question,container,false);

        return fragmentFaqQuestionBinding.getRoot();

    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        fragmentFaqQuestionBinding.etSearch.setOnEditorActionListener(new TextView.OnEditorActionListener() {
            @Override
            public boolean onEditorAction(TextView v, int actionId, KeyEvent event) {
                if (actionId == EditorInfo.IME_ACTION_DONE) {
                    InputMethodManager imm = (InputMethodManager) v.getContext().getSystemService(Context.INPUT_METHOD_SERVICE);
                    imm.hideSoftInputFromWindow(v.getWindowToken(), 0);
                    callFAQApi();
                }
                return false;
            }
        });
        callFAQApi();

    }

    private void callFAQApi() {
        FAQRequestModel faqRequestModel = new FAQRequestModel();
        faqRequestModel.setLanguageID("1");
        faqRequestModel.setUserType(Constants.USER_TYPE);
        faqRequestModel.setSearchValue( fragmentFaqQuestionBinding.etSearch.getText().toString());
        Call objectCall = RestClient.getApiService().getFaqs(faqRequestModel);
        RestClient.makeApiRequest(getActivity(), objectCall, this, 1, true);

    }

    @Override
    public void onApiResponse(Call<Object> call, Object response, int reqCode) throws Exception {
        FAQResponseModel faqResponseModel = (FAQResponseModel) response;
        // {"ResponseCode":"500","Descriptions":"Error in API.Please try in some time."}
        if(faqResponseModel.getResponseCode().equalsIgnoreCase("500")){
            Utils.showToast(getActivity(),faqResponseModel.getDescriptions());
        }
        else if(faqResponseModel.getResponseCode().equalsIgnoreCase("200")) {
            List<FAQResponseModel.Faq> headerList = faqResponseModel.getFaq();
            ExpandableListViewAdapter expandableListViewAdapter = new ExpandableListViewAdapter(getActivity(), headerList);
            fragmentFaqQuestionBinding.expandableListview.setAdapter(expandableListViewAdapter);
        }
    }

    @Override
    public void onApiError(Call<Object> call, Object response, int reqCode) throws Exception {

    }
}
