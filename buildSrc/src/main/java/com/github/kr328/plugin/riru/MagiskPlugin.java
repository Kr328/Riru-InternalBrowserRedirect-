package com.github.kr328.plugin.riru;

import com.github.kr328.plugin.riru.exts.MagiskExtension;
import com.github.kr328.plugin.riru.tasks.MagiskBuildTask;
import org.gradle.api.Plugin;
import org.gradle.api.Project;

public class MagiskPlugin implements Plugin<Project> {
    @Override
    public void apply(Project target) {
        MagiskExtension extension = target.getExtensions().create("magisk" , MagiskExtension.class);
        MagiskBuildTask task = target.getTasks().create("buildMagiskModule" , MagiskBuildTask.class);

        //task.mustRunAfter(TransformDexTask.class , CMakeBuildTask.class);
        task.mustRunAfter("transformDex" ,"cmakeBuild");

        task.extension = extension;
    }
}
