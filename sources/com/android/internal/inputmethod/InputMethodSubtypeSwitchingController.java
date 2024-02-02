package com.android.internal.inputmethod;

import android.content.Context;
import android.content.pm.PackageManager;
import android.text.TextUtils;
import android.util.Printer;
import android.view.inputmethod.InputMethodInfo;
import android.view.inputmethod.InputMethodSubtype;
import com.android.internal.annotations.VisibleForTesting;
import com.android.internal.inputmethod.InputMethodUtils;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Locale;
import java.util.Objects;
import java.util.TreeMap;
/* loaded from: classes3.dex */
public class InputMethodSubtypeSwitchingController {
    private static final boolean DEBUG = false;
    private static final int NOT_A_SUBTYPE_ID = -1;
    private static final String TAG = InputMethodSubtypeSwitchingController.class.getSimpleName();
    private ControllerImpl mController;
    private final InputMethodUtils.InputMethodSettings mSettings;
    private InputMethodAndSubtypeList mSubtypeList;

    /* loaded from: classes3.dex */
    public static class ImeSubtypeListItem implements Comparable<ImeSubtypeListItem> {
        public final CharSequence mImeName;
        public final InputMethodInfo mImi;
        public final boolean mIsSystemLanguage;
        public final boolean mIsSystemLocale;
        public final int mSubtypeId;
        public final CharSequence mSubtypeName;

        public ImeSubtypeListItem(CharSequence imeName, CharSequence subtypeName, InputMethodInfo imi, int subtypeId, String subtypeLocale, String systemLocale) {
            this.mImeName = imeName;
            this.mSubtypeName = subtypeName;
            this.mImi = imi;
            this.mSubtypeId = subtypeId;
            boolean z = false;
            if (TextUtils.isEmpty(subtypeLocale)) {
                this.mIsSystemLocale = false;
                this.mIsSystemLanguage = false;
                return;
            }
            this.mIsSystemLocale = subtypeLocale.equals(systemLocale);
            if (this.mIsSystemLocale) {
                this.mIsSystemLanguage = true;
                return;
            }
            String systemLanguage = parseLanguageFromLocaleString(systemLocale);
            String subtypeLanguage = parseLanguageFromLocaleString(subtypeLocale);
            if (systemLanguage.length() >= 2 && systemLanguage.equals(subtypeLanguage)) {
                z = true;
            }
            this.mIsSystemLanguage = z;
        }

        private static String parseLanguageFromLocaleString(String locale) {
            int idx = locale.indexOf(95);
            if (idx < 0) {
                return locale;
            }
            return locale.substring(0, idx);
        }

        private static int compareNullableCharSequences(CharSequence c1, CharSequence c2) {
            boolean empty1 = TextUtils.isEmpty(c1);
            boolean empty2 = TextUtils.isEmpty(c2);
            if (empty1 || empty2) {
                return (empty1 ? 1 : 0) - (empty2 ? 1 : 0);
            }
            return c1.toString().compareTo(c2.toString());
        }

        @Override // java.lang.Comparable
        public int compareTo(ImeSubtypeListItem other) {
            int result = compareNullableCharSequences(this.mImeName, other.mImeName);
            if (result != 0) {
                return result;
            }
            int result2 = (this.mIsSystemLocale ? -1 : 0) - (other.mIsSystemLocale ? -1 : 0);
            if (result2 != 0) {
                return result2;
            }
            int result3 = (this.mIsSystemLanguage ? -1 : 0) - (other.mIsSystemLanguage ? -1 : 0);
            if (result3 != 0) {
                return result3;
            }
            return compareNullableCharSequences(this.mSubtypeName, other.mSubtypeName);
        }

        public String toString() {
            return "ImeSubtypeListItem{mImeName=" + ((Object) this.mImeName) + " mSubtypeName=" + ((Object) this.mSubtypeName) + " mSubtypeId=" + this.mSubtypeId + " mIsSystemLocale=" + this.mIsSystemLocale + " mIsSystemLanguage=" + this.mIsSystemLanguage + "}";
        }

        public boolean equals(Object o) {
            if (o == this) {
                return true;
            }
            if (o instanceof ImeSubtypeListItem) {
                ImeSubtypeListItem that = (ImeSubtypeListItem) o;
                return Objects.equals(this.mImi, that.mImi) && this.mSubtypeId == that.mSubtypeId;
            }
            return false;
        }
    }

    /* JADX INFO: Access modifiers changed from: private */
    /* loaded from: classes3.dex */
    public static class InputMethodAndSubtypeList {
        private final Context mContext;
        private final PackageManager mPm;
        private final InputMethodUtils.InputMethodSettings mSettings;
        private final TreeMap<InputMethodInfo, List<InputMethodSubtype>> mSortedImmis = new TreeMap<>(new Comparator<InputMethodInfo>() { // from class: com.android.internal.inputmethod.InputMethodSubtypeSwitchingController.InputMethodAndSubtypeList.1
            @Override // java.util.Comparator
            public int compare(InputMethodInfo imi1, InputMethodInfo imi2) {
                if (imi2 == null) {
                    return 0;
                }
                if (imi1 != null) {
                    if (InputMethodAndSubtypeList.this.mPm == null) {
                        return imi1.getId().compareTo(imi2.getId());
                    }
                    CharSequence imiId1 = ((Object) imi1.loadLabel(InputMethodAndSubtypeList.this.mPm)) + "/" + imi1.getId();
                    CharSequence imiId2 = ((Object) imi2.loadLabel(InputMethodAndSubtypeList.this.mPm)) + "/" + imi2.getId();
                    return imiId1.toString().compareTo(imiId2.toString());
                }
                return 1;
            }
        });
        private final String mSystemLocaleStr;

        public InputMethodAndSubtypeList(Context context, InputMethodUtils.InputMethodSettings settings) {
            this.mContext = context;
            this.mSettings = settings;
            this.mPm = context.getPackageManager();
            Locale locale = context.getResources().getConfiguration().locale;
            this.mSystemLocaleStr = locale != null ? locale.toString() : "";
        }

        public List<ImeSubtypeListItem> getSortedInputMethodAndSubtypeList(boolean includeAuxiliarySubtypes, boolean isScreenLocked) {
            boolean includeAuxiliarySubtypes2;
            HashMap<InputMethodInfo, List<InputMethodSubtype>> immis;
            HashMap<InputMethodInfo, List<InputMethodSubtype>> immis2;
            int j;
            int subtypeCount;
            ArrayList<ImeSubtypeListItem> imList = new ArrayList<>();
            HashMap<InputMethodInfo, List<InputMethodSubtype>> immis3 = this.mSettings.getExplicitlyOrImplicitlyEnabledInputMethodsAndSubtypeListLocked(this.mContext);
            if (immis3 != null && immis3.size() != 0) {
                if (isScreenLocked && includeAuxiliarySubtypes) {
                    includeAuxiliarySubtypes2 = false;
                } else {
                    includeAuxiliarySubtypes2 = includeAuxiliarySubtypes;
                }
                this.mSortedImmis.clear();
                this.mSortedImmis.putAll(immis3);
                for (InputMethodInfo imi : this.mSortedImmis.keySet()) {
                    if (imi != null) {
                        List<InputMethodSubtype> explicitlyOrImplicitlyEnabledSubtypeList = immis3.get(imi);
                        HashSet<String> enabledSubtypeSet = new HashSet<>();
                        for (InputMethodSubtype subtype : explicitlyOrImplicitlyEnabledSubtypeList) {
                            enabledSubtypeSet.add(String.valueOf(subtype.hashCode()));
                        }
                        CharSequence imeLabel = imi.loadLabel(this.mPm);
                        if (enabledSubtypeSet.size() > 0) {
                            int subtypeCount2 = imi.getSubtypeCount();
                            int j2 = 0;
                            while (true) {
                                int j3 = j2;
                                if (j3 >= subtypeCount2) {
                                    break;
                                }
                                InputMethodSubtype subtype2 = imi.getSubtypeAt(j3);
                                String subtypeHashCode = String.valueOf(subtype2.hashCode());
                                if (!enabledSubtypeSet.contains(subtypeHashCode)) {
                                    immis2 = immis3;
                                    j = j3;
                                    subtypeCount = subtypeCount2;
                                } else if (!includeAuxiliarySubtypes2 && subtype2.isAuxiliary()) {
                                    immis2 = immis3;
                                    j = j3;
                                    subtypeCount = subtypeCount2;
                                } else {
                                    CharSequence subtypeLabel = subtype2.overridesImplicitlyEnabledSubtype() ? null : subtype2.getDisplayName(this.mContext, imi.getPackageName(), imi.getServiceInfo().applicationInfo);
                                    immis2 = immis3;
                                    j = j3;
                                    subtypeCount = subtypeCount2;
                                    imList.add(new ImeSubtypeListItem(imeLabel, subtypeLabel, imi, j3, subtype2.getLocale(), this.mSystemLocaleStr));
                                    enabledSubtypeSet.remove(subtypeHashCode);
                                }
                                j2 = j + 1;
                                subtypeCount2 = subtypeCount;
                                immis3 = immis2;
                            }
                            immis = immis3;
                        } else {
                            immis = immis3;
                            imList.add(new ImeSubtypeListItem(imeLabel, null, imi, -1, null, this.mSystemLocaleStr));
                        }
                        immis3 = immis;
                    }
                }
                Collections.sort(imList);
                return imList;
            }
            return Collections.emptyList();
        }
    }

    /* JADX INFO: Access modifiers changed from: private */
    public static int calculateSubtypeId(InputMethodInfo imi, InputMethodSubtype subtype) {
        if (subtype != null) {
            return InputMethodUtils.getSubtypeIdFromHashCode(imi, subtype.hashCode());
        }
        return -1;
    }

    /* JADX INFO: Access modifiers changed from: private */
    /* loaded from: classes3.dex */
    public static class StaticRotationList {
        private final List<ImeSubtypeListItem> mImeSubtypeList;

        public StaticRotationList(List<ImeSubtypeListItem> imeSubtypeList) {
            this.mImeSubtypeList = imeSubtypeList;
        }

        private int getIndex(InputMethodInfo imi, InputMethodSubtype subtype) {
            int currentSubtypeId = InputMethodSubtypeSwitchingController.calculateSubtypeId(imi, subtype);
            int N = this.mImeSubtypeList.size();
            for (int i = 0; i < N; i++) {
                ImeSubtypeListItem isli = this.mImeSubtypeList.get(i);
                if (imi.equals(isli.mImi) && isli.mSubtypeId == currentSubtypeId) {
                    return i;
                }
            }
            return -1;
        }

        public ImeSubtypeListItem getNextInputMethodLocked(boolean onlyCurrentIme, InputMethodInfo imi, InputMethodSubtype subtype, boolean forward) {
            int currentIndex;
            if (imi == null) {
                return null;
            }
            if (this.mImeSubtypeList.size() <= 1 || (currentIndex = getIndex(imi, subtype)) < 0) {
                return null;
            }
            int N = this.mImeSubtypeList.size();
            for (int i = 1; i < N; i++) {
                int offset = forward ? i : N - i;
                int candidateIndex = (currentIndex + offset) % N;
                ImeSubtypeListItem candidate = this.mImeSubtypeList.get(candidateIndex);
                if (!onlyCurrentIme || imi.equals(candidate.mImi)) {
                    return candidate;
                }
            }
            return null;
        }

        protected void dump(Printer pw, String prefix) {
            int N = this.mImeSubtypeList.size();
            for (int i = 0; i < N; i++) {
                int rank = i;
                ImeSubtypeListItem item = this.mImeSubtypeList.get(i);
                pw.println(prefix + "rank=" + rank + " item=" + item);
            }
        }
    }

    /* JADX INFO: Access modifiers changed from: private */
    /* loaded from: classes3.dex */
    public static class DynamicRotationList {
        private static final String TAG = DynamicRotationList.class.getSimpleName();
        private final List<ImeSubtypeListItem> mImeSubtypeList;
        private final int[] mUsageHistoryOfSubtypeListItemIndex;

        private DynamicRotationList(List<ImeSubtypeListItem> imeSubtypeListItems) {
            this.mImeSubtypeList = imeSubtypeListItems;
            this.mUsageHistoryOfSubtypeListItemIndex = new int[this.mImeSubtypeList.size()];
            int N = this.mImeSubtypeList.size();
            for (int i = 0; i < N; i++) {
                this.mUsageHistoryOfSubtypeListItemIndex[i] = i;
            }
        }

        private int getUsageRank(InputMethodInfo imi, InputMethodSubtype subtype) {
            int currentSubtypeId = InputMethodSubtypeSwitchingController.calculateSubtypeId(imi, subtype);
            int N = this.mUsageHistoryOfSubtypeListItemIndex.length;
            for (int usageRank = 0; usageRank < N; usageRank++) {
                int subtypeListItemIndex = this.mUsageHistoryOfSubtypeListItemIndex[usageRank];
                ImeSubtypeListItem subtypeListItem = this.mImeSubtypeList.get(subtypeListItemIndex);
                if (subtypeListItem.mImi.equals(imi) && subtypeListItem.mSubtypeId == currentSubtypeId) {
                    return usageRank;
                }
            }
            return -1;
        }

        public void onUserAction(InputMethodInfo imi, InputMethodSubtype subtype) {
            int currentUsageRank = getUsageRank(imi, subtype);
            if (currentUsageRank <= 0) {
                return;
            }
            int currentItemIndex = this.mUsageHistoryOfSubtypeListItemIndex[currentUsageRank];
            System.arraycopy(this.mUsageHistoryOfSubtypeListItemIndex, 0, this.mUsageHistoryOfSubtypeListItemIndex, 1, currentUsageRank);
            this.mUsageHistoryOfSubtypeListItemIndex[0] = currentItemIndex;
        }

        public ImeSubtypeListItem getNextInputMethodLocked(boolean onlyCurrentIme, InputMethodInfo imi, InputMethodSubtype subtype, boolean forward) {
            int currentUsageRank = getUsageRank(imi, subtype);
            if (currentUsageRank < 0) {
                return null;
            }
            int N = this.mUsageHistoryOfSubtypeListItemIndex.length;
            for (int i = 1; i < N; i++) {
                int offset = forward ? i : N - i;
                int subtypeListItemRank = (currentUsageRank + offset) % N;
                int subtypeListItemIndex = this.mUsageHistoryOfSubtypeListItemIndex[subtypeListItemRank];
                ImeSubtypeListItem subtypeListItem = this.mImeSubtypeList.get(subtypeListItemIndex);
                if (!onlyCurrentIme || imi.equals(subtypeListItem.mImi)) {
                    return subtypeListItem;
                }
            }
            return null;
        }

        protected void dump(Printer pw, String prefix) {
            for (int i = 0; i < this.mUsageHistoryOfSubtypeListItemIndex.length; i++) {
                int rank = this.mUsageHistoryOfSubtypeListItemIndex[i];
                ImeSubtypeListItem item = this.mImeSubtypeList.get(i);
                pw.println(prefix + "rank=" + rank + " item=" + item);
            }
        }
    }

    @VisibleForTesting
    /* loaded from: classes3.dex */
    public static class ControllerImpl {
        private final DynamicRotationList mSwitchingAwareRotationList;
        private final StaticRotationList mSwitchingUnawareRotationList;

        public static ControllerImpl createFrom(ControllerImpl currentInstance, List<ImeSubtypeListItem> sortedEnabledItems) {
            DynamicRotationList switchingAwareRotationList = null;
            List<ImeSubtypeListItem> switchingAwareImeSubtypes = filterImeSubtypeList(sortedEnabledItems, true);
            if (currentInstance != null && currentInstance.mSwitchingAwareRotationList != null && Objects.equals(currentInstance.mSwitchingAwareRotationList.mImeSubtypeList, switchingAwareImeSubtypes)) {
                switchingAwareRotationList = currentInstance.mSwitchingAwareRotationList;
            }
            if (switchingAwareRotationList == null) {
                switchingAwareRotationList = new DynamicRotationList(switchingAwareImeSubtypes);
            }
            StaticRotationList switchingUnawareRotationList = null;
            List<ImeSubtypeListItem> switchingUnawareImeSubtypes = filterImeSubtypeList(sortedEnabledItems, false);
            if (currentInstance != null && currentInstance.mSwitchingUnawareRotationList != null && Objects.equals(currentInstance.mSwitchingUnawareRotationList.mImeSubtypeList, switchingUnawareImeSubtypes)) {
                switchingUnawareRotationList = currentInstance.mSwitchingUnawareRotationList;
            }
            if (switchingUnawareRotationList == null) {
                switchingUnawareRotationList = new StaticRotationList(switchingUnawareImeSubtypes);
            }
            return new ControllerImpl(switchingAwareRotationList, switchingUnawareRotationList);
        }

        private ControllerImpl(DynamicRotationList switchingAwareRotationList, StaticRotationList switchingUnawareRotationList) {
            this.mSwitchingAwareRotationList = switchingAwareRotationList;
            this.mSwitchingUnawareRotationList = switchingUnawareRotationList;
        }

        public ImeSubtypeListItem getNextInputMethod(boolean onlyCurrentIme, InputMethodInfo imi, InputMethodSubtype subtype, boolean forward) {
            if (imi == null) {
                return null;
            }
            if (imi.supportsSwitchingToNextInputMethod()) {
                return this.mSwitchingAwareRotationList.getNextInputMethodLocked(onlyCurrentIme, imi, subtype, forward);
            }
            return this.mSwitchingUnawareRotationList.getNextInputMethodLocked(onlyCurrentIme, imi, subtype, forward);
        }

        public void onUserActionLocked(InputMethodInfo imi, InputMethodSubtype subtype) {
            if (imi != null && imi.supportsSwitchingToNextInputMethod()) {
                this.mSwitchingAwareRotationList.onUserAction(imi, subtype);
            }
        }

        private static List<ImeSubtypeListItem> filterImeSubtypeList(List<ImeSubtypeListItem> items, boolean supportsSwitchingToNextInputMethod) {
            ArrayList<ImeSubtypeListItem> result = new ArrayList<>();
            int ALL_ITEMS_COUNT = items.size();
            for (int i = 0; i < ALL_ITEMS_COUNT; i++) {
                ImeSubtypeListItem item = items.get(i);
                if (item.mImi.supportsSwitchingToNextInputMethod() == supportsSwitchingToNextInputMethod) {
                    result.add(item);
                }
            }
            return result;
        }

        protected void dump(Printer pw) {
            pw.println("    mSwitchingAwareRotationList:");
            this.mSwitchingAwareRotationList.dump(pw, "      ");
            pw.println("    mSwitchingUnawareRotationList:");
            this.mSwitchingUnawareRotationList.dump(pw, "      ");
        }
    }

    private InputMethodSubtypeSwitchingController(InputMethodUtils.InputMethodSettings settings, Context context) {
        this.mSettings = settings;
        resetCircularListLocked(context);
    }

    public static InputMethodSubtypeSwitchingController createInstanceLocked(InputMethodUtils.InputMethodSettings settings, Context context) {
        return new InputMethodSubtypeSwitchingController(settings, context);
    }

    public void onUserActionLocked(InputMethodInfo imi, InputMethodSubtype subtype) {
        if (this.mController == null) {
            return;
        }
        this.mController.onUserActionLocked(imi, subtype);
    }

    public void resetCircularListLocked(Context context) {
        this.mSubtypeList = new InputMethodAndSubtypeList(context, this.mSettings);
        this.mController = ControllerImpl.createFrom(this.mController, this.mSubtypeList.getSortedInputMethodAndSubtypeList(false, false));
    }

    public ImeSubtypeListItem getNextInputMethodLocked(boolean onlyCurrentIme, InputMethodInfo imi, InputMethodSubtype subtype, boolean forward) {
        if (this.mController == null) {
            return null;
        }
        return this.mController.getNextInputMethod(onlyCurrentIme, imi, subtype, forward);
    }

    public List<ImeSubtypeListItem> getSortedInputMethodAndSubtypeListLocked(boolean includingAuxiliarySubtypes, boolean isScreenLocked) {
        return this.mSubtypeList.getSortedInputMethodAndSubtypeList(includingAuxiliarySubtypes, isScreenLocked);
    }

    public void dump(Printer pw) {
        if (this.mController != null) {
            this.mController.dump(pw);
        } else {
            pw.println("    mController=null");
        }
    }
}
