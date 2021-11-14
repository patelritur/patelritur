package com.demo.news;

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

import com.demo.R;
import com.demo.carDetails.ScreenSlidePagerAdapter;
import com.demo.databinding.ItemLaunchBinding;
import com.demo.databinding.ItemNewsBinding;
import com.demo.launch.model.LaunchResponseModel;

import java.util.ArrayList;

public class NewsListAdapter extends RecyclerView.Adapter<NewsListAdapter.ViewHolder> {

    private final Context context;
    private ArrayList<NewsResponseModel.Newslist> dataModelList = new ArrayList<>();
    private FragmentManager fragmentManager;
    private int parentPosition;


    /**
     * Provide a reference to the type of views that you are using
     * (custom ViewHolder).
     */
    public static class ViewHolder extends RecyclerView.ViewHolder {
        public ItemNewsBinding itemRowBinding;

        public ViewHolder(ItemNewsBinding itemRowBinding) {
            super(itemRowBinding.getRoot());
            this.itemRowBinding = itemRowBinding;
        }

        public void bind(NewsResponseModel.Newslist obj) {
            itemRowBinding.setNews(obj);
            itemRowBinding.executePendingBindings();
        }
    }

    public NewsListAdapter(Context context,  ArrayList<NewsResponseModel.Newslist> carDealerModelsList) {
        this.context = context;
        dataModelList = carDealerModelsList;
    }

    // Create new views (invoked by the layout manager)
    @Override
    public ViewHolder onCreateViewHolder(ViewGroup viewGroup, int viewType) {
        // Create a new view, which defines the UI of the list item
        LayoutInflater inflater = (LayoutInflater)context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        ItemNewsBinding itemSearchListBinding = DataBindingUtil.inflate(inflater, R.layout.item_news,viewGroup,false);

        return new ViewHolder(itemSearchListBinding);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        NewsResponseModel.Newslist dataModel = dataModelList.get(position);
        holder.bind(dataModel);
        holder.itemRowBinding.getRoot().setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent  intent = new Intent(context,NewsPagerActivity.class);
                intent.putExtra("pos",holder.getAdapterPosition());
                ((Activity)context).startActivity(intent);

            }
        });


    }



    // Return the size of your dataset (invoked by the layout manager)
    @Override
    public int getItemCount() {
        return null != dataModelList ? dataModelList.size() : 0;

    }
}

