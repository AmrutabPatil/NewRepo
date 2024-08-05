package com.neonai.axocomplaints;

import android.annotation.SuppressLint;
import android.content.Context;
import android.net.ConnectivityManager;

public class DetectConnection {
    public static boolean checkInternetConnection(Context context) {
        @SuppressLint("WrongConstant") ConnectivityManager con_manager = (ConnectivityManager) context.getSystemService("connectivity");
        return con_manager.getActiveNetworkInfo() != null && con_manager.getActiveNetworkInfo().isAvailable() && con_manager.getActiveNetworkInfo().isConnected();
    }
}
