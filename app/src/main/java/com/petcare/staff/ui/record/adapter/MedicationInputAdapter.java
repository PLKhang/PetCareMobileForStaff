package com.petcare.staff.ui.record.adapter;

import android.app.DatePickerDialog;
import android.content.Context;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.petcare.staff.R;
import com.petcare.staff.data.model.ui.MedicalRecord;
import com.petcare.staff.data.model.ui.Medication;
import com.petcare.staff.utils.DateTime;

import java.util.Calendar;
import java.util.List;

public class MedicationInputAdapter extends RecyclerView.Adapter<MedicationInputAdapter.MedicationViewHolder> {

    private List<Medication> medicationList;
    private Context context;
    private Calendar calendar = Calendar.getInstance();

    public interface OnDeleteClickListener {
        void onDeleteClick(int position);
    }

    public List<Medication> getMedications() {
        return medicationList;
    }

    private OnDeleteClickListener listener;

    public MedicationInputAdapter(Context context, List<Medication> medicationList, OnDeleteClickListener deleteClickListener) {
        this.context = context;
        this.medicationList = medicationList;
        this.listener = deleteClickListener;
    }

    @NonNull
    @Override
    public MedicationViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.item_medication_input, parent, false);
        return new MedicationViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull MedicationViewHolder holder, int position) {
        Medication medication = medicationList.get(position);
        holder.bind(medication);

    }

    @Override
    public int getItemCount() {
        return medicationList.size();
    }

    public class MedicationViewHolder extends RecyclerView.ViewHolder {
        EditText etName, etDosage, etStartDate, etEndDate;
        Button btnDelete;

        public MedicationViewHolder(@NonNull View itemView) {
            super(itemView);
            etName = itemView.findViewById(R.id.et_med_name);
            etDosage = itemView.findViewById(R.id.et_med_dosage);
            etStartDate = itemView.findViewById(R.id.et_start_date);
            etEndDate = itemView.findViewById(R.id.et_end_date);
            btnDelete = itemView.findViewById(R.id.btn_delete_medication);
        }
        public void bind(Medication medication)
        {
            etName.setText(medication.getName());
            etDosage.setText(medication.getDosage());
            etStartDate.setText(medication.getStartDate().toString());
            etEndDate.setText(medication.getEndDate().toString());

            // Avoid multiple triggers when scrolling
            etName.addTextChangedListener(new SimpleTextWatcher(medication::setName));
            etDosage.addTextChangedListener(new SimpleTextWatcher(medication::setDosage));
            etStartDate.addTextChangedListener(new SimpleTextWatcher(text -> medication.setStartDate(DateTime.toDate(text))));
            etEndDate.addTextChangedListener(new SimpleTextWatcher(text -> medication.setEndDate(DateTime.toDate(text))));

            btnDelete.setOnClickListener(v -> {
                if (listener != null) {
                    int position = getBindingAdapterPosition();
                    if (position != RecyclerView.NO_POSITION) {
                        listener.onDeleteClick(position);
                    }
                }
            });

            if (medication.getStartDate() == null) {
                etStartDate.setText((new DateTime()).toString());
            }
            if (medication.getEndDate() == null) {
                etEndDate.setText((new DateTime()).toString());
            }

            etStartDate.setOnClickListener(v -> showDatePickerDialog(etStartDate));
            etEndDate.setOnClickListener(v -> showDatePickerDialog(etEndDate));
        }
    }

    // Helper TextWatcher to avoid duplicate code
    private static class SimpleTextWatcher implements TextWatcher {
        private final OnTextChanged onTextChanged;

        interface OnTextChanged {
            void onChanged(String text);
        }

        SimpleTextWatcher(OnTextChanged onTextChanged) {
            this.onTextChanged = onTextChanged;
        }

        @Override
        public void beforeTextChanged(CharSequence s, int start, int count, int after) { }

        @Override
        public void onTextChanged(CharSequence s, int start, int before, int count) {
            onTextChanged.onChanged(s.toString());
        }

        @Override
        public void afterTextChanged(Editable s) { }
    }

    private void showDatePickerDialog(TextView targetView) {
        int year = calendar.get(Calendar.YEAR);
        int month = calendar.get(Calendar.MONTH);
        int day = calendar.get(Calendar.DAY_OF_MONTH);

        DatePickerDialog datePickerDialog = new DatePickerDialog(
                context,
                (view, selectedYear, selectedMonth, selectedDay) -> {
                    String selectedDate = String.format("%02d/%02d/%04d", selectedDay, selectedMonth + 1, selectedYear);
                    targetView.setText(selectedDate);
                },
                year, month, day
        );

        datePickerDialog.show();
    }
}