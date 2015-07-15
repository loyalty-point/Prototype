#include <string.h>
#include <jni.h>

jstring Java_com_thesis_dont_loyaltypointadmin_models_CustomerModel_getGetListHistory(JNIEnv* env, jobject thiz)
{
    return (*env)->NewStringUTF(env, "http://104.155.233.34/web_service/customer_user/get_history_shop_list.php");
}

jstring Java_com_thesis_dont_loyaltypointadmin_models_CustomerModel_getGetListHistoryCard(JNIEnv* env, jobject thiz)
{
    return (*env)->NewStringUTF(env, "http://104.155.233.34/web_service/customer_user/get_history_card_list.php");
}
