LOCAL_PATH := $(call my-dir)

include $(CLEAR_VARS)

LOCAL_MODULE    := services
LOCAL_SRC_FILES := user-api.c shop-api.c event-api.c award-api.c gcm-api.c ticket-api.c

include $(BUILD_SHARED_LIBRARY)