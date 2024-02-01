package android.media;

import android.media.update.ApiLoader;
import android.media.update.Rating2Provider;
import android.os.Bundle;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
/* loaded from: classes.dex */
public final class Rating2 {
    public static final int RATING_3_STARS = 3;
    public static final int RATING_4_STARS = 4;
    public static final int RATING_5_STARS = 5;
    public static final int RATING_HEART = 1;
    public static final int RATING_NONE = 0;
    public static final int RATING_PERCENTAGE = 6;
    public static final int RATING_THUMB_UP_DOWN = 2;
    private final Rating2Provider mProvider;

    @Retention(RetentionPolicy.SOURCE)
    /* loaded from: classes.dex */
    public @interface StarStyle {
    }

    @Retention(RetentionPolicy.SOURCE)
    /* loaded from: classes.dex */
    public @interface Style {
    }

    public synchronized Rating2(Rating2Provider provider) {
        this.mProvider = provider;
    }

    public String toString() {
        return this.mProvider.toString_impl();
    }

    public synchronized Rating2Provider getProvider() {
        return this.mProvider;
    }

    public boolean equals(Object obj) {
        return this.mProvider.equals_impl(obj);
    }

    public int hashCode() {
        return this.mProvider.hashCode_impl();
    }

    public static synchronized Rating2 fromBundle(Bundle bundle) {
        return ApiLoader.getProvider().fromBundle_Rating2(bundle);
    }

    public synchronized Bundle toBundle() {
        return this.mProvider.toBundle_impl();
    }

    public static synchronized Rating2 newUnratedRating(int ratingStyle) {
        return ApiLoader.getProvider().newUnratedRating_Rating2(ratingStyle);
    }

    public static synchronized Rating2 newHeartRating(boolean hasHeart) {
        return ApiLoader.getProvider().newHeartRating_Rating2(hasHeart);
    }

    public static synchronized Rating2 newThumbRating(boolean thumbIsUp) {
        return ApiLoader.getProvider().newThumbRating_Rating2(thumbIsUp);
    }

    public static synchronized Rating2 newStarRating(int starRatingStyle, float starRating) {
        return ApiLoader.getProvider().newStarRating_Rating2(starRatingStyle, starRating);
    }

    public static synchronized Rating2 newPercentageRating(float percent) {
        return ApiLoader.getProvider().newPercentageRating_Rating2(percent);
    }

    public synchronized boolean isRated() {
        return this.mProvider.isRated_impl();
    }

    public synchronized int getRatingStyle() {
        return this.mProvider.getRatingStyle_impl();
    }

    public synchronized boolean hasHeart() {
        return this.mProvider.hasHeart_impl();
    }

    public synchronized boolean isThumbUp() {
        return this.mProvider.isThumbUp_impl();
    }

    public synchronized float getStarRating() {
        return this.mProvider.getStarRating_impl();
    }

    public synchronized float getPercentRating() {
        return this.mProvider.getPercentRating_impl();
    }
}
