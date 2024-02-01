package android.graphics.drawable;

import android.annotation.UnsupportedAppUsage;
import android.content.ContentResolver;
import android.content.Context;
import android.content.pm.ApplicationInfo;
import android.content.pm.PackageManager;
import android.content.res.ColorStateList;
import android.content.res.Resources;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.BlendMode;
import android.graphics.PorterDuff;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Handler;
import android.os.Message;
import android.os.Parcel;
import android.os.Parcelable;
import android.text.TextUtils;
import android.util.Log;
import com.android.internal.telephony.IccCardConstants;
import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.Arrays;
import java.util.Objects;

/* loaded from: classes.dex */
public final class Icon implements Parcelable {
    public static final int MIN_ASHMEM_ICON_SIZE = 131072;
    private static final String TAG = "Icon";
    public static final int TYPE_ADAPTIVE_BITMAP = 5;
    public static final int TYPE_BITMAP = 1;
    public static final int TYPE_DATA = 3;
    public static final int TYPE_RESOURCE = 2;
    public static final int TYPE_URI = 4;
    private static final int VERSION_STREAM_SERIALIZER = 1;
    private BlendMode mBlendMode;
    private int mInt1;
    private int mInt2;
    private Object mObj1;
    @UnsupportedAppUsage(maxTargetSdk = 28, trackingBug = 115609023)
    private String mString1;
    private ColorStateList mTintList;
    @UnsupportedAppUsage(maxTargetSdk = 28, trackingBug = 115609023)
    private final int mType;
    static final BlendMode DEFAULT_BLEND_MODE = Drawable.DEFAULT_BLEND_MODE;
    public static final Parcelable.Creator<Icon> CREATOR = new Parcelable.Creator<Icon>() { // from class: android.graphics.drawable.Icon.1
        /* JADX WARN: Can't rename method to resolve collision */
        @Override // android.os.Parcelable.Creator
        public Icon createFromParcel(Parcel in) {
            return new Icon(in);
        }

        /* JADX WARN: Can't rename method to resolve collision */
        @Override // android.os.Parcelable.Creator
        public Icon[] newArray(int size) {
            return new Icon[size];
        }
    };

    /* loaded from: classes.dex */
    public @interface IconType {
    }

    /* loaded from: classes.dex */
    public interface OnDrawableLoadedListener {
        void onDrawableLoaded(Drawable drawable);
    }

    @IconType
    public int getType() {
        return this.mType;
    }

    @UnsupportedAppUsage
    public Bitmap getBitmap() {
        int i = this.mType;
        if (i != 1 && i != 5) {
            throw new IllegalStateException("called getBitmap() on " + this);
        }
        return (Bitmap) this.mObj1;
    }

    private void setBitmap(Bitmap b) {
        this.mObj1 = b;
    }

    @UnsupportedAppUsage
    public int getDataLength() {
        int i;
        if (this.mType != 3) {
            throw new IllegalStateException("called getDataLength() on " + this);
        }
        synchronized (this) {
            i = this.mInt1;
        }
        return i;
    }

    @UnsupportedAppUsage(maxTargetSdk = 28, trackingBug = 115609023)
    public int getDataOffset() {
        int i;
        if (this.mType != 3) {
            throw new IllegalStateException("called getDataOffset() on " + this);
        }
        synchronized (this) {
            i = this.mInt2;
        }
        return i;
    }

    @UnsupportedAppUsage(maxTargetSdk = 28, trackingBug = 115609023)
    public byte[] getDataBytes() {
        byte[] bArr;
        if (this.mType != 3) {
            throw new IllegalStateException("called getDataBytes() on " + this);
        }
        synchronized (this) {
            bArr = (byte[]) this.mObj1;
        }
        return bArr;
    }

    @UnsupportedAppUsage(maxTargetSdk = 28, trackingBug = 115609023)
    public Resources getResources() {
        if (this.mType != 2) {
            throw new IllegalStateException("called getResources() on " + this);
        }
        return (Resources) this.mObj1;
    }

    public String getResPackage() {
        if (this.mType != 2) {
            throw new IllegalStateException("called getResPackage() on " + this);
        }
        return this.mString1;
    }

    public int getResId() {
        if (this.mType != 2) {
            throw new IllegalStateException("called getResId() on " + this);
        }
        return this.mInt1;
    }

    public String getUriString() {
        if (this.mType != 4) {
            throw new IllegalStateException("called getUriString() on " + this);
        }
        return this.mString1;
    }

    public Uri getUri() {
        return Uri.parse(getUriString());
    }

    private static final String typeToString(int x) {
        if (x != 1) {
            if (x != 2) {
                if (x != 3) {
                    if (x != 4) {
                        if (x == 5) {
                            return "BITMAP_MASKABLE";
                        }
                        return IccCardConstants.INTENT_VALUE_ICC_UNKNOWN;
                    }
                    return "URI";
                }
                return "DATA";
            }
            return "RESOURCE";
        }
        return "BITMAP";
    }

    public void loadDrawableAsync(Context context, Message andThen) {
        if (andThen.getTarget() == null) {
            throw new IllegalArgumentException("callback message must have a target handler");
        }
        new LoadDrawableTask(context, andThen).runAsync();
    }

    public void loadDrawableAsync(Context context, OnDrawableLoadedListener listener, Handler handler) {
        new LoadDrawableTask(context, handler, listener).runAsync();
    }

    public Drawable loadDrawable(Context context) {
        Drawable result = loadDrawableInner(context);
        if (result != null && (this.mTintList != null || this.mBlendMode != DEFAULT_BLEND_MODE)) {
            result.mutate();
            result.setTintList(this.mTintList);
            result.setTintBlendMode(this.mBlendMode);
        }
        return result;
    }

    private Drawable loadDrawableInner(Context context) {
        int i = this.mType;
        if (i != 1) {
            if (i == 2) {
                if (getResources() == null) {
                    String resPackage = getResPackage();
                    if (TextUtils.isEmpty(resPackage)) {
                        resPackage = context.getPackageName();
                    }
                    if ("android".equals(resPackage)) {
                        this.mObj1 = Resources.getSystem();
                    } else {
                        PackageManager pm = context.getPackageManager();
                        try {
                            ApplicationInfo ai = pm.getApplicationInfo(resPackage, 8192);
                            if (ai != null) {
                                this.mObj1 = pm.getResourcesForApplication(ai);
                            }
                        } catch (PackageManager.NameNotFoundException e) {
                            Log.e(TAG, String.format("Unable to find pkg=%s for icon %s", resPackage, this), e);
                        }
                    }
                }
                try {
                    return getResources().getDrawable(getResId(), context.getTheme());
                } catch (RuntimeException e2) {
                    Log.e(TAG, String.format("Unable to load resource 0x%08x from pkg=%s", Integer.valueOf(getResId()), getResPackage()), e2);
                }
            } else if (i == 3) {
                return new BitmapDrawable(context.getResources(), BitmapFactory.decodeByteArray(getDataBytes(), getDataOffset(), getDataLength()));
            } else {
                if (i == 4) {
                    Uri uri = getUri();
                    String scheme = uri.getScheme();
                    InputStream is = null;
                    if ("content".equals(scheme) || ContentResolver.SCHEME_FILE.equals(scheme)) {
                        try {
                            is = context.getContentResolver().openInputStream(uri);
                        } catch (Exception e3) {
                            Log.w(TAG, "Unable to load image from URI: " + uri, e3);
                        }
                    } else {
                        try {
                            is = new FileInputStream(new File(this.mString1));
                        } catch (FileNotFoundException e4) {
                            Log.w(TAG, "Unable to load image from path: " + uri, e4);
                        }
                    }
                    if (is != null) {
                        return new BitmapDrawable(context.getResources(), BitmapFactory.decodeStream(is));
                    }
                } else if (i == 5) {
                    return new AdaptiveIconDrawable((Drawable) null, new BitmapDrawable(context.getResources(), getBitmap()));
                }
            }
            return null;
        }
        return new BitmapDrawable(context.getResources(), getBitmap());
    }

    public Drawable loadDrawableAsUser(Context context, int userId) {
        if (this.mType == 2) {
            String resPackage = getResPackage();
            if (TextUtils.isEmpty(resPackage)) {
                resPackage = context.getPackageName();
            }
            if (getResources() == null && !getResPackage().equals("android")) {
                PackageManager pm = context.getPackageManager();
                try {
                    this.mObj1 = pm.getResourcesForApplicationAsUser(resPackage, userId);
                } catch (PackageManager.NameNotFoundException e) {
                    Log.e(TAG, String.format("Unable to find pkg=%s user=%d", getResPackage(), Integer.valueOf(userId)), e);
                }
            }
        }
        return loadDrawable(context);
    }

    public void convertToAshmem() {
        int i = this.mType;
        if ((i == 1 || i == 5) && getBitmap().isMutable() && getBitmap().getAllocationByteCount() >= 131072) {
            setBitmap(getBitmap().createAshmemBitmap());
        }
    }

    public void writeToStream(OutputStream stream) throws IOException {
        DataOutputStream dataStream = new DataOutputStream(stream);
        dataStream.writeInt(1);
        dataStream.writeByte(this.mType);
        int i = this.mType;
        if (i != 1) {
            if (i == 2) {
                dataStream.writeUTF(getResPackage());
                dataStream.writeInt(getResId());
                return;
            } else if (i == 3) {
                dataStream.writeInt(getDataLength());
                dataStream.write(getDataBytes(), getDataOffset(), getDataLength());
                return;
            } else if (i == 4) {
                dataStream.writeUTF(getUriString());
                return;
            } else if (i != 5) {
                return;
            }
        }
        getBitmap().compress(Bitmap.CompressFormat.PNG, 100, dataStream);
    }

    private Icon(int mType) {
        this.mBlendMode = Drawable.DEFAULT_BLEND_MODE;
        this.mType = mType;
    }

    public static Icon createFromStream(InputStream stream) throws IOException {
        DataInputStream inputStream = new DataInputStream(stream);
        int version = inputStream.readInt();
        if (version >= 1) {
            int type = inputStream.readByte();
            if (type != 1) {
                if (type == 2) {
                    String packageName = inputStream.readUTF();
                    int resId = inputStream.readInt();
                    return createWithResource(packageName, resId);
                } else if (type == 3) {
                    int length = inputStream.readInt();
                    byte[] data = new byte[length];
                    inputStream.read(data, 0, length);
                    return createWithData(data, 0, length);
                } else if (type == 4) {
                    String uriOrPath = inputStream.readUTF();
                    return createWithContentUri(uriOrPath);
                } else if (type == 5) {
                    return createWithAdaptiveBitmap(BitmapFactory.decodeStream(inputStream));
                } else {
                    return null;
                }
            }
            return createWithBitmap(BitmapFactory.decodeStream(inputStream));
        }
        return null;
    }

    public boolean sameAs(Icon otherIcon) {
        if (otherIcon == this) {
            return true;
        }
        if (this.mType != otherIcon.getType()) {
            return false;
        }
        int i = this.mType;
        if (i != 1) {
            if (i == 2) {
                return getResId() == otherIcon.getResId() && Objects.equals(getResPackage(), otherIcon.getResPackage());
            } else if (i == 3) {
                return getDataLength() == otherIcon.getDataLength() && getDataOffset() == otherIcon.getDataOffset() && Arrays.equals(getDataBytes(), otherIcon.getDataBytes());
            } else if (i == 4) {
                return Objects.equals(getUriString(), otherIcon.getUriString());
            } else {
                if (i != 5) {
                    return false;
                }
            }
        }
        return getBitmap() == otherIcon.getBitmap();
    }

    public static Icon createWithResource(Context context, int resId) {
        if (context == null) {
            throw new IllegalArgumentException("Context must not be null.");
        }
        Icon rep = new Icon(2);
        rep.mInt1 = resId;
        rep.mString1 = context.getPackageName();
        return rep;
    }

    @UnsupportedAppUsage
    public static Icon createWithResource(Resources res, int resId) {
        if (res == null) {
            throw new IllegalArgumentException("Resource must not be null.");
        }
        Icon rep = new Icon(2);
        rep.mInt1 = resId;
        rep.mString1 = res.getResourcePackageName(resId);
        return rep;
    }

    public static Icon createWithResource(String resPackage, int resId) {
        if (resPackage == null) {
            throw new IllegalArgumentException("Resource package name must not be null.");
        }
        Icon rep = new Icon(2);
        rep.mInt1 = resId;
        rep.mString1 = resPackage;
        return rep;
    }

    public static Icon createWithBitmap(Bitmap bits) {
        if (bits == null) {
            throw new IllegalArgumentException("Bitmap must not be null.");
        }
        Icon rep = new Icon(1);
        rep.setBitmap(bits);
        return rep;
    }

    public static Icon createWithAdaptiveBitmap(Bitmap bits) {
        if (bits == null) {
            throw new IllegalArgumentException("Bitmap must not be null.");
        }
        Icon rep = new Icon(5);
        rep.setBitmap(bits);
        return rep;
    }

    public static Icon createWithData(byte[] data, int offset, int length) {
        if (data == null) {
            throw new IllegalArgumentException("Data must not be null.");
        }
        Icon rep = new Icon(3);
        rep.mObj1 = data;
        rep.mInt1 = length;
        rep.mInt2 = offset;
        return rep;
    }

    public static Icon createWithContentUri(String uri) {
        if (uri == null) {
            throw new IllegalArgumentException("Uri must not be null.");
        }
        Icon rep = new Icon(4);
        rep.mString1 = uri;
        return rep;
    }

    public static Icon createWithContentUri(Uri uri) {
        if (uri == null) {
            throw new IllegalArgumentException("Uri must not be null.");
        }
        Icon rep = new Icon(4);
        rep.mString1 = uri.toString();
        return rep;
    }

    public Icon setTint(int tint) {
        return setTintList(ColorStateList.valueOf(tint));
    }

    public Icon setTintList(ColorStateList tintList) {
        this.mTintList = tintList;
        return this;
    }

    public Icon setTintMode(PorterDuff.Mode mode) {
        this.mBlendMode = BlendMode.fromValue(mode.nativeInt);
        return this;
    }

    public Icon setTintBlendMode(BlendMode mode) {
        this.mBlendMode = mode;
        return this;
    }

    @UnsupportedAppUsage
    public boolean hasTint() {
        return (this.mTintList == null && this.mBlendMode == DEFAULT_BLEND_MODE) ? false : true;
    }

    public static Icon createWithFilePath(String path) {
        if (path == null) {
            throw new IllegalArgumentException("Path must not be null.");
        }
        Icon rep = new Icon(4);
        rep.mString1 = path;
        return rep;
    }

    /* JADX WARN: Code restructure failed: missing block: B:10:0x0021, code lost:
        if (r1 != 5) goto L10;
     */
    /* JADX WARN: Removed duplicated region for block: B:20:0x009d  */
    /* JADX WARN: Removed duplicated region for block: B:25:0x00d0  */
    /*
        Code decompiled incorrectly, please refer to instructions dump.
        To view partially-correct code enable 'Show inconsistent code' option in preferences
    */
    public java.lang.String toString() {
        /*
            r11 = this;
            java.lang.StringBuilder r0 = new java.lang.StringBuilder
            java.lang.String r1 = "Icon(typ="
            r0.<init>(r1)
            int r1 = r11.mType
            java.lang.String r1 = typeToString(r1)
            java.lang.StringBuilder r0 = r0.append(r1)
            int r1 = r11.mType
            r2 = 2
            r3 = 0
            r4 = 1
            if (r1 == r4) goto L77
            if (r1 == r2) goto L50
            r5 = 3
            if (r1 == r5) goto L31
            r5 = 4
            if (r1 == r5) goto L24
            r5 = 5
            if (r1 == r5) goto L77
            goto L99
        L24:
            java.lang.String r1 = " uri="
            r0.append(r1)
            java.lang.String r1 = r11.getUriString()
            r0.append(r1)
            goto L99
        L31:
            java.lang.String r1 = " len="
            r0.append(r1)
            int r1 = r11.getDataLength()
            r0.append(r1)
            int r1 = r11.getDataOffset()
            if (r1 == 0) goto L99
            java.lang.String r1 = " off="
            r0.append(r1)
            int r1 = r11.getDataOffset()
            r0.append(r1)
            goto L99
        L50:
            java.lang.String r1 = " pkg="
            r0.append(r1)
            java.lang.String r1 = r11.getResPackage()
            r0.append(r1)
            java.lang.String r1 = " id="
            r0.append(r1)
            java.lang.Object[] r1 = new java.lang.Object[r4]
            int r5 = r11.getResId()
            java.lang.Integer r5 = java.lang.Integer.valueOf(r5)
            r1[r3] = r5
            java.lang.String r5 = "0x%08x"
            java.lang.String r1 = java.lang.String.format(r5, r1)
            r0.append(r1)
            goto L99
        L77:
            java.lang.String r1 = " size="
            r0.append(r1)
            android.graphics.Bitmap r1 = r11.getBitmap()
            int r1 = r1.getWidth()
            r0.append(r1)
            java.lang.String r1 = "x"
            r0.append(r1)
            android.graphics.Bitmap r1 = r11.getBitmap()
            int r1 = r1.getHeight()
            r0.append(r1)
        L99:
            android.content.res.ColorStateList r1 = r11.mTintList
            if (r1 == 0) goto Lca
            java.lang.String r1 = " tint="
            r0.append(r1)
            java.lang.String r1 = ""
            android.content.res.ColorStateList r5 = r11.mTintList
            int[] r5 = r5.getColors()
            int r6 = r5.length
            r7 = r1
            r1 = r3
        Lad:
            if (r1 >= r6) goto Lca
            r8 = r5[r1]
            java.lang.Object[] r9 = new java.lang.Object[r2]
            r9[r3] = r7
            java.lang.Integer r10 = java.lang.Integer.valueOf(r8)
            r9[r4] = r10
            java.lang.String r10 = "%s0x%08x"
            java.lang.String r9 = java.lang.String.format(r10, r9)
            r0.append(r9)
            java.lang.String r7 = "|"
            int r1 = r1 + 1
            goto Lad
        Lca:
            android.graphics.BlendMode r1 = r11.mBlendMode
            android.graphics.BlendMode r2 = android.graphics.drawable.Icon.DEFAULT_BLEND_MODE
            if (r1 == r2) goto Lda
            java.lang.String r1 = " mode="
            r0.append(r1)
            android.graphics.BlendMode r1 = r11.mBlendMode
            r0.append(r1)
        Lda:
            java.lang.String r1 = ")"
            r0.append(r1)
            java.lang.String r1 = r0.toString()
            return r1
        */
        throw new UnsupportedOperationException("Method not decompiled: android.graphics.drawable.Icon.toString():java.lang.String");
    }

    @Override // android.os.Parcelable
    public int describeContents() {
        int i = this.mType;
        return (i == 1 || i == 5 || i == 3) ? 1 : 0;
    }

    /* JADX WARN: Removed duplicated region for block: B:25:0x009c  */
    /*
        Code decompiled incorrectly, please refer to instructions dump.
        To view partially-correct code enable 'Show inconsistent code' option in preferences
    */
    private Icon(android.os.Parcel r6) {
        /*
            r5 = this;
            int r0 = r6.readInt()
            r5.<init>(r0)
            int r0 = r5.mType
            r1 = 1
            if (r0 == r1) goto L8b
            r2 = 2
            if (r0 == r2) goto L7e
            r2 = 3
            if (r0 == r2) goto L49
            r2 = 4
            if (r0 == r2) goto L42
            r2 = 5
            if (r0 != r2) goto L19
            goto L8b
        L19:
            java.lang.RuntimeException r0 = new java.lang.RuntimeException
            java.lang.StringBuilder r1 = new java.lang.StringBuilder
            r1.<init>()
            java.lang.String r2 = "invalid "
            r1.append(r2)
            java.lang.Class r2 = r5.getClass()
            java.lang.String r2 = r2.getSimpleName()
            r1.append(r2)
            java.lang.String r2 = " type in parcel: "
            r1.append(r2)
            int r2 = r5.mType
            r1.append(r2)
            java.lang.String r1 = r1.toString()
            r0.<init>(r1)
            throw r0
        L42:
            java.lang.String r0 = r6.readString()
            r5.mString1 = r0
            goto L96
        L49:
            int r0 = r6.readInt()
            byte[] r2 = r6.readBlob()
            int r3 = r2.length
            if (r0 != r3) goto L59
            r5.mInt1 = r0
            r5.mObj1 = r2
            goto L96
        L59:
            java.lang.RuntimeException r1 = new java.lang.RuntimeException
            java.lang.StringBuilder r3 = new java.lang.StringBuilder
            r3.<init>()
            java.lang.String r4 = "internal unparceling error: blob length ("
            r3.append(r4)
            int r4 = r2.length
            r3.append(r4)
            java.lang.String r4 = ") != expected length ("
            r3.append(r4)
            r3.append(r0)
            java.lang.String r4 = ")"
            r3.append(r4)
            java.lang.String r3 = r3.toString()
            r1.<init>(r3)
            throw r1
        L7e:
            java.lang.String r0 = r6.readString()
            int r2 = r6.readInt()
            r5.mString1 = r0
            r5.mInt1 = r2
            goto L96
        L8b:
            android.os.Parcelable$Creator<android.graphics.Bitmap> r0 = android.graphics.Bitmap.CREATOR
            java.lang.Object r0 = r0.createFromParcel(r6)
            android.graphics.Bitmap r0 = (android.graphics.Bitmap) r0
            r5.mObj1 = r0
        L96:
            int r0 = r6.readInt()
            if (r0 != r1) goto La6
            android.os.Parcelable$Creator<android.content.res.ColorStateList> r0 = android.content.res.ColorStateList.CREATOR
            java.lang.Object r0 = r0.createFromParcel(r6)
            android.content.res.ColorStateList r0 = (android.content.res.ColorStateList) r0
            r5.mTintList = r0
        La6:
            int r0 = r6.readInt()
            android.graphics.BlendMode r0 = android.graphics.BlendMode.fromValue(r0)
            r5.mBlendMode = r0
            return
        */
        throw new UnsupportedOperationException("Method not decompiled: android.graphics.drawable.Icon.<init>(android.os.Parcel):void");
    }

    /* JADX WARN: Code restructure failed: missing block: B:11:0x0014, code lost:
        if (r0 != 5) goto L11;
     */
    /* JADX WARN: Removed duplicated region for block: B:19:0x0055  */
    /* JADX WARN: Removed duplicated region for block: B:20:0x005a  */
    @Override // android.os.Parcelable
    /*
        Code decompiled incorrectly, please refer to instructions dump.
        To view partially-correct code enable 'Show inconsistent code' option in preferences
    */
    public void writeToParcel(android.os.Parcel r5, int r6) {
        /*
            r4 = this;
            int r0 = r4.mType
            r5.writeInt(r0)
            int r0 = r4.mType
            r1 = 1
            if (r0 == r1) goto L45
            r2 = 2
            if (r0 == r2) goto L36
            r2 = 3
            if (r0 == r2) goto L1f
            r2 = 4
            if (r0 == r2) goto L17
            r2 = 5
            if (r0 == r2) goto L45
            goto L51
        L17:
            java.lang.String r0 = r4.getUriString()
            r5.writeString(r0)
            goto L51
        L1f:
            int r0 = r4.getDataLength()
            r5.writeInt(r0)
            byte[] r0 = r4.getDataBytes()
            int r2 = r4.getDataOffset()
            int r3 = r4.getDataLength()
            r5.writeBlob(r0, r2, r3)
            goto L51
        L36:
            java.lang.String r0 = r4.getResPackage()
            r5.writeString(r0)
            int r0 = r4.getResId()
            r5.writeInt(r0)
            goto L51
        L45:
            android.graphics.Bitmap r0 = r4.getBitmap()
            android.graphics.Bitmap r2 = r4.getBitmap()
            r2.writeToParcel(r5, r6)
        L51:
            android.content.res.ColorStateList r0 = r4.mTintList
            if (r0 != 0) goto L5a
            r0 = 0
            r5.writeInt(r0)
            goto L62
        L5a:
            r5.writeInt(r1)
            android.content.res.ColorStateList r0 = r4.mTintList
            r0.writeToParcel(r5, r6)
        L62:
            android.graphics.BlendMode r0 = r4.mBlendMode
            int r0 = android.graphics.BlendMode.toValue(r0)
            r5.writeInt(r0)
            return
        */
        throw new UnsupportedOperationException("Method not decompiled: android.graphics.drawable.Icon.writeToParcel(android.os.Parcel, int):void");
    }

    public static Bitmap scaleDownIfNecessary(Bitmap bitmap, int maxWidth, int maxHeight) {
        int bitmapWidth = bitmap.getWidth();
        int bitmapHeight = bitmap.getHeight();
        if (bitmapWidth > maxWidth || bitmapHeight > maxHeight) {
            float scale = Math.min(maxWidth / bitmapWidth, maxHeight / bitmapHeight);
            return Bitmap.createScaledBitmap(bitmap, Math.max(1, (int) (bitmapWidth * scale)), Math.max(1, (int) (bitmapHeight * scale)), true);
        }
        return bitmap;
    }

    public void scaleDownIfNecessary(int maxWidth, int maxHeight) {
        int i = this.mType;
        if (i != 1 && i != 5) {
            return;
        }
        Bitmap bitmap = getBitmap();
        setBitmap(scaleDownIfNecessary(bitmap, maxWidth, maxHeight));
    }

    /* loaded from: classes.dex */
    private class LoadDrawableTask implements Runnable {
        final Context mContext;
        final Message mMessage;

        public LoadDrawableTask(Context context, Handler handler, final OnDrawableLoadedListener listener) {
            this.mContext = context;
            this.mMessage = Message.obtain(handler, new Runnable() { // from class: android.graphics.drawable.Icon.LoadDrawableTask.1
                @Override // java.lang.Runnable
                public void run() {
                    listener.onDrawableLoaded((Drawable) LoadDrawableTask.this.mMessage.obj);
                }
            });
        }

        public LoadDrawableTask(Context context, Message message) {
            this.mContext = context;
            this.mMessage = message;
        }

        @Override // java.lang.Runnable
        public void run() {
            this.mMessage.obj = Icon.this.loadDrawable(this.mContext);
            this.mMessage.sendToTarget();
        }

        public void runAsync() {
            AsyncTask.THREAD_POOL_EXECUTOR.execute(this);
        }
    }
}
