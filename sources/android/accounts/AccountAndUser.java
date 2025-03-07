package android.accounts;

import android.annotation.UnsupportedAppUsage;

/* loaded from: classes.dex */
public class AccountAndUser {
    @UnsupportedAppUsage
    public Account account;
    @UnsupportedAppUsage
    public int userId;

    @UnsupportedAppUsage
    public AccountAndUser(Account account, int userId) {
        this.account = account;
        this.userId = userId;
    }

    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o instanceof AccountAndUser) {
            AccountAndUser other = (AccountAndUser) o;
            return this.account.equals(other.account) && this.userId == other.userId;
        }
        return false;
    }

    public int hashCode() {
        return this.account.hashCode() + this.userId;
    }

    public String toString() {
        return this.account.toString() + " u" + this.userId;
    }
}
