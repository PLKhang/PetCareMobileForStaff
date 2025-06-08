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
import com.petcare.staff.ui.billing.adapter.ProductAdapter;
import com.petcare.staff.ui.billing.adapter.ProductSelectorAdapter;
import com.petcare.staff.ui.billing.viewmodel.SharedProductViewModel;

public class SelectProductFragment extends Fragment {
    private SharedProductViewModel viewModel;
    private RecyclerView recyclerView;
    private ProductSelectorAdapter adapter;

    @Override
    public void onResume() {
        super.onResume();
        ((MainActivity) requireActivity()).hideBottomNavigation(true);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_select_product, container, false);
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

        recyclerView = view.findViewById(R.id.recyclerProduct);
        recyclerView.setLayoutManager(new LinearLayoutManager(getContext()));
        adapter = new ProductSelectorAdapter(product -> {
            viewModel.addSeletedProduct(product);
        });

        btnSearch.setOnClickListener(v -> {
            String keyword = edtSearch.getText().toString();
            adapter.filter(keyword);
        });
        recyclerView.setAdapter(adapter);
    }

    private void observeViewModel() {
        viewModel = new ViewModelProvider(requireActivity()).get(SharedProductViewModel.class);
        viewModel.getAllProducts().observe(getViewLifecycleOwner(), list -> {
            if (list != null) {
                adapter.setProducts(list);
            } else {
                Toast.makeText(requireActivity(), "Danh sách hàng hóa rỗng", Toast.LENGTH_SHORT).show();
            }
        });
        viewModel.getSelectedProducts().observe(getViewLifecycleOwner(), list -> {
            if (list != null) {
                adapter.setSelectedProducts(list);
            } else {
            }
        });
    }
}