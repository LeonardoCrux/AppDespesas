package com.pi.appdespesas.config;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;

import com.pi.appdespesas.view.activity.PrincipalActivity;

public class AppUtil {

    public static void salvarIdUsuario(Context context, String userId) {
        SharedPreferences preferences = context.getSharedPreferences("APP", Context.MODE_PRIVATE);
        preferences.edit().putString("UserID", userId).apply();
    }

    public static String getIdUsuario(Context context) {
        SharedPreferences preferences = context.getSharedPreferences("APP", Context.MODE_PRIVATE);
        return preferences.getString("UserID", "");
    }

    public static void irActivityPrincipal(Context context){
        context.startActivity(new Intent(context, PrincipalActivity.class));
    }
}
