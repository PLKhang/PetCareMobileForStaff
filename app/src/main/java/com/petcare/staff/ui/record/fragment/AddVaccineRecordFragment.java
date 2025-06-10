package com.petcare.staff.ui.record.fragment;

import android.app.DatePickerDialog;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.petcare.staff.MainActivity;
import com.petcare.staff.R;
import com.petcare.staff.data.model.ui.Pet;
import com.petcare.staff.data.model.ui.User;
import com.petcare.staff.data.model.ui.VaccineRecord;
import com.petcare.staff.data.repository.PetRepository;
import com.petcare.staff.ui.common.repository.RepositoryCallback;
import com.petcare.staff.ui.pet.viewmodel.SelectedPetViewModel;
import com.petcare.staff.ui.record.viewmodel.VaccineRecordViewModel;
import com.petcare.staff.ui.userprofile.viewmodel.UserProfileViewModel;
import com.petcare.staff.utils.DateTime;
import com.petcare.staff.utils.DialogUtils;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.Locale;

public class AddVaccineRecordFragment extends Fragment {
    private VaccineRecordViewModel viewModel;
    private Pet currentPet;
    private User currentUser;
    private Calendar calendar = Calendar.getInstance();
    private ImageView petImage;
    private TextView petName, petSpecies, petBirth, petWeight;
    private TextView vetName, etDate, etNextDose;
    private EditText etLabel, etNote;
    private Button btnSubmitMedicalRecord;
    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        initViews(view);
        observeViewModel();
        showPetInfo();
        setupListener();
    }

    private void observeViewModel() {
        viewModel = new ViewModelProvider(requireActivity()).get(VaccineRecordViewModel.class);

        SelectedPetViewModel selectedPetViewModel = new ViewModelProvider(requireActivity()).get(SelectedPetViewModel.class);
        currentPet = selectedPetViewModel.getSelectedPet().getValue();

        UserProfileViewModel temp = new ViewModelProvider(requireActivity()).get(UserProfileViewModel.class);
        currentUser = temp.getUser().getValue();
        vetName.setText("ID: " + currentUser.getId() + "; Name: " + currentUser.getName());
    }

    private void setupListener() {
        btnSubmitMedicalRecord.setOnClickListener(v -> {
            DialogUtils.showCreateConfirmation(requireContext(), this::craeteRecord);
        });
        etDate.setOnClickListener(v -> showDatePickerDialog(etDate));
        etNextDose.setOnClickListener(v -> showDatePickerDialog(etNextDose));
    }

    private void craeteRecord() {
        if (!checkInfo())
            return;

        VaccineRecord record = new VaccineRecord(
                "",
                currentPet.getId(),
                currentUser.getId(),
                etLabel.getText().toString(),
                DateTime.toDate(etDate.getText().toString()),
                DateTime.toDate(etNextDose.getText().toString())
        );
        viewModel.createVaccineRecord(record, new RepositoryCallback() {
            @Override
            public void onSuccess(String message) {
                Toast.makeText(requireActivity(), "Tạo thành công, ID: " + message, Toast.LENGTH_SHORT).show();
                ((MainActivity) requireActivity()).navigateBack();
            }

            @Override
            public void onError(Throwable t) {
                Toast.makeText(requireActivity(), "Error: " + t, Toast.LENGTH_SHORT).show();
            }
        });
    }

    private boolean checkInfo() {
        String dateStr = etDate.getText().toString().trim();
        String nextDoseStr = etNextDose.getText().toString().trim();
        String label = etLabel.getText().toString().trim();

        if (dateStr.isEmpty()) {
            etDate.setError("Vui lòng chọn ngày tiêm");
            etDate.requestFocus();
            return false;
        }

        if (label.isEmpty()) {
            etLabel.setError("Vui lòng nhập nhãn thuốc");
            etLabel.requestFocus();
            return false;
        }

        // Nếu có nhập ngày tiêm kế tiếp thì kiểm tra phải lớn hơn ngày khám
        if (!nextDoseStr.isEmpty()) {
            SimpleDateFormat sdf = new SimpleDateFormat("dd/MM/yyyy", Locale.getDefault());
            try {
                Date date = sdf.parse(dateStr);
                Date nextDose = sdf.parse(nextDoseStr);
                if (nextDose != null && date != null && !nextDose.after(date)) {
                    etNextDose.setError("Ngày tiêm kế tiếp phải sau ngày được tiêm");
                    etNextDose.requestFocus();
                    return false;
                }
            } catch (ParseException e) {
                etNextDose.setError("Định dạng ngày không hợp lệ");
                etNextDose.requestFocus();
                return false;
            }
        } else {
            etNextDose.setText((new DateTime()).toString());
        }

        return true;
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


    private void showPetInfo() {
        petImage.setImageResource(R.drawable.ic_animal);
        petName.setText(currentPet.getName());
        petSpecies.setText(currentPet.getSpecies());
        petBirth.setText(currentPet.getBirth().toString());
        petWeight.setText(String.valueOf(currentPet.getWeight()));
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

        btnSubmitMedicalRecord = view.findViewById(R.id.btnSubmitMedicalRecord);
    }



    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_add_vaccine_record, container, false);
    }
}