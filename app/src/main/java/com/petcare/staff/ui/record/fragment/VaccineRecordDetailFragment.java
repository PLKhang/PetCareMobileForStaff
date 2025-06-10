package com.petcare.staff.ui.record.fragment;

import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;

import com.petcare.staff.R;
import com.petcare.staff.data.model.ui.Pet;
import com.petcare.staff.data.model.ui.User;
import com.petcare.staff.data.model.ui.VaccineRecord;
import com.petcare.staff.ui.pet.viewmodel.SelectedPetViewModel;
import com.petcare.staff.ui.record.viewmodel.VaccineRecordViewModel;
import com.petcare.staff.ui.userprofile.viewmodel.UserProfileViewModel;

public class VaccineRecordDetailFragment extends Fragment {

    private VaccineRecordViewModel viewModel;
    private VaccineRecord record;
    private Pet currentPet;
    private ImageView petImage;
    private String vaccineId;
    private TextView petName, petSpecies, petBirth, petWeight;

    private TextView vetName, etDate, etNextDose;
    private TextView etLabel, etNote;

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        if (getArguments() != null) {
            vaccineId = ((VaccineRecord) getArguments().getSerializable("record")).getId();
            Log.d("DEBUG", "vaccineId: " + vaccineId);
        }

        initViews(view);
        observeViewModel();
        showPetInfo();
    }
    private void showPetInfo() {
        petImage.setImageResource(R.drawable.ic_animal);
        petName.setText(currentPet.getName());
        petSpecies.setText(currentPet.getSpecies());
        petBirth.setText(currentPet.getBirth().toString());
        petWeight.setText(String.valueOf(currentPet.getWeight()));
    }
    private void observeViewModel() {
        viewModel = new ViewModelProvider(requireActivity()).get(VaccineRecordViewModel.class);

        viewModel.loadVaccineRecord(vaccineId);

        viewModel.getVaccineRecord().observe(getViewLifecycleOwner(), vaccineRecord -> {
            if (vaccineRecord != null) {
                record = vaccineRecord;
                etDate.setText(record.getDate().toDateTimeString());
                etNextDose.setText(record.getNextDose().toDateTimeString());
                etLabel.setText(record.getVaccineName());
                viewModel.loadVetInfo();
            }
        });

        viewModel.getVet().observe(getViewLifecycleOwner(), vet -> {
            if (vet != null) {
                Log.d("VaccineFragment", "Vet loaded: id=" + vet.getId() + ", name=" + vet.getName());
                vetName.setText("ID: " + vet.getId() + "; Name: " + vet.getName());
            } else {
                Log.d("VaccineFragment", "Vet is null");
            }
        });

        SelectedPetViewModel selectedPetViewModel = new ViewModelProvider(requireActivity()).get(SelectedPetViewModel.class);
        currentPet = selectedPetViewModel.getSelectedPet().getValue();
    }

    private void initViews(View view) {
        petImage = view.findViewById(R.id.petImage);
        petName = view.findViewById(R.id.petName);
        petSpecies = view.findViewById(R.id.petSpecies);
        petBirth = view.findViewById(R.id.petBirth);
        petWeight = view.findViewById(R.id.petWeight);

        vetName = view.findViewById(R.id.vetName);
        etDate = view.findViewById(R.id.etDate);
        etNextDose = view.findViewById(R.id.etNextDose);

        etLabel = view.findViewById(R.id.etLabel);
        etNote = view.findViewById(R.id.etNote);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_vaccine_record_detail, container, false);
    }
}