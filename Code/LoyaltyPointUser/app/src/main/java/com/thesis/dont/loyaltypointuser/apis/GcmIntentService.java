package com.thesis.dont.loyaltypointuser.apis;

import android.app.IntentService;
import android.app.Notification;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.media.Ringtone;
import android.media.RingtoneManager;
import android.net.Uri;
import android.os.Bundle;
import android.os.SystemClock;
import android.support.v4.app.NotificationCompat;
import android.util.Log;

import com.google.android.gms.gcm.GoogleCloudMessaging;
import com.thesis.dont.loyaltypointuser.R;
import com.thesis.dont.loyaltypointuser.controllers.CardsListActivity;
import com.thesis.dont.loyaltypointuser.controllers.CardsListActivity;
import com.thesis.dont.loyaltypointuser.controllers.LoginActivity;
import com.thesis.dont.loyaltypointuser.controllers.ShopDetailActivity;
import com.thesis.dont.loyaltypointuser.models.Global;
import com.thesis.dont.loyaltypointuser.models.Shop;
import com.thesis.dont.loyaltypointuser.models.ShopModel;

/**
 * Created by 11120_000 on 09/04/15.
 */
public class GcmIntentService extends IntentService {
    public static int NOTIFICATION_ID = 1;
    private NotificationManager mNotificationManager;
    NotificationCompat.Builder builder;

    Shop mShop;

    public static final String TAG = "GcmIntentService";

    public GcmIntentService() {
        super("GcmIntentService");
    }

    @Override
    protected void onHandleIntent(Intent intent) {
        final Bundle extras = intent.getExtras();
        GoogleCloudMessaging gcm = GoogleCloudMessaging.getInstance(this);
        // The getMessageType() intent parameter must be the intent you received
        // in your BroadcastReceiver.

        String messageType = gcm.getMessageType(intent);

        if (!extras.isEmpty()) {  // has effect of unparcelling Bundle
            /*
             * Filter messages based on message type. Since it is likely that GCM
             * will be extended in the future with new message types, just ignore
             * any message types you're not interested in, or that you don't
             * recognize.
             */
            if (GoogleCloudMessaging.
                    MESSAGE_TYPE_SEND_ERROR.equals(messageType)) {
                sendNotification(extras, false);
            } else if (GoogleCloudMessaging.
                    MESSAGE_TYPE_DELETED.equals(messageType)) {
                sendNotification(extras, false);
                // If it's a regular GCM message, do some work.
            } else if (GoogleCloudMessaging.
                    MESSAGE_TYPE_MESSAGE.equals(messageType)) {

                // Get shop info
                SharedPreferences preference = (SharedPreferences) getSharedPreferences(LoginActivity.LOGIN_STATE, MODE_PRIVATE);
                Global.userToken = preference.getString(LoginActivity.TOKEN, "");
                if(Global.userToken.equals("")) {
                    GcmBroadcastReceiver.completeWakefulIntent(intent);
                    return;
                }

                String shopID = extras.getString("shopID");

                ShopModel.getShopInfo(Global.userToken, shopID, new ShopModel.OnGetShopInfoResult() {
                    @Override
                    public void onSuccess(Shop shop) {
                        mShop = shop;
                        sendNotification(extras, true);
                    }

                    @Override
                    public void onError(String error) {
                        // do nothing
                    }
                });
            }
        }
        // Release the wake lock provided by the WakefulBroadcastReceiver.
        GcmBroadcastReceiver.completeWakefulIntent(intent);
    }

    // Put the message into a notification and post it.
    // This is just one simple example of what you might choose to do with
    // a GCM message.
    private void sendNotification(Bundle data, boolean isComplete) {
        mNotificationManager = (NotificationManager)
                this.getSystemService(Context.NOTIFICATION_SERVICE);

        NotificationCompat.Builder mBuilder = null;
        if(!isComplete) {
            mBuilder = new NotificationCompat.Builder(this)
                            .setSmallIcon(R.drawable.ic_bag)
                            .setContentTitle("Error!")
                            .setStyle(new NotificationCompat.BigTextStyle()
                                    .bigText(data.toString()))
                            .setContentText(data.toString());
        }else {
            String message = data.getString("message");

            if(message.equals("add point")) {

                String point = data.getString("point");
                String newPoint = data.getString("newPoint");

                mBuilder = new NotificationCompat.Builder(this)
                                .setSmallIcon(R.drawable.ic_bag)
                                .setContentTitle(mShop.getName())
                                .setStyle(new NotificationCompat.BigTextStyle()
                                        .bigText(data.toString()))
                                .setContentText("Added " + point + " point");

                Intent i = new Intent(this, ShopDetailActivity.class);
                i.putExtra(Global.SHOP_OBJECT, mShop);
                i.putExtra(Global.TAB_INDEX, 2);
                i.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                PendingIntent contentIntent = PendingIntent.getActivity(this, NOTIFICATION_ID, i, PendingIntent.FLAG_ONE_SHOT);
                mBuilder.setContentIntent(contentIntent);

            }else if(message.equals("request accepted")) {
                mBuilder = new NotificationCompat.Builder(this)
                        .setSmallIcon(R.drawable.ic_bag)
                        .setContentTitle("Congratulations")
                        .setStyle(new NotificationCompat.BigTextStyle()
                                .bigText(data.toString()))
                        .setContentText(mShop.getName() + " has accepted your register request");

                Intent i = new Intent(this, ShopDetailActivity.class);
                i.putExtra(Global.SHOP_OBJECT, mShop);
                i.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                PendingIntent contentIntent = PendingIntent.getActivity(this, NOTIFICATION_ID, i, PendingIntent.FLAG_ONE_SHOT);
                mBuilder.setContentIntent(contentIntent);
            }else if(message.equals("trade successfully")) {
                mBuilder = new NotificationCompat.Builder(this)
                        .setSmallIcon(R.drawable.ic_bag)
                        .setContentTitle("Congratulations")
                        .setStyle(new NotificationCompat.BigTextStyle()
                                .bigText(data.toString()))
                        .setContentText(mShop.getName() + " has accepted your register request");

                Intent i = new Intent(this, ShopDetailActivity.class);
                i.putExtra(Global.SHOP_OBJECT, mShop);
                i.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                PendingIntent contentIntent = PendingIntent.getActivity(this, NOTIFICATION_ID, i, PendingIntent.FLAG_ONE_SHOT);
                mBuilder.setContentIntent(contentIntent);
            }
        }

        Notification notification = mBuilder.build();
        notification.flags = Notification.DEFAULT_LIGHTS | Notification.FLAG_AUTO_CANCEL;
        mNotificationManager.notify(NOTIFICATION_ID, notification);
        NOTIFICATION_ID++;
        try {
            Uri noti = RingtoneManager.getDefaultUri(RingtoneManager.TYPE_NOTIFICATION);
            Ringtone r = RingtoneManager.getRingtone(getApplicationContext(), noti);
            r.play();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
