#include <string.h>
#include <jni.h>

jstring Java_com_thesis_dont_loyaltypointadmin_models_UserModel_getAddUser(JNIEnv* env, jobject thiz)
{
    return (*env)->NewStringUTF(env, "http://104.155.233.34/web_service/admin_user/add_user.php");
}

jstring Java_com_thesis_dont_loyaltypointadmin_models_UserModel_getCheckUser(JNIEnv* env, jobject thiz)
{
    return (*env)->NewStringUTF(env, "http://104.155.233.34/web_service/admin_user/check_user.php");
}

jstring Java_com_thesis_dont_loyaltypointadmin_models_UserModel_getSelectUser(JNIEnv* env, jobject thiz)
{
    return (*env)->NewStringUTF(env, "http://104.155.233.34/web_service/admin_user/select_user.php");
}

jstring Java_com_thesis_dont_loyaltypointadmin_models_UserModel_getCheckIdentityNumberUser(JNIEnv* env, jobject thiz)
{
    return (*env)->NewStringUTF(env, "http://104.155.233.34/web_service/admin_user/check_user_identity_number.php");
}

jstring Java_com_thesis_dont_loyaltypointadmin_models_UserModel_getGetListHistory(JNIEnv* env, jobject thiz)
{
    return (*env)->NewStringUTF(env, "http://104.155.233.34/web_service/admin_shop/get_list_history.php");
}

jstring Java_com_thesis_dont_loyaltypointadmin_models_UserModel_getGetEventHistory(JNIEnv* env, jobject thiz)
{
    return (*env)->NewStringUTF(env, "http://104.155.233.34/web_service/admin_shop/get_event_history.php");
}

jstring Java_com_thesis_dont_loyaltypointadmin_models_UserModel_getGetAwardHistory(JNIEnv* env, jobject thiz)
{
    return (*env)->NewStringUTF(env, "http://104.155.233.34/web_service/admin_shop/get_award_history.php");
}
