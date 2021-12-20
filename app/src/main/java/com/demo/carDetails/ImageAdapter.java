package com.demo.carDetails;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;

import androidx.viewpager.widget.PagerAdapter;

import com.bumptech.glide.Glide;
import com.demo.R;
import com.demo.carDetails.model.CarDetailResponse;

import java.util.ArrayList;

public class ImageAdapter extends PagerAdapter {
    Context context;

    LayoutInflater mLayoutInflater;
    ArrayList<CarDetailResponse.colorlist> colorlist;

    ImageAdapter(Context context, ArrayList<CarDetailResponse.colorlist> colorlists){
        this.context=context;
        mLayoutInflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        colorlist = colorlists;
    }
    @Override
    public int getCount() {
        return colorlist.size();
    }

    @Override
    public boolean isViewFromObject(View view, Object object) {
        return view == ((LinearLayout) object);
    }

    @Override
    public Object instantiateItem(ViewGroup container, int position) {
        View itemView = mLayoutInflater.inflate(R.layout.item_color_imagview, container, false);

        ImageView imageView = (ImageView) itemView.findViewById(R.id.imageView);
        Glide.with(context)
                .load(colorlist.get(position).getColorImage())
                .into(imageView);

        container.addView(itemView);

        return itemView;
    }

    @Override
    public void destroyItem(ViewGroup container, int position, Object object) {
        container.removeView((LinearLayout)object);
    }
}
