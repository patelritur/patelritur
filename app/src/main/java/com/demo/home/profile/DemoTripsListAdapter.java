package com.demo.home.profile;

import android.Manifest;
import android.app.Dialog;
import android.content.Context;
import android.content.pm.PackageManager;
import android.location.Location;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;

import androidx.annotation.NonNull;
import androidx.core.app.ActivityCompat;
import androidx.databinding.DataBindingUtil;
import androidx.databinding.ViewDataBinding;
import androidx.fragment.app.FragmentManager;
import androidx.recyclerview.widget.RecyclerView;

import com.demo.R;
import com.demo.databinding.DialogMytripsBinding;
import com.demo.databinding.ItemMyDemoTripsBinding;
import com.demo.home.profile.model.DemoTripResponseModel;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.LatLng;

import java.util.ArrayList;

public class DemoTripsListAdapter extends RecyclerView.Adapter<DemoTripsListAdapter.ViewHolder> implements OnMapReadyCallback {

    private final Context context;
    private ArrayList<DemoTripResponseModel.Demotrip> dataModelList = new ArrayList<>();
    private FragmentManager fragmentManager;
    private DialogMytripsBinding binding;

    @Override
    public void onMapReady(@NonNull GoogleMap googleMap) {
        googleMap.getUiSettings().setMyLocationButtonEnabled(true);
        if (ActivityCompat.checkSelfPermission(context, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(context, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            return;
        }
        googleMap.setMyLocationEnabled(true);


    }


    /**
     * Provide a reference to the type of views that you are using
     * (custom ViewHolder).
     */
    public static class ViewHolder extends RecyclerView.ViewHolder {
        public ItemMyDemoTripsBinding itemRowBinding;

        public ViewHolder(ItemMyDemoTripsBinding itemRowBinding) {
            super(itemRowBinding.getRoot());
            this.itemRowBinding = itemRowBinding;
        }

        public void bind(DemoTripResponseModel.Demotrip obj) {
            itemRowBinding.setDemoTrips(obj);
            itemRowBinding.executePendingBindings();
        }
    }

    public DemoTripsListAdapter(Context context, ArrayList<DemoTripResponseModel.Demotrip> carDealerModelsList, FragmentManager childFragmentManager) {
        this.context = context;
        dataModelList = carDealerModelsList;
        fragmentManager = childFragmentManager;
    }

    // Create new views (invoked by the layout manager)
    @Override
    public DemoTripsListAdapter.ViewHolder onCreateViewHolder(ViewGroup viewGroup, int viewType) {
        // Create a new view, which defines the UI of the list item
        LayoutInflater inflater = (LayoutInflater)context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        ItemMyDemoTripsBinding itemSearchListBinding = DataBindingUtil.inflate(inflater, R.layout.item_my_demo_trips,viewGroup,false);

        return new ViewHolder(itemSearchListBinding);
    }
    @NonNull
    private Dialog getDialog(DemoTripResponseModel.Demotrip dataModel) {
        Dialog dialog = new Dialog(context);
        if(binding!=null)
            binding=null;
        binding = DataBindingUtil.inflate(LayoutInflater.from(context), R.layout. dialog_mytrips, null, false);

        dialog.setContentView(binding.getRoot());
        binding.setDemoTrips(dataModel);
        binding.map.onCreate(null);
        binding.map.onResume();  //Probably U r missing this
        // Set the map ready callback to receive the GoogleMap object
        binding.map.getMapAsync(this);
        dialog.getWindow().setLayout(WindowManager.LayoutParams.MATCH_PARENT, WindowManager.LayoutParams.WRAP_CONTENT);
        binding.close.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dialog.dismiss();


            }
        });
        WindowManager.LayoutParams lp = dialog.getWindow().getAttributes();
        dialog.getWindow().setBackgroundDrawableResource(android.R.color.transparent);
        lp.dimAmount = 0.8f;
        dialog.getWindow().addFlags(WindowManager.LayoutParams.FLAG_DIM_BEHIND);

        return dialog;
    }


    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        DemoTripResponseModel.Demotrip dataModel = dataModelList.get(position);
        holder.bind(dataModel);
        holder.itemRowBinding.parentLl.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                getDialog(dataModel).show();
            }
        });

    }



    // Return the size of your dataset (invoked by the layout manager)
    @Override
    public int getItemCount() {
        return null != dataModelList ? dataModelList.size() : 0;

    }
}

