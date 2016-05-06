LOCAL_PATH := $(call my-dir)

include $(CLEAR_VARS)
LOCAL_MODULE := lua
LOCAL_SRC_FILES := lapi.c lcode.c lctype.c ldebug.c ldo.c ldump.c lfunc.c lgc.c llex.c lmem.c lobject.c lopcodes.c lparser.c lstate.c lstring.c ltable.c ltm.c lundump.c lvm.c lzio.c lauxlib.c lbaselib.c lbitlib.c lcorolib.c ldblib.c liolib.c lmathlib.c loslib.c lstrlib.c ltablib.c lutf8lib.c loadlib.c linit.c lua.c

# Auxiliary lua user defined file
# LOCAL_SRC_FILES += luauser.c
# LOCAL_CFLAGS := -DLUA_DL_DLOPEN -DLUA_USER_H='"luauser.h"'

LOCAL_CFLAGS := -DLUA_DL_DLOPEN -DLUA_USE_C89
LOCAL_LDLIBS += -L$(SYSROOT)/usr/lib -llog -ldl
LOCAL_CFLAGS += -pie -fPIE
LOCAL_LDFLAGS += -pie -fPIE

include $(BUILD_STATIC_LIBRARY)