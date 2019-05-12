package com.github.kr328.ibr;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.net.ContentHandler;
import java.net.URL;
import java.util.ArrayList;
import java.util.regex.Pattern;
import java.util.regex.PatternSyntaxException;

public class Rules {
    private String name = "Unlabeled";
    private ArrayList<Rule> rules = new ArrayList<>();

    private Uri lastMatchUri = Uri.EMPTY;
    private long lastMatchTime = 0;

    Rules(String configData) throws JSONException, PatternSyntaxException {
        JSONObject root = new JSONObject(configData);

        String name = root.optString("name");
        if ( name != null )
            this.name = name;

        JSONArray array = root.getJSONArray("rules");
        for ( int i = 0 ; i < array.length() ; i++ ) {
            JSONObject rule = array.getJSONObject(i);
            rules.add(new Rule(rule.getString("extra-key"), rule.getString("ignore-url"), rule.getString("force-url")));
        }
    }

    String getName() {
        return name;
    }

    Rule.Matcher matcher(Intent intent) {
        try {
            for ( Rule rule : rules ) {
                Rule.Matcher matcher = rule.matcher(intent);

                if ( matcher.matches() ) {
                    if ( lastMatchUri.equals(matcher.getUri()) && System.currentTimeMillis() - lastMatchTime < 1000 * 5 )
                        break;

                    lastMatchTime = System.currentTimeMillis();
                    lastMatchUri = matcher.getUri();

                    return matcher;
                }
            }
        }
        catch (Exception e) {
            Log.w(Constants.TAG ,e.getClass().getName(), e);
        }

        return new Rule.Matcher(false, Uri.EMPTY);
    }

    static class Rule {
        private String  extraKey;
        private Pattern ignoreUrl;
        private Pattern forceUrl;

        Rule(String extraKey, String ignoreUrl, String forceUrl) {
            this.extraKey = extraKey;
            this.ignoreUrl = Pattern.compile(ignoreUrl);
            this.forceUrl = Pattern.compile(forceUrl);
        }

        Matcher matcher(Intent intent) {
            Log.d(Constants.TAG, "matching " + ignoreUrl + " " + forceUrl);

            if ( intent == null )
                return new Matcher(false, Uri.EMPTY);

            Uri data = intent.getData();
            if ( data != null ) {
                String url = data.toString();
                if (url.startsWith("http") || url.startsWith("https"))
                    if ( !ignoreUrl.matcher(url).matches() || forceUrl.matcher(url).matches() )
                        return new Matcher(true, Uri.parse(url));
            }

            Bundle extras = intent.getExtras();
            if ( extras != null ) {
                Object s = extras.get(extraKey);
                if ( s instanceof CharSequence ) {
                    String url = s.toString();
                    if (url.startsWith("http") || url.startsWith("https")) {
                        if ( !ignoreUrl.matcher(url).matches() || forceUrl.matcher(url).matches() )
                            return new Matcher(true, Uri.parse(url));
                    }
                }
                else if ( s instanceof Uri || s instanceof URL ) {
                    String url = s.toString();
                    if (url.startsWith("http") || url.startsWith("https")) {
                        if ( !ignoreUrl.matcher(url).matches() || forceUrl.matcher(url).matches() )
                            return new Matcher(true, Uri.parse(url));
                    }
                }
            }

            return new Matcher(false, Uri.EMPTY);
        }

        static class Matcher {
            private boolean is_matches;
            private Uri uri;

            Matcher(boolean matches, Uri uri) {
                this.is_matches = matches;
                this.uri = uri;
            }

            boolean matches() {
                return is_matches;
            }

            Uri getUri() {
                return uri;
            }
        }
    }
}
