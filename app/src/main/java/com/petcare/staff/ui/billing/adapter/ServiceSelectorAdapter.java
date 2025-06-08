package com.petcare.staff.ui.billing.adapter;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.petcare.staff.R;
import com.petcare.staff.data.model.ui.Service;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;
import java.util.List;

// item_service_selection
public class ServiceSelectorAdapter extends RecyclerView.Adapter<ServiceSelectorAdapter.ServiceViewHolder> {

    private List<Service> allServices;
    private List<Service> selectedService;
    private List<Service> displayList;
    private final OnAddServiceClickListener listener;

    public interface OnAddServiceClickListener {
        void onAddClick(Service service);
    }

    public ServiceSelectorAdapter(OnAddServiceClickListener listener) {
        this.listener = listener;
        this.allServices = new ArrayList<>();
        this.selectedService = new ArrayList<>();
        this.displayList = new ArrayList<>();
    }

    public void setServices(List<Service> services) {
        this.allServices = services;
        this.displayList = new ArrayList<>(services);
        notifyDataSetChanged();
    }

    public void setSelectedService(List<Service> selectedService) {
        this.selectedService = selectedService;
    }

    public void filter(String keyword) {
        if (keyword == null || keyword.trim().isEmpty()) {
            displayList = new ArrayList<>(allServices);
        } else {
            String lowerKeyword = keyword.toLowerCase();
            displayList = new ArrayList<>();
            for (Service s : allServices) {
                if (s.getName().toLowerCase().contains(lowerKeyword) ||
                        s.getDescription().toLowerCase().contains(lowerKeyword)) {
                    displayList.add(s);
                }
            }
        }
        notifyDataSetChanged();
    }

    @NonNull
    @Override
    public ServiceViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_service_selection, parent, false);
        return new ServiceViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ServiceViewHolder holder, int position) {
        Service service = displayList.get(position);
        holder.bind(service);
    }

    @Override
    public int getItemCount() {
        return displayList.size();
    }

    class ServiceViewHolder extends RecyclerView.ViewHolder {
        TextView name, description, price;
        ImageView image;
        Button btnAdd;

        public ServiceViewHolder(@NonNull View itemView) {
            super(itemView);
            name = itemView.findViewById(R.id.serviceName);
            description = itemView.findViewById(R.id.serviceDescription);
            price = itemView.findViewById(R.id.servicePrice);
            image = itemView.findViewById(R.id.serviceImage);
            btnAdd = itemView.findViewById(R.id.btnAddService);
        }

        void bind(Service service) {
            name.setText(service.getName());
            Log.d("Debug_recycler", name.getText().toString());
            description.setText(service.getDescription());
            price.setText(String.format("%.2f $", service.getPrice()));
            if (!service.getImageUrl().equals(image.getTag())) {
                Picasso.get()
                        .load(service.getImageUrl())
                        .placeholder(R.drawable.placeholder_image)
                        .error(R.drawable.error_image)
                        .into(image);
                image.setTag(service.getImageUrl());
            }

            btnAdd.setText(selectedService.contains(service) ? "REMOVE" : "ADD");
            btnAdd.setOnClickListener(v -> {
                if (listener != null) {
                    listener.onAddClick(service);
                    btnAdd.setText(selectedService.contains(service) ? "REMOVE" : "ADD");
                }
            });
        }
    }
}
