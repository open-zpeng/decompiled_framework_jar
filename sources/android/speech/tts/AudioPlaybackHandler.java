package android.speech.tts;

import java.util.Iterator;
import java.util.concurrent.LinkedBlockingQueue;
/* JADX INFO: Access modifiers changed from: package-private */
/* loaded from: classes2.dex */
public class AudioPlaybackHandler {
    private static final boolean DBG = false;
    private static final String TAG = "TTS.AudioPlaybackHandler";
    private final LinkedBlockingQueue<PlaybackQueueItem> mQueue = new LinkedBlockingQueue<>();
    private volatile PlaybackQueueItem mCurrentWorkItem = null;
    private final Thread mHandlerThread = new Thread(new MessageLoop(), "TTS.AudioPlaybackThread");

    public synchronized void start() {
        this.mHandlerThread.start();
    }

    private synchronized void stop(PlaybackQueueItem item) {
        if (item == null) {
            return;
        }
        item.stop(-2);
    }

    public synchronized void enqueue(PlaybackQueueItem item) {
        try {
            this.mQueue.put(item);
        } catch (InterruptedException e) {
        }
    }

    public synchronized void stopForApp(Object callerIdentity) {
        removeWorkItemsFor(callerIdentity);
        PlaybackQueueItem current = this.mCurrentWorkItem;
        if (current != null && current.getCallerIdentity() == callerIdentity) {
            stop(current);
        }
    }

    public synchronized void stop() {
        removeAllMessages();
        stop(this.mCurrentWorkItem);
    }

    public synchronized boolean isSpeaking() {
        return (this.mQueue.peek() == null && this.mCurrentWorkItem == null) ? false : true;
    }

    public synchronized void quit() {
        removeAllMessages();
        stop(this.mCurrentWorkItem);
        this.mHandlerThread.interrupt();
    }

    private synchronized void removeAllMessages() {
        this.mQueue.clear();
    }

    private synchronized void removeWorkItemsFor(Object callerIdentity) {
        Iterator<PlaybackQueueItem> it = this.mQueue.iterator();
        while (it.hasNext()) {
            PlaybackQueueItem item = it.next();
            if (item.getCallerIdentity() == callerIdentity) {
                it.remove();
                stop(item);
            }
        }
    }

    /* loaded from: classes2.dex */
    private final class MessageLoop implements Runnable {
        private MessageLoop() {
        }

        @Override // java.lang.Runnable
        public void run() {
            while (true) {
                try {
                    PlaybackQueueItem item = (PlaybackQueueItem) AudioPlaybackHandler.this.mQueue.take();
                    AudioPlaybackHandler.this.mCurrentWorkItem = item;
                    item.run();
                    AudioPlaybackHandler.this.mCurrentWorkItem = null;
                } catch (InterruptedException e) {
                    return;
                }
            }
        }
    }
}
