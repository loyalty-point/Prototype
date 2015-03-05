#include <string.h>
#include <jni.h>

jstring Java_com_thesis_dont_loyaltypointadmin_models_ShopModel_getCreateShop(JNIEnv* env, jobject thiz)
{
    return (*env)->NewStringUTF(env, "http://104.155.216.164/web_service/shop/add_shop.php");
}
