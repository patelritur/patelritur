package com.demo.home.meeting;

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
import com.demo.home.HomeActivity;
import com.demo.home.booking.BookingStatusFragment;
import com.demo.home.booking.ScheduleBookingFragment;
import com.demo.home.model.MenuResponse;
import com.demo.home.model.viewmodel.MeetingPlaceViewModel;
import com.demo.home.model.viewmodel.VirtualMeetingPlaceViewModel;
import com.demo.utils.Constants;

public class MeetingPlaceFragment extends Fragment {
    LayoutDemoPlaceBinding layoutDemoPlaceBinding;
    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        layoutDemoPlaceBinding = DataBindingUtil.inflate(inflater,R.layout.layout_demo_place,container,false);
        layoutDemoPlaceBinding.backIcon.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ((HomeActivity)getActivity()).hideBottomSheet();

            }
        });
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

            ItemDemoPlaceBinding itemDemoPlaceBinding = DataBindingUtil.inflate(inflater,R.layout.item_demo_place,null,false);
            itemDemoPlaceBinding.setMenuModel(item.getDemomenu().get(i));
            int finalI = i;
            itemDemoPlaceBinding.llBookDemo.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Constants.MEETING_PLACE_TYPE_ID = item.getDemomenu().get(finalI).getMenuID();

                    if(finalI ==2)
                    {
                        ((HomeActivity)getActivity()).showScheduleFragment(new VirtualMeetFragment());

                    }
                    else
                    {
                        Constants.MEETING_TYPE_ID = "Now";
                        Constants.TIME="";
                        Constants.DATE="";
                        ((HomeActivity) getActivity()).showFragment(new BookingStatusFragment(false));
                    }
                }
            });
            itemDemoPlaceBinding.llSchedule.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Constants.MEETING_PLACE_TYPE_ID = item.getDemomenu().get(finalI).getMenuID();
                    ((HomeActivity)getActivity()).showScheduleFragment(new ScheduleBookingFragment());


                }
            });
            layoutDemoPlaceBinding.llPlacetype.setOrientation(LinearLayoutCompat.VERTICAL);
            layoutDemoPlaceBinding.llPlacetype.addView(itemDemoPlaceBinding.getRoot());
        }
    }


}
