package com.petcare.staff.ui.home.fragment;

import android.app.Activity;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.LifecycleOwner;
import androidx.lifecycle.ViewModelLazy;
import androidx.lifecycle.ViewModelProvider;
import androidx.lifecycle.ViewModelStoreOwner;
import androidx.navigation.NavController;
import androidx.navigation.NavHostController;
import androidx.navigation.fragment.NavHostFragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.petcare.staff.MainActivity;
import com.petcare.staff.R;
import com.petcare.staff.data.model.ui.User;
import com.petcare.staff.ui.billing.viewmodel.SharedProductViewModel;
import com.petcare.staff.ui.billing.viewmodel.SharedServiceViewModel;
import com.petcare.staff.ui.customer.CustomerSelectionMode;
import com.petcare.staff.ui.customer.viewmodel.CustomerViewModel;
import com.petcare.staff.ui.customer.viewmodel.SelectedCustomerViewModel;
import com.petcare.staff.ui.userprofile.viewmodel.UserProfileViewModel;

import java.util.ArrayList;

public class HomePageFragment extends Fragment {
    ImageView image;
    TextView name, email;
    LinearLayout btnBill, btnAppointment, btnInventory, btnVaccine, btnCustomer, btnPet;

    @Override
    public void onResume() {
        super.onResume();
        ((MainActivity) getActivity()).setActiveNav(1);
        ((MainActivity) getActivity()).hideBottomNavigation(false);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View v = inflater.inflate(R.layout.fragment_home_page, container, false);
        btnAppointment = v.findViewById(R.id.btn_schedule);
        btnBill = v.findViewById(R.id.btn_bill);
        btnCustomer = v.findViewById(R.id.btn_customer);
        btnVaccine = v.findViewById(R.id.btn_vaccination);
        btnPet = v.findViewById(R.id.btn_petRecord);
        btnInventory = v.findViewById(R.id.btn_inventory);

        image = v.findViewById(R.id.image);
        name = v.findViewById(R.id.name);
        email = v.findViewById(R.id.email);

        setOnNavigateItemClickListened();
        return v;
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        UserProfileViewModel viewModel = new ViewModelProvider(requireActivity()).get(UserProfileViewModel.class);
        SharedProductViewModel productViewModel = new ViewModelProvider(requireActivity()).get(SharedProductViewModel.class);
        SharedServiceViewModel serviceViewModel = new ViewModelProvider(requireActivity()).get(SharedServiceViewModel.class);
        productViewModel.clearSelectedProducts();
        productViewModel.resetClearFlag();
        serviceViewModel.clearSelectedService();
        serviceViewModel.resetClearFlag();
        CustomerViewModel customerListVM = new ViewModelProvider(requireActivity()).get(CustomerViewModel.class);
        customerListVM.loadAllCustomers();

        viewModel.getUser().observe(getViewLifecycleOwner(), user -> {
            image.setImageResource(R.drawable.temp_avatar);
            name.setText("Welcome, " + user.getName() + "!");
            email.setText(user.getEmail());
            productViewModel.loadAllProducts(user.getBranchId());
        });

    }

    private void setOnNavigateItemClickListened() {
        NavController navController = NavHostFragment.findNavController(HomePageFragment.this);
        btnCustomer.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                SelectedCustomerViewModel selectedCustomerViewModel = new ViewModelProvider(requireActivity()).get(SelectedCustomerViewModel.class);
                selectedCustomerViewModel.setSelectionMode(CustomerSelectionMode.VIEW_DETAIL);
                navController.navigate(R.id.customerListFragment);
            }
        });

        btnBill.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {navController.navigate(R.id.createBillFragment);}
        });

        btnAppointment.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                navController.navigate(R.id.appointmentListFragment);
            }
        });

        btnInventory.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                navController.navigate(R.id.inventoryFragment);
            }
        });

        btnPet.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                navController.navigate(R.id.selectProductFragment);
            }
        });

        btnVaccine.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                navController.navigate(R.id.selectServiceFragment);
            }
        });
    }
}