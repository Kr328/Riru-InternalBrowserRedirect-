package com.github.kr328.plugin.riru.conf;

import org.gradle.api.GradleScriptException;

import java.util.Properties;

public class AndroidConfig {
    public String androidSdkPath;

    public static AndroidConfig read() {
        Properties properties = LocalProperties.get();
        AndroidConfig result = new AndroidConfig();

        result.androidSdkPath = properties.getProperty("android.sdk");

        if ( result.androidSdkPath == null )
            throw new GradleScriptException("android.sdk must set in local.properies" ,new NullPointerException());

        return result;
    }
}
