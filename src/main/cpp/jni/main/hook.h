#pragma once

#include <jni.h>
#include <dlfcn.h>
#include <malloc.h>
#include <stdint.h>
#include <string.h>
#include <sys/mman.h>

#include "xhook.h"

#include "log.h"

//#define DEX_PATH               "/data/local/tmp/injector.jar"
#define DEX_PATH               "/system/framework/ibr_injector.jar"

int hook_install_forked(JNIEnv *env);
int hook_install_global(void);

extern const char *current_package;
