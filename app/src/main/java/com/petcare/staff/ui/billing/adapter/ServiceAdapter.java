package com.petcare.staff.ui.billing.adapter;

import android.text.TextWatcher;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.petcare.staff.R;
import com.petcare.staff.data.model.ui.Service;
import com.petcare.staff.ui.common.adapter.BaseExpandableAdapter;
import com.squareup.picasso.Picasso;

public class ServiceAdapter extends BaseExpandableAdapter<Service, ServiceAdapter.ServiceViewHolder> {
    public ServiceAdapter() {
    }

    @Override
    protected void bind(ServiceViewHolder holder, Service item) {
        holder.bind(item);
    }

    @NonNull
    @Override
    public ServiceViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_bill_service, parent, false);
        return new ServiceViewHolder(view);
    } // item_bill_service

    public class ServiceViewHolder extends RecyclerView.ViewHolder {
        ImageView serviceImage;
        TextView serviceName, serviceDescription, servicePrice, txtQuantity;

        public ServiceViewHolder(@NonNull View itemView) {
            super(itemView);
            serviceImage = itemView.findViewById(R.id.serviceImage);
            serviceName = itemView.findViewById(R.id.serviceName);
            serviceDescription = itemView.findViewById(R.id.serviceDescription);
            servicePrice = itemView.findViewById(R.id.servicePrice);
            txtQuantity = itemView.findViewById(R.id.txtQuantity);
        }

        public void bind(Service service) {
            Log.d("DEBUG", service.toString());
            serviceName.setText(service.getName());
            serviceDescription.setText(service.getDescription());
            servicePrice.setText(String.format("%.2f $", service.getPrice()));
            txtQuantity.setText("x" + service.getQuantity());

            if (!service.getImageUrl().equals(serviceImage.getTag())) {
                Picasso.get()
                        .load(service.getImageUrl())
                        .placeholder(R.drawable.placeholder_image)
                        .error(R.drawable.error_image)
                        .into(serviceImage);
                serviceImage.setTag(service.getImageUrl());
            }
        }
    }
}
