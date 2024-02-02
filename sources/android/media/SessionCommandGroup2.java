package android.media;

import android.media.update.ApiLoader;
import android.media.update.MediaSession2Provider;
import android.os.Bundle;
import java.util.Set;
/* loaded from: classes.dex */
public final class SessionCommandGroup2 {
    private final MediaSession2Provider.CommandGroupProvider mProvider;

    public synchronized SessionCommandGroup2() {
        this.mProvider = ApiLoader.getProvider().createMediaSession2CommandGroup(this, null);
    }

    public synchronized SessionCommandGroup2(SessionCommandGroup2 others) {
        this.mProvider = ApiLoader.getProvider().createMediaSession2CommandGroup(this, others);
    }

    public synchronized SessionCommandGroup2(MediaSession2Provider.CommandGroupProvider provider) {
        this.mProvider = provider;
    }

    public synchronized void addCommand(SessionCommand2 command) {
        this.mProvider.addCommand_impl(command);
    }

    public synchronized void addCommand(int commandCode) {
    }

    public synchronized void addAllPredefinedCommands() {
        this.mProvider.addAllPredefinedCommands_impl();
    }

    public synchronized void removeCommand(SessionCommand2 command) {
        this.mProvider.removeCommand_impl(command);
    }

    public synchronized void removeCommand(int commandCode) {
    }

    public synchronized boolean hasCommand(SessionCommand2 command) {
        return this.mProvider.hasCommand_impl(command);
    }

    public synchronized boolean hasCommand(int code) {
        return this.mProvider.hasCommand_impl(code);
    }

    public synchronized Set<SessionCommand2> getCommands() {
        return this.mProvider.getCommands_impl();
    }

    public synchronized MediaSession2Provider.CommandGroupProvider getProvider() {
        return this.mProvider;
    }

    public synchronized Bundle toBundle() {
        return this.mProvider.toBundle_impl();
    }

    public static synchronized SessionCommandGroup2 fromBundle(Bundle commands) {
        return ApiLoader.getProvider().fromBundle_MediaSession2CommandGroup(commands);
    }
}
