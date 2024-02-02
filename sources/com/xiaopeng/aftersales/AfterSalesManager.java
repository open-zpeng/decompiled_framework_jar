package com.xiaopeng.aftersales;

import android.content.Context;
import android.os.RemoteException;
import android.util.ArrayMap;
import com.android.internal.util.Preconditions;
import com.xiaopeng.aftersales.IAlertListener;
import com.xiaopeng.aftersales.IAuthModeListener;
import com.xiaopeng.aftersales.IEncryptShListener;
import com.xiaopeng.aftersales.ILogicActionListener;
import com.xiaopeng.aftersales.ILogicTreeUpgrader;
import com.xiaopeng.aftersales.IRepairModeListener;
import com.xiaopeng.aftersales.IShellCmdListener;
/* loaded from: classes3.dex */
public class AfterSalesManager {
    public static final int CMD_TYPE_CAT = 2;
    public static final int CMD_TYPE_COMMON = 0;
    public static final int CMD_TYPE_DF = 3;
    public static final int CMD_TYPE_DU = 5;
    public static final int CMD_TYPE_GETPROP = 1;
    public static final int CMD_TYPE_IFCONFIG = 9;
    public static final int CMD_TYPE_LS_AL = 8;
    public static final int CMD_TYPE_MOUNT = 4;
    public static final int CMD_TYPE_TOP_CPU = 6;
    public static final int CMD_TYPE_TOP_MEM = 7;
    public static final int ERROR_CODE_MODULE_4G = 10;
    public static final int ERROR_CODE_MODULE_AUDIO = 1;
    public static final int ERROR_CODE_MODULE_BLUETOOTH = 3;
    public static final int ERROR_CODE_MODULE_CAMERA = 2;
    public static final int ERROR_CODE_MODULE_CARSERVICE = 14;
    public static final int ERROR_CODE_MODULE_ICM_AUDIO = 17;
    public static final int ERROR_CODE_MODULE_ICM_ETH = 18;
    public static final int ERROR_CODE_MODULE_ICM_LCD = 16;
    public static final int ERROR_CODE_MODULE_ICM_SYSTEM = 19;
    public static final int ERROR_CODE_MODULE_LCD = 8;
    public static final int ERROR_CODE_MODULE_LIBHTTP = 6;
    public static final int ERROR_CODE_MODULE_NAVI = 9;
    public static final int ERROR_CODE_MODULE_PHY = 15;
    public static final int ERROR_CODE_MODULE_PM = 12;
    public static final int ERROR_CODE_MODULE_PSO = 5;
    public static final int ERROR_CODE_MODULE_SOC = 7;
    public static final int ERROR_CODE_MODULE_UFS = 11;
    public static final int ERROR_CODE_MODULE_USB = 13;
    public static final int ERROR_CODE_MODULE_WIFI = 4;
    public static final int MAX_ERROR_CODE_MODULE = 19;
    public static final int RESULT_FAIL = 0;
    public static final int RESULT_KEEP = 2;
    public static final int RESULT_PASS = 1;
    public static final int RESULT_UNKNOWN = -1;
    public static final String SERVICE_NAME = "xiaopeng_aftersales";
    private static final String TAG = "AfterSalesManager";
    private IAfterSalesManager mAfterSalesService;
    private Context mContext;
    private final ArrayMap<AlertListener, IAlertListener> mAlertListeners = new ArrayMap<>();
    private final ArrayMap<RepairModeListener, IRepairModeListener> mRepairModeListeners = new ArrayMap<>();
    private final ArrayMap<ShellCmdListener, IShellCmdListener> mShellCmdListeners = new ArrayMap<>();
    private final ArrayMap<EncryptShListener, IEncryptShListener> mEncryptShListeners = new ArrayMap<>();
    private final ArrayMap<AuthModeListener, IAuthModeListener> mAuthModeListeners = new ArrayMap<>();
    private final ArrayMap<LogicActionListener, ILogicActionListener> mLogicActionListeners = new ArrayMap<>();
    private final ArrayMap<LogicTreeUpgrader, ILogicTreeUpgrader> mLogicTreeUpgraders = new ArrayMap<>();

    public AfterSalesManager(Context context, IAfterSalesManager service) {
        this.mContext = context;
        this.mAfterSalesService = service;
    }

    public void addAlertListener(final AlertListener l) {
        IAlertListener rl = new IAlertListener.Stub() { // from class: com.xiaopeng.aftersales.AfterSalesManager.1
            @Override // com.xiaopeng.aftersales.IAlertListener
            public void alertDiagnosisError(int module, int errorCode, long time, String errorMsg) throws RemoteException {
                l.alertDiagnosisError(module, errorCode, time, errorMsg);
            }
        };
        try {
            this.mAfterSalesService.registerAlertListener(rl);
            this.mAlertListeners.put(l, rl);
        } catch (RemoteException e) {
            throw e.rethrowFromSystemServer();
        }
    }

    public void removeAlertListener(AlertListener l) {
        IAlertListener rl = this.mAlertListeners.get(l);
        Preconditions.checkArgument(rl != null, "AlertListener was not registered.");
        try {
            this.mAfterSalesService.unregisterAlertListener(rl);
        } catch (RemoteException e) {
            throw e.rethrowFromSystemServer();
        }
    }

    public void recordDiagnosisError(int module, int errorCode, long millis, String errorMsg, boolean alert) {
        if (this.mAfterSalesService != null) {
            try {
                this.mAfterSalesService.recordDiagnosisError(module, errorCode, millis, errorMsg, alert);
            } catch (RemoteException e) {
                throw e.rethrowFromSystemServer();
            }
        }
    }

    public void updateDiagnosisUploadStatus(int module, boolean result, int errorCode, long millis, String errorMsg) {
        if (this.mAfterSalesService != null) {
            try {
                this.mAfterSalesService.updateDiagnosisUploadStatus(module, result, errorCode, millis, errorMsg);
            } catch (RemoteException e) {
                throw e.rethrowFromSystemServer();
            }
        }
    }

    public void addLogicActionListener(final LogicActionListener l) {
        ILogicActionListener rl = new ILogicActionListener.Stub() { // from class: com.xiaopeng.aftersales.AfterSalesManager.2
            @Override // com.xiaopeng.aftersales.ILogicActionListener
            public void uploadLogicAction(String issueName, String conclusion, String startTime, String endTime, String logicactionTime, String logicactionEntry, String logictreeVer) throws RemoteException {
                l.uploadLogicAction(issueName, conclusion, startTime, endTime, logicactionTime, logicactionEntry, logictreeVer);
            }
        };
        try {
            this.mAfterSalesService.registerLogicActionListener(rl);
            this.mLogicActionListeners.put(l, rl);
        } catch (RemoteException e) {
            throw e.rethrowFromSystemServer();
        }
    }

    public void removeLogicActionListener(LogicActionListener l) {
        ILogicActionListener rl = this.mLogicActionListeners.get(l);
        Preconditions.checkArgument(rl != null, "LogicActionListener was not registered.");
        try {
            this.mAfterSalesService.unregisterLogicActionListener(rl);
        } catch (RemoteException e) {
            throw e.rethrowFromSystemServer();
        }
    }

    public void recordLogicAction(String issueName, String conclusion, String startTime, String endTime, String logicactionTime, String logicactionEntry, String logictreeVer) {
        if (this.mAfterSalesService != null) {
            try {
                this.mAfterSalesService.recordLogicAction(issueName, conclusion, startTime, endTime, logicactionTime, logicactionEntry, logictreeVer);
            } catch (RemoteException e) {
                throw e.rethrowFromSystemServer();
            }
        }
    }

    public void updateLogicActionUploadStatus(boolean status, String issueName, String conclusion, String startTime, String endTime, String logicactionTime, String logicactionEntry, String logictreeVer) {
        if (this.mAfterSalesService != null) {
            try {
                this.mAfterSalesService.updateLogicActionUploadStatus(status, issueName, conclusion, startTime, endTime, logicactionTime, logicactionEntry, logictreeVer);
            } catch (RemoteException e) {
                throw e.rethrowFromSystemServer();
            }
        }
    }

    public void requestUploadLogicAction() {
        if (this.mAfterSalesService != null) {
            try {
                this.mAfterSalesService.requestUploadLogicAction();
            } catch (RemoteException e) {
                throw e.rethrowFromSystemServer();
            }
        }
    }

    public void requestUpgradeLogicTree(String path) {
        if (this.mAfterSalesService != null) {
            try {
                this.mAfterSalesService.requestUpgradeLogicTree(path);
            } catch (RemoteException e) {
                throw e.rethrowFromSystemServer();
            }
        }
    }

    public void addLogicTreeUpgrader(final LogicTreeUpgrader l) {
        ILogicTreeUpgrader rl = new ILogicTreeUpgrader.Stub() { // from class: com.xiaopeng.aftersales.AfterSalesManager.3
            @Override // com.xiaopeng.aftersales.ILogicTreeUpgrader
            public void onUpgradeStatus(boolean status) throws RemoteException {
                l.onUpgradeStatus(status);
            }
        };
        try {
            this.mAfterSalesService.registerLogicTreeUpgrader(rl);
            this.mLogicTreeUpgraders.put(l, rl);
        } catch (RemoteException e) {
            throw e.rethrowFromSystemServer();
        }
    }

    public void removeLogicTreeUpgrader(LogicTreeUpgrader l) {
        ILogicTreeUpgrader rl = this.mLogicTreeUpgraders.get(l);
        Preconditions.checkArgument(rl != null, "LogicTreeUpgrader was not registered.");
        try {
            this.mAfterSalesService.unregisterLogicTreeUpgrader(rl);
        } catch (RemoteException e) {
            throw e.rethrowFromSystemServer();
        }
    }

    public void recordRepairModeAction(String action, String result) {
        if (this.mAfterSalesService != null) {
            try {
                this.mAfterSalesService.recordRepairmodeAction(action, result);
            } catch (RemoteException e) {
                throw e.rethrowFromSystemServer();
            }
        }
    }

    public void enableRepairMode() {
        if (this.mAfterSalesService != null) {
            try {
                this.mAfterSalesService.enableRepairMode();
            } catch (RemoteException e) {
                throw e.rethrowFromSystemServer();
            }
        }
    }

    public void enableRepairModeWithKey(String keyPath) {
        if (this.mAfterSalesService != null) {
            try {
                this.mAfterSalesService.enableRepairModeWithKey(keyPath);
            } catch (RemoteException e) {
                throw e.rethrowFromSystemServer();
            }
        }
    }

    public void enableRepairModeWithKeyId(String keyId) {
        if (this.mAfterSalesService != null) {
            try {
                this.mAfterSalesService.enableRepairModeWithKeyId(keyId);
            } catch (RemoteException e) {
                throw e.rethrowFromSystemServer();
            }
        }
    }

    public void disableRepairMode() {
        if (this.mAfterSalesService != null) {
            try {
                this.mAfterSalesService.disableRepairMode();
            } catch (RemoteException e) {
                throw e.rethrowFromSystemServer();
            }
        }
    }

    public boolean getRepairMode() {
        if (this.mAfterSalesService == null) {
            return false;
        }
        try {
            boolean repairmode = this.mAfterSalesService.getRepairMode();
            return repairmode;
        } catch (RemoteException e) {
            throw e.rethrowFromSystemServer();
        }
    }

    public String getRepairModeEnableTime() {
        if (this.mAfterSalesService == null) {
            return null;
        }
        try {
            String repairmodeEnableTime = this.mAfterSalesService.getRepairModeEnableTime();
            return repairmodeEnableTime;
        } catch (RemoteException e) {
            throw e.rethrowFromSystemServer();
        }
    }

    public String getRepairModeDisableTime() {
        if (this.mAfterSalesService == null) {
            return null;
        }
        try {
            String repairmodeDisableTime = this.mAfterSalesService.getRepairModeDisableTime();
            return repairmodeDisableTime;
        } catch (RemoteException e) {
            throw e.rethrowFromSystemServer();
        }
    }

    public boolean getSpeedLimitMode() {
        if (this.mAfterSalesService == null) {
            return false;
        }
        try {
            boolean speedLimitMode = this.mAfterSalesService.getSpeedLimitMode();
            return speedLimitMode;
        } catch (RemoteException e) {
            throw e.rethrowFromSystemServer();
        }
    }

    public String getSpeedLimitEnableTime() {
        if (this.mAfterSalesService == null) {
            return null;
        }
        try {
            String speedLimitEnableTime = this.mAfterSalesService.getSpeedLimitEnableTime();
            return speedLimitEnableTime;
        } catch (RemoteException e) {
            throw e.rethrowFromSystemServer();
        }
    }

    public String getSpeedLimitDisableTime() {
        if (this.mAfterSalesService == null) {
            return null;
        }
        try {
            String speedLimitDisableTime = this.mAfterSalesService.getSpeedLimitDisableTime();
            return speedLimitDisableTime;
        } catch (RemoteException e) {
            throw e.rethrowFromSystemServer();
        }
    }

    public String getRepairModeKeyId() {
        if (this.mAfterSalesService == null) {
            return null;
        }
        try {
            String repairModeKeyId = this.mAfterSalesService.getRepairModeKeyId();
            return repairModeKeyId;
        } catch (RemoteException e) {
            throw e.rethrowFromSystemServer();
        }
    }

    public void recordSpeedLimitOn() {
        if (this.mAfterSalesService != null) {
            try {
                this.mAfterSalesService.recordSpeedLimitOn();
            } catch (RemoteException e) {
                throw e.rethrowFromSystemServer();
            }
        }
    }

    public void recordSpeedLimitOff() {
        if (this.mAfterSalesService != null) {
            try {
                this.mAfterSalesService.recordSpeedLimitOff();
            } catch (RemoteException e) {
                throw e.rethrowFromSystemServer();
            }
        }
    }

    public void registerRepairModeListener(final RepairModeListener l) {
        IRepairModeListener rl = new IRepairModeListener.Stub() { // from class: com.xiaopeng.aftersales.AfterSalesManager.4
            @Override // com.xiaopeng.aftersales.IRepairModeListener
            public void onRepairModeChanged(boolean onoff, int switchResult) throws RemoteException {
                l.onRepairModeChanged(onoff, switchResult);
            }
        };
        try {
            this.mAfterSalesService.registerRepairModeListener(rl);
            this.mRepairModeListeners.put(l, rl);
        } catch (RemoteException e) {
            throw e.rethrowFromSystemServer();
        }
    }

    public void unregisterRepairModeListener(RepairModeListener l) {
        IRepairModeListener rl = this.mRepairModeListeners.get(l);
        Preconditions.checkArgument(rl != null, "RepairModeListener was not registered.");
        try {
            this.mAfterSalesService.unregisterRepairModeListener(rl);
        } catch (RemoteException e) {
            throw e.rethrowFromSystemServer();
        }
    }

    public void executeShellCmd(int cmdtype, String param, boolean isCloudCmd) {
        if (this.mAfterSalesService != null) {
            try {
                this.mAfterSalesService.executeShellCmd(cmdtype, param, isCloudCmd);
            } catch (RemoteException e) {
                throw e.rethrowFromSystemServer();
            }
        }
    }

    public void executeShellCmdWithLimitLine(int cmdtype, String param, int limitLine, String quitcmd, boolean isCloudCmd) {
        if (this.mAfterSalesService != null) {
            try {
                this.mAfterSalesService.executeShellCmdWithLimitLine(cmdtype, param, limitLine, quitcmd, isCloudCmd);
            } catch (RemoteException e) {
                throw e.rethrowFromSystemServer();
            }
        }
    }

    public void registerShellCmdListener(final ShellCmdListener l) {
        IShellCmdListener rl = new IShellCmdListener.Stub() { // from class: com.xiaopeng.aftersales.AfterSalesManager.5
            @Override // com.xiaopeng.aftersales.IShellCmdListener
            public void onShellResponse(int errorcode, String resultPath, boolean isCloudCmd) throws RemoteException {
                l.onShellResponse(errorcode, resultPath, isCloudCmd);
            }
        };
        try {
            this.mAfterSalesService.registerShellCmdListener(rl);
            this.mShellCmdListeners.put(l, rl);
        } catch (RemoteException e) {
            throw e.rethrowFromSystemServer();
        }
    }

    public void unregisterShellCmdListener(ShellCmdListener l) {
        IShellCmdListener rl = this.mShellCmdListeners.get(l);
        Preconditions.checkArgument(rl != null, "ShellCmdListener was not registered.");
        try {
            this.mAfterSalesService.unregisterShellCmdListener(rl);
        } catch (RemoteException e) {
            throw e.rethrowFromSystemServer();
        }
    }

    public void executeEncryptSh(String path, boolean isCloudCmd) {
        if (this.mAfterSalesService != null) {
            try {
                this.mAfterSalesService.executeEncryptSh(path, isCloudCmd);
            } catch (RemoteException e) {
                throw e.rethrowFromSystemServer();
            }
        }
    }

    public void registerEncryptShListener(final EncryptShListener l) {
        IEncryptShListener rl = new IEncryptShListener.Stub() { // from class: com.xiaopeng.aftersales.AfterSalesManager.6
            @Override // com.xiaopeng.aftersales.IEncryptShListener
            public void onEncryptShResponse(int errorcode, String resultPath, String outputPath, boolean isCloudCmd) throws RemoteException {
                l.onEncryptShResponse(errorcode, resultPath, outputPath, isCloudCmd);
            }
        };
        try {
            this.mAfterSalesService.registerEncryptShListener(rl);
            this.mEncryptShListeners.put(l, rl);
        } catch (RemoteException e) {
            throw e.rethrowFromSystemServer();
        }
    }

    public void unregisterEncryptShListener(EncryptShListener l) {
        IEncryptShListener rl = this.mEncryptShListeners.get(l);
        Preconditions.checkArgument(rl != null, "EncryptShListener was not registered.");
        try {
            this.mAfterSalesService.unregisterEncryptShListener(rl);
        } catch (RemoteException e) {
            throw e.rethrowFromSystemServer();
        }
    }

    public void enableAuthMode(String value, long time) {
        if (this.mAfterSalesService != null) {
            try {
                this.mAfterSalesService.enableAuthMode(value, time);
            } catch (RemoteException e) {
                throw e.rethrowFromSystemServer();
            }
        }
    }

    public void disableAuthMode() {
        if (this.mAfterSalesService != null) {
            try {
                this.mAfterSalesService.disableAuthMode();
            } catch (RemoteException e) {
                throw e.rethrowFromSystemServer();
            }
        }
    }

    public boolean getAuthMode() {
        if (this.mAfterSalesService == null) {
            return false;
        }
        try {
            boolean authmode = this.mAfterSalesService.getAuthMode();
            return authmode;
        } catch (RemoteException e) {
            throw e.rethrowFromSystemServer();
        }
    }

    public String getAuthPass() {
        if (this.mAfterSalesService == null) {
            return null;
        }
        try {
            String authPass = this.mAfterSalesService.getAuthPass();
            return authPass;
        } catch (RemoteException e) {
            throw e.rethrowFromSystemServer();
        }
    }

    public long getAuthEndTime() {
        if (this.mAfterSalesService == null) {
            return -1L;
        }
        try {
            long authPass = this.mAfterSalesService.getAuthEndTime();
            return authPass;
        } catch (RemoteException e) {
            throw e.rethrowFromSystemServer();
        }
    }

    public void registerAuthModeListener(final AuthModeListener l) {
        IAuthModeListener rl = new IAuthModeListener.Stub() { // from class: com.xiaopeng.aftersales.AfterSalesManager.7
            @Override // com.xiaopeng.aftersales.IAuthModeListener
            public void onAuthModeChanged(boolean onoff, int switchResult) throws RemoteException {
                l.onAuthModeChanged(onoff, switchResult);
            }
        };
        try {
            this.mAfterSalesService.registerAuthModeListener(rl);
            this.mAuthModeListeners.put(l, rl);
        } catch (RemoteException e) {
            throw e.rethrowFromSystemServer();
        }
    }

    public void unregisterAuthModeListener(AuthModeListener l) {
        IAuthModeListener rl = this.mAuthModeListeners.get(l);
        Preconditions.checkArgument(rl != null, "AuthModeListener was not registered.");
        try {
            this.mAfterSalesService.unregisterAuthModeListener(rl);
        } catch (RemoteException e) {
            throw e.rethrowFromSystemServer();
        }
    }
}
