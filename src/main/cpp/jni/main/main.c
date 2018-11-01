#include <stdio.h>
#include <jni.h>
#include <dlfcn.h>
#include <unistd.h>
#include <stdlib.h>
#include <string.h>
#include <android/log.h>

#include "hook.h"
#include "log.h"

#define CONFIG_PATH_FORMAT "/data/misc/riru/modules/ibr/config.%s.json"

const char *parse_package_name(JNIEnv *env, jstring appDataDir) {
    if (!appDataDir)
        return 0;

    const char *app_data_dir = (*env)->GetStringUTFChars(env ,appDataDir, NULL);

    int user = 0;
    static char package_name[256];
    if (sscanf(app_data_dir, "/data/%*[^/]/%d/%s", &user, package_name) != 2) {
        if (sscanf(app_data_dir, "/data/%*[^/]/%s", package_name) != 1) {
            package_name[0] = '\0';
            LOGW("can't parse %s", app_data_dir);
            return NULL;
        }
    }

    (*env)->ReleaseStringUTFChars(env ,appDataDir, app_data_dir);

    return package_name;
}

__attribute__((visibility("default"))) void nativeForkAndSpecializePre(JNIEnv *env, jclass clazz,
                                                                       jint _uid, jint gid,
                                                                       jintArray gids,
                                                                       jint runtime_flags,
                                                                       jobjectArray rlimits,
                                                                       jint _mount_external,
                                                                       jstring se_info,
                                                                       jstring se_name,
                                                                       jintArray fdsToClose,
                                                                       jintArray fdsToIgnore,
                                                                       jboolean is_child_zygote,
                                                                       jstring instructionSet,
                                                                       jstring appDataDir) {
    const char *package_name = parse_package_name(env ,appDataDir);
    char config_path_buffer[256];

    if ( package_name ) {
        sprintf(config_path_buffer ,CONFIG_PATH_FORMAT ,package_name);
        if ( access(config_path_buffer ,F_OK) == 0 ) {
            current_package = package_name;
            return;
        }
    }

    LOGI("Skip %s" ,package_name);

    current_package = NULL;
}

int nativeForkAndSpecializePost(JNIEnv *env, jclass clazz, jint res) {
	return (res == 0 && current_package) ? hook_install_forked(env) : 0;
}

__attribute__((constructor))
void onLibraryLoaded(void) {
	char buffer[256];
	char *p = NULL;

	strcpy(buffer,(p = getenv("CLASSPATH")) ? p : "");
	strcat(buffer,":" DEX_PATH);
	setenv("CLASSPATH",buffer,1);

	hook_install_global();
}


