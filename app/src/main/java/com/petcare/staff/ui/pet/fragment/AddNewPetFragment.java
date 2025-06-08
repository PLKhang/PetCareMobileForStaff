package com.petcare.staff.ui.pet.fragment;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;

import android.app.DatePickerDialog;
import android.os.Bundle;
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
import com.petcare.staff.data.model.ui.Customer;
import com.petcare.staff.data.model.ui.Pet;
import com.petcare.staff.data.repository.PetRepository;
import com.petcare.staff.ui.common.repository.RepositoryCallback;
import com.petcare.staff.ui.customer.viewmodel.SelectedCustomerViewModel;
import com.petcare.staff.utils.DateTime;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Locale;


public class AddNewPetFragment extends Fragment {
    private PetRepository repository;
    Customer customer;
    Calendar calendar = Calendar.getInstance();
    private ImageView image;
    private TextView name, email, phone;
    private EditText etName, etSpecies, etBirthday, etWeight, etColor, etIdentityMark;
    private Button btnCreate, btnCancel;

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        repository = new PetRepository(requireActivity());
        initViews(view);
        showCustomerInfo();
        setupListener();
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_add_new_pet, container, false);
    }

    private void initViews(View view) {
        image = view.findViewById(R.id.customerImage);
        name = view.findViewById(R.id.txtName);
        email = view.findViewById(R.id.txtEmail);
        phone = view.findViewById(R.id.txtPhone);

        etName = view.findViewById(R.id.etName);
        etSpecies = view.findViewById(R.id.etSpecies);
        etBirthday = view.findViewById(R.id.etBirthday);
        etWeight = view.findViewById(R.id.etWeight);
        etColor = view.findViewById(R.id.etColor);
        etIdentityMark = view.findViewById(R.id.etIdentityMark);

        btnCreate = view.findViewById(R.id.btnCreate);
        btnCancel = view.findViewById(R.id.btnCancel);
    }

    private void showCustomerInfo() {
        SelectedCustomerViewModel selectedCustomerVM = new ViewModelProvider(requireActivity()).get(SelectedCustomerViewModel.class);
        customer = selectedCustomerVM.getSelectedCustomer();

        image.setImageResource(R.drawable.temp_avatar);
        name.setText(customer.getName());
        email.setText(customer.getEmail());
        phone.setText(customer.getPhoneNumber());
    }

    private void setupListener() {
        btnCancel.setOnClickListener(v -> {
            ((MainActivity) requireActivity()).navigateBack();
        });

        btnCreate.setOnClickListener(v -> {
            createNewPet();
        });

        etBirthday.setOnClickListener(v -> showDatePickerDialog());
    }

    private void showDatePickerDialog() {
        int year = calendar.get(Calendar.YEAR);
        int month = calendar.get(Calendar.MONTH);
        int day = calendar.get(Calendar.DAY_OF_MONTH);

        DatePickerDialog datePickerDialog = new DatePickerDialog(
                getContext(),
                (view, selectedYear, selectedMonth, selectedDay) -> {
                    // Format lại ngày: thêm 1 vào month vì month bắt đầu từ 0
                    String selectedDate = String.format("%02d/%02d/%04d", selectedDay, selectedMonth + 1, selectedYear);
                    etBirthday.setText(selectedDate);
                },
                year, month, day
        );

        datePickerDialog.show();
    }

    private void createNewPet() {
        if (!checkInfo()) {
            return;
        }

        float weight = 0f;
        String weightStr = etWeight.getText().toString().trim();
        if (!weightStr.isEmpty()) {
            weight = Float.parseFloat(weightStr);
        }

        Pet pet = new Pet(
                "",
                etName.getText().toString().trim(),
                customer.getId(),
                DateTime.toDate(etBirthday.getText().toString()),
                etColor.getText().toString().trim(),
                etIdentityMark.getText().toString().trim(),
                etSpecies.getText().toString().trim(),
                weight
        );

        repository.addNewPet(pet, new RepositoryCallback() {
            @Override
            public void onSuccess(String newPetId) {
                Toast.makeText(requireActivity(), "Tạo thú cưng thành công! ID: " + newPetId, Toast.LENGTH_SHORT).show();
                btnCancel.performClick();
            }

            @Override
            public void onError(Throwable t) {
                Toast.makeText(requireActivity(), "Lỗi khi tạo thú cưng: " + t.getMessage(), Toast.LENGTH_SHORT).show();
            }
        });
    }

    private boolean checkInfo() {
        String name = etName.getText().toString().trim();
        String species = etSpecies.getText().toString().trim();
        String birthday = etBirthday.getText().toString().trim();
        String weightStr = etWeight.getText().toString().trim();

        if (name.isEmpty()) {
            etName.setError("Tên không được để trống");
            etName.requestFocus();
            return false;
        }

        if (species.isEmpty()) {
            etSpecies.setError("Loài không được để trống");
            etSpecies.requestFocus();
            return false;
        }

        if (birthday.isEmpty()) {
            etBirthday.setError("Ngày sinh không được để trống");
            etBirthday.requestFocus();
            return false;
        }

        // Kiểm tra định dạng ngày sinh (dd/MM/yyyy)
        try {
            SimpleDateFormat sdf = new SimpleDateFormat("dd/MM/yyyy", Locale.US);
            sdf.setLenient(false);
            sdf.parse(birthday);
        } catch (ParseException e) {
            etBirthday.setError("Ngày sinh không hợp lệ");
            etBirthday.requestFocus();
            return false;
        }

        if (!weightStr.isEmpty()) {
            try {
                float weight = Float.parseFloat(weightStr);
                if (weight <= 0) {
                    etWeight.setError("Cân nặng phải lớn hơn 0");
                    etWeight.requestFocus();
                    return false;
                }
            } catch (NumberFormatException e) {
                etWeight.setError("Cân nặng phải là số hợp lệ");
                etWeight.requestFocus();
                return false;
            }
        }

        return true; // Tất cả hợp lệ
    }


}