package com.github.kr328.plugin.riru.tasks;

import com.github.kr328.plugin.riru.conf.AndroidConfig;
import com.github.kr328.plugin.riru.exts.SignatureExtension;
import com.github.kr328.plugin.riru.utils.PathUtils;
import org.gradle.api.DefaultTask;
import org.gradle.api.GradleScriptException;
import org.gradle.api.tasks.TaskAction;

import java.io.BufferedReader;
import java.io.InputStreamReader;

public class SignatureTask extends DefaultTask {
    public SignatureExtension extension;
    public AndroidConfig      config;

    @TaskAction
    @SuppressWarnings("unused")
    public void exec() {
        if ( extension.getTargetFile() == null || extension.getKeyStore() == null || extension.getBuildToolsVersion() == null ) {
            setDidWork(false);
            return;
        }

        try {
            doSignature();
        } catch (Exception e) {
            throw new GradleScriptException("Signature failure" ,e);
        }
    }

    private void doSignature() throws Exception {
        ProcessBuilder builder = new ProcessBuilder();
        StringBuilder  outputs = new StringBuilder();

        builder.command(PathUtils.toLocalSeparator(config.androidSdkPath + "/build-tools/" + extension.getBuildToolsVersion() + "/apksigner" + PathUtils.executableSuffix(".bat")),
                "sign" ,
                "--min-sdk-version" ,"24" ,
                "--max-sdk-version" ,"28" ,
                "--ks" ,extension.getKeyStore().getAbsolutePath() ,
                "--ks-pass" ,"pass:" + extension.getKeyStorePassword() ,
                "--ks-key-alias" ,extension.getAlias() ,
                "--key-pass" ,"pass:" + extension.getAliasPassword() ,
                extension.getTargetFile().getAbsolutePath());

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
            throw new Exception("Signature build failure: \n" + outputs.toString());

        process.destroy();
    }
}
