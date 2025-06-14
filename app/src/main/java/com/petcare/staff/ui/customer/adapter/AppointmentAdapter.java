package com.petcare.staff.ui.customer.adapter;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.petcare.staff.R;
import com.petcare.staff.data.model.api.appointment.AppointmentStatus;
import com.petcare.staff.data.model.ui.SimplifiedAppointment;
import com.petcare.staff.ui.common.adapter.BaseExpandableAdapter;
import com.petcare.staff.utils.DateTime;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;

public class AppointmentAdapter extends BaseExpandableAdapter<SimplifiedAppointment, AppointmentAdapter.AppointmentViewHolder> {
    private final OnAppointmentInfoClickListener listener;

    public interface OnAppointmentInfoClickListener {
        void onMoreInfoClick(SimplifiedAppointment appointment);
    }
    // Gọi khi dữ liệu mới từ ViewModel
    public void setRawData(List<SimplifiedAppointment> data) {
        this.fullList = new ArrayList<>(data);
        sortFullList();
        applyFilter(null, null); // hiện toàn bộ
    }

    // Lọc theo ngày
    public void applyFilter(DateTime startDate, DateTime endDate) {
        List<SimplifiedAppointment> filtered = new ArrayList<>();

        for (SimplifiedAppointment appointment : fullList) {
            boolean inRange = true;

            if (startDate != null && appointment.getTime().compareTo(startDate) <= 0) {
                inRange = false;
            }

            if (endDate != null && appointment.getTime().compareTo(endDate) > 0) {
                inRange = false;
            }

            if (inRange) {
                filtered.add(appointment);
            }
        }

        this.displayList = new ArrayList<>(filtered.subList(0, Math.min(displaySize, filtered.size())));
        notifyDataSetChanged();
    }

    // Hàm sắp xếp mặc định gọi trong setRawData
    private void sortFullList() {
        Collections.sort(fullList, new Comparator<SimplifiedAppointment>() {
            @Override
            public int compare(SimplifiedAppointment a1, SimplifiedAppointment a2) {
                int priority1 = getStatusPriority(a1.getStatus());
                int priority2 = getStatusPriority(a2.getStatus());

                if (priority1 != priority2) {
                    return Integer.compare(priority1, priority2);
                } else {
                    return a1.getTime().compareTo(a2.getTime());
                }
            }

            private int getStatusPriority(AppointmentStatus status) {
                switch (status) {
                    case PENDING:
                        return 0;
                    case IN_PROGRESS:
                        return 1;
                    case COMPLETED:
                    case CANCELLED:
                        return 2;
                    default:
                        return 3;
                }
            }
        });
    }

    @Override
    public void setData(List<SimplifiedAppointment> data) {
        this.fullList = new ArrayList<>(data);

        // Sắp xếp fullList theo điều kiện
        Collections.sort(this.fullList, new Comparator<SimplifiedAppointment>() {
            @Override
            public int compare(SimplifiedAppointment a1, SimplifiedAppointment a2) {
                // Định nghĩa mức độ ưu tiên của status
                int priority1 = getStatusPriority(a1.getStatus());
                int priority2 = getStatusPriority(a2.getStatus());

                if (priority1 != priority2) {
                    return Integer.compare(priority1, priority2); // thấp hơn ưu tiên hơn
                } else {
                    return a1.getTime().compareTo(a2.getTime()); // cùng status -> theo thời gian
                }
            }

            private int getStatusPriority(AppointmentStatus status) {
                switch (status) {
                    case PENDING:
                        return 0;
                    case IN_PROGRESS:
                        return 1;
                    case COMPLETED:
                    case CANCELLED:
                        return 2;
                    default:
                        return 3; // status null hay UNSPECIFIED
                }
            }
        });

        this.displayList = new ArrayList<>(this.fullList.subList(0, Math.min(displaySize, this.fullList.size())));
        notifyDataSetChanged();
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
