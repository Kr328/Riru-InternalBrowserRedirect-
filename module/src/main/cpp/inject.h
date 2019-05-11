#pragma once

#include <jni.h>

void find_inject_class_method(JNIEnv *env);
void invoke_inject_method(JNIEnv* env, const char *config_data);