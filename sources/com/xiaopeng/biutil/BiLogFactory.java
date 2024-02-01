package com.xiaopeng.biutil;

/* loaded from: classes3.dex */
public class BiLogFactory {
    public static BiLog create(String moduleId, String pageId, String buttonId) {
        return new BiLog(moduleId, pageId, buttonId);
    }

    public static BiLog create(String pageId, String buttonId) {
        return new BiLog(pageId, buttonId);
    }

    public static BiLog create(String buttonId) {
        return new BiLog(buttonId);
    }
}
