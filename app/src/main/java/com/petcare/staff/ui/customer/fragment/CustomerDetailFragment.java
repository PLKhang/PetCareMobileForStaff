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
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.petcare.staff.MainActivity;
import com.petcare.staff.R;
import com.petcare.staff.data.model.ui.Customer;
import com.petcare.staff.ui.customer.adapter.AppointmentAdapter;
import com.petcare.staff.ui.customer.adapter.BillAdapter;
import com.petcare.staff.ui.customer.adapter.OrderAdapter;
import com.petcare.staff.ui.customer.adapter.PetAdapter;
import com.petcare.staff.ui.customer.viewmodel.CustomerDetailViewModel;
import com.petcare.staff.ui.customer.viewmodel.SelectedCustomerViewModel;
import com.petcare.staff.ui.pet.viewmodel.SelectedPetViewModel;

import java.util.ArrayList;

public class CustomerDetailFragment extends Fragment {
    private CustomerDetailViewModel customerDetailVM;
    private RecyclerView recyclerPets, recyclerOrders, recyclerAppointments, recyclerBills;
    private AppointmentAdapter appointmentAdapter;
    private BillAdapter billAdapter;
    private OrderAdapter orderAdapter;
    private PetAdapter petAdapter;
    private Customer customer;
    ImageView image;
    Button btnCreateOrder;
    private LinearLayout listOfPet, listOfAppointment, listOfOrder, listOfBill;
    private ImageButton btnTogglePet, btnToggleAppointment, btnToggleOrder, btnToggleBill;
    private TextView name, email, phone;
    private TextView txtAddPet, txtAddAppointment, txtAddOrder, txtShowMorePet;
    private TextView txtShowMoreAppointment, txtShowMoreOrder, txtShowMoreBill;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_customer_detail, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        initViews(view);
        initAdapters();
        initList();
        observeViewModel();
        setupListener();
    }

    private void initList() {
        setupToggleSection(
                SectionType.PET,
                listOfPet,
                btnTogglePet,
                () -> customerDetailVM.loadPetList(customer.getId())
        );

        setupToggleSection(
                SectionType.BILL,
                listOfBill,
                btnToggleBill,
                () -> customerDetailVM.loadBillList(new ArrayList<>())
        );

        setupToggleSection(
                SectionType.APPOINTMENT,
                listOfAppointment,
                btnToggleAppointment,
                () -> customerDetailVM.loadAppointmentList(customer.getId())
        );

        setupToggleSection(
                SectionType.ORDER,
                listOfOrder,
                btnToggleOrder,
                () -> customerDetailVM.loadOrderList(customer.getId())
        );
    }

    private void initViews(View view) {
        image = view.findViewById(R.id.customerImage);
        btnCreateOrder = view.findViewById(R.id.btnCreateOrder);

        name = view.findViewById(R.id.txtName);
        email = view.findViewById(R.id.txtEmail);
        phone = view.findViewById(R.id.txtPhone);

        // Section container
        listOfPet = view.findViewById(R.id.listOfPet);
        listOfAppointment = view.findViewById(R.id.listOfAppointment);
        listOfOrder = view.findViewById(R.id.listOfOrder);
        listOfBill = view.findViewById(R.id.listOfBill);

        // Toggle buttons
        btnTogglePet = view.findViewById(R.id.btnTogglePet);
        btnToggleAppointment = view.findViewById(R.id.btnToggleAppointment);
        btnToggleOrder = view.findViewById(R.id.btnToggleOrder);
        btnToggleBill = view.findViewById(R.id.btnToggleBill);

        // "Add" and "Show more" buttons/text
        txtAddPet = view.findViewById(R.id.txtAddPet);
        txtAddAppointment = view.findViewById(R.id.txtAddAppointment);
        txtAddOrder = view.findViewById(R.id.txtAddOrder);
        txtShowMorePet = view.findViewById(R.id.txtShowMorePet);
        txtShowMoreAppointment = view.findViewById(R.id.txtShowMoreAppointment);
        txtShowMoreOrder = view.findViewById(R.id.txtShowMoreOrder);
        txtShowMoreBill = view.findViewById(R.id.txtShowMoreBill);

        // recycler
        recyclerPets = view.findViewById(R.id.recyclerPets);
        recyclerOrders = view.findViewById(R.id.recyclerOrders);
        recyclerAppointments = view.findViewById(R.id.recyclerAppointments);
        recyclerBills = view.findViewById(R.id.recyclerBills);

        // Thiết lập layout cho từng RecyclerView
        recyclerPets.setLayoutManager(new LinearLayoutManager(getContext()));
        recyclerOrders.setLayoutManager(new LinearLayoutManager(getContext()));
        recyclerAppointments.setLayoutManager(new LinearLayoutManager(getContext()));
        recyclerBills.setLayoutManager(new LinearLayoutManager(getContext()));
    }

    private void initAdapters() {
        petAdapter = new PetAdapter(pet -> {
            // Xử lý khi click "More Info"
            SelectedPetViewModel petVM = new ViewModelProvider(requireActivity()).get(SelectedPetViewModel.class);
            petVM.setSelectedPet(pet);
            ((MainActivity) requireActivity()).navigateToWithBackStack(R.id.petDetailFragment, null);
        });

        orderAdapter = new OrderAdapter(order -> {
            // Xử lý khi click "More Info"
            Bundle bundle = new Bundle();
            bundle.putString("orderId", order.getId());
            ((MainActivity) requireActivity()).navigateToWithBackStack(R.id.billDetailFragment, bundle);
        });

        appointmentAdapter = new AppointmentAdapter(appointment -> {
            // Xử lý khi click "More Info"
            Bundle bundle = new Bundle();
            bundle.putString("appointmentId", appointment.getId());
            ((MainActivity) requireActivity()).navigateToWithBackStack(R.id.appointmentDetailFragment, bundle);
        });

        billAdapter = new BillAdapter(bill -> {
            // Xử lý khi click "More Info"
            ((MainActivity) requireActivity()).navigateToWithBackStack(R.id.billDetailFragment, null);
        });

        recyclerPets.setAdapter(petAdapter);
        recyclerOrders.setAdapter(orderAdapter);
        recyclerAppointments.setAdapter(appointmentAdapter);
        recyclerBills.setAdapter(billAdapter);

    }

    private void observeViewModel() {
        SelectedCustomerViewModel selectedCustomerVM = new ViewModelProvider(requireActivity()).get(SelectedCustomerViewModel.class);
        selectedCustomerVM.getSelectedCustomer().observe(getViewLifecycleOwner(), customer -> {
            this.customer = customer;
            showCustomerInfo();
        });
        customerDetailVM = new ViewModelProvider(requireActivity()).get(CustomerDetailViewModel.class);
        customerDetailVM.getPets().observe(getViewLifecycleOwner(), list -> {
            if (list != null) {
                petAdapter.setData(list);
            } else {
                Toast.makeText(requireActivity(), "Danh sách thú cưng rỗng", Toast.LENGTH_SHORT).show();
            }
        });
        customerDetailVM.getAppointments().observe(getViewLifecycleOwner(), list -> {
            if (list != null) {
                appointmentAdapter.setData(list);
            } else {
                Toast.makeText(requireActivity(), "Danh sách lịch hẹn rỗng", Toast.LENGTH_SHORT).show();
            }
        });
        customerDetailVM.getOrders().observe(getViewLifecycleOwner(), list -> {
            if (list != null) {
                orderAdapter.setData(list);
            } else {
                Toast.makeText(requireActivity(), "Danh sách đơn hàng rỗng", Toast.LENGTH_SHORT).show();
            }
        });
        customerDetailVM.getBills().observe(getViewLifecycleOwner(), list -> {
            if (list != null) {
                billAdapter.setData(list);
            } else {
                Toast.makeText(requireActivity(), "Danh sách hóa đơn  rỗng", Toast.LENGTH_SHORT).show();
            }
        });
    }

    private void showCustomerInfo() {
        image.setImageResource(R.drawable.temp_avatar);
        name.setText(customer.getName());
        email.setText(customer.getEmail());
        phone.setText(customer.getPhoneNumber());
    }

    private void setupListener() {
        txtShowMorePet.setOnClickListener(v -> {
            boolean hasMore = petAdapter.showMore();
            if (!hasMore) {
                txtShowMorePet.setVisibility(View.GONE);
            }
        });

        txtShowMoreOrder.setOnClickListener(v -> {
            boolean hasMore = orderAdapter.showMore();
            if (!hasMore) {
                txtShowMoreOrder.setVisibility(View.GONE);
            }
        });

        txtShowMoreAppointment.setOnClickListener(v -> {
            boolean hasMore = appointmentAdapter.showMore();
            if (!hasMore) {
                txtShowMoreAppointment.setVisibility(View.GONE);
            }
        });

        txtShowMoreBill.setOnClickListener(v -> {
            boolean hasMore = billAdapter.showMore();
            if (!hasMore) {
                txtShowMoreBill.setVisibility(View.GONE);
            }
        });

        txtAddPet.setOnClickListener(v -> {
            ((MainActivity) requireActivity()).navigateToWithBackStack(R.id.addNewPetFragment, null);
        });
        txtAddAppointment.setOnClickListener(v -> {
            ((MainActivity) requireActivity()).navigateToWithBackStack(R.id.addAppointmentFragment, null);
        });
        txtAddOrder.setOnClickListener(v -> {
            ((MainActivity) requireActivity()).navigateToWithBackStack(R.id.createBillFragment, null);
        });
        btnCreateOrder.setOnClickListener(v -> {
            ((MainActivity) requireActivity()).navigateToWithBackStack(R.id.createBillFragment, null);
        });
    }

    enum SectionType {
        PET, BILL, ORDER, APPOINTMENT
    }

    private SectionType currentOpenSection = null;

    private void setupToggleSection(
            SectionType section,
            LinearLayout contentLayout,
            ImageButton toggleButton,
            Runnable loadDataIfNeeded
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

                // Lazy load
                loadDataIfNeeded.run();
            }
        });
    }

    private void closeAllSections() {
        listOfPet.setVisibility(View.GONE);
        btnTogglePet.setImageResource(R.drawable.ic_chevron_right);

        listOfBill.setVisibility(View.GONE);
        btnToggleBill.setImageResource(R.drawable.ic_chevron_right);

        listOfOrder.setVisibility(View.GONE);
        btnToggleOrder.setImageResource(R.drawable.ic_chevron_right);

        listOfAppointment.setVisibility(View.GONE);
        btnToggleAppointment.setImageResource(R.drawable.ic_chevron_right);
    }
}