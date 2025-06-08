package com.petcare.staff.ui.common.repository;

public interface RepositoryCallback {
    void onSuccess(String message);

    void onError(Throwable t);
}
