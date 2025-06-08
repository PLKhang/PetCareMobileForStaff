package com.petcare.staff.ui.customer.adapter;

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
import com.petcare.staff.data.model.ui.Customer;

import java.util.ArrayList;
import java.util.List;

public class CustomerSelectorAdapter extends RecyclerView.Adapter<CustomerSelectorAdapter.CustomerViewHolder> { // item_customer_selection
    private List<Customer> allCustomer;
    private List<Customer> displayList;
    private final OnChooseCustomerClickListener listener;

    @NonNull
    @Override
    public CustomerViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_customer_selection, parent, false);
        return new CustomerViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull CustomerViewHolder holder, int position) {
        Customer customer = displayList.get(position);
        holder.bind(customer);
    }

    @Override
    public int getItemCount() {
        return displayList.size();
    }

    public interface OnChooseCustomerClickListener {
        void onChooseClick(Customer customer);
    }

    public CustomerSelectorAdapter(OnChooseCustomerClickListener listener) {
        this.listener = listener;
        this.allCustomer = new ArrayList<>();
        displayList = new ArrayList<>();
    }

    public void setCustomers(List<Customer> customerList) {
        this.allCustomer = customerList;
        this.displayList = new ArrayList<>(customerList);
        notifyDataSetChanged();
    }

    public void filter (String keyword) {
        if (keyword == null || keyword.trim().isEmpty()) {
            displayList = new ArrayList<>(allCustomer);
        } else {
            String lowerKeyword = keyword.toLowerCase();
            displayList = new ArrayList<>();
            for (Customer c : allCustomer) {
                if (c.getName().toLowerCase().contains(lowerKeyword) ||
                    c.getEmail().toLowerCase().contains(lowerKeyword) ||
                    c.getPhoneNumber().toLowerCase().contains(lowerKeyword)) {
                    displayList.add(c);
                }
            }
        }
        notifyDataSetChanged();
    }

    class CustomerViewHolder extends RecyclerView.ViewHolder {
        TextView name, email, phone;
        ImageView image;
        Button btnChoose;
        public CustomerViewHolder(@NonNull View itemView) {
            super(itemView);
            name = itemView.findViewById(R.id.customerName);
            email = itemView.findViewById(R.id.customerEmail);
            phone = itemView.findViewById(R.id.customerPhone);
            image = itemView.findViewById(R.id.customerImage);
            btnChoose = itemView.findViewById(R.id.btnChooseCustomer);
        }

        public void bind(Customer customer) {
            name.setText(customer.getName());
            Log.d("Debug_recycler", name.getText().toString());
            email.setText(customer.getEmail());
            phone.setText(customer.getPhoneNumber());
            image.setImageResource(R.drawable.temp_avatar);

            btnChoose.setOnClickListener(v -> {
                if (listener != null) {
                    listener.onChooseClick(customer);
                }
            });
        }
    }
}
