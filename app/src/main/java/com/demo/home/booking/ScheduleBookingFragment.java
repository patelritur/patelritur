package com.demo.home.booking;

import android.annotation.SuppressLint;
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
import androidx.lifecycle.ViewModelProviders;

import com.demo.R;
import com.demo.databinding.ItemScheduleLaterBinding;
import com.demo.databinding.LayoutScheduleBinding;
import com.demo.home.HomeActivity;
import com.demo.home.model.AppContentModel;
import com.demo.home.model.viewmodel.AppContentViewModel;
import com.demo.home.model.viewmodel.AppContentViewModelFactory;
import com.demo.utils.Constants;
import com.demo.utils.PrintLog;

public class ScheduleBookingFragment extends Fragment {

    private LayoutScheduleBinding layoutScheduleBinding;
    private ViewGroup viewGroup;

    @SuppressLint("SetTextI18n")
    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        layoutScheduleBinding = DataBindingUtil.inflate(inflater, R.layout.layout_schedule, container, false);
        viewGroup = container;
        return layoutScheduleBinding.getRoot();

    }


    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        AppContentViewModelFactory factory = new AppContentViewModelFactory(getActivity().getApplication(), Constants.SCHEDULE_MENU);
        AppContentViewModel appContentViewModel = ViewModelProviders.of(this, factory).get(AppContentViewModel.class);

        appContentViewModel.getSchedulemenuLivedata().observe(getViewLifecycleOwner(), item -> {
            updateUIForSchedule(item);

        });
    }

    private void updateUIForSchedule(AppContentModel item) {

        for (int i = 0; i < item.getLabels().size(); i++) {
            LayoutInflater inflater = (LayoutInflater) getActivity().getSystemService(Context.LAYOUT_INFLATER_SERVICE);

            ItemScheduleLaterBinding itemDemoPlaceBinding = DataBindingUtil.inflate(inflater, R.layout.item_schedule_later, viewGroup, false);
            int finalI1 = i;
            itemDemoPlaceBinding.textviewSchedule.setText(item.getLabels().get(i).getLabelInLanguage());

            itemDemoPlaceBinding.textviewSchedule.setOnClickListener(new View.OnClickListener() {

                @Override
                public void onClick(View v) {
                    Constants.BOOKING_TYPE_ID = item.getLabels().get(finalI1).getLabelName();
                    Constants.MEETING_TYPE_ID = item.getLabels().get(finalI1).getLabelName();
                    Constants.DEMOADDRESS = "";
                    Constants.DEMOLOCATIONTYPE = "Home";
                    if(finalI1!=2) {
                        Constants.TIME="";
                        Constants.DATE="";

                        ((HomeActivity) getActivity()).showFragment(new BookingStatusFragment(false));
                    }
                    else
                    {
                        Constants.BOOKING_TYPE_ID = "Schedule";
                        Constants.MEETING_TYPE_ID = "Schedule";
                        ((HomeActivity) getActivity()).showFragment(new ScheduleLaterFragment());
                    }
                }
            });

            layoutScheduleBinding.llPlacetype.setOrientation(LinearLayoutCompat.HORIZONTAL);
            layoutScheduleBinding.llPlacetype.addView(itemDemoPlaceBinding.getRoot());
        }
    }

    public int getHeight( ) {
        if(layoutScheduleBinding!=null)
        return layoutScheduleBinding.getRoot().getMeasuredHeight();
        return 0;
    }
}
