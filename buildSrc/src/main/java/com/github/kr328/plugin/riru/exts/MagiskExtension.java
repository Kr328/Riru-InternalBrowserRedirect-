package com.github.kr328.plugin.riru.exts;

import com.github.kr328.plugin.riru.utils.PathUtils;

import java.io.File;
import java.util.HashMap;

public class MagiskExtension {
    private HashMap<File,String> zipMap = new HashMap<>();

    public MagiskExtension map(File source ,String target) {
        zipMap.put(source ,target);
        return this;
    }

    public HashMap<File ,String> getZipMap() {
        return zipMap;
    }
}
