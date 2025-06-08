package com.petcare.staff.data.model.ui;

import com.petcare.staff.data.model.api.appointment.AppointmentStatus;
import com.petcare.staff.utils.DateTime;

import java.io.Serializable;
import java.util.List;

public class Appointment implements Serializable {
    private String id;
    private Order order;
    private String employeeId;
    private String customerId;
    private String branchId;
    private String customerAddress;
    private DateTime scheduledTime;
    private AppointmentStatus status;
    private String note;
    private double total;
    private List<Service> services;

    public Appointment(String customerId, String customerAddress, DateTime scheduledTime, List<Service> services, String note) {
        this.customerId = customerId;
        this.customerAddress = customerAddress;
        this.scheduledTime = scheduledTime;
        this.services = services;
        this.note = note;
    }

    public Appointment(String id, String employeeId, Order order, String customerAddress, DateTime scheduledTime,
                       AppointmentStatus status, String note, double total, List<Service> services) {
        this.id = id;
        this.employeeId = employeeId;
        this.order = order;
        this.customerAddress = customerAddress;
        this.scheduledTime = scheduledTime;
        this.status = status;
        this.note = note;
        this.total = total;
        this.services = services;
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

    public String getCustomerId() {
        return customerId;
    }

    public void setCustomerId(String customerId) {
        this.customerId = customerId;
    }

    public Order getOrder() {
        return order;
    }

    public void setOrder(Order order) {
        this.order = order;
    }

    // Getters and Setters
    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getCustomerAddress() {
        return customerAddress;
    }

    public void setCustomerAddress(String customerAddress) {
        this.customerAddress = customerAddress;
    }

    public DateTime getScheduledTime() {
        return scheduledTime;
    }

    public void setScheduledTime(DateTime scheduledTime) {
        this.scheduledTime = scheduledTime;
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

    public double getTotal() {
        return total;
    }

    public void setTotal(double total) {
        this.total = total;
    }

    public List<Service> getServices() {
        return services;
    }

    public void setServices(List<Service> services) {
        this.services = services;
    }
}