package android.media.update;

import android.graphics.Bitmap;
import android.media.MediaMetadata2;
import android.media.Rating2;
import android.os.Bundle;
import java.util.Set;
/* loaded from: classes2.dex */
public interface MediaMetadata2Provider {

    /* loaded from: classes2.dex */
    public interface BuilderProvider {
        /* JADX INFO: Access modifiers changed from: private */
        synchronized MediaMetadata2 build_impl();

        /* JADX INFO: Access modifiers changed from: private */
        synchronized MediaMetadata2.Builder putBitmap_impl(String str, Bitmap bitmap);

        /* JADX INFO: Access modifiers changed from: private */
        synchronized MediaMetadata2.Builder putFloat_impl(String str, float f);

        /* JADX INFO: Access modifiers changed from: private */
        synchronized MediaMetadata2.Builder putLong_impl(String str, long j);

        /* JADX INFO: Access modifiers changed from: private */
        synchronized MediaMetadata2.Builder putRating_impl(String str, Rating2 rating2);

        /* JADX INFO: Access modifiers changed from: private */
        synchronized MediaMetadata2.Builder putString_impl(String str, String str2);

        /* JADX INFO: Access modifiers changed from: private */
        synchronized MediaMetadata2.Builder putText_impl(String str, CharSequence charSequence);

        /* JADX INFO: Access modifiers changed from: private */
        synchronized MediaMetadata2.Builder setExtras_impl(Bundle bundle);
    }

    /* JADX INFO: Access modifiers changed from: private */
    synchronized boolean containsKey_impl(String str);

    /* JADX INFO: Access modifiers changed from: private */
    synchronized Bitmap getBitmap_impl(String str);

    /* JADX INFO: Access modifiers changed from: private */
    synchronized Bundle getExtras_impl();

    /* JADX INFO: Access modifiers changed from: private */
    synchronized float getFloat_impl(String str);

    /* JADX INFO: Access modifiers changed from: private */
    synchronized long getLong_impl(String str);

    /* JADX INFO: Access modifiers changed from: private */
    synchronized String getMediaId_impl();

    /* JADX INFO: Access modifiers changed from: private */
    synchronized Rating2 getRating_impl(String str);

    /* JADX INFO: Access modifiers changed from: private */
    synchronized String getString_impl(String str);

    /* JADX INFO: Access modifiers changed from: private */
    synchronized CharSequence getText_impl(String str);

    /* JADX INFO: Access modifiers changed from: private */
    synchronized Set<String> keySet_impl();

    /* JADX INFO: Access modifiers changed from: private */
    synchronized int size_impl();

    /* JADX INFO: Access modifiers changed from: private */
    synchronized Bundle toBundle_impl();
}
