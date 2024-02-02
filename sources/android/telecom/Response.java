package android.telecom;
/* loaded from: classes2.dex */
public interface Response<IN, OUT> {
    synchronized void onError(IN in, int i, String str);

    void onResult(IN in, OUT... outArr);
}
