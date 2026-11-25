@echo off
echo ==============================================================
echo Starting Smart Highway Toll Plaza Application...
echo ==============================================================

set JAVA_CMD=java
if exist "C:\Program Files\Java\jdk-26.0.1\bin\java.exe" (
    set "JAVA_CMD=C:\Program Files\Java\jdk-26.0.1\bin\java.exe"
)

if not exist bin\tollplaza\member4\MainApp.class (
    echo Class files not found. Compiling first...
    call compile.bat
)

"%JAVA_CMD%" -cp bin tollplaza.member4.MainApp
