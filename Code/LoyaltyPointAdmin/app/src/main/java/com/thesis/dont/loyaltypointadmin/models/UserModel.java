package com.thesis.dont.loyaltypointadmin.models;

import android.util.Log;

import com.thesis.dont.loyaltypointadmin.controllers.Helper;

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
    public static native String getSelectUser();
    public static native String getCheckIdentityNumberUser();
    public static native String getGetEventHistory();
    public static native String getGetAwardHistory();
    public static native String getGetListHistory();

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

    public static void checkIdentityNumberUser(final String token, final String username, final String identityNumber, final OnCheckIdentityNumberResult mOnCheckIdentityNumberResult) {
        Thread t = new Thread() {
            @Override
            public void run() {
                super.run();

                String link = getCheckIdentityNumberUser();

                httpclient = new DefaultHttpClient();
                httppost = new HttpPost(link);

                nameValuePairs = new ArrayList<NameValuePair>(3);

                nameValuePairs.add(new BasicNameValuePair("token", token));
                nameValuePairs.add(new BasicNameValuePair("userId", username));
                nameValuePairs.add(new BasicNameValuePair("identityNumber", identityNumber));

                try {
                    httppost.setEntity(new UrlEncodedFormEntity(nameValuePairs, "UTF-8"));
                    //ResponseHandler<String> responseHandler = new BasicResponseHandler();
                    ResponseHandler<String> responseHandler = Helper.getResponseHandler();
                    String response = null;

                    response = httpclient.execute(httppost, responseHandler); //tr? v? ki?u {"error":"","token":""}
                    CheckIdentityNumberResult result = (CheckIdentityNumberResult) Helper.jsonToObject(response, CheckIdentityNumberResult.class);
                    if(result.error.equals(""))
                        mOnCheckIdentityNumberResult.onSuccess();
                    else
                        mOnCheckIdentityNumberResult.onError(result.error);

                } catch (UnsupportedEncodingException e) {
                    mOnCheckIdentityNumberResult.onError("UnsupportedEncodingException");
                    e.printStackTrace();
                } catch (ClientProtocolException e) {
                    mOnCheckIdentityNumberResult.onError("ClientProtocolException");
                    e.printStackTrace();
                } catch (IOException e) {
                    mOnCheckIdentityNumberResult.onError("IOException");
                    e.printStackTrace();
                }
            }
        };
        t.start();
    }

    public static void getListHistory(final String token, final String shopId, final String cardId,
                                  final OnGetListHistoryResult mOnGetListHistoryResult){

        Thread t = new Thread() {
            @Override
            public void run() {
                super.run();

                String link = getGetListHistory();

                httpclient = new DefaultHttpClient();
                httppost = new HttpPost(link);

                nameValuePairs = new ArrayList<NameValuePair>(2);

                nameValuePairs.add(new BasicNameValuePair("token", token));
                nameValuePairs.add(new BasicNameValuePair("shop_id", shopId));
                nameValuePairs.add(new BasicNameValuePair("card_id", cardId));

                try {
                    httppost.setEntity(new UrlEncodedFormEntity(nameValuePairs, "UTF-8"));
                    //ResponseHandler<String> responseHandler = new BasicResponseHandler();
                    ResponseHandler<String> responseHandler = Helper.getResponseHandler();
                    String response = null;

                    response = httpclient.execute(httppost, responseHandler);
                    GetListHistoryResult result = (GetListHistoryResult) Helper.jsonToObject(response, GetListHistoryResult.class);

                    if(result.error.equals("")) {
                        ArrayList<History> listHistories = new ArrayList<History>();
                        for(int i=0; i<result.listHistories.length-1; i++) {
                            listHistories.add(result.listHistories[i]);
                        }
                        mOnGetListHistoryResult.onSuccess(listHistories);
                    }
                    else
                        mOnGetListHistoryResult.onError(result.error);

                } catch (UnsupportedEncodingException e) {
                    mOnGetListHistoryResult.onError("UnsupportedEncodingException");
                    e.printStackTrace();
                } catch (ClientProtocolException e) {
                    mOnGetListHistoryResult.onError("ClientProtocolException");
                    e.printStackTrace();
                } catch (IOException e) {
                    mOnGetListHistoryResult.onError("IOException");
                    e.printStackTrace();
                }
            }
        };
        t.start();
    }

    public static void getAchievedEventList(final String token, final String historyId, final OngetAchievedEventListResult mOngetAchievedEventListResult){
        Thread t = new Thread() {
            @Override
            public void run() {
                super.run();

                String link = getGetEventHistory();

                httpclient = new DefaultHttpClient();
                httppost = new HttpPost(link);

                nameValuePairs = new ArrayList<NameValuePair>(2);

                nameValuePairs.add(new BasicNameValuePair("token", token));
                nameValuePairs.add(new BasicNameValuePair("history_id", historyId));

                try {
                    httppost.setEntity(new UrlEncodedFormEntity(nameValuePairs, "UTF-8"));
                    //ResponseHandler<String> responseHandler = new BasicResponseHandler();
                    ResponseHandler<String> responseHandler = Helper.getResponseHandler();
                    String response = null;

                    response = httpclient.execute(httppost, responseHandler);
                    getAchievedEvents result = (getAchievedEvents) Helper.jsonToObject(response, getAchievedEvents.class);

                    if(result.error.equals("")) {
                        ArrayList<AchievedEvent> listAchievedEvents = new ArrayList<AchievedEvent>();
                        for(int i=0; i<result.listAchievedEvents.length-1; i++) {
                            listAchievedEvents.add(result.listAchievedEvents[i]);
                        }
                        mOngetAchievedEventListResult.onSuccess(listAchievedEvents);
                    }
                    else
                        mOngetAchievedEventListResult.onError(result.error);

                } catch (UnsupportedEncodingException e) {
                    mOngetAchievedEventListResult.onError("UnsupportedEncodingException");
                    e.printStackTrace();
                } catch (ClientProtocolException e) {
                    mOngetAchievedEventListResult.onError("ClientProtocolException");
                    e.printStackTrace();
                } catch (IOException e) {
                    mOngetAchievedEventListResult.onError("IOException");
                    e.printStackTrace();
                }
            }
        };
        t.start();
    }

    public static void getAwardHistory(final String token, final String historyId, final OngetAwardHistoryResult mOngetAwardHistoryResult){
        Thread t = new Thread() {
            @Override
            public void run() {
                super.run();

                String link = getGetAwardHistory();

                httpclient = new DefaultHttpClient();
                httppost = new HttpPost(link);

                nameValuePairs = new ArrayList<NameValuePair>(2);

                nameValuePairs.add(new BasicNameValuePair("token", token));
                nameValuePairs.add(new BasicNameValuePair("history_id", historyId));

                try {
                    httppost.setEntity(new UrlEncodedFormEntity(nameValuePairs, "UTF-8"));
                    //ResponseHandler<String> responseHandler = new BasicResponseHandler();
                    ResponseHandler<String> responseHandler = Helper.getResponseHandler();
                    String response = null;

                    response = httpclient.execute(httppost, responseHandler);
                    getAwardHistory result = (getAwardHistory) Helper.jsonToObject(response, getAwardHistory.class);

                    if(result.error.equals("")) {
                        mOngetAwardHistoryResult.onSuccess(result.award,result.award_number);
                    }
                    else
                        mOngetAwardHistoryResult.onError(result.error);

                } catch (UnsupportedEncodingException e) {
                    mOngetAwardHistoryResult.onError("UnsupportedEncodingException");
                    e.printStackTrace();
                } catch (ClientProtocolException e) {
                    mOngetAwardHistoryResult.onError("ClientProtocolException");
                    e.printStackTrace();
                } catch (IOException e) {
                    mOngetAwardHistoryResult.onError("IOException");
                    e.printStackTrace();
                }
            }
        };
        t.start();
    }

    public interface OngetAwardHistoryResult{
        public void onSuccess(Award award, int awardNumber);
        public void onError(String error);
    }

    public class getAwardHistory {
        public String error;
        public Award award;
        public int award_number;
    }

    public interface OnGetListHistoryResult{
        public void onSuccess(ArrayList<History> listHistories);
        public void onError(String error);
    }

    public class GetListHistoryResult{
        public String error;
        public History[] listHistories;
    }

    public interface OngetAchievedEventListResult{
        public void onSuccess(ArrayList<AchievedEvent> listAchievedEvents);
        public void onError(String error);
    }

    public class getAchievedEvents {
        public String error;
        public AchievedEvent[] listAchievedEvents;
    }

    public class LoginResult {
        String error;
        String token;
    }

    public interface OnRegisterResult {
        public void onSuccess();

        public void onError(Exception e);
    }

    public interface OnCheckIdentityNumberResult {
        public void onSuccess();

        public void onError(String error);
    }

    public class CheckIdentityNumberResult {
        String error;
    }

    public interface OnLoginResult {
        public void onSuccess(String token);

        public void onError(String error);
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
