package com.petcare.staff.data.model.api.record;

public class VaccinationResponse {
/*
    {
        "date": "string",
        "id": "string",
        "next_dose": "string",
        "pet_id": "string",
        "vaccine_name": "string",
        "vet_id": "string"
    }
    */

    private String date;
    private String next_dose;
    private String id;
    private String pet_id;
    private String vaccine_name;
    private String vet_id;

    public VaccinationResponse(String date, String next_dose, String id, String pet_id, String vaccine_name, String vet_id) {
        this.date = date;
        this.next_dose = next_dose;
        this.id = id;
        this.pet_id = pet_id;
        this.vaccine_name = vaccine_name;
        this.vet_id = vet_id;
    }

    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }

    public String getNext_dose() {
        return next_dose;
    }

    public void setNext_dose(String next_dose) {
        this.next_dose = next_dose;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getPet_id() {
        return pet_id;
    }

    public void setPet_id(String pet_id) {
        this.pet_id = pet_id;
    }

    public String getVaccine_name() {
        return vaccine_name;
    }

    public void setVaccine_name(String vaccine_name) {
        this.vaccine_name = vaccine_name;
    }

    public String getVet_id() {
        return vet_id;
    }

    public void setVet_id(String vet_id) {
        this.vet_id = vet_id;
    }
}
