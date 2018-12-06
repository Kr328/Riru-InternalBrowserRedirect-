package com.github.kr328.plugin.riru;

import com.github.kr328.plugin.riru.conf.CMakeConfig;
import com.github.kr328.plugin.riru.exts.CMakeExtension;
import com.github.kr328.plugin.riru.tasks.CMakeBuildTask;
import com.github.kr328.plugin.riru.tasks.CMakeConfigTask;
import org.gradle.api.Plugin;
import org.gradle.api.Project;

public class CMakePlugin implements Plugin<Project> {
    @Override
    public void apply(Project target) {
        CMakeExtension cMakeExtension = target.getExtensions().create("cmake" , CMakeExtension.class);
        CMakeConfig    cMakeConfig    = CMakeConfig.read();

        CMakeConfigTask cMakeConfigTask = target.getTasks().create("cmakeConfig" , CMakeConfigTask.class);
        CMakeBuildTask  cMakeBuildTask  = target.getTasks().create("cmakeBuild" ,CMakeBuildTask.class);

        cMakeConfigTask.config    = cMakeConfig;
        cMakeConfigTask.extension = cMakeExtension;
        cMakeBuildTask.config     = cMakeConfig;
        cMakeBuildTask.extension  = cMakeExtension;

        cMakeBuildTask.dependsOn(cMakeConfigTask);
    }
}
