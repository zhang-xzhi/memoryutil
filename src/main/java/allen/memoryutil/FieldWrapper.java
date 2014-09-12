package allen.memoryutil;

import java.lang.reflect.Field;

public class FieldWrapper implements Comparable<FieldWrapper> {
    Field field;
    long  offset;

    public FieldWrapper(Field field) {
        super();
        this.field = field;
        this.offset = Agent.objectFieldOffset(this.field);
    }

    @Override
    public int compareTo(FieldWrapper o) {
        long otherOffset = o.offset;
        if (offset < otherOffset) {
            return -1;
        }
        if (offset > otherOffset) {
            return 1;
        }
        return 0;
    }
}
