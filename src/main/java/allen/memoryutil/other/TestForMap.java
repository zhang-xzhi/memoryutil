package allen.memoryutil.other;

import java.util.HashMap;
import java.util.Map;
import java.util.Random;

import allen.memoryutil.dirver.TestMemory;

public class TestForMap {

    public static void main(String[] args) throws Exception {
        allenTest();
    }

    private static void allenTest() {
        TestMemory.printObjectInShort("1200, 10000",
                putKeyAndValue(new HashMap<Object, Object>(), 12, 10000));
        TestMemory.printObjectInShort("1200, 20000",
                putKeyAndValue(new HashMap<Object, Object>(), 12, 20000));
        TestMemory.printObjectInShort("1600, 10000",
                putKeyAndValue(new HashMap<Object, Object>(), 16, 10000));
        TestMemory.printObjectInShort("1600, 20000",
                putKeyAndValue(new HashMap<Object, Object>(), 16, 20000));

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
