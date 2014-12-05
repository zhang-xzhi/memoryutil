call mvn package

call java -version
call java -javaagent:target\memoryutil-0.1.jar -cp . allen.memoryutil.other.TestForMap

call D:\jdk1.6.0_33\bin\java -version
call D:\jdk1.6.0_33\bin\java -javaagent:target\memoryutil-0.1.jar -cp . allen.memoryutil.other.TestForMap

call D:\jdk1.6.0_33\bin\java -version
call D:\jdk1.6.0_33\bin\java -XX:-UseCompressedOops -javaagent:target\memoryutil-0.1.jar -cp . allen.memoryutil.other.TestForMap


call D:\jdk1.6.0_33\bin\java -version
call D:\jdk1.6.0_33\bin\java -XX:+UseCompressedOops -javaagent:target\memoryutil-0.1.jar -cp . allen.memoryutil.other.TestForMap


call D:\jdk1.6.0_33\bin\java -version
call D:\jdk1.6.0_33\bin\java -XX:+UseCompressedStrings -XX:+UseCompressedOops -javaagent:target\memoryutil-0.1.jar -cp . allen.memoryutil.other.TestForMap


pause
