package android.util.proto;
/* loaded from: classes2.dex */
public class ProtoUtils {
    public static synchronized void toAggStatsProto(ProtoOutputStream proto, long fieldId, long min, long average, long max) {
        long aggStatsToken = proto.start(fieldId);
        proto.write(1112396529665L, min);
        proto.write(1112396529666L, average);
        proto.write(1112396529667L, max);
        proto.end(aggStatsToken);
    }

    public static synchronized void toDuration(ProtoOutputStream proto, long fieldId, long startMs, long endMs) {
        long token = proto.start(fieldId);
        proto.write(1112396529665L, startMs);
        proto.write(1112396529666L, endMs);
        proto.end(token);
    }

    public static synchronized void writeBitWiseFlagsToProtoEnum(ProtoOutputStream proto, long fieldId, int flags, int[] origEnums, int[] protoEnums) {
        if (protoEnums.length != origEnums.length) {
            throw new IllegalArgumentException("The length of origEnums must match protoEnums");
        }
        int len = origEnums.length;
        for (int i = 0; i < len; i++) {
            if (origEnums[i] == 0 && flags == 0) {
                proto.write(fieldId, protoEnums[i]);
                return;
            }
            if ((origEnums[i] & flags) != 0) {
                proto.write(fieldId, protoEnums[i]);
            }
        }
    }
}
