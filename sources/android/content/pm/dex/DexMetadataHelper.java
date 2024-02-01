package android.content.pm.dex;

import android.content.pm.PackageManager;
import android.content.pm.PackageParser;
import android.util.ArrayMap;
import android.util.jar.StrictJarFile;
import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.LinkOption;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.Map;

/* loaded from: classes.dex */
public class DexMetadataHelper {
    private static final String DEX_METADATA_FILE_EXTENSION = ".dm";

    private DexMetadataHelper() {
    }

    public static boolean isDexMetadataFile(File file) {
        return isDexMetadataPath(file.getName());
    }

    private static boolean isDexMetadataPath(String path) {
        return path.endsWith(DEX_METADATA_FILE_EXTENSION);
    }

    public static long getPackageDexMetadataSize(PackageParser.PackageLite pkg) {
        long sizeBytes = 0;
        Collection<String> dexMetadataList = getPackageDexMetadata(pkg).values();
        for (String dexMetadata : dexMetadataList) {
            sizeBytes += new File(dexMetadata).length();
        }
        return sizeBytes;
    }

    public static File findDexMetadataForFile(File targetFile) {
        String dexMetadataPath = buildDexMetadataPathForFile(targetFile);
        File dexMetadataFile = new File(dexMetadataPath);
        if (dexMetadataFile.exists()) {
            return dexMetadataFile;
        }
        return null;
    }

    public static Map<String, String> getPackageDexMetadata(PackageParser.Package pkg) {
        return buildPackageApkToDexMetadataMap(pkg.getAllCodePaths());
    }

    private static Map<String, String> getPackageDexMetadata(PackageParser.PackageLite pkg) {
        return buildPackageApkToDexMetadataMap(pkg.getAllCodePaths());
    }

    private static Map<String, String> buildPackageApkToDexMetadataMap(List<String> codePaths) {
        ArrayMap<String, String> result = new ArrayMap<>();
        for (int i = codePaths.size() - 1; i >= 0; i--) {
            String codePath = codePaths.get(i);
            String dexMetadataPath = buildDexMetadataPathForFile(new File(codePath));
            if (Files.exists(Paths.get(dexMetadataPath, new String[0]), new LinkOption[0])) {
                result.put(codePath, dexMetadataPath);
            }
        }
        return result;
    }

    public static String buildDexMetadataPathForApk(String codePath) {
        if (!PackageParser.isApkPath(codePath)) {
            throw new IllegalStateException("Corrupted package. Code path is not an apk " + codePath);
        }
        return codePath.substring(0, codePath.length() - PackageParser.APK_FILE_EXTENSION.length()) + DEX_METADATA_FILE_EXTENSION;
    }

    private static String buildDexMetadataPathForFile(File targetFile) {
        if (PackageParser.isApkFile(targetFile)) {
            return buildDexMetadataPathForApk(targetFile.getPath());
        }
        return targetFile.getPath() + DEX_METADATA_FILE_EXTENSION;
    }

    public static void validatePackageDexMetadata(PackageParser.Package pkg) throws PackageParser.PackageParserException {
        Collection<String> apkToDexMetadataList = getPackageDexMetadata(pkg).values();
        for (String dexMetadata : apkToDexMetadataList) {
            validateDexMetadataFile(dexMetadata);
        }
    }

    private static void validateDexMetadataFile(String dmaPath) throws PackageParser.PackageParserException {
        StrictJarFile jarFile = null;
        try {
            try {
                StrictJarFile jarFile2 = new StrictJarFile(dmaPath, false, false);
                try {
                    jarFile2.close();
                } catch (IOException e) {
                }
            } catch (IOException e2) {
                throw new PackageParser.PackageParserException(PackageManager.INSTALL_FAILED_BAD_DEX_METADATA, "Error opening " + dmaPath, e2);
            }
        } catch (Throwable th) {
            if (0 != 0) {
                try {
                    jarFile.close();
                } catch (IOException e3) {
                }
            }
            throw th;
        }
    }

    public static void validateDexPaths(String[] paths) {
        ArrayList<String> apks = new ArrayList<>();
        for (int i = 0; i < paths.length; i++) {
            if (PackageParser.isApkPath(paths[i])) {
                apks.add(paths[i]);
            }
        }
        ArrayList<String> unmatchedDmFiles = new ArrayList<>();
        for (String dmPath : paths) {
            if (isDexMetadataPath(dmPath)) {
                boolean valid = false;
                int j = apks.size() - 1;
                while (true) {
                    if (j >= 0) {
                        if (dmPath.equals(buildDexMetadataPathForFile(new File(apks.get(j))))) {
                            valid = true;
                            break;
                        } else {
                            j--;
                        }
                    } else {
                        break;
                    }
                }
                if (!valid) {
                    unmatchedDmFiles.add(dmPath);
                }
            }
        }
        if (!unmatchedDmFiles.isEmpty()) {
            throw new IllegalStateException("Unmatched .dm files: " + unmatchedDmFiles);
        }
    }
}
