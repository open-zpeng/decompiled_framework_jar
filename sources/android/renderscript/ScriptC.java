package android.renderscript;

import android.content.res.Resources;
import java.io.IOException;
import java.io.InputStream;
/* loaded from: classes2.dex */
public class ScriptC extends Script {
    private static final String TAG = "ScriptC";

    protected ScriptC(int id, RenderScript rs) {
        super(id, rs);
    }

    protected ScriptC(long id, RenderScript rs) {
        super(id, rs);
    }

    protected ScriptC(RenderScript rs, Resources resources, int resourceID) {
        super(0L, rs);
        long id = internalCreate(rs, resources, resourceID);
        if (id == 0) {
            throw new RSRuntimeException("Loading of ScriptC script failed.");
        }
        setID(id);
    }

    protected ScriptC(RenderScript rs, String resName, byte[] bitcode32, byte[] bitcode64) {
        super(0L, rs);
        long id;
        if (RenderScript.sPointerSize == 4) {
            id = internalStringCreate(rs, resName, bitcode32);
        } else {
            id = internalStringCreate(rs, resName, bitcode64);
        }
        if (id == 0) {
            throw new RSRuntimeException("Loading of ScriptC script failed.");
        }
        setID(id);
    }

    private static synchronized long internalCreate(RenderScript rs, Resources resources, int resourceID) {
        long nScriptCCreate;
        synchronized (ScriptC.class) {
            InputStream is = resources.openRawResource(resourceID);
            try {
                try {
                    byte[] pgm = new byte[1024];
                    byte[] pgm2 = pgm;
                    int pgmLength = 0;
                    while (true) {
                        int bytesLeft = pgm2.length - pgmLength;
                        if (bytesLeft == 0) {
                            byte[] buf2 = new byte[pgm2.length * 2];
                            System.arraycopy(pgm2, 0, buf2, 0, pgm2.length);
                            pgm2 = buf2;
                            bytesLeft = pgm2.length - pgmLength;
                        }
                        int bytesRead = is.read(pgm2, pgmLength, bytesLeft);
                        if (bytesRead > 0) {
                            pgmLength += bytesRead;
                        } else {
                            byte[] pgm3 = pgm2;
                            String resName = resources.getResourceEntryName(resourceID);
                            nScriptCCreate = rs.nScriptCCreate(resName, RenderScript.getCachePath(), pgm3, pgmLength);
                        }
                    }
                } catch (IOException e) {
                    throw new Resources.NotFoundException();
                }
            } finally {
                is.close();
            }
        }
        return nScriptCCreate;
    }

    private static synchronized long internalStringCreate(RenderScript rs, String resName, byte[] bitcode) {
        long nScriptCCreate;
        synchronized (ScriptC.class) {
            nScriptCCreate = rs.nScriptCCreate(resName, RenderScript.getCachePath(), bitcode, bitcode.length);
        }
        return nScriptCCreate;
    }
}
