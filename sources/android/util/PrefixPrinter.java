package android.util;

/* loaded from: classes2.dex */
public class PrefixPrinter implements Printer {
    private final String mPrefix;
    private final Printer mPrinter;

    public static Printer create(Printer printer, String prefix) {
        if (prefix == null || prefix.equals("")) {
            return printer;
        }
        return new PrefixPrinter(printer, prefix);
    }

    private PrefixPrinter(Printer printer, String prefix) {
        this.mPrinter = printer;
        this.mPrefix = prefix;
    }

    @Override // android.util.Printer
    public void println(String str) {
        Printer printer = this.mPrinter;
        printer.println(this.mPrefix + str);
    }
}
