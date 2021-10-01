package com.hostelfinder.hostellocator.HelperClasses;

import android.content.Context;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;

public class CheckInternetConnection {

    public boolean isConnected(Context context) {
        ConnectivityManager connectivityManager = (ConnectivityManager) context.getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo WifiConn = connectivityManager.getNetworkInfo(ConnectivityManager.TYPE_WIFI);
        NetworkInfo MobileData = connectivityManager.getNetworkInfo(ConnectivityManager.TYPE_MOBILE);
        if ((WifiConn != null && WifiConn.isConnected()) || (MobileData != null && MobileData.isConnected())) {
            return true;
        } else {
            return false;
        }
    }
}
