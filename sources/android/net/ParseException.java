package android.net;

/* loaded from: classes2.dex */
public class ParseException extends RuntimeException {
    public String response;

    /* JADX INFO: Access modifiers changed from: package-private */
    public ParseException(String response) {
        super(response);
        this.response = response;
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public ParseException(String response, Throwable cause) {
        super(response, cause);
        this.response = response;
    }
}
