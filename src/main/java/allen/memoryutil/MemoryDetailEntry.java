package allen.memoryutil;

import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.Collections;
import java.util.IdentityHashMap;
import java.util.List;

import allen.memoryutil.size.ObjectHeaderSize;
import allen.memoryutil.size.PrimitiveSize;
import allen.memoryutil.size.ReferenceSize;

public class MemoryDetailEntry {

    Object                                     object;

    //root为null。
    MemoryDetailEntry                          parent;

    long                                       shallowSize;

    boolean                                    duplicate          = false;

    IdentityHashMap<MemoryDetailEntry, Object> children           = new IdentityHashMap<MemoryDetailEntry, Object>();

    //普通对象独有数据。
    List<FieldWrapper>                         fieldWrapperList   = new ArrayList<FieldWrapper>();

    //start of 数组特有数据。
    Class<?>                                   compType;
    int                                        arrayLength        = -1;
    //end of 数组特有数据。

    //--------------------------parent相关属性。

    //root为null。普通对象引用出来的object在parent的field。
    Field                                      fieldOfParent;
    //root为null。数组对象引用出来的object在parent数组的index。
    int                                        arrayIndexInParent = -1;

    //--------------------------end of parent相关属性。

    public MemoryDetailEntry(Object obj) {
        this.object = obj;
    }

    public void addField(Field field) {
        fieldWrapperList.add(new FieldWrapper(field));
    }

    public void addChildren(MemoryDetailEntry entry) {
        children.put(entry, null);
    }

    private boolean isArray() {
        return object.getClass().isArray();
    }

    private boolean isParentArray() {
        if (parent == null) {
            return false;
        }
        return parent.isArray();
    }

    private boolean isRoot() {
        return parent == null;
    }

    private long getFullSize() {
        long total = 0L;
        total += shallowSize;
        for (MemoryDetailEntry e : children.keySet()) {
            if (!duplicate) {
                total += e.getFullSize();
            }
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
        String temStr = "";
        while (temIndent-- > 0) {
            temStr += "    ";
        }

        sb.append(temStr + "object class info : "
                + object.getClass().getCanonicalName() + "\n");

        sb.append(temStr + "object identityHashCode : "
                + System.identityHashCode(object) + "\n");

        sb.append(temStr + "in parent info : ");
        if (!isRoot()) {
            if (isParentArray()) {
                sb.append("#" + arrayIndexInParent + " in parent array.\n");
            } else {
                sb.append(fieldOfParent + " in parent.\n");
            }
        } else {
            sb.append("root object" + "\n");
        }

        if (duplicate) {
            sb.append(temStr + "duplicate somewhere.\n");
            return sb.toString();
        }

        sb.append(temStr + "shallow size = " + shallowSize + "\n");
        sb.append(temStr + "full size = " + getFullSize() + "\n");

        ObjectHeaderType headerType;
        if (isArray()) {
            headerType = ObjectHeaderType.ArrayHeader;
        } else {
            headerType = ObjectHeaderType.NormalHeader;
        }
        sb.append(temStr + "-----------shallow size detail.-----------------\n");

        long realSize = 0L;

        sb.append(temStr + "headerType = " + headerType + " size = "
                + ObjectHeaderSize.getSize(headerType) + "\n");
        realSize += ObjectHeaderSize.getSize(headerType);

        if (isArray()) {
            long elementSize = 0L;
            if (compType.isPrimitive()) {
                elementSize = PrimitiveSize.getSize(compType);
            } else {
                elementSize = ReferenceSize.getReferenceSize();
            }

            sb.append(temStr + "compType = " + compType + " arrayLength = "
                    + arrayLength + " size = ( " + elementSize + " * "
                    + arrayLength + " ) = " + (elementSize * arrayLength)
                    + "\n");
            realSize += (elementSize * arrayLength);
        }

        Collections.sort(fieldWrapperList);
        for (FieldWrapper fieldWrapper : fieldWrapperList) {
            Field fld = fieldWrapper.field;
            sb.append(temStr + "offset : " + fieldWrapper.offset + " ");
            if (fld.getType().isPrimitive()) {
                sb.append("size = " + PrimitiveSize.getSize(fld.getType())
                        + " " + fld + "\n");
                realSize += PrimitiveSize.getSize(fld.getType());
            } else {
                sb.append("size = " + ReferenceSize.getReferenceSize() + " "
                        + fld + "\n");
                realSize += ReferenceSize.getReferenceSize();
            }
        }

        sb.append(temStr + "padding size = " + (shallowSize - realSize) + "\n");

        sb.append(temStr + "-----------end of shallow size detail.----------\n");

        for (MemoryDetailEntry c : children.keySet()) {
            sb.append("\n");
            sb.append(c.format(indent + 1));
        }

        return sb.toString();
    }
}
