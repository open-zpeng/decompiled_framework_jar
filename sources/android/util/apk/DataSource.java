package android.util.apk;

import java.io.IOException;
import java.security.DigestException;
/* loaded from: classes2.dex */
interface DataSource {
    void feedIntoDataDigester(DataDigester dataDigester, long j, int i) throws IOException, DigestException;

    long size();
}
