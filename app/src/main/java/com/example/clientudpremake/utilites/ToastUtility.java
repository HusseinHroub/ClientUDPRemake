package com.example.clientudpremake.utilites;

import android.content.Context;
import android.view.Gravity;
import android.widget.Toast;

public class ToastUtility {

    private ToastUtility() {

    }

    public static void showMessage(String message, Context context) {
        Toast.makeText(context, message, Toast.LENGTH_SHORT).show();
    }

    public static void showMessageAtTop(String message, Context context) {
        Toast toast = Toast.makeText(context, message, Toast.LENGTH_SHORT);
        toast.setGravity(Gravity.TOP | Gravity.CENTER_HORIZONTAL, 0, 20);
        toast.show();
    }

}
