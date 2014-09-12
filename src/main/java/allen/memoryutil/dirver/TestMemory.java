package allen.memoryutil.dirver;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.LinkedList;
import java.util.TreeMap;

import allen.memoryutil.MemoryUtil;
import allen.memoryutil.ObjectHeaderType;
import allen.memoryutil.size.ObjectHeaderSize;
import allen.memoryutil.size.PrimitiveSize;
import allen.memoryutil.size.ReferenceSize;

public class TestMemory {

    private static void printBasic() {
        System.out.println();
        for (ObjectHeaderType type : ObjectHeaderType.values()) {
            System.out.println(type + " size = "
                    + ObjectHeaderSize.getSize(type));
        }
        System.out.println("ReferenceSize size = "
                + ReferenceSize.getReferenceSize());

        System.out.println();
        for (Class<?> c : PrimitiveSize.primitives.keySet()) {
            System.out.println(c + " size = " + PrimitiveSize.getSize(c));
        }
        System.out.println();

    }

    private static void printWrapperBasic() {
        System.out.println();
        printObject("Byte", new Byte((byte) 0));
        printObject("Boolean", Boolean.TRUE);
        printObject("Integer", new Integer(0));
        printObject("Long", new Long(0));
        printObject("Float", new Float(0));
        printObject("Double", new Double(0));
        System.out.println();
    }

    private static void printStringAndRelated() {
        printObject("empty string", "");
        printObject("string abcd", "abcd");
        {
            StringBuilder sb = new StringBuilder();
            printObject("empty StringBuilder", sb);
        }
        {
            StringBuilder sb = new StringBuilder();
            sb.append("abcd");
            printObject("StringBuilder append abcd", sb);
        }
        {
            StringBuilder sb = new StringBuilder();
            sb.trimToSize();
            printObject("empty StringBuilder with trimToSize", sb);
        }
    }

    private static void printSimpleObject() {
        printObjectDetail("empty object", new Object());

        printObjectDetail("ClassWithEightBooleanField",
                new ClassWithEightBooleanField());

        printObjectDetail("ClassWithEightByteField",
                new ClassWithEightByteField());

        printObjectDetail("ClassWithOneBooleanField",
                new ClassWithOneBooleanField());

        printObjectDetail("ClassWithManyFields", new ClassWithManyFields());

    }

    private static void printArrayAndRelated() {
        printObjectDetail("4 length byte array", new byte[4]);
        printObjectDetail("5 length byte array", new byte[5]);

        printObjectDetail("0 length char array", new char[0]);
        printObjectDetail("11 length char array", new char[11]);

        printObjectDetail("0 length object array", new Object[] {});
        printObjectDetail("2 length object array", new Object[] { new Object(),
                new Object() });

        printObjectDetail("Multidimensional object array", new Object[][] {
                new Object[] { new Object() },
                new Object[] { new Object(), new Object() } });
    }

    private static void printCollections() {
        printObjectDetail("empty ArrayList", new ArrayList());
        printObjectDetail("empty LinkedList", new LinkedList());
        printObjectDetail("empty HashMap", new HashMap());
        printObjectDetail("empty TreeMap", new TreeMap());
        printObjectDetail("empty HashSet", new HashSet());
    }

    private static void printEnumAndRelated() {
        printObjectDetail("EnumA.One", EnumA.One);
    }

    public static void main(String[] args) throws Exception {

        //        printBasic();

        printWrapperBasic();
        //
        //        printStringAndRelated();
        //
        //        printSimpleObject();
        //
        //        printArrayAndRelated();
        //
        //        printCollections();
        //
        //        printEnumAndRelated();
        System.out.println();
        System.out.println();
        System.out.println();
    }

    public static void printObjectDetail(String desc, Object obj) {
        System.out.println();
        System.out.println(desc);
        System.out.println(MemoryUtil.deepMemoryDetail(obj));
        System.out.println();
    }

    public static void printObject(String desc, Object obj) {
        System.out.println("obj=" + desc + " shallow size="
                + MemoryUtil.memoryUsageOf(obj) + " full size="
                + MemoryUtil.deepMemoryUsageOf(obj));
    }

}
