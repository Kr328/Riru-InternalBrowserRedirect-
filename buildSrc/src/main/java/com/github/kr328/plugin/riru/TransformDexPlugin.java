package com.github.kr328.plugin.riru;

import com.github.kr328.plugin.riru.conf.AndroidConfig;
import com.github.kr328.plugin.riru.exts.TransformDexExtension;
import com.github.kr328.plugin.riru.tasks.TransformDexTask;
import org.gradle.api.Plugin;
import org.gradle.api.Project;
import org.gradle.api.plugins.JavaPlugin;
import org.gradle.api.tasks.compile.JavaCompile;

public class TransformDexPlugin implements Plugin<Project> {
    @Override
    public void apply(Project target) {
        target.getPluginManager().apply(JavaPlugin.class);

        TransformDexExtension extension = target.getExtensions().create("dex" , TransformDexExtension.class);
        AndroidConfig config = AndroidConfig.read();

        JavaCompile javaCompile = (JavaCompile)target.getTasks().getByName("compileJava");
        TransformDexTask transformDexTask = target.getTasks().create("transformDex" , TransformDexTask.class);

        transformDexTask.dependsOn(javaCompile);

        transformDexTask.extension = extension;
        transformDexTask.config = config;
    }
}
