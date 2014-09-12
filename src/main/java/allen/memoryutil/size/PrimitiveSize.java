package allen.memoryutil.size;

import java.util.LinkedHashMap;

import java.util.Map;

public class PrimitiveSize {

    public static Map<Class<?>, Long> primitives = new LinkedHashMap<Class<?>, Long>();
    public final static long          SizeOfInt  = 4L;
    static {
        primitives.put(byte.class, 1L);
        primitives.put(boolean.class, 1L);
        primitives.put(char.class, 2L);
        primitives.put(short.class, 2L);
        primitives.put(int.class, 4L);
        primitives.put(float.class, 4L);
        primitives.put(double.class, 8L);
        primitives.put(long.class, 8L);
    }

    public static long getSize(Class<?> cls) {

        Long result = primitives.get(cls);

        if (result == null) {
            throw new RuntimeException("" + cls);
        }

        return result;
    }
}
