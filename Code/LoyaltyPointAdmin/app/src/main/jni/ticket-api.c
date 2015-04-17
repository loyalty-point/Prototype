#include <string.h>
#include <jni.h>

jstring Java_com_thesis_dont_loyaltypointadmin_models_TicketModel_getGetUserTicket(JNIEnv* env, jobject thiz)
{
    return (*env)->NewStringUTF(env, "http://104.155.233.34/web_service/ticket/get_user_ticket.php");
}

jstring Java_com_thesis_dont_loyaltypointadmin_models_TicketModel_getDeleteUserTicket(JNIEnv* env, jobject thiz)
{
    return (*env)->NewStringUTF(env, "http://104.155.233.34/web_service/ticket/delete_user_ticket.php");
}



