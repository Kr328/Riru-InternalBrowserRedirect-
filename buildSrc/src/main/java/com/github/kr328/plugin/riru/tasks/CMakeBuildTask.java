package com.github.kr328.plugin.riru.tasks;

import com.github.kr328.plugin.riru.conf.CMakeConfig;
import com.github.kr328.plugin.riru.exts.CMakeExtension;
import com.github.kr328.plugin.riru.utils.PathUtils;
import org.gradle.api.DefaultTask;
import org.gradle.api.GradleScriptException;
import org.gradle.api.tasks.TaskAction;

import java.io.BufferedReader;
import java.io.File;
import java.io.InputStreamReader;

public class CMakeBuildTask extends DefaultTask {
    public CMakeExtension extension;
    public CMakeConfig    config;

    @TaskAction
    @SuppressWarnings("unused")
    public void executeBuild() {
        for ( String abi : extension.getAbiFilter() ) {
            try {
                executeAbi(abi);
            } catch (Exception e) {
                throw new GradleScriptException("CMake build failure." ,e);
            }
        }
    }

    private void executeAbi(String abi) throws Exception {
        ProcessBuilder builder = new ProcessBuilder();
        StringBuilder  outputs = new StringBuilder();

        builder.command(PathUtils.toLocalSeparator(config.cmakeDirectory + "/bin/cmake" + PathUtils.executableSuffix(".exe")) , "--build" ,".");

        File cmakeConfigDirectory = getProject().file(PathUtils.toLocalSeparator(getProject().getBuildDir().getAbsolutePath() + "/intermediate/cmake/" + abi)).getAbsoluteFile();

        builder.directory(cmakeConfigDirectory);

        Process process = builder.start();
        BufferedReader reader;
        String line;

        reader = new BufferedReader(new InputStreamReader(process.getInputStream()));
        while (( line = reader.readLine()) != null )
            outputs.append(line).append('\n');
        reader.close();

        reader = new BufferedReader(new InputStreamReader(process.getErrorStream()));
        while (( line = reader.readLine()) != null )
            outputs.append(line).append('\n');
        reader.close();

        int result = process.waitFor();

        if ( result != 0 )
            throw new Exception("CMake build failure: \n" + outputs.toString());

        process.destroy();
    }
}
