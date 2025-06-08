package com.petcare.staff.data.model.api.appointment;

public class UpdateAppointmentEmployeeResponse {
    String status;

    public UpdateAppointmentEmployeeResponse(String status) {
        this.status = status;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

}
