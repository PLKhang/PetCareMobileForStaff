package com.petcare.staff.data.model.api.record;

public class CreatePetRequest {
    /*
        {
            "color": "string",
            "Dob": "string",
            "identity_mark": "string"
            "name": "string",
            "owner_id": "string",
            "species": "string",
            "weight": 0
        }
        */
    private String color;
    private String Dob;
    private String identity_mark;
    private String name;
    private String owner_id;
    private String species;
    private float weight;

    public CreatePetRequest(String color, String dob, String identity_mark, String name, String owner_id, String species, float weight) {
        this.color = color;
        this.Dob = dob;
        this.identity_mark = identity_mark;
        this.name = name;
        this.owner_id = owner_id;
        this.species = species;
        this.weight = weight;
    }

    public String getColor() {
        return color;
    }

    public void setColor(String color) {
        this.color = color;
    }

    public String getDob() {
        return Dob;
    }

    public void setDob(String dob) {
        this.Dob = dob;
    }

    public String getIdentity_mark() {
        return identity_mark;
    }

    public void setIdentity_mark(String identity_mark) {
        this.identity_mark = identity_mark;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getOwner_id() {
        return owner_id;
    }

    public void setOwner_id(String owner_id) {
        this.owner_id = owner_id;
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