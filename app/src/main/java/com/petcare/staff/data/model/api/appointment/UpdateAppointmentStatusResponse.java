package com.petcare.staff.data.model.api.appointment;

public class UpdateAppointmentStatusResponse {
    private AppointmentStatus status;

    public AppointmentStatus getStatus() {
        return status;
    }

    public void setStatus(AppointmentStatus status) {
        this.status = status;
    }
}
