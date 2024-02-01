package android.nfc.cardemulation;

import android.app.Activity;
import android.app.ActivityThread;
import android.content.ComponentName;
import android.content.Context;
import android.content.pm.IPackageManager;
import android.nfc.INfcCardEmulation;
import android.nfc.NfcAdapter;
import android.os.RemoteException;
import android.provider.Settings;
import android.util.Log;
import com.android.internal.telephony.PhoneConstants;
import java.util.HashMap;
import java.util.List;
/* loaded from: classes2.dex */
public final class CardEmulation {
    public static final String ACTION_CHANGE_DEFAULT = "android.nfc.cardemulation.action.ACTION_CHANGE_DEFAULT";
    public static final String CATEGORY_OTHER = "other";
    public static final String CATEGORY_PAYMENT = "payment";
    public static final String EXTRA_CATEGORY = "category";
    public static final String EXTRA_SERVICE_COMPONENT = "component";
    public static final int SELECTION_MODE_ALWAYS_ASK = 1;
    public static final int SELECTION_MODE_ASK_IF_CONFLICT = 2;
    public static final int SELECTION_MODE_PREFER_DEFAULT = 0;
    static final String TAG = "CardEmulation";
    static INfcCardEmulation sService;
    final Context mContext;
    static boolean sIsInitialized = false;
    static HashMap<Context, CardEmulation> sCardEmus = new HashMap<>();

    private synchronized CardEmulation(Context context, INfcCardEmulation service) {
        this.mContext = context.getApplicationContext();
        sService = service;
    }

    public static synchronized CardEmulation getInstance(NfcAdapter adapter) {
        CardEmulation manager;
        synchronized (CardEmulation.class) {
            if (adapter == null) {
                throw new NullPointerException("NfcAdapter is null");
            }
            Context context = adapter.getContext();
            if (context == null) {
                Log.e(TAG, "NfcAdapter context is null.");
                throw new UnsupportedOperationException();
            }
            if (!sIsInitialized) {
                IPackageManager pm = ActivityThread.getPackageManager();
                if (pm == null) {
                    Log.e(TAG, "Cannot get PackageManager");
                    throw new UnsupportedOperationException();
                }
                try {
                    if (!pm.hasSystemFeature("android.hardware.nfc.hce", 0)) {
                        Log.e(TAG, "This device does not support card emulation");
                        throw new UnsupportedOperationException();
                    }
                    sIsInitialized = true;
                } catch (RemoteException e) {
                    Log.e(TAG, "PackageManager query failed.");
                    throw new UnsupportedOperationException();
                }
            }
            manager = sCardEmus.get(context);
            if (manager == null) {
                INfcCardEmulation service = adapter.getCardEmulationService();
                if (service == null) {
                    Log.e(TAG, "This device does not implement the INfcCardEmulation interface.");
                    throw new UnsupportedOperationException();
                }
                manager = new CardEmulation(context, service);
                sCardEmus.put(context, manager);
            }
        }
        return manager;
    }

    public boolean isDefaultServiceForCategory(ComponentName service, String category) {
        try {
            return sService.isDefaultServiceForCategory(this.mContext.getUserId(), service, category);
        } catch (RemoteException e) {
            recoverService();
            if (sService == null) {
                Log.e(TAG, "Failed to recover CardEmulationService.");
                return false;
            }
            try {
                return sService.isDefaultServiceForCategory(this.mContext.getUserId(), service, category);
            } catch (RemoteException e2) {
                Log.e(TAG, "Failed to recover CardEmulationService.");
                return false;
            }
        }
    }

    public boolean isDefaultServiceForAid(ComponentName service, String aid) {
        try {
            return sService.isDefaultServiceForAid(this.mContext.getUserId(), service, aid);
        } catch (RemoteException e) {
            recoverService();
            if (sService == null) {
                Log.e(TAG, "Failed to recover CardEmulationService.");
                return false;
            }
            try {
                return sService.isDefaultServiceForAid(this.mContext.getUserId(), service, aid);
            } catch (RemoteException e2) {
                Log.e(TAG, "Failed to reach CardEmulationService.");
                return false;
            }
        }
    }

    public boolean categoryAllowsForegroundPreference(String category) {
        if (CATEGORY_PAYMENT.equals(category)) {
            try {
                boolean preferForeground = Settings.Secure.getInt(this.mContext.getContentResolver(), Settings.Secure.NFC_PAYMENT_FOREGROUND) != 0;
                return preferForeground;
            } catch (Settings.SettingNotFoundException e) {
                return false;
            }
        }
        return true;
    }

    public int getSelectionModeForCategory(String category) {
        if (CATEGORY_PAYMENT.equals(category)) {
            String defaultComponent = Settings.Secure.getString(this.mContext.getContentResolver(), "nfc_payment_default_component");
            if (defaultComponent != null) {
                return 0;
            }
            return 1;
        }
        return 2;
    }

    public boolean registerAidsForService(ComponentName service, String category, List<String> aids) {
        AidGroup aidGroup = new AidGroup(aids, category);
        try {
            return sService.registerAidGroupForService(this.mContext.getUserId(), service, aidGroup);
        } catch (RemoteException e) {
            recoverService();
            if (sService == null) {
                Log.e(TAG, "Failed to recover CardEmulationService.");
                return false;
            }
            try {
                return sService.registerAidGroupForService(this.mContext.getUserId(), service, aidGroup);
            } catch (RemoteException e2) {
                Log.e(TAG, "Failed to reach CardEmulationService.");
                return false;
            }
        }
    }

    public List<String> getAidsForService(ComponentName service, String category) {
        try {
            AidGroup group = sService.getAidGroupForService(this.mContext.getUserId(), service, category);
            if (group != null) {
                return group.getAids();
            }
            return null;
        } catch (RemoteException e) {
            recoverService();
            if (sService == null) {
                Log.e(TAG, "Failed to recover CardEmulationService.");
                return null;
            }
            try {
                AidGroup group2 = sService.getAidGroupForService(this.mContext.getUserId(), service, category);
                if (group2 != null) {
                    return group2.getAids();
                }
                return null;
            } catch (RemoteException e2) {
                Log.e(TAG, "Failed to recover CardEmulationService.");
                return null;
            }
        }
    }

    public boolean removeAidsForService(ComponentName service, String category) {
        try {
            return sService.removeAidGroupForService(this.mContext.getUserId(), service, category);
        } catch (RemoteException e) {
            recoverService();
            if (sService == null) {
                Log.e(TAG, "Failed to recover CardEmulationService.");
                return false;
            }
            try {
                return sService.removeAidGroupForService(this.mContext.getUserId(), service, category);
            } catch (RemoteException e2) {
                Log.e(TAG, "Failed to reach CardEmulationService.");
                return false;
            }
        }
    }

    public boolean setPreferredService(Activity activity, ComponentName service) {
        if (activity == null || service == null) {
            throw new NullPointerException("activity or service or category is null");
        }
        if (!activity.isResumed()) {
            throw new IllegalArgumentException("Activity must be resumed.");
        }
        try {
            return sService.setPreferredService(service);
        } catch (RemoteException e) {
            recoverService();
            if (sService == null) {
                Log.e(TAG, "Failed to recover CardEmulationService.");
                return false;
            }
            try {
                return sService.setPreferredService(service);
            } catch (RemoteException e2) {
                Log.e(TAG, "Failed to reach CardEmulationService.");
                return false;
            }
        }
    }

    public boolean unsetPreferredService(Activity activity) {
        if (activity == null) {
            throw new NullPointerException("activity is null");
        }
        if (!activity.isResumed()) {
            throw new IllegalArgumentException("Activity must be resumed.");
        }
        try {
            return sService.unsetPreferredService();
        } catch (RemoteException e) {
            recoverService();
            if (sService == null) {
                Log.e(TAG, "Failed to recover CardEmulationService.");
                return false;
            }
            try {
                return sService.unsetPreferredService();
            } catch (RemoteException e2) {
                Log.e(TAG, "Failed to reach CardEmulationService.");
                return false;
            }
        }
    }

    public boolean supportsAidPrefixRegistration() {
        try {
            return sService.supportsAidPrefixRegistration();
        } catch (RemoteException e) {
            recoverService();
            if (sService == null) {
                Log.e(TAG, "Failed to recover CardEmulationService.");
                return false;
            }
            try {
                return sService.supportsAidPrefixRegistration();
            } catch (RemoteException e2) {
                Log.e(TAG, "Failed to reach CardEmulationService.");
                return false;
            }
        }
    }

    public synchronized boolean setDefaultServiceForCategory(ComponentName service, String category) {
        try {
            return sService.setDefaultServiceForCategory(this.mContext.getUserId(), service, category);
        } catch (RemoteException e) {
            recoverService();
            if (sService == null) {
                Log.e(TAG, "Failed to recover CardEmulationService.");
                return false;
            }
            try {
                return sService.setDefaultServiceForCategory(this.mContext.getUserId(), service, category);
            } catch (RemoteException e2) {
                Log.e(TAG, "Failed to reach CardEmulationService.");
                return false;
            }
        }
    }

    public synchronized boolean setDefaultForNextTap(ComponentName service) {
        try {
            return sService.setDefaultForNextTap(this.mContext.getUserId(), service);
        } catch (RemoteException e) {
            recoverService();
            if (sService == null) {
                Log.e(TAG, "Failed to recover CardEmulationService.");
                return false;
            }
            try {
                return sService.setDefaultForNextTap(this.mContext.getUserId(), service);
            } catch (RemoteException e2) {
                Log.e(TAG, "Failed to reach CardEmulationService.");
                return false;
            }
        }
    }

    public synchronized List<ApduServiceInfo> getServices(String category) {
        try {
            return sService.getServices(this.mContext.getUserId(), category);
        } catch (RemoteException e) {
            recoverService();
            if (sService == null) {
                Log.e(TAG, "Failed to recover CardEmulationService.");
                return null;
            }
            try {
                return sService.getServices(this.mContext.getUserId(), category);
            } catch (RemoteException e2) {
                Log.e(TAG, "Failed to reach CardEmulationService.");
                return null;
            }
        }
    }

    public static synchronized boolean isValidAid(String aid) {
        if (aid == null) {
            return false;
        }
        if ((aid.endsWith(PhoneConstants.APN_TYPE_ALL) || aid.endsWith("#")) && aid.length() % 2 == 0) {
            Log.e(TAG, "AID " + aid + " is not a valid AID.");
            return false;
        } else if (!aid.endsWith(PhoneConstants.APN_TYPE_ALL) && !aid.endsWith("#") && aid.length() % 2 != 0) {
            Log.e(TAG, "AID " + aid + " is not a valid AID.");
            return false;
        } else if (!aid.matches("[0-9A-Fa-f]{10,32}\\*?\\#?")) {
            Log.e(TAG, "AID " + aid + " is not a valid AID.");
            return false;
        } else {
            return true;
        }
    }

    synchronized void recoverService() {
        NfcAdapter adapter = NfcAdapter.getDefaultAdapter(this.mContext);
        sService = adapter.getCardEmulationService();
    }
}
