package android.nfc.dta;

import android.content.Context;
import android.nfc.INfcDta;
import android.nfc.NfcAdapter;
import android.os.RemoteException;
import android.util.Log;
import java.util.HashMap;
/* loaded from: classes2.dex */
public final class NfcDta {
    public protected static final String TAG = "NfcDta";
    public protected static HashMap<Context, NfcDta> sNfcDtas = new HashMap<>();
    public protected static INfcDta sService;
    public protected final Context mContext;

    public protected synchronized NfcDta(Context context, INfcDta service) {
        this.mContext = context.getApplicationContext();
        sService = service;
    }

    private protected static synchronized NfcDta getInstance(NfcAdapter adapter) {
        NfcDta manager;
        synchronized (NfcDta.class) {
            if (adapter == null) {
                throw new NullPointerException("NfcAdapter is null");
            }
            Context context = adapter.getContext();
            if (context == null) {
                Log.e(TAG, "NfcAdapter context is null.");
                throw new UnsupportedOperationException();
            }
            manager = sNfcDtas.get(context);
            if (manager == null) {
                INfcDta service = adapter.getNfcDtaInterface();
                if (service == null) {
                    Log.e(TAG, "This device does not implement the INfcDta interface.");
                    throw new UnsupportedOperationException();
                }
                manager = new NfcDta(context, service);
                sNfcDtas.put(context, manager);
            }
        }
        return manager;
    }

    private protected synchronized boolean enableDta() {
        try {
            sService.enableDta();
            return true;
        } catch (RemoteException e) {
            return false;
        }
    }

    private protected synchronized boolean disableDta() {
        try {
            sService.disableDta();
            return true;
        } catch (RemoteException e) {
            return false;
        }
    }

    private protected synchronized boolean enableServer(String serviceName, int serviceSap, int miu, int rwSize, int testCaseId) {
        try {
            return sService.enableServer(serviceName, serviceSap, miu, rwSize, testCaseId);
        } catch (RemoteException e) {
            return false;
        }
    }

    private protected synchronized boolean disableServer() {
        try {
            sService.disableServer();
            return true;
        } catch (RemoteException e) {
            return false;
        }
    }

    private protected synchronized boolean enableClient(String serviceName, int miu, int rwSize, int testCaseId) {
        try {
            return sService.enableClient(serviceName, miu, rwSize, testCaseId);
        } catch (RemoteException e) {
            return false;
        }
    }

    private protected synchronized boolean disableClient() {
        try {
            sService.disableClient();
            return true;
        } catch (RemoteException e) {
            return false;
        }
    }

    private protected synchronized boolean registerMessageService(String msgServiceName) {
        try {
            return sService.registerMessageService(msgServiceName);
        } catch (RemoteException e) {
            return false;
        }
    }
}
