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

    public static synchronized String executableSuffix() {
        if ( exe == null ) {
            if ( System.getProperty("os.name").toLowerCase().contains("win") )
                exe = ".exe";
            else
                exe = "";
        }

        return exe;
    }

    private static String exe;
}
