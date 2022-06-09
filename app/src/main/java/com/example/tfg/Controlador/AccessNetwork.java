package com.example.tfg.Controlador;

import android.content.Context;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;

public class AccessNetwork {
    /**
     * Metodo que comprueba si el depositivo esta conectado a internet
     * @param context
     * @return boolean
     */
    public static boolean checkNetworkState(Context context){
        ConnectivityManager connectivityManager = (ConnectivityManager) context.getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo networkInfo = connectivityManager.getActiveNetworkInfo();
        return (networkInfo!=null && networkInfo.isConnected());
    }


}
