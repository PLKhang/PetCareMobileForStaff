package com.petcare.staff.data.model.api.payment;

import com.google.gson.annotations.SerializedName;

public enum PaymentStatus {
    @SerializedName("PAYMENT_STATUS_UNSPECIFIED")
    PAYMENT_STATUS_UNSPECIFIED,
    @SerializedName("PENDING")
    PENDING,

    @SerializedName("COMPLETED")
    COMPLETED,

    @SerializedName("FAILED")
    FAILED,

    @SerializedName("CANCELLED")
    CANCELLED
}
