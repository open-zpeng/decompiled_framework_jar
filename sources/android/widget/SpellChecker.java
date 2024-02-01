package android.widget;

import android.content.Context;
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

    public synchronized SpellChecker(TextView textView) {
        this.mTextView = textView;
        setLocale(this.mTextView.getSpellCheckerLocale());
        this.mCookie = hashCode();
    }

    private synchronized void resetSession() {
        closeSession();
        this.mTextServicesManager = (TextServicesManager) this.mTextView.getContext().getSystemService(Context.TEXT_SERVICES_MANAGER_SERVICE);
        if (!this.mTextServicesManager.isSpellCheckerEnabled() || this.mCurrentLocale == null || this.mTextServicesManager.getCurrentSpellCheckerSubtype(true) == null) {
            this.mSpellCheckerSession = null;
        } else {
            this.mSpellCheckerSession = this.mTextServicesManager.newSpellCheckerSession(null, this.mCurrentLocale, this, false);
            this.mIsSentenceSpellCheckSupported = true;
        }
        for (int i = 0; i < this.mLength; i++) {
            this.mIds[i] = -1;
        }
        this.mLength = 0;
        this.mTextView.removeMisspelledSpans((Editable) this.mTextView.getText());
        this.mSuggestionSpanCache.evictAll();
    }

    private synchronized void setLocale(Locale locale) {
        this.mCurrentLocale = locale;
        resetSession();
        if (locale != null) {
            this.mWordIterator = new WordIterator(locale);
        }
        this.mTextView.onLocaleChanged();
    }

    private synchronized boolean isSessionActive() {
        return this.mSpellCheckerSession != null;
    }

    public synchronized void closeSession() {
        if (this.mSpellCheckerSession != null) {
            this.mSpellCheckerSession.close();
        }
        int length = this.mSpellParsers.length;
        for (int i = 0; i < length; i++) {
            this.mSpellParsers[i].stop();
        }
        if (this.mSpellRunnable != null) {
            this.mTextView.removeCallbacks(this.mSpellRunnable);
        }
    }

    private synchronized int nextSpellCheckSpanIndex() {
        for (int i = 0; i < this.mLength; i++) {
            if (this.mIds[i] < 0) {
                return i;
            }
        }
        this.mIds = GrowingArrayUtils.append(this.mIds, this.mLength, 0);
        this.mSpellCheckSpans = (SpellCheckSpan[]) GrowingArrayUtils.append(this.mSpellCheckSpans, this.mLength, new SpellCheckSpan());
        this.mLength++;
        return this.mLength - 1;
    }

    /* JADX INFO: Access modifiers changed from: private */
    public synchronized void addSpellCheckSpan(Editable editable, int start, int end) {
        int index = nextSpellCheckSpanIndex();
        SpellCheckSpan spellCheckSpan = this.mSpellCheckSpans[index];
        editable.setSpan(spellCheckSpan, start, end, 33);
        spellCheckSpan.setSpellCheckInProgress(false);
        int[] iArr = this.mIds;
        int i = this.mSpanSequenceCounter;
        this.mSpanSequenceCounter = i + 1;
        iArr[index] = i;
    }

    public synchronized void onSpellCheckSpanRemoved(SpellCheckSpan spellCheckSpan) {
        for (int i = 0; i < this.mLength; i++) {
            if (this.mSpellCheckSpans[i] == spellCheckSpan) {
                this.mIds[i] = -1;
                return;
            }
        }
    }

    public synchronized void onSelectionChanged() {
        spellCheck();
    }

    public synchronized void spellCheck(int start, int end) {
        Locale locale = this.mTextView.getSpellCheckerLocale();
        boolean isSessionActive = isSessionActive();
        if (locale == null || this.mCurrentLocale == null || !this.mCurrentLocale.equals(locale)) {
            setLocale(locale);
            start = 0;
            end = this.mTextView.getText().length();
        } else {
            boolean spellCheckerActivated = this.mTextServicesManager.isSpellCheckerEnabled();
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
    public synchronized void spellCheck() {
        boolean isEditing;
        if (this.mSpellCheckerSession == null) {
            return;
        }
        Editable editable = (Editable) this.mTextView.getText();
        int selectionStart = Selection.getSelectionStart(editable);
        int selectionEnd = Selection.getSelectionEnd(editable);
        TextInfo[] textInfos = new TextInfo[this.mLength];
        int textInfosCount = 0;
        int textInfosCount2 = 0;
        while (true) {
            int i = textInfosCount2;
            if (i >= this.mLength) {
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
                        isEditing = selectionEnd <= start || selectionStart > end;
                    } else {
                        isEditing = selectionEnd < start || selectionStart > end;
                    }
                }
                boolean isEditing3 = isEditing;
                if (start >= 0 && end > start && isEditing3) {
                    spellCheckSpan.setSpellCheckInProgress(true);
                    TextInfo textInfo = new TextInfo(editable, start, end, this.mCookie, this.mIds[i]);
                    textInfos[textInfosCount] = textInfo;
                    textInfosCount++;
                }
            }
            textInfosCount2 = i + 1;
        }
        if (textInfosCount > 0) {
            if (textInfosCount < textInfos.length) {
                TextInfo[] textInfosCopy = new TextInfo[textInfosCount];
                System.arraycopy(textInfos, 0, textInfosCopy, 0, textInfosCount);
                textInfos = textInfosCopy;
            }
            if (!this.mIsSentenceSpellCheckSupported) {
                this.mSpellCheckerSession.getSuggestions(textInfos, 5, false);
            } else {
                this.mSpellCheckerSession.getSentenceSuggestions(textInfos, 5);
            }
        }
    }

    private synchronized SpellCheckSpan onGetSuggestionsInternal(SuggestionsInfo suggestionsInfo, int offset, int length) {
        int start;
        int end;
        Long key;
        SuggestionSpan tempSuggestionSpan;
        if (suggestionsInfo == null || suggestionsInfo.getCookie() != this.mCookie) {
            return null;
        }
        Editable editable = (Editable) this.mTextView.getText();
        int sequenceNumber = suggestionsInfo.getSequence();
        int k = 0;
        while (true) {
            int k2 = k;
            int k3 = this.mLength;
            if (k2 >= k3) {
                return null;
            }
            if (sequenceNumber != this.mIds[k2]) {
                k = k2 + 1;
            } else {
                int attributes = suggestionsInfo.getSuggestionsAttributes();
                boolean isInDictionary = (attributes & 1) > 0;
                boolean looksLikeTypo = (attributes & 2) > 0;
                SpellCheckSpan spellCheckSpan = this.mSpellCheckSpans[k2];
                if (!isInDictionary && looksLikeTypo) {
                    createMisspelledSuggestionSpan(editable, suggestionsInfo, spellCheckSpan, offset, length);
                    return spellCheckSpan;
                } else if (!this.mIsSentenceSpellCheckSupported) {
                    return spellCheckSpan;
                } else {
                    int spellCheckSpanStart = editable.getSpanStart(spellCheckSpan);
                    int spellCheckSpanEnd = editable.getSpanEnd(spellCheckSpan);
                    if (offset != -1 && length != -1) {
                        start = spellCheckSpanStart + offset;
                        end = start + length;
                    } else {
                        start = spellCheckSpanStart;
                        end = spellCheckSpanEnd;
                    }
                    if (spellCheckSpanStart < 0 || spellCheckSpanEnd <= spellCheckSpanStart || end <= start || (tempSuggestionSpan = this.mSuggestionSpanCache.get((key = Long.valueOf(TextUtils.packRangeInLong(start, end))))) == null) {
                        return spellCheckSpan;
                    }
                    editable.removeSpan(tempSuggestionSpan);
                    this.mSuggestionSpanCache.remove(key);
                    return spellCheckSpan;
                }
            }
        }
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

    private synchronized void scheduleNewSpellCheck() {
        if (this.mSpellRunnable == null) {
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
            this.mTextView.removeCallbacks(this.mSpellRunnable);
        }
        this.mTextView.postDelayed(this.mSpellRunnable, 400L);
    }

    private synchronized void createMisspelledSuggestionSpan(Editable editable, SuggestionsInfo suggestionsInfo, SpellCheckSpan spellCheckSpan, int offset, int length) {
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

        public synchronized void parse(int start, int end) {
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

        public synchronized boolean isFinished() {
            return ((Editable) SpellChecker.this.mTextView.getText()).getSpanStart(this.mRange) < 0;
        }

        public synchronized void stop() {
            removeRangeSpan((Editable) SpellChecker.this.mTextView.getText());
        }

        private synchronized void setRangeSpan(Editable editable, int start, int end) {
            editable.setSpan(this.mRange, start, end, 33);
        }

        private synchronized void removeRangeSpan(Editable editable) {
            editable.removeSpan(this.mRange);
        }

        public synchronized void parse() {
            int start;
            int wordEnd;
            int wordStart;
            int i;
            int wordEnd2;
            Editable editable = (Editable) SpellChecker.this.mTextView.getText();
            if (SpellChecker.this.mIsSentenceSpellCheckSupported) {
                start = Math.max(0, editable.getSpanStart(this.mRange) - 50);
            } else {
                start = editable.getSpanStart(this.mRange);
            }
            int end = editable.getSpanEnd(this.mRange);
            int i2 = Math.min(end, start + 350);
            SpellChecker.this.mWordIterator.setCharSequence(editable, start, i2);
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
            if (SpellChecker.this.mIsSentenceSpellCheckSupported) {
                if (i2 < end) {
                    scheduleOtherSpellCheck = true;
                }
                int spellCheckEnd = SpellChecker.this.mWordIterator.preceding(i2);
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
                boolean createSpellCheckSpan = true;
                int i3 = 0;
                while (true) {
                    int i4 = i3;
                    int wordIteratorWindowEnd = i2;
                    if (i4 >= SpellChecker.this.mLength) {
                        wordEnd2 = wordEnd;
                        break;
                    }
                    SpellCheckSpan spellCheckSpan = SpellChecker.this.mSpellCheckSpans[i4];
                    int wordStart3 = wordStart2;
                    if (SpellChecker.this.mIds[i4] < 0) {
                        wordEnd2 = wordEnd;
                    } else if (spellCheckSpan.isSpellCheckInProgress()) {
                        wordEnd2 = wordEnd;
                    } else {
                        int spanStart = editable.getSpanStart(spellCheckSpan);
                        wordEnd2 = wordEnd;
                        int wordEnd3 = editable.getSpanEnd(spellCheckSpan);
                        if (wordEnd3 >= spellCheckStart && spellCheckEnd >= spanStart) {
                            if (spanStart <= spellCheckStart && spellCheckEnd <= wordEnd3) {
                                createSpellCheckSpan = false;
                                break;
                            }
                            editable.removeSpan(spellCheckSpan);
                            spellCheckStart = Math.min(spanStart, spellCheckStart);
                            spellCheckEnd = Math.max(wordEnd3, spellCheckEnd);
                        }
                    }
                    i3 = i4 + 1;
                    i2 = wordIteratorWindowEnd;
                    wordStart2 = wordStart3;
                    wordEnd = wordEnd2;
                }
                if (spellCheckEnd >= start) {
                    if (spellCheckEnd <= spellCheckStart) {
                        String str = SpellChecker.TAG;
                        Log.w(str, "Trying to spellcheck invalid region, from " + start + " to " + end);
                    } else if (createSpellCheckSpan) {
                        SpellChecker.this.addSpellCheckSpan(editable, spellCheckStart, spellCheckEnd);
                    }
                }
                wordStart = spellCheckEnd;
            } else {
                wordStart = wordStart2;
                do {
                    if (wordStart <= end) {
                        if (wordEnd >= start && wordEnd > wordStart) {
                            if (wordCount >= 50) {
                                scheduleOtherSpellCheck = true;
                            } else {
                                if (wordStart < start && wordEnd > start) {
                                    removeSpansAt(editable, start, spellCheckSpans);
                                    removeSpansAt(editable, start, suggestionSpans);
                                }
                                if (wordStart < end && wordEnd > end) {
                                    removeSpansAt(editable, end, spellCheckSpans);
                                    removeSpansAt(editable, end, suggestionSpans);
                                }
                                boolean createSpellCheckSpan2 = true;
                                if (wordEnd == start) {
                                    int i5 = 0;
                                    while (true) {
                                        if (i5 >= spellCheckSpans.length) {
                                            break;
                                        }
                                        int spanEnd = editable.getSpanEnd(spellCheckSpans[i5]);
                                        if (spanEnd != start) {
                                            i5++;
                                        } else {
                                            createSpellCheckSpan2 = false;
                                            break;
                                        }
                                    }
                                }
                                if (wordStart == end) {
                                    int i6 = 0;
                                    while (true) {
                                        if (i6 >= spellCheckSpans.length) {
                                            break;
                                        } else if (editable.getSpanStart(spellCheckSpans[i6]) != end) {
                                            i6++;
                                        } else {
                                            createSpellCheckSpan2 = false;
                                            break;
                                        }
                                    }
                                }
                                if (createSpellCheckSpan2) {
                                    SpellChecker.this.addSpellCheckSpan(editable, wordStart, wordEnd);
                                }
                                wordCount++;
                            }
                        }
                        int originalWordEnd = wordEnd;
                        int wordEnd4 = SpellChecker.this.mWordIterator.following(wordEnd);
                        if (i2 < end && (wordEnd4 == -1 || wordEnd4 >= i2)) {
                            i2 = Math.min(end, originalWordEnd + 350);
                            SpellChecker.this.mWordIterator.setCharSequence(editable, originalWordEnd, i2);
                            wordEnd4 = SpellChecker.this.mWordIterator.following(originalWordEnd);
                        }
                        wordEnd = wordEnd4;
                        i = -1;
                        if (wordEnd == -1) {
                            break;
                        }
                        wordStart = SpellChecker.this.mWordIterator.getBeginning(wordEnd);
                    }
                } while (wordStart != -1);
                if (!scheduleOtherSpellCheck && wordStart != i && wordStart <= end) {
                    setRangeSpan(editable, wordStart, end);
                } else {
                    removeRangeSpan(editable);
                }
                SpellChecker.this.spellCheck();
            }
            i = -1;
            if (!scheduleOtherSpellCheck) {
            }
            removeRangeSpan(editable);
            SpellChecker.this.spellCheck();
        }

        private synchronized <T> void removeSpansAt(Editable editable, int offset, T[] spans) {
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

    public static synchronized boolean haveWordBoundariesChanged(Editable editable, int start, int end, int spanStart, int spanEnd) {
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
