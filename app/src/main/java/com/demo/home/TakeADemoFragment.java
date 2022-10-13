package com.demo.home;

import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.GridView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.widget.LinearLayoutCompat;
import androidx.databinding.DataBindingUtil;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentStatePagerAdapter;
import androidx.fragment.app.FragmentTransaction;
import androidx.lifecycle.ViewModelProvider;
import androidx.viewpager.widget.ViewPager;

import com.demo.R;
import com.demo.databinding.FragmentTakedemoBinding;
import com.demo.home.model.viewmodel.CarSectionFilterViewModel;
import com.demo.utils.ClickHandlers;
import com.demo.utils.PrintLog;

public class TakeADemoFragment extends Fragment implements ClickHandlers {
    private FragmentTakedemoBinding fragmentTakedemoBinding;
    private String specialistId;

    public TakeADemoFragment(String specialistId) {
        this.specialistId = specialistId;
    }



    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        fragmentTakedemoBinding = DataBindingUtil.inflate(inflater, R.layout.fragment_takedemo,container,false);
        fragmentTakedemoBinding.setHomeModel(((HomeActivity )getActivity()).homeModel);
        fragmentTakedemoBinding.llName.weatherLl.setVisibility(View.GONE);
        return fragmentTakedemoBinding.getRoot();

    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        setViewPagerFragment();
        CarSuggestionFragment myf = new CarSuggestionFragment();
        Bundle b = new Bundle();
        b.putString("specialistId",specialistId);
        myf.setArguments(b);
        FragmentTransaction transaction = getChildFragmentManager().beginTransaction();
        transaction.add(R.id.fragment_carsuggestion, myf);
        transaction.commit();
        callCarSectionApi();
        ((HomeActivity) getActivity()).setPeekheight(fragmentTakedemoBinding.parentLl.getMeasuredHeight());
    }

    private void callCarSectionApi() {
        new ViewModelProvider(requireActivity()).get(CarSectionFilterViewModel.class).getSectionListData().observe(getViewLifecycleOwner(), item -> {
            for(int i=0;i<item.getDemomenu().size();i++){
                CarSectionFragment myf = new CarSectionFragment();
                Bundle b = new Bundle();
                b.putString("carSectionId",item.getDemomenu().get(i).getMenuID());
                b.putString("carSectionTitle",item.getDemomenu().get(i).getMenuName());
                b.putString("specialistId",specialistId);
                myf.setArguments(b);
                FragmentTransaction transaction = getChildFragmentManager().beginTransaction();
                transaction.add(R.id.dynamic_car_section, myf);
                transaction.commit();
            }
        });

    }

    private void setViewPagerFragment() {
        FragmentStatePagerAdapter adapterViewPager = new MyFilterPagerAdapter(getChildFragmentManager());
        fragmentTakedemoBinding.filterPager.setAdapter(adapterViewPager);
        fragmentTakedemoBinding.filterPager.setOffscreenPageLimit(4);
        fragmentTakedemoBinding.intoTabLayout.setupWithViewPager(fragmentTakedemoBinding.filterPager);
        fragmentTakedemoBinding.filterPager.addOnPageChangeListener(new ViewPager.OnPageChangeListener() {
            @Override
            public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {

            }

            @Override
            public void onPageSelected(int position) {
                Log.v("position","position"+position);
                View view =  fragmentTakedemoBinding.filterPager.getChildAt(position);
                try {
                    GridView gridView = (GridView) view.findViewById(R.id.gridview_per_car);
                    TextView textView = (TextView) view.findViewById(R.id.textview_title);
                    if (gridView == null)
                        return;
                    int rowHeight = gridView.getChildAt(0).getMeasuredHeight() + textView.getHeight() + 40;
                    int height = gridView.getCount() % 3 == 0 ? gridView.getCount() / 3 : (gridView.getCount() / 3) + 1;
                    int finalheight = Math.max(rowHeight * height, fragmentTakedemoBinding.filterPager.getPHeight());
                    fragmentTakedemoBinding.filterPager.setLayoutParams(new LinearLayoutCompat.LayoutParams(LinearLayoutCompat.LayoutParams.MATCH_PARENT, finalheight));
                }
                catch (Exception e)
                {
                    e.printStackTrace();
                    PrintLog.v("eee"+e);
                }
            }

            @Override
            public void onPageScrollStateChanged(int state) {

            }
        });
    fragmentTakedemoBinding.imageviewNextPage.setOnClickListener(new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            if(fragmentTakedemoBinding.filterPager.getCurrentItem()<3)
            {
                fragmentTakedemoBinding.filterPager.setCurrentItem(fragmentTakedemoBinding.filterPager.getCurrentItem()+1);
            }
        }
    });
    }


    @Override
    public void onClick(View view) {

    }

    protected CarSuggestionFragment getFragment()
    {
      return  (CarSuggestionFragment) getChildFragmentManager().findFragmentByTag("carSuggestionFragment");
    }
}
