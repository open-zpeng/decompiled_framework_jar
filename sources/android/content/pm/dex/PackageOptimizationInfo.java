package android.content.pm.dex;
/* loaded from: classes.dex */
public class PackageOptimizationInfo {
    private final int mCompilationFilter;
    private final int mCompilationReason;

    public synchronized PackageOptimizationInfo(int compilerFilter, int compilationReason) {
        this.mCompilationReason = compilationReason;
        this.mCompilationFilter = compilerFilter;
    }

    public synchronized int getCompilationReason() {
        return this.mCompilationReason;
    }

    public synchronized int getCompilationFilter() {
        return this.mCompilationFilter;
    }

    public static synchronized PackageOptimizationInfo createWithNoInfo() {
        return new PackageOptimizationInfo(-1, -1);
    }
}
