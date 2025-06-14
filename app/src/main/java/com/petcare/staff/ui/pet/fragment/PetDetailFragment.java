package com.petcare.staff.ui.pet.fragment;

import android.icu.util.BuddhistCalendar;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.petcare.staff.MainActivity;
import com.petcare.staff.R;
import com.petcare.staff.data.model.ui.Pet;
import com.petcare.staff.ui.customer.fragment.CustomerDetailFragment;
import com.petcare.staff.ui.pet.adapter.MedicalAdapter;
import com.petcare.staff.ui.pet.adapter.VaccineAdapter;
import com.petcare.staff.ui.pet.viewmodel.PetDetailViewModel;
import com.petcare.staff.ui.pet.viewmodel.SelectedPetViewModel;
import com.petcare.staff.ui.record.viewmodel.PrescriptionViewModel;

public class PetDetailFragment extends Fragment {
    private PetDetailViewModel viewModel;
    private RecyclerView recyclerMedicalRecords, recyclerVaccineRecords;
    private MedicalAdapter medicalAdapter;
    private VaccineAdapter vaccineAdapter;
    private Pet pet;
    private ImageView image;
    private TextView petName, petSpecies, petBirth, petWeight;
    private Button btnEditInfo;
    private LinearLayout listOfMedicalRecords, listOfVaccination;
    private ImageButton btnToggleMedicalRecord, btnToggleVaccination;
    private TextView txtAddVaccination, txtShowMoreVaccination, txtAddMedicalRecord, txtShowMoreMedicalRecord;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_pet_detail, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        initViews(view);
        showPetInfo();
        initAdapters();
        initList();
        observeViewModel();
        setupListener();
    }

    private void setupListener() {
        txtShowMoreMedicalRecord.setOnClickListener(v -> {
            boolean hasMore = medicalAdapter.showMore();
            if (!hasMore) {
                txtShowMoreMedicalRecord.setVisibility(View.GONE);
            }
        });
        txtShowMoreVaccination.setOnClickListener(v -> {
            boolean hasMore = vaccineAdapter.showMore();
            if (!hasMore) {
                txtShowMoreVaccination.setVisibility(View.GONE);
            }
        });
        txtAddMedicalRecord.setOnClickListener(v -> {
            ((MainActivity) requireActivity()).navigateToWithBackStack(R.id.addMedicalRecordFragment, null);
        });
        txtAddVaccination.setOnClickListener(v -> {
            ((MainActivity) requireActivity()).navigateToWithBackStack(R.id.addVaccineRecordFragment, null);
        });
        btnEditInfo.setOnClickListener(v -> {
            ((MainActivity) requireActivity()).navigateToWithBackStack(R.id.editPetInfoFragment, null);
        });
    }

    private void observeViewModel() {
        viewModel = new ViewModelProvider(requireActivity()).get(PetDetailViewModel.class);
        viewModel.getMedicalRecords().observe(getViewLifecycleOwner(), list -> {
            if (list != null) {
                medicalAdapter.setData(list);
            } else {
                Toast.makeText(requireActivity(), "Lịch sử khám rỗng", Toast.LENGTH_SHORT).show();
            }
        });
        viewModel.getVaccineRecords().observe(getViewLifecycleOwner(), list -> {
            if (list != null) {
                vaccineAdapter.setData(list);
            } else {
                Toast.makeText(requireActivity(), "Lịch sử tiêm phòng rỗng", Toast.LENGTH_SHORT).show();
            }
        });
        PrescriptionViewModel prescriptionViewModel = new ViewModelProvider(requireActivity()).get(PrescriptionViewModel.class);
        prescriptionViewModel.resetClearFlag();
    }

    private void initList() {
        setupToggleSection(
                SectionType.MEDICAL_RECORD,
                listOfMedicalRecords,
                btnToggleMedicalRecord,
                () -> viewModel.loadMedicalRecords(pet.getId())
        );
        setupToggleSection(
                SectionType.VACCINE_RECORD,
                listOfVaccination,
                btnToggleVaccination,
                () -> viewModel.loadVaccineRecords(pet.getId())
        );
    }

    private SectionType currentOpenSection = null;

    private void setupToggleSection(
            PetDetailFragment.SectionType section,
            LinearLayout contentLayout,
            ImageButton toggleButton,
            Runnable loadDataIfNeeded
    ) {
        toggleButton.setOnClickListener(v -> {
            if (currentOpenSection == section) {
                contentLayout.setVisibility(View.GONE);
                toggleButton.setImageResource(R.drawable.ic_chevron_right);
                currentOpenSection = null;
            } else {
                // Đóng tất cả trước
                closeAllSections();

                // Mở section này
                contentLayout.setVisibility(View.VISIBLE);
                toggleButton.setImageResource(R.drawable.ic_chevron_down);
                currentOpenSection = section;

                // Lazy load
                loadDataIfNeeded.run();
            }
        });
    }

    private void closeAllSections() {
        listOfMedicalRecords.setVisibility(View.GONE);
        btnToggleMedicalRecord.setImageResource(R.drawable.ic_chevron_right);

        listOfVaccination.setVisibility(View.GONE);
        btnToggleVaccination.setImageResource(R.drawable.ic_chevron_right);
    }

    enum SectionType {
        MEDICAL_RECORD, VACCINE_RECORD
    }

    private void initAdapters() {
        medicalAdapter = new MedicalAdapter(record -> {
            //Xử lý khi ấn "More Info"
            Bundle bundle = new Bundle();
            bundle.putSerializable("record", record);
            ((MainActivity) requireActivity()).navigateToWithBackStack(R.id.medicalRecordDetail, bundle);
        });

        vaccineAdapter = new VaccineAdapter(record -> {
            Bundle bundle = new Bundle();
            bundle.putSerializable("record", record);
            ((MainActivity) requireActivity()).navigateToWithBackStack(R.id.vaccineRecordDetail, bundle);
        });

        recyclerMedicalRecords.setAdapter(medicalAdapter);
        recyclerVaccineRecords.setAdapter(vaccineAdapter);
    }

    private void showPetInfo() {
        SelectedPetViewModel selectedPetViewModel = new ViewModelProvider(requireActivity()).get(SelectedPetViewModel.class);
        pet = selectedPetViewModel.getSelectedPet().getValue();

        image.setImageResource(R.drawable.ic_animal);
        petName.setText("Name: " + pet.getName());
        petSpecies.setText("Species: " + pet.getSpecies());
        petBirth.setText("Birth: " + pet.getBirth().toString());
        petWeight.setText("Weight: " + pet.getWeight());
    }

    private void initViews(View view) {
        image = view.findViewById(R.id.petImage);
        petName = view.findViewById(R.id.petName);
        petSpecies = view.findViewById(R.id.petSpecies);
        petBirth = view.findViewById(R.id.petBirth);
        petWeight = view.findViewById(R.id.petWeight);

        btnEditInfo = view.findViewById(R.id.btnEditInfo);

        listOfMedicalRecords = view.findViewById(R.id.listOfMedicalRecords);
        listOfVaccination = view.findViewById(R.id.listOfVaccination);

        btnToggleMedicalRecord = view.findViewById(R.id.btnToggleMedicalRecord);
        btnToggleVaccination = view.findViewById(R.id.btnToggleVaccination);

        txtAddVaccination = view.findViewById(R.id.txtAddVaccination);
        txtShowMoreVaccination = view.findViewById(R.id.txtShowMoreVaccination);
        txtAddMedicalRecord = view.findViewById(R.id.txtAddMedicalRecord);
        txtShowMoreMedicalRecord = view.findViewById(R.id.txtShowMoreMedicalRecord);

        recyclerMedicalRecords = view.findViewById(R.id.recyclerMedicalRecords);
        recyclerVaccineRecords = view.findViewById(R.id.recyclerVaccineRecords);

        recyclerMedicalRecords.setLayoutManager(new LinearLayoutManager(getContext()));
        recyclerVaccineRecords.setLayoutManager(new LinearLayoutManager(getContext()));
    }


}