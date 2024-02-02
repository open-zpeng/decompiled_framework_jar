package android.webkit;

import android.R;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
@Deprecated
/* loaded from: classes2.dex */
public class Plugin {
    private String mDescription;
    private String mFileName;
    private PreferencesClickHandler mHandler = new DefaultClickHandler();
    private String mName;
    private String mPath;

    /* loaded from: classes2.dex */
    public interface PreferencesClickHandler {
        synchronized void handleClickEvent(Context context);
    }

    @Deprecated
    public synchronized Plugin(String name, String path, String fileName, String description) {
        this.mName = name;
        this.mPath = path;
        this.mFileName = fileName;
        this.mDescription = description;
    }

    @Deprecated
    public String toString() {
        return this.mName;
    }

    @Deprecated
    public synchronized String getName() {
        return this.mName;
    }

    @Deprecated
    public synchronized String getPath() {
        return this.mPath;
    }

    @Deprecated
    public synchronized String getFileName() {
        return this.mFileName;
    }

    @Deprecated
    public synchronized String getDescription() {
        return this.mDescription;
    }

    @Deprecated
    public synchronized void setName(String name) {
        this.mName = name;
    }

    @Deprecated
    public synchronized void setPath(String path) {
        this.mPath = path;
    }

    @Deprecated
    public synchronized void setFileName(String fileName) {
        this.mFileName = fileName;
    }

    @Deprecated
    public synchronized void setDescription(String description) {
        this.mDescription = description;
    }

    @Deprecated
    public synchronized void setClickHandler(PreferencesClickHandler handler) {
        this.mHandler = handler;
    }

    @Deprecated
    public synchronized void dispatchClickEvent(Context context) {
        if (this.mHandler != null) {
            this.mHandler.handleClickEvent(context);
        }
    }

    @Deprecated
    /* loaded from: classes2.dex */
    private class DefaultClickHandler implements PreferencesClickHandler, DialogInterface.OnClickListener {
        private AlertDialog mDialog;

        private DefaultClickHandler() {
        }

        @Override // android.webkit.Plugin.PreferencesClickHandler
        @Deprecated
        public synchronized void handleClickEvent(Context context) {
            if (this.mDialog == null) {
                this.mDialog = new AlertDialog.Builder(context).setTitle(Plugin.this.mName).setMessage(Plugin.this.mDescription).setPositiveButton(R.string.ok, this).setCancelable(false).show();
            }
        }

        @Override // android.content.DialogInterface.OnClickListener
        @Deprecated
        public void onClick(DialogInterface dialog, int which) {
            this.mDialog.dismiss();
            this.mDialog = null;
        }
    }
}
