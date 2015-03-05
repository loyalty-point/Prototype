package com.thesis.dont.loyaltypointadmin.models;

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
 * Created by tinntt on 3/5/2015.
 */
public class ShopModel {
    static HttpPost httppost;
    static HttpResponse response;
    static HttpClient httpclient;
    static List<NameValuePair> nameValuePairs;

    static OnCreateShopResult mOnCreateShopResult;

    static {
        System.loadLibrary("services");
    }
    public static native String getCreateShop();

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
                    httppost.setEntity(new UrlEncodedFormEntity(nameValuePairs));
                    ResponseHandler<String> responseHandler = new BasicResponseHandler();
                    String response = null;

                    response = httpclient.execute(httppost, responseHandler);
                    if(response.equals("true"))
                        mOnCreateShopResult.onSuccess();
                    else
                        mOnCreateShopResult.onError(response);
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

    public interface OnCreateShopResult{
        public void onSuccess();
        public void onError(String error);
    }

    public static OnCreateShopResult getOnCreateShopResult() { return mOnCreateShopResult; }
    public static void setOnCreateShopResult(OnCreateShopResult mOnCreateShopResult){
        ShopModel.mOnCreateShopResult = mOnCreateShopResult;
    }
}
