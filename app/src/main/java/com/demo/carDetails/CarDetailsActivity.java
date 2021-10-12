package com.demo.carDetails;

import android.app.Dialog;
import android.content.ActivityNotFoundException;
import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.text.Html;
import android.view.LayoutInflater;
import android.view.View;
import android.view.WindowManager;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.widget.AppCompatTextView;
import androidx.databinding.DataBindingUtil;
import androidx.databinding.ViewDataBinding;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentActivity;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentStatePagerAdapter;
import androidx.recyclerview.widget.DividerItemDecoration;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.viewpager.widget.PagerAdapter;
import androidx.viewpager2.adapter.FragmentStateAdapter;

import com.demo.BaseActivity;
import com.demo.R;
import com.demo.carDetails.model.CarDetailRequest;
import com.demo.carDetails.model.CarDetailResponse;
import com.demo.carDetails.model.CarDetailReviewModel;
import com.demo.carDetails.model.CarDetailsSpecificationModel;
import com.demo.carDetails.model.CarPorfomaInvoiceModel;
import com.demo.databinding.ActivityCarDetailsBinding;
import com.demo.databinding.DialogReviewsBinding;
import com.demo.databinding.ItemImageviewBannerBinding;
import com.demo.databinding.ItemImageviewBinding;
import com.demo.databinding.ItemSuggestionCarsBinding;
import com.demo.home.CarDealerAdapter;
import com.demo.home.PersonalisedCarOptionsFragment;
import com.demo.home.model.CarDealerResponseModel;
import com.demo.tutorial.TutorialPagerAdapter;
import com.demo.utils.Constants;
import com.demo.utils.SharedPrefUtils;
import com.demo.webservice.ApiResponseListener;
import com.demo.webservice.RestClient;

import java.util.ArrayList;

import retrofit2.Call;

public class CarDetailsActivity  extends BaseActivity implements ApiResponseListener{


    private ActivityCarDetailsBinding activityCarDetailsBinding;
    private String carId;
    private CarDetailResponse carDetailResponse;
    private CarDetailRequest carDetailRequest;
    private DialogReviewsBinding binding;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        activityCarDetailsBinding = DataBindingUtil.setContentView(this, R.layout.activity_car_details,null);
        carId = getIntent().getExtras().getString("carId");
        callDetailApi();
    }

    private void callDetailApi() {
        carDetailRequest = new CarDetailRequest();
        carDetailRequest.setUserID(new SharedPrefUtils(this).getStringData(Constants.USER_ID));
        carDetailRequest.setLatitude(String.valueOf(Constants.LATITUDE));
        carDetailRequest.setLongitude(String.valueOf(Constants.LONGITUDE));
        carDetailRequest.setCarID(carId);
        Call objectCall = RestClient.getApiService().getCarDetail(carDetailRequest);
        RestClient.makeApiRequest(this, objectCall, this, 1, true);


    }

    private void setUpViewPager(CarDetailResponse carDetailResponse) {
        ScreenSlidePagerAdapter screenSlidePagerAdapter = new ScreenSlidePagerAdapter(getSupportFragmentManager());
        screenSlidePagerAdapter.count = carDetailResponse.getCardetail().carbanner.size();
        screenSlidePagerAdapter.setCarDetailResponse(carDetailResponse);
        activityCarDetailsBinding.viewpager.setAdapter(screenSlidePagerAdapter);
        activityCarDetailsBinding.viewpager.setOffscreenPageLimit(2);
        activityCarDetailsBinding.intoTabLayout.setupWithViewPager(activityCarDetailsBinding.viewpager);
    }

    private void setUpBannerView(CarDetailResponse carDetailResponse) {
        activityCarDetailsBinding.horizontalScrollviewPromotion.removeAllViews();
        for(int i=0;i<carDetailResponse.getCardetail().getCarofferbanner().size();i++)
        {
            LayoutInflater inflater = (LayoutInflater)this.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            CarDetailResponse.Carofferbanner  carofferbanner= carDetailResponse.getCardetail().getCarofferbanner().get(i);
            ItemImageviewBannerBinding itemImageviewBannerBinding = DataBindingUtil.inflate(inflater,R.layout.item_imageview_banner,null,false);
            itemImageviewBannerBinding.setImageUrl(carofferbanner.getBannerImage());
            itemImageviewBannerBinding.getRoot().setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if(carofferbanner.getBannerUrl().trim().length()>0)
                    {
                        String url = carofferbanner.getBannerUrl();
                        Intent i = new Intent(Intent.ACTION_VIEW, Uri.parse(url));
                        i.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                        i.setPackage("com.android.chrome");
                        try {
                            startActivity(i);
                        } catch (ActivityNotFoundException e) {
                            // Chrome is probably not installed
                            // Try with the default browser
                            i.setPackage(null);
                            startActivity(i);
                        }
                    }
                }
            });
            activityCarDetailsBinding.horizontalScrollviewPromotion.addView(itemImageviewBannerBinding.getRoot());

        }

    }

    private void setUpAwardsView(CarDetailResponse carDetailResponse) {
        activityCarDetailsBinding.horizontalScrollviewAward.removeAllViews();
        for(int i=0;i<carDetailResponse.getCardetail().getCarofferbanner().size();i++)
        {
            LayoutInflater inflater = (LayoutInflater)this.getSystemService(Context.LAYOUT_INFLATER_SERVICE);

            ItemImageviewBannerBinding itemSuggestionCarsBinding = DataBindingUtil.inflate(inflater,R.layout.item_imageview_banner,null,false);
            itemSuggestionCarsBinding.setImageUrl(carDetailResponse.getCardetail().getCarawardbanner().get(i).getBannerImage());

            activityCarDetailsBinding.horizontalScrollviewAward.addView(itemSuggestionCarsBinding.getRoot());

        }

    }


    public void callReviewApi() {
        Call objectCall = RestClient.getApiService().getCarDetailReviews(carDetailRequest);
        RestClient.makeApiRequest(this, objectCall, this, 2, true);

    }


    private void callSpecificationApi() {
        Call objectCall = RestClient.getApiService().getCarDetailSpeciification(carDetailRequest);
        RestClient.makeApiRequest(this, objectCall, this, 3, true);


    }

    private void callPorfomaInvoiceApi() {
        Call objectCall = RestClient.getApiService().getCarPorfomainvoice(carDetailRequest);
        RestClient.makeApiRequest(this, objectCall, this, 4, true);


    }


    @Override
    public void onApiResponse(Call<Object> call, Object response, int reqCode) throws Exception {
        if(reqCode==1) {
            carDetailResponse = (CarDetailResponse) response;
            carDetailResponse.setClickHandlers(this);
            activityCarDetailsBinding.setCarDetailModel(carDetailResponse);
            setUpViewPager(carDetailResponse);

            activityCarDetailsBinding.ratingbar.setRating(Float.parseFloat(carDetailResponse.getCardetail().getCarRateing()));
            setUpBannerView(carDetailResponse);
            setUpAwardsView(carDetailResponse);
        }
        else if(reqCode==2)
        {
            CarDetailReviewModel carDetailReviewModel = (CarDetailReviewModel) response;
            showReviewDialog(carDetailReviewModel);
        }
        else if(reqCode==3)
        {
            CarDetailsSpecificationModel carDetailReviewModel = (CarDetailsSpecificationModel) response;
            showSpecificationDialog(carDetailReviewModel);
        }
        else if(reqCode==4)
        {
            CarPorfomaInvoiceModel carDetailReviewModel = (CarPorfomaInvoiceModel) response;
            showPorfomaInvoiceDialog(carDetailReviewModel);
        }

    }

    private void showPorfomaInvoiceDialog(CarPorfomaInvoiceModel carPorfomaInvoiceModel)
    {
        Dialog dialog = getDialog();
        binding.textviewTitle.setText(getString(R.string.porfoma_invoice));
        binding.setCarInvoice(carPorfomaInvoiceModel.getPorfomainvoice());
        binding.layoutInvoice.getRoot().setVisibility(View.VISIBLE);
        binding.recyclerviewReview.setVisibility(View.GONE);
        binding.executePendingBindings();
        dialog.show();
    }
    private void showReviewDialog(CarDetailReviewModel carDetailReviewModel) {
        Dialog dialog = getDialog();
        binding.textviewTitle.setText(getString(R.string.reviews));
        binding.layoutInvoice.getRoot().setVisibility(View.GONE);

        binding.recyclerviewReview.setLayoutManager(new LinearLayoutManager(this));
        binding.recyclerviewReview.setAdapter(new CarDetailsReviewAdapter(CarDetailsActivity.this, (ArrayList<CarDetailReviewModel.Carreview>) carDetailReviewModel.getCarreview()));
        dialog.show();
    }

    @NonNull
    private void showSpecificationDialog(CarDetailsSpecificationModel carDetailReviewModel) {
        Dialog dialog = getDialog();
        binding.textviewTitle.setText(getString(R.string.specifications));
        binding.layoutInvoice.getRoot().setVisibility(View.GONE);
        binding.recyclerviewReview.setLayoutManager(new GridLayoutManager(this,2));
        binding.recyclerviewReview.setAdapter(new CarDetailsSpecificationsAdapter(CarDetailsActivity.this, (ArrayList<CarDetailsSpecificationModel.Carspecification>) carDetailReviewModel.getCarspecification()));
        dialog.show();
    }

    @NonNull
    private Dialog getDialog() {
        Dialog dialog = new Dialog(this);
        binding = DataBindingUtil.inflate(LayoutInflater.from(this), R.layout. dialog_reviews, null, false);
        dialog.setContentView(binding.getRoot());
        dialog.getWindow().setLayout(WindowManager.LayoutParams.MATCH_PARENT, WindowManager.LayoutParams.WRAP_CONTENT);
        binding.close.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dialog.dismiss();
            }
        });
        WindowManager.LayoutParams lp = dialog.getWindow().getAttributes();
        dialog.getWindow().setBackgroundDrawableResource(android.R.color.transparent);
        lp.dimAmount = 0.8f;
        dialog.getWindow().addFlags(WindowManager.LayoutParams.FLAG_DIM_BEHIND);

        return dialog;
    }

    @Override
    public void onApiError(Call<Object> call, Object response, int reqCode) throws Exception {

    }





    @Override
    public void onClick(View view) {
        switch(view.getId())
        {
            case R.id.reviews:
                callReviewApi();
                break;
            case R.id.specifications:
                callSpecificationApi();
                break;
            case R.id.invoice:
                callPorfomaInvoiceApi();
                break;
            case R.id.features:
                activityCarDetailsBinding.featureColors.setText(Html.fromHtml(carDetailResponse.getCardetail().getCarFeatures()));
                break;
            case R.id.colors:
                activityCarDetailsBinding.featureColors.setText(carDetailResponse.getCardetail().getCarColors());
                break;

        }

    }

}
