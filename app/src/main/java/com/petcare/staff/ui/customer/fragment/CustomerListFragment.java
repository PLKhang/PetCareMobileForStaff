package com.petcare.staff.ui.customer.fragment;

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
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.Toast;

import com.petcare.staff.MainActivity;
import com.petcare.staff.R;
import com.petcare.staff.ui.customer.adapter.CustomerSelectorAdapter;
import com.petcare.staff.ui.customer.viewmodel.CustomerViewModel;
import com.petcare.staff.ui.customer.viewmodel.SelectedCustomerViewModel;

public class CustomerListFragment extends Fragment {
    private CustomerViewModel customerListVM;
    private SelectedCustomerViewModel selectedCustomerVM;
    private RecyclerView recyclerView;
    private CustomerSelectorAdapter adapter;

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        initViews(view);
        observeViewModel();
    }


    @Override
    public void onResume() {
        super.onResume();
        ((MainActivity) getActivity()).hideBottomNavigation(true);
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_customer_list, container, false);
    }

    private void initViews(View view) {
        EditText edtSearch = view.findViewById(R.id.edtSearch);
        ImageButton btnSearch = view.findViewById(R.id.btnSearch);
        Button btnAdd = view.findViewById(R.id.btnAddCustomer);

        recyclerView = view.findViewById(R.id.recyclerCustomer);
        recyclerView.setLayoutManager(new LinearLayoutManager(getContext()));
        adapter = new CustomerSelectorAdapter(customer -> {
            selectedCustomerVM.setSelectedCustomer(customer);

            switch (selectedCustomerVM.getSelectionMode()) {
                case VIEW_DETAIL:
                    ((MainActivity) requireActivity()).navigateToWithBackStack(R.id.customerDetailFragment, null);
                    break;
                case CREATE_APPOINTMENT:
                    ((MainActivity) requireActivity()).navigateBack(); // Quay về fragment trước đó để tiếp tục tạo
                    break;
                case CREATE_BILL:
                    ((MainActivity) requireActivity()).navigateBack();
                    break;
            }

        });

        btnSearch.setOnClickListener(v -> {
            String keyword = edtSearch.getText().toString();
            adapter.filter(keyword);
        });

        btnAdd.setOnClickListener(v -> {
            ((MainActivity) requireActivity()).navigateToWithBackStack(R.id.addNewCustomerFragment, null);
            observeViewModel();
        });
        recyclerView.setAdapter(adapter);
    }

    private void observeViewModel() {
        customerListVM = new ViewModelProvider(requireActivity()).get(CustomerViewModel.class);
        customerListVM.loadAllCustomers();
        customerListVM.getAllCustomers().observe(getViewLifecycleOwner(), list -> {
                if (list != null) {
                    adapter.setCustomers(list);
                } else {
                    Toast.makeText(requireActivity(), "Danh sách khách hàng rỗng", Toast.LENGTH_SHORT).show();
                }
            }
        );
        selectedCustomerVM = new ViewModelProvider(requireActivity()).get(SelectedCustomerViewModel.class);
        selectedCustomerVM.clear();
    }
}