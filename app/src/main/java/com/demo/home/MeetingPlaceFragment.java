package com.demo.home;

import android.content.Context;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.widget.LinearLayoutCompat;
import androidx.databinding.DataBindingUtil;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;

import com.demo.R;
import com.demo.databinding.ItemDemoPlaceBinding;
import com.demo.databinding.ItemVirtualmeetBinding;
import com.demo.databinding.LayoutDemoPlaceBinding;
import com.demo.home.model.viewmodel.MeetingPlaceViewModel;
import com.demo.home.model.MenuResponse;
import com.demo.home.model.viewmodel.VirtualMeetingPlaceViewModel;

public class MeetingPlaceFragment extends Fragment {
    LayoutDemoPlaceBinding layoutDemoPlaceBinding;
    ViewGroup viewGroup;
    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        layoutDemoPlaceBinding = DataBindingUtil.inflate(inflater,R.layout.layout_demo_place,container,false);
        viewGroup = container;
        return layoutDemoPlaceBinding.getRoot();

    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        new ViewModelProvider(requireActivity()).get(MeetingPlaceViewModel.class).getMenuType().observe(getViewLifecycleOwner(), item -> {
            updateUIForPlace(item);
        });
    }

    private void updateUIForPlace(MenuResponse item) {
        for(int i=0;i<item.getDemomenu().size();i++)
        {
            LayoutInflater inflater = (LayoutInflater)getActivity().getSystemService(Context.LAYOUT_INFLATER_SERVICE);

            ItemDemoPlaceBinding itemDemoPlaceBinding = DataBindingUtil.inflate(inflater,R.layout.item_demo_place,viewGroup,false);
            itemDemoPlaceBinding.setMenuModel(item.getDemomenu().get(i));
            int finalI = i;
            itemDemoPlaceBinding.llBookDemo.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if(finalI ==2)
                    {
                        new ViewModelProvider(requireActivity()).get(VirtualMeetingPlaceViewModel.class).getMenuType().observe(getViewLifecycleOwner(), item -> {
                            updateUIForVirtualMeet(item);
                        });
                    }
                }
            });
            itemDemoPlaceBinding.llSchedule.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {

                }
            });
            layoutDemoPlaceBinding.llPlacetype.setOrientation(LinearLayoutCompat.VERTICAL);
            layoutDemoPlaceBinding.llPlacetype.addView(itemDemoPlaceBinding.getRoot());
        }
    }

    private void updateUIForVirtualMeet(MenuResponse item) {
        layoutDemoPlaceBinding.llPlacetype.removeAllViews();
        for(int i=0;i<item.getDemomenu().size();i++)
        {
            LayoutInflater inflater = (LayoutInflater)getActivity().getSystemService(Context.LAYOUT_INFLATER_SERVICE);

            ItemVirtualmeetBinding itemDemoPlaceBinding = DataBindingUtil.inflate(inflater,R.layout.item_virtualmeet,viewGroup,false);
            itemDemoPlaceBinding.setMenuModel(item.getDemomenu().get(i));
            layoutDemoPlaceBinding.titleText.setText(getString(R.string.virtual_meet));
            layoutDemoPlaceBinding.llPlacetype.setOrientation(LinearLayoutCompat.HORIZONTAL);
            layoutDemoPlaceBinding.llPlacetype.addView(itemDemoPlaceBinding.getRoot());
        }
    }
}
