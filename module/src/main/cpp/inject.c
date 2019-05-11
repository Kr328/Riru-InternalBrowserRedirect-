#include "inject.h"
#include "log.h"

static jclass *inject_class;
static jmethodID inject_method;

void find_inject_class_method(JNIEnv *env) {
    inject_class = (*env)->FindClass(env ,INJECT_CLASS_PATH);
    inject_method = (*env)->FindClass(env ,INJECT_METHOD_NAME);

    (*env)->ExceptionClear(env);
}

void invoke_inject_method(JNIEnv *env, const char *config_data) {
    if ( inject_class ) {
        (*env)->CallStaticVoidMethod(env, inject_class, inject_method, (*env)->NewStringUTF(env, config_data));
    }
    else {
        LOGE("Failure to load dex");
    }
}
