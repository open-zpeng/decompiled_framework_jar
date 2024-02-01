package android.net.wifi.aware;

import java.util.List;

/* loaded from: classes2.dex */
public class DiscoverySessionCallback {
    public void onPublishStarted(PublishDiscoverySession session) {
    }

    public void onSubscribeStarted(SubscribeDiscoverySession session) {
    }

    public void onSessionConfigUpdated() {
    }

    public void onSessionConfigFailed() {
    }

    public void onSessionTerminated() {
    }

    public void onServiceDiscovered(PeerHandle peerHandle, byte[] serviceSpecificInfo, List<byte[]> matchFilter) {
    }

    public void onServiceDiscoveredWithinRange(PeerHandle peerHandle, byte[] serviceSpecificInfo, List<byte[]> matchFilter, int distanceMm) {
    }

    public void onMessageSendSucceeded(int messageId) {
    }

    public void onMessageSendFailed(int messageId) {
    }

    public void onMessageReceived(PeerHandle peerHandle, byte[] message) {
    }
}
