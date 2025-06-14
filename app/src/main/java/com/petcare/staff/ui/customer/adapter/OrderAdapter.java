package com.petcare.staff.ui.customer.adapter;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.petcare.staff.R;
import com.petcare.staff.data.model.ui.Order;
import com.petcare.staff.ui.common.adapter.BaseExpandableAdapter;

public class OrderAdapter extends BaseExpandableAdapter<Order, OrderAdapter.OrderViewHolder> {

    public interface OnOrderInfoClickListener {
        void onMoreInfoClick(Order order);
    }

    private final OnOrderInfoClickListener listener;

    public OrderAdapter(OnOrderInfoClickListener listener) {
        this.listener = listener;
    }

    @NonNull
    @Override
    public OrderViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_customer_order, parent, false);
        return new OrderViewHolder(view);
    }

    @Override
    protected void bind(OrderViewHolder holder, Order order) {
        holder.bind(order);
    }

    class OrderViewHolder extends RecyclerView.ViewHolder {
        TextView amount, createdDate, status;
        Button btnMoreInfo;

        public OrderViewHolder(@NonNull View itemView) {
            super(itemView);
            amount = itemView.findViewById(R.id.orderAmount);
            createdDate = itemView.findViewById(R.id.orderCreatedDate);
            status = itemView.findViewById(R.id.orderStatus);
            btnMoreInfo = itemView.findViewById(R.id.btnOrderInfo);
        }

        public void bind(Order order) {
            amount.setText("Amount: " + order.getTotal_price() + "$");
            createdDate.setText("Created: " + order.getCreated_at().toDateTimeString());
            status.setText("Status: " + order.getStatus());
            btnMoreInfo.setOnClickListener(v -> {
                if (listener != null) listener.onMoreInfoClick(order);
            });
        }
    }
}
