package me.kr328.ibr;

import android.app.IActivityManager;
import android.content.ContentValues;
import android.content.Context;
import android.content.Intent;
import android.net.IConnectivityManager;
import android.net.Uri;
import android.os.IBinder;
import android.os.IServiceManager;
import android.os.ServiceManager;
import android.os.ServiceManagerNative;
import android.util.Log;
import android.webkit.URLUtil;
import com.android.internal.statusbar.IStatusBarService;

import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.util.Map;

@SuppressWarnings("unused")
public class Injector {
    private static boolean isInitial = false;

    public static IBinder getContextObjectHooked() {
        String currentPackage = getCurrentPackage();

        Log.i(Global.TAG, "Apply " + currentPackage + ": " + android.os.Process.myPid());

        if (currentPackage.equals("com.android.systemui"))
            return handleVpnIconHide();
        else
            return handleInternalBrowserRedirect(currentPackage);
    }

    private static IBinder handleInternalBrowserRedirect(String currentPackage) {
        String currentConfig = String.format("/data/misc/riru/modules/ibr/config.%s.json", currentPackage);

        GlobalConfig.load(currentConfig);
        GlobalConfig.get().dump();

        return LocalInterfaceProxyFactory.
                createInterfaceProxyBinder(ServiceManagerNative.asInterface(getContextObjectOriginal()),
                        IServiceManager.class.getName(),
                        (original, replaced, method, args) -> {
                            if ("getService".equals(method.getName())) {
                                switch (args[0].toString()) {
                                    case Context.ACTIVITY_SERVICE:
                                        return LocalInterfaceProxyFactory.
                                                createInterfaceProxyBinder(IActivityManager.Stub.asInterface(original.getService(Context.ACTIVITY_SERVICE)),
                                                        IActivityManager.class.getName(),
                                                        Injector::onActivityServiceCalled);
                                }
                            }
                            return method.invoke(original, args);
                        });
    }

    private static IBinder handleVpnIconHide() {
        return LocalInterfaceProxyFactory.
                createInterfaceProxyBinder(ServiceManagerNative.asInterface(getContextObjectOriginal()),
                        IServiceManager.class.getName(),
                        ((original, replaced, method, args) -> {
                            if ("getService".equals(method.getName()) ) {
                                //Log.i(Global.TAG ,"getService " + args[0]);
                                switch (args[0].toString()) {
                                    case Context.STATUS_BAR_SERVICE:
                                        return LocalInterfaceProxyFactory.
                                                createInterfaceProxyBinder(IStatusBarService.Stub.asInterface(original.getService(Context.STATUS_BAR_SERVICE)),
                                                        IConnectivityManager.class.getName(),
                                                        Injector::onConnectivityServiceCalled);
                                }
                            }
                            return method.invoke(original, args);
                        }));
    }

    private static Object onActivityServiceCalled(IActivityManager original, IActivityManager replaced, Method method, Object[] args) throws Throwable {
        switch (method.getName()) {
            case "startActivity":
                Intent intent = (Intent) args[2];
                Log.i(Global.TAG, "Starting " + intent + " extra " + intent.getExtras());
                args[2] = onStartActivity(intent);
                break;
        }

        return method.invoke(original, args);
    }

    private static Object onConnectivityServiceCalled(IStatusBarService original ,IStatusBarService replaced ,Method method ,Object[] args) throws Throwable {
        Log.i(Global.TAG ,"Method " + method + " called");

        switch (method.getName()) {
            case "getVpnConfig" :
                Log.i(Global.TAG ,"getVpnConfig Called");
                return null;
        }

        return method.invoke(original);
    }

    private static Intent onStartActivity(Intent intent) {
        Object object = intent.getExtras().get(GlobalConfig.get().getUrlKey());
        Uri uri;

        if (object == null)
            return intent;

        String url = object.toString();

        if (url.isEmpty())
            return intent;
        else if (!(URLUtil.isHttpUrl(url) || URLUtil.isHttpsUrl(url)))
            return intent;
        else if (GlobalConfig.get().getHostWhitePattern().matcher((uri = Uri.parse(url)).getHost()).matches())
            return intent;
        else if (lastStartUri.equals(uri) && System.currentTimeMillis() - lastStartTime < 5 * 1000)
            return intent;

        lastStartUri = uri;
        lastStartTime = System.currentTimeMillis();

        return new Intent(Intent.ACTION_VIEW).setData(uri);
    }

    public static native IBinder getContextObjectOriginal();

    public static native String getCurrentPackage();

    private static Uri lastStartUri = Uri.EMPTY;
    private static long lastStartTime = 0;
}
