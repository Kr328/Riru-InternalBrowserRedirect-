package com.github.kr328.plugin.riru.exts;

import java.io.File;

public class SignatureExtension {
    private File targetFile;
    private String buildToolsVersion;

    private File   keyStore;
    private String keyStorePassword;
    private String alias;
    private String aliasPassword;

    public File getKeyStore() {
        return keyStore;
    }

    public void setKeyStore(File keyStore) {
        this.keyStore = keyStore;
    }

    public String getKeyStorePassword() {
        return keyStorePassword;
    }

    public void setKeyStorePassword(String keyStorePassword) {
        this.keyStorePassword = keyStorePassword;
    }

    public String getAlias() {
        return alias;
    }

    public void setAlias(String alias) {
        this.alias = alias;
    }

    public String getAliasPassword() {
        return aliasPassword;
    }

    public void setAliasPassword(String aliasPassword) {
        this.aliasPassword = aliasPassword;
    }

    public File getTargetFile() {
        return targetFile;
    }

    public void setTargetFile(File targetFile) {
        this.targetFile = targetFile;
    }

    public String getBuildToolsVersion() {
        return buildToolsVersion;
    }

    public void setBuildToolsVersion(String buildToolsVersion) {
        this.buildToolsVersion = buildToolsVersion;
    }
}
