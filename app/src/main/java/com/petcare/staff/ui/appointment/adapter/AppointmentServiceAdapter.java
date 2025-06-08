package com.petcare.staff.ui.appointment.adapter;

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
import com.petcare.staff.data.model.ui.Product;
import com.petcare.staff.data.model.ui.Service;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;
import java.util.List;

public class AppointmentServiceAdapter extends RecyclerView.Adapter<AppointmentServiceAdapter.ServiceViewHolder> { // item_appointment_service
    private final List<Service> fullList = new ArrayList<>();
    private final List<Service> displayList = new ArrayList<>();
    private OnQuantityChangeListener listener;
    private boolean isActiveButtons = true;
    private final int PAGE_SIZE = 3;

    public interface OnQuantityChangeListener{
        void onQuantityChanged(Service service, int newQuantity);
    }
    public void setData(List<Service> services) {
        fullList.clear();
        fullList.addAll(services);

        displayList.clear();
        int end = Math.min(PAGE_SIZE, services.size());
        displayList.addAll(services.subList(0, end));
        notifyDataSetChanged();
    }

    public boolean showMore() {
        int start = displayList.size();
        int end = Math.min(start + PAGE_SIZE, fullList.size());

        if (start >= end) return false; // Nothing more to show

        displayList.addAll(fullList.subList(start, end));
        notifyItemRangeInserted(start, end - start);
        return displayList.size() < fullList.size();
    }

    @NonNull
    @Override
    public ServiceViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.item_appointment_service, parent, false);
        return new ServiceViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ServiceViewHolder holder, int position) {
        holder.bind(displayList.get(position));
    }

    @Override
    public int getItemCount() {
        return displayList.size();
    }
    public AppointmentServiceAdapter(Boolean active, OnQuantityChangeListener listener)
    {
        this.listener = listener;
        this.isActiveButtons = active;
    }
    public void setActiveButtons(boolean activeButtons) {
        isActiveButtons = activeButtons;
        notifyDataSetChanged();
    }

    class ServiceViewHolder extends RecyclerView.ViewHolder {
        ImageView serviceImage;
        TextView serviceName, serviceDescription, servicePrice;
        EditText itemQuantity;
        ImageButton btnIncrease, btnDecrease;

        public ServiceViewHolder(@NonNull View itemView) {
            super(itemView);
            serviceImage = itemView.findViewById(R.id.serviceImage);
            serviceName = itemView.findViewById(R.id.serviceName);
            serviceDescription = itemView.findViewById(R.id.serviceDescription);
            servicePrice = itemView.findViewById(R.id.servicePrice);
            itemQuantity = itemView.findViewById(R.id.itemQuantity);
            btnIncrease = itemView.findViewById(R.id.btnIncrease);
            btnDecrease = itemView.findViewById(R.id.btnDecrease);
        }

        void bind(Service service)
        {
            itemQuantity.setEnabled(isActiveButtons);
            btnIncrease.setEnabled(isActiveButtons);
            btnDecrease.setEnabled(isActiveButtons);

            serviceName.setText(service.getName());
            serviceDescription.setText(service.getDescription());
            servicePrice.setText(String.format("%.2f $", service.getPrice()));
            itemQuantity.setText(String.valueOf(service.getQuantity()));

            if (!service.getImageUrl().equals(serviceImage.getTag()))
            {
                Picasso.get()
                        .load(service.getImageUrl())
                        .placeholder(R.drawable.placeholder_image)
                        .error(R.drawable.error_image)
                        .into(serviceImage);
                serviceImage.setTag(service.getImageUrl());
            }

            btnIncrease.setOnClickListener(v -> {
                int newQty = service.getQuantity() + 1;
//                if (newQty <= service.getStock()) {
                    service.setQuantity(newQty);
                    itemQuantity.setText(String.valueOf(newQty));
                    if (listener != null) listener.onQuantityChanged(service, newQty);
//                }
            });

            btnDecrease.setOnClickListener(v -> {
                if (service.getQuantity() > 1) {
                    int newQty = service.getQuantity() - 1;
                    service.setQuantity(newQty);
                    itemQuantity.setText(String.valueOf(newQty));
                    if (listener != null) listener.onQuantityChanged(service, newQty);
                }
            });

            itemQuantity.setOnFocusChangeListener((v, hasFocus) -> {
                if (!hasFocus) {
                    try {
                        int value = Integer.parseInt(itemQuantity.getText().toString());
//                        value = Math.max(1, Math.min(value, service.getStock()));
                        service.setQuantity(value);
                        itemQuantity.setText(String.valueOf(value));
                        if (listener != null) listener.onQuantityChanged(service, value);
                    } catch (NumberFormatException e) {
                        itemQuantity.setText(String.valueOf(service.getQuantity()));
                    }
                }
            });
        }
    }
}
