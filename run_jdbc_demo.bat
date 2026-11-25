@echo off
echo ==============================================================
echo Starting Toll Plaza JDBC / MySQL Demonstration...
echo ==============================================================

set JAVA_CMD=java
if exist "C:\Program Files\Java\jdk-26.0.1\bin\java.exe" (
    set "JAVA_CMD=C:\Program Files\Java\jdk-26.0.1\bin\java.exe"
)

"%JAVA_CMD%" -cp "bin;lib/*" tollplaza.jdbc.TollPlazaJDBCDemo
pause
