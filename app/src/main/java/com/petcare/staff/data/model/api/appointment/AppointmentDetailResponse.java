package com.petcare.staff.data.model.api.appointment;

import java.util.List;

public class AppointmentDetailResponse {
    private Appointment appointment;
    private List<AppointmentServiceDetail> details;
    private Order order;

    public AppointmentDetailResponse(Appointment appointment, List<AppointmentServiceDetail> details, Order order) {
        this.appointment = appointment;
        this.details = details;
        this.order = order;
    }
// getters & setters

    public Appointment getAppointment() {
        return appointment;
    }

    public void setAppointment(Appointment appointment) {
        this.appointment = appointment;
    }

    public List<AppointmentServiceDetail> getDetails() {
        return details;
    }

    public void setDetails(List<AppointmentServiceDetail> details) {
        this.details = details;
    }

    public Order getOrder() {
        return order;
    }

    public void setOrder(Order order) {
        this.order = order;
    }
}
