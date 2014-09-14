package allen.memoryutil.size;

import allen.memoryutil.MemoryUtil;
import allen.memoryutil.ObjectHeaderType;

public class ObjectHeaderSize {

    public static class A {
        int a;
    }

    private static long arrayHeaderSize  = 0;
    private static long normalHeaderSize = 0;

    static {

        {
            long size_a = MemoryUtil.getShallowSize(new int[0]);
            long size_b = MemoryUtil.getShallowSize(new int[1]);
            if (size_a < size_b) {
                arrayHeaderSize = size_a;
            } else {
                arrayHeaderSize = size_a - PrimitiveSize.SizeOfInt;
            }
        }

        {
            long size_a = MemoryUtil.getShallowSize(new Object());
            long size_b = MemoryUtil.getShallowSize(new A());
            if (size_a < size_b) {
                normalHeaderSize = size_a;
            } else {
                normalHeaderSize = size_a - PrimitiveSize.SizeOfInt;
            }
        }
    }

    public static long getSize(ObjectHeaderType objectHeaderType) {
        switch (objectHeaderType) {
            case ArrayHeader:
                return arrayHeaderSize;
            case NormalHeader:
                return normalHeaderSize;
            default:
                throw new RuntimeException("" + objectHeaderType);
        }
    }
}
