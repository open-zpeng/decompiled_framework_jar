package android.media.update;

import android.os.Bundle;
/* loaded from: classes2.dex */
public interface SessionToken2Provider {
    /* JADX INFO: Access modifiers changed from: private */
    synchronized boolean equals_impl(Object obj);

    /* JADX INFO: Access modifiers changed from: private */
    synchronized String getId_imp();

    /* JADX INFO: Access modifiers changed from: private */
    synchronized String getPackageName_impl();

    /* JADX INFO: Access modifiers changed from: private */
    synchronized int getType_impl();

    /* JADX INFO: Access modifiers changed from: private */
    synchronized int getUid_impl();

    /* JADX INFO: Access modifiers changed from: private */
    synchronized int hashCode_impl();

    /* JADX INFO: Access modifiers changed from: private */
    synchronized Bundle toBundle_impl();

    /* JADX INFO: Access modifiers changed from: private */
    synchronized String toString_impl();
}
