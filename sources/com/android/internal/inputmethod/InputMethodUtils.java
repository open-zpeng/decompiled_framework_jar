package com.android.internal.inputmethod;

import android.app.AppOpsManager;
import android.content.ContentResolver;
import android.content.Context;
import android.content.pm.ApplicationInfo;
import android.content.pm.IPackageManager;
import android.content.res.Resources;
import android.os.LocaleList;
import android.os.RemoteException;
import android.provider.Settings;
import android.text.TextUtils;
import android.util.ArrayMap;
import android.util.ArraySet;
import android.util.Pair;
import android.util.Printer;
import android.util.Slog;
import android.view.inputmethod.InputMethodInfo;
import android.view.inputmethod.InputMethodSubtype;
import android.view.textservice.SpellCheckerInfo;
import android.view.textservice.TextServicesManager;
import com.android.internal.R;
import com.android.internal.annotations.GuardedBy;
import com.android.internal.annotations.VisibleForTesting;
import com.android.internal.inputmethod.LocaleUtils;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.HashMap;
import java.util.Iterator;
import java.util.LinkedHashSet;
import java.util.List;
import java.util.Locale;
import java.util.Map;
/* loaded from: classes3.dex */
public class InputMethodUtils {
    public static final boolean DEBUG = false;
    private static final char INPUT_METHOD_SEPARATOR = ':';
    private static final char INPUT_METHOD_SUBTYPE_SEPARATOR = ';';
    public static final int NOT_A_SUBTYPE_ID = -1;
    public static final String SUBTYPE_MODE_KEYBOARD = "keyboard";
    public static final String SUBTYPE_MODE_VOICE = "voice";
    private static final String TAG = "InputMethodUtils";
    private static final String TAG_ASCII_CAPABLE = "AsciiCapable";
    private static final String TAG_ENABLED_WHEN_DEFAULT_IS_NOT_ASCII_CAPABLE = "EnabledWhenDefaultIsNotAsciiCapable";
    @GuardedBy("sCacheLock")
    private static InputMethodInfo sCachedInputMethodInfo;
    @GuardedBy("sCacheLock")
    private static ArrayList<InputMethodSubtype> sCachedResult;
    @GuardedBy("sCacheLock")
    private static LocaleList sCachedSystemLocales;
    public static final String SUBTYPE_MODE_ANY = null;
    private static final Locale ENGLISH_LOCALE = new Locale("en");
    private static final String NOT_A_SUBTYPE_ID_STR = String.valueOf(-1);
    private static final Locale[] SEARCH_ORDER_OF_FALLBACK_LOCALES = {Locale.ENGLISH, Locale.US, Locale.UK};
    private static final Object sCacheLock = new Object();
    private static final LocaleUtils.LocaleExtractor<InputMethodSubtype> sSubtypeToLocale = new LocaleUtils.LocaleExtractor<InputMethodSubtype>() { // from class: com.android.internal.inputmethod.InputMethodUtils.1
        @Override // com.android.internal.inputmethod.LocaleUtils.LocaleExtractor
        public Locale get(InputMethodSubtype source) {
            if (source != null) {
                return source.getLocaleObject();
            }
            return null;
        }
    };
    private static final Locale LOCALE_EN_US = new Locale("en", "US");
    private static final Locale LOCALE_EN_GB = new Locale("en", "GB");

    private InputMethodUtils() {
    }

    public static String getApiCallStack() {
        String apiCallStack = "";
        try {
            throw new RuntimeException();
        } catch (RuntimeException e) {
            StackTraceElement[] frames = e.getStackTrace();
            for (int j = 1; j < frames.length; j++) {
                String tempCallStack = frames[j].toString();
                if (!TextUtils.isEmpty(apiCallStack) && tempCallStack.indexOf("Transact(") >= 0) {
                    break;
                }
                apiCallStack = tempCallStack;
            }
            return apiCallStack;
        }
    }

    public static boolean isSystemIme(InputMethodInfo inputMethod) {
        return (inputMethod.getServiceInfo().applicationInfo.flags & 1) != 0;
    }

    public static boolean isSystemImeThatHasSubtypeOf(InputMethodInfo imi, Context context, boolean checkDefaultAttribute, Locale requiredLocale, boolean checkCountry, String requiredSubtypeMode) {
        if (isSystemIme(imi)) {
            return (!checkDefaultAttribute || imi.isDefault(context)) && containsSubtypeOf(imi, requiredLocale, checkCountry, requiredSubtypeMode);
        }
        return false;
    }

    /* JADX WARN: Code restructure failed: missing block: B:11:0x0027, code lost:
        r3 = r3 + 1;
     */
    /* JADX WARN: Code restructure failed: missing block: B:21:0x0050, code lost:
        r3 = r3 + 1;
     */
    /*
        Code decompiled incorrectly, please refer to instructions dump.
        To view partially-correct add '--show-bad-code' argument
    */
    public static java.util.Locale getFallbackLocaleForDefaultIme(java.util.ArrayList<android.view.inputmethod.InputMethodInfo> r12, android.content.Context r13) {
        /*
            java.util.Locale[] r0 = com.android.internal.inputmethod.InputMethodUtils.SEARCH_ORDER_OF_FALLBACK_LOCALES
            int r1 = r0.length
            r2 = 0
            r3 = r2
        L5:
            if (r3 >= r1) goto L2a
            r10 = r0[r3]
            r4 = r2
        La:
            r11 = r4
            int r4 = r12.size()
            if (r11 >= r4) goto L27
            java.lang.Object r4 = r12.get(r11)
            android.view.inputmethod.InputMethodInfo r4 = (android.view.inputmethod.InputMethodInfo) r4
            r6 = 1
            r8 = 1
            java.lang.String r9 = "keyboard"
            r5 = r13
            r7 = r10
            boolean r4 = isSystemImeThatHasSubtypeOf(r4, r5, r6, r7, r8, r9)
            if (r4 == 0) goto L24
            return r10
        L24:
            int r4 = r11 + 1
            goto La
        L27:
            int r3 = r3 + 1
            goto L5
        L2a:
            java.util.Locale[] r0 = com.android.internal.inputmethod.InputMethodUtils.SEARCH_ORDER_OF_FALLBACK_LOCALES
            int r1 = r0.length
            r3 = r2
        L2e:
            if (r3 >= r1) goto L53
            r10 = r0[r3]
            r4 = r2
        L33:
            r11 = r4
            int r4 = r12.size()
            if (r11 >= r4) goto L50
            java.lang.Object r4 = r12.get(r11)
            android.view.inputmethod.InputMethodInfo r4 = (android.view.inputmethod.InputMethodInfo) r4
            r6 = 0
            r8 = 1
            java.lang.String r9 = "keyboard"
            r5 = r13
            r7 = r10
            boolean r4 = isSystemImeThatHasSubtypeOf(r4, r5, r6, r7, r8, r9)
            if (r4 == 0) goto L4d
            return r10
        L4d:
            int r4 = r11 + 1
            goto L33
        L50:
            int r3 = r3 + 1
            goto L2e
        L53:
            java.lang.String r0 = "InputMethodUtils"
            java.lang.StringBuilder r1 = new java.lang.StringBuilder
            r1.<init>()
            java.lang.String r2 = "Found no fallback locale. imis="
            r1.append(r2)
            java.lang.Object[] r2 = r12.toArray()
            java.lang.String r2 = java.util.Arrays.toString(r2)
            r1.append(r2)
            java.lang.String r1 = r1.toString()
            android.util.Slog.w(r0, r1)
            r0 = 0
            return r0
        */
        throw new UnsupportedOperationException("Method not decompiled: com.android.internal.inputmethod.InputMethodUtils.getFallbackLocaleForDefaultIme(java.util.ArrayList, android.content.Context):java.util.Locale");
    }

    /* JADX INFO: Access modifiers changed from: private */
    public static boolean isSystemAuxilialyImeThatHasAutomaticSubtype(InputMethodInfo imi, Context context, boolean checkDefaultAttribute) {
        if (isSystemIme(imi)) {
            if ((!checkDefaultAttribute || imi.isDefault(context)) && imi.isAuxiliaryIme()) {
                int subtypeCount = imi.getSubtypeCount();
                for (int i = 0; i < subtypeCount; i++) {
                    InputMethodSubtype s = imi.getSubtypeAt(i);
                    if (s.overridesImplicitlyEnabledSubtype()) {
                        return true;
                    }
                }
                return false;
            }
            return false;
        }
        return false;
    }

    public static Locale getSystemLocaleFromContext(Context context) {
        try {
            return context.getResources().getConfiguration().locale;
        } catch (Resources.NotFoundException e) {
            return null;
        }
    }

    /* JADX INFO: Access modifiers changed from: private */
    /* loaded from: classes3.dex */
    public static final class InputMethodListBuilder {
        private final LinkedHashSet<InputMethodInfo> mInputMethodSet;

        private InputMethodListBuilder() {
            this.mInputMethodSet = new LinkedHashSet<>();
        }

        public InputMethodListBuilder fillImes(ArrayList<InputMethodInfo> imis, Context context, boolean checkDefaultAttribute, Locale locale, boolean checkCountry, String requiredSubtypeMode) {
            for (int i = 0; i < imis.size(); i++) {
                InputMethodInfo imi = imis.get(i);
                if (InputMethodUtils.isSystemImeThatHasSubtypeOf(imi, context, checkDefaultAttribute, locale, checkCountry, requiredSubtypeMode)) {
                    this.mInputMethodSet.add(imi);
                }
            }
            return this;
        }

        public InputMethodListBuilder fillAuxiliaryImes(ArrayList<InputMethodInfo> imis, Context context) {
            Iterator<InputMethodInfo> it = this.mInputMethodSet.iterator();
            while (it.hasNext()) {
                if (it.next().isAuxiliaryIme()) {
                    return this;
                }
            }
            boolean added = false;
            for (int i = 0; i < imis.size(); i++) {
                InputMethodInfo imi = imis.get(i);
                if (InputMethodUtils.isSystemAuxilialyImeThatHasAutomaticSubtype(imi, context, true)) {
                    this.mInputMethodSet.add(imi);
                    added = true;
                }
            }
            if (added) {
                return this;
            }
            for (int i2 = 0; i2 < imis.size(); i2++) {
                InputMethodInfo imi2 = imis.get(i2);
                if (InputMethodUtils.isSystemAuxilialyImeThatHasAutomaticSubtype(imi2, context, false)) {
                    this.mInputMethodSet.add(imi2);
                }
            }
            return this;
        }

        public boolean isEmpty() {
            return this.mInputMethodSet.isEmpty();
        }

        public ArrayList<InputMethodInfo> build() {
            return new ArrayList<>(this.mInputMethodSet);
        }
    }

    private static InputMethodListBuilder getMinimumKeyboardSetWithSystemLocale(ArrayList<InputMethodInfo> imis, Context context, Locale systemLocale, Locale fallbackLocale) {
        InputMethodListBuilder builder = new InputMethodListBuilder();
        builder.fillImes(imis, context, true, systemLocale, true, SUBTYPE_MODE_KEYBOARD);
        if (!builder.isEmpty()) {
            return builder;
        }
        builder.fillImes(imis, context, true, systemLocale, false, SUBTYPE_MODE_KEYBOARD);
        if (!builder.isEmpty()) {
            return builder;
        }
        builder.fillImes(imis, context, true, fallbackLocale, true, SUBTYPE_MODE_KEYBOARD);
        if (!builder.isEmpty()) {
            return builder;
        }
        builder.fillImes(imis, context, true, fallbackLocale, false, SUBTYPE_MODE_KEYBOARD);
        if (!builder.isEmpty()) {
            return builder;
        }
        builder.fillImes(imis, context, false, fallbackLocale, true, SUBTYPE_MODE_KEYBOARD);
        if (!builder.isEmpty()) {
            return builder;
        }
        builder.fillImes(imis, context, false, fallbackLocale, false, SUBTYPE_MODE_KEYBOARD);
        if (!builder.isEmpty()) {
            return builder;
        }
        Slog.w(TAG, "No software keyboard is found. imis=" + Arrays.toString(imis.toArray()) + " systemLocale=" + systemLocale + " fallbackLocale=" + fallbackLocale);
        return builder;
    }

    public static ArrayList<InputMethodInfo> getDefaultEnabledImes(Context context, ArrayList<InputMethodInfo> imis, boolean onlyMinimum) {
        Locale fallbackLocale = getFallbackLocaleForDefaultIme(imis, context);
        Locale systemLocale = getSystemLocaleFromContext(context);
        InputMethodListBuilder builder = getMinimumKeyboardSetWithSystemLocale(imis, context, systemLocale, fallbackLocale);
        if (!onlyMinimum) {
            builder.fillImes(imis, context, true, systemLocale, true, SUBTYPE_MODE_ANY).fillAuxiliaryImes(imis, context);
        }
        return builder.build();
    }

    public static ArrayList<InputMethodInfo> getDefaultEnabledImes(Context context, ArrayList<InputMethodInfo> imis) {
        return getDefaultEnabledImes(context, imis, false);
    }

    public static Locale constructLocaleFromString(String localeStr) {
        if (TextUtils.isEmpty(localeStr)) {
            return null;
        }
        String[] localeParams = localeStr.split("_", 3);
        if (localeParams.length >= 1 && "tl".equals(localeParams[0])) {
            localeParams[0] = "fil";
        }
        if (localeParams.length == 1) {
            return new Locale(localeParams[0]);
        }
        if (localeParams.length == 2) {
            return new Locale(localeParams[0], localeParams[1]);
        }
        if (localeParams.length == 3) {
            return new Locale(localeParams[0], localeParams[1], localeParams[2]);
        }
        return null;
    }

    public static boolean containsSubtypeOf(InputMethodInfo imi, Locale locale, boolean checkCountry, String mode) {
        if (locale == null) {
            return false;
        }
        int N = imi.getSubtypeCount();
        for (int i = 0; i < N; i++) {
            InputMethodSubtype subtype = imi.getSubtypeAt(i);
            if (checkCountry) {
                Locale subtypeLocale = subtype.getLocaleObject();
                if (subtypeLocale == null) {
                    continue;
                } else if (TextUtils.equals(subtypeLocale.getLanguage(), locale.getLanguage())) {
                    if (!TextUtils.equals(subtypeLocale.getCountry(), locale.getCountry())) {
                        continue;
                    }
                    if (mode != SUBTYPE_MODE_ANY || TextUtils.isEmpty(mode) || mode.equalsIgnoreCase(subtype.getMode())) {
                        return true;
                    }
                } else {
                    continue;
                }
            } else {
                if (!TextUtils.equals(new Locale(getLanguageFromLocaleString(subtype.getLocale())).getLanguage(), locale.getLanguage())) {
                    continue;
                }
                return mode != SUBTYPE_MODE_ANY ? true : true;
            }
        }
        return false;
    }

    public static ArrayList<InputMethodSubtype> getSubtypes(InputMethodInfo imi) {
        ArrayList<InputMethodSubtype> subtypes = new ArrayList<>();
        int subtypeCount = imi.getSubtypeCount();
        for (int i = 0; i < subtypeCount; i++) {
            subtypes.add(imi.getSubtypeAt(i));
        }
        return subtypes;
    }

    public static ArrayList<InputMethodSubtype> getOverridingImplicitlyEnabledSubtypes(InputMethodInfo imi, String mode) {
        ArrayList<InputMethodSubtype> subtypes = new ArrayList<>();
        int subtypeCount = imi.getSubtypeCount();
        for (int i = 0; i < subtypeCount; i++) {
            InputMethodSubtype subtype = imi.getSubtypeAt(i);
            if (subtype.overridesImplicitlyEnabledSubtype() && subtype.getMode().equals(mode)) {
                subtypes.add(subtype);
            }
        }
        return subtypes;
    }

    public static InputMethodInfo getMostApplicableDefaultIME(List<InputMethodInfo> enabledImes) {
        if (enabledImes == null || enabledImes.isEmpty()) {
            return null;
        }
        int i = enabledImes.size();
        int firstFoundSystemIme = -1;
        while (i > 0) {
            i--;
            InputMethodInfo imi = enabledImes.get(i);
            if (!imi.isAuxiliaryIme()) {
                if (isSystemIme(imi) && containsSubtypeOf(imi, ENGLISH_LOCALE, false, SUBTYPE_MODE_KEYBOARD)) {
                    return imi;
                }
                if (firstFoundSystemIme < 0 && isSystemIme(imi)) {
                    firstFoundSystemIme = i;
                }
            }
        }
        return enabledImes.get(Math.max(firstFoundSystemIme, 0));
    }

    public static boolean isValidSubtypeId(InputMethodInfo imi, int subtypeHashCode) {
        return getSubtypeIdFromHashCode(imi, subtypeHashCode) != -1;
    }

    public static int getSubtypeIdFromHashCode(InputMethodInfo imi, int subtypeHashCode) {
        if (imi != null) {
            int subtypeCount = imi.getSubtypeCount();
            for (int i = 0; i < subtypeCount; i++) {
                InputMethodSubtype ims = imi.getSubtypeAt(i);
                if (subtypeHashCode == ims.hashCode()) {
                    return i;
                }
            }
            return -1;
        }
        return -1;
    }

    @VisibleForTesting
    public static ArrayList<InputMethodSubtype> getImplicitlyApplicableSubtypesLocked(Resources res, InputMethodInfo imi) {
        LocaleList systemLocales = res.getConfiguration().getLocales();
        synchronized (sCacheLock) {
            if (systemLocales.equals(sCachedSystemLocales) && sCachedInputMethodInfo == imi) {
                return new ArrayList<>(sCachedResult);
            }
            ArrayList<InputMethodSubtype> result = getImplicitlyApplicableSubtypesLockedImpl(res, imi);
            synchronized (sCacheLock) {
                sCachedSystemLocales = systemLocales;
                sCachedInputMethodInfo = imi;
                sCachedResult = new ArrayList<>(result);
            }
            return result;
        }
    }

    private static ArrayList<InputMethodSubtype> getImplicitlyApplicableSubtypesLockedImpl(Resources res, InputMethodInfo imi) {
        InputMethodSubtype lastResortKeyboardSubtype;
        List<InputMethodSubtype> subtypes = getSubtypes(imi);
        LocaleList systemLocales = res.getConfiguration().getLocales();
        String systemLocale = systemLocales.get(0).toString();
        if (TextUtils.isEmpty(systemLocale)) {
            return new ArrayList<>();
        }
        int numSubtypes = subtypes.size();
        HashMap<String, InputMethodSubtype> applicableModeAndSubtypesMap = new HashMap<>();
        for (int i = 0; i < numSubtypes; i++) {
            InputMethodSubtype subtype = subtypes.get(i);
            if (subtype.overridesImplicitlyEnabledSubtype()) {
                String mode = subtype.getMode();
                if (!applicableModeAndSubtypesMap.containsKey(mode)) {
                    applicableModeAndSubtypesMap.put(mode, subtype);
                }
            }
        }
        int i2 = applicableModeAndSubtypesMap.size();
        if (i2 > 0) {
            return new ArrayList<>(applicableModeAndSubtypesMap.values());
        }
        HashMap<String, ArrayList<InputMethodSubtype>> nonKeyboardSubtypesMap = new HashMap<>();
        ArrayList<InputMethodSubtype> keyboardSubtypes = new ArrayList<>();
        for (int i3 = 0; i3 < numSubtypes; i3++) {
            InputMethodSubtype subtype2 = subtypes.get(i3);
            String mode2 = subtype2.getMode();
            if (SUBTYPE_MODE_KEYBOARD.equals(mode2)) {
                keyboardSubtypes.add(subtype2);
            } else {
                if (!nonKeyboardSubtypesMap.containsKey(mode2)) {
                    nonKeyboardSubtypesMap.put(mode2, new ArrayList<>());
                }
                nonKeyboardSubtypesMap.get(mode2).add(subtype2);
            }
        }
        ArrayList<InputMethodSubtype> applicableSubtypes = new ArrayList<>();
        LocaleUtils.filterByLanguage(keyboardSubtypes, sSubtypeToLocale, systemLocales, applicableSubtypes);
        if (!applicableSubtypes.isEmpty()) {
            boolean hasAsciiCapableKeyboard = false;
            int numApplicationSubtypes = applicableSubtypes.size();
            int i4 = 0;
            while (true) {
                if (i4 < numApplicationSubtypes) {
                    if (!applicableSubtypes.get(i4).containsExtraValueKey(TAG_ASCII_CAPABLE)) {
                        i4++;
                    } else {
                        hasAsciiCapableKeyboard = true;
                        break;
                    }
                } else {
                    break;
                }
            }
            if (!hasAsciiCapableKeyboard) {
                int numKeyboardSubtypes = keyboardSubtypes.size();
                for (int i5 = 0; i5 < numKeyboardSubtypes; i5++) {
                    InputMethodSubtype subtype3 = keyboardSubtypes.get(i5);
                    if (SUBTYPE_MODE_KEYBOARD.equals(subtype3.getMode()) && subtype3.containsExtraValueKey(TAG_ENABLED_WHEN_DEFAULT_IS_NOT_ASCII_CAPABLE)) {
                        applicableSubtypes.add(subtype3);
                    }
                }
            }
        }
        if (applicableSubtypes.isEmpty() && (lastResortKeyboardSubtype = findLastResortApplicableSubtypeLocked(res, subtypes, SUBTYPE_MODE_KEYBOARD, systemLocale, true)) != null) {
            applicableSubtypes.add(lastResortKeyboardSubtype);
        }
        for (ArrayList<InputMethodSubtype> subtypeList : nonKeyboardSubtypesMap.values()) {
            LocaleUtils.filterByLanguage(subtypeList, sSubtypeToLocale, systemLocales, applicableSubtypes);
        }
        return applicableSubtypes;
    }

    public static String getLanguageFromLocaleString(String locale) {
        int idx = locale.indexOf(95);
        if (idx < 0) {
            return locale;
        }
        return locale.substring(0, idx);
    }

    public static InputMethodSubtype findLastResortApplicableSubtypeLocked(Resources res, List<InputMethodSubtype> subtypes, String mode, String locale, boolean canIgnoreLocaleAsLastResort) {
        if (subtypes == null || subtypes.size() == 0) {
            return null;
        }
        if (TextUtils.isEmpty(locale)) {
            locale = res.getConfiguration().locale.toString();
        }
        String language = getLanguageFromLocaleString(locale);
        boolean partialMatchFound = false;
        InputMethodSubtype applicableSubtype = null;
        InputMethodSubtype firstMatchedModeSubtype = null;
        int N = subtypes.size();
        int i = 0;
        while (true) {
            if (i >= N) {
                break;
            }
            InputMethodSubtype subtype = subtypes.get(i);
            String subtypeLocale = subtype.getLocale();
            String subtypeLanguage = getLanguageFromLocaleString(subtypeLocale);
            if (mode == null || subtypes.get(i).getMode().equalsIgnoreCase(mode)) {
                if (firstMatchedModeSubtype == null) {
                    firstMatchedModeSubtype = subtype;
                }
                if (locale.equals(subtypeLocale)) {
                    applicableSubtype = subtype;
                    break;
                } else if (!partialMatchFound && language.equals(subtypeLanguage)) {
                    applicableSubtype = subtype;
                    partialMatchFound = true;
                }
            }
            i++;
        }
        if (applicableSubtype == null && canIgnoreLocaleAsLastResort) {
            return firstMatchedModeSubtype;
        }
        return applicableSubtype;
    }

    public static boolean canAddToLastInputMethod(InputMethodSubtype subtype) {
        if (subtype == null) {
            return true;
        }
        return true ^ subtype.isAuxiliary();
    }

    public static void setNonSelectedSystemImesDisabledUntilUsed(IPackageManager packageManager, List<InputMethodInfo> enabledImis, int userId, String callingPackage) {
        String[] systemImesDisabledUntilUsed = Resources.getSystem().getStringArray(R.array.config_disabledUntilUsedPreinstalledImes);
        if (systemImesDisabledUntilUsed == null || systemImesDisabledUntilUsed.length == 0) {
            return;
        }
        SpellCheckerInfo currentSpellChecker = TextServicesManager.getInstance().getCurrentSpellChecker();
        for (String packageName : systemImesDisabledUntilUsed) {
            boolean enabledIme = false;
            int j = 0;
            while (true) {
                if (j >= enabledImis.size()) {
                    break;
                }
                InputMethodInfo imi = enabledImis.get(j);
                if (!packageName.equals(imi.getPackageName())) {
                    j++;
                } else {
                    enabledIme = true;
                    break;
                }
            }
            if (!enabledIme && (currentSpellChecker == null || !packageName.equals(currentSpellChecker.getPackageName()))) {
                try {
                    ApplicationInfo ai = packageManager.getApplicationInfo(packageName, 32768, userId);
                    if (ai != null) {
                        boolean isSystemPackage = (ai.flags & 1) != 0;
                        if (isSystemPackage) {
                            setDisabledUntilUsed(packageManager, packageName, userId, callingPackage);
                        }
                    }
                } catch (RemoteException e) {
                    Slog.w(TAG, "getApplicationInfo failed. packageName=" + packageName + " userId=" + userId, e);
                }
            }
        }
    }

    private static void setDisabledUntilUsed(IPackageManager packageManager, String packageName, int userId, String callingPackage) {
        try {
            int state = packageManager.getApplicationEnabledSetting(packageName, userId);
            if (state == 0 || state == 1) {
                try {
                    packageManager.setApplicationEnabledSetting(packageName, 4, 0, userId, callingPackage);
                } catch (RemoteException e) {
                    Slog.w(TAG, "setApplicationEnabledSetting failed. packageName=" + packageName + " userId=" + userId + " callingPackage=" + callingPackage, e);
                }
            }
        } catch (RemoteException e2) {
            Slog.w(TAG, "getApplicationEnabledSetting failed. packageName=" + packageName + " userId=" + userId, e2);
        }
    }

    public static CharSequence getImeAndSubtypeDisplayName(Context context, InputMethodInfo imi, InputMethodSubtype subtype) {
        String str;
        CharSequence imiLabel = imi.loadLabel(context.getPackageManager());
        if (subtype == null) {
            return imiLabel;
        }
        CharSequence[] charSequenceArr = new CharSequence[2];
        charSequenceArr[0] = subtype.getDisplayName(context, imi.getPackageName(), imi.getServiceInfo().applicationInfo);
        if (TextUtils.isEmpty(imiLabel)) {
            str = "";
        } else {
            str = " - " + ((Object) imiLabel);
        }
        charSequenceArr[1] = str;
        return TextUtils.concat(charSequenceArr);
    }

    public static boolean checkIfPackageBelongsToUid(AppOpsManager appOpsManager, int uid, String packageName) {
        try {
            appOpsManager.checkPackage(uid, packageName);
            return true;
        } catch (SecurityException e) {
            return false;
        }
    }

    @VisibleForTesting
    public static ArrayMap<String, ArraySet<String>> parseInputMethodsAndSubtypesString(String inputMethodsAndSubtypesString) {
        ArrayMap<String, ArraySet<String>> imeMap = new ArrayMap<>();
        if (TextUtils.isEmpty(inputMethodsAndSubtypesString)) {
            return imeMap;
        }
        TextUtils.SimpleStringSplitter typeSplitter = new TextUtils.SimpleStringSplitter(INPUT_METHOD_SEPARATOR);
        TextUtils.SimpleStringSplitter subtypeSplitter = new TextUtils.SimpleStringSplitter(';');
        List<Pair<String, ArrayList<String>>> allImeSettings = InputMethodSettings.buildInputMethodsAndSubtypeList(inputMethodsAndSubtypesString, typeSplitter, subtypeSplitter);
        for (Pair<String, ArrayList<String>> ime : allImeSettings) {
            ArraySet<String> subtypes = new ArraySet<>();
            if (ime.second != null) {
                subtypes.addAll(ime.second);
            }
            imeMap.put(ime.first, subtypes);
        }
        return imeMap;
    }

    public static String buildInputMethodsAndSubtypesString(ArrayMap<String, ArraySet<String>> map) {
        List<Pair<String, ArrayList<String>>> imeMap = new ArrayList<>(4);
        for (Map.Entry<String, ArraySet<String>> entry : map.entrySet()) {
            String imeName = entry.getKey();
            Collection<? extends String> subtypeSet = (ArraySet) entry.getValue();
            ArrayList<String> subtypes = new ArrayList<>(2);
            if (subtypeSet != null) {
                subtypes.addAll(subtypeSet);
            }
            imeMap.add(new Pair<>(imeName, subtypes));
        }
        return InputMethodSettings.buildInputMethodsSettingString(imeMap);
    }

    /* loaded from: classes3.dex */
    public static class InputMethodSettings {
        private int mCurrentUserId;
        private final HashMap<String, InputMethodInfo> mMethodMap;
        private final Resources mRes;
        private final ContentResolver mResolver;
        private final TextUtils.SimpleStringSplitter mInputMethodSplitter = new TextUtils.SimpleStringSplitter(InputMethodUtils.INPUT_METHOD_SEPARATOR);
        private final TextUtils.SimpleStringSplitter mSubtypeSplitter = new TextUtils.SimpleStringSplitter(';');
        private final HashMap<String, String> mCopyOnWriteDataStore = new HashMap<>();
        private boolean mCopyOnWrite = false;
        private String mEnabledInputMethodsStrCache = "";
        private int[] mCurrentProfileIds = new int[0];

        private static void buildEnabledInputMethodsSettingString(StringBuilder builder, Pair<String, ArrayList<String>> ime) {
            builder.append(ime.first);
            Iterator<String> it = ime.second.iterator();
            while (it.hasNext()) {
                String subtypeId = it.next();
                builder.append(';');
                builder.append(subtypeId);
            }
        }

        public static String buildInputMethodsSettingString(List<Pair<String, ArrayList<String>>> allImeSettingsMap) {
            StringBuilder b = new StringBuilder();
            boolean needsSeparator = false;
            for (Pair<String, ArrayList<String>> ime : allImeSettingsMap) {
                if (needsSeparator) {
                    b.append(InputMethodUtils.INPUT_METHOD_SEPARATOR);
                }
                buildEnabledInputMethodsSettingString(b, ime);
                needsSeparator = true;
            }
            return b.toString();
        }

        public static List<Pair<String, ArrayList<String>>> buildInputMethodsAndSubtypeList(String enabledInputMethodsStr, TextUtils.SimpleStringSplitter inputMethodSplitter, TextUtils.SimpleStringSplitter subtypeSplitter) {
            ArrayList<Pair<String, ArrayList<String>>> imsList = new ArrayList<>();
            if (TextUtils.isEmpty(enabledInputMethodsStr)) {
                return imsList;
            }
            inputMethodSplitter.setString(enabledInputMethodsStr);
            while (inputMethodSplitter.hasNext()) {
                String nextImsStr = inputMethodSplitter.next();
                subtypeSplitter.setString(nextImsStr);
                if (subtypeSplitter.hasNext()) {
                    ArrayList<String> subtypeHashes = new ArrayList<>();
                    String imeId = subtypeSplitter.next();
                    while (subtypeSplitter.hasNext()) {
                        subtypeHashes.add(subtypeSplitter.next());
                    }
                    imsList.add(new Pair<>(imeId, subtypeHashes));
                }
            }
            return imsList;
        }

        public InputMethodSettings(Resources res, ContentResolver resolver, HashMap<String, InputMethodInfo> methodMap, ArrayList<InputMethodInfo> methodList, int userId, boolean copyOnWrite) {
            this.mRes = res;
            this.mResolver = resolver;
            this.mMethodMap = methodMap;
            switchCurrentUser(userId, copyOnWrite);
        }

        public void switchCurrentUser(int userId, boolean copyOnWrite) {
            if (this.mCurrentUserId != userId || this.mCopyOnWrite != copyOnWrite) {
                this.mCopyOnWriteDataStore.clear();
                this.mEnabledInputMethodsStrCache = "";
            }
            this.mCurrentUserId = userId;
            this.mCopyOnWrite = copyOnWrite;
        }

        private void putString(String key, String str) {
            if (this.mCopyOnWrite) {
                this.mCopyOnWriteDataStore.put(key, str);
            } else {
                Settings.Secure.putStringForUser(this.mResolver, key, str, this.mCurrentUserId);
            }
        }

        private String getString(String key, String defaultValue) {
            String result;
            if (this.mCopyOnWrite && this.mCopyOnWriteDataStore.containsKey(key)) {
                result = this.mCopyOnWriteDataStore.get(key);
            } else {
                result = Settings.Secure.getStringForUser(this.mResolver, key, this.mCurrentUserId);
            }
            return result != null ? result : defaultValue;
        }

        private void putInt(String key, int value) {
            if (this.mCopyOnWrite) {
                this.mCopyOnWriteDataStore.put(key, String.valueOf(value));
            } else {
                Settings.Secure.putIntForUser(this.mResolver, key, value, this.mCurrentUserId);
            }
        }

        private int getInt(String key, int defaultValue) {
            if (this.mCopyOnWrite && this.mCopyOnWriteDataStore.containsKey(key)) {
                String result = this.mCopyOnWriteDataStore.get(key);
                if (result != null) {
                    return Integer.parseInt(result);
                }
                return 0;
            }
            return Settings.Secure.getIntForUser(this.mResolver, key, defaultValue, this.mCurrentUserId);
        }

        private void putBoolean(String key, boolean value) {
            putInt(key, value ? 1 : 0);
        }

        private boolean getBoolean(String key, boolean defaultValue) {
            return getInt(key, defaultValue ? 1 : 0) == 1;
        }

        public void setCurrentProfileIds(int[] currentProfileIds) {
            synchronized (this) {
                this.mCurrentProfileIds = currentProfileIds;
            }
        }

        public boolean isCurrentProfile(int userId) {
            synchronized (this) {
                if (userId == this.mCurrentUserId) {
                    return true;
                }
                for (int i = 0; i < this.mCurrentProfileIds.length; i++) {
                    if (userId == this.mCurrentProfileIds[i]) {
                        return true;
                    }
                }
                return false;
            }
        }

        public ArrayList<InputMethodInfo> getEnabledInputMethodListLocked() {
            return createEnabledInputMethodListLocked(getEnabledInputMethodsAndSubtypeListLocked());
        }

        public List<InputMethodSubtype> getEnabledInputMethodSubtypeListLocked(Context context, InputMethodInfo imi, boolean allowsImplicitlySelectedSubtypes) {
            List<InputMethodSubtype> enabledSubtypes = getEnabledInputMethodSubtypeListLocked(imi);
            if (allowsImplicitlySelectedSubtypes && enabledSubtypes.isEmpty()) {
                enabledSubtypes = InputMethodUtils.getImplicitlyApplicableSubtypesLocked(context.getResources(), imi);
            }
            return InputMethodSubtype.sort(context, 0, imi, enabledSubtypes);
        }

        public List<InputMethodSubtype> getEnabledInputMethodSubtypeListLocked(InputMethodInfo imi) {
            List<Pair<String, ArrayList<String>>> imsList = getEnabledInputMethodsAndSubtypeListLocked();
            ArrayList<InputMethodSubtype> enabledSubtypes = new ArrayList<>();
            if (imi != null) {
                Iterator<Pair<String, ArrayList<String>>> it = imsList.iterator();
                while (true) {
                    if (!it.hasNext()) {
                        break;
                    }
                    Pair<String, ArrayList<String>> imsPair = it.next();
                    InputMethodInfo info = this.mMethodMap.get(imsPair.first);
                    if (info != null && info.getId().equals(imi.getId())) {
                        int subtypeCount = info.getSubtypeCount();
                        for (int i = 0; i < subtypeCount; i++) {
                            InputMethodSubtype ims = info.getSubtypeAt(i);
                            Iterator<String> it2 = imsPair.second.iterator();
                            while (it2.hasNext()) {
                                String s = it2.next();
                                if (String.valueOf(ims.hashCode()).equals(s)) {
                                    enabledSubtypes.add(ims);
                                }
                            }
                        }
                    }
                }
            }
            return enabledSubtypes;
        }

        public List<Pair<String, ArrayList<String>>> getEnabledInputMethodsAndSubtypeListLocked() {
            return buildInputMethodsAndSubtypeList(getEnabledInputMethodsStr(), this.mInputMethodSplitter, this.mSubtypeSplitter);
        }

        public void appendAndPutEnabledInputMethodLocked(String id, boolean reloadInputMethodStr) {
            if (reloadInputMethodStr) {
                getEnabledInputMethodsStr();
            }
            if (TextUtils.isEmpty(this.mEnabledInputMethodsStrCache)) {
                putEnabledInputMethodsStr(id);
                return;
            }
            putEnabledInputMethodsStr(this.mEnabledInputMethodsStrCache + InputMethodUtils.INPUT_METHOD_SEPARATOR + id);
        }

        public boolean buildAndPutEnabledInputMethodsStrRemovingIdLocked(StringBuilder builder, List<Pair<String, ArrayList<String>>> imsList, String id) {
            boolean isRemoved = false;
            boolean needsAppendSeparator = false;
            for (Pair<String, ArrayList<String>> ims : imsList) {
                String curId = ims.first;
                if (curId.equals(id)) {
                    isRemoved = true;
                } else {
                    if (needsAppendSeparator) {
                        builder.append(InputMethodUtils.INPUT_METHOD_SEPARATOR);
                    } else {
                        needsAppendSeparator = true;
                    }
                    buildEnabledInputMethodsSettingString(builder, ims);
                }
            }
            if (isRemoved) {
                putEnabledInputMethodsStr(builder.toString());
            }
            return isRemoved;
        }

        private ArrayList<InputMethodInfo> createEnabledInputMethodListLocked(List<Pair<String, ArrayList<String>>> imsList) {
            ArrayList<InputMethodInfo> res = new ArrayList<>();
            for (Pair<String, ArrayList<String>> ims : imsList) {
                InputMethodInfo info = this.mMethodMap.get(ims.first);
                if (info != null && !info.isVrOnly()) {
                    res.add(info);
                }
            }
            return res;
        }

        private void putEnabledInputMethodsStr(String str) {
            if (TextUtils.isEmpty(str)) {
                putString(Settings.Secure.ENABLED_INPUT_METHODS, null);
            } else {
                putString(Settings.Secure.ENABLED_INPUT_METHODS, str);
            }
            this.mEnabledInputMethodsStrCache = str != null ? str : "";
        }

        public String getEnabledInputMethodsStr() {
            this.mEnabledInputMethodsStrCache = getString(Settings.Secure.ENABLED_INPUT_METHODS, "");
            return this.mEnabledInputMethodsStrCache;
        }

        private void saveSubtypeHistory(List<Pair<String, String>> savedImes, String newImeId, String newSubtypeId) {
            StringBuilder builder = new StringBuilder();
            boolean isImeAdded = false;
            if (!TextUtils.isEmpty(newImeId) && !TextUtils.isEmpty(newSubtypeId)) {
                builder.append(newImeId);
                builder.append(';');
                builder.append(newSubtypeId);
                isImeAdded = true;
            }
            for (Pair<String, String> ime : savedImes) {
                String imeId = ime.first;
                String subtypeId = ime.second;
                if (TextUtils.isEmpty(subtypeId)) {
                    subtypeId = InputMethodUtils.NOT_A_SUBTYPE_ID_STR;
                }
                if (isImeAdded) {
                    builder.append(InputMethodUtils.INPUT_METHOD_SEPARATOR);
                } else {
                    isImeAdded = true;
                }
                builder.append(imeId);
                builder.append(';');
                builder.append(subtypeId);
            }
            putSubtypeHistoryStr(builder.toString());
        }

        private void addSubtypeToHistory(String imeId, String subtypeId) {
            List<Pair<String, String>> subtypeHistory = loadInputMethodAndSubtypeHistoryLocked();
            Iterator<Pair<String, String>> it = subtypeHistory.iterator();
            while (true) {
                if (!it.hasNext()) {
                    break;
                }
                Pair<String, String> ime = it.next();
                if (ime.first.equals(imeId)) {
                    subtypeHistory.remove(ime);
                    break;
                }
            }
            saveSubtypeHistory(subtypeHistory, imeId, subtypeId);
        }

        private void putSubtypeHistoryStr(String str) {
            if (TextUtils.isEmpty(str)) {
                putString(Settings.Secure.INPUT_METHODS_SUBTYPE_HISTORY, null);
            } else {
                putString(Settings.Secure.INPUT_METHODS_SUBTYPE_HISTORY, str);
            }
        }

        public Pair<String, String> getLastInputMethodAndSubtypeLocked() {
            return getLastSubtypeForInputMethodLockedInternal(null);
        }

        public String getLastSubtypeForInputMethodLocked(String imeId) {
            Pair<String, String> ime = getLastSubtypeForInputMethodLockedInternal(imeId);
            if (ime != null) {
                return ime.second;
            }
            return null;
        }

        private Pair<String, String> getLastSubtypeForInputMethodLockedInternal(String imeId) {
            List<Pair<String, ArrayList<String>>> enabledImes = getEnabledInputMethodsAndSubtypeListLocked();
            List<Pair<String, String>> subtypeHistory = loadInputMethodAndSubtypeHistoryLocked();
            for (Pair<String, String> imeAndSubtype : subtypeHistory) {
                String imeInTheHistory = imeAndSubtype.first;
                if (TextUtils.isEmpty(imeId) || imeInTheHistory.equals(imeId)) {
                    String subtypeInTheHistory = imeAndSubtype.second;
                    String subtypeHashCode = getEnabledSubtypeHashCodeForInputMethodAndSubtypeLocked(enabledImes, imeInTheHistory, subtypeInTheHistory);
                    if (!TextUtils.isEmpty(subtypeHashCode)) {
                        return new Pair<>(imeInTheHistory, subtypeHashCode);
                    }
                }
            }
            return null;
        }

        private String getEnabledSubtypeHashCodeForInputMethodAndSubtypeLocked(List<Pair<String, ArrayList<String>>> enabledImes, String imeId, String subtypeHashCode) {
            List<InputMethodSubtype> implicitlySelectedSubtypes;
            for (Pair<String, ArrayList<String>> enabledIme : enabledImes) {
                if (enabledIme.first.equals(imeId)) {
                    ArrayList<String> explicitlyEnabledSubtypes = enabledIme.second;
                    InputMethodInfo imi = this.mMethodMap.get(imeId);
                    if (explicitlyEnabledSubtypes.size() == 0) {
                        if (imi != null && imi.getSubtypeCount() > 0 && (implicitlySelectedSubtypes = InputMethodUtils.getImplicitlyApplicableSubtypesLocked(this.mRes, imi)) != null) {
                            int N = implicitlySelectedSubtypes.size();
                            for (int i = 0; i < N; i++) {
                                InputMethodSubtype st = implicitlySelectedSubtypes.get(i);
                                if (String.valueOf(st.hashCode()).equals(subtypeHashCode)) {
                                    return subtypeHashCode;
                                }
                            }
                        }
                    } else {
                        Iterator<String> it = explicitlyEnabledSubtypes.iterator();
                        while (it.hasNext()) {
                            String s = it.next();
                            if (s.equals(subtypeHashCode)) {
                                try {
                                    int hashCode = Integer.parseInt(subtypeHashCode);
                                    if (!InputMethodUtils.isValidSubtypeId(imi, hashCode)) {
                                        return InputMethodUtils.NOT_A_SUBTYPE_ID_STR;
                                    }
                                    return s;
                                } catch (NumberFormatException e) {
                                    return InputMethodUtils.NOT_A_SUBTYPE_ID_STR;
                                }
                            }
                        }
                    }
                    return InputMethodUtils.NOT_A_SUBTYPE_ID_STR;
                }
            }
            return null;
        }

        private List<Pair<String, String>> loadInputMethodAndSubtypeHistoryLocked() {
            ArrayList<Pair<String, String>> imsList = new ArrayList<>();
            String subtypeHistoryStr = getSubtypeHistoryStr();
            if (TextUtils.isEmpty(subtypeHistoryStr)) {
                return imsList;
            }
            this.mInputMethodSplitter.setString(subtypeHistoryStr);
            while (this.mInputMethodSplitter.hasNext()) {
                String nextImsStr = this.mInputMethodSplitter.next();
                this.mSubtypeSplitter.setString(nextImsStr);
                if (this.mSubtypeSplitter.hasNext()) {
                    String subtypeId = InputMethodUtils.NOT_A_SUBTYPE_ID_STR;
                    String imeId = this.mSubtypeSplitter.next();
                    if (this.mSubtypeSplitter.hasNext()) {
                        subtypeId = this.mSubtypeSplitter.next();
                    }
                    imsList.add(new Pair<>(imeId, subtypeId));
                }
            }
            return imsList;
        }

        private String getSubtypeHistoryStr() {
            String history = getString(Settings.Secure.INPUT_METHODS_SUBTYPE_HISTORY, "");
            return history;
        }

        public void putSelectedInputMethod(String imeId) {
            putString(Settings.Secure.DEFAULT_INPUT_METHOD, imeId);
        }

        public void putSelectedSubtype(int subtypeId) {
            putInt(Settings.Secure.SELECTED_INPUT_METHOD_SUBTYPE, subtypeId);
        }

        public String getSelectedInputMethod() {
            String imi = getString(Settings.Secure.DEFAULT_INPUT_METHOD, null);
            return imi;
        }

        public boolean isSubtypeSelected() {
            return getSelectedInputMethodSubtypeHashCode() != -1;
        }

        private int getSelectedInputMethodSubtypeHashCode() {
            return getInt(Settings.Secure.SELECTED_INPUT_METHOD_SUBTYPE, -1);
        }

        public boolean isShowImeWithHardKeyboardEnabled() {
            return getBoolean(Settings.Secure.SHOW_IME_WITH_HARD_KEYBOARD, false);
        }

        public void setShowImeWithHardKeyboard(boolean show) {
            putBoolean(Settings.Secure.SHOW_IME_WITH_HARD_KEYBOARD, show);
        }

        public int getCurrentUserId() {
            return this.mCurrentUserId;
        }

        public int getSelectedInputMethodSubtypeId(String selectedImiId) {
            InputMethodInfo imi = this.mMethodMap.get(selectedImiId);
            if (imi == null) {
                return -1;
            }
            int subtypeHashCode = getSelectedInputMethodSubtypeHashCode();
            return InputMethodUtils.getSubtypeIdFromHashCode(imi, subtypeHashCode);
        }

        public void saveCurrentInputMethodAndSubtypeToHistory(String curMethodId, InputMethodSubtype currentSubtype) {
            String subtypeId = InputMethodUtils.NOT_A_SUBTYPE_ID_STR;
            if (currentSubtype != null) {
                subtypeId = String.valueOf(currentSubtype.hashCode());
            }
            if (InputMethodUtils.canAddToLastInputMethod(currentSubtype)) {
                addSubtypeToHistory(curMethodId, subtypeId);
            }
        }

        public HashMap<InputMethodInfo, List<InputMethodSubtype>> getExplicitlyOrImplicitlyEnabledInputMethodsAndSubtypeListLocked(Context context) {
            HashMap<InputMethodInfo, List<InputMethodSubtype>> enabledInputMethodAndSubtypes = new HashMap<>();
            Iterator<InputMethodInfo> it = getEnabledInputMethodListLocked().iterator();
            while (it.hasNext()) {
                InputMethodInfo imi = it.next();
                enabledInputMethodAndSubtypes.put(imi, getEnabledInputMethodSubtypeListLocked(context, imi, true));
            }
            return enabledInputMethodAndSubtypes;
        }

        public void dumpLocked(Printer pw, String prefix) {
            pw.println(prefix + "mCurrentUserId=" + this.mCurrentUserId);
            pw.println(prefix + "mCurrentProfileIds=" + Arrays.toString(this.mCurrentProfileIds));
            pw.println(prefix + "mCopyOnWrite=" + this.mCopyOnWrite);
            pw.println(prefix + "mEnabledInputMethodsStrCache=" + this.mEnabledInputMethodsStrCache);
        }
    }

    @VisibleForTesting
    public static ArrayList<Locale> getSuitableLocalesForSpellChecker(Locale systemLocale) {
        Locale systemLocaleLanguageCountryVariant;
        Locale systemLocaleLanguageCountry;
        Locale systemLocaleLanguage = null;
        if (systemLocale != null) {
            String language = systemLocale.getLanguage();
            boolean hasLanguage = !TextUtils.isEmpty(language);
            String country = systemLocale.getCountry();
            boolean hasCountry = !TextUtils.isEmpty(country);
            String variant = systemLocale.getVariant();
            boolean hasVariant = !TextUtils.isEmpty(variant);
            if (hasLanguage && hasCountry && hasVariant) {
                systemLocaleLanguageCountryVariant = new Locale(language, country, variant);
            } else {
                systemLocaleLanguageCountryVariant = null;
            }
            if (hasLanguage && hasCountry) {
                systemLocaleLanguageCountry = new Locale(language, country);
            } else {
                systemLocaleLanguageCountry = null;
            }
            if (hasLanguage) {
                systemLocaleLanguage = new Locale(language);
            }
        } else {
            systemLocaleLanguageCountryVariant = null;
            systemLocaleLanguageCountry = null;
        }
        ArrayList<Locale> locales = new ArrayList<>();
        if (systemLocaleLanguageCountryVariant != null) {
            locales.add(systemLocaleLanguageCountryVariant);
        }
        if (Locale.ENGLISH.equals(systemLocaleLanguage)) {
            if (systemLocaleLanguageCountry != null) {
                if (systemLocaleLanguageCountry != null) {
                    locales.add(systemLocaleLanguageCountry);
                }
                if (!LOCALE_EN_US.equals(systemLocaleLanguageCountry)) {
                    locales.add(LOCALE_EN_US);
                }
                if (!LOCALE_EN_GB.equals(systemLocaleLanguageCountry)) {
                    locales.add(LOCALE_EN_GB);
                }
                locales.add(Locale.ENGLISH);
            } else {
                locales.add(Locale.ENGLISH);
                locales.add(LOCALE_EN_US);
                locales.add(LOCALE_EN_GB);
            }
        } else {
            if (systemLocaleLanguageCountry != null) {
                locales.add(systemLocaleLanguageCountry);
            }
            if (systemLocaleLanguage != null) {
                locales.add(systemLocaleLanguage);
            }
            locales.add(LOCALE_EN_US);
            locales.add(LOCALE_EN_GB);
            locales.add(Locale.ENGLISH);
        }
        return locales;
    }

    public static boolean isSoftInputModeStateVisibleAllowed(int targetSdkVersion, int controlFlags) {
        if (targetSdkVersion < 28) {
            return true;
        }
        return ((controlFlags & 1) == 0 || (controlFlags & 2) == 0) ? false : true;
    }
}
