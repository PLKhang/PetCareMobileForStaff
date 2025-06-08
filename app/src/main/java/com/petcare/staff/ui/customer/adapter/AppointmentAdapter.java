package com.petcare.staff.ui.customer.adapter;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.petcare.staff.R;
import com.petcare.staff.data.model.ui.SimplifiedAppointment;
import com.petcare.staff.ui.common.adapter.BaseExpandableAdapter;

import java.util.ArrayList;
import java.util.List;

public class AppointmentAdapter extends BaseExpandableAdapter<SimplifiedAppointment, AppointmentAdapter.AppointmentViewHolder> {
    private final OnAppointmentInfoClickListener listener;

    public interface OnAppointmentInfoClickListener {
        void onMoreInfoClick(SimplifiedAppointment appointment);
    }

    public AppointmentAdapter(OnAppointmentInfoClickListener listener) {
        this.listener = listener;
    }

    @NonNull
    @Override
    public AppointmentViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_customer_appointment, parent, false);
        return new AppointmentViewHolder(view);
    }

    @Override
    public void bind(AppointmentViewHolder holder, SimplifiedAppointment appointment) {
        holder.bind(appointment);
    }

    class AppointmentViewHolder extends RecyclerView.ViewHolder {
        TextView time, address, status;
        Button btnMoreInfo;

        public AppointmentViewHolder(@NonNull View itemView) {
            super(itemView);
            time = itemView.findViewById(R.id.appointmentSchedule);
            address = itemView.findViewById(R.id.appointmentAddress);
            status = itemView.findViewById(R.id.appointmentStatus);
            btnMoreInfo = itemView.findViewById(R.id.btnAppointmentInfo);
        }

        public void bind(SimplifiedAppointment appointment) {
            time.setText(appointment.getTime().toDateTimeString());
            address.setText(appointment.getAddress());
            status.setText("Status: " + appointment.getStatus());

            btnMoreInfo.setOnClickListener(v -> {
                if (listener != null) listener.onMoreInfoClick(appointment);
            });
        }
    }
}
