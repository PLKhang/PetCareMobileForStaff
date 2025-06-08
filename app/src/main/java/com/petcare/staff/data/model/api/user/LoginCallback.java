package com.petcare.staff.data.model.api.user;

public interface LoginCallback {
    void onSuccess(String token);
    void onError(String message);
}
