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
import android.widget.TextView;

import com.petcare.staff.R;
import com.petcare.staff.ui.record.adapter.MedicationOutputAdapter;
import com.petcare.staff.ui.record.viewmodel.PrescriptionViewModel;

public class PrescriptionDetailFragment extends Fragment {
    private PrescriptionViewModel viewModel;
    private MedicationOutputAdapter adapter;
    private RecyclerView recyclerView;
    private TextView txtDiagnose;
    private String diagnose;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_prescription_detail, container, false);
    }

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
        viewModel.getSelectedPrescription().observe(getViewLifecycleOwner(), record -> {
            if (record != null) {
                adapter.setData(record.getMedicationList());
            }
        });
    }

    private void initAdapters() {
        adapter = new MedicationOutputAdapter();
        recyclerView.setAdapter(adapter);
    }

    private void initViews(View view) {
        txtDiagnose = view.findViewById(R.id.txtDiagnose);
        txtDiagnose.setText(diagnose);

        recyclerView = view.findViewById(R.id.recyclerMedication);
        recyclerView.setLayoutManager(new LinearLayoutManager(getContext()));
    }

}