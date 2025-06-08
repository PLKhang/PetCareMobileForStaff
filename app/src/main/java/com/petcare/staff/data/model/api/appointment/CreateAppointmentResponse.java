package com.petcare.staff.data.model.api.appointment;

public class CreateAppointmentResponse {
    private int appointment_id;
    private String status;

    public int getAppointment_id() {
        return appointment_id;
    }

    public void setAppointment_id(int appointment_id) {
        this.appointment_id = appointment_id;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }
}
