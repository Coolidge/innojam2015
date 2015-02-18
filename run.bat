@echo off

echo Run Ramzor
cd Ramzor\java
call mvn install
START java -jar buildmonitor\target\ramzor-1-jar-with-dependencies.jar

echo Run Engine
START ..\..\opencv-engine\Release\EyeLike.exe
pause