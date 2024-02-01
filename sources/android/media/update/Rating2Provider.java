package android.media.update;

import android.os.Bundle;
/* loaded from: classes2.dex */
public interface Rating2Provider {
    /* JADX INFO: Access modifiers changed from: private */
    synchronized boolean equals_impl(Object obj);

    /* JADX INFO: Access modifiers changed from: private */
    synchronized float getPercentRating_impl();

    /* JADX INFO: Access modifiers changed from: private */
    synchronized int getRatingStyle_impl();

    /* JADX INFO: Access modifiers changed from: private */
    synchronized float getStarRating_impl();

    /* JADX INFO: Access modifiers changed from: private */
    synchronized boolean hasHeart_impl();

    /* JADX INFO: Access modifiers changed from: private */
    synchronized int hashCode_impl();

    /* JADX INFO: Access modifiers changed from: private */
    synchronized boolean isRated_impl();

    /* JADX INFO: Access modifiers changed from: private */
    synchronized boolean isThumbUp_impl();

    /* JADX INFO: Access modifiers changed from: private */
    synchronized Bundle toBundle_impl();

    /* JADX INFO: Access modifiers changed from: private */
    synchronized String toString_impl();
}
