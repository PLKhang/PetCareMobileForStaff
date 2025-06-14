package com.petcare.staff.ui.appointment.fragment;

import android.annotation.SuppressLint;
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
import com.petcare.staff.data.model.ui.Appointment;
import com.petcare.staff.data.model.ui.Customer;
import com.petcare.staff.data.model.ui.Product;
import com.petcare.staff.data.model.ui.Service;
import com.petcare.staff.data.model.ui.User;
import com.petcare.staff.ui.appointment.adapter.AppointmentProductAdapter;
import com.petcare.staff.ui.appointment.adapter.AppointmentServiceAdapter;
import com.petcare.staff.ui.appointment.viewmodel.AppointmentDetailViewModel;
import com.petcare.staff.ui.appointment.viewmodel.SharedAppointmentViewModel;
import com.petcare.staff.ui.billing.viewmodel.SharedOrderViewModel;
import com.petcare.staff.ui.billing.viewmodel.SharedProductViewModel;
import com.petcare.staff.ui.billing.viewmodel.SharedServiceViewModel;
import com.petcare.staff.ui.common.repository.RepositoryCallback;
import com.petcare.staff.ui.customer.viewmodel.SelectedCustomerViewModel;
import com.petcare.staff.ui.userprofile.viewmodel.UserProfileViewModel;
import com.petcare.staff.utils.DialogUtils;

import java.util.Arrays;

public class AppointmentDetailFragment extends Fragment {
    private AppointmentDetailViewModel viewModel;
    private SharedProductViewModel productViewModel;
    private SharedServiceViewModel serviceViewModel;
    private SharedOrderViewModel createBillViewModel;
    private SharedAppointmentViewModel appointmentViewModel;
    private AppointmentProductAdapter productAdapter;
    private AppointmentServiceAdapter serviceAdapter;
    private boolean isActive = false;
    private Customer customer;
    private String appointmentId;
    private User currentUser, assignedUser;
    private RecyclerView recyclerProducts, recyclerServices;
    private ImageView image;
    private Button btnCheckout, btnAssign, btnStatus;
    private LinearLayout listOfProducts, listOfServices;
    private ImageButton btnToggleProduct, btnToggleService;
    private TextView name, email, phone;
    private TextView txtAddProduct, txtAddService, txtTotal;
    private TextView txtShowMoreProduct, txtShowMoreService;
    private TextView txtTime, txtAddress, txtNote, txtAssign;

    @Override
    public void onResume() {
        super.onResume();
        ((MainActivity) getActivity()).hideBottomNavigation(true);
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_appointment_detail, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        if (getArguments() != null) {
            appointmentId = getArguments().getString("appointmentId");
            Log.d("DEBUG", "appoinmentid: " + appointmentId);
        }

        initViews(view);
        initAdapter();
        initList();
        observerViewModel();
        setupListener();
    }

    private void setupListener() {
        btnStatus.setOnClickListener(v -> {
            Appointment appointment = viewModel.getAppointmentDetail().getValue();
            if (appointment == null) return;

            AppointmentStatus current = appointment.getStatus();

            // Chọn trạng thái mới
            String[] statuses = Arrays.stream(AppointmentStatus.values()).map(Enum::name).toArray(String[]::new);

            new AlertDialog.Builder(requireContext()).setTitle("Chọn trạng thái cuộc hẹn").setItems(statuses, (dialog, which) -> {
                AppointmentStatus selectedStatus = AppointmentStatus.valueOf(statuses[which]);
                if (selectedStatus.compareTo(current) < 1) {
                    Toast.makeText(requireContext(), "Trạng thái mới phải lớn hơn hiện tại", Toast.LENGTH_SHORT).show();
                    return;
                }
                viewModel.updateAppointmentStatus(selectedStatus, new RepositoryCallback() {
                    @Override
                    public void onSuccess(String message) {
                        Toast.makeText(requireContext(), "Cập nhật thành công: " + appointment.getStatus(), Toast.LENGTH_SHORT).show();
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

        btnAssign.setOnClickListener(v -> {
            viewModel.setAssignedUser(currentUser.getId(), new RepositoryCallback() {
                @Override
                public void onSuccess(String message) {
                    Toast.makeText(getContext(), "Đăng ký thành công!", Toast.LENGTH_SHORT).show();
//                    viewModel.updateAppointmentStatus(AppointmentStatus.CONFIRMED, new RepositoryCallback() {
                    viewModel.updateAppointmentStatus(AppointmentStatus.IN_PROGRESS, new RepositoryCallback() {
                        @Override
                        public void onSuccess(String message) {
                            Toast.makeText(getContext(), "Cập nhật trạng thái! " + message, Toast.LENGTH_SHORT).show();
                        }

                        @Override
                        public void onError(Throwable t) {
                            Toast.makeText(getContext(), "Không thể cập nhật trạng thái! " + t.getMessage(), Toast.LENGTH_SHORT).show();
                        }
                    });
                    observerViewModel();
                }

                @Override
                public void onError(Throwable t) {
                    Toast.makeText(getContext(), "Đăng ký thất bại!" + t.getMessage(), Toast.LENGTH_SHORT).show();
                }
            });
        });

        btnCheckout.setOnClickListener(v -> {
            DialogUtils.showCreateConfirmation(getContext(), new DialogUtils.OnConfirmationListener() {
                @Override
                public void onConfirmed() {
                    Bundle bundle = new Bundle();
                    bundle.putBoolean("fromOrder", false);
                    bundle.putBoolean("fromAppointment", true);
                    appointmentViewModel.setAppointmentLiveData(viewModel.getAppointmentDetail().getValue());

                    productViewModel.resetClearFlag();
                    serviceViewModel.resetClearFlag();

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

        txtAddProduct.setOnClickListener(v -> {
            ((MainActivity) requireActivity()).navigateToWithBackStack(R.id.selectProductFragment, null);
        });

        txtAddService.setOnClickListener(v -> {
            ((MainActivity) requireActivity()).navigateToWithBackStack(R.id.selectServiceFragment, null);
        });
    }

    private void observerViewModel() {
        SelectedCustomerViewModel selectedCustomerVM = new ViewModelProvider(requireActivity()).get(SelectedCustomerViewModel.class);
        selectedCustomerVM.getSelectedCustomer().observe(getViewLifecycleOwner(), customer -> {
            this.customer = customer;
            showCustomerInfo();
        });

        viewModel = new ViewModelProvider(requireActivity()).get(AppointmentDetailViewModel.class);
        createBillViewModel = new ViewModelProvider(requireActivity()).get(SharedOrderViewModel.class);
        appointmentViewModel = new ViewModelProvider(requireActivity()).get(SharedAppointmentViewModel.class);

        createBillViewModel.resetOrderLiveData();
        appointmentViewModel.resetAppointmentLiveData();

        productViewModel = new ViewModelProvider(requireActivity()).get(SharedProductViewModel.class);
        serviceViewModel = new ViewModelProvider(requireActivity()).get(SharedServiceViewModel.class);

        // 1. Quan sát toàn bộ product/service để tính giá trị sau
//        productViewModel.getAllProducts().observe(getViewLifecycleOwner(), list -> {
//            if (list != null) {
        viewModel.setProductList(productViewModel.getAllBranchProducts().getValue());
        viewModel.updateStockForAllProductList(productViewModel.getAllProducts().getValue()); //
//            }
//        });

        serviceViewModel.getServices().observe(getViewLifecycleOwner(), list -> {
            if (list != null) {
                viewModel.setServiceList(list);
            }
        });

        viewModel.loadAppointmentDetail(appointmentId);

        // 2. Quan sát appointment
        viewModel.getAppointmentDetail().observe(getViewLifecycleOwner(), appointment -> {
            if (appointment == null) return;

            appointmentViewModel.setAppointmentLiveData(appointment);
            if (appointment.getOrder() != null) {
                createBillViewModel.setOrderLiveData(appointment.getOrder());
            }

            txtTime.setText("Time: " + appointment.getScheduledTime().toDateTimeString());
            txtAddress.setText("Address: " + appointment.getCustomerAddress());
            txtNote.setText("Note: " + appointment.getNote());

            // Đồng bộ danh sách đã chọn dựa vào dữ liệu trong appointment
            if (appointment.getOrder() != null && appointment.getOrder().getProducts() != null) {
                productViewModel.setSelectedProductsById(appointment.getOrder().getProducts());
            }

            if (appointment.getServices() != null) {
                serviceViewModel.setSelectedServicesById(appointment.getServices());
            }

            viewModel.loadAssignedUser(); // luôn gọi để gán user nếu có

            checkAssignUI(appointment); // xử lý enable/disable
        });

        // 3. Quan sát user đăng nhập
        UserProfileViewModel userVM = new ViewModelProvider(requireActivity()).get(UserProfileViewModel.class);
        userVM.getUser().observe(getViewLifecycleOwner(), user -> {
            currentUser = user;
            checkAssignUI(viewModel.getAppointmentDetail().getValue());
        });

        // 4. Quan sát nhân viên phụ trách
        viewModel.getAssignedUser().observe(getViewLifecycleOwner(), user -> {
            if (user != null) {
                assignedUser = user;
                txtAssign.setText("Assign to: " + user.getName() + " (ID: " + user.getId() + ")");
            }
        });

        // 5. Cập nhật số lượng
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

    @SuppressLint("DefaultLocale")
    private void checkAssignUI(Appointment appointment) {
        if (appointment == null || currentUser == null) return;

        String staffId = appointment.getEmployeeId();
        btnStatus.setText(appointment.getStatus().toString());
        updateTotalPrice();

        if ("0".equals(staffId)) {
            // Chưa có người nhận → ẩn chức năng
            btnAssign.setEnabled(true); // Bật để đăng ký
            btnAssign.setBackgroundResource(R.drawable.draw_enable_button);
            btnStatus.setEnabled(false);
            btnStatus.setBackgroundResource(R.drawable.draw_disable_button);
            txtAddProduct.setEnabled(false);
            txtAddService.setEnabled(false);
            btnCheckout.setEnabled(false);
            btnCheckout.setBackgroundResource(R.drawable.draw_disable_button);
            productAdapter.setActiveButtons(false);
            serviceAdapter.setActiveButtons(false);
            txtAssign.setText("Assigned to: None");

        } else {
            if (assignedUser != null) {
                txtAssign.setText("Assigned to: " + assignedUser.getName() + "(ID: " + assignedUser.getId() + ")");
                if (staffId.equals(currentUser.getId())) {
                    // Người đang đăng nhập là người phụ trách
                    productAdapter.setActiveButtons(true);
                    serviceAdapter.setActiveButtons(true);
                    btnAssign.setVisibility(View.GONE);
                    btnAssign.setEnabled(false);
                    btnAssign.setBackgroundResource(R.drawable.draw_disable_button);
                    txtAddProduct.setEnabled(true);
                    txtAddService.setEnabled(true);
                    if (appointment.getStatus().equals(AppointmentStatus.COMPLETED)) {
                        btnCheckout.setEnabled(false);
                        btnCheckout.setBackgroundResource(R.drawable.draw_disable_button);
                        btnStatus.setEnabled(false);
                        btnStatus.setBackgroundResource(R.drawable.draw_disable_button);
                        productAdapter.setActiveButtons(false);
                        serviceAdapter.setActiveButtons(false);
                    } else {
                        productAdapter.setActiveButtons(true);
                        serviceAdapter.setActiveButtons(true);
//                        if (appointment.getStatus().equals(AppointmentStatus.CONFIRMED)) {
                        if (appointment.getStatus().equals(AppointmentStatus.IN_PROGRESS)) {
                            productAdapter.setActiveButtons(false);
                            serviceAdapter.setActiveButtons(false);
                        }

                        btnStatus.setEnabled(true);
                        btnStatus.setBackgroundResource(R.drawable.draw_enable_button);
                        btnCheckout.setEnabled(true);
                        btnCheckout.setBackgroundResource(R.drawable.draw_enable_button);
                    }
                } else {
                    // Người khác phụ trách
                    btnAssign.setEnabled(false);
                    btnAssign.setVisibility(View.GONE);
                    btnAssign.setBackgroundResource(R.drawable.draw_disable_button);
                    btnStatus.setEnabled(false);
                    btnStatus.setBackgroundResource(R.drawable.draw_disable_button);
                    txtAddProduct.setEnabled(false);
                    txtAddService.setEnabled(false);
                    btnCheckout.setEnabled(false);
                    btnCheckout.setBackgroundResource(R.drawable.draw_disable_button);
                    productAdapter.setActiveButtons(false);
                    serviceAdapter.setActiveButtons(false);
                }
            }
        }
    }


    private void initList() {
        setupToggleSection(SectionType.PRODUCT, listOfProducts, btnToggleProduct, () -> viewModel.loadAppointmentDetail(appointmentId));

        setupToggleSection(SectionType.SERVICE, listOfServices, btnToggleService, () -> viewModel.loadAppointmentDetail(appointmentId));
    }

    private void initAdapter() {
        productAdapter = new AppointmentProductAdapter(isActive, (product, newQty) -> {
            updateTotalPrice();
        });
        serviceAdapter = new AppointmentServiceAdapter(isActive, (service, newQty) -> {
            updateTotalPrice();
        });

        recyclerProducts.setAdapter(productAdapter);
        recyclerServices.setAdapter(serviceAdapter);
    }

    private void updateTotalPrice() {
        float total = 0f;

        for (Product p : productViewModel.getSelectedProducts().getValue()) {
            total += p.getPrice() * p.getQuantity();
        }

        for (Service s : serviceViewModel.getSelectedServices().getValue()) {
            total += s.getPrice() * s.getQuantity();
        }
        txtTotal.setText(String.format("Total: %.2f$", total));
    }

    private void showCustomerInfo() {
        image.setImageResource(R.drawable.temp_avatar);
        name.setText(customer.getName());
        email.setText(customer.getEmail());
        phone.setText(customer.getPhoneNumber());
    }

    private void initViews(View view) {
        recyclerProducts = view.findViewById(R.id.recyclerProducts);
        recyclerServices = view.findViewById(R.id.recyclerServices);

        image = view.findViewById(R.id.customerImage);
        name = view.findViewById(R.id.txtName);
        email = view.findViewById(R.id.txtEmail);
        phone = view.findViewById(R.id.txtPhone);

        btnCheckout = view.findViewById(R.id.btnCheckout);
        btnAssign = view.findViewById(R.id.btnAssign);
        btnStatus = view.findViewById(R.id.btnStatus);

        listOfProducts = view.findViewById(R.id.listOfProducts);
        listOfServices = view.findViewById(R.id.listOfServices);

        btnToggleProduct = view.findViewById(R.id.btnToggleProduct);
        btnToggleService = view.findViewById(R.id.btnToggleService);

        txtTotal = view.findViewById(R.id.txtTotal);
        txtAddProduct = view.findViewById(R.id.txtAddProduct);
        txtAddService = view.findViewById(R.id.txtAddService);
        txtShowMoreProduct = view.findViewById(R.id.txtShowMoreProducts);
        txtShowMoreService = view.findViewById(R.id.txtShowMoreServices);

        txtTime = view.findViewById(R.id.txtTime);
        txtAddress = view.findViewById(R.id.txtAddress);
        txtNote = view.findViewById(R.id.txtNote);
        txtAssign = view.findViewById(R.id.txtAssign);

        recyclerProducts.setLayoutManager(new LinearLayoutManager(getContext()));
        recyclerServices.setLayoutManager(new LinearLayoutManager(getContext()));
    }


    enum SectionType {
        PRODUCT, SERVICE
    }

    private SectionType currentOpenSection = null;

    private void setupToggleSection(SectionType section, LinearLayout contentLayout, ImageButton toggleButton, Runnable loadDataIfNeeded) {
        toggleButton.setOnClickListener(v -> {
            if (currentOpenSection == section) {
                contentLayout.setVisibility(View.GONE);
                toggleButton.setImageResource(R.drawable.ic_chevron_right);
                currentOpenSection = null;
            } else {
//                // Đóng tất cả trước
//                closeAllSections();

                // Mở section này
                contentLayout.setVisibility(View.VISIBLE);
                toggleButton.setImageResource(R.drawable.ic_chevron_down);
                currentOpenSection = section;

                // Lazy load
                loadDataIfNeeded.run();
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