package com.github.kr328.plugin.riru.conf;

import org.gradle.api.GradleScriptException;

import java.io.FileInputStream;
import java.util.Properties;

public class LocalProperties {
    private static Properties properties;

    public static synchronized Properties get() {
        if ( properties == null ) {
            properties = new Properties();
            FileInputStream fileInputStream;

            try {
                fileInputStream = new FileInputStream("local.properties");
                properties.load(fileInputStream);
            } catch (Exception e) {
                throw new GradleScriptException("local.properties load failure" ,e);
            }
        }

        return properties;
    }
}
