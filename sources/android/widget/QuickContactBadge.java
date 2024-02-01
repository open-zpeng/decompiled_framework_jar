package android.widget;

import android.annotation.UnsupportedAppUsage;
import android.content.AsyncQueryHandler;
import android.content.ContentResolver;
import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Canvas;
import android.graphics.drawable.Drawable;
import android.net.Uri;
import android.os.Bundle;
import android.provider.ContactsContract;
import android.util.AttributeSet;
import android.view.View;
import com.android.internal.R;

/* loaded from: classes3.dex */
public class QuickContactBadge extends ImageView implements View.OnClickListener {
    static final int EMAIL_ID_COLUMN_INDEX = 0;
    static final int EMAIL_LOOKUP_STRING_COLUMN_INDEX = 1;
    private static final String EXTRA_URI_CONTENT = "uri_content";
    static final int PHONE_ID_COLUMN_INDEX = 0;
    static final int PHONE_LOOKUP_STRING_COLUMN_INDEX = 1;
    private static final int TOKEN_EMAIL_LOOKUP = 0;
    private static final int TOKEN_EMAIL_LOOKUP_AND_TRIGGER = 2;
    private static final int TOKEN_PHONE_LOOKUP = 1;
    private static final int TOKEN_PHONE_LOOKUP_AND_TRIGGER = 3;
    private String mContactEmail;
    private String mContactPhone;
    private Uri mContactUri;
    private Drawable mDefaultAvatar;
    protected String[] mExcludeMimes;
    private Bundle mExtras;
    @UnsupportedAppUsage
    private Drawable mOverlay;
    private String mPrioritizedMimeType;
    private QueryHandler mQueryHandler;
    static final String[] EMAIL_LOOKUP_PROJECTION = {"contact_id", ContactsContract.ContactsColumns.LOOKUP_KEY};
    static final String[] PHONE_LOOKUP_PROJECTION = {"_id", ContactsContract.ContactsColumns.LOOKUP_KEY};

    public QuickContactBadge(Context context) {
        this(context, null);
    }

    public QuickContactBadge(Context context, AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public QuickContactBadge(Context context, AttributeSet attrs, int defStyleAttr) {
        this(context, attrs, defStyleAttr, 0);
    }

    public QuickContactBadge(Context context, AttributeSet attrs, int defStyleAttr, int defStyleRes) {
        super(context, attrs, defStyleAttr, defStyleRes);
        this.mExtras = null;
        this.mExcludeMimes = null;
        TypedArray styledAttributes = this.mContext.obtainStyledAttributes(R.styleable.Theme);
        this.mOverlay = styledAttributes.getDrawable(325);
        styledAttributes.recycle();
        setOnClickListener(this);
    }

    /* JADX INFO: Access modifiers changed from: protected */
    @Override // android.widget.ImageView, android.view.View
    public void onAttachedToWindow() {
        super.onAttachedToWindow();
        if (!isInEditMode()) {
            this.mQueryHandler = new QueryHandler(this.mContext.getContentResolver());
        }
    }

    /* JADX INFO: Access modifiers changed from: protected */
    @Override // android.widget.ImageView, android.view.View
    public void drawableStateChanged() {
        super.drawableStateChanged();
        Drawable overlay = this.mOverlay;
        if (overlay != null && overlay.isStateful() && overlay.setState(getDrawableState())) {
            invalidateDrawable(overlay);
        }
    }

    @Override // android.widget.ImageView, android.view.View
    public void drawableHotspotChanged(float x, float y) {
        super.drawableHotspotChanged(x, y);
        Drawable drawable = this.mOverlay;
        if (drawable != null) {
            drawable.setHotspot(x, y);
        }
    }

    public void setMode(int size) {
    }

    public void setPrioritizedMimeType(String prioritizedMimeType) {
        this.mPrioritizedMimeType = prioritizedMimeType;
    }

    /* JADX INFO: Access modifiers changed from: protected */
    @Override // android.widget.ImageView, android.view.View
    public void onDraw(Canvas canvas) {
        Drawable drawable;
        super.onDraw(canvas);
        if (!isEnabled() || (drawable = this.mOverlay) == null || drawable.getIntrinsicWidth() == 0 || this.mOverlay.getIntrinsicHeight() == 0) {
            return;
        }
        this.mOverlay.setBounds(0, 0, getWidth(), getHeight());
        if (this.mPaddingTop == 0 && this.mPaddingLeft == 0) {
            this.mOverlay.draw(canvas);
            return;
        }
        int saveCount = canvas.getSaveCount();
        canvas.save();
        canvas.translate(this.mPaddingLeft, this.mPaddingTop);
        this.mOverlay.draw(canvas);
        canvas.restoreToCount(saveCount);
    }

    private boolean isAssigned() {
        return (this.mContactUri == null && this.mContactEmail == null && this.mContactPhone == null) ? false : true;
    }

    public void setImageToDefault() {
        if (this.mDefaultAvatar == null) {
            this.mDefaultAvatar = this.mContext.getDrawable(R.drawable.ic_contact_picture);
        }
        setImageDrawable(this.mDefaultAvatar);
    }

    public void assignContactUri(Uri contactUri) {
        this.mContactUri = contactUri;
        this.mContactEmail = null;
        this.mContactPhone = null;
        onContactUriChanged();
    }

    public void assignContactFromEmail(String emailAddress, boolean lazyLookup) {
        assignContactFromEmail(emailAddress, lazyLookup, null);
    }

    public void assignContactFromEmail(String emailAddress, boolean lazyLookup, Bundle extras) {
        QueryHandler queryHandler;
        this.mContactEmail = emailAddress;
        this.mExtras = extras;
        if (!lazyLookup && (queryHandler = this.mQueryHandler) != null) {
            queryHandler.startQuery(0, null, Uri.withAppendedPath(ContactsContract.CommonDataKinds.Email.CONTENT_LOOKUP_URI, Uri.encode(this.mContactEmail)), EMAIL_LOOKUP_PROJECTION, null, null, null);
            return;
        }
        this.mContactUri = null;
        onContactUriChanged();
    }

    public void assignContactFromPhone(String phoneNumber, boolean lazyLookup) {
        assignContactFromPhone(phoneNumber, lazyLookup, new Bundle());
    }

    public void assignContactFromPhone(String phoneNumber, boolean lazyLookup, Bundle extras) {
        QueryHandler queryHandler;
        this.mContactPhone = phoneNumber;
        this.mExtras = extras;
        if (!lazyLookup && (queryHandler = this.mQueryHandler) != null) {
            queryHandler.startQuery(1, null, Uri.withAppendedPath(ContactsContract.PhoneLookup.CONTENT_FILTER_URI, this.mContactPhone), PHONE_LOOKUP_PROJECTION, null, null, null);
            return;
        }
        this.mContactUri = null;
        onContactUriChanged();
    }

    public void setOverlay(Drawable overlay) {
        this.mOverlay = overlay;
    }

    /* JADX INFO: Access modifiers changed from: private */
    public void onContactUriChanged() {
        setEnabled(isAssigned());
    }

    @Override // android.view.View.OnClickListener
    public void onClick(View v) {
        Bundle extras = this.mExtras;
        if (extras == null) {
            extras = new Bundle();
        }
        if (this.mContactUri != null) {
            ContactsContract.QuickContact.showQuickContact(getContext(), this, this.mContactUri, this.mExcludeMimes, this.mPrioritizedMimeType);
            return;
        }
        String str = this.mContactEmail;
        if (str != null && this.mQueryHandler != null) {
            extras.putString(EXTRA_URI_CONTENT, str);
            this.mQueryHandler.startQuery(2, extras, Uri.withAppendedPath(ContactsContract.CommonDataKinds.Email.CONTENT_LOOKUP_URI, Uri.encode(this.mContactEmail)), EMAIL_LOOKUP_PROJECTION, null, null, null);
            return;
        }
        String str2 = this.mContactPhone;
        if (str2 != null && this.mQueryHandler != null) {
            extras.putString(EXTRA_URI_CONTENT, str2);
            this.mQueryHandler.startQuery(3, extras, Uri.withAppendedPath(ContactsContract.PhoneLookup.CONTENT_FILTER_URI, this.mContactPhone), PHONE_LOOKUP_PROJECTION, null, null, null);
        }
    }

    @Override // android.widget.ImageView, android.view.View
    public CharSequence getAccessibilityClassName() {
        return QuickContactBadge.class.getName();
    }

    public void setExcludeMimes(String[] excludeMimes) {
        this.mExcludeMimes = excludeMimes;
    }

    /* JADX INFO: Access modifiers changed from: private */
    /* loaded from: classes3.dex */
    public class QueryHandler extends AsyncQueryHandler {
        public QueryHandler(ContentResolver cr) {
            super(cr);
        }

        /* JADX WARN: Removed duplicated region for block: B:32:0x0070  */
        /* JADX WARN: Removed duplicated region for block: B:35:0x007f  */
        /* JADX WARN: Removed duplicated region for block: B:39:0x00a3  */
        /* JADX WARN: Removed duplicated region for block: B:43:? A[RETURN, SYNTHETIC] */
        @Override // android.content.AsyncQueryHandler
        /*
            Code decompiled incorrectly, please refer to instructions dump.
            To view partially-correct code enable 'Show inconsistent code' option in preferences
        */
        protected void onQueryComplete(int r11, java.lang.Object r12, android.database.Cursor r13) {
            /*
                r10 = this;
                r0 = 0
                r1 = 0
                r2 = 0
                if (r12 == 0) goto L9
                r3 = r12
                android.os.Bundle r3 = (android.os.Bundle) r3
                goto Le
            L9:
                android.os.Bundle r3 = new android.os.Bundle
                r3.<init>()
            Le:
                r4 = 0
                java.lang.String r5 = "uri_content"
                r6 = 1
                if (r11 == 0) goto L52
                if (r11 == r6) goto L3c
                r7 = 2
                r8 = 0
                if (r11 == r7) goto L2d
                r7 = 3
                if (r11 == r7) goto L1f
                goto L6e
            L1f:
                r2 = 1
                java.lang.String r7 = "tel"
                java.lang.String r9 = r3.getString(r5)     // Catch: java.lang.Throwable -> L3a
                android.net.Uri r7 = android.net.Uri.fromParts(r7, r9, r8)     // Catch: java.lang.Throwable -> L3a
                r1 = r7
                goto L3c
            L2d:
                r2 = 1
                java.lang.String r7 = "mailto"
                java.lang.String r9 = r3.getString(r5)     // Catch: java.lang.Throwable -> L3a
                android.net.Uri r7 = android.net.Uri.fromParts(r7, r9, r8)     // Catch: java.lang.Throwable -> L3a
                r1 = r7
                goto L52
            L3a:
                r4 = move-exception
                goto L68
            L3c:
                if (r13 == 0) goto L6e
                boolean r7 = r13.moveToFirst()     // Catch: java.lang.Throwable -> L3a
                if (r7 == 0) goto L6e
                long r7 = r13.getLong(r4)     // Catch: java.lang.Throwable -> L3a
                java.lang.String r4 = r13.getString(r6)     // Catch: java.lang.Throwable -> L3a
                android.net.Uri r6 = android.provider.ContactsContract.Contacts.getLookupUri(r7, r4)     // Catch: java.lang.Throwable -> L3a
                r0 = r6
                goto L6e
            L52:
                if (r13 == 0) goto L6e
                boolean r7 = r13.moveToFirst()     // Catch: java.lang.Throwable -> L3a
                if (r7 == 0) goto L6e
                long r7 = r13.getLong(r4)     // Catch: java.lang.Throwable -> L3a
                java.lang.String r4 = r13.getString(r6)     // Catch: java.lang.Throwable -> L3a
                android.net.Uri r6 = android.provider.ContactsContract.Contacts.getLookupUri(r7, r4)     // Catch: java.lang.Throwable -> L3a
                r0 = r6
                goto L6e
            L68:
                if (r13 == 0) goto L6d
                r13.close()
            L6d:
                throw r4
            L6e:
                if (r13 == 0) goto L73
                r13.close()
            L73:
                android.widget.QuickContactBadge r4 = android.widget.QuickContactBadge.this
                android.widget.QuickContactBadge.access$002(r4, r0)
                android.widget.QuickContactBadge r4 = android.widget.QuickContactBadge.this
                android.widget.QuickContactBadge.access$100(r4)
                if (r2 == 0) goto La1
                android.widget.QuickContactBadge r4 = android.widget.QuickContactBadge.this
                android.net.Uri r4 = android.widget.QuickContactBadge.access$000(r4)
                if (r4 == 0) goto La1
                android.widget.QuickContactBadge r4 = android.widget.QuickContactBadge.this
                android.content.Context r4 = r4.getContext()
                android.widget.QuickContactBadge r5 = android.widget.QuickContactBadge.this
                android.net.Uri r6 = android.widget.QuickContactBadge.access$000(r5)
                android.widget.QuickContactBadge r7 = android.widget.QuickContactBadge.this
                java.lang.String[] r7 = r7.mExcludeMimes
                android.widget.QuickContactBadge r8 = android.widget.QuickContactBadge.this
                java.lang.String r8 = android.widget.QuickContactBadge.access$200(r8)
                android.provider.ContactsContract.QuickContact.showQuickContact(r4, r5, r6, r7, r8)
                goto Lba
            La1:
                if (r1 == 0) goto Lba
                android.content.Intent r4 = new android.content.Intent
                java.lang.String r6 = "com.android.contacts.action.SHOW_OR_CREATE_CONTACT"
                r4.<init>(r6, r1)
                r3.remove(r5)
                r4.putExtras(r3)
                android.widget.QuickContactBadge r5 = android.widget.QuickContactBadge.this
                android.content.Context r5 = r5.getContext()
                r5.startActivity(r4)
            Lba:
                return
            */
            throw new UnsupportedOperationException("Method not decompiled: android.widget.QuickContactBadge.QueryHandler.onQueryComplete(int, java.lang.Object, android.database.Cursor):void");
        }
    }
}
