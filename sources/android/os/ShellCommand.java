package android.os;

import com.android.internal.content.NativeLibraryHelper;
import com.android.internal.util.FastPrintWriter;
import java.io.BufferedInputStream;
import java.io.FileDescriptor;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.InputStream;
import java.io.OutputStream;
import java.io.PrintWriter;
/* loaded from: classes2.dex */
public abstract class ShellCommand {
    static final boolean DEBUG = false;
    static final String TAG = "ShellCommand";
    private int mArgPos;
    private String[] mArgs;
    private String mCmd;
    private String mCurArgData;
    private FileDescriptor mErr;
    private FastPrintWriter mErrPrintWriter;
    private FileOutputStream mFileErr;
    private FileInputStream mFileIn;
    private FileOutputStream mFileOut;
    private FileDescriptor mIn;
    private InputStream mInputStream;
    private FileDescriptor mOut;
    private FastPrintWriter mOutPrintWriter;
    private ResultReceiver mResultReceiver;
    private ShellCallback mShellCallback;
    private Binder mTarget;

    public abstract synchronized int onCommand(String str);

    public abstract synchronized void onHelp();

    public synchronized void init(Binder target, FileDescriptor in, FileDescriptor out, FileDescriptor err, String[] args, ShellCallback callback, int firstArgPos) {
        this.mTarget = target;
        this.mIn = in;
        this.mOut = out;
        this.mErr = err;
        this.mArgs = args;
        this.mShellCallback = callback;
        this.mResultReceiver = null;
        this.mCmd = null;
        this.mArgPos = firstArgPos;
        this.mCurArgData = null;
        this.mFileIn = null;
        this.mFileOut = null;
        this.mFileErr = null;
        this.mOutPrintWriter = null;
        this.mErrPrintWriter = null;
        this.mInputStream = null;
    }

    /* JADX WARN: Code restructure failed: missing block: B:30:0x0070, code lost:
        if (r12.mResultReceiver == null) goto L18;
     */
    /* JADX WARN: Code restructure failed: missing block: B:41:0x00aa, code lost:
        if (r12.mResultReceiver == null) goto L18;
     */
    /* JADX WARN: Code restructure failed: missing block: B:43:0x00ad, code lost:
        return r2;
     */
    /*
        Code decompiled incorrectly, please refer to instructions dump.
        To view partially-correct add '--show-bad-code' argument
    */
    public synchronized int exec(android.os.Binder r13, java.io.FileDescriptor r14, java.io.FileDescriptor r15, java.io.FileDescriptor r16, java.lang.String[] r17, android.os.ShellCallback r18, android.os.ResultReceiver r19) {
        /*
            r12 = this;
            r9 = r12
            r10 = r17
            r0 = 0
            if (r10 == 0) goto Lf
            int r1 = r10.length
            if (r1 <= 0) goto Lf
            r0 = r10[r0]
            r1 = 1
            r11 = r0
            r8 = r1
            goto L12
        Lf:
            r1 = 0
            r8 = r0
            r11 = r1
        L12:
            r1 = r9
            r2 = r13
            r3 = r14
            r4 = r15
            r5 = r16
            r6 = r10
            r7 = r18
            r1.init(r2, r3, r4, r5, r6, r7, r8)
            r9.mCmd = r11
            r1 = r19
            r9.mResultReceiver = r1
            r0 = -1
            r2 = r0
            r3 = 0
            java.lang.String r0 = r9.mCmd     // Catch: java.lang.Throwable -> L4a java.lang.Throwable -> L4c java.lang.SecurityException -> L73
            int r0 = r9.onCommand(r0)     // Catch: java.lang.Throwable -> L4a java.lang.Throwable -> L4c java.lang.SecurityException -> L73
            r2 = r0
            com.android.internal.util.FastPrintWriter r0 = r9.mOutPrintWriter
            if (r0 == 0) goto L37
            com.android.internal.util.FastPrintWriter r0 = r9.mOutPrintWriter
            r0.flush()
        L37:
            com.android.internal.util.FastPrintWriter r0 = r9.mErrPrintWriter
            if (r0 == 0) goto L40
            com.android.internal.util.FastPrintWriter r0 = r9.mErrPrintWriter
            r0.flush()
        L40:
            android.os.ResultReceiver r0 = r9.mResultReceiver
            if (r0 == 0) goto Lad
        L44:
            android.os.ResultReceiver r0 = r9.mResultReceiver
            r0.send(r2, r3)
            goto Lad
        L4a:
            r0 = move-exception
            goto Lae
        L4c:
            r0 = move-exception
            java.io.PrintWriter r4 = r9.getErrPrintWriter()     // Catch: java.lang.Throwable -> L4a
            r4.println()     // Catch: java.lang.Throwable -> L4a
            java.lang.String r5 = "Exception occurred while executing:"
            r4.println(r5)     // Catch: java.lang.Throwable -> L4a
            r0.printStackTrace(r4)     // Catch: java.lang.Throwable -> L4a
            com.android.internal.util.FastPrintWriter r0 = r9.mOutPrintWriter
            if (r0 == 0) goto L65
            com.android.internal.util.FastPrintWriter r0 = r9.mOutPrintWriter
            r0.flush()
        L65:
            com.android.internal.util.FastPrintWriter r0 = r9.mErrPrintWriter
            if (r0 == 0) goto L6e
            com.android.internal.util.FastPrintWriter r0 = r9.mErrPrintWriter
            r0.flush()
        L6e:
            android.os.ResultReceiver r0 = r9.mResultReceiver
            if (r0 == 0) goto Lad
            goto L44
        L73:
            r0 = move-exception
            java.io.PrintWriter r4 = r9.getErrPrintWriter()     // Catch: java.lang.Throwable -> L4a
            java.lang.StringBuilder r5 = new java.lang.StringBuilder     // Catch: java.lang.Throwable -> L4a
            r5.<init>()     // Catch: java.lang.Throwable -> L4a
            java.lang.String r6 = "Security exception: "
            r5.append(r6)     // Catch: java.lang.Throwable -> L4a
            java.lang.String r6 = r0.getMessage()     // Catch: java.lang.Throwable -> L4a
            r5.append(r6)     // Catch: java.lang.Throwable -> L4a
            java.lang.String r5 = r5.toString()     // Catch: java.lang.Throwable -> L4a
            r4.println(r5)     // Catch: java.lang.Throwable -> L4a
            r4.println()     // Catch: java.lang.Throwable -> L4a
            r0.printStackTrace(r4)     // Catch: java.lang.Throwable -> L4a
            com.android.internal.util.FastPrintWriter r0 = r9.mOutPrintWriter
            if (r0 == 0) goto L9f
            com.android.internal.util.FastPrintWriter r0 = r9.mOutPrintWriter
            r0.flush()
        L9f:
            com.android.internal.util.FastPrintWriter r0 = r9.mErrPrintWriter
            if (r0 == 0) goto La8
            com.android.internal.util.FastPrintWriter r0 = r9.mErrPrintWriter
            r0.flush()
        La8:
            android.os.ResultReceiver r0 = r9.mResultReceiver
            if (r0 == 0) goto Lad
            goto L44
        Lad:
            return r2
        Lae:
            com.android.internal.util.FastPrintWriter r4 = r9.mOutPrintWriter
            if (r4 == 0) goto Lb8
            com.android.internal.util.FastPrintWriter r4 = r9.mOutPrintWriter
            r4.flush()
        Lb8:
            com.android.internal.util.FastPrintWriter r4 = r9.mErrPrintWriter
            if (r4 == 0) goto Lc1
            com.android.internal.util.FastPrintWriter r4 = r9.mErrPrintWriter
            r4.flush()
        Lc1:
            android.os.ResultReceiver r4 = r9.mResultReceiver
            if (r4 == 0) goto Lca
            android.os.ResultReceiver r4 = r9.mResultReceiver
            r4.send(r2, r3)
        Lca:
            throw r0
        */
        throw new UnsupportedOperationException("Method not decompiled: android.os.ShellCommand.exec(android.os.Binder, java.io.FileDescriptor, java.io.FileDescriptor, java.io.FileDescriptor, java.lang.String[], android.os.ShellCallback, android.os.ResultReceiver):int");
    }

    public synchronized ResultReceiver adoptResultReceiver() {
        ResultReceiver rr = this.mResultReceiver;
        this.mResultReceiver = null;
        return rr;
    }

    public synchronized FileDescriptor getOutFileDescriptor() {
        return this.mOut;
    }

    public synchronized OutputStream getRawOutputStream() {
        if (this.mFileOut == null) {
            this.mFileOut = new FileOutputStream(this.mOut);
        }
        return this.mFileOut;
    }

    public synchronized PrintWriter getOutPrintWriter() {
        if (this.mOutPrintWriter == null) {
            this.mOutPrintWriter = new FastPrintWriter(getRawOutputStream());
        }
        return this.mOutPrintWriter;
    }

    public synchronized FileDescriptor getErrFileDescriptor() {
        return this.mErr;
    }

    public synchronized OutputStream getRawErrorStream() {
        if (this.mFileErr == null) {
            this.mFileErr = new FileOutputStream(this.mErr);
        }
        return this.mFileErr;
    }

    public synchronized PrintWriter getErrPrintWriter() {
        if (this.mErr == null) {
            return getOutPrintWriter();
        }
        if (this.mErrPrintWriter == null) {
            this.mErrPrintWriter = new FastPrintWriter(getRawErrorStream());
        }
        return this.mErrPrintWriter;
    }

    public synchronized FileDescriptor getInFileDescriptor() {
        return this.mIn;
    }

    public synchronized InputStream getRawInputStream() {
        if (this.mFileIn == null) {
            this.mFileIn = new FileInputStream(this.mIn);
        }
        return this.mFileIn;
    }

    public synchronized InputStream getBufferedInputStream() {
        if (this.mInputStream == null) {
            this.mInputStream = new BufferedInputStream(getRawInputStream());
        }
        return this.mInputStream;
    }

    public synchronized ParcelFileDescriptor openFileForSystem(String path, String mode) {
        try {
            ParcelFileDescriptor pfd = getShellCallback().openFile(path, "u:r:system_server:s0", mode);
            if (pfd != null) {
                return pfd;
            }
        } catch (RuntimeException e) {
            PrintWriter errPrintWriter = getErrPrintWriter();
            errPrintWriter.println("Failure opening file: " + e.getMessage());
        }
        PrintWriter errPrintWriter2 = getErrPrintWriter();
        errPrintWriter2.println("Error: Unable to open file: " + path);
        getErrPrintWriter().println("Consider using a file under /data/local/tmp/");
        return null;
    }

    public synchronized String getNextOption() {
        if (this.mCurArgData != null) {
            String prev = this.mArgs[this.mArgPos - 1];
            throw new IllegalArgumentException("No argument expected after \"" + prev + "\"");
        } else if (this.mArgPos >= this.mArgs.length) {
            return null;
        } else {
            String arg = this.mArgs[this.mArgPos];
            if (arg.startsWith(NativeLibraryHelper.CLEAR_ABI_OVERRIDE)) {
                this.mArgPos++;
                if (arg.equals("--")) {
                    return null;
                }
                if (arg.length() > 1 && arg.charAt(1) != '-') {
                    if (arg.length() > 2) {
                        this.mCurArgData = arg.substring(2);
                        return arg.substring(0, 2);
                    }
                    this.mCurArgData = null;
                    return arg;
                }
                this.mCurArgData = null;
                return arg;
            }
            return null;
        }
    }

    public synchronized String getNextArg() {
        if (this.mCurArgData != null) {
            String arg = this.mCurArgData;
            this.mCurArgData = null;
            return arg;
        } else if (this.mArgPos < this.mArgs.length) {
            String[] strArr = this.mArgs;
            int i = this.mArgPos;
            this.mArgPos = i + 1;
            return strArr[i];
        } else {
            return null;
        }
    }

    /* JADX INFO: Access modifiers changed from: private */
    public String peekNextArg() {
        if (this.mCurArgData != null) {
            return this.mCurArgData;
        }
        if (this.mArgPos < this.mArgs.length) {
            return this.mArgs[this.mArgPos];
        }
        return null;
    }

    public synchronized String getNextArgRequired() {
        String arg = getNextArg();
        if (arg == null) {
            String prev = this.mArgs[this.mArgPos - 1];
            throw new IllegalArgumentException("Argument expected after \"" + prev + "\"");
        }
        return arg;
    }

    public synchronized ShellCallback getShellCallback() {
        return this.mShellCallback;
    }

    public synchronized int handleDefaultCommands(String cmd) {
        if ("dump".equals(cmd)) {
            String[] newArgs = new String[this.mArgs.length - 1];
            System.arraycopy(this.mArgs, 1, newArgs, 0, this.mArgs.length - 1);
            this.mTarget.doDump(this.mOut, getOutPrintWriter(), newArgs);
            return 0;
        } else if (cmd == null || "help".equals(cmd) || "-h".equals(cmd)) {
            onHelp();
            return -1;
        } else {
            PrintWriter outPrintWriter = getOutPrintWriter();
            outPrintWriter.println("Unknown command: " + cmd);
            return -1;
        }
    }
}
