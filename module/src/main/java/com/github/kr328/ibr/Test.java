package com.github.kr328.ibr;

import android.content.Intent;
import android.net.Uri;

public class Test {
    public static void main(String[] args) throws Exception {
        String config = "{\n" +
                "\t\"name\": \"WeChat\",\n" +
                "\t\"rules\": [\n" +
                "\t\t{\n" +
                "\t\t\t\"extra-key\": \"rawUrl\",\n" +
                "\t\t\t\"ignore-url\": \".*qq\\\\.com.*\",\n" +
                "\t\t\t\"force-url\": \"\"\n" +
                "\t\t}\n" +
                "\t]\n" +
                "} ";

        Rules rules = new Rules(config);

        Rules.Rule.Matcher matcher = rules.matcher(new Intent(Intent.ACTION_VIEW));

        System.out.println(matcher.matches());
    }
}
