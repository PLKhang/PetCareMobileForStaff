package com.petcare.staff.data.model.ui;

import com.petcare.staff.data.model.api.appointment.AppointmentStatus;
import com.petcare.staff.utils.DateTime;

public class SimplifiedAppointment {
    private String id;
    private String customerId;
    private String employeeId;
    private String branchId;
    private DateTime time;
    private AppointmentStatus status;
    private String note;
    private String address;
    private float total;

    public SimplifiedAppointment(String id, String customerId, String employeeId, String branchId,
                                 DateTime time, AppointmentStatus status, String note, String address, float total) {
        this.id = id;
        this.customerId = customerId;
        this.employeeId = employeeId;
        this.branchId = branchId;
        this.time = time;
        this.status = status;
        this.note = note;
        this.address = address;
        this.total = total;
    }

    public float getTotal() {
        return total;
    }

    public void setTotal(float total) {
        this.total = total;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getCustomerId() {
        return customerId;
    }

    public void setCustomerId(String customerId) {
        this.customerId = customerId;
    }

    public String getEmployeeId() {
        return employeeId;
    }

    public void setEmployeeId(String employeeId) {
        this.employeeId = employeeId;
    }

    public String getBranchId() {
        return branchId;
    }

    public void setBranchId(String branchId) {
        this.branchId = branchId;
    }

    public DateTime getTime() {
        return time;
    }

    public void setTime(DateTime time) {
        this.time = time;
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

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }
}
