package android.net;

import android.content.ContentResolver;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.Environment;
import android.os.Parcel;
import android.os.Parcelable;
import android.os.StrictMode;
import android.provider.SettingsStringUtil;
import android.telecom.PhoneAccount;
import android.util.Log;
import com.android.internal.midi.MidiConstants;
import com.android.internal.transition.EpicenterTranslateClipReveal;
import java.io.File;
import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;
import java.nio.charset.StandardCharsets;
import java.util.AbstractList;
import java.util.ArrayList;
import java.util.Collections;
import java.util.LinkedHashSet;
import java.util.List;
import java.util.Locale;
import java.util.Objects;
import java.util.RandomAccess;
import java.util.Set;
import libcore.net.UriCodec;
/* loaded from: classes2.dex */
public abstract class Uri implements Parcelable, Comparable<Uri> {
    private static final String DEFAULT_ENCODING = "UTF-8";
    private static final int NOT_CALCULATED = -2;
    private static final int NOT_FOUND = -1;
    private static final String NOT_HIERARCHICAL = "This isn't a hierarchical URI.";
    private static final int NULL_TYPE_ID = 0;
    private static final String LOG = Uri.class.getSimpleName();
    private static final String NOT_CACHED = new String("NOT CACHED");
    public static final Uri EMPTY = new HierarchicalUri(null, Part.NULL, PathPart.EMPTY, Part.NULL, Part.NULL);
    public static final Parcelable.Creator<Uri> CREATOR = new Parcelable.Creator<Uri>() { // from class: android.net.Uri.1
        /* JADX WARN: Can't rename method to resolve collision */
        @Override // android.os.Parcelable.Creator
        public Uri createFromParcel(Parcel in) {
            int type = in.readInt();
            switch (type) {
                case 0:
                    return null;
                case 1:
                    return StringUri.readFrom(in);
                case 2:
                    return OpaqueUri.readFrom(in);
                case 3:
                    return HierarchicalUri.readFrom(in);
                default:
                    throw new IllegalArgumentException("Unknown URI type: " + type);
            }
        }

        /* JADX WARN: Can't rename method to resolve collision */
        @Override // android.os.Parcelable.Creator
        public Uri[] newArray(int size) {
            return new Uri[size];
        }
    };
    private static final char[] HEX_DIGITS = "0123456789ABCDEF".toCharArray();

    public abstract Builder buildUpon();

    public abstract String getAuthority();

    public abstract String getEncodedAuthority();

    public abstract String getEncodedFragment();

    public abstract String getEncodedPath();

    public abstract String getEncodedQuery();

    public abstract String getEncodedSchemeSpecificPart();

    public abstract String getEncodedUserInfo();

    public abstract String getFragment();

    public abstract String getHost();

    public abstract String getLastPathSegment();

    public abstract String getPath();

    public abstract List<String> getPathSegments();

    public abstract int getPort();

    public abstract String getQuery();

    public abstract String getScheme();

    public abstract String getSchemeSpecificPart();

    public abstract String getUserInfo();

    public abstract boolean isHierarchical();

    public abstract boolean isRelative();

    public abstract String toString();

    public protected Uri() {
    }

    public boolean isOpaque() {
        return !isHierarchical();
    }

    public boolean isAbsolute() {
        return !isRelative();
    }

    public boolean equals(Object o) {
        if (!(o instanceof Uri)) {
            return false;
        }
        Uri other = (Uri) o;
        return toString().equals(other.toString());
    }

    public int hashCode() {
        return toString().hashCode();
    }

    @Override // java.lang.Comparable
    public int compareTo(Uri other) {
        return toString().compareTo(other.toString());
    }

    /* JADX INFO: Access modifiers changed from: private */
    public String toSafeString() {
        String scheme = getScheme();
        String ssp = getSchemeSpecificPart();
        if (scheme != null) {
            if (scheme.equalsIgnoreCase(PhoneAccount.SCHEME_TEL) || scheme.equalsIgnoreCase("sip") || scheme.equalsIgnoreCase("sms") || scheme.equalsIgnoreCase("smsto") || scheme.equalsIgnoreCase("mailto") || scheme.equalsIgnoreCase("nfc")) {
                StringBuilder builder = new StringBuilder(64);
                builder.append(scheme);
                builder.append(':');
                if (ssp != null) {
                    for (int i = 0; i < ssp.length(); i++) {
                        char c = ssp.charAt(i);
                        if (c == '-' || c == '@' || c == '.') {
                            builder.append(c);
                        } else {
                            builder.append(EpicenterTranslateClipReveal.StateProperty.TARGET_X);
                        }
                    }
                }
                return builder.toString();
            } else if (scheme.equalsIgnoreCase(IntentFilter.SCHEME_HTTP) || scheme.equalsIgnoreCase(IntentFilter.SCHEME_HTTPS) || scheme.equalsIgnoreCase("ftp")) {
                StringBuilder sb = new StringBuilder();
                sb.append("//");
                sb.append(getHost() != null ? getHost() : "");
                sb.append(getPort() != -1 ? SettingsStringUtil.DELIMITER + getPort() : "");
                sb.append("/...");
                ssp = sb.toString();
            }
        }
        StringBuilder builder2 = new StringBuilder(64);
        if (scheme != null) {
            builder2.append(scheme);
            builder2.append(':');
        }
        if (ssp != null) {
            builder2.append(ssp);
        }
        return builder2.toString();
    }

    public static Uri parse(String uriString) {
        return new StringUri(uriString);
    }

    public static Uri fromFile(File file) {
        if (file == null) {
            throw new NullPointerException(ContentResolver.SCHEME_FILE);
        }
        PathPart path = PathPart.fromDecoded(file.getAbsolutePath());
        return new HierarchicalUri(ContentResolver.SCHEME_FILE, Part.EMPTY, path, Part.NULL, Part.NULL);
    }

    /* JADX INFO: Access modifiers changed from: private */
    /* loaded from: classes2.dex */
    public static class StringUri extends AbstractHierarchicalUri {
        static final int TYPE_ID = 1;
        private Part authority;
        private volatile int cachedFsi;
        private volatile int cachedSsi;
        private Part fragment;
        private PathPart path;
        private Part query;
        private volatile String scheme;
        private Part ssp;
        private final String uriString;

        private synchronized StringUri(String uriString) {
            super();
            this.cachedSsi = -2;
            this.cachedFsi = -2;
            this.scheme = Uri.NOT_CACHED;
            if (uriString == null) {
                throw new NullPointerException("uriString");
            }
            this.uriString = uriString;
        }

        static synchronized Uri readFrom(Parcel parcel) {
            return new StringUri(parcel.readString());
        }

        @Override // android.os.Parcelable
        public int describeContents() {
            return 0;
        }

        @Override // android.os.Parcelable
        public void writeToParcel(Parcel parcel, int flags) {
            parcel.writeInt(1);
            parcel.writeString(this.uriString);
        }

        private synchronized int findSchemeSeparator() {
            if (this.cachedSsi == -2) {
                int indexOf = this.uriString.indexOf(58);
                this.cachedSsi = indexOf;
                return indexOf;
            }
            return this.cachedSsi;
        }

        private synchronized int findFragmentSeparator() {
            if (this.cachedFsi == -2) {
                int indexOf = this.uriString.indexOf(35, findSchemeSeparator());
                this.cachedFsi = indexOf;
                return indexOf;
            }
            return this.cachedFsi;
        }

        @Override // android.net.Uri
        public boolean isHierarchical() {
            int ssi = findSchemeSeparator();
            if (ssi == -1) {
                return true;
            }
            return this.uriString.length() != ssi + 1 && this.uriString.charAt(ssi + 1) == '/';
        }

        @Override // android.net.Uri
        public boolean isRelative() {
            return findSchemeSeparator() == -1;
        }

        @Override // android.net.Uri
        public String getScheme() {
            boolean cached = this.scheme != Uri.NOT_CACHED;
            if (cached) {
                return this.scheme;
            }
            String parseScheme = parseScheme();
            this.scheme = parseScheme;
            return parseScheme;
        }

        private synchronized String parseScheme() {
            int ssi = findSchemeSeparator();
            if (ssi == -1) {
                return null;
            }
            return this.uriString.substring(0, ssi);
        }

        private synchronized Part getSsp() {
            if (this.ssp == null) {
                Part fromEncoded = Part.fromEncoded(parseSsp());
                this.ssp = fromEncoded;
                return fromEncoded;
            }
            return this.ssp;
        }

        @Override // android.net.Uri
        public String getEncodedSchemeSpecificPart() {
            return getSsp().getEncoded();
        }

        @Override // android.net.Uri
        public String getSchemeSpecificPart() {
            return getSsp().getDecoded();
        }

        private synchronized String parseSsp() {
            int ssi = findSchemeSeparator();
            int fsi = findFragmentSeparator();
            if (fsi == -1) {
                return this.uriString.substring(ssi + 1);
            }
            return this.uriString.substring(ssi + 1, fsi);
        }

        private synchronized Part getAuthorityPart() {
            if (this.authority == null) {
                String encodedAuthority = parseAuthority(this.uriString, findSchemeSeparator());
                Part fromEncoded = Part.fromEncoded(encodedAuthority);
                this.authority = fromEncoded;
                return fromEncoded;
            }
            return this.authority;
        }

        @Override // android.net.Uri
        public String getEncodedAuthority() {
            return getAuthorityPart().getEncoded();
        }

        @Override // android.net.Uri
        public String getAuthority() {
            return getAuthorityPart().getDecoded();
        }

        private synchronized PathPart getPathPart() {
            if (this.path == null) {
                PathPart fromEncoded = PathPart.fromEncoded(parsePath());
                this.path = fromEncoded;
                return fromEncoded;
            }
            return this.path;
        }

        @Override // android.net.Uri
        public String getPath() {
            return getPathPart().getDecoded();
        }

        @Override // android.net.Uri
        public String getEncodedPath() {
            return getPathPart().getEncoded();
        }

        @Override // android.net.Uri
        public List<String> getPathSegments() {
            return getPathPart().getPathSegments();
        }

        private synchronized String parsePath() {
            String uriString = this.uriString;
            int ssi = findSchemeSeparator();
            if (ssi > -1) {
                boolean schemeOnly = ssi + 1 == uriString.length();
                if (schemeOnly || uriString.charAt(ssi + 1) != '/') {
                    return null;
                }
            }
            return parsePath(uriString, ssi);
        }

        private synchronized Part getQueryPart() {
            if (this.query == null) {
                Part fromEncoded = Part.fromEncoded(parseQuery());
                this.query = fromEncoded;
                return fromEncoded;
            }
            return this.query;
        }

        @Override // android.net.Uri
        public String getEncodedQuery() {
            return getQueryPart().getEncoded();
        }

        private synchronized String parseQuery() {
            int qsi = this.uriString.indexOf(63, findSchemeSeparator());
            if (qsi == -1) {
                return null;
            }
            int fsi = findFragmentSeparator();
            if (fsi == -1) {
                return this.uriString.substring(qsi + 1);
            }
            if (fsi < qsi) {
                return null;
            }
            return this.uriString.substring(qsi + 1, fsi);
        }

        @Override // android.net.Uri
        public String getQuery() {
            return getQueryPart().getDecoded();
        }

        private synchronized Part getFragmentPart() {
            if (this.fragment == null) {
                Part fromEncoded = Part.fromEncoded(parseFragment());
                this.fragment = fromEncoded;
                return fromEncoded;
            }
            return this.fragment;
        }

        @Override // android.net.Uri
        public String getEncodedFragment() {
            return getFragmentPart().getEncoded();
        }

        private synchronized String parseFragment() {
            int fsi = findFragmentSeparator();
            if (fsi == -1) {
                return null;
            }
            return this.uriString.substring(fsi + 1);
        }

        @Override // android.net.Uri
        public String getFragment() {
            return getFragmentPart().getDecoded();
        }

        @Override // android.net.Uri
        public String toString() {
            return this.uriString;
        }

        static synchronized String parseAuthority(String uriString, int ssi) {
            int length = uriString.length();
            if (length > ssi + 2 && uriString.charAt(ssi + 1) == '/' && uriString.charAt(ssi + 2) == '/') {
                int end = ssi + 3;
                while (end < length) {
                    char charAt = uriString.charAt(end);
                    if (charAt == '#' || charAt == '/' || charAt == '?' || charAt == '\\') {
                        break;
                    }
                    end++;
                }
                return uriString.substring(ssi + 3, end);
            }
            return null;
        }

        static synchronized String parsePath(String uriString, int ssi) {
            int pathStart;
            int length = uriString.length();
            if (length > ssi + 2 && uriString.charAt(ssi + 1) == '/' && uriString.charAt(ssi + 2) == '/') {
                pathStart = ssi + 3;
                while (pathStart < length) {
                    char charAt = uriString.charAt(pathStart);
                    if (charAt == '#') {
                        return "";
                    }
                    if (charAt == '/') {
                        break;
                    } else if (charAt == '?') {
                        return "";
                    } else {
                        if (charAt == '\\') {
                            break;
                        }
                        pathStart++;
                    }
                }
            } else {
                pathStart = ssi + 1;
            }
            int pathEnd = pathStart;
            while (pathEnd < length) {
                char charAt2 = uriString.charAt(pathEnd);
                if (charAt2 == '#' || charAt2 == '?') {
                    break;
                }
                pathEnd++;
            }
            return uriString.substring(pathStart, pathEnd);
        }

        @Override // android.net.Uri
        public Builder buildUpon() {
            if (isHierarchical()) {
                return new Builder().scheme(getScheme()).authority(getAuthorityPart()).path(getPathPart()).query(getQueryPart()).fragment(getFragmentPart());
            }
            return new Builder().scheme(getScheme()).opaquePart(getSsp()).fragment(getFragmentPart());
        }
    }

    public static Uri fromParts(String scheme, String ssp, String fragment) {
        if (scheme == null) {
            throw new NullPointerException("scheme");
        }
        if (ssp == null) {
            throw new NullPointerException("ssp");
        }
        return new OpaqueUri(scheme, Part.fromDecoded(ssp), Part.fromDecoded(fragment));
    }

    /* JADX INFO: Access modifiers changed from: private */
    /* loaded from: classes2.dex */
    public static class OpaqueUri extends Uri {
        static final int TYPE_ID = 2;
        private volatile String cachedString;
        private final Part fragment;
        private final String scheme;
        private final Part ssp;

        private synchronized OpaqueUri(String scheme, Part ssp, Part fragment) {
            super();
            this.cachedString = Uri.NOT_CACHED;
            this.scheme = scheme;
            this.ssp = ssp;
            this.fragment = fragment == null ? Part.NULL : fragment;
        }

        static synchronized Uri readFrom(Parcel parcel) {
            return new OpaqueUri(parcel.readString(), Part.readFrom(parcel), Part.readFrom(parcel));
        }

        @Override // android.os.Parcelable
        public int describeContents() {
            return 0;
        }

        @Override // android.os.Parcelable
        public void writeToParcel(Parcel parcel, int flags) {
            parcel.writeInt(2);
            parcel.writeString(this.scheme);
            this.ssp.writeTo(parcel);
            this.fragment.writeTo(parcel);
        }

        @Override // android.net.Uri
        public boolean isHierarchical() {
            return false;
        }

        @Override // android.net.Uri
        public boolean isRelative() {
            return this.scheme == null;
        }

        @Override // android.net.Uri
        public String getScheme() {
            return this.scheme;
        }

        @Override // android.net.Uri
        public String getEncodedSchemeSpecificPart() {
            return this.ssp.getEncoded();
        }

        @Override // android.net.Uri
        public String getSchemeSpecificPart() {
            return this.ssp.getDecoded();
        }

        @Override // android.net.Uri
        public String getAuthority() {
            return null;
        }

        @Override // android.net.Uri
        public String getEncodedAuthority() {
            return null;
        }

        @Override // android.net.Uri
        public String getPath() {
            return null;
        }

        @Override // android.net.Uri
        public String getEncodedPath() {
            return null;
        }

        @Override // android.net.Uri
        public String getQuery() {
            return null;
        }

        @Override // android.net.Uri
        public String getEncodedQuery() {
            return null;
        }

        @Override // android.net.Uri
        public String getFragment() {
            return this.fragment.getDecoded();
        }

        @Override // android.net.Uri
        public String getEncodedFragment() {
            return this.fragment.getEncoded();
        }

        @Override // android.net.Uri
        public List<String> getPathSegments() {
            return Collections.emptyList();
        }

        @Override // android.net.Uri
        public String getLastPathSegment() {
            return null;
        }

        @Override // android.net.Uri
        public String getUserInfo() {
            return null;
        }

        @Override // android.net.Uri
        public String getEncodedUserInfo() {
            return null;
        }

        @Override // android.net.Uri
        public String getHost() {
            return null;
        }

        @Override // android.net.Uri
        public int getPort() {
            return -1;
        }

        @Override // android.net.Uri
        public String toString() {
            boolean cached = this.cachedString != Uri.NOT_CACHED;
            if (cached) {
                return this.cachedString;
            }
            StringBuilder sb = new StringBuilder();
            sb.append(this.scheme);
            sb.append(':');
            sb.append(getEncodedSchemeSpecificPart());
            if (!this.fragment.isEmpty()) {
                sb.append('#');
                sb.append(this.fragment.getEncoded());
            }
            String sb2 = sb.toString();
            this.cachedString = sb2;
            return sb2;
        }

        @Override // android.net.Uri
        public Builder buildUpon() {
            return new Builder().scheme(this.scheme).opaquePart(this.ssp).fragment(this.fragment);
        }
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    /* loaded from: classes2.dex */
    public static class PathSegments extends AbstractList<String> implements RandomAccess {
        static final PathSegments EMPTY = new PathSegments(null, 0);
        final String[] segments;
        final int size;

        synchronized PathSegments(String[] segments, int size) {
            this.segments = segments;
            this.size = size;
        }

        @Override // java.util.AbstractList, java.util.List
        public String get(int index) {
            if (index >= this.size) {
                throw new IndexOutOfBoundsException();
            }
            return this.segments[index];
        }

        @Override // java.util.AbstractCollection, java.util.Collection, java.util.List
        public int size() {
            return this.size;
        }
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    /* loaded from: classes2.dex */
    public static class PathSegmentsBuilder {
        String[] segments;
        int size = 0;

        synchronized PathSegmentsBuilder() {
        }

        synchronized void add(String segment) {
            if (this.segments == null) {
                this.segments = new String[4];
            } else if (this.size + 1 == this.segments.length) {
                String[] expanded = new String[this.segments.length * 2];
                System.arraycopy(this.segments, 0, expanded, 0, this.segments.length);
                this.segments = expanded;
            }
            String[] expanded2 = this.segments;
            int i = this.size;
            this.size = i + 1;
            expanded2[i] = segment;
        }

        synchronized PathSegments build() {
            if (this.segments == null) {
                return PathSegments.EMPTY;
            }
            try {
                return new PathSegments(this.segments, this.size);
            } finally {
                this.segments = null;
            }
        }
    }

    /* loaded from: classes2.dex */
    private static abstract class AbstractHierarchicalUri extends Uri {
        private volatile String host;
        private volatile int port;
        private Part userInfo;

        private synchronized AbstractHierarchicalUri() {
            super();
            this.host = Uri.NOT_CACHED;
            this.port = -2;
        }

        @Override // android.net.Uri
        public String getLastPathSegment() {
            List<String> segments = getPathSegments();
            int size = segments.size();
            if (size == 0) {
                return null;
            }
            return segments.get(size - 1);
        }

        private synchronized Part getUserInfoPart() {
            if (this.userInfo == null) {
                Part fromEncoded = Part.fromEncoded(parseUserInfo());
                this.userInfo = fromEncoded;
                return fromEncoded;
            }
            return this.userInfo;
        }

        @Override // android.net.Uri
        public final String getEncodedUserInfo() {
            return getUserInfoPart().getEncoded();
        }

        private synchronized String parseUserInfo() {
            int end;
            String authority = getEncodedAuthority();
            if (authority == null || (end = authority.lastIndexOf(64)) == -1) {
                return null;
            }
            return authority.substring(0, end);
        }

        @Override // android.net.Uri
        public String getUserInfo() {
            return getUserInfoPart().getDecoded();
        }

        @Override // android.net.Uri
        public String getHost() {
            boolean cached = this.host != Uri.NOT_CACHED;
            if (cached) {
                return this.host;
            }
            String parseHost = parseHost();
            this.host = parseHost;
            return parseHost;
        }

        private synchronized String parseHost() {
            String encodedHost;
            String authority = getEncodedAuthority();
            if (authority == null) {
                return null;
            }
            int userInfoSeparator = authority.lastIndexOf(64);
            int portSeparator = authority.indexOf(58, userInfoSeparator);
            if (portSeparator == -1) {
                encodedHost = authority.substring(userInfoSeparator + 1);
            } else {
                encodedHost = authority.substring(userInfoSeparator + 1, portSeparator);
            }
            return decode(encodedHost);
        }

        @Override // android.net.Uri
        public int getPort() {
            if (this.port == -2) {
                int parsePort = parsePort();
                this.port = parsePort;
                return parsePort;
            }
            return this.port;
        }

        private synchronized int parsePort() {
            String authority = getEncodedAuthority();
            if (authority == null) {
                return -1;
            }
            int userInfoSeparator = authority.lastIndexOf(64);
            int portSeparator = authority.indexOf(58, userInfoSeparator);
            if (portSeparator == -1) {
                return -1;
            }
            String portString = decode(authority.substring(portSeparator + 1));
            try {
                return Integer.parseInt(portString);
            } catch (NumberFormatException e) {
                Log.w(Uri.LOG, "Error parsing port string.", e);
                return -1;
            }
        }
    }

    /* JADX INFO: Access modifiers changed from: private */
    /* loaded from: classes2.dex */
    public static class HierarchicalUri extends AbstractHierarchicalUri {
        static final int TYPE_ID = 3;
        private final Part authority;
        private final Part fragment;
        private final PathPart path;
        private final Part query;
        private final String scheme;
        private Part ssp;
        private volatile String uriString;

        private synchronized HierarchicalUri(String scheme, Part authority, PathPart path, Part query, Part fragment) {
            super();
            this.uriString = Uri.NOT_CACHED;
            this.scheme = scheme;
            this.authority = Part.nonNull(authority);
            this.path = path == null ? PathPart.NULL : path;
            this.query = Part.nonNull(query);
            this.fragment = Part.nonNull(fragment);
        }

        static synchronized Uri readFrom(Parcel parcel) {
            return new HierarchicalUri(parcel.readString(), Part.readFrom(parcel), PathPart.readFrom(parcel), Part.readFrom(parcel), Part.readFrom(parcel));
        }

        @Override // android.os.Parcelable
        public int describeContents() {
            return 0;
        }

        @Override // android.os.Parcelable
        public void writeToParcel(Parcel parcel, int flags) {
            parcel.writeInt(3);
            parcel.writeString(this.scheme);
            this.authority.writeTo(parcel);
            this.path.writeTo(parcel);
            this.query.writeTo(parcel);
            this.fragment.writeTo(parcel);
        }

        @Override // android.net.Uri
        public boolean isHierarchical() {
            return true;
        }

        @Override // android.net.Uri
        public boolean isRelative() {
            return this.scheme == null;
        }

        @Override // android.net.Uri
        public String getScheme() {
            return this.scheme;
        }

        private synchronized Part getSsp() {
            if (this.ssp == null) {
                Part fromEncoded = Part.fromEncoded(makeSchemeSpecificPart());
                this.ssp = fromEncoded;
                return fromEncoded;
            }
            return this.ssp;
        }

        @Override // android.net.Uri
        public String getEncodedSchemeSpecificPart() {
            return getSsp().getEncoded();
        }

        @Override // android.net.Uri
        public String getSchemeSpecificPart() {
            return getSsp().getDecoded();
        }

        private synchronized String makeSchemeSpecificPart() {
            StringBuilder builder = new StringBuilder();
            appendSspTo(builder);
            return builder.toString();
        }

        private synchronized void appendSspTo(StringBuilder builder) {
            String encodedAuthority = this.authority.getEncoded();
            if (encodedAuthority != null) {
                builder.append("//");
                builder.append(encodedAuthority);
            }
            String encodedPath = this.path.getEncoded();
            if (encodedPath != null) {
                builder.append(encodedPath);
            }
            if (!this.query.isEmpty()) {
                builder.append('?');
                builder.append(this.query.getEncoded());
            }
        }

        @Override // android.net.Uri
        public String getAuthority() {
            return this.authority.getDecoded();
        }

        @Override // android.net.Uri
        public String getEncodedAuthority() {
            return this.authority.getEncoded();
        }

        @Override // android.net.Uri
        public String getEncodedPath() {
            return this.path.getEncoded();
        }

        @Override // android.net.Uri
        public String getPath() {
            return this.path.getDecoded();
        }

        @Override // android.net.Uri
        public String getQuery() {
            return this.query.getDecoded();
        }

        @Override // android.net.Uri
        public String getEncodedQuery() {
            return this.query.getEncoded();
        }

        @Override // android.net.Uri
        public String getFragment() {
            return this.fragment.getDecoded();
        }

        @Override // android.net.Uri
        public String getEncodedFragment() {
            return this.fragment.getEncoded();
        }

        @Override // android.net.Uri
        public List<String> getPathSegments() {
            return this.path.getPathSegments();
        }

        @Override // android.net.Uri
        public String toString() {
            boolean cached = this.uriString != Uri.NOT_CACHED;
            if (cached) {
                return this.uriString;
            }
            String makeUriString = makeUriString();
            this.uriString = makeUriString;
            return makeUriString;
        }

        private synchronized String makeUriString() {
            StringBuilder builder = new StringBuilder();
            if (this.scheme != null) {
                builder.append(this.scheme);
                builder.append(':');
            }
            appendSspTo(builder);
            if (!this.fragment.isEmpty()) {
                builder.append('#');
                builder.append(this.fragment.getEncoded());
            }
            return builder.toString();
        }

        @Override // android.net.Uri
        public Builder buildUpon() {
            return new Builder().scheme(this.scheme).authority(this.authority).path(this.path).query(this.query).fragment(this.fragment);
        }
    }

    /* loaded from: classes2.dex */
    public static final class Builder {
        private Part authority;
        private Part fragment;
        private Part opaquePart;
        private PathPart path;
        private Part query;
        private String scheme;

        public Builder scheme(String scheme) {
            this.scheme = scheme;
            return this;
        }

        synchronized Builder opaquePart(Part opaquePart) {
            this.opaquePart = opaquePart;
            return this;
        }

        public Builder opaquePart(String opaquePart) {
            return opaquePart(Part.fromDecoded(opaquePart));
        }

        public Builder encodedOpaquePart(String opaquePart) {
            return opaquePart(Part.fromEncoded(opaquePart));
        }

        synchronized Builder authority(Part authority) {
            this.opaquePart = null;
            this.authority = authority;
            return this;
        }

        public Builder authority(String authority) {
            return authority(Part.fromDecoded(authority));
        }

        public Builder encodedAuthority(String authority) {
            return authority(Part.fromEncoded(authority));
        }

        synchronized Builder path(PathPart path) {
            this.opaquePart = null;
            this.path = path;
            return this;
        }

        public Builder path(String path) {
            return path(PathPart.fromDecoded(path));
        }

        public Builder encodedPath(String path) {
            return path(PathPart.fromEncoded(path));
        }

        public Builder appendPath(String newSegment) {
            return path(PathPart.appendDecodedSegment(this.path, newSegment));
        }

        public Builder appendEncodedPath(String newSegment) {
            return path(PathPart.appendEncodedSegment(this.path, newSegment));
        }

        synchronized Builder query(Part query) {
            this.opaquePart = null;
            this.query = query;
            return this;
        }

        public Builder query(String query) {
            return query(Part.fromDecoded(query));
        }

        public Builder encodedQuery(String query) {
            return query(Part.fromEncoded(query));
        }

        synchronized Builder fragment(Part fragment) {
            this.fragment = fragment;
            return this;
        }

        public Builder fragment(String fragment) {
            return fragment(Part.fromDecoded(fragment));
        }

        public Builder encodedFragment(String fragment) {
            return fragment(Part.fromEncoded(fragment));
        }

        public Builder appendQueryParameter(String key, String value) {
            this.opaquePart = null;
            String encodedParameter = Uri.encode(key, null) + "=" + Uri.encode(value, null);
            if (this.query == null) {
                this.query = Part.fromEncoded(encodedParameter);
                return this;
            }
            String oldQuery = this.query.getEncoded();
            if (oldQuery == null || oldQuery.length() == 0) {
                this.query = Part.fromEncoded(encodedParameter);
            } else {
                this.query = Part.fromEncoded(oldQuery + "&" + encodedParameter);
            }
            return this;
        }

        public Builder clearQuery() {
            return query((Part) null);
        }

        public Uri build() {
            if (this.opaquePart != null) {
                if (this.scheme == null) {
                    throw new UnsupportedOperationException("An opaque URI must have a scheme.");
                }
                return new OpaqueUri(this.scheme, this.opaquePart, this.fragment);
            }
            PathPart path = this.path;
            if (path == null || path == PathPart.NULL) {
                path = PathPart.EMPTY;
            } else if (hasSchemeOrAuthority()) {
                path = PathPart.makeAbsolute(path);
            }
            return new HierarchicalUri(this.scheme, this.authority, path, this.query, this.fragment);
        }

        private synchronized boolean hasSchemeOrAuthority() {
            return (this.scheme == null && (this.authority == null || this.authority == Part.NULL)) ? false : true;
        }

        public String toString() {
            return build().toString();
        }
    }

    public Set<String> getQueryParameterNames() {
        if (isOpaque()) {
            throw new UnsupportedOperationException(NOT_HIERARCHICAL);
        }
        String query = getEncodedQuery();
        if (query == null) {
            return Collections.emptySet();
        }
        Set<String> names = new LinkedHashSet<>();
        int start = 0;
        do {
            int next = query.indexOf(38, start);
            int end = next == -1 ? query.length() : next;
            int separator = query.indexOf(61, start);
            if (separator > end || separator == -1) {
                separator = end;
            }
            String name = query.substring(start, separator);
            names.add(decode(name));
            start = end + 1;
        } while (start < query.length());
        return Collections.unmodifiableSet(names);
    }

    public List<String> getQueryParameters(String key) {
        if (isOpaque()) {
            throw new UnsupportedOperationException(NOT_HIERARCHICAL);
        }
        if (key == null) {
            throw new NullPointerException("key");
        }
        String query = getEncodedQuery();
        if (query == null) {
            return Collections.emptyList();
        }
        try {
            String encodedKey = URLEncoder.encode(key, DEFAULT_ENCODING);
            ArrayList<String> values = new ArrayList<>();
            int start = 0;
            while (true) {
                int nextAmpersand = query.indexOf(38, start);
                int end = nextAmpersand != -1 ? nextAmpersand : query.length();
                int separator = query.indexOf(61, start);
                if (separator > end || separator == -1) {
                    separator = end;
                }
                if (separator - start == encodedKey.length() && query.regionMatches(start, encodedKey, 0, encodedKey.length())) {
                    if (separator == end) {
                        values.add("");
                    } else {
                        values.add(decode(query.substring(separator + 1, end)));
                    }
                }
                if (nextAmpersand != -1) {
                    start = nextAmpersand + 1;
                } else {
                    return Collections.unmodifiableList(values);
                }
            }
        } catch (UnsupportedEncodingException e) {
            throw new AssertionError(e);
        }
    }

    public String getQueryParameter(String key) {
        if (isOpaque()) {
            throw new UnsupportedOperationException(NOT_HIERARCHICAL);
        }
        if (key == null) {
            throw new NullPointerException("key");
        }
        String query = getEncodedQuery();
        if (query == null) {
            return null;
        }
        String encodedKey = encode(key, null);
        int length = query.length();
        int start = 0;
        while (true) {
            int nextAmpersand = query.indexOf(38, start);
            int end = nextAmpersand != -1 ? nextAmpersand : length;
            int separator = query.indexOf(61, start);
            if (separator > end || separator == -1) {
                separator = end;
            }
            if (separator - start == encodedKey.length() && query.regionMatches(start, encodedKey, 0, encodedKey.length())) {
                if (separator == end) {
                    return "";
                }
                String encodedValue = query.substring(separator + 1, end);
                return UriCodec.decode(encodedValue, true, StandardCharsets.UTF_8, false);
            } else if (nextAmpersand == -1) {
                return null;
            } else {
                start = nextAmpersand + 1;
            }
        }
    }

    public boolean getBooleanQueryParameter(String key, boolean defaultValue) {
        String flag = getQueryParameter(key);
        if (flag == null) {
            return defaultValue;
        }
        String flag2 = flag.toLowerCase(Locale.ROOT);
        return ("false".equals(flag2) || "0".equals(flag2)) ? false : true;
    }

    public Uri normalizeScheme() {
        String scheme = getScheme();
        if (scheme == null) {
            return this;
        }
        String lowerScheme = scheme.toLowerCase(Locale.ROOT);
        return scheme.equals(lowerScheme) ? this : buildUpon().scheme(lowerScheme).build();
    }

    public static void writeToParcel(Parcel out, Uri uri) {
        if (uri == null) {
            out.writeInt(0);
        } else {
            uri.writeToParcel(out, 0);
        }
    }

    public static String encode(String s) {
        return encode(s, null);
    }

    public static String encode(String s, String allow) {
        if (s == null) {
            return null;
        }
        int oldLength = s.length();
        StringBuilder encoded = null;
        int current = 0;
        while (current < oldLength) {
            int nextToEncode = current;
            while (nextToEncode < oldLength && isAllowed(s.charAt(nextToEncode), allow)) {
                nextToEncode++;
            }
            if (nextToEncode == oldLength) {
                if (current == 0) {
                    return s;
                }
                encoded.append((CharSequence) s, current, oldLength);
                return encoded.toString();
            }
            if (encoded == null) {
                encoded = new StringBuilder();
            }
            if (nextToEncode > current) {
                encoded.append((CharSequence) s, current, nextToEncode);
            }
            int current2 = nextToEncode;
            int nextAllowed = current2 + 1;
            while (nextAllowed < oldLength && !isAllowed(s.charAt(nextAllowed), allow)) {
                nextAllowed++;
            }
            String toEncode = s.substring(current2, nextAllowed);
            try {
                byte[] bytes = toEncode.getBytes(DEFAULT_ENCODING);
                int bytesLength = bytes.length;
                for (int i = 0; i < bytesLength; i++) {
                    encoded.append('%');
                    encoded.append(HEX_DIGITS[(bytes[i] & 240) >> 4]);
                    encoded.append(HEX_DIGITS[bytes[i] & MidiConstants.STATUS_CHANNEL_MASK]);
                }
                current = nextAllowed;
            } catch (UnsupportedEncodingException e) {
                throw new AssertionError(e);
            }
        }
        return encoded == null ? s : encoded.toString();
    }

    private static synchronized boolean isAllowed(char c, String allow) {
        return (c >= 'A' && c <= 'Z') || (c >= 'a' && c <= 'z') || !((c < '0' || c > '9') && "_-!.~'()*".indexOf(c) == -1 && (allow == null || allow.indexOf(c) == -1));
    }

    public static String decode(String s) {
        if (s == null) {
            return null;
        }
        return UriCodec.decode(s, false, StandardCharsets.UTF_8, false);
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    /* loaded from: classes2.dex */
    public static abstract class AbstractPart {
        volatile String decoded;
        volatile String encoded;

        abstract synchronized String getEncoded();

        /* loaded from: classes2.dex */
        static class Representation {
            static final int BOTH = 0;
            static final int DECODED = 2;
            static final int ENCODED = 1;

            synchronized Representation() {
            }
        }

        synchronized AbstractPart(String encoded, String decoded) {
            this.encoded = encoded;
            this.decoded = decoded;
        }

        final synchronized String getDecoded() {
            boolean hasDecoded = this.decoded != Uri.NOT_CACHED;
            if (hasDecoded) {
                return this.decoded;
            }
            String decode = Uri.decode(this.encoded);
            this.decoded = decode;
            return decode;
        }

        final synchronized void writeTo(Parcel parcel) {
            boolean hasEncoded = this.encoded != Uri.NOT_CACHED;
            boolean hasDecoded = this.decoded != Uri.NOT_CACHED;
            if (hasEncoded && hasDecoded) {
                parcel.writeInt(0);
                parcel.writeString(this.encoded);
                parcel.writeString(this.decoded);
            } else if (hasEncoded) {
                parcel.writeInt(1);
                parcel.writeString(this.encoded);
            } else if (hasDecoded) {
                parcel.writeInt(2);
                parcel.writeString(this.decoded);
            } else {
                throw new IllegalArgumentException("Neither encoded nor decoded");
            }
        }
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    /* loaded from: classes2.dex */
    public static class Part extends AbstractPart {
        static final Part NULL = new EmptyPart(null);
        static final Part EMPTY = new EmptyPart("");

        private synchronized Part(String encoded, String decoded) {
            super(encoded, decoded);
        }

        synchronized boolean isEmpty() {
            return false;
        }

        @Override // android.net.Uri.AbstractPart
        synchronized String getEncoded() {
            boolean hasEncoded = this.encoded != Uri.NOT_CACHED;
            if (hasEncoded) {
                return this.encoded;
            }
            String encode = Uri.encode(this.decoded);
            this.encoded = encode;
            return encode;
        }

        static synchronized Part readFrom(Parcel parcel) {
            int representation = parcel.readInt();
            switch (representation) {
                case 0:
                    return from(parcel.readString(), parcel.readString());
                case 1:
                    return fromEncoded(parcel.readString());
                case 2:
                    return fromDecoded(parcel.readString());
                default:
                    throw new IllegalArgumentException("Unknown representation: " + representation);
            }
        }

        static synchronized Part nonNull(Part part) {
            return part == null ? NULL : part;
        }

        static synchronized Part fromEncoded(String encoded) {
            return from(encoded, Uri.NOT_CACHED);
        }

        static synchronized Part fromDecoded(String decoded) {
            return from(Uri.NOT_CACHED, decoded);
        }

        static synchronized Part from(String encoded, String decoded) {
            if (encoded == null) {
                return NULL;
            }
            if (encoded.length() == 0) {
                return EMPTY;
            }
            if (decoded == null) {
                return NULL;
            }
            if (decoded.length() == 0) {
                return EMPTY;
            }
            return new Part(encoded, decoded);
        }

        /* loaded from: classes2.dex */
        private static class EmptyPart extends Part {
            public synchronized EmptyPart(String value) {
                super(value, value);
            }

            @Override // android.net.Uri.Part
            synchronized boolean isEmpty() {
                return true;
            }
        }
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    /* loaded from: classes2.dex */
    public static class PathPart extends AbstractPart {
        private PathSegments pathSegments;
        static final PathPart NULL = new PathPart(null, null);
        static final PathPart EMPTY = new PathPart("", "");

        private synchronized PathPart(String encoded, String decoded) {
            super(encoded, decoded);
        }

        @Override // android.net.Uri.AbstractPart
        synchronized String getEncoded() {
            boolean hasEncoded = this.encoded != Uri.NOT_CACHED;
            if (hasEncoded) {
                return this.encoded;
            }
            String encode = Uri.encode(this.decoded, "/");
            this.encoded = encode;
            return encode;
        }

        synchronized PathSegments getPathSegments() {
            if (this.pathSegments != null) {
                return this.pathSegments;
            }
            String path = getEncoded();
            if (path == null) {
                PathSegments pathSegments = PathSegments.EMPTY;
                this.pathSegments = pathSegments;
                return pathSegments;
            }
            PathSegmentsBuilder segmentBuilder = new PathSegmentsBuilder();
            int previous = 0;
            while (true) {
                int current = path.indexOf(47, previous);
                if (current <= -1) {
                    break;
                }
                if (previous < current) {
                    String decodedSegment = Uri.decode(path.substring(previous, current));
                    segmentBuilder.add(decodedSegment);
                }
                previous = current + 1;
            }
            if (previous < path.length()) {
                segmentBuilder.add(Uri.decode(path.substring(previous)));
            }
            PathSegments build = segmentBuilder.build();
            this.pathSegments = build;
            return build;
        }

        static synchronized PathPart appendEncodedSegment(PathPart oldPart, String newSegment) {
            String newPath;
            if (oldPart == null) {
                return fromEncoded("/" + newSegment);
            }
            String oldPath = oldPart.getEncoded();
            if (oldPath == null) {
                oldPath = "";
            }
            int oldPathLength = oldPath.length();
            if (oldPathLength == 0) {
                newPath = "/" + newSegment;
            } else if (oldPath.charAt(oldPathLength - 1) == '/') {
                newPath = oldPath + newSegment;
            } else {
                newPath = oldPath + "/" + newSegment;
            }
            return fromEncoded(newPath);
        }

        static synchronized PathPart appendDecodedSegment(PathPart oldPart, String decoded) {
            String encoded = Uri.encode(decoded);
            return appendEncodedSegment(oldPart, encoded);
        }

        static synchronized PathPart readFrom(Parcel parcel) {
            int representation = parcel.readInt();
            switch (representation) {
                case 0:
                    return from(parcel.readString(), parcel.readString());
                case 1:
                    return fromEncoded(parcel.readString());
                case 2:
                    return fromDecoded(parcel.readString());
                default:
                    throw new IllegalArgumentException("Bad representation: " + representation);
            }
        }

        static synchronized PathPart fromEncoded(String encoded) {
            return from(encoded, Uri.NOT_CACHED);
        }

        static synchronized PathPart fromDecoded(String decoded) {
            return from(Uri.NOT_CACHED, decoded);
        }

        static synchronized PathPart from(String encoded, String decoded) {
            if (encoded == null) {
                return NULL;
            }
            if (encoded.length() == 0) {
                return EMPTY;
            }
            return new PathPart(encoded, decoded);
        }

        static synchronized PathPart makeAbsolute(PathPart oldPart) {
            String newEncoded;
            String newDecoded;
            boolean encodedCached = oldPart.encoded != Uri.NOT_CACHED;
            String oldPath = encodedCached ? oldPart.encoded : oldPart.decoded;
            if (oldPath == null || oldPath.length() == 0 || oldPath.startsWith("/")) {
                return oldPart;
            }
            if (!encodedCached) {
                newEncoded = Uri.NOT_CACHED;
            } else {
                newEncoded = "/" + oldPart.encoded;
            }
            boolean decodedCached = oldPart.decoded != Uri.NOT_CACHED;
            if (!decodedCached) {
                newDecoded = Uri.NOT_CACHED;
            } else {
                newDecoded = "/" + oldPart.decoded;
            }
            return new PathPart(newEncoded, newDecoded);
        }
    }

    public static Uri withAppendedPath(Uri baseUri, String pathSegment) {
        Builder builder = baseUri.buildUpon();
        return builder.appendEncodedPath(pathSegment).build();
    }

    /* JADX INFO: Access modifiers changed from: private */
    public Uri getCanonicalUri() {
        if (ContentResolver.SCHEME_FILE.equals(getScheme())) {
            try {
                String canonicalPath = new File(getPath()).getCanonicalPath();
                if (Environment.isExternalStorageEmulated()) {
                    String legacyPath = Environment.getLegacyExternalStorageDirectory().toString();
                    if (canonicalPath.startsWith(legacyPath)) {
                        return fromFile(new File(Environment.getExternalStorageDirectory().toString(), canonicalPath.substring(legacyPath.length() + 1)));
                    }
                }
                return fromFile(new File(canonicalPath));
            } catch (IOException e) {
                return this;
            }
        }
        return this;
    }

    public synchronized void checkFileUriExposed(String location) {
        if (ContentResolver.SCHEME_FILE.equals(getScheme()) && getPath() != null && !getPath().startsWith("/system/")) {
            StrictMode.onFileUriExposed(this, location);
        }
    }

    public synchronized void checkContentUriWithoutPermission(String location, int flags) {
        if ("content".equals(getScheme()) && !Intent.isAccessUriMode(flags)) {
            StrictMode.onContentUriWithoutPermission(this, location);
        }
    }

    public synchronized boolean isPathPrefixMatch(Uri prefix) {
        if (Objects.equals(getScheme(), prefix.getScheme()) && Objects.equals(getAuthority(), prefix.getAuthority())) {
            List<String> seg = getPathSegments();
            List<String> prefixSeg = prefix.getPathSegments();
            int prefixSize = prefixSeg.size();
            if (seg.size() < prefixSize) {
                return false;
            }
            for (int i = 0; i < prefixSize; i++) {
                if (!Objects.equals(seg.get(i), prefixSeg.get(i))) {
                    return false;
                }
            }
            return true;
        }
        return false;
    }
}
