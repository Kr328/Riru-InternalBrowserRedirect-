package android.os;

public class ServiceManagerNative extends Binder implements IServiceManager {
    public static IServiceManager asInterface(IBinder binder) {
        throw new IllegalArgumentException("Unsupported");
    }

    @Override
    public IBinder getService(String paramString) throws RemoteException {
        throw new IllegalArgumentException("Unsupported");
    }

    @Override
    public IBinder asBinder() {
        throw new IllegalArgumentException("Unsupported");
    }
}
