package com.thesis.dont.loyaltypointadmin.apis;

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
import android.support.v4.app.NotificationCompat;
import android.util.Log;

import com.google.android.gms.gcm.GoogleCloudMessaging;
import com.thesis.dont.loyaltypointadmin.R;
import com.thesis.dont.loyaltypointadmin.controllers.CardDetailActivity;
import com.thesis.dont.loyaltypointadmin.controllers.LoginActivity;
import com.thesis.dont.loyaltypointadmin.controllers.ShopDetailActivity;
import com.thesis.dont.loyaltypointadmin.models.Card;
import com.thesis.dont.loyaltypointadmin.models.CardModel;
import com.thesis.dont.loyaltypointadmin.models.Global;
import com.thesis.dont.loyaltypointadmin.models.Shop;
import com.thesis.dont.loyaltypointadmin.models.ShopModel;

/**
 * Created by 11120_000 on 09/04/15.
 */
public class GcmIntentService extends IntentService {
    public static int NOTIFICATION_ID = 1;
    private NotificationManager mNotificationManager;
    NotificationCompat.Builder builder;

    Shop mShop;
    Card mCard;

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
                sendNotification("Send error: " + extras.toString(), false);
            } else if (GoogleCloudMessaging.
                    MESSAGE_TYPE_DELETED.equals(messageType)) {
                sendNotification("Deleted messages on server: " +
                        extras.toString(), false);
                // If it's a regular GCM message, do some work.
            } else if (GoogleCloudMessaging.
                    MESSAGE_TYPE_MESSAGE.equals(messageType)) {

                // Get shop info
                //String token = extras.getString("token");
                SharedPreferences preference = (SharedPreferences) getSharedPreferences(LoginActivity.LOGIN_STATE, MODE_PRIVATE);
                Global.userToken = preference.getString(LoginActivity.TOKEN, "");
                if(Global.userToken.equals("")) {
                    GcmBroadcastReceiver.completeWakefulIntent(intent);
                    return;
                }
                final String type = extras.getString("type");
                if(type.equals("card")){
                    String cardId = extras.getString("cardID");
                    CardModel.getCardInfo(Global.userToken, cardId, new CardModel.OnGetCardInfoResult() {
                        @Override
                        public void onSuccess(Card card) {
                            mCard = card;
                            Log.e("get card info", "success");
                            sendNotification(extras.getString("message"), true);
                        }

                        @Override
                        public void onError(String error) {
                            Log.e("get card info", error.toString());
                        }
                    });
                }else {
                    String shopID = extras.getString("shopID");
                    String cardID = extras.getString("carID");
                    ShopModel.setOnGetShopInfoResult(new ShopModel.OnGetShopInfoResult() {
                        @Override
                        public void onSuccess(Shop shop) {
                            mShop = shop;
                            sendNotification(extras.getString("message"), true);
                        }

                        @Override
                        public void onError(String error) {
                            //sendNotification(error);
                        }
                    });
                    ShopModel.getShopInfo(Global.userToken, shopID, cardID);
                }
            }
        }
        // Release the wake lock provided by the WakefulBroadcastReceiver.
        GcmBroadcastReceiver.completeWakefulIntent(intent);
    }

    // Put the message into a notification and post it.
    // This is just one simple example of what you might choose to do with
    // a GCM message.
    private void sendNotification(String msg, boolean isComplete) {
        mNotificationManager = (NotificationManager)
                this.getSystemService(Context.NOTIFICATION_SERVICE);

        NotificationCompat.Builder mBuilder = null;
        if(!isComplete){
            mBuilder = new NotificationCompat.Builder(this)
                    .setSmallIcon(R.drawable.ic_bag)
                    .setContentTitle("Error!")
                    .setStyle(new NotificationCompat.BigTextStyle()
                            .bigText(msg.toString()))
                    .setContentText(msg.toString());
        }else{
            if (msg.equals("You've received a register request")) {

                mBuilder = new NotificationCompat.Builder(this)
                        .setSmallIcon(R.drawable.ic_bag)
                        .setContentTitle(mCard.getName())
                        .setStyle(new NotificationCompat.BigTextStyle()
                                .bigText(msg.toString()))
                        .setContentText(msg);

                Intent i = new Intent(this, CardDetailActivity.class);
                i.putExtra(Global.CARD_ID, mCard.getId());
                i.putExtra(Global.TAB_INDEX, Global.CARD_DETAIL_REGISTER_TAB);
                i.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                PendingIntent contentIntent = PendingIntent.getActivity(this, NOTIFICATION_ID, i, PendingIntent.FLAG_ONE_SHOT);
                mBuilder.setContentIntent(contentIntent);
            }else{
                Intent i = new Intent(this, ShopDetailActivity.class);
                i.putExtra(Global.TAB_INDEX, 4);
                i.putExtra(Global.SHOP_OBJECT, mShop);
                Log.e("send notification", mShop.getName());
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
