#include <string.h>
#include <jni.h>

jstring Java_com_thesis_dont_loyaltypointadmin_models_ShopModel_getCreateShop(JNIEnv* env, jobject thiz)
{
    return (*env)->NewStringUTF(env, "http://104.155.233.34/web_service/shop/add_shop.php");
}
jstring Java_com_thesis_dont_loyaltypointadmin_models_ShopModel_getGetListShop(JNIEnv* env, jobject thiz)
{
    return (*env)->NewStringUTF(env, "http://104.155.233.34/web_service/shop/select_shop_list.php");
}

jstring Java_com_thesis_dont_loyaltypointadmin_models_ShopModel_getGetShopInfo(JNIEnv* env, jobject thiz)
{
    return (*env)->NewStringUTF(env, "http://104.155.233.34/web_service/shop/select_shop.php");
}

jstring Java_com_thesis_dont_loyaltypointadmin_models_ShopModel_getEditShopInfo(JNIEnv* env, jobject thiz)
{
    return (*env)->NewStringUTF(env, "http://104.155.233.34/web_service/shop/edit_shop.php");
}

jstring Java_com_thesis_dont_loyaltypointadmin_models_ShopModel_getGetFollowingUsers(JNIEnv* env, jobject thiz)
{
    return (*env)->NewStringUTF(env, "http://104.155.233.34/web_service/shop/get_following_users.php");
}

jstring Java_com_thesis_dont_loyaltypointadmin_models_ShopModel_getGetCustomerInfo(JNIEnv* env, jobject thiz)
{
    return (*env)->NewStringUTF(env, "http://104.155.233.34/web_service/shop/get_customer_info.php");
}

jstring Java_com_thesis_dont_loyaltypointadmin_models_ShopModel_getUpdatePoint(JNIEnv* env, jobject thiz)
{
    return (*env)->NewStringUTF(env, "http://104.155.233.34/web_service/shop/update_point.php");
}

jstring Java_com_thesis_dont_loyaltypointadmin_models_ShopModel_getUpdateBackground(JNIEnv* env, jobject thiz)
{
    return (*env)->NewStringUTF(env, "http://104.155.233.34/web_service/shop/edit_background.php");
}
jstring Java_com_thesis_dont_loyaltypointadmin_models_ShopModel_getGetListRegisters(JNIEnv* env, jobject thiz)
{
    return (*env)->NewStringUTF(env, "http://104.155.233.34/web_service/shop/get_list_registers.php");
}

jstring Java_com_thesis_dont_loyaltypointadmin_models_ShopModel_getAcceptRegisterRequest(JNIEnv* env, jobject thiz)
{
    return (*env)->NewStringUTF(env, "http://104.155.233.34/web_service/shop/accept_register_request.php");
}
