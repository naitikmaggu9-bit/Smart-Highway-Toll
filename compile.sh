#!/bin/bash
echo "=============================================================="
echo "Compiling Smart Highway Toll Plaza Application (32 Classes)..."
echo "=============================================================="

mkdir -p bin
javac -d bin src/tollplaza/member1/*.java src/tollplaza/member2/*.java src/tollplaza/member3/*.java src/tollplaza/member4/*.java src/tollplaza/jdbc/*.java

if [ $? -eq 0 ]; then
    echo "[SUCCESS] Compilation completed successfully! Class files generated in bin/"
else
    echo "[ERROR] Compilation failed. Please verify JDK is installed."
fi
