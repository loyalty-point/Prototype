package apis;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.Parcel;
import android.os.Parcelable;
import android.util.Log;

import com.example.testthesisapi.GetTokenActivity;

import java.util.List;

import models.GlobalParams;
import models.Product;

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
                // Ki?m tra tham s?
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

                // L?y token
                getToken(new OnGetTokenResult() {
                    @Override
                    public void onSuccess(final String token) {
                        // ??n ?ây thì ?ã có token
                        // G?i API ?? tính ?i?m tích l?y
                        Log.e("get token", "successfully, token = " + token);
                        mOnCalculatePointResult.onSuccess(token);
                    }

                    @Override
                    public void onError(String error) {
                        mOnCalculatePointResult.onError(error);
                    }
                });
            }
        });
        t.start();
    }

    private void getToken(OnGetTokenResult onGetTokenResult) {
        try {
            Context otherContext = mContext.createPackageContext(GlobalParams.appPackageName, Context.CONTEXT_IGNORE_SECURITY);
            SharedPreferences preference = otherContext.getSharedPreferences(GlobalParams.LOGIN_STATE, Context.MODE_WORLD_READABLE);
            String token = preference.getString(GlobalParams.TOKEN, "");

            if(!token.equals(""))
                onGetTokenResult.onSuccess(token);
            else {
                // ?ã cài nh?ng ch?a ??ng nh?p
                // L?u callback l?i
                // M? màn hình login ?? ng??i dùng ??ng nh?p

                Intent intent = new Intent(mContext, GetTokenActivity.class);
                int key = intent.hashCode();
                GlobalParams.mapCallbacks.put(key, onGetTokenResult);
                intent.putExtra(GlobalParams.GET_TOKEN_CALLBACK_KEY, key);
                //intent.putExtra(GlobalParams.GET_TOKEN_CALLBACK_KEY, onGetTokenResult);
                /*Bundle b = new Bundle();
                b.putParcelable(GlobalParams.GET_TOKEN_CALLBACK_KEY, (Parcelable) onGetTokenResult);*/
                mContext.startActivity(intent);
            }
        } catch (PackageManager.NameNotFoundException e) {
            // Ch?a cài Manager App
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
            mContext.startActivity(new Intent(Intent.ACTION_VIEW, Uri.parse("market://details?id=" + GlobalParams.appPackageName)));
        } catch (android.content.ActivityNotFoundException anfe) {
            mContext.startActivity(new Intent(Intent.ACTION_VIEW, Uri.parse("https://play.google.com/store/apps/details?id=" + GlobalParams.appPackageName)));
        }
    }


    public interface OnCalculatePointResult{
        //public void onSuccess(ArrayList<AchievedEvent> result, float totalPoint);
        public void onSuccess(String token);
        public void onError(String error);
    }

    public interface OnGetTokenResult {
        public void onSuccess(String token);
        public void onError(String error);
    }
}
