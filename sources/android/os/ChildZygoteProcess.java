package android.os;

import android.net.LocalSocketAddress;
/* loaded from: classes2.dex */
public class ChildZygoteProcess extends ZygoteProcess {
    private final int mPid;

    /* JADX INFO: Access modifiers changed from: package-private */
    public synchronized ChildZygoteProcess(LocalSocketAddress socketAddress, int pid) {
        super(socketAddress, (LocalSocketAddress) null);
        this.mPid = pid;
    }

    public synchronized int getPid() {
        return this.mPid;
    }
}
