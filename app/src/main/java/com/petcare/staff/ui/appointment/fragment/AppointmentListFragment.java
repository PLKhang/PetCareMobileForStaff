package com.petcare.staff.ui.appointment.fragment;

import android.app.DatePickerDialog;
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
import android.widget.ImageButton;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.petcare.staff.MainActivity;
import com.petcare.staff.R;
import com.petcare.staff.data.model.ui.User;
import com.petcare.staff.ui.appointment.viewmodel.AppointmentListViewModel;
import com.petcare.staff.ui.customer.adapter.AppointmentAdapter;
import com.petcare.staff.ui.customer.viewmodel.CustomerViewModel;
import com.petcare.staff.ui.customer.viewmodel.SelectedCustomerViewModel;
import com.petcare.staff.ui.userprofile.viewmodel.UserProfileViewModel;
import com.petcare.staff.utils.DateTime;

import java.util.Calendar;

public class AppointmentListFragment extends Fragment {
    private AppointmentListViewModel viewModel;
    private ImageButton btnSearch, btnToggleUserAppointments, btnToggleBranchAppointments, btnToggleOtherAppointments;
    private TextView txtStartDate, txtEndDate;
    private LinearLayout listOfUserAppointments, listOfBranchAppointments, listOfOtherAppointments;
    private AppointmentAdapter userAppointmentAdapter, branchAppointmentAdapter, otherAppointmentAdapter;
    private RecyclerView userRecyclerAppointments, branchRecyclerAppointment, otherRecyclerAppointment;
    private User currentUser;
    private TextView txtShowMoreUserAppointment, txtShowMoreBranchAppointment, txtShowMoreAppointment;
    private Calendar calendar;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_appointment_list, container, false);
    }

    @Override
    public void onResume() {
        super.onResume();
        ((MainActivity) getActivity()).setActiveNav(2);
        ((MainActivity) getActivity()).hideBottomNavigation(false);
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

    private void setupListener() {
        txtShowMoreUserAppointment.setOnClickListener(v -> {
            boolean hasMore = userAppointmentAdapter.showMore();
            if (!hasMore) {
                txtShowMoreUserAppointment.setVisibility(View.GONE);
            }
        });
        txtShowMoreBranchAppointment.setOnClickListener(v -> {
            boolean hasMore = branchAppointmentAdapter.showMore();
            if (!hasMore) {
                txtShowMoreBranchAppointment.setVisibility(View.GONE);
            }
        });
        txtShowMoreAppointment.setOnClickListener(v -> {
            boolean hasMore = otherAppointmentAdapter.showMore();
            if (!hasMore) {
                txtShowMoreAppointment.setVisibility(View.GONE);
            }
        });
        txtStartDate.setOnClickListener(v -> showDatePickerDialog(txtStartDate));
        txtEndDate.setOnClickListener(v -> showDatePickerDialog(txtEndDate));
        btnSearch.setOnClickListener(v -> {

            String startStr = txtStartDate.getText().toString();
            String endStr = txtEndDate.getText().toString();

            DateTime start = startStr.isEmpty() ? null : DateTime.toDate(startStr);
            DateTime end = endStr.isEmpty() ? null : DateTime.toDate(endStr);

            userAppointmentAdapter.applyFilter(start, end);
            branchAppointmentAdapter.applyFilter(start, end);
            otherAppointmentAdapter.applyFilter(start, end);
        });
    }

    private void observeViewModel() {
        viewModel = new ViewModelProvider(requireActivity()).get(AppointmentListViewModel.class);


        viewModel.getCurrentUserAppointment().observe(getViewLifecycleOwner(), list -> {
            if (list != null) {
                userAppointmentAdapter.setRawData(list);
            }
        });
        viewModel.getBranchAppointment().observe(getViewLifecycleOwner(), list -> {
            if (list != null) {
                branchAppointmentAdapter.setRawData(list);
            }
        });
        viewModel.getOtherAppointment().observe(getViewLifecycleOwner(), list -> {
            if (list != null) {
                otherAppointmentAdapter.setRawData(list);
            }
        });

        UserProfileViewModel tempVM = new ViewModelProvider(requireActivity()).get(UserProfileViewModel.class);
        tempVM.getUser().observe(requireActivity(), user -> {
            currentUser = user;
            viewModel.loadUserAppointment(currentUser.getId());
            viewModel.loadBranchAppointment(currentUser.getBranchId());
        });
    }

    private void initList() {
        setupToggleSection(
                SectionType.USER,
                listOfUserAppointments,
                btnToggleUserAppointments,
                () -> viewModel.loadUserAppointment(currentUser.getId())
        );
        setupToggleSection(
                SectionType.USER,
                listOfBranchAppointments,
                btnToggleBranchAppointments,
                () -> viewModel.loadBranchAppointment(currentUser.getBranchId())
        );
        setupToggleSection(
                SectionType.USER,
                listOfOtherAppointments,
                btnToggleOtherAppointments,
                () -> viewModel.loadOtherAppointment(currentUser.getBranchId())
        );
    }

    private void initAdapters() {
        userAppointmentAdapter = new AppointmentAdapter(appointment -> {
            // Xử lý khi click "More Info"
            Bundle bundle = new Bundle();
            bundle.putSerializable("appointmentId", appointment.getId());
            CustomerViewModel customerViewModel = new ViewModelProvider(requireActivity()).get(CustomerViewModel.class);
            SelectedCustomerViewModel selectedCustomerViewModel = new ViewModelProvider(requireActivity()).get(SelectedCustomerViewModel.class);
            selectedCustomerViewModel.setSelectedCustomer(customerViewModel.getCustomerById(appointment.getId()));
            ((MainActivity) requireActivity()).navigateToWithBackStack(R.id.appointmentDetailFragment, bundle);
        });
        branchAppointmentAdapter = new AppointmentAdapter(appointment -> {
            // Xử lý khi click "More Info"
            Bundle bundle = new Bundle();
            bundle.putSerializable("appointmentId", appointment.getId());
            CustomerViewModel customerViewModel = new ViewModelProvider(requireActivity()).get(CustomerViewModel.class);
            SelectedCustomerViewModel selectedCustomerViewModel = new ViewModelProvider(requireActivity()).get(SelectedCustomerViewModel.class);
            selectedCustomerViewModel.setSelectedCustomer(customerViewModel.getCustomerById(appointment.getId()));
            ((MainActivity) requireActivity()).navigateToWithBackStack(R.id.appointmentDetailFragment, bundle);
        });
        otherAppointmentAdapter = new AppointmentAdapter(appointment -> {
            // Xử lý khi click "More Info"
            Bundle bundle = new Bundle();
            bundle.putSerializable("appointmentId", appointment.getId());
            CustomerViewModel customerViewModel = new ViewModelProvider(requireActivity()).get(CustomerViewModel.class);
            SelectedCustomerViewModel selectedCustomerViewModel = new ViewModelProvider(requireActivity()).get(SelectedCustomerViewModel.class);
            selectedCustomerViewModel.setSelectedCustomer(customerViewModel.getCustomerById(appointment.getId()));
            ((MainActivity) requireActivity()).navigateToWithBackStack(R.id.appointmentDetailFragment, bundle);
        });

        userRecyclerAppointments.setAdapter(userAppointmentAdapter);
        branchRecyclerAppointment.setAdapter(branchAppointmentAdapter);
        otherRecyclerAppointment.setAdapter(otherAppointmentAdapter);
    }


    private void initViews(View view) {
        calendar = Calendar.getInstance();

        btnSearch = view.findViewById(R.id.btnSearch);
        btnToggleUserAppointments = view.findViewById(R.id.btnToggleUserAppointments);
        btnToggleBranchAppointments = view.findViewById(R.id.btnToggleBranchAppointments);
        btnToggleOtherAppointments = view.findViewById(R.id.btnToggleOtherAppointments);

        txtStartDate = view.findViewById(R.id.txtStartDate);
        txtEndDate = view.findViewById(R.id.txtEndDate);

        listOfUserAppointments = view.findViewById(R.id.listOfUserAppointments);
        listOfBranchAppointments = view.findViewById(R.id.listOfBranchAppointments);
        listOfOtherAppointments = view.findViewById(R.id.listOfOtherAppointments);

        userRecyclerAppointments = view.findViewById(R.id.recyclerUserAppointments);
        otherRecyclerAppointment = view.findViewById(R.id.recyclerOtherAppointments);
        branchRecyclerAppointment = view.findViewById(R.id.recyclerBranchAppointments);

        txtShowMoreUserAppointment = view.findViewById(R.id.txtShowMoreUserAppointment);
        txtShowMoreBranchAppointment = view.findViewById(R.id.txtShowMoreBranchAppointment);
        txtShowMoreAppointment = view.findViewById(R.id.txtShowMoreAppointment);

        userRecyclerAppointments.setLayoutManager(new LinearLayoutManager(getContext()));
        otherRecyclerAppointment.setLayoutManager(new LinearLayoutManager(getContext()));
        branchRecyclerAppointment.setLayoutManager(new LinearLayoutManager(getContext()));

    }


    enum SectionType {
        USER, BRANCH, OTHER
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
        listOfUserAppointments.setVisibility(View.GONE);
        btnToggleUserAppointments.setImageResource(R.drawable.ic_chevron_right);

        listOfBranchAppointments.setVisibility(View.GONE);
        btnToggleBranchAppointments.setImageResource(R.drawable.ic_chevron_right);

        listOfOtherAppointments.setVisibility(View.GONE);
        btnToggleOtherAppointments.setImageResource(R.drawable.ic_chevron_right);
    }

    private void showDatePickerDialog(TextView targetView) {
        int year = calendar.get(Calendar.YEAR);
        int month = calendar.get(Calendar.MONTH);
        int day = calendar.get(Calendar.DAY_OF_MONTH);

        DatePickerDialog datePickerDialog = new DatePickerDialog(
                getContext(),
                (view, selectedYear, selectedMonth, selectedDay) -> {
                    String selectedDate = String.format("%02d/%02d/%04d", selectedDay, selectedMonth + 1, selectedYear);
                    targetView.setText(selectedDate);
                },
                year, month, day
        );

        datePickerDialog.show();
    }
}