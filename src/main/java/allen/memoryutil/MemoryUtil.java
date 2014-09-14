package allen.memoryutil;

import java.lang.instrument.Instrumentation;

import java.lang.reflect.Array;
import java.lang.reflect.Field;
import java.lang.reflect.Modifier;
import java.util.IdentityHashMap;
import java.util.Stack;

public class MemoryUtil {

    public static long getShallowSize(Object obj) {
        return deepMemoryDetail(obj, Agent.getInstrumentation())
                .getShallowSize();
    }

    public static long getFullSize(Object obj) {
        return deepMemoryDetail(obj, Agent.getInstrumentation()).getFullSize();
    }

    public static long getPaddingSize(Object obj) {
        return deepMemoryDetail(obj, Agent.getInstrumentation())
                .getPaddingSize();
    }

    public static long getFullPaddingSize(Object obj) {
        return deepMemoryDetail(obj, Agent.getInstrumentation())
                .getFullPaddingSize();
    }

    public static MemoryDetailEntry deepMemoryDetail(Object obj) {
        return deepMemoryDetail(obj, Agent.getInstrumentation());
    }

    private static MemoryDetailEntry deepMemoryDetail(Object obj,
            Instrumentation instrumentation) throws SecurityException {

        MemoryDetailEntry root = new MemoryDetailEntry(obj);
        IdentityHashMap<Object, Object> visitedMap = new IdentityHashMap<Object, Object>();
        Stack<MemoryDetailEntry> stack = new Stack<MemoryDetailEntry>();
        stack.push(root);

        while (!stack.isEmpty()) {
            MemoryDetailEntry detailEntry = stack.pop();

            if (visitedMap.containsKey(detailEntry.object)) {
                detailEntry.duplicate = true;
                continue;
            }

            visitedMap.put(detailEntry.object, null);

            long sz = instrumentation.getObjectSize(detailEntry.object);
            detailEntry.shallowSize = sz;

            Class<?> clz = detailEntry.object.getClass();
            Class<?> compType = clz.getComponentType();

            if (compType != null) {

                detailEntry.compType = compType;
                detailEntry.arrayLength = Array.getLength(detailEntry.object);

                if (!compType.isPrimitive()) {

                    Object[] arr = (Object[]) (detailEntry.object);
                    for (int i = 0; i < arr.length; i++) {
                        Object objInArray = arr[i];
                        if (objInArray != null) {
                            MemoryDetailEntry child = new MemoryDetailEntry(
                                    objInArray);
                            child.parent = detailEntry;
                            child.arrayIndexInParent = i;
                            detailEntry.addChildren(child);
                            stack.push(child);
                        }
                    }
                }
            }

            while (clz != null) {

                for (Field fld : clz.getDeclaredFields()) {

                    int mod = fld.getModifiers();

                    if (Modifier.isStatic(mod)) {
                        continue;
                    }

                    Class<?> fieldClass = fld.getType();
                    fld.setAccessible(true);

                    detailEntry.addField(fld);

                    if (!fieldClass.isPrimitive()) {
                        try {
                            Object subObj = fld.get(detailEntry.object);
                            if (subObj != null) {

                                MemoryDetailEntry child = new MemoryDetailEntry(
                                        subObj);
                                child.parent = detailEntry;
                                child.fieldOfParent = fld;
                                detailEntry.addChildren(child);
                                stack.push(child);

                            }
                        } catch (IllegalAccessException illAcc) {
                            throw new InternalError("Couldn't read " + fld);
                        }
                    }

                }

                clz = clz.getSuperclass();
            }
        }

        return root;
    }

}