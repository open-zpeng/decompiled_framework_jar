package android.net;
/* loaded from: classes2.dex */
public class ParseException extends RuntimeException {
    public String response;

    /* JADX INFO: Access modifiers changed from: package-private */
    public synchronized ParseException(String response) {
        this.response = response;
    }
}
