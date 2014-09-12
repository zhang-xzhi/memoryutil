package allen.memoryutil.size;

import allen.memoryutil.MemoryUtil;

public class ReferenceSize {

    private static long referenceSize = 0;

    static {
        referenceSize = MemoryUtil.memoryUsageOf(new Object[1024]) / 1024;
    }

    public static long getReferenceSize() {
        return referenceSize;
    }
}
