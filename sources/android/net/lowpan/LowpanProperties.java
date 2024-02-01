package android.net.lowpan;
/* loaded from: classes2.dex */
public final class LowpanProperties {
    private protected static final LowpanProperty<int[]> KEY_CHANNEL_MASK = new LowpanStandardProperty("android.net.lowpan.property.CHANNEL_MASK", int[].class);
    private protected static final LowpanProperty<Integer> KEY_MAX_TX_POWER = new LowpanStandardProperty("android.net.lowpan.property.MAX_TX_POWER", Integer.class);

    /* loaded from: classes2.dex */
    static final class LowpanStandardProperty<T> extends LowpanProperty<T> {
        public protected final String mName;
        public protected final Class<T> mType;

        public private protected synchronized LowpanStandardProperty(String name, Class<T> type) {
            this.mName = name;
            this.mType = type;
        }

        private protected synchronized String getName() {
            return this.mName;
        }

        private protected synchronized Class<T> getType() {
            return this.mType;
        }

        public String toString() {
            return getName();
        }
    }
}
