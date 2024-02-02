package android.media;

import android.content.ContentResolver;
import android.content.Context;
import android.database.Cursor;
import android.net.Uri;
import android.os.Environment;
import android.os.FileUtils;
import android.util.Log;
import android.util.Pair;
import android.util.Range;
import android.util.Rational;
import android.util.Size;
import java.io.File;
import java.io.FileNotFoundException;
import java.util.Arrays;
import java.util.Comparator;
import java.util.Vector;
/* loaded from: classes.dex */
class Utils {
    private static final String TAG = "Utils";

    Utils() {
    }

    public static <T extends Comparable<? super T>> void sortDistinctRanges(Range<T>[] ranges) {
        Arrays.sort(ranges, new Comparator<Range<T>>() { // from class: android.media.Utils.1
            @Override // java.util.Comparator
            public /* bridge */ /* synthetic */ int compare(Object obj, Object obj2) {
                return compare((Range) ((Range) obj), (Range) ((Range) obj2));
            }

            /* JADX WARN: Type inference failed for: r0v0, types: [java.lang.Comparable] */
            /* JADX WARN: Type inference failed for: r0v2, types: [java.lang.Comparable] */
            public int compare(Range<T> lhs, Range<T> rhs) {
                if (lhs.getUpper().compareTo(rhs.getLower()) < 0) {
                    return -1;
                }
                if (lhs.getLower().compareTo(rhs.getUpper()) > 0) {
                    return 1;
                }
                throw new IllegalArgumentException("sample rate ranges must be distinct (" + lhs + " and " + rhs + ")");
            }
        });
    }

    public static <T extends Comparable<? super T>> Range<T>[] intersectSortedDistinctRanges(Range<T>[] one, Range<T>[] another) {
        int ix = 0;
        Vector<Range<T>> result = new Vector<>();
        for (Range<T> range : another) {
            while (ix < one.length && one[ix].getUpper().compareTo(range.getLower()) < 0) {
                ix++;
            }
            while (ix < one.length && one[ix].getUpper().compareTo(range.getUpper()) < 0) {
                result.add(range.intersect(one[ix]));
                ix++;
            }
            if (ix == one.length) {
                break;
            }
            if (one[ix].getLower().compareTo(range.getUpper()) <= 0) {
                result.add(range.intersect(one[ix]));
            }
        }
        return (Range[]) result.toArray(new Range[result.size()]);
    }

    public static <T extends Comparable<? super T>> int binarySearchDistinctRanges(Range<T>[] ranges, T value) {
        return Arrays.binarySearch(ranges, Range.create(value, value), new Comparator<Range<T>>() { // from class: android.media.Utils.2
            @Override // java.util.Comparator
            public /* bridge */ /* synthetic */ int compare(Object obj, Object obj2) {
                return compare((Range) ((Range) obj), (Range) ((Range) obj2));
            }

            /* JADX WARN: Type inference failed for: r0v0, types: [java.lang.Comparable] */
            /* JADX WARN: Type inference failed for: r0v2, types: [java.lang.Comparable] */
            public int compare(Range<T> lhs, Range<T> rhs) {
                if (lhs.getUpper().compareTo(rhs.getLower()) < 0) {
                    return -1;
                }
                if (lhs.getLower().compareTo(rhs.getUpper()) > 0) {
                    return 1;
                }
                return 0;
            }
        });
    }

    static int gcd(int a, int b) {
        if (a == 0 && b == 0) {
            return 1;
        }
        if (b < 0) {
            b = -b;
        }
        if (a < 0) {
            a = -a;
        }
        while (a != 0) {
            int c = b % a;
            b = a;
            a = c;
        }
        return b;
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public static Range<Integer> factorRange(Range<Integer> range, int factor) {
        if (factor == 1) {
            return range;
        }
        return Range.create(Integer.valueOf(divUp(range.getLower().intValue(), factor)), Integer.valueOf(range.getUpper().intValue() / factor));
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public static Range<Long> factorRange(Range<Long> range, long factor) {
        if (factor == 1) {
            return range;
        }
        return Range.create(Long.valueOf(divUp(range.getLower().longValue(), factor)), Long.valueOf(range.getUpper().longValue() / factor));
    }

    private static Rational scaleRatio(Rational ratio, int num, int den) {
        int common = gcd(num, den);
        return new Rational((int) (ratio.getNumerator() * (num / common)), (int) (ratio.getDenominator() * (den / common)));
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public static Range<Rational> scaleRange(Range<Rational> range, int num, int den) {
        if (num == den) {
            return range;
        }
        return Range.create(scaleRatio(range.getLower(), num, den), scaleRatio(range.getUpper(), num, den));
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public static Range<Integer> alignRange(Range<Integer> range, int align) {
        return range.intersect(Integer.valueOf(divUp(range.getLower().intValue(), align) * align), Integer.valueOf((range.getUpper().intValue() / align) * align));
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public static int divUp(int num, int den) {
        return ((num + den) - 1) / den;
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public static long divUp(long num, long den) {
        return ((num + den) - 1) / den;
    }

    private static long lcm(int a, int b) {
        if (a == 0 || b == 0) {
            throw new IllegalArgumentException("lce is not defined for zero arguments");
        }
        return (a * b) / gcd(a, b);
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public static Range<Integer> intRangeFor(double v) {
        return Range.create(Integer.valueOf((int) v), Integer.valueOf((int) Math.ceil(v)));
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public static Range<Long> longRangeFor(double v) {
        return Range.create(Long.valueOf((long) v), Long.valueOf((long) Math.ceil(v)));
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public static Size parseSize(Object o, Size fallback) {
        try {
            return Size.parseSize((String) o);
        } catch (ClassCastException e) {
            Log.w(TAG, "could not parse size '" + o + "'");
            return fallback;
        } catch (NullPointerException e2) {
            return fallback;
        } catch (NumberFormatException e3) {
            Log.w(TAG, "could not parse size '" + o + "'");
            return fallback;
        }
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public static int parseIntSafely(Object o, int fallback) {
        if (o == null) {
            return fallback;
        }
        try {
            String s = (String) o;
            return Integer.parseInt(s);
        } catch (ClassCastException e) {
            Log.w(TAG, "could not parse integer '" + o + "'");
            return fallback;
        } catch (NullPointerException e2) {
            return fallback;
        } catch (NumberFormatException e3) {
            Log.w(TAG, "could not parse integer '" + o + "'");
            return fallback;
        }
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public static Range<Integer> parseIntRange(Object o, Range<Integer> fallback) {
        try {
            String s = (String) o;
            int ix = s.indexOf(45);
            if (ix >= 0) {
                return Range.create(Integer.valueOf(Integer.parseInt(s.substring(0, ix), 10)), Integer.valueOf(Integer.parseInt(s.substring(ix + 1), 10)));
            }
            int value = Integer.parseInt(s);
            return Range.create(Integer.valueOf(value), Integer.valueOf(value));
        } catch (ClassCastException e) {
            Log.w(TAG, "could not parse integer range '" + o + "'");
            return fallback;
        } catch (NullPointerException e2) {
            return fallback;
        } catch (NumberFormatException e3) {
            Log.w(TAG, "could not parse integer range '" + o + "'");
            return fallback;
        } catch (IllegalArgumentException e4) {
            Log.w(TAG, "could not parse integer range '" + o + "'");
            return fallback;
        }
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public static Range<Long> parseLongRange(Object o, Range<Long> fallback) {
        try {
            String s = (String) o;
            int ix = s.indexOf(45);
            if (ix >= 0) {
                return Range.create(Long.valueOf(Long.parseLong(s.substring(0, ix), 10)), Long.valueOf(Long.parseLong(s.substring(ix + 1), 10)));
            }
            long value = Long.parseLong(s);
            return Range.create(Long.valueOf(value), Long.valueOf(value));
        } catch (ClassCastException e) {
            Log.w(TAG, "could not parse long range '" + o + "'");
            return fallback;
        } catch (NullPointerException e2) {
            return fallback;
        } catch (NumberFormatException e3) {
            Log.w(TAG, "could not parse long range '" + o + "'");
            return fallback;
        } catch (IllegalArgumentException e4) {
            Log.w(TAG, "could not parse long range '" + o + "'");
            return fallback;
        }
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public static Range<Rational> parseRationalRange(Object o, Range<Rational> fallback) {
        try {
            String s = (String) o;
            int ix = s.indexOf(45);
            if (ix >= 0) {
                return Range.create(Rational.parseRational(s.substring(0, ix)), Rational.parseRational(s.substring(ix + 1)));
            }
            Rational value = Rational.parseRational(s);
            return Range.create(value, value);
        } catch (ClassCastException e) {
            Log.w(TAG, "could not parse rational range '" + o + "'");
            return fallback;
        } catch (NumberFormatException e2) {
            Log.w(TAG, "could not parse rational range '" + o + "'");
            return fallback;
        } catch (IllegalArgumentException e3) {
            Log.w(TAG, "could not parse rational range '" + o + "'");
            return fallback;
        } catch (NullPointerException e4) {
            return fallback;
        }
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public static Pair<Size, Size> parseSizeRange(Object o) {
        try {
            String s = (String) o;
            int ix = s.indexOf(45);
            if (ix >= 0) {
                return Pair.create(Size.parseSize(s.substring(0, ix)), Size.parseSize(s.substring(ix + 1)));
            }
            Size value = Size.parseSize(s);
            return Pair.create(value, value);
        } catch (ClassCastException e) {
            Log.w(TAG, "could not parse size range '" + o + "'");
            return null;
        } catch (IllegalArgumentException e2) {
            Log.w(TAG, "could not parse size range '" + o + "'");
            return null;
        } catch (NullPointerException e3) {
            return null;
        } catch (NumberFormatException e4) {
            Log.w(TAG, "could not parse size range '" + o + "'");
            return null;
        }
    }

    public static File getUniqueExternalFile(Context context, String subdirectory, String fileName, String mimeType) {
        File externalStorage = Environment.getExternalStoragePublicDirectory(subdirectory);
        externalStorage.mkdirs();
        try {
            File outFile = FileUtils.buildUniqueFile(externalStorage, mimeType, fileName);
            return outFile;
        } catch (FileNotFoundException e) {
            Log.e(TAG, "Unable to get a unique file name: " + e);
            return null;
        }
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public static String getFileDisplayNameFromUri(Context context, Uri uri) {
        String scheme = uri.getScheme();
        if (ContentResolver.SCHEME_FILE.equals(scheme)) {
            return uri.getLastPathSegment();
        }
        if ("content".equals(scheme)) {
            String[] proj = {"_display_name"};
            Cursor cursor = context.getContentResolver().query(uri, proj, null, null, null);
            if (cursor != null) {
                try {
                    if (cursor.getCount() != 0) {
                        cursor.moveToFirst();
                        String string = cursor.getString(cursor.getColumnIndex("_display_name"));
                        if (cursor != null) {
                            cursor.close();
                        }
                        return string;
                    }
                } catch (Throwable th) {
                    try {
                        throw th;
                    } catch (Throwable th2) {
                        if (cursor != null) {
                            if (th != null) {
                                try {
                                    cursor.close();
                                } catch (Throwable th3) {
                                    th.addSuppressed(th3);
                                }
                            } else {
                                cursor.close();
                            }
                        }
                        throw th2;
                    }
                }
            }
            if (cursor != null) {
                cursor.close();
            }
        }
        return uri.toString();
    }
}
