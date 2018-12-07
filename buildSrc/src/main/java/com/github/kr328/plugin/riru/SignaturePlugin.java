package com.github.kr328.plugin.riru;

import com.github.kr328.plugin.riru.conf.AndroidConfig;
import com.github.kr328.plugin.riru.exts.SignatureExtension;
import com.github.kr328.plugin.riru.tasks.SignatureTask;
import org.gradle.api.Plugin;
import org.gradle.api.Project;

public class SignaturePlugin implements Plugin<Project> {
    @Override
    public void apply(Project project) {
        SignatureExtension extension = project.getExtensions().create("signature" , SignatureExtension.class);
        SignatureTask task = project.getTasks().create("signatureOutput" ,SignatureTask.class);

        task.extension = extension;
        task.config = AndroidConfig.read();
    }
}
