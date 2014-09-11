package allen.memory;

import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.HashSet;
import java.util.LinkedList;
import java.util.Map;
import java.util.Random;
import java.util.TreeMap;

public class TestMemory {

    public static void main(String[] args) throws Exception {

        System.out.println();

        printInfo("empty object", new Object());
        printInfo("ObjectWithOneBoolean", new ObjectWithOneBooleanField());
        printInfo("ObjectWithEightBooleanField",
                new ObjectWithEightBooleanField());
        printInfo("A", new A());

        System.out.println();

        printInfo("4 length byte array", new byte[4]);
        printInfo("5 length byte array", new byte[5]);

        System.out.println();

        printInfo("0 length object array", new Object[] {});
        printInfo("2 length object array", new Object[] { new Object(),
                new Object() });

        printInfo("Multidimensional object array", new Object[][] {
                new Object[] { new Object() },
                new Object[] { new Object(), new Object() } });

        System.out.println();

        printInfo("empty ArrayList", new ArrayList());
        printInfo("empty LinkedList", new LinkedList());

        printInfo("empty HashMap", new HashMap());
        printInfo("empty TreeMap", new TreeMap());

        printInfo("empty HashSet", new HashSet());

        System.out.println();

        printInfo("empty string", "");
        printInfo("abc string", "abc");

        printInfo("empty StringBuilder", new StringBuilder());
        StringBuilder sb = new StringBuilder();
        sb.trimToSize();
        printInfo("empty StringBuilder with trimToSize", sb);

        printInfo("0 length char array", new char[0]);
        printInfo("11 length char array", new char[11]);

        System.out.println();

        printInfo("Byte", new Byte((byte) 0));
        printInfo("Integer", new Integer(0));
        printInfo("Long", new Long(0));
        printInfo("Float", new Float(0));
        printInfo("Double", new Double(0));
        printInfo("Date", new Date());

        printInfo("t1", "2088123412341234");
        printInfo("t1", "208812341234");

        printInfo("", putKeyAndValue(new HashMap<Object, Object>(), 12, 10000));
        printInfo("", putKeyAndValue(new HashMap<Object, Object>(), 12, 20000));
        printInfo("", putKeyAndValue(new HashMap<Object, Object>(), 16, 10000));
        printInfo("", putKeyAndValue(new HashMap<Object, Object>(), 16, 20000));
        //        printInfo("", putKeyAndValue(new HashMap<Object, Object>(), 16, 300000));
        System.out.println();
        System.out.println();
        System.out.println(MemoryUtil.deepMemoryDetail(new ArrayList()));
        System.out.println(MemoryUtil.deepMemoryDetail(new LinkedList()));
        System.out.println(MemoryUtil.deepMemoryDetail(new HashMap()));
        System.out.println(MemoryUtil.deepMemoryDetail(new TreeMap()));
        System.out.println(MemoryUtil.deepMemoryDetail(new HashSet()));
    }

    private static void printInfo(String desc, Object obj) {
        System.out.println("obj=" + desc + " shallow size="
                + MemoryUtil.memoryUsageOf(obj) + " full size="
                + MemoryUtil.deepMemoryUsageOf(obj));
    }

    private static Map<Object, Object> putKeyAndValue(Map<Object, Object> map,
            int keyLength, int count) {
        putKeyAndValue(keyLength, count, map);
        return map;
    }

    private static void putKeyAndValue(int keyLength, int count,
            Map<Object, Object> map) {
        Random r = new Random();
        while (count-- > 0) {
            StringBuilder sb = new StringBuilder();
            sb.append(count);
            while (sb.length() < keyLength) {
                sb.insert(0, "0");
            }
            map.put(sb.toString(), r.nextInt());
        }
    }
}

class ObjectWithOneBooleanField {
    boolean b;
}

class ObjectWithEightBooleanField {
    boolean b0;
    boolean b1;
    boolean b2;
    boolean b3;

    boolean b4;
    boolean b5;
    boolean b6;
    boolean b7;
}

class A {
    boolean val0;
    long    val1;
    int     val2;
}
