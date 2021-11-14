package com.demo.home.profile;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.graphics.Typeface;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.TextureView;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.widget.AppCompatImageView;
import androidx.appcompat.widget.LinearLayoutCompat;
import androidx.databinding.DataBindingUtil;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentStatePagerAdapter;
import androidx.lifecycle.ViewModelProviders;

import com.bumptech.glide.Glide;
import com.demo.R;
import com.demo.databinding.FragmentMyDemoTripsBinding;
import com.demo.home.PersonalisedCarOptionsFragment;
import com.demo.home.booking.BookingStatusFragment;
import com.demo.home.model.AppContentModel;
import com.demo.home.model.viewmodel.AppContentViewModel;
import com.demo.home.model.viewmodel.AppContentViewModelFactory;
import com.demo.utils.Constants;
import com.demo.utils.PrintLog;
import com.google.android.material.tabs.TabLayout;

public class MyDemoTripsFragment extends Fragment {
    private FragmentMyDemoTripsBinding fragmentMyDemoTripsBinding;
    private BroadcastReceiver br = new MyReceiver();

    public static Fragment newInstance(int position) {
        MyDemoTripsFragment fragmentFirst = new MyDemoTripsFragment();
        Bundle args = new Bundle();
        args.putInt("pos", position);
        fragmentFirst.setArguments(args);
        return fragmentFirst;
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        fragmentMyDemoTripsBinding = DataBindingUtil.inflate(inflater, R.layout.fragment_my_demo_trips,container,false);
        setUpViewPagerFragment();
        IntentFilter filter = new IntentFilter();
        filter.addAction("calldemocount");
        getActivity().registerReceiver(br, filter);
        return fragmentMyDemoTripsBinding.getRoot();

    }

    private void setUpViewPagerFragment() {

        FragmentStatePagerAdapter adapterViewPager = new MyDemotripsPager(getChildFragmentManager(),2);
        fragmentMyDemoTripsBinding.pager.setAdapter(adapterViewPager);
        fragmentMyDemoTripsBinding.pager.setOffscreenPageLimit(2);
        fragmentMyDemoTripsBinding.tabLayout.setupWithViewPager( fragmentMyDemoTripsBinding.pager);
        setUpTabLayout();
        fragmentMyDemoTripsBinding.pager.setCurrentItem(0);
        fragmentMyDemoTripsBinding.tabLayout.setOnTabSelectedListener(new TabLayout.OnTabSelectedListener() {
            @Override
            public void onTabSelected(TabLayout.Tab tab) {
                Typeface typeface = getResources().getFont(R.font.montserrat_bold);

                ((TextView) tab.getCustomView().findViewById(R.id.textview)).setTypeface(typeface);

            }

            @Override
            public void onTabUnselected(TabLayout.Tab tab) {
                Typeface typeface = getResources().getFont(R.font.montserrat_regular);
                ((TextView) tab.getCustomView().findViewById(R.id.textview)).setTypeface(typeface);
            }

            @Override
            public void onTabReselected(TabLayout.Tab tab) {

            }
        });

    }
    private void setUpTabLayout() {
        for (int i = 0; i < 2; i++) {
            // inflate the Parent LinearLayout Container for the tab
            // from the layout nav_tab.xml file that we created 'R.layout.nav_tab
            LinearLayoutCompat tab = (LinearLayoutCompat) LayoutInflater.from(getActivity()).inflate(R.layout.item_status_tab_layout, null);
            TextView tab_label = (TextView) tab.findViewById(R.id.textview);
            if(i==0) {
                tab_label.setText("Completed");
                tab_label.setTypeface(Typeface.defaultFromStyle(Typeface.BOLD));
            }
            else
                tab_label.setText("Cancelled");
            // finally publish this custom view to navigation tab
            fragmentMyDemoTripsBinding.tabLayout.getTabAt(i).setCustomView(tab);

        }

    }

    public class MyReceiver extends BroadcastReceiver {

        @Override
        public void onReceive(Context context, Intent intent) {
            PrintLog.v("receiver");
            fragmentMyDemoTripsBinding.totalDemos.setText("  "+intent.getExtras().getString("count"));
        }
    }
}
