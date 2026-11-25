@echo off
echo ==============================================================
echo Compiling Smart Highway Toll Plaza Application (32 Classes)...
echo ==============================================================

if not exist bin mkdir bin

set JAVAC_CMD=javac
if exist "C:\Program Files\Java\jdk-26.0.1\bin\javac.exe" (
    set "JAVAC_CMD=C:\Program Files\Java\jdk-26.0.1\bin\javac.exe"
)

"%JAVAC_CMD%" -d bin src/tollplaza/member1/*.java src/tollplaza/member2/*.java src/tollplaza/member3/*.java src/tollplaza/member4/*.java src/tollplaza/jdbc/*.java

if %ERRORLEVEL% EQU 0 (
    echo [SUCCESS] Compilation completed successfully! Class files generated in bin/
) else (
    echo [ERROR] Compilation failed. Please verify JDK is installed.
)
