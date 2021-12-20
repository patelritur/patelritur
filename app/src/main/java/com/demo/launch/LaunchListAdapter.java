package com.demo.launch;

import android.app.Activity;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.databinding.DataBindingUtil;
import androidx.fragment.app.FragmentManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.viewpager.widget.ViewPager;

import com.demo.R;
import com.demo.carDetails.ScreenSlidePageFragment;
import com.demo.carDetails.ScreenSlidePagerAdapter;
import com.demo.carDetails.model.CarDetailResponse;
import com.demo.databinding.ItemLaunchBinding;
import com.demo.utils.PrintLog;
import com.google.android.material.bottomsheet.BottomSheetDialog;

import java.util.ArrayList;
import java.util.List;

import com.demo.launch.model.LaunchResponseModel;

public class LaunchListAdapter extends RecyclerView.Adapter<LaunchListAdapter.ViewHolder> {

    private final Context context;
    private ArrayList<LaunchResponseModel.Latestlaunchlist> dataModelList = new ArrayList<>();
    private FragmentManager fragmentManager;
    private int parentPosition;


    /**
     * Provide a reference to the type of views that you are using
     * (custom ViewHolder).
     */
    public static class ViewHolder extends RecyclerView.ViewHolder {
        public ItemLaunchBinding itemRowBinding;

        public ViewHolder(ItemLaunchBinding itemRowBinding) {
            super(itemRowBinding.getRoot());
            this.itemRowBinding = itemRowBinding;
        }

        public void bind(LaunchResponseModel.Latestlaunchlist obj) {
            itemRowBinding.setLaunchModel(obj);
            itemRowBinding.executePendingBindings();
        }
    }

    public LaunchListAdapter(Context context, int position, FragmentManager childFragmentManager, ArrayList<LaunchResponseModel.Latestlaunchlist> carDealerModelsList) {
        this.context = context;
        dataModelList = carDealerModelsList;
        fragmentManager = childFragmentManager;
        parentPosition = position;
    }

    // Create new views (invoked by the layout manager)
    @Override
    public ViewHolder onCreateViewHolder(ViewGroup viewGroup, int viewType) {
        // Create a new view, which defines the UI of the list item
        LayoutInflater inflater = (LayoutInflater)context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        ItemLaunchBinding itemSearchListBinding = DataBindingUtil.inflate(inflater, R.layout.item_launch,viewGroup,false);

        return new ViewHolder(itemSearchListBinding);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        LaunchResponseModel.Latestlaunchlist dataModel = dataModelList.get(position);
        holder.bind(dataModel);

        setUpViewPager(dataModel,holder);
        if(parentPosition==0){
            holder.itemRowBinding.bookDemo.setText("Prebook Demo");
        }
        else
            holder.itemRowBinding.bookDemo.setText("BOOK A DEMO");

        holder.itemRowBinding.bookDemo.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

            }
        });

    }
    private void setUpViewPager(LaunchResponseModel.Latestlaunchlist carDetailResponse, ViewHolder holder) {
        ScreenSlidePagerAdapter screenSlidePagerAdapter = new ScreenSlidePagerAdapter(fragmentManager);
        screenSlidePagerAdapter.count =carDetailResponse.getBannerlist().size();
        screenSlidePagerAdapter.setBannerlist(carDetailResponse);
        holder.itemRowBinding.pager.setAdapter(screenSlidePagerAdapter);
        holder.itemRowBinding.pager.setOffscreenPageLimit(carDetailResponse.getBannerlist().size());
        holder.itemRowBinding.intoTabLayout.setupWithViewPager(holder.itemRowBinding.pager);
        holder.itemRowBinding.pager.addOnPageChangeListener(new ViewPager.OnPageChangeListener() {
            @Override
            public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {

            }

            @Override
            public void onPageSelected(int position) {
                ((ScreenSlidePageFragment) screenSlidePagerAdapter.getFragment(position)).updateView(carDetailResponse.getBannerlist().get(position).getBannerType());
            }

            @Override
            public void onPageScrollStateChanged(int state) {

            }
        });

    }


    // Return the size of your dataset (invoked by the layout manager)
    @Override
    public int getItemCount() {
        return null != dataModelList ? dataModelList.size() : 0;

    }
}

