package com.github.kr328.plugin.riru.exts;

public class CMakeExtension {
    private String   source    = "";
    private String   platform  = "android-24";
    private String[] abiFilter = new String[]{"armeabi-v7a" ,"arm64-v8a"};
    private String   stl       = "c++_static";

    public String getSource() {
        return source;
    }

    public void setSource(String source) {
        this.source = source;
    }

    public String getPlatform() {
        return platform;
    }

    public void setPlatform(String platform) {
        this.platform = platform;
    }

    public String[] getAbiFilter() {
        return abiFilter;
    }

    public void setAbiFilter(String[] abiFilter) {
        this.abiFilter = abiFilter;
    }

    public String getStl() {
        return stl;
    }

    public void setStl(String stl) {
        this.stl = stl;
    }
}
