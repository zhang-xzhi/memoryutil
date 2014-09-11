package allen.memory;

import java.lang.instrument.Instrumentation;

import java.lang.reflect.Field;
import java.lang.reflect.Modifier;
import java.util.Collection;
import java.util.IdentityHashMap;
import java.util.Stack;

public class MemoryUtil2 {

    public static long memoryUsageOf(Object obj) {
        return Agent.getInstrumentation().getObjectSize(obj);
    }

    public static long deepMemoryUsageOf(Object obj) {
        return deepMemoryUsageOf(obj, VisibilityFilter.ALL);
    }

    public static long deepMemoryUsageOf(Object obj, VisibilityFilter filter) {
        return deepMemoryUsageOf0(Agent.getInstrumentation(),
                new IdentityHashMap<Object, Object>(), obj, filter);
    }

    public static long deepMemoryUsageOfAll(Collection<? extends Object> objs) {
        return deepMemoryUsageOfAll(objs, VisibilityFilter.ALL);
    }

    public static long deepMemoryUsageOfAll(Collection<? extends Object> objs,
            VisibilityFilter filter) {

        long total = 0L;

        IdentityHashMap<Object, Object> visitedMap = new IdentityHashMap<Object, Object>();

        for (Object obj : objs) {
            total += deepMemoryUsageOf0(Agent.getInstrumentation(), visitedMap,
                    obj, filter);
        }

        return total;
    }

    private static long deepMemoryUsageOf0(Instrumentation instrumentation,
            IdentityHashMap<Object, Object> visitedMap, Object obj,
            VisibilityFilter filter) throws SecurityException {

        Stack<Object> stack = new Stack<Object>();
        stack.push(obj);

        long total = 0L;

        while (!stack.isEmpty()) {

            Object o = stack.pop();

            if (visitedMap.containsKey(o)) {
                continue;
            }

            visitedMap.put(o, null);

            long sz = instrumentation.getObjectSize(o);
            total += sz;

            Class<?> clz = o.getClass();
            Class<?> compType = clz.getComponentType();

            if ((compType != null) && (!compType.isPrimitive())) {
                Object[] arr = (Object[]) o;

                for (int i = 0; i < arr.length; i++) {
                    Object objInArray = arr[i];
                    if (objInArray != null) {

                        stack.push(objInArray);

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
                                Object subObj = fld.get(o);
                                if (subObj != null) {
                                    stack.push(subObj);
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