LOCAL_PATH:= $(call my-dir)

include $(CLEAR_VARS)

LOCAL_MODULE    := cdata
LOCAL_CFLAGS    := -Werror
LOCAL_SRC_FILES := cdata.c  crc32/crc32.c
LOCAL_LDLIBS    := -llog 

include $(BUILD_SHARED_LIBRARY)
