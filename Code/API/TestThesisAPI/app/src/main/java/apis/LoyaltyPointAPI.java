package apis;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.Parcel;
import android.os.Parcelable;
import android.provider.Settings;
import android.util.Log;

import com.example.testthesisapi.GetTokenActivity;

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

import controllers.Helper;
import models.AchievedEvent;
import models.CalculatePointResult;
import models.GlobalParams;
import models.Product;
import models.UpdatePointResult;

/**
 * Created by 11120_000 on 22/05/15.
 */
public class LoyaltyPointAPI {

    Context mContext;
    OnGetTokenResult mGetTokenResult;

    public void calculatePoint(final Context context, final String cardQRCode, final List<Product> listProducts, final float totalMoney, final OnCalculatePointResult mOnCalculatePointResult) {

        Thread t = new Thread(new Runnable() {
            @Override
            public void run() {
                // Kiểm tra tham số
                if(context == null) {
                    mOnCalculatePointResult.onError("context is null");
                    return;
                }
                mContext = context;

                /*if(mOnCalculatePointResult == null)
                    return;

                if(!Preconditions.checkNotNull(cardQRCode)) {
                    mOnCalculatePointResult.onError("cardQRCode is null or equal \"\"");
                    return;
                }

                if(!Preconditions.checkPositive(totalMoney)) {
                    mOnCalculatePointResult.onError("totalMoney is negative");
                    return;
                }

                Preconditions<Product> preconditions = new Preconditions<Product>();
                if(!preconditions.checkNotNull(listProducts)) {
                    mOnCalculatePointResult.onError("listProducts is null or has no item");
                    return;
                }*/

                // Lấy token
                getToken(new OnGetTokenResult() {
                    @Override
                    public void onSuccess(final String token) {
                        // Đến đây thì đã có token
                        // Gọi API để tính điểm tích lũy
                        //mOnCalculatePointResult.onSuccess(token);
                        doCalculatePoint(token, cardQRCode, listProducts, totalMoney, mOnCalculatePointResult);
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

    public void updatePoint(final Context context, final String cardQRCode, final List<Product> listProducts, final float totalMoney, final OnUpdatePointResult mOnUpdatePointResult) {

        Thread t = new Thread(new Runnable() {
            @Override
            public void run() {
                // Kiểm tra tham số
                if(context == null) {
                    mOnUpdatePointResult.onError("context is null");
                    return;
                }
                mContext = context;

                /*if(mOnCalculatePointResult == null)
                    return;

                if(!Preconditions.checkNotNull(cardQRCode)) {
                    mOnCalculatePointResult.onError("cardQRCode is null or equal \"\"");
                    return;
                }

                if(!Preconditions.checkPositive(totalMoney)) {
                    mOnCalculatePointResult.onError("totalMoney is negative");
                    return;
                }

                Preconditions<Product> preconditions = new Preconditions<Product>();
                if(!preconditions.checkNotNull(listProducts)) {
                    mOnCalculatePointResult.onError("listProducts is null or has no item");
                    return;
                }*/

                // Lấy token
                getToken(new OnGetTokenResult() {
                    @Override
                    public void onSuccess(final String token) {
                        // Đến đây thì đã có token
                        // Gọi API để cập nhật điểm tích lũy

                        doUpdatePoint(token, cardQRCode, listProducts, totalMoney, mOnUpdatePointResult);
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

    private void doCalculatePoint(String token, String cardQRCode, List<Product> listProducts, float totalMoney, OnCalculatePointResult mOnCalculatePointResult) {
        final String listproducts = Helper.objectToJson(listProducts);

        String link = GlobalParams.calculatePointLink;

        HttpClient httpclient = new DefaultHttpClient();
        HttpPost httppost = new HttpPost(link);

        List<NameValuePair> nameValuePairs = new ArrayList<NameValuePair>(4);

        nameValuePairs.add(new BasicNameValuePair("token", token));
        nameValuePairs.add(new BasicNameValuePair("card_qrcode", cardQRCode));
        nameValuePairs.add(new BasicNameValuePair("total_money", String.valueOf(totalMoney)));
        nameValuePairs.add(new BasicNameValuePair("list_products", listproducts));

        try {
            httppost.setEntity(new UrlEncodedFormEntity(nameValuePairs, "UTF-8"));
            //ResponseHandler<String> responseHandler = new BasicResponseHandler();
            ResponseHandler<String> responseHandler = Helper.getResponseHandler();
            String response = null;

            response = httpclient.execute(httppost, responseHandler);
            CalculatePointResult calculatePointResult = (CalculatePointResult)Helper.jsonToObject(response, CalculatePointResult.class);

            if(calculatePointResult.error.equals("")) {
                // parse from AchievedEvent[] -> ArrayList<AchievedEvent>
                ArrayList<AchievedEvent> events = new ArrayList<AchievedEvent>();
                for(int i=0; i<calculatePointResult.achievedEvents.length-1; i++) { // trừ 1 là do kết quả trả về dư 1 dấu ','
                    events.add(calculatePointResult.achievedEvents[i]);
                }
                mOnCalculatePointResult.onSuccess(events, calculatePointResult.totalPoints);
            }
            else
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

    private void doUpdatePoint(String token, String cardQRCode, List<Product> listProducts, float totalMoney, OnUpdatePointResult mOnUpdatePointResult) {
        final String listproducts = Helper.objectToJson(listProducts);

        String link = GlobalParams.updatePointLink;

        HttpClient httpclient = new DefaultHttpClient();
        HttpPost httppost = new HttpPost(link);

        List<NameValuePair> nameValuePairs = new ArrayList<NameValuePair>(4);

        nameValuePairs.add(new BasicNameValuePair("token", token));
        nameValuePairs.add(new BasicNameValuePair("card_qrcode", cardQRCode));
        nameValuePairs.add(new BasicNameValuePair("total_money", String.valueOf(totalMoney)));
        nameValuePairs.add(new BasicNameValuePair("list_products", listproducts));

        try {
            httppost.setEntity(new UrlEncodedFormEntity(nameValuePairs, "UTF-8"));
            //ResponseHandler<String> responseHandler = new BasicResponseHandler();
            ResponseHandler<String> responseHandler = Helper.getResponseHandler();
            String response = null;

            response = httpclient.execute(httppost, responseHandler);
            UpdatePointResult updatePointResult = (UpdatePointResult)Helper.jsonToObject(response, UpdatePointResult.class);

            if(updatePointResult.error.equals("")) {
                // parse from AchievedEvent[] -> ArrayList<AchievedEvent>
                ArrayList<AchievedEvent> events = new ArrayList<AchievedEvent>();
                for(int i=0; i<updatePointResult.achievedEvents.length-1; i++) { // trừ 1 là do kết quả trả về dư 1 dấu ','
                    events.add(updatePointResult.achievedEvents[i]);
                }
                mOnUpdatePointResult.onSuccess(events, updatePointResult.totalPoints);
            }
            else
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

    private void getToken(OnGetTokenResult onGetTokenResult) {
        try {
            Context otherContext = mContext.createPackageContext(GlobalParams.appPackageName, Context.CONTEXT_IGNORE_SECURITY);
            SharedPreferences preference = otherContext.getSharedPreferences(GlobalParams.LOGIN_STATE, Context.MODE_WORLD_READABLE);
            String token = preference.getString(GlobalParams.TOKEN, "");

            if(!token.equals(""))
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
            installManagerApp();
            onGetTokenResult.onError("You have not installed the Manager App yet");
        }
    }


    private boolean isManagerAppInstalled() {
        PackageManager pm = mContext.getPackageManager();
        boolean isAppInstalled;
        try {
            pm.getPackageInfo(GlobalParams.appPackageName, PackageManager.GET_ACTIVITIES);
            isAppInstalled = true;
        }
        catch (PackageManager.NameNotFoundException e) {
            isAppInstalled = false;
        }
        return isAppInstalled;
    }

    private void installManagerApp() {
        try {
            //mContext.startActivity(new Intent(Intent.ACTION_VIEW, Uri.parse("market://details?id=" + GlobalParams.appPackageName)));
            mContext.startActivity(new Intent(Intent.ACTION_VIEW, Uri.parse("market://details?id=" + "com.google.android.googlequicksearchbox")));
        } catch (android.content.ActivityNotFoundException anfe) {
            //mContext.startActivity(new Intent(Intent.ACTION_VIEW, Uri.parse("https://play.google.com/store/apps/details?id=" + GlobalParams.appPackageName)));
            mContext.startActivity(new Intent(Intent.ACTION_VIEW, Uri.parse("https://play.google.com/store/apps/details?id=" + "com.google.android.googlequicksearchbox")));
        }
    }


    public interface OnCalculatePointResult{
        void onSuccess(ArrayList<AchievedEvent> result, float totalPoint);
        //void onSuccess(String token);
        void onError(String error);
    }

    public interface OnUpdatePointResult{
        void onSuccess(ArrayList<AchievedEvent> result, float totalPoint);
        //void onSuccess(String token);
        void onError(String error);
    }

    public interface OnGetTokenResult {
        void onSuccess(String token);
        void onError(String error);
    }
}
