package com.android.internal.os;

import android.app.ApplicationLoaders;
import android.net.LocalSocket;
import android.os.Build;
import android.system.ErrnoException;
import android.system.Os;
import android.system.OsConstants;
import android.text.TextUtils;
import android.util.Log;
import android.webkit.WebViewFactory;
import android.webkit.WebViewFactoryProvider;
import android.webkit.WebViewLibraryLoader;
import java.io.DataOutputStream;
import java.io.File;
import java.io.IOException;
import java.lang.reflect.Method;
/* loaded from: classes3.dex */
class WebViewZygoteInit {
    public static final String TAG = "WebViewZygoteInit";
    private static ZygoteServer sServer;

    WebViewZygoteInit() {
    }

    /* loaded from: classes3.dex */
    private static class WebViewZygoteServer extends ZygoteServer {
        private WebViewZygoteServer() {
        }

        @Override // com.android.internal.os.ZygoteServer
        protected ZygoteConnection createNewConnection(LocalSocket socket, String abiList) throws IOException {
            return new WebViewZygoteConnection(socket, abiList);
        }
    }

    /* loaded from: classes3.dex */
    private static class WebViewZygoteConnection extends ZygoteConnection {
        WebViewZygoteConnection(LocalSocket socket, String abiList) throws IOException {
            super(socket, abiList);
        }

        @Override // com.android.internal.os.ZygoteConnection
        protected void preload() {
        }

        @Override // com.android.internal.os.ZygoteConnection
        protected boolean isPreloadComplete() {
            return true;
        }

        /* JADX WARN: Unsupported multi-entry loop pattern (BACK_EDGE: B:14:0x0068 -> B:24:0x006f). Please submit an issue!!! */
        @Override // com.android.internal.os.ZygoteConnection
        protected void handlePreloadPackage(String packagePath, String libsPath, String libFileName, String cacheKey) {
            Log.i(WebViewZygoteInit.TAG, "Beginning package preload");
            ClassLoader loader = ApplicationLoaders.getDefault().createAndCacheWebViewClassLoader(packagePath, libsPath, cacheKey);
            WebViewLibraryLoader.loadNativeLibrary(loader, libFileName);
            String[] packageList = TextUtils.split(packagePath, File.pathSeparator);
            for (String packageEntry : packageList) {
                Zygote.nativeAllowFileAcrossFork(packageEntry);
            }
            boolean preloadSucceeded = false;
            try {
                Class<WebViewFactoryProvider> providerClass = WebViewFactory.getWebViewProviderClass(loader);
                Method preloadInZygote = providerClass.getMethod("preloadInZygote", new Class[0]);
                preloadInZygote.setAccessible(true);
                if (preloadInZygote.getReturnType() != Boolean.TYPE) {
                    Log.e(WebViewZygoteInit.TAG, "Unexpected return type: preloadInZygote must return boolean");
                } else {
                    preloadSucceeded = ((Boolean) providerClass.getMethod("preloadInZygote", new Class[0]).invoke(null, new Object[0])).booleanValue();
                    if (!preloadSucceeded) {
                        Log.e(WebViewZygoteInit.TAG, "preloadInZygote returned false");
                    }
                }
            } catch (ReflectiveOperationException e) {
                Log.e(WebViewZygoteInit.TAG, "Exception while preloading package", e);
            }
            try {
                DataOutputStream socketOut = getSocketOutputStream();
                socketOut.writeInt(preloadSucceeded ? 1 : 0);
                Log.i(WebViewZygoteInit.TAG, "Package preload done");
            } catch (IOException ioe) {
                throw new IllegalStateException("Error writing to command socket", ioe);
            }
        }
    }

    public static void main(String[] argv) {
        Log.i(TAG, "Starting WebViewZygoteInit");
        String socketName = null;
        for (String arg : argv) {
            Log.i(TAG, arg);
            if (arg.startsWith(Zygote.CHILD_ZYGOTE_SOCKET_NAME_ARG)) {
                socketName = arg.substring(Zygote.CHILD_ZYGOTE_SOCKET_NAME_ARG.length());
            }
        }
        if (socketName == null) {
            throw new RuntimeException("No --zygote-socket= specified");
        }
        try {
            Os.prctl(OsConstants.PR_SET_NO_NEW_PRIVS, 1L, 0L, 0L, 0L);
            sServer = new WebViewZygoteServer();
            try {
                try {
                    sServer.registerServerSocketAtAbstractName(socketName);
                    Zygote.nativeAllowFileAcrossFork("ABSTRACT/" + socketName);
                    Runnable caller = sServer.runSelectLoop(TextUtils.join(",", Build.SUPPORTED_ABIS));
                    if (caller != null) {
                        caller.run();
                    }
                } catch (RuntimeException e) {
                    Log.e(TAG, "Fatal exception:", e);
                    throw e;
                }
            } finally {
                sServer.closeServerSocket();
            }
        } catch (ErrnoException ex) {
            throw new RuntimeException("Failed to set PR_SET_NO_NEW_PRIVS", ex);
        }
    }
}
