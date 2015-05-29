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
    public static native String getGetMyAwards();
    public static native String getGetListHistory();
    public static native String getGetEventHistory();
    public static native String getGetAwardHistory();
    public static native String getGetHistory();

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

    public static void getMyAwards(final String token, final OnGetMyAwardsResult mOnGetMyAwardsResult) {

        Thread t = new Thread() {
            @Override
            public void run() {
                super.run();

                String link = getGetMyAwards();

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
                    GetMyAwardsResult result = (GetMyAwardsResult) Helper.jsonToObject(response, GetMyAwardsResult.class);
                    if(result.error.equals("")) {
                        ArrayList<AwardHistory> listAwards = new ArrayList<AwardHistory>();
                        for(int i=0; i<result.listAwards.length-1; i++) {
                            listAwards.add(result.listAwards[i]);
                        }
                        mOnGetMyAwardsResult.onSuccess(listAwards);
                    }
                    else
                        mOnGetMyAwardsResult.onError(result.error);

                } catch (UnsupportedEncodingException e) {
                    mOnGetMyAwardsResult.onError("UnsupportedEncodingException");
                    e.printStackTrace();
                } catch (ClientProtocolException e) {
                    mOnGetMyAwardsResult.onError("ClientProtocolException");
                    e.printStackTrace();
                } catch (IOException e) {
                    mOnGetMyAwardsResult.onError("IOException");
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

    public static void getHistory(final String token, final String historyId,
                                  final OnGetHistoryResult mOnGetHistoryResult){

        Thread t = new Thread() {
            @Override
            public void run() {
                super.run();

                String link = getGetHistory();

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
                    GetHistoryResult result = (GetHistoryResult) Helper.jsonToObject(response, GetHistoryResult.class);

                    if(result.error.equals("")) {
                        mOnGetHistoryResult.onSuccess(result.history);
                    }
                    else
                        mOnGetHistoryResult.onError(result.error);

                } catch (UnsupportedEncodingException e) {
                    mOnGetHistoryResult.onError("UnsupportedEncodingException");
                    e.printStackTrace();
                } catch (ClientProtocolException e) {
                    mOnGetHistoryResult.onError("ClientProtocolException");
                    e.printStackTrace();
                } catch (IOException e) {
                    mOnGetHistoryResult.onError("IOException");
                    e.printStackTrace();
                }
            }
        };
        t.start();
    }

    public interface OnGetHistoryResult{
        public void onSuccess(History history);
        public void onError(String error);
    }

    public class GetHistoryResult{
        public String error;
        public History history;
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

    public interface OngetAchievedEventListResult{
        public void onSuccess(ArrayList<AchievedEvent> listAchievedEvents);
        public void onError(String error);
    }

    public class getAchievedEvents {
        public String error;
        public AchievedEvent[] listAchievedEvents;
    }

    public interface OnGetListHistoryResult{
        public void onSuccess(ArrayList<History> listHistories);
        public void onError(String error);
    }

    public class GetListHistoryResult{
        public String error;
        public History[] listHistories;
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

    public interface OnGetMyAwardsResult {
        public void onSuccess(ArrayList<AwardHistory> awards);

        public void onError(String e);
    }

    public class GetMyAwardsResult {
        public String error;
        public AwardHistory[] listAwards;
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
