package com.ptit.timetable.utils;

import android.util.Base64;
import android.util.Log;

import java.io.UnsupportedEncodingException;
import java.nio.charset.StandardCharsets;

public class DecodeToken {
    private static String[] split;

    public static String decoded(String JWTEncoded) throws Exception {
        try {
            split = JWTEncoded.split("\\.");
            Log.d("JWT_DECODED", "Header: " + getJson(split[0]));
            Log.d("JWT_DECODED", "Body: " + getJson(split[1]));
            Log.d("JWT_DECODED", "Signiture: " + getJson(split[2]));
        } catch (UnsupportedEncodingException e) {
            //Error
        }
        return getJson(split[1]);
    }

    private static String getJson(String strEncoded) throws UnsupportedEncodingException {
        byte[] decodedBytes = Base64.decode(strEncoded, Base64.URL_SAFE);
        return new String(decodedBytes, StandardCharsets.UTF_8);
    }

//    public static void main(String[] args) throws UnsupportedEncodingException {
//        System.out.println(getJson("eyJhbGciOiJIUzI1NiJ9.eyJpZCI6MywiZXhwIjoxNTkzNDIwODM2LCJ1c2VybmFtZSI6IjEifQ.HoSRi_xWOfMN4L4zwJxRk0cQpS27dUaOsRgJsWW4CnQ"));;
//    }
}
