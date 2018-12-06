//
// Created by Kr328 on 18-11-26.
//
#pragma once

typedef struct riru_utils_native_replace_s {
    const char *library_pattern;
    const char *symbol;
    void       *replace_function;
    void      **original_function;
} riru_utils_native_replace_t;

typedef struct riru_utils_jni_replace_class_s {
    const char *class_name;

    int method_count;
    struct {
        const char *method_name;
        const char *signature;
        void       *replace_function;
        void      **original_function;
    } methods[];
} riru_utils_jni_replace_class_t;

int riru_utils_replace_native_functions(riru_utils_native_replace_t *functions, int length);
int riru_utils_set_replace_jni_methods(riru_utils_jni_replace_class_t *classes ,int length);

//Return Riru version
int riru_utils_init_module(const char *module_name);