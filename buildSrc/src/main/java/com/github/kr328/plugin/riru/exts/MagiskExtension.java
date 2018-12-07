package com.github.kr328.plugin.riru.exts;

import com.github.kr328.plugin.riru.utils.PathUtils;
import groovy.lang.Closure;
import org.gradle.api.NamedDomainObjectCollection;
import org.gradle.api.Project;

import java.io.File;
import java.util.HashMap;

public class MagiskExtension {
    private HashMap<File,String> zipMap = new HashMap<>();
    private File output;

    public MagiskExtension map(File source ,String target) {
        zipMap.put(source ,target);
        return this;
    }

    public HashMap<File ,String> getZipMap() {
        return zipMap;
    }

    public File getOutput() {
        return output;
    }

    public void setOutput(File output) {
        this.output = output;
    }
}
