package com.petcare.staff.ui.pet.adapter;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.petcare.staff.R;
import com.petcare.staff.data.model.ui.VaccineRecord;
import com.petcare.staff.ui.common.adapter.BaseExpandableAdapter;

public class VaccineAdapter extends BaseExpandableAdapter<VaccineRecord, VaccineAdapter.VaccineViewHolder> {
    public interface OnVaccineInfoClickListener {
        void onMoreInfoClick(VaccineRecord record);
    }

    private final OnVaccineInfoClickListener listener;

    public VaccineAdapter(OnVaccineInfoClickListener listener) {
        this.listener = listener;
    }

    @Override
    protected void bind(VaccineViewHolder holder, VaccineRecord item) {
        holder.bind(item);
    }

    @NonNull
    @Override
    public VaccineViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.item_vaccine_record, parent, false);
        return new VaccineViewHolder(view);
    } // item_vaccine_record

    class VaccineViewHolder extends RecyclerView.ViewHolder {
        TextView vaccineName, vaccineDate, vaccineNextDose;
        Button btnVaccineInfo;

        public VaccineViewHolder(@NonNull View itemView) {
            super(itemView);
            vaccineName = itemView.findViewById(R.id.vaccineName);
            vaccineDate = itemView.findViewById(R.id.vaccineDate);
            vaccineNextDose = itemView.findViewById(R.id.vaccineNextDose);
            btnVaccineInfo = itemView.findViewById(R.id.btnVaccineInfo);
        }

        public void bind(VaccineRecord record) {
            vaccineName.setText("Vaccine: " + record.getVaccineName());
            vaccineDate.setText("Date: " + record.getDate().toString());
            vaccineNextDose.setText("Next dose: " + record.getNextDose().toString());

            btnVaccineInfo.setOnClickListener(v -> {
                if (listener != null) {
                    listener.onMoreInfoClick(record);
                }
            });
        }
    }
}
