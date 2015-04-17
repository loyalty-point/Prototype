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
public class TicketModel {

    static HttpPost httppost;
    static HttpResponse response;
    static HttpClient httpclient;
    static List<NameValuePair> nameValuePairs;

    static {
        System.loadLibrary("services");
    }

    public static native String getGetUserTicket();
    public static native String getDeleteUserTicket();

    public static void getUserTicket(final String token, final String userId, final String shopId, final OnGetUserTicket mOnGetUserTicket){
        Thread t = new Thread() {
            @Override
            public void run() {
                super.run();

                String link = getGetUserTicket();

                httpclient = new DefaultHttpClient();
                httppost = new HttpPost(link);

                nameValuePairs = new ArrayList<NameValuePair>(3);

                nameValuePairs.add(new BasicNameValuePair("token", token));
                nameValuePairs.add(new BasicNameValuePair("shopId", shopId));
                nameValuePairs.add(new BasicNameValuePair("userId", userId));

                try {
                    httppost.setEntity(new UrlEncodedFormEntity(nameValuePairs, "UTF-8"));
                    //ResponseHandler<String> responseHandler = new BasicResponseHandler();
                    ResponseHandler<String> responseHandler = Helper.getResponseHandler();
                    String response = null;

                    response = httpclient.execute(httppost, responseHandler);
                    GetUserTicketResult result = (GetUserTicketResult) Helper.jsonToObject(response, GetUserTicketResult.class);
                    if(result.error.equals("")) {
                        ArrayList<AwardHistory> listTickets = new ArrayList<AwardHistory>();
                        for(int i=0; i<result.listTickets.length-1; i++) {
                            listTickets.add(result.listTickets[i]);
                        }
                        mOnGetUserTicket.onSuccess(listTickets);
                    }
                    else
                        mOnGetUserTicket.onError(result.error);

                } catch (UnsupportedEncodingException e) {
                    mOnGetUserTicket.onError("UnsupportedEncodingException");
                    e.printStackTrace();
                } catch (ClientProtocolException e) {
                    mOnGetUserTicket.onError("ClientProtocolException");
                    e.printStackTrace();
                } catch (IOException e) {
                    mOnGetUserTicket.onError("IOException");
                    e.printStackTrace();
                }
            }
        };
        t.start();
    }

    public static void deleteUserTicket(final String token, final String ticketId, final OnDeleteUserTicket mOnDeleteUserTicket){
        Thread t = new Thread() {
            @Override
            public void run() {
                super.run();

                String link = getDeleteUserTicket();

                httpclient = new DefaultHttpClient();
                httppost = new HttpPost(link);

                nameValuePairs = new ArrayList<NameValuePair>(3);

                nameValuePairs.add(new BasicNameValuePair("token", token));
                nameValuePairs.add(new BasicNameValuePair("ticketId", ticketId));

                try {
                    httppost.setEntity(new UrlEncodedFormEntity(nameValuePairs, "UTF-8"));
                    //ResponseHandler<String> responseHandler = new BasicResponseHandler();
                    ResponseHandler<String> responseHandler = Helper.getResponseHandler();
                    String response = null;

                    response = httpclient.execute(httppost, responseHandler);
                    DeleteUserTicketResult result = (DeleteUserTicketResult) Helper.jsonToObject(response, DeleteUserTicketResult.class);
                    if(result.error.equals("")) {
                        mOnDeleteUserTicket.onSuccess();
                    }
                    else
                        mOnDeleteUserTicket.onError(result.error);

                } catch (UnsupportedEncodingException e) {
                    mOnDeleteUserTicket.onError("UnsupportedEncodingException");
                    e.printStackTrace();
                } catch (ClientProtocolException e) {
                    mOnDeleteUserTicket.onError("ClientProtocolException");
                    e.printStackTrace();
                } catch (IOException e) {
                    mOnDeleteUserTicket.onError("IOException");
                    e.printStackTrace();
                }
            }
        };
        t.start();
    }

    public interface OnGetUserTicket{
        public void onSuccess(ArrayList<AwardHistory> listTickets);
        public void onError(String error);
    }

    public class GetUserTicketResult {
        public String error;
        public AwardHistory[] listTickets;
    }

    public interface OnDeleteUserTicket{
        public void onSuccess();
        public void onError(String error);
    }

    public class DeleteUserTicketResult {
        public String error;
    }
}
