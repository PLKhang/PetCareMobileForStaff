package com.petcare.staff.data.model.api.appointment;

public class Appointment {
    private int id;
    private int customer_id;
    private int employee_id;
    private String customer_address;
    private String scheduled_time; // ISO 8601
    private String status;
    private String note;
    private float total;
    private int branch_id;

    public Appointment(int id, int customer_id, int employee_id, String customer_address, String scheduled_time, String status, String note, float total, int branch_id) {
        this.id = id;
        this.customer_id = customer_id;
        this.employee_id = employee_id;
        this.customer_address = customer_address;
        this.scheduled_time = scheduled_time;
        this.status = status;
        this.note = note;
        this.total = total;
        this.branch_id = branch_id;
    }

    // getters & setters

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

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public String getNote() {
        return note;
    }

    public void setNote(String note) {
        this.note = note;
    }

    public float getTotal() {
        return total;
    }

    public void setTotal(float total) {
        this.total = total;
    }

    public int getBranch_id() {
        return branch_id;
    }

    public void setBranch_id(int branch_id) {
        this.branch_id = branch_id;
    }
}
