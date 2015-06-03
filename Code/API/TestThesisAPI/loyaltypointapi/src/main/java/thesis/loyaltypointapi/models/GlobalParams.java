package thesis.loyaltypointapi.models;

import java.util.HashMap;

import thesis.loyaltypointapi.apis.LoyaltyPointAPI;
import thesis.loyaltypointapi.apis.LoyaltyPointAPI.*;

/**
 * Created by 11120_000 on 22/05/15.
 */
public class GlobalParams {
    public static final String LOGIN_STATE = "login_state";
    public static final String TOKEN = "token";
    public static final String FROM = "from";
    public static final String GET_TOKEN_CALLBACK_KEY = "get_token_callback";
    public static final int GET_TOKEN_REQUEST_CODE = 1006;

    public static String appPackageName = "com.thesis.dont.loyaltypointadmin";

    public static HashMap<Integer, OnGetTokenResult> mapCallbacks = new HashMap<Integer, OnGetTokenResult>();

    public static String calculatePointLink = "http://104.155.233.34/apis/calculate_point.php";
    public static String updatePointLink = "http://104.155.233.34/apis/update_point.php";
    public static String getListShops = "http://104.155.233.34/apis/get_list_shops.php";
    public static String getListCards = "http://104.155.233.34/apis/get_list_cards.php";
    public static String getListEvents = "http://104.155.233.34/apis/get_list_events.php";
    public static String getListCustomers = "http://104.155.233.34/apis/get_list_customers.php";

    public static void removeCallbackItem(OnGetTokenResult item) {
        mapCallbacks.remove(item);
    }

    public static void addCallbackItem(int key, OnGetTokenResult onGetTokenResult) {
        mapCallbacks.put(key, onGetTokenResult);
    }
}
