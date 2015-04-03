#include <string.h>
#include <jni.h>

jstring Java_com_thesis_dont_loyaltypointuser_models_EventModel_getAddEvent(JNIEnv* env, jobject thiz)
{
    return (*env)->NewStringUTF(env, "http://104.155.216.164/web_service/event/add_event.php");
}

jstring Java_com_thesis_dont_loyaltypointuser_models_EventModel_getCustomerGetListEvents(JNIEnv* env, jobject thiz)
{
    return (*env)->NewStringUTF(env, "http://104.155.216.164/web_service/event/customer_get_list_events.php");
}

jstring Java_com_thesis_dont_loyaltypointuser_models_EventModel_getEditEvent(JNIEnv* env, jobject thiz)
{
    return (*env)->NewStringUTF(env, "http://104.155.216.164/web_service/event/edit_event.php");
}
