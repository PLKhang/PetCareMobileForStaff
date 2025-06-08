package com.petcare.staff.ui.customer.adapter;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.petcare.staff.R;
import com.petcare.staff.data.model.ui.Bill;
import com.petcare.staff.ui.common.adapter.BaseExpandableAdapter;

import java.util.ArrayList;
import java.util.List;

public class BillAdapter extends BaseExpandableAdapter<Bill, BillAdapter.BillViewHolder> { // item_customer_bill
  private final OnBillInfoClickListener listener;

    public interface OnBillInfoClickListener {
        void onMoreInfoClick(Bill bill);
    }

    public BillAdapter(OnBillInfoClickListener listener) {
        this.listener = listener;
    }

    @NonNull
    @Override
    public BillViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_customer_bill, parent, false);
        return new BillViewHolder(view);
    }

    @Override
    public void bind(BillViewHolder holder, Bill bill) {
        holder.bind(bill);
    }

    class BillViewHolder extends RecyclerView.ViewHolder {
        TextView amount, description, status;
        Button btnMoreInfo;

        public BillViewHolder(@NonNull View itemView) {
            super(itemView);
            amount = itemView.findViewById(R.id.billAmount);
            description = itemView.findViewById(R.id.billDescription);
            status = itemView.findViewById(R.id.billStatus);
            btnMoreInfo = itemView.findViewById(R.id.btnBillInfo);
        }

        public void bind(Bill bill) {
            amount.setText("Amount: " + bill.getAmount() + "$");
            description.setText(bill.getDescription());
            status.setText("Status: " + bill.getStatus());
            btnMoreInfo.setOnClickListener(v -> {
                if (listener != null) {
                    listener.onMoreInfoClick(bill);
                }
            });
        }
    }
}
