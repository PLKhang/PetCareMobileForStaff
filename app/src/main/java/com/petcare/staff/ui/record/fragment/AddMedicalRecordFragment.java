package com.petcare.staff.ui.record.fragment;

import android.app.DatePickerDialog;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.petcare.staff.MainActivity;
import com.petcare.staff.R;
import com.petcare.staff.data.model.ui.MedicalRecord;
import com.petcare.staff.data.model.ui.Pet;
import com.petcare.staff.data.model.ui.Prescription;
import com.petcare.staff.data.model.ui.User;
import com.petcare.staff.ui.common.repository.RepositoryCallback;
import com.petcare.staff.ui.pet.viewmodel.SelectedPetViewModel;
import com.petcare.staff.ui.record.adapter.PrescriptionInputAdapter;
import com.petcare.staff.ui.record.viewmodel.MedicalRecordViewModel;
import com.petcare.staff.ui.record.viewmodel.PrescriptionViewModel;
import com.petcare.staff.ui.userprofile.viewmodel.UserProfileViewModel;
import com.petcare.staff.utils.DateTime;
import com.petcare.staff.utils.DialogUtils;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;

public class AddMedicalRecordFragment extends Fragment {
    private MedicalRecordViewModel viewModel;
    private PrescriptionViewModel prescriptionViewModel;
    private PrescriptionInputAdapter adapter;
    private Calendar calendar;
    private User currentUser;
    private Pet currentPet;
    private RecyclerView recyclerPrescription;
    private ImageView petImage;
    private TextView petName, petSpecies, petBirth;
    private TextView vetName;
    private EditText etDiagnose, etDate, etNote;
    private Button btnAddPrescription, btnSubmitMedicalRecord;
    private List<Prescription> prescriptionList = new ArrayList<>();

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_add_medical_record, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        initViews(view);
        showPetInfo();
        initAdapters();
        observeViewModel();
        setupListener();
    }

    private void setupListener() {
        btnSubmitMedicalRecord.setOnClickListener(v -> {
            DialogUtils.showCreateConfirmation(getContext(), this::createMedicalRecord);
        });
        btnAddPrescription.setOnClickListener(v -> {
            prescriptionViewModel.setSelectedPrescription(new Prescription());
            Bundle bundle = new Bundle();
            bundle.putString("diagnose", etDiagnose.getText().toString());
            ((MainActivity) requireActivity()).navigateToWithBackStack(R.id.addPrescriptionFragment, bundle);
        });
        etDate.setOnClickListener(v -> showDatePickerDialog(etDate));
    }

    private void createMedicalRecord() {
        if (!checkInfo()) {
            return;
        }

        MedicalRecord record = new MedicalRecord(
                "",
                currentPet.getId(),
                currentUser.getId(),
                etDiagnose.getText().toString(),
                DateTime.toDate(etDate.getText().toString()),
                etNote.getText().toString()
        );

        viewModel.createMedicalRecord(record, new RepositoryCallback() {
            @Override
            public void onSuccess(String message) {
                record.setId(message);
                Toast.makeText(requireActivity(), "Tạo record thành công! ID: " + message, Toast.LENGTH_SHORT).show();
                prescriptionViewModel.getPrescriptionList().observe(getViewLifecycleOwner(), prescriptions -> {
                    if (prescriptions == null || prescriptions.isEmpty()) {
                        Toast.makeText(requireContext(), "Không có đơn thuốc nào để lưu!", Toast.LENGTH_SHORT).show();
                    } else {
                        for (Prescription p : prescriptions) {
                            p.setExaminationId(message);
                            viewModel.createPrescription(p, new RepositoryCallback() {
                                @Override
                                public void onSuccess(String m) {
                                    Log.d("Prescription", "Tạo đơn thuốc thành công: " + m);
                                }

                                @Override
                                public void onError(Throwable t) {
                                    Log.e("Prescription", "Lỗi tạo đơn thuốc", t);
                                }
                            });
                        }
                    }
                });

            }

            @Override
            public void onError(Throwable t) {
                Toast.makeText(requireActivity(), "Error: " + t, Toast.LENGTH_SHORT).show();
            }
        });
    }

    private boolean checkInfo() {
        String diagnose = etDiagnose.getText().toString();
        String date = etDate.getText().toString();
        String note = etNote.getText().toString();

        if (diagnose.isEmpty() || date.isEmpty()) {
            Toast.makeText(getContext(), "Vui lòng nhập đủ thông tin", Toast.LENGTH_SHORT).show();
            return false;
        }

        return true;
    }

    private void observeViewModel() {
        viewModel = new ViewModelProvider(requireActivity()).get(MedicalRecordViewModel.class);

        prescriptionViewModel = new ViewModelProvider(requireActivity()).get(PrescriptionViewModel.class);
        prescriptionViewModel.clearPrescriptionList();

        prescriptionViewModel.getPrescriptionList().observe(getViewLifecycleOwner(), list -> {
            if (list != null)
                adapter.setData(list);

            Log.d("DEBUG", "Get prescription list called. Size: " + list.size());
        });


        UserProfileViewModel temp = new ViewModelProvider(requireActivity()).get(UserProfileViewModel.class);
        currentUser = temp.getUser().getValue();
        vetName.setText("ID: " + currentUser.getId() + "; Name: " + currentUser.getName());
    }

    private void initAdapters() {
        adapter = new PrescriptionInputAdapter(new PrescriptionInputAdapter.OnPrescriptionClickListener() {
            @Override
            public void onDeleteClick(int position) {
                prescriptionViewModel.removePrescription(position);
                adapter.notifyDataSetChanged();
            }

            @Override
            public void onViewMoreClick(Prescription record) {
                prescriptionViewModel.setSelectedPrescription(record);
                Bundle bundle = new Bundle();
                bundle.putString("diagnose", etDiagnose.getText().toString());
                ((MainActivity) requireActivity()).navigateToWithBackStack(R.id.addPrescriptionFragment, bundle);
            }
        });

        recyclerPrescription.setAdapter(adapter);
    }

    private void showPetInfo() {
        SelectedPetViewModel selectedPetViewModel = new ViewModelProvider(requireActivity()).get(SelectedPetViewModel.class);
        currentPet = selectedPetViewModel.getSelectedPet().getValue();

        petImage.setImageResource(R.drawable.ic_animal);
        petName.setText("Name: " + currentPet.getName());
        petSpecies.setText("Species: " + currentPet.getSpecies());
    }

    private void initViews(View view) {
        recyclerPrescription = view.findViewById(R.id.recyclerPrescription);

        petImage = view.findViewById(R.id.petImage);
        petName = view.findViewById(R.id.petName);
        petSpecies = view.findViewById(R.id.petSpecies);

        vetName = view.findViewById(R.id.vetName);
        etDiagnose = view.findViewById(R.id.etDiagnose);
        etDate = view.findViewById(R.id.etDate);
        etNote = view.findViewById(R.id.etNote);

        calendar = Calendar.getInstance();

        btnAddPrescription = view.findViewById(R.id.btnAddPrescription);
        btnSubmitMedicalRecord = view.findViewById(R.id.btnSubmitMedicalRecord);

        recyclerPrescription.setLayoutManager(new LinearLayoutManager(getContext()));
    }

    private void showDatePickerDialog(TextView targetView) {
        int year = calendar.get(Calendar.YEAR);
        int month = calendar.get(Calendar.MONTH);
        int day = calendar.get(Calendar.DAY_OF_MONTH);

        DatePickerDialog datePickerDialog = new DatePickerDialog(
                getContext(),
                (view, selectedYear, selectedMonth, selectedDay) -> {
                    String selectedDate = String.format("%02d/%02d/%04d", selectedDay, selectedMonth + 1, selectedYear);
                    targetView.setText(selectedDate);
                },
                year, month, day
        );

        datePickerDialog.show();
    }
}