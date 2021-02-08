LOCAL_PATH := $(call my-dir)
MY_PATH := $(LOCAL_PATH)
BUILD_EXECUTABLE := $(CLEAR_VARS)

include $(call all-subdir-makefiles)
