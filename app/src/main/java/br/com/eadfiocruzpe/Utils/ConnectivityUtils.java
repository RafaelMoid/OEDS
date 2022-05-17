package br.com.eadfiocruzpe.Utils;

import android.content.Context;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;

public class ConnectivityUtils {

    public static boolean isConnected(Context context) {
        if (context == null) {
            return false;
        }

        ConnectivityManager cm = (ConnectivityManager) context.getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo networkInfo = cm.getActiveNetworkInfo();

        if (networkInfo == null) {
            return false;
        }

        if (!networkInfo.isConnected()) {
            return false;
        }

        if (!networkInfo.isAvailable()){
            return false;
        }

        return true;
    }

}