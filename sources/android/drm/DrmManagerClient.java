package android.drm;

import android.content.ContentResolver;
import android.content.ContentValues;
import android.content.Context;
import android.content.IntentFilter;
import android.database.Cursor;
import android.database.sqlite.SQLiteException;
import android.drm.DrmStore;
import android.net.Uri;
import android.os.Handler;
import android.os.HandlerThread;
import android.os.Looper;
import android.os.Message;
import android.util.Log;
import dalvik.system.CloseGuard;
import java.io.FileDescriptor;
import java.io.IOException;
import java.lang.ref.WeakReference;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.concurrent.atomic.AtomicBoolean;
/* loaded from: classes.dex */
public class DrmManagerClient implements AutoCloseable {
    private static final int ACTION_PROCESS_DRM_INFO = 1002;
    private static final int ACTION_REMOVE_ALL_RIGHTS = 1001;
    public static final int ERROR_NONE = 0;
    public static final int ERROR_UNKNOWN = -2000;
    public static final int INVALID_SESSION = -1;
    private static final String TAG = "DrmManagerClient";
    private Context mContext;
    private EventHandler mEventHandler;
    HandlerThread mEventThread;
    private InfoHandler mInfoHandler;
    HandlerThread mInfoThread;
    private long mNativeContext;
    private OnErrorListener mOnErrorListener;
    private OnEventListener mOnEventListener;
    private OnInfoListener mOnInfoListener;
    private int mUniqueId;
    private final AtomicBoolean mClosed = new AtomicBoolean();
    private final CloseGuard mCloseGuard = CloseGuard.get();

    /* loaded from: classes.dex */
    public interface OnErrorListener {
        void onError(DrmManagerClient drmManagerClient, DrmErrorEvent drmErrorEvent);
    }

    /* loaded from: classes.dex */
    public interface OnEventListener {
        void onEvent(DrmManagerClient drmManagerClient, DrmEvent drmEvent);
    }

    /* loaded from: classes.dex */
    public interface OnInfoListener {
        void onInfo(DrmManagerClient drmManagerClient, DrmInfoEvent drmInfoEvent);
    }

    private native DrmInfo _acquireDrmInfo(int i, DrmInfoRequest drmInfoRequest);

    private native boolean _canHandle(int i, String str, String str2);

    private native int _checkRightsStatus(int i, String str, int i2);

    private native DrmConvertedStatus _closeConvertSession(int i, int i2);

    private native DrmConvertedStatus _convertData(int i, int i2, byte[] bArr);

    private native DrmSupportInfo[] _getAllSupportInfo(int i);

    private native ContentValues _getConstraints(int i, String str, int i2);

    private native int _getDrmObjectType(int i, String str, String str2);

    private native ContentValues _getMetadata(int i, String str);

    private native String _getOriginalMimeType(int i, String str, FileDescriptor fileDescriptor);

    private native int _initialize();

    private native void _installDrmEngine(int i, String str);

    private native int _openConvertSession(int i, String str);

    /* JADX INFO: Access modifiers changed from: private */
    public native DrmInfoStatus _processDrmInfo(int i, DrmInfo drmInfo);

    private native void _release(int i);

    /* JADX INFO: Access modifiers changed from: private */
    public native int _removeAllRights(int i);

    private native int _removeRights(int i, String str);

    private native int _saveRights(int i, DrmRights drmRights, String str, String str2);

    private native void _setListeners(int i, Object obj);

    static {
        System.loadLibrary("drmframework_jni");
    }

    /* JADX INFO: Access modifiers changed from: private */
    /* loaded from: classes.dex */
    public class EventHandler extends Handler {
        public EventHandler(Looper looper) {
            super(looper);
        }

        @Override // android.os.Handler
        public void handleMessage(Message msg) {
            DrmEvent event = null;
            DrmErrorEvent error = null;
            HashMap<String, Object> attributes = new HashMap<>();
            switch (msg.what) {
                case 1001:
                    if (DrmManagerClient.this._removeAllRights(DrmManagerClient.this.mUniqueId) == 0) {
                        event = new DrmEvent(DrmManagerClient.this.mUniqueId, 1001, null);
                        break;
                    } else {
                        error = new DrmErrorEvent(DrmManagerClient.this.mUniqueId, 2007, null);
                        break;
                    }
                case 1002:
                    DrmInfo drmInfo = (DrmInfo) msg.obj;
                    DrmInfoStatus status = DrmManagerClient.this._processDrmInfo(DrmManagerClient.this.mUniqueId, drmInfo);
                    attributes.put(DrmEvent.DRM_INFO_STATUS_OBJECT, status);
                    attributes.put(DrmEvent.DRM_INFO_OBJECT, drmInfo);
                    if (status != null && 1 == status.statusCode) {
                        event = new DrmEvent(DrmManagerClient.this.mUniqueId, DrmManagerClient.this.getEventType(status.infoType), null, attributes);
                        break;
                    } else {
                        int infoType = status != null ? status.infoType : drmInfo.getInfoType();
                        error = new DrmErrorEvent(DrmManagerClient.this.mUniqueId, DrmManagerClient.this.getErrorType(infoType), null, attributes);
                        break;
                    }
                    break;
                default:
                    Log.e(DrmManagerClient.TAG, "Unknown message type " + msg.what);
                    return;
            }
            if (DrmManagerClient.this.mOnEventListener != null && event != null) {
                DrmManagerClient.this.mOnEventListener.onEvent(DrmManagerClient.this, event);
            }
            if (DrmManagerClient.this.mOnErrorListener != null && error != null) {
                DrmManagerClient.this.mOnErrorListener.onError(DrmManagerClient.this, error);
            }
        }
    }

    public static synchronized void notify(Object thisReference, int uniqueId, int infoType, String message) {
        DrmManagerClient instance = (DrmManagerClient) ((WeakReference) thisReference).get();
        if (instance != null && instance.mInfoHandler != null) {
            Message m = instance.mInfoHandler.obtainMessage(1, uniqueId, infoType, message);
            instance.mInfoHandler.sendMessage(m);
        }
    }

    /* JADX INFO: Access modifiers changed from: private */
    /* loaded from: classes.dex */
    public class InfoHandler extends Handler {
        public static final int INFO_EVENT_TYPE = 1;

        public InfoHandler(Looper looper) {
            super(looper);
        }

        @Override // android.os.Handler
        public void handleMessage(Message msg) {
            DrmInfoEvent info = null;
            DrmErrorEvent error = null;
            if (msg.what == 1) {
                int uniqueId = msg.arg1;
                int infoType = msg.arg2;
                String message = msg.obj.toString();
                switch (infoType) {
                    case 1:
                    case 3:
                    case 4:
                    case 5:
                    case 6:
                        info = new DrmInfoEvent(uniqueId, infoType, message);
                        break;
                    case 2:
                        try {
                            DrmUtils.removeFile(message);
                        } catch (IOException e) {
                            e.printStackTrace();
                        }
                        info = new DrmInfoEvent(uniqueId, infoType, message);
                        break;
                    default:
                        error = new DrmErrorEvent(uniqueId, infoType, message);
                        break;
                }
                if (DrmManagerClient.this.mOnInfoListener != null && info != null) {
                    DrmManagerClient.this.mOnInfoListener.onInfo(DrmManagerClient.this, info);
                }
                if (DrmManagerClient.this.mOnErrorListener != null && error != null) {
                    DrmManagerClient.this.mOnErrorListener.onError(DrmManagerClient.this, error);
                    return;
                }
                return;
            }
            Log.e(DrmManagerClient.TAG, "Unknown message type " + msg.what);
        }
    }

    public DrmManagerClient(Context context) {
        this.mContext = context;
        createEventThreads();
        this.mUniqueId = _initialize();
        this.mCloseGuard.open("release");
    }

    protected void finalize() throws Throwable {
        try {
            if (this.mCloseGuard != null) {
                this.mCloseGuard.warnIfOpen();
            }
            close();
        } finally {
            super.finalize();
        }
    }

    @Override // java.lang.AutoCloseable
    public void close() {
        this.mCloseGuard.close();
        if (this.mClosed.compareAndSet(false, true)) {
            if (this.mEventHandler != null) {
                this.mEventThread.quit();
                this.mEventThread = null;
            }
            if (this.mInfoHandler != null) {
                this.mInfoThread.quit();
                this.mInfoThread = null;
            }
            this.mEventHandler = null;
            this.mInfoHandler = null;
            this.mOnEventListener = null;
            this.mOnInfoListener = null;
            this.mOnErrorListener = null;
            _release(this.mUniqueId);
        }
    }

    @Deprecated
    public void release() {
        close();
    }

    public synchronized void setOnInfoListener(OnInfoListener infoListener) {
        this.mOnInfoListener = infoListener;
        if (infoListener != null) {
            createListeners();
        }
    }

    public synchronized void setOnEventListener(OnEventListener eventListener) {
        this.mOnEventListener = eventListener;
        if (eventListener != null) {
            createListeners();
        }
    }

    public synchronized void setOnErrorListener(OnErrorListener errorListener) {
        this.mOnErrorListener = errorListener;
        if (errorListener != null) {
            createListeners();
        }
    }

    public String[] getAvailableDrmEngines() {
        DrmSupportInfo[] supportInfos = _getAllSupportInfo(this.mUniqueId);
        ArrayList<String> descriptions = new ArrayList<>();
        for (DrmSupportInfo drmSupportInfo : supportInfos) {
            descriptions.add(drmSupportInfo.getDescriprition());
        }
        int i = descriptions.size();
        String[] drmEngines = new String[i];
        return (String[]) descriptions.toArray(drmEngines);
    }

    public ContentValues getConstraints(String path, int action) {
        if (path == null || path.equals("") || !DrmStore.Action.isValid(action)) {
            throw new IllegalArgumentException("Given usage or path is invalid/null");
        }
        return _getConstraints(this.mUniqueId, path, action);
    }

    public ContentValues getMetadata(String path) {
        if (path == null || path.equals("")) {
            throw new IllegalArgumentException("Given path is invalid/null");
        }
        return _getMetadata(this.mUniqueId, path);
    }

    public ContentValues getConstraints(Uri uri, int action) {
        if (uri == null || Uri.EMPTY == uri) {
            throw new IllegalArgumentException("Uri should be non null");
        }
        return getConstraints(convertUriToPath(uri), action);
    }

    public ContentValues getMetadata(Uri uri) {
        if (uri == null || Uri.EMPTY == uri) {
            throw new IllegalArgumentException("Uri should be non null");
        }
        return getMetadata(convertUriToPath(uri));
    }

    public int saveRights(DrmRights drmRights, String rightsPath, String contentPath) throws IOException {
        if (drmRights == null || !drmRights.isValid()) {
            throw new IllegalArgumentException("Given drmRights or contentPath is not valid");
        }
        if (rightsPath != null && !rightsPath.equals("")) {
            DrmUtils.writeToFile(rightsPath, drmRights.getData());
        }
        return _saveRights(this.mUniqueId, drmRights, rightsPath, contentPath);
    }

    public synchronized void installDrmEngine(String engineFilePath) {
        if (engineFilePath == null || engineFilePath.equals("")) {
            throw new IllegalArgumentException("Given engineFilePath: " + engineFilePath + "is not valid");
        }
        _installDrmEngine(this.mUniqueId, engineFilePath);
    }

    public boolean canHandle(String path, String mimeType) {
        if ((path == null || path.equals("")) && (mimeType == null || mimeType.equals(""))) {
            throw new IllegalArgumentException("Path or the mimetype should be non null");
        }
        return _canHandle(this.mUniqueId, path, mimeType);
    }

    public boolean canHandle(Uri uri, String mimeType) {
        if ((uri == null || Uri.EMPTY == uri) && (mimeType == null || mimeType.equals(""))) {
            throw new IllegalArgumentException("Uri or the mimetype should be non null");
        }
        return canHandle(convertUriToPath(uri), mimeType);
    }

    public int processDrmInfo(DrmInfo drmInfo) {
        if (drmInfo == null || !drmInfo.isValid()) {
            throw new IllegalArgumentException("Given drmInfo is invalid/null");
        }
        if (this.mEventHandler == null) {
            return ERROR_UNKNOWN;
        }
        Message msg = this.mEventHandler.obtainMessage(1002, drmInfo);
        int result = this.mEventHandler.sendMessage(msg) ? 0 : -2000;
        return result;
    }

    public DrmInfo acquireDrmInfo(DrmInfoRequest drmInfoRequest) {
        if (drmInfoRequest == null || !drmInfoRequest.isValid()) {
            throw new IllegalArgumentException("Given drmInfoRequest is invalid/null");
        }
        return _acquireDrmInfo(this.mUniqueId, drmInfoRequest);
    }

    public int acquireRights(DrmInfoRequest drmInfoRequest) {
        DrmInfo drmInfo = acquireDrmInfo(drmInfoRequest);
        if (drmInfo == null) {
            return ERROR_UNKNOWN;
        }
        return processDrmInfo(drmInfo);
    }

    public int getDrmObjectType(String path, String mimeType) {
        if ((path == null || path.equals("")) && (mimeType == null || mimeType.equals(""))) {
            throw new IllegalArgumentException("Path or the mimetype should be non null");
        }
        return _getDrmObjectType(this.mUniqueId, path, mimeType);
    }

    public int getDrmObjectType(Uri uri, String mimeType) {
        if ((uri == null || Uri.EMPTY == uri) && (mimeType == null || mimeType.equals(""))) {
            throw new IllegalArgumentException("Uri or the mimetype should be non null");
        }
        String path = "";
        try {
            path = convertUriToPath(uri);
        } catch (Exception e) {
            Log.w(TAG, "Given Uri could not be found in media store");
        }
        return getDrmObjectType(path, mimeType);
    }

    /* JADX WARN: Code restructure failed: missing block: B:11:0x002a, code lost:
        if (r1 != null) goto L14;
     */
    /* JADX WARN: Code restructure failed: missing block: B:12:0x002c, code lost:
        r1.close();
     */
    /* JADX WARN: Code restructure failed: missing block: B:23:0x003c, code lost:
        if (r1 == null) goto L12;
     */
    /*
        Code decompiled incorrectly, please refer to instructions dump.
        To view partially-correct add '--show-bad-code' argument
    */
    public java.lang.String getOriginalMimeType(java.lang.String r6) {
        /*
            r5 = this;
            if (r6 == 0) goto L40
            java.lang.String r0 = ""
            boolean r0 = r6.equals(r0)
            if (r0 != 0) goto L40
            r0 = 0
            r1 = 0
            r2 = 0
            java.io.File r3 = new java.io.File     // Catch: java.lang.Throwable -> L32 java.io.IOException -> L3b
            r3.<init>(r6)     // Catch: java.lang.Throwable -> L32 java.io.IOException -> L3b
            boolean r4 = r3.exists()     // Catch: java.lang.Throwable -> L32 java.io.IOException -> L3b
            if (r4 == 0) goto L23
            java.io.FileInputStream r4 = new java.io.FileInputStream     // Catch: java.lang.Throwable -> L32 java.io.IOException -> L3b
            r4.<init>(r3)     // Catch: java.lang.Throwable -> L32 java.io.IOException -> L3b
            r1 = r4
            java.io.FileDescriptor r4 = r1.getFD()     // Catch: java.lang.Throwable -> L32 java.io.IOException -> L3b
            r2 = r4
        L23:
            int r4 = r5.mUniqueId     // Catch: java.lang.Throwable -> L32 java.io.IOException -> L3b
            java.lang.String r4 = r5._getOriginalMimeType(r4, r6, r2)     // Catch: java.lang.Throwable -> L32 java.io.IOException -> L3b
            r0 = r4
            if (r1 == 0) goto L3f
        L2c:
            r1.close()     // Catch: java.io.IOException -> L30
            goto L3f
        L30:
            r2 = move-exception
            goto L3f
        L32:
            r2 = move-exception
            if (r1 == 0) goto L3a
            r1.close()     // Catch: java.io.IOException -> L39
            goto L3a
        L39:
            r3 = move-exception
        L3a:
            throw r2
        L3b:
            r2 = move-exception
            if (r1 == 0) goto L3f
            goto L2c
        L3f:
            return r0
        L40:
            java.lang.IllegalArgumentException r0 = new java.lang.IllegalArgumentException
            java.lang.String r1 = "Given path should be non null"
            r0.<init>(r1)
            throw r0
        */
        throw new UnsupportedOperationException("Method not decompiled: android.drm.DrmManagerClient.getOriginalMimeType(java.lang.String):java.lang.String");
    }

    public String getOriginalMimeType(Uri uri) {
        if (uri == null || Uri.EMPTY == uri) {
            throw new IllegalArgumentException("Given uri is not valid");
        }
        return getOriginalMimeType(convertUriToPath(uri));
    }

    public int checkRightsStatus(String path) {
        return checkRightsStatus(path, 0);
    }

    public int checkRightsStatus(Uri uri) {
        if (uri == null || Uri.EMPTY == uri) {
            throw new IllegalArgumentException("Given uri is not valid");
        }
        return checkRightsStatus(convertUriToPath(uri));
    }

    public int checkRightsStatus(String path, int action) {
        if (path == null || path.equals("") || !DrmStore.Action.isValid(action)) {
            throw new IllegalArgumentException("Given path or action is not valid");
        }
        return _checkRightsStatus(this.mUniqueId, path, action);
    }

    public int checkRightsStatus(Uri uri, int action) {
        if (uri == null || Uri.EMPTY == uri) {
            throw new IllegalArgumentException("Given uri is not valid");
        }
        return checkRightsStatus(convertUriToPath(uri), action);
    }

    public int removeRights(String path) {
        if (path == null || path.equals("")) {
            throw new IllegalArgumentException("Given path should be non null");
        }
        return _removeRights(this.mUniqueId, path);
    }

    public int removeRights(Uri uri) {
        if (uri == null || Uri.EMPTY == uri) {
            throw new IllegalArgumentException("Given uri is not valid");
        }
        return removeRights(convertUriToPath(uri));
    }

    public int removeAllRights() {
        if (this.mEventHandler == null) {
            return ERROR_UNKNOWN;
        }
        Message msg = this.mEventHandler.obtainMessage(1001);
        int result = this.mEventHandler.sendMessage(msg) ? 0 : -2000;
        return result;
    }

    public int openConvertSession(String mimeType) {
        if (mimeType == null || mimeType.equals("")) {
            throw new IllegalArgumentException("Path or the mimeType should be non null");
        }
        return _openConvertSession(this.mUniqueId, mimeType);
    }

    public DrmConvertedStatus convertData(int convertId, byte[] inputData) {
        if (inputData == null || inputData.length <= 0) {
            throw new IllegalArgumentException("Given inputData should be non null");
        }
        return _convertData(this.mUniqueId, convertId, inputData);
    }

    public DrmConvertedStatus closeConvertSession(int convertId) {
        return _closeConvertSession(this.mUniqueId, convertId);
    }

    /* JADX INFO: Access modifiers changed from: private */
    public synchronized int getEventType(int infoType) {
        switch (infoType) {
            case 1:
            case 2:
            case 3:
                return 1002;
            default:
                return -1;
        }
    }

    /* JADX INFO: Access modifiers changed from: private */
    public synchronized int getErrorType(int infoType) {
        switch (infoType) {
            case 1:
            case 2:
            case 3:
                return 2006;
            default:
                return -1;
        }
    }

    private synchronized String convertUriToPath(Uri uri) {
        if (uri == null) {
            return null;
        }
        String scheme = uri.getScheme();
        if (scheme == null || scheme.equals("") || scheme.equals(ContentResolver.SCHEME_FILE)) {
            return uri.getPath();
        }
        if (scheme.equals(IntentFilter.SCHEME_HTTP)) {
            return uri.toString();
        } else if (scheme.equals("content")) {
            String[] projection = {"_data"};
            Cursor cursor = null;
            try {
                try {
                    cursor = this.mContext.getContentResolver().query(uri, projection, null, null, null);
                    if (cursor == null || cursor.getCount() == 0 || !cursor.moveToFirst()) {
                        throw new IllegalArgumentException("Given Uri could not be found in media store");
                    }
                    int pathIndex = cursor.getColumnIndexOrThrow("_data");
                    String path = cursor.getString(pathIndex);
                } catch (SQLiteException e) {
                    throw new IllegalArgumentException("Given Uri is not formatted in a way so that it can be found in media store.");
                }
            } finally {
                if (cursor != null) {
                    cursor.close();
                }
            }
        } else {
            throw new IllegalArgumentException("Given Uri scheme is not supported");
        }
    }

    private synchronized void createEventThreads() {
        if (this.mEventHandler == null && this.mInfoHandler == null) {
            this.mInfoThread = new HandlerThread("DrmManagerClient.InfoHandler");
            this.mInfoThread.start();
            this.mInfoHandler = new InfoHandler(this.mInfoThread.getLooper());
            this.mEventThread = new HandlerThread("DrmManagerClient.EventHandler");
            this.mEventThread.start();
            this.mEventHandler = new EventHandler(this.mEventThread.getLooper());
        }
    }

    private synchronized void createListeners() {
        _setListeners(this.mUniqueId, new WeakReference(this));
    }
}
