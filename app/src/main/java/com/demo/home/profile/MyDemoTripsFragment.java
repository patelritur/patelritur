package com.demo.home.profile;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.graphics.Typeface;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.widget.LinearLayoutCompat;
import androidx.databinding.DataBindingUtil;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentStatePagerAdapter;
import androidx.lifecycle.ViewModelProviders;
import androidx.viewpager.widget.ViewPager;

import com.demo.R;
import com.demo.databinding.FragmentMyDemoTripsBinding;
import com.demo.home.model.AppContentModel;
import com.demo.home.model.viewmodel.AppContentViewModel;
import com.demo.home.model.viewmodel.AppContentViewModelFactory;
import com.demo.utils.Constants;
import com.demo.utils.PrintLog;
import com.google.android.material.tabs.TabLayout;

import java.util.ArrayList;

public class MyDemoTripsFragment extends Fragment {
    private FragmentMyDemoTripsBinding fragmentMyDemoTripsBinding;
    private BroadcastReceiver br = new MyReceiver();
    private ArrayList<String> searchValue = new ArrayList<>();

    public static MyDemoTripsFragment newInstance(int position) {
        MyDemoTripsFragment fragmentFirst = new MyDemoTripsFragment();
        Bundle args = new Bundle();
        args.putInt("pos", position);
        fragmentFirst.setArguments(args);
        return fragmentFirst;
    }

    @Override
    public void onResume() {
        super.onResume();
        IntentFilter filter = new IntentFilter();
        filter.addAction("calldemocount");
        getActivity().registerReceiver(br, filter);
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        fragmentMyDemoTripsBinding = DataBindingUtil.inflate(inflater, R.layout.fragment_my_demo_trips,container,false);

        callMyDemoTripsMenu();

        return fragmentMyDemoTripsBinding.getRoot();

    }

    private void callMyDemoTripsMenu() {
        getViewModelStore().clear();
        AppContentViewModelFactory factory = new AppContentViewModelFactory(getActivity().getApplication(), Constants.MYDEMO_TRIPS_MENU);
        AppContentViewModel appContentViewModel = ViewModelProviders.of(this, factory).get(AppContentViewModel.class);

        appContentViewModel.getMyDemoTripsMenuLiveData().observe(requireActivity(), item -> {
            setUpViewPagerFragment(item);
            fragmentMyDemoTripsBinding.executePendingBindings();
        });
    }

    private void setUpViewPagerFragment(AppContentModel item) {

        FragmentStatePagerAdapter adapterViewPager = new MyDemotripsPager(getChildFragmentManager(),3,item);
        fragmentMyDemoTripsBinding.pager.setAdapter(adapterViewPager);
        fragmentMyDemoTripsBinding.pager.setOffscreenPageLimit(3);
        fragmentMyDemoTripsBinding.tabLayout.setupWithViewPager( fragmentMyDemoTripsBinding.pager);
        setUpTabLayout(item);
        fragmentMyDemoTripsBinding.pager.setCurrentItem(0);
        fragmentMyDemoTripsBinding.pager.addOnPageChangeListener(new ViewPager.OnPageChangeListener() {
            @Override
            public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {

            }

            @Override
            public void onPageSelected(int position) {

                ((MyDemotripsPager)adapterViewPager).getFragment(position).notifyList(searchValue);
            }

            @Override
            public void onPageScrollStateChanged(int state) {

            }
        });
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
    private void setUpTabLayout(AppContentModel item) {
        for (int i = 0; i < 3; i++) {
            // inflate the Parent LinearLayout Container for the tab
            // from the layout nav_tab.xml file that we created 'R.layout.nav_tab
            LinearLayoutCompat tab = (LinearLayoutCompat) LayoutInflater.from(getActivity()).inflate(R.layout.item_status_tab_layout, null);
            TextView tab_label = (TextView) tab.findViewById(R.id.textview);
            tab_label.setText(item.getLabels().get(i).getLabelInLanguage());
            if(i==0) {
                tab_label.setTypeface(Typeface.defaultFromStyle(Typeface.BOLD));
            }
            // finally publish this custom view to navigation tab
            fragmentMyDemoTripsBinding.tabLayout.getTabAt(i).setCustomView(tab);

        }

    }

    public void setSearchValue(ArrayList<String> searchValue) {
        this.searchValue = searchValue;
        PrintLog.v("setSearchValue"+searchValue);
    }



    public class MyReceiver extends BroadcastReceiver {

        @Override
        public void onReceive(Context context, Intent intent) {
            fragmentMyDemoTripsBinding.totalDemos.setText("  "+intent.getExtras().getString("count"));
        }
    }
}
