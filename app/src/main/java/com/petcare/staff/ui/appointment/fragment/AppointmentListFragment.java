package com.petcare.staff.ui.appointment.fragment;

import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;
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
import com.petcare.staff.ui.userprofile.viewmodel.UserProfileViewModel;

public class AppointmentListFragment extends Fragment {
    private AppointmentListViewModel viewModel;
    private ImageButton btnSearch, btnToggleUserAppointments, btnToggleBranchAppointments, btnToggleOtherAppointments;
    private TextView txtStartDate, txtEndDate;
    private LinearLayout listOfUserAppointments, listOfBranchAppointments, listOfOtherAppointments;
    private AppointmentAdapter userAppointmentAdapter, branchAppointmentAdapter, otherAppointmentAdapter;
    private RecyclerView userRecyclerAppointments, branchRecyclerAppointment, otherRecyclerAppointment;
    private User currentUser;
    private TextView txtShowMoreUserAppointment, txtShowMoreBranchAppointment, txtShowMoreAppointment;

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
    }

    private void observeViewModel() {
        viewModel = new ViewModelProvider(requireActivity()).get(AppointmentListViewModel.class);
        viewModel.getCurrentUserAppointment().observe(getViewLifecycleOwner(), list -> {
            if (list != null) {
                userAppointmentAdapter.setData(list);
            }
        });
        viewModel.getBranchAppointment().observe(getViewLifecycleOwner(), list -> {
            if (list != null) {
                branchAppointmentAdapter.setData(list);
            }
        });
        viewModel.getOtherAppointment().observe(getViewLifecycleOwner(), list -> {
            if (list != null) {
                otherAppointmentAdapter.setData(list);
            }
        });

        UserProfileViewModel tempVM = new ViewModelProvider(requireActivity()).get(UserProfileViewModel.class);
        tempVM.getUser().observe(requireActivity(), user -> {
            currentUser = user;
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
//                () -> viewModel.loadBranchAppointment(currentUser.getBranchId())
                () -> viewModel.loadBranchAppointment(currentUser.getId())
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
            ((MainActivity) requireActivity()).navigateToWithBackStack(R.id.appointmentDetailFragment, bundle);
        });
        branchAppointmentAdapter = new AppointmentAdapter(appointment -> {
            // Xử lý khi click "More Info"
            Bundle bundle = new Bundle();
            bundle.putSerializable("appointmentId", appointment.getId());
            ((MainActivity) requireActivity()).navigateToWithBackStack(R.id.appointmentDetailFragment, bundle);
        });
        otherAppointmentAdapter = new AppointmentAdapter(appointment -> {
            // Xử lý khi click "More Info"
            Bundle bundle = new Bundle();
            bundle.putSerializable("appointmentId", appointment.getId());
            ((MainActivity) requireActivity()).navigateToWithBackStack(R.id.appointmentDetailFragment, bundle);
        });
        userRecyclerAppointments.setAdapter(userAppointmentAdapter);
        branchRecyclerAppointment.setAdapter(branchAppointmentAdapter);
        otherRecyclerAppointment.setAdapter(otherAppointmentAdapter);
    }


    private void initViews(View view) {
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
}