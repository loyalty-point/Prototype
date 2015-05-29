#include <string.h>
#include <jni.h>

jstring Java_com_thesis_dont_loyaltypointadmin_models_CardModel_getGetListCards(JNIEnv* env, jobject thiz)
{
    return (*env)->NewStringUTF(env, "http://104.155.233.34/web_service/card/get_all_cards.php");
}

jstring Java_com_thesis_dont_loyaltypointadmin_models_CardModel_getCreateCard(JNIEnv* env, jobject thiz)
{
    return (*env)->NewStringUTF(env, "http://104.155.233.34/web_service/card/add_card.php");
}

jstring Java_com_thesis_dont_loyaltypointadmin_models_CardModel_getGetListShop(JNIEnv* env, jobject thiz)
{
    return (*env)->NewStringUTF(env, "http://104.155.233.34/web_service/card/get_list_shops.php");
}

jstring Java_com_thesis_dont_loyaltypointadmin_models_CardModel_getGetListEvents(JNIEnv* env, jobject thiz)
{
    return (*env)->NewStringUTF(env, "http://104.155.233.34/web_service/card/get_list_event.php");
}

jstring Java_com_thesis_dont_loyaltypointadmin_models_CardModel_getGetListAwards(JNIEnv* env, jobject thiz)
{
    return (*env)->NewStringUTF(env, "http://104.155.233.34/web_service/card/get_list_award.php");
}

jstring Java_com_thesis_dont_loyaltypointadmin_models_CardModel_getGetFollowingUsers(JNIEnv* env, jobject thiz)
{
    return (*env)->NewStringUTF(env, "http://104.155.233.34/web_service/admin_card/get_followed_users.php");
}

jstring Java_com_thesis_dont_loyaltypointadmin_models_CardModel_getGetCardInfo(JNIEnv* env, jobject thiz)
{
    return (*env)->NewStringUTF(env, "http://104.155.233.34/web_service/card/get_card_info.php");
}

jstring Java_com_thesis_dont_loyaltypointadmin_models_CardModel_getCreateEvent(JNIEnv* env, jobject thiz)
{
    return (*env)->NewStringUTF(env, "http://104.155.233.34/web_service/card/create_event.php");
}
