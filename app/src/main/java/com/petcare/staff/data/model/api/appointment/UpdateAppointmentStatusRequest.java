package com.petcare.staff.data.model.api.appointment;

public class UpdateAppointmentStatusRequest {
    private String appointment_id;
    private AppointmentStatus status;

    public UpdateAppointmentStatusRequest(String appointment_id, AppointmentStatus status) {
        this.appointment_id = appointment_id;
        this.status = status;
    }

    public String getAppointment_id() {
        return appointment_id;
    }

    public void setAppointment_id(String appointment_id) {
        this.appointment_id = appointment_id;
    }

    public AppointmentStatus getStatus() {
        return status;
    }

    public void setStatus(AppointmentStatus status) {
        this.status = status;
    }
}
