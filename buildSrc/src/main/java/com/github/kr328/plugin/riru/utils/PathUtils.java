package com.github.kr328.plugin.riru.utils;

import java.io.File;

public class PathUtils {
    public static String toLocalSeparator(String p) {
        return p.replace('/' , File.separatorChar).replace('\\' ,File.separatorChar);
    }

    public static String zipEntry(String p) {
        String result = p.replaceAll("/+" ,"/");
        if ( result.indexOf('/') == 0 )
            return result.substring(1);
        return result;
    }

    public static String executableSuffix(String suffix) {
        if ( System.getProperty("os.name").toLowerCase().contains("win") )
            return suffix;
        return "";
    }

    private static String exe;
}
