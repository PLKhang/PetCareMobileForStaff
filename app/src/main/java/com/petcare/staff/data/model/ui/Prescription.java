package com.petcare.staff.data.model.ui;

import java.util.List;

public class Prescription {
    private String id;
    private String examinationId;
    private List<Medication> medicationList;

    public Prescription(String id, String examinationId, List<Medication> medicationList) {
        this.id = id;
        this.examinationId = examinationId;
        this.medicationList = medicationList;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getExaminationId() {
        return examinationId;
    }

    public void setExaminationId(String examinationId) {
        this.examinationId = examinationId;
    }

    public List<Medication> getMedicationList() {
        return medicationList;
    }

    public void setMedicationList(List<Medication> medicationList) {
        this.medicationList = medicationList;
    }
}
