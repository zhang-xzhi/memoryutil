package allen.memoryutil;

import java.lang.instrument.Instrumentation;
import java.lang.reflect.Field;

import sun.misc.Unsafe;

public class Agent {

    private static volatile Instrumentation instrumentation;
    private static volatile Unsafe          unsafe;

    public static void premain(String args, Instrumentation instr) {
        instrumentation = instr;

        Field field = null;
        try {
            field = Unsafe.class.getDeclaredField("theUnsafe");
            field.setAccessible(true);
            unsafe = (Unsafe) field.get(null);
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    public static Instrumentation getInstrumentation() {
        Instrumentation instr = instrumentation;
        if (instr == null)
            throw new IllegalStateException("Agent not initted");
        return instr;
    }

    public static long objectFieldOffset(Field field) {
        return unsafe.objectFieldOffset(field);
    }
}