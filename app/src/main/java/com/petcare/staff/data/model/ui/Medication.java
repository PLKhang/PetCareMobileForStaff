package com.petcare.staff.data.model.ui;

import com.petcare.staff.utils.DateTime;

public class Medication {
    private String id;
    private String name;
    private String dosage;
    private DateTime startDate;
    private DateTime endDate;

    public Medication() {
        name = "";
        dosage = "";
        startDate = new DateTime();
        endDate = new DateTime();
    }

    public Medication(String name, String dosage,
                      DateTime startDate, DateTime endDate) {
        this.name = name;
        this.dosage = dosage;
        this.startDate = startDate;
        this.endDate = endDate;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getDosage() {
        return dosage;
    }

    public void setDosage(String dosage) {
        this.dosage = dosage;
    }

    public DateTime getStartDate() {
        return startDate;
    }

    public void setStartDate(DateTime startDate) {
        this.startDate = startDate;
    }

    public DateTime getEndDate() {
        return endDate;
    }

    public void setEndDate(DateTime endDate) {
        this.endDate = endDate;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }
}
