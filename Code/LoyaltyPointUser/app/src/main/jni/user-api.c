#include <string.h>
#include <jni.h>

jstring Java_com_thesis_dont_loyaltypointuser_models_UserModel_getAddUser(JNIEnv* env, jobject thiz)
{
    return (*env)->NewStringUTF(env, "http://104.155.216.164/web_service/customer_user/add_user.php");
}

jstring Java_com_thesis_dont_loyaltypointuser_models_UserModel_getCheckUser(JNIEnv* env, jobject thiz)
{
    return (*env)->NewStringUTF(env, "http://104.155.216.164/web_service/customer_user/check_user.php");
}

jstring Java_com_thesis_dont_loyaltypointuser_models_UserModel_getGetUserInfo(JNIEnv* env, jobject thiz)
{
    return (*env)->NewStringUTF(env, "http://104.155.216.164/web_service/customer_user/get_user_info.php");
}