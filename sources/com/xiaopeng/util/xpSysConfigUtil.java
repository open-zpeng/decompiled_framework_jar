package com.xiaopeng.util;

import android.util.ArraySet;
import android.util.Log;
import com.xiaopeng.sysconfig.SysConfigManager;
import java.io.BufferedReader;
import java.io.ByteArrayInputStream;
import java.io.File;
import java.io.IOException;
import java.io.StringReader;
import java.lang.reflect.Method;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.concurrent.locks.ReentrantLock;
import java.util.function.Consumer;
import javax.xml.parsers.DocumentBuilderFactory;
import org.json.JSONObject;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.NodeList;
/* loaded from: classes3.dex */
public class xpSysConfigUtil {
    public static final String DATA_DIR = "/data/xuiservice/";
    public static final String DEFAULT_VERSION_KEY = "version";
    public static final String SYSTEM_DIR = "/system/etc/xuiservice/";
    private static final String TAG = xpSysConfigUtil.class.getSimpleName();
    private static final ArraySet<SysConfigManager.SysConfigListener> retrySet = new ArraySet<>();
    private static ReentrantLock lock = new ReentrantLock();
    private static volatile boolean retryRunning = false;

    static /* synthetic */ SysConfigManager access$200() {
        return getSysConfigManager();
    }

    /* loaded from: classes3.dex */
    public static class Version implements Comparable<Version> {
        private String version;

        public Version() {
        }

        public Version(String version) {
            this.version = version;
        }

        @Override // java.lang.Comparable
        public int compareTo(Version o) {
            if (this.version == null) {
                return o.version == null ? 0 : -1;
            } else if (o.version == null) {
                return 1;
            } else {
                try {
                    double v1 = Double.parseDouble(this.version);
                    double v2 = Double.parseDouble(o.version);
                    return Double.compare(v1, v2);
                } catch (NumberFormatException e) {
                    String[] s1 = this.version.split("\\.");
                    String[] s2 = o.version.split("\\.");
                    if (s1.length == s2.length) {
                        for (int i = 0; i < s1.length; i++) {
                            try {
                                int i1 = Integer.parseInt(s1[i]);
                                int i2 = Integer.parseInt(s2[i]);
                                if (i1 == i2) {
                                    if (i == s1.length - 1) {
                                        return 0;
                                    }
                                } else {
                                    return Integer.compare(i1, i2);
                                }
                            } catch (NumberFormatException e2) {
                                return this.version.compareTo(o.version);
                            }
                        }
                    }
                    return this.version.compareTo(o.version);
                }
            }
        }

        public String toString() {
            return this.version;
        }

        public String getVersion() {
            return this.version;
        }
    }

    public static Version getFileVersion(File file) {
        return getFileVersion(file, "version");
    }

    public static Version getFileVersion(File file, String versionKey) {
        return getVersion(contentOf(file), versionKey);
    }

    public static Version getVersion(String content, String versionKey) {
        return content != null ? versionOf(content, versionKey) : new Version(null);
    }

    public static Version getVersion(String content) {
        return getVersion(content, "version");
    }

    public static String getConfigContent(String configFileName) {
        return getConfigContent(SYSTEM_DIR, DATA_DIR, configFileName, "version");
    }

    public static String getConfigContent(String dir1, String dir2, String configFileName, String versionKey) {
        File file1 = new File(dir1, configFileName);
        File file2 = new File(dir2, configFileName);
        String content1 = contentOf(file1);
        String content2 = contentOf(file2);
        if (content1 == null) {
            String str = TAG;
            Log.i(str, "getConfigContent configFileName=" + configFileName + " from " + dir2);
            return content2;
        } else if (content2 == null) {
            String str2 = TAG;
            Log.i(str2, "getConfigContent configFileName=" + configFileName + " from " + dir1);
            return content1;
        } else {
            Version version1 = versionOf(content1, versionKey);
            Version version2 = versionOf(content2, versionKey);
            String str3 = TAG;
            Log.i(str3, "version of " + configFileName + ", " + dir1 + "=" + version1);
            String str4 = TAG;
            Log.i(str4, "version of " + configFileName + ", " + dir2 + "=" + version2);
            boolean useContent1 = version1.compareTo(version2) >= 0;
            String str5 = TAG;
            StringBuilder sb = new StringBuilder();
            sb.append("getConfigContent configFileName=");
            sb.append(configFileName);
            sb.append(" from ");
            sb.append(useContent1 ? dir1 : dir2);
            Log.i(str5, sb.toString());
            return useContent1 ? content1 : content2;
        }
    }

    public static void registerConfigUpdateListener(SysConfigManager.SysConfigListener listener) {
        SysConfigManager sysConfigManager = getSysConfigManager();
        if (sysConfigManager != null) {
            sysConfigManager.registerConfigUpdateListener(listener);
            String str = TAG;
            Log.i(str, "registerConfigUpdateListener success, listener=" + listener);
            return;
        }
        lock.lock();
        try {
            if (!retrySet.contains(listener)) {
                retrySet.add(listener);
                if (!retryRunning) {
                    new RetryThread().start();
                    retryRunning = true;
                }
            }
        } finally {
            lock.unlock();
        }
    }

    /* loaded from: classes3.dex */
    private static class RetryThread extends Thread {
        private RetryThread() {
        }

        @Override // java.lang.Thread, java.lang.Runnable
        public void run() {
            final SysConfigManager sysConfigManager;
            do {
                Log.i(xpSysConfigUtil.TAG, "retry thread running, wait for xuiservice ...");
                try {
                    sleep(1000L);
                } catch (Exception e) {
                }
                sysConfigManager = xpSysConfigUtil.access$200();
            } while (sysConfigManager == null);
            xpSysConfigUtil.lock.lock();
            try {
                xpSysConfigUtil.retrySet.forEach(new Consumer() { // from class: com.xiaopeng.util.-$$Lambda$xpSysConfigUtil$RetryThread$xth9AU48Lh-0KzjU6BFpTm57O2s
                    @Override // java.util.function.Consumer
                    public final void accept(Object obj) {
                        SysConfigManager.this.registerConfigUpdateListener((SysConfigManager.SysConfigListener) obj);
                    }
                });
                xpSysConfigUtil.retrySet.clear();
                boolean unused = xpSysConfigUtil.retryRunning = false;
                Log.i(xpSysConfigUtil.TAG, "retry thread exit");
            } finally {
                xpSysConfigUtil.lock.unlock();
            }
        }
    }

    public static void unregisterConfigUpdateListener(SysConfigManager.SysConfigListener listener) {
        SysConfigManager sysConfigManager = getSysConfigManager();
        if (sysConfigManager != null) {
            sysConfigManager.unregisterConfigUpdateListener(listener);
        }
    }

    private static SysConfigManager getSysConfigManager() {
        try {
            Class<?> clazz = Class.forName("com.xiaopeng.xuimanager.XUIManager");
            Method getInstanceMethod = clazz.getMethod("getInstance", new Class[0]);
            Object xuiManager = getInstanceMethod.invoke(null, new Object[0]);
            Method getServiceMethod = clazz.getMethod("getService", String.class);
            Object sysConfigManager = getServiceMethod.invoke(xuiManager, "sysconfig");
            return (SysConfigManager) sysConfigManager;
        } catch (Exception e) {
            return null;
        }
    }

    private static String contentOf(File file) {
        try {
            return new String(Files.readAllBytes(Paths.get(file.getAbsolutePath(), new String[0])));
        } catch (IOException e) {
            String str = TAG;
            Log.e(str, "contentOf=" + file, e);
            return null;
        }
    }

    private static Version versionOf(String content, String versionKey) {
        String version = null;
        try {
            version = new JSONObject(content).getString(versionKey);
        } catch (Exception e) {
        }
        if (version == null) {
            try {
                DocumentBuilderFactory factory = DocumentBuilderFactory.newInstance();
                Document document = factory.newDocumentBuilder().parse(new ByteArrayInputStream(content.getBytes()));
                Element documentElement = document.getDocumentElement();
                NodeList nodeList = documentElement.getElementsByTagName(versionKey);
                if (nodeList.getLength() > 0) {
                    version = nodeList.item(0).getTextContent();
                }
            } catch (Exception e2) {
            }
        }
        if (version == null) {
            try {
                StringReader stringReader = new StringReader(content);
                BufferedReader bufferedReader = new BufferedReader(stringReader);
                while (true) {
                    String line = bufferedReader.readLine();
                    if (line == null) {
                        break;
                    }
                    if (line.contains("#" + versionKey)) {
                        String[] splits = line.split("=");
                        if (splits.length == 2) {
                            version = splits[1].trim();
                        }
                    }
                }
            } catch (Exception e3) {
            }
        }
        return new Version(version);
    }
}
