package com.android.internal.telephony;

import android.telephony.Rlog;
import android.telephony.SmsManager;
import android.text.TextUtils;
import com.android.internal.telephony.CbGeoUtils;
import java.util.ArrayList;
import java.util.List;
import java.util.function.Function;
import java.util.function.Predicate;
import java.util.stream.Collectors;

/* loaded from: classes3.dex */
public class CbGeoUtils {
    private static final String CIRCLE_SYMBOL = "circle";
    public static final int EARTH_RADIUS_METER = 6371000;
    public static final double EPS = 1.0E-7d;
    public static final int GEOMETRY_TYPE_CIRCLE = 3;
    public static final int GEOMETRY_TYPE_POLYGON = 2;
    public static final int GEO_FENCING_MAXIMUM_WAIT_TIME = 1;
    private static final String POLYGON_SYMBOL = "polygon";
    private static final String TAG = "CbGeoUtils";

    /* loaded from: classes3.dex */
    public interface Geometry {
        boolean contains(LatLng latLng);
    }

    /* loaded from: classes3.dex */
    public static class LatLng {
        public final double lat;
        public final double lng;

        public LatLng(double lat, double lng) {
            this.lat = lat;
            this.lng = lng;
        }

        public LatLng subtract(LatLng p) {
            return new LatLng(this.lat - p.lat, this.lng - p.lng);
        }

        public double distance(LatLng p) {
            double dlat = Math.sin(Math.toRadians(this.lat - p.lat) * 0.5d);
            double dlng = Math.sin(Math.toRadians(this.lng - p.lng) * 0.5d);
            double x = (dlat * dlat) + (dlng * dlng * Math.cos(Math.toRadians(this.lat)) * Math.cos(Math.toRadians(p.lat)));
            return Math.atan2(Math.sqrt(x), Math.sqrt(1.0d - x)) * 2.0d * 6371000.0d;
        }

        public String toString() {
            return "(" + this.lat + SmsManager.REGEX_PREFIX_DELIMITER + this.lng + ")";
        }
    }

    /* loaded from: classes3.dex */
    public static class Polygon implements Geometry {
        private static final double SCALE = 1000.0d;
        private final LatLng mOrigin;
        private final List<Point> mScaledVertices;
        private final List<LatLng> mVertices;

        public Polygon(List<LatLng> vertices) {
            this.mVertices = vertices;
            int idx = 0;
            for (int i = 1; i < vertices.size(); i++) {
                if (vertices.get(i).lng < vertices.get(idx).lng) {
                    idx = i;
                }
            }
            this.mOrigin = vertices.get(idx);
            this.mScaledVertices = (List) vertices.stream().map(new Function() { // from class: com.android.internal.telephony.-$$Lambda$CbGeoUtils$Polygon$fpP3DlgZjn6sdCx41Ymo68J7c-Y
                @Override // java.util.function.Function
                public final Object apply(Object obj) {
                    return CbGeoUtils.Polygon.this.lambda$new$0$CbGeoUtils$Polygon((CbGeoUtils.LatLng) obj);
                }
            }).collect(Collectors.toList());
        }

        public List<LatLng> getVertices() {
            return this.mVertices;
        }

        @Override // com.android.internal.telephony.CbGeoUtils.Geometry
        public boolean contains(LatLng latLng) {
            Point p = lambda$new$0$CbGeoUtils$Polygon(latLng);
            int n = this.mScaledVertices.size();
            int windingNumber = 0;
            for (int i = 0; i < n; i++) {
                Point a = this.mScaledVertices.get(i);
                Point b = this.mScaledVertices.get((i + 1) % n);
                int ccw = CbGeoUtils.sign(crossProduct(b.subtract(a), p.subtract(a)));
                if (ccw == 0) {
                    if (Math.min(a.x, b.x) <= p.x && p.x <= Math.max(a.x, b.x) && Math.min(a.y, b.y) <= p.y && p.y <= Math.max(a.y, b.y)) {
                        return true;
                    }
                } else if (CbGeoUtils.sign(a.y - p.y) <= 0) {
                    if (ccw > 0 && CbGeoUtils.sign(b.y - p.y) > 0) {
                        windingNumber++;
                    }
                } else if (ccw < 0 && CbGeoUtils.sign(b.y - p.y) <= 0) {
                    windingNumber--;
                }
            }
            return windingNumber != 0;
        }

        /* JADX INFO: Access modifiers changed from: private */
        /* renamed from: convertAndScaleLatLng */
        public Point lambda$new$0$CbGeoUtils$Polygon(LatLng latLng) {
            double x = latLng.lat - this.mOrigin.lat;
            double y = latLng.lng - this.mOrigin.lng;
            if (CbGeoUtils.sign(this.mOrigin.lng) != 0 && CbGeoUtils.sign(this.mOrigin.lng) != CbGeoUtils.sign(latLng.lng)) {
                double distCross0thMeridian = Math.abs(this.mOrigin.lng) + Math.abs(latLng.lng);
                if (CbGeoUtils.sign((2.0d * distCross0thMeridian) - 360.0d) > 0) {
                    y = CbGeoUtils.sign(this.mOrigin.lng) * (360.0d - distCross0thMeridian);
                }
            }
            return new Point(x * SCALE, SCALE * y);
        }

        private static double crossProduct(Point a, Point b) {
            return (a.x * b.y) - (a.y * b.x);
        }

        /* JADX INFO: Access modifiers changed from: package-private */
        /* loaded from: classes3.dex */
        public static final class Point {
            public final double x;
            public final double y;

            Point(double x, double y) {
                this.x = x;
                this.y = y;
            }

            public Point subtract(Point p) {
                return new Point(this.x - p.x, this.y - p.y);
            }
        }
    }

    /* loaded from: classes3.dex */
    public static class Circle implements Geometry {
        private final LatLng mCenter;
        private final double mRadiusMeter;

        public Circle(LatLng center, double radiusMeter) {
            this.mCenter = center;
            this.mRadiusMeter = radiusMeter;
        }

        public LatLng getCenter() {
            return this.mCenter;
        }

        public double getRadius() {
            return this.mRadiusMeter;
        }

        @Override // com.android.internal.telephony.CbGeoUtils.Geometry
        public boolean contains(LatLng p) {
            return this.mCenter.distance(p) <= this.mRadiusMeter;
        }
    }

    public static List<Geometry> parseGeometriesFromString(String str) {
        String[] split;
        List<Geometry> geometries = new ArrayList<>();
        for (String geometryStr : str.split("\\s*;\\s*")) {
            String[] geoParameters = geometryStr.split("\\s*\\|\\s*");
            String str2 = geoParameters[0];
            char c = 65535;
            int hashCode = str2.hashCode();
            if (hashCode != -1360216880) {
                if (hashCode == -397519558 && str2.equals(POLYGON_SYMBOL)) {
                    c = 1;
                }
            } else if (str2.equals(CIRCLE_SYMBOL)) {
                c = 0;
            }
            if (c == 0) {
                geometries.add(new Circle(parseLatLngFromString(geoParameters[1]), Double.parseDouble(geoParameters[2])));
            } else if (c == 1) {
                List<LatLng> vertices = new ArrayList<>(geoParameters.length - 1);
                for (int i = 1; i < geoParameters.length; i++) {
                    vertices.add(parseLatLngFromString(geoParameters[i]));
                }
                geometries.add(new Polygon(vertices));
            } else {
                Rlog.e(TAG, "Invalid geometry format " + geometryStr);
            }
        }
        return geometries;
    }

    public static String encodeGeometriesToString(List<Geometry> geometries) {
        if (geometries == null || geometries.isEmpty()) {
            return "";
        }
        return (String) geometries.stream().map(new Function() { // from class: com.android.internal.telephony.-$$Lambda$CbGeoUtils$QlIbyDBTlCY-ub15gKQafGKuDBI
            @Override // java.util.function.Function
            public final Object apply(Object obj) {
                String encodeGeometryToString;
                encodeGeometryToString = CbGeoUtils.encodeGeometryToString((CbGeoUtils.Geometry) obj);
                return encodeGeometryToString;
            }
        }).filter(new Predicate() { // from class: com.android.internal.telephony.-$$Lambda$CbGeoUtils$zFCyjN0-HZvGA96MJHG2QIL00to
            @Override // java.util.function.Predicate
            public final boolean test(Object obj) {
                return CbGeoUtils.lambda$encodeGeometriesToString$1((String) obj);
            }
        }).collect(Collectors.joining(";"));
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public static /* synthetic */ boolean lambda$encodeGeometriesToString$1(String encodedStr) {
        return !TextUtils.isEmpty(encodedStr);
    }

    /* JADX INFO: Access modifiers changed from: private */
    public static String encodeGeometryToString(Geometry geometry) {
        StringBuilder sb = new StringBuilder();
        if (geometry instanceof Polygon) {
            sb.append(POLYGON_SYMBOL);
            for (LatLng latLng : ((Polygon) geometry).getVertices()) {
                sb.append("|");
                sb.append(latLng.lat);
                sb.append(SmsManager.REGEX_PREFIX_DELIMITER);
                sb.append(latLng.lng);
            }
        } else if (geometry instanceof Circle) {
            sb.append(CIRCLE_SYMBOL);
            Circle circle = (Circle) geometry;
            sb.append("|");
            sb.append(circle.getCenter().lat);
            sb.append(SmsManager.REGEX_PREFIX_DELIMITER);
            sb.append(circle.getCenter().lng);
            sb.append("|");
            sb.append(circle.getRadius());
        } else {
            Rlog.e(TAG, "Unsupported geometry object " + geometry);
            return null;
        }
        return sb.toString();
    }

    public static LatLng parseLatLngFromString(String str) {
        String[] latLng = str.split("\\s*,\\s*");
        return new LatLng(Double.parseDouble(latLng[0]), Double.parseDouble(latLng[1]));
    }

    public static int sign(double value) {
        if (value > 1.0E-7d) {
            return 1;
        }
        return value < -1.0E-7d ? -1 : 0;
    }
}
