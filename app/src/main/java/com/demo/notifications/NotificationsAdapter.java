package com.demo.notifications;

import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.databinding.DataBindingUtil;
import androidx.recyclerview.widget.RecyclerView;

import com.demo.R;
import com.demo.databinding.ItemNotificationsBinding;
import com.demo.home.HomeActivity;
import com.demo.notifications.model.NotificationResponseModel;
import com.demo.utils.Constants;

import java.util.ArrayList;

public class NotificationsAdapter extends RecyclerView.Adapter<NotificationsAdapter.ViewHolder> {

    private final Context context;
    private ArrayList<NotificationResponseModel.Notification> dataModelList = new ArrayList<>();

    /**
     * Provide a reference to the type of views that you are using
     * (custom ViewHolder).
     */
    public static class ViewHolder extends RecyclerView.ViewHolder {
        public ItemNotificationsBinding itemRowBinding;

        public ViewHolder(ItemNotificationsBinding itemRowBinding) {
            super(itemRowBinding.getRoot());
            this.itemRowBinding = itemRowBinding;
        }

        public void bind(NotificationResponseModel.Notification obj) {
            itemRowBinding.setNotifications(obj);
            itemRowBinding.executePendingBindings();
        }
    }

    public NotificationsAdapter(Context context, ArrayList<NotificationResponseModel.Notification> carDealerModelsList) {
        this.context = context;
        dataModelList = carDealerModelsList;
    }

    // Create new views (invoked by the layout manager)
    @Override
    public NotificationsAdapter.ViewHolder onCreateViewHolder(ViewGroup viewGroup, int viewType) {
        // Create a new view, which defines the UI of the list item
        LayoutInflater inflater = (LayoutInflater)context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        ItemNotificationsBinding itemSearchListBinding = DataBindingUtil.inflate(inflater, R.layout.item_notifications,viewGroup,false);

        return new ViewHolder(itemSearchListBinding);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        NotificationResponseModel.Notification dataModel = dataModelList.get(position);
        holder.bind(dataModel);
        holder.itemRowBinding.getRoot().setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(dataModel.getNotification().equalsIgnoreCase("AcceptMeetRequest") || dataModel.getNotification().equalsIgnoreCase("ChangeMeetingStatus")
                || dataModel.getNotification().equalsIgnoreCase("SpecialistMeetVoiceRecording") ){
                    Constants.BOOK_TYPE = "Meeting";
                    Constants.MEETING_ID = dataModel.getNotificationUrlID();
                }
                else if(dataModel.getNotification().equalsIgnoreCase("AcceptDemoRequest")
                        || dataModel.getNotification().equalsIgnoreCase("SpecialistDemoVoiceRecording") || dataModel.getNotification().equalsIgnoreCase("ChangeDemoStatus")){
                    Constants.BOOK_TYPE = "Demo";
                    Constants.BOOKING_ID = dataModel.getNotificationUrlID();
                }
                Intent intent = new Intent(context,HomeActivity.class);
                intent.putExtra("comeFrom","notifications");
                context.startActivity(intent);

            }
        });

    }



    // Return the size of your dataset (invoked by the layout manager)
    @Override
    public int getItemCount() {
        return null != dataModelList ? dataModelList.size() : 0;

    }
}

