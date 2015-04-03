#include <string.h>
#include <jni.h>

jstring Java_com_thesis_dont_loyaltypointadmin_models_EventModel_getAddEvent(JNIEnv* env, jobject thiz)
{
    return (*env)->NewStringUTF(env, "http://104.155.216.164/web_service/event/add_event.php");
}

jstring Java_com_thesis_dont_loyaltypointadmin_models_EventModel_getGetListEvents(JNIEnv* env, jobject thiz)
{
    return (*env)->NewStringUTF(env, "http://104.155.216.164/web_service/event/get_list_events.php");
}

jstring Java_com_thesis_dont_loyaltypointadmin_models_EventModel_getEditEvent(JNIEnv* env, jobject thiz)
{
    return (*env)->NewStringUTF(env, "http://104.155.216.164/web_service/event/edit_event.php");
}

jstring Java_com_thesis_dont_loyaltypointadmin_models_EventModel_getCalculatePoint(JNIEnv* env, jobject thiz)
{
    return (*env)->NewStringUTF(env, "http://104.155.216.164/web_service/event/calculate_point.php");
}
