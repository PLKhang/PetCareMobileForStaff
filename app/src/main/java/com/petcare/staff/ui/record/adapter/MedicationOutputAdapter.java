package com.petcare.staff.ui.record.adapter;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.petcare.staff.R;
import com.petcare.staff.data.model.ui.Medication;

import java.util.List;

public class MedicationOutputAdapter extends RecyclerView.Adapter<MedicationOutputAdapter.MedicationViewHolder>{
    private List<Medication> medicationList;
    public MedicationOutputAdapter() {}
    public void setData(List<Medication> medications)
    {
        this.medicationList = medications;
        notifyDataSetChanged();
    }

    @NonNull
    @Override
    public MedicationViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_medication_output, parent, false);
        return new MedicationViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull MedicationViewHolder holder, int position) {
        holder.bind(medicationList.get(position));
    }

    @Override
    public int getItemCount() {
        return medicationList.size();
    }

    public class MedicationViewHolder extends RecyclerView.ViewHolder {
        TextView etName, etDosage, etStartDate, etEndDate;

        public MedicationViewHolder(@NonNull View itemView) {
            super(itemView);
            etName = itemView.findViewById(R.id.et_med_name);
            etDosage = itemView.findViewById(R.id.et_med_dosage);
            etStartDate = itemView.findViewById(R.id.et_start_date);
            etEndDate = itemView.findViewById(R.id.et_end_date);
        }
        public void bind(Medication medication)
        {
            etName.setText(medication.getName());
            etDosage.setText(medication.getDosage());
            etStartDate.setText(medication.getStartDate().toString());
            etEndDate.setText(medication.getEndDate().toString());
        }
    }
}
