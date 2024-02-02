package com.android.server.net;

import android.net.INetdEventCallback;
/* loaded from: classes3.dex */
public class BaseNetdEventCallback extends INetdEventCallback.Stub {
    @Override // android.net.INetdEventCallback
    public void onDnsEvent(String hostname, String[] ipAddresses, int ipAddressesCount, long timestamp, int uid) {
    }

    @Override // android.net.INetdEventCallback
    public void onPrivateDnsValidationEvent(int netId, String ipAddress, String hostname, boolean validated) {
    }

    @Override // android.net.INetdEventCallback
    public void onConnectEvent(String ipAddr, int port, long timestamp, int uid) {
    }
}
