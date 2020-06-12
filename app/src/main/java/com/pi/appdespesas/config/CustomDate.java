package com.pi.appdespesas.config;

import java.text.SimpleDateFormat;

public class CustomDate {

    public static String dataAtual(){
        long data = System.currentTimeMillis();
        SimpleDateFormat simpleDateFormat = new SimpleDateFormat("dd/MM/yyyy");
        String dataString = simpleDateFormat.format(data);
        return dataString;
    }

    public static String dataMesFormatada(String data){
        String dataRetorno[] = data.split("/");
        String dia = dataRetorno[0];
        String mes =  dataRetorno[1];
        String ano = dataRetorno[2];
        return mes+ano;
    }
}
