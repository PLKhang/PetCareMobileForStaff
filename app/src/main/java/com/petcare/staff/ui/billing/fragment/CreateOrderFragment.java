package com.petcare.staff.ui.billing.fragment;

import android.app.DatePickerDialog;
import android.app.TimePickerDialog;
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
import com.petcare.staff.data.model.ui.Appointment;
import com.petcare.staff.data.model.ui.Customer;
import com.petcare.staff.data.model.ui.Order;
import com.petcare.staff.data.model.ui.Product;
import com.petcare.staff.data.model.ui.Service;
import com.petcare.staff.data.repository.OrderRepository;
import com.petcare.staff.ui.appointment.adapter.AppointmentProductAdapter;
import com.petcare.staff.ui.appointment.adapter.AppointmentServiceAdapter;
import com.petcare.staff.ui.appointment.viewmodel.AppointmentListViewModel;
import com.petcare.staff.ui.appointment.viewmodel.SharedAppointmentViewModel;
import com.petcare.staff.ui.billing.viewmodel.SharedOrderViewModel;
import com.petcare.staff.ui.billing.viewmodel.SharedProductViewModel;
import com.petcare.staff.ui.billing.viewmodel.SharedServiceViewModel;
import com.petcare.staff.ui.common.repository.RepositoryCallback;
import com.petcare.staff.ui.customer.CustomerSelectionMode;
import com.petcare.staff.ui.customer.viewmodel.SelectedCustomerViewModel;
import com.petcare.staff.ui.home.viewmodel.BranchViewModel;
import com.petcare.staff.ui.userprofile.viewmodel.UserProfileViewModel;
import com.petcare.staff.utils.DateTime;
import com.petcare.staff.utils.DialogUtils;

import java.util.Calendar;
import java.util.List;
import java.util.Locale;

public class CreateOrderFragment extends Fragment {
    SharedProductViewModel productViewModel;
    SharedServiceViewModel serviceViewModel;
    SharedOrderViewModel sharedOrderViewModel;
    SharedAppointmentViewModel sharedAppointmentViewModel;
    SelectedCustomerViewModel selectedCustomerViewModel;
    BranchViewModel branchViewModel;
    Customer customer;
    ImageView customerImage;
    TextView txtName, txtEmail, txtPhone;
    LinearLayout listOfServices, listOfProducts;
    RecyclerView recyclerProducts, recyclerServices;
    AppointmentProductAdapter productAdapter;
    AppointmentServiceAdapter serviceAdapter;
    ImageButton btnToggleProduct, btnToggleService;
    TextView txtAddProduct, txtShowMoreProduct, txtAddService, txtShowMoreServices;
    TextView txtTotal, txtSelectTime, txtSelectDate, txtAddress;
    Button btnCreate, btnChooseCustomer;
    Calendar calendar = Calendar.getInstance();

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        initViews(view);
        initAdapters();
        initList();
        observeViewModel();
        setupListener();
    }

    private void setupListener() {
        txtAddProduct.setOnClickListener(v -> {
            ((MainActivity) requireActivity()).navigateToWithBackStack(R.id.selectProductFragment, null);
        });

        txtAddService.setOnClickListener(v -> {
            ((MainActivity) requireActivity()).navigateToWithBackStack(R.id.selectServiceFragment, null);
        });

        txtShowMoreProduct.setOnClickListener(v -> {
            boolean hasMore = productAdapter.showMore();
            if (!hasMore) {
                txtShowMoreProduct.setVisibility(View.GONE);
            }
        });

        txtShowMoreServices.setOnClickListener(v -> {
            boolean hasMore = serviceAdapter.showMore();
            if (!hasMore) {
                txtShowMoreServices.setVisibility(View.GONE);
            }
        });

        txtSelectDate.setOnClickListener(v -> showDatePicker());
        txtSelectTime.setOnClickListener(v -> showTimePicker());

        btnCreate.setOnClickListener(v -> {
            DialogUtils.showCreateConfirmation(requireActivity(), new DialogUtils.OnConfirmationListener() {
                @Override
                public void onConfirmed() {
                    if (customer != null) {
                        if (serviceViewModel.getSelectedServices().getValue().size() == 0) {
                            createOrder("0",
                                    branchViewModel.getBranchInfo().getValue().getId(),
                                    productViewModel.getSelectedProducts().getValue());
                        } else {
                            createAppointment();
                        }
                    } else {
                        Toast.makeText(getContext(), "Phải chọn khách hàng trước!", Toast.LENGTH_SHORT).show();
                    }
                }
            });
        });

        btnChooseCustomer.setOnClickListener(v -> {
            selectedCustomerViewModel.setSelectionMode(CustomerSelectionMode.CREATE_BILL);

//            productViewModel.resetClearFlag();
//            serviceViewModel.resetClearFlag();

            ((MainActivity) requireActivity()).navigateToWithBackStack(R.id.customerListFragment, null);
        });
    }

    private void showTimePicker() {
        int hour = calendar.get(Calendar.HOUR_OF_DAY);
        int minute = calendar.get(Calendar.MINUTE);

        TimePickerDialog timePickerDialog = new TimePickerDialog(requireContext(),
                (view, hourOfDay, minute1) -> {
                    String formattedTime = String.format(Locale.getDefault(), "%02d:%02d", hourOfDay, minute1);
                    txtSelectTime.setText(formattedTime);
                    sharedOrderViewModel.setSelectedTime(formattedTime);
                },
                hour, minute, true
        );

        timePickerDialog.show();
    }

    private void showDatePicker() {
        int year = calendar.get(Calendar.YEAR);
        int month = calendar.get(Calendar.MONTH);
        int day = calendar.get(Calendar.DAY_OF_MONTH);

        DatePickerDialog datePickerDialog = new DatePickerDialog(
                getContext(),
                (view, selectedYear, selectedMonth, selectedDay) -> {
                    // Format lại ngày: thêm 1 vào month vì month bắt đầu từ 0
                    String selectedDate = String.format("%02d/%02d/%04d", selectedDay, selectedMonth + 1, selectedYear);
                    txtSelectDate.setText(selectedDate);
                    sharedOrderViewModel.setSelectedDate(selectedDate);
                },
                year, month, day
        );

        datePickerDialog.show();
    }

    private void observeViewModel() {
        selectedCustomerViewModel = new ViewModelProvider(requireActivity()).get(SelectedCustomerViewModel.class);
        selectedCustomerViewModel.getSelectedCustomer().observe(getViewLifecycleOwner(), customer -> {
            if (customer != null) {
                this.customer = customer;
                showCustomerInfo();
            }
        });

        sharedOrderViewModel = new ViewModelProvider(requireActivity()).get(SharedOrderViewModel.class);
        sharedAppointmentViewModel = new ViewModelProvider(requireActivity()).get(SharedAppointmentViewModel.class);

        sharedOrderViewModel.resetOrderLiveData();
        sharedAppointmentViewModel.resetAppointmentLiveData();

        branchViewModel = new ViewModelProvider(requireActivity()).get(BranchViewModel.class);
        branchViewModel.getBranchInfo().observe(getViewLifecycleOwner(), info -> {
            txtAddress.setText(info.getLocation());
        });

        productViewModel = new ViewModelProvider(requireActivity()).get(SharedProductViewModel.class);
        serviceViewModel = new ViewModelProvider(requireActivity()).get(SharedServiceViewModel.class);

        productViewModel.clearSelectedProducts();
        serviceViewModel.clearSelectedService();

        productViewModel.getSelectedProducts().observe(getViewLifecycleOwner(), list -> {
            if (list != null) {
                productAdapter.setData(list);
                updateTotalPrice(list, serviceViewModel.getSelectedServices().getValue());
            }
        });

        serviceViewModel.getSelectedServices().observe(getViewLifecycleOwner(), list -> {
            if (list != null) {
                serviceAdapter.setData(list);
                updateTotalPrice(productViewModel.getSelectedProducts().getValue(), list);
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

    private void initAdapters() {
        productAdapter = new AppointmentProductAdapter(true, (product, newQty) -> {
            updateTotalPrice(
                    productViewModel.getSelectedProducts().getValue(),
                    serviceViewModel.getSelectedServices().getValue()
            );
        });
        serviceAdapter = new AppointmentServiceAdapter(true, (service, newQty) -> {
            updateTotalPrice(
                    productViewModel.getSelectedProducts().getValue(),
                    serviceViewModel.getSelectedServices().getValue()
            );
        });

        recyclerProducts.setAdapter(productAdapter);
        recyclerServices.setAdapter(serviceAdapter);
    }

    private void updateTotalPrice(List<Product> products, List<Service> services) {
        float total = 0f;

        if (products != null) {
            for (Product product : products) {
                total += product.getPrice() * product.getQuantity(); // quantity phải được cập nhật đúng
            }
        }

        if (services != null) {
            for (Service service : services) {
                total += service.getPrice() * service.getQuantity();
            }
        }
        sharedOrderViewModel.setTotal(total);
        txtTotal.setText(String.format(Locale.getDefault(), "Total: %.2f $", total));
    }

    private void showCustomerInfo() {
        customerImage.setImageResource(R.drawable.temp_avatar);
        txtName.setText(customer.getName());
        txtEmail.setText(customer.getEmail());
        txtPhone.setText(customer.getPhoneNumber());
    }

    private void createAppointment() {
        List<Product> productsList = productViewModel.getSelectedProducts().getValue();
        List<Service> serviceList = serviceViewModel.getSelectedServices().getValue();

        Appointment appointment = new Appointment(
                customer.getId(),
                txtAddress.getText().toString(),
                DateTime.fromDateAndTime(
                        txtSelectDate.getText().toString(),
                        txtSelectTime.getText().toString()
                ),
                serviceList,
                ""
        );

        UserProfileViewModel currentUser = new ViewModelProvider(requireActivity()).get(UserProfileViewModel.class);
        currentUser.getUser().observe(getViewLifecycleOwner(), user -> {
            appointment.setBranchId(user.getBranchId());
        });

        AppointmentListViewModel appointmentListViewModel = new ViewModelProvider(requireActivity()).get(AppointmentListViewModel.class);

        appointmentListViewModel.createAppointment(appointment, new RepositoryCallback() {
            @Override
            public void onSuccess(String appointmentId) {
                appointment.setId(appointmentId);
                sharedAppointmentViewModel.setAppointmentLiveData(appointment);
                createOrder(appointmentId, appointment.getBranchId(), productsList);
            }

            @Override
            public void onError(Throwable t) {
                Toast.makeText(getContext(), "Lỗi tạo lịch hẹn: " + t.getMessage(), Toast.LENGTH_SHORT).show();
            }
        });
    }

    private void createOrder(String appontmentId, String branchId, List<Product> productsList) {
        if (productsList == null || productsList.size() == 0) {
            Toast.makeText(getContext(), "Danh sách sản phẩm trống!", Toast.LENGTH_SHORT).show();
            return;
        }
        Order order = new Order(
                customer.getId(),
                branchId,
                appontmentId,
                productsList
        );
        order.setPickupTime(DateTime.fromDateAndTime(txtSelectDate.getText().toString(), txtSelectTime.getText().toString()));

        OrderRepository orderRepository = new OrderRepository(requireActivity());
        orderRepository.createOrder(order, new RepositoryCallback() {
            @Override
            public void onSuccess(String orderId) {
                order.setId(orderId);

                Toast.makeText(getContext(), "Tạo thành công. OrderID: " + orderId, Toast.LENGTH_SHORT).show();
                productViewModel.resetClearFlag();
                serviceViewModel.resetClearFlag();

                sharedOrderViewModel.setSelectedDate(null);
                sharedOrderViewModel.setSelectedTime(null);
                sharedOrderViewModel.setAddress(null);

                productViewModel.resetClearFlag();
                serviceViewModel.resetClearFlag();

                Bundle bundle = new Bundle();
                bundle.putBoolean("fromOrder", true);
                bundle.putBoolean("fromAppointment", false);
                sharedOrderViewModel.setOrderLiveData(order);
                ((MainActivity) requireActivity()).navigateWithoutBackStack(R.id.checkoutFragment, bundle);
            }

            @Override
            public void onError(Throwable t) {
                Toast.makeText(getContext(), "Lỗi tạo đơn hàng: " + t.getMessage(), Toast.LENGTH_SHORT).show();
            }
        });
    }

    private void initViews(View view) {
        customerImage = view.findViewById(R.id.customerImage);
        txtName = view.findViewById(R.id.txtName);
        txtEmail = view.findViewById(R.id.txtEmail);
        txtPhone = view.findViewById(R.id.txtPhone);
        txtTotal = view.findViewById(R.id.txtTotal);

        btnCreate = view.findViewById(R.id.btnCreate);
        btnChooseCustomer = view.findViewById(R.id.btnChooseCustomer);

        listOfServices = view.findViewById(R.id.listOfServices);
        listOfProducts = view.findViewById(R.id.listOfProducts);

        recyclerProducts = view.findViewById(R.id.recyclerProducts);
        recyclerServices = view.findViewById(R.id.recyclerServices);

        btnToggleProduct = view.findViewById(R.id.btnToggleProduct);
        btnToggleService = view.findViewById(R.id.btnToggleService);

        txtAddProduct = view.findViewById(R.id.txtAddProduct);
        txtShowMoreProduct = view.findViewById(R.id.txtShowMoreProduct);
        txtAddService = view.findViewById(R.id.txtAddService);
        txtShowMoreServices = view.findViewById(R.id.txtShowMoreServices);

        txtSelectTime = view.findViewById(R.id.txtTime);
        txtSelectDate = view.findViewById(R.id.txtDate);
        txtAddress = view.findViewById(R.id.txtAddress);
        txtAddress.setEnabled(false);

        recyclerProducts.setLayoutManager(new LinearLayoutManager(getContext()));
        recyclerServices.setLayoutManager(new LinearLayoutManager(getContext()));

        txtSelectDate.setText(new DateTime().toString());
        txtSelectTime.setText("12:00");
    }

    @Override
    public void onResume() {
        super.onResume();
        ((MainActivity) getActivity()).hideBottomNavigation(true);
        btnToggleProduct.performClick();
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_create_bill, container, false);
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