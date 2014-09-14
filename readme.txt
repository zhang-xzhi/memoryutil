MemoryUtil

以下功能对于32bits JVM，64bits JVM开启压缩指针，64bits JVM关闭压缩指针都适用。

MemoryUtil
可以获得对象的shallowsize。
可以获得对象的fullsize。
可以获得对象的padding size。
可以获得对象的full padding size。

MemoryDetailEntry的toString方法。
可以打印对象的详情信息。
包括对象头，各个field的offset和大小。
padding大小。
引用的对象的详情信息。

allen.memoryutil.size
可以动态检查对象头大小。（普通对象和数组对象）
可以动态检查引用大小。
可以获得基本类型大小。

如何使用
0 查看memoryutil默认输入，在jar目录下输入 java -javaagent:memoryutil-0.1.jar -cp . allen.memoryutil.dirver.TestMemory。
1 加memoryutil-0.1.jar到工程的class path。
2 建立自己的入口类，假设为test.Allen，调用memoryutil的工具类打印测试对象的信息。
3 在命令行执行 java -javaagent:memoryutil-0.1.jar -cp . test.Allen

关于java对象内存的基本知识，请参考
http://zhang-xzhi-xjtu.iteye.com/blog/2116304

"abcd"字符串的详情例子

string abcd   //对象描述，用户输入
----------------------------------------------------------------
object class info : java.lang.String                             //对象类型
object identityHashCode : 4875744                                //对象identityHashCode
in parent info : root object
shallow size = 24                                                //对象shallow size
full size = 48                                                   //对象full size
full padding size = 4                                            //对象full padding size
-----------shallow size detail.-----------------                 //对象shallow size的详情信息
headerType = NormalHeader size = 8                               //普通对象头，大小为8
offset : 8 size = 4 private final char[] java.lang.String.value  //value field的offset为8,大小为4。
offset : 12 size = 4 private final int java.lang.String.offset   //offset field的offset为12,大小为4。
offset : 16 size = 4 private final int java.lang.String.count    //count field的offset为16,大小为4。
offset : 20 size = 4 private int java.lang.String.hash           //hash field的offset为20,大小为4。
padding size = 0                                                 //padding为0。
-----------end of shallow size detail.----------
                                                                 //该字符串引用的char[]详情信息
    object class info : char[]                                   
    object identityHashCode : 15672056                            
    in parent info : private final char[] java.lang.String.value in parent.  //对象在被引用对象中的位置信息。
    shallow size = 24
    full size = 24
    full padding size = 4
    -----------shallow size detail.-----------------
    headerType = ArrayHeader size = 12                           //数组对象头，大小为12
    compType = char arrayLength = 4 size = ( 2 * 4 ) = 8         //数组的类型为char，长度为4，大小为8。
    padding size = 4                                             //padding为4。
    -----------end of shallow size detail.----------
----------------------------------------------------------------


