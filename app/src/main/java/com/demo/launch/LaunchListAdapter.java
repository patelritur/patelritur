package com.demo.launch;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.databinding.DataBindingUtil;
import androidx.fragment.app.FragmentManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.viewpager.widget.ViewPager;

import com.demo.R;
import com.demo.carDetails.ScreenSlidePageFragment;
import com.demo.carDetails.ScreenSlidePageVideoFragment;
import com.demo.carDetails.ScreenSlidePagerAdapter;
import com.demo.databinding.ItemLaunchBinding;
import com.demo.home.HomeActivity;
import com.demo.launch.model.LaunchResponseModel;
import com.demo.utils.Constants;
import com.demo.utils.Utils;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;

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

        if (Utils.DateAfter(dataModel.preBookingStartDate)) {
            holder.itemRowBinding.bookDemo.setText("Prebook A Demo");
            holder.itemRowBinding.bookMeeting.setText("Prebook A Meeting");
        }
        else {
            holder.itemRowBinding.bookDemo.setText("Book A Demo");
            holder.itemRowBinding.bookMeeting.setText("Book A Meeting");
        }

        holder.itemRowBinding.bookMeeting.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                {
                    Constants.BOOK_TYPE="Meeting";
                    startBooking(dataModel);

                }

            }
        });

        holder.itemRowBinding.bookDemo.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                {
                    Constants.BOOK_TYPE="Demo";
                    startBooking(dataModel);

                }

            }
        });

    }

    private void startBooking(LaunchResponseModel.Latestlaunchlist dataModel) {
        Constants.CARID = dataModel.carID;
        Intent intent = new Intent(context, HomeActivity.class);
        intent.putExtra("comeFrom", "launch");
        intent.putExtra("bookdate", parseDateToddMMyyyy(dataModel.preBookingStartDate));
        context.startActivity(intent);
    }

    private void setUpViewPager(LaunchResponseModel.Latestlaunchlist carDetailResponse, ViewHolder holder) {
        ScreenSlidePagerAdapter screenSlidePagerAdapter = new ScreenSlidePagerAdapter(fragmentManager);
        screenSlidePagerAdapter.count =carDetailResponse.getBannerlist().size();
        screenSlidePagerAdapter.setBannerlist(carDetailResponse);
        holder.itemRowBinding.pager.setAdapter(screenSlidePagerAdapter);
        holder.itemRowBinding.pager.setOffscreenPageLimit(1);
        holder.itemRowBinding.intoTabLayout.setupWithViewPager(holder.itemRowBinding.pager);
        holder.itemRowBinding.pager.addOnPageChangeListener(new ViewPager.OnPageChangeListener() {
            @Override
            public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {

            }

            @Override
            public void onPageSelected(int position) {
                ((ScreenSlidePageVideoFragment) screenSlidePagerAdapter.getFragment()).updateView(carDetailResponse.getBannerlist().get(position).getBanner(),carDetailResponse.getBannerlist().get(position).getBannerType());
            }

            @Override
            public void onPageScrollStateChanged(int state) {

            }
        });

    }


    public String parseDateToddMMyyyy(String time) {
        String inputPattern = "MM/dd/yyyy hh:mm:ss a";
        String outputPattern = "dd/MM/yyyy hh:mm:ss a";
        SimpleDateFormat inputFormat = new SimpleDateFormat(inputPattern);
        SimpleDateFormat outputFormat = new SimpleDateFormat(outputPattern);

        Date date = null;
        String str = null;

        try {
            date = inputFormat.parse(time);
            str = outputFormat.format(date);
        } catch (ParseException e) {
            e.printStackTrace();
        }
        return str;
    }

    // Return the size of your dataset (invoked by the layout manager)
    @Override
    public int getItemCount() {
        return null != dataModelList ? dataModelList.size() : 0;

    }
}

