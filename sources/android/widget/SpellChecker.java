package android.widget;

import android.text.Editable;
import android.text.Selection;
import android.text.TextUtils;
import android.text.method.WordIterator;
import android.text.style.SpellCheckSpan;
import android.text.style.SuggestionSpan;
import android.util.Log;
import android.util.LruCache;
import android.view.textservice.SentenceSuggestionsInfo;
import android.view.textservice.SpellCheckerSession;
import android.view.textservice.SuggestionsInfo;
import android.view.textservice.TextInfo;
import android.view.textservice.TextServicesManager;
import com.android.internal.util.ArrayUtils;
import com.android.internal.util.GrowingArrayUtils;
import java.util.Locale;

/* loaded from: classes3.dex */
public class SpellChecker implements SpellCheckerSession.SpellCheckerSessionListener {
    public static final int AVERAGE_WORD_LENGTH = 7;
    private static final boolean DBG = false;
    public static final int MAX_NUMBER_OF_WORDS = 50;
    private static final int MIN_SENTENCE_LENGTH = 50;
    private static final int SPELL_PAUSE_DURATION = 400;
    private static final int SUGGESTION_SPAN_CACHE_SIZE = 10;
    private static final String TAG = SpellChecker.class.getSimpleName();
    private static final int USE_SPAN_RANGE = -1;
    public static final int WORD_ITERATOR_INTERVAL = 350;
    final int mCookie;
    private Locale mCurrentLocale;
    private boolean mIsSentenceSpellCheckSupported;
    private int mLength;
    SpellCheckerSession mSpellCheckerSession;
    private Runnable mSpellRunnable;
    private TextServicesManager mTextServicesManager;
    private final TextView mTextView;
    private WordIterator mWordIterator;
    private SpellParser[] mSpellParsers = new SpellParser[0];
    private int mSpanSequenceCounter = 0;
    private final LruCache<Long, SuggestionSpan> mSuggestionSpanCache = new LruCache<>(10);
    private int[] mIds = ArrayUtils.newUnpaddedIntArray(1);
    private SpellCheckSpan[] mSpellCheckSpans = new SpellCheckSpan[this.mIds.length];

    public SpellChecker(TextView textView) {
        this.mTextView = textView;
        setLocale(this.mTextView.getSpellCheckerLocale());
        this.mCookie = hashCode();
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public void resetSession() {
        closeSession();
        this.mTextServicesManager = this.mTextView.getTextServicesManagerForUser();
        if (this.mCurrentLocale == null || this.mTextServicesManager == null || this.mTextView.length() == 0 || !this.mTextServicesManager.isSpellCheckerEnabled() || this.mTextServicesManager.getCurrentSpellCheckerSubtype(true) == null) {
            this.mSpellCheckerSession = null;
        } else {
            this.mSpellCheckerSession = this.mTextServicesManager.newSpellCheckerSession(null, this.mCurrentLocale, this, false);
            this.mIsSentenceSpellCheckSupported = true;
        }
        for (int i = 0; i < this.mLength; i++) {
            this.mIds[i] = -1;
        }
        this.mLength = 0;
        TextView textView = this.mTextView;
        textView.removeMisspelledSpans((Editable) textView.getText());
        this.mSuggestionSpanCache.evictAll();
    }

    private void setLocale(Locale locale) {
        this.mCurrentLocale = locale;
        resetSession();
        if (locale != null) {
            this.mWordIterator = new WordIterator(locale);
        }
        this.mTextView.onLocaleChanged();
    }

    private boolean isSessionActive() {
        return this.mSpellCheckerSession != null;
    }

    public void closeSession() {
        SpellCheckerSession spellCheckerSession = this.mSpellCheckerSession;
        if (spellCheckerSession != null) {
            spellCheckerSession.close();
        }
        int length = this.mSpellParsers.length;
        for (int i = 0; i < length; i++) {
            this.mSpellParsers[i].stop();
        }
        Runnable runnable = this.mSpellRunnable;
        if (runnable != null) {
            this.mTextView.removeCallbacks(runnable);
        }
    }

    private int nextSpellCheckSpanIndex() {
        int i = 0;
        while (true) {
            int i2 = this.mLength;
            if (i < i2) {
                if (this.mIds[i] < 0) {
                    return i;
                }
                i++;
            } else {
                this.mIds = GrowingArrayUtils.append(this.mIds, i2, 0);
                this.mSpellCheckSpans = (SpellCheckSpan[]) GrowingArrayUtils.append(this.mSpellCheckSpans, this.mLength, new SpellCheckSpan());
                this.mLength++;
                return this.mLength - 1;
            }
        }
    }

    /* JADX INFO: Access modifiers changed from: private */
    public void addSpellCheckSpan(Editable editable, int start, int end) {
        int index = nextSpellCheckSpanIndex();
        SpellCheckSpan spellCheckSpan = this.mSpellCheckSpans[index];
        editable.setSpan(spellCheckSpan, start, end, 33);
        spellCheckSpan.setSpellCheckInProgress(false);
        int[] iArr = this.mIds;
        int i = this.mSpanSequenceCounter;
        this.mSpanSequenceCounter = i + 1;
        iArr[index] = i;
    }

    public void onSpellCheckSpanRemoved(SpellCheckSpan spellCheckSpan) {
        for (int i = 0; i < this.mLength; i++) {
            if (this.mSpellCheckSpans[i] == spellCheckSpan) {
                this.mIds[i] = -1;
                return;
            }
        }
    }

    public void onSelectionChanged() {
        spellCheck();
    }

    public void spellCheck(int start, int end) {
        Locale locale;
        Locale locale2 = this.mTextView.getSpellCheckerLocale();
        boolean isSessionActive = isSessionActive();
        if (locale2 == null || (locale = this.mCurrentLocale) == null || !locale.equals(locale2)) {
            setLocale(locale2);
            start = 0;
            end = this.mTextView.getText().length();
        } else {
            TextServicesManager textServicesManager = this.mTextServicesManager;
            boolean spellCheckerActivated = textServicesManager != null && textServicesManager.isSpellCheckerEnabled();
            if (isSessionActive != spellCheckerActivated) {
                resetSession();
            }
        }
        if (isSessionActive) {
            int length = this.mSpellParsers.length;
            for (int i = 0; i < length; i++) {
                SpellParser spellParser = this.mSpellParsers[i];
                if (spellParser.isFinished()) {
                    spellParser.parse(start, end);
                    return;
                }
            }
            int i2 = length + 1;
            SpellParser[] newSpellParsers = new SpellParser[i2];
            System.arraycopy(this.mSpellParsers, 0, newSpellParsers, 0, length);
            this.mSpellParsers = newSpellParsers;
            SpellParser spellParser2 = new SpellParser();
            this.mSpellParsers[length] = spellParser2;
            spellParser2.parse(start, end);
        }
    }

    /* JADX INFO: Access modifiers changed from: private */
    public void spellCheck() {
        boolean isEditing;
        if (this.mSpellCheckerSession == null) {
            return;
        }
        Editable editable = (Editable) this.mTextView.getText();
        int selectionStart = Selection.getSelectionStart(editable);
        int selectionEnd = Selection.getSelectionEnd(editable);
        TextInfo[] textInfos = new TextInfo[this.mLength];
        int textInfosCount = 0;
        int i = 0;
        while (true) {
            int textInfosCount2 = this.mLength;
            boolean z = false;
            if (i >= textInfosCount2) {
                break;
            }
            SpellCheckSpan spellCheckSpan = this.mSpellCheckSpans[i];
            if (this.mIds[i] >= 0 && !spellCheckSpan.isSpellCheckInProgress()) {
                int start = editable.getSpanStart(spellCheckSpan);
                int end = editable.getSpanEnd(spellCheckSpan);
                if (selectionStart == end + 1 && WordIterator.isMidWordPunctuation(this.mCurrentLocale, Character.codePointBefore(editable, end + 1))) {
                    isEditing = false;
                } else {
                    boolean isEditing2 = this.mIsSentenceSpellCheckSupported;
                    if (isEditing2) {
                        if (selectionEnd <= start || selectionStart > end) {
                            z = true;
                        }
                        isEditing = z;
                    } else {
                        if (selectionEnd < start || selectionStart > end) {
                            z = true;
                        }
                        isEditing = z;
                    }
                }
                if (start >= 0 && end > start && isEditing) {
                    spellCheckSpan.setSpellCheckInProgress(true);
                    TextInfo textInfo = new TextInfo(editable, start, end, this.mCookie, this.mIds[i]);
                    textInfos[textInfosCount] = textInfo;
                    textInfosCount++;
                }
            }
            i++;
        }
        if (textInfosCount > 0) {
            if (textInfosCount < textInfos.length) {
                TextInfo[] textInfosCopy = new TextInfo[textInfosCount];
                System.arraycopy(textInfos, 0, textInfosCopy, 0, textInfosCount);
                textInfos = textInfosCopy;
            }
            if (this.mIsSentenceSpellCheckSupported) {
                this.mSpellCheckerSession.getSentenceSuggestions(textInfos, 5);
            } else {
                this.mSpellCheckerSession.getSuggestions(textInfos, 5, false);
            }
        }
    }

    private SpellCheckSpan onGetSuggestionsInternal(SuggestionsInfo suggestionsInfo, int offset, int length) {
        int start;
        int end;
        if (suggestionsInfo == null || suggestionsInfo.getCookie() != this.mCookie) {
            return null;
        }
        Editable editable = (Editable) this.mTextView.getText();
        int sequenceNumber = suggestionsInfo.getSequence();
        for (int k = 0; k < this.mLength; k++) {
            if (sequenceNumber == this.mIds[k]) {
                int attributes = suggestionsInfo.getSuggestionsAttributes();
                boolean isInDictionary = (attributes & 1) > 0;
                boolean looksLikeTypo = (attributes & 2) > 0;
                SpellCheckSpan spellCheckSpan = this.mSpellCheckSpans[k];
                if (!isInDictionary && looksLikeTypo) {
                    createMisspelledSuggestionSpan(editable, suggestionsInfo, spellCheckSpan, offset, length);
                } else if (this.mIsSentenceSpellCheckSupported) {
                    int spellCheckSpanStart = editable.getSpanStart(spellCheckSpan);
                    int spellCheckSpanEnd = editable.getSpanEnd(spellCheckSpan);
                    if (offset != -1 && length != -1) {
                        start = spellCheckSpanStart + offset;
                        end = start + length;
                    } else {
                        start = spellCheckSpanStart;
                        end = spellCheckSpanEnd;
                    }
                    if (spellCheckSpanStart >= 0 && spellCheckSpanEnd > spellCheckSpanStart && end > start) {
                        Long key = Long.valueOf(TextUtils.packRangeInLong(start, end));
                        SuggestionSpan tempSuggestionSpan = this.mSuggestionSpanCache.get(key);
                        if (tempSuggestionSpan != null) {
                            editable.removeSpan(tempSuggestionSpan);
                            this.mSuggestionSpanCache.remove(key);
                        }
                    }
                }
                return spellCheckSpan;
            }
        }
        return null;
    }

    @Override // android.view.textservice.SpellCheckerSession.SpellCheckerSessionListener
    public void onGetSuggestions(SuggestionsInfo[] results) {
        Editable editable = (Editable) this.mTextView.getText();
        for (SuggestionsInfo suggestionsInfo : results) {
            SpellCheckSpan spellCheckSpan = onGetSuggestionsInternal(suggestionsInfo, -1, -1);
            if (spellCheckSpan != null) {
                editable.removeSpan(spellCheckSpan);
            }
        }
        scheduleNewSpellCheck();
    }

    @Override // android.view.textservice.SpellCheckerSession.SpellCheckerSessionListener
    public void onGetSentenceSuggestions(SentenceSuggestionsInfo[] results) {
        Editable editable = (Editable) this.mTextView.getText();
        for (SentenceSuggestionsInfo ssi : results) {
            if (ssi != null) {
                SpellCheckSpan spellCheckSpan = null;
                for (int j = 0; j < ssi.getSuggestionsCount(); j++) {
                    SuggestionsInfo suggestionsInfo = ssi.getSuggestionsInfoAt(j);
                    if (suggestionsInfo != null) {
                        int offset = ssi.getOffsetAt(j);
                        int length = ssi.getLengthAt(j);
                        SpellCheckSpan scs = onGetSuggestionsInternal(suggestionsInfo, offset, length);
                        if (spellCheckSpan == null && scs != null) {
                            spellCheckSpan = scs;
                        }
                    }
                }
                if (spellCheckSpan != null) {
                    editable.removeSpan(spellCheckSpan);
                }
            }
        }
        scheduleNewSpellCheck();
    }

    private void scheduleNewSpellCheck() {
        Runnable runnable = this.mSpellRunnable;
        if (runnable == null) {
            this.mSpellRunnable = new Runnable() { // from class: android.widget.SpellChecker.1
                @Override // java.lang.Runnable
                public void run() {
                    int length = SpellChecker.this.mSpellParsers.length;
                    for (int i = 0; i < length; i++) {
                        SpellParser spellParser = SpellChecker.this.mSpellParsers[i];
                        if (!spellParser.isFinished()) {
                            spellParser.parse();
                            return;
                        }
                    }
                }
            };
        } else {
            this.mTextView.removeCallbacks(runnable);
        }
        this.mTextView.postDelayed(this.mSpellRunnable, 400L);
    }

    private void createMisspelledSuggestionSpan(Editable editable, SuggestionsInfo suggestionsInfo, SpellCheckSpan spellCheckSpan, int offset, int length) {
        int start;
        int end;
        String[] suggestions;
        int spellCheckSpanStart = editable.getSpanStart(spellCheckSpan);
        int spellCheckSpanEnd = editable.getSpanEnd(spellCheckSpan);
        if (spellCheckSpanStart < 0 || spellCheckSpanEnd <= spellCheckSpanStart) {
            return;
        }
        if (offset != -1 && length != -1) {
            start = spellCheckSpanStart + offset;
            end = start + length;
        } else {
            start = spellCheckSpanStart;
            end = spellCheckSpanEnd;
        }
        int suggestionsCount = suggestionsInfo.getSuggestionsCount();
        if (suggestionsCount > 0) {
            suggestions = new String[suggestionsCount];
            for (int i = 0; i < suggestionsCount; i++) {
                suggestions[i] = suggestionsInfo.getSuggestionAt(i);
            }
        } else {
            suggestions = (String[]) ArrayUtils.emptyArray(String.class);
        }
        SuggestionSpan suggestionSpan = new SuggestionSpan(this.mTextView.getContext(), suggestions, 3);
        if (this.mIsSentenceSpellCheckSupported) {
            Long key = Long.valueOf(TextUtils.packRangeInLong(start, end));
            SuggestionSpan tempSuggestionSpan = this.mSuggestionSpanCache.get(key);
            if (tempSuggestionSpan != null) {
                editable.removeSpan(tempSuggestionSpan);
            }
            this.mSuggestionSpanCache.put(key, suggestionSpan);
        }
        editable.setSpan(suggestionSpan, start, end, 33);
        this.mTextView.invalidateRegion(start, end, false);
    }

    /* JADX INFO: Access modifiers changed from: private */
    /* loaded from: classes3.dex */
    public class SpellParser {
        private Object mRange;

        private SpellParser() {
            this.mRange = new Object();
        }

        public void parse(int start, int end) {
            int parseEnd;
            int max = SpellChecker.this.mTextView.length();
            if (end > max) {
                String str = SpellChecker.TAG;
                Log.w(str, "Parse invalid region, from " + start + " to " + end);
                parseEnd = max;
            } else {
                parseEnd = end;
            }
            if (parseEnd > start) {
                setRangeSpan((Editable) SpellChecker.this.mTextView.getText(), start, parseEnd);
                parse();
            }
        }

        public boolean isFinished() {
            return ((Editable) SpellChecker.this.mTextView.getText()).getSpanStart(this.mRange) < 0;
        }

        public void stop() {
            removeRangeSpan((Editable) SpellChecker.this.mTextView.getText());
        }

        private void setRangeSpan(Editable editable, int start, int end) {
            editable.setSpan(this.mRange, start, end, 33);
        }

        private void removeRangeSpan(Editable editable) {
            editable.removeSpan(this.mRange);
        }

        public void parse() {
            int start;
            int wordEnd;
            int wordStart;
            int wordEnd2;
            Editable editable = (Editable) SpellChecker.this.mTextView.getText();
            if (SpellChecker.this.mIsSentenceSpellCheckSupported) {
                start = Math.max(0, editable.getSpanStart(this.mRange) - 50);
            } else {
                start = editable.getSpanStart(this.mRange);
            }
            int end = editable.getSpanEnd(this.mRange);
            int wordIteratorWindowEnd = Math.min(end, start + 350);
            SpellChecker.this.mWordIterator.setCharSequence(editable, start, wordIteratorWindowEnd);
            int wordStart2 = SpellChecker.this.mWordIterator.preceding(start);
            if (wordStart2 == -1) {
                wordEnd = SpellChecker.this.mWordIterator.following(start);
                if (wordEnd != -1) {
                    wordStart2 = SpellChecker.this.mWordIterator.getBeginning(wordEnd);
                }
            } else {
                wordEnd = SpellChecker.this.mWordIterator.getEnd(wordStart2);
            }
            if (wordEnd == -1) {
                removeRangeSpan(editable);
                return;
            }
            SpellCheckSpan[] spellCheckSpans = (SpellCheckSpan[]) editable.getSpans(start - 1, end + 1, SpellCheckSpan.class);
            SuggestionSpan[] suggestionSpans = (SuggestionSpan[]) editable.getSpans(start - 1, end + 1, SuggestionSpan.class);
            int wordCount = 0;
            boolean scheduleOtherSpellCheck = false;
            if (!SpellChecker.this.mIsSentenceSpellCheckSupported) {
                wordStart = wordStart2;
                while (true) {
                    if (wordStart <= end) {
                        if (wordEnd >= start && wordEnd > wordStart) {
                            if (wordCount >= 50) {
                                scheduleOtherSpellCheck = true;
                                break;
                            }
                            if (wordStart < start && wordEnd > start) {
                                removeSpansAt(editable, start, spellCheckSpans);
                                removeSpansAt(editable, start, suggestionSpans);
                            }
                            if (wordStart < end && wordEnd > end) {
                                removeSpansAt(editable, end, spellCheckSpans);
                                removeSpansAt(editable, end, suggestionSpans);
                            }
                            boolean createSpellCheckSpan = true;
                            if (wordEnd == start) {
                                int i = 0;
                                while (true) {
                                    if (i >= spellCheckSpans.length) {
                                        break;
                                    } else if (editable.getSpanEnd(spellCheckSpans[i]) != start) {
                                        i++;
                                    } else {
                                        createSpellCheckSpan = false;
                                        break;
                                    }
                                }
                            }
                            if (wordStart == end) {
                                int i2 = 0;
                                while (true) {
                                    if (i2 >= spellCheckSpans.length) {
                                        break;
                                    } else if (editable.getSpanStart(spellCheckSpans[i2]) != end) {
                                        i2++;
                                    } else {
                                        createSpellCheckSpan = false;
                                        break;
                                    }
                                }
                            }
                            if (createSpellCheckSpan) {
                                SpellChecker.this.addSpellCheckSpan(editable, wordStart, wordEnd);
                            }
                            wordCount++;
                        }
                        int originalWordEnd = wordEnd;
                        int wordEnd3 = SpellChecker.this.mWordIterator.following(wordEnd);
                        if (wordIteratorWindowEnd < end && (wordEnd3 == -1 || wordEnd3 >= wordIteratorWindowEnd)) {
                            wordIteratorWindowEnd = Math.min(end, originalWordEnd + 350);
                            SpellChecker.this.mWordIterator.setCharSequence(editable, originalWordEnd, wordIteratorWindowEnd);
                            int wordEnd4 = SpellChecker.this.mWordIterator.following(originalWordEnd);
                            wordEnd = wordEnd4;
                        } else {
                            wordEnd = wordEnd3;
                        }
                        if (wordEnd == -1) {
                            break;
                        }
                        wordStart = SpellChecker.this.mWordIterator.getBeginning(wordEnd);
                        if (wordStart == -1) {
                            break;
                        }
                    } else {
                        break;
                    }
                }
            } else {
                if (wordIteratorWindowEnd < end) {
                    scheduleOtherSpellCheck = true;
                }
                int spellCheckEnd = SpellChecker.this.mWordIterator.preceding(wordIteratorWindowEnd);
                boolean correct = spellCheckEnd != -1;
                if (correct) {
                    spellCheckEnd = SpellChecker.this.mWordIterator.getEnd(spellCheckEnd);
                    correct = spellCheckEnd != -1;
                }
                if (!correct) {
                    removeRangeSpan(editable);
                    return;
                }
                int spellCheckStart = wordStart2;
                boolean createSpellCheckSpan2 = true;
                int spellCheckEnd2 = spellCheckEnd;
                int spellCheckEnd3 = 0;
                while (true) {
                    int wordIteratorWindowEnd2 = wordIteratorWindowEnd;
                    if (spellCheckEnd3 >= SpellChecker.this.mLength) {
                        wordEnd2 = wordEnd;
                        break;
                    }
                    SpellCheckSpan spellCheckSpan = SpellChecker.this.mSpellCheckSpans[spellCheckEnd3];
                    int wordStart3 = wordStart2;
                    if (SpellChecker.this.mIds[spellCheckEnd3] < 0) {
                        wordEnd2 = wordEnd;
                    } else if (spellCheckSpan.isSpellCheckInProgress()) {
                        wordEnd2 = wordEnd;
                    } else {
                        int spanStart = editable.getSpanStart(spellCheckSpan);
                        wordEnd2 = wordEnd;
                        int spanEnd = editable.getSpanEnd(spellCheckSpan);
                        if (spanEnd >= spellCheckStart && spellCheckEnd2 >= spanStart) {
                            if (spanStart <= spellCheckStart && spellCheckEnd2 <= spanEnd) {
                                createSpellCheckSpan2 = false;
                                break;
                            }
                            editable.removeSpan(spellCheckSpan);
                            spellCheckStart = Math.min(spanStart, spellCheckStart);
                            spellCheckEnd2 = Math.max(spanEnd, spellCheckEnd2);
                        }
                    }
                    spellCheckEnd3++;
                    wordIteratorWindowEnd = wordIteratorWindowEnd2;
                    wordStart2 = wordStart3;
                    wordEnd = wordEnd2;
                }
                if (spellCheckEnd2 >= start) {
                    if (spellCheckEnd2 <= spellCheckStart) {
                        Log.w(SpellChecker.TAG, "Trying to spellcheck invalid region, from " + start + " to " + end);
                    } else if (createSpellCheckSpan2) {
                        SpellChecker.this.addSpellCheckSpan(editable, spellCheckStart, spellCheckEnd2);
                    }
                }
                wordStart = spellCheckEnd2;
            }
            if (scheduleOtherSpellCheck && wordStart != -1 && wordStart <= end) {
                setRangeSpan(editable, wordStart, end);
            } else {
                removeRangeSpan(editable);
            }
            SpellChecker.this.spellCheck();
        }

        private <T> void removeSpansAt(Editable editable, int offset, T[] spans) {
            for (T span : spans) {
                int start = editable.getSpanStart(span);
                if (start <= offset) {
                    int end = editable.getSpanEnd(span);
                    if (end >= offset) {
                        editable.removeSpan(span);
                    }
                }
            }
        }
    }

    public static boolean haveWordBoundariesChanged(Editable editable, int start, int end, int spanStart, int spanEnd) {
        if (spanEnd != start && spanStart != end) {
            return true;
        }
        if (spanEnd == start && start < editable.length()) {
            int codePoint = Character.codePointAt(editable, start);
            boolean haveWordBoundariesChanged = Character.isLetterOrDigit(codePoint);
            return haveWordBoundariesChanged;
        } else if (spanStart == end && end > 0) {
            int codePoint2 = Character.codePointBefore(editable, end);
            boolean haveWordBoundariesChanged2 = Character.isLetterOrDigit(codePoint2);
            return haveWordBoundariesChanged2;
        } else {
            return false;
        }
    }
}
