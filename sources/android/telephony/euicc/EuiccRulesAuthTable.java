package android.telephony.euicc;

import android.annotation.SystemApi;
import android.os.Parcel;
import android.os.Parcelable;
import android.service.carrier.CarrierIdentifier;
import android.text.TextUtils;
import com.android.internal.annotations.VisibleForTesting;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.util.List;

@SystemApi
/* loaded from: classes2.dex */
public final class EuiccRulesAuthTable implements Parcelable {
    public static final Parcelable.Creator<EuiccRulesAuthTable> CREATOR = new Parcelable.Creator<EuiccRulesAuthTable>() { // from class: android.telephony.euicc.EuiccRulesAuthTable.1
        /* JADX WARN: Can't rename method to resolve collision */
        @Override // android.os.Parcelable.Creator
        public EuiccRulesAuthTable createFromParcel(Parcel source) {
            return new EuiccRulesAuthTable(source);
        }

        /* JADX WARN: Can't rename method to resolve collision */
        @Override // android.os.Parcelable.Creator
        public EuiccRulesAuthTable[] newArray(int size) {
            return new EuiccRulesAuthTable[size];
        }
    };
    public static final int POLICY_RULE_FLAG_CONSENT_REQUIRED = 1;
    private final CarrierIdentifier[][] mCarrierIds;
    private final int[] mPolicyRuleFlags;
    private final int[] mPolicyRules;

    @Retention(RetentionPolicy.SOURCE)
    /* loaded from: classes2.dex */
    public @interface PolicyRuleFlag {
    }

    /* loaded from: classes2.dex */
    public static final class Builder {
        private CarrierIdentifier[][] mCarrierIds;
        private int[] mPolicyRuleFlags;
        private int[] mPolicyRules;
        private int mPosition;

        public Builder(int ruleNum) {
            this.mPolicyRules = new int[ruleNum];
            this.mCarrierIds = new CarrierIdentifier[ruleNum];
            this.mPolicyRuleFlags = new int[ruleNum];
        }

        public EuiccRulesAuthTable build() {
            int i = this.mPosition;
            int[] iArr = this.mPolicyRules;
            if (i != iArr.length) {
                throw new IllegalStateException("Not enough rules are added, expected: " + this.mPolicyRules.length + ", added: " + this.mPosition);
            }
            return new EuiccRulesAuthTable(iArr, this.mCarrierIds, this.mPolicyRuleFlags);
        }

        public Builder add(int policyRules, List<CarrierIdentifier> carrierId, int policyRuleFlags) {
            int i = this.mPosition;
            int[] iArr = this.mPolicyRules;
            if (i >= iArr.length) {
                throw new ArrayIndexOutOfBoundsException(i);
            }
            iArr[i] = policyRules;
            if (carrierId != null && carrierId.size() > 0) {
                this.mCarrierIds[this.mPosition] = (CarrierIdentifier[]) carrierId.toArray(new CarrierIdentifier[carrierId.size()]);
            }
            int[] iArr2 = this.mPolicyRuleFlags;
            int i2 = this.mPosition;
            iArr2[i2] = policyRuleFlags;
            this.mPosition = i2 + 1;
            return this;
        }
    }

    @VisibleForTesting
    public static boolean match(String mccRule, String mcc) {
        if (mccRule.length() < mcc.length()) {
            return false;
        }
        for (int i = 0; i < mccRule.length(); i++) {
            if (mccRule.charAt(i) != 'E' && (i >= mcc.length() || mccRule.charAt(i) != mcc.charAt(i))) {
                return false;
            }
        }
        return true;
    }

    private EuiccRulesAuthTable(int[] policyRules, CarrierIdentifier[][] carrierIds, int[] policyRuleFlags) {
        this.mPolicyRules = policyRules;
        this.mCarrierIds = carrierIds;
        this.mPolicyRuleFlags = policyRuleFlags;
    }

    public int findIndex(int policy, CarrierIdentifier carrierId) {
        CarrierIdentifier[] carrierIds;
        int i = 0;
        loop0: while (true) {
            int[] iArr = this.mPolicyRules;
            if (i < iArr.length) {
                if ((iArr[i] & policy) != 0 && (carrierIds = this.mCarrierIds[i]) != null && carrierIds.length != 0) {
                    for (CarrierIdentifier ruleCarrierId : carrierIds) {
                        if (match(ruleCarrierId.getMcc(), carrierId.getMcc()) && match(ruleCarrierId.getMnc(), carrierId.getMnc())) {
                            String gid = ruleCarrierId.getGid1();
                            if (TextUtils.isEmpty(gid) || gid.equals(carrierId.getGid1())) {
                                String gid2 = ruleCarrierId.getGid2();
                                if (TextUtils.isEmpty(gid2) || gid2.equals(carrierId.getGid2())) {
                                    break loop0;
                                }
                            }
                        }
                    }
                    continue;
                }
                i++;
            } else {
                return -1;
            }
        }
        return i;
    }

    public boolean hasPolicyRuleFlag(int index, int flag) {
        if (index < 0 || index >= this.mPolicyRules.length) {
            throw new ArrayIndexOutOfBoundsException(index);
        }
        return (this.mPolicyRuleFlags[index] & flag) != 0;
    }

    @Override // android.os.Parcelable
    public int describeContents() {
        return 0;
    }

    @Override // android.os.Parcelable
    public void writeToParcel(Parcel dest, int flags) {
        CarrierIdentifier[][] carrierIdentifierArr;
        dest.writeIntArray(this.mPolicyRules);
        for (CarrierIdentifier[] ids : this.mCarrierIds) {
            dest.writeTypedArray(ids, flags);
        }
        dest.writeIntArray(this.mPolicyRuleFlags);
    }

    /* JADX WARN: Code restructure failed: missing block: B:33:0x004e, code lost:
        return false;
     */
    /*
        Code decompiled incorrectly, please refer to instructions dump.
        To view partially-correct code enable 'Show inconsistent code' option in preferences
    */
    public boolean equals(java.lang.Object r10) {
        /*
            r9 = this;
            r0 = 1
            if (r9 != r10) goto L4
            return r0
        L4:
            r1 = 0
            if (r10 == 0) goto L66
            java.lang.Class r2 = r9.getClass()
            java.lang.Class r3 = r10.getClass()
            if (r2 == r3) goto L12
            goto L66
        L12:
            r2 = r10
            android.telephony.euicc.EuiccRulesAuthTable r2 = (android.telephony.euicc.EuiccRulesAuthTable) r2
            android.service.carrier.CarrierIdentifier[][] r3 = r9.mCarrierIds
            int r3 = r3.length
            android.service.carrier.CarrierIdentifier[][] r4 = r2.mCarrierIds
            int r4 = r4.length
            if (r3 == r4) goto L1e
            return r1
        L1e:
            r3 = 0
        L1f:
            android.service.carrier.CarrierIdentifier[][] r4 = r9.mCarrierIds
            int r5 = r4.length
            if (r3 >= r5) goto L4f
            r4 = r4[r3]
            android.service.carrier.CarrierIdentifier[][] r5 = r2.mCarrierIds
            r5 = r5[r3]
            if (r4 == 0) goto L46
            if (r5 == 0) goto L46
            int r6 = r4.length
            int r7 = r5.length
            if (r6 == r7) goto L33
            return r1
        L33:
            r6 = 0
        L34:
            int r7 = r4.length
            if (r6 >= r7) goto L45
            r7 = r4[r6]
            r8 = r5[r6]
            boolean r7 = r7.equals(r8)
            if (r7 != 0) goto L42
            return r1
        L42:
            int r6 = r6 + 1
            goto L34
        L45:
            goto L4b
        L46:
            if (r4 != 0) goto L4e
            if (r5 != 0) goto L4e
        L4b:
            int r3 = r3 + 1
            goto L1f
        L4e:
            return r1
        L4f:
            int[] r3 = r9.mPolicyRules
            int[] r4 = r2.mPolicyRules
            boolean r3 = java.util.Arrays.equals(r3, r4)
            if (r3 == 0) goto L64
            int[] r3 = r9.mPolicyRuleFlags
            int[] r4 = r2.mPolicyRuleFlags
            boolean r3 = java.util.Arrays.equals(r3, r4)
            if (r3 == 0) goto L64
            goto L65
        L64:
            r0 = r1
        L65:
            return r0
        L66:
            return r1
        */
        throw new UnsupportedOperationException("Method not decompiled: android.telephony.euicc.EuiccRulesAuthTable.equals(java.lang.Object):boolean");
    }

    private EuiccRulesAuthTable(Parcel source) {
        this.mPolicyRules = source.createIntArray();
        int len = this.mPolicyRules.length;
        this.mCarrierIds = new CarrierIdentifier[len];
        for (int i = 0; i < len; i++) {
            this.mCarrierIds[i] = (CarrierIdentifier[]) source.createTypedArray(CarrierIdentifier.CREATOR);
        }
        this.mPolicyRuleFlags = source.createIntArray();
    }
}
