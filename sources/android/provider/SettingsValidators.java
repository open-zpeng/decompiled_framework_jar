package android.provider;

import android.content.ComponentName;
import android.net.Uri;
import android.net.wifi.WifiEnterpriseConfig;
import android.text.TextUtils;
import com.android.internal.util.ArrayUtils;
import java.util.Locale;
import org.json.JSONException;
import org.json.JSONObject;

/* loaded from: classes2.dex */
public class SettingsValidators {
    public static final Validator BOOLEAN_VALIDATOR = new DiscreteValueValidator(new String[]{WifiEnterpriseConfig.ENGINE_DISABLE, WifiEnterpriseConfig.ENGINE_ENABLE});
    public static final Validator ANY_STRING_VALIDATOR = new Validator() { // from class: android.provider.SettingsValidators.1
        @Override // android.provider.SettingsValidators.Validator
        public boolean validate(String value) {
            return true;
        }
    };
    public static final Validator NON_NEGATIVE_INTEGER_VALIDATOR = new Validator() { // from class: android.provider.SettingsValidators.2
        @Override // android.provider.SettingsValidators.Validator
        public boolean validate(String value) {
            try {
                return Integer.parseInt(value) >= 0;
            } catch (NumberFormatException e) {
                return false;
            }
        }
    };
    public static final Validator ANY_INTEGER_VALIDATOR = new Validator() { // from class: android.provider.SettingsValidators.3
        @Override // android.provider.SettingsValidators.Validator
        public boolean validate(String value) {
            try {
                Integer.parseInt(value);
                return true;
            } catch (NumberFormatException e) {
                return false;
            }
        }
    };
    public static final Validator URI_VALIDATOR = new Validator() { // from class: android.provider.SettingsValidators.4
        @Override // android.provider.SettingsValidators.Validator
        public boolean validate(String value) {
            try {
                Uri.decode(value);
                return true;
            } catch (IllegalArgumentException e) {
                return false;
            }
        }
    };
    public static final Validator COMPONENT_NAME_VALIDATOR = new Validator() { // from class: android.provider.SettingsValidators.5
        @Override // android.provider.SettingsValidators.Validator
        public boolean validate(String value) {
            return (value == null || ComponentName.unflattenFromString(value) == null) ? false : true;
        }
    };
    public static final Validator NULLABLE_COMPONENT_NAME_VALIDATOR = new Validator() { // from class: android.provider.SettingsValidators.6
        @Override // android.provider.SettingsValidators.Validator
        public boolean validate(String value) {
            return value == null || SettingsValidators.COMPONENT_NAME_VALIDATOR.validate(value);
        }
    };
    public static final Validator PACKAGE_NAME_VALIDATOR = new Validator() { // from class: android.provider.SettingsValidators.7
        @Override // android.provider.SettingsValidators.Validator
        public boolean validate(String value) {
            return value != null && isStringPackageName(value);
        }

        private boolean isStringPackageName(String value) {
            if (value == null) {
                return false;
            }
            String[] subparts = value.split("\\.");
            boolean isValidPackageName = true;
            for (String subpart : subparts) {
                isValidPackageName &= isSubpartValidForPackageName(subpart);
                if (!isValidPackageName) {
                    break;
                }
            }
            return isValidPackageName;
        }

        private boolean isSubpartValidForPackageName(String subpart) {
            if (subpart.length() == 0) {
                return false;
            }
            boolean isValidSubpart = Character.isLetter(subpart.charAt(0));
            for (int i = 1; i < subpart.length(); i++) {
                isValidSubpart &= Character.isLetterOrDigit(subpart.charAt(i)) || subpart.charAt(i) == '_';
                if (!isValidSubpart) {
                    break;
                }
            }
            return isValidSubpart;
        }
    };
    public static final Validator LENIENT_IP_ADDRESS_VALIDATOR = new Validator() { // from class: android.provider.SettingsValidators.8
        private static final int MAX_IPV6_LENGTH = 45;

        @Override // android.provider.SettingsValidators.Validator
        public boolean validate(String value) {
            return value != null && value.length() <= 45;
        }
    };
    public static final Validator LOCALE_VALIDATOR = new Validator() { // from class: android.provider.SettingsValidators.9
        @Override // android.provider.SettingsValidators.Validator
        public boolean validate(String value) {
            if (value == null) {
                return false;
            }
            Locale[] validLocales = Locale.getAvailableLocales();
            for (Locale locale : validLocales) {
                if (value.equals(locale.toString())) {
                    return true;
                }
            }
            return false;
        }
    };
    public static final Validator JSON_OBJECT_VALIDATOR = new Validator() { // from class: android.provider.-$$Lambda$SettingsValidators$0swA5rhyuVHADD7MEwgs2ihTCGM
        @Override // android.provider.SettingsValidators.Validator
        public final boolean validate(String str) {
            return SettingsValidators.lambda$static$0(str);
        }
    };

    /* loaded from: classes2.dex */
    public interface Validator {
        boolean validate(String str);
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public static /* synthetic */ boolean lambda$static$0(String value) {
        if (TextUtils.isEmpty(value)) {
            return false;
        }
        try {
            new JSONObject(value);
            return true;
        } catch (JSONException e) {
            return false;
        }
    }

    /* loaded from: classes2.dex */
    public static final class DiscreteValueValidator implements Validator {
        private final String[] mValues;

        public DiscreteValueValidator(String[] values) {
            this.mValues = values;
        }

        @Override // android.provider.SettingsValidators.Validator
        public boolean validate(String value) {
            return ArrayUtils.contains(this.mValues, value);
        }
    }

    /* loaded from: classes2.dex */
    public static final class InclusiveIntegerRangeValidator implements Validator {
        private final int mMax;
        private final int mMin;

        public InclusiveIntegerRangeValidator(int min, int max) {
            this.mMin = min;
            this.mMax = max;
        }

        @Override // android.provider.SettingsValidators.Validator
        public boolean validate(String value) {
            try {
                int intValue = Integer.parseInt(value);
                if (intValue >= this.mMin) {
                    return intValue <= this.mMax;
                }
                return false;
            } catch (NumberFormatException e) {
                return false;
            }
        }
    }

    /* loaded from: classes2.dex */
    public static final class InclusiveFloatRangeValidator implements Validator {
        private final float mMax;
        private final float mMin;

        public InclusiveFloatRangeValidator(float min, float max) {
            this.mMin = min;
            this.mMax = max;
        }

        @Override // android.provider.SettingsValidators.Validator
        public boolean validate(String value) {
            try {
                float floatValue = Float.parseFloat(value);
                if (floatValue >= this.mMin) {
                    return floatValue <= this.mMax;
                }
                return false;
            } catch (NullPointerException | NumberFormatException e) {
                return false;
            }
        }
    }

    /* loaded from: classes2.dex */
    public static final class ComponentNameListValidator implements Validator {
        private final String mSeparator;

        public ComponentNameListValidator(String separator) {
            this.mSeparator = separator;
        }

        @Override // android.provider.SettingsValidators.Validator
        public boolean validate(String value) {
            if (value == null) {
                return false;
            }
            String[] elements = value.split(this.mSeparator);
            for (String element : elements) {
                if (!SettingsValidators.COMPONENT_NAME_VALIDATOR.validate(element)) {
                    return false;
                }
            }
            return true;
        }
    }

    /* loaded from: classes2.dex */
    public static final class PackageNameListValidator implements Validator {
        private final String mSeparator;

        public PackageNameListValidator(String separator) {
            this.mSeparator = separator;
        }

        @Override // android.provider.SettingsValidators.Validator
        public boolean validate(String value) {
            if (value == null) {
                return false;
            }
            String[] elements = value.split(this.mSeparator);
            for (String element : elements) {
                if (!SettingsValidators.PACKAGE_NAME_VALIDATOR.validate(element)) {
                    return false;
                }
            }
            return true;
        }
    }
}
