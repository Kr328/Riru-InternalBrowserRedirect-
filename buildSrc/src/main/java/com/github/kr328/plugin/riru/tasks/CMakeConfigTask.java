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

public class CMakeConfigTask extends DefaultTask {
    public CMakeExtension extension;
    public CMakeConfig    config;

    @TaskAction @SuppressWarnings("unused")
    public void execCMake() {
        for ( String abi : extension.getAbiFilter()) {
            try {
                execCMakeAbi(abi);
            } catch (Exception e) {
                throw new GradleScriptException("CMake build failure" ,e);
            }
        }
    }

    @SuppressWarnings("ResultOfMethodCallIgnored")
    private void execCMakeAbi(String abi) throws Exception {
        ProcessBuilder builder = new ProcessBuilder();
        StringBuilder  outputs = new StringBuilder();

        builder.command(PathUtils.toLocalSeparator(config.cmakeDirectory + "/bin/cmake" + PathUtils.executableSuffix(".exe")) ,
                "-G" ,"Ninja" ,
                "-DCMAKE_MAKE_PROGRAM=" + PathUtils.toLocalSeparator(config.cmakeDirectory + "/bin/ninja" + PathUtils.executableSuffix(".exe")),
                "-DCMAKE_TOOLCHAIN_FILE=" + PathUtils.toLocalSeparator(config.androidNdkPath + "/build/cmake/android.toolchain.cmake") ,
                "-DANDROID_PLATFORM=" + extension.getPlatform(),
                "-DANDROID_STL=" + extension.getStl(),
                "-DANDROID_ABI=" + abi ,
                "-DCMAKE_ARCHIVE_OUTPUT_DIRECTORY=" + PathUtils.toLocalSeparator(getProject().getBuildDir().getAbsolutePath() + "/outputs/cmake/" + abi + "/static") ,
                "-DCMAKE_LIBRARY_OUTPUT_DIRECTORY=" + PathUtils.toLocalSeparator(getProject().getBuildDir().getAbsolutePath() + "/outputs/cmake/" + abi + "/shared") ,
                "-DCMAKE_RUNTIME_OUTPUT_DIRECTORY=" + PathUtils.toLocalSeparator(getProject().getBuildDir().getAbsolutePath() + "/outputs/cmake/" + abi + "/executable") ,
                getProject().file(PathUtils.toLocalSeparator(extension.getSource())).getAbsolutePath());

        File cmakeConfigDirectory = getProject().file(PathUtils.toLocalSeparator(getProject().getBuildDir().getAbsolutePath() + "/intermediate/cmake/" + abi)).getAbsoluteFile();
        cmakeConfigDirectory.mkdirs();

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
            throw new Exception("CMake config failure: \n" + outputs.toString());

        process.destroy();
    }
}
