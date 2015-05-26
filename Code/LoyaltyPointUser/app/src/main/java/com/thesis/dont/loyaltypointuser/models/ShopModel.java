package com.thesis.dont.loyaltypointuser.models;

import com.thesis.dont.loyaltypointuser.controllers.Helper;

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
 * Created by tinntt on 3/5/2015.
 */
public class ShopModel {
    static HttpPost httppost;
    static HttpClient httpclient;
    static List<NameValuePair> nameValuePairs;

    static OnCreateShopResult mOnCreateShopResult;

    static {
        System.loadLibrary("services");
    }
    public static native String getCreateShop();
    public static native String getCustomerGetShopInfo();
    public static native String getGetUnfollowedShop();
    public static native String getFollowShop();


    public static void createShop(Shop shop, String token){
        final String token_string = token;
        final String json = Helper.objectToJson(shop);
        Thread t = new Thread() {
            @Override
            public void run() {
                super.run();

                String link = getCreateShop();

                httpclient = new DefaultHttpClient();
                httppost = new HttpPost(link);

                nameValuePairs = new ArrayList<NameValuePair>(2);

                nameValuePairs.add(new BasicNameValuePair("shop", json));
                nameValuePairs.add(new BasicNameValuePair("token", token_string));

                try {
                    httppost.setEntity(new UrlEncodedFormEntity(nameValuePairs, "UTF-8"));
                    //ResponseHandler<String> responseHandler = new BasicResponseHandler();
                    ResponseHandler<String> responseHandler = Helper.getResponseHandler();
                    String response = null;

                    response = httpclient.execute(httppost, responseHandler);
                    CreateShopResult result = (CreateShopResult) Helper.jsonToObject(response, CreateShopResult.class);
                    if(result.error == "")
                        mOnCreateShopResult.onSuccess(result);
                    else
                        mOnCreateShopResult.onError(result.error);

                } catch (UnsupportedEncodingException e) {
                    mOnCreateShopResult.onError("UnsupportedEncodingException");
                    e.printStackTrace();
                } catch (ClientProtocolException e) {
                    mOnCreateShopResult.onError("ClientProtocolException");
                    e.printStackTrace();
                } catch (IOException e) {
                    mOnCreateShopResult.onError("IOException");
                    e.printStackTrace();
                }
            }
        };
        t.start();
    }

    public class CreateShopResult {
        public String error;
        public String shopID;
        public String bucketName;
        public String fileName;
    }

    public static void getShopInfo(final String token, final String shopID, final OnGetShopInfoResult mOnGetShopInfoResult){
        /*final String token_string = token;
        final String json = Helper.objectToJson(shop);*/
        Thread t = new Thread() {
            @Override
            public void run() {
                super.run();

                String link = getCustomerGetShopInfo();

                httpclient = new DefaultHttpClient();
                httppost = new HttpPost(link);

                nameValuePairs = new ArrayList<NameValuePair>(2);

                nameValuePairs.add(new BasicNameValuePair("shop_id", shopID));
                nameValuePairs.add(new BasicNameValuePair("token", token));

                try {
                    httppost.setEntity(new UrlEncodedFormEntity(nameValuePairs, "UTF-8"));
                    //ResponseHandler<String> responseHandler = new BasicResponseHandler();
                    ResponseHandler<String> responseHandler = Helper.getResponseHandler();
                    String response = null;

                    response = httpclient.execute(httppost, responseHandler);
                    GetShopInfo getShopInfo = (GetShopInfo)Helper.jsonToObject(response, GetShopInfo.class);
                    if(getShopInfo.error.equals(""))
                        mOnGetShopInfoResult.onSuccess(getShopInfo.shop[0]);
                    else {
                        mOnGetShopInfoResult.onError(getShopInfo.error);
                    }

                } catch (UnsupportedEncodingException e) {
                    mOnGetShopInfoResult.onError("UnsupportedEncodingException");
                    e.printStackTrace();
                } catch (ClientProtocolException e) {
                    mOnGetShopInfoResult.onError("ClientProtocolException");
                    e.printStackTrace();
                } catch (IOException e) {
                    mOnGetShopInfoResult.onError("IOException");
                    e.printStackTrace();
                }
            }
        };
        t.start();
    }

    public static void getUnfollowedShop(final String token, final OnSelectAllShopResult mOnSelectAllShopResult){
        Thread t = new Thread() {
            @Override
            public void run() {
                super.run();

                String link = getGetUnfollowedShop();

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
                    GetListShops result = (GetListShops) Helper.jsonToObject(response, GetListShops.class);

                    if(result.error.equals("")){

                        ArrayList<Shop> listShops = new ArrayList<Shop>();
                        for(int i=0; i<result.listShops.length-1; i++) {
                            listShops.add(result.listShops[i]);
                        }
                        mOnSelectAllShopResult.onSuccess(listShops);
                    }
                    else
                        mOnSelectAllShopResult.onError(result.error);

                } catch (UnsupportedEncodingException e) {
                    mOnSelectAllShopResult.onError("UnsupportedEncodingException");
                    e.printStackTrace();
                } catch (ClientProtocolException e) {
                    mOnSelectAllShopResult.onError("ClientProtocolException");
                    e.printStackTrace();
                } catch (IOException e) {
                    mOnSelectAllShopResult.onError("IOException");
                    e.printStackTrace();
                }
            }
        };
        t.start();
    }

    public static void followShop(String token, final String cardId, final int point,  final OnFollowShopResult onFollowShopResult){
        final String token_string = token;
        Thread t = new Thread() {
            @Override
            public void run() {
                super.run();

                String link = getFollowShop();

                httpclient = new DefaultHttpClient();
                httppost = new HttpPost(link);

                nameValuePairs = new ArrayList<NameValuePair>(3);

                nameValuePairs.add(new BasicNameValuePair("card_id", cardId));
                nameValuePairs.add(new BasicNameValuePair("token", token_string));
                nameValuePairs.add(new BasicNameValuePair("point", String.valueOf(point)));

                try {
                    httppost.setEntity(new UrlEncodedFormEntity(nameValuePairs, "UTF-8"));
                    //ResponseHandler<String> responseHandler = new BasicResponseHandler();
                    ResponseHandler<String> responseHandler = Helper.getResponseHandler();
                    String response = null;

                    response = httpclient.execute(httppost, responseHandler);
                    FollowShop result = (FollowShop) Helper.jsonToObject(response, FollowShop.class);
                    if(result.error.equals(""))
                        onFollowShopResult.onSuccess();
                    else
                        onFollowShopResult.onError(result.error);

                } catch (UnsupportedEncodingException e) {
                    onFollowShopResult.onError("UnsupportedEncodingException");
                    e.printStackTrace();
                } catch (ClientProtocolException e) {
                    onFollowShopResult.onError("ClientProtocolException");
                    e.printStackTrace();
                } catch (IOException e) {
                    onFollowShopResult.onError("IOException");
                    e.printStackTrace();
                }
            }
        };
        t.start();
    }

    public interface OnSelectAllShopResult{
        public void onSuccess(ArrayList<Shop> listShops);
        public void onError(String error);
    }

    public interface OnCreateShopResult{
        public void onSuccess(CreateShopResult result);
        public void onError(String error);
    }

    public interface OnGetShopInfoResult{
        public void onSuccess(Shop shop);
        public void onError(String error);
    }

    public interface OnFollowShopResult{
        public void onSuccess();
        public void onError(String error);
    }

    public class GetShopInfo{
        public String error;
        public Shop[] shop;
    }

    public class FollowShop{
        public String error;
        public String data;
    }
    public class GetListShops {
        public String error;
        public Shop[] listShops;
    }
}
