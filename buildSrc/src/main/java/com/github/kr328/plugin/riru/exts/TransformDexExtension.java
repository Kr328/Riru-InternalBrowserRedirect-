package com.github.kr328.plugin.riru.exts;

import com.github.kr328.plugin.riru.utils.PathUtils;

public class TransformDexExtension {
    private String buildToolsVersion = "";
    private String compilePlatform = "";
    private String output = "classes.dex";

    public String getBuildToolsVersion() {
        return buildToolsVersion;
    }

    public void setBuildToolsVersion(String buildToolsVersion) {
        this.buildToolsVersion = buildToolsVersion;
    }

    public String getCompilePlatform() {
        return compilePlatform;
    }

    public void setCompilePlatform(String compilePlatform) {
        this.compilePlatform = compilePlatform;
    }

    public String getOutput() {
        return output;
    }

    public void setOutput(String output) {
        this.output = output;
    }
}
