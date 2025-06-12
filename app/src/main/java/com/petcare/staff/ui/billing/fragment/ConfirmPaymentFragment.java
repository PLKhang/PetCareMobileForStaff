package com.petcare.staff.ui.billing.fragment;

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

import com.petcare.staff.MainActivity;
import com.petcare.staff.R;
import com.petcare.staff.data.model.ui.Appointment;
import com.petcare.staff.data.model.ui.Customer;
import com.petcare.staff.data.model.ui.Order;
import com.petcare.staff.ui.appointment.viewmodel.SharedAppointmentViewModel;
import com.petcare.staff.ui.billing.adapter.ProductAdapter;
import com.petcare.staff.ui.billing.adapter.ServiceAdapter;
import com.petcare.staff.ui.billing.viewmodel.ConfirmPaymentViewModel;
import com.petcare.staff.ui.billing.viewmodel.SharedOrderViewModel;
import com.petcare.staff.ui.billing.viewmodel.SharedProductViewModel;
import com.petcare.staff.ui.customer.viewmodel.SelectedCustomerViewModel;
import com.petcare.staff.ui.home.viewmodel.BranchViewModel;
import com.petcare.staff.utils.DialogUtils;

public class ConfirmPaymentFragment extends Fragment {
    private SharedOrderViewModel createBillViewModel;
    private SharedAppointmentViewModel appointmentViewModel;
    private SelectedCustomerViewModel customerViewModel;
    private ConfirmPaymentViewModel paymentViewModel;
    private ProductAdapter productAdapter;
    private ServiceAdapter serviceAdapter;
    private Customer customer;
    private RecyclerView recyclerProducts, recyclerServices;
    private LinearLayout listOfProducts, listOfServices;
    private ImageButton btnToggleProduct, btnToggleService;
    private ImageView image;
    private TextView name, email, phone;
    private TextView txtShowMoreProduct, txtShowMoreService, txtTotal;
    private TextView txtTime, txtAddress, txtNote;
    private Button btnCompleted;

    @Override
    public void onResume() {
        super.onResume();
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_confirm_payment, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        initViews(view);
        initAdapter();
        initList();
        observerViewModel();
        setupListener();
    }

    private void initList() {
        setupToggleSection(
                SectionType.PRODUCT,
                listOfProducts,
                btnToggleProduct
        );

        setupToggleSection(
                SectionType.SERVICE,
                listOfServices,
                btnToggleService
        );
    }

    private void showCustomerInfo() {
        image.setImageResource(R.drawable.temp_avatar);
        name.setText(customer.getName());
        email.setText(customer.getEmail());
        phone.setText(customer.getPhoneNumber());

        if (getArguments() != null) {
            txtAddress.setText(getArguments().getString("address"));
            txtTime.setText(getArguments().getString("time"));
            txtNote.setText(getArguments().getString("note"));
        }

        setTotal();
    }

    private void setupListener() {
        btnCompleted.setOnClickListener(v -> {
            completedPayment();
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

    private void completedPayment() {
        customerViewModel.setSelectedCustomer(customer);
        ((MainActivity) requireActivity()).navigateToClearStack(R.id.customerDetailFragment, null);
    }

    public void setTotal() {
        Order order = createBillViewModel.getOrderLiveData().getValue();
        Appointment appointment = appointmentViewModel.getAppointmentLiveData().getValue();
        float total = 0f;

        boolean haveAppointment = false;

        SharedProductViewModel calculator = new ViewModelProvider(requireActivity()).get(SharedProductViewModel.class);
        if (order != null) {
            total += calculator.calculatePrice(order.getProducts());
            order.setTotal_price(total);
        }
        if (appointment != null) {
            total += appointment.getTotal();
            haveAppointment = true;
        }

        txtTotal.setText(String.format("Total: %.2f$", total));
        setAddressInfo(haveAppointment);
    }

    private void setAddressInfo(boolean haveAppointment) {
        if (haveAppointment) {
            Appointment appointment = appointmentViewModel.getAppointmentLiveData().getValue();

            txtAddress.setText("Address" + appointment.getCustomerAddress());
            txtTime.setText("Time: " + appointment.getScheduledTime().toDateTimeString());
            txtNote.setText("Note: " + appointment.getNote());
        } else {
            Order order = createBillViewModel.getOrderLiveData().getValue();
            BranchViewModel temp = new ViewModelProvider(requireActivity()).get(BranchViewModel.class);
            temp.getBranchInfoById(order.getBranch_id()).observe(getViewLifecycleOwner(), branch -> {
                if (branch != null) {
                    txtAddress.setText(branch.getLocation());
                } else {
                    txtAddress.setText("Location not found!");
                    Log.e("BranchViewModel", "branch is null for orderId: " + order.getId());
                }
            });

            txtTime.setText(order.getPickupTime().toDateTimeString());
            txtNote.setText("Note");
        }
    }

    private void initAdapter() {
        productAdapter = new ProductAdapter();
        serviceAdapter = new ServiceAdapter();

        recyclerProducts.setAdapter(productAdapter);
        recyclerServices.setAdapter(serviceAdapter);
    }

    private void observerViewModel() {
        paymentViewModel = new ViewModelProvider(requireActivity()).get(ConfirmPaymentViewModel.class);
        customerViewModel = new ViewModelProvider(requireActivity()).get(SelectedCustomerViewModel.class);
        createBillViewModel = new ViewModelProvider(requireActivity()).get(SharedOrderViewModel.class);
        appointmentViewModel = new ViewModelProvider(requireActivity()).get(SharedAppointmentViewModel.class);

        createBillViewModel.getOrderLiveData().observe(getViewLifecycleOwner(), order -> {
            if (order != null) {
                productAdapter.setData(order.getProducts());
            }
        });
        appointmentViewModel.getAppointmentLiveData().observe(getViewLifecycleOwner(), appointment -> {
            if (appointment != null) {
                serviceAdapter.setData(appointment.getServices());
            }
        });
        customerViewModel.getSelectedCustomer().observe(getViewLifecycleOwner(), customer -> {
            this.customer = customer;
            showCustomerInfo();
        });
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

        btnToggleProduct = view.findViewById(R.id.btnToggleProduct);
        btnToggleService = view.findViewById(R.id.btnToggleService);

        btnCompleted = view.findViewById(R.id.btnCompleted);

        txtTotal = view.findViewById(R.id.txtTotal);
        txtShowMoreProduct = view.findViewById(R.id.txtShowMoreProduct);
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

    private void setupToggleSection(
            SectionType section,
            LinearLayout contentLayout,
            ImageButton toggleButton
    ) {
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