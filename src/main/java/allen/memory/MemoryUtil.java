package allen.memory;

import java.lang.instrument.Instrumentation;

import java.lang.reflect.Field;
import java.lang.reflect.Modifier;
import java.util.Collection;
import java.util.IdentityHashMap;
import java.util.Stack;

public class MemoryUtil {

    public static long memoryUsageOf(Object obj) {
        return Agent.getInstrumentation().getObjectSize(obj);
    }

    public static long deepMemoryUsageOf(Object obj) {
        return deepMemoryUsageOf(obj, VisibilityFilter.ALL);
    }

    public static long deepMemoryUsageOf(Object obj, VisibilityFilter filter) {
        MemoryDetailEntry root = new MemoryDetailEntry(obj);
        return deepMemoryUsageOf0(Agent.getInstrumentation(), filter, root);
    }

    public static MemoryDetailEntry deepMemoryDetail(Object obj) {
        return deepMemoryDetail(obj, VisibilityFilter.ALL);
    }

    public static MemoryDetailEntry deepMemoryDetail(Object obj,
            VisibilityFilter filter) {
        MemoryDetailEntry root = new MemoryDetailEntry(obj);
        deepMemoryUsageOf0(Agent.getInstrumentation(), filter, root);
        return root;
    }

    private static long deepMemoryUsageOf0(Instrumentation instrumentation,
            VisibilityFilter filter, MemoryDetailEntry root)
            throws SecurityException {

        IdentityHashMap<Object, Object> visitedMap = new IdentityHashMap<Object, Object>();
        Stack<MemoryDetailEntry> stack = new Stack<MemoryDetailEntry>();
        stack.push(root);

        long total = 0L;

        while (!stack.isEmpty()) {
            MemoryDetailEntry memoryDetailEntry = stack.pop();

            if (visitedMap.containsKey(memoryDetailEntry.object)) {
                memoryDetailEntry.parent.removeChildren(memoryDetailEntry);
                continue;
            }

            visitedMap.put(memoryDetailEntry.object, null);

            long sz = instrumentation.getObjectSize(memoryDetailEntry.object);
            memoryDetailEntry.shallowSize = sz;
            total += sz;

            Class<?> clz = memoryDetailEntry.object.getClass();
            Class<?> compType = clz.getComponentType();

            if ((compType != null) && (!compType.isPrimitive())) {
                Object[] arr = (Object[]) (memoryDetailEntry.object);

                for (int i = 0; i < arr.length; i++) {
                    Object objInArray = arr[i];
                    if (objInArray != null) {
                        MemoryDetailEntry child = new MemoryDetailEntry(
                                objInArray);
                        child.parent = memoryDetailEntry;
                        memoryDetailEntry.addChildren(child);
                        stack.push(child);
                    }
                }
            }

            while (clz != null) {

                for (Field fld : clz.getDeclaredFields()) {

                    int mod = fld.getModifiers();

                    if ((!Modifier.isStatic(mod)) && (passFilter(filter, mod))) {

                        Class<?> fieldClass = fld.getType();
                        fld.setAccessible(true);

                        if (!fieldClass.isPrimitive()) {
                            try {
                                Object subObj = fld
                                        .get(memoryDetailEntry.object);
                                if (subObj != null) {

                                    MemoryDetailEntry child = new MemoryDetailEntry(
                                            subObj);
                                    child.parent = memoryDetailEntry;
                                    child.parentField = fld;
                                    memoryDetailEntry.addChildren(child);
                                    stack.push(child);

                                }
                            } catch (IllegalAccessException illAcc) {
                                throw new InternalError("Couldn't read " + fld);
                            }
                        }
                    }
                }

                clz = clz.getSuperclass();
            }
        }

        return total;
    }

    private static boolean passFilter(VisibilityFilter filter, int mod) {

        switch (filter) {
            case ALL:
                return true;
            case PRIVATE_ONLY:
                return Modifier.isPrivate(mod);
            case NON_PUBLIC:
                return !Modifier.isPublic(mod);
        }

        throw new IllegalArgumentException("Illegal filter=" + filter + " mod="
                + Modifier.toString(mod));
    }

    public static enum VisibilityFilter {
        ALL, PRIVATE_ONLY, NON_PUBLIC;
    }

}