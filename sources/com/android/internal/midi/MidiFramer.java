package com.android.internal.midi;

import android.media.midi.MidiReceiver;
import java.io.IOException;
/* loaded from: classes3.dex */
public class MidiFramer extends MidiReceiver {
    public String TAG = "MidiFramer";
    private byte[] mBuffer = new byte[3];
    private int mCount;
    private boolean mInSysEx;
    private int mNeeded;
    private MidiReceiver mReceiver;
    private byte mRunningStatus;

    public MidiFramer(MidiReceiver receiver) {
        this.mReceiver = receiver;
    }

    public static String formatMidiData(byte[] data, int offset, int count) {
        String text = "MIDI+" + offset + " : ";
        String text2 = text;
        for (int i = 0; i < count; i++) {
            text2 = text2 + String.format("0x%02X, ", Byte.valueOf(data[offset + i]));
        }
        return text2;
    }

    @Override // android.media.midi.MidiReceiver
    public void onSend(byte[] data, int offset, int count, long timestamp) throws IOException {
        int sysExStartOffset;
        int sysExStartOffset2 = this.mInSysEx ? offset : -1;
        int offset2 = offset;
        int sysExStartOffset3 = sysExStartOffset2;
        for (int sysExStartOffset4 = 0; sysExStartOffset4 < count; sysExStartOffset4++) {
            byte currentByte = data[offset2];
            int currentInt = currentByte & 255;
            if (currentInt >= 128) {
                if (currentInt < 240) {
                    this.mRunningStatus = currentByte;
                    this.mCount = 1;
                    this.mNeeded = MidiConstants.getBytesPerMessage(currentByte) - 1;
                } else if (currentInt < 248) {
                    if (currentInt == 240) {
                        this.mInSysEx = true;
                        sysExStartOffset = offset2;
                    } else if (currentInt != 247) {
                        this.mBuffer[0] = currentByte;
                        this.mRunningStatus = (byte) 0;
                        this.mCount = 1;
                        this.mNeeded = MidiConstants.getBytesPerMessage(currentByte) - 1;
                    } else if (this.mInSysEx) {
                        this.mReceiver.send(data, sysExStartOffset3, (offset2 - sysExStartOffset3) + 1, timestamp);
                        this.mInSysEx = false;
                        sysExStartOffset = -1;
                    }
                    sysExStartOffset3 = sysExStartOffset;
                } else {
                    if (this.mInSysEx) {
                        this.mReceiver.send(data, sysExStartOffset3, offset2 - sysExStartOffset3, timestamp);
                        sysExStartOffset3 = offset2 + 1;
                    }
                    this.mReceiver.send(data, offset2, 1, timestamp);
                }
            } else if (!this.mInSysEx) {
                byte[] bArr = this.mBuffer;
                int i = this.mCount;
                this.mCount = i + 1;
                bArr[i] = currentByte;
                int i2 = this.mNeeded - 1;
                this.mNeeded = i2;
                if (i2 == 0) {
                    if (this.mRunningStatus != 0) {
                        this.mBuffer[0] = this.mRunningStatus;
                    }
                    this.mReceiver.send(this.mBuffer, 0, this.mCount, timestamp);
                    this.mNeeded = MidiConstants.getBytesPerMessage(this.mBuffer[0]) - 1;
                    this.mCount = 1;
                }
            }
            offset2++;
        }
        if (sysExStartOffset3 >= 0 && sysExStartOffset3 < offset2) {
            this.mReceiver.send(data, sysExStartOffset3, offset2 - sysExStartOffset3, timestamp);
        }
    }
}
