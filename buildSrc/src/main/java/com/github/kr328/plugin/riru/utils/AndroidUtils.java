package com.github.kr328.plugin.riru.utils;

import com.github.kr328.plugin.riru.TransformDexPlugin;
import com.github.kr328.plugin.riru.conf.AndroidConfig;
import com.github.kr328.plugin.riru.conf.LocalProperties;
import com.github.kr328.plugin.riru.exts.TransformDexExtension;
import org.gradle.api.GradleScriptException;

import java.io.File;
import java.io.FileNotFoundException;

public class AndroidUtils {
    public static String androidJarPath(String platform) {
        String path = PathUtils.toLocalSeparator(AndroidConfig.read().androidSdkPath + "/platforms/" + platform + "/android.jar");
        if ( !new File(path).exists() )
            throw new GradleScriptException("android.jar not found" ,new FileNotFoundException("android.jar not found"));
        return path;
    }
}
