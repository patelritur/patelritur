package com.demo.carDetails;

import android.content.ActivityNotFoundException;
import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import androidx.viewpager.widget.PagerAdapter;
import androidx.viewpager.widget.ViewPager;

import com.bumptech.glide.Glide;
import com.demo.carDetails.model.CarDetailResponse;
import com.github.chrisbanes.photoview.PhotoView;

import java.util.List;

public class MyPageAdapter extends PagerAdapter {
   private Context context;
    private List<CarDetailResponse.Carofferbanner> carofferbanner;
    private int position;
    MyPageAdapter(Context context, List<CarDetailResponse.Carofferbanner> carofferbanner,int position){
        this.context=context;
        this.carofferbanner = carofferbanner;
        this.position = position;
    }
    @Override
    public int getCount() {
        return carofferbanner.size();
    }

    @Override
    public boolean isViewFromObject(View view, Object object) {
        return view == ((ImageView) object);
    }

    @Override
    public Object instantiateItem(ViewGroup container, int position) {
        PhotoView imageView = new PhotoView(context);
        Glide.with(imageView.getContext())
                .load(carofferbanner.get(position).getBannerImage())
                .into(imageView);
        ((ViewPager) container).addView(imageView, 0);
        imageView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(carofferbanner.get(position).getBannerUrl().trim().length()>0)
                {
                    String url = carofferbanner.get(position).getBannerUrl();
                    Intent i = new Intent(Intent.ACTION_VIEW, Uri.parse(url));
                    i.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                    i.setPackage("com.android.chrome");
                    try {
                        context.startActivity(i);
                    } catch (ActivityNotFoundException e) {
                        // Chrome is probably not installed
                        // Try with the default browser
                        i.setPackage(null);
                        context.startActivity(i);
                    }
                }
            }
        });

        return imageView;
    }

    @Override
    public void destroyItem(ViewGroup container, int position, Object object) {
        ((ViewPager) container).removeView((ImageView) object);
    }
}
