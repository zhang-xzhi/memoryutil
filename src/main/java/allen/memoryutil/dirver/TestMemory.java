package allen.memoryutil.dirver;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.LinkedList;
import java.util.TreeMap;

import allen.memoryutil.MemoryDetailEntry;
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
        printObject("empty object", new Object());

        printObject("ClassWithEightBooleanField",
                new ClassWithEightBooleanField());

        printObject("ClassWithEightByteField", new ClassWithEightByteField());

        printObject("ClassWithOneBooleanField", new ClassWithOneBooleanField());

        printObject("ClassWithManyFields", new ClassWithManyFields());

        printObject("Child", new Child());

        printObject("C_D", new C_D());

    }

    private static void printArrayAndRelated() {
        printObject("4 length byte array", new byte[4]);
        printObject("5 length byte array", new byte[5]);

        printObject("0 length char array", new char[0]);
        printObject("11 length char array", new char[11]);

        printObject("0 length object array", new Object[] {});
        printObject("2 length object array", new Object[] { new Object(),
                new Object() });

        printObject("Multidimensional object array", new Object[][] {
                new Object[] { new Object() },
                new Object[] { new Object(), new Object() } });
    }

    private static void printCollections() {
        printObject("empty ArrayList", new ArrayList());
        printObject("empty LinkedList", new LinkedList());
        printObject("empty HashMap", new HashMap());
        printObject("empty TreeMap", new TreeMap());
        printObject("empty HashSet", new HashSet());

        {
            LinkedList<String> linkedList = new LinkedList<String>();
            linkedList.add("abcd");
            printObject("linkedList with abcd string", linkedList);
        }
    }

    private static void printEnumAndRelated() {
        printObject("EnumA.One", EnumA.One);
    }

    private static boolean detailMode = false;

    public static void main(String[] args) throws Exception {

        printBasic();

        printWrapperBasic();

        printStringAndRelated();

        printSimpleObject();

        printArrayAndRelated();

        printCollections();

        printEnumAndRelated();

        System.out.println();
        System.out.println();
        System.out.println();
    }

    private static void printObject(String desc, Object obj) {
        if (detailMode) {
            printObjectDetail(desc, obj);
        } else {
            printObjectInShort(desc, obj);
        }
    }

    public static void printObjectDetail(String desc, Object obj) {
        System.out.println();
        System.out.println(desc);
        System.out.println(MemoryUtil.deepMemoryDetail(obj));
        System.out.println();
    }

    public static void printObjectInShort(String desc, Object obj) {
        System.out.println();
        MemoryDetailEntry entry = MemoryUtil.deepMemoryDetail(obj);
        System.out.println("obj=" + desc + " shallow size="
                + entry.getShallowSize() + " padding size="
                + entry.getPaddingSize() + " full size=" + entry.getFullSize()
                + " full padding size=" + entry.getFullPaddingSize());
        System.out.println();
    }

}
