package com.demo.home.profile;

import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.databinding.DataBindingUtil;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.RecyclerView;

import com.demo.R;
import com.demo.databinding.ItemFavSpecialistsBinding;
import com.demo.databinding.ItemMyDemoTripsBinding;
import com.demo.home.HomeActivity;
import com.demo.home.booking.BookingFeedbackFragment;
import com.demo.home.profile.model.DemoTripResponseModel;
import com.demo.home.profile.model.FavoritespecialistModel;
import com.demo.utils.Constants;
import com.google.android.material.bottomsheet.BottomSheetDialog;

import java.util.ArrayList;

public class FavouriteSpecialistsListAdapter extends RecyclerView.Adapter<FavouriteSpecialistsListAdapter.ViewHolder> {

    private final Fragment context;
    private Context con;
    private ArrayList<FavoritespecialistModel.Favoritespecialist> dataModelList = new ArrayList<>();


    /**
     * Provide a reference to the type of views that you are using
     * (custom ViewHolder).
     */
    public static class ViewHolder extends RecyclerView.ViewHolder {
        public ItemFavSpecialistsBinding itemRowBinding;

        public ViewHolder(ItemFavSpecialistsBinding itemRowBinding) {
            super(itemRowBinding.getRoot());
            this.itemRowBinding = itemRowBinding;
        }

        public void bind(FavoritespecialistModel.Favoritespecialist obj) {
            itemRowBinding.setFavouriteSpecialists(obj);
            itemRowBinding.executePendingBindings();
        }
    }

    public FavouriteSpecialistsListAdapter(Context con,FavouriteSpecialistFragment context, ArrayList<FavoritespecialistModel.Favoritespecialist> carDealerModelsList) {
       this.con = con;
        this.context = context;
        dataModelList = carDealerModelsList;
    }

    // Create new views (invoked by the layout manager)
    @Override
    public FavouriteSpecialistsListAdapter.ViewHolder onCreateViewHolder(ViewGroup viewGroup, int viewType) {
        // Create a new view, which defines the UI of the list item
        LayoutInflater inflater = (LayoutInflater)con.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        ItemFavSpecialistsBinding itemSearchListBinding = DataBindingUtil.inflate(inflater, R.layout.item_fav_specialists,viewGroup,false);

        return new ViewHolder(itemSearchListBinding);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        FavoritespecialistModel.Favoritespecialist dataModel = dataModelList.get(position);
        holder.bind(dataModel);
        holder.itemRowBinding.leaveFeedback.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ((FavouriteSpecialistFragment)context).showFragment(new SpecialistFeedbackFragment(dataModel.getSpecialistName(),dataModel.getSpecialistID()));
            }
        });
        holder.itemRowBinding.contact.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(Intent.ACTION_DIAL);
                intent.setData(Uri.parse("tel:"+dataModel.getSpecialistMobile()));
                context.startActivity(intent);
            }
        });

    }




    // Return the size of your dataset (invoked by the layout manager)
    @Override
    public int getItemCount() {
        return null != dataModelList ? dataModelList.size() : 0;

    }
}

