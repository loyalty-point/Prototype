#include <string.h>
#include <jni.h>

jstring Java_com_thesis_dont_loyaltypointuser_models_ShopModel_getGetUnfollowedShop(JNIEnv* env, jobject thiz)
{
    return (*env)->NewStringUTF(env, "http://104.155.233.34/web_service/customer_shop/select_not_followed_shop.php");
}

jstring Java_com_thesis_dont_loyaltypointuser_models_ShopModel_getFollowShop(JNIEnv* env, jobject thiz)
{
    return (*env)->NewStringUTF(env, "http://104.155.233.34/web_service/customer_card/add_customer_card.php");
}

jstring Java_com_thesis_dont_loyaltypointuser_models_ShopModel_getCustomerGetShopInfo(JNIEnv* env, jobject thiz)
{
    return (*env)->NewStringUTF(env, "http://104.155.233.34/web_service/shop/customer_select_shop.php");
}

jstring Java_com_thesis_dont_loyaltypointuser_models_ShopModel_getGetNumUserEventAward(JNIEnv* env, jobject thiz)
{
    return (*env)->NewStringUTF(env, "http://104.155.233.34/web_service/customer_shop/get_sum_user_event_award.php");
}

jstring Java_com_thesis_dont_loyaltypointuser_models_ShopModel_getGetNewestUserEventAward(JNIEnv* env, jobject thiz)
{
    return (*env)->NewStringUTF(env, "http://104.155.233.34/web_service/customer_shop/get_newest_user_event_award.php");
}
