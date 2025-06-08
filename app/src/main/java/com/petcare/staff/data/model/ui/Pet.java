package com.petcare.staff.data.model.ui;

import com.petcare.staff.utils.DateTime;

import java.util.List;

public class Pet {
    private List<VaccineRecord> vaccineRecordList;
    private List<MedicalRecord> medicalRecordList;
    private String id;
    private String name;
    private String ownerId;
    private DateTime birth;
    private String color;
    private String identityMark;
    private String species;
    private float weight;

    public Pet(String id, String name, String ownerId, DateTime birth,
               String color, String identityMark, String species, float weight) {
        this.id = id;
        this.name = name;
        this.ownerId = ownerId;
        this.birth = birth;
        this.color = color;
        this.identityMark = identityMark;
        this.species = species;
        this.weight = weight;
    }

    public List<VaccineRecord> getVaccineRecordList() {
        return vaccineRecordList;
    }

    public void setVaccineRecordList(List<VaccineRecord> vaccineRecordList) {
        this.vaccineRecordList = vaccineRecordList;
    }

    public List<MedicalRecord> getMedicalRecordList() {
        return medicalRecordList;
    }

    public void setMedicalRecordList(List<MedicalRecord> medicalRecordList) {
        this.medicalRecordList = medicalRecordList;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getOwnerId() {
        return ownerId;
    }

    public void setOwnerId(String ownerId) {
        this.ownerId = ownerId;
    }

    public DateTime getBirth() {
        return birth;
    }

    public void setBirth(DateTime birth) {
        this.birth = birth;
    }

    public String getColor() {
        return color;
    }

    public void setColor(String color) {
        this.color = color;
    }

    public String getIdentityMark() {
        return identityMark;
    }

    public void setIdentityMark(String identityMark) {
        this.identityMark = identityMark;
    }

    public String getSpecies() {
        return species;
    }

    public void setSpecies(String species) {
        this.species = species;
    }

    public float getWeight() {
        return weight;
    }

    public void setWeight(float weight) {
        this.weight = weight;
    }
}
