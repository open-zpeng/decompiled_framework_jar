package android.os;

import android.annotation.UnsupportedAppUsage;
import android.os.IPermissionController;

/* loaded from: classes2.dex */
public abstract class ServiceManagerNative extends Binder implements IServiceManager {
    @UnsupportedAppUsage
    public static IServiceManager asInterface(IBinder obj) {
        if (obj == null) {
            return null;
        }
        IServiceManager in = (IServiceManager) obj.queryLocalInterface(IServiceManager.descriptor);
        if (in != null) {
            return in;
        }
        return new ServiceManagerProxy(obj);
    }

    public ServiceManagerNative() {
        attachInterface(this, IServiceManager.descriptor);
    }

    @Override // android.os.Binder
    public boolean onTransact(int code, Parcel data, Parcel reply, int flags) {
        if (code == 1) {
            data.enforceInterface(IServiceManager.descriptor);
            String name = data.readString();
            IBinder service = getService(name);
            reply.writeStrongBinder(service);
            return true;
        } else if (code == 2) {
            data.enforceInterface(IServiceManager.descriptor);
            String name2 = data.readString();
            IBinder service2 = checkService(name2);
            reply.writeStrongBinder(service2);
            return true;
        } else if (code == 3) {
            data.enforceInterface(IServiceManager.descriptor);
            String name3 = data.readString();
            IBinder service3 = data.readStrongBinder();
            boolean allowIsolated = data.readInt() != 0;
            int dumpPriority = data.readInt();
            addService(name3, service3, allowIsolated, dumpPriority);
            return true;
        } else if (code != 4) {
            if (code == 6) {
                data.enforceInterface(IServiceManager.descriptor);
                IPermissionController controller = IPermissionController.Stub.asInterface(data.readStrongBinder());
                setPermissionController(controller);
                return true;
            }
            return false;
        } else {
            data.enforceInterface(IServiceManager.descriptor);
            int dumpPriority2 = data.readInt();
            String[] list = listServices(dumpPriority2);
            reply.writeStringArray(list);
            return true;
        }
    }

    @Override // android.os.IInterface
    public IBinder asBinder() {
        return this;
    }
}
