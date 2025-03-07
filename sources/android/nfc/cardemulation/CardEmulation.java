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
import java.util.HashMap;
import java.util.List;
import java.util.regex.Pattern;

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
    private static final Pattern AID_PATTERN = Pattern.compile("[0-9A-Fa-f]{10,32}\\*?\\#?");
    static boolean sIsInitialized = false;
    static HashMap<Context, CardEmulation> sCardEmus = new HashMap<>();

    private CardEmulation(Context context, INfcCardEmulation service) {
        this.mContext = context.getApplicationContext();
        sService = service;
    }

    public static synchronized CardEmulation getInstance(NfcAdapter adapter) {
        CardEmulation manager;
        synchronized (CardEmulation.class) {
            try {
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
            } catch (Throwable th) {
                throw th;
            }
        }
        return manager;
    }

    public boolean isDefaultServiceForCategory(ComponentName service, String category) {
        try {
            return sService.isDefaultServiceForCategory(this.mContext.getUserId(), service, category);
        } catch (RemoteException e) {
            recoverService();
            INfcCardEmulation iNfcCardEmulation = sService;
            if (iNfcCardEmulation == null) {
                Log.e(TAG, "Failed to recover CardEmulationService.");
                return false;
            }
            try {
                return iNfcCardEmulation.isDefaultServiceForCategory(this.mContext.getUserId(), service, category);
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
            INfcCardEmulation iNfcCardEmulation = sService;
            if (iNfcCardEmulation == null) {
                Log.e(TAG, "Failed to recover CardEmulationService.");
                return false;
            }
            try {
                return iNfcCardEmulation.isDefaultServiceForAid(this.mContext.getUserId(), service, aid);
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
            String defaultComponent = Settings.Secure.getString(this.mContext.getContentResolver(), Settings.Secure.NFC_PAYMENT_DEFAULT_COMPONENT);
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
            INfcCardEmulation iNfcCardEmulation = sService;
            if (iNfcCardEmulation == null) {
                Log.e(TAG, "Failed to recover CardEmulationService.");
                return false;
            }
            try {
                return iNfcCardEmulation.registerAidGroupForService(this.mContext.getUserId(), service, aidGroup);
            } catch (RemoteException e2) {
                Log.e(TAG, "Failed to reach CardEmulationService.");
                return false;
            }
        }
    }

    public boolean unsetOffHostForService(ComponentName service) {
        NfcAdapter adapter = NfcAdapter.getDefaultAdapter(this.mContext);
        if (adapter == null) {
            return false;
        }
        try {
            return sService.unsetOffHostForService(this.mContext.getUserId(), service);
        } catch (RemoteException e) {
            recoverService();
            INfcCardEmulation iNfcCardEmulation = sService;
            if (iNfcCardEmulation == null) {
                Log.e(TAG, "Failed to recover CardEmulationService.");
                return false;
            }
            try {
                return iNfcCardEmulation.unsetOffHostForService(this.mContext.getUserId(), service);
            } catch (RemoteException e2) {
                Log.e(TAG, "Failed to reach CardEmulationService.");
                return false;
            }
        }
    }

    public boolean setOffHostForService(ComponentName service, String offHostSecureElement) {
        NfcAdapter adapter = NfcAdapter.getDefaultAdapter(this.mContext);
        if (adapter == null || offHostSecureElement == null) {
            return false;
        }
        List<String> validSE = adapter.getSupportedOffHostSecureElements();
        if ((offHostSecureElement.startsWith("eSE") && !validSE.contains("eSE")) || (offHostSecureElement.startsWith("SIM") && !validSE.contains("SIM"))) {
            return false;
        }
        if (!offHostSecureElement.startsWith("eSE") && !offHostSecureElement.startsWith("SIM")) {
            return false;
        }
        if (offHostSecureElement.equals("eSE")) {
            offHostSecureElement = "eSE1";
        } else if (offHostSecureElement.equals("SIM")) {
            offHostSecureElement = "SIM1";
        }
        try {
            return sService.setOffHostForService(this.mContext.getUserId(), service, offHostSecureElement);
        } catch (RemoteException e) {
            recoverService();
            INfcCardEmulation iNfcCardEmulation = sService;
            if (iNfcCardEmulation == null) {
                Log.e(TAG, "Failed to recover CardEmulationService.");
                return false;
            }
            try {
                return iNfcCardEmulation.setOffHostForService(this.mContext.getUserId(), service, offHostSecureElement);
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
            INfcCardEmulation iNfcCardEmulation = sService;
            if (iNfcCardEmulation == null) {
                Log.e(TAG, "Failed to recover CardEmulationService.");
                return null;
            }
            try {
                AidGroup group2 = iNfcCardEmulation.getAidGroupForService(this.mContext.getUserId(), service, category);
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
            INfcCardEmulation iNfcCardEmulation = sService;
            if (iNfcCardEmulation == null) {
                Log.e(TAG, "Failed to recover CardEmulationService.");
                return false;
            }
            try {
                return iNfcCardEmulation.removeAidGroupForService(this.mContext.getUserId(), service, category);
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
            INfcCardEmulation iNfcCardEmulation = sService;
            if (iNfcCardEmulation == null) {
                Log.e(TAG, "Failed to recover CardEmulationService.");
                return false;
            }
            try {
                return iNfcCardEmulation.setPreferredService(service);
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
            INfcCardEmulation iNfcCardEmulation = sService;
            if (iNfcCardEmulation == null) {
                Log.e(TAG, "Failed to recover CardEmulationService.");
                return false;
            }
            try {
                return iNfcCardEmulation.unsetPreferredService();
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
            INfcCardEmulation iNfcCardEmulation = sService;
            if (iNfcCardEmulation == null) {
                Log.e(TAG, "Failed to recover CardEmulationService.");
                return false;
            }
            try {
                return iNfcCardEmulation.supportsAidPrefixRegistration();
            } catch (RemoteException e2) {
                Log.e(TAG, "Failed to reach CardEmulationService.");
                return false;
            }
        }
    }

    public boolean setDefaultServiceForCategory(ComponentName service, String category) {
        try {
            return sService.setDefaultServiceForCategory(this.mContext.getUserId(), service, category);
        } catch (RemoteException e) {
            recoverService();
            INfcCardEmulation iNfcCardEmulation = sService;
            if (iNfcCardEmulation == null) {
                Log.e(TAG, "Failed to recover CardEmulationService.");
                return false;
            }
            try {
                return iNfcCardEmulation.setDefaultServiceForCategory(this.mContext.getUserId(), service, category);
            } catch (RemoteException e2) {
                Log.e(TAG, "Failed to reach CardEmulationService.");
                return false;
            }
        }
    }

    public boolean setDefaultForNextTap(ComponentName service) {
        try {
            return sService.setDefaultForNextTap(this.mContext.getUserId(), service);
        } catch (RemoteException e) {
            recoverService();
            INfcCardEmulation iNfcCardEmulation = sService;
            if (iNfcCardEmulation == null) {
                Log.e(TAG, "Failed to recover CardEmulationService.");
                return false;
            }
            try {
                return iNfcCardEmulation.setDefaultForNextTap(this.mContext.getUserId(), service);
            } catch (RemoteException e2) {
                Log.e(TAG, "Failed to reach CardEmulationService.");
                return false;
            }
        }
    }

    public List<ApduServiceInfo> getServices(String category) {
        try {
            return sService.getServices(this.mContext.getUserId(), category);
        } catch (RemoteException e) {
            recoverService();
            INfcCardEmulation iNfcCardEmulation = sService;
            if (iNfcCardEmulation == null) {
                Log.e(TAG, "Failed to recover CardEmulationService.");
                return null;
            }
            try {
                return iNfcCardEmulation.getServices(this.mContext.getUserId(), category);
            } catch (RemoteException e2) {
                Log.e(TAG, "Failed to reach CardEmulationService.");
                return null;
            }
        }
    }

    public static boolean isValidAid(String aid) {
        if (aid == null) {
            return false;
        }
        if ((aid.endsWith("*") || aid.endsWith("#")) && aid.length() % 2 == 0) {
            Log.e(TAG, "AID " + aid + " is not a valid AID.");
            return false;
        } else if (!aid.endsWith("*") && !aid.endsWith("#") && aid.length() % 2 != 0) {
            Log.e(TAG, "AID " + aid + " is not a valid AID.");
            return false;
        } else if (!AID_PATTERN.matcher(aid).matches()) {
            Log.e(TAG, "AID " + aid + " is not a valid AID.");
            return false;
        } else {
            return true;
        }
    }

    void recoverService() {
        NfcAdapter adapter = NfcAdapter.getDefaultAdapter(this.mContext);
        sService = adapter.getCardEmulationService();
    }
}
