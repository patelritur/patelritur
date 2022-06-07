package com.demo.carDetails;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.databinding.DataBindingUtil;
import androidx.recyclerview.widget.RecyclerView;

import com.demo.R;
import com.demo.carDetails.model.CarDetailsSpecificationModel;
import com.demo.databinding.ItemCarSpecificationsBinding;

import java.util.ArrayList;

public class CarDetailsSpecificationsAdapter extends RecyclerView.Adapter<CarDetailsSpecificationsAdapter.ViewHolder> {

    private  Context context;
    private ArrayList<CarDetailsSpecificationModel.Carspecification> dataModelList = new ArrayList<>();



    /**
     * Provide a reference to the type of views that you are using
     * (custom ViewHolder).
     */
    public static class ViewHolder extends RecyclerView.ViewHolder {
        public ItemCarSpecificationsBinding itemRowBinding;

        public ViewHolder(ItemCarSpecificationsBinding itemRowBinding) {
            super(itemRowBinding.getRoot());
            this.itemRowBinding = itemRowBinding;
        }

        public void bind(CarDetailsSpecificationModel.Carspecification obj) {
            itemRowBinding.setCarDetailSpecification(obj);
            itemRowBinding.executePendingBindings();
        }


    }

    public CarDetailsSpecificationsAdapter(Context context, ArrayList<CarDetailsSpecificationModel.Carspecification> carDealerModelsList) {
        this.context = context;
        dataModelList = carDealerModelsList;
    }

    // Create new views (invoked by the layout manager)
    @Override
    public CarDetailsSpecificationsAdapter.ViewHolder onCreateViewHolder(ViewGroup viewGroup, int viewType) {
        // Create a new view, which defines the UI of the list item
        LayoutInflater inflater = (LayoutInflater)context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        ItemCarSpecificationsBinding itemSearchListBinding = DataBindingUtil.inflate(inflater, R.layout.item_car_specifications,viewGroup,false);

        return new CarDetailsSpecificationsAdapter.ViewHolder(itemSearchListBinding);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        if(dataModelList!=null) {
            CarDetailsSpecificationModel.Carspecification dataModel = dataModelList.get(position);
            holder.bind(dataModel);
        }

    }





    // Return the size of your dataset (invoked by the layout manager)
    @Override
    public int getItemCount() {
        return null != dataModelList ? dataModelList.size() : 0;

    }
}


