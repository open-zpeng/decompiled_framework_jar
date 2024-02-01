package com.android.internal.telephony;

import android.annotation.UnsupportedAppUsage;
import android.provider.Telephony;
import android.telephony.SmsMessage;
import com.android.internal.telephony.GsmAlphabet;
import com.android.internal.telephony.SmsConstants;
import java.util.Arrays;

/* loaded from: classes3.dex */
public abstract class SmsMessageBase {
    protected String mEmailBody;
    protected String mEmailFrom;
    protected boolean mIsEmail;
    @UnsupportedAppUsage
    protected boolean mIsMwi;
    @UnsupportedAppUsage
    protected String mMessageBody;
    @UnsupportedAppUsage
    public int mMessageRef;
    @UnsupportedAppUsage
    protected boolean mMwiDontStore;
    @UnsupportedAppUsage
    protected boolean mMwiSense;
    @UnsupportedAppUsage
    protected SmsAddress mOriginatingAddress;
    @UnsupportedAppUsage
    protected byte[] mPdu;
    protected String mPseudoSubject;
    protected SmsAddress mRecipientAddress;
    @UnsupportedAppUsage
    protected String mScAddress;
    protected long mScTimeMillis;
    protected byte[] mUserData;
    @UnsupportedAppUsage
    protected SmsHeader mUserDataHeader;
    protected int mStatusOnIcc = -1;
    protected int mIndexOnIcc = -1;

    public abstract SmsConstants.MessageClass getMessageClass();

    @UnsupportedAppUsage(maxTargetSdk = 28, trackingBug = 115609023)
    public abstract int getProtocolIdentifier();

    @UnsupportedAppUsage(maxTargetSdk = 28, trackingBug = 115609023)
    public abstract int getStatus();

    public abstract boolean isCphsMwiMessage();

    public abstract boolean isMWIClearMessage();

    public abstract boolean isMWISetMessage();

    public abstract boolean isMwiDontStore();

    @UnsupportedAppUsage(maxTargetSdk = 28, trackingBug = 115609023)
    public abstract boolean isReplace();

    @UnsupportedAppUsage(maxTargetSdk = 28, trackingBug = 115609023)
    public abstract boolean isReplyPathPresent();

    @UnsupportedAppUsage
    public abstract boolean isStatusReportMessage();

    /* loaded from: classes3.dex */
    public static abstract class SubmitPduBase {
        @UnsupportedAppUsage
        public byte[] encodedMessage;
        @UnsupportedAppUsage
        public byte[] encodedScAddress;

        public String toString() {
            return "SubmitPdu: encodedScAddress = " + Arrays.toString(this.encodedScAddress) + ", encodedMessage = " + Arrays.toString(this.encodedMessage);
        }
    }

    @UnsupportedAppUsage(maxTargetSdk = 28, trackingBug = 115609023)
    public String getServiceCenterAddress() {
        return this.mScAddress;
    }

    @UnsupportedAppUsage
    public String getOriginatingAddress() {
        SmsAddress smsAddress = this.mOriginatingAddress;
        if (smsAddress == null) {
            return null;
        }
        return smsAddress.getAddressString();
    }

    @UnsupportedAppUsage
    public String getDisplayOriginatingAddress() {
        if (this.mIsEmail) {
            return this.mEmailFrom;
        }
        return getOriginatingAddress();
    }

    @UnsupportedAppUsage
    public String getMessageBody() {
        return this.mMessageBody;
    }

    @UnsupportedAppUsage
    public String getDisplayMessageBody() {
        if (this.mIsEmail) {
            return this.mEmailBody;
        }
        return getMessageBody();
    }

    @UnsupportedAppUsage
    public String getPseudoSubject() {
        String str = this.mPseudoSubject;
        return str == null ? "" : str;
    }

    @UnsupportedAppUsage
    public long getTimestampMillis() {
        return this.mScTimeMillis;
    }

    public boolean isEmail() {
        return this.mIsEmail;
    }

    public String getEmailBody() {
        return this.mEmailBody;
    }

    public String getEmailFrom() {
        return this.mEmailFrom;
    }

    @UnsupportedAppUsage
    public byte[] getUserData() {
        return this.mUserData;
    }

    @UnsupportedAppUsage
    public SmsHeader getUserDataHeader() {
        return this.mUserDataHeader;
    }

    public byte[] getPdu() {
        return this.mPdu;
    }

    public int getStatusOnIcc() {
        return this.mStatusOnIcc;
    }

    public int getIndexOnIcc() {
        return this.mIndexOnIcc;
    }

    /* JADX INFO: Access modifiers changed from: protected */
    public void parseMessageBody() {
        SmsAddress smsAddress = this.mOriginatingAddress;
        if (smsAddress != null && smsAddress.couldBeEmailGateway()) {
            extractEmailAddressFromMessageBody();
        }
    }

    protected void extractEmailAddressFromMessageBody() {
        String[] parts = this.mMessageBody.split("( /)|( )", 2);
        if (parts.length < 2) {
            return;
        }
        this.mEmailFrom = parts[0];
        this.mEmailBody = parts[1];
        this.mIsEmail = Telephony.Mms.isEmailAddress(this.mEmailFrom);
    }

    /* JADX WARN: Code restructure failed: missing block: B:25:?, code lost:
        return r2;
     */
    /*
        Code decompiled incorrectly, please refer to instructions dump.
        To view partially-correct code enable 'Show inconsistent code' option in preferences
    */
    public static int findNextUnicodePosition(int r4, int r5, java.lang.CharSequence r6) {
        /*
            int r0 = r5 / 2
            int r0 = r0 + r4
            int r1 = r6.length()
            int r0 = java.lang.Math.min(r0, r1)
            int r1 = r6.length()
            if (r0 >= r1) goto L55
            java.text.BreakIterator r1 = java.text.BreakIterator.getCharacterInstance()
            java.lang.String r2 = r6.toString()
            r1.setText(r2)
            boolean r2 = r1.isBoundary(r0)
            if (r2 != 0) goto L55
            int r2 = r1.preceding(r0)
        L26:
            int r3 = r2 + 4
            if (r3 > r0) goto L43
            int r3 = java.lang.Character.codePointAt(r6, r2)
            boolean r3 = android.text.Emoji.isRegionalIndicatorSymbol(r3)
            if (r3 == 0) goto L43
            int r3 = r2 + 2
            int r3 = java.lang.Character.codePointAt(r6, r3)
            boolean r3 = android.text.Emoji.isRegionalIndicatorSymbol(r3)
            if (r3 == 0) goto L43
            int r2 = r2 + 4
            goto L26
        L43:
            if (r2 <= r4) goto L47
            r0 = r2
            goto L55
        L47:
            int r3 = r0 + (-1)
            char r3 = r6.charAt(r3)
            boolean r3 = java.lang.Character.isHighSurrogate(r3)
            if (r3 == 0) goto L55
            int r0 = r0 + (-1)
        L55:
            return r0
        */
        throw new UnsupportedOperationException("Method not decompiled: com.android.internal.telephony.SmsMessageBase.findNextUnicodePosition(int, int, java.lang.CharSequence):int");
    }

    public static GsmAlphabet.TextEncodingDetails calcUnicodeEncodingDetails(CharSequence msgBody) {
        GsmAlphabet.TextEncodingDetails ted = new GsmAlphabet.TextEncodingDetails();
        int octets = msgBody.length() * 2;
        ted.codeUnitSize = 3;
        ted.codeUnitCount = msgBody.length();
        if (octets > 140) {
            int maxUserDataBytesWithHeader = 134;
            if (!SmsMessage.hasEmsSupport() && octets <= (134 - 2) * 9) {
                maxUserDataBytesWithHeader = 134 - 2;
            }
            int pos = 0;
            int msgCount = 0;
            while (pos < msgBody.length()) {
                int nextPos = findNextUnicodePosition(pos, maxUserDataBytesWithHeader, msgBody);
                if (nextPos == msgBody.length()) {
                    ted.codeUnitsRemaining = ((maxUserDataBytesWithHeader / 2) + pos) - msgBody.length();
                }
                pos = nextPos;
                msgCount++;
            }
            ted.msgCount = msgCount;
        } else {
            ted.msgCount = 1;
            ted.codeUnitsRemaining = (140 - octets) / 2;
        }
        return ted;
    }

    public String getRecipientAddress() {
        SmsAddress smsAddress = this.mRecipientAddress;
        if (smsAddress == null) {
            return null;
        }
        return smsAddress.getAddressString();
    }
}
