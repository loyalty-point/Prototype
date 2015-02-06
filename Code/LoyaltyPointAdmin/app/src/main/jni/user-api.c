#include <string.h>
#include <jni.h>

char* host = "http://104.155.216.164/web_service/user/";

jstring Java_com_thesis_dont_loyaltypointadmin_models_UserModel_getAddUser(JNIEnv* env, jobject thiz)
{
//    char* src = "add_user.php";
//
//    char* dest = (char*)malloc(strlen(host) + strlen(src));
//
//    strcpy(dest, host);
//    strcat(dest, src);
//    jstring result = (*env)->NewStringUTF(env, dest);
//
//    free(dest);
//    return result;
    return (*env)->NewStringUTF(env, "http://104.155.216.164/web_service/user/add_user.php");
}

jstring Java_com_thesis_dont_loyaltypointadmin_models_UserModel_getCheckUser(JNIEnv* env, jobject thiz)
{
    return (*env)->NewStringUTF(env, "http://104.155.216.164/web_service/user/check_user.php");
}

jstring Java_com_thesis_dont_loyaltypointadmin_models_UserModel_getSelectUser(JNIEnv* env, jobject thiz)
{
    return (*env)->NewStringUTF(env, "http://104.155.216.164/web_service/user/select_user.php");
}