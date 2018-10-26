#pragma once

#ifdef __cplusplus
extern "C" {
#endif

#define RIRU_NATIVE_HOOK_STATUS_INIT        0
#define RIRU_NATIVE_HOOK_STATUS_SUCCESS     0
#define RIRU_NATIVE_HOOK_STATUS_NOT_EXISTED 1

typedef struct {
    const char * class_name;
    const char * method_name;
    const char * signature;
    void *       hook_function;
    void **      original_function;
    int          status;
} riru_module_native_hook_list_t;

#define RIRU_NATIVE_HOOK_LIST_END {(const char *)0 ,(const char *)0 ,(const char *)0 ,(void*)0 ,(void**)0 ,RIRU_NATIVE_HOOK_STATUS_INIT}

#define ON_MODULE_LOADED_SYMBOL                       "onModuleLoaded"
#define NATIVE_HOOK_LIST_SYMBOL                       "nativeHookList"
#define NATIVE_FORK_AND_SPECIALIZE_PRE_SYMBOL         "nativeForkAndSpecializePre"
#define NATIVE_FORK_AND_SPECIALIZE_POST_SYMBOL        "nativeForkAndSpecializePre"
#define NATIVE_FORK_SYSTEM_SERVER_PRE_SYMBOL          "nativeForkSystemServerPre"
#define NATIVE_FORK_SYSTEM_SERVER_POST_SYMBOL         "nativeForkSystemServerPre"

#ifdef __cplusplus
};
#endif