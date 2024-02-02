package android.media;

import android.app.slice.Slice;
import android.media.SubtitleTrack;
import android.net.wifi.WifiEnterpriseConfig;
import android.provider.Telephony;
import java.util.Arrays;
/* JADX INFO: Access modifiers changed from: package-private */
/* compiled from: WebVttRenderer.java */
/* loaded from: classes.dex */
public class TextTrackCue extends SubtitleTrack.Cue {
    static final int ALIGNMENT_END = 202;
    static final int ALIGNMENT_LEFT = 203;
    static final int ALIGNMENT_MIDDLE = 200;
    static final int ALIGNMENT_RIGHT = 204;
    static final int ALIGNMENT_START = 201;
    private static final String TAG = "TTCue";
    static final int WRITING_DIRECTION_HORIZONTAL = 100;
    static final int WRITING_DIRECTION_VERTICAL_LR = 102;
    static final int WRITING_DIRECTION_VERTICAL_RL = 101;
    boolean mAutoLinePosition;
    String[] mStrings;
    String mId = "";
    boolean mPauseOnExit = false;
    int mWritingDirection = 100;
    String mRegionId = "";
    boolean mSnapToLines = true;
    Integer mLinePosition = null;
    int mTextPosition = 50;
    int mSize = 100;
    int mAlignment = 200;
    TextTrackCueSpan[][] mLines = null;
    TextTrackRegion mRegion = null;

    public boolean equals(Object o) {
        if (o instanceof TextTrackCue) {
            if (this == o) {
                return true;
            }
            try {
                TextTrackCue cue = (TextTrackCue) o;
                boolean res = this.mId.equals(cue.mId) && this.mPauseOnExit == cue.mPauseOnExit && this.mWritingDirection == cue.mWritingDirection && this.mRegionId.equals(cue.mRegionId) && this.mSnapToLines == cue.mSnapToLines && this.mAutoLinePosition == cue.mAutoLinePosition && (this.mAutoLinePosition || ((this.mLinePosition != null && this.mLinePosition.equals(cue.mLinePosition)) || (this.mLinePosition == null && cue.mLinePosition == null))) && this.mTextPosition == cue.mTextPosition && this.mSize == cue.mSize && this.mAlignment == cue.mAlignment && this.mLines.length == cue.mLines.length;
                if (res) {
                    for (int line = 0; line < this.mLines.length; line++) {
                        if (!Arrays.equals(this.mLines[line], cue.mLines[line])) {
                            return false;
                        }
                    }
                }
                return res;
            } catch (IncompatibleClassChangeError e) {
                return false;
            }
        }
        return false;
    }

    public synchronized StringBuilder appendStringsToBuilder(StringBuilder builder) {
        String[] strArr;
        if (this.mStrings == null) {
            builder.append("null");
        } else {
            builder.append("[");
            boolean first = true;
            for (String s : this.mStrings) {
                if (!first) {
                    builder.append(", ");
                }
                if (s == null) {
                    builder.append("null");
                } else {
                    builder.append("\"");
                    builder.append(s);
                    builder.append("\"");
                }
                first = false;
            }
            builder.append("]");
        }
        return builder;
    }

    public synchronized StringBuilder appendLinesToBuilder(StringBuilder builder) {
        TextTrackCueSpan[][] textTrackCueSpanArr;
        if (this.mLines == null) {
            builder.append("null");
        } else {
            builder.append("[");
            boolean first = true;
            for (TextTrackCueSpan[] spans : this.mLines) {
                if (!first) {
                    builder.append(", ");
                }
                if (spans == null) {
                    builder.append("null");
                } else {
                    builder.append("\"");
                    long lastTimestamp = -1;
                    boolean innerFirst = true;
                    for (TextTrackCueSpan span : spans) {
                        if (!innerFirst) {
                            builder.append(WifiEnterpriseConfig.CA_CERT_ALIAS_DELIMITER);
                        }
                        if (span.mTimestampMs != lastTimestamp) {
                            builder.append("<");
                            builder.append(WebVttParser.timeToString(span.mTimestampMs));
                            builder.append(">");
                            lastTimestamp = span.mTimestampMs;
                        }
                        builder.append(span.mText);
                        innerFirst = false;
                    }
                    builder.append("\"");
                }
                first = false;
            }
            builder.append("]");
        }
        return builder;
    }

    public String toString() {
        String str;
        String str2;
        StringBuilder res = new StringBuilder();
        res.append(WebVttParser.timeToString(this.mStartTimeMs));
        res.append(" --> ");
        res.append(WebVttParser.timeToString(this.mEndTimeMs));
        res.append(" {id:\"");
        res.append(this.mId);
        res.append("\", pauseOnExit:");
        res.append(this.mPauseOnExit);
        res.append(", direction:");
        if (this.mWritingDirection == 100) {
            str = Slice.HINT_HORIZONTAL;
        } else if (this.mWritingDirection == 102) {
            str = "vertical_lr";
        } else {
            str = this.mWritingDirection == 101 ? "vertical_rl" : "INVALID";
        }
        res.append(str);
        res.append(", regionId:\"");
        res.append(this.mRegionId);
        res.append("\", snapToLines:");
        res.append(this.mSnapToLines);
        res.append(", linePosition:");
        res.append(this.mAutoLinePosition ? "auto" : this.mLinePosition);
        res.append(", textPosition:");
        res.append(this.mTextPosition);
        res.append(", size:");
        res.append(this.mSize);
        res.append(", alignment:");
        if (this.mAlignment == 202) {
            str2 = "end";
        } else if (this.mAlignment == 203) {
            str2 = "left";
        } else if (this.mAlignment == 200) {
            str2 = "middle";
        } else if (this.mAlignment == 204) {
            str2 = "right";
        } else {
            str2 = this.mAlignment == 201 ? Telephony.BaseMmsColumns.START : "INVALID";
        }
        res.append(str2);
        res.append(", text:");
        appendStringsToBuilder(res).append("}");
        return res.toString();
    }

    public int hashCode() {
        return toString().hashCode();
    }

    @Override // android.media.SubtitleTrack.Cue
    public synchronized void onTime(long timeMs) {
        TextTrackCueSpan[][] textTrackCueSpanArr;
        for (TextTrackCueSpan[] line : this.mLines) {
            for (TextTrackCueSpan span : line) {
                span.mEnabled = timeMs >= span.mTimestampMs;
            }
        }
    }
}
