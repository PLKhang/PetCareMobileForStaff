package com.petcare.staff.data.model.api.record;

import java.util.List;

public class CreatePrescriptionRequest {

    /*
      "examination_id": "string",
      "medications": [
        {
          "dosage": "string",
          "end_date": "string",
          "name": "string",
          "start_date": "string"
        }
      ]
  */
    private String examination_id;
    private List<MedicationResponse> medications;

    public CreatePrescriptionRequest(String examination_id, List<MedicationResponse> medications) {
        this.examination_id = examination_id;
        this.medications = medications;
    }

    public String getExamination_id() {
        return examination_id;
    }

    public void setExamination_id(String examination_id) {
        this.examination_id = examination_id;
    }

    public List<MedicationResponse> getMedications() {
        return medications;
    }

    public void setMedications(List<MedicationResponse> medications) {
        this.medications = medications;
    }
}
