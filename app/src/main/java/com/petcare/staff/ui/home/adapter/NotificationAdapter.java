package com.petcare.staff.ui.home.adapter;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.recyclerview.widget.RecyclerView;

import com.petcare.staff.R;
import com.petcare.staff.data.local.entity.NotificationEntity;
import com.petcare.staff.data.model.api.notification.NotificationResponse;

import java.util.ArrayList;
import java.util.List;

public class NotificationAdapter extends RecyclerView.Adapter<NotificationAdapter.ViewHolder> {
    private List<NotificationEntity> list = new ArrayList<>();

    public void setData(List<NotificationEntity> newList) {
//        this.list = newList;
//        notifyDataSetChanged();
    }


    public static class ViewHolder extends RecyclerView.ViewHolder {
        TextView title, message;

        public ViewHolder(View v) {
            super(v);
            title = v.findViewById(R.id.txtTitle);
            message = v.findViewById(R.id.txtMessage);
        }
    }

    @Override
    public NotificationAdapter.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.item_notification, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(ViewHolder h, int i) {
        h.title.setText(list.get(i).title);
        h.message.setText(list.get(i).message);
    }

    @Override
    public int getItemCount() {
        return list.size();
    }
}
