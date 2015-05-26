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
    public static native String getCustomerGetListAwards();
    public static native String getGetListAwards();
    public static native String getEditQuantityAward();
    public static native String getBuyAward();


    public static void editQuantityAward(final String token, final Award award,
                                         final OnEditQuantityAwardResult mOnEditQuantityAwardResult) {

        Thread t = new Thread() {
            @Override
            public void run() {
                super.run();

                String json = Helper.objectToJson(award);

                String link = getEditQuantityAward();

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
                    EditQuantityAwardResult result = (EditQuantityAwardResult) Helper.jsonToObject(response, EditQuantityAwardResult.class);
                    if (result.error.equals(""))
                        mOnEditQuantityAwardResult.onSuccess(result.quantity);
                    else
                        mOnEditQuantityAwardResult.onError(result.error);

                } catch (UnsupportedEncodingException e) {
                    mOnEditQuantityAwardResult.onError("UnsupportedEncodingException");
                    e.printStackTrace();
                } catch (ClientProtocolException e) {
                    mOnEditQuantityAwardResult.onError("ClientProtocolException");
                    e.printStackTrace();
                } catch (IOException e) {
                    mOnEditQuantityAwardResult.onError("IOException");
                    e.printStackTrace();
                }
            }
        };
        t.start();
    }

    public static void getListAwards(final String token, final String shopID, final String cardID, final OnGetListAwardsResult mOnGetListAwardsResult) {

        Thread t = new Thread() {
            @Override
            public void run() {
                super.run();

                String link = getCustomerGetListAwards();

                httpclient = new DefaultHttpClient();
                httppost = new HttpPost(link);

                nameValuePairs = new ArrayList<NameValuePair>(1);

                nameValuePairs.add(new BasicNameValuePair("token", token));
                nameValuePairs.add(new BasicNameValuePair("shopID", shopID));
                nameValuePairs.add(new BasicNameValuePair("cardID", cardID));

                try {
                    httppost.setEntity(new UrlEncodedFormEntity(nameValuePairs, "UTF-8"));
                    //ResponseHandler<String> responseHandler = new BasicResponseHandler();
                    ResponseHandler<String> responseHandler = Helper.getResponseHandler();
                    String response = null;

                    response = httpclient.execute(httppost, responseHandler);

                    GetListAwards result = (GetListAwards) Helper.jsonToObject(response, GetListAwards.class);
                    if (result.error.equals("")) {
                        // chuyển từ result.listAwards (dạng json) sang ArrayList<Award>
                        /*String[] datas = result.listAwards.split("&"); //slit data to json struture
                        ArrayList<Award> listAwards = new ArrayList<Award>();

                        for (int i = 0; i < datas.length; i++) {
                            Award award = (Award) Helper.jsonToObject(datas[i], Award.class);
                            listAwards.add(award); //add award object to array
                        }*/
                        ArrayList<Award> listAwards = new ArrayList<Award>();
                        for (int i = 0; i < result.listAwards.length - 1; i++) {
                            listAwards.add(result.listAwards[i]);
                        }

                        mOnGetListAwardsResult.onSuccess(listAwards);
                    } else
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

    public static void buyAward(final String token, final String time,
                                final String shopId,
                                final String awardID,
                                final int quantity,
                                final OnBuyAwardResult mOnBuyAwardResult){

        Thread t = new Thread() {
            @Override
            public void run() {
                super.run();

                String link = getBuyAward();

                httpclient = new DefaultHttpClient();
                httppost = new HttpPost(link);

                nameValuePairs = new ArrayList<NameValuePair>(5);

                nameValuePairs.add(new BasicNameValuePair("token", token));
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
                        mOnBuyAwardResult.onSuccess();
                    }
                    else
                        mOnBuyAwardResult.onError(result.error);

                } catch (UnsupportedEncodingException e) {
                    mOnBuyAwardResult.onError("UnsupportedEncodingException");
                    e.printStackTrace();
                } catch (ClientProtocolException e) {
                    mOnBuyAwardResult.onError("ClientProtocolException");
                    e.printStackTrace();
                } catch (IOException e) {
                    mOnBuyAwardResult.onError("IOException");
                    e.printStackTrace();
                }
            }
        };
        t.start();
    }



    public interface OnCreateAwardResult {
        public void onSuccess(CreateAwardResult createAwardResult);

        public void onError(String error);
    }

    public interface OnEditQuantityAwardResult {
        public void onSuccess(int quantity);

        public void onError(String error);
    }


    public interface OnGetListAwardsResult {
        public void onSuccess(ArrayList<Award> listAwards);

        public void onError(String error);
    }

    public class CreateAwardResult {
        public String error;
        public String bucketName;
        public String fileName;
    }

    public class EditQuantityAwardResult {
        public String error;
        public int quantity;
    }

    public class GetListAwards {
        public String error;
        public Award[] listAwards;
    }

    public interface OnBuyAwardResult{
        public void onSuccess();
        public void onError(String error);
    }

    public class BuyAwardResult{
        public String error;
    }
}
