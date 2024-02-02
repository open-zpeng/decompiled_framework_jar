package com.android.internal.os;

import android.content.Context;
import android.content.res.Resources;
import android.content.res.XmlResourceParser;
import com.android.internal.R;
import com.android.internal.annotations.VisibleForTesting;
import com.android.internal.util.XmlUtils;
import com.xiaopeng.util.FeatureOption;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import org.xmlpull.v1.XmlPullParserException;
/* loaded from: classes3.dex */
public class PowerProfile {
    private static final String ATTR_NAME = "name";
    private static final String CPU_CLUSTER_POWER_COUNT = "cpu.cluster_power.cluster";
    private static final String CPU_CORE_POWER_PREFIX = "cpu.core_power.cluster";
    private static final String CPU_CORE_SPEED_PREFIX = "cpu.core_speeds.cluster";
    private static final String CPU_PER_CLUSTER_CORE_COUNT = "cpu.clusters.cores";
    public static final String POWER_AMBIENT_DISPLAY = "ambient.on";
    public static final String POWER_AUDIO = "audio";
    public static final String POWER_BATTERY_CAPACITY = "battery.capacity";
    @Deprecated
    public static final String POWER_BLUETOOTH_ACTIVE = "bluetooth.active";
    @Deprecated
    private protected static final String POWER_BLUETOOTH_AT_CMD = "bluetooth.at";
    public static final String POWER_BLUETOOTH_CONTROLLER_IDLE = "bluetooth.controller.idle";
    public static final String POWER_BLUETOOTH_CONTROLLER_OPERATING_VOLTAGE = "bluetooth.controller.voltage";
    public static final String POWER_BLUETOOTH_CONTROLLER_RX = "bluetooth.controller.rx";
    public static final String POWER_BLUETOOTH_CONTROLLER_TX = "bluetooth.controller.tx";
    @Deprecated
    private protected static final String POWER_BLUETOOTH_ON = "bluetooth.on";
    public static final String POWER_CAMERA = "camera.avg";
    private protected static final String POWER_CPU_ACTIVE = "cpu.active";
    private protected static final String POWER_CPU_IDLE = "cpu.idle";
    public static final String POWER_CPU_SUSPEND = "cpu.suspend";
    public static final String POWER_FLASHLIGHT = "camera.flashlight";
    private protected static final String POWER_GPS_ON = "gps.on";
    public static final String POWER_GPS_OPERATING_VOLTAGE = "gps.voltage";
    public static final String POWER_GPS_SIGNAL_QUALITY_BASED = "gps.signalqualitybased";
    public static final String POWER_MEMORY = "memory.bandwidths";
    public static final String POWER_MODEM_CONTROLLER_IDLE = "modem.controller.idle";
    public static final String POWER_MODEM_CONTROLLER_OPERATING_VOLTAGE = "modem.controller.voltage";
    public static final String POWER_MODEM_CONTROLLER_RX = "modem.controller.rx";
    public static final String POWER_MODEM_CONTROLLER_SLEEP = "modem.controller.sleep";
    public static final String POWER_MODEM_CONTROLLER_TX = "modem.controller.tx";
    private protected static final String POWER_RADIO_ACTIVE = "radio.active";
    private protected static final String POWER_RADIO_ON = "radio.on";
    private protected static final String POWER_RADIO_SCANNING = "radio.scanning";
    private protected static final String POWER_SCREEN_FULL = "screen.full";
    private protected static final String POWER_SCREEN_ON = "screen.on";
    public static final String POWER_VIDEO = "video";
    private protected static final String POWER_WIFI_ACTIVE = "wifi.active";
    public static final String POWER_WIFI_BATCHED_SCAN = "wifi.batchedscan";
    public static final String POWER_WIFI_CONTROLLER_IDLE = "wifi.controller.idle";
    public static final String POWER_WIFI_CONTROLLER_OPERATING_VOLTAGE = "wifi.controller.voltage";
    public static final String POWER_WIFI_CONTROLLER_RX = "wifi.controller.rx";
    public static final String POWER_WIFI_CONTROLLER_TX = "wifi.controller.tx";
    public static final String POWER_WIFI_CONTROLLER_TX_LEVELS = "wifi.controller.tx_levels";
    private protected static final String POWER_WIFI_ON = "wifi.on";
    private protected static final String POWER_WIFI_SCAN = "wifi.scan";
    private static final String TAG_ARRAY = "array";
    private static final String TAG_ARRAYITEM = "value";
    private static final String TAG_DEVICE = "device";
    private static final String TAG_ITEM = "item";
    private CpuClusterKey[] mCpuClusters;
    static final HashMap<String, Double> sPowerItemMap = new HashMap<>();
    static final HashMap<String, Double[]> sPowerArrayMap = new HashMap<>();
    private static final Object sLock = new Object();

    /* JADX INFO: Access modifiers changed from: private */
    @VisibleForTesting
    public PowerProfile(Context context) {
        this(context, false);
    }

    @VisibleForTesting
    public synchronized PowerProfile(Context context, boolean forTest) {
        synchronized (sLock) {
            if (sPowerItemMap.size() == 0 && sPowerArrayMap.size() == 0) {
                readPowerValuesFromXml(context, forTest);
            }
            initCpuClusters();
        }
    }

    private synchronized void readPowerValuesFromXml(Context context, boolean forTest) {
        double d;
        int value;
        int id = forTest ? R.xml.power_profile_test : 18284561;
        Resources resources = context.getResources();
        XmlResourceParser parser = resources.getXml(id);
        boolean parsingArray = false;
        ArrayList<Double> array = new ArrayList<>();
        String arrayName = null;
        try {
            try {
                XmlUtils.beginDocument(parser, "device");
                while (true) {
                    XmlUtils.nextElement(parser);
                    String element = parser.getName();
                    d = FeatureOption.FO_BOOT_POLICY_CPU;
                    if (element == null) {
                        break;
                    }
                    if (parsingArray && !element.equals("value")) {
                        sPowerArrayMap.put(arrayName, (Double[]) array.toArray(new Double[array.size()]));
                        parsingArray = false;
                    }
                    if (element.equals(TAG_ARRAY)) {
                        parsingArray = true;
                        array.clear();
                        String arrayName2 = parser.getAttributeValue(null, "name");
                        arrayName = arrayName2;
                    } else if (element.equals("item") || element.equals("value")) {
                        String name = null;
                        if (!parsingArray) {
                            name = parser.getAttributeValue(null, "name");
                        }
                        if (parser.next() == 4) {
                            String power = parser.getText();
                            double value2 = FeatureOption.FO_BOOT_POLICY_CPU;
                            try {
                                value2 = Double.valueOf(power).doubleValue();
                            } catch (NumberFormatException e) {
                            }
                            if (element.equals("item")) {
                                sPowerItemMap.put(name, Double.valueOf(value2));
                            } else if (parsingArray) {
                                array.add(Double.valueOf(value2));
                            }
                        }
                    }
                }
                if (parsingArray) {
                    sPowerArrayMap.put(arrayName, (Double[]) array.toArray(new Double[array.size()]));
                }
                parser.close();
                int[] configResIds = {R.integer.config_bluetooth_idle_cur_ma, R.integer.config_bluetooth_rx_cur_ma, R.integer.config_bluetooth_tx_cur_ma, R.integer.config_bluetooth_operating_voltage_mv};
                String[] configResIdKeys = {POWER_BLUETOOTH_CONTROLLER_IDLE, POWER_BLUETOOTH_CONTROLLER_RX, POWER_BLUETOOTH_CONTROLLER_TX, POWER_BLUETOOTH_CONTROLLER_OPERATING_VOLTAGE};
                int i = 0;
                while (i < configResIds.length) {
                    String key = configResIdKeys[i];
                    if ((!sPowerItemMap.containsKey(key) || sPowerItemMap.get(key).doubleValue() <= d) && (value = resources.getInteger(configResIds[i])) > 0) {
                        sPowerItemMap.put(key, Double.valueOf(value));
                    }
                    i++;
                    d = FeatureOption.FO_BOOT_POLICY_CPU;
                }
            } catch (IOException e2) {
                throw new RuntimeException(e2);
            } catch (XmlPullParserException e3) {
                throw new RuntimeException(e3);
            }
        } catch (Throwable th) {
            parser.close();
            throw th;
        }
    }

    private synchronized void initCpuClusters() {
        if (sPowerArrayMap.containsKey(CPU_PER_CLUSTER_CORE_COUNT)) {
            Double[] data = sPowerArrayMap.get(CPU_PER_CLUSTER_CORE_COUNT);
            this.mCpuClusters = new CpuClusterKey[data.length];
            for (int cluster = 0; cluster < data.length; cluster++) {
                int numCpusInCluster = (int) Math.round(data[cluster].doubleValue());
                this.mCpuClusters[cluster] = new CpuClusterKey(CPU_CORE_SPEED_PREFIX + cluster, CPU_CLUSTER_POWER_COUNT + cluster, CPU_CORE_POWER_PREFIX + cluster, numCpusInCluster);
            }
            return;
        }
        this.mCpuClusters = new CpuClusterKey[1];
        int numCpus = 1;
        if (sPowerItemMap.containsKey(CPU_PER_CLUSTER_CORE_COUNT)) {
            numCpus = (int) Math.round(sPowerItemMap.get(CPU_PER_CLUSTER_CORE_COUNT).doubleValue());
        }
        this.mCpuClusters[0] = new CpuClusterKey("cpu.core_speeds.cluster0", "cpu.cluster_power.cluster0", "cpu.core_power.cluster0", numCpus);
    }

    /* loaded from: classes3.dex */
    public static class CpuClusterKey {
        private final String clusterPowerKey;
        private final String corePowerKey;
        private final String freqKey;
        private final int numCpus;

        private synchronized CpuClusterKey(String freqKey, String clusterPowerKey, String corePowerKey, int numCpus) {
            this.freqKey = freqKey;
            this.clusterPowerKey = clusterPowerKey;
            this.corePowerKey = corePowerKey;
            this.numCpus = numCpus;
        }
    }

    /* JADX INFO: Access modifiers changed from: private */
    public int getNumCpuClusters() {
        return this.mCpuClusters.length;
    }

    public synchronized int getNumCoresInCpuCluster(int cluster) {
        return this.mCpuClusters[cluster].numCpus;
    }

    /* JADX INFO: Access modifiers changed from: private */
    public int getNumSpeedStepsInCpuCluster(int cluster) {
        if (cluster >= 0 && cluster < this.mCpuClusters.length) {
            if (sPowerArrayMap.containsKey(this.mCpuClusters[cluster].freqKey)) {
                return sPowerArrayMap.get(this.mCpuClusters[cluster].freqKey).length;
            }
            return 1;
        }
        return 0;
    }

    public synchronized double getAveragePowerForCpuCluster(int cluster) {
        if (cluster >= 0 && cluster < this.mCpuClusters.length) {
            return getAveragePower(this.mCpuClusters[cluster].clusterPowerKey);
        }
        return FeatureOption.FO_BOOT_POLICY_CPU;
    }

    public synchronized double getAveragePowerForCpuCore(int cluster, int step) {
        if (cluster >= 0 && cluster < this.mCpuClusters.length) {
            return getAveragePower(this.mCpuClusters[cluster].corePowerKey, step);
        }
        return FeatureOption.FO_BOOT_POLICY_CPU;
    }

    public synchronized int getNumElements(String key) {
        if (sPowerItemMap.containsKey(key)) {
            return 1;
        }
        if (sPowerArrayMap.containsKey(key)) {
            return sPowerArrayMap.get(key).length;
        }
        return 0;
    }

    public synchronized double getAveragePowerOrDefault(String type, double defaultValue) {
        if (sPowerItemMap.containsKey(type)) {
            return sPowerItemMap.get(type).doubleValue();
        }
        if (sPowerArrayMap.containsKey(type)) {
            return sPowerArrayMap.get(type)[0].doubleValue();
        }
        return defaultValue;
    }

    /* JADX INFO: Access modifiers changed from: private */
    public double getAveragePower(String type) {
        return getAveragePowerOrDefault(type, FeatureOption.FO_BOOT_POLICY_CPU);
    }

    /* JADX INFO: Access modifiers changed from: private */
    public double getAveragePower(String type, int level) {
        if (sPowerItemMap.containsKey(type)) {
            return sPowerItemMap.get(type).doubleValue();
        }
        if (sPowerArrayMap.containsKey(type)) {
            Double[] values = sPowerArrayMap.get(type);
            if (values.length <= level || level < 0) {
                return (level < 0 || values.length == 0) ? FeatureOption.FO_BOOT_POLICY_CPU : values[values.length - 1].doubleValue();
            }
            return values[level].doubleValue();
        }
        return FeatureOption.FO_BOOT_POLICY_CPU;
    }

    /* JADX INFO: Access modifiers changed from: private */
    public double getBatteryCapacity() {
        return getAveragePower(POWER_BATTERY_CAPACITY);
    }
}
