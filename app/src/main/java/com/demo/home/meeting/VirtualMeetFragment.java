package com.demo.home.meeting;

import android.content.Context;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.widget.LinearLayoutCompat;
import androidx.databinding.DataBindingUtil;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;

import com.cometchat.pro.constants.CometChatConstants;
import com.cometchat.pro.core.CometChat;
import com.cometchat.pro.exceptions.CometChatException;
import com.cometchat.pro.models.User;
import com.demo.R;
import com.demo.databinding.ItemVirtualmeetBinding;
import com.demo.databinding.LayoutDemoPlaceBinding;
import com.demo.home.HomeActivity;
import com.demo.home.booking.ScheduleBookingFragment;
import com.demo.home.booking.ScheduleLaterFragment;
import com.demo.home.model.MenuResponse;
import com.demo.home.model.viewmodel.VirtualMeetingPlaceViewModel;
import com.demo.utils.Constants;
import com.demo.utils.Permissionsutils;
import com.demo.utils.Utils;
import com.demo.utils.comectChat.utils.CallUtils;

public class VirtualMeetFragment extends Fragment {

    private  boolean preBook;
    private LayoutDemoPlaceBinding layoutDemoPlaceBinding;
    private String bookDate;

    public VirtualMeetFragment(String bookDate) {
        this.bookDate = bookDate;
        if (bookDate!=null && Utils.DateAfter(bookDate)) {
            preBook = true;
        }

    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        layoutDemoPlaceBinding = DataBindingUtil.inflate(inflater, R.layout.layout_demo_place,container,false);


        layoutDemoPlaceBinding.titleText.setText(getString(R.string.virtual_meet));
        layoutDemoPlaceBinding.llPlacetype.setOrientation(LinearLayoutCompat.HORIZONTAL);

        new ViewModelProvider(requireActivity()).get(VirtualMeetingPlaceViewModel.class).getMenuType().observe(getViewLifecycleOwner(), item -> {
            updateUIForVirtualMeet(item);
        });



        return layoutDemoPlaceBinding.getRoot();

    }

    private void updateUIForVirtualMeet(MenuResponse item) {
        for(int i=0;i<item.getDemomenu().size();i++)
        {
            LayoutInflater inflater = (LayoutInflater)getActivity().getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            ItemVirtualmeetBinding itemDemoPlaceBinding = DataBindingUtil.inflate(inflater, R.layout.item_virtualmeet,null,false);
            itemDemoPlaceBinding.setMenuModel(item.getDemomenu().get(i));
            int finalI = i;


            itemDemoPlaceBinding.getRoot().setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {

                    Constants.VIRTUAL_MEET_TYPE=item.getDemomenu().get(finalI).getMenuName().split(" ")[0];
                    if(!preBook) {
                        if(Constants.VIRTUAL_MEET_TYPE.equalsIgnoreCase("video")){
                            if(!Permissionsutils.checkForCameraPermission(getActivity()))
                                Permissionsutils.askForCameraPermission(getActivity(),100);
                            else
                                ((HomeActivity) getActivity()).showFragment(new ScheduleBookingFragment());
                        }
                        else
                            ((HomeActivity) getActivity()).showFragment(new ScheduleBookingFragment());

                    }
                    else {
                        ((HomeActivity) getActivity()).showFragment(new ScheduleLaterFragment(bookDate));
                    }
                }
            });
            layoutDemoPlaceBinding.llPlacetype.addView(itemDemoPlaceBinding.getRoot());
        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        if(grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED){
            //resume tasks needing this permission
            ((HomeActivity) getActivity()).showFragment(new ScheduleBookingFragment());
        }

    }
}
