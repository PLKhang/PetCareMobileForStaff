package com.petcare.staff.data.model.ui;

import com.petcare.staff.utils.DateTime;

public class VaccineRecord {
    private String id;
    private String petId;
    private String vetId;
    private String vaccineName;
    private DateTime date;
    private DateTime nextDose;

    public VaccineRecord(String id, String petId, String vetId, String vaccineName, DateTime date, DateTime nextDose) {
        this.id = id;
        this.petId = petId;
        this.vetId = vetId;
        this.vaccineName = vaccineName;
        this.date = date;
        this.nextDose = nextDose;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getPetId() {
        return petId;
    }

    public void setPetId(String petId) {
        this.petId = petId;
    }

    public String getVetId() {
        return vetId;
    }

    public void setVetId(String vetId) {
        this.vetId = vetId;
    }

    public String getVaccineName() {
        return vaccineName;
    }

    public void setVaccineName(String vaccineName) {
        this.vaccineName = vaccineName;
    }

    public DateTime getDate() {
        return date;
    }

    public void setDate(DateTime date) {
        this.date = date;
    }

    public DateTime getNextDose() {
        return nextDose;
    }

    public void setNextDose(DateTime nextDose) {
        this.nextDose = nextDose;
    }
}
