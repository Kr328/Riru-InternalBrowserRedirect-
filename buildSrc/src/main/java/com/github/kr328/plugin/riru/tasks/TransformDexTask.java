package com.github.kr328.plugin.riru.tasks;

import com.github.kr328.plugin.riru.conf.AndroidConfig;
import com.github.kr328.plugin.riru.exts.TransformDexExtension;
import com.github.kr328.plugin.riru.utils.PathUtils;
import org.gradle.api.DefaultTask;
import org.gradle.api.GradleScriptException;
import org.gradle.api.tasks.TaskAction;
import org.gradle.api.tasks.compile.JavaCompile;

import java.io.BufferedReader;
import java.io.File;
import java.io.InputStreamReader;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

public class TransformDexTask extends DefaultTask {
    public TransformDexExtension extension;
    public AndroidConfig config;

    @TaskAction
    @SuppressWarnings("unused")
    public void exec() {
        JavaCompile task = (JavaCompile) getProject().getTasks().getByName("compileJava");
        File source = task.getDestinationDir();

        if ( task.getState().getNoSource() || task.getState().getUpToDate() ) {
            setDidWork(false);
            return;
        }

        try {
            ProcessBuilder builder = new ProcessBuilder();
            StringBuilder  outputs = new StringBuilder();

            String executable = PathUtils.toLocalSeparator(config.androidSdkPath +
                            "/build-tools/" + extension.getBuildToolsVersion() +
                            "/d8" + (System.getProperty("os.name").toLowerCase().contains("win") ? ".bat" : ""));
            String outputPath = PathUtils.toLocalSeparator(getProject().getBuildDir().getAbsolutePath().concat("/outputs/dex/").concat(extension.getOutput()));
            String libraryPath = PathUtils.toLocalSeparator(config.androidSdkPath + "/platforms/" + extension.getCompilePlatform() + "/android.jar");

            //noinspection ResultOfMethodCallIgnored
            new File(outputPath).getParentFile().mkdirs();

            ArrayList<String> command = new ArrayList<>();

            command.add(executable);
            command.add("--output");
            command.add(outputPath);
            command.add("--lib");
            command.add(libraryPath);

            List<String> classes = Files.walk(Paths.get(source.getAbsolutePath()))
                    .map(Path::toAbsolutePath)
                    .map(Path::toString)
                    .filter(s -> s.endsWith(".class"))
                    .collect(Collectors.toList());

            command.addAll(classes);

            builder.command(command.toArray(new String[0]));

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
                throw new Exception("Exec d8: " + command + "\n" + outputs.toString());

            if ( !new File(outputPath).exists() )
                throw new Exception("Exec d8: " + command + "\n" + outputs.toString());

            process.destroy();
        }
        catch (Exception e) {
            throw new GradleScriptException("Exec d8: " + e.toString(),e);
        }

    }
}
