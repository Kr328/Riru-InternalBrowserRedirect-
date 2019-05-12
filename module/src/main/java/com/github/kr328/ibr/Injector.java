package com.github.kr328.ibr;

import android.app.ActivityManager;
import android.app.IActivityManager;
import android.content.Intent;
import android.util.Log;
import android.util.Singleton;
import org.json.JSONException;

import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.util.regex.PatternSyntaxException;

@SuppressWarnings("unused")
public class Injector {
    public static void inject(String configData) {
        if ( injected ) {
            Log.i(Constants.TAG, "Ignore Injected");
            return;
        }

        injected = true;

        Log.i(Constants.TAG, "Injecting");

        try {
            rules = new Rules(configData);
        } catch (JSONException|PatternSyntaxException e) {
            Log.e(Constants.TAG, "Load config failure", e);
            return;
        }

        try {
            install();
        } catch (Exception e) {
            Log.e(Constants.TAG, "Create proxy Interface failure", e);
        }

        Log.i(Constants.TAG, "Inject " + rules.getName() + " Success");
    }

    @SuppressWarnings("JavaReflectionMemberAccess")
    private static void install() throws Exception {
        Field singletonField = ActivityManager.class.getDeclaredField("IActivityManagerSingleton");
        singletonField.setAccessible(true);
        Singleton singleton = (Singleton) singletonField.get(null);
        IActivityManager original = (IActivityManager) singleton.get();
        singletonField.set(null, new Singleton<IActivityManager>() {
            @Override
            protected IActivityManager create() {
                return LocalInterfaceProxy.createInterfaceProxy(original, new Class[]{IActivityManager.class}, Injector::onActivityCalled);
            }
        });
    }

    private static Object onActivityCalled(IActivityManager original, IActivityManager replaced, Method method, Object[] args) throws Exception {
        if (!"startActivity".equals(method.getName()))
            return method.invoke(original, args);

        if (!(args[2] instanceof Intent))
            return method.invoke(original, args);

        Intent intent = (Intent) args[2];

        Rules.Rule.Matcher matcher = rules.matcher(intent);

        if ( matcher.matches() ) {
            intent.setAction(Intent.ACTION_VIEW).setData(matcher.getUri()).setComponent(null);
            Log.i(Constants.TAG, rules.getName() + ": Modified intent " + intent + intent.getExtras());
        }
        else {
            Log.i(Constants.TAG, rules.getName() + ": Ignore intent " + intent + intent.getExtras());
        }

        return method.invoke(original, args);
    }

    private static boolean injected = false;
    private static Rules rules;
}
