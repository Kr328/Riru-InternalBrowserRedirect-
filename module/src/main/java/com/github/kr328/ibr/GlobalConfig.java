package com.github.kr328.ibr;

import android.util.Log;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.FileInputStream;
import java.io.IOException;
import java.util.regex.Pattern;
import java.util.regex.PatternSyntaxException;

public class GlobalConfig {
    public static void load(String path) {
        if ( config == null )
            config = new GlobalConfig(path);
    }

    public static GlobalConfig get() {
        return config;
    }

    private GlobalConfig(String path) {
        try {
            String configData = readFileToString(path);
            JSONObject jsonObject = new JSONObject(configData);

            urlKey           = jsonObject.getString("urlKey");
            hostWhitePattern = Pattern.compile(jsonObject.getString("hostWhitePattern"));
        } catch (IOException e) {
            Log.e(Constants.TAG ,"failure to load config " + path ,e);
        } catch (JSONException e) {
            Log.e(Constants.TAG ,"failure to parse config " + path ,e);
        } catch (PatternSyntaxException e) {
            Log.e(Constants.TAG ,"failure to compile pattern " + path ,e);
        }
    }

    @SuppressWarnings("ResultOfMethodCallIgnored")
    private String readFileToString(String path) throws IOException {
        FileInputStream stream = new FileInputStream(path);
        byte[] buffer = new byte[stream.available()];
        stream.read(buffer);
        stream.close();
        return new String(buffer);
    }

    private String urlKey = "";
    private Pattern hostWhitePattern = Pattern.compile("");

    private static GlobalConfig config = null;

    public String getUrlKey() {
        return urlKey;
    }

    public Pattern getHostWhitePattern() {
        return hostWhitePattern;
    }
}
