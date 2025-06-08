package com.petcare.staff.ui.pet.adapter;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.petcare.staff.R;
import com.petcare.staff.data.model.ui.MedicalRecord;
import com.petcare.staff.ui.common.adapter.BaseExpandableAdapter;

public class MedicalAdapter extends BaseExpandableAdapter<MedicalRecord, MedicalAdapter.MedicalViewHolder> {
    public interface OnMedicalInfoClickListener {
        void onMoreInfoClick(MedicalRecord record);
    }

    private OnMedicalInfoClickListener listener;

    public MedicalAdapter(OnMedicalInfoClickListener listener) {
        this.listener = listener;
    }

    @Override
    protected void bind(MedicalViewHolder holder, MedicalRecord item) {
        holder.bind(item);
    }

    @NonNull
    @Override
    public MedicalViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.item_medical_record, parent, false);
        return new MedicalViewHolder(view);
    } // item_medical_record

    class MedicalViewHolder extends RecyclerView.ViewHolder {
        TextView medicalDiagnosis, medicalDate, medicalNote;
        Button btnMedicalInfo;

        public MedicalViewHolder(@NonNull View itemView) {
            super(itemView);
            medicalDiagnosis = itemView.findViewById(R.id.medicalDiagnosis);
            medicalDate = itemView.findViewById(R.id.medicalDate);
            medicalNote = itemView.findViewById(R.id.medicalNote);
            btnMedicalInfo = itemView.findViewById(R.id.btnMedicalInfo);
        }

        public void bind(MedicalRecord record) {
            medicalDiagnosis.setText("Vaccine: " + record.getDiagnosis());
            medicalDate.setText("Date: " + record.getDate().toString());
            medicalNote.setText("Next dose: " + record.getNotes());

            btnMedicalInfo.setOnClickListener(v -> {
                if (listener != null) {
                    listener.onMoreInfoClick(record);
                }
            });
        }
    }
}
