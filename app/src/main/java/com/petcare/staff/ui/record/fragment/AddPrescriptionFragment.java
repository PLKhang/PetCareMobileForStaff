package com.petcare.staff.ui.record.fragment;

import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.petcare.staff.MainActivity;
import com.petcare.staff.R;
import com.petcare.staff.data.model.ui.Medication;
import com.petcare.staff.data.model.ui.Prescription;
import com.petcare.staff.ui.record.adapter.MedicationInputAdapter;
import com.petcare.staff.ui.record.viewmodel.PrescriptionViewModel;
import com.petcare.staff.utils.DialogUtils;

import java.util.ArrayList;
import java.util.List;

public class AddPrescriptionFragment extends Fragment {
    private PrescriptionViewModel viewModel;
    private List<Medication> medicationList = new ArrayList<>();
    private RecyclerView recyclerMedication;
    private MedicationInputAdapter adapter;
    private TextView txtDiagnose;
    private Button btnAddMedication, btnAddPrescription;
    private String diagnose;

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        if (getArguments() != null) {
            diagnose = (String) getArguments().getSerializable("diagnose");
            Log.d("DEBUG", "diagnose: " + diagnose);
        }

        initViews(view);
        initAdapters();
        observerViewModel();
    }

    private void observerViewModel() {
        viewModel = new ViewModelProvider(requireActivity()).get(PrescriptionViewModel.class);
        viewModel.getSelectedPrescription().observe(getViewLifecycleOwner(), prescription -> {
            if (prescription != null) {
                medicationList.clear();
                medicationList.addAll(prescription.getMedicationList()); // hoặc prescription.getMedications()
                adapter.notifyDataSetChanged();
            }
        });
    }

    private void initAdapters() {
        adapter = new MedicationInputAdapter(requireContext(), medicationList, position -> {
            View focus = requireActivity().getCurrentFocus();
            if (focus != null) focus.clearFocus(); // tránh crash khi đang nhập ở đó

            medicationList.remove(position);
            adapter.notifyItemRemoved(position);
            adapter.notifyItemRangeChanged(position, adapter.getItemCount()); // optional
        });

        recyclerMedication.setAdapter(adapter);
    }

    private void initViews(View view) {
        txtDiagnose = view.findViewById(R.id.txtDiagnose);
        btnAddMedication = view.findViewById(R.id.btnAddMedication);
        btnAddPrescription = view.findViewById(R.id.btnAddPrescription);
        recyclerMedication = view.findViewById(R.id.recyclerMedication);

        recyclerMedication.setLayoutManager(new LinearLayoutManager(getContext()));

        txtDiagnose.setText(this.diagnose);

        btnAddMedication.setOnClickListener(v -> {
            medicationList.add(new Medication());
            adapter.notifyItemInserted(medicationList.size() - 1);
        });
        btnAddPrescription.setOnClickListener(v -> {
            DialogUtils.showCreateConfirmation(getContext(), this::createPrescription);
        });
    }

    private void createPrescription() {
        medicationList = adapter.getMedications();

        if (!checkInfo(medicationList)) {
            return;
        }


        Prescription prescription = new Prescription(
                "",
                "",
                medicationList
        );
        viewModel.addPrescription(prescription);
        ((MainActivity) requireActivity()).navigateBack();
    }

    private boolean checkInfo(List<Medication> medicationList) {
        if (medicationList == null || medicationList.size() < 1) {
            Toast.makeText(getContext(), "Không có thuốc trong đơn thuốc", Toast.LENGTH_SHORT).show();
            return false;
        }
        int count = 1;
        boolean checkInfo = true;

        String label, dosage, startDate, endDate;

        for (Medication record : medicationList) {
            label = record.getName();
            dosage = record.getDosage();

            if (label.isEmpty()) {
                checkInfo = false;
                break;
            }
            if (dosage.isEmpty()) {

                checkInfo = false;
                break;
            }
            if (record.getStartDate().getDate().compareTo(record.getEndDate().getDate()) > 0) {

                checkInfo = false;
                break;
            }
            count++;
        }

        if (!checkInfo) {
            Toast.makeText(getContext(), "Item lỗi: " + count, Toast.LENGTH_SHORT).show();
            return false;
        }

        return true;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_add_prescription, container, false);
    }
}