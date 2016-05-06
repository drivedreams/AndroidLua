LOCAL_PATH := $(call my-dir)

include $(CLEAR_VARS)
LOCAL_LDLIBS    := -lm -llog 
LOCAL_C_INCLUDES += $(LOCAL_PATH)/../lua
LOCAL_MODULE     := luajava
LOCAL_SRC_FILES  := luajava.c
LOCAL_STATIC_LIBRARIES := lua

include $(BUILD_SHARED_LIBRARY)
