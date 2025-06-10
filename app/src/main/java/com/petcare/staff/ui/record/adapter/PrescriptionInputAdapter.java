package com.petcare.staff.ui.record.adapter;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.petcare.staff.R;
import com.petcare.staff.data.model.ui.Prescription;

import java.util.ArrayList;
import java.util.List;

public class PrescriptionInputAdapter extends RecyclerView.Adapter<PrescriptionInputAdapter.PrescriptionViewHolder> {
    private List<Prescription> prescriptionList = new ArrayList<>();

    public interface OnPrescriptionClickListener {
        void onDeleteClick(int position);

        void onViewMoreClick(Prescription record);
    }

    private OnPrescriptionClickListener listener;

    public PrescriptionInputAdapter(OnPrescriptionClickListener listener) {
        this.listener = listener;
    }

    public void setData(List<Prescription> data) {
        this.prescriptionList = data;
        notifyDataSetChanged();
    }

    @NonNull
    @Override
    public PrescriptionViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_prescription_input, parent, false);
        return new PrescriptionViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull PrescriptionViewHolder holder, int position) {
        holder.bind(prescriptionList.get(position));
    }

    @Override
    public int getItemCount() {
        return prescriptionList.size();
    }

    class PrescriptionViewHolder extends RecyclerView.ViewHolder {
        TextView txtPrescriptTitle;
        Button btnRemoveItem, btnPrescriptionDetail;

        public PrescriptionViewHolder(@NonNull View itemView) {
            super(itemView);
            txtPrescriptTitle = itemView.findViewById(R.id.txtPrescriptTitle);
            btnRemoveItem = itemView.findViewById(R.id.btnRemoveItem);
            btnPrescriptionDetail = itemView.findViewById(R.id.btnPrescriptionDetail);
        }

        public void bind(Prescription record) {
            txtPrescriptTitle.setText("Prescription " + (getBindingAdapterPosition() + 1));
            btnPrescriptionDetail.setOnClickListener(v -> {
                if (listener != null) {
                    listener.onViewMoreClick(record);
                }
            });
            btnRemoveItem.setOnClickListener(v -> {
                if (listener != null) {
                    int position = getBindingAdapterPosition();
                    if (position != RecyclerView.NO_POSITION) {
                        listener.onDeleteClick(position);
                    }
                }
            });

        }
    }
}
