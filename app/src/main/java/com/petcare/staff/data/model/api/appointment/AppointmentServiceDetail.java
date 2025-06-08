package com.petcare.staff.data.model.api.appointment;

public class AppointmentServiceDetail {
    private int appointment_id;
    private int service_id;
    private String service_name;
    private int quantity;

    public AppointmentServiceDetail(int appointment_id, int service_id, String service_name, float service_price, int quantity) {
        this.appointment_id = appointment_id;
        this.service_id = service_id;
        this.service_name = service_name;
        this.quantity = quantity;
    }
// getters & setters

    public int getAppointment_id() {
        return appointment_id;
    }

    public void setAppointment_id(int appointment_id) {
        this.appointment_id = appointment_id;
    }

    public int getService_id() {
        return service_id;
    }

    public void setService_id(int service_id) {
        this.service_id = service_id;
    }

    public String getService_name() {
        return service_name;
    }

    public void setService_name(String service_name) {
        this.service_name = service_name;
    }

    public int getQuantity() {
        return quantity;
    }

    public void setQuantity(int quantity) {
        this.quantity = quantity;
    }
}
