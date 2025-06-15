package com.petcare.staff.ui.home.fragment;

import android.os.Bundle;

import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.petcare.staff.MainActivity;
import com.petcare.staff.R;
import com.petcare.staff.ui.home.adapter.NotificationAdapter;
import com.petcare.staff.ui.home.viewmodel.NotificationViewModel;

public class NotificationFragment extends Fragment {
    private NotificationViewModel viewModel;
    private NotificationAdapter adapter;

    @Override
    public View onCreateView(LayoutInflater inf, ViewGroup container, Bundle savedInstanceState) {
        View view = inf.inflate(R.layout.fragment_notification, container, false);

        RecyclerView recycler = view.findViewById(R.id.recyclerView);
        recycler.setLayoutManager(new LinearLayoutManager(getContext()));
        adapter = new NotificationAdapter();
        recycler.setAdapter(adapter);

        viewModel = new ViewModelProvider(this).get(NotificationViewModel.class);
//        viewModel.getNotifications().observe(getViewLifecycleOwner(), adapter::setData);

        return view;
    }
}
