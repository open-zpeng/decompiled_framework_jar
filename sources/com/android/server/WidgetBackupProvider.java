package com.android.server;

import java.util.List;
/* loaded from: classes3.dex */
public interface WidgetBackupProvider {
    List<String> getWidgetParticipants(int i);

    byte[] getWidgetState(String str, int i);

    void restoreFinished(int i);

    void restoreStarting(int i);

    void restoreWidgetState(String str, byte[] bArr, int i);
}
