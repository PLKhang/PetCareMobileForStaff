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
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;

import com.petcare.staff.MainActivity;
import com.petcare.staff.R;
import com.petcare.staff.data.model.ui.MedicalRecord;
import com.petcare.staff.data.model.ui.Pet;
import com.petcare.staff.data.model.ui.Prescription;
import com.petcare.staff.data.model.ui.User;
import com.petcare.staff.ui.pet.viewmodel.SelectedPetViewModel;
import com.petcare.staff.ui.record.adapter.PrescriptionOutputAdapter;
import com.petcare.staff.ui.record.viewmodel.MedicalRecordViewModel;
import com.petcare.staff.ui.record.viewmodel.PrescriptionViewModel;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;

public class MedicalRecordDetailFragment extends Fragment {
    private MedicalRecordViewModel viewModel;
    private PrescriptionViewModel prescriptionViewModel;
    private PrescriptionOutputAdapter adapter;
    private MedicalRecord record;
    private User createdUser;
    private Pet currentPet;
    private RecyclerView recyclerView;
    private ImageView petImage;
    private TextView petName, petSpecies, petBirth;
    private TextView vetName;
    private TextView etDiagnose, etDate, etNote;
    private List<Prescription> prescriptionList;

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        if (getArguments() != null) {
            record = (MedicalRecord) getArguments().getSerializable("record");
            Log.d("DEBUG", "medicalId: " + record.getId());
        }

        initViews(view);
        showPetInfo();
        initAdapters();
        observeViewModel();
        setupListener();
    }

    private void setupListener() {
    }

    private void observeViewModel() {
        viewModel = new ViewModelProvider(requireActivity()).get(MedicalRecordViewModel.class);
        prescriptionViewModel = new ViewModelProvider(requireActivity()).get(PrescriptionViewModel.class);
        prescriptionViewModel.clearPrescriptionList();

        viewModel.loadUser(record.getVetId());
        prescriptionViewModel.loadPrescriptionList(record.getId());

        prescriptionViewModel.getPrescriptionList().observe(getViewLifecycleOwner(), list -> {
            if (list != null)
            {
                adapter.setData(list);
            }
        });

        viewModel.getUser().observe(getViewLifecycleOwner(), user -> {
            if (user != null) {
                vetName.setText(user.getName() + " (ID: " + user.getId() + ")");
            }
        });
    }

    private void initAdapters() {
        adapter = new PrescriptionOutputAdapter(v -> {
            Bundle bundle = new Bundle();
            bundle.putString("diagnose", etDiagnose.getText().toString());
            ((MainActivity) requireActivity()).navigateToWithBackStack(R.id.prescriptionDetailFragment, bundle);
        });

        recyclerView.setAdapter(adapter);
    }

    private void showPetInfo() {
        SelectedPetViewModel selectedPetViewModel = new ViewModelProvider(requireActivity()).get(SelectedPetViewModel.class);
        currentPet = selectedPetViewModel.getSelectedPet().getValue();

        petImage.setImageResource(R.drawable.ic_animal);
        petName.setText("Name: " + currentPet.getName());
        petSpecies.setText("Species: " + currentPet.getSpecies());
    }

    private void initViews(View view) {
        recyclerView = view.findViewById(R.id.recyclerPrescription);

        petImage = view.findViewById(R.id.petImage);
        petName = view.findViewById(R.id.petName);
        petSpecies = view.findViewById(R.id.petSpecies);

        vetName = view.findViewById(R.id.vetName);
        etDiagnose = view.findViewById(R.id.etDiagnose);
        etDate = view.findViewById(R.id.etDate);
        etNote = view.findViewById(R.id.etNote);

        etDiagnose.setText(record.getDiagnosis());
        etDate.setText(record.getDate().toString());
        etNote.setText(record.getNotes());

        recyclerView.setLayoutManager(new LinearLayoutManager(getContext()));
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_medical_record_detail, container, false);
    }
}