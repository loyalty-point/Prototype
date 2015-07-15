package com.thesis.dont.loyaltypointadmin.models;

import com.thesis.dont.loyaltypointadmin.controllers.Helper;

import org.apache.http.HttpResponse;
import org.apache.http.NameValuePair;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.HttpClient;
import org.apache.http.client.ResponseHandler;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.message.BasicNameValuePair;

import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by tinntt on 4/17/2015.
 */
public class CustomerModel {

    static HttpPost httppost;
    static HttpResponse response;
    static HttpClient httpclient;
    static List<NameValuePair> nameValuePairs;

    static {
        System.loadLibrary("services");
    }

    public static native String getGetListHistory();
    public static native String getGetListHistoryCard();

    public static void getListHistory(final String token, final String shopId, final String username,
                                      final OnGetListHistoryResult mOnGetListHistoryResult) {
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
                nameValuePairs.add(new BasicNameValuePair("customer_id", username));

                try {
                    httppost.setEntity(new UrlEncodedFormEntity(nameValuePairs, "UTF-8"));
                    //ResponseHandler<String> responseHandler = new BasicResponseHandler();
                    ResponseHandler<String> responseHandler = Helper.getResponseHandler();
                    String response = null;

                    response = httpclient.execute(httppost, responseHandler);
                    GetListHistoryResult result = (GetListHistoryResult) Helper.jsonToObject(response, GetListHistoryResult.class);

                    if (result.error.equals("")) {
                        ArrayList<History> listHistories = new ArrayList<History>();
                        for (int i = 0; i < result.listHistories.length - 1; i++) {
                            listHistories.add(result.listHistories[i]);
                        }
                        mOnGetListHistoryResult.onSuccess(listHistories);
                    } else
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

    public static void getListHistoryCard(final String token, final String cardId, final String username,
                                          final OnGetListHistoryResult mOnGetListHistoryResult) {
        Thread t = new Thread() {
            @Override
            public void run() {
                super.run();

                String link = getGetListHistoryCard();

                httpclient = new DefaultHttpClient();
                httppost = new HttpPost(link);

                nameValuePairs = new ArrayList<NameValuePair>(2);

                nameValuePairs.add(new BasicNameValuePair("token", token));
                nameValuePairs.add(new BasicNameValuePair("card_id", cardId));
                nameValuePairs.add(new BasicNameValuePair("customer_id", username));

                try {
                    httppost.setEntity(new UrlEncodedFormEntity(nameValuePairs, "UTF-8"));
                    //ResponseHandler<String> responseHandler = new BasicResponseHandler();
                    ResponseHandler<String> responseHandler = Helper.getResponseHandler();
                    String response = null;

                    response = httpclient.execute(httppost, responseHandler);
                    GetListHistoryResult result = (GetListHistoryResult) Helper.jsonToObject(response, GetListHistoryResult.class);

                    if (result.error.equals("")) {
                        ArrayList<History> listHistories = new ArrayList<History>();
                        for (int i = 0; i < result.listHistories.length - 1; i++) {
                            listHistories.add(result.listHistories[i]);
                        }
                        mOnGetListHistoryResult.onSuccess(listHistories);
                    } else
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

    public interface OnGetListHistoryResult {
        public void onSuccess(ArrayList<History> listHistories);

        public void onError(String error);
    }

    public class GetListHistoryResult {
        public String error;
        public History[] listHistories;
    }
}
