package com.github.kr328.plugin.riru.conf;

import org.gradle.api.GradleScriptException;

import java.util.Properties;

public class CMakeConfig {
    public String cmakeDirectory;
    public String androidNdkPath;

    public static CMakeConfig read() {
        Properties  properties = LocalProperties.get();
        CMakeConfig result = new CMakeConfig();

        result.cmakeDirectory = properties.getProperty("android.cmake");
        result.androidNdkPath = properties.getProperty("android.ndk");

        if ( result.cmakeDirectory == null )
            throw new GradleScriptException("android.cmake must set in local.properties" ,new Exception());

        if ( result.androidNdkPath == null )
            throw new GradleScriptException("android.ndk must set in local.properties" ,new Exception());

        return result;
    }
}
