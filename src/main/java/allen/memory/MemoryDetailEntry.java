package allen.memory;

import java.lang.reflect.Field;
import java.util.IdentityHashMap;

public class MemoryDetailEntry {

    //rootÎªnull¡£
    MemoryDetailEntry                          parent;

    //rootÎªnull¡£
    Field                                      parentField;

    Object                                     object;

    long                                       shallowSize;

    IdentityHashMap<MemoryDetailEntry, Object> children = new IdentityHashMap<MemoryDetailEntry, Object>();

    public MemoryDetailEntry(Object obj) {
        this.object = obj;
    }

    public void addChildren(MemoryDetailEntry entry) {
        children.put(entry, null);
    }

    public void removeChildren(MemoryDetailEntry entry) {
        children.remove(entry);
    }

    private long getFullSize() {
        long total = 0L;
        total += shallowSize;
        for (MemoryDetailEntry e : children.keySet()) {
            total += e.getFullSize();
        }
        return total;
    }

    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder();
        sb.append("----------------------------------------------------------------\n");
        sb.append(format(0));
        sb.append("----------------------------------------------------------------\n");
        return sb.toString();
    }

    private String format(final int indent) {

        StringBuilder sb = new StringBuilder();

        int temIndent = indent;
        while (temIndent-- > 0) {
            sb.append("  ");
        }

        sb.append(parentField + " " + object.getClass().getCanonicalName()
                + " shallow=" + shallowSize + " full=" + this.getFullSize()
                + "\n");
        for (MemoryDetailEntry c : children.keySet()) {

            sb.append(c.format(indent + 1));
        }

        return sb.toString();
    }
}
