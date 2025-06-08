package com.petcare.staff.data.model.api.payment;

import com.google.gson.annotations.SerializedName;

public enum PaymentMethod {
    @SerializedName("CASH")
    CASH,

    @SerializedName("BANK")
    BANK
}