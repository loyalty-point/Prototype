#include <string.h>
#include <jni.h>

jstring Java_com_thesis_dont_loyaltypointuser_apis_GCMHelper_getUploadRegInfo(JNIEnv* env, jobject thiz)
{
    return (*env)->NewStringUTF(env, "http://104.155.233.34/web_service/GCM/customer_upload_reg_info.php");
}