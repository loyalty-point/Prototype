package com.thesis.dont.loyaltypointuser.models;

import android.util.Log;

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
    public static native String getCustomerGetListEvents();
    public static native String getEditEvent();

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
                    CreateEventResult createEventResult = (CreateEventResult)Helper.jsonToObject(response, CreateEventResult.class);

                    if(createEventResult.error.equals(""))
                        onAddEventResult.onSuccess(createEventResult);
                    else
                        onAddEventResult.onError(createEventResult.error);
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

    public static void getListEvents(final String userToken, final String shopID, final OnGetListResult onGetListResult){
        Thread t = new Thread() {
            @Override
            public void run() {
                super.run();

                String link = getCustomerGetListEvents();

                httpclient = new DefaultHttpClient();
                httppost = new HttpPost(link);

                nameValuePairs = new ArrayList<NameValuePair>(1);

                nameValuePairs.add(new BasicNameValuePair("token", userToken));
                nameValuePairs.add(new BasicNameValuePair("shopID", shopID));

                try {
                    httppost.setEntity(new UrlEncodedFormEntity(nameValuePairs, "UTF-8"));
                    //ResponseHandler<String> responseHandler = new BasicResponseHandler();
                    ResponseHandler<String> responseHandler = Helper.getResponseHandler();
                    String response = null;

                    Log.e("Get List Events", "Befor run execute");
                    response = httpclient.execute(httppost, responseHandler);

                    GetListEvents result = (GetListEvents) Helper.jsonToObject(response, GetListEvents.class);
                    if(result.error.equals("")) {
                        ArrayList<Event> listEvents = new ArrayList<Event>();
                        for(int i=0; i<result.listEvents.length-1; i++) {
                            listEvents.add(result.listEvents[i]);
                        }
                        onGetListResult.onSuccess(listEvents);
                    }
                    else
                        onGetListResult.onError(result.error);

                } catch (UnsupportedEncodingException e) {
                    onGetListResult.onError("UnsupportedEncodingException");
                    e.printStackTrace();
                } catch (ClientProtocolException e) {
                    onGetListResult.onError("ClientProtocolException");
                    e.printStackTrace();
                } catch (IOException e) {
                    onGetListResult.onError("IOException");
                    e.printStackTrace();
                }
            }
        };
        t.start();
    }

    public static void editEvent(final String token, final String shopID, final Event event,
                                 final OnEditEventResult mOnEditEventResult){

        Thread t = new Thread() {
            @Override
            public void run() {
                super.run();

                String json = Helper.objectToJson(event);

                String link = getEditEvent();

                httpclient = new DefaultHttpClient();
                httppost = new HttpPost(link);

                nameValuePairs = new ArrayList<NameValuePair>(2);

                nameValuePairs.add(new BasicNameValuePair("event", json));
                nameValuePairs.add(new BasicNameValuePair("token", token));
                nameValuePairs.add(new BasicNameValuePair("shop_id", shopID));

                try {
                    httppost.setEntity(new UrlEncodedFormEntity(nameValuePairs, "UTF-8"));
                    //ResponseHandler<String> responseHandler = new BasicResponseHandler();
                    ResponseHandler<String> responseHandler = Helper.getResponseHandler();
                    String response = null;

                    response = httpclient.execute(httppost, responseHandler);
                    EditEventResult result = (EditEventResult) Helper.jsonToObject(response, EditEventResult.class);
                    if(result.error.equals(""))
                        mOnEditEventResult.onSuccess(result);
                    else
                        mOnEditEventResult.onError(result.error);

                } catch (UnsupportedEncodingException e) {
                    mOnEditEventResult.onError("UnsupportedEncodingException");
                    e.printStackTrace();
                } catch (ClientProtocolException e) {
                    mOnEditEventResult.onError("ClientProtocolException");
                    e.printStackTrace();
                } catch (IOException e) {
                    mOnEditEventResult.onError("IOException");
                    e.printStackTrace();
                }
            }
        };
        t.start();
    }

    public interface OnAddEventResult{
        public void onSuccess(CreateEventResult createEventResult);

        public void onError(String error);
    }

    public interface OnGetListResult{
        public void onSuccess(ArrayList<Event> listEvents);

        public void onError(String error);
    }

    public interface OnEditEventResult{
        public void onSuccess(EditEventResult result);
        public void onError(String error);
    }

    public class CreateEventResult {
        public String error;
        public String bucketName;
        public String fileName;
    }

    public class GetListEvents {
        public String error;
        public Event[] listEvents;
    }

    public class EditEventResult{
        public String error;
        public String bucketName;
        public String fileName;
    }
}
