package allen.memoryutil.other;

import java.lang.reflect.Field;

import org.junit.Test;

public class UnitTestForMemory {

    private static class A {
        boolean b;
        byte[]  ba;
    }

    @Test
    public void testForA() throws Exception {
        Field f = A.class.getDeclaredField("b");
        System.out.println(f.getType());

        f = A.class.getDeclaredField("ba");
        System.out.println(f.getType().getComponentType());
    }
}
