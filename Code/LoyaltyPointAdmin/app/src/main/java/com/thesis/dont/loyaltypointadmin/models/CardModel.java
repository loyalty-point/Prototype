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
 * Created by tinntt on 5/14/2015.
 */
public class CardModel {

    static HttpPost httppost;
    static HttpResponse response;
    static HttpClient httpclient;
    static List<NameValuePair> nameValuePairs;

    static {
        System.loadLibrary("services");
    }

    public static native String getGetListCards();
    public static native String getCreateCard();
    public static native String getGetListShop();
    public static native String getGetListEvents();
    public static native String getGetListAwards();

    public static void getListAwards(final String token, final String cardID, final OnGetListAwardsResult mOnGetListAwardsResult){

        Thread t = new Thread() {
            @Override
            public void run() {
                super.run();

                String link = getGetListAwards();

                httpclient = new DefaultHttpClient();
                httppost = new HttpPost(link);

                nameValuePairs = new ArrayList<NameValuePair>(2);

                nameValuePairs.add(new BasicNameValuePair("token", token));
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

    public static void getListEvents(final String cardID, final OnGetListEventResult mOnGetListEventResult){
        Thread t = new Thread() {
            @Override
            public void run() {
                super.run();

                String link = getGetListEvents();

                httpclient = new DefaultHttpClient();
                httppost = new HttpPost(link);

                nameValuePairs = new ArrayList<NameValuePair>(1);

                nameValuePairs.add(new BasicNameValuePair("token", Global.userToken));
                nameValuePairs.add(new BasicNameValuePair("cardID", cardID));

                try {
                    httppost.setEntity(new UrlEncodedFormEntity(nameValuePairs, "UTF-8"));
                    //ResponseHandler<String> responseHandler = new BasicResponseHandler();
                    ResponseHandler<String> responseHandler = Helper.getResponseHandler();
                    String response = null;

                    response = httpclient.execute(httppost, responseHandler);

                    GetListEvents result = (GetListEvents) Helper.jsonToObject(response, GetListEvents.class);
                    if(result.error.equals("")) {
                        ArrayList<Event> listEvents = new ArrayList<Event>();
                        for(int i=0; i<result.listEvents.length-1; i++) {
                            listEvents.add(result.listEvents[i]);
                        }
                        mOnGetListEventResult.onSuccess(listEvents);
                    }
                    else
                        mOnGetListEventResult.onError(result.error);

                } catch (UnsupportedEncodingException e) {
                    mOnGetListEventResult.onError("UnsupportedEncodingException");
                    e.printStackTrace();
                } catch (ClientProtocolException e) {
                    mOnGetListEventResult.onError("ClientProtocolException");
                    e.printStackTrace();
                } catch (IOException e) {
                    mOnGetListEventResult.onError("IOException");
                    e.printStackTrace();
                }
            }
        };
        t.start();
    }

    public static void createCard(final String userToken, Card card, final OnCreateCardResult mOnCreateCardResult){
        final String json = Helper.objectToJson(card);
        Thread t = new Thread() {
            @Override
            public void run() {
                super.run();

                String link = getCreateCard();

                httpclient = new DefaultHttpClient();
                httppost = new HttpPost(link);

                nameValuePairs = new ArrayList<NameValuePair>(2);

                nameValuePairs.add(new BasicNameValuePair("card", json));
                nameValuePairs.add(new BasicNameValuePair("token", userToken));

                try {
                    httppost.setEntity(new UrlEncodedFormEntity(nameValuePairs, "UTF-8"));
                    //ResponseHandler<String> responseHandler = new BasicResponseHandler();
                    ResponseHandler<String> responseHandler = Helper.getResponseHandler();
                    String response = null;

                    response = httpclient.execute(httppost, responseHandler);
                    CreateCardResult result = (CreateCardResult) Helper.jsonToObject(response, CreateCardResult.class);
                    if(result.error.equals(""))
                        mOnCreateCardResult.onSuccess(result);
                    else
                        mOnCreateCardResult.onError(result.error);

                } catch (UnsupportedEncodingException e) {
                    mOnCreateCardResult.onError("UnsupportedEncodingException");
                    e.printStackTrace();
                } catch (ClientProtocolException e) {
                    mOnCreateCardResult.onError("ClientProtocolException");
                    e.printStackTrace();
                } catch (IOException e) {
                    mOnCreateCardResult.onError("IOException");
                    e.printStackTrace();
                }
            }
        };
        t.start();
    }

    public static void getListCards(final String userToken, final OnGetListResult mOnGetListResult){
        Thread t = new Thread() {
            @Override
            public void run() {
                super.run();

                String link = getGetListCards();

                httpclient = new DefaultHttpClient();
                httppost = new HttpPost(link);

                nameValuePairs = new ArrayList<NameValuePair>(1);

                nameValuePairs.add(new BasicNameValuePair("token", userToken));

                try {
                    httppost.setEntity(new UrlEncodedFormEntity(nameValuePairs, "UTF-8"));
                    //ResponseHandler<String> responseHandler = new BasicResponseHandler();
                    ResponseHandler<String> responseHandler = Helper.getResponseHandler();
                    String response = null;

                    response = httpclient.execute(httppost, responseHandler);

                    GetListCards result = (GetListCards) Helper.jsonToObject(response, GetListCards.class);
                    if(result.error.equals("")) {
                        ArrayList<Card> listCards = new ArrayList<Card>();
                        for(int i=0; i<result.listCards.length-1; i++) {
                            listCards.add(result.listCards[i]);
                        }
                        mOnGetListResult.onSuccess(listCards);
                    }
                    else
                        mOnGetListResult.onError(result.error);

                } catch (UnsupportedEncodingException e) {
                    mOnGetListResult.onError("UnsupportedEncodingException");
                    e.printStackTrace();
                } catch (ClientProtocolException e) {
                    mOnGetListResult.onError("ClientProtocolException");
                    e.printStackTrace();
                } catch (IOException e) {
                    mOnGetListResult.onError("IOException");
                    e.printStackTrace();
                }
            }
        };
        t.start();
    }

    public static void getListShop(final String token, final String cardId, final OnSelectListShopResult mOnSelectListShopResult){
        Thread t = new Thread() {
            @Override
            public void run() {
                super.run();

                String link = getGetListShop();

                httpclient = new DefaultHttpClient();
                httppost = new HttpPost(link);

                nameValuePairs = new ArrayList<NameValuePair>(1);

                nameValuePairs.add(new BasicNameValuePair("token", token));
                nameValuePairs.add(new BasicNameValuePair("card_id", cardId));

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
                        mOnSelectListShopResult.onSuccess(listShops);
                    }
                    else
                        mOnSelectListShopResult.onError(result.error);

                } catch (UnsupportedEncodingException e) {
                    mOnSelectListShopResult.onError("UnsupportedEncodingException");
                    e.printStackTrace();
                } catch (ClientProtocolException e) {
                    mOnSelectListShopResult.onError("ClientProtocolException");
                    e.printStackTrace();
                } catch (IOException e) {
                    mOnSelectListShopResult.onError("IOException");
                    e.printStackTrace();
                }
            }
        };
        t.start();
    }

    public class CreateCardResult {
        public String error;
        public String cardId;
        public String bucketName;
        public String fileName;
    }

    public interface OnCreateCardResult{
        public void onSuccess(CreateCardResult result);
        public void onError(String error);
    }

    public class GetListCards {
        public String error;
        public Card[] listCards;
    }

    public interface OnGetListResult{
        public void onSuccess(ArrayList<Card> listCards);

        public void onError(String error);
    }

    public interface OnSelectListShopResult{
        public void onSuccess(ArrayList<Shop> listShops);
        public void onError(String error);
    }

    public class GetListShops {
        public String error;
        public Shop[] listShops;
    }

    public interface OnGetListEventResult{
        public void onSuccess(ArrayList<Event> listEvents);

        public void onError(String error);
    }

    public class GetListEvents {
        public String error;
        public Event[] listEvents;
    }

    public class GetListAwards {
        public String error;
        public Award[] listAwards;
    }

    public interface OnGetListAwardsResult{
        public void onSuccess(ArrayList<Award> listAwards);
        public void onError(String error);
    }
}
