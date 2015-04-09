#include <string.h>
#include <jni.h>

jstring Java_com_thesis_dont_loyaltypointuser_models_AwardModel_getCreateAward(JNIEnv* env, jobject thiz)
{
    return (*env)->NewStringUTF(env, "http://104.155.233.34/web_service/award/add_award.php");
}

jstring Java_com_thesis_dont_loyaltypointuser_models_AwardModel_getCustomerGetListAwards(JNIEnv* env, jobject thiz)
{
    return (*env)->NewStringUTF(env, "http://104.155.233.34/web_service/award/customer_get_list_awards.php");
}

jstring Java_com_thesis_dont_loyaltypointuser_models_AwardModel_getEditAward(JNIEnv* env, jobject thiz)
{
    return (*env)->NewStringUTF(env, "http://104.155.233.34/web_service/award/edit_award.php");
}

jstring Java_com_thesis_dont_loyaltypointuser_models_AwardModel_getEditAward(JNIEnv* env, jobject thiz)
{
    return (*env)->NewStringUTF(env, "http://104.155.233.34/web_service/award/edit_award.php");
}