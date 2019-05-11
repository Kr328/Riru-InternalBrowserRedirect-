package com.github.kr328.ibr;

import android.app.IActivityManager;
import android.content.Context;
import android.content.Intent;
import android.net.*;
import android.os.Bundle;
import android.os.IBinder;
import android.os.IServiceManager;
import android.os.ServiceManagerNative;
import android.util.Log;
import android.webkit.URLUtil;

import java.lang.reflect.Method;

@SuppressWarnings("unused")
public class Injector {
    public static void inject(String configData) {
        Log.i(Constants.TAG, "Injected " + configData);
    }

    private static Object onActivityServiceCalled(IActivityManager original ,IActivityManager replaced ,Method method ,Object[] args) throws Throwable {
        switch ( method.getName() ) {
            case "startActivity" :
                Intent intent = (Intent) args[2];
                Log.i(Constants.TAG ,"Starting " + intent + " extra " + intent.getExtras());
                args[2] = onStartActivity(intent);
                break;
        }

        return method.invoke(original ,args);
    }

    private static Intent onStartActivity(Intent intent) {
        Bundle extras = intent.getExtras();
        if ( extras == null )
            return intent;

        Object object = extras.get(GlobalConfig.get().getUrlKey());
        Uri uri;

        if ( object == null )
            return intent;

        String url = object.toString();

        if (url.isEmpty())
            return intent;
        else if (!URLUtil.isValidUrl(url))
            return intent;
        else if (GlobalConfig.get().getHostWhitePattern().matcher((uri = Uri.parse(url)).getHost()).matches())
            return intent;
        else if ( lastStartUri.equals(uri) && System.currentTimeMillis() - lastStartTime < 10 * 1000 )
            return intent;

        lastStartUri  = uri;
        lastStartTime = System.currentTimeMillis();

        return new Intent(Intent.ACTION_VIEW).setData(uri);
    }

    public static native IBinder getContextObjectOriginal();
    public static native String  getCurrentConfigPath();

    private static Uri  lastStartUri  = Uri.EMPTY;
    private static long lastStartTime = 0;
}
