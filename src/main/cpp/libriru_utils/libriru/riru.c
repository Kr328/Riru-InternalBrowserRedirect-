#include <dlfcn.h>
#include <memory.h>

#ifdef __LP64__
#define LIB "/system/lib64/libmemtrack.so"
#else
#define LIB "/system/lib/libmemtrack.so"
#endif


static char *riru_module_name;
static int  initiated;

static void *riru_get_version_sym;
static void *riru_get_func_sym;
static void *riru_set_func_sym;
static void *riru_get_native_method_func_sym;
static void *riru_set_native_method_func_sym;

static void init() {
    if ( initiated ) return;

    void *riru_handle = dlopen(LIB, RTLD_NOW | RTLD_GLOBAL);
    riru_get_func_sym = dlsym(riru_handle ,"riru_get_func");
    riru_set_func_sym = dlsym(riru_handle ,"riru_set_func");
    riru_get_native_method_func_sym = dlsym(riru_handle ,"riru_get_native_method_func");
    riru_set_native_method_func_sym = dlsym(riru_handle ,"riru_set_native_method_func");

    initiated = 1;
}

const char *riru_get_module_name() {
    return riru_module_name;
}

void riru_set_module_name(const char *name) {
    riru_module_name = strdup(name);
}

int riru_get_version() {
    init();
    if (riru_get_version_sym) return ((int (*)()) riru_get_version_sym)();
    return -1;
}

void *riru_get_func(const char *name) {
    init();
    if (riru_get_func_sym) return ((void *(*)(const char *, const char *)) riru_get_func_sym)(riru_get_module_name(), name);
    return NULL;
}

void *riru_get_native_method_func(const char *className, const char *name, const char *signature) {
    init();
    if (riru_get_native_method_func_sym)
        return ((void *(*)(const char *, const char *, const char *, const char *)) riru_get_native_method_func_sym)(
                riru_get_module_name(), className, name, signature);
    return NULL;
}

void riru_set_func(const char *name, void *func) {
    init();
    if (riru_set_func_sym)
        ((void *(*)(const char *, const char *, void *)) riru_set_func_sym)(riru_get_module_name(), name, func);
}

void riru_set_native_method_func(const char *className, const char *name, const char *signature,
                                 void *func) {
    init();
    if (riru_set_native_method_func_sym)
        ((void *(*)(const char *, const char *, const char *, const char *, void *)) riru_set_native_method_func_sym)(
                riru_get_module_name(), className, name, signature, func);
}