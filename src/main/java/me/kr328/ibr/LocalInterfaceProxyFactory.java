package me.kr328.ibr;

import android.os.IBinder;
import android.os.IInterface;
import android.util.Log;

import java.lang.reflect.InvocationHandler;
import java.lang.reflect.Method;
import java.lang.reflect.Proxy;

@SuppressWarnings("unchecked")
public class LocalInterfaceProxyFactory {
    public static <Interface extends IInterface> IBinder createInterfaceProxyBinder(Interface original , String interfaceName , InterfaceCallback<Interface> callback) {
        IBinder originalBinder = original.asBinder();

        InvocationHandler interfaceInvocationHandler = (Object self ,Method method ,Object[] args) -> callback.onCalled(original , (Interface) self ,method ,args);

        InvocationHandler binderInvocationHandler = (Object self, Method method, Object[] args) -> {
            try {
                if ( method.getName().equals("queryLocalInterface") ) {
                    //Log.i(Global.TAG ,"queryLocalInterface " + args[0] + " == " + interfaceName);
                    if ( interfaceName.equals(args[0]) )
                        return Proxy.newProxyInstance(LocalInterfaceProxyFactory.class.getClassLoader() ,
                                new Class[]{Class.forName(interfaceName)} ,
                                interfaceInvocationHandler);
                }
            }
            catch (Exception ignored) {
                Log.w(Global.TAG ,"Proxy " + original.getClass().getName() + " failure.");
            }

            return method.invoke(originalBinder ,args);
        };

        return (IBinder) Proxy.newProxyInstance(LocalInterfaceProxyFactory.class.getClassLoader() ,
                new Class[]{IBinder.class} ,
                binderInvocationHandler);
    }

    public interface InterfaceCallback<Interface extends IInterface> {
        Object onCalled(Interface original ,Interface replaced ,Method method ,Object[] args) throws Throwable;
    }
}
