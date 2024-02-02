package com.android.internal.telephony;

import android.content.res.Resources;
import android.telephony.Rlog;
import android.text.TextUtils;
import android.util.SparseIntArray;
import java.nio.ByteBuffer;
import java.nio.charset.Charset;
import java.util.ArrayList;
import java.util.List;
/* loaded from: classes3.dex */
public class GsmAlphabet {
    public static final byte GSM_EXTENDED_ESCAPE = 27;
    private static final String TAG = "GSM";
    public static final int UDH_SEPTET_COST_CONCATENATED_MESSAGE = 6;
    public static final int UDH_SEPTET_COST_LENGTH = 1;
    public static final int UDH_SEPTET_COST_ONE_SHIFT_TABLE = 4;
    public static final int UDH_SEPTET_COST_TWO_SHIFT_TABLES = 7;
    public protected static final SparseIntArray[] sCharsToGsmTables;
    public protected static final SparseIntArray[] sCharsToShiftTables;
    public protected static int[] sEnabledLockingShiftTables;
    public protected static int[] sEnabledSingleShiftTables;
    public protected static int sHighestEnabledSingleShiftCode;
    private static boolean sDisableCountryEncodingCheck = false;
    public protected static final String[] sLanguageTables = {"@£$¥èéùìòÇ\nØø\rÅåΔ_ΦΓΛΩΠΨΣΘΞ\uffffÆæßÉ !\"#¤%&'()*+,-./0123456789:;<=>?¡ABCDEFGHIJKLMNOPQRSTUVWXYZÄÖÑÜ§¿abcdefghijklmnopqrstuvwxyzäöñüà", "@£$¥€éùıòÇ\nĞğ\rÅåΔ_ΦΓΛΩΠΨΣΘΞ\uffffŞşßÉ !\"#¤%&'()*+,-./0123456789:;<=>?İABCDEFGHIJKLMNOPQRSTUVWXYZÄÖÑÜ§çabcdefghijklmnopqrstuvwxyzäöñüà", "", "@£$¥êéúíóç\nÔô\rÁáΔ_ªÇÀ∞^\\€Ó|\uffffÂâÊÉ !\"#º%&'()*+,-./0123456789:;<=>?ÍABCDEFGHIJKLMNOPQRSTUVWXYZÃÕÚÜ§~abcdefghijklmnopqrstuvwxyzãõ`üà", "ঁংঃঅআইঈউঊঋ\nঌ \r এঐ  ওঔকখগঘঙচ\uffffছজঝঞ !টঠডঢণত)(থদ,ধ.ন0123456789:; পফ?বভমযর ল   শষসহ়ঽািীুূৃৄ  েৈ  োৌ্ৎabcdefghijklmnopqrstuvwxyzৗড়ঢ়ৰৱ", "ઁંઃઅઆઇઈઉઊઋ\nઌઍ\r એઐઑ ઓઔકખગઘઙચ\uffffછજઝઞ !ટઠડઢણત)(થદ,ધ.ન0123456789:; પફ?બભમયર લળ વશષસહ઼ઽાિીુૂૃૄૅ ેૈૉ ોૌ્ૐabcdefghijklmnopqrstuvwxyzૠૡૢૣ૱", "ँंःअआइईउऊऋ\nऌऍ\rऎएऐऑऒओऔकखगघङच\uffffछजझञ !टठडढणत)(थद,ध.न0123456789:;ऩपफ?बभमयरऱलळऴवशषसह़ऽािीुूृॄॅॆेैॉॊोौ्ॐabcdefghijklmnopqrstuvwxyzॲॻॼॾॿ", " ಂಃಅಆಇಈಉಊಋ\nಌ \rಎಏಐ ಒಓಔಕಖಗಘಙಚ\uffffಛಜಝಞ !ಟಠಡಢಣತ)(ಥದ,ಧ.ನ0123456789:; ಪಫ?ಬಭಮಯರಱಲಳ ವಶಷಸಹ಼ಽಾಿೀುೂೃೄ ೆೇೈ ೊೋೌ್ೕabcdefghijklmnopqrstuvwxyzೖೠೡೢೣ", " ംഃഅആഇഈഉഊഋ\nഌ \rഎഏഐ ഒഓഔകഖഗഘങച\uffffഛജഝഞ !ടഠഡഢണത)(ഥദ,ധ.ന0123456789:; പഫ?ബഭമയരറലളഴവശഷസഹ ഽാിീുൂൃൄ െേൈ ൊോൌ്ൗabcdefghijklmnopqrstuvwxyzൠൡൢൣ൹", "ଁଂଃଅଆଇଈଉଊଋ\nଌ \r ଏଐ  ଓଔକଖଗଘଙଚ\uffffଛଜଝଞ !ଟଠଡଢଣତ)(ଥଦ,ଧ.ନ0123456789:; ପଫ?ବଭମଯର ଲଳ ଵଶଷସହ଼ଽାିୀୁୂୃୄ  େୈ  ୋୌ୍ୖabcdefghijklmnopqrstuvwxyzୗୠୡୢୣ", "ਁਂਃਅਆਇਈਉਊ \n  \r ਏਐ  ਓਔਕਖਗਘਙਚ\uffffਛਜਝਞ !ਟਠਡਢਣਤ)(ਥਦ,ਧ.ਨ0123456789:; ਪਫ?ਬਭਮਯਰ ਲਲ਼ ਵਸ਼ ਸਹ਼ ਾਿੀੁੂ    ੇੈ  ੋੌ੍ੑabcdefghijklmnopqrstuvwxyzੰੱੲੳੴ", " ஂஃஅஆஇஈஉஊ \n  \rஎஏஐ ஒஓஔக   ஙச\uffff ஜ ஞ !ட   ணத)(  , .ந0123456789:;னப ?  மயரறலளழவஶஷஸஹ  ாிீுூ   ெேை ொோௌ்ௐabcdefghijklmnopqrstuvwxyzௗ௰௱௲௹", "ఁంఃఅఆఇఈఉఊఋ\nఌ \rఎఏఐ ఒఓఔకఖగఘఙచ\uffffఛజఝఞ !టఠడఢణత)(థద,ధ.న0123456789:; పఫ?బభమయరఱలళ వశషసహ ఽాిీుూృౄ ెేై ొోౌ్ౕabcdefghijklmnopqrstuvwxyzౖౠౡౢౣ", "اآبٻڀپڦتۂٿ\nٹٽ\rٺټثجځڄڃڅچڇحخد\uffffڌڈډڊ !ڏڍذرڑړ)(ڙز,ږ.ژ0123456789:;ښسش?صضطظعفقکڪګگڳڱلمنںڻڼوۄەہھءیېےٍُِٗٔabcdefghijklmnopqrstuvwxyzّٰٕٖٓ"};
    public protected static final String[] sLanguageShiftTables = {"          \f         ^                   {}     \\            [~] |                                    €                          ", "          \f         ^                   {}     \\            [~] |      Ğ İ         Ş               ç € ğ ı         ş            ", "         ç\f         ^                   {}     \\            [~] |Á       Í     Ó     Ú           á   €   í     ó     ú          ", "     ê   ç\fÔô Áá  ΦΓ^ΩΠΨΣΘ     Ê        {}     \\            [~] |À       Í     Ó     Ú     ÃÕ    Â   €   í     ó     ú     ãõ  â", "@£$¥¿\"¤%&'\f*+ -/<=>¡^¡_#*০১ ২৩৪৫৬৭৮৯য়ৠৡৢ{}ৣ৲৳৴৵\\৶৷৸৹৺       [~] |ABCDEFGHIJKLMNOPQRSTUVWXYZ          €                          ", "@£$¥¿\"¤%&'\f*+ -/<=>¡^¡_#*।॥ ૦૧૨૩૪૫૬૭૮૯  {}     \\            [~] |ABCDEFGHIJKLMNOPQRSTUVWXYZ          €                          ", "@£$¥¿\"¤%&'\f*+ -/<=>¡^¡_#*।॥ ०१२३४५६७८९॒॑{}॓॔क़ख़ग़\\ज़ड़ढ़फ़य़ॠॡॢॣ॰ॱ [~] |ABCDEFGHIJKLMNOPQRSTUVWXYZ          €                          ", "@£$¥¿\"¤%&'\f*+ -/<=>¡^¡_#*।॥ ೦೧೨೩೪೫೬೭೮೯ೞೱ{}ೲ    \\            [~] |ABCDEFGHIJKLMNOPQRSTUVWXYZ          €                          ", "@£$¥¿\"¤%&'\f*+ -/<=>¡^¡_#*।॥ ൦൧൨൩൪൫൬൭൮൯൰൱{}൲൳൴൵ൺ\\ൻർൽൾൿ       [~] |ABCDEFGHIJKLMNOPQRSTUVWXYZ          €                          ", "@£$¥¿\"¤%&'\f*+ -/<=>¡^¡_#*।॥ ୦୧୨୩୪୫୬୭୮୯ଡ଼ଢ଼{}ୟ୰ୱ  \\            [~] |ABCDEFGHIJKLMNOPQRSTUVWXYZ          €                          ", "@£$¥¿\"¤%&'\f*+ -/<=>¡^¡_#*।॥ ੦੧੨੩੪੫੬੭੮੯ਖ਼ਗ਼{}ਜ਼ੜਫ਼ੵ \\            [~] |ABCDEFGHIJKLMNOPQRSTUVWXYZ          €                          ", "@£$¥¿\"¤%&'\f*+ -/<=>¡^¡_#*।॥ ௦௧௨௩௪௫௬௭௮௯௳௴{}௵௶௷௸௺\\            [~] |ABCDEFGHIJKLMNOPQRSTUVWXYZ          €                          ", "@£$¥¿\"¤%&'\f*+ -/<=>¡^¡_#*   ౦౧౨౩౪౫౬౭౮౯ౘౙ{}౸౹౺౻౼\\౽౾౿         [~] |ABCDEFGHIJKLMNOPQRSTUVWXYZ          €                          ", "@£$¥¿\"¤%&'\f*+ -/<=>¡^¡_#*\u0600\u0601 ۰۱۲۳۴۵۶۷۸۹،؍{}؎؏ؐؑؒ\\ؓؔ؛؟ـْ٘٫٬ٲٳۍ[~]۔|ABCDEFGHIJKLMNOPQRSTUVWXYZ          €                          "};

    private synchronized GsmAlphabet() {
    }

    /* loaded from: classes3.dex */
    public static class TextEncodingDetails {
        private protected int codeUnitCount;
        private protected int codeUnitSize;
        private protected int codeUnitsRemaining;
        private protected int languageShiftTable;
        private protected int languageTable;
        private protected int msgCount;

        public String toString() {
            return "TextEncodingDetails { msgCount=" + this.msgCount + ", codeUnitCount=" + this.codeUnitCount + ", codeUnitsRemaining=" + this.codeUnitsRemaining + ", codeUnitSize=" + this.codeUnitSize + ", languageTable=" + this.languageTable + ", languageShiftTable=" + this.languageShiftTable + " }";
        }
    }

    private protected static int charToGsm(char c) {
        try {
            return charToGsm(c, false);
        } catch (EncodeException e) {
            return sCharsToGsmTables[0].get(32, 32);
        }
    }

    private protected static int charToGsm(char c, boolean throwException) throws EncodeException {
        int ret = sCharsToGsmTables[0].get(c, -1);
        if (ret == -1) {
            if (sCharsToShiftTables[0].get(c, -1) == -1) {
                if (throwException) {
                    throw new EncodeException(c);
                }
                return sCharsToGsmTables[0].get(32, 32);
            }
            return 27;
        }
        return ret;
    }

    public static synchronized int charToGsmExtended(char c) {
        int ret = sCharsToShiftTables[0].get(c, -1);
        if (ret == -1) {
            return sCharsToGsmTables[0].get(32, 32);
        }
        return ret;
    }

    private protected static char gsmToChar(int gsmChar) {
        if (gsmChar >= 0 && gsmChar < 128) {
            return sLanguageTables[0].charAt(gsmChar);
        }
        return ' ';
    }

    public static synchronized char gsmExtendedToChar(int gsmChar) {
        if (gsmChar == 27 || gsmChar < 0 || gsmChar >= 128) {
            return ' ';
        }
        char c = sLanguageShiftTables[0].charAt(gsmChar);
        if (c == ' ') {
            return sLanguageTables[0].charAt(gsmChar);
        }
        return c;
    }

    public static synchronized byte[] stringToGsm7BitPackedWithHeader(String data, byte[] header) throws EncodeException {
        return stringToGsm7BitPackedWithHeader(data, header, 0, 0);
    }

    /* JADX INFO: Access modifiers changed from: private */
    public static byte[] stringToGsm7BitPackedWithHeader(String data, byte[] header, int languageTable, int languageShiftTable) throws EncodeException {
        if (header == null || header.length == 0) {
            return stringToGsm7BitPacked(data, languageTable, languageShiftTable);
        }
        int headerBits = (header.length + 1) * 8;
        int headerSeptets = (headerBits + 6) / 7;
        byte[] ret = stringToGsm7BitPacked(data, headerSeptets, true, languageTable, languageShiftTable);
        ret[1] = (byte) header.length;
        System.arraycopy(header, 0, ret, 2, header.length);
        return ret;
    }

    private protected static byte[] stringToGsm7BitPacked(String data) throws EncodeException {
        return stringToGsm7BitPacked(data, 0, true, 0, 0);
    }

    public static synchronized byte[] stringToGsm7BitPacked(String data, int languageTable, int languageShiftTable) throws EncodeException {
        return stringToGsm7BitPacked(data, 0, true, languageTable, languageShiftTable);
    }

    /* JADX INFO: Access modifiers changed from: private */
    public static byte[] stringToGsm7BitPacked(String data, int startingSeptetOffset, boolean throwException, int languageTable, int languageShiftTable) throws EncodeException {
        String str = data;
        int dataLen = str.length();
        int septetCount = countGsmSeptetsUsingTables(str, !throwException, languageTable, languageShiftTable);
        int i = -1;
        if (septetCount == -1) {
            throw new EncodeException("countGsmSeptetsUsingTables(): unencodable char");
        }
        int septetCount2 = septetCount + startingSeptetOffset;
        if (septetCount2 > 255) {
            throw new EncodeException("Payload cannot exceed 255 septets");
        }
        int byteCount = ((septetCount2 * 7) + 7) / 8;
        byte[] ret = new byte[byteCount + 1];
        SparseIntArray charToLanguageTable = sCharsToGsmTables[languageTable];
        SparseIntArray charToShiftTable = sCharsToShiftTables[languageShiftTable];
        int i2 = 0;
        int septets = startingSeptetOffset;
        int bitOffset = startingSeptetOffset * 7;
        while (i2 < dataLen && septets < septetCount2) {
            char c = str.charAt(i2);
            int v = charToLanguageTable.get(c, i);
            if (v == i) {
                int v2 = charToShiftTable.get(c, i);
                if (v2 == i) {
                    if (throwException) {
                        throw new EncodeException("stringToGsm7BitPacked(): unencodable char");
                    }
                    v = charToLanguageTable.get(32, 32);
                } else {
                    packSmsChar(ret, bitOffset, 27);
                    bitOffset += 7;
                    septets++;
                    v = v2;
                }
            }
            packSmsChar(ret, bitOffset, v);
            septets++;
            i2++;
            bitOffset += 7;
            str = data;
            i = -1;
        }
        ret[0] = (byte) septetCount2;
        return ret;
    }

    public protected static void packSmsChar(byte[] packedChars, int bitOffset, int value) {
        int shift = bitOffset % 8;
        int byteOffset = (bitOffset / 8) + 1;
        packedChars[byteOffset] = (byte) (packedChars[byteOffset] | (value << shift));
        if (shift > 1) {
            packedChars[byteOffset + 1] = (byte) (value >> (8 - shift));
        }
    }

    /* JADX INFO: Access modifiers changed from: private */
    public static String gsm7BitPackedToString(byte[] pdu, int offset, int lengthSeptets) {
        return gsm7BitPackedToString(pdu, offset, lengthSeptets, 0, 0, 0);
    }

    /* JADX INFO: Access modifiers changed from: private */
    public static String gsm7BitPackedToString(byte[] pdu, int offset, int lengthSeptets, int numPaddingBits, int languageTable, int shiftTable) {
        int i = languageTable;
        int shiftTable2 = shiftTable;
        StringBuilder ret = new StringBuilder(lengthSeptets);
        if (i < 0 || i > sLanguageTables.length) {
            Rlog.w(TAG, "unknown language table " + i + ", using default");
            i = 0;
        }
        int languageTable2 = i;
        if (shiftTable2 < 0 || shiftTable2 > sLanguageShiftTables.length) {
            Rlog.w(TAG, "unknown single shift table " + shiftTable2 + ", using default");
            shiftTable2 = 0;
        }
        boolean prevCharWasEscape = false;
        try {
            String languageTableToChar = sLanguageTables[languageTable2];
            String shiftTableToChar = sLanguageShiftTables[shiftTable2];
            int i2 = 0;
            if (languageTableToChar.isEmpty()) {
                Rlog.w(TAG, "no language table for code " + languageTable2 + ", using default");
                languageTableToChar = sLanguageTables[0];
            }
            if (shiftTableToChar.isEmpty()) {
                Rlog.w(TAG, "no single shift table for code " + shiftTable2 + ", using default");
                shiftTableToChar = sLanguageShiftTables[0];
            }
            while (true) {
                int i3 = i2;
                if (i3 < lengthSeptets) {
                    int bitOffset = (7 * i3) + numPaddingBits;
                    int byteOffset = bitOffset / 8;
                    int shift = bitOffset % 8;
                    int gsmVal = (pdu[offset + byteOffset] >> shift) & 127;
                    if (shift > 1) {
                        gsmVal = (gsmVal & (127 >> (shift - 1))) | ((pdu[(offset + byteOffset) + 1] << (8 - shift)) & 127);
                    }
                    if (prevCharWasEscape) {
                        if (gsmVal == 27) {
                            ret.append(' ');
                        } else {
                            char c = shiftTableToChar.charAt(gsmVal);
                            if (c == ' ') {
                                ret.append(languageTableToChar.charAt(gsmVal));
                            } else {
                                ret.append(c);
                            }
                        }
                        prevCharWasEscape = false;
                    } else {
                        boolean prevCharWasEscape2 = prevCharWasEscape;
                        if (gsmVal == 27) {
                            prevCharWasEscape = true;
                        } else {
                            ret.append(languageTableToChar.charAt(gsmVal));
                            prevCharWasEscape = prevCharWasEscape2;
                            i2 = i3 + 1;
                        }
                    }
                    i2 = i3 + 1;
                } else {
                    return ret.toString();
                }
            }
        } catch (RuntimeException ex) {
            Rlog.e(TAG, "Error GSM 7 bit packed: ", ex);
            return null;
        }
    }

    /* JADX INFO: Access modifiers changed from: private */
    public static String gsm8BitUnpackedToString(byte[] data, int offset, int length) {
        return gsm8BitUnpackedToString(data, offset, length, "");
    }

    /* JADX INFO: Access modifiers changed from: private */
    public static String gsm8BitUnpackedToString(byte[] data, int offset, int length, String characterset) {
        int c;
        char c2;
        boolean isMbcs = false;
        Charset charset = null;
        ByteBuffer mbcsBuffer = null;
        char c3 = 2;
        if (!TextUtils.isEmpty(characterset) && !characterset.equalsIgnoreCase("us-ascii") && Charset.isSupported(characterset)) {
            isMbcs = true;
            charset = Charset.forName(characterset);
            mbcsBuffer = ByteBuffer.allocate(2);
        }
        String languageTableToChar = sLanguageTables[0];
        String shiftTableToChar = sLanguageShiftTables[0];
        StringBuilder ret = new StringBuilder(length);
        boolean prevWasEscape = false;
        int i = offset;
        while (i < offset + length && (c = data[i] & 255) != 255) {
            if (c == 27) {
                if (prevWasEscape) {
                    ret.append(' ');
                    prevWasEscape = false;
                } else {
                    prevWasEscape = true;
                }
                c2 = c3;
            } else {
                if (prevWasEscape) {
                    char shiftChar = c < shiftTableToChar.length() ? shiftTableToChar.charAt(c) : ' ';
                    if (shiftChar == ' ') {
                        if (c < languageTableToChar.length()) {
                            ret.append(languageTableToChar.charAt(c));
                        } else {
                            ret.append(' ');
                        }
                    } else {
                        ret.append(shiftChar);
                    }
                    c2 = 2;
                } else if (!isMbcs || c < 128 || i + 1 >= offset + length) {
                    c2 = 2;
                    if (c < languageTableToChar.length()) {
                        ret.append(languageTableToChar.charAt(c));
                    } else {
                        ret.append(' ');
                    }
                } else {
                    mbcsBuffer.clear();
                    c2 = 2;
                    mbcsBuffer.put(data, i, 2);
                    mbcsBuffer.flip();
                    ret.append(charset.decode(mbcsBuffer).toString());
                    i++;
                }
                prevWasEscape = false;
            }
            i++;
            c3 = c2;
        }
        return ret.toString();
    }

    private protected static byte[] stringToGsm8BitPacked(String s) {
        int septets = countGsmSeptetsUsingTables(s, true, 0, 0);
        byte[] ret = new byte[septets];
        stringToGsm8BitUnpackedField(s, ret, 0, ret.length);
        return ret;
    }

    /* JADX WARN: Incorrect condition in loop: B:18:0x0045 */
    /*
        Code decompiled incorrectly, please refer to instructions dump.
        To view partially-correct add '--show-bad-code' argument
    */
    public static synchronized void stringToGsm8BitUnpackedField(java.lang.String r9, byte[] r10, int r11, int r12) {
        /*
            r0 = r11
            android.util.SparseIntArray[] r1 = com.android.internal.telephony.GsmAlphabet.sCharsToGsmTables
            r2 = 0
            r1 = r1[r2]
            android.util.SparseIntArray[] r3 = com.android.internal.telephony.GsmAlphabet.sCharsToShiftTables
            r2 = r3[r2]
            r3 = 0
            int r4 = r9.length()
        Lf:
            r5 = -1
            if (r3 >= r4) goto L43
            int r6 = r0 - r11
            if (r6 >= r12) goto L43
            char r6 = r9.charAt(r3)
            int r7 = r1.get(r6, r5)
            if (r7 != r5) goto L3a
            int r7 = r2.get(r6, r5)
            if (r7 != r5) goto L2d
            r5 = 32
            int r7 = r1.get(r5, r5)
            goto L3a
        L2d:
            int r8 = r0 + 1
            int r8 = r8 - r11
            if (r8 < r12) goto L33
            goto L43
        L33:
            int r5 = r0 + 1
            r8 = 27
            r10[r0] = r8
            r0 = r5
        L3a:
            int r5 = r0 + 1
            byte r8 = (byte) r7
            r10[r0] = r8
            int r3 = r3 + 1
            r0 = r5
            goto Lf
        L43:
            int r3 = r0 - r11
            if (r3 >= r12) goto L4d
            int r3 = r0 + 1
            r10[r0] = r5
            r0 = r3
            goto L43
        L4d:
            return
        */
        throw new UnsupportedOperationException("Method not decompiled: com.android.internal.telephony.GsmAlphabet.stringToGsm8BitUnpackedField(java.lang.String, byte[], int, int):void");
    }

    public static synchronized int countGsmSeptets(char c) {
        try {
            return countGsmSeptets(c, false);
        } catch (EncodeException e) {
            return 0;
        }
    }

    private protected static int countGsmSeptets(char c, boolean throwsException) throws EncodeException {
        if (sCharsToGsmTables[0].get(c, -1) != -1) {
            return 1;
        }
        if (sCharsToShiftTables[0].get(c, -1) != -1) {
            return 2;
        }
        if (throwsException) {
            throw new EncodeException(c);
        }
        return 1;
    }

    public static synchronized boolean isGsmSeptets(char c) {
        return (sCharsToGsmTables[0].get(c, -1) == -1 && sCharsToShiftTables[0].get(c, -1) == -1) ? false : true;
    }

    public static synchronized int countGsmSeptetsUsingTables(CharSequence s, boolean use7bitOnly, int languageTable, int languageShiftTable) {
        int count = 0;
        int sz = s.length();
        SparseIntArray charToLanguageTable = sCharsToGsmTables[languageTable];
        SparseIntArray charToShiftTable = sCharsToShiftTables[languageShiftTable];
        for (int i = 0; i < sz; i++) {
            char c = s.charAt(i);
            if (c == 27) {
                Rlog.w(TAG, "countGsmSeptets() string contains Escape character, skipping.");
            } else if (charToLanguageTable.get(c, -1) == -1) {
                if (charToShiftTable.get(c, -1) != -1) {
                    count += 2;
                } else if (!use7bitOnly) {
                    return -1;
                } else {
                    count++;
                }
            } else {
                count++;
            }
        }
        return count;
    }

    public static synchronized TextEncodingDetails countGsmSeptets(CharSequence s, boolean use7bitOnly) {
        int[] iArr;
        int udhLength;
        int msgCount;
        int septetsRemaining;
        int septetsRemaining2;
        if (!sDisableCountryEncodingCheck) {
            enableCountrySpecificEncodings();
        }
        int i = 160;
        int i2 = -1;
        int i3 = 0;
        if (sEnabledSingleShiftTables.length + sEnabledLockingShiftTables.length == 0) {
            TextEncodingDetails ted = new TextEncodingDetails();
            int septets = countGsmSeptetsUsingTables(s, use7bitOnly, 0, 0);
            if (septets == -1) {
                return null;
            }
            ted.codeUnitSize = 1;
            ted.codeUnitCount = septets;
            if (septets > 160) {
                ted.msgCount = (septets + 152) / 153;
                ted.codeUnitsRemaining = (ted.msgCount * 153) - septets;
            } else {
                ted.msgCount = 1;
                ted.codeUnitsRemaining = 160 - septets;
            }
            ted.codeUnitSize = 1;
            return ted;
        }
        int maxSingleShiftCode = sHighestEnabledSingleShiftCode;
        List<LanguagePairCount> lpcList = new ArrayList<>(sEnabledLockingShiftTables.length + 1);
        lpcList.add(new LanguagePairCount(0));
        for (int i4 : sEnabledLockingShiftTables) {
            if (i4 != 0 && !sLanguageTables[i4].isEmpty()) {
                lpcList.add(new LanguagePairCount(i4));
            }
        }
        int sz = s.length();
        int i5 = 0;
        while (i5 < sz && !lpcList.isEmpty()) {
            char c = s.charAt(i5);
            if (c == 27) {
                Rlog.w(TAG, "countGsmSeptets() string contains Escape character, ignoring!");
            } else {
                for (LanguagePairCount lpc : lpcList) {
                    int tableIndex = sCharsToGsmTables[lpc.languageCode].get(c, -1);
                    if (tableIndex == -1) {
                        for (int table = i3; table <= maxSingleShiftCode; table++) {
                            if (lpc.septetCounts[table] != -1) {
                                int shiftTableIndex = sCharsToShiftTables[table].get(c, -1);
                                if (shiftTableIndex == -1) {
                                    if (use7bitOnly) {
                                        int[] iArr2 = lpc.septetCounts;
                                        iArr2[table] = iArr2[table] + 1;
                                        int[] iArr3 = lpc.unencodableCounts;
                                        iArr3[table] = iArr3[table] + 1;
                                    } else {
                                        lpc.septetCounts[table] = -1;
                                    }
                                } else {
                                    int[] iArr4 = lpc.septetCounts;
                                    iArr4[table] = iArr4[table] + 2;
                                }
                            }
                        }
                    } else {
                        for (int table2 = 0; table2 <= maxSingleShiftCode; table2++) {
                            if (lpc.septetCounts[table2] != -1) {
                                int[] iArr5 = lpc.septetCounts;
                                iArr5[table2] = iArr5[table2] + 1;
                            }
                        }
                    }
                    i3 = 0;
                }
            }
            i5++;
            i3 = 0;
        }
        TextEncodingDetails ted2 = new TextEncodingDetails();
        ted2.msgCount = Integer.MAX_VALUE;
        ted2.codeUnitSize = 1;
        int minUnencodableCount = Integer.MAX_VALUE;
        for (LanguagePairCount lpc2 : lpcList) {
            int minUnencodableCount2 = minUnencodableCount;
            int minUnencodableCount3 = 0;
            while (minUnencodableCount3 <= maxSingleShiftCode) {
                int septets2 = lpc2.septetCounts[minUnencodableCount3];
                if (septets2 != i2) {
                    if (lpc2.languageCode != 0 && minUnencodableCount3 != 0) {
                        udhLength = 8;
                    } else {
                        int udhLength2 = lpc2.languageCode;
                        if (udhLength2 != 0 || minUnencodableCount3 != 0) {
                            udhLength = 5;
                        } else {
                            udhLength = 0;
                        }
                    }
                    if (septets2 + udhLength > i) {
                        if (udhLength == 0) {
                            udhLength = 1;
                        }
                        int septetsPerMessage = 160 - (udhLength + 6);
                        msgCount = ((septets2 + septetsPerMessage) - 1) / septetsPerMessage;
                        septetsRemaining = (msgCount * septetsPerMessage) - septets2;
                    } else {
                        msgCount = 1;
                        septetsRemaining = (160 - udhLength) - septets2;
                    }
                    int msgCount2 = msgCount;
                    int septetsRemaining3 = septetsRemaining;
                    int unencodableCount = lpc2.unencodableCounts[minUnencodableCount3];
                    if (!use7bitOnly || unencodableCount <= minUnencodableCount2) {
                        if (!use7bitOnly || unencodableCount >= minUnencodableCount2) {
                            if (msgCount2 < ted2.msgCount) {
                                septetsRemaining2 = septetsRemaining3;
                            } else if (msgCount2 == ted2.msgCount) {
                                septetsRemaining2 = septetsRemaining3;
                                if (septetsRemaining2 <= ted2.codeUnitsRemaining) {
                                }
                            }
                        } else {
                            septetsRemaining2 = septetsRemaining3;
                        }
                        minUnencodableCount2 = unencodableCount;
                        ted2.msgCount = msgCount2;
                        ted2.codeUnitCount = septets2;
                        ted2.codeUnitsRemaining = septetsRemaining2;
                        ted2.languageTable = lpc2.languageCode;
                        ted2.languageShiftTable = minUnencodableCount3;
                    }
                }
                minUnencodableCount3++;
                i = 160;
                i2 = -1;
            }
            minUnencodableCount = minUnencodableCount2;
            i = 160;
            i2 = -1;
        }
        if (ted2.msgCount == Integer.MAX_VALUE) {
            return null;
        }
        return ted2;
    }

    /* JADX INFO: Access modifiers changed from: private */
    public static int findGsmSeptetLimitIndex(String s, int start, int limit, int langTable, int langShiftTable) {
        int size = s.length();
        SparseIntArray charToLangTable = sCharsToGsmTables[langTable];
        SparseIntArray charToLangShiftTable = sCharsToShiftTables[langShiftTable];
        int accumulator = 0;
        for (int accumulator2 = start; accumulator2 < size; accumulator2++) {
            int encodedSeptet = charToLangTable.get(s.charAt(accumulator2), -1);
            if (encodedSeptet == -1) {
                int encodedSeptet2 = charToLangShiftTable.get(s.charAt(accumulator2), -1);
                if (encodedSeptet2 == -1) {
                    accumulator++;
                } else {
                    accumulator += 2;
                }
            } else {
                accumulator++;
            }
            if (accumulator > limit) {
                return accumulator2;
            }
        }
        return size;
    }

    public static synchronized void setEnabledSingleShiftTables(int[] tables) {
        synchronized (GsmAlphabet.class) {
            sEnabledSingleShiftTables = tables;
            sDisableCountryEncodingCheck = true;
            if (tables.length > 0) {
                sHighestEnabledSingleShiftCode = tables[tables.length - 1];
            } else {
                sHighestEnabledSingleShiftCode = 0;
            }
        }
    }

    public static synchronized void setEnabledLockingShiftTables(int[] tables) {
        synchronized (GsmAlphabet.class) {
            sEnabledLockingShiftTables = tables;
            sDisableCountryEncodingCheck = true;
        }
    }

    public static synchronized int[] getEnabledSingleShiftTables() {
        int[] iArr;
        synchronized (GsmAlphabet.class) {
            iArr = sEnabledSingleShiftTables;
        }
        return iArr;
    }

    public static synchronized int[] getEnabledLockingShiftTables() {
        int[] iArr;
        synchronized (GsmAlphabet.class) {
            iArr = sEnabledLockingShiftTables;
        }
        return iArr;
    }

    private static synchronized void enableCountrySpecificEncodings() {
        Resources r = Resources.getSystem();
        sEnabledSingleShiftTables = r.getIntArray(17236041);
        sEnabledLockingShiftTables = r.getIntArray(17236040);
        if (sEnabledSingleShiftTables.length > 0) {
            sHighestEnabledSingleShiftCode = sEnabledSingleShiftTables[sEnabledSingleShiftTables.length - 1];
        } else {
            sHighestEnabledSingleShiftCode = 0;
        }
    }

    static {
        enableCountrySpecificEncodings();
        int numTables = sLanguageTables.length;
        int numShiftTables = sLanguageShiftTables.length;
        if (numTables != numShiftTables) {
            Rlog.e(TAG, "Error: language tables array length " + numTables + " != shift tables array length " + numShiftTables);
        }
        sCharsToGsmTables = new SparseIntArray[numTables];
        for (int i = 0; i < numTables; i++) {
            String table = sLanguageTables[i];
            int tableLen = table.length();
            if (tableLen != 0 && tableLen != 128) {
                Rlog.e(TAG, "Error: language tables index " + i + " length " + tableLen + " (expected 128 or 0)");
            }
            SparseIntArray charToGsmTable = new SparseIntArray(tableLen);
            sCharsToGsmTables[i] = charToGsmTable;
            for (int j = 0; j < tableLen; j++) {
                charToGsmTable.put(table.charAt(j), j);
            }
        }
        sCharsToShiftTables = new SparseIntArray[numTables];
        for (int i2 = 0; i2 < numShiftTables; i2++) {
            String shiftTable = sLanguageShiftTables[i2];
            int shiftTableLen = shiftTable.length();
            if (shiftTableLen != 0 && shiftTableLen != 128) {
                Rlog.e(TAG, "Error: language shift tables index " + i2 + " length " + shiftTableLen + " (expected 128 or 0)");
            }
            SparseIntArray charToShiftTable = new SparseIntArray(shiftTableLen);
            sCharsToShiftTables[i2] = charToShiftTable;
            for (int j2 = 0; j2 < shiftTableLen; j2++) {
                char c = shiftTable.charAt(j2);
                if (c != ' ') {
                    charToShiftTable.put(c, j2);
                }
            }
        }
    }

    /* loaded from: classes3.dex */
    private static class LanguagePairCount {
        public private protected final int languageCode;
        public private protected final int[] septetCounts;
        public private protected final int[] unencodableCounts;

        public private protected LanguagePairCount(int code) {
            this.languageCode = code;
            int maxSingleShiftCode = GsmAlphabet.sHighestEnabledSingleShiftCode;
            this.septetCounts = new int[maxSingleShiftCode + 1];
            this.unencodableCounts = new int[maxSingleShiftCode + 1];
            int tableOffset = 0;
            for (int i = 1; i <= maxSingleShiftCode; i++) {
                if (GsmAlphabet.sEnabledSingleShiftTables[tableOffset] == i) {
                    tableOffset++;
                } else {
                    this.septetCounts[i] = -1;
                }
            }
            if (code == 1 && maxSingleShiftCode >= 1) {
                this.septetCounts[1] = -1;
            } else if (code == 3 && maxSingleShiftCode >= 2) {
                this.septetCounts[2] = -1;
            }
        }
    }
}
