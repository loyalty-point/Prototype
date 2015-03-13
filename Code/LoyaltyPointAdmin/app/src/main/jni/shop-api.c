#include <string.h>
#include <jni.h>

jstring Java_com_thesis_dont_loyaltypointadmin_models_ShopModel_getCreateShop(JNIEnv* env, jobject thiz)
{
    return (*env)->NewStringUTF(env, "http://104.155.216.164/web_service/shop/add_shop.php");
}
jstring Java_com_thesis_dont_loyaltypointadmin_models_ShopModel_getGetListShop(JNIEnv* env, jobject thiz)
{
    return (*env)->NewStringUTF(env, "http://104.155.216.164/web_service/shop/select_shop_list.php");
}

jstring Java_com_thesis_dont_loyaltypointadmin_models_ShopModel_getGetShopInfo(JNIEnv* env, jobject thiz)
{
    return (*env)->NewStringUTF(env, "http://104.155.216.164/web_service/shop/select_shop.php");
}

jstring Java_com_thesis_dont_loyaltypointadmin_models_ShopModel_getEditShopInfo(JNIEnv* env, jobject thiz)
{
    return (*env)->NewStringUTF(env, "http://104.155.216.164/web_service/shop/edit_shop.php");
}
