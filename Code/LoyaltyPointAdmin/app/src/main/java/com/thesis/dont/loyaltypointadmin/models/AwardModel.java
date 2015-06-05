package com.thesis.dont.loyaltypointadmin.models;

import com.thesis.dont.loyaltypointadmin.controllers.Helper;

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
 * Created by 11120_000 on 25/03/15.
 */
public class AwardModel {

    static HttpPost httppost;
    static HttpClient httpclient;
    static List<NameValuePair> nameValuePairs;

    static {
        System.loadLibrary("services");
    }

    public static native String getCreateAward();
    public static native String getEditAward();
    public static native String getGetListAwards();
    public static native String getSellAward();

    public static void createAward(final String token,final String shopID, final String cardID, final Award award,
                                   final OnCreateAwardResult mOnCreateAwardResult){

        Thread t = new Thread() {
            @Override
            public void run() {
                super.run();

                String json = Helper.objectToJson(award);

                String link = getCreateAward();

                httpclient = new DefaultHttpClient();
                httppost = new HttpPost(link);

                nameValuePairs = new ArrayList<NameValuePair>(4);

                nameValuePairs.add(new BasicNameValuePair("award", json));
                nameValuePairs.add(new BasicNameValuePair("token", token));
                nameValuePairs.add(new BasicNameValuePair("shop_id", shopID));
                nameValuePairs.add(new BasicNameValuePair("card_id", cardID));

                try {
                    httppost.setEntity(new UrlEncodedFormEntity(nameValuePairs, "UTF-8"));
                    //ResponseHandler<String> responseHandler = new BasicResponseHandler();
                    ResponseHandler<String> responseHandler = Helper.getResponseHandler();
                    String response = null;

                    response = httpclient.execute(httppost, responseHandler);
                    CreateAwardResult result = (CreateAwardResult) Helper.jsonToObject(response, CreateAwardResult.class);
                    if(result.error == "")
                        mOnCreateAwardResult.onSuccess(result);
                    else
                        mOnCreateAwardResult.onError(result.error);

                } catch (UnsupportedEncodingException e) {
                    mOnCreateAwardResult.onError("UnsupportedEncodingException");
                    e.printStackTrace();
                } catch (ClientProtocolException e) {
                    mOnCreateAwardResult.onError("ClientProtocolException");
                    e.printStackTrace();
                } catch (IOException e) {
                    mOnCreateAwardResult.onError("IOException");
                    e.printStackTrace();
                }
            }
        };
        t.start();
    }

    public static void editAward(final String token, final String shopId, final String cardId, final Award award,
                                   final OnEditAwardResult mOnEditAwardResult){

        Thread t = new Thread() {
            @Override
            public void run() {
                super.run();

                String json = Helper.objectToJson(award);

                String link = getEditAward();

                httpclient = new DefaultHttpClient();
                httppost = new HttpPost(link);

                nameValuePairs = new ArrayList<NameValuePair>(2);

                nameValuePairs.add(new BasicNameValuePair("award", json));
                nameValuePairs.add(new BasicNameValuePair("token", token));
                nameValuePairs.add(new BasicNameValuePair("shop_id", shopId));
                nameValuePairs.add(new BasicNameValuePair("card_id", cardId));

                try {
                    httppost.setEntity(new UrlEncodedFormEntity(nameValuePairs, "UTF-8"));
                    //ResponseHandler<String> responseHandler = new BasicResponseHandler();
                    ResponseHandler<String> responseHandler = Helper.getResponseHandler();
                    String response = null;

                    response = httpclient.execute(httppost, responseHandler);
                    EditAwardResult result = (EditAwardResult) Helper.jsonToObject(response, EditAwardResult.class);
                    if(result.error == "")
                        mOnEditAwardResult.onSuccess(result);
                    else
                        mOnEditAwardResult.onError(result.error);

                } catch (UnsupportedEncodingException e) {
                    mOnEditAwardResult.onError("UnsupportedEncodingException");
                    e.printStackTrace();
                } catch (ClientProtocolException e) {
                    mOnEditAwardResult.onError("ClientProtocolException");
                    e.printStackTrace();
                } catch (IOException e) {
                    mOnEditAwardResult.onError("IOException");
                    e.printStackTrace();
                }
            }
        };
        t.start();
    }

    public static void sellAward(final String token, final String customer_username, final String time,
                                final String shopId,
                                final String awardID,
                                final int quantity,
                                final OnSellAwardResult mOnSellAwardResult){

        Thread t = new Thread() {
            @Override
            public void run() {
                super.run();

                String link = getSellAward();

                httpclient = new DefaultHttpClient();
                httppost = new HttpPost(link);

                nameValuePairs = new ArrayList<NameValuePair>(6);

                nameValuePairs.add(new BasicNameValuePair("token", token));
                nameValuePairs.add(new BasicNameValuePair("customer_username", customer_username));
                nameValuePairs.add(new BasicNameValuePair("time", time));
                nameValuePairs.add(new BasicNameValuePair("shop_id", shopId));
                nameValuePairs.add(new BasicNameValuePair("award_id", awardID));
                nameValuePairs.add(new BasicNameValuePair("quantity", String.valueOf(quantity)));

                try {
                    httppost.setEntity(new UrlEncodedFormEntity(nameValuePairs, "UTF-8"));
                    //ResponseHandler<String> responseHandler = new BasicResponseHandler();
                    ResponseHandler<String> responseHandler = Helper.getResponseHandler();
                    String response = null;

                    response = httpclient.execute(httppost, responseHandler);
                    BuyAwardResult result = (BuyAwardResult) Helper.jsonToObject(response, BuyAwardResult.class);

                    if(result.error.equals("")) {
                        mOnSellAwardResult.onSuccess();
                    }
                    else
                        mOnSellAwardResult.onError(result.error);

                } catch (UnsupportedEncodingException e) {
                    mOnSellAwardResult.onError("UnsupportedEncodingException");
                    e.printStackTrace();
                } catch (ClientProtocolException e) {
                    mOnSellAwardResult.onError("ClientProtocolException");
                    e.printStackTrace();
                } catch (IOException e) {
                    mOnSellAwardResult.onError("IOException");
                    e.printStackTrace();
                }
            }
        };
        t.start();
    }

    public static void getListAwards(final String token, final String shopID, final String cardID, final OnGetListAwardsResult mOnGetListAwardsResult){

        Thread t = new Thread() {
            @Override
            public void run() {
                super.run();

                String link = getGetListAwards();

                httpclient = new DefaultHttpClient();
                httppost = new HttpPost(link);

                nameValuePairs = new ArrayList<NameValuePair>(2);

                nameValuePairs.add(new BasicNameValuePair("token", token));
                nameValuePairs.add(new BasicNameValuePair("shopID", shopID));
                nameValuePairs.add(new BasicNameValuePair("cardID", cardID));

                try {
                    httppost.setEntity(new UrlEncodedFormEntity(nameValuePairs, "UTF-8"));
                    //ResponseHandler<String> responseHandler d= new BasicResponseHandler();
                    ResponseHandler<String> responseHandler = Helper.getResponseHandler();
                    String response = null;

                    response = httpclient.execute(httppost, responseHandler);

                    GetListAwards result = (GetListAwards) Helper.jsonToObject(response, GetListAwards.class);
                    if(result.error.equals("")) {
                        // chuyển từ result.listAwards (dạng json) sang ArrayList<Award>
                        ArrayList<Award> listAwards = new ArrayList<Award>();
                        for(int i=0; i<result.listAwards.length-1; i++) {
                            listAwards.add(result.listAwards[i]);
                        }

                        mOnGetListAwardsResult.onSuccess(listAwards);
                    }
                    else
                        mOnGetListAwardsResult.onError(result.error);

                } catch (UnsupportedEncodingException e) {
                    mOnGetListAwardsResult.onError("UnsupportedEncodingException");
                    e.printStackTrace();
                } catch (ClientProtocolException e) {
                    mOnGetListAwardsResult.onError("ClientProtocolException");
                    e.printStackTrace();
                } catch (IOException e) {
                    mOnGetListAwardsResult.onError("IOException");
                    e.printStackTrace();
                }
            }
        };
        t.start();
    }

    public interface OnCreateAwardResult {
        public void onSuccess(CreateAwardResult result);
        public void onError(String error);
    }

    public interface OnEditAwardResult {
        public void onSuccess(EditAwardResult result);
        public void onError(String error);
    }


    public interface OnGetListAwardsResult{
        public void onSuccess(ArrayList<Award> listAwards);
        public void onError(String error);
    }

    public class CreateAwardResult {
        public String error;
        public String bucketName;
        public String fileName;
    }

    public class EditAwardResult {
        public String error;
        public String bucketName;
        public String fileName;
    }

    public class GetListAwards {
        public String error;
        public Award[] listAwards;
    }

    public interface OnSellAwardResult{
        public void onSuccess();
        public void onError(String error);
    }

    public class BuyAwardResult{
        public String error;
    }
}
