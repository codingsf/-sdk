#APP_STL := gnustl_shared
#APP_STL := stlport_static
APP_STL := system
APP_CPPFLAGS := -fexceptions -frtti
#arm64-v8a armeabi armeabi-v7a x86
APP_ABI := armeabi armeabi-v7a x86 arm64-v8a
#APP_CPPFLAGS += -std=c++11

APP_PLATFORM := android-8