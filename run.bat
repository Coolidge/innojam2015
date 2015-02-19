@echo off

echo Run Ramzor
cd Ramzor\java
REM call mvn install
copy buildmonitor\target\ramzor-1-jar-with-dependencies.jar ..\..\opencv-engine\Release\
cd ..\..\opencv-engine\Release\
START java -jar ramzor-1-jar-with-dependencies.jar
echo Run Engine
call EyeLike.exe