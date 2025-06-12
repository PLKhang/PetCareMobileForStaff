package com.petcare.staff.ui.billing.fragment;

import android.app.AlertDialog;
import android.content.Intent;
import android.net.Uri;
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
import com.petcare.staff.data.model.api.payment.PaymentMethod;
import com.petcare.staff.data.model.api.payment.PaymentStatus;
import com.petcare.staff.data.model.ui.Appointment;
import com.petcare.staff.data.model.ui.Bill;
import com.petcare.staff.data.model.ui.Customer;
import com.petcare.staff.data.model.ui.Order;
import com.petcare.staff.ui.appointment.viewmodel.SharedAppointmentViewModel;
import com.petcare.staff.ui.billing.adapter.ProductAdapter;
import com.petcare.staff.ui.billing.adapter.ServiceAdapter;
import com.petcare.staff.ui.billing.viewmodel.ConfirmPaymentViewModel;
import com.petcare.staff.ui.billing.viewmodel.SharedOrderViewModel;
import com.petcare.staff.ui.billing.viewmodel.SharedProductViewModel;
import com.petcare.staff.ui.common.repository.RepositoryCallback;
import com.petcare.staff.ui.customer.viewmodel.SelectedCustomerViewModel;
import com.petcare.staff.ui.home.viewmodel.BranchViewModel;
import com.petcare.staff.utils.DialogUtils;

import java.util.Arrays;

public class CheckoutFragment extends Fragment {
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
    private Button btnPaymentMethod, btnConfirm;
    private boolean isReturningFromBank = false;

    @Override
    public void onResume() {
        super.onResume();
        ((MainActivity) getActivity()).hideBottomNavigation(true);

        if (isReturningFromBank) {
            paymentViewModel.loadPayment();
            Toast.makeText(getContext(), "Trạng thái hóa đơn: " + paymentViewModel.getPayment().getValue().getStatus(), Toast.LENGTH_SHORT).show();
            if (paymentViewModel.getPayment().getValue().getStatus() == PaymentStatus.COMPLETED) {

                paymentViewModel.setIsCreated(false);
                ((MainActivity) requireActivity()).navigateToClearStack(R.id.confirmPaymentFragment, null);
            }
            isReturningFromBank = false;
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_checkout, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        // bundle set info ****
        paymentViewModel = new ViewModelProvider(requireActivity()).get(ConfirmPaymentViewModel.class);
        paymentViewModel.setIsCreated(false);
        initViews(view);
        createPayment();
        initAdapter();
        initList();
        observerViewModel();
        setupListener();
    }

    private void observerViewModel() {
        customerViewModel = new ViewModelProvider(requireActivity()).get(SelectedCustomerViewModel.class);

        createBillViewModel = new ViewModelProvider(requireActivity()).get(SharedOrderViewModel.class);
        appointmentViewModel = new ViewModelProvider(requireActivity()).get(SharedAppointmentViewModel.class);
        customerViewModel.getSelectedCustomer().observe(getViewLifecycleOwner(), customer -> {
            this.customer = customer;
            showCustomerInfo();
        });

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

//        paymentViewModel.loadPayment();

        paymentViewModel.getPayment().observe(getViewLifecycleOwner(), payment -> {
            if (payment != null) {
                PaymentMethod method = payment.getPaymentMethod();
                if (method != null) {
                    btnPaymentMethod.setText(method.toString());
                } else {
                    btnPaymentMethod.setText("CASH");
                }
            }
        });

        if (getArguments() != null) {
            boolean fromAppointment = getArguments().getBoolean("fromAppointment");
            boolean fromOrder = getArguments().getBoolean("fromOrder");
            if (fromOrder) {
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

            if (fromAppointment) {
                Appointment appointment = appointmentViewModel.getAppointmentLiveData().getValue();
                txtAddress.setText(appointment.getCustomerAddress());
                txtTime.setText(appointment.getScheduledTime().toDateTimeString());
                txtNote.setText(appointment.getNote());
            }
        }
    }

    private void showCustomerInfo() {
        image.setImageResource(R.drawable.temp_avatar);
        name.setText(customer.getName());
        email.setText(customer.getEmail());
        phone.setText(customer.getPhoneNumber());
    }

    private void setupListener() {
        btnPaymentMethod.setOnClickListener(v -> {
            Bill bill = paymentViewModel.getPayment().getValue();
            if (bill == null) return;
            String[] methods = Arrays.stream(PaymentMethod.values())
                    .map(Enum::name)
                    .toArray(String[]::new);

            new AlertDialog.Builder(requireContext())
                    .setTitle("Chọn phương thức thanh toán")
                    .setItems(methods, (dialog, which) -> {
                        PaymentMethod selectedMethod = PaymentMethod.valueOf(methods[which]);
                        paymentViewModel.updatePaymentMethod(selectedMethod, new RepositoryCallback() {
                            @Override
                            public void onSuccess(String message) {
                                Toast.makeText(requireContext(), "Cập nhật thành công: " + selectedMethod, Toast.LENGTH_SHORT).show();
                                Bill check = paymentViewModel.getPayment().getValue();
                                check.setPaymentMethod(selectedMethod);
                                paymentViewModel.setPayment(check);
                                if (selectedMethod == PaymentMethod.BANK) {
                                    paymentViewModel.createPaymentUrl();
                                }
                            }

                            @Override
                            public void onError(Throwable t) {
                                Toast.makeText(requireContext(), "Cập nhật thất bại", Toast.LENGTH_SHORT).show();
                            }
                        });
                    })
                    .setNegativeButton("Hủy", null)
                    .show();
        });

        btnConfirm.setOnClickListener(v -> {
            DialogUtils.showCreateConfirmation(getContext(), this::confirmPayment);
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

        btnPaymentMethod = view.findViewById(R.id.btnPaymentMethod);
        btnConfirm = view.findViewById(R.id.btnConfirm);

        listOfProducts = view.findViewById(R.id.listOfProducts);
        listOfServices = view.findViewById(R.id.listOfServices);

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

    public void createPayment() {
        if (paymentViewModel.getIsCreated() == true) {
            return;
        }
        createBillViewModel = new ViewModelProvider(requireActivity()).get(SharedOrderViewModel.class);
        appointmentViewModel = new ViewModelProvider(requireActivity()).get(SharedAppointmentViewModel.class);

        Order order = createBillViewModel.getOrderLiveData().getValue();
        Appointment appointment = appointmentViewModel.getAppointmentLiveData().getValue();
        float total = 0f;

        SharedProductViewModel calculator = new ViewModelProvider(requireActivity()).get(SharedProductViewModel.class);
        if (order != null) {
            Log.d("Debug_calculator", "Before calculate, product size: " + order.getProducts().size());
            total += calculator.calculatePrice(order.getProducts());
            order.setTotal_price(total);
        }
        if (appointment != null) total += appointment.getTotal();

        txtTotal.setText(String.format("Total: %.2f$", total));

        paymentViewModel.createPayment(order, appointment, total);

        paymentViewModel.setIsCreated(true);
    }

    public void changeMethod(PaymentMethod method) {
        paymentViewModel.updatePaymentMethod(method, new RepositoryCallback() {
            @Override
            public void onSuccess(String message) {
                Toast.makeText(getContext(), "Cập nhật thành công: " + message, Toast.LENGTH_SHORT).show();
            }

            @Override
            public void onError(Throwable t) {
                Toast.makeText(getContext(), "Cập nhật thất bại: " + t.getMessage(), Toast.LENGTH_LONG).show();
            }
        });
    }

    public void confirmPayment() {
        Bill bill = paymentViewModel.getPayment().getValue();
        switch (bill.getPaymentMethod()) {
            case CASH:
                paymentViewModel.updatePaymentStatus(bill.getId(), new RepositoryCallback() {
                    @Override
                    public void onSuccess(String message) {
                        paymentViewModel.setIsCreated(false);
                        bill.setStatus(PaymentStatus.COMPLETED);
                        ((MainActivity) requireActivity()).navigateToClearStack(
                                R.id.confirmPaymentFragment,
                                null
                        );
                    }

                    @Override
                    public void onError(Throwable t) {
                        // handle error
                    }
                });
                break;

            case BANK:
                String url = bill.getPaymentUrl();
                isReturningFromBank = true;
                Intent browserIntent = new Intent(Intent.ACTION_VIEW, Uri.parse(url));
                startActivity(browserIntent);
                break;

            default:
                break;
        }
    }
}