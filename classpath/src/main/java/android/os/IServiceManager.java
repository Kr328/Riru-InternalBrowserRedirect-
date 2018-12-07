package android.os;

public interface IServiceManager extends IInterface {
    public abstract IBinder getService(String paramString) throws RemoteException;
}
