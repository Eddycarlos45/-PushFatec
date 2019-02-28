package com.example.edson.pushfatecadmin.Util;

import android.content.Context;
import android.content.SharedPreferences;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;

public class Util {

//Retorna se esta conectado com Internet ou não
    public static boolean statusInternet_MoWi(Context context) {

        boolean status = false;
        ConnectivityManager conexao = (ConnectivityManager) context.getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo informacao = conexao.getActiveNetworkInfo();

        if (informacao != null && informacao.isConnected()) {
            status = true;
        } else
            status = false;

        return status;
    }





    public static String getTopico(Context context, String id, String nomeTopico){


        SharedPreferences preferences = context.getSharedPreferences(nomeTopico,0);

        String valor = preferences.getString(id,"");

        return valor;



    }


    public static void setTopico(Context context, String id, String nomeTopico){


        SharedPreferences preferences = context.getSharedPreferences(id,0);

        SharedPreferences.Editor editor = preferences.edit();


        editor.putString(id,nomeTopico);

        editor.commit();

    }








}
