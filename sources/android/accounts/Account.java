package android.accounts;

import android.accounts.IAccountManager;
import android.annotation.UnsupportedAppUsage;
import android.os.BadParcelableException;
import android.os.Parcel;
import android.os.Parcelable;
import android.os.RemoteException;
import android.os.ServiceManager;
import android.text.TextUtils;
import android.util.ArraySet;
import android.util.Log;
import com.android.internal.annotations.GuardedBy;
import com.android.internal.transition.EpicenterTranslateClipReveal;
import java.util.Set;

/* loaded from: classes.dex */
public class Account implements Parcelable {
    @UnsupportedAppUsage
    private static final String TAG = "Account";
    @UnsupportedAppUsage
    private final String accessId;
    private String mSafeName;
    public final String name;
    public final String type;
    @GuardedBy({"sAccessedAccounts"})
    private static final Set<Account> sAccessedAccounts = new ArraySet();
    public static final Parcelable.Creator<Account> CREATOR = new Parcelable.Creator<Account>() { // from class: android.accounts.Account.1
        /* JADX WARN: Can't rename method to resolve collision */
        @Override // android.os.Parcelable.Creator
        public Account createFromParcel(Parcel source) {
            return new Account(source);
        }

        /* JADX WARN: Can't rename method to resolve collision */
        @Override // android.os.Parcelable.Creator
        public Account[] newArray(int size) {
            return new Account[size];
        }
    };

    public boolean equals(Object o) {
        if (o == this) {
            return true;
        }
        if (o instanceof Account) {
            Account other = (Account) o;
            return this.name.equals(other.name) && this.type.equals(other.type);
        }
        return false;
    }

    public int hashCode() {
        int result = (17 * 31) + this.name.hashCode();
        return (result * 31) + this.type.hashCode();
    }

    public Account(String name, String type) {
        this(name, type, null);
    }

    public Account(Account other, String accessId) {
        this(other.name, other.type, accessId);
    }

    public Account(String name, String type, String accessId) {
        if (TextUtils.isEmpty(name)) {
            throw new IllegalArgumentException("the name must not be empty: " + name);
        } else if (TextUtils.isEmpty(type)) {
            throw new IllegalArgumentException("the type must not be empty: " + type);
        } else {
            this.name = name;
            this.type = type;
            this.accessId = accessId;
        }
    }

    public Account(Parcel in) {
        this.name = in.readString();
        this.type = in.readString();
        if (TextUtils.isEmpty(this.name)) {
            throw new BadParcelableException("the name must not be empty: " + this.name);
        } else if (TextUtils.isEmpty(this.type)) {
            throw new BadParcelableException("the type must not be empty: " + this.type);
        } else {
            this.accessId = in.readString();
            if (this.accessId != null) {
                synchronized (sAccessedAccounts) {
                    if (sAccessedAccounts.add(this)) {
                        try {
                            IAccountManager accountManager = IAccountManager.Stub.asInterface(ServiceManager.getService("account"));
                            accountManager.onAccountAccessed(this.accessId);
                        } catch (RemoteException e) {
                            Log.e(TAG, "Error noting account access", e);
                        }
                    }
                }
            }
        }
    }

    public String getAccessId() {
        return this.accessId;
    }

    @Override // android.os.Parcelable
    public int describeContents() {
        return 0;
    }

    @Override // android.os.Parcelable
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(this.name);
        dest.writeString(this.type);
        dest.writeString(this.accessId);
    }

    public String toString() {
        return "Account {name=" + this.name + ", type=" + this.type + "}";
    }

    public String toSafeString() {
        if (this.mSafeName == null) {
            this.mSafeName = toSafeName(this.name, EpicenterTranslateClipReveal.StateProperty.TARGET_X);
        }
        return "Account {name=" + this.mSafeName + ", type=" + this.type + "}";
    }

    public static String toSafeName(String name, char replacement) {
        StringBuilder builder = new StringBuilder(64);
        int len = name.length();
        for (int i = 0; i < len; i++) {
            char c = name.charAt(i);
            if (Character.isLetterOrDigit(c)) {
                builder.append(replacement);
            } else {
                builder.append(c);
            }
        }
        return builder.toString();
    }
}
