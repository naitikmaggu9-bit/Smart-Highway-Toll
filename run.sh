#!/bin/bash
echo "=============================================================="
echo "Starting Smart Highway Toll Plaza Application..."
echo "=============================================================="

if [ ! -f "bin/tollplaza/member4/MainApp.class" ]; then
    echo "Binaries not found. Running compilation first..."
    bash compile.sh
fi

java -cp bin tollplaza.member4.MainApp
