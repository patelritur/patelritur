package com.demo.home;

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
import com.demo.databinding.ItemSearchListBinding;
import com.demo.databinding.ItemTopDealersBinding;
import com.demo.home.model.CarDealerModel;
import com.demo.home.model.CarSearchResultModel;
import com.google.android.material.bottomsheet.BottomSheetDialog;

import java.util.ArrayList;

public class CarSearchResultAdapter extends RecyclerView.Adapter<CarSearchResultAdapter.ViewHolder> {

    private final Context context;
    private ArrayList<CarSearchResultModel.Carlist> dataModelList = new ArrayList<>();
    private BottomSheetDialog bottomSheetDialog;

    /**
     * Provide a reference to the type of views that you are using
     * (custom ViewHolder).
     */
    public static class ViewHolder extends RecyclerView.ViewHolder {
        public ItemSearchListBinding itemRowBinding;

        public ViewHolder(ItemSearchListBinding itemRowBinding) {
            super(itemRowBinding.getRoot());
            this.itemRowBinding = itemRowBinding;
        }

        public void bind(CarSearchResultModel.Carlist obj) {
            itemRowBinding.setCarSearchResultModel(obj);
            itemRowBinding.executePendingBindings();
        }
    }

    public CarSearchResultAdapter(Context context, ArrayList<CarSearchResultModel.Carlist> carDealerModelsList, BottomSheetDialog bottomSheetDialog) {
        this.context = context;
        dataModelList = carDealerModelsList;
        this.bottomSheetDialog = bottomSheetDialog;
    }

    // Create new views (invoked by the layout manager)
    @Override
    public CarSearchResultAdapter.ViewHolder onCreateViewHolder(ViewGroup viewGroup, int viewType) {
        // Create a new view, which defines the UI of the list item
        LayoutInflater inflater = (LayoutInflater)context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        ItemSearchListBinding itemSearchListBinding = DataBindingUtil.inflate(inflater, R.layout.item_search_list,viewGroup,false);

        return new ViewHolder(itemSearchListBinding);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        CarSearchResultModel.Carlist dataModel = dataModelList.get(position);
        holder.bind(dataModel);
        holder.itemRowBinding.ll.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(context, CarDetailsActivity.class);
                intent.putExtra("carId",dataModel.getCarID());
                intent.putExtra("specialistId","0");

                ((HomeActivity)context).startActivity(intent);
                ((HomeActivity)context).finish();
                bottomSheetDialog.dismiss();
            }
        });
    }



    // Return the size of your dataset (invoked by the layout manager)
    @Override
    public int getItemCount() {
        return null != dataModelList ? dataModelList.size() : 0;

    }
}

