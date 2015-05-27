#include <string.h>
#include <jni.h>

jstring Java_com_thesis_dont_loyaltypointuser_models_CardModel_getGetListCards(JNIEnv* env, jobject thiz)
{
    return (*env)->NewStringUTF(env, "http://104.155.233.34/web_service/customer_card/get_followed_cards.php");
}

jstring Java_com_thesis_dont_loyaltypointuser_models_CardModel_getGetListShop(JNIEnv* env, jobject thiz)
{
    return (*env)->NewStringUTF(env, "http://104.155.233.34/web_service/customer_card/get_list_shops.php");
}

jstring Java_com_thesis_dont_loyaltypointuser_models_CardModel_getGetListEvents(JNIEnv* env, jobject thiz)
{
    return (*env)->NewStringUTF(env, "http://104.155.233.34/web_service/customer_card/get_list_event.php");
}

jstring Java_com_thesis_dont_loyaltypointuser_models_CardModel_getGetListAwards(JNIEnv* env, jobject thiz)
{
    return (*env)->NewStringUTF(env, "http://104.155.233.34/web_service/customer_card/get_list_award.php");
}

jstring Java_com_thesis_dont_loyaltypointuser_models_CardModel_getGetCardInfo(JNIEnv* env, jobject thiz)
{
    return (*env)->NewStringUTF(env, "http://104.155.233.34/web_service/customer_card/get_card_info.php");
}