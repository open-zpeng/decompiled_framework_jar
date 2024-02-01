package android.media;

import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.ServiceConnection;
import android.media.IMediaScannerListener;
import android.media.IMediaScannerService;
import android.net.Uri;
import android.os.IBinder;
import android.os.RemoteException;
/* loaded from: classes.dex */
public class MediaScannerConnection implements ServiceConnection {
    private static final String TAG = "MediaScannerConnection";
    private MediaScannerConnectionClient mClient;
    private boolean mConnected;
    private Context mContext;
    private final IMediaScannerListener.Stub mListener = new IMediaScannerListener.Stub() { // from class: android.media.MediaScannerConnection.1
        @Override // android.media.IMediaScannerListener
        public void scanCompleted(String path, Uri uri) {
            MediaScannerConnectionClient client = MediaScannerConnection.this.mClient;
            if (client != null) {
                client.onScanCompleted(path, uri);
            }
        }
    };
    private IMediaScannerService mService;

    /* loaded from: classes.dex */
    public interface MediaScannerConnectionClient extends OnScanCompletedListener {
        void onMediaScannerConnected();

        @Override // android.media.MediaScannerConnection.OnScanCompletedListener
        void onScanCompleted(String str, Uri uri);
    }

    /* loaded from: classes.dex */
    public interface OnScanCompletedListener {
        void onScanCompleted(String str, Uri uri);
    }

    public MediaScannerConnection(Context context, MediaScannerConnectionClient client) {
        this.mContext = context;
        this.mClient = client;
    }

    public void connect() {
        synchronized (this) {
            if (!this.mConnected) {
                Intent intent = new Intent(IMediaScannerService.class.getName());
                intent.setComponent(new ComponentName("com.android.providers.media", "com.android.providers.media.MediaScannerService"));
                this.mContext.bindService(intent, this, 1);
                this.mConnected = true;
            }
        }
    }

    public void disconnect() {
        synchronized (this) {
            if (this.mConnected) {
                try {
                    this.mContext.unbindService(this);
                    if (this.mClient instanceof ClientProxy) {
                        this.mClient = null;
                    }
                    this.mService = null;
                } catch (IllegalArgumentException e) {
                }
                this.mConnected = false;
            }
        }
    }

    public synchronized boolean isConnected() {
        boolean z;
        if (this.mService != null) {
            z = this.mConnected;
        }
        return z;
    }

    public void scanFile(String path, String mimeType) {
        synchronized (this) {
            if (this.mService == null || !this.mConnected) {
                throw new IllegalStateException("not connected to MediaScannerService");
            }
            try {
                this.mService.requestScanFile(path, mimeType, this.mListener);
            } catch (RemoteException e) {
            }
        }
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    /* loaded from: classes.dex */
    public static class ClientProxy implements MediaScannerConnectionClient {
        final OnScanCompletedListener mClient;
        MediaScannerConnection mConnection;
        final String[] mMimeTypes;
        int mNextPath;
        final String[] mPaths;

        synchronized ClientProxy(String[] paths, String[] mimeTypes, OnScanCompletedListener client) {
            this.mPaths = paths;
            this.mMimeTypes = mimeTypes;
            this.mClient = client;
        }

        @Override // android.media.MediaScannerConnection.MediaScannerConnectionClient
        public void onMediaScannerConnected() {
            scanNextPath();
        }

        @Override // android.media.MediaScannerConnection.MediaScannerConnectionClient, android.media.MediaScannerConnection.OnScanCompletedListener
        public void onScanCompleted(String path, Uri uri) {
            if (this.mClient != null) {
                this.mClient.onScanCompleted(path, uri);
            }
            scanNextPath();
        }

        synchronized void scanNextPath() {
            if (this.mNextPath >= this.mPaths.length) {
                this.mConnection.disconnect();
                this.mConnection = null;
                return;
            }
            String mimeType = this.mMimeTypes != null ? this.mMimeTypes[this.mNextPath] : null;
            this.mConnection.scanFile(this.mPaths[this.mNextPath], mimeType);
            this.mNextPath++;
        }
    }

    public static void scanFile(Context context, String[] paths, String[] mimeTypes, OnScanCompletedListener callback) {
        ClientProxy client = new ClientProxy(paths, mimeTypes, callback);
        MediaScannerConnection connection = new MediaScannerConnection(context, client);
        client.mConnection = connection;
        connection.connect();
    }

    @Override // android.content.ServiceConnection
    public void onServiceConnected(ComponentName className, IBinder service) {
        synchronized (this) {
            this.mService = IMediaScannerService.Stub.asInterface(service);
            if (this.mService != null && this.mClient != null) {
                this.mClient.onMediaScannerConnected();
            }
        }
    }

    @Override // android.content.ServiceConnection
    public void onServiceDisconnected(ComponentName className) {
        synchronized (this) {
            this.mService = null;
        }
    }
}
