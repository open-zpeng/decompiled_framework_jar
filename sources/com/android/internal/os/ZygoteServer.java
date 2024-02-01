package com.android.internal.os;

import android.net.LocalServerSocket;
import android.net.LocalSocket;
import android.system.ErrnoException;
import android.system.Os;
import android.system.OsConstants;
import android.system.StructPollfd;
import android.util.Log;
import android.util.Slog;
import java.io.FileDescriptor;
import java.io.IOException;
import java.util.ArrayList;
/* JADX INFO: Access modifiers changed from: package-private */
/* loaded from: classes3.dex */
public class ZygoteServer {
    private static final String ANDROID_SOCKET_PREFIX = "ANDROID_SOCKET_";
    public static final String TAG = "ZygoteServer";
    private boolean mCloseSocketFd;
    private boolean mIsForkChild;
    private LocalServerSocket mServerSocket;

    /* JADX INFO: Access modifiers changed from: package-private */
    public void setForkChild() {
        this.mIsForkChild = true;
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public void registerServerSocketFromEnv(String socketName) {
        if (this.mServerSocket == null) {
            String fullSocketName = ANDROID_SOCKET_PREFIX + socketName;
            try {
                String env = System.getenv(fullSocketName);
                int fileDesc = Integer.parseInt(env);
                try {
                    FileDescriptor fd = new FileDescriptor();
                    fd.setInt$(fileDesc);
                    this.mServerSocket = new LocalServerSocket(fd);
                    this.mCloseSocketFd = true;
                } catch (IOException ex) {
                    throw new RuntimeException("Error binding to local socket '" + fileDesc + "'", ex);
                }
            } catch (RuntimeException ex2) {
                throw new RuntimeException(fullSocketName + " unset or invalid", ex2);
            }
        }
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public void registerServerSocketAtAbstractName(String socketName) {
        if (this.mServerSocket == null) {
            try {
                this.mServerSocket = new LocalServerSocket(socketName);
                this.mCloseSocketFd = false;
            } catch (IOException ex) {
                throw new RuntimeException("Error binding to abstract socket '" + socketName + "'", ex);
            }
        }
    }

    private ZygoteConnection acceptCommandPeer(String abiList) {
        try {
            return createNewConnection(this.mServerSocket.accept(), abiList);
        } catch (IOException ex) {
            throw new RuntimeException("IOException during accept()", ex);
        }
    }

    protected ZygoteConnection createNewConnection(LocalSocket socket, String abiList) throws IOException {
        return new ZygoteConnection(socket, abiList);
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public void closeServerSocket() {
        try {
            if (this.mServerSocket != null) {
                FileDescriptor fd = this.mServerSocket.getFileDescriptor();
                this.mServerSocket.close();
                if (fd != null && this.mCloseSocketFd) {
                    Os.close(fd);
                }
            }
        } catch (ErrnoException ex) {
            Log.e(TAG, "Zygote:  error closing descriptor", ex);
        } catch (IOException ex2) {
            Log.e(TAG, "Zygote:  error closing sockets", ex2);
        }
        this.mServerSocket = null;
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public FileDescriptor getServerSocketFileDescriptor() {
        return this.mServerSocket.getFileDescriptor();
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public Runnable runSelectLoop(String abiList) {
        ZygoteConnection connection;
        Runnable command;
        ArrayList<FileDescriptor> fds = new ArrayList<>();
        ArrayList<ZygoteConnection> peers = new ArrayList<>();
        fds.add(this.mServerSocket.getFileDescriptor());
        peers.add(null);
        while (true) {
            StructPollfd[] pollFds = new StructPollfd[fds.size()];
            for (int i = 0; i < pollFds.length; i++) {
                pollFds[i] = new StructPollfd();
                pollFds[i].fd = fds.get(i);
                pollFds[i].events = (short) OsConstants.POLLIN;
            }
            try {
                Os.poll(pollFds, -1);
                for (int i2 = pollFds.length - 1; i2 >= 0; i2--) {
                    if ((pollFds[i2].revents & OsConstants.POLLIN) != 0) {
                        if (i2 == 0) {
                            ZygoteConnection newPeer = acceptCommandPeer(abiList);
                            peers.add(newPeer);
                            fds.add(newPeer.getFileDesciptor());
                        } else {
                            try {
                                try {
                                    connection = peers.get(i2);
                                    command = connection.processOneCommand(this);
                                } catch (Exception e) {
                                    if (this.mIsForkChild) {
                                        Log.e(TAG, "Caught post-fork exception in child process.", e);
                                        throw e;
                                    }
                                    Slog.e(TAG, "Exception executing zygote command: ", e);
                                    ZygoteConnection conn = peers.remove(i2);
                                    conn.closeSocket();
                                    fds.remove(i2);
                                }
                                if (this.mIsForkChild) {
                                    if (command != null) {
                                        return command;
                                    }
                                    throw new IllegalStateException("command == null");
                                } else if (command != null) {
                                    throw new IllegalStateException("command != null");
                                } else {
                                    if (connection.isClosedByPeer()) {
                                        connection.closeSocket();
                                        peers.remove(i2);
                                        fds.remove(i2);
                                    }
                                    this.mIsForkChild = false;
                                }
                            } finally {
                                this.mIsForkChild = false;
                            }
                        }
                    }
                }
            } catch (ErrnoException ex) {
                throw new RuntimeException("poll failed", ex);
            }
        }
    }
}
