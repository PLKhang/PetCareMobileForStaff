package com.petcare.staff.ui.appointment.fragment;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.app.DatePickerDialog;
import android.app.TimePickerDialog;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
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
import com.petcare.staff.ui.billing.viewmodel.SharedProductViewModel;
import com.petcare.staff.ui.billing.viewmodel.SharedServiceViewModel;
import com.petcare.staff.ui.common.repository.RepositoryCallback;
import com.petcare.staff.ui.customer.viewmodel.SelectedCustomerViewModel;
import com.petcare.staff.ui.userprofile.viewmodel.UserProfileViewModel;
import com.petcare.staff.utils.DateTime;
import com.petcare.staff.utils.DialogUtils;
import com.petcare.staff.utils.SharedPrefManager;

import java.util.Calendar;
import java.util.List;
import java.util.Locale;

public class AddAppointmentFragment extends Fragment {
    SharedProductViewModel productViewModel;
    SharedServiceViewModel serviceViewModel;
    SharedAppointmentViewModel sharedAppointmentVM;
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
    Button btnCreate;
    Calendar calendar = Calendar.getInstance();

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_add_appointment, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        initViews(view);
        showCustomerInfo();
        initAdapters();
        initList();
        observeViewModel();
        setupListener();
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
        appointment.setBranchId(currentUser.getUser().getValue().getBranchId());

        AppointmentListViewModel appointmentListViewModel = new ViewModelProvider(requireActivity()).get(AppointmentListViewModel.class);

        appointmentListViewModel.createAppointment(appointment, new RepositoryCallback() {
            @Override
            public void onSuccess(String appointmentId) {
                appointment.setId(appointmentId);

                Order order = new Order(
                        customer.getId(),
                        String.valueOf(SharedPrefManager.getBranchId(requireActivity())),
                        "",
                        productsList
                );
                order.setAppointment_id(appointmentId); // Gán appointmentId vào đơn hàng

                OrderRepository orderRepository = new OrderRepository(requireActivity());
                orderRepository.createOrder(order, new RepositoryCallback() {
                    @Override
                    public void onSuccess(String orderId) {
                        order.setId(orderId);
                        appointment.setOrder(order);

                        Toast.makeText(getContext(), "Tạo thành công. OrderID: " + orderId, Toast.LENGTH_SHORT).show();
                        productViewModel.resetClearFlag();
                        serviceViewModel.resetClearFlag();

                        sharedAppointmentVM.setSelectedDate(null);
                        sharedAppointmentVM.setSelectedTime(null);
                        sharedAppointmentVM.setAddress(null);

                        Bundle bundle = new Bundle();
                        bundle.putSerializable("appointmentId", appointmentId);
                        ((MainActivity) requireActivity()).navigateWithoutBackStack(R.id.appointmentDetailFragment, bundle);
                    }

                    @Override
                    public void onError(Throwable t) {
                        Toast.makeText(getContext(), "Lỗi tạo hóa đơn: " + t.getMessage(), Toast.LENGTH_SHORT).show();
                    }
                });
            }

            @Override
            public void onError(Throwable t) {
                Toast.makeText(getContext(), "Lỗi tạo lịch hẹn: " + t.getMessage(), Toast.LENGTH_SHORT).show();
            }
        });
    }


    private void observeViewModel() {
        productViewModel = new ViewModelProvider(requireActivity()).get(SharedProductViewModel.class);
        serviceViewModel = new ViewModelProvider(requireActivity()).get(SharedServiceViewModel.class);
        productViewModel.clearSelectedProducts();
        serviceViewModel.clearSelectedService();

        productViewModel.getSelectedProducts().observe(getViewLifecycleOwner(), list -> {
            if (list != null) {
                productAdapter.setData(list);
            }
        });

        serviceViewModel.getSelectedServices().observe(getViewLifecycleOwner(), list -> {
            if (list != null) {
                serviceAdapter.setData(list);
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
        txtAddress.addTextChangedListener(new TextWatcher() {
            @Override
            public void afterTextChanged(Editable s) {
                sharedAppointmentVM.setAddress(s.toString());
            }

            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
            }
        });

        btnCreate.setOnClickListener(v -> {
            DialogUtils.showCreateConfirmation(requireActivity(), this::createAppointment);
        });
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

    private void initViews(View view) {
        customerImage = view.findViewById(R.id.customerImage); // Đảm bảo id là "image"
        txtName = view.findViewById(R.id.txtName);
        txtEmail = view.findViewById(R.id.txtEmail);
        txtPhone = view.findViewById(R.id.txtPhone);
        txtTotal = view.findViewById(R.id.txtTotal);

        btnCreate = view.findViewById(R.id.btnCreate);

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

        recyclerProducts.setLayoutManager(new LinearLayoutManager(getContext()));
        recyclerServices.setLayoutManager(new LinearLayoutManager(getContext()));

        sharedAppointmentVM = new ViewModelProvider(requireActivity()).get(SharedAppointmentViewModel.class);

        // Khôi phục dữ liệu nếu có
        if (sharedAppointmentVM.getSelectedDate().getValue() != null)
            txtSelectDate.setText(sharedAppointmentVM.getSelectedDate().getValue());

        if (sharedAppointmentVM.getSelectedTime().getValue() != null)
            txtSelectTime.setText(sharedAppointmentVM.getSelectedTime().getValue());

        if (sharedAppointmentVM.getAddress().getValue() != null)
            txtAddress.setText(sharedAppointmentVM.getAddress().getValue());

        if (sharedAppointmentVM.getTotal().getValue() != null)
            txtTotal.setText(sharedAppointmentVM.getTotal().getValue().toString());
    }

    private void showTimePicker() {
        int hour = calendar.get(Calendar.HOUR_OF_DAY);
        int minute = calendar.get(Calendar.MINUTE);

        TimePickerDialog timePickerDialog = new TimePickerDialog(requireContext(),
                (view, hourOfDay, minute1) -> {
                    String formattedTime = String.format(Locale.getDefault(), "%02d:%02d", hourOfDay, minute1);
                    txtSelectTime.setText(formattedTime);
                    sharedAppointmentVM.setSelectedTime(formattedTime);
                },
                hour, minute, true // dùng true nếu bạn muốn định dạng 24h
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
                    sharedAppointmentVM.setSelectedDate(selectedDate);
                },
                year, month, day
        );

        datePickerDialog.show();
    }

    private void showCustomerInfo() {
        SelectedCustomerViewModel selectedCustomerVM = new ViewModelProvider(requireActivity()).get(SelectedCustomerViewModel.class);
        customer = selectedCustomerVM.getSelectedCustomer();

        customerImage.setImageResource(R.drawable.temp_avatar);
        txtName.setText(customer.getName());
        txtEmail.setText(customer.getEmail());
        txtPhone.setText(customer.getPhoneNumber());
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
        sharedAppointmentVM.setTotal(total);
        txtTotal.setText(String.format(Locale.getDefault(), "Total: %.2f $", total));
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
                closeAllSections();

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