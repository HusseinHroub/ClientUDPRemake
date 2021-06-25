package com.example.clientudpremake.utilites;

import android.content.Context;

import androidx.appcompat.app.AlertDialog;

public class DialogUtility {
    public static void showConfirmationDialog(Context context, Runnable onConfirmedRunnable) {
        final AlertDialog alertDialog = createConfirmationDialog("Are you sure?", context, onConfirmedRunnable);
        alertDialog.show();
    }

    private static AlertDialog createConfirmationDialog(String title, Context context, Runnable onConfirmedRunnable) {
        final AlertDialog.Builder builder = new AlertDialog.Builder(context);
        builder.setTitle(title);
        builder.setPositiveButton("Yes", (dialog, which) -> onConfirmedRunnable.run());
        builder.setNegativeButton("Cancel", (dialog, which) -> dialog.cancel());
        return builder.create();
    }
}
