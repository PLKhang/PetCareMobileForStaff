package com.petcare.staff.data.model.api.order;

import com.google.gson.annotations.SerializedName;

public enum OrderStatus {
    UNSPECIFIED,
    @SerializedName("PENDING")
    PENDING,

    @SerializedName("PAID")
    PAID,

    @SerializedName("COMPLETED")
    COMPLETED,

    @SerializedName("CANCELLED")
    CANCELLED
}
