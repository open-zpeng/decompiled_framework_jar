package android.media;

import android.content.Context;
import android.media.SubtitleTrack;
import android.os.Handler;
import android.os.Looper;
import android.os.Message;
import android.view.accessibility.CaptioningManager;
import java.util.Iterator;
import java.util.Locale;
import java.util.Vector;
/* loaded from: classes.dex */
public class SubtitleController {
    static final /* synthetic */ boolean $assertionsDisabled = false;
    private static final int WHAT_HIDE = 2;
    private static final int WHAT_SELECT_DEFAULT_TRACK = 4;
    private static final int WHAT_SELECT_TRACK = 3;
    private static final int WHAT_SHOW = 1;
    private Anchor mAnchor;
    private CaptioningManager mCaptioningManager;
    public protected Handler mHandler;
    private Listener mListener;
    private SubtitleTrack mSelectedTrack;
    private MediaTimeProvider mTimeProvider;
    private final Handler.Callback mCallback = new Handler.Callback() { // from class: android.media.SubtitleController.1
        @Override // android.os.Handler.Callback
        public boolean handleMessage(Message msg) {
            switch (msg.what) {
                case 1:
                    SubtitleController.this.doShow();
                    return true;
                case 2:
                    SubtitleController.this.doHide();
                    return true;
                case 3:
                    SubtitleController.this.doSelectTrack((SubtitleTrack) msg.obj);
                    return true;
                case 4:
                    SubtitleController.this.doSelectDefaultTrack();
                    return true;
                default:
                    return false;
            }
        }
    };
    private CaptioningManager.CaptioningChangeListener mCaptioningChangeListener = new CaptioningManager.CaptioningChangeListener() { // from class: android.media.SubtitleController.2
        @Override // android.view.accessibility.CaptioningManager.CaptioningChangeListener
        public void onEnabledChanged(boolean enabled) {
            SubtitleController.this.selectDefaultTrack();
        }

        @Override // android.view.accessibility.CaptioningManager.CaptioningChangeListener
        public void onLocaleChanged(Locale locale) {
            SubtitleController.this.selectDefaultTrack();
        }
    };
    private boolean mTrackIsExplicit = false;
    private boolean mVisibilityIsExplicit = false;
    private Vector<Renderer> mRenderers = new Vector<>();
    private boolean mShowing = false;
    private Vector<SubtitleTrack> mTracks = new Vector<>();

    /* loaded from: classes.dex */
    public interface Anchor {
        synchronized Looper getSubtitleLooper();

        synchronized void setSubtitleWidget(SubtitleTrack.RenderingWidget renderingWidget);
    }

    /* loaded from: classes.dex */
    public interface Listener {
        synchronized void onSubtitleTrackSelected(SubtitleTrack subtitleTrack);
    }

    /* loaded from: classes.dex */
    public static abstract class Renderer {
        public abstract synchronized SubtitleTrack createTrack(MediaFormat mediaFormat);

        public abstract synchronized boolean supports(MediaFormat mediaFormat);
    }

    /* JADX INFO: Access modifiers changed from: private */
    public SubtitleController(Context context, MediaTimeProvider timeProvider, Listener listener) {
        this.mTimeProvider = timeProvider;
        this.mListener = listener;
        this.mCaptioningManager = (CaptioningManager) context.getSystemService(Context.CAPTIONING_SERVICE);
    }

    protected void finalize() throws Throwable {
        this.mCaptioningManager.removeCaptioningChangeListener(this.mCaptioningChangeListener);
        super.finalize();
    }

    public synchronized SubtitleTrack[] getTracks() {
        SubtitleTrack[] tracks;
        synchronized (this.mTracks) {
            tracks = new SubtitleTrack[this.mTracks.size()];
            this.mTracks.toArray(tracks);
        }
        return tracks;
    }

    public synchronized SubtitleTrack getSelectedTrack() {
        return this.mSelectedTrack;
    }

    private synchronized SubtitleTrack.RenderingWidget getRenderingWidget() {
        if (this.mSelectedTrack == null) {
            return null;
        }
        return this.mSelectedTrack.getRenderingWidget();
    }

    public synchronized boolean selectTrack(SubtitleTrack track) {
        if (track != null && !this.mTracks.contains(track)) {
            return false;
        }
        processOnAnchor(this.mHandler.obtainMessage(3, track));
        return true;
    }

    /* JADX INFO: Access modifiers changed from: private */
    public synchronized void doSelectTrack(SubtitleTrack track) {
        this.mTrackIsExplicit = true;
        if (this.mSelectedTrack == track) {
            return;
        }
        if (this.mSelectedTrack != null) {
            this.mSelectedTrack.hide();
            this.mSelectedTrack.setTimeProvider(null);
        }
        this.mSelectedTrack = track;
        if (this.mAnchor != null) {
            this.mAnchor.setSubtitleWidget(getRenderingWidget());
        }
        if (this.mSelectedTrack != null) {
            this.mSelectedTrack.setTimeProvider(this.mTimeProvider);
            this.mSelectedTrack.show();
        }
        if (this.mListener != null) {
            this.mListener.onSubtitleTrackSelected(track);
        }
    }

    /* JADX WARN: Removed duplicated region for block: B:33:0x0084  */
    /* JADX WARN: Removed duplicated region for block: B:34:0x0086  */
    /* JADX WARN: Removed duplicated region for block: B:41:0x0095  */
    /* JADX WARN: Removed duplicated region for block: B:42:0x0098  */
    /* JADX WARN: Removed duplicated region for block: B:45:0x009e  */
    /* JADX WARN: Removed duplicated region for block: B:46:0x00a1  */
    /*
        Code decompiled incorrectly, please refer to instructions dump.
        To view partially-correct add '--show-bad-code' argument
    */
    public synchronized android.media.SubtitleTrack getDefaultTrack() {
        /*
            r18 = this;
            r1 = r18
            r2 = 0
            r3 = -1
            android.view.accessibility.CaptioningManager r0 = r1.mCaptioningManager
            java.util.Locale r4 = r0.getLocale()
            r0 = r4
            if (r0 != 0) goto L11
            java.util.Locale r0 = java.util.Locale.getDefault()
        L11:
            r5 = r0
            android.view.accessibility.CaptioningManager r0 = r1.mCaptioningManager
            boolean r0 = r0.isEnabled()
            r6 = 1
            r0 = r0 ^ r6
            r7 = r0
            java.util.Vector<android.media.SubtitleTrack> r8 = r1.mTracks
            monitor-enter(r8)
            java.util.Vector<android.media.SubtitleTrack> r0 = r1.mTracks     // Catch: java.lang.Throwable -> Lc0
            java.util.Iterator r0 = r0.iterator()     // Catch: java.lang.Throwable -> Lc0
        L24:
            boolean r9 = r0.hasNext()     // Catch: java.lang.Throwable -> Lc0
            if (r9 == 0) goto Lbe
            java.lang.Object r9 = r0.next()     // Catch: java.lang.Throwable -> Lc0
            android.media.SubtitleTrack r9 = (android.media.SubtitleTrack) r9     // Catch: java.lang.Throwable -> Lc0
            android.media.MediaFormat r10 = r9.getFormat()     // Catch: java.lang.Throwable -> Lc0
            java.lang.String r11 = "language"
            java.lang.String r11 = r10.getString(r11)     // Catch: java.lang.Throwable -> Lc0
            java.lang.String r12 = "is-forced-subtitle"
            r13 = 0
            int r12 = r10.getInteger(r12, r13)     // Catch: java.lang.Throwable -> Lc0
            if (r12 == 0) goto L45
            r12 = r6
            goto L46
        L45:
            r12 = r13
        L46:
            java.lang.String r14 = "is-autoselect"
            int r14 = r10.getInteger(r14, r6)     // Catch: java.lang.Throwable -> Lc0
            if (r14 == 0) goto L50
            r14 = r6
            goto L51
        L50:
            r14 = r13
        L51:
            java.lang.String r15 = "is-default"
            int r15 = r10.getInteger(r15, r13)     // Catch: java.lang.Throwable -> Lc0
            if (r15 == 0) goto L5b
            r15 = r6
            goto L5c
        L5b:
            r15 = r13
        L5c:
            if (r5 == 0) goto L81
            java.lang.String r6 = r5.getLanguage()     // Catch: java.lang.Throwable -> Lc0
            java.lang.String r13 = ""
            boolean r6 = r6.equals(r13)     // Catch: java.lang.Throwable -> Lc0
            if (r6 != 0) goto L81
            java.lang.String r6 = r5.getISO3Language()     // Catch: java.lang.Throwable -> Lc0
            boolean r6 = r6.equals(r11)     // Catch: java.lang.Throwable -> Lc0
            if (r6 != 0) goto L81
            java.lang.String r6 = r5.getLanguage()     // Catch: java.lang.Throwable -> Lc0
            boolean r6 = r6.equals(r11)     // Catch: java.lang.Throwable -> Lc0
            if (r6 == 0) goto L7f
            goto L81
        L7f:
            r6 = 0
            goto L82
        L81:
            r6 = 1
        L82:
            if (r12 == 0) goto L86
            r13 = 0
            goto L88
        L86:
            r13 = 8
        L88:
            if (r4 != 0) goto L8f
            if (r15 == 0) goto L8f
            r17 = 4
            goto L91
        L8f:
            r17 = 0
        L91:
            int r13 = r13 + r17
            if (r14 == 0) goto L98
            r17 = 0
            goto L9a
        L98:
            r17 = 2
        L9a:
            int r13 = r13 + r17
            if (r6 == 0) goto La1
            r16 = 1
            goto La3
        La1:
            r16 = 0
        La3:
            int r13 = r13 + r16
            if (r7 == 0) goto Lad
            if (r12 != 0) goto Lad
        Laa:
            r6 = 1
            goto L24
        Lad:
            if (r4 != 0) goto Lb1
            if (r15 != 0) goto Lb9
        Lb1:
            if (r6 == 0) goto Lbd
            if (r14 != 0) goto Lb9
            if (r12 != 0) goto Lb9
            if (r4 == 0) goto Lbd
        Lb9:
            if (r13 <= r3) goto Lbd
            r3 = r13
            r2 = r9
        Lbd:
            goto Laa
        Lbe:
            monitor-exit(r8)     // Catch: java.lang.Throwable -> Lc0
            return r2
        Lc0:
            r0 = move-exception
            monitor-exit(r8)     // Catch: java.lang.Throwable -> Lc0
            throw r0
        */
        throw new UnsupportedOperationException("Method not decompiled: android.media.SubtitleController.getDefaultTrack():android.media.SubtitleTrack");
    }

    public synchronized void selectDefaultTrack() {
        processOnAnchor(this.mHandler.obtainMessage(4));
    }

    /* JADX INFO: Access modifiers changed from: private */
    public synchronized void doSelectDefaultTrack() {
        if (this.mTrackIsExplicit) {
            if (!this.mVisibilityIsExplicit) {
                if (this.mCaptioningManager.isEnabled() || (this.mSelectedTrack != null && this.mSelectedTrack.getFormat().getInteger(MediaFormat.KEY_IS_FORCED_SUBTITLE, 0) != 0)) {
                    show();
                } else if (this.mSelectedTrack != null && this.mSelectedTrack.getTrackType() == 4) {
                    hide();
                }
                this.mVisibilityIsExplicit = false;
                return;
            }
            return;
        }
        SubtitleTrack track = getDefaultTrack();
        if (track != null) {
            selectTrack(track);
            this.mTrackIsExplicit = false;
            if (!this.mVisibilityIsExplicit) {
                show();
                this.mVisibilityIsExplicit = false;
            }
        }
    }

    /* JADX INFO: Access modifiers changed from: private */
    public void reset() {
        checkAnchorLooper();
        hide();
        selectTrack(null);
        this.mTracks.clear();
        this.mTrackIsExplicit = false;
        this.mVisibilityIsExplicit = false;
        this.mCaptioningManager.removeCaptioningChangeListener(this.mCaptioningChangeListener);
    }

    public synchronized SubtitleTrack addTrack(MediaFormat format) {
        SubtitleTrack track;
        synchronized (this.mRenderers) {
            Iterator<Renderer> it = this.mRenderers.iterator();
            while (it.hasNext()) {
                Renderer renderer = it.next();
                if (renderer.supports(format) && (track = renderer.createTrack(format)) != null) {
                    synchronized (this.mTracks) {
                        if (this.mTracks.size() == 0) {
                            this.mCaptioningManager.addCaptioningChangeListener(this.mCaptioningChangeListener);
                        }
                        this.mTracks.add(track);
                    }
                    return track;
                }
            }
            return null;
        }
    }

    private protected void show() {
        processOnAnchor(this.mHandler.obtainMessage(1));
    }

    /* JADX INFO: Access modifiers changed from: private */
    public synchronized void doShow() {
        this.mShowing = true;
        this.mVisibilityIsExplicit = true;
        if (this.mSelectedTrack != null) {
            this.mSelectedTrack.show();
        }
    }

    private protected void hide() {
        processOnAnchor(this.mHandler.obtainMessage(2));
    }

    /* JADX INFO: Access modifiers changed from: private */
    public synchronized void doHide() {
        this.mVisibilityIsExplicit = true;
        if (this.mSelectedTrack != null) {
            this.mSelectedTrack.hide();
        }
        this.mShowing = false;
    }

    /* JADX INFO: Access modifiers changed from: private */
    public void registerRenderer(Renderer renderer) {
        synchronized (this.mRenderers) {
            if (!this.mRenderers.contains(renderer)) {
                this.mRenderers.add(renderer);
            }
        }
    }

    public synchronized boolean hasRendererFor(MediaFormat format) {
        synchronized (this.mRenderers) {
            Iterator<Renderer> it = this.mRenderers.iterator();
            while (it.hasNext()) {
                Renderer renderer = it.next();
                if (renderer.supports(format)) {
                    return true;
                }
            }
            return false;
        }
    }

    public synchronized void setAnchor(Anchor anchor) {
        if (this.mAnchor == anchor) {
            return;
        }
        if (this.mAnchor != null) {
            checkAnchorLooper();
            this.mAnchor.setSubtitleWidget(null);
        }
        this.mAnchor = anchor;
        this.mHandler = null;
        if (this.mAnchor != null) {
            this.mHandler = new Handler(this.mAnchor.getSubtitleLooper(), this.mCallback);
            checkAnchorLooper();
            this.mAnchor.setSubtitleWidget(getRenderingWidget());
        }
    }

    private synchronized void checkAnchorLooper() {
    }

    private synchronized void processOnAnchor(Message m) {
        if (Looper.myLooper() == this.mHandler.getLooper()) {
            this.mHandler.dispatchMessage(m);
        } else {
            this.mHandler.sendMessage(m);
        }
    }
}
