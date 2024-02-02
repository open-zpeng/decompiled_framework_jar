package android.net.captiveportal;

import android.text.TextUtils;
import android.util.Log;
import java.net.MalformedURLException;
import java.net.URL;
import java.text.ParseException;
import java.util.ArrayList;
import java.util.List;
import java.util.regex.Pattern;
import java.util.regex.PatternSyntaxException;
/* loaded from: classes2.dex */
public abstract class CaptivePortalProbeSpec {
    private protected static final String HTTP_LOCATION_HEADER_NAME = "Location";
    public protected static final String REGEX_SEPARATOR = "@@/@@";
    public protected static final String SPEC_SEPARATOR = "@@,@@";
    public protected static final String TAG = CaptivePortalProbeSpec.class.getSimpleName();
    public protected final String mEncodedSpec;
    public protected final URL mUrl;

    private protected abstract synchronized CaptivePortalProbeResult getResult(int i, String str);

    public private protected synchronized CaptivePortalProbeSpec(String encodedSpec, URL url) {
        this.mEncodedSpec = encodedSpec;
        this.mUrl = url;
    }

    private protected static synchronized CaptivePortalProbeSpec parseSpec(String spec) throws ParseException, MalformedURLException {
        if (TextUtils.isEmpty(spec)) {
            throw new ParseException("Empty probe spec", 0);
        }
        String[] splits = TextUtils.split(spec, REGEX_SEPARATOR);
        if (splits.length != 3) {
            throw new ParseException("Probe spec does not have 3 parts", 0);
        }
        int statusRegexPos = splits[0].length() + REGEX_SEPARATOR.length();
        int locationRegexPos = splits[1].length() + statusRegexPos + REGEX_SEPARATOR.length();
        Pattern statusRegex = parsePatternIfNonEmpty(splits[1], statusRegexPos);
        Pattern locationRegex = parsePatternIfNonEmpty(splits[2], locationRegexPos);
        return new RegexMatchProbeSpec(spec, new URL(splits[0]), statusRegex, locationRegex);
    }

    public protected static synchronized Pattern parsePatternIfNonEmpty(String pattern, int pos) throws ParseException {
        if (TextUtils.isEmpty(pattern)) {
            return null;
        }
        try {
            return Pattern.compile(pattern);
        } catch (PatternSyntaxException e) {
            throw new ParseException(String.format("Invalid status pattern [%s]: %s", pattern, e), pos);
        }
    }

    private protected static synchronized CaptivePortalProbeSpec parseSpecOrNull(String spec) {
        if (spec != null) {
            try {
                return parseSpec(spec);
            } catch (MalformedURLException | ParseException e) {
                String str = TAG;
                Log.e(str, "Invalid probe spec: " + spec, e);
                return null;
            }
        }
        return null;
    }

    private protected static synchronized CaptivePortalProbeSpec[] parseCaptivePortalProbeSpecs(String settingsVal) {
        String[] split;
        List<CaptivePortalProbeSpec> specs = new ArrayList<>();
        if (settingsVal != null) {
            for (String spec : TextUtils.split(settingsVal, SPEC_SEPARATOR)) {
                try {
                    specs.add(parseSpec(spec));
                } catch (MalformedURLException | ParseException e) {
                    Log.e(TAG, "Invalid probe spec: " + spec, e);
                }
            }
        }
        if (specs.isEmpty()) {
            Log.e(TAG, String.format("could not create any validation spec from %s", settingsVal));
        }
        return (CaptivePortalProbeSpec[]) specs.toArray(new CaptivePortalProbeSpec[specs.size()]);
    }

    private protected synchronized String getEncodedSpec() {
        return this.mEncodedSpec;
    }

    private protected synchronized URL getUrl() {
        return this.mUrl;
    }

    /* JADX INFO: Access modifiers changed from: private */
    /* loaded from: classes2.dex */
    public static class RegexMatchProbeSpec extends CaptivePortalProbeSpec {
        public private protected final Pattern mLocationHeaderRegex;
        public private protected final Pattern mStatusRegex;

        public private protected synchronized RegexMatchProbeSpec(String spec, URL url, Pattern statusRegex, Pattern locationHeaderRegex) {
            super(spec, url);
            this.mStatusRegex = statusRegex;
            this.mLocationHeaderRegex = locationHeaderRegex;
        }

        private protected synchronized CaptivePortalProbeResult getResult(int status, String locationHeader) {
            boolean statusMatch = CaptivePortalProbeSpec.safeMatch(String.valueOf(status), this.mStatusRegex);
            boolean locationMatch = CaptivePortalProbeSpec.safeMatch(locationHeader, this.mLocationHeaderRegex);
            int returnCode = (statusMatch && locationMatch) ? 204 : 302;
            return new CaptivePortalProbeResult(returnCode, locationHeader, getUrl().toString(), this);
        }
    }

    /* JADX INFO: Access modifiers changed from: public */
    public static synchronized boolean safeMatch(String value, Pattern pattern) {
        return pattern == null || TextUtils.isEmpty(value) || pattern.matcher(value).matches();
    }
}
