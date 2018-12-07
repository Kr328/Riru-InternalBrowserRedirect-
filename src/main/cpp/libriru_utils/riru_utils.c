//
// Created by Kr328 on 18-11-26.
//

#include "riru_utils.h"

#include "xhook.h"
#include "riru.h"

#include <jni.h>
#include <string.h>
#include <malloc.h>

//Private Internal
static int replace_classes_count;
static riru_utils_jni_replace_class_t *replace_classes;

static jint (*original_jni_register)(JNIEnv *env, const char *class_name, const JNINativeMethod *methods, jint length);
static jint replaced_jni_register(JNIEnv *env, const char *class_name, const JNINativeMethod *methods, jint length) {
    for ( int i = 0 ; i < replace_classes_count ; i++ ) {
        riru_utils_jni_replace_class_t *current = replace_classes + i;

        if ( !strcmp(class_name ,current->class_name) ) {
            static JNINativeMethod *new_methods;
            new_methods = realloc(new_methods ,sizeof(JNINativeMethod) * length);
            memcpy(new_methods ,methods , sizeof(JNINativeMethod) * length);

            for ( int c = 0 ; c < length ; c++ ) {
                JNINativeMethod *current_method = new_methods + c;
                for ( int d = 0 ; d < current->method_count ; d++ ) {
                    if ( !strcmp(current_method->name ,current->methods[d].method_name) && !strcmp(current_method->signature ,current->methods[d].signature) ) {
                        void *original = riru_get_native_method_func(class_name ,current_method->name ,current_method->signature);
                        if ( !original || original == current->methods[d].replace_function )
                            original = current_method->fnPtr;

                        riru_set_native_method_func(class_name ,current_method->name ,current_method->signature ,current_method->fnPtr);

                        *current->methods[d].original_function = original;
                        current_method->fnPtr = current->methods[d].replace_function;
                    }
                }
            }

            return original_jni_register(env ,class_name ,new_methods ,length);
        }
    }

    return original_jni_register(env ,class_name ,methods ,length);
}

//Public Export
int riru_utils_replace_native_functions(riru_utils_native_replace_t *functions, int length) {
    for ( int i = 0 ; i < length ; i++ ) {
        riru_utils_native_replace_t *current = functions + i;
        xhook_register(current->library_pattern ,current->symbol ,current->replace_function ,current->original_function);
    }

    if ( xhook_refresh(0) == 0 )
        xhook_clear();
    else
        return -1;

    for ( int i = 0 ; i < length ; i++ ) {
        riru_utils_native_replace_t *current = functions + i;

        void *replaced_original = riru_get_func(current->symbol);
        if ( replaced_original && replaced_original != *current->original_function ) {
            *current->original_function = replaced_original;
            riru_set_func(current->symbol ,current->replace_function);
        }
    }

    return 0;
}

int riru_utils_set_replace_jni_methods(riru_utils_jni_replace_class_t *classes ,int length) {
    for ( int i = 0 ; i < length ; i++ ) {
        for
    }

    return 0;
}

int riru_utils_init_module(const char *module_name) {
	riru_set_module_name(module_name);

	return 0;
}
