package com.thesis.dont.loyaltypointadmin.controllers;

import com.google.gson.Gson;
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
}
