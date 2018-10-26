package me.kr328.ibr;

import android.content.IContentProvider;

import java.lang.reflect.Method;
import java.lang.reflect.Proxy;

public class ContentProviderProxy {
    public static IContentProvider createContentProviderProxy(IContentProvider original , ContentProviderCallback callback) {
        return (IContentProvider) Proxy.newProxyInstance(ContentProviderProxy.class.getClassLoader() ,new Class[]{IContentProvider.class} ,(thiz , method , args) -> {
           try {
               return callback.onCall(original , (IContentProvider) thiz,method ,args);
           }
           catch ( Throwable throwable ) {}
           return method.invoke(original ,args);
        });
    }

    public interface ContentProviderCallback {
        Object onCall(IContentProvider original , IContentProvider replaced , Method method ,Object[] args) throws Throwable;
    }
}
