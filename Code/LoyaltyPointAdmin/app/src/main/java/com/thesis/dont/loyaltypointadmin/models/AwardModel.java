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

    public static void createAward(final String token, final Award award,
                                   final OnCreateAwardResult mOnCreateAwardResult){

        Thread t = new Thread() {
            @Override
            public void run() {
                super.run();

                String json = Helper.objectToJson(award);

                String link = getCreateAward();

                httpclient = new DefaultHttpClient();
                httppost = new HttpPost(link);

                nameValuePairs = new ArrayList<NameValuePair>(2);

                nameValuePairs.add(new BasicNameValuePair("award", json));
                nameValuePairs.add(new BasicNameValuePair("token", token));

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

    public interface OnCreateAwardResult {
        public void onSuccess(CreateAwardResult result);
        public void onError(String error);
    }

    public class CreateAwardResult {
        public String error;
        public String bucketName;
        public String fileName;
    }
}
