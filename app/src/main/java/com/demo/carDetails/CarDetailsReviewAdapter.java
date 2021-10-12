package com.demo.carDetails;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.databinding.DataBindingUtil;
import androidx.recyclerview.widget.RecyclerView;

import com.demo.R;
import com.demo.carDetails.model.CarDetailReviewModel;
import com.demo.databinding.ItemCarReviewsBinding;
import com.demo.home.model.CarDealerResponseModel;

import java.util.ArrayList;

public class CarDetailsReviewAdapter extends RecyclerView.Adapter<CarDetailsReviewAdapter.ViewHolder> {

    private final Context context;
    private ArrayList<CarDetailReviewModel.Carreview> dataModelList = new ArrayList<>();

    /**
     * Provide a reference to the type of views that you are using
     * (custom ViewHolder).
     */
    public static class ViewHolder extends RecyclerView.ViewHolder {
        public ItemCarReviewsBinding itemRowBinding;

        public ViewHolder(ItemCarReviewsBinding itemRowBinding) {
            super(itemRowBinding.getRoot());
            this.itemRowBinding = itemRowBinding;
        }

        public void bind(CarDetailReviewModel.Carreview obj) {
            itemRowBinding.setCarDetailReviewModel(obj);
            itemRowBinding.executePendingBindings();
        }
    }

    public CarDetailsReviewAdapter(Context context, ArrayList<CarDetailReviewModel.Carreview> carDealerModelsList) {
        this.context = context;
        dataModelList = carDealerModelsList;
    }

    // Create new views (invoked by the layout manager)
    @Override
    public CarDetailsReviewAdapter.ViewHolder onCreateViewHolder(ViewGroup viewGroup, int viewType) {
        // Create a new view, which defines the UI of the list item
        LayoutInflater inflater = (LayoutInflater)context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        ItemCarReviewsBinding itemSearchListBinding = DataBindingUtil.inflate(inflater, R.layout.item_car_reviews,viewGroup,false);

        return new CarDetailsReviewAdapter.ViewHolder(itemSearchListBinding);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        CarDetailReviewModel.Carreview dataModel = dataModelList.get(position);
        holder.bind(dataModel);
    }





    // Return the size of your dataset (invoked by the layout manager)
    @Override
    public int getItemCount() {
        return null != dataModelList ? dataModelList.size() : 0;

    }
}


