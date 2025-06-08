package com.petcare.staff.data.model.api.appointment;

import java.util.List;

public class CreateAppointmentRequest {
    private int customer_id;
    private String customer_address;
    private String scheduled_time; // RFC3339, ví dụ: "2025-03-07T10:00:00Z"
    private List<ServiceItem> services;
    private String note;
    private int branch_id;

    public static class ServiceItem {
        private int service_id;
        private int quantity;

        public ServiceItem(int service_id, int quantity) {
            this.service_id = service_id;
            this.quantity = quantity;
        }

        public int getService_id() {
            return service_id;
        }

        public void setService_id(int service_id) {
            this.service_id = service_id;
        }

        public int getQuantity() {
            return quantity;
        }

        public void setQuantity(int quantity) {
            this.quantity = quantity;
        }
    }

    public CreateAppointmentRequest(int customer_id, String customer_address, String scheduled_time, List<ServiceItem> services, String note, int branch_id) {
        this.customer_id = customer_id;
        this.customer_address = customer_address;
        this.scheduled_time = scheduled_time;
        this.services = services;
        this.note = note;
        this.branch_id = branch_id;
    }

    public int getCustomer_id() {
        return customer_id;
    }

    public void setCustomer_id(int customer_id) {
        this.customer_id = customer_id;
    }

    public String getCustomer_address() {
        return customer_address;
    }

    public void setCustomer_address(String customer_address) {
        this.customer_address = customer_address;
    }

    public String getScheduled_time() {
        return scheduled_time;
    }

    public void setScheduled_time(String scheduled_time) {
        this.scheduled_time = scheduled_time;
    }

    public List<ServiceItem> getServices() {
        return services;
    }

    public void setServices(List<ServiceItem> services) {
        this.services = services;
    }

    public String getNote() {
        return note;
    }

    public void setNote(String note) {
        this.note = note;
    }

    public int getBranch_id() {
        return branch_id;
    }

    public void setBranch_id(int branch_id) {
        this.branch_id = branch_id;
    }
}