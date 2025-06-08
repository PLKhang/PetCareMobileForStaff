package com.petcare.staff.ui.userprofile.fragment;

import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.petcare.staff.R;

import com.petcare.staff.MainActivity;
import com.petcare.staff.data.model.api.user.ChangeInfoRequest;
import com.petcare.staff.data.model.api.user.ChangePasswordRequest;
import com.petcare.staff.data.model.ui.User;
import com.petcare.staff.ui.common.repository.RepositoryCallback;
import com.petcare.staff.ui.userprofile.viewmodel.UserProfileViewModel;

public class UserProfileFragment extends Fragment {

    private UserProfileViewModel viewModel;
    private User currentUser;
    private TextView txtName, txtEmail;
    private EditText etPhoneNumber, etAddress;
    private EditText etOldPw, etNewPw, etConfirmPw;
    private Button btnUpdate, btnCancel;

    @Override
    public void onResume() {
        super.onResume();
        ((MainActivity) getActivity()).setActiveNav(4);
        ((MainActivity) getActivity()).hideBottomNavigation(false);
    }

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_user_profile, container, false);
        initViews(view);

        viewModel = new ViewModelProvider(requireActivity()).get(UserProfileViewModel.class);

        viewModel.getUser().observe(getViewLifecycleOwner(), user -> {
            if (user != null) {
                currentUser = user;
                populateUserInfo(user);
            }
        });

        btnUpdate.setOnClickListener(v -> handleUpdate());
        btnCancel.setOnClickListener(v->{
            ((MainActivity) requireActivity()).navigateWithoutBackStack(R.id.homePageFragment, null);
        });

        return view;
    }

    private void initViews(View view) {
        txtName = view.findViewById(R.id.txtName);
        txtEmail = view.findViewById(R.id.txtEmail);
        etPhoneNumber = view.findViewById(R.id.etPhone);
        etAddress = view.findViewById(R.id.etAddress);
        etOldPw = view.findViewById(R.id.etOldPw);
        etNewPw = view.findViewById(R.id.etNewPw);
        etConfirmPw = view.findViewById(R.id.etIdentityMark);
        btnUpdate = view.findViewById(R.id.btnUpdate);
        btnCancel = view.findViewById(R.id.btnCancel);
    }

    private void populateUserInfo(User user) {
        txtName.setText("Name: " + user.getName());
        txtEmail.setText("Email: " + user.getEmail());
        etPhoneNumber.setText(user.getPhoneNumber());
        etAddress.setText(user.getAddress());
    }

    private void handleUpdate() {
        String phone = etPhoneNumber.getText().toString().trim();
        String address = etAddress.getText().toString().trim();
        String oldPw = etOldPw.getText().toString().trim();
        String newPw = etNewPw.getText().toString().trim();
        String confirmPw = etConfirmPw.getText().toString().trim();

        if (phone.isEmpty() || address.isEmpty()) {
            Toast.makeText(getContext(), "Phone number and address must not be empty.", Toast.LENGTH_SHORT).show();
            return;
        }

        boolean isAnyPasswordFilled = !oldPw.isEmpty() || !newPw.isEmpty() || !confirmPw.isEmpty();

        if (isAnyPasswordFilled) {
            if (oldPw.isEmpty() || newPw.isEmpty() || confirmPw.isEmpty()) {
                Toast.makeText(getContext(), "Please fill in all password fields.", Toast.LENGTH_SHORT).show();
                return;
            }

            if (!newPw.equals(confirmPw)) {
                Toast.makeText(getContext(), "New passwords do not match.", Toast.LENGTH_SHORT).show();
                return;
            }

            // Gọi API đổi mật khẩu
            ChangePasswordRequest passwordRequest = new ChangePasswordRequest(oldPw, newPw);
            viewModel.changePassword(passwordRequest, new RepositoryCallback() {
                @Override
                public void onSuccess(String message) {
                    Toast.makeText(getContext(), "Password changed successfully.", Toast.LENGTH_SHORT).show();
                }

                @Override
                public void onError(Throwable t) {
                    Toast.makeText(getContext(), "Failed to change password: " + t.getMessage(), Toast.LENGTH_SHORT).show();
                }
            });
        }

        // Gọi API cập nhật thông tin cá nhân
        ChangeInfoRequest infoRequest = new ChangeInfoRequest(
                address,
                currentUser.getEmail(),
                currentUser.getName(),
                phone
        );

        viewModel.changeInfo(infoRequest, new RepositoryCallback() {
            @Override
            public void onSuccess(String message) {
                Toast.makeText(getContext(), "Update info SUCCESSFULLY!", Toast.LENGTH_SHORT).show();
                viewModel.loadCurrentUser();
            }

            @Override
            public void onError(Throwable t) {
                Toast.makeText(getContext(), "ERROR: " + t.getMessage(), Toast.LENGTH_SHORT).show();
            }
        });
    }
}
