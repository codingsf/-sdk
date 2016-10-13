/*
 * logutil.h
 *
 *  Created on: 2016-1-22
 *      Author: fyq
 */

#ifndef LOGUTIL_H_
#define LOGUTIL_H_

#include <android/log.h>

#define TAG "jni_log"

//#define logi(...) __android_log_print(ANDROID_LOG_INFO, TAG,__VA_ARGS__)

#define LOGI(...) if(1) __android_log_print(ANDROID_LOG_INFO, TAG,__VA_ARGS__)

#endif /* LOGUTIL_H_ */
