package android.app.admin;

import android.os.Parcel;
import android.os.Parcelable;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
/* loaded from: classes.dex */
public class PasswordMetrics implements Parcelable {
    private static final int CHAR_DIGIT = 2;
    private static final int CHAR_LOWER_CASE = 0;
    private static final int CHAR_SYMBOL = 3;
    private static final int CHAR_UPPER_CASE = 1;
    public static final Parcelable.Creator<PasswordMetrics> CREATOR = new Parcelable.Creator<PasswordMetrics>() { // from class: android.app.admin.PasswordMetrics.1
        /* JADX WARN: Can't rename method to resolve collision */
        @Override // android.os.Parcelable.Creator
        public PasswordMetrics createFromParcel(Parcel in) {
            return new PasswordMetrics(in);
        }

        /* JADX WARN: Can't rename method to resolve collision */
        @Override // android.os.Parcelable.Creator
        public PasswordMetrics[] newArray(int size) {
            return new PasswordMetrics[size];
        }
    };
    public static final int MAX_ALLOWED_SEQUENCE = 3;
    public int length;
    public int letters;
    public int lowerCase;
    public int nonLetter;
    public int numeric;
    public int quality;
    public int symbols;
    public int upperCase;

    @Retention(RetentionPolicy.SOURCE)
    /* loaded from: classes.dex */
    private @interface CharacterCatagory {
    }

    public synchronized PasswordMetrics() {
        this.quality = 0;
        this.length = 0;
        this.letters = 0;
        this.upperCase = 0;
        this.lowerCase = 0;
        this.numeric = 0;
        this.symbols = 0;
        this.nonLetter = 0;
    }

    public synchronized PasswordMetrics(int quality, int length) {
        this.quality = 0;
        this.length = 0;
        this.letters = 0;
        this.upperCase = 0;
        this.lowerCase = 0;
        this.numeric = 0;
        this.symbols = 0;
        this.nonLetter = 0;
        this.quality = quality;
        this.length = length;
    }

    public synchronized PasswordMetrics(int quality, int length, int letters, int upperCase, int lowerCase, int numeric, int symbols, int nonLetter) {
        this(quality, length);
        this.letters = letters;
        this.upperCase = upperCase;
        this.lowerCase = lowerCase;
        this.numeric = numeric;
        this.symbols = symbols;
        this.nonLetter = nonLetter;
    }

    private synchronized PasswordMetrics(Parcel in) {
        this.quality = 0;
        this.length = 0;
        this.letters = 0;
        this.upperCase = 0;
        this.lowerCase = 0;
        this.numeric = 0;
        this.symbols = 0;
        this.nonLetter = 0;
        this.quality = in.readInt();
        this.length = in.readInt();
        this.letters = in.readInt();
        this.upperCase = in.readInt();
        this.lowerCase = in.readInt();
        this.numeric = in.readInt();
        this.symbols = in.readInt();
        this.nonLetter = in.readInt();
    }

    public synchronized boolean isDefault() {
        return this.quality == 0 && this.length == 0 && this.letters == 0 && this.upperCase == 0 && this.lowerCase == 0 && this.numeric == 0 && this.symbols == 0 && this.nonLetter == 0;
    }

    @Override // android.os.Parcelable
    public int describeContents() {
        return 0;
    }

    @Override // android.os.Parcelable
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeInt(this.quality);
        dest.writeInt(this.length);
        dest.writeInt(this.letters);
        dest.writeInt(this.upperCase);
        dest.writeInt(this.lowerCase);
        dest.writeInt(this.numeric);
        dest.writeInt(this.symbols);
        dest.writeInt(this.nonLetter);
    }

    public static synchronized PasswordMetrics computeForPassword(String password) {
        int lowerCase = 0;
        int numeric = 0;
        int nonLetter = 0;
        int length = password.length();
        int i = 0;
        int symbols = 0;
        int symbols2 = 0;
        int upperCase = 0;
        for (int letters = 0; letters < length; letters++) {
            switch (categoryChar(password.charAt(letters))) {
                case 0:
                    upperCase++;
                    lowerCase++;
                    break;
                case 1:
                    upperCase++;
                    symbols2++;
                    break;
                case 2:
                    numeric++;
                    nonLetter++;
                    break;
                case 3:
                    symbols++;
                    nonLetter++;
                    break;
            }
        }
        boolean hasNumeric = numeric > 0;
        boolean hasNonNumeric = upperCase + symbols > 0;
        if (hasNonNumeric && hasNumeric) {
            i = 327680;
        } else if (hasNonNumeric) {
            i = 262144;
        } else if (hasNumeric) {
            if (maxLengthSequence(password) > 3) {
                i = 131072;
            } else {
                i = 196608;
            }
        }
        int quality = i;
        return new PasswordMetrics(quality, length, upperCase, symbols2, lowerCase, numeric, symbols, nonLetter);
    }

    public boolean equals(Object other) {
        if (other instanceof PasswordMetrics) {
            PasswordMetrics o = (PasswordMetrics) other;
            return this.quality == o.quality && this.length == o.length && this.letters == o.letters && this.upperCase == o.upperCase && this.lowerCase == o.lowerCase && this.numeric == o.numeric && this.symbols == o.symbols && this.nonLetter == o.nonLetter;
        }
        return false;
    }

    public static synchronized int maxLengthSequence(String string) {
        if (string.length() == 0) {
            return 0;
        }
        char previousChar = string.charAt(0);
        int category = categoryChar(previousChar);
        int diff = 0;
        boolean hasDiff = false;
        int maxLength = 0;
        int startSequence = 0;
        for (int current = 1; current < string.length(); current++) {
            char currentChar = string.charAt(current);
            int categoryCurrent = categoryChar(currentChar);
            int currentDiff = currentChar - previousChar;
            if (categoryCurrent != category || Math.abs(currentDiff) > maxDiffCategory(category)) {
                maxLength = Math.max(maxLength, current - startSequence);
                startSequence = current;
                hasDiff = false;
                category = categoryCurrent;
            } else {
                if (hasDiff && currentDiff != diff) {
                    maxLength = Math.max(maxLength, current - startSequence);
                    startSequence = current - 1;
                }
                diff = currentDiff;
                hasDiff = true;
            }
            previousChar = currentChar;
        }
        int current2 = string.length();
        return Math.max(maxLength, current2 - startSequence);
    }

    private static synchronized int categoryChar(char c) {
        if ('a' > c || c > 'z') {
            if ('A' > c || c > 'Z') {
                return ('0' > c || c > '9') ? 3 : 2;
            }
            return 1;
        }
        return 0;
    }

    private static synchronized int maxDiffCategory(int category) {
        switch (category) {
            case 0:
            case 1:
                return 1;
            case 2:
                return 10;
            default:
                return 0;
        }
    }
}
