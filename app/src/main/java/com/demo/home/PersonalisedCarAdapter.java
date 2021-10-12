package com.demo.home;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.GridView;

import androidx.databinding.DataBindingUtil;

import com.bumptech.glide.Glide;
import com.demo.R;
import com.demo.databinding.ItemPersonalisedCarOptionsBinding;
import com.demo.home.model.CarFilterResponse;

public class PersonalisedCarAdapter extends BaseAdapter
{
    private Context context;
    private  CarFilterResponse carFilterResponse;
    private int position;

    public PersonalisedCarAdapter(Context context,CarFilterResponse chooseOptionsList,int pos) {
        this.carFilterResponse = chooseOptionsList;
        this.context = context;
        position = pos;
    }

    @Override public int getCount()
    {
        return carFilterResponse.getPricefilter()!=null ? carFilterResponse.getPricefilter().size() : carFilterResponse.getFilter().size();
    }

    @Override public Object getItem(int i)
    {
        return null;
    }

    @Override public long getItemId(int i)
    {
        return i;
    }

    @Override
    public View getView(int position, View view, ViewGroup viewGroup)
    {
        GridView grid = (GridView)viewGroup;
        int size = grid.getColumnWidth();


        LayoutInflater inflater = (LayoutInflater)context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        ItemPersonalisedCarOptionsBinding itemPersonalisedCarOptionsBinding = DataBindingUtil.inflate(inflater,R.layout.item_personalised_car_options,viewGroup,false);
        itemPersonalisedCarOptionsBinding.getRoot().setLayoutParams(new GridView.LayoutParams(size, ViewGroup.LayoutParams.WRAP_CONTENT));
        if(this.position==0)
            itemPersonalisedCarOptionsBinding.imageviewFilterIcon.setVisibility(View.GONE);
        if(carFilterResponse.getPricefilter()!=null) {
            itemPersonalisedCarOptionsBinding.name.setText(carFilterResponse.getPricefilter().get(position).FilterName);
            itemPersonalisedCarOptionsBinding.getRoot().setTag(carFilterResponse.getPricefilter().get(position).FilterValue);
        } else{
            itemPersonalisedCarOptionsBinding.name.setText(carFilterResponse.getFilter().get(position).FilterName);
            itemPersonalisedCarOptionsBinding.getRoot().setTag(carFilterResponse.getFilter().get(position).FilterID);
            Glide.with(context)
                    .load(carFilterResponse.getFilter().get(position).getFilterIcon())
                    .into(itemPersonalisedCarOptionsBinding.imageviewFilterIcon);
        }

        switch (this.position)
        {
            case 0:
                if(((HomeActivity)context).BudgetSelectedId.contains(  carFilterResponse.getPricefilter().get(position).FilterValue)){
                    updateView(itemPersonalisedCarOptionsBinding);
                }
                break;
            case 1:
                if(((HomeActivity)context).SegmentSelectedId.contains( carFilterResponse.getFilter().get(position).FilterID)){
                    updateView((ItemPersonalisedCarOptionsBinding) itemPersonalisedCarOptionsBinding);

                }
                break;
            case 2:
                if(((HomeActivity)context).BrandSelectedId.contains( carFilterResponse.getFilter().get(position).FilterID)){
                    updateView((ItemPersonalisedCarOptionsBinding) itemPersonalisedCarOptionsBinding);

                }
                break;

        }

        return itemPersonalisedCarOptionsBinding.getRoot();
    }

    private void updateView(ItemPersonalisedCarOptionsBinding itemPersonalisedCarOptionsBinding) {
        itemPersonalisedCarOptionsBinding.llItemPersonalisedCar.setBackgroundResource(R.drawable.border_red_rounded_corner);
        itemPersonalisedCarOptionsBinding.llItemPersonalisedCar.setAlpha(1f);
    }


}
