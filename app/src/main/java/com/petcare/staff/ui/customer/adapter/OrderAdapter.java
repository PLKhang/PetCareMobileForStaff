package com.petcare.staff.ui.customer.adapter;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.petcare.staff.R;
import com.petcare.staff.data.model.api.order.OrderStatus;
import com.petcare.staff.data.model.ui.Order;
import com.petcare.staff.ui.common.adapter.BaseExpandableAdapter;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;

public class OrderAdapter extends BaseExpandableAdapter<Order, OrderAdapter.OrderViewHolder> {

    public interface OnOrderInfoClickListener {
        void onMoreInfoClick(Order order);
    }

    private final OnOrderInfoClickListener listener;

    @Override
    public void setData(List<Order> data) {
        this.fullList = new ArrayList<>(data);

        Collections.sort(this.fullList, new Comparator<Order>() {
            @Override
            public int compare(Order o1, Order o2) {
                int statusCompare = Integer.compare(getStatusPriority(o1.getStatus()), getStatusPriority(o2.getStatus()));
                if (statusCompare != 0) {
                    return statusCompare;
                }
                // Nếu status giống nhau, so sánh theo thời gian tạo
                return o1.getCreated_at().compareTo(o2.getCreated_at());
            }

            private int getStatusPriority(OrderStatus status) {
                switch (status) {
                    case PENDING:
                        return 0;
                    case PAID:
                        return 1;
                    case COMPLETED:
                        return 2;
                    case CANCELLED:
                        return 3;
                    default:
                        return 4; // UNSPECIFIED hoặc null
                }
            }
        });

        this.displayList = new ArrayList<>(this.fullList.subList(0, Math.min(displaySize, this.fullList.size())));
        notifyDataSetChanged();
    }

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
