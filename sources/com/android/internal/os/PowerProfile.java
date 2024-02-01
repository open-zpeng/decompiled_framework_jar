package com.android.internal.os;

import android.annotation.UnsupportedAppUsage;
import android.content.Context;
import android.content.res.Resources;
import android.content.res.XmlResourceParser;
import android.util.proto.ProtoOutputStream;
import com.android.internal.R;
import com.android.internal.annotations.VisibleForTesting;
import com.android.internal.os.PowerProfileProto;
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
    @UnsupportedAppUsage
    @Deprecated
    public static final String POWER_BLUETOOTH_AT_CMD = "bluetooth.at";
    public static final String POWER_BLUETOOTH_CONTROLLER_IDLE = "bluetooth.controller.idle";
    public static final String POWER_BLUETOOTH_CONTROLLER_OPERATING_VOLTAGE = "bluetooth.controller.voltage";
    public static final String POWER_BLUETOOTH_CONTROLLER_RX = "bluetooth.controller.rx";
    public static final String POWER_BLUETOOTH_CONTROLLER_TX = "bluetooth.controller.tx";
    @UnsupportedAppUsage
    @Deprecated
    public static final String POWER_BLUETOOTH_ON = "bluetooth.on";
    public static final String POWER_CAMERA = "camera.avg";
    @UnsupportedAppUsage
    public static final String POWER_CPU_ACTIVE = "cpu.active";
    @UnsupportedAppUsage
    public static final String POWER_CPU_IDLE = "cpu.idle";
    public static final String POWER_CPU_SUSPEND = "cpu.suspend";
    public static final String POWER_FLASHLIGHT = "camera.flashlight";
    @UnsupportedAppUsage
    public static final String POWER_GPS_ON = "gps.on";
    public static final String POWER_GPS_OPERATING_VOLTAGE = "gps.voltage";
    public static final String POWER_GPS_SIGNAL_QUALITY_BASED = "gps.signalqualitybased";
    public static final String POWER_MEMORY = "memory.bandwidths";
    public static final String POWER_MODEM_CONTROLLER_IDLE = "modem.controller.idle";
    public static final String POWER_MODEM_CONTROLLER_OPERATING_VOLTAGE = "modem.controller.voltage";
    public static final String POWER_MODEM_CONTROLLER_RX = "modem.controller.rx";
    public static final String POWER_MODEM_CONTROLLER_SLEEP = "modem.controller.sleep";
    public static final String POWER_MODEM_CONTROLLER_TX = "modem.controller.tx";
    @UnsupportedAppUsage
    public static final String POWER_RADIO_ACTIVE = "radio.active";
    @UnsupportedAppUsage
    public static final String POWER_RADIO_ON = "radio.on";
    @UnsupportedAppUsage
    public static final String POWER_RADIO_SCANNING = "radio.scanning";
    @UnsupportedAppUsage
    public static final String POWER_SCREEN_FULL = "screen.full";
    @UnsupportedAppUsage
    public static final String POWER_SCREEN_ON = "screen.on";
    public static final String POWER_VIDEO = "video";
    @UnsupportedAppUsage
    public static final String POWER_WIFI_ACTIVE = "wifi.active";
    public static final String POWER_WIFI_BATCHED_SCAN = "wifi.batchedscan";
    public static final String POWER_WIFI_CONTROLLER_IDLE = "wifi.controller.idle";
    public static final String POWER_WIFI_CONTROLLER_OPERATING_VOLTAGE = "wifi.controller.voltage";
    public static final String POWER_WIFI_CONTROLLER_RX = "wifi.controller.rx";
    public static final String POWER_WIFI_CONTROLLER_TX = "wifi.controller.tx";
    public static final String POWER_WIFI_CONTROLLER_TX_LEVELS = "wifi.controller.tx_levels";
    @UnsupportedAppUsage
    public static final String POWER_WIFI_ON = "wifi.on";
    @UnsupportedAppUsage
    public static final String POWER_WIFI_SCAN = "wifi.scan";
    private static final String TAG_ARRAY = "array";
    private static final String TAG_ARRAYITEM = "value";
    private static final String TAG_DEVICE = "device";
    private static final String TAG_ITEM = "item";
    private CpuClusterKey[] mCpuClusters;
    static final HashMap<String, Double> sPowerItemMap = new HashMap<>();
    static final HashMap<String, Double[]> sPowerArrayMap = new HashMap<>();
    private static final Object sLock = new Object();

    @UnsupportedAppUsage
    @VisibleForTesting
    public PowerProfile(Context context) {
        this(context, false);
    }

    @VisibleForTesting
    public PowerProfile(Context context, boolean forTest) {
        synchronized (sLock) {
            if (sPowerItemMap.size() == 0 && sPowerArrayMap.size() == 0) {
                readPowerValuesFromXml(context, forTest);
            }
            initCpuClusters();
        }
    }

    private void readPowerValuesFromXml(Context context, boolean forTest) {
        int value;
        int id = forTest ? R.xml.power_profile_test : R.xml.power_profile;
        Resources resources = context.getResources();
        XmlResourceParser parser = resources.getXml(id);
        boolean parsingArray = false;
        ArrayList<Double> array = new ArrayList<>();
        String arrayName = null;
        try {
            try {
                try {
                    XmlUtils.beginDocument(parser, "device");
                    while (true) {
                        XmlUtils.nextElement(parser);
                        String element = parser.getName();
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
                            arrayName = parser.getAttributeValue(null, "name");
                        } else if (element.equals("item") || element.equals("value")) {
                            String name = parsingArray ? null : parser.getAttributeValue(null, "name");
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
                    for (int i = 0; i < configResIds.length; i++) {
                        String key = configResIdKeys[i];
                        if ((!sPowerItemMap.containsKey(key) || sPowerItemMap.get(key).doubleValue() <= FeatureOption.FO_BOOT_POLICY_CPU) && (value = resources.getInteger(configResIds[i])) > 0) {
                            sPowerItemMap.put(key, Double.valueOf(value));
                        }
                    }
                } catch (IOException e2) {
                    throw new RuntimeException(e2);
                }
            } catch (XmlPullParserException e3) {
                throw new RuntimeException(e3);
            }
        } catch (Throwable th) {
            parser.close();
            throw th;
        }
    }

    private void initCpuClusters() {
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

        private CpuClusterKey(String freqKey, String clusterPowerKey, String corePowerKey, int numCpus) {
            this.freqKey = freqKey;
            this.clusterPowerKey = clusterPowerKey;
            this.corePowerKey = corePowerKey;
            this.numCpus = numCpus;
        }
    }

    @UnsupportedAppUsage
    public int getNumCpuClusters() {
        return this.mCpuClusters.length;
    }

    public int getNumCoresInCpuCluster(int cluster) {
        return this.mCpuClusters[cluster].numCpus;
    }

    @UnsupportedAppUsage
    public int getNumSpeedStepsInCpuCluster(int cluster) {
        if (cluster >= 0) {
            CpuClusterKey[] cpuClusterKeyArr = this.mCpuClusters;
            if (cluster < cpuClusterKeyArr.length) {
                if (sPowerArrayMap.containsKey(cpuClusterKeyArr[cluster].freqKey)) {
                    return sPowerArrayMap.get(this.mCpuClusters[cluster].freqKey).length;
                }
                return 1;
            }
            return 0;
        }
        return 0;
    }

    public double getAveragePowerForCpuCluster(int cluster) {
        if (cluster >= 0) {
            CpuClusterKey[] cpuClusterKeyArr = this.mCpuClusters;
            if (cluster < cpuClusterKeyArr.length) {
                return getAveragePower(cpuClusterKeyArr[cluster].clusterPowerKey);
            }
            return FeatureOption.FO_BOOT_POLICY_CPU;
        }
        return FeatureOption.FO_BOOT_POLICY_CPU;
    }

    public double getAveragePowerForCpuCore(int cluster, int step) {
        if (cluster >= 0) {
            CpuClusterKey[] cpuClusterKeyArr = this.mCpuClusters;
            if (cluster < cpuClusterKeyArr.length) {
                return getAveragePower(cpuClusterKeyArr[cluster].corePowerKey, step);
            }
            return FeatureOption.FO_BOOT_POLICY_CPU;
        }
        return FeatureOption.FO_BOOT_POLICY_CPU;
    }

    public int getNumElements(String key) {
        if (sPowerItemMap.containsKey(key)) {
            return 1;
        }
        if (sPowerArrayMap.containsKey(key)) {
            return sPowerArrayMap.get(key).length;
        }
        return 0;
    }

    public double getAveragePowerOrDefault(String type, double defaultValue) {
        if (sPowerItemMap.containsKey(type)) {
            return sPowerItemMap.get(type).doubleValue();
        }
        if (sPowerArrayMap.containsKey(type)) {
            return sPowerArrayMap.get(type)[0].doubleValue();
        }
        return defaultValue;
    }

    @UnsupportedAppUsage
    public double getAveragePower(String type) {
        return getAveragePowerOrDefault(type, FeatureOption.FO_BOOT_POLICY_CPU);
    }

    @UnsupportedAppUsage
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

    @UnsupportedAppUsage
    public double getBatteryCapacity() {
        return getAveragePower(POWER_BATTERY_CAPACITY);
    }

    public void writeToProto(ProtoOutputStream proto) {
        Double[] dArr;
        Double[] dArr2;
        writePowerConstantToProto(proto, POWER_CPU_SUSPEND, 1103806595073L);
        writePowerConstantToProto(proto, POWER_CPU_IDLE, 1103806595074L);
        writePowerConstantToProto(proto, POWER_CPU_ACTIVE, 1103806595075L);
        for (int cluster = 0; cluster < this.mCpuClusters.length; cluster++) {
            long token = proto.start(2246267895848L);
            proto.write(1120986464257L, cluster);
            proto.write(1103806595074L, sPowerItemMap.get(this.mCpuClusters[cluster].clusterPowerKey).doubleValue());
            proto.write(1120986464259L, this.mCpuClusters[cluster].numCpus);
            for (Double speed : sPowerArrayMap.get(this.mCpuClusters[cluster].freqKey)) {
                proto.write(PowerProfileProto.CpuCluster.SPEED, speed.doubleValue());
            }
            for (Double corePower : sPowerArrayMap.get(this.mCpuClusters[cluster].corePowerKey)) {
                proto.write(PowerProfileProto.CpuCluster.CORE_POWER, corePower.doubleValue());
            }
            proto.end(token);
        }
        writePowerConstantToProto(proto, POWER_WIFI_SCAN, 1103806595076L);
        writePowerConstantToProto(proto, POWER_WIFI_ON, 1103806595077L);
        writePowerConstantToProto(proto, POWER_WIFI_ACTIVE, 1103806595078L);
        writePowerConstantToProto(proto, POWER_WIFI_CONTROLLER_IDLE, PowerProfileProto.WIFI_CONTROLLER_IDLE);
        writePowerConstantToProto(proto, POWER_WIFI_CONTROLLER_RX, 1103806595080L);
        writePowerConstantToProto(proto, POWER_WIFI_CONTROLLER_TX, 1103806595081L);
        writePowerConstantArrayToProto(proto, POWER_WIFI_CONTROLLER_TX_LEVELS, PowerProfileProto.WIFI_CONTROLLER_TX_LEVELS);
        writePowerConstantToProto(proto, POWER_WIFI_CONTROLLER_OPERATING_VOLTAGE, PowerProfileProto.WIFI_CONTROLLER_OPERATING_VOLTAGE);
        writePowerConstantToProto(proto, POWER_BLUETOOTH_CONTROLLER_IDLE, PowerProfileProto.BLUETOOTH_CONTROLLER_IDLE);
        writePowerConstantToProto(proto, POWER_BLUETOOTH_CONTROLLER_RX, PowerProfileProto.BLUETOOTH_CONTROLLER_RX);
        writePowerConstantToProto(proto, POWER_BLUETOOTH_CONTROLLER_TX, PowerProfileProto.BLUETOOTH_CONTROLLER_TX);
        writePowerConstantToProto(proto, POWER_BLUETOOTH_CONTROLLER_OPERATING_VOLTAGE, PowerProfileProto.BLUETOOTH_CONTROLLER_OPERATING_VOLTAGE);
        writePowerConstantToProto(proto, POWER_MODEM_CONTROLLER_SLEEP, PowerProfileProto.MODEM_CONTROLLER_SLEEP);
        writePowerConstantToProto(proto, POWER_MODEM_CONTROLLER_IDLE, PowerProfileProto.MODEM_CONTROLLER_IDLE);
        writePowerConstantToProto(proto, POWER_MODEM_CONTROLLER_RX, 1103806595090L);
        writePowerConstantArrayToProto(proto, POWER_MODEM_CONTROLLER_TX, PowerProfileProto.MODEM_CONTROLLER_TX);
        writePowerConstantToProto(proto, POWER_MODEM_CONTROLLER_OPERATING_VOLTAGE, 1103806595092L);
        writePowerConstantToProto(proto, POWER_GPS_ON, 1103806595093L);
        writePowerConstantArrayToProto(proto, POWER_GPS_SIGNAL_QUALITY_BASED, PowerProfileProto.GPS_SIGNAL_QUALITY_BASED);
        writePowerConstantToProto(proto, POWER_GPS_OPERATING_VOLTAGE, 1103806595095L);
        writePowerConstantToProto(proto, POWER_BLUETOOTH_ON, 1103806595096L);
        writePowerConstantToProto(proto, POWER_BLUETOOTH_ACTIVE, 1103806595097L);
        writePowerConstantToProto(proto, POWER_BLUETOOTH_AT_CMD, 1103806595098L);
        writePowerConstantToProto(proto, POWER_AMBIENT_DISPLAY, PowerProfileProto.AMBIENT_DISPLAY);
        writePowerConstantToProto(proto, POWER_SCREEN_ON, PowerProfileProto.SCREEN_ON);
        writePowerConstantToProto(proto, POWER_RADIO_ON, PowerProfileProto.RADIO_ON);
        writePowerConstantToProto(proto, POWER_RADIO_SCANNING, PowerProfileProto.RADIO_SCANNING);
        writePowerConstantToProto(proto, POWER_RADIO_ACTIVE, PowerProfileProto.RADIO_ACTIVE);
        writePowerConstantToProto(proto, POWER_SCREEN_FULL, PowerProfileProto.SCREEN_FULL);
        writePowerConstantToProto(proto, "audio", PowerProfileProto.AUDIO);
        writePowerConstantToProto(proto, "video", PowerProfileProto.VIDEO);
        writePowerConstantToProto(proto, POWER_FLASHLIGHT, PowerProfileProto.FLASHLIGHT);
        writePowerConstantToProto(proto, POWER_MEMORY, PowerProfileProto.MEMORY);
        writePowerConstantToProto(proto, POWER_CAMERA, PowerProfileProto.CAMERA);
        writePowerConstantToProto(proto, POWER_WIFI_BATCHED_SCAN, PowerProfileProto.WIFI_BATCHED_SCAN);
        writePowerConstantToProto(proto, POWER_BATTERY_CAPACITY, PowerProfileProto.BATTERY_CAPACITY);
    }

    private void writePowerConstantToProto(ProtoOutputStream proto, String key, long fieldId) {
        if (sPowerItemMap.containsKey(key)) {
            proto.write(fieldId, sPowerItemMap.get(key).doubleValue());
        }
    }

    private void writePowerConstantArrayToProto(ProtoOutputStream proto, String key, long fieldId) {
        Double[] dArr;
        if (sPowerArrayMap.containsKey(key)) {
            for (Double d : sPowerArrayMap.get(key)) {
                proto.write(fieldId, d.doubleValue());
            }
        }
    }
}
