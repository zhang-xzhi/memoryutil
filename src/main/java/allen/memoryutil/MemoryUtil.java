package allen.memoryutil;

import java.lang.instrument.Instrumentation;

import java.lang.reflect.Array;
import java.lang.reflect.Field;
import java.lang.reflect.Modifier;
import java.util.IdentityHashMap;
import java.util.Stack;

public class MemoryUtil {

    public static long memoryUsageOf(Object obj) {
        return Agent.getInstrumentation().getObjectSize(obj);
    }

    public static long deepMemoryUsageOf(Object obj) {
        MemoryDetailEntry root = new MemoryDetailEntry(obj);
        return deepMemoryUsageOf0(root, Agent.getInstrumentation());
    }

    public static MemoryDetailEntry deepMemoryDetail(Object obj) {
        MemoryDetailEntry root = new MemoryDetailEntry(obj);
        deepMemoryUsageOf0(root, Agent.getInstrumentation());
        return root;
    }

    private static long deepMemoryUsageOf0(MemoryDetailEntry root,
            Instrumentation instrumentation) throws SecurityException {

        IdentityHashMap<Object, Object> visitedMap = new IdentityHashMap<Object, Object>();
        Stack<MemoryDetailEntry> stack = new Stack<MemoryDetailEntry>();
        stack.push(root);

        long total = 0L;

        while (!stack.isEmpty()) {
            MemoryDetailEntry detailEntry = stack.pop();

            if (visitedMap.containsKey(detailEntry.object)) {
                detailEntry.duplicate = true;
                continue;
            }

            visitedMap.put(detailEntry.object, null);

            long sz = instrumentation.getObjectSize(detailEntry.object);
            detailEntry.shallowSize = sz;
            total += sz;

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

        return total;
    }

}