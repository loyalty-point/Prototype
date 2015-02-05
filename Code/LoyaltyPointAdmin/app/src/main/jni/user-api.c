#include <string.h>
#include <jni.h>

char* host = "http://104.155.216.164/web_service/user/";

jstring Java_com_thesis_dont_loyaltypointadmin_models_UserModel_getAddUser(JNIEnv* env, jobject thiz)
{
    char* src = "add_user.php";

    char* dest = (char*)malloc(strlen(host) + strlen(src));

    strcpy(dest, host);
    strcat(dest, src);
    jstring result = (*env)->NewStringUTF(env, dest);

    free(dest);
    return result;
}