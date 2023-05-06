#!/bin/bash
MAIN=client.main.Server$1

BINPATH=/test/Assault/dir$1
GENCLASS=/lib/genclass.jar
CLASSPATH=$PWD$BINPATH:$PWD$GENCLASS

java -cp $CLASSPATH $MAIN

echo "Client $1 running"