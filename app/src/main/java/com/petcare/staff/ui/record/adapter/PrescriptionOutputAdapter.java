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

public class PrescriptionOutputAdapter extends RecyclerView.Adapter<PrescriptionOutputAdapter.PrescriptionViewHolder>{
    private List<Prescription> prescriptionList = new ArrayList<>();

    @NonNull
    @Override
    public PrescriptionViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_prescription_output, parent, false);
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

    public interface OnPrescriptionClickListener {
        void onViewMoreClick(Prescription record);
    }
    public PrescriptionOutputAdapter(OnPrescriptionClickListener listener) {
        this.listener = listener;
    }

    public void setData(List<Prescription> data) {
        this.prescriptionList = data;
        notifyDataSetChanged();
    }

    private OnPrescriptionClickListener listener;

    class PrescriptionViewHolder extends RecyclerView.ViewHolder {
        TextView txtPrescriptTitle;
        Button btnPrescriptionDetail;

        public PrescriptionViewHolder(@NonNull View itemView) {
            super(itemView);
            txtPrescriptTitle = itemView.findViewById(R.id.txtPrescriptTitle);
            btnPrescriptionDetail = itemView.findViewById(R.id.btnPrescriptionDetail);
        }

        public void bind(Prescription record) {
            txtPrescriptTitle.setText("Prescription " + (getBindingAdapterPosition() + 1));
            btnPrescriptionDetail.setOnClickListener(v -> {
                if (listener != null) {
                    listener.onViewMoreClick(record);
                }
            });
        }
    }
}
