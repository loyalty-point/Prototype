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

    public static void editAward(final String token, final Award award,
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

    public static void getListAwards(final String token, final String shopID, final OnGetListAwardsResult mOnGetListAwardsResult){

        Thread t = new Thread() {
            @Override
            public void run() {
                super.run();

                String link = getGetListAwards();

                httpclient = new DefaultHttpClient();
                httppost = new HttpPost(link);

                nameValuePairs = new ArrayList<NameValuePair>(1);

                nameValuePairs.add(new BasicNameValuePair("token", token));
                nameValuePairs.add(new BasicNameValuePair("shopID", shopID));

                try {
                    httppost.setEntity(new UrlEncodedFormEntity(nameValuePairs, "UTF-8"));
                    //ResponseHandler<String> responseHandler = new BasicResponseHandler();
                    ResponseHandler<String> responseHandler = Helper.getResponseHandler();
                    String response = null;

                    response = httpclient.execute(httppost, responseHandler);

                    GetListAwards result = (GetListAwards) Helper.jsonToObject(response, GetListAwards.class);
                    if(result.error.equals("")) {
                        // chuyển từ result.listAwards (dạng json) sang ArrayList<Award>
                        /*String[] datas = result.listAwards.split("&"); //slit data to json struture
                        ArrayList<Award> listAwards = new ArrayList<Award>();

                        for (int i = 0; i < datas.length; i++) {
                            Award award = (Award) Helper.jsonToObject(datas[i], Award.class);
                            listAwards.add(award); //add award object to array
                        }*/
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
}