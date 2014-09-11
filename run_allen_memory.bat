call mvn package
call java -version
call java -javaagent:target\perftest-0.1.jar -cp . allen.memory.TestMemory
pause
