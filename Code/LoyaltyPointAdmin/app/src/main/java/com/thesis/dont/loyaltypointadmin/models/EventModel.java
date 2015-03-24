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
 * Created by tinntt on 3/24/2015.
 */
public class EventModel {

    static HttpPost httppost;
    static HttpResponse response;
    static HttpClient httpclient;
    static List<NameValuePair> nameValuePairs;

    static {
        System.loadLibrary("services");
    }
    public static native String getAddEvent();

    public static void addEvent(Event event, final String shopId, final OnAddEventResult onAddEventResult){
        final String json = Helper.objectToJson(event);
        Thread t = new Thread() {
            @Override
            public void run() {
                super.run();

                String link = getAddEvent();

                httpclient = new DefaultHttpClient();
                httppost = new HttpPost(link);

                nameValuePairs = new ArrayList<NameValuePair>(3);

                nameValuePairs.add(new BasicNameValuePair("shop_id", shopId));
                nameValuePairs.add(new BasicNameValuePair("event", json));
                nameValuePairs.add(new BasicNameValuePair("token", Global.userToken));

                try {
                    httppost.setEntity(new UrlEncodedFormEntity(nameValuePairs, "UTF-8"));
                    ResponseHandler<String> responseHandler = new BasicResponseHandler();
                    /*ResponseHandler<String> responseHandler = Helper.getResponseHandler();*/
                    String response = null;

                    response = httpclient.execute(httppost, responseHandler);
                    if(response.equals("true"))
                        onAddEventResult.onSuccess();
                    else
                        onAddEventResult.onError(null);
                } catch (UnsupportedEncodingException e) {
                    onAddEventResult.onError(e.toString());
                    e.printStackTrace();
                } catch (ClientProtocolException e) {
                    onAddEventResult.onError(e.toString());
                    e.printStackTrace();
                } catch (IOException e) {
                    onAddEventResult.onError(e.toString());
                    e.printStackTrace();
                }
            }
        };
        t.start();
    }

    public interface OnAddEventResult{
        public void onSuccess();

        public void onError(String error);
    }
}
