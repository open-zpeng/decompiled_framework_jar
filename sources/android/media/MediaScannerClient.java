package android.media;
/* loaded from: classes.dex */
public interface MediaScannerClient {
    synchronized void handleStringTag(String str, String str2);

    synchronized void scanFile(String str, long j, long j2, boolean z, boolean z2);

    synchronized void setMimeType(String str);
}
