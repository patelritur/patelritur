package com.demo.home.booking;

import android.annotation.SuppressLint;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.ViewGroup;
import android.widget.RadioGroup;

import androidx.annotation.NonNull;
import androidx.databinding.DataBindingUtil;
import androidx.recyclerview.widget.RecyclerView;

import com.demo.R;
import com.demo.databinding.ItemCancelReasonBinding;
import com.demo.home.model.AppContentModel;
import com.demo.utils.PrintLog;

import java.util.ArrayList;

public class CancelReasonAdapter extends RecyclerView.Adapter<CancelReasonAdapter.ViewHolder> {

    private ArrayList<AppContentModel.Label> dataModelList = new ArrayList<>();
    private RadioGroup lastCheckedRadioGroup;
    private int selectedPos = -1;
    private CancelDemoFragment cancelDemoFragment;

    /**
     * Provide a reference to the type of views that you are using
     * (custom ViewHolder).
     */
    public static class ViewHolder extends RecyclerView.ViewHolder {
        public ItemCancelReasonBinding itemCancelReasonBinding;

        public ViewHolder(ItemCancelReasonBinding itemCancelReasonBinding) {
            super(itemCancelReasonBinding.getRoot());
            this.itemCancelReasonBinding = itemCancelReasonBinding;
        }

        public void bind(AppContentModel.Label obj) {
            itemCancelReasonBinding.setAppContentModel(obj);
            itemCancelReasonBinding.executePendingBindings();
        }
    }

    public CancelReasonAdapter(ArrayList<AppContentModel.Label> carDealerModelsList, CancelDemoFragment cancelDemoFragment) {
        lastCheckedRadioGroup=null;
        dataModelList = carDealerModelsList;
       this.cancelDemoFragment = cancelDemoFragment;
    }

    // Create new views (invoked by the layout manager)
    @Override
    public CancelReasonAdapter.ViewHolder onCreateViewHolder(ViewGroup viewGroup, int viewType) {
        // Create a new view, which defines the UI of the list item
        LayoutInflater inflater = (LayoutInflater)viewGroup.getContext().getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        ItemCancelReasonBinding itemSearchListBinding = DataBindingUtil.inflate(inflater, R.layout.item_cancel_reason,viewGroup,false);
           PrintLog.v("radioid-==");

        return new ViewHolder(itemSearchListBinding);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, @SuppressLint("RecyclerView") int position) {
        AppContentModel.Label dataModel = dataModelList.get(position);
        holder.bind(dataModel);
        holder.itemCancelReasonBinding.radio.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(RadioGroup radioGroup, int i) {
                PrintLog.v("radioid-=="+position);
                if (lastCheckedRadioGroup != null
                        && lastCheckedRadioGroup.getCheckedRadioButtonId()
                        != radioGroup.getCheckedRadioButtonId()
                        && lastCheckedRadioGroup.getCheckedRadioButtonId() != -1) {
                    lastCheckedRadioGroup.clearCheck();

                }
                lastCheckedRadioGroup = radioGroup;
                {
                    selectedPos = position;
                    cancelDemoFragment.checkforDelaerblock(getSelectedReason());
                }

            }
        });

    }



    // Return the size of your dataset (invoked by the layout manager)
    @Override
    public int getItemCount() {
        PrintLog.v("in==000==="+dataModelList.size());
        return null != dataModelList ? dataModelList.size() : 0;

    }

    @SuppressLint("ResourceType")
    public String getSelectedReason()
    {
        PrintLog.v("=="+selectedPos);
        return dataModelList.get(selectedPos).getLabelInLanguage();
    }
}

