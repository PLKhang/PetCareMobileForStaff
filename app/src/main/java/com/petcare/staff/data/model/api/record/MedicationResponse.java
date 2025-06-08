package com.petcare.staff.data.model.api.record;

public class MedicationResponse {
    private String id;
    private String name;
    private String dosage;
    private String end_date;
    private String start_date;

    public MedicationResponse(String dosage, String end_date, String name, String start_date) {
        this.dosage = dosage;
        this.end_date = end_date;
        this.name = name;
        this.start_date = start_date;
    }

    public MedicationResponse(String dosage, String end_date, String id, String name, String start_date) {
        this.dosage = dosage;
        this.end_date = end_date;
        this.id = id;
        this.name = name;
        this.start_date = start_date;
    }
// getters and setters

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getDosage() {
        return dosage;
    }

    public void setDosage(String dosage) {
        this.dosage = dosage;
    }

    public String getEnd_date() {
        return end_date;
    }

    public void setEnd_date(String end_date) {
        this.end_date = end_date;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getStart_date() {
        return start_date;
    }

    public void setStart_date(String start_date) {
        this.start_date = start_date;
    }
}