package com.demo.home;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.databinding.DataBindingUtil;
import androidx.recyclerview.widget.RecyclerView;

import com.demo.R;
import com.demo.databinding.ItemTopDealersBinding;
import com.demo.home.model.CarDealerModel;
import com.demo.home.model.CarDealerResponseModel;
import com.demo.utils.PrintLog;

import java.util.ArrayList;

public class CarDealerAdapter extends RecyclerView.Adapter<CarDealerAdapter.ViewHolder> {

    private final Context context;
    private ArrayList<CarDealerResponseModel.Demospecialist> dataModelList = new ArrayList<>();

    /**
     * Provide a reference to the type of views that you are using
     * (custom ViewHolder).
     */
    public static class ViewHolder extends RecyclerView.ViewHolder {
        public ItemTopDealersBinding itemRowBinding;

        public ViewHolder(ItemTopDealersBinding itemRowBinding) {
            super(itemRowBinding.getRoot());
            this.itemRowBinding = itemRowBinding;
        }

        public void bind(CarDealerResponseModel.Demospecialist obj) {
            itemRowBinding.setCarDealerModel(obj);
            itemRowBinding.executePendingBindings();
        }
    }

    public CarDealerAdapter(Context context,ArrayList<CarDealerResponseModel.Demospecialist> carDealerModelsList) {
        this.context = context;
        dataModelList = carDealerModelsList;
    }

    // Create new views (invoked by the layout manager)
    @Override
    public CarDealerAdapter.ViewHolder onCreateViewHolder(ViewGroup viewGroup, int viewType) {
        // Create a new view, which defines the UI of the list item
        LayoutInflater inflater = (LayoutInflater)context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        ItemTopDealersBinding itemTopDealersBinding = DataBindingUtil.inflate(inflater, R.layout.item_top_dealers,viewGroup,false);

        return new ViewHolder(itemTopDealersBinding);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        CarDealerResponseModel.Demospecialist dataModel = dataModelList.get(position);
        holder.bind(dataModel);
    }



    // Return the size of your dataset (invoked by the layout manager)
    @Override
    public int getItemCount() {
        return dataModelList.size();

    }
}

