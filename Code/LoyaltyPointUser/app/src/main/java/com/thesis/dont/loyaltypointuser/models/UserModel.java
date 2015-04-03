package com.thesis.dont.loyaltypointuser.models;

import com.thesis.dont.loyaltypointuser.controllers.Helper;

import org.apache.http.HttpResponse;
import org.apache.http.NameValuePair;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.HttpClient;
import org.apache.http.client.ResponseHandler;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.impl.client.BasicResponseHandler;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.message.BasicNameValuePair;

import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by tinntt on 2/3/2015.
 */
public class UserModel {
    static HttpPost httppost;
    static HttpResponse response;
    static HttpClient httpclient;
    static List<NameValuePair> nameValuePairs;

    static OnRegisterResult mOnRegisterResult;
    static OnLoginResult mOnLoginResult;


    static {
        System.loadLibrary("services");
    }
    public static native String getAddUser();
    public static native String getCheckUser();
    public static native String getGetUserInfo();

    public static void addUser(User user) {
        final String json = Helper.objectToJson(user);
        Thread t = new Thread() {
            @Override
            public void run() {
                super.run();

                String link = getAddUser();

                httpclient = new DefaultHttpClient();
                httppost = new HttpPost(link);

                nameValuePairs = new ArrayList<NameValuePair>(1);

                nameValuePairs.add(new BasicNameValuePair("user", json));

                try {
                    httppost.setEntity(new UrlEncodedFormEntity(nameValuePairs, "UTF-8"));
                    ResponseHandler<String> responseHandler = new BasicResponseHandler();
                    /*ResponseHandler<String> responseHandler = Helper.getResponseHandler();*/
                    String response = null;

                    response = httpclient.execute(httppost, responseHandler);
                    if(response.equals("true"))
                        mOnRegisterResult.onSuccess();
                    else
                        mOnRegisterResult.onError(null);
                } catch (UnsupportedEncodingException e) {
                    mOnRegisterResult.onError(e);
                    e.printStackTrace();
                } catch (ClientProtocolException e) {
                    mOnRegisterResult.onError(e);
                    e.printStackTrace();
                } catch (IOException e) {
                    mOnRegisterResult.onError(e);
                    e.printStackTrace();
                }
            }
        };
        t.start();
    }

    public static void checkUser(String username, String hashpass) {
        final String fUserName = username;
        final String fHashpass = hashpass;
        Thread t = new Thread() {
            @Override
            public void run() {
                super.run();

                String link = getCheckUser();

                httpclient = new DefaultHttpClient();
                httppost = new HttpPost(link);

                nameValuePairs = new ArrayList<NameValuePair>(1);

                nameValuePairs.add(new BasicNameValuePair("username", fUserName));
                nameValuePairs.add(new BasicNameValuePair("hashpass", fHashpass));

                try {
                    httppost.setEntity(new UrlEncodedFormEntity(nameValuePairs, "UTF-8"));
                    //ResponseHandler<String> responseHandler = new BasicResponseHandler();
                    ResponseHandler<String> responseHandler = Helper.getResponseHandler();
                    String response = null;

                    response = httpclient.execute(httppost, responseHandler); //tr? v? ki?u {"error":"","token":""}
                    LoginResult result = (LoginResult) Helper.jsonToObject(response, LoginResult.class);
                    if(result.error.equals(""))
                        mOnLoginResult.onSuccess(result.token);
                    else
                        mOnLoginResult.onError(result.error);

                } catch (UnsupportedEncodingException e) {
                    mOnLoginResult.onError("UnsupportedEncodingException");
                    e.printStackTrace();
                } catch (ClientProtocolException e) {
                    mOnLoginResult.onError("ClientProtocolException");
                    e.printStackTrace();
                } catch (IOException e) {
                    mOnLoginResult.onError("IOException");
                    e.printStackTrace();
                }
            }
        };
        t.start();
    }

    public static void getUserInfo(final String token, final OnGetUserInfoResult mOnGetUserInfoResult){

        Thread t = new Thread() {
            @Override
            public void run() {
                super.run();

                String link = getGetUserInfo();

                httpclient = new DefaultHttpClient();
                httppost = new HttpPost(link);

                nameValuePairs = new ArrayList<NameValuePair>(1);

                nameValuePairs.add(new BasicNameValuePair("token", token));

                try {
                    httppost.setEntity(new UrlEncodedFormEntity(nameValuePairs, "UTF-8"));
                    //ResponseHandler<String> responseHandler = new BasicResponseHandler();
                    ResponseHandler<String> responseHandler = Helper.getResponseHandler();
                    String response = null;

                    response = httpclient.execute(httppost, responseHandler);
                    GetUserInfoResult result = (GetUserInfoResult) Helper.jsonToObject(response, GetUserInfoResult.class);
                    if(result.error == "")
                        mOnGetUserInfoResult.onSuccess(result.user);
                    else
                        mOnGetUserInfoResult.onError(result.error);

                } catch (UnsupportedEncodingException e) {
                    mOnGetUserInfoResult.onError("UnsupportedEncodingException");
                    e.printStackTrace();
                } catch (ClientProtocolException e) {
                    mOnGetUserInfoResult.onError("ClientProtocolException");
                    e.printStackTrace();
                } catch (IOException e) {
                    mOnGetUserInfoResult.onError("IOException");
                    e.printStackTrace();
                }
            }
        };
        t.start();
    }

    public class LoginResult {
        String error;
        String token;
    }

    public class GetUserInfoResult {
        String error;
        User user;
    }

    public interface OnRegisterResult {
        public void onSuccess();

        public void onError(Exception e);
    }

    public interface OnLoginResult {
        public void onSuccess(String token);

        public void onError(String error);
    }

    public interface OnGetUserInfoResult {
        public void onSuccess(User user);

        public void onError(String e);
    }


    // GETTERS / SETTES
    public static OnLoginResult getOnLoginResult() {
        return mOnLoginResult;
    }

    public static void setOnLoginResult(OnLoginResult mOnLoginResult) {
        UserModel.mOnLoginResult = mOnLoginResult;
    }

    public static OnRegisterResult getOnRegisterResult() {
        return mOnRegisterResult;
    }

    public static void setOnRegisterResult(OnRegisterResult onRegisterResult) {
        mOnRegisterResult = onRegisterResult;
    }
}