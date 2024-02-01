package android.view.textclassifier;

import android.content.Context;
import android.database.ContentObserver;
import android.os.ServiceManager;
import android.provider.Settings;
import android.service.textclassifier.TextClassifierService;
import com.android.internal.annotations.GuardedBy;
import com.android.internal.util.Preconditions;
import java.lang.ref.WeakReference;
/* loaded from: classes2.dex */
public final class TextClassificationManager {
    private static final String LOG_TAG = "TextClassificationManager";
    private final Context mContext;
    @GuardedBy("mLock")
    private TextClassifier mCustomTextClassifier;
    @GuardedBy("mLock")
    private TextClassifier mLocalTextClassifier;
    @GuardedBy("mLock")
    private TextClassificationConstants mSettings;
    @GuardedBy("mLock")
    private TextClassifier mSystemTextClassifier;
    private final Object mLock = new Object();
    private final TextClassificationSessionFactory mDefaultSessionFactory = new TextClassificationSessionFactory() { // from class: android.view.textclassifier.-$$Lambda$TextClassificationManager$JIaezIJbMig_-kVzN6oArzkTsJE
        @Override // android.view.textclassifier.TextClassificationSessionFactory
        public final TextClassifier createTextClassificationSession(TextClassificationContext textClassificationContext) {
            return TextClassificationManager.lambda$new$0(TextClassificationManager.this, textClassificationContext);
        }
    };
    @GuardedBy("mLock")
    private TextClassificationSessionFactory mSessionFactory = this.mDefaultSessionFactory;
    private final SettingsObserver mSettingsObserver = new SettingsObserver(this);

    public static /* synthetic */ TextClassifier lambda$new$0(TextClassificationManager textClassificationManager, TextClassificationContext classificationContext) {
        return new TextClassificationSession(classificationContext, textClassificationManager.getTextClassifier());
    }

    public synchronized TextClassificationManager(Context context) {
        this.mContext = (Context) Preconditions.checkNotNull(context);
    }

    public TextClassifier getTextClassifier() {
        synchronized (this.mLock) {
            if (this.mCustomTextClassifier != null) {
                return this.mCustomTextClassifier;
            } else if (isSystemTextClassifierEnabled()) {
                return getSystemTextClassifier();
            } else {
                return getLocalTextClassifier();
            }
        }
    }

    public void setTextClassifier(TextClassifier textClassifier) {
        synchronized (this.mLock) {
            this.mCustomTextClassifier = textClassifier;
        }
    }

    /* JADX INFO: Access modifiers changed from: private */
    public TextClassifier getTextClassifier(int type) {
        if (type == 0) {
            return getLocalTextClassifier();
        }
        return getSystemTextClassifier();
    }

    private synchronized TextClassificationConstants getSettings() {
        TextClassificationConstants textClassificationConstants;
        synchronized (this.mLock) {
            if (this.mSettings == null) {
                this.mSettings = TextClassificationConstants.loadFromString(Settings.Global.getString(getApplicationContext().getContentResolver(), Settings.Global.TEXT_CLASSIFIER_CONSTANTS));
            }
            textClassificationConstants = this.mSettings;
        }
        return textClassificationConstants;
    }

    public TextClassifier createTextClassificationSession(TextClassificationContext classificationContext) {
        Preconditions.checkNotNull(classificationContext);
        TextClassifier textClassifier = this.mSessionFactory.createTextClassificationSession(classificationContext);
        Preconditions.checkNotNull(textClassifier, "Session Factory should never return null");
        return textClassifier;
    }

    public synchronized TextClassifier createTextClassificationSession(TextClassificationContext classificationContext, TextClassifier textClassifier) {
        Preconditions.checkNotNull(classificationContext);
        Preconditions.checkNotNull(textClassifier);
        return new TextClassificationSession(classificationContext, textClassifier);
    }

    public void setTextClassificationSessionFactory(TextClassificationSessionFactory factory) {
        synchronized (this.mLock) {
            try {
                if (factory != null) {
                    this.mSessionFactory = factory;
                } else {
                    this.mSessionFactory = this.mDefaultSessionFactory;
                }
            } catch (Throwable th) {
                throw th;
            }
        }
    }

    protected void finalize() throws Throwable {
        try {
            if (this.mSettingsObserver != null) {
                getApplicationContext().getContentResolver().unregisterContentObserver(this.mSettingsObserver);
            }
        } finally {
            super.finalize();
        }
    }

    private synchronized TextClassifier getSystemTextClassifier() {
        synchronized (this.mLock) {
            if (this.mSystemTextClassifier == null && isSystemTextClassifierEnabled()) {
                try {
                    this.mSystemTextClassifier = new SystemTextClassifier(this.mContext, getSettings());
                    Log.d(LOG_TAG, "Initialized SystemTextClassifier");
                } catch (ServiceManager.ServiceNotFoundException e) {
                    Log.e(LOG_TAG, "Could not initialize SystemTextClassifier", e);
                }
            }
        }
        if (this.mSystemTextClassifier != null) {
            return this.mSystemTextClassifier;
        }
        return TextClassifier.NO_OP;
    }

    private synchronized TextClassifier getLocalTextClassifier() {
        TextClassifier textClassifier;
        synchronized (this.mLock) {
            if (this.mLocalTextClassifier == null) {
                if (getSettings().isLocalTextClassifierEnabled()) {
                    this.mLocalTextClassifier = new TextClassifierImpl(this.mContext, getSettings(), TextClassifier.NO_OP);
                } else {
                    Log.d(LOG_TAG, "Local TextClassifier disabled");
                    this.mLocalTextClassifier = TextClassifier.NO_OP;
                }
            }
            textClassifier = this.mLocalTextClassifier;
        }
        return textClassifier;
    }

    private synchronized boolean isSystemTextClassifierEnabled() {
        return getSettings().isSystemTextClassifierEnabled() && TextClassifierService.getServiceComponentName(this.mContext) != null;
    }

    /* JADX INFO: Access modifiers changed from: private */
    public synchronized void invalidate() {
        synchronized (this.mLock) {
            this.mSettings = null;
            this.mLocalTextClassifier = null;
            this.mSystemTextClassifier = null;
        }
    }

    synchronized Context getApplicationContext() {
        if (this.mContext.getApplicationContext() != null) {
            return this.mContext.getApplicationContext();
        }
        return this.mContext;
    }

    public static synchronized TextClassificationConstants getSettings(Context context) {
        Preconditions.checkNotNull(context);
        TextClassificationManager tcm = (TextClassificationManager) context.getSystemService(TextClassificationManager.class);
        if (tcm != null) {
            return tcm.getSettings();
        }
        return TextClassificationConstants.loadFromString(Settings.Global.getString(context.getApplicationContext().getContentResolver(), Settings.Global.TEXT_CLASSIFIER_CONSTANTS));
    }

    /* loaded from: classes2.dex */
    private static final class SettingsObserver extends ContentObserver {
        private final WeakReference<TextClassificationManager> mTcm;

        synchronized SettingsObserver(TextClassificationManager tcm) {
            super(null);
            this.mTcm = new WeakReference<>(tcm);
            tcm.getApplicationContext().getContentResolver().registerContentObserver(Settings.Global.getUriFor(Settings.Global.TEXT_CLASSIFIER_CONSTANTS), false, this);
        }

        @Override // android.database.ContentObserver
        public void onChange(boolean selfChange) {
            TextClassificationManager tcm = this.mTcm.get();
            if (tcm != null) {
                tcm.invalidate();
            }
        }
    }
}
