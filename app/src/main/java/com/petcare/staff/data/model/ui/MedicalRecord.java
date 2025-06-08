package com.petcare.staff.data.model.ui;

import com.petcare.staff.utils.DateTime;

import java.io.Serializable;
import java.util.List;

public class MedicalRecord implements Serializable {
    private String id;
    private String petId;
    private String vetId;
    private String diagnosis;
    private DateTime date;
    private String notes;
    private List<Prescription> prescription;

    public MedicalRecord(String id, String petId, String vetId,
                         String diagnosis, DateTime date, String notes) {
        this.id = id;
        this.petId = petId;
        this.vetId = vetId;
        this.diagnosis = diagnosis;
        this.date = date;
        this.notes = notes;
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

    public String getDiagnosis() {
        return diagnosis;
    }

    public void setDiagnosis(String diagnosis) {
        this.diagnosis = diagnosis;
    }

    public DateTime getDate() {
        return date;
    }

    public void setDate(DateTime date) {
        this.date = date;
    }

    public String getNotes() {
        return notes;
    }

    public void setNotes(String notes) {
        this.notes = notes;
    }

    public List<Prescription> getPrescription() {
        return prescription;
    }

    public void setPrescription(List<Prescription> prescription) {
        this.prescription = prescription;
    }
}
