package com.petcare.staff.data.model.api.appointment;

public class AppointmentResponse {
    private int id;
    private int customer_id;
    private int employee_id; // có thể null
    private int branch_id;
    private String scheduled_time;
    private AppointmentStatus status;
    private String note;
    private float total;
    private String customer_address;

    public AppointmentResponse(int id, int customer_id, int employee_id, int branch_id, String scheduled_time,
                               AppointmentStatus status, String note, float total, String customer_address) {
        this.id = id;
        this.customer_id = customer_id;
        this.employee_id = employee_id;
        this.branch_id = branch_id;
        this.scheduled_time = scheduled_time;
        this.status = status;
        this.note = note;
        this.total = total;
        this.customer_address = customer_address;
    }

    public float getTotal() {
        return total;
    }

    public void setTotal(float total) {
        this.total = total;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public int getCustomer_id() {
        return customer_id;
    }

    public void setCustomer_id(int customer_id) {
        this.customer_id = customer_id;
    }

    public int getEmployee_id() {
        return employee_id;
    }

    public void setEmployee_id(int employee_id) {
        this.employee_id = employee_id;
    }

    public int getBranch_id() {
        return branch_id;
    }

    public void setBranch_id(int branch_id) {
        this.branch_id = branch_id;
    }

    public String getScheduled_time() {
        return scheduled_time;
    }

    public void setScheduled_time(String scheduled_time) {
        this.scheduled_time = scheduled_time;
    }

    public AppointmentStatus getStatus() {
        return status;
    }

    public void setStatus(AppointmentStatus status) {
        this.status = status;
    }

    public String getNote() {
        return note;
    }

    public void setNote(String note) {
        this.note = note;
    }

    public String getCustomer_address() {
        return customer_address;
    }

    public void setCustomer_address(String customer_address) {
        this.customer_address = customer_address;
    }
}
