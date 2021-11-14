package com.demo.home.booking;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.RadioGroup;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.databinding.DataBindingUtil;
import androidx.recyclerview.widget.RecyclerView;

import com.demo.R;
import com.demo.databinding.ItemCancelReasonBinding;
import com.demo.databinding.ItemMenuBinding;
import com.demo.home.model.AppContentModel;
import com.demo.utils.PrintLog;
import com.demo.utils.Utils;

import java.util.ArrayList;

public class CancelReasonAdapter extends RecyclerView.Adapter<CancelReasonAdapter.ViewHolder> {

    private final Context context;
    private ArrayList<AppContentModel.Label> dataModelList = new ArrayList<>();
    private RadioGroup lastCheckedRadioGroup = null;

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

    public CancelReasonAdapter(Context context, ArrayList<AppContentModel.Label> carDealerModelsList) {
        lastCheckedRadioGroup=null;
        this.context = context;
        dataModelList = carDealerModelsList;
    }

    // Create new views (invoked by the layout manager)
    @Override
    public CancelReasonAdapter.ViewHolder onCreateViewHolder(ViewGroup viewGroup, int viewType) {
        // Create a new view, which defines the UI of the list item
        LayoutInflater inflater = (LayoutInflater)context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        ItemCancelReasonBinding itemSearchListBinding = DataBindingUtil.inflate(inflater, R.layout.item_cancel_reason,viewGroup,false);

        itemSearchListBinding.radio.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(RadioGroup radioGroup, int i) {
                //since only one package is allowed to be selected
                //this logic clears previous selection
                //it checks state of last radiogroup and
                // clears it if it meets conditions
                if (lastCheckedRadioGroup != null
                        && lastCheckedRadioGroup.getCheckedRadioButtonId()
                        != radioGroup.getCheckedRadioButtonId()
                        && lastCheckedRadioGroup.getCheckedRadioButtonId() != -1) {
                    lastCheckedRadioGroup.clearCheck();

                    PrintLog.v("in"+lastCheckedRadioGroup.getCheckedRadioButtonId());
                }
                lastCheckedRadioGroup = radioGroup;

            }
        });
        return new ViewHolder(itemSearchListBinding);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        AppContentModel.Label dataModel = dataModelList.get(position);
        holder.bind(dataModel);

    }



    // Return the size of your dataset (invoked by the layout manager)
    @Override
    public int getItemCount() {
        return null != dataModelList ? dataModelList.size() : 0;

    }

    public String getSelectedReason()
    {
        try {
            if (lastCheckedRadioGroup != null) {
                if (lastCheckedRadioGroup.getCheckedRadioButtonId() > getItemCount()) {
                    return dataModelList.get(lastCheckedRadioGroup.getCheckedRadioButtonId() - getItemCount() - 1).LabelInLanguage;
                }

                return dataModelList.get(lastCheckedRadioGroup.getCheckedRadioButtonId() - 1).LabelInLanguage;
            }
        }
        catch (Exception e){
            e.printStackTrace();
        }
        return null;
    }
}

