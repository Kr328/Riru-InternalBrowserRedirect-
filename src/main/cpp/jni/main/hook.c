#include "hook.h"

#include <stdio.h>
#include <string.h>

const char *config_path = NULL;

static int       gIsDexLoaded = 0;
static jmethodID javaHookedGetContextObjectId = NULL;
static jclass    javaHookedInjectClass        = NULL;

static jint (*originalRegisterNatives)(JNIEnv *env,const char *class_name,const JNINativeMethod* methods, jint length);
static jint hookedRegisterNatives(JNIEnv *env,const char *class_name,const JNINativeMethod* methods, jint length);

static jobject (*originalGetContextObject)(JNIEnv* env, jobject clazz);
static jobject hookedGetContextObject(JNIEnv* env, jobject clazz);

static int hookedAndroidRuntime$startReg(JNIEnv *env);
static int (*originalAndroidRuntime$startReg)(JNIEnv *env);

static jobject jniGetContextObjectOriginal(JNIEnv* env, jobject clazz) {
	return originalGetContextObject(env,clazz);
}

static jstring jniGetCurrentConfigPath(JNIEnv *env ,jobject clazz) {
    return (*env)->NewStringUTF(env ,config_path);
}

int hook_install_forked(JNIEnv *env) {
	int (*register_android_os_Binder)(JNIEnv* env) = dlsym(dlopen(NULL,RTLD_LAZY),"_Z26register_android_os_BinderP7_JNIEnv");
	if ( register_android_os_Binder == NULL )
		return -1;

	if ( originalRegisterNatives == NULL ) {
		xhook_register(".*android_runtime.*","jniRegisterNativeMethods",&hookedRegisterNatives,(void **) &originalRegisterNatives);
		if ( xhook_refresh(0) == 0 )
			xhook_clear();
		else
			return -1;
	}

	register_android_os_Binder(env);

	return 0;
}

int hook_install_global(void) {
	if ( originalAndroidRuntime$startReg == NULL ) {
		xhook_register(".*android_runtime.*","_ZN7android14AndroidRuntime8startRegEP7_JNIEnv",&hookedAndroidRuntime$startReg,(void **) &originalAndroidRuntime$startReg);
		if ( xhook_refresh(0) == 0 )
			xhook_clear();
		else
			return -1;
	}

	return 0;
}

static const JNINativeMethod gBinderInternalMethod = {
	/* name, signature, funcPtr */
	"getContextObject", "()Landroid/os/IBinder;", (void*)&hookedGetContextObject
};

static const JNINativeMethod gInjectorMethod[] = {
	/* name, signature, funcPtr */
	{"getContextObjectOriginal","()Landroid/os/IBinder;", (void*)&jniGetContextObjectOriginal} ,
	{"getCurrentConfigPath","()Ljava/lang/String;" ,(void*)&jniGetCurrentConfigPath}
};

static int hookedAndroidRuntime$startReg(JNIEnv *env) {
	int result = originalAndroidRuntime$startReg(env);

	if ( !gIsDexLoaded ) {
		javaHookedInjectClass        = (*env)->FindClass(env,"me/kr328/ibr/Injector");
		javaHookedGetContextObjectId = (*env)->GetStaticMethodID(env,javaHookedInjectClass,"getContextObjectHooked","()Landroid/os/IBinder;");

		(*env)->RegisterNatives(env,javaHookedInjectClass,gInjectorMethod,2);

		gIsDexLoaded = 1;
	}

	return result;
}

static jint hookedRegisterNatives(JNIEnv *env,const char *class_name,const JNINativeMethod* methods, jint length) {
	int is_need_register = 0;

	if ( strcmp("com/android/internal/os/BinderInternal",class_name) == 0 ) {
		LOGI("Class com/android/internal/os/BinderInternal found.");
		for ( int i = 0 ; i < length ; i++ ) {
			if ( strcmp("getContextObject",methods[i].name) == 0 ) {
				LOGI("Method getContextBinder found. %p",methods[i].fnPtr);
				originalGetContextObject = methods[i].fnPtr;
				is_need_register = 1;
				break;
			}
		}
	}

	jint result = originalRegisterNatives(env,class_name,methods,length);

	if ( is_need_register )
		!originalRegisterNatives(env,class_name,&gBinderInternalMethod,1) && LOGI("Registered com/android/internal/os/BinderInternal");

	return result;
}

static jobject hookedGetContextObject(JNIEnv* env, jobject clazz) {
	LOGI("getContextObject called.");

	if ( javaHookedGetContextObjectId ) {
		LOGI("Calling javaHookedGetContextObjectId");

		jobject result = (*env)->CallStaticObjectMethod(env,javaHookedInjectClass,javaHookedGetContextObjectId);

		if ( (*env)->ExceptionCheck(env) ) {
			(*env)->ExceptionDescribe(env);
			(*env)->ExceptionClear(env);
			LOGE("Call method failure");
		} else
			return result;
	}

	return originalGetContextObject(env,clazz);
}