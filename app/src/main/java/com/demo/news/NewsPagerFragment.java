package com.demo.news;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.databinding.DataBindingUtil;
import androidx.fragment.app.Fragment;

import com.demo.R;
import com.demo.databinding.ItemNewsPagerBinding;
import com.demo.launch.LaunchPagerFragment;
import com.demo.utils.Utils;

import java.io.Serializable;

public class NewsPagerFragment extends Fragment {
    private ItemNewsPagerBinding itemNewsPager;
    private NewsResponseModel newsResponseModel;
    private int position;
    public static Fragment newInstance(NewsResponseModel newsResponseModel,int position) {
        NewsPagerFragment fragmentFirst = new NewsPagerFragment();
        Bundle args = new Bundle();
        args.putInt("pos", position);
        args.putSerializable("newsmodel", (Serializable) newsResponseModel);
        fragmentFirst.setArguments(args);
        return fragmentFirst;
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
         itemNewsPager = DataBindingUtil.inflate(inflater, R.layout.item_news_pager,container,false);
        position = getArguments().getInt("pos");
        newsResponseModel = (NewsResponseModel) getArguments().getSerializable("newsmodel");
        itemNewsPager.backIcon.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                getActivity().onBackPressed();
            }
        });
        itemNewsPager.share.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Utils.shareData(getActivity(),newsResponseModel.getNewslist().get(position).getNewsTitle(),newsResponseModel.getNewslist().get(position).getNewsDesc());
            }
        });
        itemNewsPager.setNewsResponseModel(newsResponseModel.getNewslist().get(position));
        return itemNewsPager.getRoot();

    }
}
