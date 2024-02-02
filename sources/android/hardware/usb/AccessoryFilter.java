package android.hardware.usb;

import android.media.midi.MidiDeviceInfo;
import com.android.internal.util.dump.DualDumpOutputStream;
import java.io.IOException;
import java.util.Objects;
import org.xmlpull.v1.XmlPullParser;
import org.xmlpull.v1.XmlPullParserException;
import org.xmlpull.v1.XmlSerializer;
/* loaded from: classes.dex */
public class AccessoryFilter {
    public final String mManufacturer;
    public final String mModel;
    public final String mVersion;

    public synchronized AccessoryFilter(String manufacturer, String model, String version) {
        this.mManufacturer = manufacturer;
        this.mModel = model;
        this.mVersion = version;
    }

    public synchronized AccessoryFilter(UsbAccessory accessory) {
        this.mManufacturer = accessory.getManufacturer();
        this.mModel = accessory.getModel();
        this.mVersion = accessory.getVersion();
    }

    public static synchronized AccessoryFilter read(XmlPullParser parser) throws XmlPullParserException, IOException {
        String manufacturer = null;
        String model = null;
        String version = null;
        int count = parser.getAttributeCount();
        for (int i = 0; i < count; i++) {
            String name = parser.getAttributeName(i);
            String value = parser.getAttributeValue(i);
            if (MidiDeviceInfo.PROPERTY_MANUFACTURER.equals(name)) {
                manufacturer = value;
            } else if ("model".equals(name)) {
                model = value;
            } else if ("version".equals(name)) {
                version = value;
            }
        }
        return new AccessoryFilter(manufacturer, model, version);
    }

    public synchronized void write(XmlSerializer serializer) throws IOException {
        serializer.startTag(null, "usb-accessory");
        if (this.mManufacturer != null) {
            serializer.attribute(null, MidiDeviceInfo.PROPERTY_MANUFACTURER, this.mManufacturer);
        }
        if (this.mModel != null) {
            serializer.attribute(null, "model", this.mModel);
        }
        if (this.mVersion != null) {
            serializer.attribute(null, "version", this.mVersion);
        }
        serializer.endTag(null, "usb-accessory");
    }

    public synchronized boolean matches(UsbAccessory acc) {
        if (this.mManufacturer == null || acc.getManufacturer().equals(this.mManufacturer)) {
            if (this.mModel == null || acc.getModel().equals(this.mModel)) {
                return this.mVersion == null || acc.getVersion().equals(this.mVersion);
            }
            return false;
        }
        return false;
    }

    public synchronized boolean contains(AccessoryFilter accessory) {
        if (this.mManufacturer == null || Objects.equals(accessory.mManufacturer, this.mManufacturer)) {
            if (this.mModel == null || Objects.equals(accessory.mModel, this.mModel)) {
                return this.mVersion == null || Objects.equals(accessory.mVersion, this.mVersion);
            }
            return false;
        }
        return false;
    }

    public boolean equals(Object obj) {
        if (this.mManufacturer == null || this.mModel == null || this.mVersion == null) {
            return false;
        }
        if (obj instanceof AccessoryFilter) {
            AccessoryFilter filter = (AccessoryFilter) obj;
            return this.mManufacturer.equals(filter.mManufacturer) && this.mModel.equals(filter.mModel) && this.mVersion.equals(filter.mVersion);
        } else if (obj instanceof UsbAccessory) {
            UsbAccessory accessory = (UsbAccessory) obj;
            return this.mManufacturer.equals(accessory.getManufacturer()) && this.mModel.equals(accessory.getModel()) && this.mVersion.equals(accessory.getVersion());
        } else {
            return false;
        }
    }

    public int hashCode() {
        int hashCode;
        if (this.mManufacturer != null) {
            hashCode = this.mManufacturer.hashCode();
        } else {
            hashCode = 0;
        }
        return (hashCode ^ (this.mModel == null ? 0 : this.mModel.hashCode())) ^ (this.mVersion != null ? this.mVersion.hashCode() : 0);
    }

    public String toString() {
        return "AccessoryFilter[mManufacturer=\"" + this.mManufacturer + "\", mModel=\"" + this.mModel + "\", mVersion=\"" + this.mVersion + "\"]";
    }

    public synchronized void dump(DualDumpOutputStream dump, String idName, long id) {
        long token = dump.start(idName, id);
        dump.write(MidiDeviceInfo.PROPERTY_MANUFACTURER, 1138166333441L, this.mManufacturer);
        dump.write("model", 1138166333442L, this.mModel);
        dump.write("version", 1138166333443L, this.mVersion);
        dump.end(token);
    }
}
