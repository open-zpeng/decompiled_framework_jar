package com.android.internal.midi;

import android.media.midi.MidiReceiver;
import android.media.midi.MidiSender;
import java.io.IOException;
import java.util.Iterator;
import java.util.concurrent.CopyOnWriteArrayList;
/* loaded from: classes3.dex */
public final class MidiDispatcher extends MidiReceiver {
    public protected final MidiReceiverFailureHandler mFailureHandler;
    public protected final CopyOnWriteArrayList<MidiReceiver> mReceivers;
    public protected final MidiSender mSender;

    /* loaded from: classes3.dex */
    public interface MidiReceiverFailureHandler {
        private protected synchronized void onReceiverFailure(MidiReceiver midiReceiver, IOException iOException);
    }

    /* JADX INFO: Access modifiers changed from: private */
    public synchronized MidiDispatcher() {
        this(null);
    }

    /* JADX INFO: Access modifiers changed from: private */
    public synchronized MidiDispatcher(MidiReceiverFailureHandler failureHandler) {
        this.mReceivers = new CopyOnWriteArrayList<>();
        this.mSender = new MidiSender() { // from class: com.android.internal.midi.MidiDispatcher.1
            @Override // android.media.midi.MidiSender
            public void onConnect(MidiReceiver receiver) {
                MidiDispatcher.this.mReceivers.add(receiver);
            }

            @Override // android.media.midi.MidiSender
            public void onDisconnect(MidiReceiver receiver) {
                MidiDispatcher.this.mReceivers.remove(receiver);
            }
        };
        this.mFailureHandler = failureHandler;
    }

    /* JADX INFO: Access modifiers changed from: private */
    public synchronized int getReceiverCount() {
        return this.mReceivers.size();
    }

    /* JADX INFO: Access modifiers changed from: private */
    public synchronized MidiSender getSender() {
        return this.mSender;
    }

    @Override // android.media.midi.MidiReceiver
    public void onSend(byte[] msg, int offset, int count, long timestamp) throws IOException {
        Iterator<MidiReceiver> it = this.mReceivers.iterator();
        while (it.hasNext()) {
            MidiReceiver receiver = it.next();
            try {
                receiver.send(msg, offset, count, timestamp);
            } catch (IOException e) {
                this.mReceivers.remove(receiver);
                if (this.mFailureHandler != null) {
                    this.mFailureHandler.onReceiverFailure(receiver, e);
                }
            }
        }
    }

    @Override // android.media.midi.MidiReceiver
    public void onFlush() throws IOException {
        Iterator<MidiReceiver> it = this.mReceivers.iterator();
        while (it.hasNext()) {
            MidiReceiver receiver = it.next();
            try {
                receiver.flush();
            } catch (IOException e) {
                this.mReceivers.remove(receiver);
                if (this.mFailureHandler != null) {
                    this.mFailureHandler.onReceiverFailure(receiver, e);
                }
            }
        }
    }
}
