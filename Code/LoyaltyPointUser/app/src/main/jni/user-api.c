#include <string.h>
#include <jni.h>

jstring Java_com_thesis_dont_loyaltypointuser_models_UserModel_getAddUser(JNIEnv* env, jobject thiz)
{
    
    return (*env)->NewStringUTF(env, "http://104.155.233.34/web_service/customer_user/add_user.php");
}

jstring Java_com_thesis_dont_loyaltypointuser_models_UserModel_getCheckUser(JNIEnv* env, jobject thiz)
{
    
    return (*env)->NewStringUTF(env, "http://104.155.233.34/web_service/customer_user/check_user.php");
}

jstring Java_com_thesis_dont_loyaltypointuser_models_UserModel_getGetUserInfo(JNIEnv* env, jobject thiz)
{
    
    return (*env)->NewStringUTF(env, "http://104.155.233.34/web_service/customer_user/get_user_info.php");
}

jstring Java_com_thesis_dont_loyaltypointuser_models_UserModel_getGetMyAwards(JNIEnv* env, jobject thiz)
{
    return (*env)->NewStringUTF(env, "http://104.155.233.34/web_service/customer_user/get_my_awards.php");
}

jstring Java_com_thesis_dont_loyaltypointuser_models_UserModel_getGetListHistory(JNIEnv* env, jobject thiz)
{
    return (*env)->NewStringUTF(env, "http://104.155.233.34/web_service/customer_shop/get_list_history.php");
}

jstring Java_com_thesis_dont_loyaltypointuser_models_UserModel_getGetEventHistory(JNIEnv* env, jobject thiz)
{
    return (*env)->NewStringUTF(env, "http://104.155.233.34/web_service/customer_shop/get_event_history.php");
}

jstring Java_com_thesis_dont_loyaltypointuser_models_UserModel_getGetAwardHistory(JNIEnv* env, jobject thiz)
{
    return (*env)->NewStringUTF(env, "http://104.155.233.34/web_service/customer_shop/get_award_history.php");
}

jstring Java_com_thesis_dont_loyaltypointuser_models_UserModel_getGetHistory(JNIEnv* env, jobject thiz)
{
    return (*env)->NewStringUTF(env, "http://104.155.233.34/web_service/customer_shop/get_history.php");
}


