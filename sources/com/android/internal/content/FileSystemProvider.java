package com.android.internal.content;

import android.content.ContentResolver;
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
import android.system.Int64Ref;
import android.text.TextUtils;
import android.util.ArrayMap;
import android.util.Log;
import android.webkit.MimeTypeMap;
import com.android.internal.annotations.GuardedBy;
import com.android.internal.util.ArrayUtils;
import com.android.internal.widget.MessagingMessage;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.nio.file.FileSystems;
import java.nio.file.FileVisitResult;
import java.nio.file.FileVisitor;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.attribute.BasicFileAttributes;
import java.util.Arrays;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;
import java.util.Set;
import java.util.concurrent.CopyOnWriteArrayList;
import libcore.io.IoUtils;

/* loaded from: classes3.dex */
public abstract class FileSystemProvider extends DocumentsProvider {
    private static final boolean LOG_INOTIFY = false;
    protected static final String SUPPORTED_QUERY_ARGS = joinNewline(DocumentsContract.QUERY_ARG_DISPLAY_NAME, DocumentsContract.QUERY_ARG_FILE_SIZE_OVER, DocumentsContract.QUERY_ARG_LAST_MODIFIED_AFTER, DocumentsContract.QUERY_ARG_MIME_TYPES);
    private static final String TAG = "FileSystemProvider";
    private String[] mDefaultProjection;
    private Handler mHandler;
    @GuardedBy({"mObservers"})
    private final ArrayMap<File, DirectoryObserver> mObservers = new ArrayMap<>();

    protected abstract Uri buildNotificationUri(String str);

    protected abstract String getDocIdForFile(File file) throws FileNotFoundException;

    protected abstract File getFileForDocId(String str, boolean z) throws FileNotFoundException;

    private static String joinNewline(String... args) {
        return TextUtils.join("\n", args);
    }

    protected void onDocIdChanged(String docId) {
    }

    protected void onDocIdDeleted(String docId) {
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
        }
        String mimeType = getDocumentType(documentId);
        if (DocumentsContract.Document.MIME_TYPE_DIR.equals(mimeType)) {
            final Int64Ref treeCount = new Int64Ref(0L);
            final Int64Ref treeSize = new Int64Ref(0L);
            try {
                Path path = FileSystems.getDefault().getPath(file.getAbsolutePath(), new String[0]);
                Files.walkFileTree(path, new FileVisitor<Path>() { // from class: com.android.internal.content.FileSystemProvider.1
                    @Override // java.nio.file.FileVisitor
                    public FileVisitResult preVisitDirectory(Path dir, BasicFileAttributes attrs) {
                        return FileVisitResult.CONTINUE;
                    }

                    @Override // java.nio.file.FileVisitor
                    public FileVisitResult visitFile(Path file2, BasicFileAttributes attrs) {
                        treeCount.value++;
                        treeSize.value += attrs.size();
                        return FileVisitResult.CONTINUE;
                    }

                    @Override // java.nio.file.FileVisitor
                    public FileVisitResult visitFileFailed(Path file2, IOException exc) {
                        return FileVisitResult.CONTINUE;
                    }

                    @Override // java.nio.file.FileVisitor
                    public FileVisitResult postVisitDirectory(Path dir, IOException exc) {
                        return FileVisitResult.CONTINUE;
                    }
                });
                Bundle res = new Bundle();
                res.putLong(DocumentsContract.METADATA_TREE_COUNT, treeCount.value);
                res.putLong(DocumentsContract.METADATA_TREE_SIZE, treeSize.value);
                return res;
            } catch (IOException e) {
                Log.e(TAG, "An error occurred retrieving the metadata", e);
                return null;
            }
        } else if (!file.isFile()) {
            Log.w(TAG, "Can't stream non-regular file. Returning empty metadata.");
            return null;
        } else if (!file.canRead()) {
            Log.w(TAG, "Can't stream non-readable file. Returning empty metadata.");
            return null;
        } else if (!MetadataReader.isSupportedMimeType(mimeType)) {
            Log.w(TAG, "Unsupported type " + mimeType + ". Returning empty metadata.");
            return null;
        } else {
            InputStream stream = null;
            try {
                Bundle metadata = new Bundle();
                stream = new FileInputStream(file.getAbsolutePath());
                MetadataReader.getMetadata(metadata, stream, mimeType, null);
                return metadata;
            } catch (IOException e2) {
                Log.e(TAG, "An error occurred retrieving the metadata", e2);
                return null;
            } finally {
                IoUtils.closeQuietly(stream);
            }
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
        String childId;
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
            childId = getDocIdForFile(file);
            onDocIdChanged(childId);
        } else {
            try {
                if (!file.createNewFile()) {
                    throw new IllegalStateException("Failed to touch " + file);
                }
                String childId2 = getDocIdForFile(file);
                onDocIdChanged(childId2);
                childId = childId2;
            } catch (IOException e) {
                throw new IllegalStateException("Failed to touch " + file + ": " + e);
            }
        }
        MediaStore.scanFile(getContext(), file);
        return childId;
    }

    @Override // android.provider.DocumentsProvider
    public String renameDocument(String docId, String displayName) throws FileNotFoundException {
        String displayName2 = FileUtils.buildValidFatFilename(displayName);
        File before = getFileForDocId(docId);
        File beforeVisibleFile = getFileForDocId(docId, true);
        File after = FileUtils.buildUniqueFile(before.getParentFile(), displayName2);
        if (!before.renameTo(after)) {
            throw new IllegalStateException("Failed to rename to " + after);
        }
        String afterDocId = getDocIdForFile(after);
        onDocIdChanged(docId);
        onDocIdDeleted(docId);
        onDocIdChanged(afterDocId);
        File afterVisibleFile = getFileForDocId(afterDocId, true);
        moveInMediaStore(beforeVisibleFile, afterVisibleFile);
        if (!TextUtils.equals(docId, afterDocId)) {
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
            onDocIdDeleted(sourceDocumentId);
            onDocIdChanged(docId);
            moveInMediaStore(visibleFileBefore, getFileForDocId(docId, true));
            return docId;
        }
    }

    private void moveInMediaStore(File oldVisibleFile, File newVisibleFile) {
        if (oldVisibleFile != null) {
            MediaStore.scanFile(getContext(), oldVisibleFile);
        }
        if (newVisibleFile != null) {
            MediaStore.scanFile(getContext(), newVisibleFile);
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
        onDocIdDeleted(docId);
        removeFromMediaStore(visibleFile, isDirectory);
    }

    private void removeFromMediaStore(File visibleFile, boolean isFolder) throws FileNotFoundException {
        if (visibleFile != null) {
            long token = Binder.clearCallingIdentity();
            try {
                ContentResolver resolver = getContext().getContentResolver();
                Uri externalUri = MediaStore.Files.getContentUri(MediaStore.VOLUME_EXTERNAL);
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
        File[] listFilesOrEmpty;
        File parent = getFileForDocId(parentDocumentId);
        MatrixCursor result = new DirectoryCursor(resolveProjection(projection), parentDocumentId, parent);
        if (parent.isDirectory()) {
            for (File file : FileUtils.listFilesOrEmpty(parent)) {
                includeFile(result, null, file);
            }
        } else {
            Log.w(TAG, "parentDocumentId '" + parentDocumentId + "' is not Directory");
        }
        return result;
    }

    protected final Cursor querySearchDocuments(File folder, String[] projection, Set<String> exclusion, Bundle queryArgs) throws FileNotFoundException {
        File[] listFiles;
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
            if (!exclusion.contains(file.getAbsolutePath()) && matchSearchQueryArguments(file, queryArgs)) {
                includeFile(result, null, file);
            }
        }
        String[] handledQueryArgs = DocumentsContract.getHandledQueryArguments(queryArgs);
        if (handledQueryArgs.length > 0) {
            Bundle extras = new Bundle();
            extras.putStringArray(ContentResolver.EXTRA_HONORED_ARGS, handledQueryArgs);
            result.setExtras(extras);
        }
        return result;
    }

    @Override // android.provider.DocumentsProvider
    public String getDocumentType(String documentId) throws FileNotFoundException {
        return getDocumentType(documentId, getFileForDocId(documentId));
    }

    private String getDocumentType(String documentId, File file) throws FileNotFoundException {
        if (file.isDirectory()) {
            return DocumentsContract.Document.MIME_TYPE_DIR;
        }
        int lastDot = documentId.lastIndexOf(46);
        if (lastDot >= 0) {
            String extension = documentId.substring(lastDot + 1).toLowerCase();
            String mime = MimeTypeMap.getSingleton().getMimeTypeFromExtension(extension);
            if (mime != null) {
                return mime;
            }
            return ContentResolver.MIME_TYPE_DEFAULT;
        }
        return ContentResolver.MIME_TYPE_DEFAULT;
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
                    FileSystemProvider.this.lambda$openDocument$0$FileSystemProvider(documentId, visibleFile, iOException);
                }
            });
        } catch (IOException e) {
            throw new FileNotFoundException("Failed to open for writing: " + e);
        }
    }

    public /* synthetic */ void lambda$openDocument$0$FileSystemProvider(String documentId, File visibleFile, IOException e) {
        onDocIdChanged(documentId);
        scanFile(visibleFile);
    }

    private boolean matchSearchQueryArguments(File file, Bundle queryArgs) {
        String extension;
        if (file == null) {
            return false;
        }
        String fileName = file.getName();
        if (file.isDirectory()) {
            extension = DocumentsContract.Document.MIME_TYPE_DIR;
        } else {
            int dotPos = fileName.lastIndexOf(46);
            if (dotPos < 0) {
                return false;
            }
            String extension2 = fileName.substring(dotPos + 1);
            extension = MimeTypeMap.getSingleton().getMimeTypeFromExtension(extension2);
        }
        return DocumentsContract.matchSearchQueryArguments(queryArgs, fileName, extension, file.lastModified(), file.length());
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
        String[] columns = result.getColumnNames();
        MatrixCursor.RowBuilder row = result.newRow();
        if (docId == null) {
            docId = getDocIdForFile(file);
        } else {
            file = getFileForDocId(docId);
        }
        String mimeType = getDocumentType(docId, file);
        row.add("document_id", docId);
        row.add("mime_type", mimeType);
        int flagIndex = ArrayUtils.indexOf(columns, "flags");
        if (flagIndex != -1) {
            int flags = 0;
            if (file.canWrite()) {
                if (mimeType.equals(DocumentsContract.Document.MIME_TYPE_DIR)) {
                    int flags2 = 0 | 8;
                    flags = flags2 | 4 | 64 | 256;
                } else {
                    int flags3 = 0 | 2;
                    flags = flags3 | 4 | 64 | 256;
                }
            }
            if (mimeType.startsWith(MessagingMessage.IMAGE_MIME_TYPE_PREFIX)) {
                flags |= 1;
            }
            if (typeSupportsMetadata(mimeType)) {
                flags |= 16384;
            }
            row.add(flagIndex, Integer.valueOf(flags));
        }
        int displayNameIndex = ArrayUtils.indexOf(columns, "_display_name");
        if (displayNameIndex != -1) {
            row.add(displayNameIndex, file.getName());
        }
        int lastModifiedIndex = ArrayUtils.indexOf(columns, "last_modified");
        if (lastModifiedIndex != -1) {
            long lastModified = file.lastModified();
            if (lastModified > 31536000000L) {
                row.add(lastModifiedIndex, Long.valueOf(lastModified));
            }
        }
        int sizeIndex = ArrayUtils.indexOf(columns, "_size");
        if (sizeIndex != -1) {
            row.add(sizeIndex, Long.valueOf(file.length()));
        }
        return row;
    }

    protected boolean typeSupportsMetadata(String mimeType) {
        return MetadataReader.isSupportedMimeType(mimeType) || DocumentsContract.Document.MIME_TYPE_DIR.equals(mimeType);
    }

    protected final File getFileForDocId(String docId) throws FileNotFoundException {
        return getFileForDocId(docId, false);
    }

    private String[] resolveProjection(String[] projection) {
        return projection == null ? this.mDefaultProjection : projection;
    }

    /* JADX INFO: Access modifiers changed from: private */
    public void startObserving(File file, Uri notifyUri, DirectoryCursor cursor) {
        synchronized (this.mObservers) {
            DirectoryObserver observer = this.mObservers.get(file);
            if (observer == null) {
                observer = new DirectoryObserver(file, getContext().getContentResolver(), notifyUri);
                observer.startWatching();
                this.mObservers.put(file, observer);
            }
            observer.mCursors.add(cursor);
        }
    }

    /* JADX INFO: Access modifiers changed from: private */
    public void stopObserving(File file, DirectoryCursor cursor) {
        synchronized (this.mObservers) {
            DirectoryObserver observer = this.mObservers.get(file);
            if (observer == null) {
                return;
            }
            observer.mCursors.remove(cursor);
            if (observer.mCursors.size() == 0) {
                this.mObservers.remove(file);
                observer.stopWatching();
            }
        }
    }

    /* JADX INFO: Access modifiers changed from: private */
    /* loaded from: classes3.dex */
    public static class DirectoryObserver extends FileObserver {
        private static final int NOTIFY_EVENTS = 4044;
        private final CopyOnWriteArrayList<DirectoryCursor> mCursors;
        private final File mFile;
        private final Uri mNotifyUri;
        private final ContentResolver mResolver;

        DirectoryObserver(File file, ContentResolver resolver, Uri notifyUri) {
            super(file.getAbsolutePath(), (int) NOTIFY_EVENTS);
            this.mFile = file;
            this.mResolver = resolver;
            this.mNotifyUri = notifyUri;
            this.mCursors = new CopyOnWriteArrayList<>();
        }

        @Override // android.os.FileObserver
        public void onEvent(int event, String path) {
            if ((event & NOTIFY_EVENTS) != 0) {
                Iterator<DirectoryCursor> it = this.mCursors.iterator();
                while (it.hasNext()) {
                    DirectoryCursor cursor = it.next();
                    cursor.notifyChanged();
                }
                this.mResolver.notifyChange(this.mNotifyUri, (ContentObserver) null, false);
            }
        }

        public String toString() {
            String filePath = this.mFile.getAbsolutePath();
            return "DirectoryObserver{file=" + filePath + ", ref=" + this.mCursors.size() + "}";
        }
    }

    /* loaded from: classes3.dex */
    private class DirectoryCursor extends MatrixCursor {
        private final File mFile;

        public DirectoryCursor(String[] columnNames, String docId, File file) {
            super(columnNames);
            Uri notifyUri = FileSystemProvider.this.buildNotificationUri(docId);
            setNotificationUris(FileSystemProvider.this.getContext().getContentResolver(), Arrays.asList(notifyUri), FileSystemProvider.this.getContext().getContentResolver().getUserId(), false);
            this.mFile = file;
            FileSystemProvider.this.startObserving(this.mFile, notifyUri, this);
        }

        public void notifyChanged() {
            onChange(false);
        }

        @Override // android.database.AbstractCursor, android.database.Cursor, java.io.Closeable, java.lang.AutoCloseable
        public void close() {
            super.close();
            FileSystemProvider.this.stopObserving(this.mFile, this);
        }
    }
}
