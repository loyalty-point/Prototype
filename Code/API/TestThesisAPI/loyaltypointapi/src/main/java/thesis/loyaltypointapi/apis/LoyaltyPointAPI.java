package thesis.loyaltypointapi.apis;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.net.Uri;

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
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import thesis.loyaltypointapi.controllers.GetTokenActivity;
import thesis.loyaltypointapi.controllers.Helper;
import thesis.loyaltypointapi.controllers.Preconditions;
import thesis.loyaltypointapi.models.AchievedEvent;
import thesis.loyaltypointapi.models.Award;
import thesis.loyaltypointapi.models.CalculatePointResult;
import thesis.loyaltypointapi.models.Card;
import thesis.loyaltypointapi.models.Customer;
import thesis.loyaltypointapi.models.Event;
import thesis.loyaltypointapi.models.GetListAwardsResult;
import thesis.loyaltypointapi.models.GetListCardsResult;
import thesis.loyaltypointapi.models.GetListCustomersResult;
import thesis.loyaltypointapi.models.GetListEventsResult;
import thesis.loyaltypointapi.models.GetListShopsResult;
import thesis.loyaltypointapi.models.GlobalParams;
import thesis.loyaltypointapi.models.Product;
import thesis.loyaltypointapi.models.Shop;
import thesis.loyaltypointapi.models.UpdatePointResult;

/**
 * Created by 11120_000 on 22/05/15.
 */
public class LoyaltyPointAPI {

    //Context mContext;

    public static void calculatePoint(final Context context, final String shopID, final String cardQRCode, final List<Product> listProducts, final float totalMoney, final OnCalculatePointResult mOnCalculatePointResult) {

        Thread t = new Thread(new Runnable() {
            @Override
            public void run() {
                // Kiểm tra tham số
                if (context == null) {
                    mOnCalculatePointResult.onError("context is null");
                    return;
                }
                //mContext = context;

                if (mOnCalculatePointResult == null)
                    return;

                if (!Preconditions.checkNotNull(shopID)) {
                    mOnCalculatePointResult.onError("shopID is null or equal \"\"");
                    return;
                }

                if (!Preconditions.checkNotNull(cardQRCode)) {
                    mOnCalculatePointResult.onError("cardQRCode is null or equal \"\"");
                    return;
                }

                if (!Preconditions.checkPositive(totalMoney)) {
                    mOnCalculatePointResult.onError("totalMoney is negative");
                    return;
                }

                /*Preconditions<Product> preconditions = new Preconditions<Product>();
                if(!preconditions.checkNotNull(listProducts)) {
                    mOnCalculatePointResult.onError("listProducts is null or has no item");
                    return;
                }*/

                // Lấy token
                getToken(context, new OnGetTokenResult() {
                    @Override
                    public void onSuccess(final String token) {
                        // Đến đây thì đã có token
                        // Gọi API để tính điểm tích lũy

                        String cardID = cardQRCode.substring(0, 13);
                        doCalculatePoint(token, shopID, cardID, listProducts, totalMoney, mOnCalculatePointResult);
                        GlobalParams.removeCallbackItem(this);
                    }

                    @Override
                    public void onError(String error) {
                        mOnCalculatePointResult.onError(error);
                        GlobalParams.removeCallbackItem(this);
                    }
                });
            }
        });
        t.start();
    }

    private static void doCalculatePoint(String token, String shopID, String cardID, List<Product> listProducts, float totalMoney, OnCalculatePointResult mOnCalculatePointResult) {
        if (listProducts == null)
            listProducts = new ArrayList<Product>();

        String listproducts = Helper.objectToJson(listProducts);

        String link = GlobalParams.calculatePointLink;

        HttpClient httpclient = new DefaultHttpClient();
        HttpPost httppost = new HttpPost(link);

        List<NameValuePair> nameValuePairs = new ArrayList<NameValuePair>(5);

        nameValuePairs.add(new BasicNameValuePair("token", token));
        nameValuePairs.add(new BasicNameValuePair("shop_id", shopID));
        nameValuePairs.add(new BasicNameValuePair("card_id", cardID));
        nameValuePairs.add(new BasicNameValuePair("total_money", String.valueOf(totalMoney)));
        nameValuePairs.add(new BasicNameValuePair("list_products", listproducts));

        try {
            httppost.setEntity(new UrlEncodedFormEntity(nameValuePairs, "UTF-8"));
            //ResponseHandler<String> responseHandler = new BasicResponseHandler();
            ResponseHandler<String> responseHandler = Helper.getResponseHandler();
            String response = null;

            response = httpclient.execute(httppost, responseHandler);
            CalculatePointResult calculatePointResult = (CalculatePointResult) Helper.jsonToObject(response, CalculatePointResult.class);

            if (calculatePointResult.error.equals("")) {
                // parse from AchievedEvent[] -> ArrayList<AchievedEvent>
                ArrayList<AchievedEvent> events = new ArrayList<AchievedEvent>();
                for (int i = 0; i < calculatePointResult.achievedEvents.length - 1; i++) { // trừ 1 là do kết quả trả về dư 1 dấu ','
                    events.add(calculatePointResult.achievedEvents[i]);
                }
                mOnCalculatePointResult.onSuccess(events, calculatePointResult.pointsFromMoney, calculatePointResult.totalPoints);
            } else
                mOnCalculatePointResult.onError(calculatePointResult.error);
        } catch (UnsupportedEncodingException e) {
            mOnCalculatePointResult.onError(e.toString());
            e.printStackTrace();
        } catch (ClientProtocolException e) {
            mOnCalculatePointResult.onError(e.toString());
            e.printStackTrace();
        } catch (IOException e) {
            mOnCalculatePointResult.onError(e.toString());
            e.printStackTrace();
        }
    }

    public static void updatePoint(final Context context, final String shopID, final String cardQRCode, final List<AchievedEvent> listAchievedEvents, final float totalPoint, final OnUpdatePointResult mOnUpdatePointResult) {

        Thread t = new Thread(new Runnable() {
            @Override
            public void run() {
                // Kiểm tra tham số
                if (context == null) {
                    mOnUpdatePointResult.onError("context is null");
                    return;
                }
                //mContext = context;

                if (mOnUpdatePointResult == null)
                    return;

                if (!Preconditions.checkNotNull(shopID)) {
                    mOnUpdatePointResult.onError("shopID is null or equal \"\"");
                    return;
                }

                if (!Preconditions.checkNotNull(cardQRCode)) {
                    mOnUpdatePointResult.onError("cardQRCode is null or equal \"\"");
                    return;
                }

                if (!Preconditions.checkPositive(totalPoint)) {
                    mOnUpdatePointResult.onError("totalPoint is negative");
                    return;
                }

                // Lấy token
                getToken(context, new OnGetTokenResult() {
                    @Override
                    public void onSuccess(final String token) {
                        // Đến đây thì đã có token
                        // Gọi API để cập nhật điểm tích lũy

                        String cardID = cardQRCode.substring(0, 13);
                        String username = cardQRCode.substring(13, cardQRCode.length());

                        // Lấy thời gian hiện tại
                        DateFormat dateFormat = new SimpleDateFormat("dd/MM/yyyy HH:mm:ss");
                        Date date = new Date();
                        String time = dateFormat.format(date); //2014/08/06 15:59:48

                        doUpdatePoint(token, shopID, cardID, username, listAchievedEvents, totalPoint, time, mOnUpdatePointResult);
                        GlobalParams.removeCallbackItem(this);
                    }

                    @Override
                    public void onError(String error) {
                        mOnUpdatePointResult.onError(error);
                        GlobalParams.removeCallbackItem(this);
                    }
                });
            }
        });
        t.start();
    }

    private static void doUpdatePoint(String token, String shopID, String cardID, String username, List<AchievedEvent> listAchievedEvents, float totalPoint, String time, OnUpdatePointResult mOnUpdatePointResult) {
        String listEvents = null;
        if (listAchievedEvents != null) {
            listEvents = Helper.objectToJson(listAchievedEvents);
        }

        String link = GlobalParams.updatePointLink;

        HttpClient httpclient = new DefaultHttpClient();
        HttpPost httppost = new HttpPost(link);

        List<NameValuePair> nameValuePairs = new ArrayList<NameValuePair>(7);

        nameValuePairs.add(new BasicNameValuePair("token", token));
        nameValuePairs.add(new BasicNameValuePair("shop_id", shopID));
        nameValuePairs.add(new BasicNameValuePair("card_id", cardID));
        nameValuePairs.add(new BasicNameValuePair("username", username));
        nameValuePairs.add(new BasicNameValuePair("time", time));
        nameValuePairs.add(new BasicNameValuePair("total_point", String.valueOf(totalPoint)));
        nameValuePairs.add(new BasicNameValuePair("list_events", listEvents));

        try {
            httppost.setEntity(new UrlEncodedFormEntity(nameValuePairs, "UTF-8"));
            //ResponseHandler<String> responseHandler = new BasicResponseHandler();
            ResponseHandler<String> responseHandler = Helper.getResponseHandler();
            String response = null;

            response = httpclient.execute(httppost, responseHandler);
            UpdatePointResult updatePointResult = (UpdatePointResult) Helper.jsonToObject(response, UpdatePointResult.class);

            if (updatePointResult.error.equals("")) {
                mOnUpdatePointResult.onSuccess();
            } else
                mOnUpdatePointResult.onError(updatePointResult.error);
        } catch (UnsupportedEncodingException e) {
            mOnUpdatePointResult.onError(e.toString());
            e.printStackTrace();
        } catch (ClientProtocolException e) {
            mOnUpdatePointResult.onError(e.toString());
            e.printStackTrace();
        } catch (IOException e) {
            mOnUpdatePointResult.onError(e.toString());
            e.printStackTrace();
        }
    }

    public static void getListEvents(final Context context, final String shopID, final OnGetListEventsResult mOnGetListEventsResult) {

        Thread t = new Thread(new Runnable() {
            @Override
            public void run() {
                // Kiểm tra tham số
                if (context == null) {
                    mOnGetListEventsResult.onError("context is null");
                    return;
                }
                //mContext = context;

                if (mOnGetListEventsResult == null)
                    return;

                if (!Preconditions.checkNotNull(shopID)) {
                    mOnGetListEventsResult.onError("shopID is null or equal \"\"");
                    return;
                }

                // Lấy token
                getToken(context, new OnGetTokenResult() {
                    @Override
                    public void onSuccess(final String token) {
                        // Đến đây thì đã có token
                        // Gọi API để cập nhật điểm tích lũy

                        doGetListEvents(token, shopID, mOnGetListEventsResult);
                        GlobalParams.removeCallbackItem(this);
                    }

                    @Override
                    public void onError(String error) {
                        mOnGetListEventsResult.onError(error);
                        GlobalParams.removeCallbackItem(this);
                    }
                });
            }
        });
        t.start();
    }

    private static void doGetListEvents(String token, String shopID, OnGetListEventsResult mOnGetListEvents) {

        String link = GlobalParams.getListEvents;

        HttpClient httpclient = new DefaultHttpClient();
        HttpPost httppost = new HttpPost(link);

        List<NameValuePair> nameValuePairs = new ArrayList<NameValuePair>(2);

        nameValuePairs.add(new BasicNameValuePair("token", token));
        nameValuePairs.add(new BasicNameValuePair("shop_id", shopID));

        try {
            httppost.setEntity(new UrlEncodedFormEntity(nameValuePairs, "UTF-8"));
            //ResponseHandler<String> responseHandler = new BasicResponseHandler();
            ResponseHandler<String> responseHandler = Helper.getResponseHandler();
            String response = null;

            response = httpclient.execute(httppost, responseHandler);
            GetListEventsResult getListEventsResult = (GetListEventsResult) Helper.jsonToObject(response, GetListEventsResult.class);

            if (getListEventsResult.error.equals("")) {
                // parse from Event[] -> ArrayList<Event>
                ArrayList<Event> events = new ArrayList<Event>();
                for (int i = 0; i < getListEventsResult.listEvents.length - 1; i++) { // trừ 1 là do kết quả trả về dư 1 dấu ','
                    events.add(getListEventsResult.listEvents[i]);
                }
                mOnGetListEvents.onSuccess(events);
            } else
                mOnGetListEvents.onError(getListEventsResult.error);
        } catch (UnsupportedEncodingException e) {
            mOnGetListEvents.onError(e.toString());
            e.printStackTrace();
        } catch (ClientProtocolException e) {
            mOnGetListEvents.onError(e.toString());
            e.printStackTrace();
        } catch (IOException e) {
            mOnGetListEvents.onError(e.toString());
            e.printStackTrace();
        }
    }


    public static void getListAwards(final Context context, final String shopID, final OnGetListAwardsResult mOnGetListAwardsResult) {

        Thread t = new Thread(new Runnable() {
            @Override
            public void run() {
                // Kiểm tra tham số
                if (context == null) {
                    mOnGetListAwardsResult.onError("context is null");
                    return;
                }
                //mContext = context;

                if (mOnGetListAwardsResult == null)
                    return;

                if (!Preconditions.checkNotNull(shopID)) {
                    mOnGetListAwardsResult.onError("shopID is null or equal \"\"");
                    return;
                }

                // Lấy token
                getToken(context, new OnGetTokenResult() {
                    @Override
                    public void onSuccess(final String token) {
                        // Đến đây thì đã có token
                        // Gọi API để cập nhật điểm tích lũy

                        doGetListAwards(token, shopID, mOnGetListAwardsResult);
                        GlobalParams.removeCallbackItem(this);
                    }

                    @Override
                    public void onError(String error) {
                        mOnGetListAwardsResult.onError(error);
                        GlobalParams.removeCallbackItem(this);
                    }
                });
            }
        });
        t.start();
    }

    private static void doGetListAwards(String token, String shopID, OnGetListAwardsResult mOnGetListAwards) {

        String link = GlobalParams.getListAwards;

        HttpClient httpclient = new DefaultHttpClient();
        HttpPost httppost = new HttpPost(link);

        List<NameValuePair> nameValuePairs = new ArrayList<NameValuePair>(2);

        nameValuePairs.add(new BasicNameValuePair("token", token));
        nameValuePairs.add(new BasicNameValuePair("shop_id", shopID));

        try {
            httppost.setEntity(new UrlEncodedFormEntity(nameValuePairs, "UTF-8"));
            //ResponseHandler<String> responseHandler = new BasicResponseHandler();
            ResponseHandler<String> responseHandler = Helper.getResponseHandler();
            String response = null;

            response = httpclient.execute(httppost, responseHandler);
            GetListAwardsResult getListAwardsResult = (GetListAwardsResult) Helper.jsonToObject(response, GetListAwardsResult.class);

            if (getListAwardsResult.error.equals("")) {
                // parse from Award[] -> ArrayList<Award>
                ArrayList<Award> awards = new ArrayList<Award>();
                for (int i = 0; i < getListAwardsResult.listAwards.length - 1; i++) { // trừ 1 là do kết quả trả về dư 1 dấu ','
                    awards.add(getListAwardsResult.listAwards[i]);
                }
                mOnGetListAwards.onSuccess(awards);
            } else
                mOnGetListAwards.onError(getListAwardsResult.error);
        } catch (UnsupportedEncodingException e) {
            mOnGetListAwards.onError(e.toString());
            e.printStackTrace();
        } catch (ClientProtocolException e) {
            mOnGetListAwards.onError(e.toString());
            e.printStackTrace();
        } catch (IOException e) {
            mOnGetListAwards.onError(e.toString());
            e.printStackTrace();
        }
    }

    public static void getListShops(final Context context, final OnGetListShopsResult mOnGetListShopsResult) {

        Thread t = new Thread(new Runnable() {
            @Override
            public void run() {
                // Kiểm tra tham số
                if (context == null) {
                    mOnGetListShopsResult.onError("context is null");
                    return;
                }
                //mContext = context;

                if (mOnGetListShopsResult == null)
                    return;

                // Lấy token
                getToken(context, new OnGetTokenResult() {
                    @Override
                    public void onSuccess(final String token) {
                        // Đến đây thì đã có token
                        // Gọi API để cập nhật điểm tích lũy

                        doGetListShops(token, mOnGetListShopsResult);
                        GlobalParams.removeCallbackItem(this);
                    }

                    @Override
                    public void onError(String error) {
                        mOnGetListShopsResult.onError(error);
                        GlobalParams.removeCallbackItem(this);
                    }
                });
            }
        });
        t.start();
    }

    private static void doGetListShops(String token, OnGetListShopsResult mOnGetListShopsResult) {

        String link = GlobalParams.getListShops;

        HttpClient httpclient = new DefaultHttpClient();
        HttpPost httppost = new HttpPost(link);

        List<NameValuePair> nameValuePairs = new ArrayList<NameValuePair>(1);

        nameValuePairs.add(new BasicNameValuePair("token", token));

        try {
            httppost.setEntity(new UrlEncodedFormEntity(nameValuePairs, "UTF-8"));
            //ResponseHandler<String> responseHandler = new BasicResponseHandler();
            ResponseHandler<String> responseHandler = Helper.getResponseHandler();
            String response = null;

            response = httpclient.execute(httppost, responseHandler);
            GetListShopsResult getListShopsResult = (GetListShopsResult) Helper.jsonToObject(response, GetListShopsResult.class);

            if (getListShopsResult.error.equals("")) {
                // parse from Shop[] -> ArrayList<Shop>
                ArrayList<Shop> shops = new ArrayList<Shop>();
                for (int i = 0; i < getListShopsResult.listShops.length - 1; i++) { // trừ 1 là do kết quả trả về dư 1 dấu ','
                    shops.add(getListShopsResult.listShops[i]);
                }
                mOnGetListShopsResult.onSuccess(shops);
            } else
                mOnGetListShopsResult.onError(getListShopsResult.error);
        } catch (UnsupportedEncodingException e) {
            mOnGetListShopsResult.onError(e.toString());
            e.printStackTrace();
        } catch (ClientProtocolException e) {
            mOnGetListShopsResult.onError(e.toString());
            e.printStackTrace();
        } catch (IOException e) {
            mOnGetListShopsResult.onError(e.toString());
            e.printStackTrace();
        }
    }

    public static void getListCards(final Context context, final OnGetListCardsResult mOnGetListCardsResult) {

        Thread t = new Thread(new Runnable() {
            @Override
            public void run() {
                // Kiểm tra tham số
                if (context == null) {
                    mOnGetListCardsResult.onError("context is null");
                    return;
                }
                //mContext = context;

                if (mOnGetListCardsResult == null)
                    return;

                // Lấy token
                getToken(context, new OnGetTokenResult() {
                    @Override
                    public void onSuccess(final String token) {
                        // Đến đây thì đã có token
                        // Gọi API để cập nhật điểm tích lũy

                        doGetListCards(token, mOnGetListCardsResult);
                        GlobalParams.removeCallbackItem(this);
                    }

                    @Override
                    public void onError(String error) {
                        mOnGetListCardsResult.onError(error);
                        GlobalParams.removeCallbackItem(this);
                    }
                });
            }
        });
        t.start();
    }

    private static void doGetListCards(String token, OnGetListCardsResult mOnGetListCardsResult) {

        String link = GlobalParams.getListCards;

        HttpClient httpclient = new DefaultHttpClient();
        HttpPost httppost = new HttpPost(link);

        List<NameValuePair> nameValuePairs = new ArrayList<NameValuePair>(1);

        nameValuePairs.add(new BasicNameValuePair("token", token));

        try {
            httppost.setEntity(new UrlEncodedFormEntity(nameValuePairs, "UTF-8"));
            //ResponseHandler<String> responseHandler = new BasicResponseHandler();
            ResponseHandler<String> responseHandler = Helper.getResponseHandler();
            String response = null;

            response = httpclient.execute(httppost, responseHandler);
            GetListCardsResult getListCardsResult = (GetListCardsResult) Helper.jsonToObject(response, GetListCardsResult.class);

            if (getListCardsResult.error.equals("")) {
                // parse from Card[] -> ArrayList<Card>
                ArrayList<Card> cards = new ArrayList<Card>();
                for (int i = 0; i < getListCardsResult.listCards.length - 1; i++) { // trừ 1 là do kết quả trả về dư 1 dấu ','
                    cards.add(getListCardsResult.listCards[i]);
                }
                mOnGetListCardsResult.onSuccess(cards);
            } else
                mOnGetListCardsResult.onError(getListCardsResult.error);
        } catch (UnsupportedEncodingException e) {
            mOnGetListCardsResult.onError(e.toString());
            e.printStackTrace();
        } catch (ClientProtocolException e) {
            mOnGetListCardsResult.onError(e.toString());
            e.printStackTrace();
        } catch (IOException e) {
            mOnGetListCardsResult.onError(e.toString());
            e.printStackTrace();
        }
    }

    public static void getListCustomers(final Context context, final String shopID, final OnGetListCustomersResult mOnGetListCustomersResult) {

        Thread t = new Thread(new Runnable() {
            @Override
            public void run() {
                // Kiểm tra tham số
                if (context == null) {
                    mOnGetListCustomersResult.onError("context is null");
                    return;
                }
                //mContext = context;

                if (mOnGetListCustomersResult == null)
                    return;

                if (!Preconditions.checkNotNull(shopID)) {
                    mOnGetListCustomersResult.onError("shopID is null or equal \"\"");
                    return;
                }

                // Lấy token
                getToken(context, new OnGetTokenResult() {
                    @Override
                    public void onSuccess(final String token) {
                        // Đến đây thì đã có token
                        // Gọi API để cập nhật điểm tích lũy

                        doGetListCustomers(token, shopID, mOnGetListCustomersResult);
                        GlobalParams.removeCallbackItem(this);
                    }

                    @Override
                    public void onError(String error) {
                        mOnGetListCustomersResult.onError(error);
                        GlobalParams.removeCallbackItem(this);
                    }
                });
            }
        });
        t.start();
    }

    private static void doGetListCustomers(String token, String shopID, OnGetListCustomersResult mOnGetListCustomersResult) {

        String link = GlobalParams.getListCustomers;

        HttpClient httpclient = new DefaultHttpClient();
        HttpPost httppost = new HttpPost(link);

        List<NameValuePair> nameValuePairs = new ArrayList<NameValuePair>(2);

        nameValuePairs.add(new BasicNameValuePair("token", token));
        nameValuePairs.add(new BasicNameValuePair("shop_id", shopID));

        try {
            httppost.setEntity(new UrlEncodedFormEntity(nameValuePairs, "UTF-8"));
            //ResponseHandler<String> responseHandler = new BasicResponseHandler();
            ResponseHandler<String> responseHandler = Helper.getResponseHandler();
            String response = null;

            response = httpclient.execute(httppost, responseHandler);
            GetListCustomersResult getListCustomersResult = (GetListCustomersResult) Helper.jsonToObject(response, GetListCustomersResult.class);

            if (getListCustomersResult.error.equals("")) {
                // parse from Customer[] -> ArrayList<Customer>
                ArrayList<Customer> customers = new ArrayList<Customer>();
                for (int i = 0; i < getListCustomersResult.listCustomers.length - 1; i++) { // trừ 1 là do kết quả trả về dư 1 dấu ','
                    customers.add(getListCustomersResult.listCustomers[i]);
                }
                mOnGetListCustomersResult.onSuccess(customers);
            } else
                mOnGetListCustomersResult.onError(getListCustomersResult.error);
        } catch (UnsupportedEncodingException e) {
            mOnGetListCustomersResult.onError(e.toString());
            e.printStackTrace();
        } catch (ClientProtocolException e) {
            mOnGetListCustomersResult.onError(e.toString());
            e.printStackTrace();
        } catch (IOException e) {
            mOnGetListCustomersResult.onError(e.toString());
            e.printStackTrace();
        }
    }

    private static void getToken(Context mContext, OnGetTokenResult onGetTokenResult) {
        try {
            Context otherContext = mContext.createPackageContext(GlobalParams.appPackageName, Context.CONTEXT_IGNORE_SECURITY);
            SharedPreferences preference = otherContext.getSharedPreferences(GlobalParams.LOGIN_STATE, Context.MODE_WORLD_READABLE);
            String token = preference.getString(GlobalParams.TOKEN, "");

            if (!token.equals(""))
                onGetTokenResult.onSuccess(token);
            else {
                // Đã cài nhưng chưa đăng nhập
                // Lưu callback lại
                // Mở màn hình login để người dùng đăng nhập

                Intent intent = new Intent(mContext, GetTokenActivity.class);
                int key = intent.hashCode();
                GlobalParams.addCallbackItem(key, onGetTokenResult);

                intent.putExtra(GlobalParams.GET_TOKEN_CALLBACK_KEY, key);

                mContext.startActivity(intent);
            }
        } catch (PackageManager.NameNotFoundException e) {
            // Chưa cài Manager App
            installManagerApp(mContext);
            onGetTokenResult.onError("You have not installed the Manager App yet");
        }
    }


    private static boolean isManagerAppInstalled(Context mContext) {
        PackageManager pm = mContext.getPackageManager();
        boolean isAppInstalled;
        try {
            pm.getPackageInfo(GlobalParams.appPackageName, PackageManager.GET_ACTIVITIES);
            isAppInstalled = true;
        } catch (PackageManager.NameNotFoundException e) {
            isAppInstalled = false;
        }
        return isAppInstalled;
    }

    private static void installManagerApp(Context mContext) {
        try {
            //mContext.startActivity(new Intent(Intent.ACTION_VIEW, Uri.parse("market://details?id=" + GlobalParams.appPackageName)));
            mContext.startActivity(new Intent(Intent.ACTION_VIEW, Uri.parse("market://details?id=" + "com.google.android.googlequicksearchbox")));
        } catch (android.content.ActivityNotFoundException anfe) {
            //mContext.startActivity(new Intent(Intent.ACTION_VIEW, Uri.parse("https://play.google.com/store/apps/details?id=" + GlobalParams.appPackageName)));
            mContext.startActivity(new Intent(Intent.ACTION_VIEW, Uri.parse("https://play.google.com/store/apps/details?id=" + "com.google.android.googlequicksearchbox")));
        }
    }


    public interface OnCalculatePointResult {
        public void onSuccess(ArrayList<AchievedEvent> listAchievedEvents, int pointFromMoney, int totalPoint);

        void onError(String error);
    }

    public interface OnUpdatePointResult {
        void onSuccess();

        void onError(String error);
    }

    public interface OnGetListShopsResult {
        void onSuccess(ArrayList<Shop> listShops);

        void onError(String error);
    }

    public interface OnGetListCardsResult {
        void onSuccess(ArrayList<Card> listCards);

        void onError(String error);
    }

    public interface OnGetListCustomersResult {
        void onSuccess(ArrayList<Customer> listCustomers);

        void onError(String error);
    }

    public interface OnGetListEventsResult {
        void onSuccess(ArrayList<Event> listEvents);

        void onError(String error);
    }

    public interface OnGetListAwardsResult {
        void onSuccess(ArrayList<Award> listAwards);

        void onError(String error);
    }

    public interface OnGetTokenResult {
        void onSuccess(String token);

        void onError(String error);
    }
}
