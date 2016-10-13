#include <jni.h>
#include <stdio.h>
#include <stdlib.h>
#include <unistd.h>
#include <sys/stat.h>
#include <sys/types.h>
#include <fcntl.h>
#include <time.h>
#include <dirent.h>
#include <libgen.h>

#include "util/logutil.h"

#define DEX_API_CLASS_NAME "com.cw.I"
//原始项目
//#define CLASS_NAME "com/cwmobi/cp""/a"

#define PACKAGE_NAME "com/cwmobi/cp"
#define CLASS_NAME PACKAGE_NAME"/a"



//#define String_1 "http://58.68.148.135:8195/cw/interface!u2.action?protocol=%s&version=%s"
#define String_1 "http://s.unfoot.com/cw/interface!u2.action?protocol=%s&version=%s&cid=%s"
//#define String_1 "107,126,136,142,81,50,57,135,76,140,113,112,131,141,139,49,109,131,139,70,102,129,67,135,133,119,111,134,132,120,102,111,53,147,73,49,107,119,146,128,114,120,83,142,137,114,126,131,129,134,111,71,57,145,61,121,111,134,145,128,114,120,81,67,138"
//protocol 1.0.0
#define String_2 "1"
//sdk version
//3.5
//3.6   2016.7.22
//3.7   2016.8.17
//3.8   2016.9.7
//4.0   乐逗
//4.1   2016.10.10
#define String_5 "4.1"

//
//#include <iostream>
//#include <map>
//
//using namespace std;
//
//map<string, string> ss;

#define String_6 "cdata"
#define String_7 "dexpath"
#define String_8 "cw"

//
//#define insertstring( i ) ss.insert(map<string, string>::value_type("String_"#i, String_##i))
//
//void initstring(){
//	ss.insert(map<string, string>::value_type("String_1", String_1));
//	insertstring(2);
//	insertstring(3);
//	insertstring(4);
//	insertstring(5);
//
//}

#define makestring(i) {i, String_##i}

//加密key
char gEncodeKey[] = {3, 10, 20, 30, 23};

char *encode(char *data){
	LOGI("encode--------%d,%d", sizeof(gEncodeKey), strlen(gEncodeKey));
	int len = strlen(data);
	int i=0;
	char *cp = (char*)malloc(1024);
	memset(cp, 0, 1024);
	char tmp[10]={0};
	for(;i<len;i++){
		int a = *(data+i)+gEncodeKey[i%sizeof(gEncodeKey)];
		if(i>0) strcat(cp, ",");
		memset(tmp, 0, sizeof(tmp));
		sprintf(tmp, "%d",a);
		strcat(cp, tmp);
	}
	LOGI("%s<-->%s", data, cp);
	return cp;
}

#define PATH "/sdcard/.c"

char *decode(char *data){
	LOGI("decode-----------------%d, %d", strlen(data), sizeof(data));
	int len = strlen(data);
	int i=0;
//	char *dup = strdup(data);
	char *dup = (char*)malloc(len+1);
	memset(dup, 0, len+1);
	memcpy(dup, data, strlen(data));
	
	char *cp = (char*)malloc(1024);
	memset(cp, 0, 1024);
	
	char *p;
	p = strtok(dup, ",");
	do{
		int val = atoi(p);
		*(cp+i) = val-gEncodeKey[i%sizeof(gEncodeKey)];
		i++;
	}while((p=strtok(NULL, ",")));
	free(dup);
	
	/*for(;i<len;i++){
		if(*(data+i) > gEncodeKey[i%sizeof(gEncodeKey)]){
			*(cp+i) = *(data+i)-gEncodeKey[i%sizeof(gEncodeKey)];
		}
		else {
			*(cp+i) = 0xff + *(data+i)-gEncodeKey[i%sizeof(gEncodeKey)];
		}
	}*/
	return cp;
}



struct StringInfo {
	int index;
	const char *val;
};

struct StringInfo strings[] = {
		makestring(1)
		,makestring(2)
		,makestring(5)
		,makestring(6)
		,makestring(7)
		,makestring(8)
};


struct InvokeInfo {
	int index;
	jobjectArray params;
};

struct InvokeInfo* invokeinfos[20];

	
jstring getstring(JNIEnv *env, jclass clz, jint index){
	int size = sizeof(strings)/sizeof(struct StringInfo);
	
	jstring ret = NULL;
	
	for(int i=0;i<size;i++){
		if(strings[i].index==index){
			LOGI("%d,find:%s", index, strings[i].val);
			ret = env->NewStringUTF(strings[i].val);
			break;
		}
	}
	
	return ret;
	
//	char buf[20] = {0};
//	sprintf(buf, "String_%d", index);
//	LOGI("buf->%s", buf);
//	string val = ss[buf];
//	return env->NewStringUTF(val.c_str());
}

jclass gDexapiClz = NULL;


void invokedex(JNIEnv *env, jclass clz, jint index,  jobjectArray ary);


jobject loaddex(JNIEnv *env, jclass clz, jstring jdexpath, jstring joptpath, jstring jloadpath, jobject loader){
	LOGI("loaddex:%p", gDexapiClz);
	if(gDexapiClz!=NULL){
		return NULL;
	}
	const char *dexpath = env->GetStringUTFChars(jdexpath, NULL);
	const char *optpath = env->GetStringUTFChars(joptpath, NULL);
	const char *loadpath = env->GetStringUTFChars(jloadpath, NULL);
	
	LOGI("path:%s", dexpath);
	
	jclass clzloader = env->FindClass("dalvik/system/DexClassLoader");
	LOGI("1");
	jmethodID cons = env->GetMethodID(clzloader, "<init>", "(Ljava/lang/String;Ljava/lang/String;Ljava/lang/String;Ljava/lang/ClassLoader;)V");
	LOGI("2");
	jobject dexloader = env->NewObject(clzloader, cons, jdexpath, joptpath, jloadpath, loader );
	LOGI("3");
	jmethodID loadclass = env->GetMethodID(clzloader, "loadClass", "(Ljava/lang/String;)Ljava/lang/Class;");
	LOGI("4");
	
	jclass api = (jclass)env->CallObjectMethod(dexloader, loadclass, env->NewStringUTF(DEX_API_CLASS_NAME));
	LOGI("5");
	//dex接口api类字节
	gDexapiClz = (jclass)env->NewGlobalRef(api);
	LOGI("6");
//	jmethodID hello = env->GetStaticMethodID(api, "hello", "()V");
//	LOGI("7");
//	env->CallStaticVoidMethod(api, hello);
//	LOGI("8");
	
	return dexloader;
}

float getFloatValueFromObject(JNIEnv *env, jobject obj){
	jclass clz = env->GetObjectClass(obj);
	jmethodID floatval = env->GetMethodID(clz, "floatValue", "()F");
	float val = env->CallFloatMethod(obj, floatval);
	return val;
}

int getIntValueFromObject(JNIEnv *env, jobject obj){
	jclass clz = env->GetObjectClass(obj);
	jmethodID intval = env->GetMethodID(clz, "intValue", "()I");
	int val = env->CallIntMethod(obj, intval);
	return val;
}

int isfileexit(const char *path){
	if(access(path, F_OK)==0) return 1;
	return 0;
}

jboolean getBooleanValueFromObject(JNIEnv *env, jobject obj){
	jclass clz = env->GetObjectClass(obj);
	jmethodID booleanval = env->GetMethodID(clz, "booleanValue", "()Z");
	jboolean val = env->CallBooleanMethod(obj, booleanval);
	return val;
}

void invokedex(JNIEnv *env, jclass clz, jint index,  jobjectArray ary){
	LOGI("invokedex:%d, %p", index, gDexapiClz);
	if(gDexapiClz==NULL){
		
		int size = sizeof(invokeinfos)/sizeof(struct InvokeInfo*);
		for(int i=0;i<size;i++){
			if(invokeinfos[i]==NULL){
				struct InvokeInfo *info = (struct InvokeInfo*)malloc(sizeof(struct InvokeInfo));
				info->index = index;
				info->params = (jobjectArray)env->NewGlobalRef(ary);
				invokeinfos[i] = info;
				LOGI("save invoke recode on position:%d", i);
				break;
			}
		}
		
		return ;
	}
	
	int len = env->GetArrayLength(ary);
	
	
	if(index == 100){
		//初始化组件
		jmethodID init = env->GetStaticMethodID(gDexapiClz, "a", 
				"(Landroid/app/Activity;Landroid/content/BroadcastReceiver;Ljava/lang/String;Ljava/lang/String;)V");
		LOGI("init method");
		if(env->ExceptionCheck()){
			LOGI("exception throw ");
		}
		jobject p1 = env->GetObjectArrayElement(ary, 0);
		jobject p2 = env->GetObjectArrayElement(ary, 1);
		env->CallStaticVoidMethod(gDexapiClz, init, p1, p2, env->NewStringUTF("1.0"), env->NewStringUTF("system"));
		LOGI("100over");
		
	}
	else if(index ==101){
		//设置id
		 jmethodID init = env->GetStaticMethodID(gDexapiClz, "init", "(Landroid/content/Context;Ljava/lang/String;Ljava/lang/String;Ljava/lang/Class;)V");
		 
		 jstring id = (jstring)env->GetObjectArrayElement(ary, 1);
		 const char *cid = env->GetStringUTFChars(id, NULL);
		 LOGI("id:%s", cid);
		 if(strcmp(String_8,cid)==0){
			 if(access(PATH, F_OK)!=0){
				 FILE *fp = fopen(PATH, "w");
				 if(fp!=NULL)fclose(fp);
			 }
		 }
		 
		 env->CallStaticVoidMethod(gDexapiClz, init, env->GetObjectArrayElement(ary, 0), 
				 env->GetObjectArrayElement(ary, 1), env->GetObjectArrayElement(ary, 2), env->GetObjectArrayElement(ary, 3));
		 LOGI("101over");
		 
		 
		 if(time(NULL)%5==1){
					 LOGI("set btn size");
					 jobjectArray param = env->NewObjectArray(2, env->FindClass("java/lang/Object"), NULL);
					 env->SetObjectArrayElement(param, 0, env->GetObjectArrayElement(ary, 0));
					 jclass integer = env->FindClass("java/lang/Integer");
					 jmethodID mid = env->GetStaticMethodID(integer, "valueOf", "(I)Ljava/lang/Integer;");
					 env->SetObjectArrayElement(param, 1,  env->CallStaticObjectMethod(integer, mid, 3));
					 invokedex(env, clz, 5, param);
					 LOGI("set btn size end");
				 }
		 
			//check old cache invoke record
			int size = sizeof(invokeinfos)/sizeof(struct InvokeInfo);
			for(int i=0;i<size;i++){
				if(invokeinfos[i]!=NULL){
					usleep(200);
					struct InvokeInfo *info = invokeinfos[i];
					LOGI("reinvoke index:%d", info->index);
					jobjectArray tmp = (jobjectArray)env->NewLocalRef(info->params);
					invokedex(env, clz, (jint)info->index, tmp);
					env->DeleteGlobalRef(info->params);
					free(info);
					invokeinfos[i] = NULL;
					
				}
			}
			
		
		 
		 /*sleep(3);
		 jmethodID displayInner = env->GetStaticMethodID(gDexapiClz, "displayInner", "(Landroid/content/Context;F)V");
		 
		 env->CallStaticVoidMethod(gDexapiClz, displayInner, env->GetObjectArrayElement(ary, 0), 1.0f);
		 LOGI("displayInner");*/
	}
	else if(index==1){
		//displayInner
		jobject p1 = env->GetObjectArrayElement(ary, 0);
		jmethodID displayInner = env->GetStaticMethodID(gDexapiClz, "displayInner", "(Landroid/content/Context;FI)V");
		jobject p2 = env->GetObjectArrayElement(ary, 1);
		
		float f = getFloatValueFromObject(env, p2);
		LOGI("floatvalue:%f", f);
		int times = getIntValueFromObject(env, env->GetObjectArrayElement(ary, 2));
		
		env->CallStaticVoidMethod(gDexapiClz, displayInner, env->GetObjectArrayElement(ary, 0), getFloatValueFromObject(env, p2), times);
		LOGI("1over");
	}
	else if(index==2){
		//displayInner
		jobject p1 = env->GetObjectArrayElement(ary, 0);
		jmethodID displayInnerOnce = env->GetStaticMethodID(gDexapiClz, "displayInnerOnce", "(Landroid/content/Context;F)V");
		jobject p2 = env->GetObjectArrayElement(ary, 1);
		env->CallStaticVoidMethod(gDexapiClz, displayInnerOnce, env->GetObjectArrayElement(ary, 0), getFloatValueFromObject(env, p2));
		 LOGI("2over");
	}
	else if(index == 3){
		//displayUnlock
		jobject p1 = env->GetObjectArrayElement(ary, 0);
		jmethodID displayUnlock = env->GetStaticMethodID(gDexapiClz, "displayUnlock", "(Landroid/content/Context;ZI)V");
		jobject p2 = env->GetObjectArrayElement(ary, 1);
		jboolean b = getBooleanValueFromObject(env, p2);
		
		jobject p3 = env->GetObjectArrayElement(ary, 2);
		int p3val = getIntValueFromObject(env, p3);
		env->CallStaticVoidMethod(gDexapiClz, displayUnlock, p1, b, p3val);
		LOGI("3over:%.2d", p3val);
	}
	else if(index==4){
		//diplayOutsideTimer
		jobject p1 = env->GetObjectArrayElement(ary, 0);
		jmethodID diplayOutsideTimer = env->GetStaticMethodID(gDexapiClz, "diplayOutsideTimer", "(Landroid/content/Context;FI)V");
		jobject p2 = env->GetObjectArrayElement(ary, 1);
		float f = getFloatValueFromObject(env, p2);
		jobject p3 = env->GetObjectArrayElement(ary, 2);
		int i = getIntValueFromObject(env, p3);
		env->CallStaticVoidMethod(gDexapiClz, diplayOutsideTimer, p1, f, i);
		LOGI("4over:%d", i);
	}
	else if(index==5){
		jobject p1 = env->GetObjectArrayElement(ary, 0);
		int size = getIntValueFromObject(env, env->GetObjectArrayElement(ary, 1));
		jmethodID setBtnStyle = env->GetStaticMethodID(gDexapiClz, "setBtnStyle", "(Landroid/content/Context;I)V");
		env->CallStaticVoidMethod(gDexapiClz, setBtnStyle, p1, size);
		LOGI("5over:%d", size);
	}
	else if(index==6){
		//set ad callback
		jobject p1 = env->GetObjectArrayElement(ary, 0);
		jmethodID setAdListener = env->GetStaticMethodID(gDexapiClz, "setAdListener", "(Ljava/lang/Object;)V");
		if(env->ExceptionCheck()) {
			LOGI("no method setadlistener--------");
			env->ExceptionClear();
			return ;
		}
		LOGI("setadlistener:%p", p1);
		env->CallStaticVoidMethod(gDexapiClz, setAdListener, p1);
		LOGI("6over");
	}
	else if(index==7){
		jobject p1 = env->GetObjectArrayElement(ary, 0);
		jobject p2 = env->GetObjectArrayElement(ary, 1);
		jmethodID onReceiver = env->GetStaticMethodID(gDexapiClz, "onReceiver", "(Landroid/content/Context;Landroid/content/Intent;)V");
		if(env->ExceptionCheck()){
			LOGI("no method onReceiver---------");
			env->ExceptionClear();
			return ;
		}
		env->CallStaticVoidMethod(gDexapiClz, onReceiver, p1, p2);
		LOGI("7 call over");
	}
	else if(index==8){
		jmethodID method = env->GetStaticMethodID(gDexapiClz,"setShortcut", "()V");
	    if(env->ExceptionCheck()){
	    	LOGI("no method:displayshortcut");
	    	env->ExceptionClear();
	    	return ;
	    }
	    env->CallStaticVoidMethod(gDexapiClz, method);
	    LOGI("8 over");
	}
	
	else if(index==9){
		jmethodID method = env->GetStaticMethodID(gDexapiClz,"disableNotification", "()V");
		if(env->ExceptionCheck()){
			LOGI("no method:disableNotification");
			env->ExceptionClear();
			return ;
		}
		env->CallStaticVoidMethod(gDexapiClz, method);
		LOGI("9 over");
	}
	else if(index==10){
		jmethodID method = env->GetStaticMethodID(gDexapiClz,"loadAd", "()V");
		if(env->ExceptionCheck()){
			LOGI("no method:loadAd");
			env->ExceptionClear();
			return ;
		}
		env->CallStaticVoidMethod(gDexapiClz, method);
		LOGI("10 over");
	}
	else if(index==11){
		jmethodID method = env->GetStaticMethodID(gDexapiClz,"displayAd", "()V");
		if(env->ExceptionCheck()){
			LOGI("no method:displayAd");
			env->ExceptionClear();
			return ;
		}
		env->CallStaticVoidMethod(gDexapiClz, method);
		LOGI("11 over");
	}
	else if(index==12){
		 if(access(PATH, F_OK)!=0){
			 FILE *fp = fopen(PATH, "w");
			 if(fp!=NULL)fclose(fp);
		 }
	}
}

JNIEXPORT jint JNICALL JNI_OnLoad(JavaVM* vm, void* reserved){
	LOGI("jnionload");
	
	JNIEnv *env;
	if(vm->GetEnv((void**)&env, JNI_VERSION_1_4)!=JNI_OK) {
		LOGI("load fail");
		return -1;
	}
	
	LOGI("classname:%s", CLASS_NAME);
	jclass jclz = env->FindClass(CLASS_NAME); 
	JNINativeMethod methods[] = {{"d", "(I)Ljava/lang/String;", (void*)getstring}
								,{"b", "(Ljava/lang/String;Ljava/lang/String;Ljava/lang/String;Ljava/lang/Object;)Ljava/lang/Object;", (void*)loaddex}
								,{"c", "(I[Ljava/lang/Object;)V", (void*)invokedex}
	};
	
	
	if(env->RegisterNatives(jclz, methods,  sizeof(methods)/sizeof(JNINativeMethod))==JNI_OK){
		LOGI("register method success");
	}

	
	return JNI_VERSION_1_4;
}
