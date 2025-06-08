package com.petcare.staff.data.model.api.appointment;

public class UpdateAppointmentEmployeeRequest {
    String appointment_id;
    String employee_id;

    public UpdateAppointmentEmployeeRequest(String appointment_id, String employee_id) {
        this.appointment_id = appointment_id;
        this.employee_id = employee_id;
    }

    public String getAppointment_id() {
        return appointment_id;
    }

    public void setAppointment_id(String appointment_id) {
        this.appointment_id = appointment_id;
    }

    public String getEmployee_id() {
        return employee_id;
    }

    public void setEmployee_id(String employee_id) {
        this.employee_id = employee_id;
    }
}
