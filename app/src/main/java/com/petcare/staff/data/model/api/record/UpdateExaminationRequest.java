package com.petcare.staff.data.model.api.record;

public class UpdateExaminationRequest {
    /*
    {
      "date": "string",
      "diagnosis": "string",
      "id": "string",
      "notes": "string",
      "pet_id": "string",
      "vet_id": "string"
    }
*/
    private String date;
    private String diagnosis;
    private String id;
    private String notes;
    private String pet_id;
    private String vet_id;

    public UpdateExaminationRequest(String date, String diagnosis, String id, String notes, String pet_id, String vet_id) {
        this.date = date;
        this.diagnosis = diagnosis;
        this.id = id;
        this.notes = notes;
        this.pet_id = pet_id;
        this.vet_id = vet_id;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }

    public String getDiagnosis() {
        return diagnosis;
    }

    public void setDiagnosis(String diagnosis) {
        this.diagnosis = diagnosis;
    }

    public String getNotes() {
        return notes;
    }

    public void setNotes(String notes) {
        this.notes = notes;
    }

    public String getPet_id() {
        return pet_id;
    }

    public void setPet_id(String pet_id) {
        this.pet_id = pet_id;
    }

    public String getVet_id() {
        return vet_id;
    }

    public void setVet_id(String vet_id) {
        this.vet_id = vet_id;
    }

}
