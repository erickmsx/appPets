package com.erickmxav.apppets.helper;

import android.util.Base64;

public class Base64Custom {

    public static String codifyBase64(String texto){

        return Base64.encodeToString( texto.getBytes(), Base64.DEFAULT )
    .replaceAll("\\n|\\r", "");

    }

    public static String decodificarBase64(String textoCodificao){

        return new String (Base64.decode( textoCodificao, Base64.DEFAULT ) );

    }
}
