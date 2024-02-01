package android.text.util;

import android.content.Context;
import android.telephony.PhoneNumberUtils;
import android.telephony.TelephonyManager;
import android.text.Spannable;
import android.text.SpannableString;
import android.text.method.LinkMovementMethod;
import android.text.method.MovementMethod;
import android.text.style.URLSpan;
import android.util.EventLog;
import android.util.Log;
import android.util.Patterns;
import android.view.textclassifier.TextClassifier;
import android.view.textclassifier.TextLinks;
import android.view.textclassifier.TextLinksParams;
import android.webkit.WebView;
import android.widget.TextView;
import com.android.i18n.phonenumbers.PhoneNumberMatch;
import com.android.i18n.phonenumbers.PhoneNumberUtil;
import com.android.internal.util.Preconditions;
import java.io.UnsupportedEncodingException;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.net.URLEncoder;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.Iterator;
import java.util.Locale;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.Executor;
import java.util.concurrent.Future;
import java.util.function.Consumer;
import java.util.function.Supplier;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import libcore.util.EmptyArray;
/* loaded from: classes2.dex */
public class Linkify {
    public static final int ALL = 15;
    public static final int EMAIL_ADDRESSES = 2;
    private static final String LOG_TAG = "Linkify";
    @Deprecated
    public static final int MAP_ADDRESSES = 8;
    public static final int PHONE_NUMBERS = 4;
    private static final int PHONE_NUMBER_MINIMUM_DIGITS = 5;
    public static final int WEB_URLS = 1;
    public static final MatchFilter sUrlMatchFilter = new MatchFilter() { // from class: android.text.util.Linkify.1
        @Override // android.text.util.Linkify.MatchFilter
        public final boolean acceptMatch(CharSequence s, int start, int end) {
            if (start == 0 || s.charAt(start - 1) != '@') {
                return true;
            }
            return false;
        }
    };
    public static final MatchFilter sPhoneNumberMatchFilter = new MatchFilter() { // from class: android.text.util.Linkify.2
        @Override // android.text.util.Linkify.MatchFilter
        public final boolean acceptMatch(CharSequence s, int start, int end) {
            int digitCount = 0;
            for (int digitCount2 = start; digitCount2 < end; digitCount2++) {
                if (Character.isDigit(s.charAt(digitCount2)) && (digitCount = digitCount + 1) >= 5) {
                    return true;
                }
            }
            return false;
        }
    };
    public static final TransformFilter sPhoneNumberTransformFilter = new TransformFilter() { // from class: android.text.util.Linkify.3
        @Override // android.text.util.Linkify.TransformFilter
        public final String transformUrl(Matcher match, String url) {
            return Patterns.digitsAndPlusOnly(match);
        }
    };

    @Retention(RetentionPolicy.SOURCE)
    /* loaded from: classes2.dex */
    public @interface LinkifyMask {
    }

    /* loaded from: classes2.dex */
    public interface MatchFilter {
        boolean acceptMatch(CharSequence charSequence, int i, int i2);
    }

    /* loaded from: classes2.dex */
    public interface TransformFilter {
        String transformUrl(Matcher matcher, String str);
    }

    public static final boolean addLinks(Spannable text, int mask) {
        return addLinks(text, mask, (Context) null);
    }

    private static synchronized boolean addLinks(Spannable text, int mask, Context context) {
        if (text != null && containsUnsupportedCharacters(text.toString())) {
            EventLog.writeEvent(1397638484, "116321860", -1, "");
            return false;
        } else if (mask == 0) {
            return false;
        } else {
            URLSpan[] old = (URLSpan[]) text.getSpans(0, text.length(), URLSpan.class);
            for (int i = old.length - 1; i >= 0; i--) {
                text.removeSpan(old[i]);
            }
            ArrayList<LinkSpec> links = new ArrayList<>();
            if ((mask & 1) != 0) {
                gatherLinks(links, text, Patterns.AUTOLINK_WEB_URL, new String[]{"http://", "https://", "rtsp://"}, sUrlMatchFilter, null);
            }
            if ((mask & 2) != 0) {
                gatherLinks(links, text, Patterns.AUTOLINK_EMAIL_ADDRESS, new String[]{"mailto:"}, null, null);
            }
            if ((mask & 4) != 0) {
                gatherTelLinks(links, text, context);
            }
            if ((mask & 8) != 0) {
                gatherMapLinks(links, text);
            }
            pruneOverlaps(links);
            if (links.size() == 0) {
                return false;
            }
            Iterator<LinkSpec> it = links.iterator();
            while (it.hasNext()) {
                LinkSpec link = it.next();
                applyLink(link.url, link.start, link.end, text);
            }
            return true;
        }
    }

    public static boolean containsUnsupportedCharacters(String text) {
        if (text.contains("\u202c")) {
            Log.e(LOG_TAG, "Unsupported character for applying links: u202C");
            return true;
        } else if (text.contains("\u202d")) {
            Log.e(LOG_TAG, "Unsupported character for applying links: u202D");
            return true;
        } else if (text.contains("\u202e")) {
            Log.e(LOG_TAG, "Unsupported character for applying links: u202E");
            return true;
        } else {
            return false;
        }
    }

    public static final boolean addLinks(TextView text, int mask) {
        if (mask == 0) {
            return false;
        }
        Context context = text.getContext();
        CharSequence t = text.getText();
        if (t instanceof Spannable) {
            if (!addLinks((Spannable) t, mask, context)) {
                return false;
            }
            addLinkMovementMethod(text);
            return true;
        }
        SpannableString s = SpannableString.valueOf(t);
        if (!addLinks(s, mask, context)) {
            return false;
        }
        addLinkMovementMethod(text);
        text.setText(s);
        return true;
    }

    private static final synchronized void addLinkMovementMethod(TextView t) {
        MovementMethod m = t.getMovementMethod();
        if ((m == null || !(m instanceof LinkMovementMethod)) && t.getLinksClickable()) {
            t.setMovementMethod(LinkMovementMethod.getInstance());
        }
    }

    public static final void addLinks(TextView text, Pattern pattern, String scheme) {
        addLinks(text, pattern, scheme, (String[]) null, (MatchFilter) null, (TransformFilter) null);
    }

    public static final void addLinks(TextView text, Pattern pattern, String scheme, MatchFilter matchFilter, TransformFilter transformFilter) {
        addLinks(text, pattern, scheme, (String[]) null, matchFilter, transformFilter);
    }

    public static final void addLinks(TextView text, Pattern pattern, String defaultScheme, String[] schemes, MatchFilter matchFilter, TransformFilter transformFilter) {
        SpannableString spannable = SpannableString.valueOf(text.getText());
        boolean linksAdded = addLinks(spannable, pattern, defaultScheme, schemes, matchFilter, transformFilter);
        if (linksAdded) {
            text.setText(spannable);
            addLinkMovementMethod(text);
        }
    }

    public static final boolean addLinks(Spannable text, Pattern pattern, String scheme) {
        return addLinks(text, pattern, scheme, (String[]) null, (MatchFilter) null, (TransformFilter) null);
    }

    public static final boolean addLinks(Spannable spannable, Pattern pattern, String scheme, MatchFilter matchFilter, TransformFilter transformFilter) {
        return addLinks(spannable, pattern, scheme, (String[]) null, matchFilter, transformFilter);
    }

    public static final boolean addLinks(Spannable spannable, Pattern pattern, String defaultScheme, String[] schemes, MatchFilter matchFilter, TransformFilter transformFilter) {
        if (spannable != null && containsUnsupportedCharacters(spannable.toString())) {
            EventLog.writeEvent(1397638484, "116321860", -1, "");
            return false;
        }
        if (defaultScheme == null) {
            defaultScheme = "";
        }
        if (schemes == null || schemes.length < 1) {
            schemes = EmptyArray.STRING;
        }
        String[] schemesCopy = new String[schemes.length + 1];
        schemesCopy[0] = defaultScheme.toLowerCase(Locale.ROOT);
        for (int index = 0; index < schemes.length; index++) {
            String scheme = schemes[index];
            schemesCopy[index + 1] = scheme == null ? "" : scheme.toLowerCase(Locale.ROOT);
        }
        boolean hasMatches = false;
        Matcher m = pattern.matcher(spannable);
        while (m.find()) {
            int start = m.start();
            int end = m.end();
            boolean allowed = true;
            if (matchFilter != null) {
                allowed = matchFilter.acceptMatch(spannable, start, end);
            }
            if (allowed) {
                String url = makeUrl(m.group(0), schemesCopy, m, transformFilter);
                applyLink(url, start, end, spannable);
                hasMatches = true;
            }
        }
        return hasMatches;
    }

    public static synchronized Future<Void> addLinksAsync(TextView textView, TextLinksParams params) {
        return addLinksAsync(textView, params, null, null);
    }

    public static synchronized Future<Void> addLinksAsync(TextView textView, int mask) {
        return addLinksAsync(textView, TextLinksParams.fromLinkMask(mask), null, null);
    }

    public static synchronized Future<Void> addLinksAsync(final TextView textView, TextLinksParams params, Executor executor, Consumer<Integer> callback) {
        Preconditions.checkNotNull(textView);
        final CharSequence text = textView.getText();
        final Spannable spannable = text instanceof Spannable ? (Spannable) text : SpannableString.valueOf(text);
        Runnable modifyTextView = new Runnable() { // from class: android.text.util.-$$Lambda$Linkify$wWMJCtMwD1HLtUFna4kOfNQK1Z0
            @Override // java.lang.Runnable
            public final void run() {
                Linkify.lambda$addLinksAsync$0(TextView.this, spannable, text);
            }
        };
        return addLinksAsync(spannable, textView.getTextClassifier(), params, executor, callback, modifyTextView);
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public static /* synthetic */ void lambda$addLinksAsync$0(TextView textView, Spannable spannable, CharSequence text) {
        addLinkMovementMethod(textView);
        if (spannable != text) {
            textView.setText(spannable);
        }
    }

    public static synchronized Future<Void> addLinksAsync(Spannable text, TextClassifier classifier, TextLinksParams params) {
        return addLinksAsync(text, classifier, params, null, null);
    }

    public static synchronized Future<Void> addLinksAsync(Spannable text, TextClassifier classifier, int mask) {
        return addLinksAsync(text, classifier, TextLinksParams.fromLinkMask(mask), null, null);
    }

    public static synchronized Future<Void> addLinksAsync(Spannable text, TextClassifier classifier, TextLinksParams params, Executor executor, Consumer<Integer> callback) {
        return addLinksAsync(text, classifier, params, executor, callback, null);
    }

    private static synchronized Future<Void> addLinksAsync(final Spannable text, final TextClassifier classifier, final TextLinksParams params, Executor executor, final Consumer<Integer> callback, final Runnable modifyTextView) {
        Preconditions.checkNotNull(text);
        Preconditions.checkNotNull(classifier);
        final CharSequence truncatedText = text.subSequence(0, Math.min(text.length(), classifier.getMaxGenerateLinksTextLength()));
        TextClassifier.EntityConfig entityConfig = params == null ? null : params.getEntityConfig();
        final TextLinks.Request request = new TextLinks.Request.Builder(truncatedText).setLegacyFallback(true).setEntityConfig(entityConfig).build();
        Supplier<TextLinks> supplier = new Supplier() { // from class: android.text.util.-$$Lambda$Linkify$hPjCKfcU4vqhADicCa9bWKrOoog
            @Override // java.util.function.Supplier
            public final Object get() {
                TextLinks generateLinks;
                generateLinks = TextClassifier.this.generateLinks(request);
                return generateLinks;
            }
        };
        Consumer<TextLinks> consumer = new Consumer() { // from class: android.text.util.-$$Lambda$Linkify$ZGgxzuK-YqBkZXo_7HE4xwOLsh0
            @Override // java.util.function.Consumer
            public final void accept(Object obj) {
                Linkify.lambda$addLinksAsync$2(callback, text, truncatedText, params, modifyTextView, (TextLinks) obj);
            }
        };
        if (executor == null) {
            return CompletableFuture.supplyAsync(supplier).thenAccept((Consumer) consumer);
        }
        return CompletableFuture.supplyAsync(supplier, executor).thenAccept((Consumer) consumer);
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public static /* synthetic */ void lambda$addLinksAsync$2(Consumer callback, Spannable text, CharSequence truncatedText, TextLinksParams params, Runnable modifyTextView, TextLinks links) {
        if (links.getLinks().isEmpty()) {
            if (callback != null) {
                callback.accept(1);
                return;
            }
            return;
        }
        TextLinks.TextLinkSpan[] old = (TextLinks.TextLinkSpan[]) text.getSpans(0, truncatedText.length(), TextLinks.TextLinkSpan.class);
        int i = old.length - 1;
        while (true) {
            int i2 = i;
            if (i2 < 0) {
                break;
            }
            text.removeSpan(old[i2]);
            i = i2 - 1;
        }
        int result = params.apply(text, links);
        if (result == 0 && modifyTextView != null) {
            modifyTextView.run();
        }
        if (callback != null) {
            callback.accept(Integer.valueOf(result));
        }
    }

    private static final synchronized void applyLink(String url, int start, int end, Spannable text) {
        URLSpan span = new URLSpan(url);
        text.setSpan(span, start, end, 33);
    }

    private static final synchronized String makeUrl(String url, String[] prefixes, Matcher matcher, TransformFilter filter) {
        if (filter != null) {
            url = filter.transformUrl(matcher, url);
        }
        boolean hasPrefix = false;
        int i = 0;
        while (true) {
            int i2 = i;
            int i3 = prefixes.length;
            if (i2 >= i3) {
                break;
            }
            if (!url.regionMatches(true, 0, prefixes[i2], 0, prefixes[i2].length())) {
                i = i2 + 1;
            } else {
                hasPrefix = true;
                if (!url.regionMatches(false, 0, prefixes[i2], 0, prefixes[i2].length())) {
                    url = prefixes[i2] + url.substring(prefixes[i2].length());
                }
            }
        }
        if (!hasPrefix && prefixes.length > 0) {
            return prefixes[0] + url;
        }
        return url;
    }

    private static final synchronized void gatherLinks(ArrayList<LinkSpec> links, Spannable s, Pattern pattern, String[] schemes, MatchFilter matchFilter, TransformFilter transformFilter) {
        Matcher m = pattern.matcher(s);
        while (m.find()) {
            int start = m.start();
            int end = m.end();
            if (matchFilter == null || matchFilter.acceptMatch(s, start, end)) {
                LinkSpec spec = new LinkSpec();
                String url = makeUrl(m.group(0), schemes, m, transformFilter);
                spec.url = url;
                spec.start = start;
                spec.end = end;
                links.add(spec);
            }
        }
    }

    public protected static void gatherTelLinks(ArrayList<LinkSpec> links, Spannable s, Context context) {
        TelephonyManager from;
        PhoneNumberUtil phoneUtil = PhoneNumberUtil.getInstance();
        if (context == null) {
            from = TelephonyManager.getDefault();
        } else {
            from = TelephonyManager.from(context);
        }
        TelephonyManager tm = from;
        Iterable<PhoneNumberMatch> matches = phoneUtil.findNumbers(s.toString(), tm.getSimCountryIso().toUpperCase(Locale.US), PhoneNumberUtil.Leniency.POSSIBLE, Long.MAX_VALUE);
        for (PhoneNumberMatch match : matches) {
            LinkSpec spec = new LinkSpec();
            spec.url = WebView.SCHEME_TEL + PhoneNumberUtils.normalizeNumber(match.rawString());
            spec.start = match.start();
            spec.end = match.end();
            links.add(spec);
        }
    }

    private static final synchronized void gatherMapLinks(ArrayList<LinkSpec> links, Spannable s) {
        int start;
        String string = s.toString();
        int base = 0;
        while (true) {
            try {
                String address = WebView.findAddress(string);
                if (address == null || (start = string.indexOf(address)) < 0) {
                    break;
                }
                LinkSpec spec = new LinkSpec();
                int length = address.length();
                int end = start + length;
                spec.start = base + start;
                spec.end = base + end;
                string = string.substring(end);
                base += end;
                try {
                    String encodedAddress = URLEncoder.encode(address, "UTF-8");
                    spec.url = WebView.SCHEME_GEO + encodedAddress;
                    links.add(spec);
                } catch (UnsupportedEncodingException e) {
                }
            } catch (UnsupportedOperationException e2) {
                return;
            }
        }
    }

    private static final synchronized void pruneOverlaps(ArrayList<LinkSpec> links) {
        Comparator<LinkSpec> c = new Comparator<LinkSpec>() { // from class: android.text.util.Linkify.4
            @Override // java.util.Comparator
            public final int compare(LinkSpec a, LinkSpec b) {
                if (a.start < b.start) {
                    return -1;
                }
                if (a.start <= b.start && a.end >= b.end) {
                    return a.end > b.end ? -1 : 0;
                }
                return 1;
            }
        };
        Collections.sort(links, c);
        int len = links.size();
        int i = 0;
        while (i < len - 1) {
            LinkSpec a = links.get(i);
            LinkSpec b = links.get(i + 1);
            int remove = -1;
            if (a.start <= b.start && a.end > b.start) {
                if (b.end <= a.end) {
                    remove = i + 1;
                } else if (a.end - a.start > b.end - b.start) {
                    remove = i + 1;
                } else if (a.end - a.start < b.end - b.start) {
                    remove = i;
                }
                if (remove != -1) {
                    links.remove(remove);
                    len--;
                }
            }
            i++;
        }
    }
}
