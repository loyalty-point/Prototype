package models;

import android.content.Intent;

import java.util.HashMap;

import apis.LoyaltyPointAPI.*;

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
}
