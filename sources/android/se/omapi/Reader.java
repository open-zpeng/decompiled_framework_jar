package android.se.omapi;

import android.os.RemoteException;
import android.os.ServiceSpecificException;
import android.util.Log;
import java.io.IOException;

/* loaded from: classes2.dex */
public final class Reader {
    private static final String TAG = "OMAPI.Reader";
    private final Object mLock = new Object();
    private final String mName;
    private ISecureElementReader mReader;
    private final SEService mService;

    /* JADX INFO: Access modifiers changed from: package-private */
    public Reader(SEService service, String name, ISecureElementReader reader) {
        if (reader == null || service == null || name == null) {
            throw new IllegalArgumentException("Parameters cannot be null");
        }
        this.mName = name;
        this.mService = service;
        this.mReader = reader;
    }

    public String getName() {
        return this.mName;
    }

    public Session openSession() throws IOException {
        Session session;
        if (!this.mService.isConnected()) {
            throw new IllegalStateException("service is not connected");
        }
        synchronized (this.mLock) {
            try {
                try {
                    ISecureElementSession session2 = this.mReader.openSession();
                    if (session2 == null) {
                        throw new IOException("service session is null.");
                    }
                    session = new Session(this.mService, session2, this);
                } catch (RemoteException e) {
                    throw new IllegalStateException(e.getMessage());
                } catch (ServiceSpecificException e2) {
                    throw new IOException(e2.getMessage());
                }
            } catch (Throwable th) {
                throw th;
            }
        }
        return session;
    }

    public boolean isSecureElementPresent() {
        if (!this.mService.isConnected()) {
            throw new IllegalStateException("service is not connected");
        }
        try {
            return this.mReader.isSecureElementPresent();
        } catch (RemoteException e) {
            throw new IllegalStateException("Error in isSecureElementPresent()");
        }
    }

    public SEService getSEService() {
        return this.mService;
    }

    public void closeSessions() {
        if (!this.mService.isConnected()) {
            Log.e(TAG, "service is not connected");
            return;
        }
        synchronized (this.mLock) {
            try {
                this.mReader.closeSessions();
            } catch (RemoteException e) {
            }
        }
    }
}
