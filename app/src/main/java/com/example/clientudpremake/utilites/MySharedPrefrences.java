package com.example.clientudpremake.utilites;

import android.content.Context;
import android.content.SharedPreferences;

public class MySharedPrefrences {
    private static final String MY_PREFRENCE = "serverCache";
    private static final String SERVER_KEY = "serverKey";

    public static void setServerAddress(Context context, String serverAddress) {
        SharedPreferences sharedPreferences = getSharedPreferences(context);
        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.putString(SERVER_KEY, serverAddress);
        editor.apply();
        LogUtility.log("Saved server address: " + serverAddress + " into sharedpref");
    }

    public static String getServerAddress(Context context) {
        SharedPreferences sharedPreferences = getSharedPreferences(context);
        return sharedPreferences.getString(SERVER_KEY, null);
    }

    private static SharedPreferences getSharedPreferences(Context context) {
        return context.getSharedPreferences(MY_PREFRENCE, Context.MODE_PRIVATE);
    }
}
