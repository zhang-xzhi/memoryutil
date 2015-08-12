call mvn package

call java -version
call java -javaagent:target\memoryutil-0.1.jar -cp . allen.memoryutil.dirver.TestMemory


pause
# 以下shell应该配置为64bits jvm。
call D:\jdk1.6.0_24\bin\java -version
call D:\jdk1.6.0_24\bin\java -XX:+UseCompressedOops -javaagent:target\memoryutil-0.1.jar -cp . allen.memoryutil.dirver.TestMemory


call D:\jdk1.6.0_24\bin\java -version
call D:\jdk1.6.0_24\bin\java -XX:-UseCompressedOops -javaagent:target\memoryutil-0.1.jar -cp . allen.memoryutil.dirver.TestMemory


