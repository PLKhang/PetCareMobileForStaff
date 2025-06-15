package com.petcare.staff.utils;

import android.content.Context;

import com.google.android.material.dialog.MaterialAlertDialogBuilder;

public class DialogUtils {

    public interface OnConfirmationListener {
        void onConfirmed();
    }

    public static void showConfirmationDialog(Context context,
                                              String title,
                                              String message,
                                              String positiveText,
                                              String negativeText,
                                              OnConfirmationListener listener) {
        new MaterialAlertDialogBuilder(context)
                .setTitle(title)
                .setMessage(message)
                .setPositiveButton(positiveText, (dialog, which) -> {
                    listener.onConfirmed();
                    dialog.dismiss();
                })
                .setNegativeButton(negativeText, (dialog, which) -> dialog.dismiss())
                .show();
    }

    public static void showDeleteConfirmation(Context context, OnConfirmationListener listener) {
        showConfirmationDialog(
                context,
                "Delete Record",
                "Are you sure you want to delete this record? This action cannot be undone.",
                "Delete",
                "Cancel",
                listener
        );
    }

    public static void showCreateConfirmation(Context context, OnConfirmationListener listener) {
        showConfirmationDialog(
                context,
                "Create Record",
                "Do you want to create this record?",
                "Create",
                "Cancel",
                listener
        );
    }
}

