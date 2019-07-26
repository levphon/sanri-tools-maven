package com.sanri.app.postman;

public class ConfigPath {
    private String pathName;
    private boolean isDirectory;

    public ConfigPath(String pathName, boolean isDirectory) {
        this.pathName = pathName;
        this.isDirectory = isDirectory;
    }

    public ConfigPath() {
    }

    public String getPathName() {
        return pathName;
    }

    public boolean isDirectory() {
        return isDirectory;
    }
}
