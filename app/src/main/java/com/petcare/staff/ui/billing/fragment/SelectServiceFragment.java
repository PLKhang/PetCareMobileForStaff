package com.petcare.staff.ui.billing.fragment;

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
import android.widget.Toast;

import com.petcare.staff.MainActivity;
import com.petcare.staff.R;
import com.petcare.staff.ui.billing.adapter.ServiceSelectorAdapter;
import com.petcare.staff.ui.billing.viewmodel.SharedServiceViewModel;

public class SelectServiceFragment extends Fragment {

    private SharedServiceViewModel viewModel;
    private RecyclerView recyclerView;
    private ServiceSelectorAdapter adapter;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_select_service, container, false);
    }

    @Override
    public void onResume() {
        super.onResume();
        ((MainActivity) requireActivity()).hideBottomNavigation(true);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        initViews(view);
        observeViewModel();
    }

    private void initViews(View view) {
        EditText edtSearch = view.findViewById(R.id.edtSearch);
        Button btnSearch = view.findViewById(R.id.btnSearch);

        recyclerView = view.findViewById(R.id.recyclerService);
        recyclerView.setLayoutManager(new LinearLayoutManager(getContext()));
        adapter = new ServiceSelectorAdapter(service -> {
            viewModel.addSelectedService(service);
        });

        btnSearch.setOnClickListener(v -> {
            String keyword = edtSearch.getText().toString();
            adapter.filter(keyword);
        });
        recyclerView.setAdapter(adapter);
    }

    private void observeViewModel() {
        viewModel = new ViewModelProvider(requireActivity()).get(SharedServiceViewModel.class);
        viewModel.getServices().observe(getViewLifecycleOwner(), services -> {
            if (services != null) {
                adapter.setServices(services);
            } else {
                Toast.makeText(requireActivity(), "Danh sách dịch vụ rỗng", Toast.LENGTH_SHORT).show();
            }
        });
        viewModel.getSelectedServices().observe(getViewLifecycleOwner(), services -> {
            if (services != null) {
                adapter.setSelectedService(services);
            }
        });
    }
}
