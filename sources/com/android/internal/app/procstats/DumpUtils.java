package com.android.internal.app.procstats;

import android.app.backup.FullBackup;
import android.content.Context;
import android.hardware.Camera;
import android.media.TtmlUtils;
import android.os.UserHandle;
import android.provider.CalendarContract;
import android.util.TimeUtils;
import android.util.proto.ProtoOutputStream;
import com.android.internal.app.DumpHeapActivity;
import com.android.internal.content.NativeLibraryHelper;
import java.io.PrintWriter;
import java.util.ArrayList;
/* loaded from: classes3.dex */
public final class DumpUtils {
    public static final String[] ADJ_MEM_NAMES_CSV;
    static final int[] ADJ_MEM_PROTO_ENUMS;
    static final String[] ADJ_MEM_TAGS;
    public static final String[] ADJ_SCREEN_NAMES_CSV;
    static final int[] ADJ_SCREEN_PROTO_ENUMS;
    static final String[] ADJ_SCREEN_TAGS;
    static final String CSV_SEP = "\t";
    public static final String[] STATE_NAMES = new String[14];
    public static final String[] STATE_NAMES_CSV;
    static final int[] STATE_PROTO_ENUMS;
    static final String[] STATE_TAGS;

    static {
        STATE_NAMES[0] = "Persist";
        STATE_NAMES[1] = "Top";
        STATE_NAMES[2] = "ImpFg";
        STATE_NAMES[3] = "ImpBg";
        STATE_NAMES[4] = "Backup";
        STATE_NAMES[5] = "Service";
        STATE_NAMES[6] = "ServRst";
        STATE_NAMES[7] = "Receivr";
        STATE_NAMES[8] = "HeavyWt";
        STATE_NAMES[9] = "Home";
        STATE_NAMES[10] = "LastAct";
        STATE_NAMES[11] = "CchAct";
        STATE_NAMES[12] = "CchCAct";
        STATE_NAMES[13] = "CchEmty";
        STATE_NAMES_CSV = new String[14];
        STATE_NAMES_CSV[0] = "pers";
        STATE_NAMES_CSV[1] = "top";
        STATE_NAMES_CSV[2] = "impfg";
        STATE_NAMES_CSV[3] = "impbg";
        STATE_NAMES_CSV[4] = Context.BACKUP_SERVICE;
        STATE_NAMES_CSV[5] = "service";
        STATE_NAMES_CSV[6] = "service-rs";
        STATE_NAMES_CSV[7] = "receiver";
        STATE_NAMES_CSV[8] = "heavy";
        STATE_NAMES_CSV[9] = CalendarContract.CalendarCache.TIMEZONE_TYPE_HOME;
        STATE_NAMES_CSV[10] = "lastact";
        STATE_NAMES_CSV[11] = "cch-activity";
        STATE_NAMES_CSV[12] = "cch-aclient";
        STATE_NAMES_CSV[13] = "cch-empty";
        STATE_TAGS = new String[14];
        STATE_TAGS[0] = TtmlUtils.TAG_P;
        STATE_TAGS[1] = "t";
        STATE_TAGS[2] = FullBackup.FILES_TREE_TOKEN;
        STATE_TAGS[3] = "b";
        STATE_TAGS[4] = "u";
        STATE_TAGS[5] = "s";
        STATE_TAGS[6] = "x";
        STATE_TAGS[7] = FullBackup.ROOT_TREE_TOKEN;
        STATE_TAGS[8] = "w";
        STATE_TAGS[9] = "h";
        STATE_TAGS[10] = "l";
        STATE_TAGS[11] = FullBackup.APK_TREE_TOKEN;
        STATE_TAGS[12] = FullBackup.CACHE_TREE_TOKEN;
        STATE_TAGS[13] = "e";
        STATE_PROTO_ENUMS = new int[14];
        STATE_PROTO_ENUMS[0] = 1;
        STATE_PROTO_ENUMS[1] = 2;
        STATE_PROTO_ENUMS[2] = 3;
        STATE_PROTO_ENUMS[3] = 4;
        STATE_PROTO_ENUMS[4] = 5;
        STATE_PROTO_ENUMS[5] = 6;
        STATE_PROTO_ENUMS[6] = 7;
        STATE_PROTO_ENUMS[7] = 8;
        STATE_PROTO_ENUMS[8] = 9;
        STATE_PROTO_ENUMS[9] = 10;
        STATE_PROTO_ENUMS[10] = 11;
        STATE_PROTO_ENUMS[11] = 12;
        STATE_PROTO_ENUMS[12] = 13;
        STATE_PROTO_ENUMS[13] = 14;
        ADJ_SCREEN_NAMES_CSV = new String[]{"off", Camera.Parameters.FLASH_MODE_ON};
        ADJ_MEM_NAMES_CSV = new String[]{"norm", "mod", "low", "crit"};
        ADJ_SCREEN_TAGS = new String[]{"0", "1"};
        ADJ_SCREEN_PROTO_ENUMS = new int[]{1, 2};
        ADJ_MEM_TAGS = new String[]{"n", "m", "l", FullBackup.CACHE_TREE_TOKEN};
        ADJ_MEM_PROTO_ENUMS = new int[]{1, 2, 3, 4};
    }

    private DumpUtils() {
    }

    public static void printScreenLabel(PrintWriter pw, int offset) {
        if (offset != 4) {
            switch (offset) {
                case -1:
                    pw.print("     ");
                    return;
                case 0:
                    pw.print("SOff/");
                    return;
                default:
                    pw.print("????/");
                    return;
            }
        }
        pw.print("SOn /");
    }

    public static void printScreenLabelCsv(PrintWriter pw, int offset) {
        if (offset == 4) {
            pw.print(ADJ_SCREEN_NAMES_CSV[1]);
            return;
        }
        switch (offset) {
            case -1:
                return;
            case 0:
                pw.print(ADJ_SCREEN_NAMES_CSV[0]);
                return;
            default:
                pw.print("???");
                return;
        }
    }

    public static void printMemLabel(PrintWriter pw, int offset, char sep) {
        switch (offset) {
            case -1:
                pw.print("    ");
                if (sep != 0) {
                    pw.print(' ');
                    return;
                }
                return;
            case 0:
                pw.print("Norm");
                if (sep != 0) {
                    pw.print(sep);
                    return;
                }
                return;
            case 1:
                pw.print("Mod ");
                if (sep != 0) {
                    pw.print(sep);
                    return;
                }
                return;
            case 2:
                pw.print("Low ");
                if (sep != 0) {
                    pw.print(sep);
                    return;
                }
                return;
            case 3:
                pw.print("Crit");
                if (sep != 0) {
                    pw.print(sep);
                    return;
                }
                return;
            default:
                pw.print("????");
                if (sep != 0) {
                    pw.print(sep);
                    return;
                }
                return;
        }
    }

    public static void printMemLabelCsv(PrintWriter pw, int offset) {
        if (offset >= 0) {
            if (offset <= 3) {
                pw.print(ADJ_MEM_NAMES_CSV[offset]);
            } else {
                pw.print("???");
            }
        }
    }

    public static void printPercent(PrintWriter pw, double fraction) {
        double fraction2 = fraction * 100.0d;
        if (fraction2 < 1.0d) {
            pw.print(String.format("%.2f", Double.valueOf(fraction2)));
        } else if (fraction2 < 10.0d) {
            pw.print(String.format("%.1f", Double.valueOf(fraction2)));
        } else {
            pw.print(String.format("%.0f", Double.valueOf(fraction2)));
        }
        pw.print("%");
    }

    public static void printProcStateTag(PrintWriter pw, int state) {
        printArrayEntry(pw, STATE_TAGS, printArrayEntry(pw, ADJ_MEM_TAGS, printArrayEntry(pw, ADJ_SCREEN_TAGS, state, 56), 14), 1);
    }

    public static void printProcStateTagProto(ProtoOutputStream proto, long screenId, long memId, long stateId, int state) {
        printProto(proto, stateId, STATE_PROTO_ENUMS, printProto(proto, memId, ADJ_MEM_PROTO_ENUMS, printProto(proto, screenId, ADJ_SCREEN_PROTO_ENUMS, state, 56), 14), 1);
    }

    public static void printAdjTag(PrintWriter pw, int state) {
        printArrayEntry(pw, ADJ_MEM_TAGS, printArrayEntry(pw, ADJ_SCREEN_TAGS, state, 4), 1);
    }

    public static void printProcStateTagAndValue(PrintWriter pw, int state, long value) {
        pw.print(',');
        printProcStateTag(pw, state);
        pw.print(':');
        pw.print(value);
    }

    public static void printAdjTagAndValue(PrintWriter pw, int state, long value) {
        pw.print(',');
        printAdjTag(pw, state);
        pw.print(':');
        pw.print(value);
    }

    public static long dumpSingleTime(PrintWriter pw, String prefix, long[] durations, int curState, long curStartTime, long now) {
        int printedScreen = -1;
        long totalTime = 0;
        int iscreen = 0;
        while (true) {
            long j = 0;
            if (iscreen >= 8) {
                break;
            }
            int printedMem = -1;
            long totalTime2 = totalTime;
            int imem = 0;
            while (imem < 4) {
                int state = imem + iscreen;
                long time = durations[state];
                String running = "";
                if (curState == state) {
                    time += now - curStartTime;
                    if (pw != null) {
                        running = " (running)";
                    }
                }
                if (time != j) {
                    if (pw != null) {
                        pw.print(prefix);
                        printScreenLabel(pw, printedScreen != iscreen ? iscreen : -1);
                        printedScreen = iscreen;
                        printMemLabel(pw, printedMem != imem ? imem : -1, (char) 0);
                        printedMem = imem;
                        pw.print(": ");
                        TimeUtils.formatDuration(time, pw);
                        pw.println(running);
                    }
                    totalTime2 += time;
                }
                imem++;
                j = 0;
            }
            iscreen += 4;
            totalTime = totalTime2;
        }
        if (totalTime != 0 && pw != null) {
            pw.print(prefix);
            pw.print("    TOTAL: ");
            TimeUtils.formatDuration(totalTime, pw);
            pw.println();
        }
        return totalTime;
    }

    public static void dumpAdjTimesCheckin(PrintWriter pw, String sep, long[] durations, int curState, long curStartTime, long now) {
        for (int iscreen = 0; iscreen < 8; iscreen += 4) {
            for (int imem = 0; imem < 4; imem++) {
                int state = imem + iscreen;
                long time = durations[state];
                if (curState == state) {
                    time += now - curStartTime;
                }
                if (time != 0) {
                    printAdjTagAndValue(pw, state, time);
                }
            }
        }
    }

    private static void dumpStateHeadersCsv(PrintWriter pw, String sep, int[] screenStates, int[] memStates, int[] procStates) {
        int NS = screenStates != null ? screenStates.length : 1;
        int NM = memStates != null ? memStates.length : 1;
        int NP = procStates != null ? procStates.length : 1;
        for (int is = 0; is < NS; is++) {
            for (int im = 0; im < NM; im++) {
                for (int ip = 0; ip < NP; ip++) {
                    pw.print(sep);
                    boolean printed = false;
                    if (screenStates != null && screenStates.length > 1) {
                        printScreenLabelCsv(pw, screenStates[is]);
                        printed = true;
                    }
                    if (memStates != null && memStates.length > 1) {
                        if (printed) {
                            pw.print(NativeLibraryHelper.CLEAR_ABI_OVERRIDE);
                        }
                        printMemLabelCsv(pw, memStates[im]);
                        printed = true;
                    }
                    if (procStates != null && procStates.length > 1) {
                        if (printed) {
                            pw.print(NativeLibraryHelper.CLEAR_ABI_OVERRIDE);
                        }
                        pw.print(STATE_NAMES_CSV[procStates[ip]]);
                    }
                }
            }
        }
    }

    public static void dumpProcessSummaryLocked(PrintWriter pw, String prefix, ArrayList<ProcessState> procs, int[] screenStates, int[] memStates, int[] procStates, long now, long totalTime) {
        for (int i = procs.size() - 1; i >= 0; i--) {
            ProcessState proc = procs.get(i);
            proc.dumpSummary(pw, prefix, screenStates, memStates, procStates, now, totalTime);
        }
    }

    public static void dumpProcessListCsv(PrintWriter pw, ArrayList<ProcessState> procs, boolean sepScreenStates, int[] screenStates, boolean sepMemStates, int[] memStates, boolean sepProcStates, int[] procStates, long now) {
        pw.print(DumpHeapActivity.KEY_PROCESS);
        pw.print(CSV_SEP);
        pw.print("uid");
        pw.print(CSV_SEP);
        pw.print("vers");
        dumpStateHeadersCsv(pw, CSV_SEP, sepScreenStates ? screenStates : null, sepMemStates ? memStates : null, sepProcStates ? procStates : null);
        pw.println();
        int i = procs.size() - 1;
        while (true) {
            int i2 = i;
            if (i2 < 0) {
                return;
            }
            ProcessState proc = procs.get(i2);
            pw.print(proc.getName());
            pw.print(CSV_SEP);
            UserHandle.formatUid(pw, proc.getUid());
            pw.print(CSV_SEP);
            pw.print(proc.getVersion());
            proc.dumpCsv(pw, sepScreenStates, screenStates, sepMemStates, memStates, sepProcStates, procStates, now);
            pw.println();
            i = i2 - 1;
        }
    }

    public static int printArrayEntry(PrintWriter pw, String[] array, int value, int mod) {
        int index = value / mod;
        if (index >= 0 && index < array.length) {
            pw.print(array[index]);
        } else {
            pw.print('?');
        }
        return value - (index * mod);
    }

    public static int printProto(ProtoOutputStream proto, long fieldId, int[] enums, int value, int mod) {
        int index = value / mod;
        if (index >= 0 && index < enums.length) {
            proto.write(fieldId, enums[index]);
        }
        return value - (index * mod);
    }

    public static String collapseString(String pkgName, String itemName) {
        if (itemName.startsWith(pkgName)) {
            int ITEMLEN = itemName.length();
            int PKGLEN = pkgName.length();
            if (ITEMLEN == PKGLEN) {
                return "";
            }
            if (ITEMLEN >= PKGLEN && itemName.charAt(PKGLEN) == '.') {
                return itemName.substring(PKGLEN);
            }
        }
        return itemName;
    }
}
