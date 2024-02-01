package android.os;

import android.annotation.UnsupportedAppUsage;
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

    public abstract int onCommand(String str);

    public abstract void onHelp();

    public void init(Binder target, FileDescriptor in, FileDescriptor out, FileDescriptor err, String[] args, ShellCallback callback, int firstArgPos) {
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

    /* JADX WARN: Code restructure failed: missing block: B:17:0x0041, code lost:
        if (r0 != null) goto L17;
     */
    /* JADX WARN: Code restructure failed: missing block: B:18:0x0043, code lost:
        r0.send(r2, null);
     */
    /* JADX WARN: Code restructure failed: missing block: B:39:0x009d, code lost:
        if (r0 == null) goto L18;
     */
    /* JADX WARN: Code restructure failed: missing block: B:41:0x00a0, code lost:
        return r2;
     */
    /*
        Code decompiled incorrectly, please refer to instructions dump.
        To view partially-correct code enable 'Show inconsistent code' option in preferences
    */
    public int exec(android.os.Binder r14, java.io.FileDescriptor r15, java.io.FileDescriptor r16, java.io.FileDescriptor r17, java.lang.String[] r18, android.os.ShellCallback r19, android.os.ResultReceiver r20) {
        /*
            r13 = this;
            r9 = r13
            r10 = r18
            if (r10 == 0) goto Lf
            int r0 = r10.length
            if (r0 <= 0) goto Lf
            r0 = 0
            r0 = r10[r0]
            r1 = 1
            r12 = r0
            r11 = r1
            goto L13
        Lf:
            r0 = 0
            r1 = 0
            r12 = r0
            r11 = r1
        L13:
            r1 = r13
            r2 = r14
            r3 = r15
            r4 = r16
            r5 = r17
            r6 = r18
            r7 = r19
            r8 = r11
            r1.init(r2, r3, r4, r5, r6, r7, r8)
            r9.mCmd = r12
            r1 = r20
            r9.mResultReceiver = r1
            r2 = -1
            r3 = 0
            java.lang.String r0 = r9.mCmd     // Catch: java.lang.Throwable -> L47 java.lang.SecurityException -> L6a
            int r0 = r13.onCommand(r0)     // Catch: java.lang.Throwable -> L47 java.lang.SecurityException -> L6a
            r2 = r0
            com.android.internal.util.FastPrintWriter r0 = r9.mOutPrintWriter
            if (r0 == 0) goto L38
            r0.flush()
        L38:
            com.android.internal.util.FastPrintWriter r0 = r9.mErrPrintWriter
            if (r0 == 0) goto L3f
            r0.flush()
        L3f:
            android.os.ResultReceiver r0 = r9.mResultReceiver
            if (r0 == 0) goto La0
        L43:
            r0.send(r2, r3)
            goto La0
        L47:
            r0 = move-exception
            java.io.PrintWriter r4 = r13.getErrPrintWriter()     // Catch: java.lang.Throwable -> La1
            r4.println()     // Catch: java.lang.Throwable -> La1
            java.lang.String r5 = "Exception occurred while executing:"
            r4.println(r5)     // Catch: java.lang.Throwable -> La1
            r0.printStackTrace(r4)     // Catch: java.lang.Throwable -> La1
            com.android.internal.util.FastPrintWriter r0 = r9.mOutPrintWriter
            if (r0 == 0) goto L5e
            r0.flush()
        L5e:
            com.android.internal.util.FastPrintWriter r0 = r9.mErrPrintWriter
            if (r0 == 0) goto L65
            r0.flush()
        L65:
            android.os.ResultReceiver r0 = r9.mResultReceiver
            if (r0 == 0) goto La0
            goto L43
        L6a:
            r0 = move-exception
            java.io.PrintWriter r4 = r13.getErrPrintWriter()     // Catch: java.lang.Throwable -> La1
            java.lang.StringBuilder r5 = new java.lang.StringBuilder     // Catch: java.lang.Throwable -> La1
            r5.<init>()     // Catch: java.lang.Throwable -> La1
            java.lang.String r6 = "Security exception: "
            r5.append(r6)     // Catch: java.lang.Throwable -> La1
            java.lang.String r6 = r0.getMessage()     // Catch: java.lang.Throwable -> La1
            r5.append(r6)     // Catch: java.lang.Throwable -> La1
            java.lang.String r5 = r5.toString()     // Catch: java.lang.Throwable -> La1
            r4.println(r5)     // Catch: java.lang.Throwable -> La1
            r4.println()     // Catch: java.lang.Throwable -> La1
            r0.printStackTrace(r4)     // Catch: java.lang.Throwable -> La1
            com.android.internal.util.FastPrintWriter r0 = r9.mOutPrintWriter
            if (r0 == 0) goto L94
            r0.flush()
        L94:
            com.android.internal.util.FastPrintWriter r0 = r9.mErrPrintWriter
            if (r0 == 0) goto L9b
            r0.flush()
        L9b:
            android.os.ResultReceiver r0 = r9.mResultReceiver
            if (r0 == 0) goto La0
            goto L43
        La0:
            return r2
        La1:
            r0 = move-exception
            com.android.internal.util.FastPrintWriter r4 = r9.mOutPrintWriter
            if (r4 == 0) goto La9
            r4.flush()
        La9:
            com.android.internal.util.FastPrintWriter r4 = r9.mErrPrintWriter
            if (r4 == 0) goto Lb0
            r4.flush()
        Lb0:
            android.os.ResultReceiver r4 = r9.mResultReceiver
            if (r4 == 0) goto Lb7
            r4.send(r2, r3)
        Lb7:
            throw r0
        */
        throw new UnsupportedOperationException("Method not decompiled: android.os.ShellCommand.exec(android.os.Binder, java.io.FileDescriptor, java.io.FileDescriptor, java.io.FileDescriptor, java.lang.String[], android.os.ShellCallback, android.os.ResultReceiver):int");
    }

    public ResultReceiver adoptResultReceiver() {
        ResultReceiver rr = this.mResultReceiver;
        this.mResultReceiver = null;
        return rr;
    }

    public FileDescriptor getOutFileDescriptor() {
        return this.mOut;
    }

    public OutputStream getRawOutputStream() {
        if (this.mFileOut == null) {
            this.mFileOut = new FileOutputStream(this.mOut);
        }
        return this.mFileOut;
    }

    public PrintWriter getOutPrintWriter() {
        if (this.mOutPrintWriter == null) {
            this.mOutPrintWriter = new FastPrintWriter(getRawOutputStream());
        }
        return this.mOutPrintWriter;
    }

    public FileDescriptor getErrFileDescriptor() {
        return this.mErr;
    }

    public OutputStream getRawErrorStream() {
        if (this.mFileErr == null) {
            this.mFileErr = new FileOutputStream(this.mErr);
        }
        return this.mFileErr;
    }

    public PrintWriter getErrPrintWriter() {
        if (this.mErr == null) {
            return getOutPrintWriter();
        }
        if (this.mErrPrintWriter == null) {
            this.mErrPrintWriter = new FastPrintWriter(getRawErrorStream());
        }
        return this.mErrPrintWriter;
    }

    public FileDescriptor getInFileDescriptor() {
        return this.mIn;
    }

    public InputStream getRawInputStream() {
        if (this.mFileIn == null) {
            this.mFileIn = new FileInputStream(this.mIn);
        }
        return this.mFileIn;
    }

    public InputStream getBufferedInputStream() {
        if (this.mInputStream == null) {
            this.mInputStream = new BufferedInputStream(getRawInputStream());
        }
        return this.mInputStream;
    }

    public ParcelFileDescriptor openFileForSystem(String path, String mode) {
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
        if (path == null || !path.startsWith("/data/local/tmp/")) {
            PrintWriter errPrintWriter3 = getErrPrintWriter();
            errPrintWriter3.println("Consider using a file under /data/local/tmp/");
            return null;
        }
        return null;
    }

    public String getNextOption() {
        if (this.mCurArgData != null) {
            String prev = this.mArgs[this.mArgPos - 1];
            throw new IllegalArgumentException("No argument expected after \"" + prev + "\"");
        }
        int i = this.mArgPos;
        String[] strArr = this.mArgs;
        if (i >= strArr.length) {
            return null;
        }
        String arg = strArr[i];
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

    public String getNextArg() {
        if (this.mCurArgData != null) {
            String arg = this.mCurArgData;
            this.mCurArgData = null;
            return arg;
        }
        int i = this.mArgPos;
        String[] strArr = this.mArgs;
        if (i < strArr.length) {
            this.mArgPos = i + 1;
            return strArr[i];
        }
        return null;
    }

    @UnsupportedAppUsage
    public String peekNextArg() {
        String str = this.mCurArgData;
        if (str != null) {
            return str;
        }
        int i = this.mArgPos;
        String[] strArr = this.mArgs;
        if (i < strArr.length) {
            return strArr[i];
        }
        return null;
    }

    public String getNextArgRequired() {
        String arg = getNextArg();
        if (arg == null) {
            String prev = this.mArgs[this.mArgPos - 1];
            throw new IllegalArgumentException("Argument expected after \"" + prev + "\"");
        }
        return arg;
    }

    public ShellCallback getShellCallback() {
        return this.mShellCallback;
    }

    public int handleDefaultCommands(String cmd) {
        if ("dump".equals(cmd)) {
            String[] strArr = this.mArgs;
            String[] newArgs = new String[strArr.length - 1];
            System.arraycopy(strArr, 1, newArgs, 0, strArr.length - 1);
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
