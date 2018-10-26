LOCAL_PATH := $(call my-dir)
include $(CLEAR_VARS)

LOCAL_MODULE     := libriru_ibr
LOCAL_C_INCLUDES := \
	$(LOCAL_PATH) \
	$(LOCAL_PATH)/../external/xhook
LOCAL_CPPFLAGS += $(CPPFLAGS)
LOCAL_STATIC_LIBRARIES := xhook
LOCAL_LDLIBS += -ldl -llog
LOCAL_LDFLAGS := -Wl

LOCAL_SRC_FILES:= main.c hook.c

include $(BUILD_SHARED_LIBRARY)
