package com.demo.home.booking.datePicker;

import android.app.AlarmManager;
import android.content.Context;
import android.graphics.Color;
import android.graphics.drawable.Drawable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.appcompat.widget.AppCompatTextView;
import androidx.appcompat.widget.LinearLayoutCompat;
import androidx.core.graphics.drawable.DrawableCompat;
import androidx.recyclerview.widget.RecyclerView;

import com.demo.R;
import com.demo.utils.PrintLog;

import org.joda.time.DateTime;

import java.util.ArrayList;

public class HorizontalPickerAdapter extends RecyclerView.Adapter<HorizontalPickerAdapter.ViewHolder> {

    private static final long DAY_MILLIS = AlarmManager.INTERVAL_DAY;
    private final int mBackgroundColor;
    private int itemWidth;
    private final OnItemClickedListener listener;
    private ArrayList<Day> items;

    public HorizontalPickerAdapter(DateTime dateTime,int itemWidth, OnItemClickedListener listener, Context context, int daysToCreate, int offset, int mBackgroundColor) {
        items=new ArrayList<>();
        this.itemWidth=itemWidth;
        this.listener=listener;
        generateDays(daysToCreate,dateTime.minusDays(offset).getMillis(),false);
        this.mBackgroundColor=mBackgroundColor;
    }

    public  void generateDays(int n, long initialDate, boolean cleanArray) {
        if(cleanArray)
            items.clear();
        int i=0;
        while(i<n)
        {
            DateTime actualDate = new DateTime(initialDate + (DAY_MILLIS * i++));
            items.add(new Day(actualDate));
        }
    }


    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        return new ViewHolder(
                LayoutInflater.from(parent.getContext())
                        .inflate(R.layout.item_schedule_later_time,parent,false));
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {
        Day item=getItem(position);
        holder.tvDay.setText(item.getDay());
        holder.tvWeekDay.setText(item.getWeekDay());

        if(item.isSelected())
        {
            holder.ll.setBackgroundResource(R.drawable.border_red_rounded_corner);
            holder.tvDay.setTextColor(Color.RED);
            holder.tvWeekDay.setTextColor(Color.RED);
        }
        else if(item.isToday())
        {
            holder.ll.setBackgroundDrawable(getDayTodayBackground(holder.itemView));
            holder.tvDay.setTextColor(Color.BLACK);
            holder.tvWeekDay.setTextColor(Color.BLACK);
        }
        else
        {
            holder.ll.setBackgroundResource(Color.TRANSPARENT);
            holder.tvDay.setTextColor(R.color.color_3d3d3d);
            holder.tvWeekDay.setTextColor(R.color.color_3d3d3d);
        }
    }



    private Drawable getDayTodayBackground(View view) {
        Drawable drawable=view.getResources().getDrawable(R.drawable.white_border);

        return drawable;
    }

    public Day getItem(int position) {
        return items.get(position);
    }

    @Override
    public int getItemCount() {
        return items.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {
        AppCompatTextView tvDay,tvWeekDay;
        LinearLayoutCompat ll;

        public ViewHolder(View itemView) {
            super(itemView);
            tvDay= (AppCompatTextView) itemView.findViewById(R.id.tvDay);
            ll= (LinearLayoutCompat) itemView.findViewById(R.id.ll);
          //  tvDay.setWidth(itemWidth);
            tvWeekDay= (AppCompatTextView) itemView.findViewById(R.id.tvWeekDay);
            itemView.setOnClickListener(this);
        }

        @Override
        public void onClick(View v) {
            listener.onClickView(v,getAdapterPosition());
        }
    }
}
