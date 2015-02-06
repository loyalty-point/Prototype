package com.thesis.dont.loyaltypointadmin.controllers;

import com.google.gson.Gson;

import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.Formatter;

/**
 * Created by tinntt on 2/4/2015.
 */
public class Helper {
    public static String objectToJson(Object obj){
        Gson gson = new Gson();
        return gson.toJson(obj);
    }

    public static Object jsonToObject(String json){
        Gson gson = new Gson();
        return gson.fromJson(json, Object.class);
    }

    public static String hashPassphrase(String passPhrase, String salt) {
        MessageDigest digest = null;
        try {
            digest = MessageDigest.getInstance("MD5");
        } catch (NoSuchAlgorithmException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
        digest.reset();
        digest.update(salt.getBytes());
        byte[] hashedBytes = digest.digest((passPhrase.getBytes()));
        return bytesToString(hashedBytes);
    }

    private static String bytesToString(byte[] hashValue) {
        Formatter form = new Formatter();
        for (int i = 0; i < hashValue.length; i++)
            form.format("%02x", hashValue[i]);
        return form.toString();
    }
}
