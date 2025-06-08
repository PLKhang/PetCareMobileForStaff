package com.petcare.staff.data.model.api.record;

import com.google.gson.annotations.SerializedName;

public class IdResponse {
    @SerializedName("id")
    private String id;

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }
}