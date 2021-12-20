package com.demo.home;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.databinding.DataBindingUtil;
import androidx.recyclerview.widget.RecyclerView;

import com.demo.R;
import com.demo.carDetails.CarDetailsActivity;
import com.demo.databinding.ItemMenuBinding;
import com.demo.databinding.ItemSearchListBinding;
import com.demo.faq.FAQActiviity;
import com.demo.home.model.AppContentModel;
import com.demo.home.model.CarSearchResultModel;
import com.demo.home.profile.MyDemoActivity;
import com.demo.launch.LaunchActivity;
import com.demo.news.NewsActivity;
import com.demo.rewards.s.RewardsActivity;
import com.demo.utils.Utils;

import java.util.ArrayList;

public class HomeMenuAdapter extends RecyclerView.Adapter<HomeMenuAdapter.ViewHolder> {

    private final Context context;
    private ArrayList<AppContentModel.Label> dataModelList = new ArrayList<>();

    /**
     * Provide a reference to the type of views that you are using
     * (custom ViewHolder).
     */
    public static class ViewHolder extends RecyclerView.ViewHolder {
        public ItemMenuBinding itemRowBinding;

        public ViewHolder(ItemMenuBinding itemRowBinding) {
            super(itemRowBinding.getRoot());
            this.itemRowBinding = itemRowBinding;
        }

        public void bind(AppContentModel.Label obj) {
            itemRowBinding.setMenu(obj);
            itemRowBinding.executePendingBindings();
        }
    }

    public HomeMenuAdapter(Context context, ArrayList<AppContentModel.Label> carDealerModelsList) {
        this.context = context;
        dataModelList = carDealerModelsList;
    }

    // Create new views (invoked by the layout manager)
    @Override
    public HomeMenuAdapter.ViewHolder onCreateViewHolder(ViewGroup viewGroup, int viewType) {
        // Create a new view, which defines the UI of the list item
        LayoutInflater inflater = (LayoutInflater)context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        ItemMenuBinding itemSearchListBinding = DataBindingUtil.inflate(inflater, R.layout.item_menu,viewGroup,false);

        return new ViewHolder(itemSearchListBinding);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        AppContentModel.Label dataModel = dataModelList.get(position);
        holder.bind(dataModel);
        holder.itemRowBinding.ll.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                switch (holder.getAdapterPosition()){
                    case 0:
                        ((Activity)context).startActivity(new Intent(context, MyDemoActivity.class));

                        break;
                    case 1:
                        ((Activity)context).startActivity(new Intent(context, MyDemoActivity.class).putExtra("comeFrom","notifications"));
                        break;
                    case 2:
                        ((Activity)context).startActivity(new Intent(context, LaunchActivity.class));
                        break;
                    case 3:
                        ((Activity)context).startActivity(new Intent(context, RewardsActivity.class));
                        break;
                    case 4:
                        ((Activity)context).startActivity(new Intent(context, NewsActivity.class));
                        break;
                    case 5:
                        ((Activity)context).startActivity(new Intent(context, FAQActiviity.class));
                        break;
                    case 6:
                        Intent intent = new Intent(context,FAQActiviity.class);
                        intent.putExtra("showFAQList",true);
                        ((Activity)context).startActivity(intent);
                        break;
                }
            }
        });
    }



    // Return the size of your dataset (invoked by the layout manager)
    @Override
    public int getItemCount() {
        return null != dataModelList ? dataModelList.size() : 0;

    }
}

