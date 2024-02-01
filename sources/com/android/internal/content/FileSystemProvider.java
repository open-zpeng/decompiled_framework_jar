package com.android.internal.content;

import android.content.ContentResolver;
import android.content.ContentValues;
import android.content.Intent;
import android.content.res.AssetFileDescriptor;
import android.database.ContentObserver;
import android.database.Cursor;
import android.database.MatrixCursor;
import android.graphics.Point;
import android.net.Uri;
import android.os.Binder;
import android.os.Bundle;
import android.os.CancellationSignal;
import android.os.FileObserver;
import android.os.FileUtils;
import android.os.Handler;
import android.os.ParcelFileDescriptor;
import android.provider.DocumentsContract;
import android.provider.DocumentsProvider;
import android.provider.MediaStore;
import android.provider.MetadataReader;
import android.text.TextUtils;
import android.util.ArrayMap;
import android.util.Log;
import android.webkit.MimeTypeMap;
import com.android.internal.annotations.GuardedBy;
import com.android.internal.widget.MessagingMessage;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.util.LinkedList;
import java.util.List;
import java.util.Set;
import libcore.io.IoUtils;
/* loaded from: classes3.dex */
public abstract class FileSystemProvider extends DocumentsProvider {
    static final /* synthetic */ boolean $assertionsDisabled = false;
    private static final boolean LOG_INOTIFY = false;
    private static final String MIMETYPE_JPEG = "image/jpeg";
    private static final String MIMETYPE_JPG = "image/jpg";
    private static final String MIMETYPE_OCTET_STREAM = "application/octet-stream";
    private static final String TAG = "FileSystemProvider";
    private String[] mDefaultProjection;
    private Handler mHandler;
    @GuardedBy("mObservers")
    private final ArrayMap<File, DirectoryObserver> mObservers = new ArrayMap<>();

    protected abstract Uri buildNotificationUri(String str);

    protected abstract String getDocIdForFile(File file) throws FileNotFoundException;

    protected abstract File getFileForDocId(String str, boolean z) throws FileNotFoundException;

    protected void onDocIdChanged(String docId) {
    }

    @Override // android.content.ContentProvider
    public boolean onCreate() {
        throw new UnsupportedOperationException("Subclass should override this and call onCreate(defaultDocumentProjection)");
    }

    protected void onCreate(String[] defaultProjection) {
        this.mHandler = new Handler();
        this.mDefaultProjection = defaultProjection;
    }

    @Override // android.provider.DocumentsProvider
    public boolean isChildDocument(String parentDocId, String docId) {
        try {
            File parent = getFileForDocId(parentDocId).getCanonicalFile();
            File doc = getFileForDocId(docId).getCanonicalFile();
            return FileUtils.contains(parent, doc);
        } catch (IOException e) {
            throw new IllegalArgumentException("Failed to determine if " + docId + " is child of " + parentDocId + ": " + e);
        }
    }

    @Override // android.provider.DocumentsProvider
    public Bundle getDocumentMetadata(String documentId) throws FileNotFoundException {
        File file = getFileForDocId(documentId);
        if (!file.exists()) {
            throw new FileNotFoundException("Can't find the file for documentId: " + documentId);
        } else if (!file.isFile()) {
            Log.w(TAG, "Can't stream non-regular file. Returning empty metadata.");
            return null;
        } else if (!file.canRead()) {
            Log.w(TAG, "Can't stream non-readable file. Returning empty metadata.");
            return null;
        } else {
            String mimeType = getTypeForFile(file);
            if (MetadataReader.isSupportedMimeType(mimeType)) {
                InputStream stream = null;
                try {
                    Bundle metadata = new Bundle();
                    stream = new FileInputStream(file.getAbsolutePath());
                    MetadataReader.getMetadata(metadata, stream, mimeType, null);
                    return metadata;
                } catch (IOException e) {
                    Log.e(TAG, "An error occurred retrieving the metadata", e);
                    return null;
                } finally {
                    IoUtils.closeQuietly(stream);
                }
            }
            return null;
        }
    }

    protected final List<String> findDocumentPath(File parent, File doc) throws FileNotFoundException {
        if (!doc.exists()) {
            throw new FileNotFoundException(doc + " is not found.");
        } else if (!FileUtils.contains(parent, doc)) {
            throw new FileNotFoundException(doc + " is not found under " + parent);
        } else {
            LinkedList<String> path = new LinkedList<>();
            while (doc != null && FileUtils.contains(parent, doc)) {
                path.addFirst(getDocIdForFile(doc));
                doc = doc.getParentFile();
            }
            return path;
        }
    }

    @Override // android.provider.DocumentsProvider
    public String createDocument(String docId, String mimeType, String displayName) throws FileNotFoundException {
        String displayName2 = FileUtils.buildValidFatFilename(displayName);
        File parent = getFileForDocId(docId);
        if (!parent.isDirectory()) {
            throw new IllegalArgumentException("Parent document isn't a directory");
        }
        File file = FileUtils.buildUniqueFile(parent, mimeType, displayName2);
        if (DocumentsContract.Document.MIME_TYPE_DIR.equals(mimeType)) {
            if (!file.mkdir()) {
                throw new IllegalStateException("Failed to mkdir " + file);
            }
            String childId = getDocIdForFile(file);
            onDocIdChanged(childId);
            addFolderToMediaStore(getFileForDocId(childId, true));
            return childId;
        }
        try {
            if (!file.createNewFile()) {
                throw new IllegalStateException("Failed to touch " + file);
            }
            String childId2 = getDocIdForFile(file);
            onDocIdChanged(childId2);
            return childId2;
        } catch (IOException e) {
            throw new IllegalStateException("Failed to touch " + file + ": " + e);
        }
    }

    private void addFolderToMediaStore(File visibleFolder) {
        if (visibleFolder != null) {
            long token = Binder.clearCallingIdentity();
            try {
                ContentResolver resolver = getContext().getContentResolver();
                Uri uri = MediaStore.Files.getDirectoryUri("external");
                ContentValues values = new ContentValues();
                values.put("_data", visibleFolder.getAbsolutePath());
                resolver.insert(uri, values);
            } finally {
                Binder.restoreCallingIdentity(token);
            }
        }
    }

    @Override // android.provider.DocumentsProvider
    public String renameDocument(String docId, String displayName) throws FileNotFoundException {
        String displayName2 = FileUtils.buildValidFatFilename(displayName);
        File before = getFileForDocId(docId);
        File after = FileUtils.buildUniqueFile(before.getParentFile(), displayName2);
        if (!before.renameTo(after)) {
            throw new IllegalStateException("Failed to rename to " + after);
        }
        String afterDocId = getDocIdForFile(after);
        onDocIdChanged(docId);
        onDocIdChanged(afterDocId);
        File beforeVisibleFile = getFileForDocId(docId, true);
        File afterVisibleFile = getFileForDocId(afterDocId, true);
        moveInMediaStore(beforeVisibleFile, afterVisibleFile);
        if (!TextUtils.equals(docId, afterDocId)) {
            scanFile(afterVisibleFile);
            return afterDocId;
        }
        return null;
    }

    @Override // android.provider.DocumentsProvider
    public String moveDocument(String sourceDocumentId, String sourceParentDocumentId, String targetParentDocumentId) throws FileNotFoundException {
        File before = getFileForDocId(sourceDocumentId);
        File after = new File(getFileForDocId(targetParentDocumentId), before.getName());
        File visibleFileBefore = getFileForDocId(sourceDocumentId, true);
        if (after.exists()) {
            throw new IllegalStateException("Already exists " + after);
        } else if (!before.renameTo(after)) {
            throw new IllegalStateException("Failed to move to " + after);
        } else {
            String docId = getDocIdForFile(after);
            onDocIdChanged(sourceDocumentId);
            onDocIdChanged(docId);
            moveInMediaStore(visibleFileBefore, getFileForDocId(docId, true));
            return docId;
        }
    }

    private void moveInMediaStore(File oldVisibleFile, File newVisibleFile) {
        Uri externalUri;
        if (oldVisibleFile != null && newVisibleFile != null) {
            long token = Binder.clearCallingIdentity();
            try {
                ContentResolver resolver = getContext().getContentResolver();
                if (newVisibleFile.isDirectory()) {
                    externalUri = MediaStore.Files.getDirectoryUri("external");
                } else {
                    externalUri = MediaStore.Files.getContentUri("external");
                }
                ContentValues values = new ContentValues();
                values.put("_data", newVisibleFile.getAbsolutePath());
                String path = oldVisibleFile.getAbsolutePath();
                resolver.update(externalUri, values, "_data LIKE ? AND lower(_data)=lower(?)", new String[]{path, path});
            } finally {
                Binder.restoreCallingIdentity(token);
            }
        }
    }

    @Override // android.provider.DocumentsProvider
    public void deleteDocument(String docId) throws FileNotFoundException {
        File file = getFileForDocId(docId);
        File visibleFile = getFileForDocId(docId, true);
        boolean isDirectory = file.isDirectory();
        if (isDirectory) {
            FileUtils.deleteContents(file);
        }
        if (!file.delete()) {
            throw new IllegalStateException("Failed to delete " + file);
        }
        onDocIdChanged(docId);
        removeFromMediaStore(visibleFile, isDirectory);
    }

    private void removeFromMediaStore(File visibleFile, boolean isFolder) throws FileNotFoundException {
        if (visibleFile != null) {
            long token = Binder.clearCallingIdentity();
            try {
                ContentResolver resolver = getContext().getContentResolver();
                Uri externalUri = MediaStore.Files.getContentUri("external");
                if (isFolder) {
                    String path = visibleFile.getAbsolutePath() + "/";
                    resolver.delete(externalUri, "_data LIKE ?1 AND lower(substr(_data,1,?2))=lower(?3)", new String[]{path + "%", Integer.toString(path.length()), path});
                }
                String path2 = visibleFile.getAbsolutePath();
                resolver.delete(externalUri, "_data LIKE ?1 AND lower(_data)=lower(?2)", new String[]{path2, path2});
            } finally {
                Binder.restoreCallingIdentity(token);
            }
        }
    }

    @Override // android.provider.DocumentsProvider
    public Cursor queryDocument(String documentId, String[] projection) throws FileNotFoundException {
        MatrixCursor result = new MatrixCursor(resolveProjection(projection));
        includeFile(result, documentId, null);
        return result;
    }

    @Override // android.provider.DocumentsProvider
    public Cursor queryChildDocuments(String parentDocumentId, String[] projection, String sortOrder) throws FileNotFoundException {
        File[] listFiles;
        File parent = getFileForDocId(parentDocumentId);
        MatrixCursor result = new DirectoryCursor(resolveProjection(projection), parentDocumentId, parent);
        for (File file : parent.listFiles()) {
            includeFile(result, null, file);
        }
        return result;
    }

    protected final Cursor querySearchDocuments(File folder, String query, String[] projection, Set<String> exclusion) throws FileNotFoundException {
        File[] listFiles;
        String query2 = query.toLowerCase();
        MatrixCursor result = new MatrixCursor(resolveProjection(projection));
        LinkedList<File> pending = new LinkedList<>();
        pending.add(folder);
        while (!pending.isEmpty() && result.getCount() < 24) {
            File file = pending.removeFirst();
            if (file.isDirectory()) {
                for (File child : file.listFiles()) {
                    pending.add(child);
                }
            }
            if (file.getName().toLowerCase().contains(query2) && !exclusion.contains(file.getAbsolutePath())) {
                includeFile(result, null, file);
            }
        }
        return result;
    }

    @Override // android.provider.DocumentsProvider
    public String getDocumentType(String documentId) throws FileNotFoundException {
        File file = getFileForDocId(documentId);
        return getTypeForFile(file);
    }

    @Override // android.provider.DocumentsProvider
    public ParcelFileDescriptor openDocument(final String documentId, String mode, CancellationSignal signal) throws FileNotFoundException {
        File file = getFileForDocId(documentId);
        final File visibleFile = getFileForDocId(documentId, true);
        int pfdMode = ParcelFileDescriptor.parseMode(mode);
        if (pfdMode == 268435456 || visibleFile == null) {
            return ParcelFileDescriptor.open(file, pfdMode);
        }
        try {
            return ParcelFileDescriptor.open(file, pfdMode, this.mHandler, new ParcelFileDescriptor.OnCloseListener() { // from class: com.android.internal.content.-$$Lambda$FileSystemProvider$y9rjeYFpkvVjwD2Whw-ujCM-C7Y
                @Override // android.os.ParcelFileDescriptor.OnCloseListener
                public final void onClose(IOException iOException) {
                    FileSystemProvider.lambda$openDocument$0(FileSystemProvider.this, documentId, visibleFile, iOException);
                }
            });
        } catch (IOException e) {
            throw new FileNotFoundException("Failed to open for writing: " + e);
        }
    }

    public static /* synthetic */ void lambda$openDocument$0(FileSystemProvider fileSystemProvider, String documentId, File visibleFile, IOException e) {
        fileSystemProvider.onDocIdChanged(documentId);
        fileSystemProvider.scanFile(visibleFile);
    }

    private void scanFile(File visibleFile) {
        Intent intent = new Intent(Intent.ACTION_MEDIA_SCANNER_SCAN_FILE);
        intent.setData(Uri.fromFile(visibleFile));
        getContext().sendBroadcast(intent);
    }

    @Override // android.provider.DocumentsProvider
    public AssetFileDescriptor openDocumentThumbnail(String documentId, Point sizeHint, CancellationSignal signal) throws FileNotFoundException {
        File file = getFileForDocId(documentId);
        return DocumentsContract.openImageThumbnail(file);
    }

    protected MatrixCursor.RowBuilder includeFile(MatrixCursor result, String docId, File file) throws FileNotFoundException {
        if (docId == null) {
            docId = getDocIdForFile(file);
        } else {
            file = getFileForDocId(docId);
        }
        int flags = 0;
        if (file.canWrite()) {
            if (file.isDirectory()) {
                int flags2 = 0 | 8;
                flags = flags2 | 4 | 64 | 256;
            } else {
                int flags3 = 0 | 2;
                flags = flags3 | 4 | 64 | 256;
            }
        }
        String mimeType = getTypeForFile(file);
        String displayName = file.getName();
        if (mimeType.startsWith(MessagingMessage.IMAGE_MIME_TYPE_PREFIX)) {
            flags |= 1;
        }
        if (typeSupportsMetadata(mimeType)) {
            flags |= 131072;
        }
        MatrixCursor.RowBuilder row = result.newRow();
        row.add("document_id", docId);
        row.add("_display_name", displayName);
        row.add("_size", Long.valueOf(file.length()));
        row.add("mime_type", mimeType);
        row.add("flags", Integer.valueOf(flags));
        long lastModified = file.lastModified();
        if (lastModified > 31536000000L) {
            row.add("last_modified", Long.valueOf(lastModified));
        }
        return row;
    }

    private static String getTypeForFile(File file) {
        if (file.isDirectory()) {
            return DocumentsContract.Document.MIME_TYPE_DIR;
        }
        return getTypeForName(file.getName());
    }

    protected boolean typeSupportsMetadata(String mimeType) {
        return MetadataReader.isSupportedMimeType(mimeType);
    }

    private static String getTypeForName(String name) {
        int lastDot = name.lastIndexOf(46);
        if (lastDot >= 0) {
            String extension = name.substring(lastDot + 1).toLowerCase();
            String mime = MimeTypeMap.getSingleton().getMimeTypeFromExtension(extension);
            if (mime != null) {
                return mime;
            }
            return MIMETYPE_OCTET_STREAM;
        }
        return MIMETYPE_OCTET_STREAM;
    }

    protected final File getFileForDocId(String docId) throws FileNotFoundException {
        return getFileForDocId(docId, false);
    }

    private String[] resolveProjection(String[] projection) {
        return projection == null ? this.mDefaultProjection : projection;
    }

    /* JADX INFO: Access modifiers changed from: private */
    public void startObserving(File file, Uri notifyUri) {
        synchronized (this.mObservers) {
            DirectoryObserver observer = this.mObservers.get(file);
            if (observer == null) {
                observer = new DirectoryObserver(file, getContext().getContentResolver(), notifyUri);
                observer.startWatching();
                this.mObservers.put(file, observer);
            }
            DirectoryObserver.access$008(observer);
        }
    }

    /* JADX INFO: Access modifiers changed from: private */
    public void stopObserving(File file) {
        synchronized (this.mObservers) {
            DirectoryObserver observer = this.mObservers.get(file);
            if (observer == null) {
                return;
            }
            DirectoryObserver.access$010(observer);
            if (observer.mRefCount == 0) {
                this.mObservers.remove(file);
                observer.stopWatching();
            }
        }
    }

    /* JADX INFO: Access modifiers changed from: private */
    /* loaded from: classes3.dex */
    public static class DirectoryObserver extends FileObserver {
        private static final int NOTIFY_EVENTS = 4044;
        private final File mFile;
        private final Uri mNotifyUri;
        private int mRefCount;
        private final ContentResolver mResolver;

        static /* synthetic */ int access$008(DirectoryObserver x0) {
            int i = x0.mRefCount;
            x0.mRefCount = i + 1;
            return i;
        }

        static /* synthetic */ int access$010(DirectoryObserver x0) {
            int i = x0.mRefCount;
            x0.mRefCount = i - 1;
            return i;
        }

        public DirectoryObserver(File file, ContentResolver resolver, Uri notifyUri) {
            super(file.getAbsolutePath(), NOTIFY_EVENTS);
            this.mRefCount = 0;
            this.mFile = file;
            this.mResolver = resolver;
            this.mNotifyUri = notifyUri;
        }

        @Override // android.os.FileObserver
        public void onEvent(int event, String path) {
            if ((event & NOTIFY_EVENTS) != 0) {
                this.mResolver.notifyChange(this.mNotifyUri, (ContentObserver) null, false);
            }
        }

        public String toString() {
            return "DirectoryObserver{file=" + this.mFile.getAbsolutePath() + ", ref=" + this.mRefCount + "}";
        }
    }

    /* loaded from: classes3.dex */
    private class DirectoryCursor extends MatrixCursor {
        private final File mFile;

        public DirectoryCursor(String[] columnNames, String docId, File file) {
            super(columnNames);
            Uri notifyUri = FileSystemProvider.this.buildNotificationUri(docId);
            setNotificationUri(FileSystemProvider.this.getContext().getContentResolver(), notifyUri);
            this.mFile = file;
            FileSystemProvider.this.startObserving(this.mFile, notifyUri);
        }

        @Override // android.database.AbstractCursor, android.database.Cursor, java.io.Closeable, java.lang.AutoCloseable
        public void close() {
            super.close();
            FileSystemProvider.this.stopObserving(this.mFile);
        }
    }
}
