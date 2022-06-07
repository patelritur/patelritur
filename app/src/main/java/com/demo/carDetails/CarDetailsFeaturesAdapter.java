package com.demo.carDetails;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.databinding.DataBindingUtil;
import androidx.recyclerview.widget.RecyclerView;

import com.demo.R;
import com.demo.carDetails.model.CarDetailResponse;
import com.demo.databinding.ItemCarFeaturesBinding;

import java.util.ArrayList;

public class CarDetailsFeaturesAdapter extends RecyclerView.Adapter<CarDetailsFeaturesAdapter.ViewHolder> {

    private  Context context;
    private ArrayList<CarDetailResponse.Featurelist> dataModelList = new ArrayList<>();



    /**
     * Provide a reference to the type of views that you are using
     * (custom ViewHolder).
     */
    public static class ViewHolder extends RecyclerView.ViewHolder {
        public ItemCarFeaturesBinding itemRowBinding;

        public ViewHolder(ItemCarFeaturesBinding itemRowBinding) {
            super(itemRowBinding.getRoot());
            this.itemRowBinding = itemRowBinding;
        }

        public void bind(CarDetailResponse.Featurelist obj) {
            itemRowBinding.setCarFeatures(obj);
            itemRowBinding.executePendingBindings();
        }


    }

    public CarDetailsFeaturesAdapter(Context context, ArrayList<CarDetailResponse.Featurelist> carDealerModelsList) {
        this.context = context;
        dataModelList = carDealerModelsList;
    }

    // Create new views (invoked by the layout manager)
    @Override
    public ViewHolder onCreateViewHolder(ViewGroup viewGroup, int viewType) {
        // Create a new view, which defines the UI of the list item
        LayoutInflater inflater = (LayoutInflater)context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        ItemCarFeaturesBinding itemSearchListBinding = DataBindingUtil.inflate(inflater, R.layout.item_car_features,viewGroup,false);

        return new ViewHolder(itemSearchListBinding);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        if(dataModelList!=null) {
            CarDetailResponse.Featurelist dataModel = dataModelList.get(position);
            holder.bind(dataModel);
        }
       
    }





    // Return the size of your dataset (invoked by the layout manager)
    @Override
    public int getItemCount() {
        return null != dataModelList ? dataModelList.size() : 0;

    }
}


