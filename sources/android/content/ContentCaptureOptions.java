package android.content;

import android.app.ActivityThread;
import android.os.Parcel;
import android.os.Parcelable;
import android.util.ArraySet;
import android.util.Log;
import android.view.contentcapture.ContentCaptureManager;
import com.android.internal.annotations.VisibleForTesting;
import java.io.PrintWriter;

/* loaded from: classes.dex */
public final class ContentCaptureOptions implements Parcelable {
    public final int idleFlushingFrequencyMs;
    public final boolean lite;
    public final int logHistorySize;
    public final int loggingLevel;
    public final int maxBufferSize;
    public final int textChangeFlushingFrequencyMs;
    public final ArraySet<ComponentName> whitelistedComponents;
    private static final String TAG = ContentCaptureOptions.class.getSimpleName();
    public static final Parcelable.Creator<ContentCaptureOptions> CREATOR = new Parcelable.Creator<ContentCaptureOptions>() { // from class: android.content.ContentCaptureOptions.1
        /* JADX WARN: Can't rename method to resolve collision */
        @Override // android.os.Parcelable.Creator
        public ContentCaptureOptions createFromParcel(Parcel parcel) {
            boolean lite = parcel.readBoolean();
            int loggingLevel = parcel.readInt();
            if (lite) {
                return new ContentCaptureOptions(loggingLevel);
            }
            int maxBufferSize = parcel.readInt();
            int idleFlushingFrequencyMs = parcel.readInt();
            int textChangeFlushingFrequencyMs = parcel.readInt();
            int logHistorySize = parcel.readInt();
            return new ContentCaptureOptions(loggingLevel, maxBufferSize, idleFlushingFrequencyMs, textChangeFlushingFrequencyMs, logHistorySize, parcel.readArraySet(null));
        }

        /* JADX WARN: Can't rename method to resolve collision */
        @Override // android.os.Parcelable.Creator
        public ContentCaptureOptions[] newArray(int size) {
            return new ContentCaptureOptions[size];
        }
    };

    public ContentCaptureOptions(int loggingLevel) {
        this(true, loggingLevel, 0, 0, 0, 0, null);
    }

    public ContentCaptureOptions(int loggingLevel, int maxBufferSize, int idleFlushingFrequencyMs, int textChangeFlushingFrequencyMs, int logHistorySize, ArraySet<ComponentName> whitelistedComponents) {
        this(false, loggingLevel, maxBufferSize, idleFlushingFrequencyMs, textChangeFlushingFrequencyMs, logHistorySize, whitelistedComponents);
    }

    @VisibleForTesting
    public ContentCaptureOptions(ArraySet<ComponentName> whitelistedComponents) {
        this(2, 100, 5000, 1000, 10, whitelistedComponents);
    }

    private ContentCaptureOptions(boolean lite, int loggingLevel, int maxBufferSize, int idleFlushingFrequencyMs, int textChangeFlushingFrequencyMs, int logHistorySize, ArraySet<ComponentName> whitelistedComponents) {
        this.lite = lite;
        this.loggingLevel = loggingLevel;
        this.maxBufferSize = maxBufferSize;
        this.idleFlushingFrequencyMs = idleFlushingFrequencyMs;
        this.textChangeFlushingFrequencyMs = textChangeFlushingFrequencyMs;
        this.logHistorySize = logHistorySize;
        this.whitelistedComponents = whitelistedComponents;
    }

    public static ContentCaptureOptions forWhitelistingItself() {
        ActivityThread at = ActivityThread.currentActivityThread();
        if (at == null) {
            throw new IllegalStateException("No ActivityThread");
        }
        String packageName = at.getApplication().getPackageName();
        if (!"android.contentcaptureservice.cts".equals(packageName)) {
            String str = TAG;
            Log.e(str, "forWhitelistingItself(): called by " + packageName);
            throw new SecurityException("Thou shall not pass!");
        }
        ContentCaptureOptions options = new ContentCaptureOptions((ArraySet<ComponentName>) null);
        String str2 = TAG;
        Log.i(str2, "forWhitelistingItself(" + packageName + "): " + options);
        return options;
    }

    @VisibleForTesting
    public boolean isWhitelisted(Context context) {
        if (this.whitelistedComponents == null) {
            return true;
        }
        ContentCaptureManager.ContentCaptureClient client = context.getContentCaptureClient();
        if (client == null) {
            String str = TAG;
            Log.w(str, "isWhitelisted(): no ContentCaptureClient on " + context);
            return false;
        }
        return this.whitelistedComponents.contains(client.contentCaptureClientGetComponentName());
    }

    public String toString() {
        if (this.lite) {
            return "ContentCaptureOptions [loggingLevel=" + this.loggingLevel + " (lite)]";
        }
        StringBuilder string = new StringBuilder("ContentCaptureOptions [");
        string.append("loggingLevel=");
        string.append(this.loggingLevel);
        string.append(", maxBufferSize=");
        string.append(this.maxBufferSize);
        string.append(", idleFlushingFrequencyMs=");
        string.append(this.idleFlushingFrequencyMs);
        string.append(", textChangeFlushingFrequencyMs=");
        string.append(this.textChangeFlushingFrequencyMs);
        string.append(", logHistorySize=");
        string.append(this.logHistorySize);
        if (this.whitelistedComponents != null) {
            string.append(", whitelisted=");
            string.append(this.whitelistedComponents);
        }
        string.append(']');
        return string.toString();
    }

    public void dumpShort(PrintWriter pw) {
        pw.print("logLvl=");
        pw.print(this.loggingLevel);
        if (this.lite) {
            pw.print(", lite");
            return;
        }
        pw.print(", bufferSize=");
        pw.print(this.maxBufferSize);
        pw.print(", idle=");
        pw.print(this.idleFlushingFrequencyMs);
        pw.print(", textIdle=");
        pw.print(this.textChangeFlushingFrequencyMs);
        pw.print(", logSize=");
        pw.print(this.logHistorySize);
        if (this.whitelistedComponents != null) {
            pw.print(", whitelisted=");
            pw.print(this.whitelistedComponents);
        }
    }

    @Override // android.os.Parcelable
    public int describeContents() {
        return 0;
    }

    @Override // android.os.Parcelable
    public void writeToParcel(Parcel parcel, int flags) {
        parcel.writeBoolean(this.lite);
        parcel.writeInt(this.loggingLevel);
        if (this.lite) {
            return;
        }
        parcel.writeInt(this.maxBufferSize);
        parcel.writeInt(this.idleFlushingFrequencyMs);
        parcel.writeInt(this.textChangeFlushingFrequencyMs);
        parcel.writeInt(this.logHistorySize);
        parcel.writeArraySet(this.whitelistedComponents);
    }
}
