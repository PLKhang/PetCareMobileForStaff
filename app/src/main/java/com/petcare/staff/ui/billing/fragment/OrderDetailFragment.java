package com.petcare.staff.ui.billing.fragment;

import android.app.AlertDialog;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.util.Log;
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
import com.petcare.staff.data.model.api.appointment.AppointmentStatus;
import com.petcare.staff.data.model.api.order.OrderStatus;
import com.petcare.staff.data.model.ui.Customer;
import com.petcare.staff.data.model.ui.Order;
import com.petcare.staff.data.model.ui.Service;
import com.petcare.staff.data.model.ui.User;
import com.petcare.staff.ui.appointment.viewmodel.SharedAppointmentViewModel;
import com.petcare.staff.ui.billing.adapter.ProductAdapter;
import com.petcare.staff.ui.billing.adapter.ServiceAdapter;
import com.petcare.staff.ui.billing.viewmodel.OrderDetailViewModel;
import com.petcare.staff.ui.billing.viewmodel.SharedOrderViewModel;
import com.petcare.staff.ui.billing.viewmodel.SharedProductViewModel;
import com.petcare.staff.ui.billing.viewmodel.SharedServiceViewModel;
import com.petcare.staff.ui.common.repository.RepositoryCallback;
import com.petcare.staff.ui.customer.viewmodel.SelectedCustomerViewModel;
import com.petcare.staff.ui.home.viewmodel.BranchViewModel;
import com.petcare.staff.utils.DialogUtils;

import java.util.Arrays;

public class OrderDetailFragment extends Fragment {
    private OrderDetailViewModel viewModel;
    private SharedProductViewModel productViewModel;
    private SharedServiceViewModel serviceViewModel;
    private SharedOrderViewModel createBillViewModel;
    private SharedAppointmentViewModel appointmentViewModel;
    private ProductAdapter productAdapter;
    private ServiceAdapter serviceAdapter;
    private RecyclerView recyclerProducts, recyclerServices;
    private Customer customer;
    private String orderId;
    private ImageView image;
    private Button btnCheckout, btnStatus;
    private LinearLayout listOfProducts, listOfServices;
    private ImageButton btnToggleProduct, btnToggleService;
    private TextView name, email, phone, txtTotal;
    private TextView txtShowMoreProduct, txtShowMoreService;
    private TextView txtTime, txtAddress, txtNote;
    private boolean loadedBranch;

    @Override
    public void onResume() {
        super.onResume();
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_bill_detail, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        if (getArguments() != null) {
            orderId = getArguments().getString("orderId");
            Log.d("DEBUG", "orderId: " + orderId);
        }

        initViews(view);
        initAdapter();
        initList();
        observerViewModel();
        setupListener();
    }

    private void setupListener() {
        btnStatus.setOnClickListener(v -> {
            Order order = viewModel.getOrderDetail().getValue();
            if (order == null) return;

            OrderStatus current = order.getStatus();

            // Chọn trạng thái mới
            String[] statuses = Arrays.stream(OrderStatus.values()).map(Enum::name).toArray(String[]::new);

            new AlertDialog.Builder(requireContext()).setTitle("Chọn trạng thái cuộc hẹn").setItems(statuses, (dialog, which) -> {
                OrderStatus selectedStatus = OrderStatus.valueOf(statuses[which]);
                if (selectedStatus.compareTo(current) < 1)
                {
                    Toast.makeText(requireContext(), "Trạng thái mới phải lớn hơn hiện tại", Toast.LENGTH_SHORT).show();
                    return;
                }
                viewModel.updateOrderStatus(selectedStatus, new RepositoryCallback() {
                    @Override
                    public void onSuccess(String message) {
                        Toast.makeText(requireContext(), "Cập nhật thành công: " + order.getStatus(), Toast.LENGTH_SHORT).show();
                        viewModel.setStatus(selectedStatus);
                        btnStatus.setText(selectedStatus.toString());
                    }

                    @Override
                    public void onError(Throwable t) {
                        Toast.makeText(requireContext(), "Cập nhật thất bại", Toast.LENGTH_SHORT).show();
                    }
                });
            }).setNegativeButton("Hủy", null).show();
        });
        btnCheckout.setOnClickListener(v -> {
            DialogUtils.showCreateConfirmation(getContext(), new DialogUtils.OnConfirmationListener() {
                @Override
                public void onConfirmed() {
                    viewModel.setLoadedAppointment(false);

                    productViewModel.resetClearFlag();
                    serviceViewModel.resetClearFlag();

                    Bundle bundle = new Bundle();
                    bundle.putBoolean("fromOrder", true);
                    bundle.putBoolean("fromAppointment", false);

                    createBillViewModel.setOrderLiveData(viewModel.getOrderDetail().getValue());
                    appointmentViewModel.setAppointmentLiveData(viewModel.getApointmentDetail().getValue());

                    ((MainActivity) requireActivity()).navigateToWithBackStack(R.id.checkoutFragment, bundle);
                }
            });
        });
        txtShowMoreProduct.setOnClickListener(v -> {
            boolean hasMore = productAdapter.showMore();
            if (!hasMore) {
                txtShowMoreProduct.setVisibility(View.GONE);
            }
        });

        txtShowMoreService.setOnClickListener(v -> {
            boolean hasMore = serviceAdapter.showMore();
            if (!hasMore) {
                txtShowMoreService.setVisibility(View.GONE);
            }
        });
    }

    private void updateTotalPrice() {
        Log.d("UpdateTotalPrice", "Start");
        float total = viewModel.getOrderDetail().getValue().getTotal_price();
        Log.d("UpdateTotalPrice", "total: " + total);

        for (Service s : serviceViewModel.getSelectedServices().getValue()) {
            Log.d("UpdateTotalPrice", "total: " + total);
            total += s.getPrice() * s.getQuantity();
        }
        Log.d("UpdateTotalPrice", "Finish, total: " + total);
        txtTotal.setText(String.format("Total: %.2f$", total));
    }

    private void observerViewModel() {
        SelectedCustomerViewModel selectedCustomerVM = new ViewModelProvider(requireActivity()).get(SelectedCustomerViewModel.class);
        selectedCustomerVM.getSelectedCustomer().observe(getViewLifecycleOwner(), customer -> {
            this.customer = customer;
            showCustomerInfo();
        });

        viewModel = new ViewModelProvider(requireActivity()).get(OrderDetailViewModel.class);
        createBillViewModel = new ViewModelProvider(requireActivity()).get(SharedOrderViewModel.class);
        appointmentViewModel = new ViewModelProvider(requireActivity()).get(SharedAppointmentViewModel.class);

        createBillViewModel.resetOrderLiveData();
        appointmentViewModel.resetAppointmentLiveData();

        serviceViewModel = new ViewModelProvider(requireActivity()).get(SharedServiceViewModel.class);
        productViewModel = new ViewModelProvider(requireActivity()).get(SharedProductViewModel.class);

        viewModel.setServiceList(serviceViewModel.getServices().getValue());
//        serviceViewModel.getServices().observe(getViewLifecycleOwner(), list -> {
//            if (list != null) {
//                viewModel.setServiceList(list);
//            }
//        });
        viewModel.setProductList(productViewModel.getAllBranchProducts().getValue());
//        productViewModel.getAllProducts().observe(getViewLifecycleOwner(), list -> {
//            if (list != null) {
//                viewModel.setProductList(list);
//            }
//        });

        viewModel.loadOrderDetail(orderId);
        BranchViewModel temp = new ViewModelProvider(requireActivity()).get(BranchViewModel.class);
        viewModel.getOrderDetail().observe(getViewLifecycleOwner(), order -> {
            if (order != null) {
                if (!loadedBranch) {
                    temp.getBranchInfoById(order.getBranch_id()).observe(getViewLifecycleOwner(), info -> {
                        txtAddress.setText("Address: " + info.getLocation());
                    });
                    loadedBranch = true;
                }

                txtTime.setText("Time: " + order.getPickupTime().toDateTimeString());
                txtNote.setText("Note:");

                if (order.getProducts() != null) {
                    productViewModel.setSelectedProductsById(order.getProducts());
                }

                viewModel.getApointmentDetail().observeForever(appointment -> {
                    if (appointment.getServices() != null) {
                        serviceViewModel.setSelectedServicesById(appointment.getServices());
                        updateTotalPrice();
                    }
                });

                checkUI(order);
            }
        });

        productViewModel.getSelectedProducts().observe(getViewLifecycleOwner(), list -> {
            if (list != null) {
                Log.d("DEBUG", "Selected Products Size: " + list.size());
                productAdapter.setData(list);
            }
        });

        serviceViewModel.getSelectedServices().observe(getViewLifecycleOwner(), list -> {
            if (list != null) {
                Log.d("DEBUG", "Selected Service Size: " + list.size());

                serviceAdapter.setData(list);
            }
        });
    }

    private void checkUI(Order order) {
        if (order == null) return;

        btnStatus.setText(order.getStatus().toString());
        updateTotalPrice();

        if (!order.getStatus().equals(OrderStatus.PENDING)) {
            btnCheckout.setEnabled(false);
            btnCheckout.setBackgroundResource(R.drawable.draw_disable_button);
            if (!order.getStatus().equals(OrderStatus.PAID)) {
                btnStatus.setEnabled(false);
                btnStatus.setBackgroundResource(R.drawable.draw_disable_button);
            } else {
                btnStatus.setEnabled(true);
                btnStatus.setBackgroundResource(R.drawable.draw_enable_button);
            }
        } else {
            btnCheckout.setEnabled(true);
            btnCheckout.setBackgroundResource(R.drawable.draw_enable_button);
        }
    }

    private void initList() {
        setupToggleSection(SectionType.PRODUCT, listOfProducts, btnToggleProduct);

        setupToggleSection(SectionType.SERVICE, listOfServices, btnToggleService);
    }

    private void showCustomerInfo() {
        image.setImageResource(R.drawable.temp_avatar);
        name.setText(customer.getName());
        email.setText(customer.getEmail());
        phone.setText(customer.getPhoneNumber());
    }

    private void initAdapter() {
        productAdapter = new ProductAdapter();
        serviceAdapter = new ServiceAdapter();

        recyclerProducts.setAdapter(productAdapter);
        recyclerServices.setAdapter(serviceAdapter);
    }

    private void initViews(View view) {
        recyclerProducts = view.findViewById(R.id.recyclerProducts);
        recyclerServices = view.findViewById(R.id.recyclerServices);

        image = view.findViewById(R.id.customerImage);
        name = view.findViewById(R.id.txtName);
        email = view.findViewById(R.id.txtEmail);
        phone = view.findViewById(R.id.txtPhone);

        listOfProducts = view.findViewById(R.id.listOfProducts);
        listOfServices = view.findViewById(R.id.listOfServices);

        btnStatus = view.findViewById(R.id.btnStatus);
        btnCheckout = view.findViewById(R.id.btnCheckout);

        btnToggleProduct = view.findViewById(R.id.btnToggleProduct);
        btnToggleService = view.findViewById(R.id.btnToggleService);

        txtTotal = view.findViewById(R.id.txtTotal);
        txtShowMoreProduct = view.findViewById(R.id.txtShowMoreProducts);
        txtShowMoreService = view.findViewById(R.id.txtShowMoreServices);

        txtTime = view.findViewById(R.id.txtTime);
        txtAddress = view.findViewById(R.id.txtAddress);
        txtNote = view.findViewById(R.id.txtNote);

        recyclerProducts.setLayoutManager(new LinearLayoutManager(getContext()));
        recyclerServices.setLayoutManager(new LinearLayoutManager(getContext()));
    }

    enum SectionType {
        PRODUCT, SERVICE
    }

    private SectionType currentOpenSection = null;

    private void setupToggleSection(SectionType section, LinearLayout contentLayout, ImageButton toggleButton) {
        toggleButton.setOnClickListener(v -> {
            if (currentOpenSection == section) {
                contentLayout.setVisibility(View.GONE);
                toggleButton.setImageResource(R.drawable.ic_chevron_right);
                currentOpenSection = null;
            } else {
                // Đóng tất cả trước
//                closeAllSections();

                // Mở section này
                contentLayout.setVisibility(View.VISIBLE);
                toggleButton.setImageResource(R.drawable.ic_chevron_down);
                currentOpenSection = section;

                // Lazy load
//                loadDataIfNeeded.run();
            }
        });
    }

    private void closeAllSections() {
        listOfProducts.setVisibility(View.GONE);
        btnToggleProduct.setImageResource(R.drawable.ic_chevron_right);

        listOfServices.setVisibility(View.GONE);
        btnToggleService.setImageResource(R.drawable.ic_chevron_right);
    }

}