package com.thesis.dont.loyaltypointadmin.models;

import android.app.Activity;
import android.graphics.Bitmap;

/**
 * Created by 11120_000 on 07/03/15.
 */
public class Global {
    public static final String FROM = "from";
    public static final String TOKEN = "token";
    public static String AWARD_OBJECT ="award_object";
    public static String CARD_OBJECT ="card_object";
    public static String userToken = null;
    public static String regID = null;
    public static String CARD_ID = "card_id";
    public static String OBJECT = "object";
    public static String SHOP_NAME = "shop_name";
    public static String SHOP_ID = "shop_id";
    public static String SHOP_OBJECT = "shop_object";
    public static String SHOP_LIST_OBJECT = "shop_list_object";
    public static String USER_OBJECT = "user_object";
    public static String USER_NAME = "user_name";
    public static String USER_POINT = "user_point";
    public static String USER_FULLNAME = "user_fullname";
    public static String HISTORY_OBJECT = "history_object";
    public static String PRODUCT_LIST = "prodcut_list";
    public static String CUSTOMER_LIST = "user_list";
    public static String BARCODE = "barcode";
    public static String EVENT_OBJECT = "event_object";
    public static String EVENT_LIST_TYPE = "event_list_type";
    public static String AWARD_LIST_TYPE = "award_list_type";
    public static String USER_INFO_TYPE = "user_info_type";
    public static String USER_INFO_REGISTER = "user_info_register";
    public static String USER_INFO = "user_info";
    public static String BITMAP = "bitmap";
    public static int CARD_DETAIL_REGISTER_TAB = 4;
    public static final String TAB_INDEX = "tab_index";

    public static String TOTAL_MONEY = "total_money";

    public static final int SELECT_PHOTO = 100;


    public static final int SCAN_BARCODE = 49374;
    public static final int CAMERA_REQUEST = 49375;

    public static Activity tempActivity;
    public static Bitmap tempBitmap;

    public static int CARD_CREATE_EVENT_LIST = 0;
    public static int CARD_EDIT_EVENT_LIST = 1;

    public static int CARD_CREATE_AWARD_LIST = 0;
    public static int CARD_EDIT_AWARD_LIST = 1;
}
