LOCAL_PATH := $(call my-dir)

include $(CLEAR_VARS)

LOCAL_MODULE    := cwcp
LOCAL_SRC_FILES := cwcp.cpp


LOCAL_LDLIBS := -llog

include $(BUILD_SHARED_LIBRARY)
