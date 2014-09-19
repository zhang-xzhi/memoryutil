call mvn package

call java -version
call java -javaagent:target\memoryutil-0.1.jar -cp . allen.memoryutil.dirver.TestMemory

pause