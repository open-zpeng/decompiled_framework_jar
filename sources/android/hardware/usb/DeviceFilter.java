package android.hardware.usb;

import android.util.Slog;
import com.android.internal.util.dump.DualDumpOutputStream;
import java.io.IOException;
import java.util.Objects;
import org.xmlpull.v1.XmlPullParser;
import org.xmlpull.v1.XmlPullParserException;
import org.xmlpull.v1.XmlSerializer;
/* loaded from: classes.dex */
public class DeviceFilter {
    private static final String TAG = DeviceFilter.class.getSimpleName();
    public final int mClass;
    public final String mManufacturerName;
    public final int mProductId;
    public final String mProductName;
    public final int mProtocol;
    public final String mSerialNumber;
    public final int mSubclass;
    public final int mVendorId;

    public synchronized DeviceFilter(int vid, int pid, int clasz, int subclass, int protocol, String manufacturer, String product, String serialnum) {
        this.mVendorId = vid;
        this.mProductId = pid;
        this.mClass = clasz;
        this.mSubclass = subclass;
        this.mProtocol = protocol;
        this.mManufacturerName = manufacturer;
        this.mProductName = product;
        this.mSerialNumber = serialnum;
    }

    public synchronized DeviceFilter(UsbDevice device) {
        this.mVendorId = device.getVendorId();
        this.mProductId = device.getProductId();
        this.mClass = device.getDeviceClass();
        this.mSubclass = device.getDeviceSubclass();
        this.mProtocol = device.getDeviceProtocol();
        this.mManufacturerName = device.getManufacturerName();
        this.mProductName = device.getProductName();
        this.mSerialNumber = device.getSerialNumber();
    }

    public static synchronized DeviceFilter read(XmlPullParser parser) throws XmlPullParserException, IOException {
        XmlPullParser xmlPullParser = parser;
        int count = parser.getAttributeCount();
        int i = 0;
        String serialNumber = null;
        String serialNumber2 = null;
        String manufacturerName = null;
        int deviceProtocol = -1;
        int deviceProtocol2 = -1;
        int deviceSubclass = -1;
        int deviceClass = -1;
        int productId = -1;
        int vendorId = 0;
        while (true) {
            int i2 = vendorId;
            if (i2 >= count) {
                return new DeviceFilter(productId, deviceClass, deviceSubclass, deviceProtocol2, deviceProtocol, manufacturerName, serialNumber2, serialNumber);
            }
            String name = xmlPullParser.getAttributeName(i2);
            String value = xmlPullParser.getAttributeValue(i2);
            if ("manufacturer-name".equals(name)) {
                manufacturerName = value;
            } else if ("product-name".equals(name)) {
                serialNumber2 = value;
            } else if ("serial-number".equals(name)) {
                serialNumber = value;
            } else {
                int radix = 10;
                if (value != null && value.length() > 2 && value.charAt(i) == '0' && (value.charAt(1) == 'x' || value.charAt(1) == 'X')) {
                    radix = 16;
                    value = value.substring(2);
                }
                try {
                    int deviceProtocol3 = Integer.parseInt(value, radix);
                    if ("vendor-id".equals(name)) {
                        productId = deviceProtocol3;
                    } else if ("product-id".equals(name)) {
                        deviceClass = deviceProtocol3;
                    } else if ("class".equals(name)) {
                        deviceSubclass = deviceProtocol3;
                    } else if ("subclass".equals(name)) {
                        deviceProtocol2 = deviceProtocol3;
                    } else if ("protocol".equals(name)) {
                        deviceProtocol = deviceProtocol3;
                    }
                } catch (NumberFormatException e) {
                    Slog.e(TAG, "invalid number for field " + name, e);
                }
            }
            vendorId = i2 + 1;
            xmlPullParser = parser;
            i = 0;
        }
    }

    public synchronized void write(XmlSerializer serializer) throws IOException {
        serializer.startTag(null, "usb-device");
        if (this.mVendorId != -1) {
            serializer.attribute(null, "vendor-id", Integer.toString(this.mVendorId));
        }
        if (this.mProductId != -1) {
            serializer.attribute(null, "product-id", Integer.toString(this.mProductId));
        }
        if (this.mClass != -1) {
            serializer.attribute(null, "class", Integer.toString(this.mClass));
        }
        if (this.mSubclass != -1) {
            serializer.attribute(null, "subclass", Integer.toString(this.mSubclass));
        }
        if (this.mProtocol != -1) {
            serializer.attribute(null, "protocol", Integer.toString(this.mProtocol));
        }
        if (this.mManufacturerName != null) {
            serializer.attribute(null, "manufacturer-name", this.mManufacturerName);
        }
        if (this.mProductName != null) {
            serializer.attribute(null, "product-name", this.mProductName);
        }
        if (this.mSerialNumber != null) {
            serializer.attribute(null, "serial-number", this.mSerialNumber);
        }
        serializer.endTag(null, "usb-device");
    }

    private synchronized boolean matches(int clasz, int subclass, int protocol) {
        return (this.mClass == -1 || clasz == this.mClass) && (this.mSubclass == -1 || subclass == this.mSubclass) && (this.mProtocol == -1 || protocol == this.mProtocol);
    }

    public synchronized boolean matches(UsbDevice device) {
        if (this.mVendorId == -1 || device.getVendorId() == this.mVendorId) {
            if (this.mProductId == -1 || device.getProductId() == this.mProductId) {
                if (this.mManufacturerName == null || device.getManufacturerName() != null) {
                    if (this.mProductName == null || device.getProductName() != null) {
                        if (this.mSerialNumber == null || device.getSerialNumber() != null) {
                            if (this.mManufacturerName == null || device.getManufacturerName() == null || this.mManufacturerName.equals(device.getManufacturerName())) {
                                if (this.mProductName == null || device.getProductName() == null || this.mProductName.equals(device.getProductName())) {
                                    if (this.mSerialNumber == null || device.getSerialNumber() == null || this.mSerialNumber.equals(device.getSerialNumber())) {
                                        if (matches(device.getDeviceClass(), device.getDeviceSubclass(), device.getDeviceProtocol())) {
                                            return true;
                                        }
                                        int count = device.getInterfaceCount();
                                        for (int i = 0; i < count; i++) {
                                            UsbInterface intf = device.getInterface(i);
                                            if (matches(intf.getInterfaceClass(), intf.getInterfaceSubclass(), intf.getInterfaceProtocol())) {
                                                return true;
                                            }
                                        }
                                        return false;
                                    }
                                    return false;
                                }
                                return false;
                            }
                            return false;
                        }
                        return false;
                    }
                    return false;
                }
                return false;
            }
            return false;
        }
        return false;
    }

    public synchronized boolean contains(DeviceFilter device) {
        if (this.mVendorId == -1 || device.mVendorId == this.mVendorId) {
            if (this.mProductId == -1 || device.mProductId == this.mProductId) {
                if (this.mManufacturerName == null || Objects.equals(this.mManufacturerName, device.mManufacturerName)) {
                    if (this.mProductName == null || Objects.equals(this.mProductName, device.mProductName)) {
                        if (this.mSerialNumber == null || Objects.equals(this.mSerialNumber, device.mSerialNumber)) {
                            return matches(device.mClass, device.mSubclass, device.mProtocol);
                        }
                        return false;
                    }
                    return false;
                }
                return false;
            }
            return false;
        }
        return false;
    }

    public boolean equals(Object obj) {
        if (this.mVendorId == -1 || this.mProductId == -1 || this.mClass == -1 || this.mSubclass == -1 || this.mProtocol == -1) {
            return false;
        }
        if (obj instanceof DeviceFilter) {
            DeviceFilter filter = (DeviceFilter) obj;
            if (filter.mVendorId == this.mVendorId && filter.mProductId == this.mProductId && filter.mClass == this.mClass && filter.mSubclass == this.mSubclass && filter.mProtocol == this.mProtocol) {
                if ((filter.mManufacturerName == null || this.mManufacturerName != null) && ((filter.mManufacturerName != null || this.mManufacturerName == null) && ((filter.mProductName == null || this.mProductName != null) && ((filter.mProductName != null || this.mProductName == null) && ((filter.mSerialNumber == null || this.mSerialNumber != null) && (filter.mSerialNumber != null || this.mSerialNumber == null)))))) {
                    return (filter.mManufacturerName == null || this.mManufacturerName == null || this.mManufacturerName.equals(filter.mManufacturerName)) && (filter.mProductName == null || this.mProductName == null || this.mProductName.equals(filter.mProductName)) && (filter.mSerialNumber == null || this.mSerialNumber == null || this.mSerialNumber.equals(filter.mSerialNumber));
                }
                return false;
            }
            return false;
        } else if (obj instanceof UsbDevice) {
            UsbDevice device = (UsbDevice) obj;
            if (device.getVendorId() == this.mVendorId && device.getProductId() == this.mProductId && device.getDeviceClass() == this.mClass && device.getDeviceSubclass() == this.mSubclass && device.getDeviceProtocol() == this.mProtocol) {
                if ((this.mManufacturerName == null || device.getManufacturerName() != null) && ((this.mManufacturerName != null || device.getManufacturerName() == null) && ((this.mProductName == null || device.getProductName() != null) && ((this.mProductName != null || device.getProductName() == null) && ((this.mSerialNumber == null || device.getSerialNumber() != null) && (this.mSerialNumber != null || device.getSerialNumber() == null)))))) {
                    return (device.getManufacturerName() == null || this.mManufacturerName.equals(device.getManufacturerName())) && (device.getProductName() == null || this.mProductName.equals(device.getProductName())) && (device.getSerialNumber() == null || this.mSerialNumber.equals(device.getSerialNumber()));
                }
                return false;
            }
            return false;
        } else {
            return false;
        }
    }

    public int hashCode() {
        return ((this.mVendorId << 16) | this.mProductId) ^ (((this.mClass << 16) | (this.mSubclass << 8)) | this.mProtocol);
    }

    public String toString() {
        return "DeviceFilter[mVendorId=" + this.mVendorId + ",mProductId=" + this.mProductId + ",mClass=" + this.mClass + ",mSubclass=" + this.mSubclass + ",mProtocol=" + this.mProtocol + ",mManufacturerName=" + this.mManufacturerName + ",mProductName=" + this.mProductName + ",mSerialNumber=" + this.mSerialNumber + "]";
    }

    public synchronized void dump(DualDumpOutputStream dump, String idName, long id) {
        long token = dump.start(idName, id);
        dump.write("vendor_id", 1120986464257L, this.mVendorId);
        dump.write("product_id", 1120986464258L, this.mProductId);
        dump.write("class", 1120986464259L, this.mClass);
        dump.write("subclass", 1120986464260L, this.mSubclass);
        dump.write("protocol", 1120986464261L, this.mProtocol);
        dump.write("manufacturer_name", 1138166333446L, this.mManufacturerName);
        dump.write("product_name", 1138166333447L, this.mProductName);
        dump.write("serial_number", 1138166333448L, this.mSerialNumber);
        dump.end(token);
    }
}
