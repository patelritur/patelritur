package com.demo.carDetails;

import android.annotation.SuppressLint;
import android.app.Dialog;
import android.app.DownloadManager;
import android.content.ActivityNotFoundException;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Color;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.WindowManager;
import android.webkit.JavascriptInterface;
import android.webkit.WebChromeClient;
import android.widget.ImageView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.widget.LinearLayoutCompat;
import androidx.databinding.DataBindingUtil;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.viewpager.widget.ViewPager;

import com.demo.BaseActivity;
import com.demo.R;
import com.demo.carDetails.model.CarDetailRequest;
import com.demo.carDetails.model.CarDetailResponse;
import com.demo.carDetails.model.CarDetailReviewModel;
import com.demo.carDetails.model.CarDetailsSpecificationModel;
import com.demo.carDetails.model.CarPorfomaInvoiceModel;
import com.demo.databinding.ActivityCarDetailsBinding;
import com.demo.databinding.DialogColorBinding;
import com.demo.databinding.DialogReviewsBinding;
import com.demo.databinding.ItemColorBinding;
import com.demo.databinding.ItemImageviewBannerBinding;
import com.demo.utils.Constants;
import com.demo.utils.DialogUtils;
import com.demo.utils.DownloadFileFromURL;
import com.demo.utils.FileDownloadReady;
import com.demo.utils.Permissionsutils;
import com.demo.utils.SharedPrefUtils;
import com.demo.utils.Utils;
import com.demo.webservice.ApiResponseListener;
import com.demo.webservice.RestClient;

import java.io.File;
import java.util.ArrayList;

import retrofit2.Call;

public class CarDetailsActivity  extends BaseActivity implements ApiResponseListener{

    private ActivityCarDetailsBinding activityCarDetailsBinding;
    private String carId;
    private CarDetailResponse carDetailResponse;
    private CarDetailRequest carDetailRequest;
    private DialogReviewsBinding binding;
    private CarPorfomaInvoiceModel carPorfomaInvoiceModel;
    private final int DETAIL_REQCODE = 1;
    private final int REVIEWS = 2;
    private final int SPECIFICATIONS = 3;
    private final int PERFORMA_INVOICE = 4;


    @SuppressLint("SetTextI18n")
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        activityCarDetailsBinding = DataBindingUtil.setContentView(this, R.layout.activity_car_details,null);
        carId = getIntent().getExtras().getString("carId");
        if(Constants.BOOK_TYPE.equalsIgnoreCase("Meeting"))
            activityCarDetailsBinding.takeademo.setText(getString(R.string.book_a_meeting));
        callDetailApi();
    }

    private void callDetailApi() {
        carDetailRequest = new CarDetailRequest();
        carDetailRequest.setUserID(new SharedPrefUtils(this).getStringData(Constants.USER_ID));
        carDetailRequest.setLatitude(String.valueOf(Constants.LATITUDE));
        carDetailRequest.setLongitude(String.valueOf(Constants.LONGITUDE));
        carDetailRequest.setCarID(carId);
        carDetailRequest.setStateName(new SharedPrefUtils(this).getStringData(Constants.STATENAME));
        Call objectCall = RestClient.getApiService().getCarDetail(carDetailRequest);
        RestClient.makeApiRequest(this, objectCall, this, DETAIL_REQCODE, true);


    }

    private void setUpViewPager(CarDetailResponse carDetailResponse) {
        ScreenSlidePagerAdapter screenSlidePagerAdapter = new ScreenSlidePagerAdapter(getSupportFragmentManager());
        screenSlidePagerAdapter.count = carDetailResponse.getCardetail().carbanner.size();
        screenSlidePagerAdapter.setCarDetailResponse(carDetailResponse);
        activityCarDetailsBinding.viewpager.setAdapter(screenSlidePagerAdapter);
        activityCarDetailsBinding.viewpager.setOffscreenPageLimit(carDetailResponse.getCardetail().carbanner.size());
        activityCarDetailsBinding.viewpager.addOnPageChangeListener(new ViewPager.OnPageChangeListener() {
            @Override
            public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {

            }

            @Override
            public void onPageSelected(int position) {
                ((ScreenSlidePageFragment) screenSlidePagerAdapter.getFragment(position)).updateView(carDetailResponse.getCardetail().getCarbanner().get(position).getBannerType());
            }

            @Override
            public void onPageScrollStateChanged(int state) {

            }
        });
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
        for(int i=0;i<carDetailResponse.getCardetail().getCarawardbanner().size();i++)
        {
            LayoutInflater inflater = (LayoutInflater)this.getSystemService(Context.LAYOUT_INFLATER_SERVICE);

            ItemImageviewBannerBinding itemSuggestionCarsBinding = DataBindingUtil.inflate(inflater,R.layout.item_imageview_banner,null,false);
            itemSuggestionCarsBinding.setImageUrl(carDetailResponse.getCardetail().getCarawardbanner().get(i).getBannerImage());
            itemSuggestionCarsBinding.awardYear.setVisibility(View.VISIBLE);
            itemSuggestionCarsBinding.awardYear.setText("Award Year: "+carDetailResponse.getCardetail().getCarawardbanner().get(i).getBannerYear());
            activityCarDetailsBinding.horizontalScrollviewAward.addView(itemSuggestionCarsBinding.getRoot());

        }

    }


    public void callReviewApi() {
        Call objectCall = RestClient.getApiService().getCarDetailReviews(carDetailRequest);
        RestClient.makeApiRequest(this, objectCall, this, REVIEWS, true);

    }


    private void callSpecificationApi() {
        Call objectCall = RestClient.getApiService().getCarDetailSpeciification(carDetailRequest);
        RestClient.makeApiRequest(this, objectCall, this, SPECIFICATIONS, true);


    }

    private void callPorfomaInvoiceApi() {
        Call objectCall = RestClient.getApiService().getCarPorfomainvoice(carDetailRequest);
        RestClient.makeApiRequest(this, objectCall, this, PERFORMA_INVOICE, true);


    }


    @Override
    public void onApiResponse(Call<Object> call, Object response, int reqCode) throws Exception {
        if(reqCode==DETAIL_REQCODE) {
            carDetailResponse = (CarDetailResponse) response;
            carDetailResponse.setClickHandlers(this);
            activityCarDetailsBinding.setCarDetailModel(carDetailResponse);
            setUpViewPager(carDetailResponse);
            activityCarDetailsBinding.ratingbar.setRating(Float.parseFloat(carDetailResponse.getCardetail().getCarRateing()));
            setUpBannerView(carDetailResponse);
            setUpAwardsView(carDetailResponse);
//            setUpFeaturesView(carDetailResponse);
        }
        else if(reqCode==REVIEWS)
        {
            CarDetailReviewModel carDetailReviewModel = (CarDetailReviewModel) response;
            showReviewDialog(carDetailReviewModel);
        }
        else if(reqCode==SPECIFICATIONS)
        {
            CarDetailsSpecificationModel carDetailReviewModel = (CarDetailsSpecificationModel) response;
            showSpecificationDialog(carDetailReviewModel);
        }
        else if(reqCode==PERFORMA_INVOICE)
        {
            CarPorfomaInvoiceModel carDetailReviewModel = (CarPorfomaInvoiceModel) response;
            showPorfomaInvoiceDialog(carDetailReviewModel);
        }

    }

    private void showFeaturesDialog() {
        Dialog dialog = getDialog();
        binding.textviewTitle.setText(carDetailResponse.getCardetail().getCarFeatureTitle());
        binding.layoutInvoice.getRoot().setVisibility(View.GONE);
        binding.recyclerviewReview.setLayoutManager(new GridLayoutManager(this,2));
        binding.recyclerviewReview.setAdapter(new CarDetailsFeaturesAdapter(CarDetailsActivity.this, (ArrayList<CarDetailResponse.Featurelist>) carDetailResponse.getCardetail().getFeaturelist()));
        dialog.show();
    }



    private void showPorfomaInvoiceDialog(CarPorfomaInvoiceModel carPorfomaInvoiceModel)
    {
        Dialog dialog = getDialog();
        binding.textviewTitle.setText(getString(R.string.porfoma_invoice));
        binding.setCarInvoice(carPorfomaInvoiceModel.getPorfomainvoice());
        this.carPorfomaInvoiceModel = carPorfomaInvoiceModel;
        binding.layoutInvoice.getRoot().setVisibility(View.VISIBLE);
        binding.recyclerviewReview.setVisibility(View.GONE);
        binding.imageviewDownload.setVisibility(View.VISIBLE);
        binding.imageviewDownload.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (!Permissionsutils.checkForStoragePermission(CarDetailsActivity.this)) {
                    Permissionsutils.askForStoragePermission(CarDetailsActivity.this);
                } else {
                    DownloadFileFromURL();
                }


            }
        });
        binding.executePendingBindings();
        dialog.show();
    }

    private void DownloadFileFromURL() {
        File file=new File(Environment.getExternalStorageDirectory()+"/DEMO/" + carPorfomaInvoiceModel.getPorfomainvoice().getCarName()+".pdf");
        Log.d("Environment", "Environment extraData=" + file.getPath());

        DownloadManager.Request request=new DownloadManager.Request(Uri.parse(carPorfomaInvoiceModel.getPorfomainvoice().downloadInvoiceURL))
                .setTitle(carPorfomaInvoiceModel.getPorfomainvoice().getCarName())
                .setDescription("Downloading")
                .setNotificationVisibility(DownloadManager.Request.VISIBILITY_VISIBLE)
                .setNotificationVisibility(DownloadManager.Request.VISIBILITY_VISIBLE_NOTIFY_COMPLETED)
                .setDestinationUri(Uri.fromFile(file))
                .setAllowedOverMetered(true)
                .setAllowedOverRoaming(true);
       DownloadManager downloadManager= (DownloadManager) this.getSystemService(DOWNLOAD_SERVICE);
      long  referenceID = downloadManager.enqueue(request);
        Utils.showToast(CarDetailsActivity.this,"Performa invoice is downloaded successfullly");

       /* new DownloadFileFromURL(carPorfomaInvoiceModel.getPorfomainvoice().downloadInvoiceURL, carPorfomaInvoiceModel.getPorfomainvoice().getCarName(), new FileDownloadReady() {
            @Override
            public void onFileDownloaded(String fileId) {
                Utils.showToast(CarDetailsActivity.this,"Performa invoice is downloaded successfullly");
            }
        }).execute();*/
    }


    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
            //resume tasks needing this permission
            DownloadFileFromURL();
        } else {

            DialogUtils.showAlertInfo(CarDetailsActivity.this, "Please accept storage permission to download invoice.");
        }
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
        binding.close.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dialog.dismiss();
            }
        });
        dialog.getWindow().setLayout(WindowManager.LayoutParams.MATCH_PARENT, WindowManager.LayoutParams.WRAP_CONTENT);

        WindowManager.LayoutParams lp = dialog.getWindow().getAttributes();
        dialog.getWindow().setBackgroundDrawableResource(android.R.color.transparent);
        lp.dimAmount = 0.8f;
        dialog.getWindow().addFlags(WindowManager.LayoutParams.FLAG_DIM_BEHIND);

        return dialog;
    }

    private void showColorDialog(){
        final int[] previousI = new int[1];
        Dialog dialog = new Dialog(this);
        DialogColorBinding binding = DataBindingUtil.inflate(LayoutInflater.from(this), R.layout. dialog_color, null, false);
        dialog.setContentView(binding.getRoot());
        ImageAdapter screenSlidePagerAdapter = new ImageAdapter(this, (ArrayList<CarDetailResponse.colorlist>) carDetailResponse.getCardetail().getColorlist());
        binding.viewpager.setAdapter(screenSlidePagerAdapter);
        binding.viewpager.setCurrentItem(0);
        previousI[0] = 0;
        binding.viewpager.addOnPageChangeListener(new ViewPager.OnPageChangeListener() {
            @Override
            public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {

            }

            @Override
            public void onPageSelected(int position) {
                binding.colorName.setText(carDetailResponse.getCardetail().getColorlist().get(position).getColor());
                if(carDetailResponse.getCardetail().getColorlist().get(position).getColor().equalsIgnoreCase("white"))
                    ((ImageView) binding.llColors.getChildAt(position).findViewById(R.id.tick)).setColorFilter(Color.rgb( 0, 0, 0));

                binding.llColors.getChildAt(position).findViewById(R.id.tick).setVisibility(View.VISIBLE);
                binding.llColors.getChildAt(previousI[0]).findViewById(R.id.tick).setVisibility(View.INVISIBLE);
                previousI[0] = position;

            }

            @Override
            public void onPageScrollStateChanged(int state) {

            }
        });

        for(int i = 0; i< carDetailResponse.getCardetail().getColorlist().size(); i++) {
            LayoutInflater inflater = (LayoutInflater)this.getSystemService(Context.LAYOUT_INFLATER_SERVICE);

            ItemColorBinding itemSuggestionCarsBinding = DataBindingUtil.inflate(inflater, R.layout.item_color,null,false);
            itemSuggestionCarsBinding.carColor.setBackgroundColor(Color.parseColor(carDetailResponse.getCardetail().getColorlist().get(i).getHexColorCode()));
            int finalI1 = i;
            if(i==0) {
                itemSuggestionCarsBinding.tick.setVisibility(View.VISIBLE);
                if (carDetailResponse.getCardetail().getColorlist().get(0).getColor().equalsIgnoreCase("white"))
                    itemSuggestionCarsBinding.tick.setColorFilter(Color.rgb( 0, 0, 0));
            }
            itemSuggestionCarsBinding.getRoot().setOnClickListener(new View.OnClickListener() {

                @Override
                public void onClick(View v) {
                    binding.viewpager.setCurrentItem(finalI1);
                }


            });


            binding.llColors.addView(itemSuggestionCarsBinding.getRoot());
        }

        binding.close.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dialog.dismiss();
            }
        });
        dialog.getWindow().setLayout(WindowManager.LayoutParams.MATCH_PARENT, WindowManager.LayoutParams.WRAP_CONTENT);
        WindowManager.LayoutParams lp = dialog.getWindow().getAttributes();
        dialog.getWindow().setBackgroundDrawableResource(android.R.color.transparent);
        lp.dimAmount = 0.8f;
        dialog.getWindow().addFlags(WindowManager.LayoutParams.FLAG_DIM_BEHIND);
        dialog.show();
    }

    @Override
    public void onApiError(Call<Object> call, Object response, int reqCode) throws Exception {

    }





    @Override
    public void onClick(View view) {
        switch(view.getId())
        {
            case R.id.textview_previous:
                if(activityCarDetailsBinding.viewpager.getCurrentItem()>0){
                    activityCarDetailsBinding.viewpager.setCurrentItem(activityCarDetailsBinding.viewpager.getCurrentItem()-1);
                }
                break;
            case R.id.textview_next:
                if(activityCarDetailsBinding.viewpager.getCurrentItem()<carDetailResponse.getCardetail().carbanner.size()-1){
                    activityCarDetailsBinding.viewpager.setCurrentItem(activityCarDetailsBinding.viewpager.getCurrentItem()+1);
                }
                break;
            case R.id.share_iv:
                Utils.shareData(this,carDetailResponse.getCardetail().getCarName(),carDetailResponse.getCardetail().getCarDescription());
                break;
            case R.id.takeademo:
                Constants.CARID = carId;
                setResult(100);
                finish();
                break;

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
                showFeaturesDialog();
                break;
            case R.id.colors:
                showColorDialog();
                break;

        }

    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        finish();
    }
}
