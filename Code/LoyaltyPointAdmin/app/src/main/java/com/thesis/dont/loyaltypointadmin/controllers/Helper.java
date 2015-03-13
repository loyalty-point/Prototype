package com.thesis.dont.loyaltypointadmin.controllers;

import com.google.gson.Gson;
import com.thesis.dont.loyaltypointadmin.models.UserModel;

import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.StatusLine;
import org.apache.http.client.HttpResponseException;
import org.apache.http.client.ResponseHandler;
import org.apache.http.util.EntityUtils;

import java.io.IOException;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.Formatter;
import java.util.Objects;

/**
 * Created by tinntt on 2/4/2015.
 */
public class Helper {
    public static String objectToJson(Object obj){
        Gson gson = new Gson();
        return gson.toJson(obj);
    }

    public static Object jsonToObject(String json, Class myClass){
        Gson gson = new Gson();
        return gson.fromJson(json, myClass);
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

    public static boolean checkPassword(String password) {
        // check size (trên 6 kí tự)
        if(password.length() < 6)
            return true;

        return false;
    }

    public static boolean checkUserName(String username) {
        // check size (trên 6 kí tự)
        if(username.length() < 6)
            return true;

        // check kí tự đặc biệt (username chỉ chứa kí tự và số)
        for(int i=0; i<username.length(); i++) {
            char c = username.charAt(i);
            if(c < '0' || (c > '9' && c < 'A') || (c > 'Z' && c < 'a') || c > 'z')
                return true;
        }

        return false;
    }

    public static boolean checkShopName(String shopname) {
        // check size (trên 2 kí tự)
        if(shopname.length() < 2)
            return true;

        return false;
    }

    public static boolean checkNotNull(String username, String password, String confirmPassword, String fullname, String phone) {
        if(username.equals("") || password.equals("") || confirmPassword.equals("") ||
                fullname.equals("") || phone.equals("")) {
            return true;
        }

        return false;
    }

    public static boolean checkNotNull(String shopname, String phone, String exchangeRatio, String address) {
        if(shopname.equals("") || phone.equals("") || exchangeRatio.equals("") ||
                address.equals("")) {
            return true;
        }

        return false;
    }

    public static boolean checkNotNull(String username, String password) {
        if(username.equals("") || password.equals("")) {
            return true;
        }

        return false;
    }

    public static ResponseHandler<String> getResponseHandler() {
        return new ResponseHandler<String>() {
            public String handleResponse(final HttpResponse response)
                    throws HttpResponseException, IOException {
                StatusLine statusLine = response.getStatusLine();
                if (statusLine.getStatusCode() >= 300) {
                    throw new HttpResponseException(statusLine.getStatusCode(),
                            statusLine.getReasonPhrase());
                }

                HttpEntity entity = response.getEntity();
                return entity == null ? null : EntityUtils.toString(entity, "UTF-8");
            }
        };
    }
}
