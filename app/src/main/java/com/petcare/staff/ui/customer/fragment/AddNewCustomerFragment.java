package com.petcare.staff.ui.customer.fragment;

import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;

import android.text.Editable;
import android.text.TextWatcher;
import android.util.Patterns;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.petcare.staff.MainActivity;
import com.petcare.staff.R;
import com.petcare.staff.data.model.ui.Customer;
import com.petcare.staff.ui.common.repository.RepositoryCallback;
import com.petcare.staff.ui.customer.viewmodel.CustomerViewModel;
import com.petcare.staff.utils.DialogUtils;

public class AddNewCustomerFragment extends Fragment {
    private CustomerViewModel viewModel;
    private Button btnCreate, btnCancel;
    private EditText etEmail, etName, etPhone;
    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        viewModel = new ViewModelProvider(requireActivity()).get(CustomerViewModel.class);

        btnCancel = view.findViewById(R.id.btnCancel);
        btnCreate = view.findViewById(R.id.btnCreate);

        etEmail = view.findViewById(R.id.etEmail);
        etName = view.findViewById(R.id.etName);
        etPhone = view.findViewById(R.id.etPhone);

        setupListener();
    }

    private void setupListener() {
        btnCancel.setOnClickListener(v -> {
            DialogUtils.showDeleteConfirmation(getContext(), this::goBack);
        });

        btnCreate.setOnClickListener(v -> {
            DialogUtils.showCreateConfirmation(getContext(), this::createNewCustomer);
        });
        etEmail.addTextChangedListener(clearErrorOnTyping(etEmail));
        etName.addTextChangedListener(clearErrorOnTyping(etName));
        etPhone.addTextChangedListener(clearErrorOnTyping(etPhone));
    }

    private void goBack(){
        ((MainActivity) requireActivity()).navigateBack();
    }

    private void createNewCustomer() {
        if (!checkInfo()) return;

        String email = etEmail.getText().toString().trim();
        String name = etName.getText().toString().trim();
        String phone = etPhone.getText().toString().trim();

        viewModel.addNewCustomer(new Customer(email, name, phone), new RepositoryCallback() {
            @Override
            public void onSuccess(String message) {
                Toast.makeText(getContext(), "Tạo khách hàng thành công!", Toast.LENGTH_SHORT).show();
            }

            @Override
            public void onError(Throwable t) {
                if (t.getMessage() != null && t.getMessage().contains("Existed email")) {
                    etEmail.setError("Email đã tồn tại");
                } else {
                    Toast.makeText(getContext(), "Lỗi: " + t.getMessage(), Toast.LENGTH_LONG).show();
                }
            }
        });
    }

    private TextWatcher clearErrorOnTyping(EditText editText) {
        return new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) { }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                editText.setError(null);
            }

            @Override
            public void afterTextChanged(Editable s) { }
        };
    }

    private boolean checkInfo() {
        String email = etEmail.getText().toString().trim();
        String name = etName.getText().toString().trim();
        String phone = etPhone.getText().toString().trim();

        if (email.isEmpty() || !Patterns.EMAIL_ADDRESS.matcher(email).matches()) {
            etEmail.setError("Email không hợp lệ");
            return false;
        }

        if (name.isEmpty()) {
            etName.setError("Vui lòng nhập tên");
            return false;
        }

        if (phone.isEmpty() || !phone.matches("\\d{9,11}")) {
            etPhone.setError("Số điện thoại không hợp lệ");
            return false;
        }

        return true;
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_add_new_customer, container, false);
    }
}